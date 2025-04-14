error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5080.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5080.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5080.java
text:
```scala
I@@nputStream fin = new BufferedInputStream(file.read());

/*******************************************************************************
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.backends.lwjgl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.badlogic.gdx.audio.Sound;

/**
 * Implements the {@link Sound} interface for the desktop using {@link Clip}s internally.
 * @author mzechner
 * 
 */
final class LwjglSound implements Sound {
	/** the audio format **/
	private final AudioFormat format;

	/** the audio data **/
	private final byte[] originalSamples;

	/** the float audio data **/
	private float[] samples;

	/** the audio instance **/
	private final LwjglAudio audio;

	public LwjglSound (LwjglAudio audio, LwjglFileHandle file) throws UnsupportedAudioFileException, IOException {
		this.audio = audio;
		InputStream fin = new BufferedInputStream(file.readFile());
		AudioInputStream ain = AudioSystem.getAudioInputStream(fin);
		AudioFormat baseFormat = ain.getFormat();
		AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
			baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);

		ain = AudioSystem.getAudioInputStream(decodedFormat, ain);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 10];
		int readBytes = ain.read(buffer);
		while (readBytes != -1) {
			bytes.write(buffer, 0, readBytes);
			readBytes = ain.read(buffer);
		}
		ain.close();
		System.out.println(decodedFormat);
		format = decodedFormat;
		originalSamples = bytes.toByteArray();

		ByteBuffer tmpBuffer = ByteBuffer.wrap(originalSamples);
		tmpBuffer.order(ByteOrder.LITTLE_ENDIAN);
		ShortBuffer shorts = tmpBuffer.asShortBuffer();
		samples = new float[originalSamples.length / 2];
		for (int i = 0; i < samples.length; i++) {
			float value = shorts.get(i) / (float)Short.MAX_VALUE;
			if (value < -1) value = -1;
			if (value > 1) value = 1;
			samples[i] = value;
		}

		samples = resample(samples, decodedFormat.getSampleRate(), decodedFormat.getChannels() == 1);
	}

	private float[] resample (float[] samples, float sampleRate, boolean isMono) {
		if (sampleRate == 44100) return samples;

		float idxInc = sampleRate / 44100;
		int numSamples = (int)((samples.length / (float)sampleRate) * 44100);
		if (!isMono && numSamples % 2 != 0) numSamples--;

		float[] newSamples = new float[numSamples];

		if (isMono) {
			float idx = 0;
			for (int i = 0; i < newSamples.length; i++) {
				int intIdx = (int)idx;
				if (intIdx >= samples.length - 1) break;

				float value = samples[intIdx] + samples[intIdx + 1];
				value /= 2;
				if (value > 1) value = 1;
				if (value < -1) value = -1;
				newSamples[i] = value;
				idx += idxInc;
			}
		} else {
			float idx = 0;
			for (int i = 0; i < newSamples.length; i += 2) {
				int intIdxL = (int)idx * 2;
				int intIdxR = (int)idx * 2 + 1;
				if (intIdxL >= samples.length - 2) break;

				float value = samples[intIdxL] + samples[intIdxL + 2];
				value /= 2;
				if (value > 1) value = 1;
				if (value < -1) value = -1;
				newSamples[i] = value;

				value = samples[intIdxR] + samples[intIdxR + 2];
				value /= 2;
				if (value > 1) value = 1;
				if (value < -1) value = -1;
				newSamples[i + 1] = value;

				idx += idxInc;
			}
		}

		return newSamples;
	}

	public void dispose () {

	}

	public void play () {
		audio.enqueueSound(this, 1);
	}

	public void play (float volume) {
		audio.enqueueSound(this, volume);
	}

	/**
	 * @return the {@link AudioFormat} of the audio data
	 */
	public AudioFormat getAudioFormat () {
		return format;
	}

	/**
	 * @return the audio samples in form of a byte array
	 */
	public float[] getAudioData () {
		return samples;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5080.java