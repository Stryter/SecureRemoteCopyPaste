package srcp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import srcp.domain.ReferenceCodePool;

/**
 * Created by Josh on 9/19/2015.
 */
@Repository
public interface ReferenceCodePoolRepository extends JpaRepository<ReferenceCodePool, Long> {
    ReferenceCodePool findByPoolName(String poolName);
}
