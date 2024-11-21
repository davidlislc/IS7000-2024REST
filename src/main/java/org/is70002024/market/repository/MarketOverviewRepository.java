package org.is70002024.market.repository;

import org.is70002024.market.domain.MarketOverview;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MarketOverview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarketOverviewRepository extends JpaRepository<MarketOverview, Long> {}
