error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6794.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6794.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6794.java
text:
```scala
i@@f(time < 0 || time > anim.totalDuration) throw new IllegalArgumentException("time must be 0 <= time <= animation duration");

package com.badlogic.gdx.graphics.g3d.model.skeleton;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Skeleton {
	/** each joint is a root joint in the hierachy **/
	public final Array<SkeletonJoint> hierarchy = new Array<SkeletonJoint>();
	/** the names of each joint in breadth first order **/
	public final Array<String> jointNames = new Array<String>();
	/** names to indices **/
	public final Map<String, Integer> namesToIndices = new HashMap<String, Integer>();
	/** the bind pose joints in breadth first order **/
	public final Array<SkeletonKeyframe> bindPoseJoints = new Array<SkeletonKeyframe>();
	/** the joints in breadth first order for the last calculates animation pose **/
	public final Array<SkeletonKeyframe> animPoseJoints = new Array<SkeletonKeyframe>();
	/** the offset matrices for each joint in the same order as the bindPoseJoints **/
	public final Array<Matrix4> offsetMatrices = new Array<Matrix4>();
	/** the scene matrices for each joint in the same order as bindPoseJoints **/
	public final Array<Matrix4> sceneMatrices = new Array<Matrix4>();
	/** combined scene and offset matrices **/
	public final Array<Matrix4> combinedMatrices = new Array<Matrix4>();
	/** map of animations, indexed by name **/
	public final ObjectMap<String, SkeletonAnimation> animations = new ObjectMap<String, SkeletonAnimation>();
	
	private static final Matrix4 IDENTITY = new Matrix4();
	private final Matrix4 rotMatrix = new Matrix4();
	
	/** 
	 * Fills the baseJoints, offsetMatrices and sceneMatrices Array instances
	 * with joints and Matrix4 instances in an breadth first order. This allows
	 * one to iterate over the joint arrays instead of recursing over the hierarchy
	 * when calculating the scene matrices.
	 */
	public void buildFromHierarchy () {
		jointNames.clear();
		namesToIndices.clear();
		bindPoseJoints.clear();
		animPoseJoints.clear();
		offsetMatrices.clear();
		sceneMatrices.clear();
		
		for(int i = 0; i < hierarchy.size; i++) {
			recursiveFill(hierarchy.get(i));
		}
				
		calculateMatrices(bindPoseJoints);
		calculateOffsetMatrices();
	}
	
	private void recursiveFill(SkeletonJoint joint) {
		joint.index = bindPoseJoints.size;
		joint.parentIndex = joint.parent != null? joint.parent.index: -1;
		
		SkeletonKeyframe keyFrame = new SkeletonKeyframe();
		keyFrame.position.set(joint.position);
		keyFrame.scale.set(joint.scale);
		keyFrame.rotation.set(joint.rotation);
		keyFrame.parentIndex = joint.parentIndex;
		
		jointNames.add(joint.name);
		namesToIndices.put(joint.name, joint.index);
		bindPoseJoints.add(keyFrame);
		SkeletonKeyframe animKeyframe = new SkeletonKeyframe();
		animKeyframe.parentIndex = joint.parentIndex;
		animPoseJoints.add(animKeyframe);
		offsetMatrices.add(new Matrix4());
		sceneMatrices.add(new Matrix4());
		combinedMatrices.add(new Matrix4());
		
		int len = joint.children.size;
		for(int i = 0; i < len; i++) {
			recursiveFill(joint.children.get(i));
		}
	}
	
	protected void calculateOffsetMatrices() {
		for(int i = 0; i < offsetMatrices.size; i++) {
			offsetMatrices.get(i).set(sceneMatrices.get(i)).inv();
		}
	}
	
	protected void calculateMatrices(Array<SkeletonKeyframe> joints) {
		for(int i = 0; i < joints.size; i++) {			
			SkeletonKeyframe joint = joints.get(i);
			Matrix4 sceneMatrix = sceneMatrices.get(i);
			Matrix4 parentMatrix = joint.parentIndex != -1? sceneMatrices.get(joint.parentIndex): IDENTITY;
			Matrix4 combinedMatrix = combinedMatrices.get(i);
			
			joint.rotation.toMatrix(rotMatrix.val);	
			rotMatrix.trn(joint.position);
			rotMatrix.scl(joint.scale);
			sceneMatrix.set(parentMatrix);			
			sceneMatrix.mul(rotMatrix);
			
			combinedMatrix.set(sceneMatrix);
			combinedMatrix.mul(offsetMatrices.get(i));
		}
	}
	
	public void setAnimation(String name, float time) {		
		SkeletonAnimation anim = animations.get(name);
		if(anim == null) throw new IllegalArgumentException("Animation with name '" + name + "' does not exist");
		if(time < 0 || time > anim.duration) throw new IllegalArgumentException("time must be 0 <= time <= animation duration");
		
		int len = anim.perJointkeyFrames.length;
		for(int i = 0; i < len; i++) {
			SkeletonKeyframe[] jointTrack = anim.perJointkeyFrames[i];
			int idx = 0;			
			int len2 = jointTrack.length;
			for(int j = 0; j < len2; j++) {
				SkeletonKeyframe jointFrame = jointTrack[j];
				if(jointFrame.timeStamp >= time) {
					idx = Math.max(0, j - 1);
					break;
				}
			}
			
			SkeletonKeyframe startFrame = jointTrack[idx];
			SkeletonKeyframe endFrame = idx + 1 == len2? startFrame: jointTrack[idx+1];
			float alpha = 0; 
			
			if(startFrame != endFrame) {
				alpha = Math.min(1, (time - startFrame.timeStamp) / (endFrame.timeStamp - startFrame.timeStamp));
			}
			SkeletonKeyframe animFrame = animPoseJoints.get(i);			
			animFrame.position.set(startFrame.position).lerp(endFrame.position, alpha);
			animFrame.scale.set(startFrame.scale).lerp(endFrame.scale, alpha);
			animFrame.rotation.set(startFrame.rotation).slerp(endFrame.rotation, alpha);				
		}
		
		calculateMatrices(animPoseJoints);
	}
	
	public void setBindPose() {
		calculateMatrices(bindPoseJoints);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6794.java