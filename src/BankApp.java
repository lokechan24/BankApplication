import java.io.*;
import java.util.*;

// Bank Account class;
class BankAccount {
    private String accountNumber;
    private String accountName;
    private String password;
    private double amount;

    public void setDetails(String accountNumber, String accountName, String password, double amount) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.password = password;
        this.amount = amount;
    }

    public boolean loginAccount(String accountNumber, String accountName, String password) {
        return this.accountNumber.equals(accountNumber)
                && this.accountName.equals(accountName)
                && this.password.equals(password);
    }

    public void depositAmount(double deposit) {
        if (deposit > 0) {
            this.amount += deposit;
            System.out.println("₹" + deposit + " deposited successfully.");
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdrawAmount(double withdraw) {
        if (withdraw > this.amount) {
            System.out.println("Insufficient Balance...");
        } else if (withdraw <= 0) {
            System.out.println("Invalid withdrawal amount.");
        } else {
            this.amount -= withdraw;
            System.out.println("₹" + withdraw + " withdrawn successfully.");
        }
    }

    public void balanceAmount() {
        System.out.println("Your Current Balance: ₹" + this.amount);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String toFileString() {
        return accountNumber + "," + accountName + "," + password + "," + amount;
    }

    public double getAmount() {
        return amount;
    }
}
// Main class and method;
public class BankApp {
    private static final String filePath = "src/loke.txt";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Map<String, BankAccount> accounts = new HashMap<>();

        // Load existing accounts from file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String str;
            while ((str = reader.readLine()) != null) {
                String[] parts = str.split(",");
                if (parts.length == 4) {
                    BankAccount acc = new BankAccount();
                    acc.setDetails(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
                    accounts.put(parts[0], acc);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        int choice;
        do {
            System.out.println("\n=== Bank Application ===");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scan.nextInt();
            scan.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter your Account Number: ");
                    String accountNumber = scan.nextLine();
                    System.out.print("Enter your Account Name: ");
                    String accountName = scan.nextLine();
                    System.out.print("Enter your 4-digit Password: ");
                    String password = scan.nextLine();

                    BankAccount account = accounts.get(accountNumber);

                    if (account != null && account.loginAccount(accountNumber, accountName, password)) {
                        System.out.println("Login Successful!");
                        int loginChoice;
                        do {
                            System.out.println("\n--- Transaction Menu ---");
                            System.out.println("1. Deposit");
                            System.out.println("2. Withdraw");
                            System.out.println("3. Check Balance");
                            System.out.println("4. Logout");
                            System.out.print("Enter your choice: ");
                            loginChoice = scan.nextInt();

                            switch (loginChoice) {
                                case 1:
                                    System.out.print("Enter deposit amount: ");
                                    double deposit = scan.nextDouble();
                                    account.depositAmount(deposit);
                                    break;
                                case 2:
                                    System.out.print("Enter withdrawal amount: ");
                                    double withdraw = scan.nextDouble();
                                    account.withdrawAmount(withdraw);
                                    break;
                                case 3:
                                    account.balanceAmount();
                                    break;
                                case 4:
                                    System.out.println("Logged out.");
                                    break;
                                default:
                                    System.out.println("Invalid choice.");
                            }
                        } while (loginChoice != 4);

                        // After transactions, update file
                        saveAccountsToFile(accounts);
                    } else {
                        System.out.println("Login Failed. Incorrect credentials or account not found.");
                    }
                    break;

                case 2:
                    System.out.print("Enter your Account Number: ");
                    String accountNumberSignUp = scan.nextLine();

                    if (accounts.containsKey(accountNumberSignUp)) {
                        System.out.println("Account already exists. Please login.");
                        break;
                    }

                    System.out.print("Enter your Account Name: ");
                    String accountNameSignUp = scan.nextLine();
                    System.out.print("Enter your 4-digit Password: ");
                    String passwordSignUp = scan.nextLine();

                    BankAccount newAccount = new BankAccount();
                    newAccount.setDetails(accountNumberSignUp, accountNameSignUp, passwordSignUp, 0.0);
                    accounts.put(accountNumberSignUp, newAccount);

                    System.out.println("Account created successfully!");

                    // Save new account to file
                    saveAccountsToFile(accounts);
                    break;

                case 3:
                    System.out.println("Thank you for using the Bank App.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 3);

        scan.close();
    }

    // Save all accounts back to file
    private static void saveAccountsToFile(Map<String, BankAccount> accounts) {
        try (FileWriter writer = new FileWriter(filePath, false)) {
            for (BankAccount acc : accounts.values()) {
                writer.write(acc.toFileString() + "\n");
            }
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
