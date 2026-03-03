package io.springbatch.springbatchlecture;

import org.springframework.batch.item.ItemWriter;

import java.io.Serializable;
import java.util.List;

public class CustomItemWriter implements ItemWriter<Customer> {

    @Override
    public void write(List<? extends Customer> items) throws Exception {
        items.forEach(item -> System.out.println("item = " + item));
    }
}
