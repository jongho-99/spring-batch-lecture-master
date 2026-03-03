package io.springbatch.springbatchlecture;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class StepConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job batchJob() {
        return jobBuilderFactory.get("batchJob12")
//                .incrementer(new RunIdIncrementer())
                .start(chunkStep())
//                .next(step02())
//                .next(chunkStep())
                .build();
    }

    @Bean
    public Step step01() {
        return stepBuilderFactory.get("step01")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("[STEP01] stepContribution = " + stepContribution + ", chunkContext = " + chunkContext);
                        return RepeatStatus.FINISHED;
                    }
                })
                .allowStartIfComplete(true)
                .build();
    }
    @Bean
    public Step step02() {
        return stepBuilderFactory.get("step02")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("[STEP02] stepContribution = " + stepContribution + ", chunkContext = " + chunkContext);
                        throw new RuntimeException("step02 was failed");
//                        return null;
                    }
                })
//                .startLimit(4)
                .build();
    }


    @Bean
    public Step chunkStep() {
        return stepBuilderFactory.get("chunkStep")
                .<String, String>chunk(5)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }

    private CustomItemStreamReader itemReader() {
        List<String> items = new ArrayList<>(10);

        for(int i = 0; i<10; i++) {
            items.add(String.valueOf(i));
        }
        return new CustomItemStreamReader(items);
    }

    private CustomItemStreamWriter itemWriter() {
        return new CustomItemStreamWriter();
    }



}
