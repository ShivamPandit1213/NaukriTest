package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	private static Properties properties;

	private ConfigReader() {
	}

	private static void loadProperties() {
		if (properties == null) {
			properties = new Properties();
			try {
				FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
				properties.load(fis);
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("❌ Unable to load config.properties file.");
			}
		}
	}

	public static String get(String key) {
		loadProperties();
		String value = properties.getProperty(key);
		if (value == null) {
			throw new RuntimeException("❌ Property '" + key + "' not found in config.properties.");
		}
		return value;
	}
}
