package com.example.nu.myapplication.Model.Person;

import android.media.Image;


import com.example.nu.myapplication.Model.Enumerator.Role;
import com.example.nu.myapplication.Model.Enumerator.TypeOfService;
import com.example.nu.myapplication.Model.Position.Address;

import java.io.Serializable;
import java.util.ArrayList;

/**

 * Created by User on 28/8/2559.
 */
public class Student extends Person  {
    private TypeOfService typeOfService;
    private String studentId;

    public Student(Role role, String id, Image pic, String token, String tel, String user, String firstName, String surName, String faceBookId, ArrayList<Address> address, TypeOfService typeOfService, String studentId) {
        super(role, id, pic, token, tel, user, firstName, surName, faceBookId,address);
        this.typeOfService = typeOfService;
        this.studentId = studentId;
    }

    public TypeOfService getTypeOfService() {
        return typeOfService;
    }

    public void setTypeOfService(TypeOfService typeOfService) {
        this.typeOfService = typeOfService;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
