error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3389.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3389.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3389.java
text:
```scala
final C@@omponentTag tag = (ComponentTag)getNextFilter().nextTag();

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
package org.apache.wicket.markup.parser.filter;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.WicketParseException;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;
import org.apache.wicket.util.collections.ArrayListStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is a markup inline filter. It identifies HTML specific issues which make HTML not 100% xml
 * compliant. E.g. tags like &lt;p&gt; often are missing the corresponding close tag.
 * 
 * @author Juergen Donnerstag
 */
public final class HtmlHandler extends AbstractMarkupFilter
{
	/** Logging */
	private static final Logger log = LoggerFactory.getLogger(HtmlHandler.class);

	/** Tag stack to find balancing tags */
	final private ArrayListStack<ComponentTag> stack = new ArrayListStack<ComponentTag>();

	/** Map of simple tags. */
	private static final Map<String, Boolean> doesNotRequireCloseTag = new HashMap<String, Boolean>();

	static
	{
		// Tags which are allowed not be closed in HTML
		doesNotRequireCloseTag.put("p", Boolean.TRUE);
		doesNotRequireCloseTag.put("br", Boolean.TRUE);
		doesNotRequireCloseTag.put("img", Boolean.TRUE);
		doesNotRequireCloseTag.put("input", Boolean.TRUE);
		doesNotRequireCloseTag.put("hr", Boolean.TRUE);
		doesNotRequireCloseTag.put("link", Boolean.TRUE);
		doesNotRequireCloseTag.put("meta", Boolean.TRUE);
	}

	/**
	 * Construct.
	 */
	public HtmlHandler()
	{
	}

	/**
	 * @see org.apache.wicket.markup.parser.IMarkupFilter#nextTag()
	 */
	public MarkupElement nextTag() throws ParseException
	{
		// Get the next tag. If null, no more tags are available
		final ComponentTag tag = (ComponentTag)getParent().nextTag();
		if (tag == null)
		{
			// No more tags from the markup.
			// If there's still a non-simple tag left, it's an error
			while (stack.size() > 0)
			{
				final ComponentTag top = stack.peek();

				if (!requiresCloseTag(top.getName()))
				{
					stack.pop();
				}
				else
				{
					throw new WicketParseException("Tag does not have a close tag:", top);
				}
			}

			return tag;
		}

		if (log.isDebugEnabled())
		{
			log.debug("tag: " + tag.toUserDebugString() + ", stack: " + stack);
		}

		// Check tag type
		if (tag.isOpen())
		{
			// Push onto stack
			stack.push(tag);
		}
		else if (tag.isClose())
		{
			// Check that there is something on the stack
			if (stack.size() > 0)
			{
				// Pop the top tag off the stack
				ComponentTag top = stack.pop();

				// If the name of the current close tag does not match the
				// tag on the stack then we may have a mismatched close tag
				boolean mismatch = !top.hasEqualTagName(tag);

				if (mismatch)
				{
					top.setHasNoCloseTag(true);

					// Pop any simple tags off the top of the stack
					while (mismatch && !requiresCloseTag(top.getName()))
					{
						top.setHasNoCloseTag(true);

						// Pop simple tag
						if (stack.isEmpty())
						{
							break;
						}
						top = stack.pop();

						// Does new top of stack mismatch too?
						mismatch = !top.hasEqualTagName(tag);
					}

					// If adjusting for simple tags did not fix the problem,
					// it must be a real mismatch.
					if (mismatch)
					{
						throw new ParseException("Tag " + top.toUserDebugString() +
							" has a mismatched close tag at " + tag.toUserDebugString(),
							top.getPos());
					}
				}

				// Tag matches, so add pointer to matching tag
				tag.setOpenTag(top);
			}
			else
			{
				throw new WicketParseException("Tag does not have a matching open tag:", tag);
			}
		}
		else if (tag.isOpenClose())
		{
			// Tag closes itself
			tag.setOpenTag(tag);
		}

		return tag;
	}

	/**
	 * Gets whether this tag does not require a closing tag.
	 * 
	 * @param name
	 *            The tag's name, e.g. a, br, div, etc.
	 * @return True if this tag does not require a closing tag
	 */
	public static boolean requiresCloseTag(final String name)
	{
		return doesNotRequireCloseTag.get(name.toLowerCase()) == null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3389.java