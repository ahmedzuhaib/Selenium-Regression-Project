package com.examples.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.google.common.base.Supplier;

/**
 * 
 * This class is a factory it returns a List of Supplier objects, uses one of
 * Google's utility interfaces that just returns an instance of an object.
 * 
 * @author Mohammed Imran
 * 
 */
public class WebDriverSupplier {
	static Logger logger = Logger.getLogger(WebDriverSupplier.class); 
	static Utilities util = new Utilities();
	static String machineBitType  = util.getBitType();
	
	// list of WebDrivers to run all 'tests' with
	static HashMap<String, ArrayList<Supplier<WebDriver>>> hashmap = 
        new HashMap<String, ArrayList<Supplier<WebDriver>>>();
	
	/**
	 * Generate a single instance of the Google Chrome Web Driver
	 * initialize it on first call, and return it on consequent calls
	 */
	private static final Supplier<WebDriver> CHROME_SUPPLIER = new Supplier<WebDriver>() {
		private WebDriver chromeDriver;
		private File currentDirectory;
		@Override
		public WebDriver get() {
			if (chromeDriver == null) {
			
				 if(System.getProperty("os.name").startsWith("Windows")){
					 currentDirectory = new File(new File(".").getAbsolutePath()+ "\\lib\\CHROME_Win_DriverServer\\chromedriver.exe");
				 }
				 else
				 {
					 currentDirectory = new File(new File(".").getAbsolutePath()+ "\\lib\\CHROME_Linux_"+machineBitType+"_DriverServer\\chromedriver.exe");
				 }
				System.setProperty("webdriver.chrome.driver", currentDirectory.getAbsolutePath());
				chromeDriver = new ChromeDriver();
				chromeDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			}
			return chromeDriver;
		}
	};

	/**
	 * Generate a single instance of the FireFox Web Driver
	 * initialize it on first call, and return it on consequent calls
	 */
	private  final static Supplier<WebDriver> FIREFOX_SUPPLIER = new Supplier<WebDriver>() {
		private WebDriver firefoxDriver;
		private FirefoxProfile profile;
		
		@Override
		public WebDriver get() {
			if (firefoxDriver == null) {
				
				//There are two ways to use a custom Firefox profile
				if(util.useCustomProfile()){
					
					//1) Assuming that the profile has been created using Firefox's profile manager 
					//   ie using "firefox -ProfileManager" on the pc we are running the test from:
					if(!util.getProfileName().equals("")){
						ProfilesIni allProfiles = new ProfilesIni();
						profile = allProfiles.getProfile(util.getProfileName());
						logger.info("using a custom profile on current pc");
					}
					//2) Alternatively, if the profile isn't already registered with Firefox on the pc we are running the test then provide 
					//   the path to the top level of a firefox profile folder
					else if (!util.getProfileDir().equals("")){
						File profileDir = new File(util.getProfileDir());
						profile = new FirefoxProfile(profileDir);
						logger.info("using a custom profile folder");
					}
					firefoxDriver = new FirefoxDriver(profile);
					firefoxDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				}
				else{
					firefoxDriver = new FirefoxDriver();
					firefoxDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				}
			}
			return firefoxDriver;
		}
	};

	/**
	 * Generate a single instance of the Internet Explorer Web Driver
	 * initialize it on first call, and return it on consequent calls
	 */
	private  final static Supplier<WebDriver> INTERNET_EXPLORER_SUPPLIER = new Supplier<WebDriver>() {
		private WebDriver ieDriver;
		
		@Override
		public WebDriver get() {
			if (ieDriver == null) {
				ieDriver = new InternetExplorerDriver();
				ieDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			}
			return ieDriver;
		}
	};
	
	/**
	 * 
	 * @return a list of WebDriver instances
	 */
	public static void getWebDrivers(String browsers) {
		
        logger.info("Requested to ADD a webbrowser supplier for :" + browsers);
		
		if(browsers.equals("IE")){
			if (System.getProperty("os.name").startsWith("Windows")) 
			{
				File currentDirectory = new File(new File(".").getAbsolutePath()+ "\\lib\\IE_"+machineBitType+"_DriverServer\\IEDriverServer.exe");
				System.setProperty("webdriver.ie.driver", currentDirectory.getAbsolutePath());
				
				final ArrayList<Supplier<WebDriver>> arraylist_IE = new ArrayList<Supplier<WebDriver>>();
				arraylist_IE.add(INTERNET_EXPLORER_SUPPLIER);
				hashmap.put("IE", arraylist_IE);
			}
		}
		else if(browsers.equals("FX"))
		{
			final ArrayList<Supplier<WebDriver>> arraylist_FX = new ArrayList<Supplier<WebDriver>>();
			arraylist_FX.add(FIREFOX_SUPPLIER);
			hashmap.put("FX", arraylist_FX);
		}
		else if(browsers.equals("CH"))
		{
			final ArrayList<Supplier<WebDriver>> arraylist_CH = new ArrayList<Supplier<WebDriver>>();
			arraylist_CH.add(CHROME_SUPPLIER);
			hashmap.put("CH", arraylist_CH);
		}
		else if(browsers.equals("ALL"))
		{
			final ArrayList<Supplier<WebDriver>> arraylist_ALL = new ArrayList<Supplier<WebDriver>>();
			
			if (System.getProperty("os.name").startsWith("Windows"))
			{
				File currentDirectory = new File(new File(".").getAbsolutePath()+ "\\lib\\IE_"+machineBitType+"_DriverServer\\IEDriverServer.exe");
				System.setProperty("webdriver.ie.driver", currentDirectory.getAbsolutePath());
				arraylist_ALL.add(INTERNET_EXPLORER_SUPPLIER);
			}
			arraylist_ALL.add(FIREFOX_SUPPLIER);
			arraylist_ALL.add(CHROME_SUPPLIER);
			hashmap.put("ALL", arraylist_ALL);
		}
		logger.info("ADDED webbrowser supplier for :" + browsers + " the complete map now contains the following:" + hashmap.keySet());
	}
	
	public static String getHostURL(){
		return util.getHostURL();
	}
}
	
