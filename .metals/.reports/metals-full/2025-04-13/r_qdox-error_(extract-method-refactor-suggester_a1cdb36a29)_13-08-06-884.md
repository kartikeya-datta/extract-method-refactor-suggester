error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18011.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18011.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18011.java
text:
```scala
i@@f (debug) System.err.println("TraceFactory.instance=" + instance);

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
package org.aspectj.weaver.tools;

import org.aspectj.util.LangUtil;

public abstract class TraceFactory {
    
	public final static String DEBUG_PROPERTY = "org.aspectj.tracing.debug";
	public final static String FACTORY_PROPERTY = "org.aspectj.tracing.factory";
	public final static String DEFAULT_FACTORY_NAME = "default";
	
    private static boolean debug = getBoolean(DEBUG_PROPERTY,false); 
    private static TraceFactory instance;
    
    public Trace getTrace (Class clazz) {
    	return instance.getTrace(clazz);
    }
    
    public static TraceFactory getTraceFactory () {
    	return instance;
    }
    
    protected static boolean getBoolean(String name, boolean def) {
		String defaultValue = String.valueOf(def);
		String value = System.getProperty(name,defaultValue);
		return Boolean.valueOf(value).booleanValue();
	}

	static {
		
		/*
		 * Allow user to override default behaviour or specify their own factory 
		 */
		String factoryName = System.getProperty(FACTORY_PROPERTY);
		if (factoryName != null) try {
			if (factoryName.equals(DEFAULT_FACTORY_NAME)) {
				instance = new DefaultTraceFactory();
			}
			else {
	    		Class factoryClass = Class.forName(factoryName);
	    		instance = (TraceFactory)factoryClass.newInstance();
			}
		}
    	catch (Throwable th) {
    		if (debug) th.printStackTrace();
    	}
    	
		/*
		 * Try to load external trace infrastructure using supplied factories
		 */
    	if (instance == null) try {
			if (LangUtil.is15VMOrGreater()) {
	    		Class factoryClass = Class.forName("org.aspectj.weaver.tools.Jdk14TraceFactory");
	    		instance = (TraceFactory)factoryClass.newInstance();
			} else {
	    		Class factoryClass = Class.forName("org.aspectj.weaver.tools.CommonsTraceFactory");
	    		instance = (TraceFactory)factoryClass.newInstance();
			}
    	}
    	catch (Throwable th) {
    		if (debug) th.printStackTrace();
    	}

    	/*
		 * Use default trace 
		 */
    	if (instance == null) {
    	    instance = new DefaultTraceFactory();
    	}
    	
    	if (debug) System.out.println("TraceFactory.instance=" + instance);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18011.java