package jness.internationalization;

public enum SupportExtension {
	JAVA("java", "*.java"),
	JNS("jns", "*.jns"),
	JS("js", "*.js");
	//XML("xml", "*.xml");
	
	private String text;
	private String format;
	
	private SupportExtension(String text, String format) {
		this.text = text;
		this.format = format;
	}
	
	public String getText() {
		return text;
	}
	
	public static String[] formats() {
		String formats = "";
		for (SupportExtension e : values()) {
			formats = formats + e.format + ";";
		}
		
		return new String[] { formats };
	}
	
	public static boolean contains(String extension) {
		for (SupportExtension value : values()) {
			if (value.text.equalsIgnoreCase(extension)) {
				return true;
			}
		}
		
		return false;
	}
}
