//package com.mysite.core.schedulers;
//
//
//import com.day.cq.search.PredicateGroup;
//import com.day.cq.search.QueryBuilder;
//import com.day.cq.search.result.Hit;
//import com.day.cq.search.result.SearchResult;
//import org.apache.sling.api.resource.*;
//import org.apache.sling.commons.scheduler.ScheduleOptions;
//import org.osgi.service.component.annotations.Activate;
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.Reference;
//import org.osgi.service.metatype.annotations.Designate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.jcr.RepositoryException;
//import javax.jcr.Session;
//import java.util.*;
//
//@Designate(ocd= TestSchedulerConfig.class)
//@Component(service = Runnable.class)
//public class TestScheduler implements Runnable{
//
//    @Reference
//    ResourceResolverFactory resolverFactory;
//    private Logger logger =  LoggerFactory.getLogger(getClass());
//    QueryBuilder queryBuilder;
//
//    ResourceResolver resourceResolver = null;
//    @Override
//    public void run() {
//        String fieldLabel = "fkvbfkvbfev";
//        Map<String,Object> param = new HashMap<>();
//        param.put(ResourceResolverFactory.SUBSERVICE,"myuser");
//        try {
//            resourceResolver = resolverFactory.getServiceResourceResolver(param);
//            if(Objects.nonNull(resourceResolver)) logger.info("resourceresolver is Obtained");
//        } catch (LoginException e) {
//            throw new RuntimeException(e);
//        }
//        queryBuilder =  resourceResolver.adaptTo(QueryBuilder.class);
//        if(Objects.nonNull(queryBuilder)){
//            try {
//                executeQuery(resourceResolver,queryBuilder,fieldLabel);
//            } catch (PersistenceException e) {
//                throw new RuntimeException(e);
//            }
//        }else logger.info("QueryBuilder is Null/Empty");
//
//    }
//
//    @Activate
//    protected void activate(TestSchedulerConfig config){
//        logger.info("Activate method triggered");
//    }
//
//
//    protected void executeQuery(ResourceResolver resolver, QueryBuilder queryBuilder,String fieldlabel) throws PersistenceException {
//        logger.info("Execution of Query started(executeQuery())");
//        String path = "/content/mysite/us/en";
//
//        Map<String, String> queryParams = new HashMap<>();
//        queryParams.put("path",path);
//        queryParams.put("property", "sling:resourceType");
//        queryParams.put("property.value", "mysite/components/myvideo");
//
//        SearchResult result = queryBuilder.createQuery(PredicateGroup.create(queryParams),resolver.adaptTo(Session.class)).getResult();
//        Iterator<Hit> hits = result.getHits().iterator();
//        if(!hits.hasNext()) logger.info("No Hits found for your Query Search");
////        Session session = resolver.adaptTo(Session.class);
//        while(hits.hasNext()){
//            Hit hit = hits.next();
//            try {
//                Resource resource = hit.getResource();
//                logger.info("Component found on resources = "+resource);
//                ModifiableValueMap valueMap = resource.adaptTo(ModifiableValueMap.class);
//                if(Objects.nonNull(valueMap))
//                    valueMap.put("fieldLabel",fieldlabel);
//                logger.info("Property added successfully to ->"+resource);
//            }catch (RepositoryException e) {
//                throw new RuntimeException(e);
//            }
//        }
////        session.logout();
//        resolver.commit();
//        resolver.close();
//
//    }
//}
