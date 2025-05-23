error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4245.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4245.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4245.java
text:
```scala
S@@IGBase sig = (SIGBase) this;

// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.io.*;
import java.text.*;
import java.lang.reflect.*;
import java.util.*;
import org.xbill.DNS.utils.*;

/**
 * A generic DNS resource record.  The specific record types extend this class.
 * A record contains a name, type, class, and rdata.
 *
 * @author Brian Wellington
 */

public abstract class Record implements Cloneable, Comparable {

protected Name name;
protected int type, dclass;
protected long ttl;
private boolean empty;

private static final Record [] knownRecords = new Record[256];
private static final Class [] emptyClassArray = new Class[0];
private static final Object [] emptyObjectArray = new Object[0];

private static final DecimalFormat byteFormat = new DecimalFormat();

static {
	byteFormat.setMinimumIntegerDigits(3);
}

protected
Record() {}

Record(Name name, int type, int dclass, long ttl) {
	if (!name.isAbsolute())
		throw new RelativeNameException(name);
	Type.check(type);
	DClass.check(dclass);
	TTL.check(ttl);
	this.name = name;
	this.type = type;
	this.dclass = dclass;
	this.ttl = ttl;
}

private static final Record
getTypedObject(int type) {
	if (type < 0 || type > knownRecords.length)
		return UNKRecord.getMember();
	if (knownRecords[type] != null)
		return knownRecords[type];

	/* Construct the class name by putting the type before "Record". */
	String s = Record.class.getName();
	StringBuffer sb = new StringBuffer(s);
	sb.insert(s.lastIndexOf("Record"), Type.string(type));

	try {
		Class c = Class.forName(sb.toString());
		Method m = c.getDeclaredMethod("getMember", emptyClassArray);
		knownRecords[type] = (Record) m.invoke(null, emptyObjectArray);
	}
	catch (ClassNotFoundException e) {
		/* This is normal; do nothing */
	}
	catch (InvocationTargetException e) {
		if (Options.check("verbose"))
			System.err.println(e);
	}
	catch (NoSuchMethodException e) {
		if (Options.check("verbose"))
			System.err.println(e);
	}
	catch (IllegalAccessException e) {
		if (Options.check("verbose"))
			System.err.println(e);
	}
	if (knownRecords[type] == null)
		knownRecords[type] = UNKRecord.getMember();
	return knownRecords[type];
}

/**
 * Converts the type-specific RR to wire format - must be overriden
 */
abstract Record rrFromWire(Name name, int type, int dclass, long ttl,
			   int length, DataByteInputStream in)
throws IOException;

private static Record
newRecord(Name name, int type, int dclass, long ttl, int length,
	  DataByteInputStream in) throws IOException
{
	Record rec;
	int recstart;
	rec = getTypedObject(type);
	if (in == null)
		recstart = 0;
	else
		recstart = in.getPos();

	rec = rec.rrFromWire(name, type, dclass, ttl, length, in);
	if (in == null) {
		rec.empty = true;
	}
	if (in != null && in.getPos() - recstart != length)
		throw new IOException("Invalid record length");
	return rec;
}

/**
 * Creates a new record, with the given parameters.
 * @param name The owner name of the record.
 * @param type The record's type.
 * @param dclass The record's class.
 * @param ttl The record's time to live.
 * @param length The length of the record's data.
 * @param data The rdata of the record, in uncompressed DNS wire format.  Only
 * the first length bytes are used.
 */
public static Record
newRecord(Name name, int type, int dclass, long ttl, int length, byte [] data) {
	if (!name.isAbsolute())
		throw new RelativeNameException(name);
	Type.check(type);
	DClass.check(dclass);
	TTL.check(ttl);

	DataByteInputStream dbs;
	if (data != null)
		dbs = new DataByteInputStream(data);
	else
		dbs = null;
	try {
		return newRecord(name, type, dclass, ttl, length, dbs);
	}
	catch (IOException e) {
		return null;
	}
}

/**
 * Creates a new record, with the given parameters.
 * @param name The owner name of the record.
 * @param type The record's type.
 * @param dclass The record's class.
 * @param ttl The record's time to live.
 * @param data The complete rdata of the record, in uncompressed DNS wire
 * format.
 */
public static Record
newRecord(Name name, int type, int dclass, long ttl, byte [] data) {
	return newRecord(name, type, dclass, ttl, data.length, data);
}

/**
 * Creates a new empty record, with the given parameters.
 * @param name The owner name of the record.
 * @param type The record's type.
 * @param dclass The record's class.
 * @param ttl The record's time to live.
 * @return An object of a subclass of Record
 */
public static Record
newRecord(Name name, int type, int dclass, long ttl) {
	return newRecord(name, type, dclass, ttl, 0, (byte []) null);
}

/**
 * Creates a new empty record, with the given parameters.  This method is
 * designed to create records that will be added to the QUERY section
 * of a message.
 * @param name The owner name of the record.
 * @param type The record's type.
 * @param dclass The record's class.
 * @return An object of a subclass of Record
 */
public static Record
newRecord(Name name, int type, int dclass) {
	return newRecord(name, type, dclass, 0, 0, (byte []) null);
}

static Record
fromWire(DataByteInputStream in, int section) throws IOException {
	int type, dclass;
	long ttl;
	int length;
	Name name;
	Record rec;
	int start;

	start = in.getPos();

	name = new Name(in);
	type = in.readUnsignedShort();
	dclass = in.readUnsignedShort();

	if (section == Section.QUESTION)
		return newRecord(name, type, dclass);

	ttl = in.readUnsignedInt();
	length = in.readUnsignedShort();
	if (length == 0)
		return newRecord(name, type, dclass, ttl);
	rec = newRecord(name, type, dclass, ttl, length, in);
	return rec;
}

/**
 * Builds a Record from DNS uncompressed wire format.
 */
public static Record
fromWire(byte [] b, int section) throws IOException {
	DataByteInputStream in = new DataByteInputStream(b);
	return fromWire(in, section);
}

void
toWire(DataByteOutputStream out, int section, Compression c) {
	int start = out.getPos();
	name.toWire(out, c);
	out.writeShort(type);
	out.writeShort(dclass);
	if (section == Section.QUESTION)
		return;
	out.writeUnsignedInt(ttl);
	int lengthPosition = out.getPos();
	out.writeShort(0); /* until we know better */
	if (!empty)
		rrToWire(out, c, false);
	out.writeShortAt(out.getPos() - lengthPosition - 2, lengthPosition);
}

/**
 * Converts a Record into DNS uncompressed wire format.
 */
public byte []
toWire(int section) {
	DataByteOutputStream out = new DataByteOutputStream();
	toWire(out, section, null);
	return out.toByteArray();
}

private void
toWireCanonical(DataByteOutputStream out, boolean noTTL) {
	name.toWireCanonical(out);
	out.writeShort(type);
	out.writeShort(dclass);
	if (noTTL) {
		out.writeUnsignedInt(0);
	} else {
		out.writeUnsignedInt(ttl);
	}
	int lengthPosition = out.getPos();
	out.writeShort(0); /* until we know better */
	if (!empty)
		rrToWire(out, null, true);
	out.writeShortAt(out.getPos() - lengthPosition - 2, lengthPosition);
}

/*
 * Converts a Record into canonical DNS uncompressed wire format (all names are
 * converted to lowercase), optionally ignoring the TTL.
 */
private byte []
toWireCanonical(boolean noTTL) {
	DataByteOutputStream out = new DataByteOutputStream();
	toWireCanonical(out, noTTL);
	return out.toByteArray();
}

/**
 * Converts a Record into canonical DNS uncompressed wire format (all names are
 * converted to lowercase).
 */
public byte []
toWireCanonical() {
	return toWireCanonical(false);
}

/**
 * Converts the rdata in a Record into canonical DNS uncompressed wire format
 * (all names are converted to lowercase).
 */
public byte []
rdataToWireCanonical() {
	DataByteOutputStream out = new DataByteOutputStream();
	rrToWire(out, null, true);
	return out.toByteArray();
}

public abstract String rdataToString();

/**
 * Converts a Record into a String representation
 */
public String
toString() {
	StringBuffer sb = new StringBuffer();
	sb.append(name);
	if (sb.length() < 8)
		sb.append("\t");
	if (sb.length() < 16)
		sb.append("\t");
	sb.append("\t");
	if (Options.check("BINDTTL"))
		sb.append(TTL.format(ttl));
	else
		sb.append(ttl);
	sb.append("\t");
	if (dclass != DClass.IN || !Options.check("noPrintIN")) {
		sb.append(DClass.string(dclass));
		sb.append("\t");
	}
	sb.append(Type.string(type));
	if (!empty) {
		sb.append("\t");
		sb.append(rdataToString());
	}
	return sb.toString();
}

/**
 * Converts the text format of an RR to the internal format - must be overriden
 */
abstract Record
rdataFromString(Name name, int dclass, long ttl, Tokenizer st, Name origin)
throws IOException;

/**
 * Converts a String into a byte array.
 */
protected static byte []
byteArrayFromString(String s) throws TextParseException {
	byte [] array = s.getBytes();
	boolean escaped = false;
	boolean hasEscapes = false;

	for (int i = 0; i < array.length; i++) {
		if (array[i] == '\\') {
			hasEscapes = true;
			break;
		}
	}
	if (!hasEscapes)
		return array;

	ByteArrayOutputStream os = new ByteArrayOutputStream();

	int digits = 0;
	int intval = 0;
	for (int i = 0; i < array.length; i++) {
		byte b = array[i];
		if (escaped) {
			if (b >= '0' && b <= '9' && digits < 3) {
				digits++; 
				intval *= 10;
				intval += (b - '0');
				if (intval > 255)
					throw new TextParseException
								("bad escape");
				if (digits < 3)
					continue;
				b = (byte) intval;
			}
			else if (digits > 0 && digits < 3)
				throw new TextParseException("bad escape");
			os.write(b);
			escaped = false;
		}
		else if (array[i] == '\\') {
			escaped = true;
			digits = 0;
			intval = 0;
		}
		else
			os.write(array[i]);
	}
	if (digits > 0 && digits < 3)
		throw new TextParseException("bad escape");
	return os.toByteArray();
}

/**
 * Converts a byte array into a String.
 */
protected static String
byteArrayToString(byte [] array, boolean quote) {
	StringBuffer sb = new StringBuffer();
	if (quote)
		sb.append('"');
	for (int i = 0; i < array.length; i++) {
		short b = (short)(array[i] & 0xFF);
		if (b < 0x20 || b >= 0x7f) {
			sb.append('\\');
			sb.append(byteFormat.format(b));
		} else if (b == '"' || b == ';' || b == '\\') {
			sb.append('\\');
			sb.append((char)b);
		} else
			sb.append((char)b);
	}
	if (quote)
		sb.append('"');
	return sb.toString();
}

/**
 * Builds a new Record from its textual representation
 * @param name The owner name of the record.
 * @param type The record's type.
 * @param dclass The record's class.
 * @param ttl The record's time to live.
 * @param st A tokenizer containing the textual representation of the rdata.
 * @param origin The default origin to be appended to relative domain names.
 * @return The new record
 * @throws IOException The text format was invalid.
 */
public static Record
fromString(Name name, int type, int dclass, long ttl, Tokenizer st, Name origin)
throws IOException
{
	Record rec;

	if (!name.isAbsolute())
		throw new RelativeNameException(name);
	Type.check(type);
	DClass.check(dclass);
	TTL.check(ttl);

	Tokenizer.Token t = st.get();
	if (t.type == Tokenizer.IDENTIFIER && t.value.equals("\\#")) {
		int length = st.getUInt16();
		byte [] data = st.getHex();
		if (length != data.length)
			throw st.exception("invalid unknown RR encoding: " +
					   "length mismatch");
		DataByteInputStream in = new DataByteInputStream(data);
		return newRecord(name, type, dclass, ttl, length, in);
	}
	st.unget();
	rec = getTypedObject(type);
	rec = rec.rdataFromString(name, dclass, ttl, st, origin);
	t = st.get();
	if (t.type != Tokenizer.EOL && t.type != Tokenizer.EOF) {
		throw st.exception("unexpected tokens at end of record");
	}
	return rec;
}

/**
 * Builds a new Record from its textual representation
 * @param name The owner name of the record.
 * @param type The record's type.
 * @param dclass The record's class.
 * @param ttl The record's time to live.
 * @param s The textual representation of the rdata.
 * @param origin The default origin to be appended to relative domain names.
 * @return The new record
 * @throws IOException The text format was invalid.
 */
public static Record
fromString(Name name, int type, int dclass, long ttl, String s, Name origin)
throws IOException
{
	return fromString(name, type, dclass, ttl, new Tokenizer(s), origin);
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
 * Returns the record's type
 * @see Type
 */
public int
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
public int
getRRsetType() {
	if (type == Type.SIG || type == Type.RRSIG) {
		SIGRecord sig = (SIGBase) this;
		return sig.getTypeCovered();
	}
	return type;
}

/**
 * Returns the record's class
 */
public int
getDClass() {
	return dclass;
}

/**
 * Returns the record's TTL
 */
public long
getTTL() {
	return ttl;
}

/**
 * Converts the type-specific RR to wire format - must be overriden
 */
abstract void
rrToWire(DataByteOutputStream out, Compression c, boolean canonical);

/**
 * Determines if two Records are identical.  This compares the name, type,
 * class, and rdata (with names canonicalized).  The TTLs are not compared.
 * @param arg The record to compare to
 * @return true if the records are equal, false otherwise.
 */
public boolean
equals(Object arg) {
	if (arg == null || !(arg instanceof Record))
		return false;
	Record r = (Record) arg;
	if (type != r.type || dclass != r.dclass || !name.equals(r.name))
		return false;
	byte [] array1 = rdataToWireCanonical();
	byte [] array2 = r.rdataToWireCanonical();
	return Arrays.equals(array1, array2);
}

/**
 * Generates a hash code based on the Record's data.
 */
public int
hashCode() {
	byte [] array = toWireCanonical(true);
	int code = 0;
	for (int i = 0; i < array.length; i++)
		code += ((code << 3) + (array[i] & 0xFF));
	return code;
}

private Record
cloneRecord() {
	try {
		return (Record) clone();
	}
	catch (CloneNotSupportedException e) {
		throw new IllegalStateException();
	}
}

/**
 * Creates a new record identical to the current record, but with a different
 * name.  This is most useful for replacing the name of a wildcard record.
 */
public Record
withName(Name name) {
	if (!name.isAbsolute())
		throw new RelativeNameException(name);
	Record rec = cloneRecord();
	rec.name = name;
	return rec;
}

/**
 * Creates a new record identical to the current record, but with a different
 * class and ttl.  This is most useful for dynamic update.
 */
Record
withDClass(int dclass, long ttl) {
	Record rec = cloneRecord();
	rec.dclass = dclass;
	rec.ttl = ttl;
	return rec;
}

/**
 * Compares this Record to another Object.
 * @param o The Object to be compared.
 * @return The value 0 if the argument is a record equivalent to this record;
 * a value less than 0 if the argument is less than this record in the
 * canonical ordering, and a value greater than 0 if the argument is greater
 * than this record in the canonical ordering.  The canonical ordering
 * is defined to compare by name, class, type, and rdata.
 * @throws ClassCastException if the argument is not a Record.
 */
public int
compareTo(Object o) {
	Record arg = (Record) o;

	if (this == arg)
		return (0);

	int n = name.compareTo(arg.name);
	if (n != 0)
		return (n);
	n = dclass - arg.dclass;
	if (n != 0)
		return (n);
	n = type - arg.type;
	if (n != 0)
		return (n);
	byte [] rdata1 = rdataToWireCanonical();
	byte [] rdata2 = arg.rdataToWireCanonical();
	for (int i = 0; i < rdata1.length && i < rdata2.length; i++) {
		n = (rdata1[i] & 0xFF) - (rdata2[i] & 0xFF);
		if (n != 0)
			return (n);
	}
	return (rdata1.length - rdata2.length);
}

/**
 * Returns the name for which additional data processing should be done
 * for this record.  This can be used both for building responses and
 * parsing responses.
 * @return The name to used for additional data processing, or null if this
 * record type does not require additional data processing.
 */
public Name
getAdditionalName() {
	return null;
}

/* Checks that an int contains an unsigned 8 bit value */
static void
checkU8(String field, int val) {
	if (val < 0 || val > 0xFF)
		throw new IllegalArgumentException("\"" + field + "\" " + val + 
						   "must be an unsigned 8 " +
						   "bit value");
}

/* Checks that an int contains an unsigned 16 bit value */
static void
checkU16(String field, int val) {
	if (val < 0 || val > 0xFFFF)
		throw new IllegalArgumentException("\"" + field + "\" " + val + 
						   "must be an unsigned 16 " +
						   "bit value");
}

/* Checks that a long contains an unsigned 32 bit value */
static void
checkU32(String field, long val) {
	if (val < 0 || val > 0xFFFFFFFFL)
		throw new IllegalArgumentException("\"" + field + "\" " + val + 
						   "must be an unsigned 32 " +
						   "bit value");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4245.java