error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8111.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8111.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[6,1]

error in qdox parser
file content:
```java
offset: 163
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8111.java
text:
```scala
public class RRset implements TypedObject {

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

p@@ackage org.xbill.DNS;

import java.util.*;
import java.io.*;

/**
 * A set of Records with the same name, type, and class.  Also included
 * are all SIG records signing the data records.
 * @see Record
 * @see SIGRecord
 *
 * @author Brian Wellington
 */

public class RRset {

private List rrs;
private List sigs;
private int start;
private byte securityStatus;

/** Creates an empty RRset */
public
RRset() {
	rrs = new ArrayList(1);
	sigs = null;
	start = 0;
	securityStatus = DNSSEC.Insecure;
}

/** Adds a Record to an RRset */
public void
addRR(Record r) {
	if (r.getType() != Type.SIG) {
		synchronized (rrs) {
			if (!rrs.contains(r))
				rrs.add(r);
			start = 0;
		}
	}
	else {
		if (sigs == null)
			sigs = new ArrayList();
		if (!sigs.contains(r))
			sigs.add(r);
	}
}

/** Deletes a Record from an RRset */
public void
deleteRR(Record r) {
	if (r.getType() != Type.SIG) {
		synchronized (rrs) {
			rrs.remove(r);
			start = 0;
		}
	}
	else if (sigs != null)
		sigs.remove(r);
}

/** Deletes all Records from an RRset */
public void
clear() {
	synchronized (rrs) {
		rrs.clear();
		start = 0;
	}
	sigs = null;
}

/**
 * Returns an Iterator listing all (data) records.  This cycles through
 * the records, so each Iterator will start with a different record.
 */
public synchronized Iterator
rrs() {
	int size = rrs.size();
	if (size == 0)
		return Collections.EMPTY_LIST.iterator();
	if (start == size)
		start = 0;
	if (start++ == 0)
		return (rrs.iterator());
	List list = new ArrayList(rrs.subList(start - 1, size));
	list.addAll(rrs.subList(0, start - 1));
	return list.iterator();
}

/** Returns an Iterator listing all signature records */
public Iterator
sigs() {
	if (sigs == null)
		return Collections.EMPTY_LIST.iterator();
	else
		return sigs.iterator();
}

/** Returns the number of (data) records */
public int
size() {
	return rrs.size();
}

/**
 * Returns the name of the records
 * @see Name
 */
public Name
getName() {
	Record r = first();
	if (r == null)
		return null;
	return r.getName();
}

/**
 * Returns the type of the records
 * @see Type
 */
public short
getType() {
	Record r = first();
	if (r == null)
		return 0;
	return r.getType();
}

/**
 * Returns the class of the records
 * @see DClass
 */
public short
getDClass() {
	Record r = first();
	if (r == null)
		return 0;
	return r.getDClass();
}

/** Returns the ttl of the records */
public int
getTTL() {
	synchronized (rrs) {
		if (rrs.size() == 0)
			return 0;
		int ttl = Integer.MAX_VALUE;
		Iterator it = rrs.iterator();
		while (it.hasNext()) {
			Record r = (Record)it.next();
			if (r.getTTL() < ttl)
				ttl = r.getTTL();
		}
		return (ttl);
	}
}

/** Returns the first record */
public Record
first() {
	try {
		return (Record) rrs.get(0);
	}
	catch (IndexOutOfBoundsException e) {
		return null;
	}
}

/** Sets the DNSSEC security of the RRset. */
void
setSecurity(byte status) {
	securityStatus = status;
}

/** Returns the DNSSEC security of the RRset. */
public byte
getSecurity() {
	return securityStatus;
}

private String
iteratorToString(Iterator it) {
	StringBuffer sb = new StringBuffer();
	while (it.hasNext()) {
		Record rr = (Record) it.next();
		sb.append("[");
		sb.append(rr.rdataToString());
		sb.append("]");
		if (it.hasNext())
			sb.append(" ");
	}
	return sb.toString();
}

/** Converts the RRset to a String */
public String
toString() {
	if (rrs == null)
		return ("{empty}");
	StringBuffer sb = new StringBuffer();
	sb.append("{ ");
	sb.append(getName() + " ");
	sb.append(getTTL() + " ");
	sb.append(DClass.string(getDClass()) + " ");
	sb.append(Type.string(getType()) + " ");
	sb.append(iteratorToString(rrs.iterator()));
	if (sigs != null) {
		sb.append(" sigs: ");
		sb.append(iteratorToString(sigs.iterator()));
	}
	sb.append(" }");
	return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8111.java