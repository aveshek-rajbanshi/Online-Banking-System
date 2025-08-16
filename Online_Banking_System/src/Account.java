import java.math.BigInteger;
import java.sql.*;
import java.util.Scanner;

public class Account {

    private final Connection conn;
    private final Scanner scanner;
    public Account(Connection conn, Scanner scanner){
        this.conn = conn;
        this.scanner = scanner;
    }

    public void create_Account(String userEmail){
        scanner.nextLine();
        System.out.println("\n**** Open an Bank Account at Nabil Bank Ltd. ****");
        System.out.print("Enter Your Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter Your Balance: ");
        Double balance = scanner.nextDouble();
        System.out.print("Enter Your Security Pin: ");
        String securityPIN = scanner.next();
        scanner.nextLine();

        String userAccount_Query = "INSERT INTO account(fullName, email, balance, security_pin) VALUES(?, ?, ?, ?);";
        try{
            PreparedStatement preparedStmt = conn.prepareStatement(userAccount_Query);
            preparedStmt.setString(1, fullName);
            preparedStmt.setString(2, userEmail);
            preparedStmt.setDouble(3, balance);
            preparedStmt.setString(4, securityPIN);
            int rowAffected = preparedStmt.executeUpdate();
            if(rowAffected > 0){
                System.out.println("\n>>  Hi! "+fullName+", Your Account Created Successfully.");
            }else{
                System.out.println("\n>>  Oops! Failed to Created Account.");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public BigInteger getAccount_Number(String userEmail){
        String getAccountNumber_Query = "SELECT account_number from account where email = ?;";
        if(account_exist(userEmail)){
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(getAccountNumber_Query);
                preparedStmt.setString(1, userEmail);
                ResultSet rs = preparedStmt.executeQuery();
                if(rs.next()){
                    return rs.getBigDecimal("account_number").toBigInteger();
                }else{
                    return null;
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public boolean account_exist(String userEmail){
        String userCheck = "SELECT account_number FROM account WHERE email = ?;";
        try{
            PreparedStatement preparedStmt = conn.prepareStatement(userCheck);
            preparedStmt.setString(1, userEmail);
            ResultSet rs = preparedStmt.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
