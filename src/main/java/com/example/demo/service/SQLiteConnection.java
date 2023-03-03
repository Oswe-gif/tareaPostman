package com.example.demo.service;

import com.example.demo.controller.dto.DepositMoneyUserDto;
import com.example.demo.controller.dto.TransferDTO;
import com.example.demo.controller.dto.SavingsAccountDTO;

import java.sql.*;

public class SQLiteConnection implements OperationBD{

    static Connection conn = null;
    static String url = "jdbc:sqlite:ourdatabase";
    public static void connect() {


        try {
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }



    @Override
    public String createCount(SavingsAccountDTO account) {

        String sql = "INSERT INTO users (Name,Document,CreationDate,AccountAmount,AccountNumber) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, account.ownerName);
            pstmt.setInt(2, account.ownerDocument);
            pstmt.setString(3, account.creationDate);
            pstmt.setInt(4, account.accountFunds);
            pstmt.setInt(5, account.accountNumber);
            pstmt.executeUpdate();
            return "An account has been created: User:"+account.accountNumber+" "+ account.ownerDocument+ " "+account.creationDate+" "+account.accountFunds+" "+account.accountNumber;


        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return "check fields or data type";
        }

    }

    @Override
    public String depositMoney(DepositMoneyUserDto depositMoneyUserDto) {
        
        String sql = "UPDATE users SET AccountAmount = (AccountAmount + ? ) WHERE AccountNumber = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, depositMoneyUserDto.moneyAmount);
            pstmt.setInt(2, depositMoneyUserDto.accountNumber);
            pstmt.executeUpdate();
            return "An account has been recharged ";


        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return "check fields or data type";
        }

    }

    @Override
    public String checkBalance(int accountNumber) {
        String sql = "SELECT Name, AccountAmount FROM Users where AccountNumber=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountNumber);
            ResultSet rs  = pstmt.executeQuery();
            return "Name: "+rs.getString("Name") +" Money: $" +rs.getInt("AccountAmount");

        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return "check fields or the account does not exist";
        }
    }

    @Override
    public Integer getAccountAmount(int accountNumber) {
        String sql = "SELECT AccountAmount FROM Users where AccountNumber=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountNumber);
            ResultSet rs  = pstmt.executeQuery();
            return rs.getInt("AccountAmount");

        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    @Override
    public String transferMoney(TransferDTO transferData) {
        String sql = "UPDATE users SET AccountAmount = (AccountAmount - ? ) WHERE AccountNumber = ? AND SET AccountAmount = (AccountAmount + ? ) WHERE AccountNumber = ?";
        try {
            int amountSender=getAccountAmount(transferData.accountNumberSender);
            if (amountSender>transferData.MoneyAmounttoSend) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, transferData.MoneyAmounttoSend);
                pstmt.setInt(2, transferData.accountNumberSender);
                pstmt.setInt(3, transferData.MoneyAmounttoSend);
                pstmt.setInt(4, transferData.accountNumberReceiver);
                pstmt.executeUpdate();
                return "Account "+transferData.accountNumberSender+" has transferred "+transferData.MoneyAmounttoSend+" to account "+transferData.accountNumberReceiver;
            }
           
            else{
                return "Insufficient funds, cannot make the transfer";
            }

        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return "check fields or data type";
        }
    }
}
