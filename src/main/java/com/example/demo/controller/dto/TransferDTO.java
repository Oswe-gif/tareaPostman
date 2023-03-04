package com.example.demo.controller.dto;
import lombok.*;

@Getter
@Setter
public class TransferDTO {
    private int accountNumberSender;
    private int moneyAmountSend;
    private int accountNumberReceiver;

}
