package srcp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import srcp.domain.ReferenceCodePool;
import srcp.domain.vo.ReferenceCode;
import srcp.exceptions.ReferenceCodeException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by Josh on 9/19/2015.
 */
@Scope(value = "singleton")
@Service(value = "refCodeGenerator")
public class ReferenceCodeGenerator {
    private static final Logger logger = LoggerFactory
            .getLogger(ReferenceCodeGenerator.class);
    private static final int CACHE_SIZE = 15;
    public static final long UNKNOWN = -1;

    @PersistenceContext
    protected EntityManager entityManager;

    /**
     * This hash table uses a ReferencecodePool object as its key. The
     * corresponding value is a vector of reference numbers (not yet base24
     * encoded) that have been allocated but have not yet been used.
     *
     * This is a thread-safe value.
     */
    private volatile Hashtable<String, Vector<Long>> referenceCodeCache;

    @PostConstruct
    protected void init() {
        referenceCodeCache = new Hashtable<>();
    }

    /**
     *
     * @return
     */
    public ReferenceCode generateReferenceCode(String poolName)
            throws ReferenceCodeException {
        poolName = poolName.toUpperCase();
        long referenceNumber = getNextReferenceNumber(poolName);
        ReferenceCode refCode = new ReferenceCode(poolName, referenceNumber);
        logger.debug("generated reference code \"" + refCode + "\"");
        return refCode;
    }

    /**
     *
     * @return
     */
    private long getNextReferenceNumber(String poolName)
            throws ReferenceCodeException {
        long retval = UNKNOWN;
        synchronized (referenceCodeCache) {

            // make sure we have allocated a cache for this pool
            if (!referenceCodeCache.containsKey(poolName)) {
                referenceCodeCache.put(poolName, new Vector<Long>(CACHE_SIZE));
            }

            // if the cache is empty, reload it
            if (referenceCodeCache.get(poolName).size() == 0) {
                reloadReferenceCodeCache(poolName);
            }

            // remove a value from the cache for use
            retval = referenceCodeCache.get(poolName).remove(0);
        }
        logger.debug("retrieved next reference code sequence value for ["
                + poolName + "]: \"" + retval + "\"");
        return (retval);
    }

    /**
     *
     */
    private void reloadReferenceCodeCache(String poolName)
            throws ReferenceCodeException {

        logger.debug("reloading reference code cache [" + poolName + "]...");

        Transaction tx = null;
        Vector<Long> cache = referenceCodeCache.get(poolName);
        long numberToLoad = CACHE_SIZE - cache.size();

        try {
            // start a transaction
            entityManager.joinTransaction();
            tx = (Transaction)entityManager.getTransaction();

            // load the ReferenceCodePool
            String hql = "SELECT s FROM ReferenceCodePool s WHERE s.poolName = :pool";
            Query query = entityManager.createQuery(hql);
            query.setParameter("pool", poolName);
            ReferenceCodePool pool = (ReferenceCodePool) query.getSingleResult();

            // make sure the ReferenceCodePool actually existed
            if (pool == null) {
                throw new ReferenceCodeException(
                        "No Reference Code Pool exists " + "\"" + poolName
                                + "\"");
            }

            // update the next sequence in the pool based on the cache size
            // requested
            long beginBoundary = pool.getFirstSequence();
            long endBoundary = pool.getLastSequence();
            long beginSeq = Math.max(pool.getNextSequence(), beginBoundary);
            if (beginSeq >= endBoundary)
                throw new ReferenceCodeException(
                        "ReferenceCodePool exhausted [" + poolName + "].");
            long newNextSeq = Math
                    .min(endBoundary + 1, beginSeq + numberToLoad);
            pool.setNextSequence(newNextSeq);
            entityManager.persist(pool);
            entityManager.flush();

            // now populate the cache vector
            while (beginSeq < newNextSeq) {
                cache.add(beginSeq++);
            }

        } catch (Exception e1) {
            // roll back our transaction
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (SystemException e2) {
                    logger.error("" + e2, e2);
                }
            }
            if (e1 instanceof ReferenceCodeException) {
                throw (ReferenceCodeException) e1;
            }

            String msg = "failed to fetch sequence for ReferenceCodePool ["
                    + poolName + "]";
            throw new ReferenceCodeException(msg, e1);
        }

        // don't forget to commit the transaction!
        try {
            tx.commit();
        } catch (RollbackException e) {
            e.printStackTrace();
        } catch (HeuristicMixedException e) {
            e.printStackTrace();
        } catch (HeuristicRollbackException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        }

    }// end reloadReferenceCodeCache
}
