package com.example.servicenovigradproject;

public class Service {

    // Class Variables
    private String name;  // Name of service
    private double rate;  // Rate
    private boolean driverLicense;
    private boolean healthCard;
    private boolean photoID;

    // Constructor
    public Service(){
    }

    public Service(String name, double rate, boolean driverLicense, boolean healthCard, boolean photoID){
        this.name = name;
        this.rate = rate;
        this.driverLicense = driverLicense;
        this.healthCard = healthCard;
        this.photoID = photoID;
    }

    public Service(Service old){
        this.name = old.getName();
        this.rate = old.getRate();
        this.driverLicense = old.isDriverLicense();
        this.healthCard = old.isHealthCard();
        this.photoID = old.isPhotoID();
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }

    public boolean isDriverLicense() { return driverLicense; }
    public void setDriverLicense(boolean driverLicense) { this.driverLicense = driverLicense; }

    public boolean isHealthCard() { return healthCard; }
    public void setHealthCard(boolean healthCard) { this.healthCard = healthCard; }

    public boolean isPhotoID() { return photoID; }
    public void setPhotoID(boolean photoID) { this.photoID = photoID; }

    public String toString(){
        return "Service: " + name + " with rate $" + rate + " documents required: Drivers License " + driverLicense + " Health Card " + healthCard + " Photo ID " + photoID;
    }

    public boolean equals(Object o){
        if(o==this){
            return true;
        }
        else if(o == null){
            return false;
        }
        else if(!(o instanceof Service)){
            return false;
        }
        else{
            return ((this.getName().equals(((Service) o).getName()))&&(this.getRate() == ((Service) o).getRate()) && (this.isDriverLicense() == ((Service) o).isDriverLicense()) && (this.isHealthCard() == ((Service) o).isHealthCard()) && (this.isPhotoID() == ((Service) o).isPhotoID()));
        }
    }
}
