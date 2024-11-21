package org.is70002024.market.repository;

import java.util.List;
import java.util.Optional;
import org.is70002024.market.domain.Subscriptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Subscriptions entity.
 */
@Repository
public interface SubscriptionsRepository extends JpaRepository<Subscriptions, Long> {
    @Query("select subscriptions from Subscriptions subscriptions where subscriptions.user.login = ?#{authentication.name}")
    List<Subscriptions> findByUserIsCurrentUser();

    default Optional<Subscriptions> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Subscriptions> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Subscriptions> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select subscriptions from Subscriptions subscriptions left join fetch subscriptions.user left join fetch subscriptions.service",
        countQuery = "select count(subscriptions) from Subscriptions subscriptions"
    )
    Page<Subscriptions> findAllWithToOneRelationships(Pageable pageable);

    @Query("select subscriptions from Subscriptions subscriptions left join fetch subscriptions.user left join fetch subscriptions.service")
    List<Subscriptions> findAllWithToOneRelationships();

    @Query(
        "select subscriptions from Subscriptions subscriptions left join fetch subscriptions.user left join fetch subscriptions.service where subscriptions.id =:id"
    )
    Optional<Subscriptions> findOneWithToOneRelationships(@Param("id") Long id);
}
