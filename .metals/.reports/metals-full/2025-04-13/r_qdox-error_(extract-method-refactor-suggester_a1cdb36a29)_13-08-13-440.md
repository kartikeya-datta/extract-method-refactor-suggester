error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8227.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8227.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8227.java
text:
```scala
R@@epeat

/*******************************************************************************
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com)
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
package com.badlogic.gdx.graphics;

/**
 * <p>A Texture wraps a standard OpenGL ES texture.</p> 
 *  
 * <p>It is constructed by an {@link Graphics} via one of the
 * {@link Graphics.newTexture()} methods.</p>
 * 
 * <p>A Texture can be managed. If the OpenGL context is lost
 * all textures get invalidated. This happens when a user switches
 * to another application or receives an incoming call. Managed
 * textures get reloaded automatically. 
 * </p>
 * 
 * <p>
 * A Texture has to be bound via the {@link Texture.bind()} method in order for it to be applied to geometry.
 * </p>
 * 
 * <p> 
 * You can draw {@link Pixmap}s to a texture at any time. The changes will be automatically uploaded to
 * texture memory. This is of course not extremely fast so use it with care.
 * </p>
 * 
 * <p>
 * A Texture must be disposed when it is no longer used
 * </p>
 * 
 * @author badlogicgames@gmail.com
 *
 */
public interface Texture 
{
	/**
	 * Texture filter enum featuring the 3 most used filters.
	 * @author badlogicgames@gmail.com
	 *
	 */
	public enum TextureFilter
	{
		Nearest,
		Linear,
		MipMap
	}
	
	/**
	 * Texture wrap enum
	 * 
	 * @author badlogicgames@gmail.com
	 *
	 */
	public enum TextureWrap
	{
		ClampToEdge,
		Wrap
	}
	
	/**
	 * Binds this texture. You have to enable texturing via
	 * {@link Application.enable( RenderState.Texturing )} in 
	 * order for the texture to actually be applied to geometry.
	 */
	public void bind( );

	/**
	 * Draws the given {@link Pixmap} to the texture at position x, y.
	 * 
	 * @param pixmap The Pixmap
	 * @param x The x coordinate in pixels
	 * @param y The y coordinate in pixels
	 */
	public void draw( Pixmap pixmap, int x, int y );
	
	/**
	 * 
	 * @return the width of the texture in pixels
	 */
	public int getWidth( );
	
	/**
	 * 
	 * @return the height of the texture in pixels
	 */
	public int getHeight( );

	/**
	 * @return whether this texture is managed or not.
	 */
	public boolean isManaged( );
	
	/**
	 * Disposes all resources associated with the texture
	 * @return
	 */
	public void dispose( );
	
	/**
	 * @return the OpenGL texture object handle so you can change texture parameters.
	 */
	public int getTextureObjectHandle( );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8227.java