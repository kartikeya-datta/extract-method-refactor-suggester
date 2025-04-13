error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5848.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5848.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[16,1]

error in qdox parser
file content:
```java
offset: 819
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5848.java
text:
```scala
class LwjglMusic implements Music, Runnable {

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

p@@ackage com.badlogic.gdx.backends.desktop;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.badlogic.gdx.audio.Music;

public class LwjglMusic implements Music, Runnable {
	private enum State {
		Playing, Stopped, Paused
	}

	private State state = State.Stopped;
	private final Thread thread;
	private final File file;
	private AudioInputStream ain;
	private final SourceDataLine line;
	private final byte[] buffer;
	private boolean looping = false;
	private boolean disposed = false;

	public LwjglMusic (LwjglFileHandle handle) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.file = handle.getFile();

		openAudioInputStream();
		AudioFormat audioFormat = ain.getFormat();
		line = AudioSystem.getSourceDataLine(audioFormat);
		line.open(audioFormat); // FIXME reduce latency, gotta reimplement the playback thread.
		line.start();
		buffer = new byte[10000 * ain.getFormat().getFrameSize()];
		ain.close();
		ain = null;

		thread = new Thread(this, "LWJGL Music");
		thread.setDaemon(true);
		thread.start();
	}

	private void openAudioInputStream () throws UnsupportedAudioFileException, IOException {
		ain = AudioSystem.getAudioInputStream(file);
		AudioFormat baseFormat = ain.getFormat();
		AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
			baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);

		ain = AudioSystem.getAudioInputStream(decodedFormat, ain);
	}

	public void dispose () {
		disposed = true;
		try {
			thread.join();
			line.close();
			ain.close();
		} catch (Exception e) {
			// nothing we can do here
		}
	}

	public boolean isLooping () {
		return looping;
	}

	public boolean isPlaying () {
		return state == State.Playing;
	}

	public void pause () {
		synchronized (this) {
			if (state == State.Playing) state = State.Paused;
		}
	}

	public void play () {
		synchronized (this) {
			if (state == State.Playing) return;

			if (state == State.Paused) {
				state = State.Playing;
				return;
			}

			try {
				openAudioInputStream();
				state = State.Playing;
			} catch (Exception e) {
				state = State.Stopped;
			}
		}
	}

	public void stop () {
		synchronized (this) {
			if (state == State.Stopped) return;

			state = State.Stopped;
			line.flush();
			try {
				ain.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ain = null;
		}
	}

	public void setLooping (boolean isLooping) {
		looping = isLooping;
	}

	public void setVolume (float volume) {
		try {
			volume = Math.min(1, volume);
			volume = Math.max(0, volume);
			FloatControl control = (FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN);
			control.setValue(-80 + volume * 80);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
	}

	public void run () {
		int readBytes = 0;
		long readSamples = 0;

		while (!disposed) {
			synchronized (this) {
				if (state == State.Playing) {
					try {

						readBytes = ain.read(buffer);

						if (readBytes != -1) {
							int writtenBytes = line.write(buffer, 0, readBytes);
							while (writtenBytes != readBytes)
								writtenBytes += line.write(buffer, writtenBytes, readBytes - writtenBytes);
							readSamples += readBytes / ain.getFormat().getFrameSize();
						} else {
							System.out.println("samples: " + readSamples);
							ain.close();
							if (!isLooping())
								state = State.Stopped;
							else
								openAudioInputStream();
						}
					} catch (Exception ex) {
						try {
							ain.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						line.close();
						ex.printStackTrace();
						state = State.Stopped;
						return;
					}
				}

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5848.java