package com.example.springX.service;

import com.example.springX.entity.Transaction;
import com.example.springX.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
            trx.setVersion(trx.getVersion()+1);
            return transactionRepository.save(trx);
        }
        return null;
    }

    public Page<Transaction> search(Pageable pageable, Specification<Transaction> transactionSpecification) {
        return transactionRepository.findAll(transactionSpecification, pageable);
    }
}
