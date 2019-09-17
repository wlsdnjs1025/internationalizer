package jness.internationalization.executor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import jness.internationalization.model.TargetFile;

public class ProjectCopier {

	public static void copy(File sourceFile, File targetFile, File tempPropertyFile){
		for (File childFile : targetFile.listFiles()) {
			String existFileName = FilenameUtils.getBaseName(childFile.getAbsolutePath());
			String projectName = FilenameUtils.getBaseName(sourceFile.getAbsolutePath());
			
			if (existFileName.equalsIgnoreCase(projectName)) {
				MessageDialog.openError(Display.getDefault().getActiveShell(), 
						"Internationalization", "내보낼 경로에 선택한 프로젝트와 동일한 이름의 폴더가 존재합니다.");
				return;
			}
		}
		
		// 프로젝트 폴더 생성
		File projectFolder = new File(targetFile.getAbsolutePath() + File.separator + sourceFile.getName());
		projectFolder.mkdir();
		
		// 하위 모든 폴더 생성
		if (copyChildren(sourceFile, projectFolder, tempPropertyFile)) {
			File propertyFile = new File(sourceFile, "htdocs\\WEB-INF\\src\\messages\\messages_ko_KR.properties");
			ProjectCopier.copyFile(tempPropertyFile, propertyFile);
			tempPropertyFile.delete();
			
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), 
					"Internationalization", "완료되었습니다.");
		}
		else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(), 
					"Internationalization", "실패하였습니다.");
		}
	}
	
	public static boolean copyChildren(File sourceFile, File parentFile, File propertyFile){
		File[] childFiles = sourceFile.listFiles();
		
		for (File childFile : childFiles) {
			File newFile = new File(parentFile.getAbsolutePath() + File.separator + childFile.getName());
			
			if (childFile.isDirectory()) {
				// .git 폴더는 복사하지 않음
				if (childFile.getName().equals(".git")) {
					continue;
				}
				
				newFile.mkdir();
				
				boolean result = copyChildren(childFile, newFile, propertyFile);
				if (!result) {
					return false;
				}
				
				continue;
			}
			
			boolean result = copyFile(childFile, newFile, propertyFile);
			if (!result) {
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean copyFile(File sourceFile, File targetFile, File propertyFile) {
		if (TargetFile.contains(sourceFile)) {
			return InternationalizationExecutor.run(sourceFile, targetFile, propertyFile);
		}
		
		return copyFile(sourceFile, targetFile);
	}
	
	private static boolean copyFile(File sourceFile, File targetFile) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		try {
			fis = new FileInputStream(sourceFile);
			fos = new FileOutputStream(targetFile);
			
			byte[] b = new byte[4096];
			int cnt = 0;
			
			while ((cnt = fis.read(b)) != -1){
				fos.write(b, 0, cnt);
			}
			
			fis.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
