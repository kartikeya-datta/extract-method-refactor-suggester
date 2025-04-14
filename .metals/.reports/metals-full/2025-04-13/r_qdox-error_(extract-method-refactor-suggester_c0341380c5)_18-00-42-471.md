error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4077.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4077.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4077.java
text:
```scala
f@@or (int j = 0; j < records.length; j++) {

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.util.*;
import java.io.*;
import org.xbill.DNS.utils.*;

/**
 * A DNS Message.  A message is the basic unit of communication between
 * the client and server of a DNS operation.  A message consists of a Header
 * and 4 message sections.
 * @see Resolver
 * @see Header
 * @see Section
 *
 * @author Brian Wellington
 */

public class Message implements Cloneable {

private Header header;
private List [] sections;
private int size;
private byte [] wireFormat;
private boolean frozen;
boolean TSIGsigned, TSIGverified;

/** Creates a new Message with the specified Message ID */
public
Message(int id) {
	sections = new List[4];
	for (int i=0; i<4; i++)
		sections[i] = new LinkedList();
	header = new Header(id);
	wireFormat = null;
	frozen = false;
}

/** Creates a new Message with a random Message ID */
public
Message() {
	this(Header.randomID());
}

/**
 * Creates a new Message with a random Message ID suitable for sending as a
 * query.
 * @param r A record containing the question
 */
public static Message
newQuery(Record r) {
	Message m = new Message();
	m.header.setOpcode(Opcode.QUERY);
	m.header.setFlag(Flags.RD);
	m.addRecord(r, Section.QUESTION);
	return m;
}

/**
 * Creates a new Message to contain a dynamic update.  A random Message ID
 * the zone are filled in.
 * @param zone The zone to be updated
 */
public static Message
newUpdate(Name zone) {
	Message m = new Message();
	m.header.setOpcode(Opcode.UPDATE);
	Record soa = Record.newRecord(zone, Type.SOA, DClass.IN);
	m.addRecord(soa, Section.QUESTION);
	return m;
}

Message(DataByteInputStream in) throws IOException {
	this();
	header = new Header(in);
	for (int i = 0; i < 4; i++) {
		for (int j = 0; j < header.getCount(i); j++) {
			Record rec = Record.fromWire(in, i);
			sections[i].add(rec);
		}
	}
	size = in.getPos();
}

/** Creates a new Message from its DNS wire format representation */
public
Message(byte [] b) throws IOException {
	this(new DataByteInputStream(b));
}

/**
 * Replaces the Header with a new one
 * @see Header
 */
public void
setHeader(Header h) {
	header = h;
}

/**
 * Retrieves the Header
 * @see Header
 */
public Header
getHeader() {
	return header;
}

/**
 * Adds a record to a section of the Message, and adjusts the header
 * @see Record
 * @see Section
 */
public void
addRecord(Record r, int section) {
	sections[section].add(r);
	header.incCount(section);
}

/**
 * Removes a record from a section of the Message, and adjusts the header
 * @see Record
 * @see Section
 */
public boolean
removeRecord(Record r, int section) {
	if (sections[section].remove(r)) {
		header.decCount(section);
		return true;
	}
	else
		return false;
}

/**
 * Removes all records from a section of the Message, and adjusts the header
 * @see Record
 * @see Section
 */
public void
removeAllRecords(int section) {
	sections[section].clear();
	header.setCount(section, (short)0);
}

/**
 * Determines if the given record is already present in the given section
 * @see Record
 * @see Section
 */
public boolean
findRecord(Record r, int section) {
	return (sections[section].contains(r));
}

/**
 * Determines if the given record is already present in any section
 * @see Record
 * @see Section
 */
public boolean
findRecord(Record r) {
	return (sections[Section.ANSWER].contains(r) ||
		sections[Section.AUTHORITY].contains(r) ||
		sections[Section.ADDITIONAL].contains(r));
}

/**
 * Determines if an RRset with the given name and type is already
 * present in the given section
 * @see RRset
 * @see Section
 */
public boolean
findRRset(Name name, short type, int section) {
	for (int i = 0; i < sections[section].size(); i++) {
		Record r = (Record) sections[section].get(i);
		if (r.getType() == type && name.equals(r.getName()))
			return true;
	}
	return false;
}

/**
 * Determines if an RRset with the given name and type is already
 * present in any section
 * @see RRset
 * @see Section
 */
public boolean
findRRset(Name name, short type) {
	return (findRRset(name, type, Section.ANSWER) ||
		findRRset(name, type, Section.AUTHORITY) ||
		findRRset(name, type, Section.ADDITIONAL));
}

/**
 * Returns the first record in the QUESTION section
 * @see Record
 * @see Section
 */
public Record
getQuestion() {
	try {
		return (Record) sections[Section.QUESTION].get(0);
	}
	catch (NoSuchElementException e) {
		return null;
	}
}

/**
 * Returns the TSIG record from the ADDITIONAL section, if one is present
 * @see TSIGRecord
 * @see TSIG
 * @see Section
 */
public TSIGRecord
getTSIG() {
	int count = header.getCount(Section.ADDITIONAL);
	if (count == 0)
		return null;
	List l = sections[Section.ADDITIONAL];
	Record rec = (Record) l.get(count - 1);
	if (rec.type !=  Type.TSIG)
		return null;
	return (TSIGRecord) rec;
}

/**
 * Was this message signed by a TSIG?
 * @see TSIG
 */
public boolean
isSigned() {
	return TSIGsigned;
}

/**
 * If this message was signed by a TSIG, was the TSIG verified?
 * @see TSIG
 */
public boolean
isVerified() {
	return TSIGverified;
}

/**
 * Returns the OPT record from the ADDITIONAL section, if one is present
 * @see OPTRecord
 * @see Section
 */
public OPTRecord
getOPT() {
	Record [] additional = getSectionArray(Section.ADDITIONAL);
	for (int i = 0; i < additional.length; i++)
		if (additional[i] instanceof OPTRecord)
			return (OPTRecord) additional[i];
	return null;
}

/**
 * Returns the message's rcode (error code).  This incorporates the EDNS
 * extended rcode.
 */
public short
getRcode() {
	short rcode = header.getRcode();
	OPTRecord opt = getOPT();
	if (opt != null)
		rcode += (short)(opt.getExtendedRcode() << 4);
	return rcode;
}

/**
 * Returns an Enumeration listing all records in the given section
 * @see Record
 * @see Section
 */
public Enumeration
getSection(int section) {
	return Collections.enumeration(sections[section]);
}

/**
 * Returns an array containing all records in the given section
 * @see Record
 * @see Section
 */
public Record []
getSectionArray(int section) {
	List l = sections[section];
	return (Record []) l.toArray(new Record[l.size()]);
}

void
toWire(DataByteOutputStream out) throws IOException {
	header.toWire(out);
	Compression c = new Compression();
	for (int i = 0; i < 4; i++) {
		for (int j = 0; j < sections[i].size(); j++) {
			Record rec = (Record)sections[i].get(j);
			rec.toWire(out, i, c);
		}
	}
}

/**
 * Returns an array containing the wire format representation of the Message.
 */
public byte []
toWire() throws IOException {
	if (frozen && wireFormat != null)
		return wireFormat;
	DataByteOutputStream out = new DataByteOutputStream();
	toWire(out);
	size = out.getPos();
	if (frozen) {
		wireFormat = out.toByteArray();
		return wireFormat;
	}
	else
		return out.toByteArray();
}

/**
 * Indicates that a message's contents will not be changed until a thaw
 * operation.
 * @see #thaw
 */
public void
freeze() {
	frozen = true;
}

/**
 * Indicates that a message's contents can now change (are no longer frozen).
 * @see #freeze
 */
public void
thaw() {
	frozen = false;
	wireFormat = null;
}

/**
 * Returns the size of the message.  Only valid if the message has been
 * converted to or from wire format.
 */
public int
numBytes() {
	return size;
}

/**
 * Converts the given section of the Message to a String
 * @see Section
 */
public String
sectionToString(int i) {
	if (i > 3)
		return null;

	StringBuffer sb = new StringBuffer();

	Record [] records = getSectionArray(i);
	for (int j = 0; i < records.length; j++) {
		Record rec = records[j];
		if (i == Section.QUESTION) {
			sb.append(";;\t" + rec.name);
			sb.append(", type = " + Type.string(rec.type));
			sb.append(", class = " + DClass.string(rec.dclass));
		}
		else
			sb.append(rec);
		sb.append("\n");
	}
	return sb.toString();
}

/**
 * Converts the Message to a String
 */
public String
toString() {
	StringBuffer sb = new StringBuffer();
	OPTRecord opt = getOPT();
	if (opt != null)
		sb.append(header.toStringWithRcode(getRcode()) + "\n");
	else
		sb.append(header + "\n");
	if (isSigned()) {
		sb.append(";; TSIG ");
		if (isVerified())
			sb.append("ok");
		else
			sb.append("invalid");
		sb.append('\n');
	}
	for (int i = 0; i < 4; i++) {
		if (header.getOpcode() != Opcode.UPDATE)
			sb.append(";; " + Section.longString(i) + ":\n");
		else
			sb.append(";; " + Section.updString(i) + ":\n");
		sb.append(sectionToString(i) + "\n");
	}
	sb.append(";; done (" + numBytes() + " bytes)");
	return sb.toString();
}

/**
 * Creates a copy of this Message.  This is done by the Resolver before adding
 * TSIG and OPT records, for example.
 * @see Resolver
 * @see TSIGRecord
 * @see OPTRecord
 */
public Object
clone() {
	Message m = new Message();
	for (int i = 0; i < sections.length; i++)
		m.sections[i] = new LinkedList(sections[i]);
	m.header = (Header) header.clone();
	m.size = size;
	return m;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4077.java