error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15211.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15211.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15211.java
text:
```scala
protected S@@tringEncoder createEncoder() {

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

import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringEncoderAbstractTest;

/**
 * @author Apache Software Foundation
 * @version $Id$
 */
public class MetaphoneTest extends StringEncoderAbstractTest {

    private Metaphone metaphone = null;

    public MetaphoneTest(String name) {
        super(name);
    }

    public void assertIsMetaphoneEqual(String source, String[] matches) {
        // match source to all matches
        for (int i = 0; i < matches.length; i++) {
            assertTrue("Source: " + source + ", should have same Metaphone as: " + matches[i],
                       this.getMetaphone().isMetaphoneEqual(source, matches[i]));
        }
        // match to each other
        for (int i = 0; i < matches.length; i++) {
            for (int j = 0; j < matches.length; j++) {
                assertTrue(this.getMetaphone().isMetaphoneEqual(matches[i], matches[j]));
            }
        }
    }

    public void assertMetaphoneEqual(String[][] pairs) {
        this.validateFixture(pairs);
        for (int i = 0; i < pairs.length; i++) {
            String name0 = pairs[i][0];
            String name1 = pairs[i][1];
            String failMsg = "Expected match between " + name0 + " and " + name1;
            assertTrue(failMsg, this.getMetaphone().isMetaphoneEqual(name0, name1));
            assertTrue(failMsg, this.getMetaphone().isMetaphoneEqual(name1, name0));
        }
    }
    /**
     * @return Returns the metaphone.
     */
    private Metaphone getMetaphone() {
        return this.metaphone;
    }

    protected StringEncoder makeEncoder() {
        return new Metaphone();
    }

    /**
     * @param metaphone
     *                  The metaphone to set.
     */
    private void setMetaphone(Metaphone metaphone) {
        this.metaphone = metaphone;
    }

    public void setUp() throws Exception {
        super.setUp();
        this.setMetaphone(new Metaphone());
    }

    public void tearDown() throws Exception {
        super.tearDown();
        this.setMetaphone(null);
    }

    public void testIsMetaphoneEqual1() {
        this.assertMetaphoneEqual(new String[][] { { "Case", "case" }, {
                "CASE", "Case" }, {
                "caSe", "cAsE" }, {
                "quick", "cookie" }
        });
    }

    /**
     * Matches computed from http://www.lanw.com/java/phonetic/default.htm
     */
    public void testIsMetaphoneEqual2() {
        this.assertMetaphoneEqual(new String[][] { { "Lawrence", "Lorenza" }, {
                "Gary", "Cahra" }, });
    }

    /**
     * Initial AE case.
     * 
     * Match data computed from http://www.lanw.com/java/phonetic/default.htm
     */
    public void testIsMetaphoneEqualAero() {
        this.assertIsMetaphoneEqual("Aero", new String[] { "Eure" });
    }

    /**
     * Initial WH case.
     * 
     * Match data computed from http://www.lanw.com/java/phonetic/default.htm
     */
    public void testIsMetaphoneEqualWhite() {
        this.assertIsMetaphoneEqual(
            "White",
            new String[] { "Wade", "Wait", "Waite", "Wat", "Whit", "Wiatt", "Wit", "Wittie", "Witty", "Wood", "Woodie", "Woody" });
    }

    /**
     * Initial A, not followed by an E case.
     * 
     * Match data computed from http://www.lanw.com/java/phonetic/default.htm
     */
    public void testIsMetaphoneEqualAlbert() {
        this.assertIsMetaphoneEqual("Albert", new String[] { "Ailbert", "Alberik", "Albert", "Alberto", "Albrecht" });
    }

    /**
     * Match data computed from http://www.lanw.com/java/phonetic/default.htm
     */
    public void testIsMetaphoneEqualGary() {
        this.assertIsMetaphoneEqual(
            "Gary",
            new String[] {
                "Cahra",
                "Cara",
                "Carey",
                "Cari",
                "Caria",
                "Carie",
                "Caro",
                "Carree",
                "Carri",
                "Carrie",
                "Carry",
                "Cary",
                "Cora",
                "Corey",
                "Cori",
                "Corie",
                "Correy",
                "Corri",
                "Corrie",
                "Corry",
                "Cory",
                "Gray",
                "Kara",
                "Kare",
                "Karee",
                "Kari",
                "Karia",
                "Karie",
                "Karrah",
                "Karrie",
                "Karry",
                "Kary",
                "Keri",
                "Kerri",
                "Kerrie",
                "Kerry",
                "Kira",
                "Kiri",
                "Kora",
                "Kore",
                "Kori",
                "Korie",
                "Korrie",
                "Korry" });
    }

    /**
     * Match data computed from http://www.lanw.com/java/phonetic/default.htm
     */
    public void testIsMetaphoneEqualJohn() {
        this.assertIsMetaphoneEqual(
            "John",
            new String[] {
                "Gena",
                "Gene",
                "Genia",
                "Genna",
                "Genni",
                "Gennie",
                "Genny",
                "Giana",
                "Gianna",
                "Gina",
                "Ginni",
                "Ginnie",
                "Ginny",
                "Jaine",
                "Jan",
                "Jana",
                "Jane",
                "Janey",
                "Jania",
                "Janie",
                "Janna",
                "Jany",
                "Jayne",
                "Jean",
                "Jeana",
                "Jeane",
                "Jeanie",
                "Jeanna",
                "Jeanne",
                "Jeannie",
                "Jen",
                "Jena",
                "Jeni",
                "Jenn",
                "Jenna",
                "Jennee",
                "Jenni",
                "Jennie",
                "Jenny",
                "Jinny",
                "Jo Ann",
                "Jo-Ann",
                "Jo-Anne",
                "Joan",
                "Joana",
                "Joane",
                "Joanie",
                "Joann",
                "Joanna",
                "Joanne",
                "Joeann",
                "Johna",
                "Johnna",
                "Joni",
                "Jonie",
                "Juana",
                "June",
                "Junia",
                "Junie" });
    }

    /**
     * Initial KN case.
     * 
     * Match data computed from http://www.lanw.com/java/phonetic/default.htm
     */
    public void testIsMetaphoneEqualKnight() {
        this.assertIsMetaphoneEqual(
            "Knight",
            new String[] {
                "Hynda",
                "Nada",
                "Nadia",
                "Nady",
                "Nat",
                "Nata",
                "Natty",
                "Neda",
                "Nedda",
                "Nedi",
                "Netta",
                "Netti",
                "Nettie",
                "Netty",
                "Nita",
                "Nydia" });
    }
    /**
     * Match data computed from http://www.lanw.com/java/phonetic/default.htm
     */
    public void testIsMetaphoneEqualMary() {
        this.assertIsMetaphoneEqual(
            "Mary",
            new String[] {
                "Mair",
                "Maire",
                "Mara",
                "Mareah",
                "Mari",
                "Maria",
                "Marie",
                "Mary",
                "Maura",
                "Maure",
                "Meara",
                "Merrie",
                "Merry",
                "Mira",
                "Moira",
                "Mora",
                "Moria",
                "Moyra",
                "Muire",
                "Myra",
                "Myrah" });
    }

    /**
     * Match data computed from http://www.lanw.com/java/phonetic/default.htm
     */
    public void testIsMetaphoneEqualParis() {
        this.assertIsMetaphoneEqual("Paris", new String[] { "Pearcy", "Perris", "Piercy", "Pierz", "Pryse" });
    }

    /**
     * Match data computed from http://www.lanw.com/java/phonetic/default.htm
     */
    public void testIsMetaphoneEqualPeter() {
        this.assertIsMetaphoneEqual(
            "Peter",
            new String[] { "Peadar", "Peder", "Pedro", "Peter", "Petr", "Peyter", "Pieter", "Pietro", "Piotr" });
    }

    /**
     * Match data computed from http://www.lanw.com/java/phonetic/default.htm
     */
    public void testIsMetaphoneEqualRay() {
        this.assertIsMetaphoneEqual("Ray", new String[] { "Ray", "Rey", "Roi", "Roy", "Ruy" });
    }

    /**
     * Match data computed from http://www.lanw.com/java/phonetic/default.htm
     */
    public void testIsMetaphoneEqualSusan() {
        this.assertIsMetaphoneEqual(
            "Susan",
            new String[] {
                "Siusan",
                "Sosanna",
                "Susan",
                "Susana",
                "Susann",
                "Susanna",
                "Susannah",
                "Susanne",
                "Suzann",
                "Suzanna",
                "Suzanne",
                "Zuzana" });
    }

    /**
     * Initial WR case.
     * 
     * Match data computed from http://www.lanw.com/java/phonetic/default.htm
     */
    public void testIsMetaphoneEqualWright() {
        this.assertIsMetaphoneEqual("Wright", new String[] { "Rota", "Rudd", "Ryde" });
    }

    /**
     * Match data computed from http://www.lanw.com/java/phonetic/default.htm
     */
    public void testIsMetaphoneEqualXalan() {
        this.assertIsMetaphoneEqual(
            "Xalan",
            new String[] { "Celene", "Celina", "Celine", "Selena", "Selene", "Selina", "Seline", "Suellen", "Xylina" });
    }

    public void testMetaphone() {
        assertEquals("HL", this.getMetaphone().metaphone("howl"));
        assertEquals("TSTN", this.getMetaphone().metaphone("testing"));
        assertEquals("0", this.getMetaphone().metaphone("The"));
        assertEquals("KK", this.getMetaphone().metaphone("quick"));
        assertEquals("BRN", this.getMetaphone().metaphone("brown"));
        assertEquals("FKS", this.getMetaphone().metaphone("fox"));
        assertEquals("JMPT", this.getMetaphone().metaphone("jumped"));
        assertEquals("OFR", this.getMetaphone().metaphone("over"));
        assertEquals("0", this.getMetaphone().metaphone("the"));
        assertEquals("LS", this.getMetaphone().metaphone("lazy"));
        assertEquals("TKS", this.getMetaphone().metaphone("dogs"));
    }
    
    public void testWordEndingInMB() {
        assertEquals( "KM", this.getMetaphone().metaphone("COMB") );
        assertEquals( "TM", this.getMetaphone().metaphone("TOMB") );
        assertEquals( "WM", this.getMetaphone().metaphone("WOMB") );
    }

    public void testDiscardOfSCEOrSCIOrSCY() {
        assertEquals( "SNS", this.getMetaphone().metaphone("SCIENCE") );
        assertEquals( "SN", this.getMetaphone().metaphone("SCENE") );
        assertEquals( "S", this.getMetaphone().metaphone("SCY") );
    }

    /**
     * Tests (CODEC-57) Metaphone.metaphone(String) returns an empty string when passed the word "why"
     */
    public void testWhy() {
        // PHP returns "H". The original metaphone returns an empty string. 
        assertEquals("", this.getMetaphone().metaphone("WHY"));
    }

    public void testWordsWithCIA() {
        assertEquals( "XP", this.getMetaphone().metaphone("CIAPO") );
    }

    public void testTranslateOfSCHAndCH() {
        assertEquals( "SKTL", this.getMetaphone().metaphone("SCHEDULE") );
        assertEquals( "SKMT", this.getMetaphone().metaphone("SCHEMATIC") );

        assertEquals( "KRKT", this.getMetaphone().metaphone("CHARACTER") );
        assertEquals( "TX", this.getMetaphone().metaphone("TEACH") );
    }

    public void testTranslateToJOfDGEOrDGIOrDGY() {
        assertEquals( "TJ", this.getMetaphone().metaphone("DODGY") );
        assertEquals( "TJ", this.getMetaphone().metaphone("DODGE") );
        assertEquals( "AJMT", this.getMetaphone().metaphone("ADGIEMTI") );
    }

    public void testDiscardOfSilentHAfterG() {
        assertEquals( "KNT", this.getMetaphone().metaphone("GHENT") );
        assertEquals( "B", this.getMetaphone().metaphone("BAUGH") );
    }

    public void testDiscardOfSilentGN() {
        // NOTE: This does not test for silent GN, but for starting with GN
        assertEquals( "N", this.getMetaphone().metaphone("GNU") );

        // NOTE: Trying to test for GNED, but expected code does not appear to execute
        assertEquals( "SNT", this.getMetaphone().metaphone("SIGNED") );
    }

    public void testPHTOF() {
        assertEquals( "FX", this.getMetaphone().metaphone("PHISH") );
    }

    public void testSHAndSIOAndSIAToX() {
        assertEquals( "XT", this.getMetaphone().metaphone("SHOT") );
        assertEquals( "OTXN", this.getMetaphone().metaphone("ODSIAN") );
        assertEquals( "PLXN", this.getMetaphone().metaphone("PULSION") );
    }

    public void testTIOAndTIAToX() {
        assertEquals( "OX", this.getMetaphone().metaphone("OTIA") );
        assertEquals( "PRXN", this.getMetaphone().metaphone("PORTION") );
    }

    public void testTCH() {
        assertEquals( "RX", this.getMetaphone().metaphone("RETCH") );
        assertEquals( "WX", this.getMetaphone().metaphone("WATCH") );
    }

    public void testExceedLength() {
        // should be AKSKS, but istruncated by Max Code Length
        assertEquals( "AKSK", this.getMetaphone().metaphone("AXEAXE") );
    }

    public void testSetMaxLengthWithTruncation() {
        // should be AKSKS, but istruncated by Max Code Length
        this.getMetaphone().setMaxCodeLen( 6 );
        assertEquals( "AKSKSK", this.getMetaphone().metaphone("AXEAXEAXE") );
    }

    public void validateFixture(String[][] pairs) {
        if (pairs.length == 0) {
            fail("Test fixture is empty");
        }
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i].length != 2) {
                fail("Error in test fixture in the data array at index " + i);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15211.java