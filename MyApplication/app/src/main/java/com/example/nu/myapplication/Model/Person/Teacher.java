package com.example.nu.myapplication.Model.Person;

import android.media.Image;

import com.example.nu.myapplication.Model.Enumerator.Role;
import com.example.nu.myapplication.Model.Position.Address;

import java.util.ArrayList;

/**
 * Created by User on 28/8/2559.
 */
public class Teacher extends Person{
    public Teacher(Role role, String id, Image pic, String token, String tel, String user, String firstName, String surName, String faceBookId, ArrayList<Address> address) {
        super(role, id, pic, token, tel, user, firstName, surName, faceBookId, address);
    }
}
