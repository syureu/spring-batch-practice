package me.syureu.practice.spring.batch.chapter04.joblistener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

public class JobLoggerListener2 {

    private static String START_MESSAGE = "%s is beginning execution%n";
    private static String END_MESSAGE = "%s has completed with the status %s%n";

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        System.out.printf(START_MESSAGE, jobExecution.getJobInstance().getJobName());
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        System.out.printf(END_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
    }
}
