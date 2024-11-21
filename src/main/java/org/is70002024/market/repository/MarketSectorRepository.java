package org.is70002024.market.repository;

import org.is70002024.market.domain.MarketSector;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MarketSector entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarketSectorRepository extends JpaRepository<MarketSector, Long> {}
