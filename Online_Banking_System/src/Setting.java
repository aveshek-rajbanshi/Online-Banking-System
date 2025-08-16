import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Setting {
    private final Connection conn;
    private final Scanner scanner;

    public Setting(Connection conn, Scanner scanner){
        this.conn = conn;
        this.scanner = scanner;
    }

    public void myDetails(String useEmail){
        String bankDetail_query = "SELECT user.user_FullName, user.user_Email, user.user_Password, account.account_number, account.fullName, account.email, account.balance, account.security_pin"+
                             " from user"+
                             " JOIN account"+
                             " ON user.user_Email = account.email WHERE user_Email = ?;";

        try{
            PreparedStatement getBank_Detail = conn.prepareStatement(bankDetail_query);
            getBank_Detail.setString(1, useEmail);
            ResultSet rs = getBank_Detail.executeQuery();
            if(rs.next()){
                String userName = rs.getString("user_FullName");
                String userEmail = rs.getString("user_Email");
                String userPassword = rs.getString("user_Password");
                BigInteger customerBank_AccountNumber = rs.getBigDecimal("account_number").toBigInteger();
                String customerName = rs.getString("fullName");
                String customerEmail = rs.getString("email");
                Double bankBalance = rs.getDouble("balance");
                String securityPIN = rs.getString("security_pin");

                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t**  My Details  **\n");
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t -- PERSONAL CREDENTIALS --\n");
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tFull Name: "+userName);
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tEmail: "+userEmail);
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tPassword: "+userPassword);
                System.out.println("\n");
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t-- BANK CREDENTIALS --\n");
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tCustomer Bank Account: "+customerBank_AccountNumber);
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tCustomer Name: "+customerName);
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tCustomer Email: "+customerEmail);
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tsecurity PIN: "+securityPIN);
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tCurrent Balance: "+bankBalance);
                System.out.println("\n");



            }else{
                System.out.println("\n>>  Oops! Failed to Load Details.");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void changePassword(String userEmail){
        System.out.println("\n**  Change Password  **");
        System.out.print("Enter Your Old Password: ");
        String oldPassword = scanner.next();
        System.out.print("Enter Your New Password: ");
        String newPassword = scanner.next();

        String check_oldPassword = "SELECT user_Password from user WHERE user_Password = ?;";
        String changePassword_query = "UPDATE user SET user_Password = ? WHERE user_Email = ?;";

        try{
            PreparedStatement checkPassword = conn.prepareStatement(check_oldPassword);
            checkPassword.setString(1, oldPassword);
            ResultSet rs = checkPassword.executeQuery();
            if(rs.next()){
                try{
                    PreparedStatement updatePassword = conn.prepareStatement(changePassword_query);
                    updatePassword.setString(1, newPassword);
                    updatePassword.setString(2, userEmail);
                    int rowAffected2 = updatePassword.executeUpdate();
                    if(rowAffected2 > 0){
                        System.out.println("\n>>  Password Changed Successfully.");
                    }
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }else{
                System.out.println("\n>>  Old Password Not Match. Please! Try Again.");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void changeSecurity_Pin(String userEmail){
        System.out.println("\n**  Change Password  **");
        System.out.print("Enter Your Old Security PIN: ");
        String old_securityPIN = scanner.next();
        System.out.print("Enter Your New Security PIN: ");
        String new_securityPIN = scanner.next();

        String checkPIN_query = "SELECT email from account WHERE email = ? AND security_pin = ?;";
        String update_securityPIN = "UPDATE account SET security_pin = ? WHERE email = ?;";

        try{
           PreparedStatement checkPIN = conn.prepareStatement(checkPIN_query);
           checkPIN.setString(1, userEmail);
           checkPIN.setString(2, old_securityPIN);
           ResultSet rs = checkPIN.executeQuery();
           if(rs.next()){
               try{
                   PreparedStatement updatePIN = conn.prepareStatement(update_securityPIN);
                   updatePIN.setString(1, new_securityPIN);
                   updatePIN.setString(2, userEmail);
                   int rowAffected = updatePIN.executeUpdate();
                   if(rowAffected > 0){
                       System.out.println("\n>>  Security Pin Changed Successfully.");
                   }
               }catch (SQLException e){
                   System.out.println(e.getMessage());
               }

           }else {
               System.out.println(">>  Old Security Pin Not Match. Please! Try Again.");
           }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
