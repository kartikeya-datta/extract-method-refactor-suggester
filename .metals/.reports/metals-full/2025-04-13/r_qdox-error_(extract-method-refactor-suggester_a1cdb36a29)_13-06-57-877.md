error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2987.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2987.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2987.java
text:
```scala
r@@eadRegistry(Platform.getExtensionRegistry(),

/******************************************************************************* 
 * Copyright (c) 2000, 2003 IBM Corporation and others. 
 * All rights reserved. This program and the accompanying materials! 
 * are made available under the terms of the Common Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 * 
 * Contributors: 
 *     IBM Corporation - initial API and implementation 
 *     Dan Rubel <dan_rubel@instantiations.com>
 *       - Fix for bug 11490 - define hidden view (placeholder for view) in plugin.xml 
************************************************************************/

package org.eclipse.ui.internal.registry;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IWorkbenchConstants;
import org.eclipse.ui.internal.PageLayout;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * A strategy to read perspective extension from the registry.
 * A pespective extension is one of a view, viewAction, perspAction,
 * newWizardAction, or actionSet.
 */
public class PerspectiveExtensionReader extends RegistryReader {
	private String targetID;
	private PageLayout pageLayout;
	private Set includeOnlyTags = null;  
	
	public static final String TAG_EXTENSION="perspectiveExtension";//$NON-NLS-1$
	public static final String TAG_ACTION_SET="actionSet";//$NON-NLS-1$
	public static final String TAG_WIZARD_SHORTCUT="newWizardShortcut";//$NON-NLS-1$
	public static final String TAG_VIEW_SHORTCUT="viewShortcut";//$NON-NLS-1$
	public static final String TAG_PERSP_SHORTCUT="perspectiveShortcut";//$NON-NLS-1$
	public static final String TAG_VIEW="view";//$NON-NLS-1$
	public static final String TAG_SHOW_IN_PART="showInPart";//$NON-NLS-1$
	private static final String ATT_ID="id";//$NON-NLS-1$
	private static final String ATT_TARGET_ID="targetID";//$NON-NLS-1$
	private static final String ATT_RELATIVE="relative";//$NON-NLS-1$
	private static final String ATT_RELATIONSHIP="relationship";//$NON-NLS-1$
	private static final String ATT_RATIO="ratio";//$NON-NLS-1$
	// ATT_VISIBLE added by dan_rubel@instantiations.com  
	private static final String ATT_VISIBLE="visible";//$NON-NLS-1$
	private static final String VAL_LEFT="left";//$NON-NLS-1$
	private static final String VAL_RIGHT="right";//$NON-NLS-1$
	private static final String VAL_TOP="top";//$NON-NLS-1$
	private static final String VAL_BOTTOM="bottom";//$NON-NLS-1$
	private static final String VAL_STACK="stack";//$NON-NLS-1$
	private static final String VAL_FAST="fast";//$NON-NLS-1$
	// VAL_FALSE added by dan_rubel@instantiations.com  
	private static final String VAL_FALSE="false";//$NON-NLS-1$	

/**
 * PerspectiveExtensionReader constructor..
 */
public PerspectiveExtensionReader() {
}
/**
 * Read the view extensions within a registry.
 */
public void extendLayout(String id, PageLayout out)
{
	targetID = id;
	pageLayout = out;
	readRegistry(Platform.getPluginRegistry(), 
		PlatformUI.PLUGIN_ID, 
		IWorkbenchConstants.PL_PERSPECTIVE_EXTENSIONS);
}

/**
 * Returns whether the given tag should be included.
 */
private boolean includeTag(String tag) {
	return includeOnlyTags == null || includeOnlyTags.contains(tag);
}

/**
 * Process an action set.
 */
private boolean processActionSet(IConfigurationElement element) {
	String id = element.getAttribute(ATT_ID);
	if (id != null)
		pageLayout.addActionSet(id);
	return true;
}
/**
 * Process an extension.
 * Assumption: Extension is for current perspective.
 */
private boolean processExtension(IConfigurationElement element) {
	IConfigurationElement [] children = element.getChildren();
	for (int nX = 0; nX < children.length; nX ++) {
		IConfigurationElement child = children[nX];
		String type = child.getName();
		if (includeTag(type)) { 
			boolean result = false;
			if (type.equals(TAG_ACTION_SET)) 
				result = processActionSet(child);
			else if (type.equals(TAG_VIEW))
				result = processView(child);
			else if (type.equals(TAG_VIEW_SHORTCUT))
				result = processViewShortcut(child);
			else if (type.equals(TAG_WIZARD_SHORTCUT))
				result = processWizardShortcut(child);
			else if (type.equals(TAG_PERSP_SHORTCUT))
				result = processPerspectiveShortcut(child);
			else if (type.equals(TAG_SHOW_IN_PART))
				result = processShowInPart(child);
			if (!result) {
				WorkbenchPlugin.log("Unable to process element: " +//$NON-NLS-1$
					type +
					" in perspective extension: " +//$NON-NLS-1$
					element.getDeclaringExtension().getUniqueIdentifier());
			}
		}
	}
	return true;
}
/**
 * Process a perspective shortcut
 */
private boolean processPerspectiveShortcut(IConfigurationElement element) {
	String id = element.getAttribute(ATT_ID);
	if (id != null)
		pageLayout.addPerspectiveShortcut(id);
	return true;
}

/**
 * Process a show in element.
 */
private boolean processShowInPart(IConfigurationElement element) {
	String id = element.getAttribute(ATT_ID);
	if (id != null)
		pageLayout.addShowInPart(id);
	return true;
}

// processView(IConfigurationElement) modified by dan_rubel@instantiations.com
/**
 * Process a view
 */
private boolean processView(IConfigurationElement element) {
	// Get id, relative, and relationship.
	String id = element.getAttribute(ATT_ID);
	String relative = element.getAttribute(ATT_RELATIVE);
	String relationship = element.getAttribute(ATT_RELATIONSHIP);
	String ratioString = element.getAttribute(ATT_RATIO);
	float ratio;
	
	if (id == null || relative == null || relationship == null)
		return false;
	
	// Get the ratio.
	if (ratioString == null) {
		// The ratio has not been specified.
		ratio = IPageLayout.NULL_RATIO;
	} else {
		try {
			ratio = new Float(ratioString).floatValue();
		} catch (NumberFormatException e) {
			return false;
		}
		// If the ratio is outside the allowable range, mark it as invalid.
		if (ratio < IPageLayout.RATIO_MIN || ratio > IPageLayout.RATIO_MAX)
			ratio = IPageLayout.INVALID_RATIO;
	}

	// Get relationship details.
	boolean stack = false;
	boolean fast = false;
	int intRelation = 0;
	if (relationship.equals(VAL_LEFT))
		intRelation = IPageLayout.LEFT;
	else if (relationship.equals(VAL_RIGHT))
		intRelation = IPageLayout.RIGHT;
	else if (relationship.equals(VAL_TOP))
		intRelation = IPageLayout.TOP;
	else if (relationship.equals(VAL_BOTTOM))
		intRelation = IPageLayout.BOTTOM;
	else if (relationship.equals(VAL_STACK)) 
		stack = true;
	else if (relationship.equals(VAL_FAST))
		fast = true;
	else
		return false;
	boolean visible = !VAL_FALSE.equals(element.getAttribute(ATT_VISIBLE));

	// If stack ..
	if (stack) {
		if (visible)
			pageLayout.stackView(id, relative);
		else
			pageLayout.stackPlaceholder(id, relative);
		return true;
	}
	
	// If the view is a fast view...
	if (fast) {
		if (ratio == IPageLayout.NULL_RATIO) {
			// The ratio has not been specified.
			pageLayout.addFastView(id);
		} else {
			pageLayout.addFastView(id, ratio);
		}
		return true;
	}
	
	// The view is a regular view.
	// If the ratio is not specified or is invalid, use the default ratio.
	if (ratio == IPageLayout.NULL_RATIO || ratio == IPageLayout.INVALID_RATIO)
		ratio = IPageLayout.DEFAULT_VIEW_RATIO;

	if (visible)
		pageLayout.addView(id, intRelation, ratio, relative);
	else
		pageLayout.addPlaceholder(id, intRelation, ratio, relative);
	return true;
}
/**
 * Process a view shortcut
 */
private boolean processViewShortcut(IConfigurationElement element) {
	String id = element.getAttribute(ATT_ID);
	if (id != null)
		pageLayout.addShowViewShortcut(id);
	return true;
}
/**
 * Process a wizard shortcut
 */
private boolean processWizardShortcut(IConfigurationElement element) {
	String id = element.getAttribute(ATT_ID);
	if (id != null)
		pageLayout.addNewWizardShortcut(id);
	return true;
}

protected boolean readElement(IConfigurationElement element) {
	String type = element.getName();
	if (type.equals(TAG_EXTENSION)) {
		String id = element.getAttribute(ATT_TARGET_ID);
		if (targetID.equals(id))
			return processExtension(element);
		return true;
	}
	return false;
}

/**
 * Sets the tags to include.  All others are ignored.
 */
public void setIncludeOnlyTags(String[] tags) {
	includeOnlyTags = new HashSet();
	for (int i = 0; i < tags.length; i++) {
		includeOnlyTags.add(tags[i]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2987.java