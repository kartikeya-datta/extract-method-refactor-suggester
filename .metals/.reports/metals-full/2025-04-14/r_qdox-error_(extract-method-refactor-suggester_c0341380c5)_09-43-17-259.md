error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3117.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3117.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 862
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3117.java
text:
```scala
class ExtendedBufferedReader extends BufferedReader  {

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
p@@ackage org.apache.commons.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * ExtendedBufferedReader
 *
 * A special reader decorater which supports more
 * sophisticated access to the underlying reader object.
 * 
 * In particular the reader supports a look-ahead option,
 * which allows you to see the next char returned by
 * next().
 * Furthermore the skip-method supports skipping until
 * (but excluding) a given char. Similar functionality
 * is supported by the reader as well.
 * 
 */
public class ExtendedBufferedReader extends BufferedReader  {

  
  /** the end of stream symbol */
  public static final int END_OF_STREAM = -1;
  /** undefined state for the lookahead char */
  public static final int UNDEFINED = -2;
  
  /** the lookahead chars */
  private int lookaheadChar = UNDEFINED;
  /** the last char returned */
  private int lastChar = UNDEFINED;
  /** the line counter */
  private int lineCounter = 0;
  private CharBuffer line = new CharBuffer();
  
  /**
   * Created extended buffered reader using default buffer-size
   *
   */
  public ExtendedBufferedReader(Reader r) {
    super(r);
    /* note uh: do not fetch the first char here,
     *          because this might block the method!
     */
  }
    
  /**
   * Create extended buffered reader using the given buffer-size
   */
  public ExtendedBufferedReader(Reader r, int bufSize) {
    super(r, bufSize);
    /* note uh: do not fetch the first char here,
     *          because this might block the method!
     */
  }
  
  /**
   * Reads the next char from the input stream.
   * @return the next char or END_OF_STREAM if end of stream has been reached.
   */
  public int read() throws IOException {
    // initalize the lookahead
    if (lookaheadChar == UNDEFINED) {
      lookaheadChar = super.read();
    }
    lastChar = lookaheadChar;
    if (super.ready()) {
      lookaheadChar = super.read();
    } else {
      lookaheadChar = UNDEFINED;
    }
    if (lastChar == '\n') {
      lineCounter++;
    } 
    return lastChar;
  }
  
  /**
   * Returns the last read character again.
   * 
   * @return the last read char or UNDEFINED
   */
  public int readAgain() {
    return lastChar;  
  }
  
  /**
   * Non-blocking reading of len chars into buffer buf starting
   * at bufferposition off.
   * 
   * performs an iteratative read on the underlying stream
   * as long as the following conditions hold:
   *   - less than len chars have been read
   *   - end of stream has not been reached
   *   - next read is not blocking
   * 
   * @return nof chars actually read or END_OF_STREAM
   */
  public int read(char[] buf, int off, int len) throws IOException {
    // do not claim if len == 0
    if (len == 0) {
      return 0;
    } 
    
    // init lookahead, but do not block !!
    if (lookaheadChar == UNDEFINED) {
        if (ready()) {
         lookaheadChar = super.read();
        } else {
          return -1;
        }
    }
    // 'first read of underlying stream'
    if (lookaheadChar == -1) {
      return -1;
    }
    // continue until the lookaheadChar would block
    int cOff = off;
    while (len > 0 && ready()) {
      if (lookaheadChar == -1) {
        // eof stream reached, do not continue
        return cOff - off;
      } else {
        buf[cOff++] = (char) lookaheadChar;
        if (lookaheadChar == '\n') {
          lineCounter++;
        } 
        lastChar = lookaheadChar;
        lookaheadChar = super.read();
        len--;
      }
    }
    return cOff - off;
  }
 
 /**
  * Reads all characters up to (but not including) the given character.
  * 
  * @param c the character to read up to
  * @return the string up to the character <code>c</code>
  * @throws IOException
  */
 public String readUntil(char c) throws IOException {
   if (lookaheadChar == UNDEFINED) {
     lookaheadChar = super.read();
   }
   line.clear(); // reuse
   while (lookaheadChar != c && lookaheadChar != END_OF_STREAM) {
     line.append((char) lookaheadChar);
     if (lookaheadChar == '\n') {
       lineCounter++;
     } 
     lastChar = lookaheadChar;
     lookaheadChar = super.read();
   }
   return line.toString();    
 }
 
 /**
  * @return A String containing the contents of the line, not 
  *         including any line-termination characters, or null 
  *         if the end of the stream has been reached
  */
  public String readLine() throws IOException {
    
    if (lookaheadChar == UNDEFINED) {
      lookaheadChar = super.read(); 
    }
    
    line.clear(); //reuse
    
    // return null if end of stream has been reached
    if (lookaheadChar == END_OF_STREAM) {
      return null;
    }
    // do we have a line termination already
    char laChar = (char) lookaheadChar;
    if (laChar == '\n' || laChar == '\r') {
      lastChar = lookaheadChar;
      lookaheadChar = super.read();
      // ignore '\r\n' as well
      if ((char) lookaheadChar == '\n') {
        lastChar = lookaheadChar;
        lookaheadChar = super.read();
      }
      lineCounter++;
      return line.toString();
    }
    
    // create the rest-of-line return and update the lookahead
    line.append(laChar);
    String restOfLine = super.readLine(); // TODO involves copying
    lastChar = lookaheadChar;
    lookaheadChar = super.read();
    if (restOfLine != null) {
      line.append(restOfLine);
    }
    lineCounter++;
    return line.toString();
  }
  
  /**
   * Skips char in the stream
   * 
   * ATTENTION: invalidates the line-counter !!!!!
   * 
   * @return nof skiped chars
   */
  public long skip(long n) throws IllegalArgumentException, IOException  {
    
    if (lookaheadChar == UNDEFINED) {
      lookaheadChar = super.read();   
    }
    
    // illegal argument
    if (n < 0) {
      throw new IllegalArgumentException("negative argument not supported");  
    }
    
    // no skipping
    if (n == 0 || lookaheadChar == END_OF_STREAM) {
      return 0;
    } 
    
    // skip and reread the lookahead-char
    long skiped = 0;
    if (n > 1) {
      skiped = super.skip(n - 1);
    }
    lookaheadChar = super.read();
    // fixme uh: we should check the skiped sequence for line-terminations...
    lineCounter = Integer.MIN_VALUE;
    return skiped + 1;
  }
  
  /**
   * Skips all chars in the input until (but excluding) the given char
   * 
   * @param c
   * @return
   * @throws IllegalArgumentException
   * @throws IOException
   */
  public long skipUntil(char c) throws IllegalArgumentException, IOException {
    if (lookaheadChar == UNDEFINED) {
      lookaheadChar = super.read();   
    }
    long counter = 0;
    while (lookaheadChar != c && lookaheadChar != END_OF_STREAM) {
      if (lookaheadChar == '\n') {
        lineCounter++;
      } 
      lookaheadChar = super.read();
      counter++;
    }
    return counter;
  }
  
  /**
   * Returns the next char in the stream without consuming it.
   * 
   * Remember the next char read by read(..) will always be
   * identical to lookAhead().
   * 
   * @return the next char (without consuming it) or END_OF_STREAM
   */
  public int lookAhead() throws IOException {
    if (lookaheadChar == UNDEFINED) {
      lookaheadChar = super.read();
    }
    return lookaheadChar;
  }
  
  
  /**
   * Returns the nof line read
   * ATTENTION: the skip-method does invalidate the line-number counter
   * 
   * @return the current-line-number (or -1)
   */ 
  public int getLineNumber() {
    if (lineCounter > -1) {
      return lineCounter;
    } else {
      return -1;
    }
  }
  public boolean markSupported() {
    /* note uh: marking is not supported, cause we cannot
     *          see into the future...
     */
    return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3117.java