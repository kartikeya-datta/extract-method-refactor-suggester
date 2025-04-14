error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3384.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3384.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3384.java
text:
```scala
r@@eturn (Integer) item < 0;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;

/**
 * Tests Rule.
 * 
 * @since 1.6
 */
public class RuleTest {
    private static class NegativeIntegerBaseMatcher extends BaseMatcher<Integer> {
        public void describeTo(Description description) {
            description.appendText("value should be negative");
        }

        public boolean matches(Object item) {
            return ((Integer) item) < 0;
        }
    }

    private Rule.Phoneme[][] makePhonemes() {
        String[][] words = {
                { "rinD", "rinDlt", "rina", "rinalt", "rino", "rinolt", "rinu", "rinult" },
                { "dortlaj", "dortlej", "ortlaj", "ortlej", "ortlej-dortlaj" } };
        Rule.Phoneme[][] phonemes = new Rule.Phoneme[words.length][];

        for (int i = 0; i < words.length; i++) {
            String[] words_i = words[i];
            Rule.Phoneme[] phonemes_i = phonemes[i] = new Rule.Phoneme[words_i.length];
            for (int j = 0; j < words_i.length; j++) {
                phonemes_i[j] = new Rule.Phoneme(words_i[j], Languages.NO_LANGUAGES);
            }
        }

        return phonemes;
    }

    @Test
    public void testPhonemeComparedToLaterIsNegative() {
        for (Rule.Phoneme[] phs : makePhonemes()) {
            for (int i = 0; i < phs.length; i++) {
                for (int j = i + 1; j < phs.length; j++) {
                    int c = Rule.Phoneme.COMPARATOR.compare(phs[i], phs[j]);

                    assertThat("Comparing " + phs[i].getPhonemeText() + " to " + phs[j].getPhonemeText() + " should be negative", c,
                            new NegativeIntegerBaseMatcher());
                }
            }
        }
    }

    @Test
    public void testPhonemeComparedToSelfIsZero() {
        for (Rule.Phoneme[] phs : makePhonemes()) {
            for (Rule.Phoneme ph : phs) {
                assertEquals("Phoneme compared to itself should be zero: " + ph.getPhonemeText(), 0,
                        Rule.Phoneme.COMPARATOR.compare(ph, ph));
            }
        }
    }

    @Test
    public void testSubSequenceWorks() {
        // AppendableCharSequence is private to Rule. We can only make it through a Phoneme.

        Rule.Phoneme a = new Rule.Phoneme("a", null);
        Rule.Phoneme b = new Rule.Phoneme("b", null);
        Rule.Phoneme cd = new Rule.Phoneme("cd", null);
        Rule.Phoneme ef = new Rule.Phoneme("ef", null);
        Rule.Phoneme ghi = new Rule.Phoneme("ghi", null);
        Rule.Phoneme jkl = new Rule.Phoneme("jkl", null);

        assertEquals('a', a.getPhonemeText().charAt(0));
        assertEquals('b', b.getPhonemeText().charAt(0));
        assertEquals('c', cd.getPhonemeText().charAt(0));
        assertEquals('d', cd.getPhonemeText().charAt(1));
        assertEquals('e', ef.getPhonemeText().charAt(0));
        assertEquals('f', ef.getPhonemeText().charAt(1));
        assertEquals('g', ghi.getPhonemeText().charAt(0));
        assertEquals('h', ghi.getPhonemeText().charAt(1));
        assertEquals('i', ghi.getPhonemeText().charAt(2));
        assertEquals('j', jkl.getPhonemeText().charAt(0));
        assertEquals('k', jkl.getPhonemeText().charAt(1));
        assertEquals('l', jkl.getPhonemeText().charAt(2));

        Rule.Phoneme a_b = a.append(b.getPhonemeText());
        assertEquals('a', a_b.getPhonemeText().charAt(0));
        assertEquals('b', a_b.getPhonemeText().charAt(1));
        assertEquals("ab", a_b.getPhonemeText().subSequence(0, 2).toString());
        assertEquals("a", a_b.getPhonemeText().subSequence(0, 1).toString());
        assertEquals("b", a_b.getPhonemeText().subSequence(1, 2).toString());

        Rule.Phoneme cd_ef = cd.append(ef.getPhonemeText());
        assertEquals('c', cd_ef.getPhonemeText().charAt(0));
        assertEquals('d', cd_ef.getPhonemeText().charAt(1));
        assertEquals('e', cd_ef.getPhonemeText().charAt(2));
        assertEquals('f', cd_ef.getPhonemeText().charAt(3));
        assertEquals("c", cd_ef.getPhonemeText().subSequence(0, 1).toString());
        assertEquals("d", cd_ef.getPhonemeText().subSequence(1, 2).toString());
        assertEquals("e", cd_ef.getPhonemeText().subSequence(2, 3).toString());
        assertEquals("f", cd_ef.getPhonemeText().subSequence(3, 4).toString());
        assertEquals("cd", cd_ef.getPhonemeText().subSequence(0, 2).toString());
        assertEquals("de", cd_ef.getPhonemeText().subSequence(1, 3).toString());
        assertEquals("ef", cd_ef.getPhonemeText().subSequence(2, 4).toString());
        assertEquals("cde", cd_ef.getPhonemeText().subSequence(0, 3).toString());
        assertEquals("def", cd_ef.getPhonemeText().subSequence(1, 4).toString());
        assertEquals("cdef", cd_ef.getPhonemeText().subSequence(0, 4).toString());

        Rule.Phoneme a_b_cd = a.append(b.getPhonemeText()).append(cd.getPhonemeText());
        assertEquals('a', a_b_cd.getPhonemeText().charAt(0));
        assertEquals('b', a_b_cd.getPhonemeText().charAt(1));
        assertEquals('c', a_b_cd.getPhonemeText().charAt(2));
        assertEquals('d', a_b_cd.getPhonemeText().charAt(3));
        assertEquals("a", a_b_cd.getPhonemeText().subSequence(0, 1).toString());
        assertEquals("b", a_b_cd.getPhonemeText().subSequence(1, 2).toString());
        assertEquals("c", a_b_cd.getPhonemeText().subSequence(2, 3).toString());
        assertEquals("d", a_b_cd.getPhonemeText().subSequence(3, 4).toString());
        assertEquals("ab", a_b_cd.getPhonemeText().subSequence(0, 2).toString());
        assertEquals("bc", a_b_cd.getPhonemeText().subSequence(1, 3).toString());
        assertEquals("cd", a_b_cd.getPhonemeText().subSequence(2, 4).toString());
        assertEquals("abc", a_b_cd.getPhonemeText().subSequence(0, 3).toString());
        assertEquals("bcd", a_b_cd.getPhonemeText().subSequence(1, 4).toString());
        assertEquals("abcd", a_b_cd.getPhonemeText().subSequence(0, 4).toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3384.java