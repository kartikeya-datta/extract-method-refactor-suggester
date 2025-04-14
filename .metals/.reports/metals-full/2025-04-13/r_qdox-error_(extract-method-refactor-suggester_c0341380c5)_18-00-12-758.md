error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5750.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5750.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5750.java
text:
```scala
t@@mp == '[' || tmp == ';' || tmp == '!')

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
package org.apache.wicket.util.string;


/**
 * Strips comments and whitespace from javascript
 * 
 * @author Matej Knopp
 */
public class JavaScriptStripper
{
	/*
	 * Determines the state of script processing.
	 */
	/** Inside regular text */
	private final static int REGULAR_TEXT = 1;

	/** String started with single quote (') */
	private final static int STRING_SINGLE_QUOTE = 2;

	/** String started with double quotes (") */
	private final static int STRING_DOUBLE_QUOTES = 3;

	/** Inside two or more whitespace characters */
	private final static int WHITE_SPACE = 4;

	/** Inside a line comment (// ) */
	private final static int LINE_COMMENT = 5;

	/** Inside a multi line comment */
	private final static int MULTILINE_COMMENT = 6;

	/** Inside a regular expression */
	private final static int REG_EXP = 7;

	private int getPrevCount(String s, int fromIndex, char c)
	{
		int count = 0;
		--fromIndex;
		while (fromIndex >= 0)
		{
			if (s.charAt(fromIndex--) == c)
			{
				++count;
			}
			else
			{
				break;
			}
		}
		return count;
	}

	/**
	 * Removes javascript comments and whitespace from specified string.
	 * 
	 * @param original
	 *            Source string
	 * @return String with removed comments and whitespace
	 */
	public String stripCommentsAndWhitespace(String original)
	{
		// let's be optimistic
		AppendingStringBuffer result = new AppendingStringBuffer(original.length() / 2);
		int state = REGULAR_TEXT;
		boolean wasNewLineInWhitespace = false;

		for (int i = 0; i < original.length(); ++i)
		{
			char c = original.charAt(i);
			char next = (i < original.length() - 1) ? original.charAt(i + 1) : 0;
			char prev = (i > 0) ? original.charAt(i - 1) : 0;

			if (state == WHITE_SPACE)
			{
				// WICKET 2060
				if (c == '\n' && !wasNewLineInWhitespace)
				{
					result.append("\n");
					wasNewLineInWhitespace = true;
				}
				if (Character.isWhitespace(next) == false)
				{
					state = REGULAR_TEXT;
				}
				continue;
			}

			if (state == REGULAR_TEXT)
			{
				if (c == '/' && next == '/' && prev != '\\')
				{
					state = LINE_COMMENT;
					continue;
				}
				else if (c == '/' && next == '*')
				{
					state = MULTILINE_COMMENT;
					++i;
					continue;
				}
				else if (c == '/')
				{
					// This might be a divide operator, or it might be a regular expression.
					// Work out if it's a regular expression by finding the previous non-whitespace
					// char, which
					// will be either '=' or '('. If it's not, it's just a divide operator.
					int idx = result.length() - 1;
					while (idx > 0)
					{
						char tmp = result.charAt(idx);
						if (Character.isWhitespace(tmp))
						{
							idx--;
							continue;
						}
						if (tmp == '=' || tmp == '(' || tmp == '{' || tmp == ':' || tmp == ',' ||
							tmp == '[' || tmp == ';')
						{
							state = REG_EXP;
							break;
						}
						break;
					}
				}
				else if (Character.isWhitespace(c) && Character.isWhitespace(next))
				{
					// WICKET-2060
					if (c == '\n' || next == '\n')
					{
						c = '\n';
						wasNewLineInWhitespace = true;
					}
					else
					{
						c = ' ';
						wasNewLineInWhitespace = false;
					}
					// ignore all whitespace characters after this one
					state = WHITE_SPACE;
				}
				else if (c == '\'')
				{
					state = STRING_SINGLE_QUOTE;
				}
				else if (c == '"')
				{
					state = STRING_DOUBLE_QUOTES;
				}
				result.append(c);
				continue;
			}

			if (state == LINE_COMMENT)
			{
				if (c == '\n' || c == '\r')
				{
					state = REGULAR_TEXT;
					continue;
				}
			}

			if (state == MULTILINE_COMMENT)
			{
				if (c == '*' && next == '/')
				{
					state = REGULAR_TEXT;
					++i;
					continue;
				}
			}

			if (state == STRING_SINGLE_QUOTE)
			{
				// to leave a string expression we need even (or zero) number of backslashes
				int count = getPrevCount(original, i, '\\');
				if (c == '\'' && count % 2 == 0)
				{
					state = REGULAR_TEXT;
				}
				result.append(c);
				continue;
			}

			if (state == STRING_DOUBLE_QUOTES)
			{
				// to leave a string expression we need even (or zero) number of backslashes
				int count = getPrevCount(original, i, '\\');
				if (c == '"' && count % 2 == 0)
				{
					state = REGULAR_TEXT;
				}
				result.append(c);
				continue;
			}

			if (state == REG_EXP)
			{
				// to leave regular expression we need even (or zero) number of backslashes
				int count = getPrevCount(original, i, '\\');
				if (c == '/' && count % 2 == 0)
				{
					state = REGULAR_TEXT;
				}
				result.append(c);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5750.java