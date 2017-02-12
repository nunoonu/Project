package com.example.nu.myapplication.Utils;

import com.example.nu.myapplication.Model.Person.Parent;
import com.example.nu.myapplication.Model.Person.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

/**
 * Created by EVILUTION on 25/1/2560.
 */

public class JsonToObjectUtil {

    public static Student jsonToStudent(JSONObject json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Student stu = mapper.readValue(json.toString(), Student.class);
            return stu;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Parent jsonToParent(JSONObject json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Parent parent = mapper.readValue(json.toString(), Parent.class);
            return parent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
