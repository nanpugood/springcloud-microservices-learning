package com.central.canal.config;

import java.net.InetSocketAddress;

import org.springframework.stereotype.Service;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.central.canal.consts.CommonConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description canal 初始化连接
 * @author weicl
 * @date 2021年3月11日
 */
@Slf4j
@Service
public class CanalPool {
	
	public CanalConnector getConnector() {
		
		String canalHost = CommonConstant.CANAL_HOST;
		int canalPort = CommonConstant.CANAL_PORT;
		String canalDest = CommonConstant.CANAL_DEST;
				
		log.info("canal客户端开始连接服务端，地址:{}，端口:{},实例名:{}",canalHost,canalPort,canalDest);
        // 基于zookeeper动态获取canal server的地址
		CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(canalHost, canalPort), canalDest,"","");
        return connector;
	}
}
