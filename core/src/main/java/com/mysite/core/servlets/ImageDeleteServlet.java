package com.mysite.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;

@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=Delete the image from DAM",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/deleteasset" })
public class ImageDeleteServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ImageDeleteServlet.class);

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        String assetName = req.getParameter("assetName"); // Name of the asset to be deleted
        String path = req.getParameter("path"); // Path to search for the asset
        out.println("Asset Name: " + assetName);
        out.println("Path: " + path);

        ResourceResolver resourceResolver = req.getResourceResolver();
        AssetManager assetManager = resourceResolver.adaptTo(AssetManager.class);

        if (assetName == null || assetName.isEmpty() || path == null || path.isEmpty()) {
            resp.setContentType("text/plain");
            resp.getWriter().write("Please provide valid asset name and path parameters");
            return;
        }

        try {
            // Check if the asset exists at the specified path
            Asset existingAsset = assetManager.createAsset(path + "/" + assetName, null, null, false);
            out.println("existingAsset="+existingAsset);
            if (existingAsset != null) {
                // Remove the asset if it exists
                boolean removed = assetManager.removeAssetForBinary(path.substring("/content/dam".length()) + "/" + assetName);
                //boolean removed = assetManager.removeAssetForBinary(path + "/" + assetName);
                out.println("removed="+removed);
                if (removed) {
                    resp.setContentType("text/plain");
                    resp.getWriter().write("Image Deleted: " + assetName);
                } else {
                    resp.setContentType("text/plain");
                    resp.getWriter().write("Failed to delete image: " + assetName);
                }
            } else {
                resp.setContentType("text/plain");
                resp.getWriter().write("Image not found: " + assetName + " at path: " + path);
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting the asset: {}", e.getMessage(), e);
            resp.setContentType("text/plain");
            resp.getWriter().write("Error occurred while deleting the asset: " + e.getMessage());
        }
    }
}