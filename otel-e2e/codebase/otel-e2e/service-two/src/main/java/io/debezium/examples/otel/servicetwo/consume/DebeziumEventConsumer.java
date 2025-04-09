package io.debezium.examples.otel.servicetwo.consume;

import com.fasterxml.jackson.databind.JsonNode;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DebeziumEventConsumer {

    private static long count;

    @KafkaListener(topics = "debezium_event_business_topic",
            properties = { "max.poll.interval.ms:300000", "max.poll.records=10" })
    public void receiveDebeziumEvent(@Payload JsonNode data, @Headers MessageHeaders headers) {
        count++;
        log.info("Current traceparent: {}", getSpanContext());
        log.info("Received Debezium Event Number {}", count);
        String table = data.get("payload").get("source").get("table").textValue();
        log.info("Event received for table : {}", table);
    }

    private String getSpanContext() {
        SpanContext context = Span.current().getSpanContext();
        return String.format("00-%s-%s-01", context.getTraceId(), context.getSpanId());
    }
}
