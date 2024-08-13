package com.mysite.core.models;

import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.api.resource.Resource;


@Model(adaptables = Resource.class)
public class CheckboxModel {

    @ValueMapValue
    @Default(values = "false")
    private String employee;
    @ValueMapValue
    private String edeg;
    @ValueMapValue
    private String ename;
    @ValueMapValue
    @Default(values = "false")
    private String student;
    @ValueMapValue
    private String sname;
    @ValueMapValue
    private String roll;

    public String getEmployee() {
        return employee;
    }
    public String getEdeg() {
        return edeg;
    }
    public String getEname() {
        return ename;
    }
    public String getStudent() {
        return student;
    }
    public String getSname() {
        return sname;
    }
    public String getRoll() {
        return roll;
    }
}