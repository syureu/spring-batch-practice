package me.syureu.practice.spring.batch.chapter04;

import me.syureu.practice.spring.batch.chapter04.joblistener.JobLoggerListener;
import me.syureu.practice.spring.batch.chapter04.jobparameter.DailyJobTimeStamper;
import me.syureu.practice.spring.batch.chapter04.validator.ParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Date;


@EnableBatchProcessing
@SpringBootApplication
public class Chapter04Application {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public Chapter04Application(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public CompositeJobParametersValidator validator() {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();

        DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator(new String[]{"fileName"}, new String[]{"name", "currentDate", "run.id"});
        defaultJobParametersValidator.afterPropertiesSet();

        validator.setValidators(Arrays.asList(new ParameterValidator(), defaultJobParametersValidator));
        return validator;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(helloWorldTaskLet(null, null, null, null))
                .build();
    }

    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("basicJob")
                .start(step1())
                .validator(validator())
                .incrementer(new DailyJobTimeStamper())
                .listener(new JobLoggerListener())
                .build();
    }

    @StepScope
    @Bean
    public Tasklet helloWorldTaskLet(
            @Value("#{jobParameters['name']}") String name,
            @Value("#{jobParameters['fileName']}") String fileName,
            @Value("#{jobParameters['currentDate']}") Date currentDate,
            @Value("#{jobParameters['run.id']}") Long id
    ) {
        return (stepContribution, chunkContext) -> {
            System.out.printf("Hello, %s!%n", name);
            System.out.printf("fileName = %s%n", fileName);
            System.out.printf("currentDate = %s%n", currentDate);
            System.out.printf("run.id = %d%n", id);
            return RepeatStatus.FINISHED;
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Chapter04Application.class, args);
    }

}
