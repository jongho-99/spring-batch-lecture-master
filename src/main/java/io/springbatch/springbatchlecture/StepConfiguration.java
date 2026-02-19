package io.springbatch.springbatchlecture;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StepConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final ExecutionContextConfiguration1 executionContextConfiguration1;
    private final ExecutionContextConfiguration2 executionContextConfiguration2;
    private final ExecutionContextConfiguration3 executionContextConfiguration3;
    private final ExecutionContextConfiguration4 executionContextConfiguration4;


    @Bean
    public Job job() {
        return jobBuilderFactory.get("jobTest2")
                .start(step01())
                .next(step02())
                .next(step03())
                .next(step04())
                .build();
    }


    public Step step01() {
        return stepBuilderFactory.get("step01")
                .tasklet(executionContextConfiguration1)
                .build();
    }

    public Step step02() {
        return stepBuilderFactory.get("step02")
                .tasklet(executionContextConfiguration2)
                .build();
    }

    public Step step03() {
        return stepBuilderFactory.get("step03")
                .tasklet(executionContextConfiguration3)
                .build();
    }

    public Step step04() {
        return stepBuilderFactory.get("step04")
                .tasklet(executionContextConfiguration4)
                .build();
    }
}
