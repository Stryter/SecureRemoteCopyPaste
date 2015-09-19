package srcp.web.controllers.v01ws;

import org.springframework.web.bind.annotation.RequestMapping;
import srcp.domain.User;

import java.nio.file.Paths;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/v01ws/srcp/registration")
public class UserRegistrationController extends SRCPController {

    @RequestMapping(value = "/user", method = POST)
    public User registerUser(UserRegistrationDTO registrationDTO) {
        return null;
    }

    public class UserRegistrationDTO {
        private String email;
        private String phoneNumber;
        private String userId;
    }
}
