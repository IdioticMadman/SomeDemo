package com.robert.somedemo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by robert on 2017/3/5.
 */

@Entity
public class User {

    @Id
    private String id;
    @Index
    private String name;

    private String department;

    @Generated(hash = 64866948)
    public User(String id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
