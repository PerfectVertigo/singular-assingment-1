package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesLoader {
    private PropertiesLoader() {}

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in =
                     PropertiesLoader.class.getClassLoader()
                                           .getResourceAsStream("test-config.properties")) {
            if (in == null) {
                throw new IllegalStateException(
                        "Could not find test-config.properties on classpath");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to load test-config.properties", e);
        }
    }

    /** Returns the property value, or throws if it’s missing. */
    public static String getRequiredProperty(String key) {
        String val = PROPS.getProperty(key);
        if (val == null) {
            throw new IllegalStateException(
                    "Required property '" + key + "' is missing from test-config.properties");
        }
        return val;
    }
}

