package com.example.demo.controller.dto;
import lombok.*;

@Getter
@Setter
public class SavingsAccountDTO {
    private String ownerName;
    private int ownerDocument;
    private String creationDate;
    private int accountFunds=0;
    private int accountNumber;
}
