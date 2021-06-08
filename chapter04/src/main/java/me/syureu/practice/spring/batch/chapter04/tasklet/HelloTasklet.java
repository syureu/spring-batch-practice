package me.syureu.practice.spring.batch.chapter04.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class HelloTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        chunkContext.getStepContext().getStepExecution().getExecutionContext().put("name", Math.random());
        return RepeatStatus.FINISHED;
    }
}
