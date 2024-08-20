package com.example.springX.service;

import com.example.springX.entity.Transaction;
import com.example.springX.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction updateTransactionDescription(Long accountNumber, String description) {
        Optional<Transaction> transaction = transactionRepository.findById(accountNumber);
        if (transaction.isPresent()) {
            Transaction trx = transaction.get();
            trx.setDescription(description);
            return transactionRepository.save(trx);
        }
        return null;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
