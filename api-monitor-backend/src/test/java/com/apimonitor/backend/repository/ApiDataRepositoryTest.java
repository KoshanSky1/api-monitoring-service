package com.apimonitor.backend.repository;

import com.apimonitor.backend.entity.ApiDataEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ApiDataRepositoryTest {

    @Autowired private ApiDataRepository repository;
    @Autowired private TestEntityManager entityManager;

    @Test
    void findTop10ByOrderByCreatedAtDesc_shouldReturnSortedEntities() {
        ApiDataEntity e1 = ApiDataEntity.builder().createdAt(Instant.now().minusSeconds(100)).success(true).payload("old").build();
        ApiDataEntity e2 = ApiDataEntity.builder().createdAt(Instant.now().minusSeconds(50)).success(false).payload("mid").build();
        ApiDataEntity e3 = ApiDataEntity.builder().createdAt(Instant.now()).success(true).payload("new").build();

        entityManager.persist(e1);
        entityManager.persist(e2);
        entityManager.persist(e3);
        entityManager.flush();

        List<ApiDataEntity> result = repository.findTop10ByOrderByCreatedAtDesc();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getCreatedAt()).isAfterOrEqualTo(result.get(1).getCreatedAt());
        assertThat(result.get(1).getCreatedAt()).isAfterOrEqualTo(result.get(2).getCreatedAt());
        assertThat(result.get(2).getPayload()).isEqualTo("old");
    }
}