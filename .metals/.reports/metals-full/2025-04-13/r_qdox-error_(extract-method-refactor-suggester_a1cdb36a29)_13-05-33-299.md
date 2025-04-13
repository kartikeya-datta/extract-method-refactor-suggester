error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/448.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/448.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/448.java
text:
```scala
t@@hrow new RuntimeException("writeDouble");

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

public class DataOutputStream extends OutputStream implements DataOutput {

	OutputStream os;

	public DataOutputStream (OutputStream os) {
		this.os = os;
	}

	@Override
	public void write (int b) throws IOException {
		os.write(b);
	}

	public void writeBoolean (boolean v) throws IOException {
		os.write(v ? 1 : 0);
	}

	public void writeByte (int v) throws IOException {
		os.write(v);
	}

	public void writeBytes (String s) throws IOException {
		int len = s.length();
		for (int i = 0; i < len; i++) {
			os.write(s.charAt(i) & 0xff);
		}
	}

	public void writeChar (int v) throws IOException {
		os.write(v >> 8);
		os.write(v);
	}

	public void writeChars (String s) throws IOException {
		throw new RuntimeException("writeChars NYI");
	}

	public void writeDouble (double v) throws IOException {
		writeLong(Double.doubleToLongBits(v));
	}

	public void writeFloat (float v) throws IOException {
		writeInt(Numbers.floatToIntBits(v));
	}

	public void writeInt (int v) throws IOException {
		os.write(v >> 24);
		os.write(v >> 16);
		os.write(v >> 8);
		os.write(v);
	}

	public void writeLong (long v) throws IOException {
		writeInt((int)(v >> 32L));
		writeInt((int)v);
	}

	public void writeShort (int v) throws IOException {
		os.write(v >> 8);
		os.write(v);
	}

	public void writeUTF (String s) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c > 0 && c < 80) {
				baos.write(c);
			} else if (c < '\u0800') {
				baos.write(0xc0 | (0x1f & (c >> 6)));
				baos.write(0x80 | (0x3f & c));
			} else {
				baos.write(0xe0 | (0x0f & (c >> 12)));
				baos.write(0x80 | (0x3f & (c >> 6)));
				baos.write(0x80 | (0x3f & c));
			}
		}
		writeShort(baos.count);
		os.write(baos.buf, 0, baos.count);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/448.java