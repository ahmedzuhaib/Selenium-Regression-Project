package com.examples.${package};

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.testng.annotations.Test;
import com.examples.core.Browsers;
import com.examples.core.BaseSeleniumTest;
import com.google.common.base.Supplier;
import org.apache.log4j.Logger;

@Browsers({ "FX" })

public class ${class} extends BaseSeleniumTest {
	static Logger logger = Logger.getLogger(${class}.class);

	@Test(dataProvider = "dataProviderMethod")
	public void testMethod(Supplier<WebDriver> supplier, String hostURL) {
	
		try {
			currentDriver = supplier.get();
			currentDriver.manage().window().maximize();
			
			selenium = new WebDriverBackedSelenium(currentDriver, hostURL);
			selenium.open("/");

			${script}
			
		} catch (Exception e) {
			e.printStackTrace(); // For debugging purposes
		}
	}
}