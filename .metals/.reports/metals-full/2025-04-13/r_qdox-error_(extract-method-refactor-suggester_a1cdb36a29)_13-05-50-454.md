error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3489.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3489.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3489.java
text:
```scala
i@@f (counts[field] == 0)

// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.io.*;
import java.util.*;

/**
 * A DNS message header
 * @see Message
 *
 * @author Brian Wellington
 */

public class Header {

private int id; 
private int flags;
private int [] counts;

private static Random random = new Random();

/** The length of a DNS Header in wire format. */
public static final int LENGTH = 12;

private void
init() {
	counts = new int[4];
	flags = 0;
	id = -1;
}

/**
 * Create a new empty header.
 * @param id The message id
 */
public
Header(int id) {
	init();
	setID(id);
}

/**
 * Create a new empty header with a random message id
 */
public
Header() {
	init();
}

/**
 * Parses a Header from a stream containing DNS wire format.
 */
Header(DNSInput in) throws IOException {
	this(in.readU16());
	flags = in.readU16();
	for (int i = 0; i < counts.length; i++)
		counts[i] = in.readU16();
}

/**
 * Creates a new Header from its DNS wire format representation
 * @param b A byte array containing the DNS Header.
 */
public
Header(byte [] b) throws IOException {
	this(new DNSInput(b));
}

void
toWire(DNSOutput out) {
	out.writeU16(getID());
	out.writeU16(flags);
	for (int i = 0; i < counts.length; i++)
		out.writeU16(counts[i]);
}

public byte []
toWire() {
	DNSOutput out = new DNSOutput();
	toWire(out);
	return out.toByteArray();
}

static private boolean
validFlag(int bit) {
	return (bit >= 0 && bit <= 0xF && Flags.isFlag(bit));
}

static private void
checkFlag(int bit) {
	if (!validFlag(bit))
		throw new IllegalArgumentException("invalid flag bit " + bit);
}

/**
 * Sets a flag to the supplied value
 * @see Flags
 */
public void
setFlag(int bit) {
	checkFlag(bit);
	// bits are indexed from left to right
	flags |= (1 << (15 - bit));
}

/**
 * Sets a flag to the supplied value
 * @see Flags
 */
public void
unsetFlag(int bit) {
	checkFlag(bit);
	// bits are indexed from left to right
	flags &= ~(1 << (15 - bit));
}

/**
 * Retrieves a flag
 * @see Flags
 */
public boolean
getFlag(int bit) {
	checkFlag(bit);
	// bits are indexed from left to right
	return (flags & (1 << (15 - bit))) != 0;
}

boolean []
getFlags() {
	boolean [] array = new boolean[16];
	for (int i = 0; i < array.length; i++)
		if (validFlag(i))
			array[i] = getFlag(i);
	return array;
}

/**
 * Retrieves the message ID
 */
public int
getID() {
	if (id >= 0)
		return id;
	synchronized (this) {
		if (id < 0)
			id = random.nextInt(0xffff);
		return id;
	}
}

/**
 * Sets the message ID
 */
public void
setID(int id) {
	if (id < 0 || id > 0xffff)
		throw new IllegalArgumentException("DNS message ID " + id +
						   " is out of range");
	this.id = id;
}

/**
 * Sets the message's rcode
 * @see Rcode
 */
public void
setRcode(int value) {
	if (value < 0 || value > 0xF)
		throw new IllegalArgumentException("DNS Rcode " + value +
						   " is out of range");
	flags &= ~0xF;
	flags |= value;
}

/**
 * Retrieves the mesasge's rcode
 * @see Rcode
 */
public int
getRcode() {
	return flags & 0xF;
}

/**
 * Sets the message's opcode
 * @see Opcode
 */
public void
setOpcode(int value) {
	if (value < 0 || value > 0xF)
		throw new IllegalArgumentException("DNS Opcode " + value +
						   "is out of range");
	flags &= ~0x87FF;
	flags |= (value << 11);
}

/**
 * Retrieves the mesasge's opcode
 * @see Opcode
 */
public int
getOpcode() {
	return (flags >> 11) & 0xF;
}

void
setCount(int field, int value) {
	if (value < 0 || value > 0xFF)
		throw new IllegalArgumentException("DNS section count " +
						   value + " is out of range");
	counts[field] = value;
}

void
incCount(int field) {
	if (counts[field] == 0xFF)
		throw new IllegalStateException("DNS section count cannot " +
						"be incremented");
	counts[field]++;
}

void
decCount(int field) {
	if (counts[field] == 0xFF)
		throw new IllegalStateException("DNS section count cannot " +
						"be decremented");
	counts[field]--;
}

/**
 * Retrieves the record count for the given section
 * @see Section
 */
public int
getCount(int field) {
	return counts[field];
}

/** Converts the header's flags into a String */
public String
printFlags() {
	StringBuffer sb = new StringBuffer();

	for (int i = 0; i < 16; i++)
		if (validFlag(i) && getFlag(i)) {
			sb.append(Flags.string(i));
			sb.append(" ");
		}
	return sb.toString();
}

String
toStringWithRcode(int newrcode) {
	StringBuffer sb = new StringBuffer();

	sb.append(";; ->>HEADER<<- "); 
	sb.append("opcode: " + Opcode.string(getOpcode()));
	sb.append(", status: " + Rcode.string(newrcode));
	sb.append(", id: " + getID());
	sb.append("\n");

	sb.append(";; flags: " + printFlags());
	sb.append("; ");
	for (int i = 0; i < 4; i++)
		sb.append(Section.string(i) + ": " + getCount(i) + " ");
	return sb.toString();
}

/** Converts the header into a String */
public String
toString() {
	return toStringWithRcode(getRcode());
}

/* Creates a new Header identical to the current one */
public Object
clone() {
	Header h = new Header();
	h.id = id;
	h.flags = flags;
	System.arraycopy(counts, 0, h.counts, 0, counts.length);
	return h;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3489.java