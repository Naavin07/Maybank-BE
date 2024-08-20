package com.example.springX.controller;

import com.example.springX.entity.Transaction;
import com.example.springX.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "User", description = "API to perform User functions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping()
    public List<Transaction> getListTransactions() {
        return transactionService.getAllTransactions();
    }

    @PutMapping("/{accountNumber}")
    public ResponseEntity<Transaction> updateTransactionDescription(
            @PathVariable Long accountNumber, @RequestParam String description) {
        Transaction updatedTransaction = transactionService.updateTransactionDescription(accountNumber, description);
        if (updatedTransaction != null) {
            return ResponseEntity.ok(updatedTransaction);
        }
        return ResponseEntity.notFound().build();
    }
}
