package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {
    public static Properties getParserProperties(String properties) {
        try {
            Properties prop = new Properties();
            ClassLoader classLoader = Utils.class.getClassLoader();
            InputStream in = classLoader.getResourceAsStream(properties);
            prop.load(in);
            assert in != null;
            in.close();
            return prop;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
