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

import java.util.Arrays;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class StepConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job batchJob() {
        return jobBuilderFactory.get("batchJob1")
                .incrementer(new RunIdIncrementer())
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


//    @Bean
//    public Step chunkStep() {
//        return stepBuilderFactory.get("chunkStep")
//                .<String, String>chunk(3)
//                .reader(new ListItemReader<>(Arrays.asList("item1","item2","item3","item4","item5")))
//                .processor(new ItemProcessor<String, String>() {
//                    @Override
//                    public String process(String item) throws Exception {
//                        return item.toUpperCase();
//                    }
//                })
//                .writer(new ItemWriter<String>() {
//                    @Override
//                    public void write(List<? extends String> items) throws Exception {
//                        items.forEach(item -> System.out.println("items = " + item));
//                    }
//                })
//                .build();
//    }

    @Bean
    public Step chunkStep() {
        return stepBuilderFactory.get("chunkStep")
                .<Customer, Customer>chunk(3)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    private ItemWriter<? super Customer> itemWriter() {
        return new CustomItemWriter();
    }

    private ItemProcessor<? super Customer, ? extends Customer> itemProcessor() {
        return new CustomItemProcessor();
    }

    @Bean
    public ItemReader<Customer> itemReader() {
        return new CustomItemReader(Arrays.asList(new Customer("user01"),
                new Customer("user02"),
                new Customer("user03")));
    }

}
