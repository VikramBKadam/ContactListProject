package com.example.assignment.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.assignment.repository.DateConverter;

import java.util.Date;
import java.util.Objects;

@Entity
public class User {
@PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name="name")
    private String name;
    @ColumnInfo(name="image")
    private String image;
    @ColumnInfo(name="phoneNumber")
    private String  phoneNumber;
    @ColumnInfo
    private String birthday;
    @TypeConverters(DateConverter.class)
    private Date date;

    public User(String name, String phoneNumber, String birthday,String image,Date date) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.image=image;
        this.date=date;
    }
    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(image, user.image) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(birthday, user.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, image, phoneNumber, birthday);
    }
}
