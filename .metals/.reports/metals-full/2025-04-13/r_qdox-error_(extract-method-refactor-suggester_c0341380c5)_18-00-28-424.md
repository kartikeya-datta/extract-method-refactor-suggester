error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14575.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14575.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 840
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14575.java
text:
```scala
class DefaultMetaFormatFactory {

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
p@@ackage org.apache.commons.lang.text;

import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;

/**
 * Factory methods to produce metaformat instances that behave like
 * java.text.MessageFormat.
 * 
 * @author Matt Benson
 * @since 2.4
 * @version $Id$
 */
/* package-private */ class DefaultMetaFormatFactory {

    /** Number key */
    public static final String NUMBER_KEY = "number";

    /** Date key */
    public static final String DATE_KEY = "date";

    /** Time key */
    public static final String TIME_KEY = "time";

    /** Choice key */
    public static final String CHOICE_KEY = "choice";

    private static final String[] NO_SUBFORMAT_KEYS = new String[] {
            NUMBER_KEY, DATE_KEY, TIME_KEY };

    private static final String[] NO_PATTERN_KEYS = new String[] { NUMBER_KEY,
            DATE_KEY, TIME_KEY, CHOICE_KEY };

    private static final String[] PATTERN_KEYS = new String[] { DATE_KEY,
            TIME_KEY };

    private static class OrderedNameKeyedMetaFormat extends NameKeyedMetaFormat {
        private static final long serialVersionUID = -7688772075239431055L;

        private List keys;

        private OrderedNameKeyedMetaFormat(String[] names, Format[] formats) {
            super(createMap(names, formats));
            this.keys = Arrays.asList(names);
        }

        private static Map createMap(String[] names, Format[] formats) {
            Validate.isTrue(ArrayUtils.isSameLength(names, formats));
            HashMap result = new HashMap(names.length);
            for (int i = 0; i < names.length; i++) {
                result.put(names[i], formats[i]);
            }
            return result;
        }

        protected Iterator iterateKeys() {
            return keys.iterator();
        }
    }

    /**
     * Get a default metaformat for the specified Locale.
     * 
     * @param locale
     *            the Locale for the resulting Format instance.
     * @return Format
     */
    public static Format getFormat(final Locale locale) {
        Format nmf = new NumberMetaFormat(locale);
        Format dmf = new DateMetaFormat(locale).setHandlePatterns(false);
        Format tmf = new TimeMetaFormat(locale).setHandlePatterns(false);

        return new MultiFormat(new Format[] {
                new OrderedNameKeyedMetaFormat(NO_SUBFORMAT_KEYS, new Format[] {
                        getDefaultFormat(nmf), getDefaultFormat(dmf),
                        getDefaultFormat(tmf) }),
                new OrderedNameKeyedMetaFormat(NO_PATTERN_KEYS, new Format[] {
                        nmf, dmf, tmf, ChoiceMetaFormat.INSTANCE }),
                new OrderedNameKeyedMetaFormat(PATTERN_KEYS,
                        new Format[] { new DateMetaFormat(locale),
                                new TimeMetaFormat(locale) }) });
    }

    private static Format getDefaultFormat(Format metaformat) {
        ParsePosition pos = new ParsePosition(0);
        Object o = metaformat.parseObject("", pos);
        return pos.getErrorIndex() < 0 ? (Format) o : null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14575.java