package jness.internationalizer;

import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import jness.internationalizer.executor.KoreanTextExtractor;
import jness.internationalizer.model.SupportExtension;

public class TextExtractionDialog extends Dialog {
	private Text textArea;
	private Set<String> allKoreans;

	public TextExtractionDialog(Shell shell, Set<String> allKoreans) {
		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE);

		this.allKoreans = allKoreans;
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(350, 500);
	}

	@Override
	public void create() {
		super.create();
		
		setData();
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("텍스트 추출 결과");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		textArea = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		textArea.setLayoutData(new GridData(GridData.FILL_BOTH));
		textArea.addKeyListener(SELECT_ALL_LISTENER);

		DropTarget dropTarget = new DropTarget(textArea, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dropTarget.addDropListener(DROP_TARGET_LISTENET);
		
		return composite;
	}
	
	public void setData() {
		if (textArea != null && !textArea.isDisposed()) {
			StringBuilder sb = new StringBuilder();
			for (String line : allKoreans) {
				sb.append(line).append("\n");
			}
			
			textArea.setText(sb.toString());
		}
	}
	
	private final DropTargetListener DROP_TARGET_LISTENET = new DropTargetAdapter() {
		@Override
		public void drop(DropTargetEvent event) {
			if (!(event.data instanceof String[])) {
				textArea.setText("");
				return;
			}

			String[] data = (String[]) event.data;
			if (data.length != 1) {
				textArea.setText("");
				return;
			}

			String filePath = data[0];
			String extension = FilenameUtils.getExtension(filePath);
			
			if (SupportExtension.contains(extension)) {
				Set<String> allKoreans = KoreanTextExtractor.extract(filePath);
	
				StringBuilder sb = new StringBuilder();
				for (String line : allKoreans) {
					sb.append(line).append("\n");
				}
	
				textArea.setText(sb.toString());
			} else {
				textArea.setText("");
			}
		}
	};

	private final KeyListener SELECT_ALL_LISTENER = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent event) {
			// Ctrl + A
			if (event.keyCode == 'a' && (event.stateMask & SWT.MODIFIER_MASK) == SWT.CTRL) {
				textArea.selectAll();
			}
		}
	};
}
