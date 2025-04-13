error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3271.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3271.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3271.java
text:
```scala
D@@isplay.sync( 60 );


package com.badlogic.gdx.backends.desktop;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.GdxRuntimeException;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.RenderListener;
import com.badlogic.gdx.Version;

@SuppressWarnings("unchecked")
public class LwjglApplication implements Application {
	static {
		System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "true");
		Version.loadLibrary();
		
		String os = System.getProperty( "os.name" );
		String arch = System.getProperty( "os.arch" );
		boolean is64Bit = false;
		
		if( arch.equals( "amd64" ) )						
			is64Bit = true;
		
		if( os.contains( "Windows" ) )
			loadLibrariesWindows( is64Bit );
		if( os.contains( "Linux" ) )
			loadLibrariesLinux( is64Bit );
		if( os.contains( "Mac") )
			loadLibrariesMac( );		
		
		 System.setProperty("org.lwjgl.librarypath", new File("").getAbsolutePath() );
	}
	
	private static void loadLibrariesWindows(  boolean is64Bit )
	{			
		String[] libNames = null;
		if( is64Bit )
			libNames = new String[]{ "OpenAL64.dll", "lwjgl64.dll", "jinput-raw_64.dll", "jinput-dx8_64.dll" };
		else
			libNames = new String[]{ "OpenAL32.dll", "lwjgl.dll", "jinput-raw.dll", "jinput-dx8.dll" };
		
		for( String libName: libNames )
			loadLibrary( libName, "/native/windows/" );
	}
	
	private static void loadLibrariesLinux(  boolean is64Bit )
	{			
		String[] libNames = null;
		if( is64Bit )
			libNames = new String[]{ "libopenal64.so", "liblwjgl64.so", "jinput-linux64.so",  };
		else
			libNames = new String[]{ "libopenal.so", "liblwjgl.so", "jinput-linux.so",  };
		
		for( String libName: libNames )
			loadLibrary( libName, "/native/linux/" );
	}
	
	
	private static void loadLibrariesMac( )
	{			
		throw new GdxRuntimeException( "loading native libs on Mac OS X not supported, mail contact@badlogicgames.com" );
	}
	
	private static void loadLibrary( String libName, String classPath )
	{
		InputStream in = null;
		BufferedOutputStream out = null;
		
		try
		{
			in = LwjglApplication.class.getResourceAsStream( classPath + libName );
			out = new BufferedOutputStream( new FileOutputStream( libName ) );
			byte[] bytes = new byte[1024*4];
			while( true )
			{
				int read_bytes = in.read(bytes);
				if( read_bytes == -1 )
					break;
				
				out.write( bytes, 0, read_bytes );
			}						
			out.close();
			out = null;
			in.close();
			in = null;
		}
		catch( Throwable t )
		{
			new GdxRuntimeException( "Couldn't load lwjgl native, " + libName, t );
		}
		finally
		{
			if( out != null )
				try{ out.close(); } catch( Exception ex ) { };
			if( in != null )
				try{ in.close(); } catch( Exception ex ) { }
		}
	}

	private final LwjglGraphics graphics;
	private final LwjglInput input;
	private final LwjglAudio audio;

	protected int width, height;
	private String title = "";	
	private boolean mousePressed;
	private int mouseX, mouseY;
	private volatile boolean running = true;
	final ArrayList<RenderListener> listeners = new ArrayList();
	private Thread gameThread;
	private ApplicationListener appListener;

	public LwjglApplication (String title, int width, int height, boolean useGL20IfAvailable) {
		if (title == null) throw new IllegalArgumentException("title cannot be null.");
		this.title = title;
		this.width = width;
		this.height = height;

		graphics = new LwjglGraphics(this, title, width, height, useGL20IfAvailable);
		listeners.add(graphics);
		input = new LwjglInput();
		audio = new LwjglAudio();

		Gdx.app = this;
		Gdx.graphics = this.getGraphics();
		Gdx.input = this.getInput();
		Gdx.audio = this.getAudio();
		Gdx.files = this.getFiles();

		new Thread("LWJGL") {
			public void run () {
				try {
					LwjglApplication.this.start();
				} catch (LWJGLException ex) {
					throw new GdxRuntimeException(ex);
				}
			}
		}.start();
	}

	protected void setupDisplay () throws LWJGLException {
		Display.setDisplayMode(new DisplayMode(width, height));
		Display.setFullscreen(false);
		Display.setTitle(title);
		int samples = 0;
		try {
			Display.create(new PixelFormat(8, 8, 0, samples));
		} catch (Exception ex) {
			Display.destroy();
			try {
				Display.create(new PixelFormat(8, 8, 0));
			} catch (Exception ex2) {
				Display.destroy();
				Display.create(new PixelFormat());
			}
		}
	}

	void start () throws LWJGLException {
		gameThread = Thread.currentThread();

		setupDisplay();

		for (RenderListener listener : listeners) {
			listener.surfaceCreated();
			listener.surfaceChanged(getWidth(), getHeight());
		}
		for (RenderListener listener : listeners)
			listener.render();
		
		while (running && !Display.isCloseRequested()) {

			if (Keyboard.isCreated()) {
				while (Keyboard.next()) {
					if (Keyboard.getEventKeyState())
						input.fireKeyDown(LwjglInput.getKeyCode(Keyboard.getEventKey()));
					else {
						input.fireKeyUp(LwjglInput.getKeyCode(Keyboard.getEventKey()));
						input.fireKeyTyped(Keyboard.getEventCharacter());
					}
				}
			}

			if (Mouse.isCreated()) {
				int x = Mouse.getX();
				int y = height - Mouse.getY();
				while (Mouse.next()) {
					if (isButtonPressed()) {
						if( mousePressed == false )
						{
							mousePressed = true;
							mouseX = x;
							mouseY = y;
							input.fireTouchDown(x, y, 0);
						}
						else {
							if(mouseX != x || mouseY != y) {
								input.fireTouchDragged(x, y, 0);
								mouseX = x;
								mouseY = y;								
							}
						}
					} else {						
						if( mousePressed == true )
						{
							mousePressed = false;
							input.fireTouchUp(x, y, 0);
						}
					}
				}				
			}

			for (int i = 0, n = listeners.size(); i < n; i++)
				listeners.get(i).render();

			Display.update();		
//			Display.sync( 60 );
		}

		if (appListener != null) appListener.pause();
		try {
			for (int i = 0, n = listeners.size(); i < n; i++)
				listeners.get(i).dispose();
		} finally {
			Display.destroy();
		}
		if (appListener != null) appListener.destroy();
	}

	private boolean isButtonPressed( )
	{
		for( int i = 0; i < Mouse.getButtonCount(); i++ )
			if( Mouse.isButtonDown( i ) )
				return true;
		return false;
	}
	
	public void stop () {
		running = false;
		try {
			gameThread.join();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public int getWidth () {
		return width;
	}

	public int getHeight () {
		return height;
	}

	public void close () {
		running = false;
	}

	public Audio getAudio () {
		return audio;
	}

	public Files getFiles () {
		return new LwjglFiles();
	}

	public Graphics getGraphics () {
		return graphics;
	}

	public Input getInput () {
		return input;
	}

	public void setApplicationListener (ApplicationListener appListener) {
		this.appListener = appListener;
	}

	public void log (String tag, String message) {
		System.out.println(tag + ": " + message);
	}

	public ApplicationType getType () {
		return ApplicationType.Desktop;
	}
	
	@Override
	public int getVersion() 
	{
		return 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3271.java