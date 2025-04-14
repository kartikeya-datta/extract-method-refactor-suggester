error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7416.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7416.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7416.java
text:
```scala
private L@@ist<DirectCandidateGenerator> generators = new ArrayList<>();

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.search.suggest.phrase;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.search.suggest.DirectSpellcheckerSettings;
import org.elasticsearch.search.suggest.Suggester;
import org.elasticsearch.search.suggest.SuggestionSearchContext.SuggestionContext;

class PhraseSuggestionContext extends SuggestionContext {
    private final BytesRef SEPARATOR = new BytesRef(" ");

    private float maxErrors = 0.5f;
    private BytesRef separator = SEPARATOR;
    private float realworldErrorLikelihood = 0.95f;
    private List<DirectCandidateGenerator> generators = new ArrayList<PhraseSuggestionContext.DirectCandidateGenerator>();
    private int gramSize = 1;
    private float confidence = 1.0f;
    private int tokenLimit = NoisyChannelSpellChecker.DEFAULT_TOKEN_LIMIT;
    private BytesRef preTag;
    private BytesRef postTag;

    private WordScorer.WordScorerFactory scorer;

    private boolean requireUnigram = true;

    public PhraseSuggestionContext(Suggester<? extends PhraseSuggestionContext> suggester) {
        super(suggester);
    }

    public float maxErrors() {
        return maxErrors;
    }

    public void setMaxErrors(Float maxErrors) {
        this.maxErrors = maxErrors;
    }

    public BytesRef separator() {
        return separator;
    }

    public void setSeparator(BytesRef separator) {
        this.separator = separator;
    }

    public Float realworldErrorLikelyhood() {
        return realworldErrorLikelihood;
    }

    public void setRealWordErrorLikelihood(Float realworldErrorLikelihood) {
        this.realworldErrorLikelihood = realworldErrorLikelihood;
    }

    public void addGenerator(DirectCandidateGenerator generator) {
        this.generators.add(generator);
    }
    
    public List<DirectCandidateGenerator> generators() {
        return this.generators ;
    }
    
    public void setGramSize(int gramSize) {
        this.gramSize = gramSize;
    }
    
    public int gramSize() {
        return gramSize;
    }
    
    public float confidence() {
        return confidence;
    }
    
    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }
    
    public void setModel(WordScorer.WordScorerFactory scorer) {
        this.scorer = scorer;
    }

    public WordScorer.WordScorerFactory model() {
        return scorer;
    }
    
    static class DirectCandidateGenerator extends DirectSpellcheckerSettings {
        private Analyzer preFilter;
        private Analyzer postFilter;
        private String field;
        private int size = 5;

        public String field() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public int size() {
            return size;
        }

        public void size(int size) {
            if (size <= 0) {
                throw new ElasticsearchIllegalArgumentException("Size must be positive");
            }
            this.size = size;
        }
        
        public Analyzer preFilter() {
            return preFilter;
        }

        public void preFilter(Analyzer preFilter) {
            this.preFilter = preFilter;
        }

        public Analyzer postFilter() {
            return postFilter;
        }

        public void postFilter(Analyzer postFilter) {
            this.postFilter = postFilter;
        }
        
        
    }

    public void setRequireUnigram(boolean requireUnigram) {
        this.requireUnigram  = requireUnigram;
    }
    
    public boolean getRequireUnigram() {
        return requireUnigram;
    }
    
    public void setTokenLimit(int tokenLimit) {
        this.tokenLimit = tokenLimit;
    }
   
    public int getTokenLimit() {
        return tokenLimit;
    }

    public void setPreTag(BytesRef preTag) {
        this.preTag = preTag;
    }

    public BytesRef getPreTag() {
        return preTag;
    }

    public void setPostTag(BytesRef postTag) {
        this.postTag = postTag;
    }

    public BytesRef getPostTag() {
        return postTag;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7416.java