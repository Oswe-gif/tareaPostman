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
            pstmt.setString(1, account.ownerName);
            pstmt.setInt(2, account.ownerDocument);
            pstmt.executeUpdate();
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, account.accountNumber);
            pstmt2.setInt(2, account.accountFunds);
            pstmt2.setString(3, account.creationDate);
            pstmt2.setInt(4, account.ownerDocument);
            pstmt2.executeUpdate();
            return "An account has been created: User:" + account.accountNumber + " " + account.ownerDocument + " "
                    + account.creationDate + " " + account.accountFunds + " " + account.accountNumber;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "check fields or data type";
        }
    }
    @Override
    public String depositMoney(DepositMoneyUserDto depositMoneyUserDto) {
        String sql = "UPDATE Account SET AccountAmount = (AccountAmount + ? ) WHERE AccountNumber = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, depositMoneyUserDto.moneyAmount);
            pstmt.setInt(2, depositMoneyUserDto.accountNumber);
            pstmt.executeUpdate();
            return "An account has been recharged ";
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
    public Integer getAccountAmount(int accountNumber) {
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
            int amountSender = getAccountAmount(transferData.accountNumberSender);
            if (amountSender >= transferData.MoneyAmounttoSend) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, transferData.MoneyAmounttoSend);
                pstmt.setInt(2, transferData.accountNumberSender);
                pstmt.executeUpdate();
                Boolean its_recharged = rechargeReceiverAccount(transferData);
                if (its_recharged) {
                    return "Account " + transferData.accountNumberSender + " has transferred "
                            + transferData.MoneyAmounttoSend + " to account " + transferData.accountNumberReceiver;
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
            pstmt.setInt(1, transferData.MoneyAmounttoSend);
            pstmt.setInt(2, transferData.accountNumberReceiver);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
