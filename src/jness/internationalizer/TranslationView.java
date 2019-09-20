package jness.internationalizer;

import java.io.File;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import jness.internationalizer.model.Language;

public class TranslationView extends Composite {
	private Text koPropertyText;
	private Text targetText;
	private ComboViewer combo;
	private Button okButton;

	public TranslationView(Composite tabFolder) {
		super(tabFolder, SWT.NO_BACKGROUND);
		this.setLayout(new FillLayout());

		Composite mainComposite = new Composite(this, SWT.NONE);
		mainComposite.setLayout(new GridLayout());
		mainComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Composite composite = new Composite(mainComposite, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setFocus();
		
		createPropertyBrowseComposite(composite);
		createLanguageComposite(composite);
		createBrowseComposite(composite);
		createOkButtonComposite(mainComposite);
	}
	
	private void createPropertyBrowseComposite(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("한글 프로퍼티: ");
		
		koPropertyText = new Text(parent, SWT.BORDER);
		koPropertyText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		koPropertyText.setEditable(false);
		koPropertyText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				boolean valid = !koPropertyText.getText().isEmpty() && !targetText.getText().isEmpty();
				okButton.setEnabled(valid);
			}
		});
		
		Button browseButton = new Button(parent, SWT.PUSH);
		browseButton.setText("찾아보기...");
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
				dialog.setText("프로퍼티 파일 선택");
				dialog.setFilterPath("C:/");
				dialog.setFilterExtensions(new String[] { "*.properties" });

				String path = dialog.open();
				if (path == null || path.isEmpty()) {
					return;
				}
				
				koPropertyText.setText(path);
			}
		});
	}

	private void createBrowseComposite(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("내보낼 경로: ");

		targetText = new Text(parent, SWT.BORDER);
		targetText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		targetText.setEditable(false);
		targetText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				boolean valid = !koPropertyText.getText().isEmpty() && !targetText.getText().isEmpty();
				okButton.setEnabled(valid);
			}
		});
		
		Button browseButton = new Button(parent, SWT.PUSH);
		browseButton.setText("찾아보기...");
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DirectoryDialog dialog = new DirectoryDialog(Display.getDefault().getActiveShell(), SWT.OPEN);
				dialog.setText("내보낼 경로 선택");
				dialog.setFilterPath("C:/");

				String path = dialog.open();
				if (path == null || path.isEmpty()) {
					return;
				}
				
				targetText.setText(path);
			}
		});
	}
	
	private void createLanguageComposite(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("번역할 언어: ");
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginTop = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginBottom = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
		
		combo = new ComboViewer(composite);
		combo.getCombo().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.setContentProvider(ArrayContentProvider.getInstance());
		combo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Language) element).getText();
			}
		});
		
		combo.setInput(Language.values());
		combo.setSelection(new StructuredSelection(Language.EN_US));
	}
	
	private void createOkButtonComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		okButton = new Button(composite, SWT.PUSH);
		okButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		okButton.setText("번역하기");
		okButton.setEnabled(false);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				IStructuredSelection selection = (IStructuredSelection) combo.getSelection();
				File sourceFile = new File(koPropertyText.getText());
				Language lang = (Language) selection.getFirstElement();
				File targetFile = new File(targetText.getText());
			}
		});
	}
}
