error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3412.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3412.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3412.java
text:
```scala
t@@okenFiltersBindings.processTokenFilter("hyphenation_decompounder", HyphenationCompoundWordTokenFilterFactory.class);

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

import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.Scopes;
import org.elasticsearch.common.inject.assistedinject.FactoryProvider;
import org.elasticsearch.common.inject.multibindings.MapBinder;
import org.elasticsearch.common.settings.NoClassSettingsException;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.compound.DictionaryCompoundWordTokenFilterFactory;
import org.elasticsearch.index.analysis.compound.HyphenationCompoundWordTokenFilterFactory;
import org.elasticsearch.index.analysis.phonetic.PhoneticTokenFilterFactory;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;

import java.util.LinkedList;
import java.util.Map;

/**
 * @author kimchy (shay.banon)
 */
public class AnalysisModule extends AbstractModule {

    public static class AnalysisBinderProcessor {

        public void processCharFilters(CharFiltersBindings charFiltersBindings) {

        }

        public static class CharFiltersBindings {
            private final Map<String, Class<? extends CharFilterFactory>> charFilters = Maps.newHashMap();

            public CharFiltersBindings() {
            }

            public void processCharFilter(String name, Class<? extends CharFilterFactory> charFilterFactory) {
                charFilters.put(name, charFilterFactory);
            }
        }

        public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {

        }

        public static class TokenFiltersBindings {
            private final Map<String, Class<? extends TokenFilterFactory>> tokenFilters = Maps.newHashMap();

            public TokenFiltersBindings() {
            }

            public void processTokenFilter(String name, Class<? extends TokenFilterFactory> tokenFilterFactory) {
                tokenFilters.put(name, tokenFilterFactory);
            }
        }

        public void processTokenizers(TokenizersBindings tokenizersBindings) {

        }

        public static class TokenizersBindings {
            private final MapBinder<String, TokenizerFactoryFactory> binder;
            private final Map<String, Settings> groupSettings;
            private final IndicesAnalysisService indicesAnalysisService;

            public TokenizersBindings(MapBinder<String, TokenizerFactoryFactory> binder, Map<String, Settings> groupSettings, IndicesAnalysisService indicesAnalysisService) {
                this.binder = binder;
                this.groupSettings = groupSettings;
                this.indicesAnalysisService = indicesAnalysisService;
            }

            public void processTokenizer(String name, Class<? extends TokenizerFactory> tokenizerFactory) {
                if (!groupSettings.containsKey(name)) {
                    if (indicesAnalysisService != null && indicesAnalysisService.hasTokenizer(name)) {
                        // don't register it here, we will do it in AnalysisService
                        //binder.addBinding(name).toInstance(indicesAnalysisService.tokenizerFactoryFactory(name));
                    } else {
                        binder.addBinding(name).toProvider(FactoryProvider.newFactory(TokenizerFactoryFactory.class, tokenizerFactory)).in(Scopes.SINGLETON);
                    }
                }
            }
        }

        public void processAnalyzers(AnalyzersBindings analyzersBindings) {

        }

        public static class AnalyzersBindings {
            private final MapBinder<String, AnalyzerProviderFactory> binder;
            private final Map<String, Settings> groupSettings;
            private final IndicesAnalysisService indicesAnalysisService;

            public AnalyzersBindings(MapBinder<String, AnalyzerProviderFactory> binder, Map<String, Settings> groupSettings, IndicesAnalysisService indicesAnalysisService) {
                this.binder = binder;
                this.groupSettings = groupSettings;
                this.indicesAnalysisService = indicesAnalysisService;
            }

            public void processAnalyzer(String name, Class<? extends AnalyzerProvider> analyzerProvider) {
                if (!groupSettings.containsKey(name)) {
                    if (indicesAnalysisService != null && indicesAnalysisService.hasAnalyzer(name)) {
                        // don't register here, we will register it in the AnalysisService
                        //binder.addBinding(name).toInstance(indicesAnalysisService.analyzerProviderFactory(name));
                    } else {
                        binder.addBinding(name).toProvider(FactoryProvider.newFactory(AnalyzerProviderFactory.class, analyzerProvider)).in(Scopes.SINGLETON);
                    }
                }
            }
        }
    }

    private final Settings settings;

    private final IndicesAnalysisService indicesAnalysisService;

    private final LinkedList<AnalysisBinderProcessor> processors = Lists.newLinkedList();

    public AnalysisModule(Settings settings) {
        this(settings, null);
    }

    public AnalysisModule(Settings settings, IndicesAnalysisService indicesAnalysisService) {
        this.settings = settings;
        this.indicesAnalysisService = indicesAnalysisService;
        processors.add(new DefaultProcessor());
        try {
            processors.add(new ExtendedProcessor());
        } catch (Throwable t) {
            // ignore. no extended ones
        }
    }

    public AnalysisModule addProcessor(AnalysisBinderProcessor processor) {
        processors.addFirst(processor);
        return this;
    }

    @Override protected void configure() {
        MapBinder<String, CharFilterFactoryFactory> charFilterBinder
                = MapBinder.newMapBinder(binder(), String.class, CharFilterFactoryFactory.class);

        // CHAR FILTERS

        AnalysisBinderProcessor.CharFiltersBindings charFiltersBindings = new AnalysisBinderProcessor.CharFiltersBindings();
        for (AnalysisBinderProcessor processor : processors) {
            processor.processCharFilters(charFiltersBindings);
        }

        Map<String, Settings> charFiltersSettings = settings.getGroups("index.analysis.char_filter");
        for (Map.Entry<String, Settings> entry : charFiltersSettings.entrySet()) {
            String charFilterName = entry.getKey();
            Settings charFilterSettings = entry.getValue();

            Class<? extends CharFilterFactory> type = null;
            try {
                type = charFilterSettings.getAsClass("type", null, "org.elasticsearch.index.analysis.", "CharFilterFactory");
            } catch (NoClassSettingsException e) {
                // nothing found, see if its in bindings as a binding name
                if (charFilterSettings.get("type") != null) {
                    type = charFiltersBindings.charFilters.get(charFilterSettings.get("type"));
                }
            }
            if (type == null) {
                // nothing found, see if its in bindings as a binding name
                throw new ElasticSearchIllegalArgumentException("Char Filter [" + charFilterName + "] must have a type associated with it");
            }
            charFilterBinder.addBinding(charFilterName).toProvider(FactoryProvider.newFactory(CharFilterFactoryFactory.class, type)).in(Scopes.SINGLETON);
        }
        // go over the char filters in the bindings and register the ones that are not configured
        for (Map.Entry<String, Class<? extends CharFilterFactory>> entry : charFiltersBindings.charFilters.entrySet()) {
            String charFilterName = entry.getKey();
            Class<? extends CharFilterFactory> clazz = entry.getValue();
            // we don't want to re-register one that already exists
            if (charFiltersSettings.containsKey(charFilterName)) {
                continue;
            }
            // check, if it requires settings, then don't register it, we know default has no settings...
            if (clazz.getAnnotation(AnalysisSettingsRequired.class) != null) {
                continue;
            }
            // register it as default under the name
            if (indicesAnalysisService != null && indicesAnalysisService.hasCharFilter(charFilterName)) {
                // don't register it here, we will use explicitly register it in the AnalysisService
                //charFilterBinder.addBinding(charFilterName).toInstance(indicesAnalysisService.charFilterFactoryFactory(charFilterName));
            } else {
                charFilterBinder.addBinding(charFilterName).toProvider(FactoryProvider.newFactory(CharFilterFactoryFactory.class, clazz)).in(Scopes.SINGLETON);
            }
        }


        // TOKEN FILTERS

        MapBinder<String, TokenFilterFactoryFactory> tokenFilterBinder
                = MapBinder.newMapBinder(binder(), String.class, TokenFilterFactoryFactory.class);

        // initial default bindings
        AnalysisBinderProcessor.TokenFiltersBindings tokenFiltersBindings = new AnalysisBinderProcessor.TokenFiltersBindings();
        for (AnalysisBinderProcessor processor : processors) {
            processor.processTokenFilters(tokenFiltersBindings);
        }

        Map<String, Settings> tokenFiltersSettings = settings.getGroups("index.analysis.filter");
        for (Map.Entry<String, Settings> entry : tokenFiltersSettings.entrySet()) {
            String tokenFilterName = entry.getKey();
            Settings tokenFilterSettings = entry.getValue();

            Class<? extends TokenFilterFactory> type = null;
            try {
                type = tokenFilterSettings.getAsClass("type", null, "org.elasticsearch.index.analysis.", "TokenFilterFactory");
            } catch (NoClassSettingsException e) {
                // nothing found, see if its in bindings as a binding name
                if (tokenFilterSettings.get("type") != null) {
                    type = tokenFiltersBindings.tokenFilters.get(tokenFilterSettings.get("type"));
                }
                if (type == null) {
                    throw new ElasticSearchIllegalArgumentException("failed to find token filter type for [" + tokenFilterName + "]", e);
                }
            }
            if (type == null) {
                throw new ElasticSearchIllegalArgumentException("token filter [" + tokenFilterName + "] must have a type associated with it");
            }
            tokenFilterBinder.addBinding(tokenFilterName).toProvider(FactoryProvider.newFactory(TokenFilterFactoryFactory.class, type)).in(Scopes.SINGLETON);
        }
        // go over the filters in the bindings and register the ones that are not configured
        for (Map.Entry<String, Class<? extends TokenFilterFactory>> entry : tokenFiltersBindings.tokenFilters.entrySet()) {
            String tokenFilterName = entry.getKey();
            Class<? extends TokenFilterFactory> clazz = entry.getValue();
            // we don't want to re-register one that already exists
            if (tokenFiltersSettings.containsKey(tokenFilterName)) {
                continue;
            }
            // check, if it requires settings, then don't register it, we know default has no settings...
            if (clazz.getAnnotation(AnalysisSettingsRequired.class) != null) {
                continue;
            }
            // register it as default under the name
            if (indicesAnalysisService != null && indicesAnalysisService.hasTokenFilter(tokenFilterName)) {
                // don't register it here, we will use explicitly register it in the AnalysisService
                // tokenFilterBinder.addBinding(tokenFilterName).toInstance(indicesAnalysisService.tokenFilterFactoryFactory(tokenFilterName));
            } else {
                tokenFilterBinder.addBinding(tokenFilterName).toProvider(FactoryProvider.newFactory(TokenFilterFactoryFactory.class, clazz)).in(Scopes.SINGLETON);
            }
        }

        // TOKENIZER

        MapBinder<String, TokenizerFactoryFactory> tokenizerBinder
                = MapBinder.newMapBinder(binder(), String.class, TokenizerFactoryFactory.class);

        Map<String, Settings> tokenizersSettings = settings.getGroups("index.analysis.tokenizer");
        for (Map.Entry<String, Settings> entry : tokenizersSettings.entrySet()) {
            String tokenizerName = entry.getKey();
            Settings tokenizerSettings = entry.getValue();

            try {
                Class<? extends TokenizerFactory> type = tokenizerSettings.getAsClass("type", null, "org.elasticsearch.index.analysis.", "TokenizerFactory");
                if (type == null) {
                    throw new ElasticSearchIllegalArgumentException("Tokenizer [" + tokenizerName + "] must have a type associated with it");
                }

                // if it requires settings, and it has none, then don't register it
                if (tokenizerSettings.getAsMap().isEmpty() && type.getAnnotation(AnalysisSettingsRequired.class) != null) {
                    continue;
                }

                tokenizerBinder.addBinding(tokenizerName).toProvider(FactoryProvider.newFactory(TokenizerFactoryFactory.class, type)).in(Scopes.SINGLETON);
            } catch (NoClassSettingsException e) {
                throw new ElasticSearchIllegalArgumentException("failed to find tokenizer type for [" + tokenizerName + "]", e);
            }
        }

        AnalysisBinderProcessor.TokenizersBindings tokenizersBindings = new AnalysisBinderProcessor.TokenizersBindings(tokenizerBinder, tokenizersSettings, indicesAnalysisService);
        for (AnalysisBinderProcessor processor : processors) {
            processor.processTokenizers(tokenizersBindings);
        }

        // ANALYZER

        MapBinder<String, AnalyzerProviderFactory> analyzerBinder
                = MapBinder.newMapBinder(binder(), String.class, AnalyzerProviderFactory.class);

        Map<String, Settings> analyzersSettings = settings.getGroups("index.analysis.analyzer");
        for (Map.Entry<String, Settings> entry : analyzersSettings.entrySet()) {
            String analyzerName = entry.getKey();
            Settings analyzerSettings = entry.getValue();
            try {
                Class<? extends AnalyzerProvider> type = analyzerSettings.getAsClass("type", null, "org.elasticsearch.index.analysis.", "AnalyzerProvider");
                if (type == null) {
                    // no specific type, check if it has a tokenizer associated with it
                    String tokenizerName = analyzerSettings.get("tokenizer");
                    if (tokenizerName != null) {
                        // we have a tokenizer, use the CustomAnalyzer
                        type = CustomAnalyzerProvider.class;
                    } else {
                        throw new ElasticSearchIllegalArgumentException("analyzer [" + analyzerName + "] must have a type associated with it or a tokenizer");
                    }
                }
                analyzerBinder.addBinding(analyzerName).toProvider(FactoryProvider.newFactory(AnalyzerProviderFactory.class, type)).in(Scopes.SINGLETON);
            } catch (NoClassSettingsException e) {
                throw new ElasticSearchIllegalArgumentException("failed to find analyzer type for [" + analyzerName + "]", e);
            }
        }

        AnalysisBinderProcessor.AnalyzersBindings analyzersBindings = new AnalysisBinderProcessor.AnalyzersBindings(analyzerBinder, analyzersSettings, indicesAnalysisService);
        for (AnalysisBinderProcessor processor : processors) {
            processor.processAnalyzers(analyzersBindings);
        }

        bind(AnalysisService.class).in(Scopes.SINGLETON);
    }

    private static class DefaultProcessor extends AnalysisBinderProcessor {

        @Override public void processCharFilters(CharFiltersBindings charFiltersBindings) {
            charFiltersBindings.processCharFilter("html_strip", HtmlStripCharFilterFactory.class);
        }

        @Override public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
            tokenFiltersBindings.processTokenFilter("stop", StopTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("reverse", ReverseTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("asciifolding", ASCIIFoldingTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("length", LengthTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("lowercase", LowerCaseTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("porter_stem", PorterStemTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("kstem", KStemTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("standard", StandardTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("nGram", NGramTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("ngram", NGramTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("edgeNGram", EdgeNGramTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("edge_ngram", EdgeNGramTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("shingle", ShingleTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("unique", UniqueTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("truncate", TruncateTokenFilterFactory.class);
        }

        @Override public void processTokenizers(TokenizersBindings tokenizersBindings) {
            tokenizersBindings.processTokenizer("standard", StandardTokenizerFactory.class);
            tokenizersBindings.processTokenizer("uax_url_email", UAX29URLEmailTokenizerFactory.class);
            tokenizersBindings.processTokenizer("path_hierarchy", PathHierarchyTokenizerFactory.class);
            tokenizersBindings.processTokenizer("keyword", KeywordTokenizerFactory.class);
            tokenizersBindings.processTokenizer("letter", LetterTokenizerFactory.class);
            tokenizersBindings.processTokenizer("lowercase", LowerCaseTokenizerFactory.class);
            tokenizersBindings.processTokenizer("whitespace", WhitespaceTokenizerFactory.class);

            tokenizersBindings.processTokenizer("nGram", NGramTokenizerFactory.class);
            tokenizersBindings.processTokenizer("ngram", NGramTokenizerFactory.class);
            tokenizersBindings.processTokenizer("edgeNGram", EdgeNGramTokenizerFactory.class);
            tokenizersBindings.processTokenizer("edge_ngram", EdgeNGramTokenizerFactory.class);
        }

        @Override public void processAnalyzers(AnalyzersBindings analyzersBindings) {
            analyzersBindings.processAnalyzer("default", StandardAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("standard", StandardAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("standard_html_strip", StandardHtmlStripAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("simple", SimpleAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("stop", StopAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("whitespace", WhitespaceAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("keyword", KeywordAnalyzerProvider.class);
        }
    }

    private static class ExtendedProcessor extends AnalysisBinderProcessor {
        @Override public void processCharFilters(CharFiltersBindings charFiltersBindings) {
            charFiltersBindings.processCharFilter("mapping", MappingCharFilterFactory.class);
        }

        @Override public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
            tokenFiltersBindings.processTokenFilter("snowball", SnowballTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("stemmer", StemmerTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("word_delimiter", WordDelimiterTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("synonym", SynonymTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("elision", ElisionTokenFilterFactory.class);

            tokenFiltersBindings.processTokenFilter("pattern_replace", PatternReplaceTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("phonetic", PhoneticTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("dictionary_decompounder", DictionaryCompoundWordTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("hypennation_decompounder", HyphenationCompoundWordTokenFilterFactory.class);

            tokenFiltersBindings.processTokenFilter("arabic_stem", ArabicStemTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("brazilian_stem", BrazilianStemTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("czech_stem", CzechStemTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("dutch_stem", DutchStemTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("french_stem", FrenchStemTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("german_stem", GermanStemTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("russian_stem", RussianStemTokenFilterFactory.class);

            tokenFiltersBindings.processTokenFilter("keyword_marker", KeywordMarkerTokenFilterFactory.class);
            tokenFiltersBindings.processTokenFilter("stemmer_override", StemmerOverrideTokenFilterFactory.class);
        }

        @Override public void processTokenizers(TokenizersBindings tokenizersBindings) {
            tokenizersBindings.processTokenizer("pattern", PatternTokenizerFactory.class);
        }

        @Override public void processAnalyzers(AnalyzersBindings analyzersBindings) {
            analyzersBindings.processAnalyzer("pattern", PatternAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("snowball", SnowballAnalyzerProvider.class);

            analyzersBindings.processAnalyzer("arabic", ArabicAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("armenian", ArmenianAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("basque", BasqueAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("brazilian", BrazilianAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("bulgarian", BulgarianAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("catalan", CatalanAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("chinese", ChineseAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("cjk", CjkAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("czech", CzechAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("danish", DanishAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("dutch", DutchAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("english", EnglishAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("finnish", FinnishAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("french", FrenchAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("galician", GalicianAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("german", GermanAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("greek", GreekAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("hindi", HindiAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("hungarian", HungarianAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("indonesian", IndonesianAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("italian", ItalianAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("norwegian", NorwegianAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("persian", PersianAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("portuguese", PortugueseAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("romanian", RomanianAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("russian", RussianAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("spanish", SpanishAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("swedish", SwedishAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("turkish", TurkishAnalyzerProvider.class);
            analyzersBindings.processAnalyzer("thai", ThaiAnalyzerProvider.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3412.java