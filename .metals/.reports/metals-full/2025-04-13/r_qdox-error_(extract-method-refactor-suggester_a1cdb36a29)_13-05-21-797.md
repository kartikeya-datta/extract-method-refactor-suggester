error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5756.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5756.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5756.java
text:
```scala
i@@d = configurationElement.getAttribute(ATT_ID);

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.registry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.views.IStickyViewDescriptor;

/**
 * @since 3.0
 */
public class StickyViewDescriptor implements IStickyViewDescriptor {
    private static final String ATT_ID = "id"; //$NON-NLS-1$

    private static final String ATT_LOCATION = "location"; //$NON-NLS-1$

    private static final String ATT_CLOSEABLE = "closeable"; //$NON-NLS-1$    

    private static final String ATT_MOVEABLE = "moveable"; //$NON-NLS-1$

	private IConfigurationElement configurationElement;

	private String id;

	/**
	 * Folder constant for right sticky views.
	 */
	public static final String STICKY_FOLDER_RIGHT = "stickyFolderRight"; //$NON-NLS-1$

	/**
	 * Folder constant for left sticky views.
	 */
	public static final String STICKY_FOLDER_LEFT = "stickyFolderLeft"; //$NON-NLS-1$

	/**
	 * Folder constant for top sticky views.
	 */
	public static final String STICKY_FOLDER_TOP = "stickyFolderTop"; //$NON-NLS-1$

	/**
	 * Folder constant for bottom sticky views.
	 */	
	public static final String STICKY_FOLDER_BOTTOM = "stickyFolderBottom"; //$NON-NLS-1$

    /**
     * @param element
     * @throws CoreException
     */
    public StickyViewDescriptor(IConfigurationElement element)
            throws CoreException {
    	this.configurationElement = element;
    	id = configurationElement.getAttribute(ATT_ID);;
        if (id == null)
            throw new CoreException(new Status(IStatus.ERROR, element
                    .getDeclaringExtension().getNamespace(), 0,
                    "Invalid extension (missing id) ", null));//$NON-NLS-1$
    }
    
	/**
	 * @return
	 */
	public IConfigurationElement getConfigurationElement() {
		return configurationElement;
	}

    public int getLocation() {
    	int direction = IPageLayout.RIGHT;
    	
    	String location = configurationElement.getAttribute(ATT_LOCATION);
        if (location != null) {
            if (location.equalsIgnoreCase("left")) //$NON-NLS-1$
                direction = IPageLayout.LEFT;
            else if (location.equalsIgnoreCase("top")) //$NON-NLS-1$
                direction = IPageLayout.TOP;
            else if (location.equalsIgnoreCase("bottom")) //$NON-NLS-1$
                direction = IPageLayout.BOTTOM;
            //no else for right - it is the default value;
        }    	
        return direction;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.registry.IStickyViewDescriptor#getId()
     */
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.registry.IStickyViewDescriptor#isFixed()
     */
    public boolean isCloseable() {
    	boolean closeable = true;
    	String closeableString = configurationElement.getAttribute(ATT_CLOSEABLE);
        if (closeableString != null) {
            closeable = !closeableString.equals("false"); //$NON-NLS-1$
        }
        return closeable;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.registry.IStickyViewDescriptor#isMoveable()
     */
    public boolean isMoveable() {
    	boolean moveable = true;
    	String moveableString = configurationElement.getAttribute(ATT_MOVEABLE);
        if (moveableString != null) {
            moveable = !moveableString.equals("false"); //$NON-NLS-1$
        }    	
        return moveable;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5756.java