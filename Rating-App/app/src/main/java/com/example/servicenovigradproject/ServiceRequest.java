package com.example.servicenovigradproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class ServiceRequest {

    // Class Variables
    private String name;  // Name of service
    private double rate;  // Rate
    private String id;  // Firebase Uid
    private ArrayList<String> lstDocuments;

    // Constructor 1: Initialization from firebase database
    public ServiceRequest(){}

    // Constructor 2: Initialization from customers at the CreateRequest page
    public ServiceRequest(String name, double rate, ArrayList<File> images){
        this.name = name;
        this.rate = rate;
        this.id = null;
        convertImgToBytes(images);  // Convert ArrayList images to base64 strings for easy storage
    }

    private void convertImgToBytes(ArrayList<File> images){
        try {
            for (File f : images) {
                String filePath = f.getAbsolutePath();  // Get file path
                Bitmap bitmapImage = BitmapFactory.decodeFile(filePath);  // Create Bitmap object from file
                ByteArrayOutputStream stream = new ByteArrayOutputStream();  // Create byte stream
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();  // Save bitmap to byte array
                String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);  // Save byte array to base64 string
                this.lstDocuments.add(base64Image);  // Store base64 string data into lst_documents
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getRate() {
        return rate;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public ArrayList<String> getDocuments() {
        return lstDocuments;
    }
    public void setDocuments(ArrayList<String> lstDocuments) {
        this.lstDocuments = lstDocuments;
    }

    public boolean equals(Object o){
        if(o==this){
            return true;
        }
        if(o == null){
            return false;
        }
        if(!(o instanceof ServiceRequest)){
            return false;
        }
        else {
            boolean toReturn = true;
            toReturn = toReturn && this.getId().equals(((ServiceRequest) o).getId());
            toReturn = toReturn && this.getRate() == ((ServiceRequest) o).getRate();
            toReturn = toReturn && this.getName().equals(((ServiceRequest) o).getName());
            toReturn = toReturn && this.getDocuments().equals(((ServiceRequest) o).getDocuments());
            return toReturn;
        }

    }
}
