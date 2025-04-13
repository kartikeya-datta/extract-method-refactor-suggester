error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7461.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7461.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7461.java
text:
```scala
r@@eturn TTL.parseTTL(st.nextToken());

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.util.*;
import java.io.*;
import org.xbill.DNS.utils.*;

/**
 * A DNS master file parser.  This incrementally parses the file, returning
 * one record at a time.  When directives are seen, they are added to the
 * state and used when parsing future records.
 *
 * @author Brian Wellington
 */

public class Master {

private Name origin;
private BufferedReader br;
private File file;
private Record last = null;
private int defaultTTL = 3600;
private Master included = null;

Master(File file, Name defaultOrigin) throws IOException {
	FileInputStream fis;
	this.file = file;
	try {
		fis = new FileInputStream(file);
	}
	catch (FileNotFoundException e) {
		throw new IOException(e.toString());
	}
	br = new BufferedReader(new InputStreamReader(fis));
	origin = defaultOrigin;
}

/** Begins parsing the specified file with an initial origin */
public
Master(String filename, Name origin) throws IOException {
	this(new File(filename), origin);
}

/** Begins parsing the specified file */
public
Master(String filename) throws IOException {
	this(new File(filename), null);
}

/** Begins parsing from an input reader with an initial origin */
public
Master(BufferedReader in, Name defaultOrigin) {
	br = in;
	origin = defaultOrigin;
}

/** Begins parsing from an input reader */
public
Master(BufferedReader in) {
	this(in, null);
}

/** Returns the next record in the master file */
public Record
nextRecord() throws IOException {
	String line;
	MyStringTokenizer st;

	if (included != null) {
		Record rec = included.nextRecord();
		if (rec != null)
			return rec;
		included = null;
	}
	while (true) {
		line = readExtendedLine(br);
		if (line == null)
			return null;
		if (line.length() == 0 || line.startsWith(";"))
			continue;

		boolean space = line.startsWith(" ") || line.startsWith("\t");
		st = new MyStringTokenizer(line);

		String s = st.nextToken();
		if (s.equals("$ORIGIN")) {
			origin = parseOrigin(st);
			continue;
		}
		if (s.equals("$TTL")) {
			defaultTTL = parseTTL(st);
			continue;
		}
		if (s.equals("$INCLUDE")) {
			parseInclude(st);
			/*
			 * If we continued, we wouldn't be looking in
			 * the new file.  Recursing works better.
			 */
			return nextRecord();
		}
		else if (s.charAt(0) == '$')
			throw new IOException("Invalid directive: " + s);
		st.putBackToken(s);
		return (last = parseRR(st, space, last, origin));
	}
}

private Name
parseOrigin(MyStringTokenizer st) throws IOException {
	if (!st.hasMoreTokens())
		throw new IOException ("Missing ORIGIN");
	return Name.fromString(st.nextToken(), Name.root);
}

private int
parseTTL(MyStringTokenizer st) throws IOException {
	if (!st.hasMoreTokens())
		throw new IOException ("Missing TTL");
	return Integer.parseInt(st.nextToken());
}

private void
parseInclude(MyStringTokenizer st) throws IOException {
	if (!st.hasMoreTokens())
		throw new IOException ("Missing file to include");
	String filename = st.nextToken();
	String parent = file.getParent();
	File newfile = new File(file.getParent(), filename);
	Name incorigin = origin;
	if (st.hasMoreTokens())
		incorigin = Name.fromString(st.nextToken(), Name.root);
	included = new Master(newfile, incorigin);
}

private Record
parseRR(MyStringTokenizer st, boolean useLast, Record last, Name origin)
throws IOException
{
	Name name;
	int ttl;
	short type, dclass;

	if (!useLast)
		name = Name.fromString(st.nextToken(), origin);
	else
		name = last.getName();

	String s = st.nextToken();

	try {
		ttl = TTL.parseTTL(s);
		s = st.nextToken();
	}
	catch (NumberFormatException e) {
		if (!useLast || last == null)
			ttl = defaultTTL;
		else
			ttl = last.getTTL();
	}

	if ((dclass = DClass.value(s)) > 0)
		s = st.nextToken();
	else
		dclass = DClass.IN;
		

	if ((type = Type.value(s)) < 0)
		throw new IOException("Parse error: invalid type '" + s + "'");

	return Record.fromString(name, type, dclass, ttl, st, origin);
}

private static String
stripTrailing(String s) {
	if (s == null)
		return null;
	int lastChar;
	int semi;
	if ((semi = s.lastIndexOf(';')) < 0)
		lastChar = s.length() - 1;
	else
		lastChar = semi - 1;
	for (int i = lastChar; i >= 0; i--) {
		if (!Character.isWhitespace(s.charAt(i)))
			return s.substring(0, i+1);
	}
	return "";
}

/**
 * Reads a line using the master file format.  Removes all data following
 * a semicolon and uses parentheses as line continuation markers.
 * @param br The BufferedReader supplying the data
 * @return A String representing the normalized line
 */
public static String
readExtendedLine(BufferedReader br) throws IOException {
	String s = stripTrailing(br.readLine());
	if (s == null)
		return null;
	if (!s.endsWith("("))
		return s;
	StringBuffer sb = new StringBuffer(s.substring(0, s.length() - 1));
	while (true) {
		s = stripTrailing(br.readLine().trim());
		if (s == null)
			return sb.toString();
		sb.append(" ");
		if (s.endsWith(")")) {
			sb.append(s.substring(0, s.length() - 1));
			break;
		}
		sb.append(s);
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7461.java