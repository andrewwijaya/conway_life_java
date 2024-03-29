package com.drew.conway.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();
    public static final String GRID_WIDTH = "grid.width";
    public static final String GRID_HEIGHT = "grid.height";

    public static Properties getProperties(String path) throws IOException {
        URL resource = ConfigManager.class.getClassLoader().getResource(path);
        properties.load(new FileReader(new File(resource.getFile())));
        InputStream is = ConfigManager.class.getResourceAsStream("/" + path);
        properties.load(is);
        return properties;
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}

