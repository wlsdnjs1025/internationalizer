package jness.internationalizer.model;

public enum Language {
	EN_US("영어", "us", "messages_en_US.properties"),
	JA_JP("일본어", "jp", "messages_ja_JP.properties");
	
	private final String text;
	private final String locale;
	private final String fileName;
	
	private Language(String text, String locale, String fileName) {
		this.text = text;
		this.locale = locale;
		this.fileName = fileName;
	}
	
	public String getText() {
		return text;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public static Language get(String locale) {
		for (Language lang : values()) {
			if (lang.locale.equalsIgnoreCase(locale)) {
				return lang;
			}
		}
		
		return EN_US;
	}
}
