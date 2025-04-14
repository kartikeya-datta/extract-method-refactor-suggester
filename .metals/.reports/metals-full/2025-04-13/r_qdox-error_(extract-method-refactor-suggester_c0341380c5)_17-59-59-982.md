error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1426.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1426.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1426.java
text:
```scala
t@@his.current = start;

// Copyright (c) 2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.io.*;
import java.util.*;

/**
 * A representation of a $GENERATE statement in a master file.
 *
 * @author Brian Wellington
 */

public class Generator {

/** The start of the range. */
public long start;

/** The end of the range. */
public long end;

/** The step value of the range. */
public long step;

/** The pattern to use for generating record names. */
public final String namePattern;

/** The type of the generated records. */
public final int type;

/** The class of the generated records. */
public final int dclass;

/** The ttl of the generated records. */
public final long ttl;

/** The pattern to use for generating record data. */
public final String rdataPattern;

/** The origin to append to relative names. */
public final Name origin;

private long current;

/**
 * Indicates whether generation is supported for this type.
 * @throws InvalidTypeException The type is out of range.
 */
public static boolean
supportedType(int type) {
	Type.check(type);
	return (type == Type.PTR || type == Type.CNAME || type == Type.DNAME ||
		type == Type.A || type == Type.AAAA || type == Type.NS);
}

/**
 * Creates a specification for generating records, as a $GENERATE
 * statement in a master file.
 * @param start The start of the range.
 * @param end The end of the range.
 * @param step The step value of the range.
 * @param namePattern The pattern to use for generating record names.
 * @param type The type of the generated records.  The supported types are
 * PTR, CNAME, DNAME, A, AAAA, and NS.
 * @param dclass The class of the generated records.
 * @param ttl The ttl of the generated records.
 * @param rdataPattern The pattern to use for generating record data.
 * @param origin The origin to append to relative names.
 * @throws IllegalArgumentException The range is invalid.
 * @throws IllegalArgumentException The type does not support generation.
 * @throws IllegalArgumentException The dclass is not a valid class.
 */
public
Generator(long start, long end, long step, String namePattern,
	  int type, int dclass, long ttl, String rdataPattern, Name origin)
{
	if (start < 0 || end < 0 || start > end || step <= 0)
		throw new IllegalArgumentException
				("invalid range specification");
	if (!supportedType(type))
		throw new IllegalArgumentException("unsupported type");
	DClass.check(dclass);

	this.start = start;
	this.end = end;
	this.step = step;
	this.namePattern = namePattern;
	this.type = type;
	this.dclass = dclass;
	this.ttl = ttl;
	this.rdataPattern = rdataPattern;
	this.origin = origin;
	this.current = 0;
}

private String
substitute(String spec, long n) throws IOException {
	boolean escaped = false;
	byte [] str = spec.getBytes();
	StringBuffer sb = new StringBuffer();

	for (int i = 0; i < str.length; i++) {
		char c = (char)(str[i] & 0xFF);
		if (escaped) {
			sb.append(c);
			escaped = false;
		} else if (c == '\\') {
			if (i + 1 == str.length)
				throw new TextParseException
						("invalid escape character");
			escaped = true;
		} else if (c == '$') {
			boolean negative = false;
			long offset = 0;
			long width = 0;
			long base = 10;
			boolean wantUpperCase = false;
			if (i + 1 < str.length && str[i + 1] == '$') {
				// '$$' == literal '$' for backwards
				// compatibility with old versions of BIND.
				c = (char)(str[++i] & 0xFF);
				sb.append(c);
				continue;
			} else if (i + 1 < str.length && str[i + 1] == '{') {
				// It's a substitution with modifiers.
				i++;
				if (i + 1 < str.length && str[i + 1] == '-') {
					negative = true;
					i++;
				}
				while (i + 1 < str.length) {
					c = (char)(str[++i] & 0xFF);
					if (c == ',' || c == '}')
						break;
					if (c < '0' || c > '9')
						throw new TextParseException(
							"invalid offset");
					c -= '0';
					offset *= 10;
					offset += c;
				}
				if (negative)
					offset = -offset;

				if (c == ',') {
					while (i + 1 < str.length) {
						c = (char)(str[++i] & 0xFF);
						if (c == ',' || c == '}')
							break;
						if (c < '0' || c > '9')
							throw new
							   TextParseException(
							   "invalid width");
						c -= '0';
						width *= 10;
						width += c;
					}
				}

				if (c == ',') {
					if  (i + 1 == str.length)
						throw new TextParseException(
							   "invalid base");
					c = (char)(str[++i] & 0xFF);
					if (c == 'o')
						base = 8;
					else if (c == 'x')
						base = 16;
					else if (c == 'X') {
						base = 16;
						wantUpperCase = true;
					}
					else if (c != 'd')
						throw new TextParseException(
							   "invalid base");
				}

				if (i + 1 == str.length || str[i + 1] != '}')
					throw new TextParseException
						("invalid modifiers");
				i++;
			}
			long v = n + offset;
			if (v < 0)
				throw new TextParseException
						("invalid offset expansion");
			String number;
			if (base == 8)
				number = Long.toOctalString(v);
			else if (base == 16)
				number = Long.toHexString(v);
			else
				number = Long.toString(v);
			if (wantUpperCase)
				number = number.toUpperCase();
			if (width != 0 && width > number.length()) {
				int zeros = (int)width - number.length();
				while (zeros-- > 0)
					sb.append('0');
			}
			sb.append(number);
		} else {
			sb.append(c);
		}
	}
	return sb.toString();
}

/**
 * Constructs and returns the next record in the expansion.
 * @throws IOException The name or rdata was invalid after substitutions were
 * performed.
 */
public Record
nextRecord() throws IOException {
	if (current > end)
		return null;
	String namestr = substitute(namePattern, current);
	Name name = Name.fromString(namestr, origin);
	String rdata = substitute(rdataPattern, current);
	current += step;
	return Record.fromString(name, type, dclass, ttl, rdata, origin);
}

/**
 * Constructs and returns all records in the expansion.
 * @throws IOException The name or rdata of a record was invalid after
 * substitutions were performed.
 */
public Record []
expand() throws IOException {
	List list = new ArrayList();
	for (long i = start; i < end; i += step) {
		String namestr = substitute(namePattern, current);
		Name name = Name.fromString(namestr, origin);
		String rdata = substitute(rdataPattern, current);
		list.add(Record.fromString(name, type, dclass, ttl,
					   rdata, origin));
	}
	return (Record []) list.toArray(new Record[list.size()]);
}

/**
 * Converts the generate specification to a string containing the corresponding
 * $GENERATE statement.
 */
public String
toString() {
	StringBuffer sb = new StringBuffer();
	sb.append("$GENERATE ");
	sb.append(start + "-" + end);
	if (step > 1)
		sb.append("/" + step);
	sb.append(" ");
	sb.append(namePattern + " ");
	sb.append(ttl + " ");
	if (dclass != DClass.IN || !Options.check("noPrintIN"))
		sb.append(DClass.string(dclass) + " ");
	sb.append(Type.string(type) + " ");
	sb.append(rdataPattern + " ");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1426.java