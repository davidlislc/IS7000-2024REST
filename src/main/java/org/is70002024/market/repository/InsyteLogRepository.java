package org.is70002024.market.repository;

import java.util.List;
import java.util.Optional;
import org.is70002024.market.domain.InsyteLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsyteLog entity.
 */
@Repository
public interface InsyteLogRepository extends JpaRepository<InsyteLog, Long> {
    @Query("select insyteLog from InsyteLog insyteLog where insyteLog.user.login = ?#{authentication.name}")
    List<InsyteLog> findByUserIsCurrentUser();

    default Optional<InsyteLog> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<InsyteLog> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<InsyteLog> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select insyteLog from InsyteLog insyteLog left join fetch insyteLog.user",
        countQuery = "select count(insyteLog) from InsyteLog insyteLog"
    )
    Page<InsyteLog> findAllWithToOneRelationships(Pageable pageable);

    @Query("select insyteLog from InsyteLog insyteLog left join fetch insyteLog.user")
    List<InsyteLog> findAllWithToOneRelationships();

    @Query("select insyteLog from InsyteLog insyteLog left join fetch insyteLog.user where insyteLog.id =:id")
    Optional<InsyteLog> findOneWithToOneRelationships(@Param("id") Long id);
}
