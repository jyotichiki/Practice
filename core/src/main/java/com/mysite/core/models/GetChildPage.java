package com.mysite.core.models;


import com.day.cq.search.PredicateGroup;

import com.day.cq.search.QueryBuilder;

import com.day.cq.search.result.Hit;

import com.day.cq.search.result.SearchResult;

import org.apache.sling.api.resource.Resource;

import org.apache.sling.api.resource.ResourceResolver;

import org.apache.sling.api.resource.ValueMap;

import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import org.apache.sling.models.annotations.Model;

import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;

import javax.jcr.Session;

import java.util.*;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class GetChildPage {

    private static final Logger log = LoggerFactory.getLogger(GetChildPage.class);

    @ValueMapValue

    private static String childPage;

    @ValueMapValue

    private static String tag;

    public String getChildPage() {

        return childPage;

    }

    public String getTag() {

        return tag;

    }

    @SlingObject

    private ResourceResolver resolver;

    //private static List<Resource> resultResources;

    public List<Resource> getChildWithTag() {

        if (Objects.nonNull(childPage) && Objects.nonNull(tag)){

            List<Resource>  resultResources = new ArrayList<>();

            QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);

            Map<String, String> param = new HashMap<>();

            param.put("path", childPage);

            param.put("type", "cq:Page");

            SearchResult result = queryBuilder.createQuery(PredicateGroup.create(param), resolver.adaptTo(Session.class)).getResult();

            Iterator<Hit> hits = result.getHits().iterator();

            if (!hits.hasNext()) {

                log.info("No resource found under the root_path ");

                return null;

            }

            while (hits.hasNext()) {

                Hit hit = hits.next();

                Resource resource = null;

                try {

                    resource = hit.getResource();

                } catch (RepositoryException e) {

                    throw new RuntimeException(e);

                }

                Resource child = resource.getChild("jcr:content");

                ValueMap valueMap = child.getValueMap();

                if (valueMap.containsKey("cq:tags")) {

                    String[] cqTagsArray = valueMap.get("cq:tags", String[].class);

                    List<String> cqTagsList = Arrays.asList(cqTagsArray);

                    if (cqTagsList.contains(tag)) {

                        resultResources.add(resource);

                    }

                }

            }

            if (resultResources.size() != 0)

                for (Resource r :

                        resultResources) {

                    log.info("Tag " + tag + " available on pages :");

                    log.info(r.getPath());

                }

            else {

                log.info("No Page found with the tag :" + tag);

            }

            return resultResources;

        }else log.info("Either or Both the childPage and tag are null");

        return null;

    }

}
