package com.example.novingrad;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Tests{

    @Test
    public void TestAdministrator(){
        Administrator myAdministrator = new Administrator("a", "a@a", "a");
        assertEquals("administrator", myAdministrator.getName());
    }

    @Test
    public void TestBranch(){
        Branch myBranch = new Branch("a", "a@a", "a", "a", "a", "address");
        assertEquals("branch", myBranch.getType());
    }

    @Test
    public void TestCustomer(){
        Customer myCustomer = new Customer("a", "a@a", "a", "a", "a");
        assertEquals("customer", myCustomer.getType());
    }

    @Test
    public void TestService(){
        Service myService = new Service();
        assertEquals(null, myService.getServiceType());
    }

    @Test
    public void AdminUsername(){
        assertEquals("user12345", MainActivity.getAdminUsername());
    }

    @Test
    public void Test1(){
        assertEquals(0, CustomerActivity.getList().size());
    }

    @Test
    public void Test2(){
        assertEquals(1, CustomerActivity.testingServiceSearchResults1());
    }

    @Test
    public void Test3(){
        assertEquals("this", CustomerActivity.testingServiceSearchResults2());
    }

    @Test
    public void Test4(){
        assertEquals(1, CustomerActivity.testingAddressSearchResults1());
    }

    public void Test5(){
        assertEquals("", CustomerActivity.testingAddressSearchResults2());
    }
}