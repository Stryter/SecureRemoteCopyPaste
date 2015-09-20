package srcp.web.controllers.v01ws.dtos;

import srcp.domain.User;

/**
 * Created by Josh on 9/20/2015.
 */
public class RegistrationDTO {
    private String userId;

    public RegistrationDTO(User user) {
        this.userId = user.getUserId();
    }

    public String getUserId() {
        return userId;
    }
}
