error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5537.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5537.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[20,1]

error in qdox parser
file content:
```java
offset: 893
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5537.java
text:
```scala
public class BeiderMorseEncoderTest extends StringEncoderAbstractTest<StringEncoder> {

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

p@@ackage org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringEncoderAbstractTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests BeiderMorseEncoder.
 *
 * @since 1.6
 */
public class BeiderMorseEncoderTest extends StringEncoderAbstractTest {
    private static final char[] TEST_CHARS = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'o', 'u' };

    private void assertNotEmpty(final BeiderMorseEncoder bmpm, final String value) throws EncoderException {
        Assert.assertFalse(value, bmpm.encode(value).equals(""));
    }

    private BeiderMorseEncoder createGenericApproxEncoder() {
        final BeiderMorseEncoder encoder = new BeiderMorseEncoder();
        encoder.setNameType(NameType.GENERIC);
        encoder.setRuleType(RuleType.APPROX);
        return encoder;
    }

    @Override
    protected StringEncoder createStringEncoder() {
        return new BeiderMorseEncoder();
    }

    /**
     * Tests we do not blow up.
     *
     * @throws EncoderException
     */
    @Test
    public void testAllChars() throws EncoderException {
        final BeiderMorseEncoder bmpm = createGenericApproxEncoder();
        for (char c = Character.MIN_VALUE; c < Character.MAX_VALUE; c++) {
            bmpm.encode(Character.toString(c));
        }
    }

    @Test
    public void testAsciiEncodeNotEmpty1Letter() throws EncoderException {
        final BeiderMorseEncoder bmpm = createGenericApproxEncoder();
        for (char c = 'a'; c <= 'z'; c++) {
            final String value = Character.toString(c);
            final String valueU = value.toUpperCase();
            assertNotEmpty(bmpm, value);
            assertNotEmpty(bmpm, valueU);
        }
    }

    @Test
    public void testAsciiEncodeNotEmpty2Letters() throws EncoderException {
        final BeiderMorseEncoder bmpm = createGenericApproxEncoder();
        for (char c1 = 'a'; c1 <= 'z'; c1++) {
            for (char c2 = 'a'; c2 <= 'z'; c2++) {
                final String value = new String(new char[] { c1, c2 });
                final String valueU = value.toUpperCase();
                assertNotEmpty(bmpm, value);
                assertNotEmpty(bmpm, valueU);
            }
        }
    }

    @Test
    public void testEncodeAtzNotEmpty() throws EncoderException {
        final BeiderMorseEncoder bmpm = createGenericApproxEncoder();
        //String[] names = { "ácz", "átz", "Ignácz", "Ignátz", "Ignác" };
        final String[] names = { "\u00e1cz", "\u00e1tz", "Ign\u00e1cz", "Ign\u00e1tz", "Ign\u00e1c" };
        for (final String name : names) {
            assertNotEmpty(bmpm, name);
        }
    }

    /**
     * Tests https://issues.apache.org/jira/browse/CODEC-125?focusedCommentId=13071566&page=com.atlassian.jira.plugin.system.issuetabpanels:
     * comment-tabpanel#comment-13071566
     *
     * @throws EncoderException
     */
    @Test
    public void testEncodeGna() throws EncoderException {
        final BeiderMorseEncoder bmpm = createGenericApproxEncoder();
        bmpm.encode("gna");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLangIllegalArgumentException() {
        Rule.getInstance(NameType.GENERIC, RuleType.APPROX, "noSuchLanguage");
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidLangIllegalStateException() {
        Lang.loadFromResource("thisIsAMadeUpResourceName", Languages.getInstance(NameType.GENERIC));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLanguageIllegalArgumentException() {
        Languages.getInstance("thereIsNoSuchLanguage");
    }

    @Test(timeout = 10000L)
    public void testLongestEnglishSurname() throws EncoderException {
        final BeiderMorseEncoder bmpm = createGenericApproxEncoder();
        bmpm.encode("MacGhilleseatheanaich");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeIndexForRuleMatchIndexOutOfBoundsException() {
        final Rule r = new Rule("a", "", "", new Rule.Phoneme("", Languages.ANY_LANGUAGE));
        r.patternAndContextMatches("bob", -1);
    }

    @Test
    public void testOOM() throws EncoderException {
        final String phrase = "200697900'-->&#1913348150;</  bceaeef >aadaabcf\"aedfbff<!--\'-->?>cae"
                + "cfaaa><?&#<!--</script>&lang&fc;aadeaf?>>&bdquo<    cc =\"abff\"    /></   afe  >"
                + "<script><!-- f(';<    cf aefbeef = \"bfabadcf\" ebbfeedd = fccabeb >";

        final BeiderMorseEncoder encoder = new BeiderMorseEncoder();
        encoder.setNameType(NameType.GENERIC);
        encoder.setRuleType(RuleType.EXACT);
        encoder.setMaxPhonemes(10);

        final String phonemes = encoder.encode(phrase);
        assertTrue(phonemes.length() > 0);

        final String[] phonemeArr = phonemes.split("\\|");
        assertTrue(phonemeArr.length <= 10);
    }

    @Test
    public void testSetConcat() {
        final BeiderMorseEncoder bmpm = new BeiderMorseEncoder();
        bmpm.setConcat(false);
        assertFalse("Should be able to set concat to false", bmpm.isConcat());
    }

    @Test
    public void testSetNameTypeAsh() {
        final BeiderMorseEncoder bmpm = new BeiderMorseEncoder();
        bmpm.setNameType(NameType.ASHKENAZI);
        assertEquals("Name type should have been set to ash", NameType.ASHKENAZI, bmpm.getNameType());
    }

    @Test
    public void testSetRuleTypeExact() {
        final BeiderMorseEncoder bmpm = new BeiderMorseEncoder();
        bmpm.setRuleType(RuleType.EXACT);
        assertEquals("Rule type should have been set to exact", RuleType.EXACT, bmpm.getRuleType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRuleTypeToRulesIllegalArgumentException() {
        final BeiderMorseEncoder bmpm = new BeiderMorseEncoder();
        bmpm.setRuleType(RuleType.RULES);
    }

    /**
     * (Un)luckily, the worse performing test because of the data in {@link #TEST_CHARS}
     *
     * @throws EncoderException
     */
    @Test(/* timeout = 20000L */)
    public void testSpeedCheck() throws EncoderException {
        final BeiderMorseEncoder bmpm = this.createGenericApproxEncoder();
        final StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(TEST_CHARS[0]);
        for (int i = 0, j = 1; i < 40; i++, j++) {
            if (j == TEST_CHARS.length) {
                j = 0;
            }
            bmpm.encode(stringBuffer.toString());
            stringBuffer.append(TEST_CHARS[j]);
        }
    }

    @Test
    public void testSpeedCheck2() throws EncoderException {
        final BeiderMorseEncoder bmpm = this.createGenericApproxEncoder();
        final String phrase = "ItstheendoftheworldasweknowitandIfeelfine";

        for (int i = 1; i <= phrase.length(); i++) {
            bmpm.encode(phrase.subSequence(0, i));
        }
    }

    @Test
    public void testSpeedCheck3() throws EncoderException {
        final BeiderMorseEncoder bmpm = this.createGenericApproxEncoder();
        final String phrase = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";

        for (int i = 1; i <= phrase.length(); i++) {
            bmpm.encode(phrase.subSequence(0, i));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5537.java