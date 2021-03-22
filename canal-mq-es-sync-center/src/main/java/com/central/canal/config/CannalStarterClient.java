package com.central.canal.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.central.canal.consts.CommonConstant;
import com.central.canal.model.CanalChangeInfo;
import com.central.canal.model.CanalMsg;
import com.central.canal.model.CanalMsgContent;
import com.central.canal.model.CanalMsgHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description canal客户端启动
 * @author weicl
 * @date 2021年3月11日
 */
@Slf4j
@Component
public class CannalStarterClient /*implements InitializingBean*/ implements ApplicationRunner{
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		log.info("启动了");
//	}
    @Autowired
    private CanalMsgHandler canalMsgHandler;

    @Autowired
    private CanalPool canalPool;

	@Override
	public void /*afterPropertiesSet()*/ run(ApplicationArguments args) throws Exception {
		
		int emptyCount = 0;
        CanalConnector canalConnector = canalPool.getConnector();
        
        try {
        	
            canalConnector.connect();
            canalConnector.subscribe(".*\\..*");
            canalConnector.rollback();
            log.info("canal连接成功！！！！");
            while (emptyCount < CommonConstant.CANAL_TOTAL_EMPTY_COUNT) {
                // 获取指定数量的数据
                Message message = canalConnector.getWithoutAck(CommonConstant.BATCH_SIZE);
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    //System.out.println(emptyCount);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    	e.printStackTrace();
                    }
                } else {
                	/**
                	 * 开始处理MySQL同步过来的数据
                	 */
                    emptyCount = 0;
                    processEntry(message.getEntries());
                }
                // 提交确认
                canalConnector.ack(batchId);
                // 处理失败, 回滚数据
                // connector.rollback(batchId);
            }
        } catch (Exception e) {
            log.warn("canal process error", e);
        } finally {
        	log.info("跳出while同步循环条件满足，关闭canal连接！！！！");
            canalConnector.disconnect();
        }
	}
	
	private void processEntry(List<CanalEntry.Entry> entries) {
        List<CanalMsg> msgList = convertToCanalMsgList(entries);
        for (CanalMsg msg : msgList) {
        	/**
        	 * 将信息发送给MQ
        	 */
            canalMsgHandler.handle(msg);
        }
    }
	private List<CanalMsg> convertToCanalMsgList(List<CanalEntry.Entry> entries) {
        List<CanalMsg> msgList = new ArrayList<CanalMsg>();
        CanalMsgContent canalMsgContent = null;
        for (CanalEntry.Entry entry : entries) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }

            CanalEntry.RowChange rowChange = null;
            try {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parse error, data:" + entry.toString(), e);
            }

            CanalEntry.EventType eventType = rowChange.getEventType();
            //将canal同步过来的信息转化为自己实体信息
            canalMsgContent = new CanalMsgContent();
            canalMsgContent.setBinLogFile(entry.getHeader().getLogfileName());
            canalMsgContent.setBinlogOffset(entry.getHeader().getLogfileOffset());
            canalMsgContent.setDbName(entry.getHeader().getSchemaName());
            canalMsgContent.setTableName(entry.getHeader().getTableName());
            canalMsgContent.setEventType(eventType.toString().toLowerCase());

            for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                canalMsgContent.setDataBefore(convertToCanalChangeInfoList(rowData.getBeforeColumnsList()));
                canalMsgContent.setDataAfter(convertToCanalChangeInfoList(rowData.getAfterColumnsList()));
                CanalMsg canalMsg = new CanalMsg(canalMsgContent);
                msgList.add(canalMsg);
            }
        }

        return msgList;
    }	
	
	private List<CanalChangeInfo> convertToCanalChangeInfoList(List<CanalEntry.Column> columnList) {
        List<CanalChangeInfo> canalChangeInfoList = new ArrayList<CanalChangeInfo>();
        for (CanalEntry.Column column : columnList) {
            CanalChangeInfo canalChangeInfo = new CanalChangeInfo();
            canalChangeInfo.setName(column.getName());
            canalChangeInfo.setValue(column.getValue());
            canalChangeInfo.setUpdate(column.getUpdated());
            canalChangeInfoList.add(canalChangeInfo);
        }

        return canalChangeInfoList;
    }	
	
}
