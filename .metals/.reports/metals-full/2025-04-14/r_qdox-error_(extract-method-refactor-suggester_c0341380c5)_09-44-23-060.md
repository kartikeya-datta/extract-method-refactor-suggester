error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4691.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4691.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4691.java
text:
```scala
s@@uper(controller, "Window", ((DefaultFrameController) controller)

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
package org.columba.core.gui.globalactions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;

import org.columba.api.exception.PluginHandlerNotFoundException;
import org.columba.api.exception.PluginLoadingFailedException;
import org.columba.api.gui.frame.IContainer;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.frame.DefaultFrameController;
import org.columba.core.gui.frame.FrameManager;
import org.columba.core.gui.menu.IMenu;
import org.columba.core.plugin.PluginManager;
import org.columba.core.pluginhandler.FrameExtensionHandler;

/**
 * @author fdietz
 * 
 */
public class SwitchPerspectiveSubmenu extends IMenu implements ActionListener {

	private static final String MAIL_PERSPECTIVE = "ThreePaneMail";

	private static final String ADDRESSBOOK_PERSPECTIVE = "Addressbook";

	private static final String CHAT_PERSPECTIVE = "AlturaFrame";

	private JRadioButtonMenuItem mailMenu;

	private JRadioButtonMenuItem addressbookMenu;

	private JRadioButtonMenuItem chatMenu;

	private ButtonGroup group;

	private FrameExtensionHandler handler;

	/**
	 * @param controller
	 * @param caption
	 */
	public SwitchPerspectiveSubmenu(IFrameMediator controller) {
		super(controller, "Show View", ((DefaultFrameController) controller)
				.getViewItem().get("id"));

		String id = ((DefaultFrameController) getFrameMediator()).getViewItem()
				.get("id");

		// check if this is a management frame instance
		// -> if so create submenu to switch perspectives
		// -> otherwise, don't create submenu
		boolean isManagedFrame = false;

		try {
			handler = (FrameExtensionHandler) PluginManager.getInstance()
					.getHandler(FrameExtensionHandler.NAME);
		} catch (PluginHandlerNotFoundException e) {
			e.printStackTrace();
		}

		String[] managedFrames = null;
		if (id != null) {
			managedFrames = handler.getManagedFrames();
			for (int i = 0; i < managedFrames.length; i++) {
				if (id.equals(managedFrames[i]))
					isManagedFrame = true;
			}
		}

		if (!isManagedFrame)
			return;

		group = new ButtonGroup();

		for (int i = 0; i < managedFrames.length; i++) {
			JRadioButtonMenuItem menu = createMenu(managedFrames[i],
					managedFrames[i]);
			if (id.equals(managedFrames[i]))
				menu.setSelected(true);

			add(menu);
		}

	}

	/**
	 * @return
	 */
	private JRadioButtonMenuItem createMenu(String name, String actionCommand) {
		JRadioButtonMenuItem menu = new JRadioButtonMenuItem(name);
		group.add(menu);
		menu.setActionCommand(actionCommand);
		menu.addActionListener(this);
		return menu;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		final String action = arg0.getActionCommand();

		// awt-event-thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				IFrameMediator mediator = getFrameMediator();

				IContainer container = mediator.getContainer();

				try {
					FrameManager.getInstance().switchView(container, action);
				} catch (PluginLoadingFailedException e) {
					e.printStackTrace();
				}
			}
		});

		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4691.java