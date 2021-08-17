package me.syureu.practice.spring.batch.chapter04.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class MethodInvokingTaskletConfiguration {

    final private JobBuilderFactory jobBuilderFactory;
    final private StepBuilderFactory stepBuilderFactory;

    public MethodInvokingTaskletConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job methodInvokingJob() {
        return this.jobBuilderFactory.get("methodInvokingJob")
                .start(methodInvokingStep())
                .build();
    }

    @Bean
    public Step methodInvokingStep() {
        return this.stepBuilderFactory.get("methodInvokingStep")
                .tasklet(methodInvokingTasklet(null))
                .build();
    }

    @StepScope
    @Bean
    public MethodInvokingTaskletAdapter methodInvokingTasklet(@Value("#{jobParameters['fileName']}") String file) {
        MethodInvokingTaskletAdapter methodInvokingTaskletAdapter = new MethodInvokingTaskletAdapter();

        methodInvokingTaskletAdapter.setTargetObject(new CustomService());
        methodInvokingTaskletAdapter.setTargetMethod("serviceMethod");
        methodInvokingTaskletAdapter.setArguments(new String[]{file});

        return methodInvokingTaskletAdapter;
    }

    public static class CustomService {
        public void serviceMethod(String fileName) {
            System.out.println("Service method was called");
            System.out.println("fileName is " + fileName);
        }
    }
}
