package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ChildTestSamples.*;
import static com.mycompany.myapp.domain.ParentsTestSamples.*;
import static com.mycompany.myapp.domain.PointsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PointsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Points.class);
        Points points1 = getPointsSample1();
        Points points2 = new Points();
        assertThat(points1).isNotEqualTo(points2);

        points2.setId(points1.getId());
        assertThat(points1).isEqualTo(points2);

        points2 = getPointsSample2();
        assertThat(points1).isNotEqualTo(points2);
    }

    @Test
    void parentsTest() throws Exception {
        Points points = getPointsRandomSampleGenerator();
        Parents parentsBack = getParentsRandomSampleGenerator();

        points.setParents(parentsBack);
        assertThat(points.getParents()).isEqualTo(parentsBack);
        assertThat(parentsBack.getPoints()).isEqualTo(points);

        points.parents(null);
        assertThat(points.getParents()).isNull();
        assertThat(parentsBack.getPoints()).isNull();
    }

    @Test
    void childTest() throws Exception {
        Points points = getPointsRandomSampleGenerator();
        Child childBack = getChildRandomSampleGenerator();

        points.setChild(childBack);
        assertThat(points.getChild()).isEqualTo(childBack);
        assertThat(childBack.getPoints()).isEqualTo(points);

        points.child(null);
        assertThat(points.getChild()).isNull();
        assertThat(childBack.getPoints()).isNull();
    }
}
