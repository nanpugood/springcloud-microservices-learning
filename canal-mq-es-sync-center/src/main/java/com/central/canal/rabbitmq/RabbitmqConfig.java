package com.central.canal.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description RabbitMQ配置类
 * @author weicl
 * @date 2021年3月11日
 */
@Configuration
public class RabbitmqConfig {
	
	@Bean
    public DirectExchange orderToProductExchange() {
        DirectExchange directExchange = new DirectExchange(MqConst.CANAL_TO_ES_EXCHANGE_NAME,true,false);
        return directExchange;
    }

    @Bean
    public Queue orderToProductQueue() {
        Queue queue = new Queue(MqConst.CANAL_TO_ES_QUEUE_NAME,true,false,false);
        return queue;
    }

    @Bean
    public Binding orderToProductBinding() {
        return BindingBuilder.bind(orderToProductQueue()).to(orderToProductExchange()).with(MqConst.CANAL_TO_ES_ROUTING_KEY);
    }
}
