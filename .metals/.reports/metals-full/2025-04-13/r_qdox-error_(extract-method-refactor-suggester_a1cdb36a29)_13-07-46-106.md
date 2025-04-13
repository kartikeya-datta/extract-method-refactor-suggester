error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/805.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/805.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/805.java
text:
```scala
i@@f (body != null) body.dispose();

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
package com.badlogic.gdx.tests.bullet;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.model.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.btCollisionObject;
import com.badlogic.gdx.physics.bullet.btCollisionShape;
import com.badlogic.gdx.physics.bullet.btMotionState;
import com.badlogic.gdx.physics.bullet.btRigidBody;
import com.badlogic.gdx.physics.bullet.btRigidBodyConstructionInfo;
import com.badlogic.gdx.utils.Disposable;

/** @author xoppa
 * Renderable BaseEntity with a bullet physics body. 
 */
public class BulletEntity extends BaseEntity {
	private final static Matrix4 tmpM = new Matrix4();
	public BulletEntity.MotionState motionState;
	public btCollisionObject body;

	public BulletEntity (final Model model, final btRigidBodyConstructionInfo bodyInfo, final float x, final float y, final float z) {
		this(model, bodyInfo == null ? null : new btRigidBody(bodyInfo), x, y, z);
	}
	
	public BulletEntity (final Model model, final btRigidBodyConstructionInfo bodyInfo, final Matrix4 transform) {
		this(model, bodyInfo == null ? null : new btRigidBody(bodyInfo), transform);
	}
	
	public BulletEntity (final Model model, final btCollisionObject body, final float x, final float y, final float z) {
		this(model, body, tmpM.setToTranslation(x, y, z));
	}
	
	public BulletEntity (final Model model, final btCollisionObject body, final Matrix4 transform) {
		this.model = model;
		this.transform.set(transform);
		this.body = body;
		
		if (body != null) {
			body.userData = this;
			if (body instanceof btRigidBody) {
				this.motionState = new MotionState(this.transform);
				((btRigidBody)this.body).setMotionState(motionState);
			} else
				body.setWorldTransform(transform);
		}
	}

	@Override
	public void dispose () {
		// Don't rely on the GC
		if (motionState != null) motionState.dispose();
		if (body != null) body.delete();
		// And remove the reference
		motionState = null;
		body = null;
	}
	
	static class MotionState extends btMotionState implements Disposable {
		private final Matrix4 transform;
		
		public MotionState(final Matrix4 transform) {
			this.transform = transform;
		}
		
		/**
		 * For dynamic and static bodies this method is called by bullet once to get the initial state of the body.
		 * For kinematic bodies this method is called on every update, unless the body is deactivated.
		 */
		@Override
		public void getWorldTransform (final Matrix4 worldTrans) {
			worldTrans.set(transform);
		}

		/**
		 * For dynamic bodies this method is called by bullet every update to inform about the new position and rotation.
		 */
		@Override
		public void setWorldTransform (final Matrix4 worldTrans) {
			transform.set(worldTrans);
		}
		
		@Override
		public void dispose () {
			delete();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/805.java