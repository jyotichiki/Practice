package com.mysite.core.servlets;


import com.mysite.core.config.OpenWeatherMapConfig;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component(
        service = { Servlet.class },
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.paths=/bin/getaddress"
        }
)
@Designate(ocd = OpenWeatherMapConfig.class)
public class GetAddressServlet extends SlingAllMethodsServlet {
    private static final Logger logger = LoggerFactory.getLogger(GetAddressServlet.class);
    private OpenWeatherMapConfig config;

    @Activate
    @Modified
    protected void activate(OpenWeatherMapConfig config) {
        this.config = config;
    }
    //private static final String WEATHER_API_KEY = "8ea177c0a93070dca5334724c455990e"; // Your OpenWeatherMap API key


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        // Get latitude and longitude from the request parameters
        String latitudeParam = request.getParameter("latitude");
        String longitudeParam = request.getParameter("longitude");
        if (latitudeParam != null && longitudeParam != null) {
            try {
                double latitude = Double.parseDouble(latitudeParam);
                double longitude = Double.parseDouble(longitudeParam);
                JSONObject address = fetchLocationData(latitude, longitude);
                response.setContentType("application/json");
                String city = address.optString("city");
                response.getWriter().write(address.toString());
                logger.info("Address is " + address);
            } catch (NumberFormatException e) {
                logger.error("Invalid latitude or longitude format");
                response.getWriter().write("Invalid latitude or longitude format");
            } catch (JSONException e) {
                logger.error("Error parsing JSON response: {}", e.getMessage(), e);
                response.getWriter().write("Error parsing JSON response: " + e.getMessage());
            }
        } else {
            logger.error("Latitude or longitude parameter is missing");
            response.getWriter().write("Latitude or longitude parameter is missing");
        }
    }

    private JSONObject fetchLocationData(double latitude, double longitude) throws IOException, JSONException {
        JSONObject jsonResponse = new JSONObject();
        String url = config.baseUrl() + latitude + "&lon=" + longitude + "&appid=" + config.apiKey();
        logger.info("Requesting geolocation data from URL: {}", url);
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to fetch data. Response code: " + responseCode);
        }
        StringBuilder responseBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
        }
        String jsonResponseString = responseBuilder.toString();

        // Attempt to parse response as JSON
        try {
            JSONObject jsonObject = new JSONObject(jsonResponseString);
            if (jsonObject.has("name")) {
                String cityName = jsonObject.getString("name");
                jsonResponse.put("city", cityName);
            } else {
                logger.error("City name not found in JSON response.");
            }
        } catch (JSONException e) {
            // If parsing fails, create a simple JSON object with the city name
            jsonResponse.put("city", jsonResponseString);
            logger.error("Error parsing JSON response: {}", e.getMessage(), e);
        }
        return jsonResponse;
    }
}
