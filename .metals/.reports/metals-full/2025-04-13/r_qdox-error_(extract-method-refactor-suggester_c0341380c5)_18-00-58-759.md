error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2936.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2936.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2936.java
text:
```scala
s@@earcher = new IndexSearcher(rd, true);

package org.apache.lucene.search;
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.IOException;

import org.apache.lucene.util.LuceneTestCase;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.RAMDirectory;

public class TestBooleanOr extends LuceneTestCase {

	private static String FIELD_T = "T";
	private static String FIELD_C = "C";

	private TermQuery t1 = new TermQuery(new Term(FIELD_T, "files"));
	private TermQuery t2 = new TermQuery(new Term(FIELD_T, "deleting"));
	private TermQuery c1 = new TermQuery(new Term(FIELD_C, "production"));
	private TermQuery c2 = new TermQuery(new Term(FIELD_C, "optimize"));

	private IndexSearcher searcher = null;

	private int search(Query q) throws IOException {
    QueryUtils.check(q,searcher);
    return searcher.search(q, null, 1000).totalHits;
	}

	public void testElements() throws IOException {
		assertEquals(1, search(t1));
		assertEquals(1, search(t2));
		assertEquals(1, search(c1));
		assertEquals(1, search(c2));
	}

	/**
	 * <code>T:files T:deleting C:production C:optimize </code>
	 * it works.
	 *
	 * @throws IOException
	 */
	public void testFlat() throws IOException {
		BooleanQuery q = new BooleanQuery();
		q.add(new BooleanClause(t1, BooleanClause.Occur.SHOULD));
		q.add(new BooleanClause(t2, BooleanClause.Occur.SHOULD));
		q.add(new BooleanClause(c1, BooleanClause.Occur.SHOULD));
		q.add(new BooleanClause(c2, BooleanClause.Occur.SHOULD));
		assertEquals(1, search(q));
	}

	/**
	 * <code>(T:files T:deleting) (+C:production +C:optimize)</code>
	 * it works.
	 *
	 * @throws IOException
	 */
	public void testParenthesisMust() throws IOException {
		BooleanQuery q3 = new BooleanQuery();
		q3.add(new BooleanClause(t1, BooleanClause.Occur.SHOULD));
		q3.add(new BooleanClause(t2, BooleanClause.Occur.SHOULD));
		BooleanQuery q4 = new BooleanQuery();
		q4.add(new BooleanClause(c1, BooleanClause.Occur.MUST));
		q4.add(new BooleanClause(c2, BooleanClause.Occur.MUST));
		BooleanQuery q2 = new BooleanQuery();
		q2.add(q3, BooleanClause.Occur.SHOULD);
		q2.add(q4, BooleanClause.Occur.SHOULD);
		assertEquals(1, search(q2));
	}

	/**
	 * <code>(T:files T:deleting) +(C:production C:optimize)</code>
	 * not working. results NO HIT.
	 *
	 * @throws IOException
	 */
	public void testParenthesisMust2() throws IOException {
		BooleanQuery q3 = new BooleanQuery();
		q3.add(new BooleanClause(t1, BooleanClause.Occur.SHOULD));
		q3.add(new BooleanClause(t2, BooleanClause.Occur.SHOULD));
		BooleanQuery q4 = new BooleanQuery();
		q4.add(new BooleanClause(c1, BooleanClause.Occur.SHOULD));
		q4.add(new BooleanClause(c2, BooleanClause.Occur.SHOULD));
		BooleanQuery q2 = new BooleanQuery();
		q2.add(q3, BooleanClause.Occur.SHOULD);
		q2.add(q4, BooleanClause.Occur.MUST);
		assertEquals(1, search(q2));
	}

	/**
	 * <code>(T:files T:deleting) (C:production C:optimize)</code>
	 * not working. results NO HIT.
	 *
	 * @throws IOException
	 */
	public void testParenthesisShould() throws IOException {
		BooleanQuery q3 = new BooleanQuery();
		q3.add(new BooleanClause(t1, BooleanClause.Occur.SHOULD));
		q3.add(new BooleanClause(t2, BooleanClause.Occur.SHOULD));
		BooleanQuery q4 = new BooleanQuery();
		q4.add(new BooleanClause(c1, BooleanClause.Occur.SHOULD));
		q4.add(new BooleanClause(c2, BooleanClause.Occur.SHOULD));
		BooleanQuery q2 = new BooleanQuery();
		q2.add(q3, BooleanClause.Occur.SHOULD);
		q2.add(q4, BooleanClause.Occur.SHOULD);
		assertEquals(1, search(q2));
	}

	protected void setUp() throws Exception {
		super.setUp();
		super.setUp();

		//
		RAMDirectory rd = new RAMDirectory();

		//
		IndexWriter writer = new IndexWriter(rd, new StandardAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);

		//
		Document d = new Document();
		d.add(new Field(
				FIELD_T,
				"Optimize not deleting all files",
				Field.Store.YES,
				Field.Index.ANALYZED));
		d.add(new Field(
				FIELD_C,
				"Deleted When I run an optimize in our production environment.",
				Field.Store.YES,
				Field.Index.ANALYZED));

		//
		writer.addDocument(d);
		writer.close();

		//
		searcher = new IndexSearcher(rd);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2936.java