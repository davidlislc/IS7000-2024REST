package org.is70002024.market.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.is70002024.market.domain.BatchTestSamples.*;

import org.is70002024.market.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BatchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Batch.class);
        Batch batch1 = getBatchSample1();
        Batch batch2 = new Batch();
        assertThat(batch1).isNotEqualTo(batch2);

        batch2.setId(batch1.getId());
        assertThat(batch1).isEqualTo(batch2);

        batch2 = getBatchSample2();
        assertThat(batch1).isNotEqualTo(batch2);
    }
}
