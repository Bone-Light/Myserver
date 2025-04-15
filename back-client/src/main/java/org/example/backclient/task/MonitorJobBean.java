package org.example.backclient.task;

import jakarta.annotation.Resource;
import org.example.backclient.entity.RuntimeDetail;
import org.example.backclient.utils.MonitorUtils;
import org.example.backclient.utils.NetUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
@Component
public class MonitorJobBean extends QuartzJobBean {
    @Resource
    MonitorUtils monitor;
    @Resource
    NetUtils netUtils;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        RuntimeDetail runtimeDetail = monitor.monitorRuntimeDetail();
        System.out.println(runtimeDetail);
        System.out.println("end");
        netUtils.updateRuntimeDetails(runtimeDetail);
    }
}
