package com.examples.core;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.examples.core.reporter.Reporter;

import static org.testng.Assert.*;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Supplier;
import com.thoughtworks.selenium.Selenium;

/**
 * 
 * @author Mohammed Imran
 * 
 */
public class BaseSeleniumTest {
	static Logger logger = Logger.getLogger(BaseSeleniumTest.class);
	private List<Supplier<WebDriver>> arraylist = new ArrayList<Supplier<WebDriver>>();
	private String newLine = System.getProperty("line.separator");
	private int testsTorun = Integer.valueOf(System.getProperty("testsTorun"));
	private int arraySize;
	private Object[][] data;
	private static int testsRun;
	protected WebDriver currentDriver;
	private static Reporter reporter;
	private Browsers testInBrowsers;
	private int declaredTests;
	private int declaredTestsRun;
	private boolean driversAdded;
	private String[] browsersToTest;
	protected Selenium selenium;

	public BaseSeleniumTest() {
		checkBrowsers(this);
	}

	private void checkBrowsers(Object x) {
		// Check Class
		if (x.getClass().isAnnotationPresent(Browsers.class)) {
			testInBrowsers = x.getClass().getAnnotation(Browsers.class);

			// first check if the class has Test annotation which means all
			// methods ARE test methods
			if (x.getClass().isAnnotationPresent(Test.class)) {
				declaredTests = x.getClass().getDeclaredMethods().length;
			} else {
				Method[] declaredMethods = x.getClass().getDeclaredMethods();

				for (Method currentMethod : declaredMethods) {
					if (currentMethod.isAnnotationPresent(Test.class)) {
						declaredTests++;
					}
				}
				logger.info("total test methods found:" + declaredTests);
			}

			browsersToTest = testInBrowsers.value();
			validateAnnotation("Class", x.getClass().getName());
			checkBrowserAnnotations("Class", x.getClass().getName());
		}
	}

	private void validateAnnotation(String annotationType, String name) {

		// check annotations here and if there valid do something
		if ((Arrays.asList(browsersToTest).contains("IE")
				|| Arrays.asList(browsersToTest).contains("FX") || Arrays
				.asList(browsersToTest).contains("CH"))
				&& Arrays.asList(browsersToTest).contains("ALL")) {

			assertEquals(false, true, "Found incorrect " + annotationType
					+ " annotation declared for " + name + ": ie OR fx OR ch");
		}
	}

	private void checkBrowserAnnotations(String annotationType, String name) {
		if (browsersToTest.length == 1) {
			logger.info(" "
					+ newLine
					+ "CHECKING IF ANY WEBDRIVERS HAVE NOT PREVIOUSLY BEEN ADDED:");
			checkAndAddBrowsers(browsersToTest[0]);
		} else {
			logger.info(" "
					+ newLine
					+ "CHECKING IF ANY WEBDRIVERS HAVE NOT PREVIOUSLY BEEN ADDED:");
			for (String findBrowsers : browsersToTest) {
				checkAndAddBrowsers(findBrowsers);
			}
		}
	}

	private void checkAndAddBrowsers(String browserToAdd) {

		if (WebDriverSupplier.hashmap.containsKey(browserToAdd)) {
			logger.info("BROWSER HAS ALREADY BEEN ADDED:" + browserToAdd);
		} else {
			logger.info("BROWSER NOT PRESENT SO ADDING BROWSER:" + browserToAdd);
			WebDriverSupplier.getWebDrivers(browserToAdd);
		}
	}

	private List<Supplier<WebDriver>> getActualSuppliers() {

		if (!driversAdded) {
			// check if only 1 browser (IE,FX,ALL) has been selected to test
			if (browsersToTest.length == 1) {
				addWebDriverInstance(browsersToTest[0]);
			} else {
				for (String currentBrowserToAdd : browsersToTest) {
					addWebDriverInstance(currentBrowserToAdd);
				}
			}
			logger.info("RETURN:" + arraylist.size());
			arraySize = arraylist.size();
			driversAdded = true;
		}

		return arraylist;
	}

	private void addWebDriverInstance(String browserToAdd) {

		for (Supplier<WebDriver> relevantWebDriver : WebDriverSupplier.hashmap.get(browserToAdd)) {
			arraylist.add(relevantWebDriver);
		}
	}

	/*
	 * Just take a screenshot with no specific image name or caption
	 */
	protected void takeScreenshot() {
		takeScreenshot("", "");
	}

	/*
	 * Just take a screenshot with specific image name and no caption
	 */
	protected void takeScreenshot(String imageName) {
		takeScreenshot(imageName, "");
	}

	protected void takeScreenshot(String imageName, String caption) {
		String browser = "";

		if (currentDriver instanceof InternetExplorerDriver) {
			browser = "IE";
		} else if (currentDriver instanceof FirefoxDriver) {
			browser = "FX";
		} else if (currentDriver instanceof ChromeDriver) {
			browser = "CH";
		}

		reporter.recordStep(browser, caption, imageName, currentDriver);
	}

	// A Data Provider is a method on your class that returns an array of array
	// of objects
	@DataProvider(name = "dataProviderMethod")
	public Object[][] parameterMethod() {

		int x = 0;
		// Create a new instance of each type of WebDriver(browser)
		for (final Supplier<WebDriver> supplier : getActualSuppliers()) {
			if (data == null) {
				data = new Object[arraySize][2];
			}
			data[x][0] = supplier;
			data[x][1] = WebDriverSupplier.getHostURL();
			x++;
		}
		return data;
	}

	@AfterSuite
	public void checkLast() {
		if (declaredTestsRun == declaredTests) {
			if (testsRun == testsTorun) {

				for (String currentWebDriver : WebDriverSupplier.hashmap.keySet()) {

					for (Supplier<WebDriver> supplier : WebDriverSupplier.hashmap.get(currentWebDriver)) {
						WebDriver currentDriver = supplier.get();

						if (currentDriver != null) {

							currentDriver.close();
							currentDriver.quit();
						}
					}
				}
			}
		}
	}

	@AfterMethod
	public void updateTestMethodsCount() {
		declaredTestsRun++;
		
		if(selenium != null){
			selenium.stop();
		}
		logger.info("finish TEST " + declaredTestsRun + " out of " + declaredTests + " to run");
	}

	@AfterClass
	public void updateTestCount() {
		testsRun++;
		logger.info("finished TEST SUITE " + testsRun + " out of " + testsTorun + " to run");
		reporter.endTest();
	}

	@BeforeClass
	public void setupReporter() {

		File directory = new File("SeleniumTestResults//");
		try {
			reporter = new Reporter(directory);
			reporter.currentTestName = this.getClass().getSimpleName();
			logger.info("SET UP REPORTER FOR  " + this.getClass().getSimpleName());
			reporter.startTest();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Directory for reports ex:" + ex);
		}
	}
}