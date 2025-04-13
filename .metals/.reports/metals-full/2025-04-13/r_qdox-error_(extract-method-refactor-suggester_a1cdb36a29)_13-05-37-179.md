error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2724.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2724.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2724.java
text:
```scala
r@@eturn new AndroidAudioDevice( isMono );

/*
 *  This file is part of Libgdx by Mario Zechner (badlogicgames@gmail.com)
 *
 *  Libgdx is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Libgdx is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with libgdx.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.badlogic.gdx.backends.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

/**
 * An implementation of the {@link Audio} interface for Android.
 * 
 * @author mzechner
 *
 */
final class AndroidAudio implements Audio
{
	private  SoundPool soundPool;
	private final AudioManager manager;
	protected final List<AndroidMusic> musics = new ArrayList<AndroidMusic>( );
	protected final List<Boolean> wasPlaying = new ArrayList<Boolean>( );
	
	AndroidAudio( Activity context )
	{
		soundPool = new SoundPool( 16, AudioManager.STREAM_MUSIC, 100);		
		manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		context.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
	
	protected void pause( )
	{
		wasPlaying.clear();
		for( AndroidMusic music: musics )
		{
			if( music.isPlaying() )
			{
				music.pause();
				wasPlaying.add( true );
			}
			else
				wasPlaying.add( false );
		}
	}
	
	protected void resume( )
	{
		for( int i = 0; i < musics.size(); i++ )
		{
			if( wasPlaying.get(i) )		
				musics.get(i).play();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AudioDevice newAudioDevice(boolean isMono) 
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Music newMusic(FileHandle file) 
	{
		AndroidFileHandle aHandle = (AndroidFileHandle)file;
		
		
		MediaPlayer mediaPlayer = new MediaPlayer();
		
		if( aHandle.isAsset() )
		{
			try
			{
				AssetFileDescriptor descriptor = aHandle.getAssetManager().openFd( aHandle.getFileName() );
				mediaPlayer.setDataSource( descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength() );
				descriptor.close();
				mediaPlayer.prepare();				
				AndroidMusic music = new AndroidMusic( this, mediaPlayer );
				musics.add( music );
				return music;
			}
			catch( Exception ex )
			{
				ex.printStackTrace();				
				return null;
			}
		}
		else
		{
			try
			{				
				mediaPlayer.setDataSource( aHandle.getFileName() );
				mediaPlayer.prepare();				
				AndroidMusic music = new AndroidMusic( this, mediaPlayer );
				musics.add( music );
				return music;
			}
			catch( Exception ex )
			{
				ex.printStackTrace();				
				return null;
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Sound newSound(FileHandle file) 
	{
		AndroidFileHandle aHandle = (AndroidFileHandle)file;
		if( aHandle.isAsset() )
		{
			try {
				AssetFileDescriptor descriptor = aHandle.getAssetManager().openFd( aHandle.getFileName() );
				AndroidSound sound = new AndroidSound( soundPool, manager, soundPool.load( descriptor, 1) );
				descriptor.close();
				return sound;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		else
		{
			try
			{
				return new AndroidSound( soundPool, manager, soundPool.load(aHandle.getFileName(), 1) );
			}
			catch( Exception e )
			{
				e.printStackTrace();
				return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2724.java