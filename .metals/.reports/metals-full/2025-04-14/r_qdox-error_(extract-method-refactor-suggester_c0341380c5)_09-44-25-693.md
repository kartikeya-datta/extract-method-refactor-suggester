error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8483.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8483.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8483.java
text:
```scala
i@@f (!(obj instanceof CSVConfig)) {

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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The CSVConfig is used to configure the CSV writer
 *
 * @author Martin van den Bemt
 * @version $Id: $
 */
public class CSVConfig {

    /** specifies if it is a fixed width csv file */
    private boolean fixedWidth;
    
    /** list of fields */
    private List<CSVField> fields;

    /** Do no do any filling */
    public static final int FILLNONE = 0;
    
    /** Fill content the the left. Mainly usable together with fixedWidth */
    public static final int FILLLEFT = 1;
    
    /** Fill content to the right. Mainly usable together with fixedWidth */
    public static final int FILLRIGHT = 2;

    /** The fill pattern */
    private int fill;
    
    /** The fill char. Defaults to a space */
    private char fillChar = ' ';
    
    /** The seperator character. Defaults to ,*/
    private char delimiter = ',';
    
    /** The row separator. Defaults to \n */
    private String rowDelimiter = "\n";
    
    /** Should we ignore the delimiter. Defaults to false */
    private boolean ignoreDelimiter = false;
    
    /** the value delimiter. Defaults to " */
    private char valueDelimiter = '"';
    
    /** Should we ignore the value delimiter. Defaults to true */
    private boolean ignoreValueDelimiter = true;
    
    /** Specifies if we want to use a field header */
    private boolean fieldHeader = false;
    
    /** Specifies if the end of the line needs to be trimmed */
    private boolean endTrimmed = false;

    /**
     * @return if the CSV file is fixedWidth
     */
    public boolean isFixedWidth() {
        return fixedWidth;
    }

    /**
     * Specify if the CSV file is fixed width.
     * Defaults to false
     *
     * @param fixedWidth the fixedwidth
     */
    public void setFixedWidth(boolean fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    public void addField(CSVField field) {
        if (fields == null) {
            fields = new ArrayList<CSVField>();
        }
        fields.add(field);
    }

    /**
     * Set the fields that should be used by the writer.
     * This will overwrite currently added fields completely!
     *
     * @param csvFields the csvfields array. If null it will do nothing
     */
    public void setFields(CSVField[] csvFields) {
        if (csvFields == null) {
            return;
        }
        fields = new ArrayList(Arrays.asList(csvFields));
    }

    /**
     * Set the fields that should be used by the writer
     *
     * @param csvField a collection with fields. If null it will do nothing
     */
    public void setFields(Collection csvField) {
        if (csvField == null) {
            return;
        }
        fields = new ArrayList(csvField);
    }

    /**
     * @return an array with the known fields (even if no fields are specified)
     */
    public CSVField[] getFields() {
        CSVField[] csvFields = new CSVField[0];
        if (fields != null) {
            return (CSVField[]) fields.toArray(csvFields);
        }
        return csvFields;
    }

    public CSVField getField(String name) {
        if (fields == null || name == null) {
            return null;
        }
        for (int i = 0; i < fields.size(); i++) {
            CSVField field = (CSVField) fields.get(i);
            if (name.equals(field.getName())) {
                return field;
            }
        }
        return null;
    }

    /**
     * @return the fill pattern.
     */
    public int getFill() {
        return fill;
    }

    /**
     * Set the fill pattern. Defaults to {@link #FILLNONE}
     * <br/>Other options are : {@link #FILLLEFT} and {@link #FILLRIGHT}
     *
     * @param fill the fill pattern.
     */
    public void setFill(int fill) {
        this.fill = fill;
    }

    /**
     * @return the fillchar. Defaults to a space.
     */
    public char getFillChar() {
        return fillChar;
    }

    /**
     * Set the fill char
     *
     * @param fillChar the fill char
     */
    public void setFillChar(char fillChar) {
        this.fillChar = fillChar;
    }

    /**
     * @return the delimeter used.
     */
    public char getDelimiter() {
        return delimiter;
    }

    /**
     * Set the delimiter to use
     *
     * @param delimiter the delimiter character.
     */
    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * @return the rowDelimiter used.
     */
    public String getRowDelimiter() {
        return rowDelimiter;
    }

    /**
     * Set the rowDelimiter to use
     *
     * @param rowDelimiter the row delimiter character.
     */
    public void setRowDelimiter(String rowDelimiter) {
        this.rowDelimiter = rowDelimiter;
    }

    /**
     * @return if the writer should ignore the delimiter character.
     */
    public boolean isDelimiterIgnored() {
        return ignoreDelimiter;
    }

    /**
     * Specify if the writer should ignore the delimiter.
     *
     * @param ignoreDelimiter defaults to false.
     */
    public void setIgnoreDelimiter(boolean ignoreDelimiter) {
        this.ignoreDelimiter = ignoreDelimiter;
    }

    /**
     * @return the value delimeter used. Defaults to "
     */
    public char getValueDelimiter() {
        return valueDelimiter;
    }

    /**
     * Set the value delimiter to use
     *
     * @param valueDelimiter the value delimiter character.
     */
    public void setValueDelimiter(char valueDelimiter) {
        this.valueDelimiter = valueDelimiter;
    }

    /**
     * @return if the writer should ignore the value delimiter character.
     *         Defaults to true.
     */
    public boolean isValueDelimiterIgnored() {
        return ignoreValueDelimiter;
    }

    /**
     * Specify if the writer should ignore the value delimiter.
     *
     * @param ignoreValueDelimiter defaults to false.
     */
    public void setIgnoreValueDelimiter(boolean ignoreValueDelimiter) {
        this.ignoreValueDelimiter = ignoreValueDelimiter;
    }

    /**
     * @return if a field header is used. Defaults to false
     */
    public boolean isFieldHeader() {
        return fieldHeader;
    }

    /**
     * Specify if you want to use a field header.
     *
     * @param fieldHeader true or false.
     */
    public void setFieldHeader(boolean fieldHeader) {
        this.fieldHeader = fieldHeader;
    }

    /**
     * TODO..
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == null && !(obj instanceof CSVConfig)) {
            return false;
        }
        return super.equals(obj);
//        CSVConfig config = (CSVConfig) obj;
//        getFill() == config.getFill()
//        getFields().equals(config.getFields())
    }

    /**
     * Creates a config based on a stream. It tries to guess<br/>
     * NOTE : The stream will be closed.
     *
     * @param inputStream the inputstream.
     * @return the guessed config.
     */
    public static CSVConfig guessConfig(InputStream inputStream) {
        return null;
    }

    /**
     * @return if the end of the line should be trimmed. Default is false.
     */
    public boolean isEndTrimmed() {
        return endTrimmed;
    }

    /**
     * Specify if the end of the line needs to be trimmed. Defaults to false.
     *
     * @param endTrimmed
     */
    public void setEndTrimmed(boolean endTrimmed) {
        this.endTrimmed = endTrimmed;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8483.java