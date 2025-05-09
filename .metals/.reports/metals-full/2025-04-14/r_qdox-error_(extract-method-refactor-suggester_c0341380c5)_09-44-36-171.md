error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12502.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12502.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12502.java
text:
```scala
private final c@@har[] soundexMapping;

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
 * Encodes a string into a Refined Soundex value. A refined soundex code is
 * optimized for spell checking words. Soundex method originally developed by
 * <CITE>Margaret Odell</CITE> and <CITE>Robert Russell</CITE>.
 * 
 * @author Apache Software Foundation
 * @version $Id$
 */
public class RefinedSoundex implements StringEncoder {

    /**
	 * This static variable contains an instance of the RefinedSoundex using
	 * the US_ENGLISH mapping.
	 */
    public static final RefinedSoundex US_ENGLISH = new RefinedSoundex();

    /**
	 * RefinedSoundex is *refined* for a number of reasons one being that the
	 * mappings have been altered. This implementation contains default
	 * mappings for US English.
	 */
    public static final char[] US_ENGLISH_MAPPING = "01360240043788015936020505".toCharArray();

    /**
	 * Every letter of the alphabet is "mapped" to a numerical value. This char
	 * array holds the values to which each letter is mapped. This
	 * implementation contains a default map for US_ENGLISH
	 */
    private char[] soundexMapping;

    /**
	 * Creates an instance of the RefinedSoundex object using the default US
	 * English mapping.
	 */
    public RefinedSoundex() {
        this(US_ENGLISH_MAPPING);
    }

    /**
	 * Creates a refined soundex instance using a custom mapping. This
	 * constructor can be used to customize the mapping, and/or possibly
	 * provide an internationalized mapping for a non-Western character set.
	 * 
	 * @param mapping
	 *                  Mapping array to use when finding the corresponding code for
	 *                  a given character
	 */
    public RefinedSoundex(char[] mapping) {
        this.soundexMapping = mapping;
    }

    /**
	 * Returns the number of characters in the two encoded Strings that are the
	 * same. This return value ranges from 0 to the length of the shortest
	 * encoded String: 0 indicates little or no similarity, and 4 out of 4 (for
	 * example) indicates strong similarity or identical values. For refined
	 * Soundex, the return value can be greater than 4.
	 * 
	 * @param s1
	 *                  A String that will be encoded and compared.
	 * @param s2
	 *                  A String that will be encoded and compared.
	 * @return The number of characters in the two encoded Strings that are the
	 *             same from 0 to to the length of the shortest encoded String.
	 * 
	 * @see SoundexUtils#difference(StringEncoder,String,String)
	 * @see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_de-dz_8co5.asp">
	 *          MS T-SQL DIFFERENCE</a>
	 * 
	 * @throws EncoderException
	 *                  if an error occurs encoding one of the strings
     * @since 1.3
	 */
    public int difference(String s1, String s2) throws EncoderException {
        return SoundexUtils.difference(this, s1, s2);
    }

    /**
	 * Encodes an Object using the refined soundex algorithm. This method is
	 * provided in order to satisfy the requirements of the Encoder interface,
	 * and will throw an EncoderException if the supplied object is not of type
	 * java.lang.String.
	 * 
	 * @param pObject
	 *                  Object to encode
	 * @return An object (or type java.lang.String) containing the refined
	 *             soundex code which corresponds to the String supplied.
	 * @throws EncoderException
	 *                  if the parameter supplied is not of type java.lang.String
	 */
    public Object encode(Object pObject) throws EncoderException {
        if (!(pObject instanceof java.lang.String)) {
            throw new EncoderException("Parameter supplied to RefinedSoundex encode is not of type java.lang.String");
        }
        return soundex((String) pObject);
    }

    /**
	 * Encodes a String using the refined soundex algorithm.
	 * 
	 * @param pString
	 *                  A String object to encode
	 * @return A Soundex code corresponding to the String supplied
	 */
    public String encode(String pString) {
        return soundex(pString);
    }

    /**
	 * Returns the mapping code for a given character. The mapping codes are
	 * maintained in an internal char array named soundexMapping, and the
	 * default values of these mappings are US English.
	 * 
	 * @param c
	 *                  char to get mapping for
	 * @return A character (really a numeral) to return for the given char
	 */
    char getMappingCode(char c) {
        if (!Character.isLetter(c)) {
            return 0;
        }
        return this.soundexMapping[Character.toUpperCase(c) - 'A'];
    }

    /**
	 * Retreives the Refined Soundex code for a given String object.
	 * 
	 * @param str
	 *                  String to encode using the Refined Soundex algorithm
	 * @return A soundex code for the String supplied
	 */
    public String soundex(String str) {
        if (str == null) {
            return null;
        }
        str = SoundexUtils.clean(str);
        if (str.length() == 0) {
            return str;
        }

        StringBuffer sBuf = new StringBuffer();
        sBuf.append(str.charAt(0));

        char last, current;
        last = '*';

        for (int i = 0; i < str.length(); i++) {

            current = getMappingCode(str.charAt(i));
            if (current == last) {
                continue;
            } else if (current != 0) {
                sBuf.append(current);
            }

            last = current;

        }

        return sBuf.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12502.java