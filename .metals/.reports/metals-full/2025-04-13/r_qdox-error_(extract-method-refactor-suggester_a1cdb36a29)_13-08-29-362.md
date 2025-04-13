error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/399.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/399.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/399.java
text:
```scala
private L@@uceneStemmerAdapter() {

package org.apache.solr.handler.clustering.carrot2;

/*
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

import java.nio.CharBuffer;
import java.util.HashMap;

import org.apache.lucene.analysis.ar.ArabicNormalizer;
import org.apache.lucene.analysis.ar.ArabicStemmer;
import org.carrot2.core.LanguageCode;
import org.carrot2.text.linguistic.IStemmer;
import org.carrot2.text.linguistic.IStemmerFactory;
import org.carrot2.util.ReflectionUtils;
import org.slf4j.Logger;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.DanishStemmer;
import org.tartarus.snowball.ext.DutchStemmer;
import org.tartarus.snowball.ext.EnglishStemmer;
import org.tartarus.snowball.ext.FinnishStemmer;
import org.tartarus.snowball.ext.FrenchStemmer;
import org.tartarus.snowball.ext.GermanStemmer;
import org.tartarus.snowball.ext.HungarianStemmer;
import org.tartarus.snowball.ext.ItalianStemmer;
import org.tartarus.snowball.ext.NorwegianStemmer;
import org.tartarus.snowball.ext.PortugueseStemmer;
import org.tartarus.snowball.ext.RomanianStemmer;
import org.tartarus.snowball.ext.RussianStemmer;
import org.tartarus.snowball.ext.SpanishStemmer;
import org.tartarus.snowball.ext.SwedishStemmer;
import org.tartarus.snowball.ext.TurkishStemmer;

/**
 * An implementation of Carrot2's {@link IStemmerFactory} based on Lucene's
 * APIs. Should the relevant Lucene APIs need to change, the changes can be made
 * in this class.
 */
public class LuceneCarrot2StemmerFactory implements IStemmerFactory {
  final static Logger logger = org.slf4j.LoggerFactory
      .getLogger(LuceneCarrot2StemmerFactory.class);

  @Override
  public IStemmer getStemmer(LanguageCode language) {
    switch (language) {
    case ARABIC:
      return ArabicStemmerFactory.createStemmer();

    case CHINESE_SIMPLIFIED:
      return IdentityStemmer.INSTANCE;

    default:
      /*
       * For other languages, try to use snowball's stemming.
       */
      return SnowballStemmerFactory.createStemmer(language);
    }
  }

  /**
   * Factory of {@link IStemmer} implementations from the <code>snowball</code>
   * project.
   */
  private final static class SnowballStemmerFactory {
    /**
     * Static hard mapping from language codes to stemmer classes in Snowball.
     * This mapping is not dynamic because we want to keep the possibility to
     * obfuscate these classes.
     */
    private static HashMap<LanguageCode, Class<? extends SnowballProgram>> snowballStemmerClasses;
    static {
      snowballStemmerClasses = new HashMap<LanguageCode, Class<? extends SnowballProgram>>();
      snowballStemmerClasses.put(LanguageCode.DANISH, DanishStemmer.class);
      snowballStemmerClasses.put(LanguageCode.DUTCH, DutchStemmer.class);
      snowballStemmerClasses.put(LanguageCode.ENGLISH, EnglishStemmer.class);
      snowballStemmerClasses.put(LanguageCode.FINNISH, FinnishStemmer.class);
      snowballStemmerClasses.put(LanguageCode.FRENCH, FrenchStemmer.class);
      snowballStemmerClasses.put(LanguageCode.GERMAN, GermanStemmer.class);
      snowballStemmerClasses
          .put(LanguageCode.HUNGARIAN, HungarianStemmer.class);
      snowballStemmerClasses.put(LanguageCode.ITALIAN, ItalianStemmer.class);
      snowballStemmerClasses
          .put(LanguageCode.NORWEGIAN, NorwegianStemmer.class);
      snowballStemmerClasses.put(LanguageCode.PORTUGUESE,
          PortugueseStemmer.class);
      snowballStemmerClasses.put(LanguageCode.ROMANIAN, RomanianStemmer.class);
      snowballStemmerClasses.put(LanguageCode.RUSSIAN, RussianStemmer.class);
      snowballStemmerClasses.put(LanguageCode.SPANISH, SpanishStemmer.class);
      snowballStemmerClasses.put(LanguageCode.SWEDISH, SwedishStemmer.class);
      snowballStemmerClasses.put(LanguageCode.TURKISH, TurkishStemmer.class);
    }

    /**
     * An adapter converting Snowball programs into {@link IStemmer} interface.
     */
    private static class SnowballStemmerAdapter implements IStemmer {
      private final SnowballProgram snowballStemmer;

      public SnowballStemmerAdapter(SnowballProgram snowballStemmer) {
        this.snowballStemmer = snowballStemmer;
      }

      public CharSequence stem(CharSequence word) {
        snowballStemmer.setCurrent(word.toString());
        if (snowballStemmer.stem()) {
          return snowballStemmer.getCurrent();
        } else {
          return null;
        }
      }
    }

    /**
     * Create and return an {@link IStemmer} adapter for a
     * {@link SnowballProgram} for a given language code. An identity stemmer is
     * returned for unknown languages.
     */
    public static IStemmer createStemmer(LanguageCode language) {
      final Class<? extends SnowballProgram> stemmerClazz = snowballStemmerClasses
          .get(language);

      if (stemmerClazz == null) {
        logger.warn("No Snowball stemmer class for: " + language.name()
            + ". Quality of clustering may be degraded.");
        return IdentityStemmer.INSTANCE;
      }

      try {
        return new SnowballStemmerAdapter(stemmerClazz.newInstance());
      } catch (Exception e) {
        logger.warn("Could not instantiate snowball stemmer"
            + " for language: " + language.name()
            + ". Quality of clustering may be degraded.", e);

        return IdentityStemmer.INSTANCE;
      }
    }
  }

  /**
   * Factory of {@link IStemmer} implementations for the
   * {@link LanguageCode#ARABIC} language. Requires <code>lucene-contrib</code>
   * to be present in classpath, otherwise an empty (identity) stemmer is
   * returned.
   */
  private static class ArabicStemmerFactory {
    static {
      try {
        ReflectionUtils.classForName(ArabicStemmer.class.getName(), false);
        ReflectionUtils.classForName(ArabicNormalizer.class.getName(), false);
      } catch (ClassNotFoundException e) {
        logger
            .warn(
                "Could not instantiate Lucene stemmer for Arabic, clustering quality "
                    + "of Arabic content may be degraded. For best quality clusters, "
                    + "make sure Lucene's Arabic analyzer JAR is in the classpath",
                e);
      }
    }

    /**
     * Adapter to lucene-contrib Arabic analyzers.
     */
    private static class LuceneStemmerAdapter implements IStemmer {
      private final org.apache.lucene.analysis.ar.ArabicStemmer delegate;
      private final org.apache.lucene.analysis.ar.ArabicNormalizer normalizer;

      private char[] buffer = new char[0];

      private LuceneStemmerAdapter() throws Exception {
        delegate = new org.apache.lucene.analysis.ar.ArabicStemmer();
        normalizer = new org.apache.lucene.analysis.ar.ArabicNormalizer();
      }

      public CharSequence stem(CharSequence word) {
        if (word.length() > buffer.length) {
          buffer = new char[word.length()];
        }

        for (int i = 0; i < word.length(); i++) {
          buffer[i] = word.charAt(i);
        }

        int newLen = normalizer.normalize(buffer, word.length());
        newLen = delegate.stem(buffer, newLen);

        if (newLen != word.length() || !equals(buffer, newLen, word)) {
          return CharBuffer.wrap(buffer, 0, newLen);
        }

        // Same-same.
        return null;
      }

      private boolean equals(char[] buffer, int len, CharSequence word) {
        assert len == word.length();

        for (int i = 0; i < len; i++) {
          if (buffer[i] != word.charAt(i))
            return false;
        }

        return true;
      }
    }

    public static IStemmer createStemmer() {
      try {
        return new LuceneStemmerAdapter();
      } catch (Throwable e) {
        return IdentityStemmer.INSTANCE;
      }
    }
  }

  /**
   * An implementation of {@link IStemmer} that always returns <code>null</code>
   * which means no stemming.
   */
  private static class IdentityStemmer implements IStemmer {
    private final static IdentityStemmer INSTANCE = new IdentityStemmer();

    @Override
    public CharSequence stem(CharSequence word) {
      return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/399.java