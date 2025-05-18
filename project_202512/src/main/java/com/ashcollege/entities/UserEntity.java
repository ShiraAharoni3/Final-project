package com.ashcollege.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.catalina.User;

import java.util.Date;
import java.util.List;
import java.time.LocalDate;

public class UserEntity extends BaseEntity {

    private String username;
    private String phoneNumber;
    @JsonIgnore
    private String otp;


    private String token;
    private String firstName;
    private String lastName;

    private String email;
    private String role;

    private String passwordRecovery; //סיסמא שתיווצר במידה ומשתמש שכח סיסמא וצריך לאפס לו
    private LocalDate lastLogin ;
    //private StatisticsEntity statistics;
    public UserEntity () {

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }


    public String getPasswordRecovery() {
        return passwordRecovery;
    }

    public void setPasswordRecovery(String passwordRecovery) {
        this.passwordRecovery = passwordRecovery;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

   /* public UserEntity(String username, String role, String phoneNumber, String otp, String password, String firstName,
                      String lastName, String email) {
        this.username = username;
        this.role = new RoleEntity();;
        setTheRole(role);
        this.phoneNumber = phoneNumber;
        this.otp = otp;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.lastLogin = LocalDate.now();
    }*/

   /* public UserEntity(String username, String password, String firstName, String lastName, String email, String role, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = new RoleEntity();
        setTheRole(role);
        this.phoneNumber = phoneNumber;
        this.lastLogin = LocalDate.now();
        //this.statistics = new StatisticsEntity();;
    }*/
   public UserEntity(String username, String firstName, String lastName, String email, String token) {
       this.username = username;
       this.firstName = firstName;
       this.lastName = lastName;
       this.email = email;
       this.token = token;
       this.lastLogin = LocalDate.now();
       this.role = "Student" ;
       //this.statistics = new StatisticsEntity();;
   }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getLastLogin()
    {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
