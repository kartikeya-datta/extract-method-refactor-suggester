error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1053.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1053.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1053.java
text:
```scala
l@@ocalTransform.idt().translate(translation).rotate(rotation).scale(scale.x, scale.y, scale.z);

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.graphics.g3d.model;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

/**
 * A node is part of a hierarchy of Nodes in a {@link Model}. A Node encodes
 * a transform relative to its parents. A Node can have child nodes. Optionally
 * a node can specify a {@link MeshPart} and a {@link Material} to be applied to the mesh part.
 * @author badlogic
 *
 */
public class Node {
	/** the id, may be null, FIXME is this unique? **/
	public String id;
	/** parent node, may be null **/
	public Node parent;
	/** child nodes **/
	public final Array<Node> children = new Array<Node>(2);
	/** Whether this node is currently being animated, if so the translation, rotation and scale values are not used. */
	public boolean isAnimated;
	/** the translation, relative to the parent, not modified by animations **/
	public final Vector3 translation = new Vector3();
	/** the rotation, relative to the parent, not modified by animations **/
	public final Quaternion rotation = new Quaternion(0, 0, 0, 1);
	/** the scale, relative to the parent, not modified by animations **/
	public final Vector3 scale = new Vector3(1, 1, 1);
	/** the local transform, based on translation/rotation/scale ({@link #calculateLocalTransform()}) or any applied animation **/
	public final Matrix4 localTransform = new Matrix4();
	/** the global transform, product of local transform and transform of the parent node, calculated via {@link #calculateWorldTransform()}**/
	public final Matrix4 globalTransform = new Matrix4();

	public Array<NodePart> parts = new Array<NodePart>(2);
	
	/**
	 * Calculates the local transform based on the translation, scale and rotation
	 * @return the local transform
	 */
	public Matrix4 calculateLocalTransform() {
		if (!isAnimated)
			localTransform.set(translation, rotation, scale);
		return localTransform;
	}

	/**
	 * Calculates the world transform; the product of local transform and the
	 * parent's world transform. 
	 * @return the world transform
	 */
	public Matrix4 calculateWorldTransform() {
		if (parent == null) 
			globalTransform.set(localTransform);
		else
			globalTransform.set(parent.globalTransform).mul(localTransform);
		return globalTransform;
	}
	
	/**
	 * Calculates the local and world transform of this node and optionally all
	 * its children.
	 * 
	 * @param recursive whether to calculate the local/world transforms for children.
	 */
	public void calculateTransforms(boolean recursive) {
		calculateLocalTransform();
		calculateWorldTransform();
		
		if(recursive) {
			for(Node child: children) {
				child.calculateTransforms(true);
			}
		}
	}
	
	public void calculateBoneTransforms(boolean recursive) {
		for (final NodePart part : parts) {
			if (part.invBoneBindTransforms == null || part.bones == null || part.invBoneBindTransforms.size != part.bones.length)
				continue;
			final int n = part.invBoneBindTransforms.size;
			for (int i = 0; i < n; i++)
				part.bones[i].set(part.invBoneBindTransforms.keys[i].globalTransform).mul(part.invBoneBindTransforms.values[i]);
		}
		if(recursive) {
			for(Node child: children) {
				child.calculateBoneTransforms(true);
			}
		}
	}

	/** Calculate the bounding box of this Node.
	 * This is a potential slow operation, it is advised to cache the result. */
	public BoundingBox calculateBoundingBox(final BoundingBox out) {
		out.inf();
		return extendBoundingBox(out);
	}
	
	/** Calculate the bounding box of this Node.
	 * This is a potential slow operation, it is advised to cache the result. */
	public BoundingBox calculateBoundingBox(final BoundingBox out, boolean transform) {
		out.inf();
		return extendBoundingBox(out, transform);
	}

	/** Extends the bounding box with the bounds of this Node.
	 * This is a potential slow operation, it is advised to cache the result. */
	public BoundingBox extendBoundingBox(final BoundingBox out) {
		return extendBoundingBox(out, true);
	}
	
	/** Extends the bounding box with the bounds of this Node.
	 * This is a potential slow operation, it is advised to cache the result. */
	public BoundingBox extendBoundingBox(final BoundingBox out, boolean transform) {
		final int partCount = parts.size;
		for (int i = 0; i < partCount; i++) {
			final MeshPart meshPart = parts.get(i).meshPart;
			if (transform)
				meshPart.mesh.extendBoundingBox(out, meshPart.indexOffset, meshPart.numVertices, globalTransform);
			else
				meshPart.mesh.extendBoundingBox(out, meshPart.indexOffset, meshPart.numVertices);
		}
		final int childCount = children.size;
		for (int i = 0; i < childCount; i++)
			children.get(i).extendBoundingBox(out);
		return out;
	}
	
	/** @param recursive false to fetch a root child only, true to search the entire node tree for the specified node.
	 * @return The node with the specified id, or null if not found. */
	public Node getChild(final String id, boolean recursive, boolean ignoreCase) {
		return getNode(children, id, recursive, ignoreCase);
	}
	
	/** Helper method to recursive fetch a node from an array
	 * @param recursive false to fetch a root node only, true to search the entire node tree for the specified node.
	 * @return The node with the specified id, or null if not found. */
	public static Node getNode(final Array<Node> nodes, final String id, boolean recursive, boolean ignoreCase) {
		final int n = nodes.size;
		Node node;
		if (ignoreCase) {
			for (int i = 0; i < n; i++)
				if ((node = nodes.get(i)).id.equalsIgnoreCase(id))
					return node;
		} else {
			for (int i = 0; i < n; i++)
				if ((node = nodes.get(i)).id.equals(id))
					return node;
		}
		if (recursive) {
			for (int i = 0; i < n; i++)
				if ((node = getNode(nodes.get(i).children, id, true, ignoreCase)) != null)
					return node;
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1053.java