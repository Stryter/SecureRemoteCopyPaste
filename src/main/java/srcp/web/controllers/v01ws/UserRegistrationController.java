package srcp.web.controllers.v01ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import srcp.domain.User;
import srcp.exceptions.UserNotFoundException;
import srcp.services.UserRegistrationService;

import java.nio.file.Paths;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/v01ws/srcp/registration")
public class UserRegistrationController extends SRCPController {

    private UserRegistrationService userRegistrationService;

    @RequestMapping(value = "/{email}/exists", method = GET)
    public UserRegistrationDTO userExists(@PathVariable String email) {
        User user = Optional.ofNullable(userRegistrationService.findUserByEmail(email)).orElseThrow(() -> new UserNotFoundException(email));

        return new UserRegistrationDTO().userId(user.getUserId());
    }

    @RequestMapping(value = "/user", method = POST)
    public User registerUser(UserRegistrationDTO registrationDTO) {
        return null;
    }

    @Autowired
    public void setUserRegistrationService(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    public class UserRegistrationDTO {
        private String email;
        private String phoneNumber;
        private String userId;

        public UserRegistrationDTO email(String email) {
            this.email = email;
            return this;
        }

        public UserRegistrationDTO phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserRegistrationDTO userId(String userId) {
            this.userId = userId;
            return this;
        }
    }
}
