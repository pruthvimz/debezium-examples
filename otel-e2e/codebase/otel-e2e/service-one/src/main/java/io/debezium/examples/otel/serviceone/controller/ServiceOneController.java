package io.debezium.examples.otel.serviceone.controller;

import io.debezium.examples.otel.serviceone.entity.TableOneEntity;
import io.debezium.examples.otel.serviceone.entity.TableTwoEntity;
import io.debezium.examples.otel.serviceone.repository.TableOneRepository;
import io.debezium.examples.otel.serviceone.repository.TableTwoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/v1/service-one")
public class ServiceOneController {

    @Autowired
    private TableOneRepository tableOneRepository;

    @Autowired
    private TableTwoRepository tableTwoRepository;

    @PutMapping("/record/{data1}/{data2}")
    public @ResponseBody void record(@PathVariable("data1") String data1, @PathVariable("data2") String data2) {
        processEvent(data1, data2);
    }

    private void processEvent(final String data1, final String data2) {
        log.info("Started Processing {} and {}", data1, data2);
        processTableOne(data1);
        processTableTwo(data2);
    }
    private void processTableOne(final String data1) {
        TableOneEntity tableOneEntity = tableOneRepository.findByData(data1)
                .orElseGet(TableOneEntity::new);
        tableOneEntity.setData(data1);
        tableOneRepository.save(tableOneEntity);
        log.info("Table One Process Completed");
    }
    private void processTableTwo(final String data2) {
        TableTwoEntity tableTwoEntity = tableTwoRepository.findByData(data2)
                .orElseGet(TableTwoEntity::new);
        tableTwoEntity.setData(data2);
        tableTwoRepository.save(tableTwoEntity);
        log.info("Table Two Process Completed");
    }
}
