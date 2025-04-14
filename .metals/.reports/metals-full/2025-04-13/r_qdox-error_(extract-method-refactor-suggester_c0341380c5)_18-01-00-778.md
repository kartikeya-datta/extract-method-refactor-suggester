error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4696.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4696.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 61
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4696.java
text:
```scala
public class ModelInstance implements RenderableProvider {

p@@ackage com.badlogic.gdx.graphics.g3d;

import com.badlogic.gdx.graphics.g3d.materials.NewMaterial;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.MeshPartMaterial;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;

/**
 * An instance of a {@link Model}, allows to specify global transform and modify the materials, as it
 * has a copy of the model's materials. Multiple instances can be created from the same Model, 
 * all sharing the meshes and textures of the Model. The Model owns the meshes and textures, to 
 * dispose of these, the Model has to be disposed.</p>
 * 
 * The ModelInstance creates a full copy of all materials and nodes.
 * @author badlogic
 *
 */
public class ModelInstance {
	/** the {@link Model} this instances derrives from **/
	public final Model model;
	/** the world transform **/
	public final Matrix4 transform = new Matrix4();
	/** a copy of the materials of the original model **/
	public final Array<NewMaterial> materials = new Array<NewMaterial>();
	/** a copy of the nodes of the original model, referencing the copied materials in their {@link MeshPartMaterial} instances **/
	public final Array<Node> nodes = new Array<Node>();
	
	public ModelInstance(Model model) {
		this(model, new Matrix4());
	}
	
	public ModelInstance(Model model, Matrix4 transform) {
		this.model = model;
		this.transform.set(transform);
		copyMaterials(model.materials);
		copyNodes(model.nodes);
		calculateTransforms();
	}

	private void copyMaterials (Array<NewMaterial> materials) {
		for(NewMaterial material: materials) {
			this.materials.add(material.copy());
		}		
	}

	private void copyNodes (Array<Node> nodes) {
		for(Node node: nodes) {
			this.nodes.add(copyNode(null, node));
		}
	}
	
	private Node copyNode(Node parent, Node node) {
		Node copy = new Node();
		copy.id = node.id;
		copy.boneId = node.boneId;
		copy.parent = parent;
		copy.translation.set(node.translation);
		copy.rotation.set(node.rotation);
		copy.scale.set(node.scale);
		copy.localTransform.set(node.localTransform);
		copy.worldTransform.set(node.worldTransform);
		for(MeshPartMaterial meshPart: node.meshPartMaterials) {
			copy.meshPartMaterials.add(copyMeshPart(meshPart));
		}
		for(Node child: node.children) {
			copy.children.add(copyNode(copy, child));
		}
		return copy;
	}
	
	private MeshPartMaterial copyMeshPart (MeshPartMaterial meshPart) {
		MeshPartMaterial copy = new MeshPartMaterial();
		copy.meshPart = new MeshPart();
		copy.meshPart.id = meshPart.meshPart.id;
		copy.meshPart.indexOffset = meshPart.meshPart.indexOffset;
		copy.meshPart.numVertices = meshPart.meshPart.numVertices;
		copy.meshPart.primitiveType = meshPart.meshPart.primitiveType;
		copy.meshPart.mesh = meshPart.meshPart.mesh;
		
		int index = model.materials.indexOf(meshPart.material, true);
		if(index == -1) {
			throw new GdxRuntimeException("Inconsistent model, material in MeshPartMaterial not found in Model");
		}
		copy.material = materials.get(index);
		return copy;
	}

	/**
	 * Calculates the local and world transform of all {@link Node} instances in this model, recursively.
	 * First each {@link Node#localTransform} transform is calculated based on the translation, rotation and
	 * scale of each Node. Then each {@link Node#calculateWorldTransform()}
	 * is calculated, based on the parent's world transform and the local transform of each Node.</p>
	 * 
	 * This method can be used to recalculate all transforms if any of the Node's local properties (translation, rotation, scale)
	 * was modified.
	 */
	public void calculateTransforms() {
		for(Node node: nodes) {
			node.calculateTransforms(true);
		}
	}
	
	/**
	 * Traverses the Node hierarchy and collects {@link Renderable} instances for every
	 * node with a graphical representation. Renderables are obtained from the provided
	 * pool. The resulting array can be rendered via a {@link ModelBatch}.
	 * 
	 * @param renderables the output array
	 * @param pool the pool to obtain Renderables from
	 */
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		for(Node node: nodes) {
			getRenderables(node, renderables, pool);
		}
	}
	
	private void getRenderables(Node node, Array<Renderable> renderables, Pool<Renderable> pool) {
		if(node.meshPartMaterials.size > 0) {
			for(MeshPartMaterial meshPart: node.meshPartMaterials) {
				Renderable renderable = pool.obtain();
				renderable.material = meshPart.material;
				renderable.mesh = meshPart.meshPart.mesh;
				renderable.meshPartOffset = meshPart.meshPart.indexOffset;
				renderable.meshPartSize = meshPart.meshPart.numVertices;
				renderable.primitiveType = meshPart.meshPart.primitiveType;
				renderable.transform.set(transform).mul(node.worldTransform);
				renderables.add(renderable);
			}
		}
		
		for(Node child: node.children) {
			getRenderables(child, renderables, pool);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4696.java