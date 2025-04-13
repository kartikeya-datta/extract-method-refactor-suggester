error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8710.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8710.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8710.java
text:
```scala
p@@osition.y = y;

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.tests.utils.GdxTest;

public class SimpleAnimationTest extends GdxTest implements InputProcessor {

	@Override public boolean needsGL20 () {
		return false;
	}

   private Animation currentWalk;
   private float currentFrameTime;
   private Vector2 position;
   
   private Texture tex;
   
   private Animation downWalk;
   private Animation leftWalk;
   private Animation rightWalk;
   private Animation upWalk;
   
   private SpriteBatch spriteBatch;
   
   private static final float ANIMATION_SPEED = 0.2f;
   
   @Override
   public void create()
   {
      Gdx.input.setInputProcessor(this);
      tex = new Texture(Gdx.files.internal("data/animation.png"));
      TextureRegion[][] regions = TextureRegion.split(tex, 32, 48);
      TextureRegion[] downWalkReg = regions[0];
      TextureRegion[] leftWalkReg = regions[1];
      TextureRegion[] rightWalkReg = regions[2];
      TextureRegion[] upWalkReg = regions[3];
      downWalk = new Animation(ANIMATION_SPEED, downWalkReg);
      leftWalk = new Animation(ANIMATION_SPEED, leftWalkReg);
      rightWalk = new Animation(ANIMATION_SPEED, rightWalkReg);
      upWalk = new Animation(ANIMATION_SPEED, upWalkReg);
      
      currentWalk = leftWalk;
      currentFrameTime = 0.0f;
      
      spriteBatch = new SpriteBatch();
      position = new Vector2();
   }

   @Override
   public void render() 
   {
      Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
      currentFrameTime += Gdx.graphics.getDeltaTime();
      
      spriteBatch.begin();
      TextureRegion frame = currentWalk.getKeyFrame(currentFrameTime, true);
      spriteBatch.draw(frame, position.x, position.y);
      spriteBatch.end();
   }


   @Override
   public boolean touchDown(int x, int y, int pointer, int button) {
      position.x = x;
      position.y = -y + 48;
      //System.out.println(position);
      return true;
   }

	@Override public boolean keyDown (int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public boolean keyTyped (char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public boolean touchUp (int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public boolean touchDragged (int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public boolean touchMoved (int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public boolean scrolled (int amount) {
		// TODO Auto-generated method stub
		return false;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8710.java