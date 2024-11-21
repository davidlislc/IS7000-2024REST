package org.is70002024.market.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.is70002024.market.domain.InsyteLogTestSamples.*;

import org.is70002024.market.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsyteLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsyteLog.class);
        InsyteLog insyteLog1 = getInsyteLogSample1();
        InsyteLog insyteLog2 = new InsyteLog();
        assertThat(insyteLog1).isNotEqualTo(insyteLog2);

        insyteLog2.setId(insyteLog1.getId());
        assertThat(insyteLog1).isEqualTo(insyteLog2);

        insyteLog2 = getInsyteLogSample2();
        assertThat(insyteLog1).isNotEqualTo(insyteLog2);
    }
}
