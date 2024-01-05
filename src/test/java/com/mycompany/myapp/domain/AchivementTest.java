package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AchivementTestSamples.*;
import static com.mycompany.myapp.domain.ChildTestSamples.*;
import static com.mycompany.myapp.domain.ParentsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AchivementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Achivement.class);
        Achivement achivement1 = getAchivementSample1();
        Achivement achivement2 = new Achivement();
        assertThat(achivement1).isNotEqualTo(achivement2);

        achivement2.setId(achivement1.getId());
        assertThat(achivement1).isEqualTo(achivement2);

        achivement2 = getAchivementSample2();
        assertThat(achivement1).isNotEqualTo(achivement2);
    }

    @Test
    void childTest() throws Exception {
        Achivement achivement = getAchivementRandomSampleGenerator();
        Child childBack = getChildRandomSampleGenerator();

        achivement.setChild(childBack);
        assertThat(achivement.getChild()).isEqualTo(childBack);

        achivement.child(null);
        assertThat(achivement.getChild()).isNull();
    }

    @Test
    void parentsTest() throws Exception {
        Achivement achivement = getAchivementRandomSampleGenerator();
        Parents parentsBack = getParentsRandomSampleGenerator();

        achivement.setParents(parentsBack);
        assertThat(achivement.getParents()).isEqualTo(parentsBack);

        achivement.parents(null);
        assertThat(achivement.getParents()).isNull();
    }
}
