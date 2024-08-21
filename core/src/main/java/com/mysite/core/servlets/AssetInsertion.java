package com.mysite.core.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.Servlet;
import javax.servlet.ServletException;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;

@Component(service = Servlet.class, property = {
        "sling.servlet.paths=" + "/bin/uploadasset" })
public class AssetInsertion extends SlingSafeMethodsServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(AssetInsertion.class);

    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        InputStream is = null;
        String mimeType = "";
        try {
            // Path to the image file on your local drive
            File imageFile = new File("C:\\Users\\jyoti\\Downloads");
            is = new FileInputStream(imageFile);
            // Get MIME type of the image
            mimeType = "image/jpeg"; // Assuming it's JPEG, you can determine dynamically if needed
            // Get file extension
            String fileExt = "jpg"; // You can extract the file extension from the imageFile name if needed
            AssetManager assetManager = req.getResourceResolver().adaptTo(AssetManager.class);
            Asset imageAsset = assetManager.createAsset("/content/dam/mysite/rise" +
                    "."+fileExt, is, mimeType , true);

            resp.setContentType("text/plain");
            resp.getWriter().write("Image Uploaded = " + imageAsset.getName() +"  to this path ="+ imageAsset.getPath());
        }catch (Exception e) {
            log.error("error  occured while uploading the asset {}",e.getMessage());
        }finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.error("error  occured {}",e.getMessage());
            }
        }
    }


}
