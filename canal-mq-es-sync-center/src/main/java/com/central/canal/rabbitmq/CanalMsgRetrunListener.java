package com.central.canal.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 消息不可达监听
 * @author weicl
 * @date 2021年3月11日
 */
@Slf4j
@Component
public class CanalMsgRetrunListener implements RabbitTemplate.ReturnCallback {

	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		log.info("****调用消息不可达方法****");
	}

}
