package com.example.novingrad;

public class User {
    private String username;
    private String name;
    private String email;
    private String password;
    private String type;

    //ADDED DATABASE ID
    private String id;

    /**
     * Leave this empty constructor as well as all the getters as the database needs it to parse Java objects as entries
     */
    public User(){

    }


    //Added constructor without ID for admin to instantiate
    public User(String username, String email,String password, String type, String name ){
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.type = type;
    }


    //Added constructor with ID for user subclasses stored in the database
    public User(String username, String email,String password, String type, String name, String id ){
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.type = type;
        this.id = id;
    }

    public String getUsername(){
        return this.username;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public String getType(){
        return this.type;
    }

    public String getName(){return this.name;}

    public String getId(){return id;}
}
