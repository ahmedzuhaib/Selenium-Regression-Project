package com.examples.core.reporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.net.URL;
import org.apache.commons.io.FileUtils;

/**
 * 
 * @author Mohammed Imran
 * 
 */
public class IDEConverter {
	private File convDir = new File("SeleniumConversion//");
	private Configuration cfg;
	private File currentTestDir;

	private Map<String, Serializable> testModel;

	public static final String ROOT_TEMPLATE_DIR = "com\\examples\\core\\reporter\\ftlFiles\\";
	public static final String CONVERT_TEMPLATE = "convert.ftl";

	public IDEConverter() throws IOException {

		convDir.mkdirs();
		File templatesDir = new File(convDir, "templates");
		templatesDir.mkdirs();

		URL convertFtlLocation = Thread.currentThread().getContextClassLoader().getResource(ROOT_TEMPLATE_DIR + CONVERT_TEMPLATE);
		FileUtils.copyURLToFile(convertFtlLocation, new File(templatesDir,CONVERT_TEMPLATE));

		cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(templatesDir);
		cfg.setObjectWrapper(new DefaultObjectWrapper());

		readIDEScriptsDir();
	}

	public void readIDEScriptsDir() {
		try {
			File convertDir = new File(new File(".").getCanonicalPath()+ "\\convert");

			File[] files = convertDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".java");
				}
			});

			for (File ideScriptfile : files) {
				convertIDEGeneratedFile(ideScriptfile);
				ideScriptfile.delete();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void convertIDEGeneratedFile(File input) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(input));
		StringBuilder sb = new StringBuilder();

		String className = input.getName().replace(".java", "");
		String packageName = "";

		try {

			String line = br.readLine();

			while (line != null) {
				// extract the package name
				if (line.contains("package")) {
					packageName = line;
					packageName = packageName.substring(packageName.lastIndexOf(".")).replace(".", "").replace(";", "");
				}

				if (!line.contains("import") && !line.contains("open(\"/\"")
						&& line.contains("selenium")) {

					sb.append(line);
					sb.append("\n");

					// for any click actions insert a wait action
					if (line.contains("click")) {
						String spaceBeforeCommand = line.substring(0,line.indexOf("selenium"));
						sb.append(spaceBeforeCommand + "selenium.waitForPageToLoad(\"3000\");");
						sb.append("\n");
					}
				}
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		writeNewTest(packageName, className, sb.toString());
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		try {
			IDEConverter ID = new IDEConverter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method will set up the jfreemaker templates required for our IDE
	 */
	public void writeNewTest(String packageName, String className,String ideScript) {
		
		// make the new package if it does not exist!
		try {
			currentTestDir = new File(new File(".").getCanonicalPath()+ "\\src\\com\\examples\\", packageName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentTestDir.mkdirs();

		testModel = new HashMap<String, Serializable>();
		testModel.put("package", packageName);
		testModel.put("class", className);
		testModel.put("script", ideScript);

		File summary = new File(currentTestDir, className + ".java");
		Writer out = null;
		try {
			Template temp = cfg.getTemplate(CONVERT_TEMPLATE);
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
}
