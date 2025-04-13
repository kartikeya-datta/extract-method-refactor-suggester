error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1238.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1238.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[21,1]

error in qdox parser
file content:
```java
offset: 868
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1238.java
text:
```scala
public class CustomPassageFormatter extends PassageFormatter {

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

p@@ackage org.apache.lucene.search.postingshighlight;

import org.apache.lucene.search.highlight.Encoder;
import org.elasticsearch.search.highlight.HighlightUtils;

/**
Custom passage formatter that allows us to:
1) extract different snippets (instead of a single big string) together with their scores ({@link Snippet})
2) use the {@link Encoder} implementations that are already used with the other highlighters
 */
public class CustomPassageFormatter extends XPassageFormatter {

    private final String preTag;
    private final String postTag;
    private final Encoder encoder;

    public CustomPassageFormatter(String preTag, String postTag, Encoder encoder) {
        this.preTag = preTag;
        this.postTag = postTag;
        this.encoder = encoder;
    }

    @Override
    public Snippet[] format(Passage[] passages, String content) {
        Snippet[] snippets = new Snippet[passages.length];
        int pos;
        for (int j = 0; j < passages.length; j++) {
            Passage passage = passages[j];
            StringBuilder sb = new StringBuilder();
            pos = passage.startOffset;
            for (int i = 0; i < passage.numMatches; i++) {
                int start = passage.matchStarts[i];
                int end = passage.matchEnds[i];
                // its possible to have overlapping terms
                if (start > pos) {
                    append(sb, content, pos, start);
                }
                if (end > pos) {
                    sb.append(preTag);
                    append(sb, content, Math.max(pos, start), end);
                    sb.append(postTag);
                    pos = end;
                }
            }
            // its possible a "term" from the analyzer could span a sentence boundary.
            append(sb, content, pos, Math.max(pos, passage.endOffset));
            //we remove the paragraph separator if present at the end of the snippet (we used it as separator between values)
            if (sb.charAt(sb.length() - 1) == HighlightUtils.PARAGRAPH_SEPARATOR) {
                sb.deleteCharAt(sb.length() - 1);
            }
            //and we trim the snippets too
            snippets[j] = new Snippet(sb.toString().trim(), passage.score, passage.numMatches > 0);
        }
        return snippets;
    }

    protected void append(StringBuilder dest, String content, int start, int end) {
        dest.append(encoder.encodeText(content.substring(start, end)));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1238.java