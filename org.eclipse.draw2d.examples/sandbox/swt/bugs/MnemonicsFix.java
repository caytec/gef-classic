/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package swt.bugs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MnemonicsFix {

	/**
	 * Utility class for disabling mnemonics for Controls nested inside a composite
	 * whenever that composite is not the focus owner.
	 */
	static class MnemonicDisabler implements Listener {

		private final Control root;

		/**
		 * Constructs a MnemonicDisabler for the given Composite. Controls nested inside
		 * this composite will only be focusable via mnemonics after this composite has
		 * become the focus owner.
		 *
		 * @param root the activation root
		 */
		public MnemonicDisabler(Composite root) {
			this.root = root;
			root.addListener(SWT.Activate, this);
			root.addListener(SWT.Deactivate, this);
			if (!root.isFocusControl()) {
				hookFilter(root.getDisplay());
			}
			root.addDisposeListener(e -> unhookFilter(e.display));
		}

		private void unhookFilter(Display d) {
			d.removeFilter(SWT.Traverse, this);
		}

		@Override
		public void handleEvent(Event event) {
			switch (event.type) {
			case SWT.Activate:
				unhookFilter(event.display);
				break;
			case SWT.Deactivate:
				hookFilter(event.display);
				break;
			case SWT.Traverse:
				handleTraverse(event);
				break;
			default:
				break;
			}
		}

		private void handleTraverse(Event event) {
			// if doit == true, the control is the candidate to receive focus via mnemonics
			if ((event.detail == SWT.TRAVERSE_MNEMONIC) && (event.doit == true)) {
				Control control = (Control) event.widget;
				while (control != null) {
					if (control == root) {
						event.doit = false;
						return;
					}
					control = control.getParent();
				}
			}
		}

		private void hookFilter(Display display) {
			display.addFilter(SWT.Traverse, this);
		}

	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell();
		shell.setLayout(new GridLayout(2, false));

		Menu menubar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menubar);

		MenuItem help = new MenuItem(menubar, SWT.MENU);
		help.setText("&Help"); //$NON-NLS-1$

		Menu helpMenu = new Menu(help);
		help.setMenu(helpMenu);

		new MenuItem(helpMenu, 0).setText("About"); //$NON-NLS-1$

		Text editor = new Text(shell, SWT.BORDER | SWT.MULTI);
		editor.setText("import foo.bar;"); //$NON-NLS-1$
		editor.setLayoutData(new GridData(GridData.FILL_BOTH));
		editor.addTraverseListener(e -> {
			if (e.detail == SWT.TRAVERSE_TAB_NEXT) {
				e.doit = true;
			}
		});

		Group props = new Group(shell, 0);
		props.setText("properties view"); //$NON-NLS-1$
		props.setLayoutData(new GridData(GridData.FILL_BOTH));

		new MnemonicDisabler(props);

		props.setLayout(new GridLayout(2, false));
		new Label(props, 0).setText("&Width"); //$NON-NLS-1$
		new Text(props, SWT.BORDER).setText("50"); //$NON-NLS-1$
		new Label(props, 0).setText("&Height"); //$NON-NLS-1$
		new Text(props, SWT.BORDER).setText("30"); //$NON-NLS-1$

		props.addListener(SWT.Activate, event -> {
		});

		shell.setSize(500, 300);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}