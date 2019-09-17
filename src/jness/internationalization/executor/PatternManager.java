package jness.internationalization.executor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public static boolean remainJavaTag(String line) {
		String regex = "(.+)%>[ㄱ-ㅎㅏ-ㅣ가-힣]+"; // aaa%>한글
		Pattern pattern =  Pattern.compile(regex);
		Matcher matcher = pattern.matcher(line);
		
		return matcher.find();
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
    
    public static boolean containsTag(String line) {
		String regex = "<([^ㄱ-ㅎㅏ-ㅣ가-힣]+)>"; // <한글 이외의 문자> 인 경우.  ex) '<span>0:0</span>' or '<strong>'
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
     * @return "" 또는 >< 사이에 포함되는 문자열
     */
    public static String getExtractMessageRegex() {
    	return "\"(.*?)\"|>(.*?)<";
    }
    
    /**
     * @return 한글 포함 문자열
     */
    public static String getKoreanIncludedRegex() {
    	return ".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*";
    }
}
