error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3701.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3701.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3701.java
text:
```scala
M@@oreLikeThisQuery mlt=new MoreLikeThisQuery(DOMUtils.getText(e),fields,analyzer, fields[0]);

/*
 * Created on 25-Jan-2006
 */
package org.apache.lucene.xmlparser.builders;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.search.similar.MoreLikeThisQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.xmlparser.DOMUtils;
import org.apache.lucene.xmlparser.ParserException;
import org.apache.lucene.xmlparser.QueryBuilder;
import org.w3c.dom.Element;
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

/**
 * 
 */
public class LikeThisQueryBuilder implements QueryBuilder {

	private Analyzer analyzer;
	String defaultFieldNames [];
	int defaultMaxQueryTerms=20;
	int defaultMinTermFrequency=1;
	float defaultPercentTermsToMatch=30; //default is a 3rd of selected terms must match

	public LikeThisQueryBuilder(Analyzer analyzer,String [] defaultFieldNames)
	{
		this.analyzer=analyzer;
		this.defaultFieldNames=defaultFieldNames;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.lucene.xmlparser.QueryObjectBuilder#process(org.w3c.dom.Element)
	 */
	public Query getQuery(Element e) throws ParserException {
		String fieldsList=e.getAttribute("fieldNames"); //a comma-delimited list of fields
		String fields[]=defaultFieldNames;
		if((fieldsList!=null)&&(fieldsList.trim().length()>0))
		{
			fields=fieldsList.trim().split(",");
			//trim the fieldnames
			for (int i = 0; i < fields.length; i++) {
				fields[i]=fields[i].trim();
			}
		}
		
		//Parse any "stopWords" attribute
		//TODO MoreLikeThis needs to ideally have per-field stopWords lists - until then 
		//I use all analyzers/fields to generate multi-field compatible stop list
		String stopWords=e.getAttribute("stopWords");
		Set<String> stopWordsSet=null;
		if((stopWords!=null)&&(fields!=null))
		{
		    stopWordsSet=new HashSet<String>();
		    for (int i = 0; i < fields.length; i++)
            {
                try
                {
                  TokenStream ts = analyzer.reusableTokenStream(fields[i],new StringReader(stopWords));
                  CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
                  ts.reset();
	                while(ts.incrementToken()) {
	                    stopWordsSet.add(termAtt.toString());
	                }
	                ts.end();
	                ts.close();
                }
                catch(IOException ioe)
                {
                    throw new ParserException("IoException parsing stop words list in "
                            +getClass().getName()+":"+ioe.getLocalizedMessage());
                }
            }
		}
		
		
		MoreLikeThisQuery mlt=new MoreLikeThisQuery(DOMUtils.getText(e),fields,analyzer);
		mlt.setMaxQueryTerms(DOMUtils.getAttribute(e,"maxQueryTerms",defaultMaxQueryTerms));
		mlt.setMinTermFrequency(DOMUtils.getAttribute(e,"minTermFrequency",defaultMinTermFrequency));
		mlt.setPercentTermsToMatch(DOMUtils.getAttribute(e,"percentTermsToMatch",defaultPercentTermsToMatch)/100);
		mlt.setStopWords(stopWordsSet);
		int minDocFreq=DOMUtils.getAttribute(e,"minDocFreq",-1);
		if(minDocFreq>=0)
		{
			mlt.setMinDocFreq(minDocFreq);
		}

		mlt.setBoost(DOMUtils.getAttribute(e,"boost",1.0f));

		return mlt;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3701.java