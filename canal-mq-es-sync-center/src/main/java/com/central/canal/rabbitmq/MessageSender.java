package com.central.canal.rabbitmq;

import java.util.UUID;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.central.canal.model.CanalMsgContent;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description rabbitMQ发送消息
 * @author weicl
 * @date 2021年3月11日
 */
@Slf4j
@Service
public class MessageSender implements InitializingBean{
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
    private CanalMsgComfirm canalMsgComfirm;

    @Autowired
    private CanalMsgRetrunListener canalMsgRetrunListener;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public Boolean sendMessage(String routingKey, Object message){
        try {
            String msg = objectMapper.writeValueAsString(message);
            log.info("MQ生产端发送的消息内容为{}",msg);
            String msgId = UUID.randomUUID().toString();
            msgId = routingKey+"_"+msgId;
            CorrelationData correlationData = new CorrelationData(msgId);
            /**
             * MQ生产者发送消息
             */
            //rabbitTemplate.convertAndSend(MqConst.CANAL_TO_ES_EXCHANGE_NAME,MqConst.CANAL_TO_ES_ROUTING_KEY,msg,correlationData);;
            rabbitTemplate.convertAndSend(MqConst.CANAL_TO_ES_EXCHANGE_NAME,MqConst.CANAL_TO_ES_ROUTING_KEY,(CanalMsgContent)message,correlationData);
            return true;
        } catch (Exception e) {
        	log.warn("send to mq failed", e);
            return false;
        }
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		rabbitTemplate.setConfirmCallback(canalMsgComfirm);
	    rabbitTemplate.setReturnCallback(canalMsgRetrunListener);
	    //设置消息转换器
	    Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
	    rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);		
	}
	 
}
