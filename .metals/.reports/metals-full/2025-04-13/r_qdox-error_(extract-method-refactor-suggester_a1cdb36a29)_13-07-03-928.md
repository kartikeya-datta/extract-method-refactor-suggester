error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/878.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/878.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/878.java
text:
```scala
i@@nternalSetContentDescription(""); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.part;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.util.Util;

/**
 * Abstract base implementation of all workbench views.
 * <p>
 * This class should be subclassed by clients wishing to define new views.
 * The name of the subclass should be given as the <code>"class"</code> 
 * attribute in a <code>view</code> extension contributed to the workbench's
 * view extension point (named <code>"org.eclipse.ui.views"</code>).
 * For example, the plug-in's XML markup might contain:
 * <pre>
 * &LT;extension point="org.eclipse.ui.views"&GT;
 *      &LT;view id="com.example.myplugin.view"
 *         name="My View"
 *         class="com.example.myplugin.MyView"
 *         icon="images/eview.gif"
 *      /&GT;
 * &LT;/extension&GT;
 * </pre>
 * where <code>com.example.myplugin.MyView</code> is the name of the
 * <code>ViewPart</code> subclass.
 * </p>
 * <p>
 * Subclasses must implement the following methods:
 * <ul>
 *   <li><code>createPartControl</code> - to create the view's controls </li>
 *   <li><code>setFocus</code> - to accept focus</li>
 * </ul>
 * </p>
 * <p>
 * Subclasses may extend or reimplement the following methods as required:
 * <ul>
 *   <li><code>setInitializationData</code> - extend to provide additional 
 *       initialization when view extension is instantiated</li>
 *   <li><code>init(IWorkbenchPartSite)</code> - extend to provide additional
 *       initialization when view is assigned its site</li>
 *   <li><code>dispose</code> - extend to provide additional cleanup</li>
 *   <li><code>getAdapter</code> - reimplement to make their view adaptable</li>
 * </ul>
 * </p>
 */
public abstract class ViewPart extends WorkbenchPart implements IViewPart {

/**
 * Listens to PROP_TITLE property changes in this object until the first call to
 * setContentDescription. Used for compatibility with old parts that call setTitle
 * or overload getTitle instead of using setContentDescription. 
 */
private IPropertyListener compatibilityTitleListener = new IPropertyListener() {
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPropertyListener#propertyChanged(java.lang.Object, int)
	 */
	public void propertyChanged(Object source, int propId) {
		if (propId == IWorkbenchPartConstants.PROP_TITLE) {
			setDefaultContentDescription();
		}
	}
};

/**
 * Creates a new view.
 */
protected ViewPart() {
	super();
	
	addPropertyListener(compatibilityTitleListener);
}
/* (non-Javadoc)
 * Method declared on IViewPart.
 */
public IViewSite getViewSite() {
	return (IViewSite)getSite();
}
/* (non-Javadoc)
 * Initializes this view at the given view site.
 */
public void init(IViewSite site) throws PartInitException {
	setSite(site);
	
	setDefaultContentDescription();
}
/* (non-Javadoc)
 * Initializes this view with the given view site.  A memento is passed to
 * the view which contains a snapshot of the views state from a previous
 * session.  Where possible, the view should try to recreate that state
 * within the part controls.
 * <p>
 * This implementation will ignore the memento and initialize the view in
 * a fresh state.  Subclasses may override the implementation to perform any
 * state restoration as needed.
 */
public void init(IViewSite site,IMemento memento) throws PartInitException {
	init(site);
}
/* (non-Javadoc)
 * Method declared on IViewPart.
 */
public void saveState(IMemento memento){
    // do nothing
}

/* (non-Javadoc)
 * @see org.eclipse.ui.part.WorkbenchPart#setPartName(java.lang.String)
 */
protected void setPartName(String partName) {
	if (compatibilityTitleListener != null) {
		removePropertyListener(compatibilityTitleListener);
		compatibilityTitleListener = null;
	}

	super.setPartName(partName);
}

/* (non-Javadoc)
 * @see org.eclipse.ui.part.WorkbenchPart#setContentDescription(java.lang.String)
 */
protected void setContentDescription(String description) {
	if (compatibilityTitleListener != null) {
		removePropertyListener(compatibilityTitleListener);
		compatibilityTitleListener = null;
	}
	
	super.setContentDescription(description);
}

/* (non-Javadoc)
 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
 */
public void setInitializationData(IConfigurationElement cfig,
		String propertyName, Object data) {
	super.setInitializationData(cfig, propertyName, data);
	
	setDefaultContentDescription();
}

private void setDefaultContentDescription() {
	if (compatibilityTitleListener == null) {
		return;
	}
	
	String partName = getPartName();
	String title = getTitle();
	
	if (Util.equals(partName, title)) {
		internalSetContentDescription("");
	} else {
		internalSetContentDescription(title);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/878.java