package com.mysite.core.models;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;
import com.day.cq.dam.api.DamConstants;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Model(adaptables = {Resource.class,SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchImageModel {

    private static final Logger log = LoggerFactory.getLogger(SearchImageModel.class);

    @ValueMapValue
    private String keyword;

    @Self
    @Optional
    private SlingHttpServletRequest request;

    @Self
    @Optional
    private SlingHttpServletResponse response;


    public String getImage() throws IOException, RepositoryException, JSONException {
        String unsplashImageUrl = fetchImageFromUnsplash();

        if (unsplashImageUrl != null) {
            try {
                // Obtain a valid ResourceResolver instance
                ResourceResolver resourceResolver = request.getResourceResolver();
                uploadImageToDAM(unsplashImageUrl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return unsplashImageUrl;
    }





    public String fetchImageFromUnsplash() throws IOException, JSONException {
        String unsplashUrl = "https://api.unsplash.com/search/photos?query=" + keyword
                + "&client_id=wEVAsCpWrRBIX0aWGR8j0C0vC_hRFrLPOZRL_bsbA5k";
        log.info("unsplashUrl=" + unsplashUrl);

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet getRequest = new HttpGet(unsplashUrl);
        HttpResponse httpResponse = httpClient.execute(getRequest);
        log.info("response received successfully... :" + httpResponse);

        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            InputStream inputStream = httpResponse.getEntity().getContent();
            String jsonResponse = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            log.info("jsonResponse:" + jsonResponse);

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray("results");
            log.info("results:" + results);

            // Check if results array is not empty
            if (results.length() > 0) {
                JSONObject firstResult = results.getJSONObject(0);
                // Check if "urls" field exists
                if (firstResult.has("urls")) {
                    JSONObject urls = firstResult.getJSONObject("urls");
                    log.info("urls"+urls);
                    // Check if "raw" URL exists
                    if (urls.has("raw")) {
                        String imageUrl = urls.getString("raw");
                        log.info("image-url="+imageUrl);
                        // Return the raw version of the image
                        return imageUrl;
                    } else {
                        log.error("Raw image URL not found in JSON response.");
                    }
                } else {
                    log.error("URLs field not found in JSON response.");
                }
            } else {
                log.error("No results found in JSON response.");
            }
        } else {
            log.error("Failed to fetch image from Unsplash API. Status code: {}",
                    httpResponse.getStatusLine().getStatusCode());
        }

        return null;
    }


    private void uploadImageToDAM(String unsplashImageUrl) throws Exception {
        ResourceResolver resourceResolver = request.getResourceResolver();
        AssetManager assetManager = resourceResolver.adaptTo(AssetManager.class);
        if (assetManager != null) {
            // Create a title for the asset (can be customized as needed)
            String title = "Image from Unsplash";
            // Provide a name for the asset (can be customized as needed)
            String assetName = keyword+".jpg";

            // Fetch the InputStream of the image from the URL
            URL url = new URL(unsplashImageUrl);
            InputStream inputStream = url.openStream();

            // Create the asset node in the DAM
            Asset asset = assetManager.createAsset("/content/dam/mysite/" + assetName, inputStream, "image/jpeg", true);
            if (asset != null) {
                // Set the title for the asset
                ModifiableValueMap properties = asset.adaptTo(ModifiableValueMap.class);
                if (properties != null) {
                    properties.put(DamConstants.DC_TITLE, title);
                    // Set custom properties if needed
                    properties.put("customProperty", "customValue");
                }

                // Save the asset
                resourceResolver.commit();
                log.info("Image saved successfully in DAM at: " + asset.getPath());
            } else {
                log.error("Failed to create asset in DAM");
            }
        } else {
            log.error("AssetManager is null, unable to store image in DAM");
        }
    }



}
