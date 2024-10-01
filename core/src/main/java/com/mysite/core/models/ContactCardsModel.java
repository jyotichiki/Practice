package com.mysite.core.models;


import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;

import javax.inject.Inject;


@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class ContactCardsModel {
    //    @SlingObject
//    SlingHttpServletRequest slingRequest;
    @Inject
    @Via("resource")
    public List<Contact> contact;

    public List<Contact> getContact() {
        return contact;
    }
    public int getSize(){
        return contact.size();
    }


}
