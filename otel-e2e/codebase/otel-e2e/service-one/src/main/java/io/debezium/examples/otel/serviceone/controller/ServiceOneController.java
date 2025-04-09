package io.debezium.examples.otel.serviceone.controller;

import io.debezium.examples.otel.serviceone.entity.TableOneEntity;
import io.debezium.examples.otel.serviceone.entity.TableTwoEntity;
import io.debezium.examples.otel.serviceone.repository.TableOneRepository;
import io.debezium.examples.otel.serviceone.repository.TableTwoRepository;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.StringWriter;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

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
        String traceParent = getSpanContext();
        log.info("Current traceparent: {}", traceParent);
        processTableOne(data1, traceParent);
        processTableTwo(data2, traceParent);
    }
    private void processTableOne(final String data1, final String traceParent) {
        Optional<TableOneEntity> tableOneEntityOpt = tableOneRepository.findByData(data1);
        TableOneEntity tableOneEntity;
        if (tableOneEntityOpt.isEmpty()) {
            tableOneEntity = new TableOneEntity();
            tableOneEntity.setCreatedOn(new Date());
        } else {
            tableOneEntity = tableOneEntityOpt.get();
            tableOneEntity.setUpdatedOn(new Date());
        }
        tableOneEntity.setTracingspancontext(getSerializedTraceParentProperties(traceParent));
        tableOneEntity.setData(data1);
        tableOneRepository.save(tableOneEntity);
        log.info("Table One Process Completed");
    }
    private void processTableTwo(final String data2, final String traceParent) {
        Optional<TableTwoEntity> tableTwoEntityOpt = tableTwoRepository.findByData(data2);
        TableTwoEntity tableTwoEntity;
        if (tableTwoEntityOpt.isEmpty()) {
            tableTwoEntity = new TableTwoEntity();
            tableTwoEntity.setCreatedOn(new Date());
        } else {
            tableTwoEntity = tableTwoEntityOpt.get();
            tableTwoEntity.setUpdatedOn(new Date());
        }
        tableTwoEntity.setTracingspancontext(getSerializedTraceParentProperties(traceParent));
        tableTwoEntity.setData(data2);
        tableTwoRepository.save(tableTwoEntity);
        log.info("Table Two Process Completed");
    }

    private String getSpanContext() {
        SpanContext context = Span.current().getSpanContext();
        return String.format("00-%s-%s-01", context.getTraceId(), context.getSpanId());
    }

    @Nullable
    public static String getSerializedTraceParentProperties(String traceParent) {
        Properties props = new Properties();
        props.setProperty("traceparent", traceParent);
        try (StringWriter writer = new StringWriter()) {
            props.store(writer, null);
            return writer.toString();
        } catch (Exception e) {
//            throw new RuntimeException("Failed to serialize properties", e);
            log.error("Failed to serialize traceparent property", e);
            return null;
        }
    }
}
