package srcp.repositories;

import org.springframework.data.repository.CrudRepository;
import srcp.domain.User;

/**
 * Created by Josh on 9/18/2015.
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
