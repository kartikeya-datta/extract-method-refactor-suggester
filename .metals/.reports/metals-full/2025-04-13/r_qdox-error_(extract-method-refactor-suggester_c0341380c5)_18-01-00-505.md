error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8040.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8040.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8040.java
text:
```scala
public static f@@loat velocityThreshold = 1.0f;

/*******************************************************************************
 * Copyright (c) 2011, Daniel Murphy
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright notice,
 * 	  this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright notice,
 * 	  this list of conditions and the following disclaimer in the documentation
 * 	  and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

package org.jbox2d.common;

/**
 * Global tuning constants based on MKS units and various integer maximums (vertices per shape,
 * pairs, etc.).
 */
public class Settings {

  /** A "close to zero" float epsilon value for use */
  public static final float EPSILON = 1.1920928955078125E-7f;

  /** Pi. */
  public static final float PI = (float) Math.PI;

  // JBox2D specific settings
  /**
   * needs to be final, or will slow down math methods
   */
  public static final boolean FAST_MATH = true;
  public static final int CONTACT_STACK_INIT_SIZE = 10;
  public static final boolean SINCOS_LUT_ENABLED = true;
  /**
   * smaller the precision, the larger the table. If a small table is used (eg, precision is .006 or
   * greater), make sure you set the table to lerp it's results. Accuracy chart is in the MathUtils
   * source. Or, run the tests yourself in {@link SinCosTest}.</br> </br> Good lerp precision
   * values:
   * <ul>
   * <li>.0092</li>
   * <li>.008201</li>
   * <li>.005904</li>
   * <li>.005204</li>
   * <li>.004305</li>
   * <li>.002807</li>
   * <li>.001508</li>
   * <li>9.32500E-4</li>
   * <li>7.48000E-4</li>
   * <li>8.47000E-4</li>
   * <li>.0005095</li>
   * <li>.0001098</li>
   * <li>9.50499E-5</li>
   * <li>6.08500E-5</li>
   * <li>3.07000E-5</li>
   * <li>1.53999E-5</li>
   * </ul>
   */
  public static final float SINCOS_LUT_PRECISION = .00011f;
  public static final int SINCOS_LUT_LENGTH = (int) Math.ceil(Math.PI * 2 / SINCOS_LUT_PRECISION);
  /**
   * Use if the table's precision is large (eg .006 or greater). Although it is more expensive, it
   * greatly increases accuracy. Look in the MathUtils source for some test results on the accuracy
   * and speed of lerp vs non lerp. Or, run the tests yourself in {@link SinCosTest}.
   */
  public static final boolean SINCOS_LUT_LERP = false;


  // Collision

  /**
   * The maximum number of contact points between two convex shapes.
   */
  public static final int maxManifoldPoints = 2;

  /**
   * The maximum number of vertices on a convex polygon.
   */
  public static final int maxPolygonVertices = 8;

  /**
   * This is used to fatten AABBs in the dynamic tree. This allows proxies to move by a small amount
   * without triggering a tree adjustment. This is in meters.
   */
  public static final float aabbExtension = 0.1f;

  /**
   * This is used to fatten AABBs in the dynamic tree. This is used to predict the future position
   * based on the current displacement. This is a dimensionless multiplier.
   */
  public static final float aabbMultiplier = 2.0f;

  /**
   * A small length used as a collision and constraint tolerance. Usually it is chosen to be
   * numerically significant, but visually insignificant.
   */
  public static final float linearSlop = 0.005f;

  /**
   * A small angle used as a collision and constraint tolerance. Usually it is chosen to be
   * numerically significant, but visually insignificant.
   */
  public static final float angularSlop = (2.0f / 180.0f * PI);

  /**
   * The radius of the polygon/edge shape skin. This should not be modified. Making this smaller
   * means polygons will have and insufficient for continuous collision. Making it larger may create
   * artifacts for vertex collision.
   */
  public static final float polygonRadius = (2.0f * linearSlop);

  /** Maximum number of sub-steps per contact in continuous physics simulation. */
  public static final int maxSubSteps = 8;

  // Dynamics

  /**
   * Maximum number of contacts to be handled to solve a TOI island.
   */
  public static final int maxTOIContacts = 32;

  /**
   * A velocity threshold for elastic collisions. Any collision with a relative linear velocity
   * below this threshold will be treated as inelastic.
   */
  public static final float velocityThreshold = 1.0f;

  /**
   * The maximum linear position correction used when solving constraints. This helps to prevent
   * overshoot.
   */
  public static final float maxLinearCorrection = 0.2f;

  /**
   * The maximum angular position correction used when solving constraints. This helps to prevent
   * overshoot.
   */
  public static final float maxAngularCorrection = (8.0f / 180.0f * PI);

  /**
   * The maximum linear velocity of a body. This limit is very large and is used to prevent
   * numerical problems. You shouldn't need to adjust this.
   */
  public static final float maxTranslation = 2.0f;
  public static final float maxTranslationSquared = (maxTranslation * maxTranslation);

  /**
   * The maximum angular velocity of a body. This limit is very large and is used to prevent
   * numerical problems. You shouldn't need to adjust this.
   */
  public static final float maxRotation = (0.5f * PI);
  public static float maxRotationSquared = (maxRotation * maxRotation);

  /**
   * This scale factor controls how fast overlap is resolved. Ideally this would be 1 so that
   * overlap is removed in one time step. However using values close to 1 often lead to overshoot.
   */
  public static final float baumgarte = 0.2f;
  public static final float toiBaugarte = 0.75f;


  // Sleep

  /**
   * The time that a body must be still before it will go to sleep.
   */
  public static final float timeToSleep = 0.5f;

  /**
   * A body cannot sleep if its linear velocity is above this tolerance.
   */
  public static final float linearSleepTolerance = 0.01f;

  /**
   * A body cannot sleep if its angular velocity is above this tolerance.
   */
  public static final float angularSleepTolerance = (2.0f / 180.0f * PI);

  /**
   * Friction mixing law. Feel free to customize this. TODO djm: add customization
   * 
   * @param friction1
   * @param friction2
   * @return
   */
  public static final float mixFriction(float friction1, float friction2) {
    return MathUtils.sqrt(friction1 * friction2);
  }

  /**
   * Restitution mixing law. Feel free to customize this. TODO djm: add customization
   * 
   * @param restitution1
   * @param restitution2
   * @return
   */
  public static final float mixRestitution(float restitution1, float restitution2) {
    return restitution1 > restitution2 ? restitution1 : restitution2;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8040.java