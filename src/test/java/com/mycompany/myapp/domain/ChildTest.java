package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AchivementTestSamples.*;
import static com.mycompany.myapp.domain.BalanceTestSamples.*;
import static com.mycompany.myapp.domain.ChildTestSamples.*;
import static com.mycompany.myapp.domain.CreditScoreTestSamples.*;
import static com.mycompany.myapp.domain.ParentsTestSamples.*;
import static com.mycompany.myapp.domain.PointsTestSamples.*;
import static com.mycompany.myapp.domain.TaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ChildTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Child.class);
        Child child1 = getChildSample1();
        Child child2 = new Child();
        assertThat(child1).isNotEqualTo(child2);

        child2.setId(child1.getId());
        assertThat(child1).isEqualTo(child2);

        child2 = getChildSample2();
        assertThat(child1).isNotEqualTo(child2);
    }

    @Test
    void creditScoreTest() throws Exception {
        Child child = getChildRandomSampleGenerator();
        CreditScore creditScoreBack = getCreditScoreRandomSampleGenerator();

        child.setCreditScore(creditScoreBack);
        assertThat(child.getCreditScore()).isEqualTo(creditScoreBack);

        child.creditScore(null);
        assertThat(child.getCreditScore()).isNull();
    }

    @Test
    void pointsTest() throws Exception {
        Child child = getChildRandomSampleGenerator();
        Points pointsBack = getPointsRandomSampleGenerator();

        child.setPoints(pointsBack);
        assertThat(child.getPoints()).isEqualTo(pointsBack);

        child.points(null);
        assertThat(child.getPoints()).isNull();
    }

    @Test
    void accountTest() throws Exception {
        Child child = getChildRandomSampleGenerator();
        Balance balanceBack = getBalanceRandomSampleGenerator();

        child.setAccount(balanceBack);
        assertThat(child.getAccount()).isEqualTo(balanceBack);

        child.account(null);
        assertThat(child.getAccount()).isNull();
    }

    @Test
    void taskTest() throws Exception {
        Child child = getChildRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        child.setTask(taskBack);
        assertThat(child.getTask()).isEqualTo(taskBack);

        child.task(null);
        assertThat(child.getTask()).isNull();
    }

    @Test
    void achivementTest() throws Exception {
        Child child = getChildRandomSampleGenerator();
        Achivement achivementBack = getAchivementRandomSampleGenerator();

        child.addAchivement(achivementBack);
        assertThat(child.getAchivements()).containsOnly(achivementBack);
        assertThat(achivementBack.getChild()).isEqualTo(child);

        child.removeAchivement(achivementBack);
        assertThat(child.getAchivements()).doesNotContain(achivementBack);
        assertThat(achivementBack.getChild()).isNull();

        child.achivements(new HashSet<>(Set.of(achivementBack)));
        assertThat(child.getAchivements()).containsOnly(achivementBack);
        assertThat(achivementBack.getChild()).isEqualTo(child);

        child.setAchivements(new HashSet<>());
        assertThat(child.getAchivements()).doesNotContain(achivementBack);
        assertThat(achivementBack.getChild()).isNull();
    }

    @Test
    void parentsTest() throws Exception {
        Child child = getChildRandomSampleGenerator();
        Parents parentsBack = getParentsRandomSampleGenerator();

        child.addParents(parentsBack);
        assertThat(child.getParents()).containsOnly(parentsBack);
        assertThat(parentsBack.getChildren()).containsOnly(child);

        child.removeParents(parentsBack);
        assertThat(child.getParents()).doesNotContain(parentsBack);
        assertThat(parentsBack.getChildren()).doesNotContain(child);

        child.parents(new HashSet<>(Set.of(parentsBack)));
        assertThat(child.getParents()).containsOnly(parentsBack);
        assertThat(parentsBack.getChildren()).containsOnly(child);

        child.setParents(new HashSet<>());
        assertThat(child.getParents()).doesNotContain(parentsBack);
        assertThat(parentsBack.getChildren()).doesNotContain(child);
    }
}
