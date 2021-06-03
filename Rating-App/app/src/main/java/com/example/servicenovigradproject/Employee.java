package com.example.servicenovigradproject;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Employee{
    private ArrayList <String> offeredServices;
    private ArrayList<DailyHours> workingHours;
    private String address;
    private String phoneNumber;
    private boolean branchSetUp;
    public Employee(String address, String phoneNumber) {
        workingHours = new ArrayList<>();
        offeredServices = new ArrayList<>();
        this.address = address;
        this.phoneNumber = phoneNumber;
        branchSetUp = false;
    }
    public Employee(String address, String phoneNumber, boolean branchSetUp) {
        workingHours = new ArrayList<>();
        offeredServices = new ArrayList<>();
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.branchSetUp = branchSetUp;
    }
    public Employee(){
        address = "";
        phoneNumber = "";
        workingHours = new ArrayList<>();
        offeredServices = new ArrayList<>();
        branchSetUp = false;
    }
    public Employee(String address, String phoneNumber, boolean branchSetUp, ArrayList<DailyHours> workingHours, ArrayList<String> offeredServices) {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.branchSetUp = branchSetUp;
        this.workingHours = workingHours;
        this.offeredServices = offeredServices;
    }


    public void setWorkingHours(ArrayList<DailyHours> workingHours) {
        this.workingHours = workingHours;
    }

    /**
     *
     * @param day string representing day of the week
     * @param startHour int representing the hour the location opens. in range -1 - 23,
     * @param startMinute int representing the minute the location opens. in range -1 - 59
     * @param endHour int representing the hour the location closes. in range -1 - 23,
     * @param endMinute int representing the minute the location closes. in range -1 - 59
     * -1 for all values (other than day) = closed
     */
    public void setWorkingHours(String day, int startHour, int startMinute, int endHour, int endMinute){
        try{
            workingHours.set(dayToInt(day),new DailyHours(startHour,startMinute,endHour,endMinute));
        }
        catch (IndexOutOfBoundsException e){
            Log.e("Employee","Invalid day sent, DailyHours unchanged");
        }

    }

    public void setWorkingHours(int day, int startHour, int startMinute, int endHour, int endMinute){
        try{
            workingHours.set(day,new DailyHours(startHour,startMinute,endHour,endMinute));
        }
        catch (IndexOutOfBoundsException e){
            Log.e("Employee","Invalid day sent, DailyHours unchanged");
        }
    }

    public void setOfferedServices(ArrayList<String> services){this.offeredServices = services;}
    public void addOfferedService(String service){
        offeredServices.add(service);
    }

    public ArrayList<DailyHours> getWorkingHours(){
        return workingHours;
    }

    public void setupBranch(){
        branchSetUp = true;
    }

    public boolean removeOfferedService(String service){
        try{
            offeredServices.remove(service);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public ArrayList<String> getOfferedServices() {
        return offeredServices;
    }

    public String getAddress(){
        return address;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setAddress(String address){
        this.address = address;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public boolean isBranchSetUp(){
        return branchSetUp;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "offeredServices=" + offeredServices +
                ", workingHours=" + workingHours +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", branchSetUp=" + branchSetUp +
                '}';
    }

    private int dayToInt(String day){
        day.toLowerCase();
        switch(day){
            case "sunday":
                return 0;
            case "monday":
                return 1;
            case "tuesday":
                return 2;
            case "wednesday":
                return 3;
            case "thursday":
                return 4;
            case "friday":
                return 5;
            case "saturday":
                return 6;
            default:
                return -1;
        }
    }

    public boolean equals(Object o){
        if(o==this){
            return true;
        }
        if(o == null){
            return false;
        }
        if(!(o instanceof Employee)){
            return false;
        }
        else {
            boolean toReturn = true;
            toReturn = toReturn && ((Employee) o).getPhoneNumber().equals(this.getPhoneNumber());
            toReturn = toReturn && ((Employee) o).getAddress().equals(this.getAddress());
            toReturn = toReturn && ((Employee) o).getOfferedServices().equals(this.getOfferedServices());
            toReturn = toReturn && ((Employee) o).getWorkingHours().equals(this.getWorkingHours());
            return toReturn;
        }

    }

}
