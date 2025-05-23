error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5558.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5558.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5558.java
text:
```scala
f@@or(int j= 0; j<NROUNDS; ++j) {

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
package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

/**
 * Unit tests {@link org.apache.commons.lang3.time.FastDateFormat}.
 *
 * @since 2.0
 * @version $Id$
 */
public class FastDateFormatTest {

    /*
     * Only the cache methods need to be tested here.  
     * The print methods are tested by {@link FastDateFormat_PrinterTest}
     * and the parse methods are tested by {@link FastDateFormat_ParserTest}
     */
    @Test
    public void test_getInstance() {
        final FastDateFormat format1 = FastDateFormat.getInstance();
        final FastDateFormat format2 = FastDateFormat.getInstance();
        assertSame(format1, format2);
    }

    @Test
    public void test_getInstance_String() {
        final FastDateFormat format1 = FastDateFormat.getInstance("MM/DD/yyyy");
        final FastDateFormat format2 = FastDateFormat.getInstance("MM-DD-yyyy");
        final FastDateFormat format3 = FastDateFormat.getInstance("MM-DD-yyyy");

        assertTrue(format1 != format2); // -- junit 3.8 version -- assertFalse(format1 == format2);
        assertSame(format2, format3);
        assertEquals("MM/DD/yyyy", format1.getPattern());
        assertEquals(TimeZone.getDefault(), format1.getTimeZone());
        assertEquals(TimeZone.getDefault(), format2.getTimeZone());
    }

    @Test
    public void test_getInstance_String_TimeZone() {
        final Locale realDefaultLocale = Locale.getDefault();
        final TimeZone realDefaultZone = TimeZone.getDefault();
        try {
            Locale.setDefault(Locale.US);
            TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));

            final FastDateFormat format1 = FastDateFormat.getInstance("MM/DD/yyyy",
                    TimeZone.getTimeZone("Atlantic/Reykjavik"));
            final FastDateFormat format2 = FastDateFormat.getInstance("MM/DD/yyyy");
            final FastDateFormat format3 = FastDateFormat.getInstance("MM/DD/yyyy", TimeZone.getDefault());
            final FastDateFormat format4 = FastDateFormat.getInstance("MM/DD/yyyy", TimeZone.getDefault());
            final FastDateFormat format5 = FastDateFormat.getInstance("MM-DD-yyyy", TimeZone.getDefault());
            final FastDateFormat format6 = FastDateFormat.getInstance("MM-DD-yyyy");

            assertTrue(format1 != format2); // -- junit 3.8 version -- assertFalse(format1 == format2);
            assertEquals(TimeZone.getTimeZone("Atlantic/Reykjavik"), format1.getTimeZone());
            assertEquals(TimeZone.getDefault(), format2.getTimeZone());
            assertSame(format3, format4);
            assertTrue(format3 != format5); // -- junit 3.8 version -- assertFalse(format3 == format5);
            assertTrue(format4 != format6); // -- junit 3.8 version -- assertFalse(format3 == format5);

        } finally {
            Locale.setDefault(realDefaultLocale);
            TimeZone.setDefault(realDefaultZone);
        }
    }

    @Test
    public void test_getInstance_String_Locale() {
        final Locale realDefaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.US);
            final FastDateFormat format1 = FastDateFormat.getInstance("MM/DD/yyyy", Locale.GERMANY);
            final FastDateFormat format2 = FastDateFormat.getInstance("MM/DD/yyyy");
            final FastDateFormat format3 = FastDateFormat.getInstance("MM/DD/yyyy", Locale.GERMANY);

            assertTrue(format1 != format2); // -- junit 3.8 version -- assertFalse(format1 == format2);
            assertSame(format1, format3);
            assertEquals(Locale.GERMANY, format1.getLocale());

        } finally {
            Locale.setDefault(realDefaultLocale);
        }
    }

    @Test
    public void test_changeDefault_Locale_DateInstance() {
        final Locale realDefaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.US);
            final FastDateFormat format1 = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.GERMANY);
            final FastDateFormat format2 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
            Locale.setDefault(Locale.GERMANY);
            final FastDateFormat format3 = FastDateFormat.getDateInstance(FastDateFormat.FULL);

            assertSame(Locale.GERMANY, format1.getLocale());
            assertSame(Locale.US, format2.getLocale());
            assertSame(Locale.GERMANY, format3.getLocale());
            assertTrue(format1 != format2); // -- junit 3.8 version -- assertFalse(format1 == format2);
            assertTrue(format2 != format3);

        } finally {
            Locale.setDefault(realDefaultLocale);
        }
    }

    @Test
    public void test_changeDefault_Locale_DateTimeInstance() {
        final Locale realDefaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.US);
            final FastDateFormat format1 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.GERMANY);
            final FastDateFormat format2 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
            Locale.setDefault(Locale.GERMANY);
            final FastDateFormat format3 = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);

            assertSame(Locale.GERMANY, format1.getLocale());
            assertSame(Locale.US, format2.getLocale());
            assertSame(Locale.GERMANY, format3.getLocale());
            assertTrue(format1 != format2); // -- junit 3.8 version -- assertFalse(format1 == format2);
            assertTrue(format2 != format3);

        } finally {
            Locale.setDefault(realDefaultLocale);
        }
    }

    @Test
    public void test_getInstance_String_TimeZone_Locale() {
        final Locale realDefaultLocale = Locale.getDefault();
        final TimeZone realDefaultZone = TimeZone.getDefault();
        try {
            Locale.setDefault(Locale.US);
            TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));

            final FastDateFormat format1 = FastDateFormat.getInstance("MM/DD/yyyy",
                    TimeZone.getTimeZone("Atlantic/Reykjavik"), Locale.GERMANY);
            final FastDateFormat format2 = FastDateFormat.getInstance("MM/DD/yyyy", Locale.GERMANY);
            final FastDateFormat format3 = FastDateFormat.getInstance("MM/DD/yyyy",
                    TimeZone.getDefault(), Locale.GERMANY);

            assertTrue(format1 != format2); // -- junit 3.8 version -- assertNotSame(format1, format2);
            assertEquals(TimeZone.getTimeZone("Atlantic/Reykjavik"), format1.getTimeZone());
            assertEquals(TimeZone.getDefault(), format2.getTimeZone());
            assertEquals(TimeZone.getDefault(), format3.getTimeZone());
            assertEquals(Locale.GERMANY, format1.getLocale());
            assertEquals(Locale.GERMANY, format2.getLocale());
            assertEquals(Locale.GERMANY, format3.getLocale());

        } finally {
            Locale.setDefault(realDefaultLocale);
            TimeZone.setDefault(realDefaultZone);
        }
    }       

    @Test
    public void testCheckDefaults() {
        final FastDateFormat format = FastDateFormat.getInstance();
        final FastDateFormat medium = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
        assertEquals(medium, format);
        
        final SimpleDateFormat sdf = new SimpleDateFormat();
        assertEquals(sdf.toPattern(), format.getPattern());
        
        assertEquals(Locale.getDefault(), format.getLocale());
        assertEquals(TimeZone.getDefault(), format.getTimeZone());        
    }

    @Test
    public void testCheckDifferingStyles() {
        final FastDateFormat shortShort = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, Locale.US);
        final FastDateFormat shortLong = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.LONG, Locale.US);
        final FastDateFormat longShort = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT, Locale.US);
        final FastDateFormat longLong = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.US);
        
        assertFalse(shortShort.equals(shortLong));
        assertFalse(shortShort.equals(longShort));
        assertFalse(shortShort.equals(longLong));      
        assertFalse(shortLong.equals(longShort));
        assertFalse(shortLong.equals(longLong));
        assertFalse(longShort.equals(longLong));
    }

    @Test
    public void testDateDefaults() {
        assertEquals(FastDateFormat.getDateInstance(FastDateFormat.LONG, Locale.CANADA), 
                FastDateFormat.getDateInstance(FastDateFormat.LONG, TimeZone.getDefault(), Locale.CANADA));
        
        assertEquals(FastDateFormat.getDateInstance(FastDateFormat.LONG, TimeZone.getTimeZone("America/New_York")), 
                FastDateFormat.getDateInstance(FastDateFormat.LONG, TimeZone.getTimeZone("America/New_York"), Locale.getDefault()));

        assertEquals(FastDateFormat.getDateInstance(FastDateFormat.LONG), 
                FastDateFormat.getDateInstance(FastDateFormat.LONG, TimeZone.getDefault(), Locale.getDefault()));
    }

    @Test
    public void testTimeDefaults() {
        assertEquals(FastDateFormat.getTimeInstance(FastDateFormat.LONG, Locale.CANADA),
                FastDateFormat.getTimeInstance(FastDateFormat.LONG, TimeZone.getDefault(), Locale.CANADA));

        assertEquals(FastDateFormat.getTimeInstance(FastDateFormat.LONG, TimeZone.getTimeZone("America/New_York")),
                FastDateFormat.getTimeInstance(FastDateFormat.LONG, TimeZone.getTimeZone("America/New_York"), Locale.getDefault()));

        assertEquals(FastDateFormat.getTimeInstance(FastDateFormat.LONG),
                FastDateFormat.getTimeInstance(FastDateFormat.LONG, TimeZone.getDefault(), Locale.getDefault()));
    }

    @Test
    public void testTimeDateDefaults() {
        assertEquals(FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM, Locale.CANADA),
                FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM, TimeZone.getDefault(), Locale.CANADA));

        assertEquals(FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM, TimeZone.getTimeZone("America/New_York")),
                FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM, TimeZone.getTimeZone("America/New_York"), Locale.getDefault()));

        assertEquals(FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM),
                FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM, TimeZone.getDefault(), Locale.getDefault()));
    }

    @Test
    public void testParseSync() throws InterruptedException {
        final String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS Z";
        final FastDateFormat formatter= FastDateFormat.getInstance(pattern);
        
        final long sdfTime= measureTime(formatter, new SimpleDateFormat(pattern) {
                        private static final long serialVersionUID = 1L;  // because SimpleDateFormat is serializable

                        @Override
                        public Object parseObject(final String formattedDate) throws ParseException {
                            synchronized(this) {
                                return super.parse(formattedDate);
                            }
                        }
        });
        
        final long fdfTime= measureTime(formatter, FastDateFormat.getInstance(pattern));
        
        final String times= ">>FastDateFormatTest: FastDateParser:"+fdfTime+"  SimpleDateFormat:"+sdfTime;
        System.out.println(times);
    }

    final static private int NTHREADS= 10;
    final static private int NROUNDS= 10000;
    
    private long measureTime(final Format formatter, final Format parser) throws InterruptedException {
        final ExecutorService pool = Executors.newFixedThreadPool(NTHREADS);
        final AtomicInteger failures= new AtomicInteger(0);
        final AtomicLong totalElapsed= new AtomicLong(0);
        
        for(int i= 0; i<NTHREADS; ++i) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    for(int i= 0; i<NROUNDS; ++i) {
                        try {
                            final Date date= new Date();
                            final String formattedDate= formatter.format(date);
                            final long start= System.currentTimeMillis();        
                            final Object pd= parser.parseObject(formattedDate);
                            totalElapsed.addAndGet(System.currentTimeMillis()-start);
                            if(!date.equals(pd)) {
                                failures.incrementAndGet();
                            }
                        } catch (final Exception e) {
                            failures.incrementAndGet();
                            e.printStackTrace();
                        }
                    }
                }                
            });
        }
        pool.shutdown();                        
        if(!pool.awaitTermination(20, TimeUnit.SECONDS)) {
            pool.shutdownNow();
            fail("did not complete tasks");
        }
        assertEquals(0, failures.get());
        return totalElapsed.get();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5558.java