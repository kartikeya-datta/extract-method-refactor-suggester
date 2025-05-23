error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5985.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5985.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5985.java
text:
```scala
.@@getMiscIcon("trayicon.png");

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

package org.columba.core.gui.trayicon;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.JPopupMenu;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.base.OSInfo;
import org.columba.core.gui.base.SelfClosingPopupMenu;
import org.columba.core.gui.menu.MenuXMLDecoder;
import org.columba.core.io.DiskIO;
import org.columba.core.logging.Logging;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.core.shutdown.ShutdownManager;

/**
 * Uses the JDIC api to add a tray icon to the system default tray.
 * 
 * @author Timo Stich <tstich@users.sourceforge.net>
 */
public class ColumbaTrayIcon {

	private IFrameMediator frameMediator;

	/**
	 * Default icon for the TrayIcon.
	 */
	public static final Icon DEFAULT_ICON = ImageLoader
			.getImageIcon("trayicon.png");

	private static ColumbaTrayIcon instance = new ColumbaTrayIcon();

	private JPopupMenu menu;

	private TrayIconInterface activeIcon;

	protected ColumbaTrayIcon() {
		activeIcon = new DefaultTrayIcon();

	}

	/**
	 * Gets the instance of the ColumbaTrayIcon.
	 * 
	 * @return singleton instance
	 */
	public static ColumbaTrayIcon getInstance() {
		return instance;
	}

	/**
	 * Add the tray icon to the default system tray.
	 * 
	 */
	public void addToSystemTray(IFrameMediator frameMediator) {

		initPopupMenu();

		this.frameMediator = frameMediator;
		activeIcon.addToTray(DEFAULT_ICON, "Columba");
		activeIcon.setPopupMenu(menu);

		ShutdownManager.getInstance().register(new Runnable() {
			public void run() {
				ColumbaTrayIcon.getInstance().removeFromSystemTray();
			}

		});
	}

	/**
	 * Sets the tooltip of the tray icon.
	 * 
	 * @param tooltip
	 */
	public void setTooltip(String tooltip) {
		activeIcon.setTooltip(tooltip);
	}

	/**
	 * Sets the icon of the tray icon.
	 * 
	 * @param icon
	 */
	public void setIcon(Icon icon) {
		activeIcon.setIcon(icon);
	}

	/**
	 * Removes the tray icon from the system tray.s
	 */
	public void removeFromSystemTray() {
		activeIcon.removeFromTray();
	}

	private void initPopupMenu() {
		if (menu == null) {
			menu = new JPopupMenu();

			try {
				InputStream is = DiskIO
						.getResourceStream("org/columba/core/action/trayiconmenu.xml");

				menu = new MenuXMLDecoder(frameMediator).createPopupMenu(is);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// menu.add(new CMenuItem(new OpenNewMailWindowAction(null)));
			// menu.add(new CMenuItem(new
			// OpenNewAddressbookWindowAction(null)));
			// menu.addSeparator();
			// menu.add(new CMenuItem(new AboutDialogAction(null)));
			// menu.add(new CMenuItem(new ShowHelpAction(null)));
			// menu.addSeparator();
			// menu.add(new CMenuItem(new ExitAction(null)));

			new SelfClosingPopupMenu(menu);
		}
	}

	/**
	 * @return Returns the activeIcon.
	 */
	public TrayIconInterface getActiveIcon() {
		return activeIcon;
	}

	/**
	 * @param activeIcon
	 *            The activeIcon to set.
	 */
	public void initActiveIcon() {
		try {
			if (OSInfo.isLinux()) {
				activeIcon = new JDICTrayIcon();
			} else if (OSInfo.isWin32Platform()) {
				activeIcon = new JDICTrayIcon();
			} else if (OSInfo.isMac()) {
				// tray icon not supported on Mac
			}
		} catch (Exception e) {
			if (Logging.DEBUG)
				e.printStackTrace();

			activeIcon = new DefaultTrayIcon();
		} catch (Error e) {
			if (Logging.DEBUG)
				e.printStackTrace();

			activeIcon = new DefaultTrayIcon();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5985.java