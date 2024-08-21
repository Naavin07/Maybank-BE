package com.example.springX.batch.job;

import com.example.springX.batch.mapper.TransactionMapper;
import com.example.springX.batch.processor.TransactionProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class Transaction {
    @Autowired
    private JobRepository jobRepository;

    @Bean(name = "importTransactionJob")
    public Job importTransactionJob(Step step1) {
        return new JobBuilder("importTransactionJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(PlatformTransactionManager transactionManager, FlatFileItemReader<com.example.springX.entity.Transaction> flatFileItemReader) {
        return new StepBuilder("step1", jobRepository).<com.example.springX.entity.Transaction, com.example.springX.entity.Transaction>chunk(100, transactionManager)
                .reader(flatFileItemReader)
                .processor(new TransactionProcessor())
                .writer(new JdbcBatchItemWriter<>())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<com.example.springX.entity.Transaction> reader(@Value("#{jobParameters['filePath']}") String filePath) {
        FlatFileItemReader<com.example.springX.entity.Transaction> reader = new FlatFileItemReader<>();
        Resource resource = new FileSystemResource(filePath);
        reader.setResource(resource);
        reader.setLinesToSkip(1);

        DefaultLineMapper<com.example.springX.entity.Transaction> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("|");
        lineTokenizer.setNames("ACCOUNT_NUMBER", "TRX_AMOUNT", "DESCRIPTION", "TRX_DATE", "TRX_TIME", "CUSTOMER_ID");

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(new TransactionMapper());
        reader.setLineMapper(defaultLineMapper);
        return reader;
    }
}
