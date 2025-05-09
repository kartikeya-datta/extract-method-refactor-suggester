error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13592.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13592.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13592.java
text:
```scala
a@@ssertTimeZonesEquals(c1, (CalendarFields) pm.detachCopy(c2));

/*
 * TestCalendarFields.java
 *
 * Created on October 9, 2006, 6:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

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
package org.apache.openjpa.persistence.kernel;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;



import org.apache.openjpa.persistence.kernel.common.apps.CalendarFields;

import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.lib.util.JavaVersions;
import org.apache.openjpa.persistence.OpenJPAEntityManager;

public class TestCalendarFields extends BaseKernelTest {

    /**
     * Creates a new instance of TestCalendarFields
     */
    public TestCalendarFields(String name) {
        super(name);
    }

    public void setUp() {
        deleteAll(CalendarFields.class);
    }

    public void testFieldDefaultTimeZone() {
        CalendarFields cal = new CalendarFields();

        OpenJPAEntityManager pm;

        pm = getPM();
        startTx(pm);
        cal.setSingapore(Calendar.
            getInstance(TimeZone.getTimeZone("America/New_York")));
        pm.persist(cal);
        int id = cal.getId();
        endTx(pm);
        endEm(pm);

        pm = getPM();
        cal = (CalendarFields) pm.find(CalendarFields.class, id);
        assertEquals(TimeZone.getTimeZone("Asia/Singapore"),
            cal.getSingapore().getTimeZone());
        endEm(pm);
    }

    public void testTimeZoneEquals() {
        CalendarFields c1 = new CalendarFields();
        CalendarFields c2 = new CalendarFields();
        assertTimeZonesEquals(c1, c2);

        OpenJPAEntityManager pm;

        pm = getPM();
        startTx(pm);
        pm.persist(c2);
        int id2 = c2.getId();
        assertTimeZonesEquals(c1, c2);
        endTx(pm);
        assertTimeZonesEquals(c1, c2);
        endEm(pm);

        pm = getPM();
        c2 = (CalendarFields) pm.find(CalendarFields.class, id2);
        assertTimeZonesEquals(c1, c2);
        assertTimeZonesEquals(c1, (CalendarFields) pm.detach(c2));
        endEm(pm);
    }

    public void testCalendarQuery() {
        long time = 1136660560572L;

        CalendarFields cal = new CalendarFields();

        OpenJPAEntityManager pm;

        pm = getPM();
        startTx(pm);
        cal.getSingapore().setTime(new Date(time));
        pm.persist(cal);
        int id = cal.getId();
        endTx(pm);
        endEm(pm);

        pm = getPM();

        //FIXME jthomas       
        Date date = new Date(time + 100000);
        Calendar cals = Calendar.getInstance();
        cals.setTime(date);

        String query =
            "SELECT o FROM CalendarFields o WHERE o.singapore < :cal";
        int size = pm.createQuery(query).setParameter("cal", cals).
            getResultList().size();

        assertEquals(1, size);

//        assertSize(0, pm.newQuery(CalendarFields.class, "singapore < :date").execute(new Date(time - 100000)));
//
//        assertSize(0, pm.newQuery(CalendarFields.class, "singapore > :date").execute(new Date(time + 100000)));
//        assertSize(1, pm.newQuery(CalendarFields.class, "singapore > :date").execute(new Date(time - 100000)));
//
//        assertSize(1, pm.newQuery(CalendarFields.class, "singapore < :date").execute(newCalendar(new Date(time + 100000), null)));
//        assertSize(0, pm.newQuery(CalendarFields.class, "singapore < :date").execute(newCalendar(new Date(time - 100000), null)));
//
//        assertSize(0, pm.newQuery(CalendarFields.class, "singapore > :date").execute(newCalendar(new Date(time + 100000), null)));
//        assertSize(1, pm.newQuery(CalendarFields.class, "singapore > :date").execute(newCalendar(new Date(time - 100000), null)));

        endEm(pm);
    }

    private static Calendar newCalendar(Date date, String tz) {
        Calendar cal = Calendar.getInstance(
            tz == null ? TimeZone.getDefault() : TimeZone.getTimeZone(tz));
        cal.setTime(date);
        return cal;
    }

    public void testMutateCalendarDirties() {
        CalendarFields c1 = new CalendarFields();

        OpenJPAEntityManager pm;

        pm = getPM();
        startTx(pm);
        pm.persist(c1);
        int id = c1.getId();
        endTx(pm);
        endEm(pm);

        pm = getPM();
        c1 = pm.find(CalendarFields.class, id);

        startTx(pm);

        OpenJPAStateManager sm = getStateManager(c1, pm);

        assertFalse(sm.getDirty().get(sm.getMetaData().
            getField("gmt").getIndex()));

        // test setting to same value doesn't dirty the field
        /*
        setTimeInMillis(c1.getGmtc1.getGmt().getTime().getTime());
        c1.getGmt().setTime(c1.getGmt().getTime());
        assertFalse(sm.getDirty().get(sm.getMetaData().
            getField("gmt").getIndex()));
        */

        // test changing time
        setTimeInMillis(c1.getGmt(), 12345);
        assertTrue(sm.getDirty().get(sm.getMetaData().
            getField("gmt").getIndex()));

        assertFalse(sm.getDirty().get(sm.getMetaData().
            getField("newYork").getIndex()));
        // test mutate via "add()" method
        c1.getNewYork().add(Calendar.SECOND, -1);
        assertTrue(sm.getDirty().get(sm.getMetaData().
            getField("newYork").getIndex()));

        assertFalse(sm.getDirty().get(sm.getMetaData().
            getField("berlin").getIndex()));
        // test mutate via "setTimeZone()" method
        c1.getBerlin().setTimeZone(TimeZone.getTimeZone("GMT"));
        assertTrue(sm.getDirty().get(sm.getMetaData().
            getField("berlin").getIndex()));

        // Calendar.set can only be subclassed in JDK 1.4+ (it is final in
        // 1.3), so we only run this test in JDK 1.4+
        if (JavaVersions.VERSION >= 4) {
            assertFalse(sm.getDirty().get(sm.getMetaData().
                getField("singapore").getIndex()));
            // test mutate via "set()" method
            c1.getSingapore().set(Calendar.YEAR, 1998);
            assertTrue(sm.getDirty().get(sm.getMetaData().
                getField("singapore").getIndex()));
        }

        assertFalse(sm.getDirty().get(sm.getMetaData().
            getField("pacific").getIndex()));
        // test mutate via "roll()" method
        c1.getPacific().roll(Calendar.YEAR, 5);
        assertTrue(sm.getDirty().get(sm.getMetaData().
            getField("pacific").getIndex()));

        endTx(pm);
        endEm(pm);
    }

    private static void setTimeInMillis(Calendar cal, long millis) {
        // "setTimeInMillis" is protected in JDK 1.3, put public in 1.4 & 1.5
        try {
            // Equivalent to: cal.setTimeInMillis (millis);
            cal.getClass().getMethod("setTimeInMillis",
                new Class[]{ long.class }).invoke(cal,
                new Object[]{ new Long(millis) });
        } catch (Exception e) {
            cal.setTime(new Date(millis));
        }
    }

    private void assertTimeZonesEquals(CalendarFields c1, CalendarFields c2) {
        for (int i = 0; i < c1.getCalendars().size(); i++) {
            Calendar cal1 = (Calendar) c1.getCalendars().get(i);
            Calendar cal2 = (Calendar) c2.getCalendars().get(i);

            if (cal1 != null && cal2 != null)
                assertEquals(cal1.getTimeZone().getID(),
                    cal2.getTimeZone().getID());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13592.java