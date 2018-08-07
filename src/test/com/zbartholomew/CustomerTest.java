package com.zbartholomew;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link Customer}
 *
 * @author Zach Bartholomew
 */
public class CustomerTest
{
    private String username = "johnsmith";

    /**
     * Tests the constructor properly updates fields
     */
    @Test
    public void testConstructor()
    {
        Customer customer = new Customer(username, "1255");
        Assert.assertNotNull("Customer should not be null", customer);
        Assert.assertNotNull("An account should have been created when a new customer is created", customer.getAccount());
        Assert.assertNotNull("UUID should not be null", customer.getUuid());
        Assert.assertEquals("username should have been saved", username, customer.getUsername());
        Assert.assertEquals("userMap should contain new customer", customer, Customer.getUser(customer.getUsername()));
    }

    /**
     * Tests the constructor for handling a bad username
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorBadUsername()
    {
        new Customer("john", "1255");
    }

    /**
     * Tests that the validate pin method correctly returns true and false for valid and non valid pins
     */
    @Test
    public void testValidatePin()
    {
        Customer customer = new Customer(username, "1255");
        Assert.assertTrue(customer.validatePin("1255"));
        Assert.assertFalse(customer.validatePin("1236587"));
    }
}