error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17877.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17877.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17877.java
text:
```scala
w@@hile ((pos=new String(str).indexOf("%"))!=-1) {

/* *******************************************************************
 * Copyright (c) 2004 IBM Corporation
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *    Andy Clement IBM     initial implementation 30-May-2004
 * ******************************************************************/

package org.aspectj.bridge;

public class WeaveMessage extends Message {

    // Kinds of weaving message we can produce

	public static WeaveMessageKind WEAVEMESSAGE_DECLAREPARENTSIMPLEMENTS = 
	  new WeaveMessageKind(1,"Extending interface set for type '%1' (%2) to include '%3' (%4)");
 	       
	public static WeaveMessageKind WEAVEMESSAGE_ITD =
	  new WeaveMessageKind(2,"Type '%1' (%2) has intertyped %3 from '%4' (%5)");
      
    // %6 is information like "[with runtime test]"
	public static WeaveMessageKind WEAVEMESSAGE_ADVISES = 
	  new WeaveMessageKind(3,"Type '%1' (%2) advised by %3 advice from '%4' (%5)%6");

	public static WeaveMessageKind WEAVEMESSAGE_DECLAREPARENTSEXTENDS = 
	  new WeaveMessageKind(4,"Setting superclass of type '%1' (%2) to '%3' (%4)");

	public static WeaveMessageKind WEAVEMESSAGE_SOFTENS = 
	  new WeaveMessageKind(5,"Softening exceptions in type '%1' (%2) as defined by aspect '%3' (%4)");


    // private ctor - use the static factory method
	private WeaveMessage(String message) {
		super(message, IMessage.WEAVEINFO, null, null);
	}    

    /**
     * Static helper method for constructing weaving messages.
     * @param kind what kind of message (e.g. declare parents)
     * @param inserts inserts for the message (inserts are marked %n in the message)
     * @param affectedtypename the type which is being advised/declaredUpon
     * @param aspectname the aspect that defined the advice or declares
     * @return new weaving message
     */
	public static WeaveMessage constructWeavingMessage(
	  WeaveMessageKind kind,
	  String[] inserts) {
		StringBuffer str = new StringBuffer(kind.getMessage());
		int pos = -1;
		while ((pos=str.indexOf("%"))!=-1) {
			int n = Character.getNumericValue(str.charAt(pos+1));
			str.replace(pos,pos+2,inserts[n-1]);
		}
		return new WeaveMessage(str.toString());
	}
	
	
	
	public static class WeaveMessageKind {
    	
		private int id;
		private String message;
    	
		public WeaveMessageKind(int id,String message) {
			this.id = id;
			this.message = message;
		}
    	
		public String getMessage() { return message; }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17877.java