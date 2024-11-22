package org.is70002024.market.repository;

import java.util.List;
import java.util.Optional;
import org.is70002024.market.domain.Giftcard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Giftcard entity.
 */
@Repository
public interface GiftcardRepository extends JpaRepository<Giftcard, Long> {
    @Query("select giftcard from Giftcard giftcard where giftcard.user.login = ?#{authentication.name}")
    List<Giftcard> findByUserIsCurrentUser();

    default Optional<Giftcard> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Giftcard> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Giftcard> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select giftcard from Giftcard giftcard left join fetch giftcard.user",
        countQuery = "select count(giftcard) from Giftcard giftcard"
    )
    Page<Giftcard> findAllWithToOneRelationships(Pageable pageable);

    @Query("select giftcard from Giftcard giftcard left join fetch giftcard.user")
    List<Giftcard> findAllWithToOneRelationships();

    @Query("select giftcard from Giftcard giftcard left join fetch giftcard.user where giftcard.id =:id")
    Optional<Giftcard> findOneWithToOneRelationships(@Param("id") Long id);
}
