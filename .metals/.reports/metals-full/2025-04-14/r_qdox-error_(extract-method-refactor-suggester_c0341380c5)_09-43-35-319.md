error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1374.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1374.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1374.java
text:
```scala
I@@mageLoader.getImageIcon("icon16.png").getImage());

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

package org.columba.core.gui.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.columba.core.config.ViewItem;
import org.columba.core.config.WindowItem;
import org.columba.core.gui.menu.Menu;
import org.columba.core.gui.toolbar.ToolBar;
import org.columba.core.gui.util.ImageLoader;

/**
 * 
 * The view is responsible for creating the initial frame, menu and
 * toolbar, statusbar.
 * 
 * 
 * @author fdietz
 */
public abstract class AbstractFrameView
	extends JFrame
	implements WindowListener {
		
	/**
	 * internally used toolbar ID
	 */
	public static final String MAIN_TOOLBAR = "main";
	
	/**
	 * 
	 * every view contains a reference to its creator
	 * 
	 */
	protected AbstractFrameController frameController;
	
	protected Menu menu;

	protected ToolBar toolbar;

	/**
	 * in order to support multiple toolbars we use a panel as
	 * parent container
	 */
	protected JPanel toolbarPane;

	public AbstractFrameView(AbstractFrameController frameController) {
		this.frameController = frameController;

		this.setIconImage(
			ImageLoader.getImageIcon("ColumbaIcon.png").getImage());

		setTitle(
			"Columba - version: "
				+ org.columba.core.main.MainInterface.version);


		JPanel panel = (JPanel) this.getContentPane();
		panel.setLayout(new BorderLayout());
		
		// add statusbar
		panel.add(frameController.getStatusBar(), BorderLayout.SOUTH);

		// add window listener
		addWindowListener(this);


		// add toolbar
		toolbarPane = new JPanel();
		toolbarPane.setLayout(new BoxLayout(toolbarPane, BoxLayout.Y_AXIS));
		panel.add(toolbarPane, BorderLayout.NORTH);

		// create menu
		menu = createMenu(frameController);
		if (menu != null)
			setJMenuBar(menu);

		// create toolbar
		toolbar = createToolbar(frameController);
		if ((toolbar != null) && (isToolbarVisible())) {
			toolbarPane.add(toolbar);
		}
	}

	
	/**
	 * 
	 * @return	true, if toolbar is enabled, false otherwise
	 * 
	 */
	public boolean isToolbarVisible() {

		return frameController.isToolbarEnabled(MAIN_TOOLBAR);
		
	}

	/**
	 * Load the window position, size and maximization state
	 *
	 */
	public void loadWindowPosition() {
		ViewItem viewItem = frameController.getViewItem();
		// *20030831, karlpeder* Also location is restored
		int x = viewItem.getInteger("window", "x");
		int y = viewItem.getInteger("window", "y");
		int w = viewItem.getInteger("window", "width");
		int h = viewItem.getInteger("window", "height");
		boolean maximized = viewItem.getBoolean("window", "maximized", true);

		// if window is maximized -> ignore the window size
		// properties
		if (maximized)
		WindowMaximizer.maximize(this);
		else {
			// otherwise, use window size property 
			Dimension dim = new Dimension(w, h);
			Point     p   = new Point(x, y); 
			setSize(dim);
			setLocation(p);

			validate();
		}
	}

	/**
	 * 
	 * Save current window position, size and maximization state
	 *
	 */
	public void saveWindowPosition() {

		java.awt.Dimension d = getSize();
		java.awt.Point   loc = getLocation();

		WindowItem item = frameController.getViewItem().getWindowItem();

		// *20030831, karlpeder* Now also location is stored
		//item.set("x", 0);
		//item.set("y", 0);
		item.set("x", loc.x);
		item.set("y", loc.y);
		item.set("width", d.width);
		item.set("height", d.height);

		boolean isMaximized = WindowMaximizer.isWindowMaximized(this);

		item.set("maximized", isMaximized);
	}

	/**
	 * Show toolbar
	 *
	 */
	public void showToolbar() {

		boolean b = isToolbarVisible();

		if (toolbar == null)
			return;

		if (b) {
			toolbarPane.remove(toolbar);
			frameController.enableToolbar(MAIN_TOOLBAR, false);

		} else {
			frameController.enableToolbar(MAIN_TOOLBAR, true);
			toolbarPane.add(toolbar);

		}

		validate();
		repaint();
	}
	/**
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent arg0) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent arg0) {

	}

	/**
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent arg0) {
		frameController.close();
	}

	/**
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent arg0) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent arg0) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent arg0) {
	}

	/**
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent arg0) {
	}
	/**
	 * @return Menu
	 */
	public Menu getMenu() {
		return menu;
	}

	/**
	 * Overwrite method to add custom menu.
	 * 
	 * Use core menu and plug all the mail/addressbook specific actions
	 * in the menu.
	 * 
	 * @param controller	controller of view
	 * @return				complete menu
	 */
	protected abstract Menu createMenu(AbstractFrameController controller);

	/**
	 * Overwrite method to add custom toolbar.
	 * 
	 * Use core toolbar and plug all the mail/addressbook specific actions
	 * in the toolbar.
	 * 
	 * @param controller	controller of view
	 * @return				complete toolbar
	 */
	protected abstract ToolBar createToolbar(AbstractFrameController controller);


	/**
	 * Return controller of this view
	 * 
	 * @return FrameController
	 */
	public AbstractFrameController getFrameController() {
		return frameController;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1374.java