error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4047.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4047.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4047.java
text:
```scala
M@@ethod mMul = ClassReflection.getMethod(Vector2.class, "scl", float.class);

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

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.reflect.ArrayReflection;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.Method;

/** Performs some tests with {@link ClassReflection} and prints the results on the screen.
 * @author hneuer */
public class ReflectionTest extends GdxTest {
	String message = "";
	BitmapFont font;
	SpriteBatch batch;

	@Override
	public void create () {
		font = new BitmapFont();
		batch = new SpriteBatch();

		try {
			Vector2 fromDefaultConstructor = ClassReflection.newInstance(Vector2.class);
			println("From default constructor: " + fromDefaultConstructor);

			Method mSet = ClassReflection.getMethod(Vector2.class, "set", float.class, float.class);
			mSet.invoke(fromDefaultConstructor, 10, 11);
			println("Set to 10/11: " + fromDefaultConstructor);

			Constructor copyConstroctor = ClassReflection.getConstructor(Vector2.class, Vector2.class);
			Vector2 fromCopyConstructor = (Vector2)copyConstroctor.newInstance(fromDefaultConstructor);
			println("From copy constructor: " + fromCopyConstructor);

			Method mMul = ClassReflection.getMethod(Vector2.class, "mul", float.class);
			println("Multiplied by 2; " + mMul.invoke(fromCopyConstructor, 2));

			Method mNor = ClassReflection.getMethod(Vector2.class, "nor");
			println("Normalized: " + mNor.invoke(fromCopyConstructor));

			Vector2 fieldCopy = new Vector2();
			Field fx = ClassReflection.getField(Vector2.class, "x");
			Field fy = ClassReflection.getField(Vector2.class, "y");
			fx.set(fieldCopy, fx.get(fromCopyConstructor));
			fy.set(fieldCopy, fy.get(fromCopyConstructor));
			println("Copied field by field: " + fieldCopy);

			Json json = new Json();
			String jsonString = json.toJson(fromCopyConstructor);
			Vector2 fromJson = json.fromJson(Vector2.class, jsonString);
			println("JSON serialized: " + jsonString);
			println("JSON deserialized: " + fromJson);
			fromJson.x += 1;
			fromJson.y += 1;
			println("JSON deserialized + 1/1: " + fromJson);

			Object array = ArrayReflection.newInstance(int.class, 5);
			ArrayReflection.set(array, 0, 42);
			println("Array int: length=" + ArrayReflection.getLength(array) + ", access=" + ArrayReflection.get(array, 0));

			array = ArrayReflection.newInstance(String.class, 5);
			ArrayReflection.set(array, 0, "test string");
			println("Array String: length=" + ArrayReflection.getLength(array) + ", access=" + ArrayReflection.get(array, 0));
		} catch (Exception e) {
			message = "FAILED: " + e.getMessage() + "\n";
			message += e.getClass();
		}
	}

	private void println (String line) {
		message += line + "\n";
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		font.drawMultiLine(batch, message, 20, Gdx.graphics.getHeight() - 20);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4047.java