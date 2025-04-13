error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/135.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/135.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/135.java
text:
```scala
public v@@oid dispose() {

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

package com.badlogic.gdx.backends.jogl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Version;

/**
 * An implemenation of the {@link Application} interface based on Jogl for Windows, Linux and Mac. Instantiate this class with
 * apropriate parameters and then register {@link ApplicationListener}, {@link RenderListener} or {@link InputProcessor} instances.
 * 
 * @author mzechner
 * 
 */
public final class JoglApplication implements Application {
	static {
		Version.loadLibrary();
	}

	final JoglGraphics graphics;	
	final JoglInput input;	
	final JoglFiles files;	
	final JoglAudio audio;	
	JFrame frame;
	

	/**
	 * Creates a new {@link JoglApplication} with the given title and dimensions. If useGL20IfAvailable is set the JoglApplication
	 * will try to create an OpenGL 2.0 context which can then be used via {@link JoglApplication.getGraphics().getGL20()}. To query whether enabling OpenGL 2.0
	 * was successful use the {@link JoglApplication.getGraphics().isGL20Available()} method.
	 * 
	 * @param listener the ApplicationListener implementing the program logic
	 * @param title the title of the application
	 * @param width the width of the surface in pixels
	 * @param height the height of the surface in pixels
	 * @param useGL20IfAvailable wheter to use OpenGL 2.0 if it is available or not
	 */
	public JoglApplication (ApplicationListener listener, String title, int width, int height, boolean useGL20IfAvailable) {
		graphics = new JoglGraphics(listener, title, width, height, useGL20IfAvailable);
		input = new JoglInput(graphics.getCanvas());
		audio = new JoglAudio();
		files = new JoglFiles();

		Gdx.app = this;
		Gdx.graphics = this.getGraphics();
		Gdx.input = this.getInput();
		Gdx.audio = this.getAudio();
		Gdx.files = this.getFiles();
		
		initialize(title, width, height);
	}
	
	private void initialize(String title, int width, int height) {
		graphics.getCanvas().setPreferredSize(new Dimension(width, height));
		
		frame = new JFrame(title);		
		frame.setSize(width + frame.getInsets().left + frame.getInsets().right, frame.getInsets().top + frame.getInsets().bottom
			+ height);
		frame.add(graphics.getCanvas(), BorderLayout.CENTER);		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		frame.addWindowListener( new WindowAdapter() {		
			@Override
			public void windowOpened(WindowEvent arg0) {
				graphics.getCanvas().requestFocus();
				graphics.getCanvas().requestFocusInWindow();				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {			
				graphics.pause();								
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				graphics.resume();
			}		
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				graphics.pause();				
				graphics.destroy();
				frame.remove(graphics.getCanvas());
			}				
		});
		
		frame.pack();
		frame.setVisible(true);
		graphics.create();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public Audio getAudio () {
		return audio;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public Files getFiles () {
		return files;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public Graphics getGraphics () {
		return graphics;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public Input getInput () {
		return input;
	}

	@Override public void log (String tag, String message) {
		System.out.println(tag + ": " + message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public ApplicationType getType () {
		return ApplicationType.Desktop;
	}

	@Override public int getVersion () {
		return 0;
	}

	public static void main(String[] argv) {
		ApplicationListener listener = new ApplicationListener() {
			
			@Override
			public void resume() {
				System.out.println("resumed");
			}
			
			@Override
			public void render() {
				System.out.println("render");				
			}
			
			@Override
			public void pause() {
				System.out.println("paused");				
			}
			
			@Override
			public void destroy() {
				System.out.println("destroy");				
			}
			
			@Override
			public void create() {
				System.out.println("created");				
			}

			@Override
			public void resize(int width, int height) {
				System.out.println("resize");
			}
		};
		
		new JoglApplication(listener, "Jogl Application Test", 480, 320, false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/135.java