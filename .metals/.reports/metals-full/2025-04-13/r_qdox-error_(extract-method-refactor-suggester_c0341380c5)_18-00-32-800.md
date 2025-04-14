error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1712.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1712.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[18,1]

error in qdox parser
file content:
```java
offset: 816
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1712.java
text:
```scala
public class CullTest extends GdxTest implements ApplicationListener {

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
p@@ackage com.badlogic.gdx.tests;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.math.Vector3;

public class CullTest implements ApplicationListener {

   public boolean needsGL20() {
      return false;
   }

   Mesh sphere;
   Camera cam;
   SpriteBatch batch;
   BitmapFont font;
   Vector3[] positions = new Vector3[100];
   
   @Override public void create() {
      sphere = ObjLoader.loadObj(Gdx.files.internal("data/sphere.obj").read());
      //cam = new PerspectiveCamera(45, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      cam = new OrthographicCamera(45, 45 * (Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight()) );
      
      cam.near = 1;
      cam.far = 200;
      
      Random rand = new Random();
      for(int i = 0; i < positions.length; i++) {
         positions[i] = new Vector3(rand.nextFloat() * 100 - rand.nextFloat() * 100, 
                                    rand.nextFloat() * 100 - rand.nextFloat() * 100, 
                                    rand.nextFloat() * -100 - 3);
      }
      batch = new SpriteBatch();
      font = new BitmapFont();
      //Gdx.graphics.setVSync(true);
      //Gdx.app.log("CullTest", "" + Gdx.graphics.getBufferFormat().toString());
   }
   
   @Override public void render() {
      GL10 gl = Gdx.gl10;
      
      gl.glClearColor(0, 0, 0, 0);
      gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
      gl.glEnable(GL10.GL_DEPTH_TEST);
      
      cam.update();
      cam.apply(gl);
      
      int visible = 0;
      for(int i = 0; i < positions.length; i++) {
         if(cam.frustum.sphereInFrustum(positions[i], 1)) {
            gl.glColor4f(1, 1, 1, 1);
            visible++;
         }
         else {
            gl.glColor4f(1, 0, 0, 1);
         }
         gl.glPushMatrix();
         gl.glTranslatef(positions[i].x, positions[i].y, positions[i].z);
         sphere.render(GL10.GL_TRIANGLES);
         gl.glPopMatrix();
      }
      
      if(Gdx.input.isKeyPressed(Keys.A))
         cam.rotate(20 * Gdx.graphics.getDeltaTime(), 0, 1, 0);
      if(Gdx.input.isKeyPressed(Keys.D))
         cam.rotate(-20 * Gdx.graphics.getDeltaTime(), 0, 1, 0);      
      
      gl.glDisable(GL10.GL_DEPTH_TEST);
      batch.begin();
      font.draw(batch, "visible: " + visible + "/100" + ", fps: " + Gdx.graphics.getFramesPerSecond(), 0, 20);
      batch.end();
   }

   @Override
   public void dispose() {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void pause() {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void resize(int width, int height) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void resume() {
      // TODO Auto-generated method stub
      
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1712.java