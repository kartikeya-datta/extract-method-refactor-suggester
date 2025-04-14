error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/982.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/982.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/982.java
text:
```scala
D@@erby - Class org.apache.derby.iapi.util.ReuseFactory

/*

   Derby - Class com.ihost.cs.ReuseFactory

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derby.iapi.util;

/**
	Factory methods for reusable objects. So far, the objects allocated
	by this factory are all immutable. Any immutable object can be re-used.

	All the methods in this class are static.
*/
public class ReuseFactory {

	/** Private constructor so no instances can be made */
	private ReuseFactory() {
	}

	private static final Integer[] staticInts =
		{new Integer(0), new Integer(1), new Integer(2), new Integer(3),
		 new Integer(4), new Integer(5), new Integer(6), new Integer(7),
		 new Integer(8), new Integer(9), new Integer(10), new Integer(11),
		 new Integer(12), new Integer(13), new Integer(14), new Integer(15),
		 new Integer(16), new Integer(17), new Integer(18)};
	private static final Integer FIFTY_TWO = new Integer(52);
	private static final Integer TWENTY_THREE = new Integer(23);
	private static final Integer MAXINT = new Integer(Integer.MAX_VALUE);
	private static final Integer MINUS_ONE = new Integer(-1);

	public static Integer getInteger(int i)
	{
		if (i >= 0 && i < staticInts.length)
		{
			return staticInts[i];
		}
		else
		{
			// Look for other common values
			switch (i)
			{
			  case 23:
				return TWENTY_THREE;	// precision of Int

			  case 52:
				return FIFTY_TWO;	// precision of Double

			  case Integer.MAX_VALUE:
				return MAXINT;

			  case -1:
				return MINUS_ONE;

			  default:
				return new Integer(i);
			}
		}
	}

	private static final Short[] staticShorts =
		{new Short((short) 0), new Short((short) 1), new Short((short) 2),
		 new Short((short) 3), new Short((short) 4), new Short((short) 5),
		 new Short((short) 6), new Short((short) 7), new Short((short) 8),
		 new Short((short) 9), new Short((short) 10)};

	public static Short getShort(short i)
	{
		if (i >= 0 && i < staticShorts.length)
			return staticShorts[i];
		else
			return new Short(i);
	}

	private static final Byte[] staticBytes =
		{new Byte((byte) 0), new Byte((byte) 1), new Byte((byte) 2),
		 new Byte((byte) 3), new Byte((byte) 4), new Byte((byte) 5),
		 new Byte((byte) 6), new Byte((byte) 7), new Byte((byte) 8),
		 new Byte((byte) 9), new Byte((byte) 10)};

	public static Byte getByte(byte i)
	{
		if (i >= 0 && i < staticBytes.length)
			return staticBytes[i];
		else
			return new Byte(i);
	}

	private static final Long[] staticLongs =
		{new Long(0), new Long(1), new Long(2),
		 new Long(3), new Long(4), new Long(5),
		 new Long(6), new Long(7), new Long(8),
		 new Long(9), new Long(10)};

	public static Long getLong(long i)
	{
		if (i >= 0 && i < staticLongs.length)
			return staticLongs[(int) i];
		else
			return new Long(i);
	}

    public static Boolean getBoolean( boolean b)
    {
        return b ? Boolean.TRUE : Boolean.FALSE;
    }

	private static final byte[] staticZeroLenByteArray = new byte[0];
	public static byte[] getZeroLenByteArray() 
	{
		return staticZeroLenByteArray;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/982.java