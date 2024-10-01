package com.mysite.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Bill {

    @ValueMapValue
    private String dishName;

    @ValueMapValue
    private String dishPrice;

    public String getDishPrice() {
        return dishPrice;
    }

    public String getDishName() {
        return dishName;
    }


}
