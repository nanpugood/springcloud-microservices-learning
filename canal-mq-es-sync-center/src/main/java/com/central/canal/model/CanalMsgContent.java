package com.central.canal.model;

import lombok.Data;

import java.util.List;

/**
 * @Description Canal基础信息 包括表名等
 * @author weicl
 * @date 2021年3月11日
 */
@Data
public class CanalMsgContent {

    private String binLogFile;
    private Long binlogOffset;
    private String dbName;
    private String tableName;
    private String eventType;
    private List<CanalChangeInfo> dataBefore;
    private List<CanalChangeInfo> dataAfter;
}
