error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/529.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/529.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[196,2]

error in qdox parser
file content:
```java
offset: 7035
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/529.java
text:
```scala
import org.eclipse.ecf.internal.example.collab.CollabClient;

/*******************************************************************************
 * Copyright (c) 2004 Peter Nehrer and Composent, Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Peter Nehrer - initial API and implementation
 *******************************************************************************/
package org.eclipse.ecf.example.sdo.editor;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.example.collab.CollabClient;
import org.eclipse.ecf.sdo.DataGraphSharingFactory;
import org.eclipse.ecf.sdo.ISharedDataGraph;
import org.eclipse.ecf.sdo.IUpdateConsumer;
import org.eclipse.ecf.sdo.WaitablePublicationCallback;
import org.eclipse.ecf.sdo.WaitableSubscriptionCallback;
import org.eclipse.ecf.sdo.emf.EMFUpdateProvider;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import commonj.sdo.DataGraph;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author pnehrer
 */
public class EditorPlugin extends AbstractUIPlugin {
    // The shared instance.
    private static EditorPlugin plugin;

    // Resource bundle.
    private ResourceBundle resourceBundle;

    /**
     * The constructor.
     */
    public EditorPlugin() {
        super();
        plugin = this;
        try {
            resourceBundle = ResourceBundle
                    .getBundle("org.eclipse.ecf.example.sdo.editor.EditorPluginResources");
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
    }

    /**
     * This method is called upon plug-in activation
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
     * This method is called when the plug-in is stopped
     */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }

    /**
     * Returns the shared instance.
     */
    public static EditorPlugin getDefault() {
        return plugin;
    }

    /**
     * Returns the string from the plugin's resource bundle, or 'key' if not
     * found.
     */
    public static String getResourceString(String key) {
        ResourceBundle bundle = EditorPlugin.getDefault().getResourceBundle();
        try {
            return (bundle != null) ? bundle.getString(key) : key;
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
     * Returns the plugin's resource bundle,
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void log(Throwable t) {
        if (t instanceof CoreException)
            getLog().log(((CoreException) t).getStatus());
        else
            getLog().log(
                    new Status(Status.ERROR, getBundle().getSymbolicName(), 0,
                            "An unexpected error occurred.", t));
    }

    public synchronized ISharedDataGraph subscribe(String path,
            IUpdateConsumer consumer) throws ECFException {
        Path p = new Path(path);
        ISharedObjectContainer container = getContainer(ResourcesPlugin
                .getWorkspace().getRoot().getProject(p.segment(0)));
        PublishedGraphTracker tracker = getTracker(container);

        ID id = IDFactory.getDefault().createStringID(path);
        WaitableSubscriptionCallback mutex = new WaitableSubscriptionCallback();
        ISharedDataGraph result = DataGraphSharingFactory.getDataGraphSharing(
                container, "default").subscribe(id, new EMFUpdateProvider(),
                consumer, mutex);
        ID containerID = null;
        try {
            containerID = mutex.waitForSubscription(5000);
        } catch (InterruptedException e) {
            throw new ECFException(e);
        }

        if (containerID == null)
            throw new ECFException("Subscription timed out.");

        tracker.add(id);
        return result;
    }

    public synchronized ISharedDataGraph publish(String path,
            DataGraph dataGraph, IUpdateConsumer consumer) throws ECFException {
        Path p = new Path(path);
        ISharedObjectContainer container = getContainer(ResourcesPlugin
                .getWorkspace().getRoot().getProject(p.segment(0)));
        PublishedGraphTracker tracker = getTracker(container);

        ID id = IDFactory.getDefault().createStringID(path);
        WaitablePublicationCallback mutex = new WaitablePublicationCallback();
        ISharedDataGraph result = DataGraphSharingFactory.getDataGraphSharing(
                container, "default").publish(dataGraph, id,
                new EMFUpdateProvider(), consumer, mutex);
        try {
            if (!mutex.waitForPublication(5000))
                throw new ECFException("Publication timed out.");
        } catch (InterruptedException e) {
            throw new ECFException(e);
        }

        tracker.add(id);
        return result;
    }

    public synchronized boolean isPublished(String path) throws ECFException {
        Path p = new Path(path);
        ISharedObjectContainer container = getContainer(ResourcesPlugin
                .getWorkspace().getRoot().getProject(p.segment(0)));
        PublishedGraphTracker tracker = getTracker(container);
        return tracker.isPublished(IDFactory.getDefault().createStringID(path));
    }

    public synchronized void checkConnected(IProject project)
            throws ECFException {
        if (getContainer(project) == null)
            throw new ECFException("Project " + project.getName()
                    + " is not connected.");
    }

    private ISharedObjectContainer getContainer(IProject project)
            throws ECFException {
        return CollabClient.getContainer(project);
    }

    private PublishedGraphTracker getTracker(ISharedObjectContainer container)
            throws ECFException {
        ID id = IDFactory.getDefault().createStringID(PublishedGraphTracker.class.getName());
        PublishedGraphTracker tracker = (PublishedGraphTracker) container
                .getSharedObjectManager().getSharedObject(id);
        if (tracker == null) {
            tracker = new PublishedGraphTracker();
            container.getSharedObjectManager().addSharedObject(id, tracker,
                    null);
        }

        return tracker;
    }
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/529.java