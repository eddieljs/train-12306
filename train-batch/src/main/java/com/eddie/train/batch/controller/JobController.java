package com.eddie.train.batch.controller;

import com.eddie.train.batch.req.CronJobReq;
import com.eddie.train.batch.resp.CronJobResp;
import com.eddie.train.common.resp.Result; // 导入新的Result类
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/admin/job")
public class JobController {

    private static Logger LOG = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @RequestMapping(value = "/run")
    public Result<Object> run(@RequestBody CronJobReq cronJobReq) throws SchedulerException {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        LOG.info("手动执行任务开始：{}, {}", jobClassName, jobGroupName);
        schedulerFactoryBean.getScheduler().triggerJob(JobKey.jobKey(jobClassName, jobGroupName));
        return Result.success(null); // 成功但无返回数据
    }

    @RequestMapping(value = "/add")
    public Result<String> add(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        String cronExpression = cronJobReq.getCronExpression();
        String description = cronJobReq.getDescription();
        LOG.info("创建定时任务开始：{}，{}，{}，{}", jobClassName, jobGroupName, cronExpression, description);

        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.start();

            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(jobClassName))
                    .withIdentity(jobClassName, jobGroupName).build();

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobClassName, jobGroupName)
                    .withDescription(description)
                    .withSchedule(scheduleBuilder)
                    .build();

            sched.scheduleJob(jobDetail, trigger);
            return Result.success("任务创建成功"); // 返回成功消息
        } catch (SchedulerException e) {
            LOG.error("创建定时任务失败:" + e);
            return Result.error("创建定时任务失败:调度异常");
        } catch (ClassNotFoundException e) {
            LOG.error("创建定时任务失败:" + e);
            return Result.error("创建定时任务失败：任务类不存在");
        }
    }

    @RequestMapping(value = "/pause")
    public Result<String> pause(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        LOG.info("暂停定时任务开始：{}，{}", jobClassName, jobGroupName);

        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
            return Result.success("任务暂停成功");
        } catch (SchedulerException e) {
            LOG.error("暂停定时任务失败:" + e);
            return Result.error("暂停定时任务失败:调度异常");
        }
    }

    @RequestMapping(value = "/resume")
    public Result<String> resume(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        LOG.info("重启定时任务开始：{}，{}", jobClassName, jobGroupName);

        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
            return Result.success("任务重启成功");
        } catch (SchedulerException e) {
            LOG.error("重启定时任务失败:" + e);
            return Result.error("重启定时任务失败:调度异常");
        }
    }

    @RequestMapping(value = "/reschedule")
    public Result<String> reschedule(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        String cronExpression = cronJobReq.getCronExpression();
        String description = cronJobReq.getDescription();
        LOG.info("更新定时任务开始：{}，{}，{}，{}", jobClassName, jobGroupName, cronExpression, description);

        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTriggerImpl trigger1 = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
            trigger1.setStartTime(new Date());

            CronTrigger trigger = trigger1.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withDescription(description)
                    .withSchedule(scheduleBuilder)
                    .build();

            scheduler.rescheduleJob(triggerKey, trigger);
            return Result.success("任务更新成功");
        } catch (Exception e) {
            LOG.error("更新定时任务失败:" + e);
            return Result.error("更新定时任务失败:调度异常");
        }
    }

    @RequestMapping(value = "/delete")
    public Result<String> delete(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        LOG.info("删除定时任务开始：{}，{}", jobClassName, jobGroupName);

        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
            return Result.success("任务删除成功");
        } catch (SchedulerException e) {
            LOG.error("删除定时任务失败:" + e);
            return Result.error("删除定时任务失败:调度异常");
        }
    }

    @RequestMapping(value="/query")
    public Result<List<CronJobResp>> query() {
        LOG.info("查看所有定时任务开始");
        List<CronJobResp> cronJobDtoList = new ArrayList<>();

        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    CronJobResp cronJobResp = new CronJobResp();
                    cronJobResp.setName(jobKey.getName());
                    cronJobResp.setGroup(jobKey.getGroup());

                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    if (!triggers.isEmpty()) {
                        CronTrigger cronTrigger = (CronTrigger) triggers.get(0);
                        cronJobResp.setNextFireTime(cronTrigger.getNextFireTime());
                        cronJobResp.setPreFireTime(cronTrigger.getPreviousFireTime());
                        cronJobResp.setCronExpression(cronTrigger.getCronExpression());
                        cronJobResp.setDescription(cronTrigger.getDescription());
                        Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
                        cronJobResp.setState(triggerState.name());
                    }
                    cronJobDtoList.add(cronJobResp);
                }
            }
            return Result.success(cronJobDtoList); // 返回数据列表
        } catch (SchedulerException e) {
            LOG.error("查看定时任务失败:" + e);
            return Result.error("查看定时任务失败:调度异常");
        }
    }
}