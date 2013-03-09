package com.examples.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * This class is used to read configuration properties.
 * 
 * @author Mohammed Imran
 * 
 */
public class Utilities {
	static Logger logger = Logger.getLogger(Utilities.class);
	private static Properties cfgProps = new Properties();

	static {
		readProperties();
	}

	/**
	 * This method is used to read properties from a file
	 */
	public static void readProperties() {
		String cfgFile;
		try {
			cfgFile = new File(".").getCanonicalPath()+ "\\config\\config.properties";
			getProperties(cfgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param fileName
	 *            - name of property file to read
	 */
	public static void getProperties(String fileName) {
		try {
			FileInputStream in = new FileInputStream(new File(fileName));
			cfgProps.load(in);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @returns boolean that represents whether to use a custom profile
	 */
	public boolean useCustomProfile() {
		return cfgProps.getProperty("use.profile").equals("yes") ? true : false;
	}

	/**
	 * @returns String that represents the host url
	 */
	public String getHostURL() {
		return cfgProps.getProperty("host.url");
	}

	/**
	 * @returns String that represents the profile name
	 */
	public String getProfileName() {
		return cfgProps.getProperty("profile.name");
	}

	/**
	 * @returns String that represents the profile directory
	 */
	public String getProfileDir() {
		return cfgProps.getProperty("profile.directory");
	}
	
	/**
	 * @returns String that represents the machine bit type
	 */
	public String getBitType() {
		return cfgProps.getProperty("bit.type");
	}
}
