error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4980.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4980.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4980.java
text:
```scala
public static i@@nt getDateTypeCode(Class<?> dtype) {

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
package org.apache.openjpa.jdbc.meta;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;

import org.apache.openjpa.meta.JavaTypes;
import serp.util.Numbers;

/**
 * Java SQL type constants.
 *
 * @author Abe White
 */
public class JavaSQLTypes
    extends JavaTypes {

    // constants for the sql types that aren't directly supported by
    // OpenJPA; make sure these don't conflict with our standard metadata types
    public static final int SQL_ARRAY = 1000;
    public static final int ASCII_STREAM = 1001;
    public static final int BINARY_STREAM = 1002;
    public static final int BLOB = 1003;
    public static final int BYTES = 1004;
    public static final int CHAR_STREAM = 1005;
    public static final int CLOB = 1006;
    public static final int SQL_DATE = 1007;
    public static final int SQL_OBJECT = 1008;
    public static final int REF = 1009;
    public static final int TIME = 1010;
    public static final int TIMESTAMP = 1011;
    public static final int JDBC_DEFAULT = 1012;

    private static final Byte ZERO_BYTE = new Byte((byte) 0);
    private static final Character ZERO_CHAR = new Character((char) 0);
    private static final Double ZERO_DOUBLE = new Double(0d);
    private static final Float ZERO_FLOAT = new Float(0f);
    private static final Short ZERO_SHORT = new Short((short) 0);
    private static final BigDecimal ZERO_BIGDECIMAL = new BigDecimal(0d);

    private static final Byte NONZERO_BYTE = new Byte((byte) 1);
    private static final Character NONZERO_CHAR = new Character((char) 'a');
    private static final Double NONZERO_DOUBLE = new Double(1d);
    private static final Float NONZERO_FLOAT = new Float(1f);
    private static final Short NONZERO_SHORT = new Short((short) 1);
    private static final BigInteger NONZERO_BIGINTEGER = new BigInteger("1");
    private static final BigDecimal NONZERO_BIGDECIMAL = new BigDecimal(1d);

    /**
     * Return the proper date typecode.
     */
    public static int getDateTypeCode(Class dtype) {
        if (dtype == java.util.Date.class)
            return DATE;
        if (dtype == java.sql.Date.class)
            return SQL_DATE;
        if (dtype == Timestamp.class)
            return TIMESTAMP;
        if (dtype == Time.class)
            return TIME;
        return OBJECT;
    }

    /**
     * Return an empty value object for the given type code.
     */
    public static Object getEmptyValue(int type) {
        switch (type) {
            case JavaTypes.STRING:
                return "";
            case JavaTypes.BOOLEAN:
            case JavaTypes.BOOLEAN_OBJ:
                return Boolean.FALSE;
            case JavaTypes.BYTE:
            case JavaTypes.BYTE_OBJ:
                return ZERO_BYTE;
            case JavaTypes.CHAR:
            case JavaTypes.CHAR_OBJ:
                return ZERO_CHAR;
            case JavaTypes.DOUBLE:
            case JavaTypes.DOUBLE_OBJ:
                return ZERO_DOUBLE;
            case JavaTypes.FLOAT:
            case JavaTypes.FLOAT_OBJ:
                return ZERO_FLOAT;
            case JavaTypes.INT:
            case JavaTypes.INT_OBJ:
                return Numbers.valueOf(0);
            case JavaTypes.LONG:
            case JavaTypes.LONG_OBJ:
                return Numbers.valueOf(0L);
            case JavaTypes.SHORT:
            case JavaTypes.SHORT_OBJ:
                return ZERO_SHORT;
            case JavaTypes.BIGINTEGER:
                return BigInteger.ZERO;
            case JavaTypes.BIGDECIMAL:
            case JavaTypes.NUMBER:
                return ZERO_BIGDECIMAL;
            default:
                return null;
        }
    }

    /**
     * Return a non-empty value object for the given type code.
     */
    public static Object getNonEmptyValue(int type) {
        switch (type) {
            case JavaTypes.STRING:
                return "x";
            case JavaTypes.BOOLEAN:
            case JavaTypes.BOOLEAN_OBJ:
                return Boolean.TRUE;
            case JavaTypes.BYTE:
            case JavaTypes.BYTE_OBJ:
                return NONZERO_BYTE;
            case JavaTypes.CHAR:
            case JavaTypes.CHAR_OBJ:
                return NONZERO_CHAR;
            case JavaTypes.DOUBLE:
            case JavaTypes.DOUBLE_OBJ:
                return NONZERO_DOUBLE;
            case JavaTypes.FLOAT:
            case JavaTypes.FLOAT_OBJ:
                return NONZERO_FLOAT;
            case JavaTypes.INT:
            case JavaTypes.INT_OBJ:
                return Numbers.valueOf(1);
            case JavaTypes.LONG:
            case JavaTypes.LONG_OBJ:
                return Numbers.valueOf(1L);
            case JavaTypes.SHORT:
            case JavaTypes.SHORT_OBJ:
                return NONZERO_SHORT;
            case JavaTypes.BIGINTEGER:
                return NONZERO_BIGINTEGER;
            case JavaTypes.BIGDECIMAL:
            case JavaTypes.NUMBER:
                return NONZERO_BIGDECIMAL;
            default:
                return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4980.java