error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/736.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/736.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/736.java
text:
```scala
t@@mp.add( tq, BooleanClause.Occur.SHOULD);

/**
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.search.similar;

import java.io.*;
import java.util.*;
import java.net.*;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.*;
import org.apache.lucene.search.*;
import org.apache.lucene.index.*;
import org.apache.lucene.util.*;

/**
 * Simple similarity measures.
 *
 *
 * @see MoreLikeThis
 */
public final class SimilarityQueries
{
	/**
	 *
	 */
	private SimilarityQueries()
	{
	}
	
	/**
     * Simple similarity query generators.
	 * Takes every unique word and forms a boolean query where all words are optional.
	 * After you get this you'll use to to query your {@link IndexSearcher} for similar docs.
	 * The only caveat is the first hit returned <b>should be</b> your source document - you'll
	 * need to then ignore that.
	 *
	 * <p>
	 *
	 * So, if you have a code fragment like this:
	 * <br>
	 * <code>
	 * Query q = formSimilaryQuery( "I use Lucene to search fast. Fast searchers are good", new StandardAnalyzer(), "contents", null);
	 * </code>
	 *
	 * <p>
	 *
	 
	 * The query returned, in string form, will be <code>'(i use lucene to search fast searchers are good')</code>.
	 *
	 * <p>
	 * The philosophy behind this method is "two documents are similar if they share lots of words".
	 * Note that behind the scenes, Lucenes scoring algorithm will tend to give two documents a higher similarity score if the share more uncommon words.
	 *
	 * <P>
	 * This method is fail-safe in that if a long 'body' is passed in and
	 * {@link BooleanQuery#add BooleanQuery.add()} (used internally)
	 * throws
	 * {@link org.apache.lucene.search.BooleanQuery.TooManyClauses BooleanQuery.TooManyClauses}, the
	 * query as it is will be returned.
	 *
	 * 
	 * 
	 *
	 *
	 * @param body the body of the document you want to find similar documents to
	 * @param a the analyzer to use to parse the body
	 * @param field the field you want to search on, probably something like "contents" or "body"
	 * @param stop optional set of stop words to ignore
	 * @return a query with all unique words in 'body'
	 * @throws IOException this can't happen...
	 */
    public static Query formSimilarQuery( String body,
										  Analyzer a,
										  String field,
										  Set stop)
										  throws IOException
	{	
		TokenStream ts = a.tokenStream( field, new StringReader( body));
		org.apache.lucene.analysis.Token t;
		BooleanQuery tmp = new BooleanQuery();
		Set already = new HashSet(); // ignore dups
		while ( (t = ts.next()) != null)
		{
			String word = t.termText();
			// ignore opt stop words
			if ( stop != null &&
				 stop.contains( word)) continue;
			// ignore dups
			if ( ! already.add( word)) continue;
			// add to query
			TermQuery tq = new TermQuery( new Term( field, word));
			try
			{
				tmp.add( tq, false, false);
			}
			catch( BooleanQuery.TooManyClauses too)
			{
				// fail-safe, just return what we have, not the end of the world
				break;
			}
		}
		return tmp;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/736.java