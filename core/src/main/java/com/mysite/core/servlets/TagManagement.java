package com.mysite.core.servlets;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

@Component(service = Servlet.class,immediate = true)
@SlingServletPaths(value = "/apps/tagManagement")
public class TagManagement extends SlingAllMethodsServlet {

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ResourceResolver resolver = request.getResourceResolver();
        Tag resolve=resolver.adaptTo(TagManager.class).resolve("/content/cq:tags/maintag/color");//Convert the resourses to Object
        Iterator<Tag> listChildren=resolve.listChildren();
        JSONObject pages=new JSONObject();
        JSONArray pagesArray=new JSONArray();
        while(listChildren.hasNext()){
            Tag next=listChildren.next();
            next.getTitle();
            next.getPath().toString();
            try {
                pages.put(next.getTitle(),next.getPath().toString());
                pagesArray.put(pages);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        response.getWriter().write(pagesArray.toString());
    }

}
