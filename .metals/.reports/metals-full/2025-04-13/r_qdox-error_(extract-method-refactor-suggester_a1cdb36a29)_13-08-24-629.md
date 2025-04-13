error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1411.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1411.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1411.java
text:
```scala
t@@his.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.setPosition(position);

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

package com.badlogic.gdx.backends.gwt.soundmanager2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.backends.gwt.GwtMusic;
import com.badlogic.gdx.backends.gwt.soundmanager2.SoundManager.SoundManagerCallback;
import com.google.gwt.core.client.JavaScriptObject;

public class SMSound {
	
	public interface SMSoundCallback {
		public void onfinish ();
	}
	
	/** Constants for play state. */
	public static final int STOPPED = 0;
	public static final int PLAYING = 1;
	
	private JavaScriptObject jsSound;
	
	protected SMSound (JavaScriptObject jsSound) {
		this.jsSound = jsSound;
	}

	/** Stops, unloads and destroys a sound, freeing resources etc. */
	public native final void destruct () /*-{
		this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.destruct();
	}-*/;

	/** The current location of the "play head" within the sound, specified in milliseconds (1 sec = 1000 msec).
	 * @return The current playing position of the sound. */
	public native final int getPosition () /*-{
		return this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.position;
	}-*/;
	
	/** Seeks to a given position within a sound, specified by miliseconds (1000 msec = 1 second.) Affects position property.
	 * @param position the position to seek to. */
	public native final void setPosition (int position) /*-{
		return this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.setPosition(position);
	}-*/;
	
	/** Pauses the given sound. (Does not toggle.) Affects paused property (boolean.) */
	public native final void pause () /*-{
		this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.pause();
	}-*/;
	
	/** Starts playing the given sound. */
	public native final void play (SMSoundOptions options) /*-{
		this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.play(
			{
				volume: options.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSoundOptions::volume,
				pan: options.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSoundOptions::pan,
				loops: options.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSoundOptions::loops,
				from: options.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSoundOptions::from,
				onfinish: function() {
					var callback = options.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSoundOptions::callback;
					if(callback != null) {
						callback.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound.SMSoundCallback::onfinish()();
					}
				}
			}
		);
	}-*/;
	
	/** Starts playing the given sound. */
	public native final void play () /*-{
		this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.play();
	}-*/;

	/** Resumes the currently-paused sound. Does not affect currently-playing sounds. */
	public native final void resume () /*-{
		this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.resume();
	}-*/;

	/** Stops playing the given sound. */
	public native final void stop () /*-{
		this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.stop();
	}-*/;

	/** Sets the volume of the given sound. Affects volume property. 
	 * @param volume the volume, accepted values: 0-100.*/
	public native final void setVolume (int volume) /*-{
		this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.setVolume(volume);
	}-*/;

	/** Gets the volume of the give sound.
	 * @return the volume as a value between 0-100. */
	public native final int getVolume () /*-{
		return this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.volume;
	}-*/;

	/** Sets the stereo pan (left/right bias) of the given sound. Affects pan property.
	 * @param pan the panning amount, accepted values: -100 to 100 (L/R, 0 = center.) */
	public native final void setPan (int pan) /*-{
		this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.setPan(pan);
	}-*/;
	
	/** Gets the pan of the give sound.
	 * @return the pan as a value between -100-100. (L/R, 0 = center.)*/
	public native final int getPan () /*-{
		return this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.pan;
	}-*/;
	
	/** Numeric value indicating the current playing state of the sound.
	 * 0 = stopped/uninitialised.
	 * 1 = playing or buffering sound (play has been called, waiting for data etc.).
	 * Note that a 1 may not always guarantee that sound is being heard, given buffering and autoPlay status.
	 * @return the current playing state. */
	public native final int getPlayState () /*-{
		return this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.playState;
	}-*/;
	
	/** Boolean indicating pause status. True/False. */
	public native final boolean getPaused () /*-{
		return this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.paused;
	}-*/;
	
	/** Number of times to loop the sound. */
	public native final int getLoops () /*-{
		return this.@com.badlogic.gdx.backends.gwt.soundmanager2.SMSound::jsSound.loops;
	}-*/;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1411.java