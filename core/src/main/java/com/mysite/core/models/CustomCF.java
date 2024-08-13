package com.mysite.core.models;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CustomCF {

    @ValueMapValue
    private String cfPath;

    public String getCfPath() {
        return cfPath;
    }

    @SlingObject

    private ResourceResolver resolver;


    public Map<String,String> getCFAuthored(){

        Map<String, String> resultCFs = new HashMap<>();

        if(Objects.nonNull(resolver)) {

            Resource cfResource = resolver.getResource(cfPath);

            if (Objects.nonNull(cfResource)){

                ContentFragment cf = cfResource.adaptTo(ContentFragment.class);

                Iterator<ContentElement> elements = cf.getElements();

                if (Objects.nonNull(elements)) {

                    while (elements.hasNext()) {

                        ContentElement next = elements.next();

                        resultCFs.put(next.getName(), next.getContent());

                    }

                } else return null;
            }else return null;
        }
        return resultCFs;
    }
}