package com.matheusluizago.libraryapi.repository;

import com.matheusluizago.libraryapi.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class TransactionsTest {

    @Autowired
    TransactionService transactionService;

    @Test
    //No final de cada transação é feito um commit ou rollback
    //Ele automaticamente da commit se tiver certo, ou rollback se tiver rolado exception
    void simpleTransaction(){
        transactionService.execute();
    }
}
