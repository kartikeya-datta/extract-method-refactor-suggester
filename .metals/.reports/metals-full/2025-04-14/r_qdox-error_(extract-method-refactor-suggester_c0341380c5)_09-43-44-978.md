error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2985.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2985.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2985.java
text:
```scala
(@@text.length()<= maxDocCharsToAnalyze)

package org.apache.lucene.search.highlight;
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
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.PriorityQueue;

/**
 * Class used to markup highlighted terms found in the best sections of a
 * text, using configurable {@link Fragmenter}, {@link Scorer}, {@link Formatter},
 * {@link Encoder} and tokenizers.
 * @author mark@searcharea.co.uk
 */
public class Highlighter
{
  public static final int DEFAULT_MAX_CHARS_TO_ANALYZE = 50*1024;
  /**
   * @deprecated See {@link #DEFAULT_MAX_CHARS_TO_ANALYZE}
   */
	public static final  int DEFAULT_MAX_DOC_BYTES_TO_ANALYZE=DEFAULT_MAX_CHARS_TO_ANALYZE;
  private int maxDocCharsToAnalyze = DEFAULT_MAX_CHARS_TO_ANALYZE;
	private Formatter formatter;
	private Encoder encoder;
	private Fragmenter textFragmenter=new SimpleFragmenter();
	private Scorer fragmentScorer=null;

	public Highlighter(Scorer fragmentScorer)
	{
		this(new SimpleHTMLFormatter(),fragmentScorer);
	}


 	public Highlighter(Formatter formatter, Scorer fragmentScorer)
 	{
		this(formatter,new DefaultEncoder(),fragmentScorer);
	}


	public Highlighter(Formatter formatter, Encoder encoder, Scorer fragmentScorer)
	{
 		this.formatter = formatter;
		this.encoder = encoder;
 		this.fragmentScorer = fragmentScorer;
 	}

	/**
	 * Highlights chosen terms in a text, extracting the most relevant section.
	 * This is a convenience method that calls
	 * {@link #getBestFragment(TokenStream, String)}
	 *
	 * @param analyzer   the analyzer that will be used to split <code>text</code>
	 * into chunks
	 * @param text text to highlight terms in
	 * @param fieldName Name of field used to influence analyzer's tokenization policy
	 *
	 * @return highlighted text fragment or null if no terms found
	 */
	public final String getBestFragment(Analyzer analyzer, String fieldName,String text)
		throws IOException
	{
		TokenStream tokenStream = analyzer.tokenStream(fieldName, new StringReader(text));
		return getBestFragment(tokenStream, text);
	}

	/**
	 * Highlights chosen terms in a text, extracting the most relevant section.
	 * The document text is analysed in chunks to record hit statistics
	 * across the document. After accumulating stats, the fragment with the highest score
	 * is returned
	 *
	 * @param tokenStream   a stream of tokens identified in the text parameter, including offset information.
	 * This is typically produced by an analyzer re-parsing a document's
	 * text. Some work may be done on retrieving TokenStreams more efficently
	 * by adding support for storing original text position data in the Lucene
	 * index but this support is not currently available (as of Lucene 1.4 rc2).
	 * @param text text to highlight terms in
	 *
	 * @return highlighted text fragment or null if no terms found
	 */
	public final String getBestFragment(TokenStream tokenStream, String text)
		throws IOException
	{
		String[] results = getBestFragments(tokenStream,text, 1);
		if (results.length > 0)
		{
			return results[0];
		}
		return null;
	}

	/**
	 * Highlights chosen terms in a text, extracting the most relevant sections.
	 * This is a convenience method that calls
	 * {@link #getBestFragments(TokenStream, String, int)}
	 *
	 * @param analyzer   the analyzer that will be used to split <code>text</code>
	 * into chunks
	 * @param text        	text to highlight terms in
	 * @param maxNumFragments  the maximum number of fragments.
	 * @deprecated This method incorrectly hardcodes the choice of fieldname. Use the
	 * method of the same name that takes a fieldname.
	 * @return highlighted text fragments (between 0 and maxNumFragments number of fragments)
	 */
	public final String[] getBestFragments(
		Analyzer analyzer,
		String text,
		int maxNumFragments)
		throws IOException
	{
		TokenStream tokenStream = analyzer.tokenStream("field", new StringReader(text));
		return getBestFragments(tokenStream, text, maxNumFragments);
	}
	/**
	 * Highlights chosen terms in a text, extracting the most relevant sections.
	 * This is a convenience method that calls
	 * {@link #getBestFragments(TokenStream, String, int)}
	 *
	 * @param analyzer   the analyzer that will be used to split <code>text</code>
	 * into chunks
	 * @param fieldName     the name of the field being highlighted (used by analyzer)
	 * @param text        	text to highlight terms in
	 * @param maxNumFragments  the maximum number of fragments.
	 *
	 * @return highlighted text fragments (between 0 and maxNumFragments number of fragments)
	 */
	public final String[] getBestFragments(
		Analyzer analyzer,
		String fieldName,
		String text,
		int maxNumFragments)
		throws IOException
	{
		TokenStream tokenStream = analyzer.tokenStream(fieldName, new StringReader(text));
		return getBestFragments(tokenStream, text, maxNumFragments);
	}

	/**
	 * Highlights chosen terms in a text, extracting the most relevant sections.
	 * The document text is analysed in chunks to record hit statistics
	 * across the document. After accumulating stats, the fragments with the highest scores
	 * are returned as an array of strings in order of score (contiguous fragments are merged into
	 * one in their original order to improve readability)
	 *
	 * @param text        	text to highlight terms in
	 * @param maxNumFragments  the maximum number of fragments.
	 *
	 * @return highlighted text fragments (between 0 and maxNumFragments number of fragments)
	 */
	public final String[] getBestFragments(
		TokenStream tokenStream,
		String text,
		int maxNumFragments)
		throws IOException
	{
		maxNumFragments = Math.max(1, maxNumFragments); //sanity check

		TextFragment[] frag =getBestTextFragments(tokenStream,text, true,maxNumFragments);

		//Get text
		ArrayList fragTexts = new ArrayList();
		for (int i = 0; i < frag.length; i++)
		{
			if ((frag[i] != null) && (frag[i].getScore() > 0))
			{
				fragTexts.add(frag[i].toString());
			}
		}
		return (String[]) fragTexts.toArray(new String[0]);
	}


	/**
	 * Low level api to get the most relevant (formatted) sections of the document.
	 * This method has been made public to allow visibility of score information held in TextFragment objects.
	 * Thanks to Jason Calabrese for help in redefining the interface.
	 * @param tokenStream
	 * @param text
	 * @param maxNumFragments
	 * @param mergeContiguousFragments
	 * @throws IOException
	 */
	public final TextFragment[] getBestTextFragments(
		TokenStream tokenStream,
		String text,
		boolean mergeContiguousFragments,
		int maxNumFragments)
		throws IOException
	{
		ArrayList docFrags = new ArrayList();
		StringBuffer newText=new StringBuffer();

		TextFragment currentFrag =	new TextFragment(newText,newText.length(), docFrags.size());
		fragmentScorer.startFragment(currentFrag);
		docFrags.add(currentFrag);

		FragmentQueue fragQueue = new FragmentQueue(maxNumFragments);

		try
		{
			org.apache.lucene.analysis.Token token;
			String tokenText;
			int startOffset;
			int endOffset;
			int lastEndOffset = 0;
			textFragmenter.start(text);

			TokenGroup tokenGroup=new TokenGroup();
			token = tokenStream.next();
			while ((token!= null)&&(token.startOffset()< maxDocCharsToAnalyze))
			{
				if((tokenGroup.numTokens>0)&&(tokenGroup.isDistinct(token)))
				{
					//the current token is distinct from previous tokens -
					// markup the cached token group info
					startOffset = tokenGroup.matchStartOffset;
					endOffset = tokenGroup.matchEndOffset;
					tokenText = text.substring(startOffset, endOffset);
					String markedUpText=formatter.highlightTerm(encoder.encodeText(tokenText), tokenGroup);
					//store any whitespace etc from between this and last group
					if (startOffset > lastEndOffset)
						newText.append(encoder.encodeText(text.substring(lastEndOffset, startOffset)));
					newText.append(markedUpText);
					lastEndOffset=Math.max(endOffset, lastEndOffset);
					tokenGroup.clear();

					//check if current token marks the start of a new fragment
					if(textFragmenter.isNewFragment(token))
					{
						currentFrag.setScore(fragmentScorer.getFragmentScore());
						//record stats for a new fragment
						currentFrag.textEndPos = newText.length();
						currentFrag =new TextFragment(newText, newText.length(), docFrags.size());
						fragmentScorer.startFragment(currentFrag);
						docFrags.add(currentFrag);
					}
				}

				tokenGroup.addToken(token,fragmentScorer.getTokenScore(token));

//				if(lastEndOffset>maxDocBytesToAnalyze)
//				{
//					break;
//				}
				token = tokenStream.next();
			}
			currentFrag.setScore(fragmentScorer.getFragmentScore());

			if(tokenGroup.numTokens>0)
			{
				//flush the accumulated text (same code as in above loop)
				startOffset = tokenGroup.matchStartOffset;
				endOffset = tokenGroup.matchEndOffset;
				tokenText = text.substring(startOffset, endOffset);
				String markedUpText=formatter.highlightTerm(encoder.encodeText(tokenText), tokenGroup);
				//store any whitespace etc from between this and last group
				if (startOffset > lastEndOffset)
					newText.append(encoder.encodeText(text.substring(lastEndOffset, startOffset)));
				newText.append(markedUpText);
				lastEndOffset=Math.max(lastEndOffset,endOffset);
			}

			//Test what remains of the original text beyond the point where we stopped analyzing 
			if (
//					if there is text beyond the last token considered..
					(lastEndOffset < text.length()) 
					&&
//					and that text is not too large...
					(text.length()< maxDocCharsToAnalyze)
				)				
			{
				//append it to the last fragment
				newText.append(encoder.encodeText(text.substring(lastEndOffset)));
			}

			currentFrag.textEndPos = newText.length();

			//sort the most relevant sections of the text
			for (Iterator i = docFrags.iterator(); i.hasNext();)
			{
				currentFrag = (TextFragment) i.next();

				//If you are running with a version of Lucene before 11th Sept 03
				// you do not have PriorityQueue.insert() - so uncomment the code below
				/*
									if (currentFrag.getScore() >= minScore)
									{
										fragQueue.put(currentFrag);
										if (fragQueue.size() > maxNumFragments)
										{ // if hit queue overfull
											fragQueue.pop(); // remove lowest in hit queue
											minScore = ((TextFragment) fragQueue.top()).getScore(); // reset minScore
										}


									}
				*/
				//The above code caused a problem as a result of Christoph Goller's 11th Sept 03
				//fix to PriorityQueue. The correct method to use here is the new "insert" method
				// USE ABOVE CODE IF THIS DOES NOT COMPILE!
				fragQueue.insert(currentFrag);
			}

			//return the most relevant fragments
			TextFragment frag[] = new TextFragment[fragQueue.size()];
			for (int i = frag.length - 1; i >= 0; i--)
			{
				frag[i] = (TextFragment) fragQueue.pop();
			}

			//merge any contiguous fragments to improve readability
			if(mergeContiguousFragments)
			{
				mergeContiguousFragments(frag);
				ArrayList fragTexts = new ArrayList();
				for (int i = 0; i < frag.length; i++)
				{
					if ((frag[i] != null) && (frag[i].getScore() > 0))
					{
						fragTexts.add(frag[i]);
					}
				}
				frag= (TextFragment[]) fragTexts.toArray(new TextFragment[0]);
			}

			return frag;

		}
		finally
		{
			if (tokenStream != null)
			{
				try
				{
					tokenStream.close();
				}
				catch (Exception e)
				{
				}
			}
		}
	}


	/** Improves readability of a score-sorted list of TextFragments by merging any fragments
	 * that were contiguous in the original text into one larger fragment with the correct order.
	 * This will leave a "null" in the array entry for the lesser scored fragment. 
	 * 
	 * @param frag An array of document fragments in descending score
	 */
	private void mergeContiguousFragments(TextFragment[] frag)
	{
		boolean mergingStillBeingDone;
		if (frag.length > 1)
			do
			{
				mergingStillBeingDone = false; //initialise loop control flag
				//for each fragment, scan other frags looking for contiguous blocks
				for (int i = 0; i < frag.length; i++)
				{
					if (frag[i] == null)
					{
						continue;
					}
					//merge any contiguous blocks 
					for (int x = 0; x < frag.length; x++)
					{
						if (frag[x] == null)
						{
							continue;
						}
						if (frag[i] == null)
						{
							break;
						}
						TextFragment frag1 = null;
						TextFragment frag2 = null;
						int frag1Num = 0;
						int frag2Num = 0;
						int bestScoringFragNum;
						int worstScoringFragNum;
						//if blocks are contiguous....
						if (frag[i].follows(frag[x]))
						{
							frag1 = frag[x];
							frag1Num = x;
							frag2 = frag[i];
							frag2Num = i;
						}
						else
							if (frag[x].follows(frag[i]))
							{
								frag1 = frag[i];
								frag1Num = i;
								frag2 = frag[x];
								frag2Num = x;
							}
						//merging required..
						if (frag1 != null)
						{
							if (frag1.getScore() > frag2.getScore())
							{
								bestScoringFragNum = frag1Num;
								worstScoringFragNum = frag2Num;
							}
							else
							{
								bestScoringFragNum = frag2Num;
								worstScoringFragNum = frag1Num;
							}
							frag1.merge(frag2);
							frag[worstScoringFragNum] = null;
							mergingStillBeingDone = true;
							frag[bestScoringFragNum] = frag1;
						}
					}
				}
			}
			while (mergingStillBeingDone);
	}
	
	
	/**
	 * Highlights terms in the  text , extracting the most relevant sections
	 * and concatenating the chosen fragments with a separator (typically "...").
	 * The document text is analysed in chunks to record hit statistics
	 * across the document. After accumulating stats, the fragments with the highest scores
	 * are returned in order as "separator" delimited strings.
	 *
	 * @param text        text to highlight terms in
	 * @param maxNumFragments  the maximum number of fragments.
	 * @param separator  the separator used to intersperse the document fragments (typically "...")
	 *
	 * @return highlighted text
	 */
	public final String getBestFragments(
		TokenStream tokenStream,	
		String text,
		int maxNumFragments,
		String separator)
		throws IOException
	{
		String sections[] =	getBestFragments(tokenStream,text, maxNumFragments);
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < sections.length; i++)
		{
			if (i > 0)
			{
				result.append(separator);
			}
			result.append(sections[i]);
		}
		return result.toString();
	}

	/**
	 * @return the maximum number of bytes to be tokenized per doc
   *
   * @deprecated See {@link #getMaxDocCharsToAnalyze()}, since this value has always counted on chars.  They both set the same internal value, however
	 */
	public int getMaxDocBytesToAnalyze()
	{
		return maxDocCharsToAnalyze;
	}

	/**
	 * @param byteCount the maximum number of bytes to be tokenized per doc
	 * (This can improve performance with large documents)
   *
   * @deprecated See {@link #setMaxDocCharsToAnalyze(int)}, since this value has always counted chars
	 */
	public void setMaxDocBytesToAnalyze(int byteCount)
	{
		maxDocCharsToAnalyze = byteCount;
	}

  public int getMaxDocCharsToAnalyze() {
    return maxDocCharsToAnalyze;
  }

  public void setMaxDocCharsToAnalyze(int maxDocCharsToAnalyze) {
    this.maxDocCharsToAnalyze = maxDocCharsToAnalyze;
  }

  /**
	 */
	public Fragmenter getTextFragmenter()
	{
		return textFragmenter;
	}

	/**
	 * @param fragmenter
	 */
	public void setTextFragmenter(Fragmenter fragmenter)
	{
		textFragmenter = fragmenter;
	}

	/**
	 * @return Object used to score each text fragment 
	 */
	public Scorer getFragmentScorer()
	{
		return fragmentScorer;
	}


	/**
	 * @param scorer
	 */
	public void setFragmentScorer(Scorer scorer)
	{
		fragmentScorer = scorer;
	}

    public Encoder getEncoder()
    {
        return encoder;
    }
    public void setEncoder(Encoder encoder)
    {
        this.encoder = encoder;
    }
}
class FragmentQueue extends PriorityQueue
{
	public FragmentQueue(int size)
	{
		initialize(size);
	}

	public final boolean lessThan(Object a, Object b)
	{
		TextFragment fragA = (TextFragment) a;
		TextFragment fragB = (TextFragment) b;
		if (fragA.getScore() == fragB.getScore())
			return fragA.fragNum > fragB.fragNum;
		else
			return fragA.getScore() < fragB.getScore();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2985.java