error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2201.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2201.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2201.java
text:
```scala
public i@@nt getNumLayers () {

package com.badlogic.gdx.maps;

import java.util.Iterator;

import com.badlogic.gdx.utils.Array;

/**
 * Ordered list of {@link MapLayer} instances owned by a {@link Map}
 */
public class MapLayers implements Iterable<MapLayer> {
	private Array<MapLayer> layers = new Array<MapLayer>();

	/**
	 * @param index
	 * @return layer at index
	 */
	public MapLayer getLayer(int index) {
		return layers.get(index);
	}
	
	/**
	 * @param name
	 * @return first layer matching the name, null otherwise
	 */
	public MapLayer getLayer(String name) {
		for (MapLayer layer : layers) {
			if (name.equals(layer.getName())) {
				return layer;
			}
		}
		return null;
	}
	
	/** @return number of layers in the collection */
	public int getCount() {
		return layers.size;
	}

	/**
	 * @param layer layer to be added to the set
	 */
	public void addLayer(MapLayer layer) {
		this.layers.add(layer);
	}
	
	/**
	 * @param index removes layer at index
	 */
	public void removeLayer(int index) {
		layers.removeIndex(index);
	}
	
	/**
	 * @param layer layer to be removed
	 */
	public void removeLayer(MapLayer layer) {
		layers.removeValue(layer, true);
	}

	/**
	 * @param type
	 * @return array with all the layers matching type
	 */
	public <T extends MapLayer> Array<T> getLayersByType(Class<T> type) {
		return getLayersByType(type, new Array<T>());	
	}
	
	/**
	 * 
	 * @param type
	 * @param fill array to be filled with the matching layers
	 * @return array with all the layers matching type
	 */
	public <T extends MapLayer> Array<T> getLayersByType(Class<T> type, Array<T> fill) {
		fill.clear();
		for (MapLayer layer : layers) {
			if (type.isInstance(layer)) {
				fill.add((T) layer);
			}
		}
		return fill;
	}

	/**
	 * @return iterator to set of layers
	 */
	@Override
	public Iterator<MapLayer> iterator() {
		return layers.iterator();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2201.java