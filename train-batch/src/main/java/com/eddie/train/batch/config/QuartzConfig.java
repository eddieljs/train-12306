package com.eddie.train.batch.config;// package com.jiawa.train.batch.config;

 import com.eddie.train.batch.job.TestJob;
 import org.quartz.*;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;

 @DisallowConcurrentExecution//禁止并发执行
 @Configuration
 public class QuartzConfig {

     /**
      * 声明一个任务
      * @return
      */
     @Bean
     public JobDetail jobDetail() {
         return JobBuilder.newJob(TestJob.class)
                 .withIdentity("TestJob", "test")
                 .storeDurably()
                 .build();
     }

     /**
      * 声明一个触发器，什么时候触发这个任务
      * @return
      */
     @Bean
     public Trigger trigger() {
         return TriggerBuilder.newTrigger()
                 .forJob(jobDetail())
                 .withIdentity("trigger", "trigger")
                 .startNow()
                 .withSchedule(CronScheduleBuilder.cronSchedule("*/3 * * * * ?"))
                 .build();
     }
 }
