package com.mysite.core.models;


import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.util.*;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageListModel {
    private static final Logger logger = LoggerFactory.getLogger(PageListModel.class);
    @ValueMapValue
    private String path;
    private List<String> paths = new ArrayList<>();

    @ValueMapValue
    private String tag;


    @SlingObject
    private ResourceResolver resolver;
    private String some;
    Map<String, String> predicate = new HashMap<>();

    public String getPath() {
        return path;
    }

    public String getTag() {
        return tag;
    }

    public String getSome() {
        QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);
        predicate.put("path", path);
        predicate.put("1_property", "sling:resourceType");
        predicate.put("1_property.value", "mysite/components/page");
        predicate.put("2_property", "cq:tags");
        predicate.put("2_property.value", tag);
        Query query = queryBuilder.createQuery(PredicateGroup.create(predicate), resolver.adaptTo(Session.class));
        SearchResult result = query.getResult();
        for (Hit hit : result.getHits()) {
            try {
                some = hit.getPath();
                paths.add(some);
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public List<String> getPaths() {
        return paths;
    }

    private List<String> thumbnailPath1 = new ArrayList();

    public List<String> getThumbnailPath() {
        String thumbnailPath = null;
        String next = null;
        String s = null;
        try {
            if (paths != null) {
                logger.info("Page Path " + paths);
                List<String> paths = getPaths();
                Iterator<String> resource = paths.iterator();
                while (resource.hasNext()) {
                    next = resource.next();
                    Resource resource1 = resolver.getResource(next);
                    Resource imageResource = resource1.getChild("image");
                    if (imageResource != null) {
                        ValueMap valueMap = imageResource.adaptTo(ValueMap.class);
                        if (valueMap != null) {
                            thumbnailPath = valueMap.get("fileReference", String.class);
                        }
                    }
                    thumbnailPath1.add(thumbnailPath);
                    if (thumbnailPath1 != null) {
                        logger.info("Thumbnail path: " + thumbnailPath1);
                    } else {
                        logger.warn("Thumbnail path is null");
                    }
                }
            } else {
                logger.warn("Image resource is null");
            }
        } catch (Exception e) {
            logger.error("Error fetching thumbnail path: " + e.getMessage());
        }
        return thumbnailPath1;
    }
    private List<String> titles=new ArrayList<>();
    public List<String> getPageTitle() {
        String next = null;
        String s = null;
        if (paths != null) {
            logger.info("Page Path " + paths);
            List<String> paths = getPaths();
            Iterator<String> resource = paths.iterator();
            while (resource.hasNext()) {
                next = resource.next();
                Resource resource1 = resolver.getResource(next);
                ValueMap map=resource1.adaptTo(ValueMap.class);
                if (map != null) {
                    s = map.get("jcr:title", String.class);
                }
                titles.add(s);
            }
        }
        return titles;

    }
    public List<List<String>> getDataForTable() {
        List<List<String>> tableData = new ArrayList<>();

        List<String> paths = getPaths();
        List<String> thumbnailPaths = getThumbnailPath();
        List<String> pageTitles = getPageTitle();

        int maxSize = paths.size();

        for (int i = 0; i<maxSize; i++) {
            List<String> row = new ArrayList<>();
            row.add(i < paths.size() ? paths.get(i) : "");
            row.add(getTag());
            row.add(i < thumbnailPaths.size() ? thumbnailPaths.get(i) : "");
            row.add("");
            row.add(i < pageTitles.size() ? pageTitles.get(i) : "");
            tableData.add(row);
        }
        return tableData;
    }

}

