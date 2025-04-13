error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5603.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5603.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5603.java
text:
```scala
r@@eturn channels == 1;


package com.badlogic.gdx.backends.openal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL11;

import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

import static org.lwjgl.openal.AL10.*;

/** @author Nathan Sweet */
public class OpenALAudioDevice implements AudioDevice {
	static private final int bufferSize = 512;
	static private final int bufferCount = 9;
	static private final int bytesPerSample = 2;
	static private final ByteBuffer tempBuffer = BufferUtils.createByteBuffer(bufferSize);

	private final OpenALAudio audio;
	private final int channels;
	private IntBuffer buffers;
	private int sourceID = -1;
	private int format, sampleRate;
	private boolean isPlaying;
	private float volume = 1;
	private float renderedSeconds, secondsPerBuffer;
	private byte[] bytes;

	public OpenALAudioDevice (OpenALAudio audio, int sampleRate, boolean isMono) {
		this.audio = audio;
		channels = isMono ? 1 : 2;
		this.format = channels > 1 ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16;
		this.sampleRate = sampleRate;
		secondsPerBuffer = (float)bufferSize / bytesPerSample / channels / sampleRate;
	}

	public void writeSamples (short[] samples, int offset, int numSamples) {
		if (bytes == null || bytes.length < numSamples * 2) bytes = new byte[numSamples * 2];
		for (int i = offset, ii = 0; i < numSamples; i++) {
			short sample = samples[i];
			bytes[ii++] = (byte)(sample & 0xFF);
			bytes[ii++] = (byte)((sample >> 8) & 0xFF);
		}
		writeSamples(bytes, 0, numSamples * 2);
	}

	public void writeSamples (float[] samples, int offset, int numSamples) {
		if (bytes == null || bytes.length < numSamples * 2) bytes = new byte[numSamples * 2];
		for (int i = offset, ii = 0; i < numSamples; i++) {
			float floatSample = samples[i];
			floatSample = MathUtils.clamp(floatSample, -1f, 1f);
			int intSample = (int)(floatSample * 32767);
			bytes[ii++] = (byte)(intSample & 0xFF);
			bytes[ii++] = (byte)((intSample >> 8) & 0xFF);
		}
		writeSamples(bytes, 0, numSamples * 2);
	}

	public void writeSamples (byte[] data, int offset, int length) {
		if (length < 0) throw new IllegalArgumentException("length cannot be < 0.");

		if (sourceID == -1) {
			sourceID = audio.obtainSource(true);
			if (sourceID == -1) return;
			if (buffers == null) {
				buffers = BufferUtils.createIntBuffer(bufferCount);
				alGenBuffers(buffers);
				if (alGetError() != AL_NO_ERROR) throw new GdxRuntimeException("Unabe to allocate audio buffers.");
			}
			alSourcei(sourceID, AL_LOOPING, AL_FALSE);
			alSourcef(sourceID, AL_GAIN, volume);
			// Fill initial buffers.
			int queuedBuffers = 0;
			for (int i = 0; i < bufferCount; i++) {
				int bufferID = buffers.get(i);
				int written = Math.min(bufferSize, length);
				tempBuffer.clear();
				tempBuffer.put(data, offset, written).flip();
				alBufferData(bufferID, format, tempBuffer, sampleRate);
				alSourceQueueBuffers(sourceID, bufferID);
				length -= written;
				offset += written;
				queuedBuffers++;
			}
			// Queue rest of buffers, empty.
			tempBuffer.clear().flip();
			for (int i = queuedBuffers; i < bufferCount; i++) {
				int bufferID = buffers.get(i);
				alBufferData(bufferID, format, tempBuffer, sampleRate);
				alSourceQueueBuffers(sourceID, bufferID);
			}
			alSourcePlay(sourceID);
			isPlaying = true;
		}

		while (length > 0) {
			int written = fillBuffer(data, offset, length);
			length -= written;
			offset += written;
		}
	}

	/** Blocks until some of the data could be buffered. */
	private int fillBuffer (byte[] data, int offset, int length) {
		int written = Math.min(bufferSize, length);

		outer:
		while (true) {
			int buffers = alGetSourcei(sourceID, AL_BUFFERS_PROCESSED);
			while (buffers-- > 0) {
				int bufferID = alSourceUnqueueBuffers(sourceID);
				if (bufferID == AL_INVALID_VALUE) break;
				renderedSeconds += secondsPerBuffer;

				tempBuffer.clear();
				tempBuffer.put(data, offset, written).flip();
				alBufferData(bufferID, format, tempBuffer, sampleRate);

				alSourceQueueBuffers(sourceID, bufferID);
				break outer;
			}
			// Wait for buffer to be free.
			try {
				Thread.sleep((long)(1000 * secondsPerBuffer / bufferCount));
			} catch (InterruptedException ignored) {
			}
		}

		// A buffer underflow will cause the source to stop.
		if (!isPlaying || alGetSourcei(sourceID, AL_SOURCE_STATE) != AL_PLAYING) {
			alSourcePlay(sourceID);
			isPlaying = true;
		}

		return written;
	}

	public void stop () {
		if (sourceID == -1) return;
		audio.freeSource(sourceID);
		sourceID = -1;
		renderedSeconds = 0;
		isPlaying = false;
	}

	public boolean isPlaying () {
		if (sourceID == -1) return false;
		return isPlaying;
	}

	public void setVolume (float volume) {
		this.volume = volume;
		if (sourceID != -1) alSourcef(sourceID, AL_GAIN, volume);
	}

	public float getPosition () {
		if (sourceID == -1) return 0;
		return renderedSeconds + alGetSourcef(sourceID, AL11.AL_SEC_OFFSET);
	}

	public void setPosition (float position) {
		renderedSeconds = position;
	}

	public int getChannels () {
		return format == AL_FORMAT_STEREO16 ? 2 : 1;
	}

	public int getRate () {
		return sampleRate;
	}

	public void dispose () {
		if (buffers == null) return;
		if (sourceID != -1) {
			audio.freeSource(sourceID);
			sourceID = -1;
		}
		alDeleteBuffers(buffers);
		buffers = null;
	}

	public boolean isMono () {
		return false;
	}

	public int getLatency () {
		return (int)(secondsPerBuffer * bufferCount * 1000);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5603.java