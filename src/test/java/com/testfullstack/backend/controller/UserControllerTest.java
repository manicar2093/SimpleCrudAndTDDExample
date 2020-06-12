package com.testfullstack.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testfullstack.backend.entity.User;
import com.testfullstack.backend.exception.UserAlreadyExists;
import com.testfullstack.backend.exception.UserNotExists;
import com.testfullstack.backend.service.IUserService;
import com.testfullstack.backend.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    private Map<String,Object> request;

    @Before
    public void setUp() throws Exception{

        request = new HashMap<>();
        request.put("name","nametest");
        request.put("last_name","lastnametest");
        request.put("username","usernametest");
        request.put("password","passwordtest");

    }


    @Test
    /**
     * Test a successfull request to save a user
     */
    public void regitryUserEndpointSuccessful() throws Exception {

        User user_expected = new User();
        user_expected.setId(5L);
        user_expected.setName("nametest");
        user_expected.setLast_name("lastnametest");
        user_expected.setUsername("usernametest");
        user_expected.setPassword("passwordtest");

        given(userService.registryUser(any())).willReturn(user_expected);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/registry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(this.request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("message").value("User usernametest has been registrated"))
                .andExpect(jsonPath("id").value(5));
    }

    /**
     *
     * Test an unsuccessfull request to registry a new User. If the {@link UserServiceImpl}
     * If user already exist will be thrown a {@link UserAlreadyExists}
     *
     * @throws Exception
     */
    @Test
    public void registryUserEndpointUserAlreadyExist() throws Exception {
        given(userService.registryUser(any()))
                .willThrow(new UserAlreadyExists());

        mockMvc.perform(MockMvcRequestBuilders.post("/user/registry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(this.request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("User usernametest has not been registrated. It already exists."));

    }

    /**
     *
     * Test that retrieve a user details
     *
     * @throws Exception
     */
    @Test
    public void getUserInformationSuccessfully() throws Exception {

        User user_test = new User();
        user_test.setId(2L);
        user_test.setName("nametest");
        user_test.setLast_name("lastnametest");
        user_test.setUsername("usernametest");

        given(userService.getUserInformation(anyLong()))
                .willReturn(user_test);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/details/2")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("user.id").value(user_test.getId()))
            .andExpect(jsonPath("user.name").value(user_test.getName()))
            .andExpect(jsonPath("user.last_name").value(user_test.getLast_name()))
            .andExpect(jsonPath("user.username").value(user_test.getUsername()))
            .andExpect(jsonPath("user.password").doesNotExist());
    }

    /**
     *
     * Test that ensures when a user does not exist, the client will be notify
     *
     * @throws Exception
     */
    @Test()
    public void getUserInformationUnsuccessfully() throws Exception {

        given(userService.getUserInformation(anyLong()))
                .willThrow(UserNotExists.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/details/88")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("User with id 88 does not exist."));
    }

    /**
     *
     * Test that validates a user's updates successfully
     *
     * @throws Exception
     */
    @Test
    public void updateUserSuccessfully() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("name","nametestmodified");
        request.put("last_name","lastnametestmodified");
        request.put("username","usernametestmodified");
        request.put("password","passwordtestmodified");

        User user_test = new User();
        user_test.setId(2L);
        user_test.setName("nametestmodified");
        user_test.setLast_name("lastnametestmodified");
        user_test.setUsername("usernametest");

        given(userService.modifyUser(any(),any())).willReturn(user_test);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/update/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("message").value("User with id 2 has been modified."))
                .andExpect(jsonPath("user.id").value(2))
                .andExpect(jsonPath("user.name").value(user_test.getName()))
                .andExpect(jsonPath("user.last_name").value(user_test.getLast_name()));
    }

    @Test
    public void updateUserDoesNotExists() throws Exception {

        given(userService.modifyUser(any(),any())).willThrow(UserNotExists.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/update/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("User with id 2 does not exist."));
    }

    /**
     * Test to ensures a User will be deleted
     * @throws Exception
     */
    @Test
    public void deletingAnUserSuccessfully() throws Exception {
        given(userService.deleteUser(anyLong())).willReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("User with id 2 has been deleted."));

    }


    /**
     * Test to ensures a User will be deleted
     * @throws Exception
     */
    @Test
    public void deletingAnUserUnsuccessfully() throws Exception {
        given(userService.deleteUser(anyLong())).willReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("User with id 2 has not been deleted. User does not exist."));

    }

    @Test
    public void getAllUsersRegistretedSuccessfully() throws Exception {

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());

        given(userService.getAllUsers()).willReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("users").isArray())
                .andExpect(jsonPath("users").isNotEmpty());
    }

    @Test
    public void getAllUsersRegistretedEmpty() throws Exception {
        given(userService.getAllUsers()).willReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("users").isEmpty());

    }


    /**
     * Transforms any object to an {@link ObjectMapper} to send data through any http request
     * @param obj
     * @return
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
