package com.mysite.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/search/tag/in/pages")
public class SearchTags extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String tagName = request.getParameter("tag");
        String root_path = request.getParameter("root_path");
        if (!Objects.nonNull(root_path)) root_path = "/content/mysite/us/en";
        String type = request.getParameter("type");
        if (!Objects.nonNull(type)) type = "cq:Page";
        if (!Objects.nonNull(tagName)) {
            out.write("Please provide the tag name you are searching for");
            return;
        }

        ResourceResolver resolver = request.getResourceResolver();
        List<Resource> resultResources = new ArrayList<>();
        TagManager tagManager = resolver.adaptTo(TagManager.class);
        QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);

        Map<String,String> param = new HashMap<>();
        param.put("path",root_path);
        param.put("type",type);
        SearchResult result = queryBuilder.createQuery(PredicateGroup.create(param),resolver.adaptTo(Session.class)).getResult();

        Iterator<Hit> hits = result.getHits().iterator();
        if(!hits.hasNext()){
            out.write("No resource found under the root_path ");
            return;}
        while(hits.hasNext()){
            Hit hit = hits.next();
            Resource resource = null;
            try {
                resource = hit.getResource();
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
            Resource child = resource.getChild("jcr:content");
            ValueMap valueMap = child.getValueMap();

            if(valueMap.containsKey("cq:tags")){
                String[] cqTagsArray = valueMap.get("cq:tags",String[].class);
                List<String> cqTagsList =  Arrays.asList(cqTagsArray);
                if(cqTagsList.contains(tagName)){
                    resultResources.add(resource);
                }
            }
        }
        if(resultResources.size()!=0)
            for (Resource r : resultResources) {
                out.write("Tag " + tagName + " available on pages :");
                out.write(r.getPath());
            }
        else  {
            out.write("No Asset found with the tag :"+tagName);
        }

        Tag resolvedTag = tagManager.resolve(tagName);
        if (resolvedTag != null) {
            String tagTitle = resolvedTag.getTitle();
            out.write("Tag Title: " + tagTitle);
        } else {
            out.write("Tag not found: " + tagName);
        }

        out.flush();
        out.close();
    }
}
