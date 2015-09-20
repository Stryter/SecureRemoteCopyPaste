package srcp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import srcp.repositories.UserRepository;
import srcp.services.UserRegistrationService;

@Configuration
public class ServicesConfig {
    @Autowired
    private UserRepository userRepository;

    @Bean
    public UserRegistrationService userRegistrationService() {
        return new UserRegistrationService(userRepository);
    }
}
