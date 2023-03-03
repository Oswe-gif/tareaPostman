package com.example.demo.service;

import com.example.demo.controller.dto.DepositMoneyUserDto;
import com.example.demo.controller.dto.MoneySenderDto;
import com.example.demo.controller.dto.SavingsAccountDTO;

public interface OperationBD {
    String createCount(SavingsAccountDTO account);
    String depositMoney(DepositMoneyUserDto depositMoneyUserDto);
    String checkBalance(int accountNumber);
    String transferMoney(MoneySenderDto sender);
}
