package jness.internationalizer.executor;

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

import jness.internationalizer.model.ConstMsg;
import jness.internationalizer.model.SupportExtension;

public class InternationalizationExecutor extends PatternManager {
	private static Map<String, String> allProperty;
	private static List<String> allLines;
	private static String convertedLine;
	
	private static boolean isJava;
	private static boolean isJS;
	private static boolean isWrittenInConst;
	private static int index;
	
	private InternationalizationExecutor() {}
	
	public static void init() {
		// 'Internationalization' 버튼 선택 시 초기화
		allProperty = new HashMap<String, String>();
		isJava = false;
		isJS = false;
		isWrittenInConst = false;
		index = 0;
	}
	
	private static void initBeforeRun(File sourceFile) {
		allLines = new ArrayList<String>();
		
		String extension = FilenameUtils.getExtension(sourceFile.getAbsolutePath());
		boolean isJavaScript = SupportExtension.JS.getText().equals(extension);
		
		isJava = !isJavaScript;
		isJS = isJavaScript;
	}
	
	public static boolean run(File sourceFile, File targetFile, File propertyFile) {
		Map<String, String> newProperties = new HashMap<String, String>();
		String line;
		
		initBeforeRun(sourceFile);
		
		try {
        	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "UTF-8"));
			while ((line = br.readLine()) != null) {
				convertedLine = line;
				String trimLine = line.trim();
				
				if (isComment(trimLine) || isQuery(trimLine) || isLog(trimLine) || isDicKoreanChar(sourceFile, trimLine)) {
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
				
				Map<String, String> koreans = getNewProperties(line, sourceFile);
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
	
    private static Map<String, String> getNewProperties(String line, File sourceFile) {
    	// util.js의 msg인 경우 (메시지에 변수 처리 필요)
		if (isJSUtilMsg(sourceFile, line)) {
			return getJSUtilMsgProperty(line.trim());
		}
    				
		Map<String, String> newProperties = new HashMap<String, String>();
    	Pattern pattern = Pattern.compile(getExtractMessageRegex());
    	Matcher matcher = pattern.matcher(line);
    	
    	while (matcher.find()) {
    		String message = matcher.group();

    		boolean containsKorean = message.matches(getKoreanIncludedRegex());
    		if (!containsKorean) {
    			continue;
    		}
    		
    		// 해당 함수 파라미터에 포함된 경우 제외
			if (containsFunc(line, "StringUtil.isEquals", message)) {
				continue;
			}
			
			newProperties.putAll(getNewPropertiesBetweenChar(line, message));
    	}
    	
    	// Const.java 메시지가 포함된 경우
    	if (containsConstMsg(convertedLine)) {
    		convertedLine = getConstMsgConvertedString(convertedLine);
    	}
    	
    	// "" 또는 >< 사이가 아니지만, 한글이 포함된 라인인 경우 (HTML, JS에서 가능함)
    	if (convertedLine.equals(line) && line.matches(getKoreanIncludedRegex())) {
    		isJava = false;
    		
    		if (line.contains("//")) {
        		line = getCommentRemovedString(line);
        	}
    		
    		// 한글 정규식인 경우 
    		if (containsKoreanRegex(line)) {
    			return newProperties;
    		}
    		
    		if (line.matches(getKoreanIncludedRegex())) {
    			newProperties.putAll(getNewPropertiesInOtherLine(line.trim()));
    		}
    	}
    	
    	return newProperties;
    }
    
    /**
     * @param line
     * @return 변수처리 된 프로퍼티 맵
     */
    private static Map<String, String> getJSUtilMsgProperty(String line) {
    	Map<String, String> JSUtilMsgProperty = new HashMap<String, String>();
    	List<String> messages = new ArrayList<>();

    	boolean containsKorean = line.matches(getKoreanIncludedRegex());
		if (!containsKorean) {
			return new HashMap<String, String>();
		}
    	
    	Pattern pattern = Pattern.compile("\"(.*?)\"");
    	Matcher matcher = pattern.matcher(line);
    	
    	while (matcher.find()) {
    		messages.add(matcher.group());
    	}
    	
    	if (messages.isEmpty()) {
    		return new HashMap<String, String>();
    	}
    	
    	String newMessage = getNewJSUtilMsg(line, messages);
    	List<String> vars = getJSUtilMsgVars(line, messages);
    	String existingKey = getExistingKey(newMessage);
    	
		if (existingKey.isEmpty()) {
			String newKey = getNewKey();
			
			convertedLine = convertedLine.replace(line.split("msg = ")[1], getFormatMessageFunc(newKey, vars)) + ";";
			
			JSUtilMsgProperty.put(newKey, newMessage);
			allProperty.put(newKey, newMessage);

			index++;
		}
		else {
			convertedLine = convertedLine.replace(line.split("msg = ")[1], getFormatMessageFunc(existingKey, vars)) + ";";
		}
			
    	return JSUtilMsgProperty;
    }
    
    private static String getNewJSUtilMsg(String line, List<String> messages) {
    	String newMessage = "";
    	int varNum = 0;
    	
    	String removedMsg = line.substring(6).trim(); // 'msg = ' 제거
    	
    	if (!removedMsg.startsWith(messages.get(0))) { // 맨 앞에 변수가 있음
    		newMessage += "{" + varNum + "}";
    		varNum++;
    	}

    	for (String message : messages) {
    		newMessage += message.substring(1, message.length() - 1); // "" 제거

			if (removedMsg.endsWith(message + ";")) {
				break;
			}

			newMessage += "{" + varNum + "}";
			varNum++;
    	}
    	
    	return newMessage;
    }
    
    private static List<String> getJSUtilMsgVars(String line, List<String> messages) {
    	List<String> vars = new ArrayList<>();
    	
    	String removedStr = line.substring(6).trim(); // 'msg = ' 제거
    	String firstMsg = messages.get(0);
    	
    	// 맨 앞에 변수가 있음
    	if (!removedStr.startsWith(firstMsg)) {
    		String firstStr = removedStr.split(firstMsg)[0].trim();
    		String firstVar = firstStr.substring(0, firstStr.length() - 1).trim(); // 뒤에 '+' 제거
    		
    		vars.add(firstVar);
    	}
    	
    	// 문자열 사이에 변수가 있음
    	Pattern pattern =  Pattern.compile(getBetweenPlusRegex());
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			String var = matcher.group().trim();
			var = var.substring(2, var.length() - 2).trim(); // 양 옆에 "+ 와 +" 제거
			vars.add(var);
		}
    	
    	String lastMsg = messages.get(messages.size() - 1);
    	
    	// 맨 뒤에 변수가 있음
    	if (!removedStr.endsWith(lastMsg + ";")) {
    		String lastStr = removedStr.split(lastMsg)[1].trim();
    		String lastVar = lastStr.substring(1).trim(); // 앞에 '+' 제거
    		lastVar = lastVar.substring(0, lastVar.length() - 1).trim(); // 뒤에 ';' 제거
    		
    		vars.add(lastVar);
		}
    	
    	return vars;
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
    		return "jmbd.getMessage(\"" + key + "\")";
    	}
    	
    	return "<%=mbd.getMessage(\"" + key + "\")%>";
    }
    
    private static String getFormatMessageFunc(String key, List<String> vars) {
    	String result = "jmbd.getFormatMessage(\"" + key + "\"";
    	
    	for (String var : vars) {
    		result += ", " + var;
    	}
    	
    	return result + ")";
    }
    
    private static String getNewKey() {
    	String key = "message_";
    	
    	for (int i = 0; i < 4; i++) {
    		String indexStr = String.valueOf(index);
    		if (indexStr.length() - 1 < i) {
    			key += "0";
    		}
    	}
    	
    	return key + index;
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
			constMap = getSortedMap(ConstMsg.getPropertyMap());
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
}
