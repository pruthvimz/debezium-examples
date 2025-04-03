package io.debezium.examples.otel.servicetwo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class KafkaConfiguration {

	@Bean
	public NewTopic debeziumEventTopic() {
		return new NewTopic("debezium_event_business_topic", 1, (short) 1);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			ConsumerFactory<Object, Object> kafkaConsumerFactory) {
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setRetryTemplate(retryTemplate());
		factory.setMessageConverter(new JsonMessageConverter());
		factory.getContainerProperties().setAckMode(AckMode.RECORD);
		factory.setConcurrency(1);
		configurer.configure(factory, kafkaConsumerFactory);
		return factory;
	}

	@Bean
	public RetryTemplate retryTemplate() {
		final ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(1000);
		backOffPolicy.setMultiplier(1);
		final RetryTemplate template = new RetryTemplate();
//		template.setRetryPolicy(new AlwaysRetryPolicy());
		// TODO:Check this config
		template.setRetryPolicy(new SimpleRetryPolicy(1));
		template.setBackOffPolicy(backOffPolicy);
		return template;
	}

}
