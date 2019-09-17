package jness.internationalization;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class App {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		
		shell.setText("Internationalization");
//		shell.setImage(new Image(Display.getDefault(), App.class.getClassLoader().getResource("icon.png").getPath()));
		shell.setLayout(new FillLayout());
		shell.setBounds(100, 100, 400, 190);
		
		new MenuCreator(shell);
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new MainView(composite);
		
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
