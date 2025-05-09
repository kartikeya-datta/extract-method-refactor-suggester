error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1994.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1994.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1994.java
text:
```scala
e@@xcelButton.setText("New Excel Sheet");

/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.ole.win32;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.widgets.*;

/**
 * OLEExample is an example that uses <code>org.eclipse.swt</code> 
 * libraries to implement a simple SWT window that can host different Active X
 * controls.
 *
 * @since 3.3
 */ 
public class OLEExample {

	OleClientSite clientSite;
	OleFrame oleFrame;
	Button closeButton;
	
	public static void main(String[] args) {
		Display display = new Display();
		OLEExample example = new OLEExample();
		example.open(display);
		display.dispose();
	}

	/** 
	 * Create a file Exit menu item 
	 */
	void addFileMenu(OleFrame frame) {
		final Shell shell = frame.getShell();
		Menu menuBar = shell.getMenuBar();
		if (menuBar == null) {
			menuBar = new Menu(shell, SWT.BAR);
			shell.setMenuBar(menuBar);
		}
		MenuItem fileMenu = new MenuItem(menuBar, SWT.CASCADE);
		fileMenu.setText("&File");
		Menu menuFile = new Menu(fileMenu);
		fileMenu.setMenu(menuFile);
		frame.setFileMenus(new MenuItem[] { fileMenu });
		
		MenuItem menuFileExit = new MenuItem(menuFile, SWT.CASCADE);
		menuFileExit.setText("Exit");
		menuFileExit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
	}

	void disposeClient() {
		if (clientSite != null)
			clientSite.dispose();
		clientSite = null;
	}

	/**
	 * Prompt the user for a file and try to open it with some known ActiveX controls.
	 */
	void fileOpen() {
		Shell shell = oleFrame.getShell();
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		String fileName = dialog.open();
		if (fileName == null) return;

		disposeClient();

		// try opening a .doc file using Word
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("doc") || 
						fileExtension.equalsIgnoreCase("rtf") ||
						fileExtension.equalsIgnoreCase("txt")) {
					try {
						clientSite = new OleClientSite(oleFrame, SWT.NONE, "Word.Document", new File(fileName));
					} catch (SWTException error2) {
						disposeClient();
					}
				}
			}
		}

		// try opening a xls file with Excel
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("xls")) {
					try {
						clientSite = new OleClientSite(oleFrame, SWT.NONE, "Excel.Sheet", new File(fileName));
					} catch (SWTException error2) {
						disposeClient();
					}
				}
			}
		}

		// try opening a media file with MPlayer
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("mpa")) {
					try {
						clientSite = new OleClientSite(oleFrame, SWT.NONE, "MPlayer", new File(fileName));
					} catch (SWTException error2) {
						disposeClient();
					}
				}
			}
		}

		// try opening with wmv, mpg, mpeg, avi, asf, wav with WMPlayer
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("wmv")
 fileExtension.equalsIgnoreCase("mpg")
 fileExtension.equalsIgnoreCase("mpeg")
 fileExtension.equalsIgnoreCase("avi")
 fileExtension.equalsIgnoreCase("asf")
 fileExtension.equalsIgnoreCase("wav")) {
					try {
						clientSite = new OleClientSite(oleFrame, SWT.NONE, "WMPlayer.OCX");
						OleAutomation player = new OleAutomation(clientSite);
						int playURL[] = player.getIDsOfNames(new String[] { "URL" });
						if (playURL != null) {
							boolean suceeded = player.setProperty(playURL[0], new Variant(fileName));
							if (!suceeded)
								disposeClient();
						} else {
							disposeClient();
						}
						player.dispose();
					} catch (SWTException error2) {
						disposeClient();
					}
				}
			}
		}

		// try opening a PDF file with Acrobat reader
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("pdf")) {
					try {
						clientSite = new OleClientSite(oleFrame, SWT.NONE, "PDF.PdfCtrl.5");
						clientSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
					    OleAutomation pdf = new OleAutomation (clientSite);
					    int loadFile[] = pdf.getIDsOfNames (new String [] {"LoadFile"});
					    if (loadFile != null) {
					    	Variant result = pdf.invoke(loadFile[0], new Variant[] {new Variant(fileName)});
							if (result == null)
								disposeClient();
							else
								result.dispose();
					    } else {
							disposeClient();
					    }
					    pdf.dispose();
					} catch (SWTException error2) {
						disposeClient();
					}
				}
			}
		}

		// try opening with Explorer
		if (clientSite == null) {
			try {
				clientSite = new OleClientSite(oleFrame, SWT.NONE, "Shell.Explorer");
				OleAutomation explorer = new OleAutomation(clientSite);
				int[] navigate = explorer.getIDsOfNames(new String[]{"Navigate"}); 
				
				if (navigate != null) {
					Variant result = explorer.invoke(navigate[0], new Variant[] {new Variant(fileName)});
					if (result == null)
						disposeClient();
					else
						result.dispose();
				} else {
					disposeClient();
				}
				explorer.dispose();
			} catch (SWTException error2) {
				disposeClient();
			}
		}

		if (clientSite != null){
			clientSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
		}
	}

	void newClientSite(String progID) {
		disposeClient();
		try {
			clientSite = new OleClientSite(oleFrame, SWT.NONE, progID);
		} catch (SWTException error) {

		}
		if (clientSite != null)
			clientSite.doVerb(OLE.OLEIVERB_SHOW);
	}

	public void open(Display display) {
		Shell shell = new Shell(display);
		shell.setText("OLE Example");
		shell.setLayout(new FillLayout());

		Composite parent = new Composite(shell, SWT.NONE);
		parent.setLayout(new GridLayout(4, true));
		
		Composite buttons = new Composite(parent, SWT.NONE);
		buttons.setLayout(new GridLayout());
		GridData gridData = new GridData(SWT.BEGINNING, SWT.FILL, false, false);
		buttons.setLayoutData(gridData);
		
		Composite displayArea = new Composite(parent, SWT.BORDER);
		displayArea.setLayout(new FillLayout());
		displayArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		Button excelButton = new Button(buttons, SWT.RADIO);
		excelButton.setText("New Exel Sheet");
		excelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.widget).getSelection())
					newClientSite("Excel.Sheet");
			}
		});
		Button mediaPlayerButton = new Button(buttons, SWT.RADIO);
		mediaPlayerButton.setText("New MPlayer");
		mediaPlayerButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.widget).getSelection())
					newClientSite("MPlayer");
			}
		});
		Button powerPointButton = new Button(buttons, SWT.RADIO);
		powerPointButton.setText("New PowerPoint Slide");
		powerPointButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.widget).getSelection())
					newClientSite("PowerPoint.Slide");
			}
		});
		Button wordButton = new Button(buttons, SWT.RADIO);
		wordButton.setText("New Word Document");
		wordButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.widget).getSelection())
					newClientSite("Word.Document");
			}
		});
		new Label(buttons, SWT.NONE);
		Button openButton = new Button(buttons, SWT.RADIO);
		openButton.setText("Open file...");
		openButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.widget).getSelection())
					fileOpen();
			}
		});
		new Label(buttons, SWT.NONE);
		closeButton = new Button(buttons, SWT.RADIO);
		closeButton.setText("Close file");
		closeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (((Button) e.widget).getSelection())
					disposeClient();
			}
		});	
		closeButton.setSelection(true);
		
		oleFrame = new OleFrame(displayArea, SWT.NONE);
		addFileMenu(oleFrame);

		shell.setSize(800, 600);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1994.java