error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6968.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6968.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[21,1]

error in qdox parser
file content:
```java
offset: 1041
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6968.java
text:
```scala
class FlushableDataOutputImpl implements FlushableDataOutput, Closeable {

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
p@@ackage org.jboss.as.protocol.mgmt;

import java.io.Closeable;
import java.io.IOException;

import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.SimpleDataOutput;
import org.jboss.remoting3.MessageOutputStream;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class FlushableDataOutputImpl implements FlushableDataOutput, Closeable {

    private final SimpleDataOutput output;

    private FlushableDataOutputImpl(SimpleDataOutput output) {
        this.output = output;
    }

    static FlushableDataOutputImpl create(MessageOutputStream output) {
        return new FlushableDataOutputImpl(new SimpleDataOutput(Marshalling.createByteOutput(output)));
    }

    /**
     * @return
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return output.hashCode();
    }

    /**
     * @param v
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#write(int)
     */
    public void write(int v) throws IOException {
        output.write(v);
    }

    /**
     * @param bytes
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#write(byte[])
     */
    public void write(byte[] bytes) throws IOException {
        output.write(bytes);
    }

    /**
     * @param bytes
     * @param off
     * @param len
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#write(byte[], int, int)
     */
    public void write(byte[] bytes, int off, int len) throws IOException {
        output.write(bytes, off, len);
    }

    /**
     * @param v
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#writeBoolean(boolean)
     */
    public void writeBoolean(boolean v) throws IOException {
        output.writeBoolean(v);
    }

    /**
     * @param v
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#writeByte(int)
     */
    public void writeByte(int v) throws IOException {
        output.writeByte(v);
    }

    /**
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return output.equals(obj);
    }

    /**
     * @param v
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#writeShort(int)
     */
    public void writeShort(int v) throws IOException {
        output.writeShort(v);
    }

    /**
     * @param v
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#writeChar(int)
     */
    public void writeChar(int v) throws IOException {
        output.writeChar(v);
    }

    /**
     * @param v
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#writeInt(int)
     */
    public void writeInt(int v) throws IOException {
        output.writeInt(v);
    }

    /**
     * @param v
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#writeLong(long)
     */
    public void writeLong(long v) throws IOException {
        output.writeLong(v);
    }

    /**
     * @param v
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#writeFloat(float)
     */
    public void writeFloat(float v) throws IOException {
        output.writeFloat(v);
    }

    /**
     * @param v
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#writeDouble(double)
     */
    public void writeDouble(double v) throws IOException {
        output.writeDouble(v);
    }

    /**
     * @return
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return output.toString();
    }

    /**
     * @param s
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#writeBytes(java.lang.String)
     */
    public void writeBytes(String s) throws IOException {
        output.writeBytes(s);
    }

    /**
     * @param s
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#writeChars(java.lang.String)
     */
    public void writeChars(String s) throws IOException {
        output.writeChars(s);
    }

    /**
     * @param s
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#writeUTF(java.lang.String)
     */
    public void writeUTF(String s) throws IOException {
        output.writeUTF(s);
    }

    /**
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#flush()
     */
    public void flush() throws IOException {
        output.flush();
    }

    /**
     * @throws IOException
     * @see org.jboss.marshalling.SimpleDataOutput#close()
     */
    public void close() throws IOException {
        output.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6968.java