package srcp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.jpa.domain.AbstractAuditable;
import srcp.exceptions.ReferenceCodeException;
import srcp.repositories.UserRepository;
import srcp.services.ReferenceCodeGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Created by Josh on 9/18/2015.
 */
@JsonIgnoreProperties({"createdBy", "createdDate"})
@Entity
@Table(name = "User")
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

    public User save() {
        return userRepository.save(this);
    }

    public static class Builder {
        private ReferenceCodeGenerator referenceCodeGenerator;
        private User user;

        public Builder(ReferenceCodeGenerator referenceCodeGenerator, UserRepository userRepository) {
            this.referenceCodeGenerator = referenceCodeGenerator;
            user = new User(userRepository);
        }

        public Builder email(String email) {
            user.email = email;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            user.phoneNumber = phoneNumber;
            return this;
        }

        public User build() throws ReferenceCodeException {
            user.userId = referenceCodeGenerator.generateReferenceCode("SRC").getReferenceCode();
            user.registrationTime = Instant.now();
            return user.save();
        }
    }
}
