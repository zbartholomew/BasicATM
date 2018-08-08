package com.zbartholomew;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a Transaction that allows a current user
 *
 * @author Zach Bartholomew
 */
public class Transaction
{
    // The ID number of the user
    private UUID uuid;

    // The amount for the transaction
    private int amount;

    // The account posting the transaction
    private Account transactionAccount;

    // The time and date of the transaction
    private Date timestamp;

    /**
     * Creates a Transaction for the account specified
     *
     * @param amount             transaction amount we are trying to withdraw or deposit
     * @param transactionAccount account associated with the transaction
     */
    public Transaction(int amount, Account transactionAccount)
    {
        this.uuid = UUID.randomUUID();
        this.amount = amount;
        this.transactionAccount = Objects.requireNonNull(transactionAccount, "Account linked to transaction cannot be null)");
        this.timestamp = new Date();
        try
        {
            this.transactionAccount.addTransaction(this);
        } catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public int getAmount()
    {
        return amount;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public Account getAccount()
    {
        return transactionAccount;
    }

    public UUID getUuid()
    {
        return uuid;
    }
}
