error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15496.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15496.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15496.java
text:
```scala
private static final b@@oolean debug = Boolean.getBoolean("org.aspectj.testing.server.debug");

/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Matthew Webster - initial implementation
 *******************************************************************************/
package org.aspectj.testing.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

public class TestServer implements Runnable {

	private static final boolean debug = true;

	private boolean exitOnError = true;
	private File workingDirectory;
	private ClassLoader rootLoader;
	private Map loaders = new HashMap();

	private String mainClass = "UnknownClass";
	private String mainLoader = "UnknownLoader";
	
	public void initialize () throws IOException {
		createRootLoader();
		loadConfiguration();
	}
	
	private void loadConfiguration () throws IOException {
		File file = new File(workingDirectory,"server.properties");
		Properties props = new Properties();
		FileInputStream in = new FileInputStream(file);
		props.load(in);
		in.close();
		
		Enumeration enu = props.propertyNames();
		while (enu.hasMoreElements()) {
			String key = (String)enu.nextElement();
			if (key.startsWith("loader.")) {
				createLoader(props.getProperty(key));
			}
			else if (key.equals("main")) {
				StringTokenizer st = new StringTokenizer(props.getProperty(key),",");
				mainClass = st.nextToken();
				mainLoader = st.nextToken();
			}
		}
	}
	
	private void createLoader (String property) throws IOException {
		ClassLoader parent = rootLoader;

		StringTokenizer st = new StringTokenizer(property,",");
		String name = st.nextToken();
		String classpath = st.nextToken();
		if (st.hasMoreTokens()) {
			String parentName = st.nextToken();
			parent = (ClassLoader)loaders.get(parentName);
			if (parent == null) error("No such loader: " + parentName);
		}

		List urlList = new ArrayList();
		st = new StringTokenizer(classpath,";");
		while (st.hasMoreTokens()) {
			String fileName = st.nextToken();
			File file = new File(workingDirectory,fileName).getCanonicalFile();
			if (!file.exists()) error("Missing or invalid file: " + file.getPath());
			URL url = file.toURL();
			urlList.add(url);
		}
		URL[] urls = new URL[urlList.size()];
		urlList.toArray(urls);
		ClassLoader loader = new URLClassLoader(urls, parent);
		if (debug) System.err.println("? TestServer.createLoader() loader=" + loader + ", name='" + name + "', urls=" + urlList + ", parent=" + parent);

		loaders.put(name,loader);
	}
	
	private void createRootLoader () throws IOException {
		List urlList = new ArrayList();
		
		/* Sandbox */
		URL url = workingDirectory.getCanonicalFile().toURL();
		urlList.add(url);

		/* AspectJ runtime */
		URL[] urls = ((URLClassLoader)getClass().getClassLoader()).getURLs();
		for (int i = 0; i < urls.length; i++) {
			url = urls[i];
			String file = url.getFile();
			if (debug) System.err.println("? TestServer.createRootLoader() " + file);
			if (file.indexOf("runtime") != -1 || file.indexOf("aspectjrt") != -1 || file.indexOf("aspectj5rt") != -1) {
				urlList.add(url);
			}
		}
		
		urls = new URL[urlList.size()];
		urlList.toArray(urls);
		ClassLoader parent = getClass().getClassLoader().getParent();
		rootLoader = new URLClassLoader(urls,parent);
		if (debug) System.err.println("? TestServer.createRootLoader() loader=" + rootLoader + ", urlList=" + urlList + ", parent=" + parent);
	}
	
	public void setExitOntError (boolean b) {
		exitOnError = b;
	}
	
	public void setWorkingDirectory (String name) {
		workingDirectory = new File(name);
		if (!workingDirectory.exists()) error("Missing or invalid working directory: " + workingDirectory.getPath());
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("Starting ...");
		
		TestServer server = new TestServer();
		server.setWorkingDirectory(args[0]);
		server.initialize();
		
		Thread thread = new Thread(server,"application");
		thread.start();
		thread.join();
		
		System.out.println("Stopping ...");
	}

	public void run() {
		System.out.println("Running " + mainClass);
		runClass(mainClass,(ClassLoader)loaders.get(mainLoader));
	}

	private void runClass (String className, ClassLoader classLoader) {
		try {
			Class clazz = Class.forName(className,false,classLoader);
			invokeMain(clazz,new String[] {});
		}
		catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			error(ex.toString());
		}
	}
	
	public void invokeMain (Class clazz, String[] args)
	{
		Class[] paramTypes = new Class[1];
		paramTypes[0] = args.getClass();
	
		try {
			Method method = clazz.getDeclaredMethod("main",paramTypes);
			Object[] params = new Object[1];
			params[0] = args;
			method.invoke(null,params);
		}
		catch (InvocationTargetException ex) {
			Throwable th = ex.getTargetException();
			th.printStackTrace();
			error(th.toString());
		}
		catch (Throwable th) {
			th.printStackTrace();
			error(th.toString());
		}
	}
	
	private void error (String message) {
		System.out.println(message);
		if (exitOnError) System.exit(0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15496.java