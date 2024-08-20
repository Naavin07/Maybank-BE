package com.example.springX.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/import")
@Tag(name = "User", description = "API to perform import files")
public class ImportController {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job importTransactionJob;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Upload Transaction file", description = "Access Roles: [User]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content")
    })
    ResponseEntity<Object> importTransactionFile(@RequestPart("file") MultipartFile file) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("filePath", file.getResource().getFile().getAbsolutePath())
                .toJobParameters();
        jobLauncher.run(importTransactionJob, jobParameters);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

