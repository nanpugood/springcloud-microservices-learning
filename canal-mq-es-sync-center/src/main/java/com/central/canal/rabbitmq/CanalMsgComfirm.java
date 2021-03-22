package com.central.canal.rabbitmq;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 消息确认组件
 * @author weicl
 * @date 2021年3月11日
 */

@Slf4j
@Component
public class CanalMsgComfirm implements RabbitTemplate.ConfirmCallback{

	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		String msgId = correlationData.getId();
        if(ack) {
            log.info("MQ消息签收成功！！canal同步数据处理消息Id:{}对应的消息被broker签收成功",msgId);
        }else{
            log.warn("MQ消息签收成功！！canal同步数据处理消息Id:{}对应的消息被broker签收失败:{}",msgId,cause);
        }
	}
}
