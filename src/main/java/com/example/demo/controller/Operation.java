package com.example.demo.controller;
import com.example.demo.controller.dto.*;
import com.example.demo.service.OperationBD;
import com.example.demo.service.SQLiteConnection;
import org.springframework.web.bind.annotation.*;

@RestController
public class Operation {
    OperationBD operationBD = new SQLiteConnection();
    @PostMapping(path = "/account/SavingsAccount")
    public String createAccount(@RequestBody SavingsAccountDTO savingsAccount) {
        return operationBD.createCount(savingsAccount);
    }
    @PutMapping (path = "/account/depositMoney/depositMoneyUser")
    public String depositMoney(@RequestBody DepositMoneyUserDto depositMoneyUserDto) {
        return operationBD.depositMoney(depositMoneyUserDto);
    }
    @GetMapping(path = "/account/checkBalance/{accountNumber}")
    public String checkBalance(@PathVariable int accountNumber) {
        return operationBD.checkBalance(accountNumber);
    }
    @PutMapping (path = "/account/transferMoney/transferMoneyUser")
    public String transferMoney(@RequestBody TransferDTO transferData) {
        return operationBD.transferMoney(transferData);
    }
}
