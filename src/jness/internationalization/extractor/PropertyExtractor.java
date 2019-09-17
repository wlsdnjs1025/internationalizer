package jness.internationalization.extractor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;

import jness.internationalization.SupportExtension;

public class PropertyExtractor extends KoreanExtractor {
	private static Map<String, String> allProperty;
	private static List<String> allLines;
	private static String convertedLine;
	
	private static boolean isJava;
	private static boolean isJS;
	private static boolean isWrittenInConst;
	private static int index;
	
	private PropertyExtractor() {}
	
	public static void init() {
		// 프로퍼티 추출 버튼 선택 시 초기화
		allProperty = new HashMap<String, String>();
		isJava = false;
		isJS = false;
		isWrittenInConst = false;
		index = 0;
	}
	
	public static boolean run(File sourceFile, File targetFile, File propertyFile) {
		allLines = new ArrayList<String>();
		String line;

		Map<String, String> newProperties = new HashMap<String, String>();
		
		String extension = FilenameUtils.getExtension(sourceFile.getAbsolutePath());
		if (SupportExtension.JAVA.getText().equals(extension)) {
			isJava = true;
			isJS = false;
		}
		
		if (SupportExtension.JS.getText().equals(extension)) {
			isJava = false;
			isJS = true;
		}
		
		try {
        	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "UTF-8"));
			while ((line = br.readLine()) != null) {
				convertedLine = line;
				String trimLine = line.trim();
				
				if (isComment(trimLine) || isQuery(trimLine) || isLog(trimLine)) {
					allLines.add(convertedLine);
					continue;
				}
				
				if (trimLine.startsWith("<%")) {
					isJava = true;
				}
				
				if (trimLine.startsWith("<script")) {
					if (!trimLine.contains("text/x-jquery-tmpl") && !trimLine.contains("text/jworks")) {
						isJS = true;
					}
				}
				
				Map<String, String> koreans = getNewProperties(line);
				newProperties.putAll(koreans);
				
				allLines.add(convertedLine);
				
				if (trimLine.endsWith("%>")) {
					isJava = false;
				}
				
				if (trimLine.endsWith("</script>")) {
					isJS = false;
				}
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		writeConvertedFile(sourceFile, targetFile);
		writePropertyFile(propertyFile, newProperties);
		
		return true;
	}
	
    private static Map<String, String> getNewProperties(String line) {
    	Map<String, String> newProperties = new HashMap<String, String>();
    	
    	Pattern pattern = Pattern.compile(getExtractMessageRegex());
    	Matcher matcher = pattern.matcher(line);
    	
    	while (matcher.find()) {
    		String message = matcher.group();

    		boolean containsKorean = message.matches(getContainsKoreanRegex());
    		if (!containsKorean) {
    			continue;
    		}
    		
    		// isEquals 함수 파라미터에 포함된 경우 제외
			if (containsFunc(line, "StringUtil.isEquals", message)) {
				continue;
			}
			
			newProperties.putAll(getNewPropertiesBetweenChar(line, message));
    	}
    	
    	// "" 또는 >< 사이가 아니지만, 한글이 포함된 라인인 경우
    	if (convertedLine.equals(line) && line.matches(getContainsKoreanRegex())) {
    		isJava = false;
    		
    		if (line.contains("//")) {
        		line = getCommentRemovedString(line);
        	}
    		
    		if (line.matches(getContainsKoreanRegex())) {
    			String value = line.trim();
    			newProperties.putAll(getNewPropertiesInOtherLine(value));
    		}
    	}
    	
    	return newProperties;
    }
    
    /**
     * @param line
     * @param message
     * @return "" 또는 >< 사이의 문자열에서 추출한 프로퍼티 맵
     */
    private static Map<String, String> getNewPropertiesBetweenChar(String line, String message) {
    	Map<String, String> newProperty = new HashMap<String, String>();
    	
    	String value = message.substring(1, message.length() - 1); // "" 또는 >< 제거
    	
    	if (message.startsWith(">") && message.endsWith("<")) {
    		value = getRemovedStr(value);
    	}
    	
    	String existingKey = getExistingKey(value);
    	
		if (existingKey.isEmpty()) {
			String newKey = getNewKey();
			
			if (isJava || containsJava(line, value) || isJS) {
				if (message.equals("\"" + value + "\"") ) {
					convertedLine = convertedLine.replace(message, getMessageFunc(newKey));
				}
				else {
					convertedLine = convertedLine.replace(value, getMessageFunc(newKey));
				}
			}
			else {
				convertedLine = convertedLine.replace(value, getMessageFunc(newKey));
			}
		
			newProperty.put(newKey, value);
			allProperty.put(newKey, value);

			index++;
		}
		else {
			if (isJava || containsJava(line, value) || isJS) {
				if (message.equals("\"" + value + "\"") ) {
					convertedLine = convertedLine.replace(message, getMessageFunc(existingKey));
				}
				else {
					convertedLine = convertedLine.replace(value, getMessageFunc(existingKey));
				}
	    	}
			else {
				convertedLine = convertedLine.replace(value, getMessageFunc(existingKey));
			}
		}
		
		return newProperty;
    }
    
    private static String getRemovedStr(String value) {
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
			isJava = false;
			value = getJavaTagRemovedString(value);
		}
    	
    	return value;
    }
    
    /**
     * @param value
     * @return "" 또는 >< 사이가 아니지만, 한글이 포함된 라인에서 추출한 프로퍼티 맵
     */
    private static Map<String, String> getNewPropertiesInOtherLine(String value) {
    	Map<String, String> newProperty = new HashMap<String, String>();
    	
    	value = getRemovedStr(value);
    	
		String existingKey = getExistingKey(value);
		
		if (existingKey.isEmpty()) {
			String newKey = getNewKey();
			convertedLine = convertedLine.replace(value, getMessageFunc(newKey));

			newProperty.put(newKey, value);
			allProperty.put(newKey, value);

			index++;
		}
		else {
			convertedLine = convertedLine.replace(value, getMessageFunc(existingKey));
		}
		
		return newProperty;
    }
    
    private static String getExistingKey(String message) {
    	for (String existingKey : allProperty.keySet()) {
			String value = allProperty.get(existingKey);
			
			if (value.equalsIgnoreCase(message)) {
				return existingKey;
			}
		}
    	
    	return "";
    }
    
    private static String getMessageFunc(String key) {
    	if (isJava) {
    		return "mbd.getMessage(\"" + key + "\")";
    	}
    	
    	if (isJS) {
    		return "msg.getMessage(\"" + key + "\")";
    	}
    	
    	return "<%=mbd.getMessage(\"" + key + "\")%>";
    }
    
    private static String getNewKey() {
    	String key = "message_";
    	
    	for (int i = 0; i < 4; i++) {
    		String indexStr = String.valueOf(index);
    		if (indexStr.length() - 1 < i) {
    			key += "0";
    		}
    	}
    	
    	return  key + index;
    }
    
    private static boolean containsJava(String line, String message) {
    	String regex = "<%(.*?)%>"; // <% %> 사이에 포함
    	Pattern pattern = Pattern.compile(regex);
    	Matcher matcher = pattern.matcher(line);
    	
    	while (matcher.find()) {
    		if (matcher.group().contains(message)) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    private static void writeConvertedFile(File sourceFile, File targetFile) {
    	try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));
			
			for (String str : allLines) {
				bw.append(str).append("\n");
			}
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	private static void writePropertyFile(File propertyFile, Map<String, String> propertyMap) {
		LinkedHashMap<String, String> constMap = new LinkedHashMap<String, String>();
		
		if (!isWrittenInConst) {
			// Const.java를 프로퍼티로 추출
			constMap = getSortedMap(getConstProperties());
		}
		
		LinkedHashMap<String, String> keyMap = getSortedMap(propertyMap);
    	
    	try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(propertyFile, true), "UTF-8"));
			
			if (!isWrittenInConst) {
				bw.append("# Const.java").append("\n");
				
				for (String key : constMap.keySet()) {
					bw.append(key).append("=").append(constMap.get(key)).append("\n");
				}
				
				bw.append("\n");
			}
			
			for (String key : keyMap.keySet()) {
				bw.append(key).append("=").append(keyMap.get(key)).append("\n");
			}
			
			bw.flush();
			bw.close();
			
			isWrittenInConst = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	private static LinkedHashMap<String, String> getSortedMap(Map<String, String> map) {
		return map.entrySet().stream()
		.sorted(Map.Entry.comparingByKey())
		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (ov, nv) -> ov, LinkedHashMap::new));
	}
	
	private static Map<String, String> getConstProperties() {
		Map<String, String> constMap = new HashMap<String, String>();
		
		constMap.put("message_C000", "PC-OFF 시스템");
		constMap.put("message_C001", "검색된 데이터가 없습니다.");
		constMap.put("message_C002", "사번");
		constMap.put("message_C003", "세션이 만료되었습니다.");
		constMap.put("message_C004", "비정상적으로 접속하셨습니다.");
		constMap.put("message_C005", "직위");
		constMap.put("message_C006", "추가된 데이터가 없습니다.");
		constMap.put("message_C007", "사용 권한이 없습니다.");
		
		return constMap;
	}
}
