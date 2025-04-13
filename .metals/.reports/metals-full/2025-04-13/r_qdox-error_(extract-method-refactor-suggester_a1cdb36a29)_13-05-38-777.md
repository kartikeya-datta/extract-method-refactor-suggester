error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/765.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/765.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/765.java
text:
```scala
public static P@@rice makePrice( ) { return new Price( "USD", BigDecimal.valueOf(1L), DEFAULT_TIMESTAMP ); }

/*

   Derby - Class org.apache.derbyTesting.functionTests.tests.lang.Price

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derbyTesting.functionTests.tests.lang;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Sample UDT for tests.
 */
public class Price implements Externalizable
{
    // initial version id
    private static final int FIRST_VERSION = 0;
    private static final int TIMESTAMPED_VERSION = FIRST_VERSION + 1;

    private static final Timestamp DEFAULT_TIMESTAMP = new Timestamp( 0L );

    private static Price _savedPrice;

    public String currencyCode;
    public BigDecimal amount;
    public Timestamp timeInstant;

    // methods to be registered as functions
    public static Price makePrice( ) { return new Price( "USD", new BigDecimal( 1 ), DEFAULT_TIMESTAMP ); }
    public static Price makePrice( String currencyCode, BigDecimal amount, Timestamp timeInstant ) { return new Price( currencyCode, amount, timeInstant ); }
    public static String getCurrencyCode( Price price ) { return price.currencyCode; }
    public static BigDecimal getAmount( Price price ) { return price.amount; }
    public static Timestamp getTimeInstant( Price price ) { return price.timeInstant; }
    public static void savePrice( Price price ) { _savedPrice = price; }
    public static Price getSavedPrice() { return _savedPrice; }

    // 0-arg constructor needed by Externalizable machinery
    public Price() {}

    public Price( String currencyCode, BigDecimal amount, Timestamp timeInstant )
    {
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.timeInstant = timeInstant;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append( "Price( " + currencyCode + ", " + amount + ", "  );
        if ( DEFAULT_TIMESTAMP.equals( timeInstant ) ) { buffer.append( "XXX" ); }
        else { buffer.append( timeInstant ); }
        buffer.append( " )" );

        return buffer.toString();
    }

    public boolean equals( Object other )
    {
        if ( other == null )  { return false; }
        if ( !(other instanceof Price) ) { return false; }

        Price that = (Price) other;

        return this.toString().equals( that.toString() );
    }

    // Externalizable implementation
    public void writeExternal(ObjectOutput out) throws IOException
    {
        // first write the version id
        out.writeInt( TIMESTAMPED_VERSION );

        // now write the state
        out.writeObject( currencyCode );
        out.writeObject( amount );
        out.writeObject( timeInstant );
    }  
    public void readExternal(ObjectInput in)throws IOException, ClassNotFoundException
    {
        // read the version id
        int oldVersion = in.readInt();
        if ( oldVersion < FIRST_VERSION ) { throw new IOException( "Corrupt data stream." ); }
        if ( oldVersion > TIMESTAMPED_VERSION ) { throw new IOException( "Can't deserialize from the future." ); }

        currencyCode = (String) in.readObject();
        amount = (BigDecimal) in.readObject();

        if ( oldVersion >= TIMESTAMPED_VERSION ) { timeInstant = (Timestamp) in.readObject(); }
        else { timeInstant = DEFAULT_TIMESTAMP; }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/765.java