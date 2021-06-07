package me.syureu.practice.spring.batch.chapter04.joblistener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobLoggerListener implements JobExecutionListener {

    private static String START_MESSAGE = "%s is beginning execution%n";
    private static String END_MESSAGE = "%s has completed with the status %s%n";

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.printf(START_MESSAGE, jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.printf(END_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
    }
}
