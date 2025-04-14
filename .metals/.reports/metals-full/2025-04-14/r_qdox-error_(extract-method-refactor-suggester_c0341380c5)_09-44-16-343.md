error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5626.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5626.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5626.java
text:
```scala
S@@ystem.out.println("Dumping to " + fileName);

/* *******************************************************************
 * Copyright (c) 2004 IBM Corporation
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Matthew Webster 
 * ******************************************************************/
package org.aspectj.weaver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHolder;
import org.aspectj.bridge.Version;

/**
 * @author websterm
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Dump {

	/* Format for unique filename based on date & time */
	private static final String FILENAME_PREFIX = "ajcore";
//	private static final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); 
//	private static final DateFormat timeFormat = new SimpleDateFormat("HHmmss.SSS"); 
	private static final String FILENAME_SUFFIX = "txt";
	
	public static final String UNKNOWN_FILENAME = "Unknown";
	public static final String DUMP_EXCLUDED = "Excluded";
	public static final String NULL_OR_EMPTY = "Empty";
	
	private static Class exceptionClass;
	private static IMessage.Kind conditionKind = IMessage.ABORT;

	private String reason;
	private String fileName;
	private PrintStream print;
	
	private static String[] savedCommandLine;
	private static List savedFullClasspath;
	private static IMessageHolder savedMessageHolder;
	
	private static Map nodes = new HashMap();
	private static String lastDumpFileName = UNKNOWN_FILENAME;
	
	/*
	 * Dump methods
	 */
	public static String dump (String reason) {
		String fileName = UNKNOWN_FILENAME;
		Dump dump = null;
		try {
			dump = new Dump(reason);
			fileName = dump.getFileName();
			dump.dumpDefault();
		}
		finally {
			if (dump != null) dump.close();
		}
		return fileName;
	}
	
	public static String dumpWithException (Throwable th) {
		String fileName = UNKNOWN_FILENAME;
		Dump dump = null;
		try {
			dump = new Dump(th.getClass().getName());
			fileName = dump.getFileName();
			dump.dumpException(th);
		}
		finally {
			if (dump != null) dump.close();
		}
		return fileName;
	}

	public static String dumpOnExit () {
		if (!shouldDumpOnExit()) return DUMP_EXCLUDED;
		
		String fileName = UNKNOWN_FILENAME;
		Dump dump = null;
		try {
			dump = new Dump(conditionKind.toString());
			fileName = dump.getFileName();
			dump.dumpDefault();
		}
		finally {
			if (dump != null) dump.close();
		}
		return fileName;
	}

	private static boolean shouldDumpOnExit () {
		return (savedMessageHolder == null) || savedMessageHolder.hasAnyMessage(conditionKind,true);
	}

	/*
	 * Dump configuration
	 */
	public static void setDumpOnException (boolean b) {
		if (b) {
			exceptionClass = java.lang.Throwable.class;
		}
		else {
			exceptionClass = null;
		}
	}
	
	public static boolean getDumpOnException () {
		return (exceptionClass != null);
	}

	public static boolean setDumpOnExit (IMessage.Kind condition) {
		conditionKind = condition;
		return true;
	}

	public static boolean setDumpOnExit (String condition) {
		for (Iterator i = IMessage.KINDS.iterator(); i.hasNext();) {
			IMessage.Kind kind = (IMessage.Kind)i.next();
			if (kind.toString().equals(condition)) {
				return setDumpOnExit(kind);
			}
		}
		return false;
	}
	
	public static IMessage.Kind getDumpOnExit () {
		return conditionKind; 
	}
	
	public static String getLastDumpFileName () {
		return lastDumpFileName;
	}

	/*
	 * Dump registration
	 */
	public static void saveCommandLine (String[] args) {
		savedCommandLine = new String[args.length];
		System.arraycopy(args,0,savedCommandLine,0,args.length); 
	}

	public static void saveFullClasspath (List list) {
		savedFullClasspath = list;
	}

	public static void saveMessageHolder (IMessageHolder holder) {
		savedMessageHolder = holder;
	}

	public static void registerNode (Class module, INode newNode) {
		nodes.put(module,newNode);
	}
	
	/*
	 * Dump methods
	 */
	private Dump (String reason) {
		this.reason = reason;
		
		openDump();
		dumpAspectJProperties();
		dumpDumpConfiguration();
	}

	public String getFileName() {
		return fileName;
	}
	
	private void dumpDefault () {
		dumpSytemProperties();
		dumpCommandLine();
		dumpFullClasspath();
		dumpCompilerMessages();
		
		/*
		 * Dump registered nodes
		 */
		IVisitor dumpVisitor = new IVisitor() {

			public void visitString (String s) {
				println(s);
			}

			public void visitList (List list) {
				println(list);
			}
		};
		for (Iterator i = nodes.keySet().iterator(); i.hasNext();) {
			Class module = (Class)i.next();
			println("---- " + module.getName() + " ----");
			INode dumpNode = (INode)nodes.get(module);
			try {
				dumpNode.accept(dumpVisitor); 
			}
			catch (Exception ex) {
				println(ex.toString());
			}
		}
	}
	
	private void dumpException (Throwable th) {
		println("---- Exception Information ---");
		println(th);
		dumpDefault();
	}
	
	private void dumpAspectJProperties () {
		println("---- AspectJ Properties ---");
		println("AspectJ Compiler " + Version.text + " built on " + Version.time_text);
	}
	
	private void dumpDumpConfiguration () {
		println("---- Dump Properties ---");
		println("Dump file: " + fileName);
		println("Dump reason: " + reason);
		println("Dump on exception: " + (exceptionClass != null));
		println("Dump at exit condition: " + conditionKind);
	}
	
	private void dumpFullClasspath () {
		println("---- Full Classpath ---");
		if (savedFullClasspath != null && savedFullClasspath.size() > 0) {
			for (Iterator iter = savedFullClasspath.iterator(); iter.hasNext(); ) {
				String fileName = (String)iter.next();
				File file = new File(fileName);
				println(file);
			}
		}
		else {
			println(NULL_OR_EMPTY);
		}
	}
	
	private void dumpSytemProperties () {
		println("---- System Properties ---");
		Properties props = System.getProperties();
		println(props);
	}
	
	private void dumpCommandLine () {
		println("---- Command Line ---");
		println(savedCommandLine);
	}
	
	private void dumpCompilerMessages () {
		println("---- Compiler Messages ---");
		if (savedMessageHolder != null) for (Iterator i = savedMessageHolder.getUnmodifiableListView().iterator(); i.hasNext(); ) {
			IMessage message = (IMessage)i.next();
			println(message.toString());
		}
		else {
			println(NULL_OR_EMPTY);
		}
	}

	/*
	 * Dump output
	 */	
	private void openDump () {
		if (print != null) return;
		
		Date now = new Date();
		fileName = FILENAME_PREFIX + "."
			+ new SimpleDateFormat("yyyyMMdd").format(now) + "."
			+ new SimpleDateFormat("HHmmss.SSS").format(now) + "."
			+ FILENAME_SUFFIX;
		try {
			print = new PrintStream(new FileOutputStream(fileName),true);
			//System.out.println("Dumping to " + fileName);
		}
		catch (FileNotFoundException ex) {
			print = System.err;
			System.out.println("Dumping to stderr");
			fileName = UNKNOWN_FILENAME;
		}
		
		lastDumpFileName = fileName;
	}
	
	public void close () {
		print.close();
	}
	
	private void println (String s) {
		print.println(s);
	}
	
	private void println (Object[] array) {
		if (array == null) {
			println(NULL_OR_EMPTY);
			return;
		}
		
		for (int i = 0; i < array.length; i++) {
			print.println(array[i]);
		}
	}
	
	private void println (Properties props) {
		Iterator iter = props.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String)iter.next();
			String value = props.getProperty(key);
			print.println(key + "=" + value);
		}
	}
	
	private void println (Throwable th) {
		th.printStackTrace(print);
	}
	
	private void println (File file) {
		print.print(file.getAbsolutePath());
		if (!file.exists()) {
			println("(missing)");			
		}
		else if (file.isDirectory()) {
			int count = file.listFiles().length;
			println("(" + count + " entries)");			
		}
		else {
			println("(" + file.length() + " bytes)");			
		}
	}
	
	private void println (List list) {
		if (list == null || list.isEmpty()) println(NULL_OR_EMPTY);
		else for (Iterator i = list.iterator(); i.hasNext();) {
            Object o = i.next();
            if (o instanceof Exception) {
                println((Exception)o);
            } else {
            	println(o.toString());
            }
		}
	}
	
	static {
		String exceptionName = System.getProperty("org.aspectj.weaver.Dump.exception","true");
		if (!exceptionName.equals("false")) setDumpOnException(true);
		
		String conditionName = System.getProperty("org.aspectj.weaver.Dump.condition","true");
		setDumpOnExit(conditionName);
	}

	public interface INode {
		
		public void accept (IVisitor visior);
		
	}

	public interface IVisitor {
		
		public void visitString (String s);
		public void visitList (List list);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5626.java