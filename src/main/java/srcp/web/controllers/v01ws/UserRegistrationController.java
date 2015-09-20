package srcp.web.controllers.v01ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import srcp.domain.User;
import srcp.exceptions.UserNotFoundException;
import srcp.services.UserRegistrationService;
import srcp.web.controllers.v01ws.dtos.RegistrationDTO;

import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/v01ws/srcp/registration")
@RestController
public class UserRegistrationController extends SRCPController {

    private UserRegistrationService userRegistrationService;

    @RequestMapping(value = "/{email}/exists", method = GET)
    public RegistrationDTO userExists(@PathVariable String email) {
        User user = Optional.ofNullable(userRegistrationService.findUserByEmail(email)).orElseThrow(() -> new UserNotFoundException(email));

        return new RegistrationDTO(user);
    }

    @Autowired
    public void setUserRegistrationService(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }
}
