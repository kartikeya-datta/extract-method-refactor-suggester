error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4742.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4742.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4742.java
text:
```scala
w@@hile ( (line = bIn.readLine()) != null && counter <= 10) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.csv.writer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Tries to guess a config based on an InputStream.
 *
 * @author Martin van den Bemt
 * @version $Id: $
 */
public class CSVConfigGuesser {

    /** The stream to read */
    private InputStream in;
    /** 
     * if the file has a field header (need this info, to be able to guess better)
     * Defaults to false
     */
    private boolean hasFieldHeader = false;
    /** The found config */
    protected CSVConfig config;
    
    /**
     * 
     */
    public CSVConfigGuesser() {
        this.config = new CSVConfig();
    }
    
    /**
     * @param in the inputstream to guess from
     */
    public CSVConfigGuesser(InputStream in) {
        this();
        setInputStream(in);
    }
    
    public void setInputStream(InputStream in) {
        this.in = in;
    }
    
    /**
     * Allow override.
     * @return the inputstream that was set.
     */
    protected InputStream getInputStream() {
        return in;
    }
    
    /**
     * Guess the config based on the first 10 (or less when less available) 
     * records of a CSV file.
     * 
     * @return the guessed config.
     */
    public CSVConfig guess() {
        try {
            // tralalal
            BufferedReader bIn = new BufferedReader(new InputStreamReader((getInputStream())));
            String[] lines = new String[10];
            String line = null;
            int counter = 0;
            while ( (line = bIn.readLine()) != null || counter > 10) {
                lines[counter] = line;
                counter++;
            }
            if (counter < 10) {
                // remove nulls from the array, so we can skip the null checking.
                String[] newLines = new String[counter];
                System.arraycopy(lines, 0, newLines, 0, counter);
                lines = newLines;
            }
            analyseLines(lines);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch(Exception e) {
                    // ignore exception.
                }
            }
        }
        CSVConfig conf = config;
        // cleanup the config.
        config = null;
        return conf;
    }
    
    protected void analyseLines(String[] lines) {
        guessFixedWidth(lines);
        guessFieldSeperator(lines);
    }
    
    /**
     * Guess if this file is fixedwidth.
     * Just basing the fact on all lines being of the same length
     * @param lines
     */
    protected void guessFixedWidth(String[] lines) {
        int lastLength = 0;
        // assume fixedlength.
        config.setFixedWidth(true);
        for (int i = 0; i < lines.length; i++) {
            if (i == 0) {
                lastLength = lines[i].length();
            } else {
                if (lastLength != lines[i].length()) {
                    config.setFixedWidth(false);
                }
            }
        }
    }
        

    protected void guessFieldSeperator(String[] lines) {
        if (config.isFixedWidth()) {
            guessFixedWidthSeperator(lines);
            return;
        }
        for (int i = 0; i < lines.length; i++) {
        }
    }
    
    protected void guessFixedWidthSeperator(String[] lines) {
        // keep track of the fieldlength
        int previousMatch = -1;
        for (int i = 0; i < lines[0].length(); i++) {
            char last = ' ';
            boolean charMatches = true;
            for (int j = 0; j < lines.length; j++) {
                if (j == 0) {
                    last = lines[j].charAt(i);
                }
                if (last != lines[j].charAt(i)) {
                    charMatches = false;
                    break;
                } 
            }
            if (charMatches) {
                if (previousMatch == -1) {
                    previousMatch = 0;
                }
                CSVField field = new CSVField();
                field.setName("field"+config.getFields().length+1);
                field.setSize((i-previousMatch));
                config.addField(field);
            }
        }
    }
    /**
     * 
     * @return if the field uses a field header. Defaults to false.
     */
    public boolean hasFieldHeader() {
        return hasFieldHeader;
    }

    /**
     * Specify if the CSV file has a field header
     * @param hasFieldHeader true or false
     */
    public void setHasFieldHeader(boolean hasFieldHeader) {
        this.hasFieldHeader = hasFieldHeader;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4742.java