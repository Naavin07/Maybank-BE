package com.example.springX.batch.reader;

import com.example.springX.entity.Transaction;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class TransactionPsvReader implements ItemReader<Transaction> {
    @Override
    public Transaction read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Transaction transaction = new Transaction();
        return transaction;
    }
}
