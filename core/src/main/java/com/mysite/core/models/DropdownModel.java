package com.mysite.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;


@Model(adaptables = Resource.class)
public class DropdownModel {

    @ValueMapValue(name = "qualification", injectionStrategy = InjectionStrategy.OPTIONAL)
    /*injectionStrategy = InjectionStrategy.OPTIONAL
    defines the injection strategy as optional,
    meaning if the property doesn't exist or is null, it won't throw an error*/
    private String qualification;

    @ValueMapValue(name = "tenth", injectionStrategy = InjectionStrategy.OPTIONAL)
    private String tenth;

    @ValueMapValue(name = "syllabus", injectionStrategy = InjectionStrategy.OPTIONAL)
    private String syllabus;

    @ValueMapValue(name = "inter", injectionStrategy = InjectionStrategy.OPTIONAL)
    private String inter;

    @ValueMapValue(name = "syllabus1", injectionStrategy = InjectionStrategy.OPTIONAL)
    private String syllabus1;

    @ValueMapValue(name = "degree", injectionStrategy = InjectionStrategy.OPTIONAL)
    private String degree;

    @ValueMapValue(name = "syllabus2", injectionStrategy = InjectionStrategy.OPTIONAL)
    private String syllabus2;



    public String getQualification() {
        return qualification;
    }

    public String getTenth() {
        return tenth;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public String getInter() {
        return inter;
    }

    public String getSyllabus1() {
        return syllabus1;
    }

    public String getDegree() {
        return degree;
    }

    public String getSyllabus2() {
        return syllabus2;
    }
}