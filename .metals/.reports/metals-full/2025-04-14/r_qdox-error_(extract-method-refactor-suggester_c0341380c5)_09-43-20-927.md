error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4326.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4326.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4326.java
text:
```scala
s@@uper(controller, "Show Headers","show_headers_menu");

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.gui.message.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;

import org.columba.core.action.IMenu;
import org.columba.core.config.DefaultItem;
import org.columba.core.config.IDefaultItem;
import org.columba.core.gui.frame.FrameMediator;
import org.columba.core.xml.XmlElement;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.config.MailConfig;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.table.action.ViewMessageAction;

/**
 * Submenu containing three choices: Show default headers, show custom headers
 * and show all available headers.
 * 
 * @author fdietz
 */
public class HeadersMenu extends IMenu implements ActionListener, Observer {

	private XmlElement element;

	private JRadioButtonMenuItem defaultMenuItem;

	private JRadioButtonMenuItem customMenuItem;

	private JRadioButtonMenuItem allMenuItem;

	/**
	 * @param controller
	 * @param caption
	 */
	public HeadersMenu(FrameMediator controller) {
		super(controller, "Show Headers");

		ButtonGroup group = new ButtonGroup();

		defaultMenuItem = new JRadioButtonMenuItem("Default Headers");
		defaultMenuItem.setActionCommand("DEFAULT");
		defaultMenuItem.addActionListener(this);
		group.add(defaultMenuItem);

		add(defaultMenuItem);

		customMenuItem = new JRadioButtonMenuItem("Custom Headers");
		customMenuItem.setActionCommand("CUSTOM");
		customMenuItem.addActionListener(this);
		group.add(customMenuItem);
		add(customMenuItem);

		allMenuItem = new JRadioButtonMenuItem("All Headers");
		allMenuItem.setActionCommand("ALL");
		allMenuItem.addActionListener(this);
		group.add(allMenuItem);
		add(allMenuItem);

		element = MailConfig.getInstance().get("options").getElement(
				"/options/headerviewer");
		element.addObserver(this);

		update(element, null);
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		// get current message list selection
		IMailFolderCommandReference r = ((MailFrameMediator) getFrameMediator())
				.getTableSelection();

		if (action.equals("DEFAULT")) {
			element.addAttribute("style", "0");

			new ViewMessageAction(getFrameMediator()).actionPerformed(null);
		} else if (action.equals("CUSTOM")) {
			element.addAttribute("style", "1");

			new ViewMessageAction(getFrameMediator()).actionPerformed(null);
		} else if (action.equals("ALL")) {
			element.addAttribute("style", "2");

			new ViewMessageAction(getFrameMediator()).actionPerformed(null);
		}
	}

	/**
	 * Method is called when configuration changes.
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public void update(Observable arg0, Object arg1) {
		IDefaultItem item = new DefaultItem(element);
		int style = item.getIntegerWithDefault("style", 0);
		switch (style) {
		case 0:
			defaultMenuItem.setSelected(true);
			break;
		case 1:
			customMenuItem.setSelected(true);
			break;
		case 2:
			allMenuItem.setSelected(true);
			break;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4326.java