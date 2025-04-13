error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2749.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2749.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2749.java
text:
```scala
o@@pcode = (byte) ((flagsval >> 11) & 0xF);

// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.io.*;
import java.util.*;
import org.xbill.DNS.utils.*;

/**
 * A DNS message header
 * @see Message
 *
 * @author Brian Wellington
 */

public class Header {

private int id; 
private boolean [] flags;
private int rcode;
private int opcode;
private int [] counts;

private static Random random = new Random();

/** The length of a DNS Header in wire format. */
public static final int LENGTH = 12;

/**
 * Create a new empty header.
 * @param id The message id
 */
public
Header(int id) {
	counts = new int[4];
	flags = new boolean[16];
	this.id = id;
}

/**
 * Create a new empty header with a random message id
 */
public
Header() {
	this(random.nextInt(0xffff));
}

/**
 * Parses a Header from a stream containing DNS wire format.
 */
Header(DNSInput in) throws IOException {
	this(in.readU16());
	readFlags(in);
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
	writeFlags(out);
	for (int i = 0; i < counts.length; i++)
		out.writeU16(counts[i]);
}

public byte []
toWire() {
	DNSOutput out = new DNSOutput();
	toWire(out);
	return out.toByteArray();
}

/**
 * Sets a flag to the supplied value
 * @see Flags
 */
public void
setFlag(int bit) {
	flags[bit] = true;
}

/**
 * Sets a flag to the supplied value
 * @see Flags
 */
public void
unsetFlag(int bit) {
	flags[bit] = false;
}

/**
 * Retrieves a flag
 * @see Flags
 */
public boolean
getFlag(int bit) {
	return flags[bit];
}

boolean []
getFlags() {
	return flags;
}

/**
 * Retrieves the message ID
 */
public int
getID() {
	return id & 0xFFFF;
}

/**
 * Sets the message ID
 */
public void
setID(int id) {
	if (opcode > 0xFF)
		throw new IllegalArgumentException("DNS message ID " + id +
						   "is out of range");
	this.id = id;
}

/**
 * Generates a random number suitable for use as a message ID
 */
static int
randomID() {
	return (random.nextInt(0xffff));
}

/**
 * Sets the message's rcode
 * @see Rcode
 */
public void
setRcode(int value) {
	if (opcode > 0xF)
		throw new IllegalArgumentException("DNS Rcode " + value +
						   "is out of range");
	rcode = value;
}

/**
 * Retrieves the mesasge's rcode
 * @see Rcode
 */
public int
getRcode() {
	return rcode;
}

/**
 * Sets the message's opcode
 * @see Opcode
 */
public void
setOpcode(int value) {
	if (opcode > 0xF)
		throw new IllegalArgumentException("DNS Opcode " + value +
						   "is out of range");
	opcode = value;
}

/**
 * Retrieves the mesasge's opcode
 * @see Opcode
 */
public int
getOpcode() {
	return opcode;
}

void
setCount(int field, int value) {
	if (value > 0xFF)
		throw new IllegalArgumentException("DNS section count " +
						   value +
						   "is out of range");
	counts[field] = value;
}

void
incCount(int field) {
	counts[field]++;
}

void
decCount(int field) {
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

private void
writeFlags(DNSOutput out) {
	int flagsval = 0;
	for (int i = 0; i < 16; i++) {
		if (flags[i])
			flagsval |= (1 << (15-i));
	}
	flagsval |= (opcode << 11);
	flagsval |= (rcode);
	out.writeU16(flagsval);
}

private void
readFlags(DNSInput in) throws IOException {
	int flagsval = in.readU16();
	for (int i = 0; i < 16; i++) {
		flags[i] = ((flagsval & (1 << (15 - i))) != 0);
	}
	opcode = (byte) ((flagsval >> 3) & 0xF);
	rcode = (byte) (flagsval & 0xF);
}

/** Converts the header's flags into a String */
public String
printFlags() {
	StringBuffer sb = new StringBuffer();

	for (int i = 0; i < flags.length; i++)
		if (Flags.isFlag(i) && getFlag(i)) {
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
	Header h = new Header(id);
	System.arraycopy(counts, 0, h.counts, 0, counts.length);
	System.arraycopy(flags, 0, h.flags, 0, flags.length);
	h.rcode = rcode;
	h.rcode = rcode;
	h.opcode = opcode;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2749.java