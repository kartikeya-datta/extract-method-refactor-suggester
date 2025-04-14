error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8662.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8662.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8662.java
text:
```scala
J@@Button cancelButton = new JButton(MailResourceLoader.getString("global", "cancel"));

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

package org.columba.mail.gui.config.general;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import org.columba.core.gui.config.themes.ThemePanel;

import org.columba.mail.util.MailResourceLoader;

public class GeneralOptionsDialog extends JDialog implements ActionListener
{
	JTabbedPane centerPane;
	GeneralPanel generalPanel;
	ComposerPanel composerPanel;
	ThemePanel themePanel;
	FontPanel fontPanel;

	JButton okButton;
	JButton cancelButton;
	boolean result;

	public GeneralOptionsDialog( JFrame frame )
	{
		//LOCALIZE
		super( frame, "General Options", true );
		initComponents();
		pack();
		setLocationRelativeTo(null);
	}

	public void updateComponents( boolean b )
	{
		generalPanel.updateComponents( b );
		composerPanel.updateComponents( b );
		themePanel.updateComponents( b );
		fontPanel.updateComponents( b );
	}


	protected void initComponents()
	{
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0,0));
		centerPane = new JTabbedPane();
		generalPanel = new GeneralPanel();
		//LOCALIZE
		centerPane.add( generalPanel, "General" );
		composerPanel = new ComposerPanel();
		//LOCALIZE
		centerPane.add( composerPanel, "Composer" );
		themePanel = new ThemePanel();
		//LOCALIZE
		centerPane.add( themePanel, "Themes and Icons" );
		fontPanel = new FontPanel();
		//LOCALIZE
		centerPane.add( fontPanel, "Fonts" );
		contentPane.add(centerPane, BorderLayout.CENTER);
		JPanel bottomPanel = new JPanel(new BorderLayout(0,0));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(17, 0, 11, 11));
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
		okButton = new JButton(MailResourceLoader.getString("global", "ok"));
		//mnemonic
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		buttonPanel.add(okButton);
		JButton cancelButton = new JButton(MailResourceLoader.getString("global", "ok"));
		//mnemonic
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		bottomPanel.add(buttonPanel, BorderLayout.EAST);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		setContentPane(contentPane);
		getRootPane().setDefaultButton(okButton);
		getRootPane().registerKeyboardAction(this,"CANCEL",
						KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),
						JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void actionPerformed(ActionEvent event)
	{
		String action = event.getActionCommand();

		if (action.equals("OK"))
		{
			result = true;
			setVisible(false);

		}
		else if (action.equals("CANCEL"))
		{
			result = false;
			setVisible(false);

		}
	}

	public boolean getResult()
	{
		return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8662.java