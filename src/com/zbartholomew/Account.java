package com.zbartholomew;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an Account within the ATM interface.
 *
 * @author Zach Bartholomew
 */
public class Account
{
    // The accound ID
    private UUID uuid;

    // The current balance
    private BigDecimal balance;

    // The user associated with the account
    private Customer accountCustomer;

    // The map of transaction for the account
    private Map<Date, List<Transaction>> transactionMap;

    // The daily limit of the account
    public static final int DAILY_WITHDRAW_LIMIT = -600;

    /**
     * Creates an Account for a user.  Should not be creating accounts directly since creating a user will
     * create the associated account.
     *
     * @param accountCustomer user
     */
    private Account(Customer accountCustomer)
    {
        this.uuid = UUID.randomUUID();
        this.balance = BigDecimal.ZERO;
        this.accountCustomer = Objects.requireNonNull(accountCustomer, "Customer account cannot be null");
        this.transactionMap = new HashMap<>();

        System.out.println("A new Account was created for user: " + accountCustomer.getUsername());
    }

    /**
     * Gets an account for the given user or creates one if one does not exist.
     *
     * @param accountCustomer - user associated with the account
     * @return {@link Account}
     */
    //Since my implementation only allows for one account per user we need to make sure others
    //cannot create their own instance of an Account that may not be linked to the user
    public static Account getAccountForUser(Customer accountCustomer)
    {
        Objects.requireNonNull(accountCustomer);
        if (accountCustomer.getAccount() != null)
        {
            return accountCustomer.getAccount();
        } else
        {
            return new Account(accountCustomer);
        }
    }

    /**
     * Adds a transaction to the account and verifies the transaction is valid
     *
     * @param transaction {@link Transaction} that is being processed for this account
     */
    public void addTransaction(Transaction transaction)
    {
        Date currentTransactionDate = transaction.getTimestamp();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date today = null;
        try
        {
            today = dateFormat.parse(dateFormat.format(currentTransactionDate));
        } catch (ParseException e)
        {
            System.err.println("Unable to parse the date time for transaction");
        }
        int transactionAmount = transaction.getAmount();
        // Check to see if transaction amount is greater than withdraw limit
        if (transaction.getAmount() > DAILY_WITHDRAW_LIMIT)
        {
            // Could eliminate this with use of enums to track whether transaction is deposit or withdraw
            BigDecimal tempBalanceAfterTransaction = balance.add(BigDecimal.valueOf(transactionAmount));
            // Make sure new balance is positive
            if (tempBalanceAfterTransaction.compareTo(BigDecimal.ZERO) >= 0)
            {
                // See if there are transactions on this account for this day
                if (!transactionMap.isEmpty() && transactionMap.get(today) != null)
                {
                    List<Transaction> dailyTransactionList = transactionMap.get(today);
                    int dailyTransactionTotal = dailyTransactionList
                            .stream()
                            .filter(i -> i.getAmount() < 0)
                            .mapToInt(Transaction::getAmount)
                            .sum();
                    dailyTransactionTotal += transactionAmount;
                    // compare latest transaction timestamp with current transaction timestamp
                    if (dailyTransactionTotal > DAILY_WITHDRAW_LIMIT)
                    {
                        // Add transaction to list of daily transactions
                        balance = balance.add(BigDecimal.valueOf(transactionAmount));
                        transactionMap.get(today).add(transaction);
                    } else
                    {
                        throw new IllegalArgumentException("Cannot process a transaction amount of " + transactionAmount +
                                " because it is over your daily limit of " + DAILY_WITHDRAW_LIMIT);
                    }
                } else
                {
                    balance = balance.add(BigDecimal.valueOf(transactionAmount));
                    List<Transaction> transactionList = new ArrayList<>();
                    transactionList.add(transaction);
                    this.transactionMap.put(today, transactionList);
                }
            } else
            {
                throw new IllegalArgumentException("Cannot process a transaction amount of " + transactionAmount +
                        " because you do not have sufficient funds -- current balance of " + balance);
            }
        } else
        {
            // Probably should make our own exception for this, but for simplicity
            throw new IllegalArgumentException("Cannot process a transaction amount of " + transactionAmount +
                    " because it is over your daily limit of " + DAILY_WITHDRAW_LIMIT);
        }
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public Customer getAccountCustomer()
    {
        return accountCustomer;
    }
}
