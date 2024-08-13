package com.mysite.core.workflow;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.PageManagerFactory;
import com.day.cq.wcm.api.Template;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;

@Component(
        service = WorkflowProcess.class,
        immediate = true,
        property = {
                "process.label=Mysite Create Page Workflow Process",
                Constants.SERVICE_VENDOR + "=AEM Mysite",
                Constants.SERVICE_DESCRIPTION + "=Custom workflow step for creating a page."
        }
)
public class CreatePageWorkflowProcess implements WorkflowProcess {

    private static final Logger log = LoggerFactory.getLogger(CreatePageWorkflowProcess.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private PageManagerFactory pageManagerFactory;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap processArguments) {
        log.info("Starting the Create Page Workflow Process");

        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(null);
            PageManager pageManager = pageManagerFactory.getPageManager(resourceResolver);

            // Define the parent path, page name, template path, and page title
            String parentPath = processArguments.get("parentPath", String.class);
            String pageName = processArguments.get("pageName", String.class);
            String templatePath = processArguments.get("templatePath", String.class);
            String pageTitle = processArguments.get("pageTitle", String.class);

            if (parentPath != null && pageName != null && templatePath != null && pageTitle != null) {
                // Check if the parent path exists
                if (!ResourceUtil.isNonExistingResource(resourceResolver.getResource(parentPath))) {
                    Template template = pageManager.getTemplate(templatePath);
                    Page newPage = pageManager.create(parentPath, pageName, template.getPath(), pageTitle);

                    // Optional: Set additional properties on the new page
                    if (newPage != null) {
                        Node pageNode = newPage.adaptTo(Node.class);
                        pageNode.setProperty("jcr:title", pageTitle);
                        resourceResolver.commit();
                        log.info("Page created successfully at: {}", newPage.getPath());
                    } else {
                        log.error("Failed to create the page");
                    }
                } else {
                    log.error("Parent path does not exist: {}", parentPath);
                }
            } else {
                log.error("Missing required arguments: parentPath, pageName, templatePath, or pageTitle");
            }

        } catch (Exception e) {
            log.error("Error during page creation", e);
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
    }
}
