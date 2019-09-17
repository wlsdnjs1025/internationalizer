package jness.internationalization.extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextExtractor extends KoreanExtractor {
	private TextExtractor() {}

	public static Set<String> extract(String filePath) {
		Set<String> allKoreans = new HashSet<String>();
		
		File file = new File(filePath);
		String line;
		
		try {
        	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			while ((line = br.readLine()) != null) {
				line = line.trim();
				
				if (isComment(line) || isQuery(line) || isLog(line)) {
					continue;
				}
				
				allKoreans.addAll(getKoreans(line));
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return allKoreans;
	}
	
    private static List<String> getKoreans(String line) {
    	List<String> result = new ArrayList<String>();
    	
    	Pattern pattern = Pattern.compile(getExtractMessageRegex());
    	Matcher matcher = pattern.matcher(line);
    	
    	while (matcher.find()) {
    		String message = matcher.group();
    		boolean containsKorean = message.matches(getContainsKoreanRegex());
    		
    		if (!containsKorean) {
    			continue;
    		}
    		
    		// isEquals 함수 파라미터에 포함된
			if (containsFunc(line, "StringUtil.isEquals", message)) {
				continue;
			}
			
			String value = message.substring(1, message.length() - 1); // "" 또는 >< 제거
			result.add(getProperty(value));
    	}
    	
    	if (result.isEmpty() && line.matches(getContainsKoreanRegex())) {
    		if (line.contains("//")) {
        		line = getCommentRemovedString(line);
        	}
    		
    		if (line.matches(getContainsKoreanRegex())) {
    			result.add(getAnotherProperty(line));
    		}
    	}
    	
    	return result;
    }
    
    private static String getProperty(String value) {
		if (value.contains("&nbsp;")) {
			value = getNbspRemovedString(value);
		}
		
    	if (value.endsWith("\n")) {
    		value = value.substring(0, value.length() - 2);
    	}
		
		if (containsTag(value)) {
			value = getTagRemovedString(value);
		}
		
		if (remainJavaTag(value)) {
			value = getJavaTagRemovedString(value);
		}
		
		return value;
    }
    
    private static String getAnotherProperty(String value) {
    	if (value.contains("&nbsp;")) {
    		value = getNbspRemovedString(value);
		}
    	
    	if (value.endsWith("\n")) {
    		value = value.substring(0, value.length() - 2);
    	}

		if (containsTag(value)) {
			value = getTagRemovedString(value);
		}
		
		return value;
    }
}
