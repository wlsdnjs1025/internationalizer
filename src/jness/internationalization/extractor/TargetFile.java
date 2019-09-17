package jness.internationalization.extractor;

import java.io.File;

public enum TargetFile {
	// User
	DASHBOARD("DashBoard.jns", "jns"),
	DASHBOARD_UI("DashBoard_UI.js", "js"),
	OT_EDIT("OT_Edit.jns", "jns"),
	OT_COMMAND("OT_Command.jns", "jns"),
	OT_UI("OT_UI.js", "js"),
	OT_VALIDATION("OT_Validation.xml", "xml"),
	REAUTH_EDIT("ReAuth_Edit.jns", "jns"),
	REAUTH_UI("ReAuth_UI.js", "js"),
	REAUTH_VALIDATION("ReAuth_Validation.xml", "xml"),
	
	// Approver
	HOME("Home.jns", "jns"),
	OT_REQUEST("OTRequest.jns", "jns"),
	APPROVER_MANAGE("ApproverManage.jns", "jns"),
	APPROVER_MANAGE_VALIDATION("ApproverManage_Validation.xml", "xml"),
	APPROVER_MANAGE_SEL("ApproverManage_Sel.jns", "jns"),
	
	// Manager
	WORKSET("WorkSet.jns", "jns"),
	WORKSET_EDIT("WorkSet_Edit.jns", "jns"),
	WORKSET_VALIDATION("WorkSet_Validation.xml", "xml"),
	WORKSET_LOG("WorkSet_Log.jns", "jns"),
	WORKSET_MANAGE("WorkSetManage.jns", "jns"),
	WORKSET_MANAGE_EDITGRP("WorkSetManage_EditGrp.jns", "jns"),
	WORKSET_MANAGE_VALIDATION("WorkSetManage_Validation.xml", "xml"),
	WORKSET_MANAGE_EDITDPT("WorkSetManage_EditDpt.jns", "jns"),
	WORKSET_MANAGE_EDITEMP("WorkSetManage_EditEmp.jns", "jns"),
	PRG_SETTING("PrgSetting.jns", "jns"),
	PRG_SETTING_VALIDATION("PrgSetting_Validation.xml", "xml"),
	PRG_SETTING_LOG("PrgSettingLog.jns", "jns");
	
	private final String fileName;
	private final String extension;
	
	private TargetFile(String fileName, String extension) {
		this.fileName = fileName;
		this.extension = extension;
	}
	
	private String getFileName() {
		return fileName;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public static boolean contains(File file) {
		String fileName = file.getName();

		if (HOME.getFileName().equals(fileName) && file.getParentFile().getName().equals("Manager")) {
			return false;
		}
		
		if (APPROVER_MANAGE.getFileName().equals(fileName) || APPROVER_MANAGE_VALIDATION.getFileName().equals(fileName) ||
				APPROVER_MANAGE_SEL.getFileName().equals(fileName)) {
			if (file.getParentFile().getName().equals("Manager")) {
				return false;
			}
		}
		
		for (TargetFile targetFile : values()) {
			if (targetFile.getFileName().equalsIgnoreCase(fileName)) {
				return true;
			}
		}
		
		return false;
	}
}
