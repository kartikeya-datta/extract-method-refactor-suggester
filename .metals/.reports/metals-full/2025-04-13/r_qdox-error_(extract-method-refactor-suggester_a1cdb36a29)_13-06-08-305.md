error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4055.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4055.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 60
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4055.java
text:
```scala
class Comparator implements java.util.Comparator<Decal> {

p@@ackage com.badlogic.gdx.graphics.g3d.decals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Sort;

/**
 * <p>
 * Minimalistic grouping strategy useful for orthogonal scenes where the camera faces the negative z axis.
 * Handles enabling and disabling of blending and uses world-z only front to back sorting for transparent decals.
 * </p>
 * <p>
 * States (* = any, EV = entry value - same as value before flush):<br/>
 * <table>
 * <tr>
 * <td></td><td>expects</td><td>exits on</td>
 * </tr>
 * <tr>
 * <td>glDepthMask</td><td>true</td><td>EV | true</td>
 * </tr>
 * <tr>
 * <td>GL_DEPTH_TEST</td><td>enabled</td><td>EV</td>
 * </tr>
 * <tr>
 * <td>glDepthFunc</td><td>GL_LESS | GL_LEQUAL</td><td>EV</td>
 * </tr>
 * <tr>
 * <td>GL_BLEND</td><td>disabled</td><td>EV | disabled</td>
 * </tr>
 * <tr>
 * <td>glBlendFunc</td><td>*</td><td>*</td>
 * </tr>
 * <tr>
 * <td>GL_TEXTURE_2D</td><td>*</td><td>disabled</td>
 * </tr>
 * </table>
 * </p>
 */
public class SimpleOrthoGroupStrategy implements GroupStrategy {
	private Comparator comparator = new Comparator();
	private static final int GROUP_OPAQUE = 0;
	private static final int GROUP_BLEND = 1;

	@Override
	public int decideGroup(Decal decal) {
		return decal.getMaterial().isOpaque() ? GROUP_OPAQUE : GROUP_BLEND;
	}

	@Override
	public void beforeGroup(int group, Array<Decal> contents) {
		if(group == GROUP_BLEND) {
			Sort.instance().sort(contents, comparator);
			Gdx.gl10.glEnable(GL10.GL_BLEND);
			//no need for writing into the z buffer if transparent decals are the last thing to be rendered
			//and they are rendered back to front
			Gdx.gl10.glDepthMask(false);
		} else {
			// FIXME sort by material
		}
	}

	@Override
	public void afterGroup(int group) {
		if(group == GROUP_BLEND) {
			Gdx.gl10.glDepthMask(true);
			Gdx.gl10.glDisable(GL10.GL_BLEND);
		}
	}

	@Override
	public void beforeGroups() {
		Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
	}

	@Override
	public void afterGroups() {
		Gdx.gl10.glDisable(GL10.GL_TEXTURE_2D);
	}

	private class Comparator implements java.util.Comparator<Decal> {
		@Override
		public int compare(Decal a, Decal b) {
			return a.getZ() - b.getZ() < 0 ? -1 : 1;
		}
	}

	@Override
	public ShaderProgram getGroupShader (int group) {
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4055.java