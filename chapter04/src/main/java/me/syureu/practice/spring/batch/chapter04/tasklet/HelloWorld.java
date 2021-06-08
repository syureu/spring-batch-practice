package me.syureu.practice.spring.batch.chapter04.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class HelloWorld implements Tasklet {

    private static final String HELLO_WORLD = "Hello, %s%n";

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String name = (String) chunkContext.getStepContext()
                .getJobParameters()
                .get("name");

        ExecutionContext jobContext = chunkContext.getStepContext()
                .getStepExecution()
                .getExecutionContext();

        jobContext.put("user.name", name);

        System.out.printf(HELLO_WORLD, name);
        return RepeatStatus.FINISHED;
    }
}
