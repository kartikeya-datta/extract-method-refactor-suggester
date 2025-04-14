error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2159.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2159.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2159.java
text:
```scala
@Override public v@@oid setAnimation(String animation, float time, boolean loop) {

package com.badlogic.gdx.graphics.g3d.model.skeleton;

import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.model.AnimatedModel;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Model;
import com.badlogic.gdx.graphics.g3d.model.SubMesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class SkeletonModel implements AnimatedModel {
	public final Skeleton skeleton;
	public final SkeletonSubMesh[] subMeshes;
	
	public SkeletonModel(Skeleton skeleton, SkeletonSubMesh[] subMeshes) {
		this.skeleton = skeleton;
		this.subMeshes = subMeshes;
	}
	
	public void setBindPose() {
		skeleton.setBindPose();
		for(int i = 0; i < subMeshes.length; i++) {
			skin(subMeshes[i], skeleton.combinedMatrices);
		}
	}
	
	@Override public void setAnimation(String animation, float time) {
		skeleton.setAnimation(animation, time);
		for(int i = 0; i < subMeshes.length; i++) {
			skin(subMeshes[i], skeleton.combinedMatrices);
		}
	}
	
	final Vector3 v = new Vector3();
	public void skin(SkeletonSubMesh subMesh, Array<Matrix4> boneMatrices) {
		final int stride = subMesh.mesh.getVertexSize() / 4;
		final int numVertices = subMesh.mesh.getNumVertices();
		int idx = 0;
		int nidx = subMesh.mesh.getVertexAttribute(Usage.Normal) == null? -1: subMesh.mesh.getVertexAttribute(Usage.Normal).offset / 4; 
		final float[] vertices = subMesh.vertices;
		final float[] skinnedVertices = subMesh.skinnedVertices;
			
		System.arraycopy(subMesh.vertices, 0, skinnedVertices, 0, subMesh.vertices.length);
		
		for(int i = 0; i < numVertices; i++, idx += stride, nidx += stride) {
			final int[] boneIndices = subMesh.boneAssignments[i];
			final float[] boneWeights = subMesh.boneWeights[i];
			
			final float ox = vertices[idx], oy = vertices[idx+1], oz = vertices[idx+2];			
			float x = 0, y = 0, z = 0;
			final float onx = 0, ony = 0, onz = 0;
			float nx = 0, ny = 0, nz = 0;
			
			if(nidx != -1) {
				nx = vertices[nidx];
				ny = vertices[nidx+1];
				nz = vertices[nidx+2];
			}
			
			for(int j = 0; j < boneIndices.length; j++) {
				int boneIndex = boneIndices[j];
				float weight = boneWeights[j];
				v.set(ox, oy, oz);
				v.mul(boneMatrices.get(boneIndex));
				x += v.x * weight;
				y += v.y * weight;
				z += v.z * weight;
				
				if(nidx != -1) {
					v.set(onx, ony, onz);
					v.rot(boneMatrices.get(boneIndex));
					nx += v.x * weight;
					ny += v.y * weight;
					nz += v.z * weight;
				}
			}			
			
			skinnedVertices[idx] = x;
			skinnedVertices[idx+1] = y;
			skinnedVertices[idx+2] = z;
			
			if(nidx != -1) {
				skinnedVertices[nidx] = nx;
				skinnedVertices[nidx+1] = ny;
				skinnedVertices[nidx+2] = nz;
			}
		}
		
		subMesh.mesh.setVertices(skinnedVertices);
	}
	
	@Override public void render() {
		int len = subMeshes.length;
		for(int i = 0; i < len; i++) {
			SkeletonSubMesh subMesh = subMeshes[i];
			if(i == 0 ) {
				subMesh.material.bind();
			} else if (!subMeshes[i-1].material.equals(subMesh.material)) {
				subMesh.material.bind();
			}
			subMesh.mesh.render(subMesh.primitiveType);
		}	
	}
	
	@Override public void render (ShaderProgram program) {
		// FIXME
	}
	
	@Override public void setMaterials(Material ... materials) {
		if(materials.length != subMeshes.length) throw new UnsupportedOperationException("number of materials must equal number of sub-meshes");
		int len = materials.length;
		for(int i = 0; i < len; i++) {
			subMeshes[i].material = materials[i];
		}
	}
	
	@Override public void setMaterial(Material material) {
		int len = subMeshes.length;
		for(int i = 0; i < len; i++) {
			subMeshes[i].material = material;
		}
	}
	
	@Override public SubMesh getSubMesh(String name) {
		int len = subMeshes.length;
		for(int i = 0; i < len; i++) {
			if(subMeshes[i].name.equals(name)) return subMeshes[i];
		}		
		return null;
	}

	@Override public SubMesh[] getSubMeshes () {
		return subMeshes;
	}

	@Override public Animation getAnimation (String name) {
		// FIXME
		return null;
	}

	@Override public Animation[] getAnimations () {
		// FIXME
		return null;
	}

	@Override
	public Model getSubModel(String... subMeshNames) {
		// FIXME
		return null;
	}
	
	private final static BoundingBox tmpBox = new BoundingBox();
	@Override public void getBoundingBox (BoundingBox bbox) {
		bbox.inf();
		for(int i = 0; i < subMeshes.length; i++) {
			subMeshes[i].mesh.calculateBoundingBox(tmpBox);
			bbox.ext(tmpBox);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2159.java