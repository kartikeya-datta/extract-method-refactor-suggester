error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/408.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/408.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/408.java
text:
```scala
i@@nt mode = 1;

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.IndexBufferObjectSubData;
import com.badlogic.gdx.graphics.glutils.VertexArray;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.graphics.glutils.VertexBufferObjectSubData;
import com.badlogic.gdx.graphics.glutils.VertexData;
import com.badlogic.gdx.tests.utils.GdxTest;

public class VBOVATest extends GdxTest {

	static final int TRIANGLES = 2000;
	VertexBufferObject vbo;
	VertexBufferObjectSubData vbosd;
	IndexBufferObject ibo;
	IndexBufferObjectSubData ibosd;
	IndexBufferObject vaibo;
	VertexArray va;
	VertexData vertexBuffer;
	float[] vertices;
	short[] indices;
	int mode = 0;
	long startTime = 0;
	int frames = 0;
	boolean isStatic = false;
	
	@Override
	public void create() {
		VertexAttribute[] attributes = { new VertexAttribute(Usage.Position, 3, "a_pos")};
		vbo = new VertexBufferObject(false, TRIANGLES*3, attributes);
		vbosd = new VertexBufferObjectSubData(false, TRIANGLES*3, attributes);
		ibo = new IndexBufferObject(false, TRIANGLES*3);
		ibosd = new IndexBufferObjectSubData(false, TRIANGLES*3);
		vaibo = new IndexBufferObject(false, TRIANGLES*3);
		
		va = new VertexArray(TRIANGLES*3, attributes);
		vertices = new float[TRIANGLES*3*3];
		indices = new short[TRIANGLES*3];
		
		int len = vertices.length;
		float col = Color.WHITE.toFloatBits();
		for(int i = 0; i < len; i+=9) {
			float x = (float)Math.random() * 2 - 1f;
			float y = (float)Math.random() * 2 - 1f;
			vertices[i+0] = -.01f + x; vertices[i+1] = -.01f + y; vertices[i+2] = 0;
			vertices[i+3] = .01f + x; vertices[i+4] = -.01f + y; vertices[i+5] = 0;
			vertices[i+6] = 0f + x; vertices[i+7] = .01f + y; vertices[i+8] = 0;
		}
		
		len = indices.length;
		for(int i = 0; i < len; i++ ) {
			indices[i] = (short)i;
		}
		
		startTime = System.nanoTime();
	}
	
	@Override
	public void render() {
		Gdx.gl11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		switch(mode) {
		case 0:
		case 3:
			vertexBuffer = vbo;
			Gdx.gl11.glColor4f(1, 0, 0, 1);
			break;
		case 1:
		case 4:
			vertexBuffer = vbosd;
			Gdx.gl11.glColor4f(0, 1, 0, 1);
			break;
		case 2:
		case 5:
			vertexBuffer = va;
			Gdx.gl11.glColor4f(0, 0, 1, 1);
			break;
		}
		
		for(int i=0; i < 5; i++ ) {
			vertexBuffer.bind();
			if(mode==3) {
				ibo.bind();
				if(!isStatic) 
					ibo.setIndices(indices, 0, indices.length);
			}
			if(mode==4) {
				ibosd.bind();
				if(!isStatic)
					ibosd.setIndices(indices, 0, indices.length);
			}
			if(mode==5) {
				vaibo.setIndices(indices, 0, indices.length);
			}
			
			if(!isStatic)
				vertexBuffer.setVertices(vertices, 0, vertices.length);
			
			if(mode<=2) {
				Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLES, 0, TRIANGLES*3);
			}
			else {
				if(mode>4)
					Gdx.gl11.glDrawElements(GL11.GL_TRIANGLES,TRIANGLES*3, GL11.GL_UNSIGNED_SHORT, ibo.getBuffer());
				else
					Gdx.gl11.glDrawElements(GL11.GL_TRIANGLES,TRIANGLES*3, GL11.GL_UNSIGNED_SHORT, 0);
			}
			if(mode==3)
				ibo.unbind();
			if(mode==4)
				ibosd.unbind();
			vertexBuffer.unbind();
		}
		
		long endTime = System.nanoTime();
		if(endTime-startTime >= 4000000000l ) {
			double secs = (endTime-startTime)/1000000000.0;
			double fps = frames / secs;
			Gdx.app.log("VBOVATest", vertexBuffer.getClass().getSimpleName() + ", " + isStatic + ", " + (mode>2) + ", " + fps);
			mode++;
			if( mode > 5) {
				mode = 0;
				isStatic = !isStatic;
			}
			startTime = System.nanoTime();
			frames = 0;
		}
	
		frames++;
	}
	
	@Override
	public void resume() {
		vbo.invalidate();
		vbosd.invalidate();
		ibo.invalidate();
		ibosd.invalidate();
	}
	
	
	@Override
	public boolean needsGL20() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/408.java