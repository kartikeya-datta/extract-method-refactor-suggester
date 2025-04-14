error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2316.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2316.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2316.java
text:
```scala
r@@eturn FastMath.sqrt( sum / values.length );

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
package com.badlogic.gdx.math;

/**
 * A simple class keeping track
 * of the mean of a stream of values
 * within a certain window. the WindowedMean
 * will only return a value in case enough
 * data has been sampled. After enough
 * data has been sampled the oldest sample
 * will be replaced by the newest in case
 * a new sample is added.
 * 
 * @author badlogicgames@gmail.com
 *
 */
public final class WindowedMean 
{
	float values[];
	int added_values = 0;
	int last_value;	
	float mean = 0;
	boolean dirty = true;
	
	/**
	 * constructor, window_size specifies
	 * the number of samples we will continuously
	 * get the mean and variance from. the
	 * class will only return meaning full values
	 * if at least window_size values have been
	 * added.
	 * 
	 * @param window_size size of the sample window
	 */
	public WindowedMean( int window_size )
	{
		values = new float[window_size];		
	}
	
	/**
	 * @return wheter the value returned will be meaningfull
	 */
	public boolean hasEnoughData()
	{
		return added_values >= values.length;
	}
	
	/**
	 * clears this WindowedMean. The class will
	 * only return meaningfull values after enough
	 * data has been added again.
	 */
	public void clear( )
	{
		added_values = 0;
		last_value = 0;
		for( int i = 0; i < values.length; i++ )
			values[i] = 0;
		dirty = true;
	}
	
	/**
	 * adds a new sample to this mean. in case the
	 * window is full the oldest value will be replaced
	 * by this new value.
	 * 
	 * @param value The value to add
	 */
	public void addValue( float value )
	{
		added_values++;
		values[last_value++] = value;
		if( last_value > values.length - 1 )
			last_value = 0;
		dirty = true;
	}
	
	/**
	 * returns the mean of the samples added
	 * to this instance. only returns meaningfull
	 * results when at least window_size samples
	 * as specified in the constructor have been
	 * added.
	 * @return the mean
	 */
	public float getMean( )
	{
		if( hasEnoughData() )
		{
			if( dirty == true )
			{
				float mean = 0;			
				for( int i = 0; i < values.length; i++ )
					mean += values[i];
				
				this.mean = mean / values.length;				
				dirty = false;
			}
			return this.mean;
		}			
		else
			return 0;
	}	
	
	/**
	 * @return the oldest value in the window
	 */
	public float getOldest( )
	{
		return last_value == values.length - 1 ? values[0]: values[last_value+1];
	}

	/**
	 * @return the value last added
	 */
	public float getLatest() 
	{
		return values[last_value-1 == -1?values.length-1:last_value-1];
	}

	/**
	 * @return The standard deviation
	 */
	public float standardDeviation() 
	{
		if( !hasEnoughData() )
			return 0;
		
		float mean = getMean();
		float sum = 0;
		for( int i = 0; i < values.length; i++ )
		{
			sum += (values[i] - mean) * (values[i] - mean);
		}
		
		return (float)Math.sqrt( sum / values.length );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2316.java