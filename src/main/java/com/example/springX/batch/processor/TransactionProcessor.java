package com.example.springX.batch.processor;

import com.example.springX.entity.Transaction;
import org.springframework.batch.item.ItemProcessor;

public class TransactionProcessor implements ItemProcessor<Transaction, Transaction> {
    @Override
    public Transaction process(Transaction item) throws Exception {
        return item;
    }
}
