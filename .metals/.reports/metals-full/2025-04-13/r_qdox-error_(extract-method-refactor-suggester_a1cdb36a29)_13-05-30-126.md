error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3440.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3440.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3440.java
text:
```scala
t@@hrow new RuntimeException("readDouble");

/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package java.io;

import com.google.gwt.corp.compatibility.Numbers;

public class DataInputStream extends InputStream implements DataInput {

	private final InputStream is;

	public DataInputStream (final InputStream is) {
		this.is = is;
	}

	@Override
	public int read () throws IOException {
		return is.read();
	}

	public boolean readBoolean () throws IOException {
		return readByte() != 0;
	}

	public byte readByte () throws IOException {
		int i = read();
		if (i == -1) {
			throw new EOFException();
		}
		return (byte)i;
	}

	public char readChar () throws IOException {
		int a = is.read();
		int b = readUnsignedByte();
		return (char)((a << 8) | b);
	}

	public double readDouble () throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	public float readFloat () throws IOException {
		return Numbers.intBitsToFloat(readInt());
	}

	public void readFully (byte[] b) throws IOException {
		readFully(b, 0, b.length);
	}

	public void readFully (byte[] b, int off, int len) throws IOException {
		while (len > 0) {
			int count = is.read(b, off, len);
			if (count <= 0) {
				throw new EOFException();
			}
			off += count;
			len -= count;
		}
	}

	public int readInt () throws IOException {
		int a = is.read();
		int b = is.read();
		int c = is.read();
		int d = readUnsignedByte();
		return (a << 24) | (b << 16) | (c << 8) | d;
	}

	public String readLine () throws IOException {
		throw new RuntimeException("readline NYI");
	}

	public long readLong () throws IOException {
		long a = readInt();
		long b = readInt() & 0x0ffffffff;
		return (a << 32) | b;
	}

	public short readShort () throws IOException {
		int a = is.read();
		int b = readUnsignedByte();
		return (short)((a << 8) | b);
	}

	public String readUTF () throws IOException {
		int bytes = readUnsignedShort();
		StringBuilder sb = new StringBuilder();

		while (bytes > 0) {
			bytes -= readUtfChar(sb);
		}

		return sb.toString();
	}

	private int readUtfChar (StringBuilder sb) throws IOException {
		int a = readUnsignedByte();
		if ((a & 0x80) == 0) {
			sb.append((char)a);
			return 1;
		}
		if ((a & 0xe0) == 0xb0) {
			int b = readUnsignedByte();
			sb.append((char)(((a & 0x1F) << 6) | (b & 0x3F)));
			return 2;
		}
		if ((a & 0xf0) == 0xe0) {
			int b = is.read();
			int c = readUnsignedByte();
			sb.append((char)(((a & 0x0F) << 12) | ((b & 0x3F) << 6) | (c & 0x3F)));
			return 3;
		}
		throw new UTFDataFormatException();
	}

	public int readUnsignedByte () throws IOException {
		int i = read();
		if (i == -1) {
			throw new EOFException();
		}
		return i;
	}

	public int readUnsignedShort () throws IOException {
		int a = is.read();
		int b = readUnsignedByte();
		return ((a << 8) | b);
	}

	public int skipBytes (int n) throws IOException {
		// note: This is actually a valid implementation of this method, rendering it quite useless...
		return 0;
	}

	@Override
	public int available () {
		return is.available();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3440.java