package srcp.domain;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.jpa.domain.AbstractAuditable;
import srcp.repositories.UserRepository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Created by Josh on 9/18/2015.
 */
@Entity
public class User extends AbstractAuditable<User, Long> {

    @NotNull
    @Column(unique = true)
    private String userId;

    @Email
    @NotNull
    @Column
    private String email;

    @NotNull
    @Column
    private String phoneNumber;

    @NotNull
    @Column
    private Instant registrationTime;

    @Transient
    private UserRepository userRepository;

    private User() {}

    public User(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Instant getRegistrationTime() {
        return registrationTime;
    }

    public User persist() {
        return userRepository.save(this);
    }

    public static class Builder {
        private User user;

        public Builder() {
            user = new User();
        }

        public Builder email(String email) {
            user.email = email;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            user.phoneNumber = phoneNumber;
            return this;
        }

        public User build() {
            user.registrationTime = Instant.now();
            return user.persist();
        }
    }
}
