package jness.internationalization.model;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public enum TargetFile {
	WEB_SERVICE("/WebService/"),
	JS_UTIL("/jworks/commom/js/");
	
	private final String parentPath;
	
	private TargetFile(String parentPath) {
		this.parentPath = parentPath;
	}
	
	private String getParentPath() {
		return parentPath;
	}
	
	public static boolean contains(File file) {
		String filePath = file.getAbsolutePath();
		String extension = FilenameUtils.getExtension(file.getName());
		
		for (TargetFile targetFile : values()) {
			if (filePath.contains(targetFile.getParentPath()) && SupportExtension.contains(extension)) {
				return true;
			}
		}
		
		return false;
	}
}
