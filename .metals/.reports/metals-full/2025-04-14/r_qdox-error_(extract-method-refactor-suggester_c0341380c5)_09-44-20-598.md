error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5034.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5034.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5034.java
text:
```scala
public v@@oid showDialog(final Throwable ex) {

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.

package org.columba.core.gui.util;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.columba.core.util.GlobalResourceLoader;
import org.columba.mail.gui.util.URLController;

public class ExceptionDialog implements ActionListener {

	public static final String CMD_CLOSE = "CLOSE";
	public static final String CMD_REPORT_BUG = "REPORT_BUG";
	private static final String RESOURCE_BUNDLE_PATH =
		"org.columba.core.i18n.dialog";

	private JDialog dialog;
	private boolean bool = false;
	private JTextField textField;
	private String stackTrace;

	public ExceptionDialog() {
	}

	public void showDialog(final Exception ex) {
		JLabel topLabel =
			new JLabel(
				GlobalResourceLoader.getString(
					RESOURCE_BUNDLE_PATH,
					"exception",
					"hint"),
				ImageLoader.getImageIcon("stock_dialog_error_48.png"),
				SwingConstants.LEFT);

		JLabel label =
			new JLabel(
				GlobalResourceLoader.getString(
					RESOURCE_BUNDLE_PATH,
					"exception",
					"message"));
		/*
		MultiLineLabel mlLabel = new MultiLineLabel( ex.getMessage() );
		mlLabel.setLineWrap( true );
		mlLabel.setWrapStyleWord( true );
		mlLabel.setColumns(70);
		mlLabel.setRows(3);
		*/

		JTextArea textArea = new JTextArea(ex.getMessage(), 3, 50);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);

		JLabel label2 =
			new JLabel(
				GlobalResourceLoader.getString(
					RESOURCE_BUNDLE_PATH,
					"exception",
					"stack_trace"));
		JTextArea textArea2 = new JTextArea(10, 50);
		StringWriter stringWriter = new StringWriter();
		ex.printStackTrace(new PrintWriter(stringWriter));
		stackTrace = stringWriter.toString();
		textArea2.append(stringWriter.toString());
		textArea2.setEditable(false);
		JScrollPane scrollPane2 = new JScrollPane(textArea2);

		ButtonWithMnemonic closeButton =
			new ButtonWithMnemonic(
				GlobalResourceLoader.getString("global", "global", "close"));
		closeButton.setActionCommand(CMD_CLOSE);
		closeButton.addActionListener(this);

		ButtonWithMnemonic reportBugButton =
			new ButtonWithMnemonic(
				GlobalResourceLoader.getString(
					RESOURCE_BUNDLE_PATH,
					"exception",
					"report_bug"));

		reportBugButton.setActionCommand(CMD_REPORT_BUG);
		reportBugButton.addActionListener(this);

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		dialog =
			DialogStore.getDialog(
				GlobalResourceLoader.getString(
					RESOURCE_BUNDLE_PATH,
					"exception",
					"title"));
		dialog.getContentPane().setLayout(layout);
		dialog.getRootPane().setDefaultButton(closeButton);

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1;
		c.insets = new Insets(10, 10, 0, 10);
		c.anchor = GridBagConstraints.WEST;
		layout.setConstraints(topLabel, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		c.insets = new Insets(10, 10, 0, 10);
		c.anchor = GridBagConstraints.WEST;
		layout.setConstraints(label, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 1;
		c.insets = new Insets(0, 10, 10, 10);
		c.anchor = GridBagConstraints.WEST;
		layout.setConstraints(scrollPane, c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 1;
		c.insets = new Insets(10, 10, 0, 10);
		c.anchor = GridBagConstraints.WEST;
		layout.setConstraints(label2, c);

		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 1.0;
		c.gridwidth = 1;
		//c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 10, 10);
		c.anchor = GridBagConstraints.WEST;
		layout.setConstraints(scrollPane2, c);

		JPanel panel = new JPanel();
		panel.add(closeButton);
		panel.add(reportBugButton);

		c.gridx = 0;
		c.gridy = 5;
		c.weightx = 1.0;
		//c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridwidth = 1;
		c.insets = new Insets(10, 10, 10, 5);
		c.anchor = GridBagConstraints.SOUTHEAST;
		layout.setConstraints(panel, c);

		/*
		c.gridx=GridBagConstraints.REMAINDER;
		c.gridy=4;
		c.weightx = 1.0;
		//c.gridwidth = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10,10,10,10);
		c.anchor = GridBagConstraints.WEST;
		layout.setConstraints(buttons[1], c);
		*/

		dialog.getContentPane().add(label);
		dialog.getContentPane().add(scrollPane);
		dialog.getContentPane().add(label2);
		dialog.getContentPane().add(scrollPane2);
		dialog.getContentPane().add(topLabel);
		dialog.getContentPane().add(panel);

		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.show();
	}

	public boolean success() {
		return bool;
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (CMD_CLOSE.equals(command)) {

			bool = true;

			dialog.dispose();
		} else if (CMD_REPORT_BUG.equals(command)) {
			bool = false;

			URLController c = new URLController();
			try {
				c.open(
					new URL("http://columba.sourceforge.net/phpBB2/viewforum.php?f=15"));
			} catch (MalformedURLException mue) {
			}
			/*
			dialog.dispose();
					
			ComposerFrame composer = new ComposerFrame();
					
			composer.setSubject("bug report:");
					
			composer.setTo("columba-bugs@lists.sourceforge.net");
					
			StringBuffer buf = new StringBuffer();
			StringWriter stringWriter2 = new StringWriter();
			exception.printStackTrace(
				new PrintWriter(stringWriter2));
			String str = new String(stringWriter2.toString());
			buf.append("\nTrace:\n");
			buf.append(str);
			buf.append("\n\n");
			buf.append("Columba version: "
				+ org.columba.core.main.MainInterface.version);
			buf.append("\n");
			buf.append("JDK version: "
					+ System.getProperty("java.version"));
			buf.append("\n");
			buf.append("JDK vendor: " + System.getProperty("java.vendor"));
			buf.append("\n");
			buf.append("OS version: " + System.getProperty("os.name"));
					
			composer.setBodyText(buf.toString());
					
			//dialog.dispose();
			*/
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5034.java