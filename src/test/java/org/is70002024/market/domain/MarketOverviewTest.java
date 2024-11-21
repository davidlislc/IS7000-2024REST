package org.is70002024.market.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.is70002024.market.domain.MarketOverviewTestSamples.*;

import org.is70002024.market.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarketOverviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarketOverview.class);
        MarketOverview marketOverview1 = getMarketOverviewSample1();
        MarketOverview marketOverview2 = new MarketOverview();
        assertThat(marketOverview1).isNotEqualTo(marketOverview2);

        marketOverview2.setId(marketOverview1.getId());
        assertThat(marketOverview1).isEqualTo(marketOverview2);

        marketOverview2 = getMarketOverviewSample2();
        assertThat(marketOverview1).isNotEqualTo(marketOverview2);
    }
}
