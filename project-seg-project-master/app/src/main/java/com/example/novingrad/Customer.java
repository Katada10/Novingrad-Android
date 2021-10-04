package com.example.novingrad;

public class Customer extends User {

    public Customer(){

    }
    public Customer(String username, String email,String password, String name, String id){
        super(username,email,password,"customer",name, id);
    }


}
