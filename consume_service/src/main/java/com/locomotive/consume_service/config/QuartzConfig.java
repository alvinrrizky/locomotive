package com.locomotive.consume_service.config;

import com.locomotive.consume_service.service.SummaryService;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail summaryJobDetail() {
        return JobBuilder.newJob(SummaryService.class)
                .withIdentity("summaryService")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger summaryJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(summaryJobDetail())
                .withIdentity("summaryTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
                .build();
    }
}
