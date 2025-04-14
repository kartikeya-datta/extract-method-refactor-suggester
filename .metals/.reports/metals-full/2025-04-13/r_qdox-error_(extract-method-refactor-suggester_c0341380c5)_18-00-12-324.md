error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9631.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9631.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9631.java
text:
```scala
static private final d@@ouble BIG_ENOUGH_CEIL = BIG_ENOUGH_INT + 0.99999999;

/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.badlogic.gdx.utils;

/**
 * Utility and fast math functions.
 * 
 * Thanks to:<br>
 * Riven on JavaGaming.org for sin/cos/atan2/floor/ceil.<br>
 * Roquen on JavaGaming.org for random numbers.<br>
 */
public class MathUtils {
	static public final float PI = 3.1415927f;

	static private final int SIN_BITS = 13; // Adjust for accuracy.
	static private final int SIN_MASK = ~(-1 << SIN_BITS);
	static private final int SIN_COUNT = SIN_MASK + 1;

	static private final float radFull = PI * 2;
	static private final float degFull = 360;
	static private final float radToIndex = SIN_COUNT / radFull;
	static private final float degToIndex = SIN_COUNT / degFull;
	static public final float radiansToDegrees = 180f / PI;
	static public final float degreesToRadians = PI / 180;

	static public final float[] sin = new float[SIN_COUNT];
	static public final float[] cos = new float[SIN_COUNT];
	static {
		for (int i = 0; i < SIN_COUNT; i++) {
			float a = (i + 0.5f) / SIN_COUNT * radFull;
			sin[i] = (float)Math.sin(a);
			cos[i] = (float)Math.cos(a);
		}
	}

	static public final float sin (float rad) {
		return sin[(int)(rad * radToIndex) & SIN_MASK];
	}

	static public final float cos (float rad) {
		return cos[(int)(rad * radToIndex) & SIN_MASK];
	}

	static public final float sinDeg (float deg) {
		return sin[(int)(deg * degToIndex) & SIN_MASK];
	}

	static public final float cosDeg (float deg) {
		return cos[(int)(deg * degToIndex) & SIN_MASK];
	}

	// ---

	static private final int ATAN2_BITS = 7; // Adjust for accuracy.
	static private final int ATAN2_BITS2 = ATAN2_BITS << 1;
	static private final int ATAN2_MASK = ~(-1 << ATAN2_BITS2);
	static private final int ATAN2_COUNT = ATAN2_MASK + 1;
	static private final int ATAN2_DIM = (int)Math.sqrt(ATAN2_COUNT);
	static private final float INV_ATAN2_DIM_MINUS_1 = 1.0f / (ATAN2_DIM - 1);
	static private final float[] atan2 = new float[ATAN2_COUNT];
	static {
		for (int i = 0; i < ATAN2_DIM; i++) {
			for (int j = 0; j < ATAN2_DIM; j++) {
				float x0 = (float)i / ATAN2_DIM;
				float y0 = (float)j / ATAN2_DIM;
				atan2[j * ATAN2_DIM + i] = (float)Math.atan2(y0, x0);
			}
		}
	}

	static public final float atan2 (float y, float x) {
		float add, mul;
		if (x < 0) {
			if (y < 0) {
				y = -y;
				mul = 1;
			} else
				mul = -1;
			x = -x;
			add = -3.141592653f;
		} else {
			if (y < 0) {
				y = -y;
				mul = -1;
			} else
				mul = 1;
			add = 0;
		}
		float invDiv = 1 / ((x < y ? y : x) * INV_ATAN2_DIM_MINUS_1);
		int xi = (int)(x * invDiv);
		int yi = (int)(y * invDiv);
		return (atan2[yi * ATAN2_DIM + xi] + add) * mul;
	}

	// ---

	static private int randomSeed = (int)System.currentTimeMillis();

	/**
	 * Returns a random number between 0 (inclusive) and the specified value (inclusive).
	 * @param range Must be >= 0.
	 */
	static public final int random (int range) {
		int seed = randomSeed * 1103515245 + 12345;
		randomSeed = seed;
		return (seed >>> 15) * (range + 1) >>> 17;
	}

	static public final int random (int start, int end) {
		int seed = randomSeed * 1103515245 + 12345;
		randomSeed = seed;
		return ((seed >>> 15) * (end - start + 1) >>> 17) + start;
	}

	static public final boolean randomBoolean () {
		int seed = randomSeed * 1103515245 + 12345;
		randomSeed = seed;
		return seed > 0;
	}

	static public final float random () {
		int seed = randomSeed * 1103515245 + 12345;
		randomSeed = seed;
		return (seed >>> 8) * 1f / (1 << 24);
	}

	static public final float random (float range) {
		int seed = randomSeed * 1103515245 + 12345;
		randomSeed = seed;
		return (seed >>> 8) * 1f / (1 << 24) * range;
	}

	static public final float random (float start, float end) {
		int seed = randomSeed * 1103515245 + 12345;
		randomSeed = seed;
		return start + (seed >>> 8) * 1f / (1 << 24) * (end - start);
	}

	// ---

	static public int nextPowerOfTwo (int value) {
		return 1 << 32 - Integer.numberOfLeadingZeros(value - 1);
	}

	static public boolean isPowerOfTwo (int value) {
		return value != 0 && (value & value - 1) == 0;
	}

	// ---

	static private final int BIG_ENOUGH_INT = 16 * 1024;
	static private final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT;
	static private final double BIG_ENOUGH_CEIL = BIG_ENOUGH_INT + 0.5;

	static public int floor (float x) {
		return (int)(x + BIG_ENOUGH_FLOOR) - BIG_ENOUGH_INT;
	}

	static public int ceil (float x) {
		return (int)(x + BIG_ENOUGH_CEIL) - BIG_ENOUGH_INT;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9631.java