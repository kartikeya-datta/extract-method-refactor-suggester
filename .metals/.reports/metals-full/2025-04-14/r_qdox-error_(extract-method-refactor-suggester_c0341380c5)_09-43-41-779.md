error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6604.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6604.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6604.java
text:
```scala
i@@f (!xfrin.isAXFR())

// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.io.*;
import java.util.*;

/**
 * A DNS Zone.  This encapsulates all data related to a Zone, and provides
 * convenient lookup methods.
 *
 * @author Brian Wellington
 */

public class Zone {

/** A primary zone */
public static final int PRIMARY = 1;

/** A secondary zone */
public static final int SECONDARY = 2;

private Map data;
private int type;
private Name origin;
private Object originNode;
private int dclass = DClass.IN;
private RRset NS;
private SOARecord SOA;
private boolean hasWild;

class ZoneIterator implements Iterator {
	private Iterator zentries;
	private RRset [] current;
	private int count;
	private boolean wantLastSOA;

	ZoneIterator(boolean axfr) {
		zentries = data.entrySet().iterator();
		wantLastSOA = axfr;
		RRset [] sets = allRRsets(originNode);
		current = new RRset[sets.length];
		for (int i = 0, j = 2; i < sets.length; i++) {
			int type = sets[i].getType();
			if (type == Type.SOA)
				current[0] = sets[i];
			else if (type == Type.NS)
				current[1] = sets[i];
			else
				current[j++] = sets[i];
		}
	}

	public boolean
	hasNext() {
		return (current != null || wantLastSOA);
	}

	public Object
	next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		if (current == null && wantLastSOA) {
			wantLastSOA = false;
			return oneRRset(originNode, Type.SOA);
		}
		Object set = current[count++];
		if (count == current.length) {
			current = null;
			while (zentries.hasNext()) {
				Map.Entry entry = (Map.Entry) zentries.next();
				if (entry.getKey().equals(origin))
					continue;
				RRset [] sets = allRRsets(entry.getValue());
				if (sets.length == 0)
					continue;
				current = sets;
				count = 0;
				break;
			}
		}
		return set;
	}

	public void
	remove() {
		throw new UnsupportedOperationException();
	}
}

private void
validate() throws IOException {
	originNode = exactName(origin);
	if (originNode == null)
		throw new IOException(origin + ": no data specified");

	RRset rrset = oneRRset(originNode, Type.SOA);
	if (rrset == null || rrset.size() != 1)
		throw new IOException(origin +
				      ": exactly 1 SOA must be specified");
	Iterator it = rrset.rrs();
	SOA = (SOARecord) it.next();

	NS = oneRRset(originNode, Type.NS);
	if (NS == null)
		throw new IOException(origin + ": no NS set specified");
}

private final void
maybeAddRecord(Record record) throws IOException {
	int rtype = record.getType();
	Name name = record.getName();

	if (rtype == Type.SOA && !name.equals(origin)) {
		throw new IOException("SOA owner " + name +
				      " does not match zone origin " +
				      origin);
	}
	if (name.subdomain(origin))
		addRecord(record);
}

/**
 * Creates a Zone from the records in the specified master file.
 * @param zone The name of the zone.
 * @param file The master file to read from.
 * @see Master
 */
public
Zone(Name zone, String file) throws IOException {
	data = new HashMap();
	type = PRIMARY;

	if (zone == null)
		throw new IllegalArgumentException("no zone name specified");
	Master m = new Master(file, zone);
	Record record;

	origin = zone;
	while ((record = m.nextRecord()) != null)
		maybeAddRecord(record);
	validate();
}

/**
 * Creates a Zone from an array of records.
 * @param zone The name of the zone.
 * @param records The records to add to the zone.
 * @see Master
 */
public
Zone(Name zone, Record [] records) throws IOException {
	data = new HashMap();
	type = PRIMARY;

	if (zone == null)
		throw new IllegalArgumentException("no zone name specified");
	origin = zone;
	for (int i = 0; i < records.length; i++)
		maybeAddRecord(records[i]);
	validate();
}

private void
fromXFR(ZoneTransferIn xfrin) throws IOException, ZoneTransferException {
	data = new HashMap();
	type = SECONDARY;

	if (xfrin.getType() != Type.AXFR)
		throw new IllegalArgumentException("zones can only be " +
						   "created from AXFRs");
	origin = xfrin.getName();
	List records = xfrin.run();
	for (Iterator it = records.iterator(); it.hasNext(); ) {
		Record record = (Record) it.next();
		maybeAddRecord(record);
	}
	validate();
}

/**
 * Creates a Zone by doing the specified zone transfer.
 * @param xfrin The incoming zone transfer to execute.
 * @see ZoneTransferIn
 */
public
Zone(ZoneTransferIn xfrin) throws IOException, ZoneTransferException {
	fromXFR(xfrin);
}

/**
 * Creates a Zone by performing a zone transfer to the specified host.
 * @see ZoneTransferIn
 */
public
Zone(Name zone, int dclass, String remote)
throws IOException, ZoneTransferException
{
	ZoneTransferIn xfrin = ZoneTransferIn.newAXFR(zone, remote, null);
	xfrin.setDClass(dclass);
	fromXFR(xfrin);
}

/** Returns the Zone's origin */
public Name
getOrigin() {
	return origin;
}

/** Returns the Zone origin's NS records */
public RRset
getNS() {
	return NS;
}

/** Returns the Zone's SOA record */
public SOARecord
getSOA() {
	return SOA;
}

/** Returns the Zone's class */
public int
getDClass() {
	return dclass;
}

private synchronized Object
exactName(Name name) {
	return data.get(name);
}

private synchronized RRset []
allRRsets(Object types) {
	if (types instanceof List) {
		List typelist = (List) types;
		return (RRset []) typelist.toArray(new RRset[typelist.size()]);
	} else {
		RRset set = (RRset) types;
		return new RRset [] {set};
	}
}

private synchronized RRset
oneRRset(Object types, int type) {
	if (type == Type.ANY)
		throw new IllegalArgumentException("oneRRset(ANY)");
	if (types instanceof List) {
		List list = (List) types;
		for (int i = 0; i < list.size(); i++) {
			RRset set = (RRset) list.get(i);
			if (set.getType() == type)
				return set;
		}
	} else {
		RRset set = (RRset) types;
		if (set.getType() == type)
			return set;
	}
	return null;
}

private synchronized RRset
findRRset(Name name, int type) {
	Object types = exactName(name);
	if (types == null)
		return null;
	return oneRRset(types, type);
}

private synchronized void
addRRset(Name name, RRset rrset) {
	if (!hasWild && name.isWild())
		hasWild = true;
	Object types = data.get(name);
	if (types == null) {
		data.put(name, rrset);
		return;
	}
	int rtype = rrset.getType();
	if (types instanceof List) {
		List list = (List) types;
		for (int i = 0; i < list.size(); i++) {
			RRset set = (RRset) list.get(i);
			if (set.getType() == rtype) {
				list.set(i, rrset);
				return;
			}
		}
		list.add(rrset);
	} else {
		RRset set = (RRset) types;
		if (set.getType() == rtype)
			data.put(name, rrset);
		else {
			LinkedList list = new LinkedList();
			list.add(set);
			list.add(rrset);
			data.put(name, list);
		}
	}
}

private synchronized void
removeRRset(Name name, int type) {
	Object types = data.get(name);
	if (types == null) {
		return;
	}
	if (types instanceof List) {
		List list = (List) types;
		for (int i = 0; i < list.size(); i++) {
			RRset set = (RRset) list.get(i);
			if (set.getType() == type) {
				list.remove(i);
				if (list.size() == 0)
					data.remove(name);
				return;
			}
		}
	} else {
		RRset set = (RRset) types;
		if (set.getType() != type)
			return;
		data.remove(name);
	}
}

private synchronized SetResponse
lookup(Name name, int type) {
	int labels;
	int olabels;
	int tlabels;
	RRset rrset;
	Name tname;
	Object types;
	SetResponse sr;

	if (!name.subdomain(origin))
		return SetResponse.ofType(SetResponse.NXDOMAIN);

	labels = name.labels();
	olabels = origin.labels();

	for (tlabels = olabels; tlabels <= labels; tlabels++) {
		boolean isOrigin = (tlabels == olabels);
		boolean isExact = (tlabels == labels);

		if (isOrigin)
			tname = origin;
		else if (isExact)
			tname = name;
		else
			tname = new Name(name, labels - tlabels);

		types = exactName(tname);
		if (types == null)
			continue;

		/* If this is a delegation, return that. */
		if (!isOrigin) {
			RRset ns = oneRRset(types, Type.NS);
			if (ns != null)
				return new SetResponse(SetResponse.DELEGATION,
						       ns);
		}

		/* If this is an ANY lookup, return everything. */
		if (isExact && type == Type.ANY) {
			sr = new SetResponse(SetResponse.SUCCESSFUL);
			RRset [] sets = allRRsets(types);
			for (int i = 0; i < sets.length; i++)
				sr.addRRset(sets[i]);
			return sr;
		}

		/*
		 * If this is the name, look for the actual type or a CNAME.
		 * Otherwise, look for a DNAME.
		 */
		if (isExact) {
			rrset = oneRRset(types, type);
			if (rrset != null) {
				sr = new SetResponse(SetResponse.SUCCESSFUL);
				sr.addRRset(rrset);
				return sr;
			}
			rrset = oneRRset(types, Type.CNAME);
			if (rrset != null)
				return new SetResponse(SetResponse.CNAME,
						       rrset);
		} else {
			rrset = oneRRset(types, Type.DNAME);
			if (rrset != null)
				return new SetResponse(SetResponse.DNAME,
						       rrset);
		}

		/* We found the name, but not the type. */
		if (isExact)
			return SetResponse.ofType(SetResponse.NXRRSET);
	}

	if (hasWild) {
		for (int i = 0; i < labels - olabels; i++) {
			tname = name.wild(i + 1);

			types = exactName(tname);
			if (types == null)
				continue;

			rrset = oneRRset(types, type);
			if (rrset != null) {
				sr = new SetResponse(SetResponse.SUCCESSFUL);
				sr.addRRset(rrset);
				return sr;
			}
		}
	}

	return SetResponse.ofType(SetResponse.NXDOMAIN);
}

/**     
 * Looks up Records in the Zone.  This follows CNAMEs and wildcards.
 * @param name The name to look up
 * @param type The type to look up
 * @return A SetResponse object
 * @see SetResponse
 */ 
public SetResponse
findRecords(Name name, int type) {
	return lookup(name, type);
}

/**
 * Looks up Records in the zone, finding exact matches only.
 * @param name The name to look up
 * @param type The type to look up
 * @return The matching RRset
 * @see RRset
 */ 
public RRset
findExactMatch(Name name, int type) {
	Object types = exactName(name);
	if (types == null)
		return null;
	return oneRRset(types, type);
}

/**
 * Adds an RRset to the Zone
 * @param rrset The RRset to be added
 * @see RRset
 */
public void
addRRset(RRset rrset) {
	Name name = rrset.getName();
	addRRset(name, rrset);
}

/**
 * Adds a Record to the Zone
 * @param r The record to be added
 * @see Record
 */
public void
addRecord(Record r) {
	Name name = r.getName();
	int rtype = r.getRRsetType();
	synchronized (this) {
		RRset rrset = findRRset(name, rtype);
		if (rrset == null) {
			rrset = new RRset(r);
			addRRset(name, rrset);
		} else {
			rrset.addRR(r);
		}
	}
}

/**
 * Removes a record from the Zone
 * @param r The record to be removed
 * @see Record
 */
public void
removeRecord(Record r) {
	Name name = r.getName();
	int rtype = r.getRRsetType();
	synchronized (this) {
		RRset rrset = findRRset(name, rtype);
		if (rrset == null)
			return;
		rrset.deleteRR(r);
		if (rrset.size() == 0)
			removeRRset(name, rtype);
	}
}

/**
 * Returns an Iterator over the RRsets in the zone.
 */
public Iterator
iterator() {
	return new ZoneIterator(false);
}

/**
 * Returns an Iterator over the RRsets in the zone that can be used to
 * construct an AXFR response.  This is identical to {@link #iterator} except
 * that the SOA is returned at the end as well as the beginning.
 */
public Iterator
AXFR() {
	return new ZoneIterator(true);
}

private void
nodeToString(StringBuffer sb, Object node) {
	RRset [] sets = allRRsets(node);
	for (int i = 0; i < sets.length; i++) {
		RRset rrset = sets[i];
		Iterator it = rrset.rrs();
		while (it.hasNext())
			sb.append(it.next() + "\n");
		it = rrset.sigs();
		while (it.hasNext())
			sb.append(it.next() + "\n");
	}
}

/**
 * Returns the contents of the Zone in master file format.
 */
public String
toMasterFile() {
	Iterator zentries = data.entrySet().iterator();
	StringBuffer sb = new StringBuffer();
	nodeToString(sb, originNode);
	while (zentries.hasNext()) {
		Map.Entry entry = (Map.Entry) zentries.next();
		if (!origin.equals(entry.getKey()))
			nodeToString(sb, entry.getValue());
	}
	return sb.toString();
}

/**
 * Returns the contents of the Zone as a string (in master file format).
 */
public String
toString() {
	return toMasterFile();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6604.java