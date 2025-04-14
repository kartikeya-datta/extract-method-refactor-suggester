error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7441.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7441.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7441.java
text:
```scala
L@@ist<Field> versionFields = new ArrayList<>();

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

import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ngram.*;
import org.apache.lucene.analysis.reverse.ReverseStringFilter;
import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.Version;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.test.ElasticsearchTokenStreamTestCase;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.carrotsearch.randomizedtesting.RandomizedTest.scaledRandomIntBetween;
import static org.hamcrest.Matchers.instanceOf;

public class NGramTokenizerFactoryTests extends ElasticsearchTokenStreamTestCase {

    @Test
    public void testParseTokenChars() {
        final Index index = new Index("test");
        final String name = "ngr";
        final Settings indexSettings = ImmutableSettings.EMPTY;
        for (String tokenChars : Arrays.asList("letters", "number", "DIRECTIONALITY_UNDEFINED")) {
            final Settings settings = ImmutableSettings.builder().put("min_gram", 2).put("max_gram", 3).put("token_chars", tokenChars).build();
            try {
                new NGramTokenizerFactory(index, indexSettings, name, settings).create(new StringReader(""));
                fail();
            } catch (ElasticsearchIllegalArgumentException expected) {
                // OK
            }
        }
        for (String tokenChars : Arrays.asList("letter", " digit ", "punctuation", "DIGIT", "CoNtRoL", "dash_punctuation")) {
            final Settings settings = ImmutableSettings.builder().put("min_gram", 2).put("max_gram", 3).put("token_chars", tokenChars).build();
            new NGramTokenizerFactory(index, indexSettings, name, settings).create(new StringReader(""));
            // no exception
        }
    }

    @Test
    public void testNoTokenChars() throws IOException {
        final Index index = new Index("test");
        final String name = "ngr";
        final Settings indexSettings = ImmutableSettings.EMPTY;
        final Settings settings = ImmutableSettings.builder().put("min_gram", 2).put("max_gram", 4).putArray("token_chars", new String[0]).build();
        Tokenizer tokenizer = new NGramTokenizerFactory(index, indexSettings, name, settings).create(new StringReader("1.34"));
        assertTokenStreamContents(tokenizer, new String[] {"1.", "1.3", "1.34", ".3", ".34", "34"});
    }

    @Test
    public void testPreTokenization() throws IOException {
        // Make sure that pretokenization works well and that it can be used even with token chars which are supplementary characters
        final Index index = new Index("test");
        final String name = "ngr";
        final Settings indexSettings = ImmutableSettings.EMPTY;
        Settings settings = ImmutableSettings.builder().put("min_gram", 2).put("max_gram", 3).put("token_chars", "letter,digit").build();
        assertTokenStreamContents(new NGramTokenizerFactory(index, indexSettings, name, settings).create(new StringReader("Åbc déf g\uD801\uDC00f ")),
                new String[] {"Åb", "Åbc", "bc", "dé", "déf", "éf", "g\uD801\uDC00", "g\uD801\uDC00f", "\uD801\uDC00f"});
        settings = ImmutableSettings.builder().put("min_gram", 2).put("max_gram", 3).put("token_chars", "letter,digit,punctuation,whitespace,symbol").build();
        assertTokenStreamContents(new NGramTokenizerFactory(index, indexSettings, name, settings).create(new StringReader(" a!$ 9")),
            new String[] {" a", " a!", "a!", "a!$", "!$", "!$ ", "$ ", "$ 9", " 9"});
    }

    @Test
    public void testPreTokenizationEdge() throws IOException {
        // Make sure that pretokenization works well and that it can be used even with token chars which are supplementary characters
        final Index index = new Index("test");
        final String name = "ngr";
        final Settings indexSettings = ImmutableSettings.EMPTY;
        Settings settings = ImmutableSettings.builder().put("min_gram", 2).put("max_gram", 3).put("token_chars", "letter,digit").build();
        assertTokenStreamContents(new EdgeNGramTokenizerFactory(index, indexSettings, name, settings).create(new StringReader("Åbc déf g\uD801\uDC00f ")),
                new String[] {"Åb", "Åbc", "dé", "déf", "g\uD801\uDC00", "g\uD801\uDC00f"});
        settings = ImmutableSettings.builder().put("min_gram", 2).put("max_gram", 3).put("token_chars", "letter,digit,punctuation,whitespace,symbol").build();
        assertTokenStreamContents(new EdgeNGramTokenizerFactory(index, indexSettings, name, settings).create(new StringReader(" a!$ 9")),
                new String[] {" a", " a!"});
    }
    
    @Test
    public void testBackwardsCompatibilityEdgeNgramTokenizer() throws IllegalArgumentException, IllegalAccessException {
        int iters = scaledRandomIntBetween(20, 100);
        final Index index = new Index("test");
        final String name = "ngr";
        for (int i = 0; i < iters; i++) {
            Version v = randomVersion(random());
            if (v.onOrAfter(Version.V_0_90_2)) {
                Builder builder = ImmutableSettings.builder().put("min_gram", 2).put("max_gram", 3).put("token_chars", "letter,digit");
                boolean compatVersion = false;
                if ((compatVersion = random().nextBoolean())) {
                    builder.put("version", "4." + random().nextInt(3));
                    builder.put("side", "back");
                }
                Settings settings = builder.build();
                Settings indexSettings = ImmutableSettings.builder().put(IndexMetaData.SETTING_VERSION_CREATED, v.id).build();
                Tokenizer edgeNGramTokenizer = new EdgeNGramTokenizerFactory(index, indexSettings, name, settings).create(new StringReader(
                        "foo bar"));
                if (compatVersion) {
                    assertThat(edgeNGramTokenizer, instanceOf(Lucene43EdgeNGramTokenizer.class));
                } else {
                    assertThat(edgeNGramTokenizer, instanceOf(EdgeNGramTokenizer.class));
                }

            } else {
                Settings settings = ImmutableSettings.builder().put("min_gram", 2).put("max_gram", 3).put("side", "back").build();
                Settings indexSettings = ImmutableSettings.builder().put(IndexMetaData.SETTING_VERSION_CREATED, v.id).build();
                Tokenizer edgeNGramTokenizer = new EdgeNGramTokenizerFactory(index, indexSettings, name, settings).create(new StringReader(
                        "foo bar"));
                assertThat(edgeNGramTokenizer, instanceOf(Lucene43EdgeNGramTokenizer.class));
            }
        }
        Settings settings = ImmutableSettings.builder().put("min_gram", 2).put("max_gram", 3).put("side", "back").build();
        Settings indexSettings = ImmutableSettings.builder().put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT).build();
        try {
            new EdgeNGramTokenizerFactory(index, indexSettings, name, settings).create(new StringReader("foo bar"));
            fail("should fail side:back is not supported anymore");
        } catch (ElasticsearchIllegalArgumentException ex) {
        }
        
    }
    
    @Test
    public void testBackwardsCompatibilityNgramTokenizer() throws IllegalArgumentException, IllegalAccessException {
        int iters = scaledRandomIntBetween(20, 100);
        for (int i = 0; i < iters; i++) {
            final Index index = new Index("test");
            final String name = "ngr";
            Version v = randomVersion(random());
            if (v.onOrAfter(Version.V_0_90_2)) {
                Builder builder = ImmutableSettings.builder().put("min_gram", 2).put("max_gram", 3).put("token_chars", "letter,digit");
                boolean compatVersion = false;
                if ((compatVersion = random().nextBoolean())) {
                    builder.put("version", "4." + random().nextInt(3));
                }
                Settings settings = builder.build();
                Settings indexSettings = ImmutableSettings.builder().put(IndexMetaData.SETTING_VERSION_CREATED, v.id).build();
                Tokenizer nGramTokenizer = new NGramTokenizerFactory(index, indexSettings, name, settings).create(new StringReader(
                        "foo bar"));
                if (compatVersion) { 
                    assertThat(nGramTokenizer, instanceOf(Lucene43NGramTokenizer.class));
                } else {
                    assertThat(nGramTokenizer, instanceOf(NGramTokenizer.class));
                }

            } else {
                Settings settings = ImmutableSettings.builder().put("min_gram", 2).put("max_gram", 3).build();
                Settings indexSettings = ImmutableSettings.builder().put(IndexMetaData.SETTING_VERSION_CREATED, v.id).build();
                Tokenizer nGramTokenizer = new NGramTokenizerFactory(index, indexSettings, name, settings).create(new StringReader(
                        "foo bar"));
                assertThat(nGramTokenizer, instanceOf(Lucene43NGramTokenizer.class));
            }
        }
    }
    
    @Test
    public void testBackwardsCompatibilityEdgeNgramTokenFilter() throws IllegalArgumentException, IllegalAccessException {
        int iters = scaledRandomIntBetween(20, 100);
        for (int i = 0; i < iters; i++) {
            final Index index = new Index("test");
            final String name = "ngr";
            Version v = randomVersion(random());
            if (v.onOrAfter(Version.V_0_90_2)) {
                Builder builder = ImmutableSettings.builder().put("min_gram", 2).put("max_gram", 3);
                boolean compatVersion = false;
                if ((compatVersion = random().nextBoolean())) {
                    builder.put("version", "4." + random().nextInt(3));
                }
                boolean reverse = random().nextBoolean();
                if (reverse) {
                    builder.put("side", "back");
                }
                Settings settings = builder.build();
                Settings indexSettings = ImmutableSettings.builder().put(IndexMetaData.SETTING_VERSION_CREATED, v.id).build();
                TokenStream edgeNGramTokenFilter = new EdgeNGramTokenFilterFactory(index, indexSettings, name, settings).create(new MockTokenizer(new StringReader(
                        "foo bar")));
                if (compatVersion) { 
                    assertThat(edgeNGramTokenFilter, instanceOf(EdgeNGramTokenFilter.class));
                } else if (reverse && !compatVersion){
                    assertThat(edgeNGramTokenFilter, instanceOf(ReverseStringFilter.class));
                } else {
                    assertThat(edgeNGramTokenFilter, instanceOf(EdgeNGramTokenFilter.class));
                }

            } else {
                Builder builder = ImmutableSettings.builder().put("min_gram", 2).put("max_gram", 3);
                boolean reverse = random().nextBoolean();
                if (reverse) {
                    builder.put("side", "back");
                }
                Settings settings = builder.build();
                Settings indexSettings = ImmutableSettings.builder().put(IndexMetaData.SETTING_VERSION_CREATED, v.id).build();
                TokenStream edgeNGramTokenFilter = new EdgeNGramTokenFilterFactory(index, indexSettings, name, settings).create(new MockTokenizer(new StringReader(
                        "foo bar")));
                assertThat(edgeNGramTokenFilter, instanceOf(EdgeNGramTokenFilter.class));
            }
        }
    }

    
    private Version randomVersion(Random random) throws IllegalArgumentException, IllegalAccessException {
        Field[] declaredFields = Version.class.getDeclaredFields();
        List<Field> versionFields = new ArrayList<Field>();
        for (Field field : declaredFields) {
            if ((field.getModifiers() & Modifier.STATIC) != 0 && field.getName().startsWith("V_") && field.getType() == Version.class) {
                versionFields.add(field);
            }
        }
        return (Version) versionFields.get(random.nextInt(versionFields.size())).get(Version.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7441.java