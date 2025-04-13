error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13760.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13760.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13760.java
text:
```scala
/* private */ static b@@oolean unchanged(byte[] b1, byte[] b2) {

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.weaver.bcel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.bcel.classfile.JavaClass;
import org.aspectj.util.FileUtil;

public class UnwovenClassFile {
	protected String filename;
	protected byte[] bytes;
//	protected JavaClass javaClass = null;
	//protected byte[] writtenBytes = null;
	protected List /* ChildClass */ writtenChildClasses = new ArrayList(0);
	protected String className = null;
	
	public UnwovenClassFile(String filename, byte[] bytes) {
		this.filename = filename;
		this.bytes = bytes;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String makeInnerFileName(String innerName) {
		String prefix = filename.substring(0, filename.length()-6); // strip the .class
		return prefix + "$" + innerName + ".class";
	}
	
	public byte[] getBytes() {
//		if (bytes == null) bytes = javaClass.getBytes();
		return bytes;
	}
	
	public JavaClass getJavaClass() {
		//XXX need to know when to make a new class and when not to
		//XXX this is an important optimization
		if (getBytes() == null) {
			System.out.println("no bytes for: " + getFilename());
			Thread.currentThread().dumpStack();
		}
		return Utility.makeJavaClass(filename, getBytes());
//		if (javaClass == null) javaClass = Utility.makeJavaClass(filename, getBytes());
//		return javaClass;
	}
	
	public boolean exists() {
		return getBytes() != null;
	}

	
	public void writeUnchangedBytes() throws IOException {
		writeWovenBytes(getBytes(), Collections.EMPTY_LIST);
	}
	
	public void writeWovenBytes(byte[] bytes, List childClasses) throws IOException {	
		writeChildClasses(childClasses);
		
		//System.err.println("should write: " + getClassName());
		
		//System.err.println("about to write: " + this + ", " + writtenBytes + ", ");
//					+ writtenBytes != null + " && " + unchanged(bytes, writtenBytes) );
			
		//if (writtenBytes != null && unchanged(bytes, writtenBytes)) return;
		
		//System.err.println("    actually wrote it");
		
		BufferedOutputStream os = FileUtil.makeOutputStream(new File(filename));
		os.write(bytes);
		os.close();
		
		//writtenBytes = bytes;
	}

	private void writeChildClasses(List childClasses) throws IOException {
		//??? we only really need to delete writtenChildClasses whose
		//??? names aren't in childClasses; however, it's unclear
		//??? how much that will affect performance
		deleteAllChildClasses();

		childClasses.removeAll(writtenChildClasses); //XXX is this right
		
		for (Iterator iter = childClasses.iterator(); iter.hasNext();) {
			ChildClass childClass = (ChildClass) iter.next();
			writeChildClassFile(childClass.name, childClass.bytes);
			
		}
		
		writtenChildClasses = childClasses;
		
	}

	private void writeChildClassFile(String innerName, byte[] bytes) throws IOException {
		BufferedOutputStream os =
			FileUtil.makeOutputStream(new File(makeInnerFileName(innerName)));
		os.write(bytes);
		os.close();
	}


	protected void deleteAllChildClasses() {
		for (Iterator iter = writtenChildClasses.iterator(); iter.hasNext();) {
			ChildClass childClass = (ChildClass) iter.next();
			deleteChildClassFile(childClass.name);
		}
	}

	protected void deleteChildClassFile(String innerName) {
		File childClassFile = new File(makeInnerFileName(innerName));
		childClassFile.delete();
	}



	private static boolean unchanged(byte[] b1, byte[] b2) {
		int len = b1.length;
		if (b2.length != len) return false;
		for (int i=0; i < len; i++) {
			if (b1[i] != b2[i]) return false;
		}
		return true;
	}

	
	public String getClassName() {
		if (className == null) className = getJavaClass().getClassName();
		return className;
	}
	
	public String toString() {
		return "UnwovenClassFile(" + filename + ", " + getClassName() + ")";
	}

	public void deleteRealFile() throws IOException {
		new File(filename).delete();
	}




	// record
	public static class ChildClass {
		public final String name;
		public final byte[] bytes;
		
		ChildClass(String name, byte[] bytes) {
			this.name = name;
			this.bytes = bytes;
		}
		
		public boolean equals(Object other) {
			if (! (other instanceof ChildClass)) return false;
			ChildClass o = (ChildClass) other;
			return o.name.equals(name) && unchanged(o.bytes, bytes);
		}
		public int hashCode() {
			return name.hashCode();
		}
		
		public String toString() {
			return "(ChildClass " + name + ")";
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13760.java