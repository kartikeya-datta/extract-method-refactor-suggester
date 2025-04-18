error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5588.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5588.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5588.java
text:
```scala
G@@dx.app.log("Mp3", "channels: " + decoder.getChannels() + ", rate: " + decoder.getRate() + ", length: " + decoder.getLength());

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

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.io.Mpg123Decoder;
import com.badlogic.gdx.audio.io.VorbisDecoder;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tests.utils.GdxTest;

/**
 * Demonstrates how to read and playback an OGG file with the {@link VorbisDecoder} found
 * in the gdx-audio extension.
 * @author mzechner
 *
 */
public class Mpg123Test extends GdxTest {
	/** the file to playback **/
	private static final String FILE = "data/8.12.mp3";
	/** a VorbisDecoder to read PCM data from the ogg file **/
	Mpg123Decoder decoder;
	/** an AudioDevice for playing back the PCM data **/
	AudioDevice device;
	
	@Override
	public void create () {
		// copy ogg file to SD card, can't playback from assets
		FileHandle externalFile = Gdx.files.external("tmp/test.mp3");
		Gdx.files.internal(FILE).copyTo(externalFile);
		
		// Create the decoder and log some properties. Note that we need
		// an external or absolute file
		decoder = new Mpg123Decoder(externalFile);
		Gdx.app.log("Vorbis", "channels: " + decoder.getChannels() + ", rate: " + decoder.getRate() + ", length: " + decoder.getLength());

		// Create an audio device for playback
		device = Gdx.audio.newAudioDevice(decoder.getRate(), decoder.getChannels() == 1? true: false);
		
		// start a thread for playback
		Thread playbackThread = new Thread(new Runnable() {
			@Override
			public void run() {
				int readSamples = 0;
				// we need a short[] to pass the data to the AudioDevice
				short[] samples = new short[2048];
				
				// read until we reach the end of the file
				while((readSamples = decoder.readSamples(samples, 0, samples.length)) > 0) {
					// write the samples to the AudioDevice
					device.writeSamples(samples, 0, readSamples);
				}
			}
		});
		playbackThread.setDaemon(true);
		playbackThread.start();
	}

	@Override
	public void dispose() {
		// we should synchronize with the thread here
		// left as an excercise to the reader :)
		device.dispose();
		decoder.dispose();
		// kill the file again
		Gdx.files.external("tmp/test.mp3").delete();
	}

	@Override
	public boolean needsGL20 () {
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5588.java