error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7274.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7274.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7274.java
text:
```scala
i@@f (name.equals(item.getText())) {

/*
 *  @(#)CommandMenu.java
 *
 *  Project:		JHotdraw - a GUI framework for technical drawings
 *  http://www.jhotdraw.org
 *  http://jhotdraw.sourceforge.net
 *  Copyright:	© by the original author(s) and all contributors
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package CH.ifa.draw.contrib;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

import CH.ifa.draw.framework.JHotDrawRuntimeException;
import CH.ifa.draw.util.Command;
import CH.ifa.draw.util.CommandListener;
import CH.ifa.draw.util.CommandMenu;

/**
 * A Command enabled menu. Selecting a menu item
 * executes the corresponding command.
 *
 * @author    Eduardo Francos  (adapted from initial implementation by Wolfram Kaiser)
 * @created   2 mai 2002
 * @see       Command
 * @version   <$CURRENT_VERSION$>
 */
public class CTXCommandMenu extends JMenu implements ActionListener, CommandListener {

	public CTXCommandMenu(String name) {
		super(name);
	}

	/**
	 * Adds a command to the menu. The item's label is
	 * the command's name.
	 */
	public synchronized void add(Command command) {
		addMenuItem(new CommandMenuItem(command));
	}

	/**
	 * Adds a command with the given short cut to the menu. The item's label is
	 * the command's name.
	 */
	public synchronized void add(Command command, MenuShortcut shortcut) {
		addMenuItem(new CommandMenuItem(command, shortcut.getKey()));
	}

	/**
	 * Adds a command with the given short cut to the menu. The item's label is
	 * the command's name.
	 */
	public synchronized void addCheckItem(Command command) {
		addMenuItem(new CommandCheckBoxMenuItem(command));
	}

	/**
	 * Adds a command menu item
	 *
	 * @param item  the command menu item
	 */
	public synchronized void add(CommandMenuItem item) {
		addMenuItem(item);
	}

	/**
	 * Adds a command checkbox menu item
	 *
	 * @param checkItem  the checkbox item
	 */
	public synchronized void add(CommandCheckBoxMenuItem checkItem) {
		addMenuItem(checkItem);
	}

	/**
	 * Adds a normal menu item to the menu
	 *
	 * @param m  The menu item
	 */
	protected void addMenuItem(JMenuItem m) {
		m.addActionListener(this);
		add(m);
		((CommandHolder)m).getCommand().addCommandListener(this);
	}

	/**
	 * Removes a command item from the menu
	 *
	 * @param command  the command tor emove
	 */
	public synchronized void remove(Command command) {
		throw new JHotDrawRuntimeException("not implemented");
	}

	/**
	 * Removes an item from the menu
	 *
	 * @param item  the item to remove
	 */
	public synchronized void remove(MenuItem item) {
		throw new JHotDrawRuntimeException("not implemented");
	}

	/**
	 * Changes the enabling/disabling state of a named menu item.
	 *
	 * @param name   Description of the Parameter
	 * @param state  Description of the Parameter
	 */
	public synchronized void enable(String name, boolean state) {
		for (int i = 0; i < getItemCount(); i++) {
			JMenuItem item = getItem(i);
			if (name.equals(item.getLabel())) {
				item.setEnabled(state);
				return;
			}
		}
	}

	/** Description of the Method */
	public synchronized void checkEnabled() {
		int j = 0;
		for (int i = 0; i < getMenuComponentCount(); i++) {
			JMenuItem currentItem = getItem(i);
			if (currentItem instanceof CommandMenu) {
				((CommandMenu)currentItem).checkEnabled();
			}
			else if (currentItem instanceof CTXCommandMenu) {
				((CTXCommandMenu)currentItem).checkEnabled();
			}
			else if (currentItem instanceof CommandHolder) {
				currentItem.setEnabled(((CommandHolder)currentItem).getCommand().isExecutable());
			}
			else if (currentItem instanceof Command) {
				currentItem.setEnabled(((Command)currentItem).isExecutable());
			}
			j++;
		}
	}

	/**
	 * Executes the command.
	 *
	 * @param e  Description of the Parameter
	 */
	public void actionPerformed(ActionEvent e) {
		int j = 0;
		Object source = e.getSource();
		for (int i = 0; i < getItemCount(); i++) {
			// ignore separators
			// a separator has a hyphen as its label
			if (getMenuComponent(i) instanceof JSeparator) {
				continue;
			}
			JMenuItem item = getItem(i);
			if (source == item) {
				Command cmd = ((CommandHolder)item).getCommand();
				cmd.execute();
				break;
			}
			j++;
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param commandEvent  Description of the Parameter
	 */
	public void commandExecuted(EventObject commandEvent) {
//		checkEnabled();
	}

	/**
	 * Description of the Method
	 *
	 * @param commandEvent  Description of the Parameter
	 */
	public void commandExecutable(EventObject commandEvent) {
//		checkEnabled();
	}

	/**
	 * Description of the Method
	 *
	 * @param commandEvent  Description of the Parameter
	 */
	public void commandNotExecutable(EventObject commandEvent) {
//		checkEnabled();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7274.java