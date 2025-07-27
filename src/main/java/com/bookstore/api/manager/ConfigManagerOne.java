package com.bookstore.api.manager;

import java.io.*;
import java.util.Properties;

public class ConfigManagerOne {

	private static Properties properties = new Properties();

	// mvn clean install -Denv=qa/stage/dev/aut/dev
	// mvn clean install -- if env is not given then run the test cases on QA env by
	// default.

	// Support added for multiple environment:
//	private static final String envName = System.getProperty("env","qa");
	private static final String envName = null;
	
	private static final String CONFIG_FILE_PATH = getPath(envName);

	static {

		System.out.println(get("bookstore_baseurl"));
		try {
			File file = new File(CONFIG_FILE_PATH);
			if (!file.exists()) {
				System.out.println("Properties: ======> "+properties);
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

			try (InputStream input = new FileInputStream(file)) {
				properties.load(input);
			}
		} catch (IOException e) {
			System.out.println("Failed to load properties.");
			e.printStackTrace();
		}
	}

	public static String get(String key) {
		return properties.getProperty(key);
	}

	public static String getPath(String envName) {
		String configPath = null;
		if (envName == null) {
			configPath = "./src/test/resources/config/config.properties";
		} else {
			String fileName = "config_" + envName + ".properties";
			configPath = "./src/test/resources/" + fileName;
		}
		return configPath;
	}

	public static void set(String key, String value) {
		properties.setProperty(key, value);
		save();
	}

	private static void save() {
		try (OutputStream output = new FileOutputStream(CONFIG_FILE_PATH)) {
			properties.store(output, "Saved by ConfigManagerOne");
		} catch (IOException e) {
			System.out.println(" Failed to save properties.");
			e.printStackTrace();
		}
	}
}
