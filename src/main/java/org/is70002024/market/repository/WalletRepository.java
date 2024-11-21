package org.is70002024.market.repository;

import java.util.List;
import java.util.Optional;
import org.is70002024.market.domain.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Wallet entity.
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    default Optional<Wallet> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Wallet> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Wallet> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select wallet from Wallet wallet left join fetch wallet.user", countQuery = "select count(wallet) from Wallet wallet")
    Page<Wallet> findAllWithToOneRelationships(Pageable pageable);

    @Query("select wallet from Wallet wallet left join fetch wallet.user")
    List<Wallet> findAllWithToOneRelationships();

    @Query("select wallet from Wallet wallet left join fetch wallet.user where wallet.id =:id")
    Optional<Wallet> findOneWithToOneRelationships(@Param("id") Long id);
}
