package com.example.novingrad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Service {


    private String serviceType; //details of the service
    private String requiredDocuments;



    public Service(){
    //KEEP EMPTY CONSTRUCTOR NEEDED FOR DB
    }

    public Service(String serviceType, String requiredDocuments){
        this.serviceType = serviceType;
        this.requiredDocuments = requiredDocuments;
        //Initialize hash map for service hours
    }


    public void doService(){

    }


    public String getServiceType() {
        return serviceType;
    }

    public String getRequiredDocuments() {
        return requiredDocuments;
    }

    public String toString(){
        return serviceType;
    }



}
