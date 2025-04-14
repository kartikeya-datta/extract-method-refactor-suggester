error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14457.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14457.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14457.java
text:
```scala
private static final R@@andom c_random = new Random();

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
package org.apache.commons.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.commons.io.input.DemuxInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.DemuxOutputStream;

/**
 * Basic unit tests for the multiplexing streams.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public class DemuxTestCase
    extends TestCase
{
    private static final String T1 = "Thread1";
    private static final String T2 = "Thread2";
    private static final String T3 = "Thread3";
    private static final String T4 = "Thread4";

    private static final String DATA1 = "Data for thread1";
    private static final String DATA2 = "Data for thread2";
    private static final String DATA3 = "Data for thread3";
    private static final String DATA4 = "Data for thread4";

    private static Random c_random = new Random();
    private HashMap<String, ByteArrayOutputStream> m_outputMap = new HashMap<String, ByteArrayOutputStream>();
    private HashMap<String, Thread> m_threadMap = new HashMap<String, Thread>();

    public DemuxTestCase( String name )
    {
        super( name );
    }

    private String getOutput( String threadName )
    {
        ByteArrayOutputStream output =
            m_outputMap.get( threadName );
        assertNotNull( "getOutput()", output );

        return output.toString();
    }

    private String getInput( String threadName )
    {
        ReaderThread thread = (ReaderThread)m_threadMap.get( threadName );
        assertNotNull( "getInput()", thread );

        return thread.getData();
    }

    private void doStart()
        throws Exception
    {
        Iterator<String> iterator = m_threadMap.keySet().iterator();
        while( iterator.hasNext() )
        {
            String name = iterator.next();
            Thread thread = m_threadMap.get( name );
            thread.start();
        }
    }

    private void doJoin()
        throws Exception
    {
        Iterator<String> iterator = m_threadMap.keySet().iterator();
        while( iterator.hasNext() )
        {
            String name = iterator.next();
            Thread thread = m_threadMap.get( name );
            thread.join();
        }
    }

    private void startWriter( String name,
                              String data,
                              DemuxOutputStream demux )
        throws Exception
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        m_outputMap.put( name, output );
        WriterThread thread =
            new WriterThread( name, data, output, demux );
        m_threadMap.put( name, thread );
    }

    private void startReader( String name,
                              String data,
                              DemuxInputStream demux )
        throws Exception
    {
        ByteArrayInputStream input = new ByteArrayInputStream( data.getBytes() );
        ReaderThread thread = new ReaderThread( name, input, demux );
        m_threadMap.put( name, thread );
    }

    public void testOutputStream()
        throws Exception
    {
        DemuxOutputStream output = new DemuxOutputStream();
        startWriter( T1, DATA1, output );
        startWriter( T2, DATA2, output );
        startWriter( T3, DATA3, output );
        startWriter( T4, DATA4, output );

        doStart();
        doJoin();

        assertEquals( "Data1", DATA1, getOutput( T1 ) );
        assertEquals( "Data2", DATA2, getOutput( T2 ) );
        assertEquals( "Data3", DATA3, getOutput( T3 ) );
        assertEquals( "Data4", DATA4, getOutput( T4 ) );
    }

    public void testInputStream()
        throws Exception
    {
        DemuxInputStream input = new DemuxInputStream();
        startReader( T1, DATA1, input );
        startReader( T2, DATA2, input );
        startReader( T3, DATA3, input );
        startReader( T4, DATA4, input );

        doStart();
        doJoin();

        assertEquals( "Data1", DATA1, getInput( T1 ) );
        assertEquals( "Data2", DATA2, getInput( T2 ) );
        assertEquals( "Data3", DATA3, getInput( T3 ) );
        assertEquals( "Data4", DATA4, getInput( T4 ) );
    }

    private static class ReaderThread
        extends Thread
    {
        private StringBuffer m_buffer = new StringBuffer();
        private InputStream m_input;
        private DemuxInputStream m_demux;

        ReaderThread( String name,
                      InputStream input,
                      DemuxInputStream demux )
        {
            super( name );
            m_input = input;
            m_demux = demux;
        }

        public String getData()
        {
            return m_buffer.toString();
        }

        @Override
        public void run()
        {
            m_demux.bindStream( m_input );

            try
            {
                int ch = m_demux.read();
                while( -1 != ch )
                {
                    //System.out.println( "Reading: " + (char)ch );
                    m_buffer.append( (char)ch );

                    int sleepTime = Math.abs( c_random.nextInt() % 10 );
                    Thread.sleep( sleepTime );
                    ch = m_demux.read();
                }
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
        }
    }

    private static class WriterThread
        extends Thread
    {
        private byte[] m_data;
        private OutputStream m_output;
        private DemuxOutputStream m_demux;

        WriterThread( String name,
                      String data,
                      OutputStream output,
                      DemuxOutputStream demux )
        {
            super( name );
            m_output = output;
            m_demux = demux;
            m_data = data.getBytes();
        }

        @Override
        public void run()
        {
            m_demux.bindStream( m_output );
            for( int i = 0; i < m_data.length; i++ )
            {
                try
                {
                    //System.out.println( "Writing: " + (char)m_data[ i ] );
                    m_demux.write( m_data[ i ] );
                    int sleepTime = Math.abs( c_random.nextInt() % 10 );
                    Thread.sleep( sleepTime );
                }
                catch( Exception e )
                {
                    e.printStackTrace();
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14457.java