package me.syureu.practice.spring.batch.chapter04.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.SimpleSystemProcessExitCodeMapper;
import org.springframework.batch.core.step.tasklet.SystemCommandTasklet;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@EnableBatchProcessing
@Configuration
public class SystemCommandTaskletConfiguration {

    final private JobBuilderFactory jobBuilderFactory;
    final private StepBuilderFactory stepBuilderFactory;

    public SystemCommandTaskletConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job systemCommandJob() {
        return jobBuilderFactory.get("systemCommandJob")
                .start(systemCommandStep())
                .build();
    }

    @Bean
    public Step systemCommandStep() {
        return stepBuilderFactory.get("systemCommandStep")
                .tasklet(systemCommandTasklet(null))
                .build();
    }

    @StepScope
    @Bean
    public Tasklet systemCommandTasklet(@Value("#{jobParameters['fileName']}") String fileName) {
        SystemCommandTasklet systemCommandTasklet = new SystemCommandTasklet();

        systemCommandTasklet.setCommand("touch " + fileName);
        systemCommandTasklet.setTimeout(5000);
        systemCommandTasklet.setInterruptOnCancel(true);

        systemCommandTasklet.setWorkingDirectory("/Users/user/spring-batch-practice");

        systemCommandTasklet.setSystemProcessExitCodeMapper(new SimpleSystemProcessExitCodeMapper());
        systemCommandTasklet.setTerminationCheckInterval(1000);
        systemCommandTasklet.setTaskExecutor(new SimpleAsyncTaskExecutor());
        systemCommandTasklet.setEnvironmentParams(new String[]{"JAVA_HOME=/java", "BATCH_HOME=/Users/user/spring-batch-practice"});

        return systemCommandTasklet;
    }
}
