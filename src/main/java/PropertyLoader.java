import exceptions.PropertiesLoadingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class PropertyLoader {
    static Properties loadProperties() {
        String classpathFileName = "weather-app.properties";
        Properties properties = new Properties();
        try (InputStream resourceAsStream1 = WeatherApp.class.getClassLoader().getResourceAsStream(classpathFileName)) {
            properties.load(resourceAsStream1);
            return properties;
        } catch (IOException e) {
            throw new PropertiesLoadingException(e);
        }
    }
}
