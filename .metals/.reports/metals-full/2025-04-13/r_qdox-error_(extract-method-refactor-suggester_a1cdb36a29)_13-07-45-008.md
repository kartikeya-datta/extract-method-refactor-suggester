error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9450.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9450.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9450.java
text:
```scala
m@@_dummy.setHeaderID( new ZipShort( 1 ) );

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

package org.apache.commons.compress.zip;

import junit.framework.TestCase;

/**
 * JUnit testcases ExtraFieldUtils.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class ExtraFieldUtilsTestCase
    extends TestCase
    implements UnixStat
{
    private AsiExtraField m_field;
    private UnrecognizedExtraField m_dummy;
    private byte[] m_data;
    private byte[] m_local;

    public ExtraFieldUtilsTestCase( final String name )
    {
        super( name );
    }

    public void setUp()
    {
        m_field = new AsiExtraField();
        m_field.setMode( 0755 );
        m_field.setDirectory( true );
        m_dummy = new UnrecognizedExtraField();
        m_dummy.setHeaderId( new ZipShort( 1 ) );
        m_dummy.setLocalFileDataData( new byte[ 0 ] );
        m_dummy.setCentralDirectoryData( new byte[]{0} );

        m_local = m_field.getLocalFileDataData();
        final byte[] dummyLocal = m_dummy.getLocalFileDataData();
        m_data = new byte[ 4 + m_local.length + 4 + dummyLocal.length ];
        System.arraycopy( m_field.getHeaderID().getBytes(), 0, m_data, 0, 2 );
        System.arraycopy( m_field.getLocalFileDataLength().getBytes(), 0, m_data, 2, 2 );
        System.arraycopy( m_local, 0, m_data, 4, m_local.length );
        System.arraycopy( m_dummy.getHeaderID().getBytes(), 0, m_data,
                          4 + m_local.length, 2 );
        System.arraycopy( m_dummy.getLocalFileDataLength().getBytes(), 0, m_data,
                          4 + m_local.length + 2, 2 );
        System.arraycopy( dummyLocal, 0, m_data,
                          4 + m_local.length + 4, dummyLocal.length );

    }

    /**
     * test parser.
     */
    public void testParse() throws Exception
    {
        final ZipExtraField[] extraField = ExtraFieldUtils.parse( m_data );
        assertEquals( "number of fields", 2, extraField.length );
        assertTrue( "type field 1", extraField[ 0 ] instanceof AsiExtraField );
        assertEquals( "mode field 1", 040755,
                      ( (AsiExtraField)extraField[ 0 ] ).getMode() );
        assertTrue( "type field 2", extraField[ 1 ] instanceof UnrecognizedExtraField );
        assertEquals( "data length field 2", 0,
                      extraField[ 1 ].getLocalFileDataLength().getValue() );

        final byte[] data2 = new byte[ m_data.length - 1 ];
        System.arraycopy( m_data, 0, data2, 0, data2.length );
        try
        {
            ExtraFieldUtils.parse( data2 );
            fail( "data should be invalid" );
        }
        catch( Exception e )
        {
            assertEquals( "message",
                          "data starting at " + ( 4 + m_local.length ) + " is in unknown format",
                          e.getMessage() );
        }
    }

    /**
     * Test merge methods
     */
    public void testMerge()
    {
        final byte[] local =
            ExtraFieldUtils.mergeLocalFileDataData( new ZipExtraField[]{m_field, m_dummy} );
        assertEquals( "local length", m_data.length, local.length );
        for( int i = 0; i < local.length; i++ )
        {
            assertEquals( "local byte " + i, m_data[ i ], local[ i ] );
        }

        final byte[] dummyCentral = m_dummy.getCentralDirectoryData();
        final byte[] data2 = new byte[ 4 + m_local.length + 4 + dummyCentral.length ];
        System.arraycopy( m_data, 0, data2, 0, 4 + m_local.length + 2 );
        System.arraycopy( m_dummy.getCentralDirectoryLength().getBytes(), 0,
                          data2, 4 + m_local.length + 2, 2 );
        System.arraycopy( dummyCentral, 0, data2,
                          4 + m_local.length + 4, dummyCentral.length );

        final byte[] central =
            ExtraFieldUtils.mergeCentralDirectoryData( new ZipExtraField[]{m_field, m_dummy} );
        assertEquals( "central length", data2.length, central.length );
        for( int i = 0; i < central.length; i++ )
        {
            assertEquals( "central byte " + i, data2[ i ], central[ i ] );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9450.java