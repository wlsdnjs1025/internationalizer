package jness.internationalization.model;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public enum TargetFile {
	WEB_SERVICE_USER("\\WebService\\User", true),
	//WEB_SERVICE_APPROVER("\\WebService\\Approver", true),
	//WEB_SERVICE_MANAGER("\\WebService\\Manager", true),
	
	// Approver
	HOME("\\Approver\\Home.jns", false),
	OT_REQUEST("\\Approver\\OTRequest.jns", false),
	APPROVER_MANAGE("\\Approver\\ApproverManage.jns", false),
	APPROVER_MANAGE_SEL("\\Approver\\ApproverManage.jns", false),
	
	// Manager
	WORKSET("\\Manager\\WorkSet.jns", false),
	WORKSET_EDIT("\\Manager\\WorkSet_Edit.jns", false),
	WORKSET_LOG("\\Manager\\WorkSet_Log.jns", false),
	WORKSET_MANAGE("\\Manager\\WorkSetManage.jns", false),
	WORKSET_MANAGE_EDITGRP("\\Manager\\WorkSetManage_EditGrp.jns", false),
	WORKSET_MANAGE_EDITDPT("\\Manager\\WorkSetManage_EditDpt.jns", false),
	WORKSET_MANAGE_EDITEMP("\\Manager\\WorkSetManage_EditEmp.jns", false),
	PRG_SETTING("\\Manager\\PrgSetting.jns", false),
	PRG_SETTING_LOG("\\Manager\\PrgSettingLog.jns", false),
	
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
		
		String filePath = file.getAbsolutePath(); 

		for (TargetFile targetFile : values()) {
			if (targetFile.isParent) {
				if (filePath.contains(targetFile.path)) {
					return true;
				}
			}
			else {
				if (filePath.endsWith(targetFile.path)) {
					return true;
				}
			}
		}
		
		return false;
	}
}
