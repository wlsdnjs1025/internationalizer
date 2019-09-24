package jness.internationalizer.executor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import jness.internationalizer.App;
import jness.internationalizer.model.Language;
import jness.internationalizer.model.SortedProperties;

public class PropertyTranslator {
	private static Properties koDictionary;

	private PropertyTranslator() {}
	
	public static boolean run(File koPropertyFile, File exportPath, Language lang) {
		InputStream koDictionaryInputStream = App.class.getClassLoader().getResourceAsStream("resources/messages_ko_KR.properties");
		koDictionary = loadProperty(koDictionaryInputStream);
		
		Properties koProperty = loadProperty(koPropertyFile.getAbsolutePath());
		
		Properties translatedProperty = getTranslatedProperty(koProperty, lang);
		
		return writePropertyFile(exportPath, lang, translatedProperty);
	}
	
	public static Properties loadProperty(InputStream inputStream) {
		Properties properties = new Properties();
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			properties.load(br);
			
			inputStream.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return properties;
	}
	
	public static Properties loadProperty(String filePath) {
		Properties properties = new Properties();
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8"));
			properties.load(br);
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return properties;
	}
	
	private static Properties getTranslatedProperty(Properties koProperty, Language lang) {
		SortedProperties properties = new SortedProperties();
		
		InputStream distionaryInputStream = App.class.getClassLoader().getResourceAsStream("resources/" + lang.getFileName());
		Properties dictionary = loadProperty(distionaryInputStream);
		
		for (Object keyObj : koProperty.keySet()) {
			String key = (String) keyObj;
			String koStr = koProperty.getProperty(key);
			
			properties.setProperty(key, getValue(koStr, dictionary));
		}
		
		return properties;
	}
	
	private static String getValue(String koStr, Properties dictionary) {
		for (Object keyObj : koDictionary.keySet()) {
			String key = (String) keyObj;
			String value = koDictionary.getProperty(key);
			
			if (koStr.equalsIgnoreCase(value)) {
				return dictionary.getProperty(key);
			}
		}
		
		return koStr;
	}
	
	private static boolean writePropertyFile(File exportPath, Language lang, Properties newProperty) {
		try {
			File newFile = new File(exportPath, lang.getFileName());
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8"));
			newProperty.store(bw, lang.getFileName());

			bw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
