package com.locomotive.generate_service.config;

import com.locomotive.generate_service.job.RandomDataJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail randomDataJobDetail() {
        return JobBuilder.newJob(RandomDataJob.class)
                .withIdentity("randomDataJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger randomDataJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(randomDataJobDetail())
                .withIdentity("randomDataTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?")) // Setiap 10 detik
                .build();
    }
}