error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1006.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1006.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1006.java
text:
```scala
i@@nt result = super.hashCode();

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

package com.badlogic.gdx.graphics.g3d.attributes;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.NumberUtils;

public class DepthTestAttribute extends Attribute {
	public final static String Alias = "depthStencil";
	public final static long Type = register(Alias);

	protected static long Mask = Type;

	public final static boolean is (final long mask) {
		return (mask & Mask) != 0;
	}

	/** The depth test function, or 0 to disable depth test (default: GL10.GL_LEQUAL) */
	public int depthFunc;
	/** Mapping of near clipping plane to window coordinates (default: 0) */
	public float depthRangeNear;
	/** Mapping of far clipping plane to window coordinates (default: 1) */
	public float depthRangeFar;
	/** Whether to write to the depth buffer (default: true) */
	public boolean depthMask;

	public DepthTestAttribute () {
		this(GL20.GL_LEQUAL);
	}

	public DepthTestAttribute (boolean depthMask) {
		this(GL20.GL_LEQUAL, depthMask);
	}

	public DepthTestAttribute (final int depthFunc) {
		this(depthFunc, true);
	}

	public DepthTestAttribute (int depthFunc, boolean depthMask) {
		this(depthFunc, 0, 1, depthMask);
	}

	public DepthTestAttribute (int depthFunc, float depthRangeNear, float depthRangeFar) {
		this(depthFunc, depthRangeNear, depthRangeFar, true);
	}

	public DepthTestAttribute (int depthFunc, float depthRangeNear, float depthRangeFar, boolean depthMask) {
		this(Type, depthFunc, depthRangeNear, depthRangeFar, depthMask);
	}

	public DepthTestAttribute (final long type, int depthFunc, float depthRangeNear, float depthRangeFar, boolean depthMask) {
		super(type);
		if (!is(type)) throw new GdxRuntimeException("Invalid type specified");
		this.depthFunc = depthFunc;
		this.depthRangeNear = depthRangeNear;
		this.depthRangeFar = depthRangeFar;
		this.depthMask = depthMask;
	}

	public DepthTestAttribute (final DepthTestAttribute rhs) {
		this(rhs.type, rhs.depthFunc, rhs.depthRangeNear, rhs.depthRangeFar, rhs.depthMask);
	}

	@Override
	public Attribute copy () {
		return new DepthTestAttribute(this);
	}

	@Override
	public int hashCode () {
		int result = (int)type;
		result = 971 * result + depthFunc;
		result = 971 * result + NumberUtils.floatToRawIntBits(depthRangeNear);
		result = 971 * result + NumberUtils.floatToRawIntBits(depthRangeFar);
		result = 971 * result + (depthMask ? 1 : 0);
		return result; 
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1006.java