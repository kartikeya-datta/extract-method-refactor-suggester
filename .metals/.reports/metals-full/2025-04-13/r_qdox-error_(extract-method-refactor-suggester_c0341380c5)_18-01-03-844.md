error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7540.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7540.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7540.java
text:
```scala
c@@ = '\n';

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.util.string;


/**
 * Strips comments and whitespace from javascript
 * 
 * @author Matej Knopp
 */
public class JavascriptStripper
{
	/**
	 * Determines the state of script proessing.
	 * @author Matej Knopp
	 */
	private enum State {
		/** Inside regular text */
		REGULAR_TEXT, 
		
		/** String started with single quote (') */
		STRING_SINGLE_QUOTE,
		
		/** String started with double quotes (") */
		STRING_DOUBLE_QUOTES, 
		
		/** Inside two or more whitespace characters */
		WHITE_SPACE, 
		
		/** Inside a line comment (//   ) */
		LINE_COMMENT, 
		
		/** Inside a multi line comment */
		MULTILINE_COMMENT
	};

	/**
	 * Removes javascript comments and whitespaces from specified string.
	 * 
	 * @param original
	 *            Source string
	 * @return String with removed comments and whitespaces
	 */
	public static String stripCommentsAndWhitespace(String original)
	{
		// let's be optimistic
		StringBuilder result = new StringBuilder(original.length() / 2);
		State state = State.REGULAR_TEXT;

		for (int i = 0; i < original.length(); ++i)
		{
			char c = original.charAt(i);
			char next = (i < original.length() - 1) ? original.charAt(i + 1) : 0;
			char prev = (i > 0) ? original.charAt(i - 1) : 0;

			if (state == State.WHITE_SPACE)
			{
				if (Character.isWhitespace(next) == false)
				{
					state = State.REGULAR_TEXT;
				}
				continue;
			}

			if (state == State.REGULAR_TEXT)
			{
				if (c == '/' && next == '/')
				{
					state = State.LINE_COMMENT;
					continue;
				}
				else if (c == '/' && next == '*')
				{
					state = State.MULTILINE_COMMENT;
					++i;
					continue;
				}
				else if (Character.isWhitespace(c) && Character.isWhitespace(next))
				{
					// ignore all whitespace characters after this one
					state = State.WHITE_SPACE;
					c = ' ';
				}
				else if (c == '\'')
				{
					state = State.STRING_SINGLE_QUOTE;
				}
				else if (c == '"')
				{
					state = State.STRING_DOUBLE_QUOTES;
				}
				result.append(c);
				continue;
			}

			if (state == State.LINE_COMMENT)
			{
				if (c == '\n')
				{
					state = State.REGULAR_TEXT;
					continue;
				}
			}

			if (state == State.MULTILINE_COMMENT)
			{
				if (c == '*' && next == '/')
				{
					state = State.REGULAR_TEXT;
					++i;
					continue;
				}
			}

			if (state == State.STRING_SINGLE_QUOTE)
			{
				if (c == '\'' && prev != '\\')
				{
					state = State.REGULAR_TEXT;
				}
				result.append(c);
				continue;
			}

			if (state == State.STRING_DOUBLE_QUOTES)
			{
				if (c == '"' && prev != '\\')
				{
					state = State.REGULAR_TEXT;
				}
				result.append(c);
				continue;
			}
		}

		return result.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7540.java