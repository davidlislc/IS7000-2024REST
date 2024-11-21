package org.is70002024.market.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.is70002024.market.domain.ServiceTestSamples.*;
import static org.is70002024.market.domain.SubscriptionsTestSamples.*;

import org.is70002024.market.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscriptionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subscriptions.class);
        Subscriptions subscriptions1 = getSubscriptionsSample1();
        Subscriptions subscriptions2 = new Subscriptions();
        assertThat(subscriptions1).isNotEqualTo(subscriptions2);

        subscriptions2.setId(subscriptions1.getId());
        assertThat(subscriptions1).isEqualTo(subscriptions2);

        subscriptions2 = getSubscriptionsSample2();
        assertThat(subscriptions1).isNotEqualTo(subscriptions2);
    }

    @Test
    void serviceTest() {
        Subscriptions subscriptions = getSubscriptionsRandomSampleGenerator();
        Service serviceBack = getServiceRandomSampleGenerator();

        subscriptions.setService(serviceBack);
        assertThat(subscriptions.getService()).isEqualTo(serviceBack);

        subscriptions.service(null);
        assertThat(subscriptions.getService()).isNull();
    }
}
