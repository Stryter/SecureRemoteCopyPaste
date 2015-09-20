package srcp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import srcp.domain.ReferenceCodePool;
import srcp.domain.User;
import srcp.exceptions.ReferenceCodeException;
import srcp.repositories.ReferenceCodePoolRepository;
import srcp.repositories.UserRepository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Josh on 9/19/2015.
 */
@Scope(value = "singleton")
@Service(value = "dataInitializerService")
public class DataInitializerService {
    private static final Logger logger = LoggerFactory
            .getLogger(DataInitializerService.class);
    public static final String KEY_HBM_AUTO = "hibernate.hbm2ddl.auto";
    public static final String DEF_GROUP = "ses";
    public static final String DEF_USERNAME = "ses";
    public static final String DEF_PASSWORD = "$up4m4thu91p";
    @PersistenceContext
    protected EntityManager entityManager;
    @Resource
    ReferenceCodePoolRepository referenceCodePoolRepo;
    @Autowired
    private ReferenceCodePoolRepository referenceCodePoolRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReferenceCodeGenerator referenceCodeGenerator;

    @PostConstruct
    protected void init() throws ReferenceCodeException {
        setup();
    }

    /**
     *
     * @return
     */
    public void setup() throws ReferenceCodeException {
        Map<String, Object> props = entityManager.getEntityManagerFactory()
                .getProperties();
        String value = (String)props.get(KEY_HBM_AUTO);
        logger.debug("Setting up the database with basic data...");

        if (Pattern.compile("create", Pattern.CASE_INSENSITIVE).matcher(value).find()) {
            setupUserReferenceCodePool();
            setupDummyUser();
        }
    }

    @Transactional
    private void setupDummyUser() throws ReferenceCodeException {
        logger.debug("Adding a dummy user...");
        User user;

        if (userRepository.findByEmail("jtrisler@outlook.com") == null) {
            user = new User.Builder(referenceCodeGenerator, userRepository).email("jtrisler@outlook.com").phoneNumber("6154972591").build();
        }
    }

    /**
     * Load the ReferenceCodePool for SES.
     */
    @Transactional
    private synchronized void setupUserReferenceCodePool() {
        logger.debug("Allocating the User ReferenceCodePool...");
        ReferenceCodePool pool;

        if (referenceCodePoolRepo.findByPoolName("SRC") == null) {
            pool = new ReferenceCodePool.Builder("SRC", referenceCodePoolRepository).buildAndPersist();
        }
    }
}
