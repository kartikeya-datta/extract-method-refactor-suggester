error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1955.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1955.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1955.java
text:
```scala
i@@f (token.charAt(0) == '[')

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.io.*;
import java.util.*;
import org.xbill.DNS.utils.*;

/**
 * A representation of a domain name. 
 *
 * @author Brian Wellington
 */

public class Name {

private static final int LABEL_NORMAL = 0;
private static final int LABEL_COMPRESSION = 0xC0;
private static final int LABEL_EXTENDED = 0x40;
private static final int LABEL_LOCAL_COMPRESSION = 0x80;
private static final int LABEL_MASK = 0xC0;

private static final int EXT_LABEL_COMPRESSION = 0;
private static final int EXT_LABEL_BITSTRING = 1;
private static final int EXT_LABEL_LOCAL_COMPRESSION = 2;

private Object [] name;
private byte labels;
private boolean qualified;

/** The root name */
public static Name root = new Name("");

/** The maximum number of labels in a Name */
static final int MAXLABELS = 256;

/**
 * Create a new name from a string and an origin
 * @param s  The string to be converted
 * @param origin  If the name is unqalified, the origin to be appended
 */
public
Name(String s, Name origin) {
	labels = 0;
	name = new Object[MAXLABELS];

	if (s.equals("@") && origin != null) {
		append(origin);
		qualified = true;
		return;
	}
	try {
		MyStringTokenizer st = new MyStringTokenizer(s, ".");

		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.charAt(0) == '\\')
				name[labels++] = new BitString(token);
			else
				name[labels++] = token;
		}

		if (st.hasMoreDelimiters())
			qualified = true;
		else {
			if (origin != null) {
				append(origin);
				qualified = true;
			}
			else {
				/* This isn't exactly right, but it's close.
				 * Partially qualified names are evil.
				 */
				if (Options.check("pqdn"))
					qualified = false;
				else
					qualified = (labels > 1);
			}
		}
	}
	catch (Exception e) {
		StringBuffer sb = new StringBuffer();
		sb.append(s);
		if (origin != null) {
			sb.append(".");
			sb.append(origin);
		}
		if (e instanceof ArrayIndexOutOfBoundsException)
			sb.append(" has too many labels");
		else if (e instanceof IOException)
			sb.append(" contains an invalid binary label");
		else
			sb.append(" is invalid");
		System.err.println(sb.toString());
		name = null;
		labels = 0;
	}
}

/**
 * Create a new name from a string
 * @param s  The string to be converted
 */
public
Name(String s) {
	this (s, null);
}

/**
 * Create a new name from DNS wire format
 * @param in  A stream containing the input data
 * @param c  The compression context.  This should be null unless a full
 * message is being parsed.
 */
public
Name(DataByteInputStream in, Compression c) throws IOException {
	int len, start, pos, count = 0;
	Name name2;

	labels = 0;
	name = new Object[MAXLABELS];

	start = in.getPos();
loop:
	while ((len = in.readUnsignedByte()) != 0) {
		switch(len & LABEL_MASK) {
		case LABEL_NORMAL:
			byte [] b = new byte[len];
			in.read(b);
			name[labels++] = new String(b);
			count++;
			break;
		case LABEL_COMPRESSION:
			pos = in.readUnsignedByte();
			pos += ((len & ~LABEL_MASK) << 8);
			name2 = (c == null) ? null : c.get(pos);
			if (Options.check("verbosecompression"))
				System.err.println("Looking at " + pos +
						   ", found " + name2);
			if (name2 == null)
				throw new WireParseException("bad compression");
			else {
				System.arraycopy(name2.name, 0, name, labels,
						 name2.labels);
				labels += name2.labels;
			}
			break loop;
		case LABEL_EXTENDED:
			int type = len & ~LABEL_MASK;
			switch (type) {
			case EXT_LABEL_COMPRESSION:
				pos = in.readUnsignedShort();
				name2 = (c == null) ? null : c.get(pos);
				if (Options.check("verbosecompression"))
					System.err.println("Looking at " +
							   pos + ", found " +
							   name2);
				if (name2 == null)
					throw new WireParseException(
							"bad compression");
				else {
					System.arraycopy(name2.name, 0, name,
							 labels, name2.labels);
					labels += name2.labels;
				}
				break loop;
			case EXT_LABEL_BITSTRING:
				int bits = in.readUnsignedByte();
				if (bits == 0)
					bits = 256;
				int bytes = (bits + 7) / 8;
				byte [] data = new byte[bytes];
				in.read(data);
				name[labels++] = new BitString(bits, data);
				count++;
				break;
			case EXT_LABEL_LOCAL_COMPRESSION:
				throw new WireParseException(
						"Long local compression");
			default:
				throw new WireParseException(
						"Unknown name format");
			} /* switch */
			break;
		case LABEL_LOCAL_COMPRESSION:
			throw new WireParseException("Local compression");
		} /* switch */
	}
	if (c != null) {
		pos = start;
		for (int i = 0; i < count; i++) {
			Name tname = new Name(this, i);
			c.add(pos, tname);
			if (Options.check("verbosecompression"))
				System.err.println("Adding " + tname +
						   " at " + pos);
			if (name[i] instanceof String)
				pos += (((String)name[i]).length() + 1);
			else
				pos += (((BitString)name[i]).bytes() + 2);
		}
	}
	qualified = true;
}

/**
 * Create a new name by removing labels from the beginning of an existing Name
 * @param d  An existing Name
 * @param n  The number of labels to remove from the beginning in the copy
 */
/* Skips n labels and creates a new name */
public
Name(Name d, int n) {
	name = new Object[MAXLABELS];

	labels = (byte) (d.labels - n);
	System.arraycopy(d.name, n, name, 0, labels);
	qualified = d.qualified;
}

/**
 * Generates a new Name with the first label replaced by a wildcard 
 * @return The wildcard name
 */
public Name
wild() {
	Name wild = new Name(this, 0);
	wild.name[0] = "*";
	return wild;
}

/**
 * Is this name a wildcard?
 */
public boolean
isWild() {
	return name[0].equals("*");
}

/**
 * Is this name fully qualified?
 */
public boolean
isQualified() {
	return qualified;
}

/**
 * Appends the specified name to the end of the current Name
 */
public void
append(Name d) {
	System.arraycopy(d.name, 0, name, labels, d.labels);
	labels += d.labels;
}

/**
 * The length
 */
public short
length() {
	short total = 0;
	for (int i = 0; i < labels; i++) {
		if (name[i] instanceof String)
			total += (((String)name[i]).length() + 1);
		else
			total += (((BitString)name[i]).bytes() + 2);
	}
	return ++total;
}

/**
 * The number of labels
 */
public byte
labels() {
	return labels;
}

/**
 * Is the current Name a subdomain of the specified name?
 */
public boolean
subdomain(Name domain) {
	if (domain == null || domain.labels > labels)
		return false;
	int i = labels, j = domain.labels;
	while (j > 0)
		if (!name[--i].equals(domain.name[--j]))
			return false;
	return true;
}

/**
 * Convert Name to a String
 */
public String
toString() {
	StringBuffer sb = new StringBuffer();
	if (labels == 0)
		sb.append(".");
	for (int i = 0; i < labels; i++) {
		sb.append(name[i]);
		if (qualified || i < labels - 1)
			sb.append(".");
	}
	return sb.toString();
}

/**
 * Convert Name to DNS wire format
 */
public void
toWire(DataByteOutputStream out, Compression c) throws IOException {
	for (int i = 0; i < labels; i++) {
		Name tname = new Name(this, i);
		int pos = -1;
		if (c != null) {
			pos = c.get(tname);
			if (Options.check("verbosecompression"))
				System.err.println("Looking for " + tname +
						   ", found " + pos);
		}
		if (pos >= 0) {
			pos |= (LABEL_MASK << 8);
			out.writeShort(pos);
			return;
		}
		else {
			if (c != null) {
				c.add(out.getPos(), tname);
				if (Options.check("verbosecompression"))
					System.err.println("Adding " + tname +
							   " at " +
							   out.getPos());
			}
			if (name[i] instanceof String)
				out.writeString((String)name[i]);
			else {
				out.writeByte(LABEL_EXTENDED |
					      EXT_LABEL_BITSTRING);
				out.writeByte(((BitString)name[i]).wireBits());
				out.write(((BitString)name[i]).data);
			}
		}
	}
	out.writeByte(0);
}

/**
 * Convert Name to canonical DNS wire format (all lowercase)
 */
public void
toWireCanonical(DataByteOutputStream out) throws IOException {
	for (int i = 0; i < labels; i++) {
		if (name[i] instanceof String)
			out.writeStringCanonical((String)name[i]);
		else {
			out.writeByte(LABEL_EXTENDED | EXT_LABEL_BITSTRING);
			out.writeByte(((BitString)name[i]).wireBits());
			out.write(((BitString)name[i]).data);
		}
	}
	out.writeByte(0);
}

/**
 * Are these two Names equivalent?
 */
public boolean
equals(Object arg) {
	if (arg == null || !(arg instanceof Name))
		return false;
	Name d = (Name) arg;
	if (d.labels != labels)
		return false;
	for (int i = 0; i < labels; i++) {
		if (name[i].getClass() != d.name[i].getClass())
			return false;
		if (name[i] instanceof String) {
			String s1 = (String) name[i];
			String s2 = (String) d.name[i];
			if (!s1.equalsIgnoreCase(s2))
				return false;
		}
		else {
			/* BitString */
			if (!name[i].equals(d.name[i]))
				return false;
		}
	}
	return true;
}

/**
 * Computes a hashcode based on the value
 */
public int
hashCode() {
	int code = labels;
	for (int i = 0; i < labels; i++) {
		if (name[i] instanceof String) {
			String s = (String) name[i];
			for (int j = 0; j < s.length(); j++)
				code += Character.toLowerCase(s.charAt(j));
		}
		else {
			BitString b = (BitString) name[i];
			for (int j = 0; j < b.bytes(); j++)
				code += b.data[i];
		}
	}
	return code;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1955.java