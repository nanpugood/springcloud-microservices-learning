package com.central.canal.rabbitmq;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.central.canal.consts.CommonEnums.CanalOprDBType;
import com.central.canal.domain.Role;
import com.central.canal.domain.User;
import com.central.canal.model.CanalChangeInfo;
import com.central.canal.model.CanalMsgContent;
import com.central.canal.repository.RoleRepository;
import com.central.canal.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description MQ消费端
 * @author weicl
 * @date 2021年3月11日
 */
@Slf4j
@Service
public class MessageReceiver {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	/**队列名称*/
    public static final String CANAL_TO_ES_QUEUE_NAME = "canal-to-es.queue";
    
    
	
    @RabbitListener(queues = {CANAL_TO_ES_QUEUE_NAME})
    @RabbitHandler
    public void consumerMsgWithLock(Message message, Channel channel) throws IOException {
    	CanalMsgContent content = null;
    	try {
    		content = objectMapper.readValue(message.getBody(),CanalMsgContent.class);
    	}catch(Exception e) {
    		log.warn("json decode failed", e);
    		throw e;
    	}
    	String receiveInfoString = objectMapper.writeValueAsString(content);
    	log.info("MQ接收端收到的消息内容为:{}",receiveInfoString);
    	
    	Long deliveryTag = (Long) message.getMessageProperties().getDeliveryTag();
        //更新的表单
        String tablbeName = content.getTableName();
        String operateType = content.getEventType();
        String DBName = content.getDbName();
        log.info("表名：{},操作类型:{},DB名称:{}",tablbeName,operateType,DBName);
        //新增和更新可以使用DataAfter。
        //删除要使用DataBefore
        List<CanalChangeInfo> afterList = content.getDataAfter();
        log.info("afterList的长度为{}",afterList.size());
        Map<String, String> map = new HashMap<>();
        for (CanalChangeInfo changeInfo : afterList) {
            map.put(changeInfo.getName(), changeInfo.getValue());
        }
        
        List<CanalChangeInfo> beforeList = content.getDataBefore();
        Map<String, String> beforeMap = new HashMap<>();
        for (CanalChangeInfo changeInfo : beforeList) {
        	beforeMap.put(changeInfo.getName(), changeInfo.getValue());
        }
        log.info("读取的DB表单的数据内容为：{}",map);
        operateType = operateType.toUpperCase();
        //实际处理逻辑！！！！！
        //先根据表单类型处理--后续完善
        if(StringUtils.equals(tablbeName, "t_role")) {
        	//新增和更新统一使用save
        	if(StringUtils.equals(CanalOprDBType.INSERT.getValue(), operateType)||StringUtils.equals(CanalOprDBType.UPDATE.getValue(), operateType)) {
              	Role role = new Role();
            	role.setId(Integer.valueOf(map.get("id")));
            	role.setName(String.valueOf(map.get("name")));
            	role.setAge(String.valueOf(map.get("age")));
            	role.setRemark(String.valueOf(map.get("remark")));
            	roleRepository.save(role);
        	}else if(StringUtils.equals(CanalOprDBType.DELETE.getValue(), operateType)){
        		roleRepository.deleteById(Long.valueOf(String.valueOf(beforeMap.get("id"))));
        	}else {
        		log.error("未找到CANAL执行的类型");
        	}
        }else if(StringUtils.equals(tablbeName, "t_user")) {
        	if(StringUtils.equals(CanalOprDBType.INSERT.getValue(), operateType)||StringUtils.equals(CanalOprDBType.UPDATE.getValue(), operateType)) {
            	User user = new User();
            	user.setId(Integer.valueOf(map.get("id")));
            	user.setName(String.valueOf(map.get("name")));
            	user.setAge(String.valueOf(map.get("age")));
            	//新增和更新使用同一个接口 save
            	userRepository.save(user);
        	}else if(StringUtils.equals(CanalOprDBType.DELETE.getValue(), operateType)){
            	userRepository.deleteById(Long.valueOf(String.valueOf(beforeMap.get("id"))));
        	}else {
        		log.error("未找到CANAL执行的类型");
        	}
        }else {
        	log.info("没有找到表名");
        }
        
        channel.basicAck(deliveryTag, false);
        log.info("MQ接收端处理完毕");
    	
    }


}
