package com.testfullstack.backend.service;

import com.testfullstack.backend.entity.User;

import java.util.List;

public interface IUserService {

    /**
     *
     * Registry a new {@link User} to database. It generates its password has with SHA256 hash
     *
     * @param user
     * @return User
     * @throws Exception
     */
    User registryUser(User user);

    /**
     *
     * Searchs information of an user by its id.
     *
     * @param user_id User's id which it is registred on database
     * @return
     */
    User getUserInformation(Long user_id);

    /**
     *
     * Update a user using the given id. It ignores the next user's attributes:
     * <ul>
     *     <li>Password</li>
     *     <li>Username</li>
     * </ul>
     *
     * @param user_id
     * @param user
     * @return
     */
    User modifyUser(Long user_id, User user);

    boolean deleteUser(long user_id);

    List<User> getAllUsers();
}
