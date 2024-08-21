//package com.mysite.core.servlets;
//
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//import javax.servlet.Servlet;
//import javax.servlet.ServletException;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.sling.api.SlingHttpServletRequest;
//import org.apache.sling.api.SlingHttpServletResponse;
//import org.apache.sling.api.resource.ResourceResolver;
//import org.apache.sling.api.resource.ResourceResolverFactory;
//import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.Reference;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.day.cq.dam.api.Asset;
//import com.day.cq.dam.api.AssetManager;
//// Import statements
//
//@Component(service = Servlet.class, property = {
//        "sling.servlet.paths=" + "/bin/getasset" })
//public class SearchAsset extends SlingSafeMethodsServlet {
//
//    private static final long serialVersionUID = 1L;
//    @Reference
//    private ResourceResolverFactory resolverFactory;
//    ResourceResolver resolver;
//    private static final Logger log = LoggerFactory.getLogger(SearchAsset.class);
//
//    @Override
//    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
//            throws ServletException, IOException {
//
//        String assetPath = "/content/dam/mysite/nature.jpeg/jcr:content/metadata/dc:format";
//        // Specify the path to your asset
//        InputStream inputStream = null;
//
//        try {
//            Map<String, Object> param = new HashMap<>();
//            param.put(ResourceResolverFactory.SUBSERVICE, "myuser3");
//            resolver = resolverFactory.getServiceResourceResolver(param);
//            // Adapt to AssetManager directly from ResourceResolver
//            AssetManager assetManager = resolver.adaptTo(AssetManager.class);
//            ThumbModel thumbModel = resolver.adaptTo(ThumbModel.class);
//            String thumbnailPath = thumbModel.getThumbnailPath();
//            log.info("Thumbnail Path "+thumbnailPath);
//            // Get the asset based on its path
//            Asset asset = assetManager.getAssetForBinary(assetPath);
//
//            if (asset != null) {
//                // Get the input stream of the asset's binary data
//                inputStream = asset.getOriginal().getStream();
//
//                // Set the content type of the response to match the asset's MIME type
//                response.setContentType(asset.getMimeType());
//
//                // Set content disposition to attachment to force download
//                response.setHeader("Content-Disposition", "attachment; filename=\"" + asset.getName() + "\"");
//
//                // Copy the input stream to the response output stream
//                IOUtils.copy(inputStream, response.getOutputStream());
//                response.getWriter().println("Asset is not null");
//            } else {
//                // Log a warning if the asset is not found
//                log.warn("Asset not found at: {}", assetPath);
//
//                // Asset not found
//                response.sendError(SlingHttpServletResponse.SC_NOT_FOUND, "Asset not found at: " + assetPath);
//            }
//
//        } catch (Exception e) {
//            // Log the error message and stack trace
//            log.error("Error occurred while retrieving the asset", e);
//            response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//                    "Error occurred while retrieving the asset: " + e.getMessage());
//        } finally {
//            if (inputStream != null) {
//                inputStream.close();
//            }
//            if (resolver != null && resolver.isLive()) {
//                resolver.close();
//            }
//        }
//    }
//}