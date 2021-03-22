package com.central.canal.model;

/**
 * @Description canal消息处理
 * @author weicl
 * @date 2021年3月11日
 */
public interface CanalMsgHandler {

    Boolean handle(CanalMsg canalMsg);

}
