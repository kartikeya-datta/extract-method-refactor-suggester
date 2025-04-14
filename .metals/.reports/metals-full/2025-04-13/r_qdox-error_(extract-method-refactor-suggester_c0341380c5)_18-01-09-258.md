error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3001.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3001.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3001.java
text:
```scala
b@@yte i = (byte) sections.getValue(s.toLowerCase());

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.util.*;
import org.xbill.DNS.utils.*;

/** Constants and functions relating to DNS message sections */

public final class Section {

private static StringValueTable sections = new StringValueTable();
private static StringValueTable longSections = new StringValueTable();
private static StringValueTable updSections = new StringValueTable();

/** The question (first) section */
public static final byte QUESTION	= 0;

/** The answer (second) section */
public static final byte ANSWER		= 1;

/** The authority (third) section */
public static final byte AUTHORITY	= 2;

/** The additional (fourth) section */
public static final byte ADDITIONAL	= 3;

/* Aliases for dynamic update */
/** The zone (first) section of a dynamic update message */
public static final byte ZONE		= 0;

/** The prerequisite (second) section of a dynamic update message */
public static final byte PREREQ		= 1;

/** The update (third) section of a dynamic update message */
public static final byte UPDATE		= 2;

static {
	sections.put2(QUESTION, "qd");
	sections.put2(ANSWER, "an");
	sections.put2(AUTHORITY, "au");
	sections.put2(ADDITIONAL, "ad");

	longSections.put2(QUESTION, "QUESTIONS");
	longSections.put2(ANSWER, "ANSWERS");
	longSections.put2(AUTHORITY, "AUTHORITY RECORDS");
	longSections.put2(ADDITIONAL, "ADDITIONAL RECORDS");

	updSections.put2(ZONE, "ZONE");
	updSections.put2(PREREQ, "PREREQUISITES");
	updSections.put2(UPDATE, "UPDATE RECORDS");
	updSections.put2(ADDITIONAL, "ADDITIONAL RECORDS");

}

private
Section() {}


/** Converts a numeric Section into an abbreviation String */
public static String
string(int i) {
	String s = sections.getString(i);
	return (s != null) ? s : new Integer(i).toString();
}

/** Converts a numeric Section into a full description String */
public static String
longString(int i) {
	String s = longSections.getString(i);
	return (s != null) ? s : new Integer(i).toString();
}

/**
 * Converts a numeric Section into a full description String for an update
 * Message.
 */
public static String
updString(int i) {
	String s = updSections.getString(i);
	return (s != null) ? s : new Integer(i).toString();
}

/** Converts a String representation of a Section into its numeric value */
public static byte
value(String s) {
	byte i = (byte) sections.getValue(s.toUpperCase());
	if (i >= 0)
		return i;
	try {
		return Byte.parseByte(s);
	}
	catch (Exception e) {
		return (-1);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3001.java