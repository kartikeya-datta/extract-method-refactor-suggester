error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1210.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1210.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1210.java
text:
```scala
q@@ueryFactory.addBuilder("UserQuery",new UserInputQueryBuilder(parser));

package org.apache.lucene.xmlparser;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.xmlparser.builders.BooleanQueryBuilder;
import org.apache.lucene.xmlparser.builders.ConstantScoreQueryBuilder;
import org.apache.lucene.xmlparser.builders.FilteredQueryBuilder;
import org.apache.lucene.xmlparser.builders.RangeFilterBuilder;
import org.apache.lucene.xmlparser.builders.SpanFirstBuilder;
import org.apache.lucene.xmlparser.builders.SpanNearBuilder;
import org.apache.lucene.xmlparser.builders.SpanNotBuilder;
import org.apache.lucene.xmlparser.builders.SpanOrBuilder;
import org.apache.lucene.xmlparser.builders.SpanOrTermsBuilder;
import org.apache.lucene.xmlparser.builders.SpanQueryBuilderFactory;
import org.apache.lucene.xmlparser.builders.SpanTermBuilder;
import org.apache.lucene.xmlparser.builders.TermQueryBuilder;
import org.apache.lucene.xmlparser.builders.UserInputQueryBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Assembles a QueryBuilder which uses only core Lucene Query objects
 * @author Mark
 *
 */
public class CoreParser implements QueryBuilder
{
	
	protected Analyzer analyzer;
	protected QueryParser parser;
	protected QueryBuilderFactory queryFactory;
	protected FilterBuilderFactory filterFactory;

	public CoreParser(Analyzer analyzer, QueryParser parser)
	{
		this.analyzer=analyzer;
		this.parser=parser;
		filterFactory = new FilterBuilderFactory();
		filterFactory.addBuilder("RangeFilter",new RangeFilterBuilder());
		
		
		queryFactory = new QueryBuilderFactory();
		queryFactory.addBuilder("TermQuery",new TermQueryBuilder());
		queryFactory.addBuilder("BooleanQuery",new BooleanQueryBuilder(queryFactory));
		queryFactory.addBuilder("UserQuery",new UserInputQueryBuilder(new QueryParser("contents", analyzer)));
		queryFactory.addBuilder("FilteredQuery",new FilteredQueryBuilder(filterFactory,queryFactory));
		queryFactory.addBuilder("ConstantScoreQuery",new ConstantScoreQueryBuilder(filterFactory));
		
		SpanQueryBuilderFactory sqof=new SpanQueryBuilderFactory();

		SpanNearBuilder snb=new SpanNearBuilder(sqof);
		sqof.addBuilder("SpanNear",snb);
		queryFactory.addBuilder("SpanNear",snb);

		SpanTermBuilder snt=new SpanTermBuilder();
		sqof.addBuilder("SpanTerm",snt);
		queryFactory.addBuilder("SpanTerm",snt);
		
		SpanOrBuilder sot=new SpanOrBuilder(sqof);
		sqof.addBuilder("SpanOr",sot);
		queryFactory.addBuilder("SpanOr",sot);

		SpanOrTermsBuilder sots=new SpanOrTermsBuilder(analyzer);
		sqof.addBuilder("SpanOrTerms",sots);
		queryFactory.addBuilder("SpanOrTerms",sots);		
		
		SpanFirstBuilder sft=new SpanFirstBuilder(sqof);
		sqof.addBuilder("SpanFirst",sft);
		queryFactory.addBuilder("SpanFirst",sft);
		
		SpanNotBuilder snot=new SpanNotBuilder(sqof);
		sqof.addBuilder("SpanNot",snot);
		queryFactory.addBuilder("SpanNot",snot);	
	}
	
	public Query parse(InputStream xmlStream) throws ParserException
	{
		return getQuery(parseXML(xmlStream).getDocumentElement());
	}
	
	public void addQueryBuilder(String nodeName,QueryBuilder builder)
	{
		queryFactory.addBuilder(nodeName,builder);
	}
	public void addFilterBuilder(String nodeName,FilterBuilder builder)
	{
		filterFactory.addBuilder(nodeName,builder);
	}
	
	private static Document parseXML(InputStream pXmlFile) throws ParserException
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try
		{
			db = dbf.newDocumentBuilder();
		}
		catch (Exception se)
		{
			throw new ParserException("XML Parser configuration error", se);
		}
		org.w3c.dom.Document doc = null;
		try
		{
			doc = db.parse(pXmlFile);
		}
		catch (Exception se)
		{
			throw new ParserException("Error parsing XML stream:" + se, se);
		}
		return doc;
	}
	

	public Query getQuery(Element e) throws ParserException
	{
		return queryFactory.getQuery(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1210.java