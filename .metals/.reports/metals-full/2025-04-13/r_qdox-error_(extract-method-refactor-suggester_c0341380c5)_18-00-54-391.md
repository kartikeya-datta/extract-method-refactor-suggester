error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4116.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4116.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4116.java
text:
```scala
K@@eyframe keyframe = anim.keyframes[(int)time];

package com.badlogic.gdx.graphics.g3d.model.keyframe;

import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.model.AnimatedModel;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Model;
import com.badlogic.gdx.graphics.g3d.model.SubMesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;


public class KeyframedModel implements AnimatedModel {
	public KeyframedSubMesh[] subMeshes;

	@Override public void render() {		
		int len = subMeshes.length;
		for(int i = 0; i < len; i++) {
			KeyframedSubMesh subMesh = subMeshes[i];
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

	@Override public void setAnimation (String animation, float time) {
		int len = subMeshes.length;
		for(int i = 0; i < len; i++) {
			KeyframedSubMesh subMesh = subMeshes[i];
			KeyframedAnimation anim = subMesh.animations.get(animation);
			if(anim == null) throw new IllegalArgumentException("No animation with name '" + animation + "' in submesh #" + i);
			// FIXME actually select frames and blend...
			Keyframe keyframe = anim.keyframes[9];					
			
			float[] src = keyframe.vertices;
			int numComponents = keyframe.animatedComponents;
			int srcLen = numComponents * subMesh.mesh.getNumVertices();
			
			float[] dst = subMesh.blendedVertices;
			int dstInc = subMesh.mesh.getVertexSize() / 4 - numComponents;
			
			for(int srcIdx = 0, dstIdx = 0; srcIdx < srcLen; dstIdx += dstInc) {
				for(int j = 0; j < numComponents; j++) {
					dst[dstIdx++] = src[srcIdx++];
				}
			}
			
			subMesh.mesh.setVertices(dst);
		}
	}
	
	@Override public Animation getAnimation (String name) {
		return subMeshes[0].animations.get(name);		
	}

	private Animation[] animations = null;
	@Override public Animation[] getAnimations () {
		if(animations == null || animations.length != subMeshes[0].animations.size) {
			Array<KeyframedAnimation> meshAnims = subMeshes[0].animations.values().toArray();
			animations = new KeyframedAnimation[meshAnims.size];
			for(int i = 0; i < animations.length; i++) {
				animations[i] = meshAnims.get(i);
			}
		}
		return animations;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4116.java