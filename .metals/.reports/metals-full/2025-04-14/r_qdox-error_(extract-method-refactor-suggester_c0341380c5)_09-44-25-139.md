error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9340.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9340.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9340.java
text:
```scala
public static native v@@oid convertToShort (FloatBuffer source, ShortBuffer target, int numSamples);

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
package com.badlogic.gdx.audio.analysis;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Class holding various static native methods for processing audio data.
 * 
 * @author mzechner
 * 
 */
public class AudioTools {
	/**
	 * Converts the 16-bit signed PCM data given in source to 32-bit float PCM in the range [-1,1]. It is assumed that there's
	 * numSamples elements available in both buffers. Source and target get read and written to from offset 0. All buffers must be
	 * direct.
	 * 
	 * @param source the source buffer
	 * @param target the target buffer
	 * @param numSamples the number of samples
	 */
	public static native void convertToFloat (ShortBuffer source, FloatBuffer target, int numSamples);

	/**
	 * Converts the 32-bit float PCM data given in source to 16-bit signed PCM in the range [-1,1]. It is assumed that there's
	 * numSamples elements available in both buffers. Source and target get read and written to from offset 0. All buffers must be
	 * direct.
	 * 
	 * @param source the source buffer
	 * @param target the target buffer
	 * @param numSamples the number of samples
	 */
	public static native void convertToShort (FloatBuffer source, FloatBuffer target, int numSamples);

	/**
	 * Converts the samples in source which are assumed to be interleaved left/right stereo samples to mono, writting the converted
	 * samples to target. Source is assumed to hold numSamples samples, target should hold numSamples / 2. Samples are read and
	 * written from position 0 up to numSamples. All buffers must be direct.
	 * 
	 * @param source the source buffer
	 * @param target the target buffer
	 * @param numSamples the number of samples to convert (target will have numSamples /2 filled after a call to this)
	 */
	public static native void convertToMono (ShortBuffer source, ShortBuffer target, int numSamples);

	/**
	 * Converts the samples in source which are assumed to be interleaved left/right stereo samples to mono, writting the converted
	 * samples to target. Source is assumed to hold numSamples samples, target should hold numSamples / 2. Samples are read and
	 * written from position 0 up to numSamples. All buffers must be direct.
	 * 
	 * @param source the source buffer
	 * @param target the target buffer
	 * @param numSamples the number of samples to convert (target will have numSamples /2 filled after a call to this)
	 */
	public static native void convertToMono (FloatBuffer source, FloatBuffer target, int numSamples);

	/**
	 * Calculates the spectral flux between the two given spectra. Both buffers are assumed to hold numSamples elements. Spectrum B
	 * is the current spectrum spectrum A the last spectrum. All buffers must be direct.
	 * 
	 * @param spectrumA the first spectrum
	 * @param spectrumB the second spectrum
	 * @param numSamples the number of elements
	 * @return the spectral flux
	 */
	public static native float spectralFlux (FloatBuffer spectrumA, FloatBuffer spectrumB, int numSamples);

	/**
	 * Allcoates a direct buffer for the given number of samples and channels. The final numer of samples is numSamples *
	 * numChannels.
	 * 
	 * @param numSamples the number of samples
	 * @param numChannels the number of channels
	 * @return the direct buffer
	 */
	public static FloatBuffer allocateFloatBuffer (int numSamples, int numChannels) {
		ByteBuffer b = ByteBuffer.allocateDirect(numSamples * numChannels * 4);
		b.order(ByteOrder.nativeOrder());
		return b.asFloatBuffer();
	}

	/**
	 * Allcoates a direct buffer for the given number of samples and channels. The final numer of samples is numSamples *
	 * numChannels.
	 * 
	 * @param numSamples the number of samples
	 * @param numChannels the number of channels
	 * @return the direct buffer
	 */
	public static ShortBuffer allocateShortBuffer (int numSamples, int numChannels) {
		ByteBuffer b = ByteBuffer.allocateDirect(numSamples * numChannels * 2);
		b.order(ByteOrder.nativeOrder());
		return b.asShortBuffer();
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9340.java