error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5272.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5272.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5272.java
text:
```scala
r@@eturn modelBuilder.createCylinder(radius * 2, hh * 2f, radius * 2f, 16, new NewMaterial(new ColorAttribute(ColorAttribute.Diffuse, Color.WHITE)), new VertexAttributes(new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE)));

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

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.NewMaterial;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.btCollisionObject;
import com.badlogic.gdx.physics.bullet.btConeTwistConstraint;
import com.badlogic.gdx.physics.bullet.btConstraintSetting;
import com.badlogic.gdx.physics.bullet.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.btHingeConstraint;
import com.badlogic.gdx.physics.bullet.btPoint2PointConstraint;
import com.badlogic.gdx.physics.bullet.btRigidBody;
import com.badlogic.gdx.physics.bullet.btTypedConstraint;
import com.badlogic.gdx.physics.bullet.btVector3;
import com.badlogic.gdx.physics.bullet.gdxBullet;
import com.badlogic.gdx.utils.Array;

/** @author xoppa */
public class RayPickRagdollTest extends BaseBulletTest {
	
	final Array<btTypedConstraint> constraints = new Array<btTypedConstraint>();
	btPoint2PointConstraint pickConstraint = null;
	btRigidBody pickedBody = null;
	float pickDistance;
	
	@Override
	public void create () {
		super.create();
		instructions = "Tap to shoot\nDrag ragdoll to pick\nLong press to toggle debug mode\nSwipe for next test";
		
		camera.position.set(4f, 2f, 4f);
		camera.lookAt(0f, 1f, 0f);
		camera.update();
		
		world.addConstructor("pelvis", new BulletConstructor(createCapsuleModel(0.15f, 0.2f), 1f, new btCapsuleShape(0.15f, 0.2f)));
		world.addConstructor("spine", new BulletConstructor(createCapsuleModel(0.15f, 0.28f), 1f, new btCapsuleShape(0.15f, 0.28f)));
		world.addConstructor("head", new BulletConstructor(createCapsuleModel(0.1f, 0.05f), 1f, new btCapsuleShape(0.1f, 0.05f)));
		world.addConstructor("upperleg", new BulletConstructor(createCapsuleModel(0.07f, 0.45f), 1f, new btCapsuleShape(0.07f, 0.45f)));
		world.addConstructor("lowerleg", new BulletConstructor(createCapsuleModel(0.05f, 0.37f), 1f, new btCapsuleShape(0.05f, 0.37f)));
		world.addConstructor("upperarm", new BulletConstructor(createCapsuleModel(0.05f, 0.33f), 1f, new btCapsuleShape(0.05f, 0.33f)));
		world.addConstructor("lowerarm", new BulletConstructor(createCapsuleModel(0.04f, 0.25f), 1f, new btCapsuleShape(0.04f, 0.25f)));
		
		world.add("ground", 0f, 0f, 0f)
			.setColor(0.25f + 0.5f * (float)Math.random(), 0.25f + 0.5f * (float)Math.random(), 0.25f + 0.5f * (float)Math.random(), 1f);
		
		addRagdoll(0, 3f, 0);
		addRagdoll(1f, 6f, 0);
		addRagdoll(-1f, 12f, 0);
	}
	
	@Override
	public void dispose () {
		for (int i = 0; i < constraints.size; i++) {
			((btDynamicsWorld)world.collisionWorld).removeConstraint(constraints.get(i));
			constraints.get(i).delete();
		}
		constraints.clear();
		super.dispose();
	}	
	
	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		boolean result = false;
		if (button == Buttons.LEFT) {
			Ray ray = camera.getPickRay(screenX, screenY);
			Vector3.tmp.set(ray.direction).mul(10f).add(ray.origin);
			ClosestRayResultCallback cb = new ClosestRayResultCallback(ray.origin, Vector3.tmp);
			world.collisionWorld.rayTest(ray.origin, Vector3.tmp, cb);
			if (cb.hasHit()) {
				btRigidBody body = btRigidBody.upcast(cb.getM_collisionObject());
				if (body != null && !body.isStaticObject() && !body.isKinematicObject()) {
					pickedBody = body;
					body.setActivationState(gdxBullet.DISABLE_DEACTIVATION);
					
					btVector3 hitpoint = cb.getM_hitPointWorld();
					Vector3.tmp.set(hitpoint.getX(), hitpoint.getY(), hitpoint.getZ());
					Vector3.tmp.mul(body.getCenterOfMassTransform().inv());
					
					pickConstraint = new btPoint2PointConstraint(body,Vector3.tmp);
					btConstraintSetting setting = pickConstraint.getM_setting();
					setting.setM_impulseClamp(30f);
					setting.setM_tau(0.001f);
					pickConstraint.setM_setting(setting);
					
					((btDynamicsWorld)world.collisionWorld).addConstraint(pickConstraint);
		
					pickDistance = Vector3.tmp.sub(camera.position).len();
					result = true;
				}
			}
			cb.delete();
		}
		return result ? result : super.touchDown(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		boolean result = false;
		if (button == Buttons.LEFT) {
			if (pickConstraint != null) {
				((btDynamicsWorld)world.collisionWorld).removeConstraint(pickConstraint);
				pickConstraint.delete();
				pickConstraint = null;
				result = true;
			}
			if (pickedBody != null) {
				pickedBody.forceActivationState(gdxBullet.ACTIVE_TAG);
				pickedBody.setDeactivationTime(0f);
				pickedBody = null;
			}
		}
		return result ? result : super.touchUp(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		boolean result = false;
		if (pickConstraint != null) {
			Ray ray = camera.getPickRay(screenX, screenY);
			Vector3.tmp.set(ray.direction).mul(pickDistance).add(camera.position);
			pickConstraint.setPivotB(Vector3.tmp);
			result = true;
		}
		return result ? result : super.touchDragged(screenX, screenY, pointer);
	}
	
	@Override
	public boolean tap (float x, float y, int count, int button) {
		shoot(x, y);
		return true;
	}
	
	final static float PI = MathUtils.PI;
	final static float PI2 = 0.5f * PI;
	final static float PI4 = 0.25f * PI;
	public void addRagdoll(final float x, final float y, final float z) {
		final Matrix4 tmpM = new Matrix4();
		btRigidBody pelvis = (btRigidBody)world.add("pelvis", x, y+1, z).body;
		btRigidBody spine = (btRigidBody)world.add("spine", x, y+1.2f, z).body;
		btRigidBody head = (btRigidBody)world.add("head", x, y+1.6f, z).body;
		btRigidBody leftupperleg = (btRigidBody)world.add("upperleg", x-0.18f, y+0.65f, z).body;
		btRigidBody leftlowerleg = (btRigidBody)world.add("lowerleg", x-0.18f, y+0.2f, z).body;
		btRigidBody rightupperleg = (btRigidBody)world.add("upperleg", x+0.18f, y+0.65f, z).body;
		btRigidBody rightlowerleg = (btRigidBody)world.add("lowerleg", x+0.18f, y+0.2f, z).body;		
		btRigidBody leftupperarm = (btRigidBody)world.add("upperarm", tmpM.setFromEulerAngles(PI2, 0, 0).trn(x-0.35f, y+1.45f, z)).body;
		btRigidBody leftlowerarm = (btRigidBody)world.add("lowerarm", tmpM.setFromEulerAngles(PI2, 0, 0).trn(x-0.7f, y+1.45f, z)).body;
		btRigidBody rightupperarm = (btRigidBody)world.add("upperarm", tmpM.setFromEulerAngles(-PI2, 0, 0).trn(x+0.35f, y+1.45f, z)).body;
		btRigidBody rightlowerarm = (btRigidBody)world.add("lowerarm", tmpM.setFromEulerAngles(-PI2, 0, 0).trn(x+0.7f, y+1.45f, z)).body;

		final Matrix4 localA = new Matrix4();
		final Matrix4 localB = new Matrix4();
		btHingeConstraint hingeC = null;
		btConeTwistConstraint coneC = null;
		
		// PelvisSpine
		localA.setFromEulerAngles(0, PI2, 0).trn(0, 0.15f, 0);
		localB.setFromEulerAngles(0, PI2, 0).trn(0, -0.15f, 0);
		constraints.add(hingeC = new btHingeConstraint(pelvis, spine, localA, localB));
		hingeC.setLimit(-PI4, PI2);
		((btDynamicsWorld)world.collisionWorld).addConstraint(hingeC, true);
		
		// SpineHead
		localA.setFromEulerAngles(PI2, 0, 0).trn(0, 0.3f, 0);
		localB.setFromEulerAngles(PI2, 0, 0).trn(0, -0.14f, 0);
		constraints.add(coneC = new btConeTwistConstraint(spine, head, localA, localB));
		coneC.setLimit(PI4, PI4, PI2);
		((btDynamicsWorld)world.collisionWorld).addConstraint(coneC, true);
		
		// LeftHip
		localA.setFromEulerAngles(-PI4*5f, 0, 0).trn(-0.18f, -0.1f, 0);
		localB.setFromEulerAngles(-PI4*5f, 0, 0).trn(0, 0.225f, 0);
		constraints.add(coneC = new btConeTwistConstraint(pelvis, leftupperleg, localA, localB));
		coneC.setLimit(PI4, PI4, 0);
		((btDynamicsWorld)world.collisionWorld).addConstraint(coneC, true);
		
		// LeftKnee
		localA.setFromEulerAngles(0, PI2, 0).trn(0, -0.225f, 0);
		localB.setFromEulerAngles(0, PI2, 0).trn(0, 0.185f, 0);
		constraints.add(hingeC = new btHingeConstraint(leftupperleg, leftlowerleg, localA, localB));
		hingeC.setLimit(0, PI2);
		((btDynamicsWorld)world.collisionWorld).addConstraint(hingeC, true);
		
		// RightHip
		localA.setFromEulerAngles(-PI4*5f, 0, 0).trn(0.18f, -0.1f, 0);
		localB.setFromEulerAngles(-PI4*5f, 0, 0).trn(0, 0.225f, 0);
		constraints.add(coneC = new btConeTwistConstraint(pelvis, rightupperleg, localA, localB));
		coneC.setLimit(PI4, PI4, 0);
		((btDynamicsWorld)world.collisionWorld).addConstraint(coneC, true);
		
		// RightKnee
		localA.setFromEulerAngles(0, PI2, 0).trn(0, -0.225f, 0);
		localB.setFromEulerAngles(0, PI2, 0).trn(0, 0.185f, 0);
		constraints.add(hingeC = new btHingeConstraint(rightupperleg, rightlowerleg, localA, localB));
		hingeC.setLimit(0, PI2);
		((btDynamicsWorld)world.collisionWorld).addConstraint(hingeC, true);
		
		// LeftShoulder
		localA.setFromEulerAngles(PI, 0, 0).trn(-0.2f, 0.15f, 0);
		localB.setFromEulerAngles(PI2, 0, 0).trn(0, -0.18f, 0);
		constraints.add(coneC = new btConeTwistConstraint(pelvis, leftupperarm, localA, localB));
		coneC.setLimit(PI2, PI2, 0);
		((btDynamicsWorld)world.collisionWorld).addConstraint(coneC, true);
		
		// LeftElbow
		localA.setFromEulerAngles(0, PI2, 0).trn(0, 0.18f, 0);
		localB.setFromEulerAngles(0, PI2, 0).trn(0, -0.14f, 0);
		constraints.add(hingeC = new btHingeConstraint(leftupperarm, leftlowerarm, localA, localB));
		hingeC.setLimit(0, PI2);
		((btDynamicsWorld)world.collisionWorld).addConstraint(hingeC, true);
		
		// RightShoulder
		localA.setFromEulerAngles(PI, 0, 0).trn(0.2f, 0.15f, 0);
		localB.setFromEulerAngles(PI2, 0, 0).trn(0, -0.18f, 0);
		constraints.add(coneC = new btConeTwistConstraint(pelvis, rightupperarm, localA, localB));
		coneC.setLimit(PI2, PI2, 0);
		((btDynamicsWorld)world.collisionWorld).addConstraint(coneC, true);
		
		// RightElbow
		localA.setFromEulerAngles(0, PI2, 0).trn(0, 0.18f, 0);
		localB.setFromEulerAngles(0, PI2, 0).trn(0, -0.14f, 0);
		constraints.add(hingeC = new btHingeConstraint(rightupperarm, rightlowerarm, localA, localB));
		hingeC.setLimit(0, PI2);
		((btDynamicsWorld)world.collisionWorld).addConstraint(hingeC, true);
	}
	
	protected Model createCapsuleModel(float radius, float height) {
		final float hh = radius + 0.5f * height;
		// return ModelBuilder
		return ModelBuilder.createCylinder(radius * 2, hh * 2f, radius * 2f, 16, new NewMaterial(new ColorAttribute(ColorAttribute.Diffuse, Color.WHITE)), new VertexAttributes(new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE)));
		// return ModelBuilder.createBox(radius*2f, hh*2f, radius*2f, new NewMaterial(new ColorAttribute(ColorAttribute.Diffuse, Color.WHITE)));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5272.java