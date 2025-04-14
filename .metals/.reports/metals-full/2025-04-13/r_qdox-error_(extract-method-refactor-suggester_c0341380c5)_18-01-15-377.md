error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5408.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5408.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5408.java
text:
```scala
a@@nalyzerProviders.put("default", new StandardAnalyzerProvider(index, indexSettings, null, "default", ImmutableSettings.Builder.EMPTY_SETTINGS));

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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

package org.elasticsearch.index.analysis;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.collect.ImmutableMap;
import org.elasticsearch.common.component.CloseableComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;

import java.util.Map;

import static org.elasticsearch.common.collect.Maps.*;

/**
 * @author kimchy (Shay Banon)
 */
public class AnalysisService extends AbstractIndexComponent implements CloseableComponent {

    private final ImmutableMap<String, NamedAnalyzer> analyzers;

    private final ImmutableMap<String, TokenizerFactory> tokenizers;

    private final ImmutableMap<String, CharFilterFactory> charFilters;

    private final ImmutableMap<String, TokenFilterFactory> tokenFilters;

    public AnalysisService(Index index) {
        this(index, ImmutableSettings.Builder.EMPTY_SETTINGS, null, null, null, null);
    }

    @Inject public AnalysisService(Index index, @IndexSettings Settings indexSettings,
                                   @Nullable Map<String, AnalyzerProviderFactory> analyzerFactoryFactories,
                                   @Nullable Map<String, TokenizerFactoryFactory> tokenizerFactoryFactories,
                                   @Nullable Map<String, CharFilterFactoryFactory> charFilterFactoryFactories,
                                   @Nullable Map<String, TokenFilterFactoryFactory> tokenFilterFactoryFactories) {
        super(index, indexSettings);

        Map<String, AnalyzerProvider> analyzerProviders = newHashMap();
        if (analyzerFactoryFactories != null) {
            Map<String, Settings> analyzersSettings = indexSettings.getGroups("index.analysis.analyzer");
            for (Map.Entry<String, AnalyzerProviderFactory> entry : analyzerFactoryFactories.entrySet()) {
                String analyzerName = entry.getKey();
                AnalyzerProviderFactory analyzerFactoryFactory = entry.getValue();

                Settings analyzerSettings = analyzersSettings.get(analyzerName);
                if (analyzerSettings == null) {
                    analyzerSettings = ImmutableSettings.Builder.EMPTY_SETTINGS;
                }

                AnalyzerProvider analyzerFactory = analyzerFactoryFactory.create(analyzerName, analyzerSettings);
                analyzerProviders.put(analyzerName, analyzerFactory);
            }
        }

        if (!analyzerProviders.containsKey("default")) {
            analyzerProviders.put("default", new StandardAnalyzerProvider(index, indexSettings, "default", ImmutableSettings.Builder.EMPTY_SETTINGS));
        }
        if (!analyzerProviders.containsKey("default_index")) {
            analyzerProviders.put("default_index", analyzerProviders.get("default"));
        }
        if (!analyzerProviders.containsKey("default_search")) {
            analyzerProviders.put("default_search", analyzerProviders.get("default"));
        }

        Map<String, NamedAnalyzer> analyzers = newHashMap();
        for (AnalyzerProvider analyzerFactory : analyzerProviders.values()) {
            NamedAnalyzer analyzer = new NamedAnalyzer(analyzerFactory.name(), analyzerFactory.scope(), analyzerFactory.get());
            analyzers.put(analyzerFactory.name(), analyzer);
            analyzers.put(Strings.toCamelCase(analyzerFactory.name()), analyzer);
            String strAliases = indexSettings.get("index.analysis.analyzer." + analyzerFactory.name() + ".alias");
            if (strAliases != null) {
                for (String alias : Strings.commaDelimitedListToStringArray(strAliases)) {
                    analyzers.put(alias, analyzer);
                }
            }
            String[] aliases = indexSettings.getAsArray("index.analysis.analyzer." + analyzerFactory.name() + ".alias");
            for (String alias : aliases) {
                analyzers.put(alias, analyzer);
            }
        }
        this.analyzers = ImmutableMap.copyOf(analyzers);

        Map<String, TokenizerFactory> tokenizers = newHashMap();
        if (tokenizerFactoryFactories != null) {
            Map<String, Settings> tokenizersSettings = indexSettings.getGroups("index.analysis.tokenizer");
            for (Map.Entry<String, TokenizerFactoryFactory> entry : tokenizerFactoryFactories.entrySet()) {
                String tokenizerName = entry.getKey();
                TokenizerFactoryFactory tokenizerFactoryFactory = entry.getValue();

                Settings tokenizerSettings = tokenizersSettings.get(tokenizerName);
                if (tokenizerSettings == null) {
                    tokenizerSettings = ImmutableSettings.Builder.EMPTY_SETTINGS;
                }

                TokenizerFactory tokenizerFactory = tokenizerFactoryFactory.create(tokenizerName, tokenizerSettings);
                tokenizers.put(tokenizerName, tokenizerFactory);
                tokenizers.put(Strings.toCamelCase(tokenizerName), tokenizerFactory);
            }
        }
        this.tokenizers = ImmutableMap.copyOf(tokenizers);

        Map<String, CharFilterFactory> charFilters = newHashMap();
        if (charFilterFactoryFactories != null) {
            Map<String, Settings> charFiltersSettings = indexSettings.getGroups("index.analysis.char_filter");
            for (Map.Entry<String, CharFilterFactoryFactory> entry : charFilterFactoryFactories.entrySet()) {
                String charFilterName = entry.getKey();
                CharFilterFactoryFactory charFilterFactoryFactory = entry.getValue();

                Settings charFilterSettings = charFiltersSettings.get(charFilterName);
                if (charFilterSettings == null) {
                    charFilterSettings = ImmutableSettings.Builder.EMPTY_SETTINGS;
                }

                CharFilterFactory tokenFilterFactory = charFilterFactoryFactory.create(charFilterName, charFilterSettings);
                charFilters.put(charFilterName, tokenFilterFactory);
                charFilters.put(Strings.toCamelCase(charFilterName), tokenFilterFactory);
            }
        }
        this.charFilters = ImmutableMap.copyOf(charFilters);

        Map<String, TokenFilterFactory> tokenFilters = newHashMap();
        if (tokenFilterFactoryFactories != null) {
            Map<String, Settings> tokenFiltersSettings = indexSettings.getGroups("index.analysis.filter");
            for (Map.Entry<String, TokenFilterFactoryFactory> entry : tokenFilterFactoryFactories.entrySet()) {
                String tokenFilterName = entry.getKey();
                TokenFilterFactoryFactory tokenFilterFactoryFactory = entry.getValue();

                Settings tokenFilterSettings = tokenFiltersSettings.get(tokenFilterName);
                if (tokenFilterSettings == null) {
                    tokenFilterSettings = ImmutableSettings.Builder.EMPTY_SETTINGS;
                }

                TokenFilterFactory tokenFilterFactory = tokenFilterFactoryFactory.create(tokenFilterName, tokenFilterSettings);
                tokenFilters.put(tokenFilterName, tokenFilterFactory);
                tokenFilters.put(Strings.toCamelCase(tokenFilterName), tokenFilterFactory);
            }
        }
        this.tokenFilters = ImmutableMap.copyOf(tokenFilters);
    }

    public void close() {
        for (NamedAnalyzer analyzer : analyzers.values()) {
            if (analyzer.scope() == AnalyzerScope.INDEX) {
                try {
                    analyzer.close();
                } catch (NullPointerException e) {
                    // because analyzers are aliased, they might be closed several times
                    // an NPE is thrown in this case, so ignore....
                } catch (Exception e) {
                    logger.debug("failed to close analyzer " + analyzer);
                }
            }
        }
    }

    public NamedAnalyzer analyzer(String name) {
        return analyzers.get(name);
    }

    public NamedAnalyzer defaultAnalyzer() {
        return analyzers.get("default");
    }

    public NamedAnalyzer defaultIndexAnalyzer() {
        return defaultAnalyzer();
    }

    public NamedAnalyzer defaultSearchAnalyzer() {
        return defaultAnalyzer();
    }

    public TokenizerFactory tokenizer(String name) {
        return tokenizers.get(name);
    }

    public CharFilterFactory charFilter(String name) {
        return charFilters.get(name);
    }

    public TokenFilterFactory tokenFilter(String name) {
        return tokenFilters.get(name);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5408.java