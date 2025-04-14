error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3223.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3223.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3223.java
text:
```scala
M@@D5Test.class,

/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.badlogic.gdx.tests.utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.tests.AlphaTest;
import com.badlogic.gdx.tests.AudioDeviceTest;
import com.badlogic.gdx.tests.AudioRecorderTest;
import com.badlogic.gdx.tests.BitmapFontAlignmentTest;
import com.badlogic.gdx.tests.BitmapFontFlipTest;
import com.badlogic.gdx.tests.BitmapFontTest;
import com.badlogic.gdx.tests.BobTest;
import com.badlogic.gdx.tests.Box2DTest;
import com.badlogic.gdx.tests.Box2DTestCollection;
import com.badlogic.gdx.tests.FilesTest;
import com.badlogic.gdx.tests.FloatTest;
import com.badlogic.gdx.tests.FrameBufferTest;
import com.badlogic.gdx.tests.ImmediateModeRendererTest;
import com.badlogic.gdx.tests.IndexBufferObjectClassTest;
import com.badlogic.gdx.tests.IndexBufferObjectShaderTest;
import com.badlogic.gdx.tests.InputTest;
import com.badlogic.gdx.tests.IsometricTileTest;
import com.badlogic.gdx.tests.LifeCycleTest;
import com.badlogic.gdx.tests.MD5Test;
import com.badlogic.gdx.tests.ManagedTest;
import com.badlogic.gdx.tests.ManualBindTest;
import com.badlogic.gdx.tests.MeshMultitextureTest;
import com.badlogic.gdx.tests.MeshShaderTest;
import com.badlogic.gdx.tests.MeshTest;
import com.badlogic.gdx.tests.MultitouchTest;
import com.badlogic.gdx.tests.MyFirstTriangle;
import com.badlogic.gdx.tests.ObjTest;
import com.badlogic.gdx.tests.OrthoCamBorderTest;
import com.badlogic.gdx.tests.ParticleEmitterTest;
import com.badlogic.gdx.tests.PixelsPerInchTest;
import com.badlogic.gdx.tests.PixmapBlendingTest;
import com.badlogic.gdx.tests.Pong;
import com.badlogic.gdx.tests.SimpleTest;
import com.badlogic.gdx.tests.SoundTest;
import com.badlogic.gdx.tests.SpriteBatchRotationTest;
import com.badlogic.gdx.tests.SpriteBatchShaderTest;
import com.badlogic.gdx.tests.SpriteBatchTest;
import com.badlogic.gdx.tests.SpriteCacheOffsetTest;
import com.badlogic.gdx.tests.SpriteCacheTest;
import com.badlogic.gdx.tests.SpritePerformanceTest;
import com.badlogic.gdx.tests.StageTest;
import com.badlogic.gdx.tests.TerrainTest;
import com.badlogic.gdx.tests.TextureAtlasTest;
import com.badlogic.gdx.tests.TextureRenderTest;
import com.badlogic.gdx.tests.TileTest;
import com.badlogic.gdx.tests.UITest;
import com.badlogic.gdx.tests.VBOVATest;
import com.badlogic.gdx.tests.VertexArrayClassTest;
import com.badlogic.gdx.tests.VertexArrayTest;
import com.badlogic.gdx.tests.VertexBufferObjectClassTest;
import com.badlogic.gdx.tests.VertexBufferObjectShaderTest;
import com.badlogic.gdx.tests.VertexBufferObjectTest;
import com.badlogic.gdx.tests.WaterRipples;
import com.badlogic.gdx.tests.gles2.HelloTriangle;
import com.badlogic.gdx.tests.gles2.SimpleVertexShader;

/**
 * List of GdxTest classes. To be used by the test launchers.
 * If you write your own test, add it in here!
 * 
 * @author badlogicgames@gmail.com
 *
 */
public class GdxTests 
{
	public static final Class[] tests = {
		AlphaTest.class,
		AudioDeviceTest.class,
		AudioRecorderTest.class,
		BitmapFontAlignmentTest.class,
		BitmapFontFlipTest.class,
		BitmapFontTest.class,
		BobTest.class,
		Box2DTest.class,
		Box2DTestCollection.class,
		FilesTest.class,
		//FillrateTest.class,
		FloatTest.class,
		FrameBufferTest.class,
		ImmediateModeRendererTest.class,
		IndexBufferObjectClassTest.class,
		IndexBufferObjectShaderTest.class,
		InputTest.class,
		IsometricTileTest.class,
		LifeCycleTest.class,
		ManagedTest.class,
		ManualBindTest.class,
		//MD5Test.class,
		MeshMultitextureTest.class,
		MeshShaderTest.class,
		MeshTest.class,
		//Mpg123Test.class,
		MultitouchTest.class,
		MyFirstTriangle.class,
		ObjTest.class,
		OrthoCamBorderTest.class,
		ParticleEmitterTest.class,
		PixelsPerInchTest.class,
		PixmapBlendingTest.class,
		Pong.class,
		SimpleTest.class,
		SoundTest.class,
		SpriteCacheTest.class,
		SpriteCacheOffsetTest.class,
		SpriteBatchRotationTest.class,
		SpriteBatchShaderTest.class,
		SpriteBatchTest.class,
		SpritePerformanceTest.class,
		StageTest.class,
		TerrainTest.class,		
		TextureAtlasTest.class,
		TextureRenderTest.class,
		TileTest.class,
		UITest.class,
		VBOVATest.class,
		VertexArrayTest.class,		
		VertexBufferObjectTest.class,
		VertexArrayClassTest.class,
		VertexBufferObjectClassTest.class,
		VertexBufferObjectShaderTest.class,
		//VorbisTest.class,
		WaterRipples.class,
		HelloTriangle.class,		
		SimpleVertexShader.class
	};
	
	public static String[] getNames () {
		List<String> names = new ArrayList<String>( );
		for( Class clazz: tests )
			names.add(clazz.getSimpleName());
		return names.toArray(new String[names.size()]);
	}

	public static GdxTest newTest (String testName)	{
		try {
			Class clazz = Class.forName("com.badlogic.gdx.tests." + testName);
			return (GdxTest)clazz.newInstance();
		}
		catch( Exception ex ) {
			return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3223.java