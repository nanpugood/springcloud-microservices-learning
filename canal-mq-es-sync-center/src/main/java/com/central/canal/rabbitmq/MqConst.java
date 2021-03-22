package com.central.canal.rabbitmq;

/**
 * @Description 消息队列常量定义类
 * @author weicl
 * @date 2021年3月11日
 */
public class MqConst {
	
	 /**交换机名称*/
    public static final String CANAL_TO_ES_EXCHANGE_NAME = "canal-to-es.exchange";

    /**队列名称*/
    public static final String CANAL_TO_ES_QUEUE_NAME = "canal-to-es.queue";

    /**路由key*/
    public static final String CANAL_TO_ES_ROUTING_KEY = "canal-to-es.key";

    /**消息重发的最大次数*/
    public static final Integer MSG_RETRY_COUNT = 5;

    public static final Integer TIME_DIFF = 30;
}
