package io.debezium.examples.otel.serviceone.repository;

import io.debezium.examples.otel.serviceone.entity.TableOneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableOneRepository extends JpaRepository<TableOneEntity, Long> {

    Optional<TableOneEntity> findByData(String data);
}
