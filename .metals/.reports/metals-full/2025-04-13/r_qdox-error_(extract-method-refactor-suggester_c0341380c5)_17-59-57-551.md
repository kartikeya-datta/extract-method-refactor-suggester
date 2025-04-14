error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12200.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12200.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12200.java
text:
```scala
e@@xception.toString(), exception));

/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.cxf.core.context.Java2WSPersistentContext;
import org.eclipse.jst.ws.internal.cxf.core.context.WSDL2JavaPersistentContext;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author sclarke
 */
public class CXFCorePlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.eclipse.jst.ws.cxf.core"; //$NON-NLS-1$

    public static final String CXF_VERSION_2_0 = "2.0"; //$NON-NLS-1$

    public static final String CXF_VERSION_2_1 = "2.1"; //$NON-NLS-1$

    // The shared instance
    private static CXFCorePlugin plugin;

    private Java2WSPersistentContext java2WSContext;
    private WSDL2JavaPersistentContext wsdl2JavaContext;

    private Version currentRuntimeVersion;
    
    /**
     * The constructor
     */
    public CXFCorePlugin() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static CXFCorePlugin getDefault() {
        return plugin;
    }

    public Java2WSPersistentContext getJava2WSContext() {
        if (java2WSContext == null) {
            java2WSContext = new Java2WSPersistentContext();
            java2WSContext.load();
        }
        return java2WSContext;
    }

    public WSDL2JavaPersistentContext getWSDL2JavaContext() {
        if (wsdl2JavaContext == null) {
            wsdl2JavaContext = new WSDL2JavaPersistentContext();
            wsdl2JavaContext.load();
        }
        return wsdl2JavaContext;
    }

    public Version getCurrentRuntimeVersion() {
        if (currentRuntimeVersion == null) {
            String cxfRuntimeVersion = CXFCorePlugin.getDefault().getJava2WSContext().getCxfRuntimeVersion();
            if (cxfRuntimeVersion.length() == 0) {
                cxfRuntimeVersion = "0.0.0";
            }
            this.currentRuntimeVersion = new Version(cxfRuntimeVersion);
        }
        return currentRuntimeVersion;
    }

    public void setCurrentRuntimeVersion(Version version) {
        this.currentRuntimeVersion = version;
    }
    
    public static void logMessage(int severity, String message) {
        CXFCorePlugin.log(new Status(severity, CXFCorePlugin.PLUGIN_ID, message));
    }

    public static void log(IStatus status) {
        CXFCorePlugin.getDefault().getLog().log(status);
    }
    
    public static void log(Throwable exception) {
        CXFCorePlugin.log(new Status(IStatus.ERROR, CXFCorePlugin.PLUGIN_ID, 
                exception.getLocalizedMessage(), exception));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12200.java