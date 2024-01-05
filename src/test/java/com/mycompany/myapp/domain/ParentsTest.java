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

class ParentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parents.class);
        Parents parents1 = getParentsSample1();
        Parents parents2 = new Parents();
        assertThat(parents1).isNotEqualTo(parents2);

        parents2.setId(parents1.getId());
        assertThat(parents1).isEqualTo(parents2);

        parents2 = getParentsSample2();
        assertThat(parents1).isNotEqualTo(parents2);
    }

    @Test
    void creditScoreTest() throws Exception {
        Parents parents = getParentsRandomSampleGenerator();
        CreditScore creditScoreBack = getCreditScoreRandomSampleGenerator();

        parents.setCreditScore(creditScoreBack);
        assertThat(parents.getCreditScore()).isEqualTo(creditScoreBack);

        parents.creditScore(null);
        assertThat(parents.getCreditScore()).isNull();
    }

    @Test
    void pointsTest() throws Exception {
        Parents parents = getParentsRandomSampleGenerator();
        Points pointsBack = getPointsRandomSampleGenerator();

        parents.setPoints(pointsBack);
        assertThat(parents.getPoints()).isEqualTo(pointsBack);

        parents.points(null);
        assertThat(parents.getPoints()).isNull();
    }

    @Test
    void balanceTest() throws Exception {
        Parents parents = getParentsRandomSampleGenerator();
        Balance balanceBack = getBalanceRandomSampleGenerator();

        parents.addBalance(balanceBack);
        assertThat(parents.getBalances()).containsOnly(balanceBack);
        assertThat(balanceBack.getParents()).isEqualTo(parents);

        parents.removeBalance(balanceBack);
        assertThat(parents.getBalances()).doesNotContain(balanceBack);
        assertThat(balanceBack.getParents()).isNull();

        parents.balances(new HashSet<>(Set.of(balanceBack)));
        assertThat(parents.getBalances()).containsOnly(balanceBack);
        assertThat(balanceBack.getParents()).isEqualTo(parents);

        parents.setBalances(new HashSet<>());
        assertThat(parents.getBalances()).doesNotContain(balanceBack);
        assertThat(balanceBack.getParents()).isNull();
    }

    @Test
    void taskTest() throws Exception {
        Parents parents = getParentsRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        parents.setTask(taskBack);
        assertThat(parents.getTask()).isEqualTo(taskBack);

        parents.task(null);
        assertThat(parents.getTask()).isNull();
    }

    @Test
    void childTest() throws Exception {
        Parents parents = getParentsRandomSampleGenerator();
        Child childBack = getChildRandomSampleGenerator();

        parents.addChild(childBack);
        assertThat(parents.getChildren()).containsOnly(childBack);

        parents.removeChild(childBack);
        assertThat(parents.getChildren()).doesNotContain(childBack);

        parents.children(new HashSet<>(Set.of(childBack)));
        assertThat(parents.getChildren()).containsOnly(childBack);

        parents.setChildren(new HashSet<>());
        assertThat(parents.getChildren()).doesNotContain(childBack);
    }

    @Test
    void achivementTest() throws Exception {
        Parents parents = getParentsRandomSampleGenerator();
        Achivement achivementBack = getAchivementRandomSampleGenerator();

        parents.addAchivement(achivementBack);
        assertThat(parents.getAchivements()).containsOnly(achivementBack);
        assertThat(achivementBack.getParents()).isEqualTo(parents);

        parents.removeAchivement(achivementBack);
        assertThat(parents.getAchivements()).doesNotContain(achivementBack);
        assertThat(achivementBack.getParents()).isNull();

        parents.achivements(new HashSet<>(Set.of(achivementBack)));
        assertThat(parents.getAchivements()).containsOnly(achivementBack);
        assertThat(achivementBack.getParents()).isEqualTo(parents);

        parents.setAchivements(new HashSet<>());
        assertThat(parents.getAchivements()).doesNotContain(achivementBack);
        assertThat(achivementBack.getParents()).isNull();
    }
}
