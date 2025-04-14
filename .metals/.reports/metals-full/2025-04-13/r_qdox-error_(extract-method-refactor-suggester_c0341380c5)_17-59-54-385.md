error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2898.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2898.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2898.java
text:
```scala
.@@setCollationQueryString((String) o));

package org.apache.solr.client.solrj.response;
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

import org.apache.solr.common.util.NamedList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates responses from SpellCheckComponent
 *
 * @version $Id$
 * @since solr 1.3
 */
public class SpellCheckResponse {
  private boolean correctlySpelled;
  private List<Collation> collations;
  private List<Suggestion> suggestions = new ArrayList<Suggestion>();
  Map<String, Suggestion> suggestionMap = new LinkedHashMap<String, Suggestion>();

  public SpellCheckResponse(NamedList<Object> spellInfo) {
    NamedList<Object> sugg = (NamedList<Object>) spellInfo.get("suggestions");
    if (sugg == null) {
      correctlySpelled = true;
      return;
    }
    for (int i = 0; i < sugg.size(); i++) {
      String n = sugg.getName(i);
      if ("correctlySpelled".equals(n)) {
        correctlySpelled = (Boolean) sugg.getVal(i);
			} else if ("collationInternalRank".equals(n)){
				//continue;
			} else if ("collation".equals(n)) {
				List<Object> collationInfo = sugg.getAll(n);
				collations = new ArrayList<Collation>(collationInfo.size());
				for (Object o : collationInfo) {
					if (o instanceof String) {
						collations.add(new Collation()
								.setCollationQueryString((String) sugg.getVal(i)));
					} else if (o instanceof NamedList) {
						NamedList expandedCollation = (NamedList) o;
						String collationQuery = (String) expandedCollation
								.get("collationQuery");
						int hits = (Integer) expandedCollation.get("hits");
						NamedList<String> misspellingsAndCorrections = (NamedList<String>) expandedCollation
								.get("misspellingsAndCorrections");

						Collation collation = new Collation();
						collation.setCollationQueryString(collationQuery);
						collation.setNumberOfHits(hits);

						for (int ii = 0; ii < misspellingsAndCorrections.size(); ii++) {
							String misspelling = misspellingsAndCorrections.getName(ii);
							String correction = misspellingsAndCorrections.getVal(ii);
							collation.addMisspellingsAndCorrection(new Correction(
									misspelling, correction));
						}
						collations.add(collation);
					} else {
						throw new AssertionError(
								"Should get Lists of Strings or List of NamedLists here.");
					}
				} 	
      } else {
        Suggestion s = new Suggestion(n, (NamedList<Object>) sugg.getVal(i));
        suggestionMap.put(n, s);
        suggestions.add(s);
      }
    }
  }

  public boolean isCorrectlySpelled() {
    return correctlySpelled;
  }

  public List<Suggestion> getSuggestions() {
    return suggestions;
  }

  public Map<String, Suggestion> getSuggestionMap() {
    return suggestionMap;
  }

  public Suggestion getSuggestion(String token) {
    return suggestionMap.get(token);
  }

  public String getFirstSuggestion(String token) {
    Suggestion s = suggestionMap.get(token);
    if (s==null || s.getAlternatives().isEmpty()) return null;
    return s.getAlternatives().get(0);
  }

  /**
   * <p>
   *  Return the first collated query string.  For convenience and backwards-compatibility.  Use getCollatedResults() for full data.
   * </p>
   * @return first collated query string
   */
  public String getCollatedResult() {
    return collations==null || collations.size()==0 ? null : collations.get(0).collationQueryString;
  }
  
  /**
   * <p>
   *  Return all collations.  
   *  Will include # of hits and misspelling-to-correction details if "spellcheck.collateExtendedResults was true.
   * </p>
   * @return all collations
   */
  public List<Collation> getCollatedResults() {
  	return collations;
  }

  public static class Suggestion {
    private String token;
    private int numFound;
    private int startOffset;
    private int endOffset;
    private int originalFrequency;
    private List<String> alternatives = new ArrayList<String>();
    private List<Integer> alternativeFrequencies;

    public Suggestion(String token, NamedList<Object> suggestion) {
      this.token = token;
      for (int i = 0; i < suggestion.size(); i++) {
        String n = suggestion.getName(i);

        if ("numFound".equals(n)) {
          numFound = (Integer) suggestion.getVal(i);
        } else if ("startOffset".equals(n)) {
          startOffset = (Integer) suggestion.getVal(i);
        } else if ("endOffset".equals(n)) {
          endOffset = (Integer) suggestion.getVal(i);
        } else if ("origFreq".equals(n)) {
          originalFrequency = (Integer) suggestion.getVal(i);
        } else if ("suggestion".equals(n)) {
          List list = (List)suggestion.getVal(i);
          if (list.size() > 0 && list.get(0) instanceof NamedList) {
            // extended results detected
            alternativeFrequencies = new ArrayList<Integer>();
            for (NamedList nl : (List<NamedList>)list) {
              alternatives.add((String)nl.get("word"));
              alternativeFrequencies.add((Integer)nl.get("freq"));
            }
          } else {
            alternatives.addAll(list);
          }
        }
      }
    }

    public String getToken() {
      return token;
    }

    public int getNumFound() {
      return numFound;
    }

    public int getStartOffset() {
      return startOffset;
    }

    public int getEndOffset() {
      return endOffset;
    }

    public int getOriginalFrequency() {
      return originalFrequency;
    }

    /** The list of alternatives */
    public List<String> getAlternatives() {
      return alternatives;
    }

    /** The frequencies of the alternatives in the corpus, or null if this information was not returned */
    public List<Integer> getAlternativeFrequencies() {
      return alternativeFrequencies;
    }

    @Deprecated
    /** @see #getAlternatives */
    public List<String> getSuggestions() {
      return alternatives;
    }

    @Deprecated
    /** @see #getAlternativeFrequencies */
    public List<Integer> getSuggestionFrequencies() {
      return alternativeFrequencies;
    }

  }

	public class Collation {
		private String collationQueryString;
		private List<Correction> misspellingsAndCorrections = new ArrayList<Correction>();
		private long numberOfHits;

		public long getNumberOfHits() {
			return numberOfHits;
		}

		public void setNumberOfHits(long numberOfHits) {
			this.numberOfHits = numberOfHits;
		}

		public String getCollationQueryString() {
			return collationQueryString;
		}

		public Collation setCollationQueryString(String collationQueryString) {
			this.collationQueryString = collationQueryString;
			return this;
		}

		public List<Correction> getMisspellingsAndCorrections() {
			return misspellingsAndCorrections;
		}

		public Collation addMisspellingsAndCorrection(Correction correction) {
			this.misspellingsAndCorrections.add(correction);
			return this;
		}

	}

	public class Correction {
		private String original;
		private String correction;

		public Correction(String original, String correction) {
			this.original = original;
			this.correction = correction;
		}

		public String getOriginal() {
			return original;
		}

		public void setOriginal(String original) {
			this.original = original;
		}

		public String getCorrection() {
			return correction;
		}

		public void setCorrection(String correction) {
			this.correction = correction;
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2898.java