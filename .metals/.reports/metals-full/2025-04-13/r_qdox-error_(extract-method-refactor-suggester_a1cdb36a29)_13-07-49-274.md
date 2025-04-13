error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9059.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9059.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9059.java
text:
```scala
t@@his.getStringEncoder().isEncodeEqual(element[1], element[0]);

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

package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoderAbstractTest;
import org.junit.Test;

/**
 * Tests the {@code ColognePhonetic} class.
 *
 * <p>Keep this file in UTF-8 encoding for proper Javadoc processing.</p>
 *
 */
public class ColognePhoneticTest extends StringEncoderAbstractTest<ColognePhonetic> {

    @Override
    protected ColognePhonetic createStringEncoder() {
        return new ColognePhonetic();
    }

    @Test
    public void testAabjoe() throws EncoderException {
        this.checkEncoding("01", "Aabjoe");
    }

    @Test
    public void testAaclan() throws EncoderException {
        this.checkEncoding("0856", "Aaclan");
    }

    /**
     * Tests [CODEC-122]
     *
     * @throws EncoderException
     */
    @Test
    public void testAychlmajrForCodec122() throws EncoderException {
        this.checkEncoding("04567", "Aychlmajr");
    }

    @Test
    public void testEdgeCases() throws EncoderException {
        String[][] data = {
            {"a", "0"},
            {"e", "0"},
            {"i", "0"},
            {"o", "0"},
            {"u", "0"},
            {"\u00E4", "0"}, // a-umlaut
            {"\u00F6", "0"}, // o-umlaut
            {"\u00FC", "0"}, // u-umlaut
            {"aa", "0"},
            {"ha", "0"},
            {"h", ""},
            {"aha", "0"},
            {"b", "1"},
            {"p", "1"},
            {"ph", "3"},
            {"f", "3"},
            {"v", "3"},
            {"w", "3"},
            {"g", "4"},
            {"k", "4"},
            {"q", "4"},
            {"x", "48"},
            {"ax", "048"},
            {"cx", "48"},
            {"l", "5"},
            {"cl", "45"},
            {"acl", "085"},
            {"mn", "6"},
            {"r", "7"}};
        this.checkEncodings(data);
    }

    @Test
    public void testExamples() throws EncoderException {
        String[][] data = {
            {"m\u00DCller", "657"}, // mÜller - why upper case U-umlaut?
            {"schmidt", "862"},
            {"schneider", "8627"},
            {"fischer", "387"},
            {"weber", "317"},
            {"wagner", "3467"},
            {"becker", "147"},
            {"hoffmann", "0366"},
            {"sch\u00C4fer", "837"}, // schÄfer - why upper case A-umlaut ?
            {"Breschnew", "17863"},
            {"Wikipedia", "3412"},
            {"peter", "127"},
            {"pharma", "376"},
            {"m\u00f6nchengladbach", "664645214"}, // mönchengladbach
            {"deutsch", "28"},
            {"deutz", "28"},
            {"hamburg", "06174"},
            {"hannover", "0637"},
            {"christstollen", "478256"},
            {"Xanthippe", "48621"},
            {"Zacharias", "8478"},
            {"Holzbau", "0581"},
            {"matsch", "68"},
            {"matz", "68"},
            {"Arbeitsamt", "071862"},
            {"Eberhard", "01772"},
            {"Eberhardt", "01772"},
            {"heithabu", "021"}};
        this.checkEncodings(data);
    }

    @Test
    public void testHyphen() throws EncoderException {
        String[][] data = {{"bergisch-gladbach", "174845214"},
                {"M\u00fcller-L\u00fcdenscheidt", "65752682"}}; // Müller-Lüdenscheidt
        this.checkEncodings(data);
    }

    @Test
    public void testIsEncodeEquals() {
        String[][] data = {
            {"Meyer", "M\u00fcller"}, // Müller
            {"Meyer", "Mayr"},
            {"house", "house"},
            {"House", "house"},
            {"Haus", "house"},
            {"ganz", "Gans"},
            {"ganz", "G\u00e4nse"}, // Gänse
            {"Miyagi", "Miyako"}};
        for (String[] element : data) {
            ((ColognePhonetic) this.getStringEncoder()).isEncodeEqual(element[1], element[0]);
        }
    }

    @Test
    public void testVariationsMella() throws EncoderException {
        String data[] = {"mella", "milah", "moulla", "mellah", "muehle", "mule"};
        this.checkEncodingVariations("65", data);
    }

    @Test
    public void testVariationsMeyer() throws EncoderException {
        String data[] = {"Meier", "Maier", "Mair", "Meyer", "Meyr", "Mejer", "Major"};
        this.checkEncodingVariations("67", data);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9059.java