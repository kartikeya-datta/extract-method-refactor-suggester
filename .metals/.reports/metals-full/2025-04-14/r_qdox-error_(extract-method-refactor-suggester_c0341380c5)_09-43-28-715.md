error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16361.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16361.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16361.java
text:
```scala
i@@f (title != null) shell.setText(title);

/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class PromptDialog extends Dialog {
	
	public PromptDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public PromptDialog(Shell parent) {
		this(parent, 0);
	}
	
	public void confirmEx(String title, String text, String check, String button1, String button2, String button3, final int[] checkValue, final int[] result) {
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(title);
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);
		Label label = new Label(shell, SWT.WRAP);
		label.setText(text);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		label.setLayoutData (data);
		
		final Button[] buttons = new Button[4];
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				if (buttons[0] != null) checkValue[0] = buttons[0].getSelection() ? 1 : 0;
				Widget widget = event.widget;
				for (int i = 1; i < buttons.length; i++) {
					if (widget == buttons[i]) {
						result[0] = i - 1;
						break;
					}
				}
				shell.close();
			}	
		};
		if (check != null) {
			buttons[0] = new Button(shell, SWT.CHECK);
			buttons[0].setText(check);
			buttons[0].setSelection(checkValue[0] != 0);
			data = new GridData ();
			data.horizontalAlignment = GridData.END;
			buttons[0].setLayoutData (data);
		}
		Composite composite = new Composite(shell, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		composite.setLayoutData (data);
		composite.setLayout(new RowLayout());
		if (button1 != null) {
			buttons[1] = new Button(composite, SWT.PUSH);
			buttons[1].setText(button1);
			buttons[1].addListener(SWT.Selection, listener);
		}
		if (button2 != null) {
			buttons[2] = new Button(composite, SWT.PUSH);
			buttons[2].setText(button2);
			buttons[2].addListener(SWT.Selection, listener);
		}
		if (button3 != null) {
			buttons[3] = new Button(composite, SWT.PUSH);
			buttons[3].setText(button3);
			buttons[3].addListener(SWT.Selection, listener);
		}
		shell.pack();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}
	
	public void prompt(String title, String text, String check, final String[] value, final int[] checkValue, final int[] result) {
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(title);
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);
		Label label = new Label(shell, SWT.WRAP);
		label.setText(text);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		label.setLayoutData (data);
				
		final Text valueText = new Text(shell, SWT.BORDER);
		if (value[0] != null) valueText.setText(value[0]);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		valueText.setLayoutData(data);
				
		final Button[] buttons = new Button[3];
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				if (buttons[0] != null) checkValue[0] = buttons[0].getSelection() ? 1 : 0;
				value[0] = valueText.getText();
				result[0] = event.widget == buttons[1] ? 1 : 0;
				shell.close();
			}	
		};
		if (check != null) {
			buttons[0] = new Button(shell, SWT.CHECK);
			buttons[0].setText(check);
			buttons[0].setSelection(checkValue[0] != 0);
			data = new GridData ();
			data.horizontalAlignment = GridData.END;
			buttons[0].setLayoutData (data);
		}
		Composite composite = new Composite(shell, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		composite.setLayoutData (data);
		composite.setLayout(new RowLayout());
		buttons[1] = new Button(composite, SWT.PUSH);
		buttons[1].setText(SWT.getMessage("SWT_OK")); //$NON-NLS-1$
		buttons[1].addListener(SWT.Selection, listener);
		buttons[2] = new Button(composite, SWT.PUSH);
		buttons[2].setText(SWT.getMessage("SWT_Cancel")); //$NON-NLS-1$
		buttons[2].addListener(SWT.Selection, listener);

		shell.pack();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}	
	}
	public void promptUsernameAndPassword(String title, String text, String check, final String[] user, final String[] pass, final int[] checkValue, final int[] result) {
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(title);
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);
		Label label = new Label(shell, SWT.WRAP);
		label.setText(text);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		label.setLayoutData (data);
		
		Label userLabel = new Label(shell, SWT.NONE);
		userLabel.setText(SWT.getMessage("SWT_Username")); //$NON-NLS-1$
		
		final Text userText = new Text(shell, SWT.BORDER);
		if (user[0] != null) userText.setText(user[0]);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		userText.setLayoutData(data);
		
		Label passwordLabel = new Label(shell, SWT.NONE);
		passwordLabel.setText(SWT.getMessage("SWT_Password")); //$NON-NLS-1$
		
		final Text passwordText = new Text(shell, SWT.PASSWORD | SWT.BORDER);
		if (pass[0] != null) passwordText.setText(pass[0]);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		passwordText.setLayoutData(data);

		final Button[] buttons = new Button[3];
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				if (buttons[0] != null) checkValue[0] = buttons[0].getSelection() ? 1 : 0;
				user[0] = userText.getText();
				pass[0] = passwordText.getText();
				result[0] = event.widget == buttons[1] ? 1 : 0;
				shell.close();
			}	
		};
		if (check != null) {
			buttons[0] = new Button(shell, SWT.CHECK);
			buttons[0].setText(check);
			buttons[0].setSelection(checkValue[0] != 0);
			data = new GridData ();
			data.horizontalAlignment = GridData.END;
			buttons[0].setLayoutData (data);
		}
		Composite composite = new Composite(shell, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.END;
		composite.setLayoutData (data);
		composite.setLayout(new RowLayout());
		buttons[1] = new Button(composite, SWT.PUSH);
		buttons[1].setText(SWT.getMessage("SWT_OK")); //$NON-NLS-1$
		buttons[1].addListener(SWT.Selection, listener);
		buttons[2] = new Button(composite, SWT.PUSH);
		buttons[2].setText(SWT.getMessage("SWT_Cancel")); //$NON-NLS-1$
		buttons[2].addListener(SWT.Selection, listener);

		shell.pack();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16361.java