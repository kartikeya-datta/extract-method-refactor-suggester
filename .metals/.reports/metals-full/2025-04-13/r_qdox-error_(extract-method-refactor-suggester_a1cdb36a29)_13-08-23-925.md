error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9385.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9385.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9385.java
text:
```scala
i@@tem = new MenuItem (menu, SWT.PUSH);

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.javaviewer;


import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import java.util.*;
import java.io.*;
import java.text.*;

/**
 */
public class JavaViewer {  
	Shell shell;
	StyledText text;
	JavaLineStyler lineStyler = new JavaLineStyler();
	FileDialog fileDialog;
	static ResourceBundle resources = ResourceBundle.getBundle("examples_javaviewer");

Menu createFileMenu() {
	Menu bar = shell.getMenuBar ();
	Menu menu = new Menu (bar);
	MenuItem item;

	// Open 
	item = new MenuItem (menu, SWT.CASCADE);
	item.setText (resources.getString("Open_menuitem"));
	item.setAccelerator(SWT.MOD1 + 'O');
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			openFile();
		}
	});

	// Exit
	item = new MenuItem (menu, SWT.PUSH);
	item.setText (resources.getString("Exit_menuitem"));
	item.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			menuFileExit ();
		}
	});
	return menu;
}

void createMenuBar () {
	Menu bar = new Menu (shell, SWT.BAR);
	shell.setMenuBar (bar);

	MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
	fileItem.setText (resources.getString("File_menuitem"));
	fileItem.setMenu (createFileMenu ());

}

void createShell (Display display) {
	shell = new Shell (display);
	shell.setText (resources.getString("Window_title"));	
	GridLayout layout = new GridLayout();
	layout.numColumns = 1;
	shell.setLayout(layout);
	shell.addShellListener (new ShellAdapter () {
		public void shellClosed (ShellEvent e) {
			lineStyler.disposeColors();
			text.removeLineStyleListener(lineStyler);
		}
	});
}
void createStyledText() {
	text = new StyledText (shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
	GridData spec = new GridData();
	spec.horizontalAlignment = GridData.FILL;
	spec.grabExcessHorizontalSpace = true;
	spec.verticalAlignment = GridData.FILL;
	spec.grabExcessVerticalSpace = true;
	text.setLayoutData(spec);
	text.addLineStyleListener(lineStyler);
	text.setEditable(false);
	Color bg = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	text.setBackground(bg);
}

void displayError(String msg) {
	MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
	box.setMessage(msg);
	box.open();
}

public static void main (String [] args) {
	Display display = new Display();
	JavaViewer example = new JavaViewer ();
	Shell shell = example.open (display);
	while (!shell.isDisposed ())
		if (!display.readAndDispatch ()) display.sleep ();
	display.dispose ();
}

public Shell open (Display display) {
	createShell (display);
	createMenuBar ();
	createStyledText ();
	shell.setSize(500, 400);
	shell.open ();
	return shell;
}

void openFile() {	
	if (fileDialog == null) {
		fileDialog = new FileDialog(shell, SWT.OPEN);
	}

	fileDialog.setFilterExtensions(new String[] {"*.java", "*.*"});
	String name = fileDialog.open();
	
	open(name);
}

void open(String name) {
	final String textString;
	
	if ((name == null) || (name.length() == 0)) return;

	File file = new File(name);
	if (!file.exists()) {
		String message = MessageFormat.format(resources.getString("Err_file_no_exist"), new String[] {file.getName()});
		displayError(message);
		return;
	}

	try {
		FileInputStream stream= new FileInputStream(file.getPath());
		try {
			Reader in = new BufferedReader(new InputStreamReader(stream));
			char[] readBuffer= new char[2048];
			StringBuffer buffer= new StringBuffer((int) file.length());
			int n;
			while ((n = in.read(readBuffer)) > 0) {
				buffer.append(readBuffer, 0, n);
			}
			textString = buffer.toString();
			stream.close();
		}
		catch (IOException e) {
			// Err_file_io
			String message = MessageFormat.format(resources.getString("Err_file_io"), new String[] {file.getName()});
			displayError(message);
			return;
		}
	}
	catch (FileNotFoundException e) {
		String message = MessageFormat.format(resources.getString("Err_not_found"), new String[] {file.getName()});
		displayError(message);
		return;
	}
	// Guard against superfluous mouse move events -- defer action until later
	Display display = text.getDisplay();
	display.asyncExec(new Runnable() {
		public void run() {
			text.setText(textString);
		}
	});	
	
	// parse the block comments up front since block comments can go across
	// lines - inefficient way of doing this
	lineStyler.parseBlockComments(textString);
}

void menuFileExit () {
	shell.close ();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9385.java