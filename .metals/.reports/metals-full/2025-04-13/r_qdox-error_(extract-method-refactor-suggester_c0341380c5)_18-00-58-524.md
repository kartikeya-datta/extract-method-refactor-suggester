error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9910.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9910.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9910.java
text:
```scala
i@@f (!cxfLibraryVersion.equals(getCxfRuntimeVersion())) {

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

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * 
 * @author sclarke
 *
 */
public class CXFClasspathContainer implements IClasspathContainer {

	private IPath path;
	private List<IClasspathEntry> classpathEntries;
	private String cxfLibraryEdition;
	private String cxfLibraryVersion;
	private String cxfLibraryLocation;
	
	public CXFClasspathContainer(IPath path, IJavaProject javaProject) {
		this.path = path;
		classpathEntries =  new ArrayList<IClasspathEntry>();
		cxfLibraryLocation = getCxfRuntimeLocation();
		cxfLibraryVersion = getCxfRuntimeVersion();
		cxfLibraryEdition = getCxfRuntimeEdition();
	}
	
	public IClasspathEntry[] getClasspathEntries() {
        if (cxfLibraryVersion != getCxfRuntimeVersion()) {
            classpathEntries = new ArrayList<IClasspathEntry>();
            cxfLibraryLocation = getCxfRuntimeLocation();
            cxfLibraryVersion = getCxfRuntimeVersion();
            cxfLibraryEdition = getCxfRuntimeEdition();
        }

	    if (classpathEntries.size() == 0) {
	        File cxfLibDirectory = getCXFLibraryDirectory();
	        if (cxfLibDirectory.exists() && cxfLibDirectory.isDirectory()) {
	            String[] files = cxfLibDirectory.list();
	            for (int i = 0; i < files.length; i++) {
	                File file = new File(cxfLibDirectory.getPath() + File.separator + files[i]);
	                String fileName = file.getName();
	                if (fileName.indexOf(".") != -1
	                        && fileName.substring(fileName.lastIndexOf("."), fileName.length()).equals(
	                                ".jar")) {
	                	classpathEntries.add(JavaCore.newLibraryEntry(new Path(file.getAbsolutePath()), null, 
	                	        new Path("/")));
	                }
	            }
	        }
		}
		return classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]);
	}
	
	public boolean isValid() {
	    if (getCxfRuntimeLocation().length() > 0) {
            File cxfLibDirectory = getCXFLibraryDirectory();
            return cxfLibDirectory.exists() && cxfLibDirectory.isDirectory();
	    }
	    return false;
	}
	
	public String getDescription() {
		return  MessageFormat.format(CXFCoreMessages.CXF_CONTAINER_LIBRARY, cxfLibraryEdition, 
		        cxfLibraryVersion);
	}

	public int getKind() {
		return K_APPLICATION;
	}

	public IPath getPath() {
		return path;
	}
	
	private String getCxfRuntimeLocation() {
	    return CXFCorePlugin.getDefault().getJava2WSContext().getCxfRuntimeLocation();
    }

    private String getCxfRuntimeVersion() {
        return CXFCorePlugin.getDefault().getJava2WSContext().getCxfRuntimeVersion();
    }

    private String getCxfRuntimeEdition() {
        return CXFCorePlugin.getDefault().getJava2WSContext().getCxfRuntimeEdition();
    }

	private File getCXFLibraryDirectory() {
        IPath cxfLibPath = new Path(cxfLibraryLocation);
        if (!cxfLibPath.hasTrailingSeparator()) {
            cxfLibPath = cxfLibPath.addTrailingSeparator();
        }
        cxfLibPath = cxfLibPath.append("lib"); //$NON-NLS-1$

        File cxfLibDirectory = new File(cxfLibPath.toOSString());
        return cxfLibDirectory;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9910.java