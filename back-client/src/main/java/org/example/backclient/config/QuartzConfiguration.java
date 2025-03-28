package org.example.backclient.config;

import org.example.backclient.task.MonitorJobBean;
import org.quartz.*;
import org.springframework.context.annotation.Bean;

public class QuartzConfiguration {
    @Bean
    public JobDetail quartzFactoryBean() {
        return JobBuilder.newJob(MonitorJobBean.class)
                .withIdentity("monitor-task")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger cronTriggerFactoryBean(JobDetail detail) {
        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule("*/10 * * * * * ?");
        return TriggerBuilder.newTrigger()
                .forJob(detail)
                .withIdentity("monitor-trigger")
                .withSchedule(cron)
                .build();
    }

}
