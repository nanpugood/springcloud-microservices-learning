package com.central.canal.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.central.canal.rabbitmq.MessageSender;


/**
 * @Description 利用MQ处理canal消息处理
 * @author weicl
 * @date 2021年3月11日
 */
@Service
public class CanalMsgMQHandlerImpl implements CanalMsgHandler {

    @Autowired
    private MessageSender messageSender;

    @Override
    public Boolean handle(CanalMsg canalMsg) {
        return messageSender.sendMessage(canalMsg.getKey(), canalMsg.getCanalMsgContent());
    }

}
