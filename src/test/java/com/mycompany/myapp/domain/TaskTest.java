package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ChildTestSamples.*;
import static com.mycompany.myapp.domain.ParentsTestSamples.*;
import static com.mycompany.myapp.domain.TaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Task.class);
        Task task1 = getTaskSample1();
        Task task2 = new Task();
        assertThat(task1).isNotEqualTo(task2);

        task2.setId(task1.getId());
        assertThat(task1).isEqualTo(task2);

        task2 = getTaskSample2();
        assertThat(task1).isNotEqualTo(task2);
    }

    @Test
    void childTest() throws Exception {
        Task task = getTaskRandomSampleGenerator();
        Child childBack = getChildRandomSampleGenerator();

        task.addChild(childBack);
        assertThat(task.getChildren()).containsOnly(childBack);
        assertThat(childBack.getTask()).isEqualTo(task);

        task.removeChild(childBack);
        assertThat(task.getChildren()).doesNotContain(childBack);
        assertThat(childBack.getTask()).isNull();

        task.children(new HashSet<>(Set.of(childBack)));
        assertThat(task.getChildren()).containsOnly(childBack);
        assertThat(childBack.getTask()).isEqualTo(task);

        task.setChildren(new HashSet<>());
        assertThat(task.getChildren()).doesNotContain(childBack);
        assertThat(childBack.getTask()).isNull();
    }

    @Test
    void parentsTest() throws Exception {
        Task task = getTaskRandomSampleGenerator();
        Parents parentsBack = getParentsRandomSampleGenerator();

        task.addParents(parentsBack);
        assertThat(task.getParents()).containsOnly(parentsBack);
        assertThat(parentsBack.getTask()).isEqualTo(task);

        task.removeParents(parentsBack);
        assertThat(task.getParents()).doesNotContain(parentsBack);
        assertThat(parentsBack.getTask()).isNull();

        task.parents(new HashSet<>(Set.of(parentsBack)));
        assertThat(task.getParents()).containsOnly(parentsBack);
        assertThat(parentsBack.getTask()).isEqualTo(task);

        task.setParents(new HashSet<>());
        assertThat(task.getParents()).doesNotContain(parentsBack);
        assertThat(parentsBack.getTask()).isNull();
    }
}
