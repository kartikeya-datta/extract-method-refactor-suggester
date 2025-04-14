error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8356.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8356.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8356.java
text:
```scala
C@@lassPathManager.ClassFile classFile = classPath.find(UnresolvedType.forName(name));

/* *******************************************************************
 * Copyright (c) 2004 IBM Corporation
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Matthew Webster, Adrian Colyer, 
 *     Martin Lippert     initial implementation 
 * ******************************************************************/

package org.aspectj.weaver;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;

import org.aspectj.util.FileUtil;
import org.aspectj.weaver.bcel.ClassPathManager;

public abstract class ExtensibleURLClassLoader extends URLClassLoader {
	
	private ClassPathManager classPath;
	
	public ExtensibleURLClassLoader (URL[] urls, ClassLoader parent) {
		super(urls,parent);

//		System.err.println("? ExtensibleURLClassLoader.<init>() path=" + WeavingAdaptor.makeClasspath(urls));
		classPath = new ClassPathManager(FileUtil.makeClasspath(urls),null);
	}

	protected void addURL(URL url) {
		super.addURL(url);  // amc - this call was missing and is needed in
		                 // WeavingURLClassLoader chains
		classPath.addPath(url.getPath(),null);
	}
	
	protected Class findClass(String name) throws ClassNotFoundException {
//		System.err.println("? ExtensibleURLClassLoader.findClass(" + name + ")");
		try {
			byte[] bytes = getBytes(name);
			if (bytes != null) {
				return defineClass(name,bytes);
			}
			else {
				throw new ClassNotFoundException(name);
			}
		}
		catch (IOException ex) {
			throw new ClassNotFoundException(name);
		}
	}

	protected Class defineClass(String name, byte[] b, CodeSource cs) throws IOException {
//		System.err.println("? ExtensibleURLClassLoader.defineClass(" + name + ",[" + b.length + "])");
		return defineClass(name, b, 0, b.length, cs);
	}

	protected byte[] getBytes (String name) throws IOException {
		byte[] b = null;
		ClassPathManager.ClassFile classFile = classPath.find(TypeX.forName(name));
		if (classFile != null) {
			b = FileUtil.readAsByteArray(classFile.getInputStream());
		}
		return b;
	}

	private Class defineClass(String name, byte[] bytes /*ClassPathManager.ClassFile classFile*/) throws IOException {
		String packageName = getPackageName(name);
		if (packageName != null) {
			Package pakkage = getPackage(packageName);
			if (pakkage == null) {
				definePackage(packageName,null,null,null,null,null,null,null);
			}
		}
		
		return defineClass(name, bytes, null);
	}

	private String getPackageName (String className) {
		int offset = className.lastIndexOf('.');
		return (offset == -1)? null : className.substring(0,offset);  
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8356.java