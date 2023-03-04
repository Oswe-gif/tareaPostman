package com.example.demo.service;
import com.example.demo.controller.dto.DepositMoneyUserDto;
import com.example.demo.controller.dto.TransferDTO;
import com.example.demo.controller.dto.SavingsAccountDTO;
import java.sql.*;


public class SQLiteConnection implements OperationBD {
    static Connection conn = null;
    static String url = "jdbc:sqlite:companydatabase";
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
        String sql = "INSERT INTO User (Name,Document) VALUES(?,?)";
        String sql2 = "INSERT INTO Account (AccountNumber,AccountAmount,CreationDate,Document) VALUES(?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, account.getOwnerName());
            pstmt.setInt(2, account.getOwnerDocument());
            pstmt.executeUpdate();
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, account.getAccountNumber());
            pstmt2.setInt(2, account.getAccountFunds());
            pstmt2.setString(3, account.getCreationDate());
            pstmt2.setInt(4, account.getOwnerDocument());
            pstmt2.executeUpdate();
            return "An account has been created: User:" + account.getAccountNumber() + " " + account.getOwnerDocument() + " "
                    + account.getCreationDate() + " " + account.getAccountFunds() + " " + account.getAccountNumber();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "check fields, data type or keys";
        }
    }
    @Override
    public String depositMoney(DepositMoneyUserDto depositMoneyUserDto) {
        String sql = "UPDATE Account SET AccountAmount = (AccountAmount + ? ) WHERE AccountNumber = ?";
        String sql2= "select AccountAmount from Account where AccountNumber = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, depositMoneyUserDto.getMoneyAmount());
            pstmt.setInt(2, depositMoneyUserDto.getAccountNumber());
            pstmt.executeUpdate();
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, depositMoneyUserDto.getAccountNumber());
            ResultSet rs=pstmt2.executeQuery();
            return "An account "+depositMoneyUserDto.getAccountNumber()+" ha been recharged. Balance: $"+rs.getInt("AccountAmount");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "check fields or data type";
        }
    }
    @Override
    public String checkBalance(int accountNumber) {
        String sql = "SELECT u.Name, a.AccountAmount FROM User u, Account a where (a.AccountNumber=? and u.Document=a.Document)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            return "Name: " + rs.getString("Name") + " Money: $" + rs.getInt("AccountAmount");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "check fields or the account does not exist";
        }
    }
    @Override
    public Integer getAccountAmount(int accountNumber) {//bien
        String sql = "SELECT AccountAmount FROM Account where AccountNumber=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("AccountAmount");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
    @Override
    public String transferMoney(TransferDTO transferData) {
        String sql = "UPDATE Account SET AccountAmount = (AccountAmount - ? ) WHERE AccountNumber = ?";
        try {
            int amountSender = getAccountAmount(transferData.getAccountNumberSender());//bien
            System.out.println(transferData.getMoneyAmountSend());
            if (amountSender >= transferData.getMoneyAmountSend()) {

                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, transferData.getMoneyAmountSend());
                pstmt.setInt(2, transferData.getAccountNumberSender());
                pstmt.executeUpdate();
                Boolean its_recharged = rechargeReceiverAccount(transferData);
                if (its_recharged) {
                    return "Account " + transferData.getAccountNumberSender() + " has transferred "
                            + transferData.getMoneyAmountSend() + " to account " + transferData.getAccountNumberReceiver();
                } else {
                    return "Transaction unable to complete";
                }
            } else {
                return "Insufficient funds, cannot make the transfer";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "check fields or data type";
        }
    }
    @Override
    public Boolean rechargeReceiverAccount(TransferDTO transferData) {
        String sql = "UPDATE Account SET AccountAmount = (AccountAmount + ? ) WHERE AccountNumber = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, transferData.getMoneyAmountSend());
            pstmt.setInt(2, transferData.getAccountNumberReceiver());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
