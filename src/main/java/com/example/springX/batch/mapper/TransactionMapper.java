package com.example.springX.batch.mapper;

import com.example.springX.entity.Transaction;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class TransactionMapper implements FieldSetMapper<Transaction> {
    @Override
    public Transaction mapFieldSet(FieldSet fieldSet) {

        try {
            LocalDate date = LocalDate.ofInstant(new SimpleDateFormat("yyyy-MM-dd").parse(fieldSet.readString("TRX_DATE")).toInstant(), ZoneId.systemDefault());
            LocalTime time = LocalTime.ofInstant(new SimpleDateFormat("HH:mm:ss").parse(fieldSet.readString("TRX_TIME")).toInstant(), ZoneId.systemDefault());

            return Transaction.builder()
                    .accountNumber(fieldSet.readString("ACCOUNT_NUMBER"))
                    .trxAmount(fieldSet.readBigDecimal("TRX_AMOUNT"))
                    .description(fieldSet.readString("DESCRIPTION"))
                    .trxDate(date)
                    .trxTime(time)
                    .customerId(fieldSet.readString("CUSTOMER_ID"))
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
