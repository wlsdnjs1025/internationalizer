package jness.internationalization.model;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public enum TargetFile {
	IN_WEB_SERVICE("\\WebService\\", true),
	JS_UTIL("util.js", false),
	JS_BIZ("biz.js", false);
	
	private final String path;
	private final boolean isParent;
	
	private TargetFile(String path, boolean isParent) {
		this.path = path;
		this.isParent = isParent;
	}
	
	public String getPath() {
		return path;
	}
	
	public static boolean contains(File file) {
		String extension = FilenameUtils.getExtension(file.getName());
		
		if (!SupportExtension.contains(extension)) {
			return false;
		}
		
		for (TargetFile targetFile : values()) {
			if (targetFile.isParent) {
				if (file.getAbsolutePath().contains(targetFile.path)) {
					return true;
				}
			}
			else {
				if (file.getName().equalsIgnoreCase(targetFile.path)) {
					return true;
				}
			}
		}
		
		return false;
	}
}
