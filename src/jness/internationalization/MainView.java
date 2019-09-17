package jness.internationalization;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import jness.internationalization.extractor.ProjectCopier;
import jness.internationalization.extractor.PropertyExtractor;

public class MainView {
	private Text projectText;
	private Text targetText;
	private Button okButton;

	public MainView(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setFocus();
		
		projectText = createBrowseComposite(composite, "프로젝트");
		targetText = createBrowseComposite(composite, "내보낼 경로");
		createOkButtonComposite(parent);
	}

	private Text createBrowseComposite(Composite parent, String title) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(title + ": ");

		Text text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setEditable(false);
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				boolean valid = !projectText.getText().isEmpty() && !targetText.getText().isEmpty();
				okButton.setEnabled(valid);
			}
		});
		
		Button browseButton = new Button(parent, SWT.PUSH);
		browseButton.setText("찾아보기...");
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DirectoryDialog dialog = new DirectoryDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
				dialog.setText(title + " 선택");
				dialog.setFilterPath("C:/");

				String path = dialog.open();
				if (path == null || path.isEmpty()) {
					return;
				}
				
				text.setText(path);
			}
		});
		
		return text;
	}
	
	private void createOkButtonComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		okButton = new Button(composite, SWT.PUSH);
		okButton.setText("Internationalization");
		okButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		okButton.setEnabled(false);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				File sourceFile = new File(projectText.getText());
				File targetFile = new File(targetText.getText());
				File tempPropertyFile = new File(targetText.getText() + File.separator + "messages_ko.properties");
				
				PropertyExtractor.init();
				
				ProjectCopier.copy(sourceFile, targetFile, tempPropertyFile);
			}
		});
	}
}
