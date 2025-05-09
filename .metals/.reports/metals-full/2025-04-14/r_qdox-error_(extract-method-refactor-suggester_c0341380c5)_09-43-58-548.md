error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12451.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12451.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12451.java
text:
```scala
t@@ext = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(cal);

/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.commons.lang.time;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.TimeZone;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * TestCase for DurationFormatUtils.
 *
 * @author Apache Ant - DateUtilsTest
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author Stephen Colebourne
 * @author <a href="mailto:ggregory@seagullsw.com">Gary Gregory</a>
 */
public class DurationFormatUtilsTest extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
    	TestSuite suite = new TestSuite(DurationFormatUtilsTest.class);
    	suite.setName("DurationFormatUtils Tests");
        return suite;
    }

    public DurationFormatUtilsTest(String s) {
        super(s);
    }

    //-----------------------------------------------------------------------
    public void testConstructor() {
        assertNotNull(new DurationFormatUtils());
        Constructor[] cons = DurationFormatUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertEquals(true, Modifier.isPublic(cons[0].getModifiers()));
        assertEquals(false, Modifier.isPublic(DurationFormatUtils.class.getModifiers()));
        assertEquals(false, Modifier.isFinal(DurationFormatUtils.class.getModifiers()));
    }
    
    //-----------------------------------------------------------------------
    public void testFormatWords(){
        String text = null;
        
        text = DurationFormatUtils.formatWords(50*1000, true, false);
        assertEquals("50 seconds", text);
        text = DurationFormatUtils.formatWords(65*1000, true, false);
        assertEquals("1 minute 5 seconds", text);
        text = DurationFormatUtils.formatWords(120*1000, true, false);
        assertEquals("2 minutes 0 seconds", text);
        text = DurationFormatUtils.formatWords(121*1000, true, false);
        assertEquals("2 minutes 1 second", text);
        text = DurationFormatUtils.formatWords(72*60*1000, true, false);
        assertEquals("1 hour 12 minutes 0 seconds", text);
        
        text = DurationFormatUtils.formatWords(50*1000, true, true);
        assertEquals("50 seconds", text);
        text = DurationFormatUtils.formatWords(65*1000, true, true);
        assertEquals("1 minute 5 seconds", text);
        text = DurationFormatUtils.formatWords(120*1000, true, true);
        assertEquals("2 minutes", text);
        text = DurationFormatUtils.formatWords(121*1000, true, true);
        assertEquals("2 minutes 1 second", text);
        text = DurationFormatUtils.formatWords(72*60*1000, true, true);
        assertEquals("1 hour 12 minutes", text);
        
        text = DurationFormatUtils.formatWords(50*1000, false, true);
        assertEquals("0 days 0 hours 0 minutes 50 seconds", text);
        text = DurationFormatUtils.formatWords(65*1000, false, true);
        assertEquals("0 days 0 hours 1 minute 5 seconds", text);
        text = DurationFormatUtils.formatWords(120*1000, false, true);
        assertEquals("0 days 0 hours 2 minutes", text);
        text = DurationFormatUtils.formatWords(121*1000, false, true);
        assertEquals("0 days 0 hours 2 minutes 1 second", text);
        text = DurationFormatUtils.formatWords(72*60*1000, false, true);
        assertEquals("0 days 1 hour 12 minutes", text);
        
        text = DurationFormatUtils.formatWords(50*1000, false, false);
        assertEquals("0 days 0 hours 0 minutes 50 seconds", text);
        text = DurationFormatUtils.formatWords(65*1000, false, false);
        assertEquals("0 days 0 hours 1 minute 5 seconds", text);
        text = DurationFormatUtils.formatWords(120*1000, false, false);
        assertEquals("0 days 0 hours 2 minutes 0 seconds", text);
        text = DurationFormatUtils.formatWords(121*1000, false, false);
        assertEquals("0 days 0 hours 2 minutes 1 second", text);
        text = DurationFormatUtils.formatWords(72*60*1000, false, false);
        assertEquals("0 days 1 hour 12 minutes 0 seconds", text);
    }

    public void testFormatISOStyle(){
        long time = 0;
        assertEquals("0:00:00.000", DurationFormatUtils.formatISO(time));
        
        time = 1;
        assertEquals("0:00:00.001", DurationFormatUtils.formatISO(time));
        
        time = 15;
        assertEquals("0:00:00.015", DurationFormatUtils.formatISO(time));
        
        time = 165;
        assertEquals("0:00:00.165", DurationFormatUtils.formatISO(time));
        
        time = 1675;
        assertEquals("0:00:01.675", DurationFormatUtils.formatISO(time));
        
        time = 13465;
        assertEquals("0:00:13.465", DurationFormatUtils.formatISO(time));
        
        time = 72789;
        assertEquals("0:01:12.789", DurationFormatUtils.formatISO(time));
        
        time = 12789 + 32 * 60000;
        assertEquals("0:32:12.789", DurationFormatUtils.formatISO(time));
        
        time = 12789 + 62 * 60000;
        assertEquals("1:02:12.789", DurationFormatUtils.formatISO(time));
    }

    public void testISODurationFormat(){
        TimeZone timeZone = TimeZone.getTimeZone("GMT-3");
        Calendar cal = Calendar.getInstance(timeZone);
        cal.set(2002, 1, 23, 9, 11, 12);
        cal.set(Calendar.MILLISECOND, 1);
        String text;
        // repeat a test from testDateTimeISO to compare extended and not extended.
        text = DateFormatUtils.ISO_DATETIME_TIMEZONE_FORMAT.format(cal);
        assertEquals("2002-02-23T09:11:12-03:00", text);
        // test fixture is the same as above, but now with extended format.
        text = DurationFormatUtils.ISO_EXTENDED_FORMAT.format(cal);
        assertEquals("P2002Y2M23DT9H11M12.1S", text);
        // test fixture from example in http://www.w3.org/TR/xmlschema-2/#duration
        cal.set(1, 1, 3, 10, 30, 0);
        cal.set(Calendar.MILLISECOND, 0);
        text = DurationFormatUtils.ISO_EXTENDED_FORMAT.format(cal);
        assertEquals("P1Y2M3DT10H30M0.0S", text);
        // want a way to say 'don't print the seconds in format()' or other fields for that matter:
        //assertEquals("P1Y2M3DT10H30M", text);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12451.java