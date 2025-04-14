error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15443.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15443.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15443.java
text:
```scala
M@@etaPattern.XML_ATTRIBUTE_NAME }));

/*
 * $Id: VariableAssignmentParser.java,v 1.6 2005/01/15 19:24:03 jonathanlocke
 * Exp $ $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.util.parse.metapattern.parsers;

import wicket.util.parse.metapattern.Group;
import wicket.util.parse.metapattern.MetaPattern;
import wicket.util.parse.metapattern.OptionalMetaPattern;

/**
 * Parses key value assignment statements like "foo=bar" but also supporting
 * namespaces like "wicket:foo=bar". However the 'key' value returned will
 * contain "wicket:foo". It does not separate namespace and name.
 * 
 * @author Jonathan Locke
 */
public final class VariableAssignmentParser extends MetaPatternParser
{
	/** The optional namespace like "namespace:*" */
	private static final MetaPattern namespace = new OptionalMetaPattern(new MetaPattern[] {
			MetaPattern.VARIABLE_NAME, MetaPattern.COLON });

	/** The key (lvalue) like "name" or "namespace:name" */
	private static final Group key = new Group(new MetaPattern(new MetaPattern[] { namespace,
			MetaPattern.VARIABLE_NAME }));

	/** The rvalue of the assignment */
	private static final Group value = new Group(MetaPattern.STRING);

	/** The whole assignment without optional leading and trailing spaces */
	private static final MetaPattern variableAssignment = new MetaPattern(new MetaPattern[] {
			MetaPattern.OPTIONAL_WHITESPACE, MetaPattern.EQUALS, MetaPattern.OPTIONAL_WHITESPACE,
			value });

	/** Ignore leading and trailing spaces surrounding the assignment */
	private static final MetaPattern pattern = new MetaPattern(new MetaPattern[] {
			MetaPattern.OPTIONAL_WHITESPACE, key, new OptionalMetaPattern(variableAssignment),
			MetaPattern.OPTIONAL_WHITESPACE });

	/**
	 * Construct a variable assignment parser against a given input character
	 * sequence
	 * 
	 * @param input
	 *            The input to parse
	 */
	public VariableAssignmentParser(final CharSequence input)
	{
		super(pattern, input);
	}

	/**
	 * Gets the key part (eg 'foo' in 'foo=bar'). The key will include the
	 * optional namespace (eg 'html:foo' in 'html:foo=bar').
	 * 
	 * @return The key part
	 */
	public String getKey()
	{
		return key.get(matcher());
	}

	/**
	 * Gets the value part (eg 'bar' in 'foo=bar').
	 * 
	 * @return The value part
	 */
	public String getValue()
	{
		return value.get(matcher());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15443.java