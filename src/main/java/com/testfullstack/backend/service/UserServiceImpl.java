package com.testfullstack.backend.service;

import com.testfullstack.backend.entity.User;
import com.testfullstack.backend.exception.UserAlreadyExists;
import com.testfullstack.backend.exception.UserNotExists;
import com.testfullstack.backend.repository.UserRepository;
import com.testfullstack.backend.util.Encriptors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Encriptors encriptors;

    public UserServiceImpl(UserRepository userRepository, Encriptors encriptors) {
        this.userRepository = userRepository;
        this.encriptors = encriptors;
    }

    @Override
    public User registryUser(User user) {

        Optional<User> user_stored = userRepository.findByUsername(user.getUsername());

        if(user_stored.isPresent()){
            throw new UserAlreadyExists();
        }

        user.setPassword(
                encriptors.hashStringToSha256(user.getPassword())
        );

        return userRepository.save(user);
    }

    @Override
    public User getUserInformation(Long user_id) {

        Optional<User> user_storeged = userRepository.findById(user_id);
        if(!user_storeged.isPresent()){
            LOG.error(String.format("User with id %s does not exist", user_id));
            throw new UserNotExists();
        }
        user_storeged.get().setPassword(null);
        return user_storeged.get();

    }

    @Override
    public User modifyUser(Long user_id, User user) {

        Optional<User> user_storaged = userRepository.findById(user_id);

        if(!user_storaged.isPresent()){
            LOG.error(String.format("User with id %s does not exist", user_id));
            throw new UserNotExists();
        }

        /*
        *   Ignoring password and username
        * */
        user_storaged.get()
                .setName(user.getName());
        user_storaged.get()
                .setLast_name(user.getLast_name());

        User user_saved = userRepository.save(user_storaged.get());
        user_saved.setPassword(null);

        return user_saved;
    }

    @Override
    public boolean deleteUser(long user_id) {

        Optional<User> user_storeged = userRepository.findById(user_id);

        if (!user_storeged.isPresent()){
            return false;
        }

        userRepository.deleteById(user_id);
        return true;
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }
}
