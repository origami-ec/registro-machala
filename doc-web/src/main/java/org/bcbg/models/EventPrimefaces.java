/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.models;

/**
 *
 * @author ORIGAMI
 */
public class EventPrimefaces {

    private String status;
    private String details;
    private String users;
    private String date;
    private String icon;
    private String color;
    private String image;

    public EventPrimefaces() {
    }

    public EventPrimefaces(String status, String details, String users, String date, String icon, String color) {
        this.status = status;
        this.details = details;
        this.users = users;
        this.date = date;
        this.icon = icon;
        this.color = color;
    }

    public EventPrimefaces(String status, String details, String users, String date, String icon, String color, String image) {
        this.status = status;
        this.details = details;
        this.users = users;
        this.date = date;
        this.icon = icon;
        this.color = color;
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

}
