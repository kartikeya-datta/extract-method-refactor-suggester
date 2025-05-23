error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4144.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4144.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4144.java
text:
```scala
final V@@ersion indexVersion = Version.indexCreated(indexSettings);

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

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ar.ArabicStemFilter;
import org.apache.lucene.analysis.bg.BulgarianStemFilter;
import org.apache.lucene.analysis.br.BrazilianStemFilter;
import org.apache.lucene.analysis.ckb.SoraniStemFilter;
import org.apache.lucene.analysis.cz.CzechStemFilter;
import org.apache.lucene.analysis.de.GermanLightStemFilter;
import org.apache.lucene.analysis.de.GermanMinimalStemFilter;
import org.apache.lucene.analysis.el.GreekStemFilter;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.es.SpanishLightStemFilter;
import org.apache.lucene.analysis.fi.FinnishLightStemFilter;
import org.apache.lucene.analysis.fr.FrenchLightStemFilter;
import org.apache.lucene.analysis.fr.FrenchMinimalStemFilter;
import org.apache.lucene.analysis.gl.GalicianMinimalStemFilter;
import org.apache.lucene.analysis.gl.GalicianStemFilter;
import org.apache.lucene.analysis.hi.HindiStemFilter;
import org.apache.lucene.analysis.hu.HungarianLightStemFilter;
import org.apache.lucene.analysis.id.IndonesianStemFilter;
import org.apache.lucene.analysis.it.ItalianLightStemFilter;
import org.apache.lucene.analysis.lv.LatvianStemFilter;
import org.apache.lucene.analysis.no.NorwegianLightStemFilter;
import org.apache.lucene.analysis.no.NorwegianLightStemmer;
import org.apache.lucene.analysis.no.NorwegianMinimalStemFilter;
import org.apache.lucene.analysis.pt.PortugueseLightStemFilter;
import org.apache.lucene.analysis.pt.PortugueseMinimalStemFilter;
import org.apache.lucene.analysis.pt.PortugueseStemFilter;
import org.apache.lucene.analysis.ru.RussianLightStemFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.sv.SwedishLightStemFilter;
import org.elasticsearch.Version;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;
import org.tartarus.snowball.ext.*;

/**
 */
public class StemmerTokenFilterFactory extends AbstractTokenFilterFactory {

    private String language;

    @Inject
    public StemmerTokenFilterFactory(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
        this.language = Strings.capitalize(settings.get("language", settings.get("name", "porter")));
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        final Version indexVersion = indexSettings.getAsVersion(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT);

        if ("arabic".equalsIgnoreCase(language)) {
            return new ArabicStemFilter(tokenStream);
        } else if ("armenian".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new ArmenianStemmer());
        } else if ("basque".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new BasqueStemmer());
        } else if ("brazilian".equalsIgnoreCase(language)) {
            return new BrazilianStemFilter(tokenStream);
        } else if ("bulgarian".equalsIgnoreCase(language)) {
            return new BulgarianStemFilter(tokenStream);
        } else if ("catalan".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new CatalanStemmer());
        } else if ("czech".equalsIgnoreCase(language)) {
            return new CzechStemFilter(tokenStream);
        } else if ("danish".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new DanishStemmer());

            // Dutch stemmers
        } else if ("dutch".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new DutchStemmer());
        } else if ("dutch_kp".equalsIgnoreCase(language) || "dutchKp".equalsIgnoreCase(language) || "kp".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new KpStemmer());

            // English stemmers
        } else if ("english".equalsIgnoreCase(language)) {
            if (indexVersion.onOrAfter(Version.V_1_3_0)) {
                return new PorterStemFilter(tokenStream);
            } else {
                return new SnowballFilter(tokenStream, new EnglishStemmer());
            }
        } else if ("light_english".equalsIgnoreCase(language) || "lightEnglish".equalsIgnoreCase(language)
 "kstem".equalsIgnoreCase(language)) {
            return new KStemFilter(tokenStream);
        } else if ("lovins".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new LovinsStemmer());
        } else if ("porter".equalsIgnoreCase(language)) {
            return new PorterStemFilter(tokenStream);
        } else if ("porter2".equalsIgnoreCase(language)) {
            if (indexVersion.onOrAfter(Version.V_1_3_0)) {
                return new SnowballFilter(tokenStream, new EnglishStemmer());
            } else {
                return new SnowballFilter(tokenStream, new PorterStemmer());
            }
        } else if ("minimal_english".equalsIgnoreCase(language) || "minimalEnglish".equalsIgnoreCase(language)) {
            return new EnglishMinimalStemFilter(tokenStream);
        } else if ("possessive_english".equalsIgnoreCase(language) || "possessiveEnglish".equalsIgnoreCase(language)) {
            return new EnglishPossessiveFilter(version, tokenStream);

            // Finnish stemmers
        } else if ("finnish".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new FinnishStemmer());
        } else if ("light_finish".equalsIgnoreCase(language) || "lightFinish".equalsIgnoreCase(language)) {
            // leaving this for backward compatibility
            return new FinnishLightStemFilter(tokenStream);
        } else if ("light_finnish".equalsIgnoreCase(language) || "lightFinnish".equalsIgnoreCase(language)) {
            return new FinnishLightStemFilter(tokenStream);

            // French stemmers
        } else if ("french".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new FrenchStemmer());
        } else if ("light_french".equalsIgnoreCase(language) || "lightFrench".equalsIgnoreCase(language)) {
            return new FrenchLightStemFilter(tokenStream);
        } else if ("minimal_french".equalsIgnoreCase(language) || "minimalFrench".equalsIgnoreCase(language)) {
            return new FrenchMinimalStemFilter(tokenStream);
            
            // Galician stemmers
        } else if ("galician".equalsIgnoreCase(language)) {
            return new GalicianStemFilter(tokenStream);
        } else if ("minimal_galician".equalsIgnoreCase(language)) {
            return new GalicianMinimalStemFilter(tokenStream);

            // German stemmers
        } else if ("german".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new GermanStemmer());
        } else if ("german2".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new German2Stemmer());
        } else if ("light_german".equalsIgnoreCase(language) || "lightGerman".equalsIgnoreCase(language)) {
            return new GermanLightStemFilter(tokenStream);
        } else if ("minimal_german".equalsIgnoreCase(language) || "minimalGerman".equalsIgnoreCase(language)) {
            return new GermanMinimalStemFilter(tokenStream);

        } else if ("greek".equalsIgnoreCase(language)) {
            return new GreekStemFilter(tokenStream);
        } else if ("hindi".equalsIgnoreCase(language)) {
            return new HindiStemFilter(tokenStream);

            // Hungarian stemmers
        } else if ("hungarian".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new HungarianStemmer());
        } else if ("light_hungarian".equalsIgnoreCase(language) || "lightHungarian".equalsIgnoreCase(language)) {
            return new HungarianLightStemFilter(tokenStream);

        } else if ("indonesian".equalsIgnoreCase(language)) {
            return new IndonesianStemFilter(tokenStream);
            
            // Irish stemmer
        } else if ("irish".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new IrishStemmer());

            // Italian stemmers
        } else if ("italian".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new ItalianStemmer());
        } else if ("light_italian".equalsIgnoreCase(language) || "lightItalian".equalsIgnoreCase(language)) {
            return new ItalianLightStemFilter(tokenStream);

        } else if ("latvian".equalsIgnoreCase(language)) {
            return new LatvianStemFilter(tokenStream);

            // Norwegian (Bokmål) stemmers
        } else if ("norwegian".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new NorwegianStemmer());
        } else if ("light_norwegian".equalsIgnoreCase(language) || "lightNorwegian".equalsIgnoreCase(language)) {
            return new NorwegianLightStemFilter(tokenStream);
        } else if ("minimal_norwegian".equalsIgnoreCase(language) || "minimalNorwegian".equals(language)) {
            return new NorwegianMinimalStemFilter(tokenStream);
            
            // Norwegian (Nynorsk) stemmers 
        } else if ("light_nynorsk".equalsIgnoreCase(language) || "lightNynorsk".equalsIgnoreCase(language)) {
            return new NorwegianLightStemFilter(tokenStream, NorwegianLightStemmer.NYNORSK);
        } else if ("minimal_nynorsk".equalsIgnoreCase(language) || "minimalNynorsk".equalsIgnoreCase(language)) {
            return new NorwegianMinimalStemFilter(tokenStream, NorwegianLightStemmer.NYNORSK);

            // Portuguese stemmers
        } else if ("portuguese".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new PortugueseStemmer());
        } else if ("light_portuguese".equalsIgnoreCase(language) || "lightPortuguese".equalsIgnoreCase(language)) {
            return new PortugueseLightStemFilter(tokenStream);
        } else if ("minimal_portuguese".equalsIgnoreCase(language) || "minimalPortuguese".equalsIgnoreCase(language)) {
            return new PortugueseMinimalStemFilter(tokenStream);
        } else if ("portuguese_rslp".equalsIgnoreCase(language)) {
            return new PortugueseStemFilter(tokenStream);

        } else if ("romanian".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new RomanianStemmer());

            // Russian stemmers
        } else if ("russian".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new RussianStemmer());
        } else if ("light_russian".equalsIgnoreCase(language) || "lightRussian".equalsIgnoreCase(language)) {
            return new RussianLightStemFilter(tokenStream);

            // Spanish stemmers
        } else if ("spanish".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new SpanishStemmer());
        } else if ("light_spanish".equalsIgnoreCase(language) || "lightSpanish".equalsIgnoreCase(language)) {
            return new SpanishLightStemFilter(tokenStream);
            
            // Sorani Kurdish stemmer
        } else if ("sorani".equalsIgnoreCase(language)) {
            return new SoraniStemFilter(tokenStream);

            // Swedish stemmers
        } else if ("swedish".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new SwedishStemmer());
        } else if ("light_swedish".equalsIgnoreCase(language) || "lightSwedish".equalsIgnoreCase(language)) {
            return new SwedishLightStemFilter(tokenStream);

        } else if ("turkish".equalsIgnoreCase(language)) {
            return new SnowballFilter(tokenStream, new TurkishStemmer());
        }

        return new SnowballFilter(tokenStream, language);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4144.java