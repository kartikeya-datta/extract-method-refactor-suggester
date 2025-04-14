error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1477.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1477.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 51
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1477.java
text:
```scala
class WindowCreator extends ApplicationAdapter {

p@@ackage com.badlogic.gdx.tests.lwjgl;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.tests.MusicTest;
import com.badlogic.gdx.tests.ObjTest;
import com.badlogic.gdx.tests.UITest;

/**
 * Demonstrates how to use LwjglAWTCanvas to have multiple GL widgets in a
 * Swing application.
 * @author mzechner
 *
 */
public class SwingLwjglTest extends JFrame {
	LwjglAWTCanvas canvas1;
	
	public SwingLwjglTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container container = getContentPane();
		canvas1 = new LwjglAWTCanvas(new MusicTest(), false);
		LwjglAWTCanvas canvas2 = new LwjglAWTCanvas(new UITest(), false, canvas1);
		LwjglAWTCanvas canvas3 = new LwjglAWTCanvas(new WindowCreator(), false, canvas1);
		
		canvas1.getCanvas().setSize(200, 480);
		canvas2.getCanvas().setSize(200, 480);
		canvas3.getCanvas().setSize(200, 480);
		
		container.add(canvas1.getCanvas(), BorderLayout.LINE_START);
		container.add(canvas2.getCanvas(), BorderLayout.CENTER);
		container.add(canvas3.getCanvas(), BorderLayout.LINE_END);
		
		pack();
		setVisible(true);
		setSize(800, 480);
	}
	
	private class WindowCreator extends ApplicationAdapter {
		SpriteBatch batch;
		BitmapFont font;

		@Override
		public void create () {
			batch = new SpriteBatch();
			font = new BitmapFont();
		}

		@Override
		public void render () {
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			batch.begin();
			font.draw(batch, "Click to create a new window", 10, 100);
			batch.end();
			
			if(Gdx.input.justTouched()) {
				createWindow();
			}
		}
		
		private void createWindow() {
			JFrame window = new JFrame();
			LwjglAWTCanvas canvas = new LwjglAWTCanvas(new ObjTest(), false, canvas1);
			window.getContentPane().add(canvas.getCanvas(), BorderLayout.CENTER);
			window.pack();
			window.setVisible(true);
			window.setSize(200, 200);
		}
	}
	
	public static void main (String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run () {
				new SwingLwjglTest();
			}
		});
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1477.java