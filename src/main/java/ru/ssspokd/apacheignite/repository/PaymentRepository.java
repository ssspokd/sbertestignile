package ru.ssspokd.apacheignite.repository;

import org.apache.ignite.springdata20.repository.IgniteRepository;
import org.apache.ignite.springdata20.repository.config.Query;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;
import org.springframework.data.domain.Pageable;
import ru.ssspokd.apacheignite.model.Payment;

import javax.cache.Cache;
import java.util.List;

@RepositoryConfig(cacheName = "PaymenCache")
public interface PaymentRepository  extends IgniteRepository<Long, Payment>
{
    public List<Payment> findByFirstName(String namePayment);
    /**
     * Returns top Payment with the specified surname.
     * @param name Payment surname.
     * @return Payment that satisfy the query.
     */
    public Cache.Entry<Long, Payment> findTopByLastNameLike(String name);

    /**
     * Getting ids of all the Person satisfying the custom query from {@link Query} annotation.
     *
     * @param orgId Query parameter.
     * @param pageable Pageable interface.
     * @return A list of Persons' ids.
     */
    @Query("SELECT id FROM Payment WHERE orgId > ?")
    public List<Long> selectId(long orgId, Pageable pageable);
}
