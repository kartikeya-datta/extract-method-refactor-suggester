error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1026.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1026.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1026.java
text:
```scala
S@@ystem.arraycopy(tmp, 0, bytes, 0, (maxRead >= pos) ? maxRead + 1 : pos);

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIESOR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.aries.jpa.container.parsing.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;

import org.apache.aries.jpa.container.parsing.ParsedPersistenceUnit;
import org.apache.aries.jpa.container.parsing.PersistenceDescriptor;
import org.apache.aries.jpa.container.parsing.PersistenceDescriptorParser;
import org.apache.aries.jpa.container.parsing.PersistenceDescriptorParserException;
import org.osgi.framework.Bundle;

/**
 * This class may be used to parse JPA persistence descriptors. The parser validates
 * using the relevant version of the persistence schema as defined by the xml file. 
 */
public class PersistenceDescriptorParserImpl implements PersistenceDescriptorParser {

  /**
   * This class is used internally to prevent the first pass parse from
   * closing the InputStream when it exits.
   */
  private static class RememberingInputStream extends InputStream {

    /** The size by which to grow our array */
    private static final int bufferGrowthSize = 0x4000;
    /** The bytes that have been read so far */
    private byte[] bytes = new byte[bufferGrowthSize];
    /** Index of the next empty entry in the array */
    private int pos = 0;
    /** The input stream that actually holds the data */
    private final InputStream stream;
    /** Index of the last valid byte in the byte array */
    private int maxRead = -1;
    /** The point to reset to */
    private int markPoint = -1;
    
    
    public RememberingInputStream(InputStream in) throws IOException{
      stream = in;
      // Pre fill with data that we know we're going to need - it's 
      // more efficient than the single byte reads are - hopefully
      // someone reading a lot of data will do reads in bulk
      
      maxRead = stream.read(bytes) - 1;
    }

    @Override
    public int read() throws IOException {
      
      if(pos <= maxRead)
      {
        //We can't return the byte directly, because it is signed
        //We can pretend this is an unsigned byte by using boolean
        //& to set the low end byte of an int.
        return bytes[pos++] & 0xFF;
      } else {
        int i = stream.read();
        if(i<0)
          return i;
      
        ensureCapacity(0);
        bytes[pos++] = (byte) i;
        return i;
      }
    }

    /**
     * Ensure our internal byte array can hold enough data
     * @param i one less than the number of bytes that need
     *          to be held.
     */
    private void ensureCapacity(int i) {
      if((pos + i) >= bytes.length) {
        byte[] tmp = bytes;
        int newLength = bytes.length + bufferGrowthSize;
        while(newLength < pos + i) {
          newLength += bufferGrowthSize;
        }
        bytes = new byte[newLength];
        System.arraycopy(tmp, 0, bytes, 0, pos);
      }
    }

    @Override
    public int read(byte[] b) throws IOException {
      return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      if(pos <= maxRead) {
        if(pos + len <= maxRead)
        {
          System.arraycopy(bytes, pos, b, off, len);
          pos += len;
          return len;
        } else {
          int lengthLeftOfBuffer = (maxRead - pos) + 1;
          System.arraycopy(bytes, pos, b, off, lengthLeftOfBuffer);
          int read = stream.read(b, off + lengthLeftOfBuffer, len - lengthLeftOfBuffer);
          if(read < 0) {
            pos += lengthLeftOfBuffer;
            return lengthLeftOfBuffer;
          }
          ensureCapacity(lengthLeftOfBuffer + read - 1);
          System.arraycopy(b, off + lengthLeftOfBuffer, bytes, maxRead + 1, read);
          pos +=  (lengthLeftOfBuffer + read);
          return lengthLeftOfBuffer + read;
        }
      } else {
        int i = stream.read(b, off, len);
        if(i<0)
          return i;
        ensureCapacity(i - 1);
        System.arraycopy(b, off, bytes, pos, i);
        pos += i;
        return i;
      }
    }

    @Override
    public long skip(long n) throws IOException {
      throw new IOException("Skip is unsupported");
    }

    @Override
    public int available() throws IOException {
      if(pos <= maxRead) 
        return (maxRead - pos) + 1;
      else 
        return stream.available(); 
    }

    @Override
    public synchronized void mark(int readlimit) {
      markPoint = pos;
    }

    @Override
    public synchronized void reset() throws IOException {
      if(maxRead < pos)
        maxRead = pos - 1;
      pos = markPoint;
    }

    @Override
    public boolean markSupported() {
      return true;
    }

    @Override
    public void close() throws IOException {
      //No op, don't close the parent.
    }
  }
  
  /* (non-Javadoc)
   * @see org.apache.aries.jpa.container.parsing.impl.PersistenceDescriptorParser#parse(org.osgi.framework.Bundle, org.apache.aries.jpa.container.parsing.PersistenceDescriptor)
   */
  public Collection<ParsedPersistenceUnit> parse(Bundle b, PersistenceDescriptor descriptor) throws PersistenceDescriptorParserException {
    Collection<ParsedPersistenceUnit> persistenceUnits = new ArrayList<ParsedPersistenceUnit>();
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    InputStream is = null;
    boolean schemaFound = false;
    try {
      //Buffer the InputStream so we can mark it, though we'll be in 
      //trouble if we have to read more than 8192 characters before finding
      //the schema!
      is = new RememberingInputStream(descriptor.getInputStream());
      is.mark(Integer.MAX_VALUE);
      SAXParser parser = parserFactory.newSAXParser();
      try{
        parser.parse(is, new SchemaLocatingHandler());
      } catch (EarlyParserReturn epr) {
        //This is not really an exception, but a way to work out which
        //version of the persistence schema to use in validation
        Schema s = epr.getSchema();
        
        if(s != null) {
          schemaFound = true;
          parserFactory.setSchema(s);
          parserFactory.setNamespaceAware(true);
          parser = parserFactory.newSAXParser();
         
          //Get back to the beginning of the stream
          is.reset();
          
          JPAHandler handler = new JPAHandler(b, epr.getVersion());
          parser.parse(is, handler);
          persistenceUnits.addAll(handler.getPersistenceUnits());
        } 
      }
    } catch (Exception e) {
      throw new PersistenceDescriptorParserException("There was an error parsing " + descriptor.getLocation() 
          + " in bundle " + b.getSymbolicName() + "_" + b.getVersion(), e);
    } finally {
      if(is != null) try {
        is.close();
      } catch (IOException e) {
        //No logging necessary, just consume
      }
    }
    if(!!!schemaFound) {
    throw new PersistenceDescriptorParserException("No Schema could be located for the" +
        "persistence descriptor " + descriptor.getLocation() 
        + " in bundle " + b.getSymbolicName() + "_" + b.getVersion());
    }
    return persistenceUnits;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1026.java