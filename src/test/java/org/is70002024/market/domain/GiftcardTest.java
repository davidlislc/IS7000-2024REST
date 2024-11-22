package org.is70002024.market.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.is70002024.market.domain.GiftcardTestSamples.*;

import org.is70002024.market.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GiftcardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Giftcard.class);
        Giftcard giftcard1 = getGiftcardSample1();
        Giftcard giftcard2 = new Giftcard();
        assertThat(giftcard1).isNotEqualTo(giftcard2);

        giftcard2.setId(giftcard1.getId());
        assertThat(giftcard1).isEqualTo(giftcard2);

        giftcard2 = getGiftcardSample2();
        assertThat(giftcard1).isNotEqualTo(giftcard2);
    }
}
