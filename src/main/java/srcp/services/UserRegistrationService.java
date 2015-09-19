package srcp.services;

import srcp.domain.User;
import srcp.repositories.UserRepository;

/**
 * Created by Josh on 9/19/2015.
 */
public class UserRegistrationService {
    private UserRepository userRepository;

    public UserRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
