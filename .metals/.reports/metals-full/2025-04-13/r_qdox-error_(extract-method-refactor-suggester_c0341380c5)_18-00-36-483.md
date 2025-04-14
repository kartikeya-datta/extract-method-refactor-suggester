error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5270.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5270.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5270.java
text:
```scala
c@@ode += Character.toLowerCase(name[i].charAt(j));

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package DNS;

import java.io.*;
import java.util.*;
import DNS.utils.*;

public class Name {

private String [] name;
private byte labels;

public static Name root = new Name("");

static final int MAXLABELS = 64;

public
Name(String s, Name origin) {
	labels = 0;
	name = new String[MAXLABELS];

	if (s.equals("@") && origin != null) {
		append(origin);
		return;
	}
	try {
		MyStringTokenizer st = new MyStringTokenizer(s, ".");

		while (st.hasMoreTokens())
			name[labels++] = st.nextToken();

		if (!st.hasMoreDelimiters() && origin != null)
			append(origin);
	}
	catch (ArrayIndexOutOfBoundsException e) {
		StringBuffer sb = new StringBuffer();
		sb.append("String ");
		sb.append(s);
		if (origin != null) {
			sb.append(".");
			sb.append(origin);
		}
		sb.append(" has too many labels");
		System.out.println(sb.toString());
		name = null;
		labels = 0;
	}
	
}

public
Name(String s) {
	this (s, null);
}

Name(DataByteInputStream in, Compression c) throws IOException {
	int len, start, count = 0;

	labels = 0;
	name = new String[MAXLABELS];

	start = in.getPos();
	while ((len = in.readUnsignedByte()) != 0) {
		if ((len & 0xC0) != 0) {
			int pos = in.readUnsignedByte();
			pos += ((len & ~0xC0) << 8);
			Name name2 = (c == null) ? null : c.get(pos);
/*System.out.println("Looking for name at " + pos + ", found " + name2);*/
			if (name2 == null)
				name[labels++] = new String("<compressed>");
			else {
				System.arraycopy(name2.name, 0, name, labels,
						 name2.labels);
				labels += name2.labels;
			}
			break;
		}
		byte [] b = new byte[len];
		in.read(b);
		name[labels++] = new String(b);
		count++;
	}
	if (c != null) 
		for (int i = 0, pos = start; i < count; i++) {
			Name tname = new Name(this, i);
			c.add(pos, tname);
/*System.out.println("(D) Adding " + tname + " at " + pos);*/
			pos += (name[i].length() + 1);
		}
}

/* Skips n labels and creates a new name */
public
Name(Name d, int n) {
	name = new String[MAXLABELS];

	labels = (byte) (d.labels - n);
	System.arraycopy(d.name, n, name, 0, labels);
}

public void
append(Name d) {
	System.arraycopy(d.name, 0, name, labels, d.labels);
	labels += d.labels;
}

public short
length() {
	short total = 0;
	for (int i = 0; i < labels; i++)
		total += (name[i].length() + 1);
	return ++total;
}

public byte
labels() {
	return labels;
}

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

public String
toString() {
	StringBuffer sb = new StringBuffer();
	if (labels == 0)
		sb.append(".");
	for (int i=0; i<labels; i++)
		sb.append(name[i] + ".");
	return sb.toString();
}

public void
toWire(DataByteOutputStream out, Compression c) throws IOException {
	for (int i=0; i<labels; i++) {
		Name tname = new Name(this, i);
		int pos;
		if (c != null)
			pos = c.get(tname);
		else
			pos = -1;
/*System.out.println("Looking for compressed " + tname + ", found " + pos);*/
		if (pos >= 0) {
			pos |= (0xC0 << 8);
			out.writeShort(pos);
			return;
		}
		else {
			if (c != null)
				c.add(out.getPos(), tname);
/*System.out.println("(C) Adding " + tname + " at " + out.getPos());*/
			out.writeString(name[i]);
		}
	}
	out.writeByte(0);
}

public void
toWireCanonical(DataByteOutputStream out) throws IOException {
	for (int i=0; i<labels; i++) {
		out.writeByte(name[i].length());
		for (int j=0; j<name[i].length(); j++)
			out.writeByte(Character.toLowerCase(name[i].charAt(j)));
	}
	out.writeByte(0);
}

public boolean
equals(Object arg) {
	if (arg == null || !(arg instanceof Name))
		return false;
	Name d = (Name) arg;
	if (d.labels != labels)
		return false;
	for (int i = 0; i < labels; i++) {
		if (!d.name[i].equalsIgnoreCase(name[i]))
			return false;
	}
	return true;
}

public int
hashCode() {
	int code = labels;
	for (int i = 0; i < labels; i++) {
		for (int j = 0; j < name[i].length(); j++)
			code += name[i].charAt(j);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5270.java