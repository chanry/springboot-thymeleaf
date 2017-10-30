package com.trs.gallery.internal.scheduling;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class KeepaliveSchedulerTask {
	
	private final static Logger LOG = LoggerFactory.getLogger(KeepaliveSchedulerTask.class);
	

    @Scheduled(cron="0 0 0/4 * * ?")
    private void process(){
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	LOG.info("执行定时任务，保持数据库连接不断开，当前时间=[{}]", df.format(new Date()));
    	//List<DataStatistic> list = dataListDao.selectAllLabelCount();
    	//LOG.info("查询数据集=[{}]", list);
    }
	
}
