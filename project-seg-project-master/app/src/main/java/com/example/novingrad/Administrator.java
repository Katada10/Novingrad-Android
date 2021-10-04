package com.example.novingrad;

public class Administrator extends User {

    public Administrator(String username,String email,String password){
        super(username,email,password,"administrator", "administrator");
    }

    public void deleteService(Service service){};

    public void createService(Service service){};

    public void editService(Service service,Service newService){};
}
