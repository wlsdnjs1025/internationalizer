package jness.internationalization.executor;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jness.internationalization.model.ConstMsg;
import jness.internationalization.model.TargetFile;

public class PatternManager {
	public static boolean isComment = false;
	
	public static boolean isComment(String line) {
		if (line.startsWith("//")) {
			return true;
		}
		
		if (line.startsWith("/*") || line.startsWith("<!--") || line.startsWith("<%--")) {
			isComment = true;
		}
		
		if (line.endsWith("*/") || line.endsWith("-->") || line.endsWith("--%>")) {
			isComment = false;
			return true;
		}
		
		return isComment;
	}
	
	public static boolean isQuery(String line) {
		return line.startsWith("cmd.setComment") || line.startsWith("cmd.setQuery") ||
				line.startsWith("setComment") || line.startsWith("setQuery");
	}
	
	public static boolean isLog(String line) {
		return line.startsWith("log +=") || line.startsWith("daemon_dm_dspt");
	}
	
	public static boolean isJSUtilMsg(File sourceFile, String line) {
    	String sourceFileName = sourceFile.getName();
    	
    	if (!sourceFileName.equalsIgnoreCase(TargetFile.JS_UTIL.getPath())) {
    		return false;
    	}
    	
    	if (!line.trim().startsWith("msg = ")) {
    		return false;
    	}
    	
    	return true;
    }
	
    /**
		함수 파라미터에 str이 포함됨
     * @param line
     * @param func
     * @param str
     * @return
     */
    public static boolean containsFunc(String line, String func, String str) {
    	String regex = func + "\\(.*?\\\"(.*?)\\\"\\)";
    	Pattern pattern =  Pattern.compile(regex);
    	Matcher matcher = pattern.matcher(line);
    	
    	if (matcher.find()) {
    		return matcher.group().contains(str);
    	}
    	
    	return false;
    }
    
    /**
     * @param line
     * @param message
     * @return '<% %>' 사이에 포함된 문자가 있는지 검사
     */
    public static boolean containsJava(String line, String message) {
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
    
    /**
     * @param line
     * @return 태그가 포함된 문자열인지 검사
     */
    public static boolean containsTag(String line) {
		String regex = "<([^ㄱ-ㅎㅏ-ㅣ가-힣]+)>"; // <한글 이외의 문자> 인 경우.  ex) '<span>0:0</span>' or '<strong>'
		Pattern pattern =  Pattern.compile(regex);
		Matcher matcher = pattern.matcher(line);
		
		return matcher.find();
	}

	/**
	 * @return 한글 정규식이 포함된 라인인지 검사
	 */
	public static boolean containsKoreanRegex(String line) {
		String regex = "\\[.*ㄱ.*힣.*\\]";
		
		Pattern pattern =  Pattern.compile(regex);
		Matcher matcher = pattern.matcher(line);
		
		return matcher.find();
	}
	
    /**
     * @return 'Const.DEFAULT_'가 포함된 라인인지 검사
     */
    public static boolean containsConstMsg(String line) {
    	return line.contains("Const.DEFAULT_");
    }
	
    /**
     * @param line
     * @return 문자열에 '<%' 또는 '%>' 태그가 남았는지 검사
     */
	public static boolean remainJavaTag(String line) {
		String regex = "(.+)%>[ㄱ-ㅎㅏ-ㅣ가-힣]+"; // aaa%>한글
		Pattern pattern =  Pattern.compile(regex);
		Matcher matcher = pattern.matcher(line);
		
		return matcher.find();
	}

	public static String getCommentRemovedString(String line) {
    	String[] array = line.split("//");
		String comment = array[array.length - 1];
		return line.replace(comment, "").trim();
    }
    
    public static String getTagRemovedString(String line) {
    	String removedLine = line;

    	String regex = "<([^ㄱ-ㅎㅏ-ㅣ가-힣]+)>";
    	Pattern pattern =  Pattern.compile(regex);
    	Matcher matcher = pattern.matcher(line);
    	
    	while (matcher.find()) {
    		String tag = matcher.group();

    		if (removedLine.startsWith(tag)) {
    			removedLine = removedLine.substring(tag.length());
    		}
    		else if (removedLine.endsWith(tag)) {
    			removedLine = removedLine.substring(0, removedLine.length() - tag.length());
    		}
    	}
    	
    	return removedLine;
    }
    
    public static String getConstMsgConvertedString(String line) {
    	String convertedLine = line;
    	
    	String regex = "Const.DEFAULT_";
    	Pattern pattern =  Pattern.compile(regex);
		Matcher matcher = pattern.matcher(line);
		
		while (matcher.find()) {
			String constVar = ConstMsg.get(convertedLine);
			
			if (!constVar.isEmpty()) {
				String var = "Const." + constVar;
				convertedLine = convertedLine.replace(var, "mbd.getWord(" + var + ")");
			}
		}
		
		return convertedLine;
    }
    
    public static String getJavaTagRemovedString(String line) {
    	String regex = "(.+)%>[ㄱ-ㅎㅏ-ㅣ가-힣]+";
    	Pattern pattern =  Pattern.compile(regex);
    	Matcher matcher = pattern.matcher(line);
    	
    	if (matcher.find()) {
    		String tag = matcher.group();

    		if (line.startsWith(tag)) {
    			line = line.split("%>")[1];
    		}
    	}
    	
    	return line;
    }
    
    public static String getNbspRemovedString(String line) {
    	String nbsp = "&nbsp;";
    	
    	if (line.startsWith(nbsp)) {
    		line = line.substring(nbsp.length());
    		return getNbspRemovedString(line);
    	}
    	
    	if (line.endsWith(nbsp)) {
    		line = line.substring(0, line.length() - nbsp.length());
    		return getNbspRemovedString(line);
    	}
    	
    	return line;
    }
    
    /**
     * @return "" 또는 >< 사이에 포함
     */
    public static String getExtractMessageRegex() {
    	return "\"(.*?)\"|>(.*?)<";
    }
    
    /**
     * @return 한글 포함
     */
    public static String getKoreanIncludedRegex() {
    	return ".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*";
    }
    
    /**
     * @return "+ 와 +" 사이
     */
    public static String getBetweenPlusRegex() {
    	return "\"\\+(.*?)\\+\"";
    }
}
