error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2852.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2852.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2852.java
text:
```scala
private final static S@@impleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss Z");

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.commons.compress.archivers.zip;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.zip.ZipException;

import static org.apache.commons.compress.archivers.zip.X5455_ExtendedTimestamp.ACCESS_TIME_BIT;
import static org.apache.commons.compress.archivers.zip.X5455_ExtendedTimestamp.CREATE_TIME_BIT;
import static org.apache.commons.compress.archivers.zip.X5455_ExtendedTimestamp.MODIFY_TIME_BIT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class X5455_ExtendedTimestampTest {
    private final static ZipShort X5455 = new ZipShort(0x5455);

    private final static ZipLong ZERO_TIME = new ZipLong(0);
    private final static ZipLong MAX_TIME_SECONDS = new ZipLong(0xFFFFFFFFL);
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd/HH:mm:ss Z");

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }


    /**
     * The extended field (xf) we are testing.
     */
    private X5455_ExtendedTimestamp xf;

    @Before
    public void before() {
        xf = new X5455_ExtendedTimestamp();
    }

    @Test
    public void testSampleFile() throws Exception {

        /*
        Contains entries with zipTime, accessTime, and modifyTime.
        The file name tells you the year we tried to set the time to
        (Jan 1st, Midnight, UTC).

        For example:

        COMPRESS-210_unix_time_zip_test/1999
        COMPRESS-210_unix_time_zip_test/2000
        COMPRESS-210_unix_time_zip_test/2108

        File's last-modified is 1st second after midnight.
        Zip-time's 2-second granularity rounds that up to 2nd second.
        File's last-access is 3rd second after midnight.

        So, from example above:

        1999's zip time:  Jan 1st, 1999-01-01/00:00:02
        1999's mod time:  Jan 1st, 1999-01-01/00:00:01
        1999's acc time:  Jan 1st, 1999-01-01/00:00:03
         */

        URL zip = getClass().getResource("/COMPRESS-210_unix_time_zip_test.zip");
        File archive = new File(new URI(zip.toString()));
        ZipFile zf = null;

        try {
            zf = new ZipFile(archive);
            Enumeration<ZipArchiveEntry> en = zf.getEntries();

            // We expect EVERY entry of this zip file
            // to contain extra field 0x5455.
            while (en.hasMoreElements()) {

                ZipArchiveEntry zae = en.nextElement();
                String name = zae.getName();
                X5455_ExtendedTimestamp xf = (X5455_ExtendedTimestamp) zae.getExtraField(X5455);

                Date z = zae.getLastModifiedDate();
                Date m = xf.getModifyJavaTime();
                Date a = xf.getAccessJavaTime();

                String zipTime = DATE_FORMAT.format(z);
                String modTime = DATE_FORMAT.format(m);
                String accTime = DATE_FORMAT.format(a);

                if (!zae.isDirectory()) {
                    int x = name.lastIndexOf('/');
                    String yearString = name.substring(x + 1);
                    int year;
                    try {
                        year = Integer.parseInt(yearString);
                    } catch (NumberFormatException nfe) {
                        year = -1;
                    }
                    if (year >= 0) {
                        switch (year) {
                            case 2107:
                                // Zip time is okay up to 2107.
                                assertEquals(zipTime, year + "-01-01/00:00:02 +0000");
                                // But the X5455 data has overflowed:
                                assertEquals(modTime, "1970-11-24/17:31:45 +0000");
                                assertEquals(accTime, "1970-11-24/17:31:47 +0000");
                                break;
                            case 2108:
                                // Zip time is still okay at Jan 1st midnight (UTC) in 2108
                                // because we created the zip file in pacific time zone, so it's
                                // actually still 2107 in the zip file!
                                assertEquals(zipTime, year + "-01-01/00:00:02 +0000");
                                // The X5455 data is still overflowed, of course:
                                assertEquals(modTime, "1971-11-24/17:31:45 +0000");
                                assertEquals(accTime, "1971-11-24/17:31:47 +0000");
                                break;
                            case 2109:
                                // All three timestamps have overflowed by 2109.
                                assertEquals(zipTime, "1981-01-01/00:00:02 +0000");
                                assertEquals(modTime, "1972-11-24/17:31:45 +0000");
                                assertEquals(accTime, "1972-11-24/17:31:47 +0000");

                                // Hmmm.... looks like one could examine both DOS time
                                // and the Unix time together to hack a nice workaround to
                                // get timestamps past 2106 in a reverse-compatible way.

                                break;
                            default:
                                // X5455 time is good from epoch (1970) to 2106.
                                // Zip time is good from 1980 to 2107.
                                if (year < 1980) {
                                    assertEquals(zipTime, "1980-01-01/08:00:00 +0000");
                                } else {
                                    assertEquals(zipTime, year + "-01-01/00:00:02 +0000");
                                }
                                assertEquals(modTime, year + "-01-01/00:00:01 +0000");
                                assertEquals(accTime, year + "-01-01/00:00:03 +0000");
                                break;
                        }
                    }
                }
            }
        } finally {
            if (zf != null) {
                zf.close();
            }
        }
    }


    @Test
    public void testMisc() throws Exception {
        assertFalse(xf.equals(new Object()));
        assertTrue(xf.toString().startsWith("0x5455 Zip Extra Field"));
        assertTrue(!xf.toString().contains(" Modify:"));
        assertTrue(!xf.toString().contains(" Access:"));
        assertTrue(!xf.toString().contains(" Create:"));
        Object o = xf.clone();
        assertEquals(o.hashCode(), xf.hashCode());
        assertTrue(xf.equals(o));

        xf.setModifyJavaTime(new Date(1111));
        xf.setAccessJavaTime(new Date(2222));
        xf.setCreateJavaTime(new Date(3333));
        xf.setFlags((byte) 7);
        assertFalse(xf.equals(o));
        assertTrue(xf.toString().startsWith("0x5455 Zip Extra Field"));
        assertTrue(xf.toString().contains(" Modify:"));
        assertTrue(xf.toString().contains(" Access:"));
        assertTrue(xf.toString().contains(" Create:"));
        o = xf.clone();
        assertEquals(o.hashCode(), xf.hashCode());
        assertTrue(xf.equals(o));
    }

    @Test
    public void testGettersSetters() {
        // X5455 is concerned with time, so let's
        // get a timestamp to play with (Jan 1st, 2000).
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.YEAR, 2000);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date timeMillis = cal.getTime();
        ZipLong time = new ZipLong(timeMillis.getTime() / 1000);

        // set too big
        try {
            // Java time is 1000 x larger (milliseconds).
            xf.setModifyJavaTime(new Date(1000L * (MAX_TIME_SECONDS.getValue() + 1L)));
            fail("Time too big for 32 bits!");
        } catch (IllegalArgumentException iae) {
            // All is good.
        }

        // get/set modify time
        xf.setModifyTime(time);
        assertEquals(time, xf.getModifyTime());
        assertEquals(timeMillis, xf.getModifyJavaTime());
        xf.setModifyJavaTime(timeMillis);
        assertEquals(time, xf.getModifyTime());
        assertEquals(timeMillis, xf.getModifyJavaTime());
        // Make sure milliseconds get zeroed out:
        xf.setModifyJavaTime(new Date(timeMillis.getTime() + 123));
        assertEquals(time, xf.getModifyTime());
        assertEquals(timeMillis, xf.getModifyJavaTime());
        // Null
        xf.setModifyTime(null);
        assertNull(xf.getModifyJavaTime());
        xf.setModifyJavaTime(null);
        assertNull(xf.getModifyTime());

        // get/set access time
        xf.setAccessTime(time);
        assertEquals(time, xf.getAccessTime());
        assertEquals(timeMillis, xf.getAccessJavaTime());
        xf.setAccessJavaTime(timeMillis);
        assertEquals(time, xf.getAccessTime());
        assertEquals(timeMillis, xf.getAccessJavaTime());
        // Make sure milliseconds get zeroed out:
        xf.setAccessJavaTime(new Date(timeMillis.getTime() + 123));
        assertEquals(time, xf.getAccessTime());
        assertEquals(timeMillis, xf.getAccessJavaTime());
        // Null
        xf.setAccessTime(null);
        assertNull(xf.getAccessJavaTime());
        xf.setAccessJavaTime(null);
        assertNull(xf.getAccessTime());

        // get/set create time
        xf.setCreateTime(time);
        assertEquals(time, xf.getCreateTime());
        assertEquals(timeMillis, xf.getCreateJavaTime());
        xf.setCreateJavaTime(timeMillis);
        assertEquals(time, xf.getCreateTime());
        assertEquals(timeMillis, xf.getCreateJavaTime());
        // Make sure milliseconds get zeroed out:
        xf.setCreateJavaTime(new Date(timeMillis.getTime() + 123));
        assertEquals(time, xf.getCreateTime());
        assertEquals(timeMillis, xf.getCreateJavaTime());
        // Null
        xf.setCreateTime(null);
        assertNull(xf.getCreateJavaTime());
        xf.setCreateJavaTime(null);
        assertNull(xf.getCreateTime());


        // initialize for flags
        xf.setModifyTime(time);
        xf.setAccessTime(time);
        xf.setCreateTime(time);

        // get/set flags: 000
        xf.setFlags((byte) 0);
        assertEquals(0, xf.getFlags());
        assertFalse(xf.isBit0_modifyTimePresent());
        assertFalse(xf.isBit1_accessTimePresent());
        assertFalse(xf.isBit2_createTimePresent());
        // Local length=1, Central length=1 (flags only!)
        assertEquals(1, xf.getLocalFileDataLength().getValue());
        assertEquals(1, xf.getCentralDirectoryLength().getValue());

        // get/set flags: 001
        xf.setFlags((byte) 1);
        assertEquals(1, xf.getFlags());
        assertTrue(xf.isBit0_modifyTimePresent());
        assertFalse(xf.isBit1_accessTimePresent());
        assertFalse(xf.isBit2_createTimePresent());
        // Local length=5, Central length=5 (flags + mod)
        assertEquals(5, xf.getLocalFileDataLength().getValue());
        assertEquals(5, xf.getCentralDirectoryLength().getValue());

        // get/set flags: 010
        xf.setFlags((byte) 2);
        assertEquals(2, xf.getFlags());
        assertFalse(xf.isBit0_modifyTimePresent());
        assertTrue(xf.isBit1_accessTimePresent());
        assertFalse(xf.isBit2_createTimePresent());
        // Local length=5, Central length=1
        assertEquals(5, xf.getLocalFileDataLength().getValue());
        assertEquals(1, xf.getCentralDirectoryLength().getValue());

        // get/set flags: 100
        xf.setFlags((byte) 4);
        assertEquals(4, xf.getFlags());
        assertFalse(xf.isBit0_modifyTimePresent());
        assertFalse(xf.isBit1_accessTimePresent());
        assertTrue(xf.isBit2_createTimePresent());
        // Local length=5, Central length=1
        assertEquals(5, xf.getLocalFileDataLength().getValue());
        assertEquals(1, xf.getCentralDirectoryLength().getValue());

        // get/set flags: 111
        xf.setFlags((byte) 7);
        assertEquals(7, xf.getFlags());
        assertTrue(xf.isBit0_modifyTimePresent());
        assertTrue(xf.isBit1_accessTimePresent());
        assertTrue(xf.isBit2_createTimePresent());
        // Local length=13, Central length=5
        assertEquals(13, xf.getLocalFileDataLength().getValue());
        assertEquals(5, xf.getCentralDirectoryLength().getValue());

        // get/set flags: 11111111
        xf.setFlags((byte) -1);
        assertEquals(-1, xf.getFlags());
        assertTrue(xf.isBit0_modifyTimePresent());
        assertTrue(xf.isBit1_accessTimePresent());
        assertTrue(xf.isBit2_createTimePresent());
        // Local length=13, Central length=5
        assertEquals(13, xf.getLocalFileDataLength().getValue());
        assertEquals(5, xf.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(X5455, xf.getHeaderId());
    }

    @Test
    public void testParseReparse() throws ZipException {
        /*
         * Recall the spec:
         *
         * 0x5455        Short       tag for this extra block type ("UT")
         * TSize         Short       total data size for this block
         * Flags         Byte        info bits
         * (ModTime)     Long        time of last modification (UTC/GMT)
         * (AcTime)      Long        time of last access (UTC/GMT)
         * (CrTime)      Long        time of original creation (UTC/GMT)
         */
        final byte[] NULL_FLAGS = {0};
        final byte[] AC_CENTRAL = {2}; // central data only contains the AC flag and no actual data
        final byte[] CR_CENTRAL = {4}; // central data only dontains the CR flag and no actual data

        final byte[] MOD_ZERO = {1, 0, 0, 0, 0};
        final byte[] MOD_MAX = {1, -1, -1, -1, -1};
        final byte[] AC_ZERO = {2, 0, 0, 0, 0};
        final byte[] AC_MAX = {2, -1, -1, -1, -1};
        final byte[] CR_ZERO = {4, 0, 0, 0, 0};
        final byte[] CR_MAX = {4, -1, -1, -1, -1};
        final byte[] MOD_AC_ZERO = {3, 0, 0, 0, 0, 0, 0, 0, 0};
        final byte[] MOD_AC_MAX = {3, -1, -1, -1, -1, -1, -1, -1, -1};
        final byte[] MOD_AC_CR_ZERO = {7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        final byte[] MOD_AC_CR_MAX = {7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

        parseReparse(null, NULL_FLAGS, NULL_FLAGS);
        parseReparse(ZERO_TIME, MOD_ZERO, MOD_ZERO);
        parseReparse(MAX_TIME_SECONDS, MOD_MAX, MOD_MAX);
        parseReparse(ZERO_TIME, AC_ZERO, AC_CENTRAL);
        parseReparse(MAX_TIME_SECONDS, AC_MAX, AC_CENTRAL);
        parseReparse(ZERO_TIME, CR_ZERO, CR_CENTRAL);
        parseReparse(MAX_TIME_SECONDS, CR_MAX, CR_CENTRAL);
        parseReparse(ZERO_TIME, MOD_AC_ZERO, MOD_ZERO);
        parseReparse(MAX_TIME_SECONDS, MOD_AC_MAX, MOD_MAX);
        parseReparse(ZERO_TIME, MOD_AC_CR_ZERO, MOD_ZERO);
        parseReparse(MAX_TIME_SECONDS, MOD_AC_CR_MAX, MOD_MAX);

        // As far as the spec is concerned (December 2012) all of these flags
        // are spurious versions of 7 (a.k.a. binary 00000111).
        parseReparse((byte) 15, MAX_TIME_SECONDS, (byte) 7, MOD_AC_CR_MAX, MOD_MAX);
        parseReparse((byte) 31, MAX_TIME_SECONDS, (byte) 7, MOD_AC_CR_MAX, MOD_MAX);
        parseReparse((byte) 63, MAX_TIME_SECONDS, (byte) 7, MOD_AC_CR_MAX, MOD_MAX);
        parseReparse((byte) 71, MAX_TIME_SECONDS, (byte) 7, MOD_AC_CR_MAX, MOD_MAX);
        parseReparse((byte) 127, MAX_TIME_SECONDS, (byte) 7, MOD_AC_CR_MAX, MOD_MAX);
        parseReparse((byte) -1, MAX_TIME_SECONDS, (byte) 7, MOD_AC_CR_MAX, MOD_MAX);
    }

    private void parseReparse(
            final ZipLong time,
            final byte[] expectedLocal,
            final byte[] almostExpectedCentral
    ) throws ZipException {
        parseReparse(null, time, null, expectedLocal, almostExpectedCentral);
    }

    private void parseReparse(
            final Byte providedFlagsByte,
            final ZipLong time,
            final Byte expectedFlagsByte,
            final byte[] expectedLocal,
            final byte[] almostExpectedCentral
    ) throws ZipException {
        final byte providedFlags = providedFlagsByte == null ? expectedLocal[0] : providedFlagsByte;
        final byte expectedFlags = expectedFlagsByte == null ? expectedLocal[0] : expectedFlagsByte;

        // We're responsible for expectedCentral's flags.  Too annoying to set in caller.
        final byte[] expectedCentral = new byte[almostExpectedCentral.length];
        System.arraycopy(almostExpectedCentral, 0, expectedCentral, 0, almostExpectedCentral.length);
        expectedCentral[0] = expectedFlags;

        xf.setFlags(providedFlags);
        xf.setModifyTime(time);
        xf.setAccessTime(time);
        xf.setCreateTime(time);
        byte[] result = xf.getLocalFileDataData();
        assertTrue(Arrays.equals(expectedLocal, result));

        // And now we re-parse:
        xf.parseFromLocalFileData(result, 0, result.length);
        assertEquals(expectedFlags, xf.getFlags());
        if (isFlagSet(expectedFlags, MODIFY_TIME_BIT)) {
            assertTrue(xf.isBit0_modifyTimePresent());
            assertEquals(time, xf.getModifyTime());
        }
        if (isFlagSet(expectedFlags, ACCESS_TIME_BIT)) {
            assertTrue(xf.isBit1_accessTimePresent());
            assertEquals(time, xf.getAccessTime());
        }
        if (isFlagSet(expectedFlags, CREATE_TIME_BIT)) {
            assertTrue(xf.isBit2_createTimePresent());
            assertEquals(time, xf.getCreateTime());
        }

        // Do the same as above, but with Central Directory data:
        xf.setFlags(providedFlags);
        xf.setModifyTime(time);
        xf.setAccessTime(time);
        xf.setCreateTime(time);
        result = xf.getCentralDirectoryData();
        assertTrue(Arrays.equals(expectedCentral, result));

        // And now we re-parse:
        xf.parseFromCentralDirectoryData(result, 0, result.length);
        assertEquals(expectedFlags, xf.getFlags());
        // Central Directory never contains ACCESS or CREATE, but
        // may contain MODIFY.
        if (isFlagSet(expectedFlags, MODIFY_TIME_BIT)) {
            assertTrue(xf.isBit0_modifyTimePresent());
            assertEquals(time, xf.getModifyTime());
        }
    }

    private static boolean isFlagSet(byte data, byte flag) { return (data & flag) == flag; }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2852.java