package com.example.springX.batch.job;

import com.example.springX.batch.processor.TransactionProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Objects;

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
                .writer(new JdbcBatchItemWriter<>()).build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<com.example.springX.entity.Transaction> reader(JobParameters jobParameters) {
        FlatFileItemReader<com.example.springX.entity.Transaction> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(Objects.requireNonNull(jobParameters.getString("filePath"))));

        DefaultLineMapper<com.example.springX.entity.Transaction> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("|");
        lineTokenizer.setNames("ACCOUNT_NUMBER", "TRX_AMOUNT", "DESCRIPTION", "TRX_DATE", "TRX_TIME", "CUSTOMER_ID");

        BeanWrapperFieldSetMapper<com.example.springX.entity.Transaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(com.example.springX.entity.Transaction.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        reader.setLineMapper(defaultLineMapper);
        return reader;
    }
}
