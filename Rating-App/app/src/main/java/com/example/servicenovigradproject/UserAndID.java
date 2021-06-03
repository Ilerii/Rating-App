package com.example.servicenovigradproject;

public class UserAndID {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userType;
    private String UID;

    public UserAndID(String uid, String fName, String lName, String email, String pass, String uType){
        this.UID = uid;
        this.firstName = fName;
        this.lastName = lName;
        this.email = email;
        this.password = pass;
        this.userType = uType;
    }

    public UserAndID(String uid, User user){
        this.UID = uid;
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.userType = user.getUserType();
    }

    public String getFirstName(){return this.firstName;}
    public String getLastName(){return this.lastName;}
    public String getEmail(){return this.email;}
    public String getUserType(){return this.userType;}
    public String getPassword(){return this.password;}
    public String getUID(){return this.UID;}

    public void setFirstName(String fName){this.firstName = fName;}
    public void setLastName(String lName){this.lastName = lName;}
    public void setEmail(String email){this.email = email;}
    public void setPassword(String pass){this.password = pass;}
    public void setUserType(String uType){this.userType = uType;}
    public void setUID(String uid){this.UID = uid;}

    public String toString(){
        return getUID()+" "+getFirstName()+" "+getLastName()+" "+getEmail()+" "+getPassword()+" "+getUserType();
    }

    public boolean equals(Object o){
        if(o==this){
            return true;
        }
        if(o == null){
            return false;
        }
        if(o instanceof  User){
            User ot = (User)o;
            return (this.getUserType().equals(ot.getUserType()) && this.getEmail().equals(ot.getEmail()) && this.getFirstName().equals(ot.getFirstName()) && this.getLastName().equals(ot.getLastName()) && this.getPassword().equals(ot.getPassword()));
        }
        else if(!(o instanceof UserAndID)){
            return false;
        }
        UserAndID ot = (UserAndID) o;
        return (this.getUID().equals(ot.getUID()) && this.getUserType().equals(ot.getUserType()) && this.getEmail().equals(ot.getEmail()) && this.getFirstName().equals(ot.getFirstName()) && this.getLastName().equals(ot.getLastName()) && this.getPassword().equals(ot.getPassword()));
    }
}
