error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10193.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10193.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10193.java
text:
```scala
public A@@udioRecorder newAudioRecorder (int samplingRate, boolean isMono) {

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

package com.badlogic.gdx.backends.openal;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_NO_ERROR;
import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.AL10.AL_PAUSED;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.AL_STOPPED;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alGetError;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alListener;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcei;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.ObjectMap;

/** @author Nathan Sweet */
public class OpenALAudio implements Audio {
	private IntArray idleSources, allSources;
	/** sound id to source **/
	private LongMap<Integer> soundIdToSource;
	private IntMap<Long> sourceToSoundId;
	private long nextSoundId = 0;
	private ObjectMap<String, Class<? extends OpenALSound>> extensionToSoundClass = new ObjectMap();
	private ObjectMap<String, Class<? extends OpenALMusic>> extensionToMusicClass = new ObjectMap();

	Array<OpenALMusic> music = new Array(false, 1, OpenALMusic.class);

	public OpenALAudio () {
		this(16);
	}

	public OpenALAudio (int simultaneousSources) {
		registerSound("ogg", Ogg.Sound.class);
		registerMusic("ogg", Ogg.Music.class);
		registerSound("wav", Wav.Sound.class);
		registerMusic("wav", Wav.Music.class);
		registerSound("mp3", Mp3.Sound.class);
		registerMusic("mp3", Mp3.Music.class);

		try {
			AL.create();
		} catch (LWJGLException ex) {
			throw new GdxRuntimeException("Error initializing OpenAL.", ex);
		}

		allSources = new IntArray(false, simultaneousSources);
		for (int i = 0; i < simultaneousSources; i++) {
			int sourceID = alGenSources();
			if (alGetError() != AL_NO_ERROR) break;
			allSources.add(sourceID);
		}
		idleSources = new IntArray(allSources);
		soundIdToSource = new LongMap<Integer>();
		sourceToSoundId = new IntMap<Long>();

		FloatBuffer orientation = (FloatBuffer)BufferUtils.createFloatBuffer(6)
			.put(new float[] {0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f}).flip();
		alListener(AL_ORIENTATION, orientation);
		FloatBuffer velocity = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).flip();
		alListener(AL_VELOCITY, velocity);
		FloatBuffer position = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).flip();
		alListener(AL_POSITION, position);
	}

	public void registerSound (String extension, Class<? extends OpenALSound> soundClass) {
		if (extension == null) throw new IllegalArgumentException("extension cannot be null.");
		if (soundClass == null) throw new IllegalArgumentException("soundClass cannot be null.");
		extensionToSoundClass.put(extension, soundClass);
	}

	public void registerMusic (String extension, Class<? extends OpenALMusic> musicClass) {
		if (extension == null) throw new IllegalArgumentException("extension cannot be null.");
		if (musicClass == null) throw new IllegalArgumentException("musicClass cannot be null.");
		extensionToMusicClass.put(extension, musicClass);
	}

	public OpenALSound newSound (FileHandle file) {
		if (file == null) throw new IllegalArgumentException("file cannot be null.");
		Class<? extends OpenALSound> soundClass = extensionToSoundClass.get(file.extension());
		if (soundClass == null) throw new GdxRuntimeException("Unknown file extension for sound: " + file);
		try {
			return soundClass.getConstructor(new Class[] {OpenALAudio.class, FileHandle.class}).newInstance(this, file);
		} catch (Exception ex) {
			throw new GdxRuntimeException("Error creating sound " + soundClass.getName() + " for file: " + file, ex);
		}
	}

	public OpenALMusic newMusic (FileHandle file) {
		if (file == null) throw new IllegalArgumentException("file cannot be null.");
		Class<? extends OpenALMusic> musicClass = extensionToMusicClass.get(file.extension());
		if (musicClass == null) throw new GdxRuntimeException("Unknown file extension for music: " + file);
		try {
			return musicClass.getConstructor(new Class[] {OpenALAudio.class, FileHandle.class}).newInstance(this, file);
		} catch (Exception ex) {
			throw new GdxRuntimeException("Error creating music " + musicClass.getName() + " for file: " + file, ex);
		}
	}

	int obtainSource (boolean isMusic) {
		for (int i = 0, n = idleSources.size; i < n; i++) {
			int sourceId = idleSources.get(i);
			int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
			if (state != AL_PLAYING && state != AL_PAUSED) {
				if (isMusic) {
					idleSources.removeIndex(i);
				} else {
					if (sourceToSoundId.containsKey(sourceId)) {
						long soundId = sourceToSoundId.get(sourceId);
						sourceToSoundId.remove(sourceId);
						soundIdToSource.remove(soundId);
					}

					long soundId = nextSoundId++;
					sourceToSoundId.put(sourceId, soundId);
					soundIdToSource.put(soundId, sourceId);
				}
				alSourceStop(sourceId);
				alSourcei(sourceId, AL_BUFFER, 0);
				AL10.alSourcef(sourceId, AL10.AL_GAIN, 1);
				AL10.alSourcef(sourceId, AL10.AL_PITCH, 1);
				AL10.alSource3f(sourceId, AL10.AL_POSITION, 0, 0, 0);
				return sourceId;
			}
		}
		return -1;
	}

	void freeSource (int sourceID) {
		alSourceStop(sourceID);
		alSourcei(sourceID, AL_BUFFER, 0);
		if (sourceToSoundId.containsKey(sourceID)) {
			long soundId = sourceToSoundId.remove(sourceID);
			soundIdToSource.remove(soundId);
		}
		idleSources.add(sourceID);
	}

	void freeBuffer (int bufferID) {
		for (int i = 0, n = idleSources.size; i < n; i++) {
			int sourceID = idleSources.get(i);
			if (alGetSourcei(sourceID, AL_BUFFER) == bufferID) {
				if (sourceToSoundId.containsKey(sourceID)) {
					long soundId = sourceToSoundId.remove(sourceID);
					soundIdToSource.remove(soundId);
				}
				alSourceStop(sourceID);
				alSourcei(sourceID, AL_BUFFER, 0);
			}
		}
	}

	void stopSourcesWithBuffer (int bufferID) {
		for (int i = 0, n = idleSources.size; i < n; i++) {
			int sourceID = idleSources.get(i);
			if (alGetSourcei(sourceID, AL_BUFFER) == bufferID) {
				if (sourceToSoundId.containsKey(sourceID)) {
					long soundId = sourceToSoundId.remove(sourceID);
					soundIdToSource.remove(soundId);
				}
				alSourceStop(sourceID);
			}
		}
	}

	public void update () {
		for (int i = 0; i < music.size; i++)
			music.items[i].update();
	}

	public long getSoundId (int sourceId) {
		if (!sourceToSoundId.containsKey(sourceId)) return -1;
		return sourceToSoundId.get(sourceId);
	}

	public void stopSound (long soundId) {
		if (!soundIdToSource.containsKey(soundId)) return;
		int sourceId = soundIdToSource.get(soundId);
		alSourceStop(sourceId);
	}

	public void setSoundGain (long soundId, float volume) {
		if (!soundIdToSource.containsKey(soundId)) return;
		int sourceId = soundIdToSource.get(soundId);
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
	}

	public void setSoundLooping (long soundId, boolean looping) {
		if (!soundIdToSource.containsKey(soundId)) return;
		int sourceId = soundIdToSource.get(soundId);
		alSourcei(sourceId, AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);
	}

	public void setSoundPitch (long soundId, float pitch) {
		if (!soundIdToSource.containsKey(soundId)) return;
		int sourceId = soundIdToSource.get(soundId);
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	}

	public void setSoundPan (long soundId, float pan, float volume) {
		if (!soundIdToSource.containsKey(soundId)) return;
		int sourceId = soundIdToSource.get(soundId);

		AL10.alSource3f(sourceId, AL10.AL_POSITION, pan, 0, 0);
	}

	public void dispose () {
		for (int i = 0, n = allSources.size; i < n; i++) {
			int sourceID = allSources.get(i);
			int state = alGetSourcei(sourceID, AL_SOURCE_STATE);
			if (state != AL_STOPPED) alSourceStop(sourceID);
			alDeleteSources(sourceID);
		}

		sourceToSoundId.clear();
		soundIdToSource.clear();

		AL.destroy();
		while (AL.isCreated()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	public AudioDevice newAudioDevice (int sampleRate, boolean isMono) {
		return new OpenALAudioDevice(this, sampleRate, isMono);
	}

	public AudioRecorder newAudioRecoder (int samplingRate, boolean isMono) {
		// BOZO - Write OpenAL recorder.
		return new JavaSoundAudioRecorder(samplingRate, isMono);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10193.java