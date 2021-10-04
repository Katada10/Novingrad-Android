package com.example.novingrad;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Branch extends User {
    private Map<String, Service> services;
    private int[] workingHours;
    private String address;
    private HashMap<String, ArrayList<String>> serviceHours;
    private int rating;
    private int numOfReviews;




    public Branch(){
    //LEAVE FOR DB

    }



    public Branch(String username,String email,String password, String name, String id, String address) {
        super(username, email, password, "branch", name,id);
        services = new HashMap<>();
        serviceHours = new  HashMap<>();
        rating = 0;
        numOfReviews = 0;
        this.address = address;

    }

    public void updateHours(HashMap<String, ArrayList<String>> hours){
        this.serviceHours = hours;
    }

    /**
     * Adds a service to be offered by this branch
     * @param serviceType
     * @param requirements
     */
    public void addService(String serviceType, String requirements){
        if(services == null)
            services = new HashMap<>();

        Service newService = new Service(serviceType, requirements);
//        newService.updateHours(hours); //ADD args

        services.put(serviceType, newService);
    }


    /**
     * Removes a service that is no longer offered
     * @param serviceType
     */
    public void deleteService(String serviceType){

        if(services != null) {
            if (services.containsKey(serviceType)) {
                services.remove(serviceType);
            }
        }
    }


    public int getRating(){
        if(numOfReviews > 0) return rating/numOfReviews;

        return 0;
    }

    public void rateBranch(int val){
        rating = val+rating;
        numOfReviews++;
    }


    public String getAddress(){return address;}

    public HashMap<String, ArrayList<String>> getServiceHours(){return  serviceHours;};

    public Service getService(String key){
        return services.get(key);
    }

    public Map<String, Service> getServices(){
        return services;
    }

}

