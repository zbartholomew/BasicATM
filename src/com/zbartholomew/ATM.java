package com.zbartholomew;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

import static com.zbartholomew.Account.DAILY_WITHDRAW_LIMIT;

/**
 * Provides the interface for the customer to interact with all the systems that make up a basic ATM
 *
 * @author Zach Bartholomew
 */
public class ATM
{
    private Scanner input = new Scanner(System.in);
    private NumberFormat usCurrency = NumberFormat.getCurrencyInstance(Locale.US);
    private Customer customer;

    public ATM()
    {
        createWelcomeMenu();
    }

    /**
     * Creates the welcome menu text gui
     */
    private void createWelcomeMenu()
    {
        customer = null;
        System.out.println("\n\nWelcome to our ATM.");
        System.out.println("Please enter your username:");

        String username = input.nextLine();

        customer = Customer.getUser(username);
        if (customer != null)
        {
            createPinMenu();
        } else
        {
            System.out.println("Invalid username");
            returnToWelcomeMenu();
        }
    }

    /**
     * Creates the pin menu text gui
     */
    private void createPinMenu()
    {
        System.out.println("Please enter PIN: ");
        try
        {
            int pin = input.nextInt();
            input.nextLine();

            int remainingChances = 3;
            if (isPinValid(pin))
            {
                createUserMenu();
            } else
            {
                while (remainingChances > 0)
                {
                    System.out.println("Invalid PIN please try again (" + remainingChances + " chances remaining)...");
                    pin = input.nextInt();
                    if (isPinValid(pin))
                    {
                        createUserMenu();
                        break;
                    }
                    remainingChances--;
                }
                returnToWelcomeMenu();
            }
        } catch (InputMismatchException e)
        {
            System.out.println("Invalid input.  Please enter a whole number.");
            returnToWelcomeMenu();
        }
    }

    /**
     * Creates the customer menu text gui
     */
    private void createUserMenu()
    {
        System.out.println("\n\nWelcome " + customer.getUsername() + "!");
        System.out.println("What would you like to do?");
        System.out.println("========================");
        System.out.println("| [1]  Check Balance   |");
        System.out.println("| [2]  Withdrawal      |");
        System.out.println("| [3]  Deposit         |");
        System.out.println("| [4]  Exit            |");
        System.out.println("========================");

        try
        {
            int selection = input.nextInt();
            input.nextLine();

            switch (selection)
            {
                case 1:
                    viewBalance();
                    break;
                case 2:
                    withdraw();
                    break;
                case 3:
                    deposit();
                    break;
                case 4:
                    System.out.println("Thank you for using our ATM.  Returning to Welcome Menu.");
                    createWelcomeMenu();
                    break;
                default:
                    System.out.println("Unrecognized option.  Please try again.");
                    returnToUserMenu();
            }
        } catch (InputMismatchException e)
        {
            System.out.println("Invalid input.  Please enter a whole number.");
            returnToUserMenu();
        }
    }

    /**
     * Prints out the current account balance for customer
     */
    private void viewBalance()
    {
        System.out.println("Current Balance on account is: " + usCurrency.format(customer.getAccount().getBalance()));
        returnToUserMenu();
    }

    /**
     * Prints and executes a withdrawal from customer's account
     */
    private void withdraw()
    {
        System.out.println("Please enter the amount you want to withdraw (Daily limit of " + Math.abs(DAILY_WITHDRAW_LIMIT) + ")");
        int amount = input.nextInt();
        input.nextLine();

        if (!isPositiveAmount(amount) || customer == null)
        {
            return;
        }

        if (amount < DAILY_WITHDRAW_LIMIT)
        {
            System.out.println("The requested amount of " + amount + " is greater than the daily withdraw limit of " + Math.abs(DAILY_WITHDRAW_LIMIT));
        }

        try
        {
            amount *= -1;
            new Transaction(amount, customer.getAccount());
            System.out.println("Withdraw successful!");
            returnToUserMenu();
        } catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
            returnToUserMenu();
        }
    }

    /**
     * Prints and executes a deposit from customer's account
     */
    private void deposit()
    {
        System.out.println("Please enter the amount you want to deposit: ");
        int amount = input.nextInt();

        if (!isPositiveAmount(amount) || customer == null)
        {
            return;
        }

        try
        {
            new Transaction(amount, customer.getAccount());
            System.out.println("Deposit successful!");
            returnToUserMenu();
        } catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
            returnToUserMenu();
        }
    }

    /**
     * Verifies amount is positive
     * @param amount integer value of deposit or withdrawal
     * @return boolean true if postive amount, false if negative
     */
    private boolean isPositiveAmount(int amount)
    {
        if (amount < 0)
        {
            System.out.println("Invalid amount - please enter positive number");
            System.out.println("Returning to Main Menu");
            returnToUserMenu();
            return false;
        }
        return true;
    }

    /**
     * Validates the PIN supplied is a valid pin for the customer
     * @param pin supplied pin to compare
     * @return true if pin is valid and false if not
     */
    private boolean isPinValid(int pin)
    {
        if (customer != null)
        {
            return customer.validatePin(String.valueOf(pin));
        } else
        {
            System.out.println("Cannot validate PIN.  Customer is null.");
            return false;
        }
    }

    /**
     * Returns to the customer menu given a key input from the customer
     */
    private void returnToUserMenu()
    {
        System.out.println("Press enter key to return to Main Menu");
        try
        {
            System.in.read();
            input.nextLine();
            createUserMenu();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Returns to the welcome menu given a key input from the customer
     */
    private void returnToWelcomeMenu()
    {
        System.out.println("Press enter key to return to Welcome Menu");
        try
        {
            System.in.read();
            input.nextLine();
            createWelcomeMenu();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
