error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3156.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3156.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3156.java
text:
```scala
s@@b.append((long)ttl & 0xFFFFFFFFL);

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import org.xbill.DNS.utils.*;

/**
 * The base class that all records are derived from.
 *
 * @author Brian Wellington
 */

abstract public class Record implements Cloneable {

protected Name name;
protected short type, dclass;
protected int ttl;
protected int wireLength = -1;

private	static Class [] knownTypes = new Class[256];

private static Class [] fromWireList = new Class [] {Name.class,
                                                     Short.TYPE,
                                                     Integer.TYPE,
                                                     Integer.TYPE,
                                                     DataByteInputStream.class,
                                                     Compression.class};
private static Class [] fromTextList = new Class [] {Name.class,
						     Short.TYPE,
						     Integer.TYPE,
						     MyStringTokenizer.class,
						     Name.class};

protected
Record() {}

Record(Name _name, short _type, short _dclass, int _ttl) {
	name = _name;
	type = _type;
	dclass = _dclass;
	ttl = _ttl;
}

private static final Class
toClass(short type) throws ClassNotFoundException {
	/*
	 * First, see if we've already found this type.
	 */
	if (type < 0 || type > 255)
		throw new ClassNotFoundException();
	if (knownTypes[type] != null)
		return knownTypes[type];

	String s = Record.class.toString();
	/*
	 * Remove "class " from the beginning, and "Record" from the end.
	 * Then construct the new class name.
	 */
	knownTypes[type] = Class.forName(s.substring(6, s.length() - 6) +
					 Type.string(type) + "Record");
	return knownTypes[type];
}

private static Record
newRecord(Name name, short type, short dclass, int ttl, int length,
	  DataByteInputStream in, Compression c) throws IOException
{
	Record rec;
	try {
		Class rrclass;
		Constructor m;

		rrclass = toClass(type);
		m = rrclass.getDeclaredConstructor(fromWireList);
		rec = (Record) m.newInstance(new Object [] {
							name,
							new Short(dclass),
							new Integer(ttl),
							new Integer(length),
							in, c
						});
		return rec;
	}
	catch (ClassNotFoundException e) {
		rec = new UNKRecord(name, type, dclass, ttl, length, in, c);
		rec.wireLength = length;
		return rec;
	}
	catch (InvocationTargetException e) {
		if (Options.check("verbose")) {
			System.err.println("new record: " + e);
			System.err.println(e.getTargetException());
		}
		return null;
	}
	catch (Exception e) {
		if (Options.check("verbose"))
			System.err.println("new record: " + e);
		return null;
	}
}

/**
 * Creates a new record, with the given parameters.
 * @return An object of a type extending Record
 */
public static Record
newRecord(Name name, short type, short dclass, int ttl, int length,
	  byte [] data)
{
	DataByteInputStream dbs;
	if (data != null)
		dbs = new DataByteInputStream(data);
	else
		dbs = null;
	try {
		return newRecord(name, type, dclass, ttl, length, dbs, null);
	}
	catch (IOException e) {
		return null;
	}
}

/**
 * Creates a new empty record, with the given parameters.
 * @return An object of a type extending Record
 */
public static Record
newRecord(Name name, short type, short dclass, int ttl) {
	return newRecord(name, type, dclass, ttl, 0, null);
}

/**
 * Creates a new empty record, with the given parameters.  This method is
 * designed to create records that will be added to the QUERY section
 * of a message.
 * @return An object of a type extending Record
 */
public static Record
newRecord(Name name, short type, short dclass) {
	return newRecord(name, type, dclass, 0, 0, null);
}

static Record
fromWire(DataByteInputStream in, int section, Compression c)
throws IOException
{
	short type, dclass;
	int ttl;
	short length;
	Name name;
	Record rec;
	int start, datastart;

	start = in.getPos();

	name = new Name(in, c);

	type = in.readShort();
	dclass = in.readShort();

	if (section == Section.QUESTION)
		return newRecord(name, type, dclass);

	ttl = in.readInt();
	length = in.readShort();
	datastart = in.getPos();
	rec = newRecord(name, type, dclass, ttl, length, in, c);
	if (in.getPos() - datastart != length)
		throw new IOException("Invalid record length");
	rec.wireLength = in.getPos() - start;
	return rec;
}

/**
 * Builds a Record from DNS uncompressed wire format.
 */
public static Record
fromWire(byte [] b, int section) throws IOException {
	DataByteInputStream in = new DataByteInputStream(b);
	return fromWire(in, section, null);
}

void
toWire(DataByteOutputStream out, int section, Compression c)
throws IOException
{
	int start = out.getPos();
	name.toWire(out, c);
	out.writeShort(type);
	out.writeShort(dclass);
	if (section == Section.QUESTION)
		return;
	out.writeInt(ttl);
	int lengthPosition = out.getPos();
	out.writeShort(0); /* until we know better */
	rrToWire(out, c);
	out.writeShortAt(out.getPos() - lengthPosition - 2, lengthPosition);
	wireLength = out.getPos() - start;
}

/**
 * Converts a Record into DNS uncompressed wire format.
 */
public byte []
toWire(int section) throws IOException {
	DataByteOutputStream out = new DataByteOutputStream();
	toWire(out, section, null);
	return out.toByteArray();
}

void
toWireCanonical(DataByteOutputStream out) throws IOException {
	name.toWireCanonical(out);
	out.writeShort(type);
	out.writeShort(dclass);
	out.writeInt(ttl);
	int lengthPosition = out.getPos();
	out.writeShort(0); /* until we know better */
	rrToWireCanonical(out);
	out.writeShortAt(out.getPos() - lengthPosition - 2, lengthPosition);
}

/**
 * Converts a Record into canonical DNS uncompressed wire format (all names are
 * converted to lowercase).
 */
public byte []
toWireCanonical() throws IOException {
	DataByteOutputStream out = new DataByteOutputStream();
	toWireCanonical(out);
	return out.toByteArray();
}


StringBuffer
toStringNoData() {
	StringBuffer sb = new StringBuffer();
	sb.append(name);
	sb.append("\t");
	if (Options.check("BINDTTL"))
		sb.append(TTL.format(ttl));
	else
		sb.append(ttl);
	sb.append(" ");
	if (dclass != DClass.IN || !Options.check("noPrintIN")) {
		sb.append(DClass.string(dclass));
		sb.append(" ");
	}
	sb.append(Type.string(type));
	sb.append("\t\t");
	return sb;
}

/**
 * Converts a Record into a String representation
 */
public String
toString() {
	StringBuffer sb = toStringNoData();
	sb.append("<unknown format>");
	return sb.toString();
}

/**
 * Builds a new Record from its textual representation
 */
public static Record
fromString(Name name, short type, short dclass, int ttl,
	   MyStringTokenizer st, Name origin)
throws IOException
{
	Record rec;

	String s = st.nextToken();
	/* the string tokenizer loses the \\. */
	if (s.equals("#")) {
		s = st.nextToken();
		short length = Short.parseShort(s);
		s = st.remainingTokens();
		byte [] data = base16.fromString(s);
		if (length != data.length)
			throw new IOException("Invalid unknown RR encoding: length mismatch");
		return newRecord(name, type, dclass, ttl, length, data);
	}
	st.putBackToken(s);

	try {
		Class rrclass;
		Constructor m;

		rrclass = toClass(type);
		m = rrclass.getDeclaredConstructor(fromTextList);
		rec = (Record) m.newInstance(new Object [] {
						name,
						new Short(dclass),
						new Integer(ttl),
						st, origin
					     });
		return rec;
	}
	catch (ClassNotFoundException e) {
		rec = new UNKRecord(name, type, dclass, ttl, st, origin);
		return rec;
	}
	catch (InvocationTargetException e) {
		if (Options.check("verbose")) {
			System.err.println("from text: " + e);
			System.err.println(e.getTargetException());
		}
		return null;
	}
	catch (Exception e) {
		if (Options.check("verbose"))
			System.err.println("from text: " + e);
		return null;
	}
}

/**
 * Returns the record's name
 * @see Name
 */
public Name
getName() {
	return name;
}

/**
 * Returns record's type
 * @see Type
 */
public short
getType() {
	return type;
}

/**
 * Returns the type of RRset that this record would belong to.  For all types
 * except SIGRecord, this is equivalent to getType().
 * @return The type of record, if not SIGRecord.  If the type is SIGRecord,
 * the type covered is returned.
 * @see Type
 * @see RRset
 * @see SIGRecord
 */
public short
getRRsetType() {
	if (type == Type.SIG) {
		SIGRecord sig = (SIGRecord) this;
		return sig.getTypeCovered();
	}
	return type;
}

/**
 * Returns the record's class
 */
public short
getDClass() {
	return dclass;
}

/**
 * Returns the record's TTL
 */
public int
getTTL() {
	return ttl;
}

/**
 * Returns the length of this record in wire format, based on the last time
 * this record was parsed from data or converted to data.  The wire format
 * may or may not be compressed
 * @return The last known length, or -1 if the record has never been in wire
 * format
 */
public short
getWireLength() {
	return (short) wireLength;
}

/**
 * Converts the type-specific RR to wire format - must be overriden
 */
abstract void rrToWire(DataByteOutputStream out, Compression c) throws IOException;

/**
 * Converts the type-specific RR to canonical wire format - must be overriden
 * if the type-specific RR data includes a Name
 * @see Name
 */
void
rrToWireCanonical(DataByteOutputStream out) throws IOException {
	rrToWire(out, null);
}

/**
 * Determines if two Records are identical
 */
public boolean
equals(Object arg) {
	if (arg == null || !(arg instanceof Record))
		return false;
	Record r = (Record) arg;
	try {
		byte [] array1 = toWire(Section.ANSWER);
		byte [] array2 = r.toWire(Section.ANSWER);
		if (array1.length != array2.length)
			return false;
		for (int i = 0; i < array1.length; i++)
			if (array1[i] != array2[i])
				return false;
		return true;
	}
	catch (IOException e) {
		return false;
	}
}

/**
 * Generates a hash code based on the Record's data
 */
public int
hashCode() {
	try {
		byte [] array1 = toWire(Section.ANSWER);
		return array1.hashCode();
	}
	catch (IOException e) {
		return 0;
	}
}

/**
 * Creates a new record identical to the current record, but with a different
 * name.  This is most useful for replacing the name of a wildcard record.
 */
public Record
withName(Name name) {
	Record rec = null;
	try {
		rec = (Record) clone();
	}
	catch (CloneNotSupportedException e) {
	}
	rec.name = name;
	return rec;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3156.java