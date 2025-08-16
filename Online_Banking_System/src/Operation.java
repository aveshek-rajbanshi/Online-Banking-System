import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Set;

public class Operation{
    private final Scanner scanner;
    private final Connection conn;
    private final Setting setting;
    private BigInteger userAccountNumber = BigInteger.ZERO;
    private String userEmail;

    public Operation(Connection conn, Scanner scanner, Setting setting){
        this.conn = conn;
        this.scanner = scanner;
        this.setting = setting;
    }

    public void atmOperation(BigInteger userAccountNumber, String userEmail){
        this.userAccountNumber = userAccountNumber;
        this.userEmail = userEmail;


            int choice3;
            do {
                System.out.println("\n****  Online Nabil Bank Operation  ****");
                System.out.println("\t\t 1. Debit Balance");
                System.out.println("\t\t 2. Credit Balance");
                System.out.println("\t\t 3. Transfer Balance");
                System.out.println("\t\t 4. Check Balance");
                System.out.println("\t\t 5. Setting");
                System.out.println("\t\t 6. Logout");
                System.out.println();
                System.out.print("--> Enter your choice: ");
                choice3 = scanner.nextInt();

                switch (choice3) {
                    case 1:
                        debitBalance(userAccountNumber);
                        break;
                    case 2:
                        creditBalance(userAccountNumber);
                        break;
                    case 3:
                        transferBalance(userAccountNumber);
                        break;
                    case 4:
                        checkBalance(userAccountNumber);
                        break;
                    case 5:
                        int settingChoice;
                        do {
                            System.out.println("\n*****  Settings  *****");
                            System.out.println("\t1. My Profile");
                            System.out.println("\t2. Change Password");
                            System.out.println("\t3. Change Security PIN");
                            System.out.println("\t0. Back");
                            System.out.println();
                            System.out.print("--> Enter your choice: ");
                            settingChoice = scanner.nextInt();
                            scanner.nextLine();  // consume newline

                            switch (settingChoice) {
                                case 1:
                                    setting.myDetails(userEmail);
                                    break;
                                case 2:
                                    setting.changePassword(userEmail);
                                    break;
                                case 3:
                                    setting.changeSecurity_Pin(userEmail);
                                    break;
                                case 0:
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please! enter correct choice.");
                                    break;
                            }
                        } while (settingChoice != 0);
                    break;
                    case 6:
                        break;
                    default:
                        System.out.println("\n>>  Invalid Input. Please! enter valid choice.");
                        break;
                }
            }while (choice3 != 6);

    }

    //Debit Method Operation
    public void debitBalance(BigInteger userAccountNumber){
        System.out.print("\n**  Debit Money  **\n");
        System.out.print("Enter Your Amount: ");
        Double withdrawAmount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Your Security PIN: ");
        String securityPIN = scanner.next();
        scanner.nextLine();

        try{
            String userAccount_query = "SELECT * FROM account WHERE account_number = ? AND security_pin = ?;";
            String debit_query = "UPDATE account SET balance = balance  - ? WHERE account_number = ?;";

            conn.setAutoCommit(false);
            PreparedStatement selectStatement = conn.prepareStatement(userAccount_query);
            selectStatement.setLong(1, userAccountNumber.longValue());
            selectStatement.setString(2, securityPIN);
            ResultSet rs = selectStatement.executeQuery();
            if(rs.next()){
                Double currentBalance = rs.getDouble("balance");
                if(withdrawAmount <= currentBalance){
                    PreparedStatement updateStatement = conn.prepareStatement(debit_query);
                    updateStatement.setDouble(1, withdrawAmount);
                    updateStatement.setLong(2, userAccountNumber.longValue());
                    int rowAffected = updateStatement.executeUpdate();
                    if(rowAffected > 0){
                        System.out.println("\n>>  Rs."+withdrawAmount+" debited successfully.");
                        conn.commit();
                        conn.setAutoCommit(true);
                    }else{
                        System.out.println("\n>> Failed to debit amount rs."+withdrawAmount+" , Please! check your Balance.");
                        conn.rollback();
                    }
                }else{
                    System.out.println("\n>> Insufficient Balance. Please! check your balance.");
                    conn.rollback();
                }
            }else{
                System.out.println("\n>>  Invalid Account Number & Security Pin Number.");
                conn.rollback();
            }
            conn.setAutoCommit(true);
        }catch (SQLException e){
            try {
                conn.rollback(); // ️Rollback on any error
                System.out.println("Transaction rolled back due to an error.");
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.out.println("Error: " + e.getMessage());
        }
    }


    //Credit Method Operation
    public void creditBalance(BigInteger userAccountNumber){
        System.out.print("\n**  Credit Money  **\n");
        System.out.print("Enter Your Amount: ");
        Double depositeAmount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Your Security PIN: ");
        String securityPIN = scanner.next();
        scanner.nextLine();

        try{
            String userAccount_query = "SELECT * FROM account WHERE account_number = ? AND security_pin = ?;";
            String debit_query = "UPDATE account SET balance = balance  + ? WHERE account_number = ?;";

            conn.setAutoCommit(false);
            PreparedStatement selectStatement = conn.prepareStatement(userAccount_query);
            selectStatement.setLong(1, userAccountNumber.longValue());
            selectStatement.setString(2, securityPIN);
            ResultSet rs = selectStatement.executeQuery();
            if(rs.next()){
                if(depositeAmount >= 0){
                    PreparedStatement updateStatement = conn.prepareStatement(debit_query);
                    updateStatement.setDouble(1, depositeAmount);
                    updateStatement.setLong(2, userAccountNumber.longValue());
                    int rowAffected = updateStatement.executeUpdate();
                    if(rowAffected > 0){
                        System.out.println("\n>>  Rs."+depositeAmount+" credited successfully.");
                        conn.commit();
                        conn.setAutoCommit(true);
                    }else{
                        System.out.println("\n>> Failed to credit amount rs."+depositeAmount+" , Please! check your Balance.");
                        conn.rollback();
                    }
                }else{
                    System.out.println("\n>> Balance Amount Incorrect.");
                    conn.rollback();
                }
            }else{
                System.out.println("\n>>  Invalid Account Number & Security Pin Number.");
                conn.rollback();
            }
            conn.setAutoCommit(true);
        }catch (SQLException e){
            try {
                conn.rollback(); // Rollback on any error
                System.out.println("Transaction rolled back due to an error.");
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.out.println("Error: " + e.getMessage());
        }
    }


    //Transfer Method Operation
    public void transferBalance(BigInteger senderAccountNumber){
        String checkAccount = "SELECT * FROM account WHERE account_number = ?;";
        String debit_query = "UPDATE account SET balance = balance - ? WHERE account_number = ?;";
        String credit_query = "UPDATE account SET balance = balance + ? WHERE account_number = ?;";

        System.out.println("\n**  Transfer Money  **");
        System.out.print("Enter Receiver Account Number: ");
        BigInteger receiverAccountNumber = scanner.nextBigInteger();
        scanner.nextLine();
        System.out.print("Enter Transfer Amount: ");
        Double transferAmount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security PIN: ");
        String securityPIN = scanner.next();
        scanner.nextLine();

        try{
            conn.setAutoCommit(false);
            PreparedStatement selectStatement = conn.prepareStatement(checkAccount);
            selectStatement.setLong(1, receiverAccountNumber.longValue());
            ResultSet rs = selectStatement.executeQuery();
            if(rs.next()){
                //sender debit balance transaction
                PreparedStatement debitStatement = conn.prepareStatement(debit_query);
                debitStatement.setDouble(1, transferAmount);
                debitStatement.setLong(2, senderAccountNumber.longValue());
                int debit_AmountUpdated = debitStatement.executeUpdate();

                //receiver credit balance transaction
                PreparedStatement creditStatement = conn.prepareStatement(credit_query);
                creditStatement.setDouble(1, transferAmount);
                creditStatement.setLong(2, receiverAccountNumber.longValue());
                int credit_AmountUpdated = creditStatement.executeUpdate();

                if(debit_AmountUpdated > 0 && credit_AmountUpdated > 0){
                    System.out.println("\n>>  Rs."+transferAmount+" Transfer Successfully.");
                    conn.commit();
                }else{
                    System.out.println("\n>>  Failed to Transfer rs."+transferAmount);
                    conn.rollback();
                }

            }else{
                System.out.println("\n>>  Receiver Account Number "+receiverAccountNumber+" Not Found.");
                conn.rollback();
            }
            conn.setAutoCommit(true);
        }catch (SQLException e){
            try {
                conn.rollback(); // Rollback on any error
                System.out.println("Transaction rolled back due to an error.");
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Check User Account Balance Method Operation
    public void checkBalance(BigInteger userAccountNumber){
        try {
            String checkBalance = "SELECT * FROM account WHERE account_number = ?;";
            PreparedStatement selectStatement = conn.prepareStatement(checkBalance);
            selectStatement.setLong(1, userAccountNumber.longValue());
            ResultSet rs = selectStatement.executeQuery();
            if(rs.next()){
                Double currentBalance = rs.getDouble("balance");
                System.out.println("\n>>  Your Current Balance is rs."+currentBalance);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }



}
