package org.aoxinhu.trend.job;
 
import java.util.List;
 
import cn.hutool.core.date.DateUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
 
import org.aoxinhu.trend.pojo.Index;
import org.aoxinhu.trend.service.IndexDataService;
import org.aoxinhu.trend.service.IndexService;
 
public class IndexDataSyncJob extends QuartzJobBean {
     
    @Autowired
    private IndexService indexService;
 
    @Autowired
    private IndexDataService indexDataService;
     
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Timer start:" + DateUtil.now());
        List<Index> indexes = indexService.fresh();
        for (Index index : indexes) {
             indexDataService.fresh(index.getCode());
        }
        System.out.println("Timer end:" + DateUtil.now());
 
    }
 
}