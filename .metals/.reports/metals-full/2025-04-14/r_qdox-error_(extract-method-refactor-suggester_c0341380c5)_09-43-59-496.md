error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1230.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1230.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1230.java
text:
```scala
a@@ssertNull(parser.getRecord());

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

package org.apache.commons.csv;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * CSVParserTest
 *
 * The test are organized in three different sections:
 * The 'setter/getter' section, the lexer section and finally the parser
 * section. In case a test fails, you should follow a top-down approach for
 * fixing a potential bug (its likely that the parser itself fails if the lexer
 * has problems...).
 */
public class CSVParserTest {

    String code = "a,b,c,d\n"
                    + " a , b , 1 2 \n"
                    + "\"foo baar\", b,\n"
                    // + "   \"foo\n,,\n\"\",,\n\\\"\",d,e\n";
                    + "   \"foo\n,,\n\"\",,\n\"\"\",d,e\n";   // changed to use standard CSV escaping
    String[][] res = {
            {"a", "b", "c", "d"},
            {"a", "b", "1 2"},
            {"foo baar", "b", ""},
            {"foo\n,,\n\",,\n\"", "d", "e"}
    };

    @Test
    public void testGetLine() throws IOException {
        CSVParser parser = new CSVParser(new StringReader(code));
        for (String[] re : res) {
            assertTrue(Arrays.equals(re, parser.getRecord().values()));
        }
        
        assertTrue(parser.getRecord() == null);
    }

    @Test
    public void testGetRecords() throws IOException {
        CSVParser parser = new CSVParser(new StringReader(code));
        List<CSVRecord> records = parser.getRecords();
        assertEquals(res.length, records.size());
        assertTrue(records.size() > 0);
        for (int i = 0; i < res.length; i++) {
            assertTrue(Arrays.equals(res[i], records.get(i).values()));
        }
    }

    @Test
    public void testExcelFormat1() throws IOException {
        String code =
                "value1,value2,value3,value4\r\na,b,c,d\r\n  x,,,"
                        + "\r\n\r\n\"\"\"hello\"\"\",\"  \"\"world\"\"\",\"abc\ndef\",\r\n";
        String[][] res = {
                {"value1", "value2", "value3", "value4"},
                {"a", "b", "c", "d"},
                {"  x", "", "", ""},
                {""},
                {"\"hello\"", "  \"world\"", "abc\ndef", ""}
        };
        CSVParser parser = new CSVParser(code, CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(res.length, records.size());
        assertTrue(records.size() > 0);
        for (int i = 0; i < res.length; i++) {
            assertTrue(Arrays.equals(res[i], records.get(i).values()));
        }
    }

    @Test
    public void testExcelFormat2() throws Exception {
        String code = "foo,baar\r\n\r\nhello,\r\n\r\nworld,\r\n";
        String[][] res = {
                {"foo", "baar"},
                {""},
                {"hello", ""},
                {""},
                {"world", ""}
        };
        CSVParser parser = new CSVParser(code, CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(res.length, records.size());
        assertTrue(records.size() > 0);
        for (int i = 0; i < res.length; i++) {
            assertTrue(Arrays.equals(res[i], records.get(i).values()));
        }
    }

    @Test
    public void testEndOfFileBehaviourExcel() throws Exception {
        String[] codes = {
                "hello,\r\n\r\nworld,\r\n",
                "hello,\r\n\r\nworld,",
                "hello,\r\n\r\nworld,\"\"\r\n",
                "hello,\r\n\r\nworld,\"\"",
                "hello,\r\n\r\nworld,\n",
                "hello,\r\n\r\nworld,",
                "hello,\r\n\r\nworld,\"\"\n",
                "hello,\r\n\r\nworld,\"\""
        };
        String[][] res = {
                {"hello", ""},
                {""},  // Excel format does not ignore empty lines
                {"world", ""}
        };
        
        for (String code : codes) {
            CSVParser parser = new CSVParser(code, CSVFormat.EXCEL);
            List<CSVRecord> records = parser.getRecords();
            assertEquals(res.length, records.size());
            assertTrue(records.size() > 0);
            for (int i = 0; i < res.length; i++) {
                assertTrue(Arrays.equals(res[i], records.get(i).values()));
            }
        }
    }

    @Test
    public void testEndOfFileBehaviorCSV() throws Exception {
        String[] codes = {
                "hello,\r\n\r\nworld,\r\n",
                "hello,\r\n\r\nworld,",
                "hello,\r\n\r\nworld,\"\"\r\n",
                "hello,\r\n\r\nworld,\"\"",
                "hello,\r\n\r\nworld,\n",
                "hello,\r\n\r\nworld,",
                "hello,\r\n\r\nworld,\"\"\n",
                "hello,\r\n\r\nworld,\"\""
        };
        String[][] res = {
                {"hello", ""},  // CSV format ignores empty lines
                {"world", ""}
        };
        for (String code : codes) {
            CSVParser parser = new CSVParser(new StringReader(code));
            List<CSVRecord> records = parser.getRecords();
            assertEquals(res.length, records.size());
            assertTrue(records.size() > 0);
            for (int i = 0; i < res.length; i++) {
                assertTrue(Arrays.equals(res[i], records.get(i).values()));
            }
        }
    }

    @Test
    public void testEmptyLineBehaviourExcel() throws Exception {
        String[] codes = {
                "hello,\r\n\r\n\r\n",
                "hello,\n\n\n",
                "hello,\"\"\r\n\r\n\r\n",
                "hello,\"\"\n\n\n"
        };
        String[][] res = {
                {"hello", ""},
                {""},  // Excel format does not ignore empty lines
                {""}
        };
        for (String code : codes) {
            CSVParser parser = new CSVParser(code, CSVFormat.EXCEL);
            List<CSVRecord> records = parser.getRecords();
            assertEquals(res.length, records.size());
            assertTrue(records.size() > 0);
            for (int i = 0; i < res.length; i++) {
                assertTrue(Arrays.equals(res[i], records.get(i).values()));
            }
        }
    }

    @Test
    public void testEmptyLineBehaviourCSV() throws Exception {
        String[] codes = {
                "hello,\r\n\r\n\r\n",
                "hello,\n\n\n",
                "hello,\"\"\r\n\r\n\r\n",
                "hello,\"\"\n\n\n"
        };
        String[][] res = {
                {"hello", ""}  // CSV format ignores empty lines
        };
        for (String code : codes) {
            CSVParser parser = new CSVParser(new StringReader(code));
            List<CSVRecord> records = parser.getRecords();
            assertEquals(res.length, records.size());
            assertTrue(records.size() > 0);
            for (int i = 0; i < res.length; i++) {
                assertTrue(Arrays.equals(res[i], records.get(i).values()));
            }
        }
    }

    @Test
    @Ignore
    public void testBackslashEscapingOld() throws IOException {
        String code =
                "one,two,three\n"
                        + "on\\\"e,two\n"
                        + "on\"e,two\n"
                        + "one,\"tw\\\"o\"\n"
                        + "one,\"t\\,wo\"\n"
                        + "one,two,\"th,ree\"\n"
                        + "\"a\\\\\"\n"
                        + "a\\,b\n"
                        + "\"a\\\\,b\"";
        String[][] res = {
                {"one", "two", "three"},
                {"on\\\"e", "two"},
                {"on\"e", "two"},
                {"one", "tw\"o"},
                {"one", "t\\,wo"},  // backslash in quotes only escapes a delimiter (",")
                {"one", "two", "th,ree"},
                {"a\\\\"},     // backslash in quotes only escapes a delimiter (",")
                {"a\\", "b"},  // a backslash must be returnd
                {"a\\\\,b"}    // backslash in quotes only escapes a delimiter (",")
        };
        CSVParser parser = new CSVParser(new StringReader(code));
        List<CSVRecord> records = parser.getRecords();
        assertEquals(res.length, records.size());
        assertTrue(records.size() > 0);
        for (int i = 0; i < res.length; i++) {
            assertTrue(Arrays.equals(res[i], records.get(i).values()));
        }
    }

    @Test
    public void testBackslashEscaping() throws IOException {

        // To avoid confusion over the need for escaping chars in java code,
        // We will test with a forward slash as the escape char, and a single
        // quote as the encapsulator.

        String code =
                "one,two,three\n" // 0
                        + "'',''\n"       // 1) empty encapsulators
                        + "/',/'\n"       // 2) single encapsulators
                        + "'/'','/''\n"   // 3) single encapsulators encapsulated via escape
                        + "'''',''''\n"   // 4) single encapsulators encapsulated via doubling
                        + "/,,/,\n"       // 5) separator escaped
                        + "//,//\n"       // 6) escape escaped
                        + "'//','//'\n"   // 7) escape escaped in encapsulation
                        + "   8   ,   \"quoted \"\" /\" // string\"   \n"     // don't eat spaces
                        + "9,   /\n   \n"  // escaped newline
                        + "";
        String[][] res = {
                {"one", "two", "three"}, // 0
                {"", ""},                // 1
                {"'", "'"},              // 2
                {"'", "'"},              // 3
                {"'", "'"},              // 4
                {",", ","},              // 5
                {"/", "/"},              // 6
                {"/", "/"},              // 7
                {"   8   ", "   \"quoted \"\" \" / string\"   "},
                {"9", "   \n   "},
        };


        CSVFormat format = new CSVFormat(',', '\'', CSVFormat.DISABLED, '/', false, false, true, "\r\n", null);

        CSVParser parser = new CSVParser(code, format);
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.size() > 0);
        for (int i = 0; i < res.length; i++) {
            assertTrue(Arrays.equals(res[i], records.get(i).values()));
        }
    }

    @Test
    public void testBackslashEscaping2() throws IOException {

        // To avoid confusion over the need for escaping chars in java code,
        // We will test with a forward slash as the escape char, and a single
        // quote as the encapsulator.

        String code = ""
                + " , , \n"           // 1)
                + " \t ,  , \n"       // 2)
                + " // , /, , /,\n"   // 3)
                + "";
        String[][] res = {
                {" ", " ", " "},         // 1
                {" \t ", "  ", " "},     // 2
                {" / ", " , ", " ,"},    // 3
        };


        CSVFormat format = new CSVFormat(',',  CSVFormat.DISABLED,  CSVFormat.DISABLED, '/', false, false, true, "\r\n", null);

        CSVParser parser = new CSVParser(code, format);
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.size() > 0);

        assertTrue(CSVPrinterTest.equals(res, records));
    }

    @Test
    public void testDefaultFormat() throws IOException {
        String code = ""
                + "a,b\n"            // 1)
                + "\"\n\",\" \"\n"   // 2)
                + "\"\",#\n"   // 2)
                ;
        String[][] res = {
                {"a", "b"},
                {"\n", " "},
                {"", "#"},
        };

        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals(CSVFormat.DISABLED, format.getCommentStart());

        CSVParser parser = new CSVParser(code, format);
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.size() > 0);

        assertTrue(CSVPrinterTest.equals(res, records));

        String[][] res_comments = {
                {"a", "b"},
                {"\n", " "},
                {""},
        };

        format = CSVFormat.DEFAULT.withCommentStart('#');
        parser = new CSVParser(code, format);
        records = parser.getRecords();
        
        assertTrue(CSVPrinterTest.equals(res_comments, records));
    }

    @Test
    public void testCarriageReturnLineFeedEndings() throws IOException {
        String code = "foo\r\nbaar,\r\nhello,world\r\n,kanu";
        CSVParser parser = new CSVParser(new StringReader(code));
        List<CSVRecord> records = parser.getRecords();
        assertEquals(4, records.size());
    }

    @Test
    public void testCarriageReturnEndings() throws IOException {
        String code = "foo\rbaar,\rhello,world\r,kanu";
        CSVParser parser = new CSVParser(new StringReader(code));
        List<CSVRecord> records = parser.getRecords();
        assertEquals(4, records.size());
    }

    @Test
    public void testLineFeedEndings() throws IOException {
        String code = "foo\nbaar,\nhello,world\n,kanu";
        CSVParser parser = new CSVParser(new StringReader(code));
        List<CSVRecord> records = parser.getRecords();
        assertEquals(4, records.size());
    }

    @Test
    public void testIgnoreEmptyLines() throws IOException {
        String code = "\nfoo,baar\n\r\n,\n\n,world\r\n\n";
        //String code = "world\r\n\n";
        //String code = "foo;baar\r\n\r\nhello;\r\n\r\nworld;\r\n";
        CSVParser parser = new CSVParser(new StringReader(code));
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
    }

    @Test
    public void testForEach() throws Exception {
        List<CSVRecord> records = new ArrayList<CSVRecord>();
        
        Reader in = new StringReader("a,b,c\n1,2,3\nx,y,z");
        
        for (CSVRecord record : CSVFormat.DEFAULT.parse(in)) {
            records.add(record);
        }
        
        assertEquals(3, records.size());
        assertTrue(Arrays.equals(new String[]{"a", "b", "c"}, records.get(0).values()));
        assertTrue(Arrays.equals(new String[]{"1", "2", "3"}, records.get(1).values()));
        assertTrue(Arrays.equals(new String[]{"x", "y", "z"}, records.get(2).values()));
    }

    @Test
    public void testIterator() throws Exception {
        Reader in = new StringReader("a,b,c\n1,2,3\nx,y,z");
        
        Iterator<CSVRecord> iterator = CSVFormat.DEFAULT.parse(in).iterator();
        
        assertTrue(iterator.hasNext());
        try {
            iterator.remove();
            fail("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
        }
        assertTrue(Arrays.equals(new String[]{"a", "b", "c"}, iterator.next().values()));
        assertTrue(Arrays.equals(new String[]{"1", "2", "3"}, iterator.next().values()));
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertTrue(Arrays.equals(new String[]{"x", "y", "z"}, iterator.next().values()));
        assertFalse(iterator.hasNext());
        
        try {
            iterator.next();
            fail("NoSuchElementException expected");
        } catch (NoSuchElementException e) {
            // expected
        }
    }
    
    @Test
    public void testHeader() throws Exception {
        Reader in = new StringReader("a,b,c\n1,2,3\nx,y,z");

        Iterator<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(in).iterator();
        
        for (int i = 0; i < 2; i++) {
            assertTrue(records.hasNext());
            CSVRecord record = records.next();
            assertEquals(record.get(0), record.get("a"));
            assertEquals(record.get(1), record.get("b"));
            assertEquals(record.get(2), record.get("c"));
        }
        
        assertFalse(records.hasNext());
    }

    @Test
    public void testProvidedHeader() throws Exception {
        Reader in = new StringReader("a,b,c\n1,2,3\nx,y,z");

        Iterator<CSVRecord> records = CSVFormat.DEFAULT.withHeader("A", "B", "C").parse(in).iterator();

        for (int i = 0; i < 3; i++) {
            assertTrue(records.hasNext());
            CSVRecord record = records.next();
            assertEquals(record.get(0), record.get("A"));
            assertEquals(record.get(1), record.get("B"));
            assertEquals(record.get(2), record.get("C"));
        }

        assertFalse(records.hasNext());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1230.java