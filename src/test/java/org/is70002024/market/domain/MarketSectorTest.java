package org.is70002024.market.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.is70002024.market.domain.MarketSectorTestSamples.*;

import org.is70002024.market.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarketSectorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarketSector.class);
        MarketSector marketSector1 = getMarketSectorSample1();
        MarketSector marketSector2 = new MarketSector();
        assertThat(marketSector1).isNotEqualTo(marketSector2);

        marketSector2.setId(marketSector1.getId());
        assertThat(marketSector1).isEqualTo(marketSector2);

        marketSector2 = getMarketSectorSample2();
        assertThat(marketSector1).isNotEqualTo(marketSector2);
    }
}
