error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8845.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8845.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8845.java
text:
```scala
p@@ossiblePath = new Path("C:\\Program Files\\Sausalito CoreSDK 1.0.12");

/*******************************************************************************
 * Copyright (c) 2008, 2009 28msec Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gabriel Petrovay (28msec) - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xquery.set.internal.launching.variables;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;
import org.eclipse.wst.xquery.launching.XQDTLaunchingPlugin;
import org.eclipse.wst.xquery.set.launching.SETLaunchingPlugin;
import org.osgi.framework.Bundle;

public class CoreSdkLocationResolver implements IDynamicVariableResolver {

    public String resolveValue(IDynamicVariable variable, String argument) throws CoreException {
        String result = findStrategies();

        return result;
    }

    private String findStrategies() {
        String result = null;

        // I. first search for the shipped Sausalito CoreSDK
        // this case happens in the 28msec distribution of the plugins
        result = findShippedCoreSDK();
        if (result != null) {
            return result;
        }

        // II. if no CoreSDK is shipped (for some platforms)
        // go and search in some predefined install locations
        result = findInstalledCoreSDK();
        if (result != null) {
            return result;
        }

        return result;
    }

    private String findShippedCoreSDK() {
        String os = Platform.getOS();

        String osPart = "." + os;
        String archPart = "";

        // in case of non-Windows platforms we make have more versions of the CoreSDK
        if (!os.equals(Platform.OS_WIN32)) {
            archPart = "." + Platform.getOSArch();
        }

        String pluginID = "com.28msec.sausalito";
        String fragment = pluginID + osPart + archPart;

        Bundle[] bundles = Platform.getBundles(fragment, null);
        if (bundles == null || bundles.length == 0) {
            if (XQDTLaunchingPlugin.DEBUG_AUTOMATIC_PROCESSOR_DETECTION) {
                log(IStatus.INFO, "Could not find plug-in fragment: " + fragment
                        + ". No default Sausalito CoreSDK will be configured.", null);
            }
            return null;
        }
        if (XQDTLaunchingPlugin.DEBUG_AUTOMATIC_PROCESSOR_DETECTION) {
            log(IStatus.INFO, "Found Sausalito CoreSDK plug-in fragment: " + fragment, null);
        }

        Bundle bundle = bundles[0];
        URL coreSdkUrl = FileLocator.find(bundle, new Path("coresdk"), null);
        if (coreSdkUrl == null) {
            return null;
        }
        try {
            coreSdkUrl = FileLocator.toFileURL(coreSdkUrl);
        } catch (IOException ioe) {
            log(IStatus.ERROR, "Cound not retrieve the Eclipse bundle location: " + fragment, ioe);
            return null;
        }

        IPath coreSdkPath = new Path(coreSdkUrl.getPath());

        return locateScriptIn(coreSdkPath);
    }

    private String findInstalledCoreSDK() {
        String os = Platform.getOS();
        Path possiblePath = null;

        if (os.equals(Platform.OS_WIN32)) {
            possiblePath = new Path("C:\\Program Files\\Sausalito CoreSDK 1.0.0");
        } else {
            possiblePath = new Path("/opt/sausalito");
        }

        return locateScriptIn(possiblePath);
    }

    private String locateScriptIn(IPath coreSdkPath) {
        if (coreSdkPath == null) {
            NullPointerException npe = new NullPointerException();
            log(IStatus.ERROR, "Could not locate the Sausalito script.", npe);
            return null;
        }

        IPath scriptPath = coreSdkPath.append("bin").append("sausalito");
        if (Platform.getOS().equals(Platform.OS_WIN32)) {
            scriptPath = scriptPath.addFileExtension("bat");
        }

        File scrptFile = scriptPath.toFile();
        if (!scrptFile.exists()) {
            log(IStatus.ERROR, "Could not find the Sausalito script at location: " + coreSdkPath.toOSString(), null);
            return null;
        }

        return scriptPath.toOSString();
    }

    public static IStatus log(int severity, String message, Throwable t) {
        Status status = new Status(severity, SETLaunchingPlugin.PLUGIN_ID, message, t);
        SETLaunchingPlugin.log(status);
        return status;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8845.java