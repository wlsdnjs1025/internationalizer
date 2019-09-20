package jness.internationalizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class App {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		
		shell.setText("Internationalizer");
//		shell.setImage(new Image(Display.getDefault(), App.class.getClassLoader().getResource("icon.png").getPath()));
		shell.setLayout(new FillLayout());
		shell.setBounds(100, 100, 400, 220);
		
		//new MenuCreator(shell);
		
		TabFolder tabFolder = new TabFolder(shell, SWT.BORDER);

	    TabItem projectTab = new TabItem(tabFolder, SWT.BORDER);
	    projectTab.setText("Internationalization");
	    projectTab.setControl(new ProjectView(tabFolder));
	    
	    TabItem translationTab = new TabItem(tabFolder, SWT.BORDER);
	    translationTab.setText("프로퍼티 번역");
	    translationTab.setControl(new TranslationView(tabFolder));
		
		shell.open();
		
		while (!shell.isDisposed()) {
		    if(!display.readAndDispatch()) {
		        display.sleep();
		    }
		}
		
		display.dispose();
	}
	
//	private static void addEventListeners(PropertyExtractionView view) {
//		EventManager.add(EventType.EXTRACT_TEXT, view);
//		EventManager.add(EventType.EXTRACT_PROPERTY, view);
//		EventManager.add(EventType.REFRESH_VIEW, view);
//		EventManager.add(EventType.CLEAR, view);
//	}
//	
//	private static void removeEventListeners(PropertyExtractionView view) {
//		EventManager.remove(EventType.EXTRACT_TEXT, view);
//		EventManager.remove(EventType.EXTRACT_PROPERTY, view);
//		EventManager.remove(EventType.REFRESH_VIEW, view);
//		EventManager.remove(EventType.CLEAR, view);
//	}
}
