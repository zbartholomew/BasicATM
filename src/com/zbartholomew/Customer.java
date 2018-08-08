package com.zbartholomew;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a Customer for our ATM.
 *
 * @author Zach Bartholomew
 */
public class Customer
{
    // The Customer Name for the user -- decided to go this route instead of first name and last name for simplicity
    private String username;

    // The ID number of the user
    private UUID uuid;

    // The SHA-256 hash of the user's pin
    private byte pinHash[];
    // Used for encryption
    private MessageDigest messageDigest;

    // The account associated with the user
    private Account account;

    // The Customer Map with username as the key and Customer object as the value
    private static Map<String, Customer> customerMap = new HashMap<>();

    /**
     * Creates a new Customer
     *
     * @param username the customer's username - used to log in with
     * @param pin      the customer's pin
     * @throws IllegalArgumentException - username cannot be less than 6 characters
     */
    public Customer(String username, String pin)
    {
        if (username.length() < 6)
        {
            throw new IllegalArgumentException("Username cannot be less than 6 characters");
        }
        this.username = username;
        // For our situation this way of generating a UUID is acceptable, but probably not use this in practice
        this.uuid = UUID.randomUUID();
        try
        {
            messageDigest = MessageDigest.getInstance("SHA-256");
            this.pinHash = messageDigest.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e)
        {
            System.err.println("Error!  NoSuchAlgorithmException when trying to do get an instance of SHA-256");
            throw new RuntimeException();
        }
        this.account = Account.getAccountForUser(this);
        customerMap.put(username, this);
        System.out.println("New user and account was created with username: " + username + " and account ID of " + account.getUuid().toString());
    }

    /**
     * Decrypts the PIN and validates the User's PIN is the same as the one provided
     *
     * @param pin compared to encrypted pin
     * @return true if valid pin, false if not
     */
    public boolean validatePin(String pin)
    {
        if (messageDigest != null)
        {
            return MessageDigest.isEqual(messageDigest.digest(pin.getBytes()), pinHash);
        } else
        {
            System.out.println("messageDigest should not be null");
            return false;
        }
    }

    public String getUsername()
    {
        return username;
    }

    public com.zbartholomew.Account getAccount()
    {
        return account;
    }

    /**
     * Searches the customer map for the username provided
     *
     * @param username key value to user map
     * @return {@link Customer}
     */
    public static Customer getUser(String username)
    {
        return customerMap.get(username);
    }

    public UUID getUuid()
    {
        return uuid;
    }
}
