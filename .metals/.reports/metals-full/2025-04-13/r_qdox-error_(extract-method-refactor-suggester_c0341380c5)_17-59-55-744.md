error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/421.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/421.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/421.java
text:
```scala
b@@acktrace.insertElementAt(cname, 0);

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package DNS;

import java.util.*;
import DNS.utils.*;

/**
 * The Response from a query to Zone.findRecords().
 * @see Zone
 */

public class ZoneResponse {

/**
 * The Zone does not that name
 */
static final byte NXDOMAIN	= 0;

/**
 * The Zone contains the name, but no data of the requested type
 */
static final byte NODATA	= 1;

/**
 * The Zone contains information that has allowed a partial lookup.
 * This normally occurs when a CNAME is found that points to data outside
 * of the Zone.
 * @see CNAMERecord
 */
static final byte PARTIAL	= 2;

/**
 * The Zone has successfully found the requested data.
 */
static final byte SUCCESSFUL	= 3;

private byte type;
private Object data;
private Vector backtrace;

private
ZoneResponse() {}

ZoneResponse(byte _type, Object _data) {
	type = _type;
	data = _data;
}

ZoneResponse(byte _type) {
	this(_type, null);
}

/**
 * Sets a ZoneResponse to have a different value without destroying the 
 * backtrace
 */
void
set(byte _type, Object _data) {
	type = _type;
	data = _data;
}

void
addRRset(RRset rrset) {
	if (data == null)
		data = new Vector();
	Vector v = (Vector) data;
	v.addElement(rrset);
}

void
addCNAME(CNAMERecord cname) {
	if (backtrace == null)
		backtrace = new Vector();
	backtrace.addElement(cname);
}

/** Is the answer to the query that the name does not exist? */
public boolean
isNXDOMAIN() {
	return (type == NXDOMAIN);
}

/** Is the answer to the query that the data does not exist? */
public boolean
isNODATA() {
	return (type == NODATA);
}

/** Did the query partially succeed? */
public boolean
isPartial() {
	return (type == PARTIAL);
}

/** Was the query successful? */
public boolean
isSuccessful() {
	return (type == SUCCESSFUL);
}

/** If the query was successful, return the answers */
public RRset []
answers() {
	if (type != SUCCESSFUL)
		return null;
	Vector v = (Vector) data;
	RRset [] rrsets = new RRset[v.size()];
	for (int i = 0; i < rrsets.length; i++)
		rrsets[i] = (RRset) v.elementAt(i);
	return rrsets;
}

/**
 * If the query was partially successful, return the last CNAME found in
 * the lookup process.
 */
public CNAMERecord
partial() {
	if (type != SUCCESSFUL)
		return null;
	return (CNAMERecord) data;
}

/**
 * If the query involved CNAME traversals, return a Vector containing all
 * CNAMERecords traversed.
 */
public Vector
backtrace() {
	return backtrace;
}

/** Prints the value of the ZoneResponse */
public String
toString() {
	switch (type) {
		case NXDOMAIN:	return "NXDOMAIN";
		case NODATA:	return "NODATA";
		case PARTIAL:	return "partial: reached " + data;
		case SUCCESSFUL:return "successful";
		default:	return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/421.java