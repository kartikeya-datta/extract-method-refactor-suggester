error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4714.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4714.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4714.java
text:
```scala
t@@hread = new Thread(this, "LWJGL Audio");

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

package com.badlogic.gdx.backends.desktop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.GdxRuntimeException;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

/**
 * An implementation of the {@link Audio} interface for the desktop.
 * 
 * @author mzechner
 * 
 */
final class LwjglAudio implements Audio, Runnable {
	/** the audio line for sound effects **/
	private SourceDataLine line;

	/** The current buffers to play **/
	private final List<LwjglSoundBuffer> buffers = new ArrayList<LwjglSoundBuffer>();

	/** The sound effects thread **/
	private Thread thread;

	/**
	 * Helper class for playing back sound effects concurrently.
	 * 
	 * @author mzechner
	 * 
	 */
	class LwjglSoundBuffer {
		private final float[] samples;
		private final AudioFormat format;
		private final float volume;
		private int writtenSamples = 0;

		public LwjglSoundBuffer (LwjglSound sound, float volume) throws Exception {
			samples = sound.getAudioData();
			format = sound.getAudioFormat();
			this.volume = volume;
		}

		/**
		 * Writes the next numFrames frames to the line for playback
		 * @return whether playback is done or not.
		 */
		public boolean writeSamples (int numSamples, float[] buffer) {
			if (format.getChannels() == 1) {
				int remainingSamples = Math.min(samples.length, writtenSamples + numSamples / 2);
				for (int i = writtenSamples, j = 0; i < remainingSamples; i++, j += 2) {
					buffer[j] += samples[i] * volume;
					buffer[j + 1] += samples[i] * volume;
					writtenSamples++;
				}
			} else {
				int remainingSamples = Math.min(samples.length, writtenSamples + numSamples);
				for (int i = writtenSamples, j = 0; i < remainingSamples; i += 2, j += 2) {
					buffer[j] += samples[i] * volume;
					buffer[j + 1] += samples[i + 1] * volume;
					writtenSamples += 2;
				}
			}

			if (writtenSamples >= samples.length)
				return false;
			else
				return true;
		}
	}

	LwjglAudio () {
		try {
			AudioFormat format = new AudioFormat(44100.0f, 16, 2, true, false);
			line = AudioSystem.getSourceDataLine(format);
			line.open(format, 4410);
			line.start();
			thread = new Thread(this);
			thread.setDaemon(true);
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AudioDevice newAudioDevice (boolean isMono) {
		return new LwjglAudioDevice(isMono);
	}

	public Music newMusic (FileHandle file) {
		try {
			LwjglMusic music = new LwjglMusic(((LwjglFileHandle)file));
			return music;
		} catch (Exception e) {
			throw new GdxRuntimeException("Couldn't create Music instance from file '" + file + "'", e);
		}
	}

	public Sound newSound (FileHandle file) {
		try {
			LwjglSound sound = new LwjglSound(this, ((LwjglFileHandle)file));
			return sound;
		} catch (Exception e) {
			throw new GdxRuntimeException("Couldn't create Sound instance from file '" + file + "'", e);
		}
	}

	protected void enqueueSound (LwjglSound sound, float volume) {
		try {
			synchronized (this) {
				buffers.add(new LwjglSoundBuffer(sound, volume));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void run () {

		int NUM_SAMPLES = 44100 * 2;
		float[] buffer = new float[NUM_SAMPLES];
		byte[] bytes = new byte[2 * NUM_SAMPLES];

		while (true) {
			int samplesToWrite = line.available() / 2;

			if (samplesToWrite > 0) {
				fillBuffer(buffer, bytes, samplesToWrite);
				int writtenBytes = line.write(bytes, 0, samplesToWrite * 2);
				while (writtenBytes != samplesToWrite * 2)
					writtenBytes += line.write(bytes, writtenBytes, samplesToWrite - writtenBytes);
			}

			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void fillBuffer (float[] buffer, byte[] bytes, int samplesToWrite) {
		for (int i = 0; i < buffer.length; i++)
			buffer[i] = 0.0f;
		for (int i = 0; i < bytes.length; i++)
			bytes[i] = 0;

		int numBuffers = buffers.size();
		synchronized (this) {
			Iterator<LwjglSoundBuffer> bufferIter = buffers.iterator();
			while (bufferIter.hasNext()) {
				LwjglSoundBuffer soundBuffer = bufferIter.next();
				if (!soundBuffer.writeSamples(samplesToWrite, buffer)) bufferIter.remove();
			}
		}

		if (numBuffers > 0) {
			for (int i = 0, j = 0; i < samplesToWrite; i++, j += 2) {
				float fValue = buffer[i];
				if (fValue > 1) fValue = 1;
				if (fValue < -1) fValue = -1;
				short value = (short)(fValue * Short.MAX_VALUE);
				bytes[j] = (byte)(value | 0xff);
				bytes[j + 1] = (byte)(value >> 8);
			}
		}
	}

	public AudioRecorder newAudioRecoder (int samplingRate, boolean isMono) {
		return new LwjglAudioRecorder(samplingRate, isMono);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4714.java