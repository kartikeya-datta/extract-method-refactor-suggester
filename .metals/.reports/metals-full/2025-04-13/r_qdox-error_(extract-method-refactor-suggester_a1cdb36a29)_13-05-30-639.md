error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/362.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/362.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/362.java
text:
```scala
private G@@wtMusic[] sounds;

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

package com.badlogic.gdx.backends.gwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.gwt.soundmanager2.SMSound;
import com.badlogic.gdx.backends.gwt.soundmanager2.SMSoundOptions;
import com.badlogic.gdx.backends.gwt.soundmanager2.SoundManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BooleanArray;

public class GwtSound implements Sound {
	
	/** The maximum number of sound instances to create to support simultaneous playback. */
	private static final int MAX_SOUNDS = 8;
	
	/** Our sounds. */
	protected GwtMusic[] sounds;
	/** The next player we think should be available for play - we circle through them to find a free one. */
	private int soundIndex;
	/** The path to the sound file. */
	private FileHandle soundFile;
	
	public GwtSound (FileHandle file) {
		soundFile = file;
		sounds = new GwtMusic[MAX_SOUNDS];
		sounds[0] = new GwtMusic(file);
		soundIndex = 0;
	}
	
	/** Let's find a sound that isn't currently playing.
	 * @return  The index of the sound or -1 if none is available. */
	private int findAvailableSound() {
		for (int i = 0; i < sounds.length; i++) {
			int index = (soundIndex + i) % sounds.length;
			if (sounds[index] == null || !sounds[index].isPlaying()) {
				// point to the next likely free player
				soundIndex = (index + 1) % sounds.length; 
				
				// return the free player
				return index;
			}
		}
		
		// all are busy playing, stop the next sound in the queue and reuse it
		int index = soundIndex % sounds.length;
		soundIndex = (index + 1) % sounds.length;
		return index;
	}

	@Override
	public long play () {
		return play(1.0f, 1.0f, 0.0f, false);
	}

	@Override
	public long play (float volume) {
		return play(volume, 1.0f, 0.0f, false);
	}

	@Override
	public long play (float volume, float pitch, float pan) {
		return play(volume, pitch, pan, false);
	}
	
	private long play (float volume, float pitch, float pan, boolean loop) {
		int soundId = findAvailableSound();
		if (soundId >= 0) {
			GwtMusic sound;
			if (sounds[soundId] == null) {
				sounds[soundId] = new GwtMusic(soundFile);
			}
			sound = sounds[soundId];
			sound.stop();
			sound.setPan(pan, volume);
			sound.setLooping(loop);
			sound.play();
		}
		return soundId;
	}

	@Override
	public long loop () {
		return play(1.0f, 1.0f, 0.0f, true);
	}

	@Override
	public long loop (float volume) {
		return play(volume, 1.0f, 0.0f, true);
	}
	
	@Override
	public long loop (float volume, float pitch, float pan) {
		return play(volume, pitch, pan, true);
	}
	
	@Override
	public void stop () {
		for (int i = 0; i < sounds.length; i++) {
			if (sounds[i] != null)
				sounds[i].stop();
		}
	}

	@Override
	public void dispose () {
		stop();
		for (int i = 0; i < sounds.length; i++) {
			if (sounds[i] != null)
				sounds[i].dispose();
		}
		sounds = null;
	}

	@Override
	public void stop (long soundId) {
		if (soundId >= 0 && sounds[(int)soundId] != null)
			sounds[(int)soundId].stop();
	}

	@Override
	public void setLooping (long soundId, boolean looping) {
		if (soundId >= 0 && sounds[(int)soundId] != null)
			sounds[(int)soundId].setLooping(looping);
	}

	@Override
	public void setPitch (long soundId, float pitch) {
		// FIXME - Not possible?
	}

	@Override
	public void setVolume (long soundId, float volume) {
		if (soundId >= 0 && sounds[(int)soundId] != null)
			sounds[(int)soundId].setVolume(volume);
	}

	@Override
	public void setPan (long soundId, float pan, float volume) {
		if (soundId >= 0 && sounds[(int)soundId] != null) {
			sounds[(int)soundId].setPan(pan, volume);
		}
	}

	@Override
	public void setPriority (long soundId, int priority) {
		// FIXME
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/362.java