package org.example.backclient.task;

import jakarta.annotation.Resource;
import org.example.backclient.entity.RuntimeDetail;
import org.example.backclient.utils.NetUtils;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.management.monitor.Monitor;

@Component
public class MonitorJobBean extends QuartzJobBean {
    @Resource
    Monitor monitor;
    @Resource
    NetUtils netUtils;

    @Override
    protected void executeInternal(JobExcutionContext context) throws JobExecutionException {
        RuntimeDetail runtimeDetail = new RuntimeDetail();
        netUtils.updateRuntimeDetails(runtimeDetail);
    }
}
