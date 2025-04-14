error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9927.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9927.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[18,1]

error in qdox parser
file content:
```java
offset: 793
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9927.java
text:
```scala
public static final class Usage {

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
p@@ackage com.badlogic.gdx.graphics;

/**
 * Instances of this class specify the vertex attributes of a mesh. VertexAttributes are used by {@link Mesh} instances to define
 * its vertex structure. Vertex attributes have an order. The order is specified by the order they are added to this class.
 * 
 * @author mzechner
 * 
 */
public final class VertexAttributes {
	/**
	 * The usage of a vertex attribute.
	 * 
	 * @author mzechner
	 * 
	 */
	public final class Usage {
		public static final int Position = 0;
		public static final int Color = 1;
		public static final int ColorPacked = 5;
		public static final int Normal = 2;
		public static final int TextureCoordinates = 3;
		public static final int Generic = 4;
	}

	/** the attributes in the order they were specified **/
	private final VertexAttribute[] attributes;

	/** the size of a single vertex in bytes **/
	public final int vertexSize;

	/**
	 * Constructor, sets the vertex attributes in a specific order
	 */
	public VertexAttributes (VertexAttribute... attributes) {
		if (attributes.length == 0) throw new IllegalArgumentException("attributes must be >= 1");

		VertexAttribute[] list = new VertexAttribute[attributes.length];
		for (int i = 0; i < attributes.length; i++)
			list[i] = attributes[i];

		this.attributes = list;

		checkValidity();
		vertexSize = calculateOffsets();
	}

	private int calculateOffsets () {
		int count = 0;
		for (int i = 0; i < attributes.length; i++) {
			VertexAttribute attribute = attributes[i];
			attribute.offset = count;
			if (attribute.usage == VertexAttributes.Usage.ColorPacked)
				count += 4;
			else
				count += 4 * attribute.numComponents;
		}

		return count;
	}

	private void checkValidity () {
		boolean pos = false;
		boolean cols = false;
		boolean nors = false;

		for (int i = 0; i < attributes.length; i++) {
			VertexAttribute attribute = attributes[i];
			if (attribute.usage == Usage.Position) {
				if (pos) throw new IllegalArgumentException("two position attributes were specified");
				pos = true;
			}

			if (attribute.usage == Usage.Normal) {
				if (nors) throw new IllegalArgumentException("two normal attributes were specified");
			}

			if (attribute.usage == Usage.Color || attribute.usage == Usage.ColorPacked) {
				if (attribute.numComponents != 4) throw new IllegalArgumentException("color attribute must have 4 components");

				if (cols) throw new IllegalArgumentException("two color attributes were specified");
				cols = true;
			}
		}

		if (pos == false) throw new IllegalArgumentException("no position attribute was specified");
	}

	/**
	 * @return the number of attributes
	 */
	public int size () {
		return attributes.length;
	}

	/**
	 * @param index the index
	 * @return the VertexAttribute at the given index
	 */
	public VertexAttribute get (int index) {
		return attributes[index];
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < attributes.length; i++) {
			builder.append(attributes[i].alias);
			builder.append(", ");
			builder.append(attributes[i].usage);
			builder.append(", ");
			builder.append(attributes[i].numComponents);
			builder.append(", ");
			builder.append(attributes[i].offset);
			builder.append("\n");
		}
		return builder.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9927.java