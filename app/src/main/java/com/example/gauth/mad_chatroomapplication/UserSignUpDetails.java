package com.example.gauth.mad_chatroomapplication;

public class UserSignUpDetails {
    String Firstname;
    String Lastname;
    String Email;
    String Password;
    String Uid;

    public UserSignUpDetails(String firstname,String lastname,String email,String Password,String Uid)
    {
        this.Firstname=firstname;
        this.Lastname=lastname;
        this.Email=email;
        this.Password=Password;
        this.Uid=Uid;
    }
    UserSignUpDetails()
    {

    }


}
