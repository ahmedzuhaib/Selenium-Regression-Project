package com.examples.core.reporter;

import java.awt.AWTException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleSequence;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * 
 * @author Mohammed Imran
 * 
 */
public class Reporter {
    private File reportsDir;
    private Configuration cfg;
    private File currentTestDir;
    
    private Map<String, Serializable> testModel;

    public static final String ROOT_TEMPLATE_DIR = "com\\examples\\core\\reporter\\ftlFiles\\";    
    public static final String TEST_TEMPLATE = "test.ftl";    
    public String currentTestName;
    private SimpleDateFormat formatter;
    
    public Reporter(File reportsDir) throws AWTException, IOException {
        this.reportsDir = reportsDir;
        this.reportsDir.mkdirs();
      
        File templatesDir = new File(reportsDir, "templates");
        templatesDir.mkdirs();
                
        URL testFtlLocation = Thread.currentThread().getContextClassLoader().getResource(ROOT_TEMPLATE_DIR + TEST_TEMPLATE);       
        FileUtils.copyURLToFile(testFtlLocation, new File(templatesDir, TEST_TEMPLATE));

        URL includeFtl = Thread.currentThread().getContextClassLoader().getResource(ROOT_TEMPLATE_DIR + "simpleGallery.ftl");
        FileUtils.copyURLToFile(includeFtl, new File(templatesDir, "simpleGallery.ftl"));

        URL headerFtl = Thread.currentThread().getContextClassLoader().getResource(ROOT_TEMPLATE_DIR + "header.ftl");
        FileUtils.copyURLToFile(headerFtl, new File(templatesDir, "header.ftl"));
             
        cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(templatesDir);
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        
        formatter = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm:ss z");      
    }
    
    /**
     * This method will set up the jfreemaker templates required for our html page 
     */
    public void startTest() {                       
        currentTestDir = new File(reportsDir, currentTestName);
        currentTestDir.mkdirs();
        
        testModel = new HashMap<String, Serializable>();        
        testModel.put("name", currentTestName);
        testModel.put("screenshots", new SimpleSequence());        
        testModel.put("start", formatter.format(new Date()));        
    }

    /**
     * This method will produce a jquery slide show html page for the test that has been run
     */
    public void endTest() {        
        File summary = new File(currentTestDir, "index.html");
        Writer out = null;
        try {
            Template temp = cfg.getTemplate(TEST_TEMPLATE);        
            out = new FileWriter(summary);
            temp.process(testModel, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally { 
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 
     * @param browser     browser to prefix the image with
     * @param description description to show with the image
     * @param imageName   image name to save the image as
     * @param webdriver   webdriver instance
     */
    public void recordStep(String browser,String description,String imageName,WebDriver webdriver) {
    	
    	if(imageName.equals("")){
    		imageName = String.valueOf(System.currentTimeMillis());
    	}
    	
    	String screenShotName = browser + "_" + currentTestName + "_" + imageName + ".png";
		File screenShot = ((TakesScreenshot) webdriver).getScreenshotAs(OutputType.FILE);
        
		try {
			File screenShotDir = new File(new File(".").getCanonicalPath()+"\\SeleniumTestResults\\" + currentTestName + "\\" + screenShotName);
			FileUtils.copyFile(screenShot, screenShotDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        Map<String, String> thisScreenShot = new HashMap<String, String>();
        thisScreenShot.put("image", screenShotName);
        thisScreenShot.put("description", browser + "_"+ description);
        ((SimpleSequence) testModel.get("screenshots")).add(thisScreenShot);        
    }
}

