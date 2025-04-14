error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6984.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6984.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6984.java
text:
```scala
r@@eadRegistry(registry, PlatformUI.PLUGIN_ID, extensionPoint);

package org.eclipse.ui.internal;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.runtime.*;
import org.eclipse.ui.*;
import org.eclipse.ui.part.*;
import java.util.*;
import org.eclipse.ui.internal.*;
import org.eclipse.jface.action.*;
import org.eclipse.ui.internal.registry.RegistryReader;

/**
 * This class contains shared functionality for reading
 * action contributions from plugins into workbench parts (both editors and views).
 */
public abstract class PluginActionBuilder extends RegistryReader {
	protected String targetID;
	protected String targetContributionTag;
	protected List cache;
	
	public static final String TAG_MENU="menu";//$NON-NLS-1$
	public static final String TAG_ACTION="action";//$NON-NLS-1$
	public static final String TAG_SEPARATOR="separator";//$NON-NLS-1$
	public static final String TAG_GROUP_MARKER="groupMarker";//$NON-NLS-1$
	public static final String TAG_FILTER="filter";//$NON-NLS-1$
	public static final String TAG_VISIBILITY="visibility";//$NON-NLS-1$
	public static final String TAG_ENABLEMENT="enablement";//$NON-NLS-1$
	public static final String TAG_SELECTION="selection";//$NON-NLS-1$
	
	public static final String ATT_TARGET_ID = "targetID";//$NON-NLS-1$
	public static final String ATT_ID="id";//$NON-NLS-1$
	public static final String ATT_LABEL="label";//$NON-NLS-1$
	public static final String ATT_ENABLES_FOR="enablesFor";//$NON-NLS-1$
	public static final String ATT_NAME="name";//$NON-NLS-1$
	public static final String ATT_PATH="path";//$NON-NLS-1$
/**
 * The default constructor.
 */
public PluginActionBuilder() {
}
/**
 * Adds a group to a contribution manager.
 * Subclasses may override.
 */
protected void addGroup(IContributionManager mgr, String name) {
	mgr.add(new Separator(name));
}
/**
 * Contributes submenus and/or actions into the provided menu and tool bar managers.
 */
public void contribute(IMenuManager menu, IToolBarManager toolbar, boolean appendIfMissing) {
	if (cache == null)
		return;
	for (int i = 0; i < cache.size(); i++) {
		Object obj = cache.get(i);
		if (obj instanceof IConfigurationElement) {
			if (menu != null) {
				IConfigurationElement menuElement = (IConfigurationElement) obj;
				contributeMenu(menuElement, menu, appendIfMissing);
			}
		} else if (obj instanceof ActionDescriptor) {
			ActionDescriptor ad = (ActionDescriptor) obj;
			if (menu != null)
				contributeMenuAction(ad, menu, appendIfMissing);
			if (toolbar != null)
				contributeToolbarAction(ad, toolbar, appendIfMissing);
		}
	}
}
/**
 * Creates a menu from the information in the menu configuration element and
 * adds it into the provided menu manager. If 'appendIfMissing' is true, and
 * menu path slot is not found, it will be created and menu will be added
 * into it. Otherwise, add operation will fail.
 */
protected void contributeMenu(IConfigurationElement menuElement, IMenuManager mng, boolean appendIfMissing) {
	// Get config data.
	String id = menuElement.getAttribute(ATT_ID);
	String label = menuElement.getAttribute(ATT_LABEL);
	String path = menuElement.getAttribute(ATT_PATH);
	if (label == null) {
		WorkbenchPlugin.log("Invalid Menu Extension (label == null): " + id); //$NON-NLS-1$
		return;
	}
	
	// Calculate menu path and group.
	String group = null;
	if (path != null) {
		int loc = path.lastIndexOf('/');
		if (loc != -1) {
			group = path.substring(loc + 1);
			path = path.substring(0, loc);
		}
		else {
			// assume that path represents a slot
			// so actual path portion should be null
			group = path;
			path = null;
		}
	}
	
	// Find parent menu.
	IMenuManager parent = mng;
	if (path != null) {
		parent = mng.findMenuUsingPath(path);
		if (parent == null) {
			WorkbenchPlugin.log("Invalid Menu Extension (Path is invalid): " + id);//$NON-NLS-1$
			return;
		}
	}

	// Find reference group.
	if (group == null) 
		group = IWorkbenchActionConstants.MB_ADDITIONS;
	IContributionItem sep = parent.find(group);
	if (sep==null) {
		if (appendIfMissing)
			addGroup(parent, group);
		else {
			WorkbenchPlugin.log("Invalid Menu Extension (Group is invalid): " + id);//$NON-NLS-1$
			return;
		}
	}
	
	// If the menu does not exist create it.
	IMenuManager newMenu = parent.findMenuUsingPath(id);
	if (newMenu == null)
		newMenu = new MenuManager(label, id);
	
	// Create separators.
	IConfigurationElement[] children = menuElement.getChildren();
	for (int i = 0; i < children.length; i++) {
		String childName = children[i].getName();
		if(childName.equals(TAG_SEPARATOR)) {
			contributeSeparator(newMenu, children[i]);
		} else if(childName.equals(TAG_GROUP_MARKER)) {
			contributeGroupMarker(newMenu, children[i]);
		}
	}

	// Add new menu
	try {
		insertAfter(parent, group, newMenu);
	} catch (IllegalArgumentException e) {
		WorkbenchPlugin.log("Invalid Menu Extension (Group is missing): " + id);//$NON-NLS-1$
	}
}
/**
 * Contributes action from action descriptor into the provided menu manager.
 */
protected void contributeMenuAction(ActionDescriptor ad, IMenuManager menu, boolean appendIfMissing) {
	// Get config data.
	String mpath = ad.getMenuPath();
	String mgroup = ad.getMenuGroup();
	if (mpath == null && mgroup == null)
		return;

	// Find parent menu.
	IMenuManager parent = menu;
	if (mpath != null) {
		parent = parent.findMenuUsingPath(mpath);
		if (parent == null) {
			WorkbenchPlugin.log("Invalid Menu Extension (Path is invalid): " + ad.getId());//$NON-NLS-1$
			return;
		}
	}

	// Find reference group.
	if (mgroup == null)
		mgroup = IWorkbenchActionConstants.MB_ADDITIONS;
	IContributionItem sep = parent.find(mgroup);
	if (sep == null) {
		if (appendIfMissing)
			addGroup(parent, mgroup);
		else {
			WorkbenchPlugin.log("Invalid Menu Extension (Group is invalid): " + ad.getId());//$NON-NLS-1$
			return;
		}
	}

	// Add action.
	try {
		insertAfter(parent, mgroup, ad.getAction());
	} catch (IllegalArgumentException e) {
		WorkbenchPlugin.log("Invalid Menu Extension (Group is missing): " + ad.getId());//$NON-NLS-1$
	}
}
/**
 * Creates a named menu separator from the information in the configuration element.
 * If the separator already exists do not create a second.
 */
protected void contributeSeparator(IMenuManager menu, IConfigurationElement element) {
	String id = element.getAttribute(ATT_NAME);
	if (id == null || id.length() <= 0)
		return;
	IContributionItem sep = menu.find(id);
	if (sep != null)
		return;
	menu.add(new Separator(id));
}
/**
 * Creates a named menu group marker from the information in the configuration element.
 * If the marker already exists do not create a second.
 */
protected void contributeGroupMarker(IMenuManager menu, IConfigurationElement element) {
	String id = element.getAttribute(ATT_NAME);
	if (id == null || id.length() <= 0)
		return;
	IContributionItem marker = menu.find(id);
	if (marker != null)
		return;
	menu.add(new GroupMarker(id));
}
/**
 * Contributes action from the action descriptor into the provided tool bar manager.
 */
protected void contributeToolbarAction(ActionDescriptor ad, IToolBarManager toolbar, boolean appendIfMissing) {
	// Get config data.
	String tpath = ad.getToolbarPath();
	String tgroup = ad.getToolbarGroup();
	if (tpath == null && tgroup == null)
		return;

	// Find reference group.
	if (tgroup == null) tgroup = IWorkbenchActionConstants.MB_ADDITIONS;
	IContributionItem sep = null;
	sep = toolbar.find(tgroup);
	if (sep == null) {
		if (appendIfMissing) {
			addGroup(toolbar, tgroup);
		} else {
			WorkbenchPlugin.log("Invalid Toolbar Extension (Group is invalid): " + ad.getId());//$NON-NLS-1$
			return;
		}
	} 	
	// Add action to tool bar.
	try {
		insertAfter(toolbar, tgroup, ad.getAction());
	} catch (IllegalArgumentException e) {
		WorkbenchPlugin.log("Invalid Toolbar Extension (Group is missing): " + ad.getId());//$NON-NLS-1$
	}
}
/**
 * This factory method returns a new ActionDescriptor for the
 * configuration element.  It should be implemented by subclasses.
 */
protected abstract ActionDescriptor createActionDescriptor(IConfigurationElement element);
/**
 * Returns the name of the part ID attribute that is expected
 * in the target extension.
 */
protected String getTargetID(IConfigurationElement element) {
	String value=element.getAttribute(ATT_TARGET_ID);
	return value!=null? value : "???";//$NON-NLS-1$
}
/**
 * Inserts an action after another named contribution item.
 * Subclasses may override.
 */
protected void insertAfter(IContributionManager mgr, String refId, 
	IAction action) 
{
	insertAfter(mgr, refId, new ActionContributionItem(action));
}
/**
 * Inserts a contribution item after another named contribution item.
 * Subclasses may override.
 */
protected void insertAfter(IContributionManager mgr, String refId, 
	IContributionItem item) 
{
	mgr.insertAfter(refId, item);
}
/**
 * Reads the contributions from the registry for the provided workbench
 * part and the provided extension point ID.
 */
protected void readContributions(String id, String tag, String extensionPoint) {
	cache = null;
	targetID = id;
	targetContributionTag = tag;
	IPluginRegistry registry = Platform.getPluginRegistry();
	readRegistry(registry, IWorkbenchConstants.PLUGIN_ID, extensionPoint);
}
/**
 * Implements abstract method to handle the provided XML element
 * in the registry.
 */
protected boolean readElement(IConfigurationElement element) {
	String tag = element.getName();
	if (tag.equals(ObjectActionContributorReader.TAG_OBJECT_CONTRIBUTION)) {
		// This builder is sometimes used to read the popup menu
		// extension point.  Ignore all object contributions.
		return true;
	}
	if (tag.equals(targetContributionTag)) {
		String id = getTargetID(element);
		if (id == null || !id.equals(targetID)) {
			// This is not of interest to us - don't go deeper
			return true;
		}
	} else if (tag.equals(TAG_MENU)) {
		if (cache == null)
			cache = new ArrayList();
		cache.add(element);
		return true; // just cache the element - don't go into it
	} else if (tag.equals(TAG_ACTION)) {
		if (cache == null)
			cache = new ArrayList();
		cache.add(createActionDescriptor(element));
		return true; // just cache the action - don't go into
	} else {
		return false;
	}
	
	readElementChildren(element);
	return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6984.java