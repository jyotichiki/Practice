package com.mysite.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "OpenWeatherMap Configuration", description = "Configuration for OpenWeatherMap API")
public @interface OpenWeatherMapConfig {

    @AttributeDefinition(name = "API Key", description = "API Key for accessing OpenWeatherMap API")
    String apiKey() default "8ea177c0a93070dca5334724c455990e";

    @AttributeDefinition(name = "Base URL", description = "Base URL for OpenWeatherMap API")
    String baseUrl() default "https://api.openweathermap.org/data/2.5/weather?q=";


    @AttributeDefinition(name = "Unsplash Access Key", description = "Access key for Unsplash API")
    String unsplashAccessKey() default "z0SSos04pyw7jNCEyK-Sf_33UBsCZ3SJE0dzcia-XSo";

    @AttributeDefinition(name = "Unsplash API URL", description = "URL for Unsplash API")
    String unsplashApiUrl() default "https://api.unsplash.com/search/photos?client_id=";

}

