error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/257.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/257.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/257.java
text:
```scala
public static <@@E extends Exception> void closeWhileHandlingException(E priorException, Iterable<? extends Closeable> objects) throws E, IOException {

package org.apache.lucene.util;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

/** This class emulates the new Java 7 "Try-With-Resources" statement.
 * Remove once Lucene is on Java 7.
 * @lucene.internal */
public final class IOUtils {
  
  /**
   * UTF-8 charset string
   * @see Charset#forName(String)
   */
  public static final String UTF_8 = "UTF-8";
  
  /**
   * UTF-8 {@link Charset} instance to prevent repeated
   * {@link Charset#forName(String)} lookups
   */
  public static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");
  private IOUtils() {} // no instance

  /**
   * <p>Closes all given <tt>Closeable</tt>s, suppressing all thrown exceptions. Some of the <tt>Closeable</tt>s
   * may be null, they are ignored. After everything is closed, method either throws <tt>priorException</tt>,
   * if one is supplied, or the first of suppressed exceptions, or completes normally.</p>
   * <p>Sample usage:<br/>
   * <pre>
   * Closeable resource1 = null, resource2 = null, resource3 = null;
   * ExpectedException priorE = null;
   * try {
   *   resource1 = ...; resource2 = ...; resource3 = ...; // Acquisition may throw ExpectedException
   *   ..do..stuff.. // May throw ExpectedException
   * } catch (ExpectedException e) {
   *   priorE = e;
   * } finally {
   *   closeSafely(priorE, resource1, resource2, resource3);
   * }
   * </pre>
   * </p>
   * @param priorException  <tt>null</tt> or an exception that will be rethrown after method completion
   * @param objects         objects to call <tt>close()</tt> on
   */
  public static <E extends Exception> void closeWhileHandlingException(E priorException, Closeable... objects) throws E, IOException {
    Throwable th = null;

    for (Closeable object : objects) {
      try {
        if (object != null) {
          object.close();
        }
      } catch (Throwable t) {
        addSuppressed((priorException == null) ? th : priorException, t);
        if (th == null) {
          th = t;
        }
      }
    }

    if (priorException != null) {
      throw priorException;
    } else if (th != null) {
      if (th instanceof IOException) throw (IOException) th;
      if (th instanceof RuntimeException) throw (RuntimeException) th;
      if (th instanceof Error) throw (Error) th;
      throw new RuntimeException(th);
    }
  }

  /** @see #closeWhileHandlingException(Exception, Closeable...) */
  public static <E extends Exception> void closeWhileHandlingException(E priorException, Iterable<Closeable> objects) throws E, IOException {
    Throwable th = null;

    for (Closeable object : objects) {
      try {
        if (object != null) {
          object.close();
        }
      } catch (Throwable t) {
        addSuppressed((priorException == null) ? th : priorException, t);
        if (th == null) {
          th = t;
        }
      }
    }

    if (priorException != null) {
      throw priorException;
    } else if (th != null) {
      if (th instanceof IOException) throw (IOException) th;
      if (th instanceof RuntimeException) throw (RuntimeException) th;
      if (th instanceof Error) throw (Error) th;
      throw new RuntimeException(th);
    }
  }

  /**
   * Closes all given <tt>Closeable</tt>s.  Some of the
   * <tt>Closeable</tt>s may be null; they are
   * ignored.  After everything is closed, the method either
   * throws the first exception it hit while closing, or
   * completes normally if there were no exceptions.
   * 
   * @param objects
   *          objects to call <tt>close()</tt> on
   */
  public static void close(Closeable... objects) throws IOException {
    Throwable th = null;

    for (Closeable object : objects) {
      try {
        if (object != null) {
          object.close();
        }
      } catch (Throwable t) {
        addSuppressed(th, t);
        if (th == null) {
          th = t;
        }
      }
    }

    if (th != null) {
      if (th instanceof IOException) throw (IOException) th;
      if (th instanceof RuntimeException) throw (RuntimeException) th;
      if (th instanceof Error) throw (Error) th;
      throw new RuntimeException(th);
    }
  }
  
  /**
   * @see #close(Closeable...)
   */
  public static void close(Iterable<? extends Closeable> objects) throws IOException {
    Throwable th = null;

    for (Closeable object : objects) {
      try {
        if (object != null) {
          object.close();
        }
      } catch (Throwable t) {
        addSuppressed(th, t);
        if (th == null) {
          th = t;
        }
      }
    }

    if (th != null) {
      if (th instanceof IOException) throw (IOException) th;
      if (th instanceof RuntimeException) throw (RuntimeException) th;
      if (th instanceof Error) throw (Error) th;
      throw new RuntimeException(th);
    }
  }

  /**
   * Closes all given <tt>Closeable</tt>s, suppressing all thrown exceptions.
   * Some of the <tt>Closeable</tt>s may be null, they are ignored.
   * 
   * @param objects
   *          objects to call <tt>close()</tt> on
   */
  public static void closeWhileHandlingException(Closeable... objects) {
    for (Closeable object : objects) {
      try {
        if (object != null) {
          object.close();
        }
      } catch (Throwable t) {
      }
    }
  }
  
  /**
   * @see #closeWhileHandlingException(Closeable...)
   */
  public static void closeWhileHandlingException(Iterable<? extends Closeable> objects) {
    for (Closeable object : objects) {
      try {
        if (object != null) {
          object.close();
        }
      } catch (Throwable t) {
      }
    }
  }
  
  /** This reflected {@link Method} is {@code null} before Java 7 */
  private static final Method SUPPRESS_METHOD;
  static {
    Method m;
    try {
      m = Throwable.class.getMethod("addSuppressed", Throwable.class);
    } catch (Exception e) {
      m = null;
    }
    SUPPRESS_METHOD = m;
  }

  /** adds a Throwable to the list of suppressed Exceptions of the first Throwable (if Java 7 is detected)
   * @param exception this exception should get the suppressed one added
   * @param suppressed the suppressed exception
   */
  private static final void addSuppressed(Throwable exception, Throwable suppressed) {
    if (SUPPRESS_METHOD != null && exception != null && suppressed != null) {
      try {
        SUPPRESS_METHOD.invoke(exception, suppressed);
      } catch (Exception e) {
        // ignore any exceptions caused by invoking (e.g. security constraints)
      }
    }
  }
  
  /**
   * Wrapping the given {@link InputStream} in a reader using a {@link CharsetDecoder}.
   * Unlike Java's defaults this reader will throw an exception if your it detects 
   * the read charset doesn't match the expected {@link Charset}. 
   * <p>
   * Decoding readers are useful to load configuration files, stopword lists or synonym files
   * to detect character set problems. However, its not recommended to use as a common purpose 
   * reader.
   * 
   * @param stream the stream to wrap in a reader
   * @param charSet the expected charset
   * @return a wrapping reader
   */
  public static Reader getDecodingReader(InputStream stream, Charset charSet) {
    final CharsetDecoder charSetDecoder = charSet.newDecoder()
        .onMalformedInput(CodingErrorAction.REPORT)
        .onUnmappableCharacter(CodingErrorAction.REPORT);
    return new BufferedReader(new InputStreamReader(stream, charSetDecoder));
  }
  
  /**
   * Opens a Reader for the given {@link File} using a {@link CharsetDecoder}.
   * Unlike Java's defaults this reader will throw an exception if your it detects 
   * the read charset doesn't match the expected {@link Charset}. 
   * <p>
   * Decoding readers are useful to load configuration files, stopword lists or synonym files
   * to detect character set problems. However, its not recommended to use as a common purpose 
   * reader.
   * @param file the file to open a reader on
   * @param charSet the expected charset
   * @return a reader to read the given file
   */
  public static Reader getDecodingReader(File file, Charset charSet) throws IOException {
    FileInputStream stream = null;
    boolean success = false;
    try {
      stream = new FileInputStream(file);
      final Reader reader = getDecodingReader(stream, charSet);
      success = true;
      return reader;

    } finally {
      if (!success) {
        IOUtils.close(stream);
      }
    }
  }

  /**
   * Opens a Reader for the given resource using a {@link CharsetDecoder}.
   * Unlike Java's defaults this reader will throw an exception if your it detects 
   * the read charset doesn't match the expected {@link Charset}. 
   * <p>
   * Decoding readers are useful to load configuration files, stopword lists or synonym files
   * to detect character set problems. However, its not recommended to use as a common purpose 
   * reader.
   * @param clazz the class used to locate the resource
   * @param resource the resource name to load
   * @param charSet the expected charset
   * @return a reader to read the given file
   * 
   */
  public static Reader getDecodingReader(Class<?> clazz, String resource, Charset charSet) throws IOException {
    InputStream stream = null;
    boolean success = false;
    try {
      stream = clazz
      .getResourceAsStream(resource);
      final Reader reader = getDecodingReader(stream, charSet);
      success = true;
      return reader;
    } finally {
      if (!success) {
        IOUtils.close(stream);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/257.java