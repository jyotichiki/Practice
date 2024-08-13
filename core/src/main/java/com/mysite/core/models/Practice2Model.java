package com.mysite.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ResourcePath;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "mysite/components/practice2"
)
public class Practice2Model {

    @SlingObject
    ResourceResolver resourceResolver;
    /*The ResourceResolver is used to resolve resources,
    which represent content nodes in the repository, and to perform operations
     such as reading, writing, and deleting content,  In AEM development to interact with the repository,
      such as retrieving content, creating nodes, updating properties, etc .*/

    @SlingObject
    private SlingHttpServletRequest request;


    @Self
    SlingHttpServletRequest slingHttpServletRequest;
    /*The method property of slingHttpServletRequest returns the HTTP method
    (e.g., GET, POST, PUT, DELETE) used in the current request.
    It's useful for determining the type of operation being performed by the client on the server.*/



    @Inject
    @Via("resource")
    private String veg;

    @Inject
    @Via("resource")
    private String nonveg;

    @Inject
    @Via("resource")
    @Named("jcr:lastModifiedBy")
    String modifiedBy;
    //when we want to impersonate your property name with other name to make our getter easy

    @ScriptVariable
    private Resource resource;


    public String getVeg() {
        return veg;
    }

    public String getNonveg() {
        return nonveg;
    }

    public Resource getResource() {
        return resource;

    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public ResourceResolver getResourceResolver() {
        return resourceResolver;

    }

    public SlingHttpServletRequest getSlingHttpServletRequest() {
        return slingHttpServletRequest;
    }

    public String logComponentPath() {
        if (request != null) {
            String componentPath = request.getRequestPathInfo().getResourcePath();
            // Log the component path
            org.slf4j.LoggerFactory.getLogger(getClass()).info("Component path: {}", componentPath);
            return componentPath;
        }
        return null;
    }

    public SlingHttpServletRequest getRequest() {
        return request;
    }


}