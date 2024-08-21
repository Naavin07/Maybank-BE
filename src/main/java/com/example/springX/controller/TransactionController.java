package com.example.springX.controller;

import com.example.springX.entity.Transaction;
import com.example.springX.rsql.CustomRsqlVisitor;
import com.example.springX.service.TransactionService;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "User", description = "API to perform User functions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping()
    @Operation(summary = "Search Transactions", description = "Access Roles: [User]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TransactionPage.class)))
    })
    @PageableAsQueryParam
    public ResponseEntity<Object> search(@Parameter(hidden = true) Pageable pageable, @RequestParam(value = "filter", required = false) String filter) {
        Specification<Transaction> specification = Specification.where(null);
        if(filter != null && !filter.isEmpty()){
            Node rootNode = new RSQLParser().parse(filter);
            specification = rootNode.accept(new CustomRsqlVisitor<>());
        }
        return new ResponseEntity<>(transactionService.search(pageable, specification), HttpStatus.OK);
    }

    @PatchMapping("/{accountNumber}")
    public ResponseEntity<Transaction> updateTransactionDescription(@PathVariable Long accountNumber, @RequestParam String description) {
        Transaction updatedTransaction = transactionService.updateTransactionDescription(accountNumber, description);
        if (updatedTransaction != null) {
            return ResponseEntity.ok(updatedTransaction);
        }
        return ResponseEntity.notFound().build();
    }
}

class TransactionPage extends PagedModel<Transaction> {
    public TransactionPage(Page<Transaction> page) {
        super(page);
    }
}
