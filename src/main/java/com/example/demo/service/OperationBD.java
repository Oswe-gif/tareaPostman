package com.example.demo.service;

import com.example.demo.controller.dto.DepositMoneyUserDto;
import com.example.demo.controller.dto.TransferDTO;
import com.example.demo.controller.dto.SavingsAccountDTO;

public interface OperationBD {
    String createCount(SavingsAccountDTO account);
    String depositMoney(DepositMoneyUserDto depositMoneyUserDto);
    String checkBalance(int accountNumber);
    String transferMoney(TransferDTO transferData);
    Integer getAccountAmount(int accountNumber);
}
