error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4133.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4133.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4133.java
text:
```scala
r@@eturn org.elasticsearch.Version.indexCreated(indexSettings).luceneVersion;

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

package org.elasticsearch.index.analysis;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.NumericTokenStream;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.analysis.bg.BulgarianAnalyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.ca.CatalanAnalyzer;
import org.apache.lucene.analysis.ckb.SoraniAnalyzer;
import org.apache.lucene.analysis.cz.CzechAnalyzer;
import org.apache.lucene.analysis.da.DanishAnalyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.el.GreekAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.eu.BasqueAnalyzer;
import org.apache.lucene.analysis.fa.PersianAnalyzer;
import org.apache.lucene.analysis.fi.FinnishAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.ga.IrishAnalyzer;
import org.apache.lucene.analysis.gl.GalicianAnalyzer;
import org.apache.lucene.analysis.hi.HindiAnalyzer;
import org.apache.lucene.analysis.hu.HungarianAnalyzer;
import org.apache.lucene.analysis.hy.ArmenianAnalyzer;
import org.apache.lucene.analysis.id.IndonesianAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.lv.LatvianAnalyzer;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.analysis.no.NorwegianAnalyzer;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.ro.RomanianAnalyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.sv.SwedishAnalyzer;
import org.apache.lucene.analysis.th.ThaiAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tr.TurkishAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;
import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.collect.MapBuilder;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.settings.IndexSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.*;

/**
 *
 */
public class Analysis {

    public static Version parseAnalysisVersion(@IndexSettings Settings indexSettings, Settings settings, ESLogger logger) {
        // check for explicit version on the specific analyzer component
        String sVersion = settings.get("version");
        if (sVersion != null) {
            return Lucene.parseVersion(sVersion, Lucene.ANALYZER_VERSION, logger);
        }
        // check for explicit version on the index itself as default for all analysis components
        sVersion = indexSettings.get("index.analysis.version");
        if (sVersion != null) {
            return Lucene.parseVersion(sVersion, Lucene.ANALYZER_VERSION, logger);
        }
        // resolve the analysis version based on the version the index was created with
        return indexSettings.getAsVersion(IndexMetaData.SETTING_VERSION_CREATED, org.elasticsearch.Version.CURRENT).luceneVersion;
    }

    public static boolean isNoStopwords(Settings settings) {
        String value = settings.get("stopwords");
        return value != null && "_none_".equals(value);
    }

    public static CharArraySet parseStemExclusion(Settings settings, CharArraySet defaultStemExclusion, Version version) {
        String value = settings.get("stem_exclusion");
        if (value != null) {
            if ("_none_".equals(value)) {
                return CharArraySet.EMPTY_SET;
            } else {
                // LUCENE 4 UPGRADE: Should be settings.getAsBoolean("stem_exclusion_case", false)?
                return new CharArraySet(version, Strings.commaDelimitedListToSet(value), false);
            }
        }
        String[] stemExclusion = settings.getAsArray("stem_exclusion", null);
        if (stemExclusion != null) {
            // LUCENE 4 UPGRADE: Should be settings.getAsBoolean("stem_exclusion_case", false)?
            return new CharArraySet(version, Arrays.asList(stemExclusion), false);
        } else {
            return defaultStemExclusion;
        }
    }

    public static final ImmutableMap<String, Set<?>> namedStopWords = MapBuilder.<String, Set<?>>newMapBuilder()
            .put("_arabic_", ArabicAnalyzer.getDefaultStopSet())
            .put("_armenian_", ArmenianAnalyzer.getDefaultStopSet())
            .put("_basque_", BasqueAnalyzer.getDefaultStopSet())
            .put("_brazilian_", BrazilianAnalyzer.getDefaultStopSet())
            .put("_bulgarian_", BulgarianAnalyzer.getDefaultStopSet())
            .put("_catalan_", CatalanAnalyzer.getDefaultStopSet())
            .put("_czech_", CzechAnalyzer.getDefaultStopSet())
            .put("_danish_", DanishAnalyzer.getDefaultStopSet())
            .put("_dutch_", DutchAnalyzer.getDefaultStopSet())
            .put("_english_", EnglishAnalyzer.getDefaultStopSet())
            .put("_finnish_", FinnishAnalyzer.getDefaultStopSet())
            .put("_french_", FrenchAnalyzer.getDefaultStopSet())
            .put("_galician_", GalicianAnalyzer.getDefaultStopSet())
            .put("_german_", GermanAnalyzer.getDefaultStopSet())
            .put("_greek_", GreekAnalyzer.getDefaultStopSet())
            .put("_hindi_", HindiAnalyzer.getDefaultStopSet())
            .put("_hungarian_", HungarianAnalyzer.getDefaultStopSet())
            .put("_indonesian_", IndonesianAnalyzer.getDefaultStopSet())
            .put("_irish_", IrishAnalyzer.getDefaultStopSet())
            .put("_italian_", ItalianAnalyzer.getDefaultStopSet())
            .put("_latvian_", LatvianAnalyzer.getDefaultStopSet())
            .put("_norwegian_", NorwegianAnalyzer.getDefaultStopSet())
            .put("_persian_", PersianAnalyzer.getDefaultStopSet())
            .put("_portuguese_", PortugueseAnalyzer.getDefaultStopSet())
            .put("_romanian_", RomanianAnalyzer.getDefaultStopSet())
            .put("_russian_", RussianAnalyzer.getDefaultStopSet())
            .put("_sorani_", SoraniAnalyzer.getDefaultStopSet())
            .put("_spanish_", SpanishAnalyzer.getDefaultStopSet())
            .put("_swedish_", SwedishAnalyzer.getDefaultStopSet())
            .put("_thai_", ThaiAnalyzer.getDefaultStopSet())
            .put("_turkish_", TurkishAnalyzer.getDefaultStopSet())
            .immutableMap();

    public static CharArraySet parseWords(Environment env, Settings settings, String name, CharArraySet defaultWords, ImmutableMap<String, Set<?>> namedWords, Version version, boolean ignoreCase) {
        String value = settings.get(name);
        if (value != null) {
            if ("_none_".equals(value)) {
                return CharArraySet.EMPTY_SET;
            } else {
                return resolveNamedWords(Strings.commaDelimitedListToSet(value), namedWords, version, ignoreCase);
            }
        }
        List<String> pathLoadedWords = getWordList(env, settings, name);
        if (pathLoadedWords != null) {
            return resolveNamedWords(pathLoadedWords, namedWords, version, ignoreCase);
        }
        return defaultWords;
    }

    public static CharArraySet parseCommonWords(Environment env, Settings settings, CharArraySet defaultCommonWords, Version version, boolean ignoreCase) {
        return parseWords(env, settings, "common_words", defaultCommonWords, namedStopWords, version, ignoreCase);
    }

    public static CharArraySet parseArticles(Environment env, Settings settings, Version version) {
        return parseWords(env, settings, "articles", null, null, version, settings.getAsBoolean("articles_case", false));
    }

    public static CharArraySet parseStopWords(Environment env, Settings settings, CharArraySet defaultStopWords, Version version) {
        return parseStopWords(env, settings, defaultStopWords, version, settings.getAsBoolean("stopwords_case", false));
    }

    public static CharArraySet parseStopWords(Environment env, Settings settings, CharArraySet defaultStopWords, Version version, boolean ignoreCase) {
        return parseWords(env, settings, "stopwords", defaultStopWords, namedStopWords, version, ignoreCase);
    }

    private static CharArraySet resolveNamedWords(Collection<String> words, ImmutableMap<String, Set<?>> namedWords, Version version, boolean ignoreCase) {
        if (namedWords == null) {
            return new CharArraySet(version, words, ignoreCase);
        }
        CharArraySet setWords = new CharArraySet(version, words.size(), ignoreCase);
        for (String word : words) {
            if (namedWords.containsKey(word)) {
                setWords.addAll(namedWords.get(word));
            } else {
                setWords.add(word);
            }
        }
        return setWords;
    }

    public static CharArraySet getWordSet(Environment env, Settings settings, String settingsPrefix, Version version) {
        List<String> wordList = getWordList(env, settings, settingsPrefix);
        if (wordList == null) {
            return null;
        }
        return new CharArraySet(version, wordList, settings.getAsBoolean(settingsPrefix + "_case", false));
    }

    /**
     * Fetches a list of words from the specified settings file. The list should either be available at the key
     * specified by settingsPrefix or in a file specified by settingsPrefix + _path.
     *
     * @throws org.elasticsearch.ElasticsearchIllegalArgumentException
     *          If the word list cannot be found at either key.
     */
    public static List<String> getWordList(Environment env, Settings settings, String settingPrefix) {
        String wordListPath = settings.get(settingPrefix + "_path", null);

        if (wordListPath == null) {
            String[] explicitWordList = settings.getAsArray(settingPrefix, null);
            if (explicitWordList == null) {
                return null;
            } else {
                return Arrays.asList(explicitWordList);
            }
        }

        URL wordListFile = env.resolveConfig(wordListPath);

        try {
            return loadWordList(new InputStreamReader(wordListFile.openStream(), Charsets.UTF_8), "#");
        } catch (IOException ioe) {
            String message = String.format(Locale.ROOT, "IOException while reading %s_path: %s", settingPrefix, ioe.getMessage());
            throw new ElasticsearchIllegalArgumentException(message);
        }
    }

    public static List<String> loadWordList(Reader reader, String comment) throws IOException {
        final List<String> result = new ArrayList<>();
        BufferedReader br = null;
        try {
            if (reader instanceof BufferedReader) {
                br = (BufferedReader) reader;
            } else {
                br = new BufferedReader(reader);
            }
            String word = null;
            while ((word = br.readLine()) != null) {
                if (!Strings.hasText(word)) {
                    continue;
                }
                if (!word.startsWith(comment)) {
                    result.add(word.trim());
                }
            }
        } finally {
            if (br != null)
                br.close();
        }
        return result;
    }

    /**
     * @return null If no settings set for "settingsPrefix" then return <code>null</code>.
     * @throws org.elasticsearch.ElasticsearchIllegalArgumentException
     *          If the Reader can not be instantiated.
     */
    public static Reader getReaderFromFile(Environment env, Settings settings, String settingPrefix) {
        String filePath = settings.get(settingPrefix, null);

        if (filePath == null) {
            return null;
        }

        URL fileUrl = env.resolveConfig(filePath);

        Reader reader = null;
        try {
            reader = new InputStreamReader(fileUrl.openStream(), Charsets.UTF_8);
        } catch (IOException ioe) {
            String message = String.format(Locale.ROOT, "IOException while reading %s_path: %s", settingPrefix, ioe.getMessage());
            throw new ElasticsearchIllegalArgumentException(message);
        }

        return reader;
    }

    /**
     * Check whether the provided token stream is able to provide character
     * terms.
     * <p>Although most analyzers generate character terms (CharTermAttribute),
     * some token only contain binary terms (BinaryTermAttribute,
     * CharTermAttribute being a special type of BinaryTermAttribute), such as
     * {@link NumericTokenStream} and unsuitable for highlighting and
     * more-like-this queries which expect character terms.</p>
     */
    public static boolean isCharacterTokenStream(TokenStream tokenStream) {
        try {
            tokenStream.addAttribute(CharTermAttribute.class);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Check whether {@link TokenStream}s generated with <code>analyzer</code>
     * provide with character terms.
     * @see #isCharacterTokenStream(TokenStream)
     */
    public static boolean generatesCharacterTokenStream(Analyzer analyzer, String fieldName) throws IOException {
        return isCharacterTokenStream(analyzer.tokenStream(fieldName, ""));
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4133.java