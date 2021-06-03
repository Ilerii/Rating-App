package com.example.servicenovigradproject;


public class User{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userType;
    private boolean isDisabled;

    public User(String fName, String lName, String email, String pass, String uType){
        this.firstName = fName;
        this.lastName = lName;
        this.email = email;
        this.password = pass;
        this.userType = uType;
        this.isDisabled = false;
    }

    public User(String fName, String lName, String email, String pass, String uType, boolean isDisabled){
        this.firstName = fName;
        this.lastName = lName;
        this.email = email;
        this.password = pass;
        this.userType = uType;
        this.isDisabled = isDisabled;
    }

    public User(User oldUser, boolean isDisabled){
        this.firstName = oldUser.getFirstName();
        this.lastName = oldUser.getLastName();
        this.email = oldUser.getEmail();
        this.password = oldUser.getPassword();
        this.userType = oldUser.getUserType();
        this.isDisabled = isDisabled;
    }

    public User(){}

    public String getFirstName(){return this.firstName;}
    public String getLastName(){return this.lastName;}
    public String getEmail(){return this.email;}
    public String getUserType(){return this.userType;}
    public String getPassword(){return this.password;}
    public boolean getDisabledFlag(){return this.isDisabled; };

    public void setFirstName(String fName){this.firstName = fName;}
    public void setLastName(String lName){this.lastName = lName;}
    public void setEmail(String email){this.email = email;}
    public void setPassword(String pass){this.password = pass;}
    public void setUserType(String uType){this.userType = uType;}
    public void setDisabledFlag(boolean bool){this.isDisabled = bool;}

    public String toString(){
        return getFirstName()+" "+getLastName()+" "+getEmail()+" "+getPassword()+" "+getUserType();
    }

    public boolean equals(Object o){
        if(o==this){
            return true;
        }
        if(o == null){
            return false;
        }
        if(o instanceof  UserAndID){
            UserAndID ot = (UserAndID)o;
            return (this.getUserType().equals(ot.getUserType()) && this.getEmail().equals(ot.getEmail()) && this.getFirstName().equals(ot.getFirstName()) && this.getLastName().equals(ot.getLastName()) && this.getPassword().equals(ot.getPassword()));
        }
        else if(!(o instanceof User)){
            return false;
        }
        User ot = (User)o;
        return (this.getUserType().equals(ot.getUserType()) && this.getEmail().equals(ot.getEmail()) && this.getFirstName().equals(ot.getFirstName()) && this.getLastName().equals(ot.getLastName()) && this.getPassword().equals(ot.getPassword()));
    }
}
