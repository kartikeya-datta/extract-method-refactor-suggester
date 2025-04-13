error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7696.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7696.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7696.java
text:
```scala
e@@lse return obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj));

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

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public abstract class AbstractTrace implements Trace {

	protected Class tracedClass;

	private static SimpleDateFormat timeFormat;
	
	protected AbstractTrace (Class clazz) {
		this.tracedClass = clazz;
	}
	
	public abstract void enter (String methodName, Object thiz, Object[] args);
	
	public abstract void enter(String methodName, Object thiz);

	public abstract void exit(String methodName, Object ret);

	public abstract void exit(String methodName, Throwable th);

	/*
	 * Convenience methods
	 */
	public void enter (String methodName) {
		enter(methodName,null,null);
	}

	public void enter (String methodName, Object thiz, Object arg) {
		enter(methodName,thiz,new Object[] { arg });
	}

	public void enter (String methodName, Object thiz, boolean z) {
		enter(methodName,thiz,new Boolean(z));
	}

	public void exit (String methodName, boolean b) {
		exit(methodName,new Boolean(b));
	}

	public void event (String methodName, Object thiz, Object arg) {
		event(methodName,thiz,new Object[] { arg });
	}

	public void warn(String message) {
		warn(message,null);
	}

	public void error(String message) {
		error(message,null);
	}

	public void fatal (String message) {
		fatal(message,null);
	}
	
	/*
	 * Formatting
	 */
	protected String formatMessage(String kind, String className, String methodName, Object thiz, Object[] args) {
		StringBuffer message = new StringBuffer();
		Date now = new Date();
		message.append(formatDate(now)).append(" ");
		message.append(Thread.currentThread().getName()).append(" ");
		message.append(kind).append(" ");
		message.append(className);
		message.append(".").append(methodName);
		if (thiz != null) message.append(" ").append(formatObj(thiz));
		if (args != null) message.append(" ").append(formatArgs(args));
		return message.toString();
	}
	
	protected String formatMessage(String kind, String text, Throwable th) {
		StringBuffer message = new StringBuffer();
		Date now = new Date();
		message.append(formatDate(now)).append(" ");
		message.append(Thread.currentThread().getName()).append(" ");
		message.append(kind).append(" ");
		message.append(text);
		if (th != null) message.append(" ").append(formatObj(th));
		return message.toString();
	}
	
	private static String formatDate (Date date) {
		if (timeFormat == null) {
			timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
		}
		
		return timeFormat.format(date);
	}

	/**
	 * Format objects safely avoiding toString which can cause recursion,
	 * NullPointerExceptions or highly verbose results.
	 *  
	 * @param obj parameter to be formatted
	 * @return the formated parameter
	 */
	protected Object formatObj(Object obj) {
		
		/* These classes have a safe implementation of toString() */
		if (obj == null
 obj instanceof String
 obj instanceof Number
 obj instanceof Boolean
 obj instanceof Exception
 obj instanceof Character
 obj instanceof Class
 obj instanceof File
 obj instanceof StringBuffer
		    ) return obj;
		else if (obj.getClass().isArray()) {
			return formatArray(obj);
		}
		else if (obj instanceof Collection) {
			return formatCollection((Collection)obj);
		}
		else try {
			
			/* Classes can provide an alternative implementation of toString() */
			if (obj instanceof Traceable) {
				Traceable t = (Traceable)obj;
				return t.toTraceString();
			}
			
			/* Use classname@hashcode */
			else return obj.getClass().getName() + "@" + Integer.toHexString(obj.hashCode());
		
		/* Object.hashCode() can be override and may thow an exception */	
		} catch (Exception ex) {
			return obj.getClass().getName() + "@FFFFFFFF";
		}
	}
	
	protected String formatArray (Object obj) {
		return obj.getClass().getComponentType().getName() + "[" + Array.getLength(obj) + "]"; 
	}
	
	protected String formatCollection (Collection c) {
		return c.getClass().getName() + "(" + c.size() + ")"; 
	}

	/** 
	 * Format arguments into a comma separated list
	 * 
	 * @param names array of argument names
	 * @param args array of arguments
	 * @return the formated list
	 */
	protected String formatArgs(Object[] args) {
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < args.length; i++) {
			sb.append(formatObj(args[i]));
			if (i < args.length-1) sb.append(", ");
		}
		
		return sb.toString();
	}
	
	protected Object[] formatObjects(Object[] args) {
		for (int i = 0; i < args.length; i++) {
			args[i] = formatObj(args[i]);
		}
		
		return args;
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7696.java