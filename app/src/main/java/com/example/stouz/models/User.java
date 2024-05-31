package com.example.stouz.models;

public class User
{
    public String Name;
    public String Surname;
    public String Email;
    public String Password;

    public User(){

    }

    public User(String name, String surname, String email, String password){
        this.Name = name;
        this.Surname = surname;
        this.Email = email;
        this.Password = password;
    }
}
