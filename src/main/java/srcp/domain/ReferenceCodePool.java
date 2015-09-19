package srcp.domain;

import org.springframework.data.jpa.domain.AbstractAuditable;
import srcp.repositories.ReferenceCodePoolRepository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class ReferenceCodePool extends AbstractAuditable<User, Long> {
    @Transient
    private ReferenceCodePoolRepository referenceCodePoolRepository;

    @SuppressWarnings("unused")
    private ReferenceCodePool() {
    }

    public ReferenceCodePool(ReferenceCodePoolRepository referenceCodePoolRepository) {
        this.referenceCodePoolRepository = referenceCodePoolRepository;
    }

    @NotNull
    @Column(unique = true, columnDefinition = "CHAR(3)")
    @Pattern(regexp = "^[A-Z]{3,3}$")
    private String poolName;

    @NotNull
    private Long nextSequence = 0L;
    @NotNull
    private Long firstSequence = 0L;
    @NotNull
    private Long lastSequence = 4586471423L;

    protected ReferenceCodePool persist() {
        return referenceCodePoolRepository.save(this);
    }

    public String getPoolName() {
        return poolName;
    }

    public Long getNextSequence() {
        return nextSequence;
    }

    public Long getFirstSequence() {
        return firstSequence;
    }

    public Long getLastSequence() {
        return lastSequence;
    }

    public void setNextSequence(long nextSequence) {
        this.nextSequence = nextSequence;
    }

    public static class Builder {

        private ReferenceCodePool pool;

        public Builder(String poolName) {
            pool = new ReferenceCodePool();
            pool.poolName = poolName;
        }

        public ReferenceCodePool build() {
            return pool.persist();
        }
    }
}
