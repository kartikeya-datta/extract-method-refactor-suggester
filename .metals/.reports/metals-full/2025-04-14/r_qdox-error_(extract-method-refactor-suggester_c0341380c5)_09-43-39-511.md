error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1998.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1998.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1998.java
text:
```scala
i@@f (u1.version() == 1 && u2.version() == 1)

package org.apache.cassandra.db.marshal;

import static org.junit.Assert.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import org.apache.cassandra.utils.UUIDGen;
import org.apache.log4j.Logger;
import org.junit.Test;

public class UUIDTypeTest
{

    private static final Logger logger = Logger.getLogger(UUIDTypeTest.class);

    UUIDType uuidType = new UUIDType();

    @Test
    public void testCompare()
    {

        UUID t1 = newTimeBasedUUID();
        UUID t2 = newTimeBasedUUID();

        testCompare(t1, t2, -1);
        testCompare(t1, t1, 0);
        testCompare(t2, t2, 0);

        UUID nullId = new UUID(0, 0);

        testCompare(nullId, t1, -1);
        testCompare(t2, nullId, 1);
        testCompare(nullId, nullId, 0);

        for (int test = 1; test < 32; test++)
        {
            UUID r1 = UUID.randomUUID();
            UUID r2 = UUID.randomUUID();

            testCompare(r1, r2, compareUUID(r1, r2));
            testCompare(r1, r1, 0);
            testCompare(r2, r2, 0);

            testCompare(t1, r1, -1);
            testCompare(r2, t2, 1);
        }
    }

    public UUID newTimeBasedUUID()
    {
        try
        {
            return UUIDGen.makeType1UUIDFromHost(InetAddress.getLocalHost());
        } catch (UnknownHostException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static int compareUnsigned(long n1, long n2)
    {
        if (n1 == n2)
        {
            return 0;
        }
        if ((n1 < n2) ^ ((n1 < 0) != (n2 < 0)))
        {
            return -1;
        }
        return 1;
    }

    public static int compareUUID(UUID u1, UUID u2)
    {
        int c = compareUnsigned(u1.getMostSignificantBits(),
                u2.getMostSignificantBits());
        if (c != 0)
        {
            return c;
        }
        return compareUnsigned(u1.getLeastSignificantBits(),
                u2.getLeastSignificantBits());
    }

    public String describeCompare(UUID u1, UUID u2, int c)
    {
        String tb1 = (u1.version() == 1) ? "time-based " : "random ";
        String tb2 = (u2.version() == 1) ? "time-based " : "random ";
        String comp = (c < 0) ? " < " : ((c == 0) ? " = " : " > ");
        return tb1 + u1 + comp + tb2 + u2;
    }

    public int sign(int i)
    {
        if (i < 0)
        {
            return -1;
        }
        if (i > 0)
        {
            return 1;
        }
        return 0;
    }

    public static ByteBuffer bytebuffer(UUID uuid)
    {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] bytes = new byte[16];

        for (int i = 0; i < 8; i++)
        {
            bytes[i] = (byte) (msb >>> 8 * (7 - i));
        }
        for (int i = 8; i < 16; i++)
        {
            bytes[i] = (byte) (lsb >>> 8 * (7 - i));
        }

        return ByteBuffer.wrap(bytes);
    }

    public void logJdkUUIDCompareToVariance(UUID u1, UUID u2, int expC)
    {
        if (u1.version() != u2.version())
        {
            return;
        }
        if (u1.version() == 1)
        {
            return;
        }
        if (u1.compareTo(u2) != expC)
        {
            logger.info("*** Note: java.util.UUID.compareTo() would have compared this differently");
        }

    }

    public void testCompare(UUID u1, UUID u2, int expC)
    {
        int c = sign(uuidType.compare(bytebuffer(u1), bytebuffer(u2)));
        expC = sign(expC);
        assertEquals("Expected " + describeCompare(u1, u2, expC) + ", got "
                + describeCompare(u1, u2, c), expC, c);

        if (u1.version() == 1)
            assertEquals(c, sign(TimeUUIDType.instance.compare(bytebuffer(u1), bytebuffer(u2))));

        logJdkUUIDCompareToVariance(u1, u2, c);
    }

    @Test
    public void testTimeEquality()
    {
        UUID a = newTimeBasedUUID();
        UUID b = new UUID(a.getMostSignificantBits(),
                a.getLeastSignificantBits());

        assertEquals(0, uuidType.compare(bytebuffer(a), bytebuffer(b)));
    }

    @Test
    public void testTimeSmaller()
    {
        UUID a = newTimeBasedUUID();
        UUID b = newTimeBasedUUID();
        UUID c = newTimeBasedUUID();

        assert uuidType.compare(bytebuffer(a), bytebuffer(b)) < 0;
        assert uuidType.compare(bytebuffer(b), bytebuffer(c)) < 0;
        assert uuidType.compare(bytebuffer(a), bytebuffer(c)) < 0;
    }

    @Test
    public void testTimeBigger()
    {
        UUID a = newTimeBasedUUID();
        UUID b = newTimeBasedUUID();
        UUID c = newTimeBasedUUID();

        assert uuidType.compare(bytebuffer(c), bytebuffer(b)) > 0;
        assert uuidType.compare(bytebuffer(b), bytebuffer(a)) > 0;
        assert uuidType.compare(bytebuffer(c), bytebuffer(a)) > 0;
    }

    @Test
    public void testTimestampComparison()
    {
        Random rng = new Random();
        ByteBuffer[] uuids = new ByteBuffer[100];
        for (int i = 0; i < uuids.length; i++)
        {
            uuids[i] = ByteBuffer.allocate(16);
            rng.nextBytes(uuids[i].array());
            // set version to 1
            uuids[i].array()[6] &= 0x0F;
            uuids[i].array()[6] |= 0x10;
        }
        Arrays.sort(uuids, uuidType);
        for (int i = 1; i < uuids.length; i++)
        {
            long i0 = UUIDGen.getUUID(uuids[i - 1]).timestamp();
            long i1 = UUIDGen.getUUID(uuids[i]).timestamp();
            assert i0 <= i1;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1998.java