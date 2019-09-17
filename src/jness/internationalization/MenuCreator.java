package jness.internationalization;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import jness.internationalization.extractor.TextExtractor;

public class MenuCreator {
	private final Shell shell;

	public MenuCreator(Shell shell) {
		this.shell = shell;
		createUI();
	}
	
	private void createUI() {
		Menu menuBar = new Menu(shell, SWT.BAR);
        MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
        cascadeFileMenu.setText("&텍스트 추출");
        
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        cascadeFileMenu.setMenu(fileMenu);
        
        createTextMenu(fileMenu);

        shell.setMenuBar(menuBar);
	}
	
	private void createTextMenu(Menu parentMenu) {
		MenuItem openMenu = new MenuItem(parentMenu, SWT.PUSH);
		openMenu.setText("&한글...");
		openMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setText("파일 선택");
				dialog.setFilterPath("C:/");
				dialog.setFilterExtensions(SupportExtension.formats());

				String path = dialog.open();
				if (path == null || path.isEmpty()) {
					return;
				}

				Set<String> allKoreans = TextExtractor.extract(path);
				TextExtractionDialog textArea = new TextExtractionDialog(shell, allKoreans);
				textArea.open();
			}
		});
	}
}
