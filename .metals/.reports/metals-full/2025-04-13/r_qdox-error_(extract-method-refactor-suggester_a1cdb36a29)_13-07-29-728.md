error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1302.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1302.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1302.java
text:
```scala
.@@getExtensionHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_FRAME);

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

package org.columba.core.gui.frame;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JFrame;

import org.columba.api.exception.PluginException;
import org.columba.api.exception.PluginHandlerNotFoundException;
import org.columba.api.exception.PluginLoadingFailedException;
import org.columba.api.gui.frame.IContainer;
import org.columba.api.gui.frame.IFrameManager;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.plugin.IExtension;
import org.columba.api.plugin.IExtensionHandler;
import org.columba.api.plugin.IExtensionHandlerKeys;
import org.columba.core.config.Config;
import org.columba.core.config.ViewItem;
import org.columba.core.logging.Logging;
import org.columba.core.plugin.PluginManager;
import org.columba.core.shutdown.ShutdownManager;
import org.columba.core.xml.XmlElement;

/**
 * FrameManager manages all frames. It keeps a list of every controller. Its
 * also the place to create a new frame, or save and close all frames at once.
 * 
 * Frame controllers are plugins.
 * 
 * @see FrameExtensionHandler
 * 
 * @author fdietz
 */
public class FrameManager implements IFrameManager {

	private static final Logger LOG = Logger
			.getLogger("org.columba.core.gui.frame");

	/** list of frame controllers */
	protected List activeFrameCtrls = new LinkedList();

	/** viewlist xml treenode */
	protected XmlElement viewList = Config.getInstance().get("views")
			.getElement("/views/viewlist");

	/** Default view specifications to be used when opening a new view */
	protected XmlElement defaultViews = Config.getInstance().get("views")
			.getElement("/views/defaultviews");

	protected IExtensionHandler handler;

	private static FrameManager instance = new FrameManager();

	/**
	 * we cache instances for later re-use
	 */
	protected Map frameMediatorCache;

	/**
	 * Obtains a reference to the frame plugin handler and registers a shutdown
	 * hook with the ShutdownManager.
	 */
	public FrameManager() {

		frameMediatorCache = new HashMap();

		// get plugin handler for handling frames
		try {
			handler = PluginManager.getInstance()
					.getHandler(IExtensionHandlerKeys.ORG_COLUMBA_CORE_FRAME);
		} catch (PluginHandlerNotFoundException ex) {
			throw new RuntimeException(ex);
		}

		// this is executed on shutdown: store all open frames so that they
		// can be restored on the next start
		ShutdownManager.getInstance().register(new Runnable() {
			public void run() {
				storeViews();
			}
		});
	}

	public static FrameManager getInstance() {
		return instance;
	}

	/**
	 * Close all frames and re-open them again.
	 * <p>
	 * This is necessary when updating translations, adding new plugins which
	 * extend the menu and probably also look and feel changes.
	 * 
	 */
	public void refresh() {
		storeViews();
		openStoredViews();
	}

	/**
	 * Store all open frames so that they can be restored on next startup.
	 * 
	 */
	public void storeViews() {
		// used to temporarily store the values while the original
		// viewList gets modified by the close method
		List newViewList = new LinkedList();

		ViewItem v;

		// we cannot use an iterator here because the close method
		// manipulates the list
		while (activeFrameCtrls.size() > 0) {
			DefaultContainer c = (DefaultContainer) activeFrameCtrls.get(0);
			v = c.getViewItem();

			// store every open frame in our temporary list
			newViewList.add(v.getRoot());

			// close every open frame
			c.close();
		}

		// if not we haven't actually closed a frame, leave viewList as is
		if (newViewList.size() > 0) {
			// the close method manipulates the viewList so we have to
			// remove the existing element and fill in our temporarily
			// stored ones
			viewList.removeAllElements();

			for (Iterator it = newViewList.iterator(); it.hasNext();) {
				viewList.addElement((XmlElement) it.next());
			}
		}
	}

	/**
	 * Opens all views stored in the configuration.
	 */
	public void openStoredViews() {
		// load all frames from configuration file
		for (int i = 0; i < viewList.count(); i++) {
			// get element from view list
			XmlElement view = viewList.getElement(i);
			try {
				createFrameMediator(new ViewItem(view));
			} catch (PluginLoadingFailedException plfe) {
				// should not occur
				continue;
			}

		}

	}

	/**
	 * Returns an array of all open frames.
	 */
	public IContainer[] getOpenFrames() {
		return (IContainer[]) activeFrameCtrls.toArray(new IContainer[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.core.gui.frame.IFrameManager#getActiveFrameMediator()
	 */
	public IContainer getActiveFrameMediator() {
		Iterator it = activeFrameCtrls.iterator();
		while (it.hasNext()) {
			IContainer m = (IContainer) it.next();
			JFrame frame = m.getFrame();
			if (frame.isActive())
				return m;
		}

		return null;
	}

	/**
	 * Get active/focused JFrame.
	 * 
	 * @return active frame
	 */
	public JFrame getActiveFrame() {
		IContainer m = getActiveFrameMediator();
		if (m != null)
			return m.getFrame();

		// fall-back
		return new JFrame();
	}

	/**
	 * @param viewItem
	 * @param id
	 * @return
	 * @throws PluginLoadingFailedException
	 */
	private IFrameMediator instanciateFrameMediator(ViewItem viewItem)
			throws PluginLoadingFailedException {
		String id = viewItem.get("id");

		IFrameMediator frame = null;
		if (frameMediatorCache.containsKey(id)) {
			LOG.fine("use cached instance " + id);

			// found cached instance
			// -> re-use this instance and remove it from cache
			frame = (IFrameMediator) frameMediatorCache.remove(id);
		} else {
			LOG.fine("create new instance " + id);
			Object[] args = { viewItem };
			// create new instance
			// -> get frame controller using the plugin handler found above

			try {
				IExtension extension = handler.getExtension(id);
				frame = (IFrameMediator) extension.instanciateExtension(args);
			} catch (PluginException e) {
				LOG.severe(e.getMessage());
				if (Logging.DEBUG)
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		}
		return frame;
	}

	protected IFrameMediator createFrameMediator(ViewItem viewItem)
			throws PluginLoadingFailedException {

	

		IFrameMediator frame = instanciateFrameMediator(viewItem);

		IContainer c = new DefaultContainer((DefaultFrameController) frame);

		activeFrameCtrls.add(c);

		return frame;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.core.gui.frame.IFrameManager#openView(java.lang.String)
	 */
	public IFrameMediator openView(String id)
			throws PluginLoadingFailedException {
		// look for default view settings (if not found, null is returned)
		ViewItem view = loadDefaultView(id);

		if (view == null)
			view = ViewItem.createDefault(id);

		// Create a frame controller for this view
		// view = null => defaults specified by frame controller is used
		IFrameMediator controller = createFrameMediator(view);

		return controller;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.core.gui.frame.IFrameManager#switchView(org.columba.core.gui.frame.IContainer,
	 *      java.lang.String)
	 */
	public IFrameMediator switchView(IContainer c, String id)
			throws PluginLoadingFailedException {
		// look for default view settings (if not found, null is returned)
		ViewItem view = loadDefaultView(id);

		if (view == null)
			view = ViewItem.createDefault(id);

		// Create a frame controller for this view

		// save old framemediator in cache (use containers's old id)
		frameMediatorCache.put(((DefaultContainer) c).getViewItem().get("id"),
				c.getFrameMediator());

		IFrameMediator frame = instanciateFrameMediator(view);

		c.switchFrameMediator(frame);

		return frame;
	}

	/**
	 * Gets default view settings for a given view type
	 * 
	 * @param id
	 *            id specifying view type
	 * @return View settings
	 */
	protected ViewItem loadDefaultView(String id) {
		// If defaultViews doesn't exist, create it (backward compatibility)
		if (defaultViews == null) {
			XmlElement gui = Config.getInstance().get("views").getElement(
					"/views");
			defaultViews = new XmlElement("defaultviews");
			gui.addElement(defaultViews);
		}

		// search through defaultViews to get settings for given id
		ViewItem view = null;

		for (int i = 0; i < defaultViews.count(); i++) {
			XmlElement child = defaultViews.getElement(i);
			String childId = child.getAttribute("id");

			if ((childId != null) && childId.equals(id)) {
				view = new ViewItem(child);

				break;
			}
		}

		return view;
	}

	/**
	 * Saves default view settings for given view type. These will be used as
	 * startup values next a view of this type is opened. Though, views opened
	 * at startup will use settings from viewlist instead.
	 * 
	 * Only one set of settings are stored for each view id.
	 * 
	 * @param view
	 *            view settings to be stored
	 */
	protected void saveDefaultView(ViewItem view) {
		if (view == null) {
			return; // nothing to save
		}

		String id = view.get("id");

		// removed previous default values
		ViewItem oldView = loadDefaultView(id);

		if (oldView != null) {
			defaultViews.removeElement(oldView.getRoot());
		}

		// store current view settings
		defaultViews.addElement(view.getRoot());
	}

	/**
	 * Called when a frame is closed. The reference is removed from the list of
	 * active (shown) frames. If it's the last open view, the view settings are
	 * stored in the view list.
	 * 
	 * @param c
	 *            Reference to frame controller for the view which is closed
	 */
	public void close(IContainer c) {

		// Check if the frame controller has been registered, else do nothing
		if (activeFrameCtrls.contains(c)) {
			ViewItem v = ((DefaultContainer) c).getViewItem();

			// save in cache
			frameMediatorCache.put(v.get("id"), c.getFrameMediator());

			saveDefaultView(v);
			activeFrameCtrls.remove(c);

			if (activeFrameCtrls.size() == 0) {
				// this is the last frame so store its data in the viewList
				viewList.removeAllElements();
				viewList.addElement(v.getRoot());
			}
		}

		if (activeFrameCtrls.size() == 0) {
			// shutdown Columba if no frame exists anymore
			if (getOpenFrames().length == 0) {

				ShutdownManager.getInstance().shutdown(0);
			}
		}
	}

	public ViewItem createCustomViewItem(String id) {
		XmlElement parent = Config.getInstance().get("views").getElement(
				"views");
		XmlElement custom = parent.getElement("custom");
		if (custom == null)
			custom = parent.addSubElement("custom");

		for (int i = 0; i < custom.count(); i++) {
			XmlElement child = custom.getElement(i);
			String name = child.getAttribute("id");
			if (name.equals(id))
				return new ViewItem(child);
		}

		ViewItem viewItem = ViewItem.createDefault(id);
		custom.addElement(viewItem.getRoot());

		return viewItem;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1302.java