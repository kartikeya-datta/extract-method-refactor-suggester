error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7565.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7565.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7565.java
text:
```scala
S@@ystem.loadLibrary( "gdx-" + Version.VERSION );

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


import android.app.Activity;
import android.util.Log;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;

/**
 * An implementation of the {@link Application} interface for Android. Create an {@link Activity}
 * that derives from this class. In the {@link Activity.onCreate()} method call the {@link initialize()}
 * method specifying the configuration for the GLSurfaceView.
 * 
 * @author mzechner
 *
 */
public class AndroidApplication extends Activity implements Application
{
	static
	{
		System.loadLibrary( "gdx" + Version.VERSION );
	}
	
	/** the android graphics instance **/
	private AndroidGraphics graphics;	
	
	/** the input instance **/
	private AndroidInput input;
	
	/** the audio instance **/
	private AndroidAudio audio;
	
	/** the resources instance **/
	private AndroidFiles resources;
	
	/** the DestroyListener **/
	private ApplicationListener listener;
	
	/**
	 * This method has to be called in the {@link Activity.onCreate()}
	 * method. It sets up all the things necessary to get input, render
	 * via OpenGL and so on. If useGL20IfAvailable is set the
	 * AndroidApplication will try to create an OpenGL ES 2.0 context
	 * which can then be used via {@link AndroidApplication.getGraphics().getGL20()}. The
	 * {@link GL10} and {@link GL11} interfaces should not be used when
	 * OpenGL ES 2.0 is enabled. To query whether enabling OpenGL ES 2.0 was
	 * successful use the {@link AndroidApplication.getGraphics().isGL20Available()}
	 * method. Sleep time in touch event handler is 0, so no sleeping is performed.
	 * 
	 * @param useGL2IfAvailable whether to use OpenGL ES 2.0 if its available.
	 */
	public void initialize( boolean useGL2IfAvailable )
	{
		graphics = new AndroidGraphics( this, useGL2IfAvailable );
		input = new AndroidInput( this, graphics.view, 0 );
		graphics.setInput( input );
		audio = new AndroidAudio( this );
		resources = new AndroidFiles( this.getAssets() );
	}
	
	/**
	 * This method has to be called in the {@link Activity.onCreate()}
	 * method. It sets up all the things necessary to get input, render
	 * via OpenGL and so on. If useGL20IfAvailable is set the
	 * AndroidApplication will try to create an OpenGL ES 2.0 context
	 * which can then be used via {@link AndroidApplication.getGraphics().getGL20()}. The
	 * {@link GL10} and {@link GL11} interfaces should not be used when
	 * OpenGL ES 2.0 is enabled. To query whether enabling OpenGL ES 2.0 was
	 * successful use the {@link AndroidApplication.getGraphics().isGL20Available()}
	 * method. sleepTime specifies the number of milliseconds to sleep in the touch
	 * event handler. This may be used on <= 1.6 Android devices. Note that it will not 
	 * solve the CPU usage problem of the event handler of the Android system. Things will
	 * still slow down. 
	 * 
	 * @param useGL2IfAvailable whether to use OpenGL ES 2.0 if its available.
	 */
	public void initialize( boolean useGL2IfAvailable, int sleepTime )
	{
		graphics = new AndroidGraphics( this, useGL2IfAvailable );
		input = new AndroidInput( this, graphics.view, sleepTime );
		graphics.setInput( input );
		audio = new AndroidAudio( this );
		resources = new AndroidFiles( this.getAssets() );
	}
	
	@Override
	protected void onPause( )
	{
		super.onPause( );
		
		if( isFinishing() )		
			graphics.disposeRenderListener();		
		
		if( graphics.view != null )
			graphics.view.onPause();
		
		audio.pause();
		
		if( listener != null )
			listener.pause( this );
	}
	
	@Override
	protected void onResume( )
	{
		super.onResume();
		
		if( listener != null )
			listener.resume( this );			
		
		if( graphics.view != null )
			graphics.view.onResume();
		
		audio.resume();
	}

	@Override
	protected void onDestroy( )
	{
		super.onDestroy();
		
		if( listener != null )
			listener.destroy(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Audio getAudio() 
	{	
		return audio;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Files getFiles() 
	{	
		return resources;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Graphics getGraphics() 
	{		
		return graphics;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Input getInput() 
	{	
		return input;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setApplicationListener(ApplicationListener listener) 
	{	
		this.listener = listener;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(String tag, String message) 
	{	
		Log.d( tag, message );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ApplicationType getType() 
	{	
		return ApplicationType.Android;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7565.java