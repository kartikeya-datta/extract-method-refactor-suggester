error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4172.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4172.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[5,1]

error in qdox parser
file content:
```java
offset: 89
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4172.java
text:
```scala
public class RRset {

// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

p@@ackage org.xbill.DNS;

import java.util.*;

/**
 * A set of Records with the same name, type, and class.  Also included
 * are all SIG/RRSIG records signing the data records.
 * @see Record
 * @see SIGRecord 
 * @see RRSIGRecord 
 *
 * @author Brian Wellington
 */

public class RRset implements TypedObject {

/*
 * rrs contains both normal and SIG/RRSIG records, with the SIG/RRSIG records
 * at the end.
 */
private List rrs;
private short nsigs;
private short position;

/** Creates an empty RRset */
public
RRset() {
	rrs = new ArrayList(1);
	nsigs = 0;
	position = 0;
}

/** Creates an RRset and sets its contents to the specified record */
public
RRset(Record record) {
	this();
	safeAddRR(record);
}

/** Creates an RRset with the contents of an existing RRset */
public
RRset(RRset rrset) {
	synchronized (rrset) {
		rrs = (List) ((ArrayList)rrset.rrs).clone();
		nsigs = rrset.nsigs;
		position = rrset.position;
	}
}

private void
safeAddRR(Record r) {
	if (!(r instanceof SIGBase)) {
		if (nsigs == 0)
			rrs.add(r);
		else
			rrs.add(rrs.size() - nsigs, r);
	} else {
		rrs.add(r);
		nsigs++;
	}
}

/** Adds a Record to an RRset */
public synchronized void
addRR(Record r) {
	if (rrs.size() == 0) {
		safeAddRR(r);
		return;
	}
	Record first = first();
	if (!r.sameRRset(first))
		throw new IllegalArgumentException("record does not match " +
						   "rrset");

	if (r.getTTL() != first.getTTL()) {
		if (r.getTTL() > first.getTTL()) {
			r = r.cloneRecord();
			r.setTTL(first.getTTL());
		} else {
			for (int i = 0; i < rrs.size(); i++) {
				Record tmp = (Record) rrs.get(i);
				tmp = tmp.cloneRecord();
				tmp.setTTL(r.getTTL());
				rrs.set(i, tmp);
			}
		}
	}

	if (!rrs.contains(r))
		safeAddRR(r);
}

/** Deletes a Record from an RRset */
public synchronized void
deleteRR(Record r) {
	if (rrs.remove(r) && (r instanceof SIGBase))
		nsigs--;
}

/** Deletes all Records from an RRset */
public synchronized void
clear() {
	rrs.clear();
	position = 0;
	nsigs = 0;
}

private synchronized Iterator
iterator(boolean data, boolean cycle) {
	int size, start, total;

	total = rrs.size();

	if (data)
		size = total - nsigs;
	else
		size = nsigs;
	if (size == 0)
		return Collections.EMPTY_LIST.iterator();

	if (data) {
		if (!cycle)
			start = 0;
		else {
			if (position >= size)
				position = 0;
			start = position++;
		}
	} else {
		start = total - nsigs;
	}

	List list = new ArrayList(size);
	if (data) {
		list.addAll(rrs.subList(start, size));
		if (start != 0)
			list.addAll(rrs.subList(0, start));
	} else {
		list.addAll(rrs.subList(start, total));
	}

	return list.iterator();
}

/**
 * Returns an Iterator listing all (data) records.
 * @param cycle If true, cycle through the records so that each Iterator will
 * start with a different record.
 */
public synchronized Iterator
rrs(boolean cycle) {
	return iterator(true, cycle);
}

/**
 * Returns an Iterator listing all (data) records.  This cycles through
 * the records, so each Iterator will start with a different record.
 */
public synchronized Iterator
rrs() {
	return iterator(true, true);
}

/** Returns an Iterator listing all signature records */
public synchronized Iterator
sigs() {
	return iterator(false, false);
}

/** Returns the number of (data) records */
public int
size() {
	return rrs.size() - nsigs;
}

/**
 * Returns the name of the records
 * @see Name
 */
public Name
getName() {
	return first().getName();
}

/**
 * Returns the type of the records
 * @see Type
 */
public int
getType() {
	return first().getRRsetType();
}

/**
 * Returns the class of the records
 * @see DClass
 */
public int
getDClass() {
	return first().getDClass();
}

/** Returns the ttl of the records */
public synchronized long
getTTL() {
	return first().getTTL();
}

/**
 * Returns the first record
 * @throws IllegalStateException if the rrset is empty
 */
public synchronized Record
first() {
	if (rrs.size() == 0)
		throw new IllegalStateException("rrset is empty");
	return (Record) rrs.get(0);
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
	sb.append(iteratorToString(iterator(true, false)));
	if (nsigs > 0) {
		sb.append(" sigs: ");
		sb.append(iteratorToString(iterator(false, false)));
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4172.java