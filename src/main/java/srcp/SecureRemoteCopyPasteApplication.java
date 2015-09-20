package srcp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import srcp.repositories.UserRepository;

@EnableLoadTimeWeaving
@EnableJpaRepositories(basePackages = "srcp.repositories")
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = {"srcp"})
public class SecureRemoteCopyPasteApplication {
    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(SecureRemoteCopyPasteApplication.class, args);
    }

    @Bean
    public InstrumentationLoadTimeWeaver loadTimeWeaver()  throws Throwable {
        InstrumentationLoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();
        return loadTimeWeaver;
    }
}
