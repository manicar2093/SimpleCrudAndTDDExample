package com.testfullstack.backend.repository;

import com.testfullstack.backend.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    private User user;

    @Before
    public void setUp() throws Exception {
        this.user = new User();
        this.user.setName("nameTest");
        this.user.setLast_name("lastnameTest");
        this.user.setUsername("usernameTest");
        this.user.setPassword("fdd324c4643e3dc57b46923a200bb7db8ce1f71c58b96196859e2db2fdeff47c");
    }

    @Test
    public void findbyUsernameWithDataTest() throws Exception {

        User stored_user = entityManager.persistAndFlush(this.user);
        Optional<User> retrieved_user = repository.findByUsername(stored_user.getUsername());

        Assertions.assertTrue(retrieved_user.isPresent());
        Assertions.assertNotNull(stored_user.getCreatedAt());
    }

}