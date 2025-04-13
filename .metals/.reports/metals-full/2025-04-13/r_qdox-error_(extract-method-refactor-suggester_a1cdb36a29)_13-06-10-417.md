error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14377.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14377.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14377.java
text:
```scala
f@@ormat = new CSVFormat('\t',  CSVFormat.DISABLED,  CSVFormat.DISABLED, '\\', false, false, false, false);

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
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;

import junit.framework.TestCase;

/**
 * CSVPrinterTest
 */
public class CSVPrinterTest extends TestCase {

    String lineSeparator = "\n";

    public void testPrinter1() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        String[] line1 = {"a", "b"};
        printer.println(line1);
        assertEquals("a,b" + lineSeparator, sw.toString());
    }

    public void testPrinter2() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        String[] line1 = {"a,b", "b"};
        printer.println(line1);
        assertEquals("\"a,b\",b" + lineSeparator, sw.toString());
    }

    public void testPrinter3() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        String[] line1 = {"a, b", "b "};
        printer.println(line1);
        assertEquals("\"a, b\",\"b \"" + lineSeparator, sw.toString());
    }

    public void testPrinter4() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        String[] line1 = {"a", "b\"c"};
        printer.println(line1);
        assertEquals("a,\"b\"\"c\"" + lineSeparator, sw.toString());
    }

    public void testPrinter5() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        String[] line1 = {"a", "b\nc"};
        printer.println(line1);
        assertEquals("a,\"b\nc\"" + lineSeparator, sw.toString());
    }

    public void testPrinter6() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        String[] line1 = {"a", "b\r\nc"};
        printer.println(line1);
        assertEquals("a,\"b\r\nc\"" + lineSeparator, sw.toString());
    }

    public void testPrinter7() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        String[] line1 = {"a", "b\\c"};
        printer.println(line1);
        assertEquals("a,b\\c" + lineSeparator, sw.toString());
    }

    public void testExcelPrinter1() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.EXCEL);
        String[] line1 = {"a", "b"};
        printer.println(line1);
        assertEquals("a,b" + lineSeparator, sw.toString());
    }

    public void testExcelPrinter2() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.EXCEL);
        String[] line1 = {"a,b", "b"};
        printer.println(line1);
        assertEquals("\"a,b\",b" + lineSeparator, sw.toString());
    }


    public void testRandom() throws Exception {
        int iter = 10000;
        format = CSVFormat.DEFAULT;
        doRandom(iter);
        format = CSVFormat.EXCEL;
        doRandom(iter);

        // Format for MySQL
        format = new CSVFormat('\t', CSVFormat.ENCAPSULATOR_DISABLED, CSVFormat.COMMENTS_DISABLED, '\\', false, false, false, false);
        doRandom(iter);
    }

    Random r = new Random();
    CSVFormat format;

    public void doRandom(int iter) throws Exception {
        for (int i = 0; i < iter; i++) {
            doOneRandom();
        }
    }

    public void doOneRandom() throws Exception {
        int nLines = r.nextInt(4) + 1;
        int nCol = r.nextInt(3) + 1;
        // nLines=1;nCol=2;
        String[][] lines = new String[nLines][];
        for (int i = 0; i < nLines; i++) {
            String[] line = new String[nCol];
            lines[i] = line;
            for (int j = 0; j < nCol; j++) {
                line[j] = randStr();
            }
        }

        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, format);

        for (int i = 0; i < nLines; i++) {
            // for (int j=0; j<lines[i].length; j++) System.out.println("### VALUE=:" + printable(lines[i][j]));
            printer.println(lines[i]);
        }

        printer.flush();
        String result = sw.toString();
        // System.out.println("### :" + printable(result));

        StringReader reader = new StringReader(result);

        CSVParser parser = new CSVParser(reader, format);
        String[][] parseResult = parser.getRecords();

        if (!equals(lines, parseResult)) {
            System.out.println("Printer output :" + printable(result));
            assertTrue(false);
        }
    }

    public static boolean equals(String[][] a, String[][] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            String[] linea = a[i];
            String[] lineb = b[i];
            if (linea.length != lineb.length) {
                return false;
            }
            for (int j = 0; j < linea.length; j++) {
                String aval = linea[j];
                String bval = lineb[j];
                if (!aval.equals(bval)) {
                    System.out.println("expected  :" + printable(aval));
                    System.out.println("got       :" + printable(bval));
                    return false;
                }
            }
        }
        return true;
    }

    public static String printable(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch <= ' ' || ch >= 128) {
                sb.append("(" + (int) ch + ")");
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public String randStr() {
        int sz = r.nextInt(20);
        // sz = r.nextInt(3);
        char[] buf = new char[sz];
        for (int i = 0; i < sz; i++) {
            // stick in special chars with greater frequency
            char ch;
            int what = r.nextInt(20);
            switch (what) {
                case 0:
                    ch = '\r';
                    break;
                case 1:
                    ch = '\n';
                    break;
                case 2:
                    ch = '\t';
                    break;
                case 3:
                    ch = '\f';
                    break;
                case 4:
                    ch = ' ';
                    break;
                case 5:
                    ch = ',';
                    break;
                case 6:
                    ch = '"';
                    break;
                case 7:
                    ch = '\'';
                    break;
                case 8:
                    ch = '\\';
                    break;
                default:
                    ch = (char) r.nextInt(300);
                    break;
                // default: ch = 'a'; break;
            }
            buf[i] = ch;
        }
        return new String(buf);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14377.java