error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9517.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9517.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9517.java
text:
```scala
v@@.addElement(String.valueOf(o));

/*
 * Copyright 1999,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.log4j.spi;

import java.io.PrintWriter;
import java.io.Writer;

import java.lang.reflect.Method;

import java.util.Vector;

import org.apache.log4j.helpers.PlatformInfo;


/**
 * ThrowableInformation is log4j's internal representation of throwables. It
 * essentially consists of a string array, called 'rep', where the first
 * element, that is rep[0], represents the string representation of the
 * throwable (i.e. the value you get when you do throwable.toString()) and
 * subsequent elements correspond the stack trace with the top most entry of the
 * stack corresponding to the second entry of the 'rep' array that is rep[1].
 *
 * Note that ThrowableInformation does not store the throwable it represents.
 *
 * @author Ceki G&uuml;lc&uuml;
 *
 */
public class ThrowableInformation implements java.io.Serializable {
  static final long serialVersionUID = -4748765566864322735L;

  //private transient Throwable throwable;
  private String[] rep;

  public ThrowableInformation(Throwable throwable) {
    VectorWriter vw = new VectorWriter();
    extractStringRep(throwable, vw);
    rep = vw.toStringArray();
  }

  public ThrowableInformation(String[] rep) {
    this.rep = rep;
  }

  // public Throwable getThrowable() {
  // return throwable;
  //}

  public void extractStringRep(Throwable t, VectorWriter vw) {
    t.printStackTrace(vw);

    // Check if the Throwable t has a nested Throwable. If so, invoke
    // extractStringRep recursively.
    // Note that the Throwable.getCause was added in JDK 1.4. The printStackTrace
    // method was modified in JDK 1.4 to handle the nested throwable returned
    // by Throwable.getCause.
    try {
      Class tC = t.getClass();
      Method[] mA = tC.getMethods();
      Method nextThrowableMethod = null;
      for (int i = 0; i < mA.length; i++) {
        if (("getCause".equals(mA[i].getName()) && !PlatformInfo.isJDK14OrLater())
 "getRootCause".equals(mA[i].getName())
 "getNextException".equals(mA[i].getName())
 "getException".equals(mA[i].getName())) {
          // check param types
          Class[] params = mA[i].getParameterTypes();
          if ((params == null) || (params.length == 0)) {
            // just found the getter for the nested throwable
            nextThrowableMethod = mA[i];
            break; // no need to search further
          }
        }
      }

      if (nextThrowableMethod != null) {
        // get the nested throwable and log it
        Throwable nextT =
          (Throwable) nextThrowableMethod.invoke(t, new Object[0]);
        if (nextT != null) {
          vw.print("Root cause follows.");
          extractStringRep(nextT, vw);
        }
      }
    } catch (Exception e) {
      // do nothing
    }
  }

  /**
   * Retun a clone of the string representation of the exceptopn (throwable)
   * that this object represents.
   */
  public String[] getThrowableStrRep() {
    return (String[]) rep.clone();
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ThrowableInformation)) {
      return false;
    }

    ThrowableInformation r = (ThrowableInformation) o;

    if (rep == null) {
      if (r.rep != null) {
        return false;
      } else {
        return true;
      }
    }

    // at this point we know that both rep and r.rep are non-null.
    if (rep.length != r.rep.length) {
      return false;
    }

    int len = rep.length;
    for (int i = 0; i < len; i++) {
      if (!rep[i].equals(r.rep[i])) {
        return false;
      }
    }

    return true;
  }
}


/**
 * VectorWriter is a seemingly trivial implemtantion of PrintWriter. The
 * throwable instance that we are trying to represnt is asked to print itself to
 * a VectorWriter.
 *
 * By our design choice, r string representation of the throwable does not
 * contain any line separators. It follows that println() methods of
 * VectorWriter ignore the 'ln' part.
 */
class VectorWriter extends PrintWriter {
  private Vector v;

  VectorWriter() {
    super(new NullWriter());
    v = new Vector();
  }

  public void print(Object o) {
    v.addElement(o.toString());
  }

  public void print(char[] chars) {
    v.addElement(new String(chars));
  }

  public void print(String s) {
    v.addElement(s);
  }

  public void println(Object o) {
    v.addElement(o.toString());
  }

  // JDK 1.1.x apprenly uses this form of println while in
  // printStackTrace()
  public void println(char[] chars) {
    v.addElement(new String(chars));
  }

  public void println(String s) {
    v.addElement(s);
  }

  public void write(char[] chars) {
    v.addElement(new String(chars));
  }

  public void write(char[] chars, int off, int len) {
    v.addElement(new String(chars, off, len));
  }

  public void write(String s, int off, int len) {
    v.addElement(s.substring(off, off + len));
  }

  public void write(String s) {
    v.addElement(s);
  }

  public String[] toStringArray() {
    int len = v.size();
    String[] sa = new String[len];

    for (int i = 0; i < len; i++) {
      sa[i] = (String) v.elementAt(i);
    }

    return sa;
  }
}


class NullWriter extends Writer {
  public void close() {
    // blank
  }

  public void flush() {
    // blank
  }

  public void write(char[] cbuf, int off, int len) {
    // blank
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9517.java