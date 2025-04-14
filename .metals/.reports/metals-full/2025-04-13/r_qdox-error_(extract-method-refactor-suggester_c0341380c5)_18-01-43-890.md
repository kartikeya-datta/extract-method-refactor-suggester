error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8771.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8771.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8771.java
text:
```scala
t@@xt = txt.toLowerCase(java.util.Locale.ENGLISH);

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
import org.apache.commons.codec.StringEncoder;

/**
 * Encodes a string into a caverphone value. 
 *
 * This is an algorithm created the Caversham Project at the University of Otago. 
 * It implements the Caverphone 2.0 algorithm:
 *
 *
 * @author Apache Software Foundation
 * @version $Id$
 * @see <a href="http://en.wikipedia.org/wiki/Caverphone">Wikipedia - Caverphone</a>
 * @see <a href="http://caversham.otago.ac.nz/files/working/ctp150804.pdf">Caverphone 2.0 specification</a>
 */
public class Caverphone implements StringEncoder {

    /**
     * Creates an instance of the Caverphone encoder
     */
    public Caverphone() {
        super();
    }

    /**
     * Find the caverphone value of a String. 
     *
     * @param txt String to find the caverphone code for
     * @return A caverphone code corresponding to the String supplied
     */
    public String caverphone(String txt) {
        // NOTE: Version 1.0 of Caverphone is easily derivable from this code 
        // by commenting out the 2.0 lines and adding in the 1.0 lines

        if( txt == null || txt.length() == 0 ) {
            return "1111111111";
        }

        // 1. Convert to lowercase
        txt = txt.toLowerCase();

        // 2. Remove anything not A-Z
        txt = txt.replaceAll("[^a-z]", "");

        // 2.5. Remove final e
        txt = txt.replaceAll("e$", "");             // 2.0 only

        // 3. Handle various start options
        txt = txt.replaceAll("^cough", "cou2f");
        txt = txt.replaceAll("^rough", "rou2f");
        txt = txt.replaceAll("^tough", "tou2f");
        txt = txt.replaceAll("^enough", "enou2f");  // 2.0 only
        txt = txt.replaceAll("^trough", "trou2f");  // 2.0 only - note the spec says ^enough here again, c+p error I assume
        txt = txt.replaceAll("^gn", "2n");
        txt = txt.replaceAll("^mb", "m2");

        // 4. Handle replacements
        txt = txt.replaceAll("cq", "2q");
        txt = txt.replaceAll("ci", "si");
        txt = txt.replaceAll("ce", "se");
        txt = txt.replaceAll("cy", "sy");
        txt = txt.replaceAll("tch", "2ch");
        txt = txt.replaceAll("c", "k");
        txt = txt.replaceAll("q", "k");
        txt = txt.replaceAll("x", "k");
        txt = txt.replaceAll("v", "f");
        txt = txt.replaceAll("dg", "2g");
        txt = txt.replaceAll("tio", "sio");
        txt = txt.replaceAll("tia", "sia");
        txt = txt.replaceAll("d", "t");
        txt = txt.replaceAll("ph", "fh");
        txt = txt.replaceAll("b", "p");
        txt = txt.replaceAll("sh", "s2");
        txt = txt.replaceAll("z", "s");
        txt = txt.replaceAll("^[aeiou]", "A");
        txt = txt.replaceAll("[aeiou]", "3");
        txt = txt.replaceAll("j", "y");        // 2.0 only
        txt = txt.replaceAll("^y3", "Y3");     // 2.0 only
        txt = txt.replaceAll("^y", "A");       // 2.0 only
        txt = txt.replaceAll("y", "3");        // 2.0 only
        txt = txt.replaceAll("3gh3", "3kh3");
        txt = txt.replaceAll("gh", "22");
        txt = txt.replaceAll("g", "k");
        txt = txt.replaceAll("s+", "S");
        txt = txt.replaceAll("t+", "T");
        txt = txt.replaceAll("p+", "P");
        txt = txt.replaceAll("k+", "K");
        txt = txt.replaceAll("f+", "F");
        txt = txt.replaceAll("m+", "M");
        txt = txt.replaceAll("n+", "N");
        txt = txt.replaceAll("w3", "W3");
        //txt = txt.replaceAll("wy", "Wy");    // 1.0 only
        txt = txt.replaceAll("wh3", "Wh3");
        txt = txt.replaceAll("w$", "3");       // 2.0 only
        //txt = txt.replaceAll("why", "Why");  // 1.0 only
        txt = txt.replaceAll("w", "2");
        txt = txt.replaceAll("^h", "A");
        txt = txt.replaceAll("h", "2");
        txt = txt.replaceAll("r3", "R3");
        txt = txt.replaceAll("r$", "3");       // 2.0 only
        //txt = txt.replaceAll("ry", "Ry");    // 1.0 only
        txt = txt.replaceAll("r", "2");
        txt = txt.replaceAll("l3", "L3");
        txt = txt.replaceAll("l$", "3");       // 2.0 only
        //txt = txt.replaceAll("ly", "Ly");    // 1.0 only
        txt = txt.replaceAll("l", "2");
        //txt = txt.replaceAll("j", "y");      // 1.0 only
        //txt = txt.replaceAll("y3", "Y3");    // 1.0 only
        //txt = txt.replaceAll("y", "2");      // 1.0 only

        // 5. Handle removals
        txt = txt.replaceAll("2", "");
        txt = txt.replaceAll("3$", "A");       // 2.0 only
        txt = txt.replaceAll("3", "");

        // 6. put ten 1s on the end
        txt = txt + "111111" + "1111";        // 1.0 only has 6 1s

        // 7. take the first six characters as the code
        return txt.substring(0, 10);          // 1.0 truncates to 6
    }

    /**
     * Encodes an Object using the caverphone algorithm.  This method
     * is provided in order to satisfy the requirements of the
     * Encoder interface, and will throw an EncoderException if the
     * supplied object is not of type java.lang.String.
     *
     * @param pObject Object to encode
     * @return An object (or type java.lang.String) containing the 
     *         caverphone code which corresponds to the String supplied.
     * @throws EncoderException if the parameter supplied is not
     *                          of type java.lang.String
     */
    public Object encode(Object pObject) throws EncoderException {
        if (!(pObject instanceof java.lang.String)) {
            throw new EncoderException("Parameter supplied to Caverphone encode is not of type java.lang.String"); 
        }
        return caverphone((String) pObject);
    }

    /**
     * Encodes a String using the Caverphone algorithm. 
     *
     * @param pString String object to encode
     * @return The caverphone code corresponding to the String supplied
     */
    public String encode(String pString) {
        return caverphone(pString);   
    }

    /**
     * Tests if the caverphones of two strings are identical.
     *
     * @param str1 First of two strings to compare
     * @param str2 Second of two strings to compare
     * @return <code>true</code> if the caverphones of these strings are identical, 
     *        <code>false</code> otherwise.
     */
    public boolean isCaverphoneEqual(String str1, String str2) {
        return caverphone(str1).equals(caverphone(str2));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8771.java