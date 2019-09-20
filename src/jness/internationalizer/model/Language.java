package jness.internationalizer.model;

public enum Language {
	EN_US("영어", "messages_en_US.properties"),
	JA_JP("일본어", "messages_ja_JP.properties");
	
	private final String text;
	private final String fileName;
	
	private Language(String text, String fileName) {
		this.text = text;
		this.fileName = fileName;
	}
	
	public String getText() {
		return text;
	}
}
