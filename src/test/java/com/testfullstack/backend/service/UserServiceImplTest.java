package com.testfullstack.backend.service;

import com.testfullstack.backend.entity.User;
import com.testfullstack.backend.exception.UserAlreadyExists;
import com.testfullstack.backend.exception.UserNotExists;
import com.testfullstack.backend.repository.UserRepository;
import com.testfullstack.backend.util.Encriptors;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private Encriptors encriptors;

    private UserServiceImpl userServiceImpl;

    private User user;

    @Before
    public void setUp() throws Exception {
        this.userServiceImpl = new UserServiceImpl(userRepository, encriptors);
        this.user = new User();
        this.user.setName("nametest");
        this.user.setLast_name("lastnametest");
        this.user.setUsername("usernametest");
        this.user.setPassword("passwordtest");
    }

    /**
     *
     * Test a successfull registry of an {@link User}
     *
     * @throws Exception
     */
    @Test
    public void registryUserSuccessfull() throws Exception {

        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());
        User user_expected = new User();
        user_expected.setId(1L);
        user_expected.setName("nametest");
        user_expected.setLast_name("lastnametest");
        user_expected.setUsername("usernametest");
        user_expected.setPassword("a7574a42198b7d7eee2c037703a0b95558f195457908d6975e681e2055fd5eb9");

        given(userRepository.save(any())).willReturn(user_expected);
        given(encriptors.hashStringToSha256(anyString())).willReturn("a7574a42198b7d7eee2c037703a0b95558f195457908d6975e681e2055fd5eb9");

        User user_saved = userServiceImpl.registryUser(this.user);

        Assertions.assertNotNull(user_saved);
        Assertions.assertEquals(
                "a7574a42198b7d7eee2c037703a0b95558f195457908d6975e681e2055fd5eb9",
                user_saved.getPassword());
        Assertions.assertEquals(this.user.getName(), user_saved.getName());
        Assertions.assertEquals(this.user.getLast_name(), user_saved.getLast_name());
        Assertions.assertEquals(this.user.getUsername(), user_saved.getUsername());
    }

    /**
     *
     * Test when a user already exists throws an UserAlreadyExists exception
     *
     * @throws Exception
     */
    @Test(expected = UserAlreadyExists.class)
    public void registryUserExists() throws Exception {

        User user_expected = new User();
        user_expected.setId(1L);
        user_expected.setName("nametest");
        user_expected.setLast_name("lastnametest");
        user_expected.setUsername("usernametest");
        user_expected.setPassword("a7574a42198b7d7eee2c037703a0b95558f195457908d6975e681e2055fd5eb9");

        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user_expected));

        User user_saved = userServiceImpl.registryUser(this.user);

    }

    /**
     *
     * Test for a successfully user information retrieving
     *
     * @throws Exception
     */
    @Test
    public void getUserInformationSuccessfullyTest() throws Exception {

        User user_expected = new User();
        user_expected.setId(1L);
        user_expected.setName("nametest");
        user_expected.setLast_name("lastnametest");
        user_expected.setUsername("usernametest");
        user_expected.setPassword("a7574a42198b7d7eee2c037703a0b95558f195457908d6975e681e2055fd5eb9");

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user_expected));

        User user_retrived = userServiceImpl.getUserInformation(1L);
        Assertions.assertNotNull(user_retrived);
        Assertions.assertNull(user_retrived.getPassword());

    }

    /**
     *
     * Test when a user does not exist throws a UserNotExists exception
     *
     * @throws Exception
     */
    @Test(expected = UserNotExists.class)
    public void getUserInformationThatDoesNotExistsTest() throws Exception {

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());
        User user_retrived = userServiceImpl.getUserInformation(1L);

    }

    /**
     *
     * Test that a user can be modify correctly
     *
     */
    @Test
    public void modifyUserSuccessfull(){
        User user_test = new User();
        user_test.setId(1L);
        user_test.setName("nametest");
        user_test.setLast_name("lastnametest");
        user_test.setUsername("usernametest");
        user_test.setPassword("apassword");

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user_test));

        User user_expected = new User();
        user_expected.setId(1L);
        user_expected.setName("nametestmodified");
        user_expected.setLast_name("lastnametestmodified");
        user_expected.setUsername("usernametest");
        user_expected.setPassword("apassword");

        given(userRepository.save(any())).willReturn(user_expected);

        User user_retrieved = userServiceImpl.modifyUser(user_test.getId(), user_test);

        Assertions.assertNotNull(user_retrieved);
        Assertions.assertEquals(user_expected.getName(),user_retrieved.getName());
        Assertions.assertEquals(user_expected.getLast_name(),user_retrieved.getLast_name());
        Assertions.assertNull(user_retrieved.getPassword());
    }

    @Test(expected = UserNotExists.class)
    public void modifyUserDoesNotExist() throws Exception {

        User user_test = new User();
        user_test.setId(1L);
        user_test.setName("nametest");
        user_test.setLast_name("lastnametest");
        user_test.setUsername("usernametest");

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        User user_expected = new User();
        user_test.setId(1L);
        user_test.setName("nametestmodified");
        user_test.setLast_name("lastnametestmodified");
        user_test.setUsername("usernametest");

        User user_retrieved = userServiceImpl.modifyUser(user_test.getId(), user_test);

    }

    @Test
    public void deleteUserSuccessfullyTest() throws Exception {
        given(userRepository.findById(anyLong())).willReturn(Optional.of(new User()));
        boolean response =  userServiceImpl.deleteUser(1);
        Assertions.assertTrue(response);
    }

    @Test
    public void deleteUserUnsuccessfullyTest() throws Exception {
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());
        boolean response =  userServiceImpl.deleteUser(1);
        Assertions.assertFalse(response);
    }

    @Test
    public void getAllUsersSuccessfully() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());

        given(userRepository.findAll()).willReturn(users);

        List<User> users_retieved = userServiceImpl.getAllUsers();

        Assertions.assertNotNull(users_retieved);
        Assertions.assertEquals(users_retieved.size(), 3);
    }

    @Test
    public void getAllUsersEmptySuccessfully() throws Exception {
        List<User> users = new ArrayList<>();

        given(userRepository.findAll()).willReturn(users);

        List<User> users_retieved = userServiceImpl.getAllUsers();

        Assertions.assertNotNull(users_retieved);
        Assertions.assertEquals(users_retieved.size(), 0);
        Assertions.assertTrue(users_retieved.isEmpty());
    }

}