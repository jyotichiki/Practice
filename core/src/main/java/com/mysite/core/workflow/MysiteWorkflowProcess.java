package com.mysite.core.workflow;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
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
public class MysiteWorkflowProcess implements WorkflowProcess {

    private static final Logger log = LoggerFactory.getLogger(MysiteWorkflowProcess.class);

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap processArguments) {
        log.info("Starting the Create Page Workflow Process");

        Session session = null;
        try {
            WorkflowData workflowData = workItem.getWorkflowData();
            if (workflowData.getPayloadType().equals("JCR_PATH")) {
                session = workflowSession.adaptTo(Session.class);

                // Hardcoded paths and properties for page creation
                String parentPath = "/content/mysite/us/en";
                String pageName = "workflow-create-page";
                String templatePath = "/conf/mysite/settings/wcm/templates/base_template";
                String pageTitle = "Workflow Created Page";

                createPage(session, parentPath, pageName, templatePath, pageTitle);
            }
        } catch (Exception e) {
            log.error("Error during page creation", e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    private void createPage(Session session, String parentPath, String pageName, String templatePath, String pageTitle) throws Exception {
        Node parentNode = session.getNode(parentPath);

        if (parentNode != null) {
            Node newPageNode = parentNode.addNode(pageName, "cq:Page");
            Node jcrContentNode = newPageNode.addNode("jcr:content", "cq:PageContent");
            jcrContentNode.setProperty("cq:template", templatePath);
            jcrContentNode.setProperty("jcr:title", pageTitle);
            jcrContentNode.setProperty("sling:resourceType", "mysite/components/structure/page");

            session.save();
            log.info("Page created successfully at: {}", newPageNode.getPath());
        } else {
            log.error("Parent path does not exist: {}", parentPath);
        }
    }
}