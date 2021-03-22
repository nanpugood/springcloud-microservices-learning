package com.central.canal.model;

import com.central.canal.consts.CommonConstant;

import lombok.Data;

/**
 * @Description canal消息
 * @author weicl
 * @date 2021年3月11日
 */
@Data
public class CanalMsg {

    private String key;

    private CanalMsgContent canalMsgContent;

    public CanalMsg(CanalMsgContent canalMsgContent) {
        this.key = CommonConstant.CANAL_MSG_KEY_PREFIX + CommonConstant.KEY_SEPARATOR + canalMsgContent.getDbName() + CommonConstant.KEY_SEPARATOR + canalMsgContent.getTableName();
        this.canalMsgContent = canalMsgContent;
    }

    public CanalMsg() {}

}
