error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2636.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2636.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2636.java
text:
```scala
L@@ogger logger = Logger.getLogger(FileNamePattern.class);

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

package org.apache.log4j.rolling.helper;

import org.apache.log4j.Logger;

import java.util.Date;


/**
 *
 * This class helps parse file name patterns. Given a number or a date it returns
 * a file name according to the file name pattern.
 *
 * @author Ceki G&uuml;lc&uuml;
 *
 */
public class FileNamePattern {
  static Logger logger = Logger.getLogger(FileNamePattern.class);
  String pattern;
  int patternLength;
  TokenConverter headTokenConverter;

  public FileNamePattern(String pattern) {
    if (pattern == null) {
      throw new IllegalArgumentException(
        "The argument to constrcutor cannot be null. ");
    }

    this.pattern = pattern;

    if (pattern != null) {
      patternLength = pattern.length();
      // We do not want to deal with trailing spaces in the pattern.
      this.pattern = this.pattern.trim();
    }

    parse();
  }

  public String toString() {
    return pattern;
  }

  void parse() {
    int lastIndex = 0;

    TokenConverter tc = null;

MAIN_LOOP: 
    while (true) {
      int i = pattern.indexOf('%', lastIndex);

      if (i == -1) {
        String remainingStr = pattern.substring(lastIndex);

        //System.out.println("adding the identity token, I");
        addTokenConverter(tc, new IdentityTokenConverter(remainingStr));

        break;
      } else {
        // test for degenerate case where the '%' character is at the end.
        if (i == (patternLength - 1)) {
          String remainingStr = pattern.substring(lastIndex);
          addTokenConverter(tc, new IdentityTokenConverter(remainingStr));

          break;
        }

        //System.out.println("adding the identity token, II");
        tc =
          addTokenConverter(
            tc, new IdentityTokenConverter(pattern.substring(lastIndex, i)));

        // At this stage, we can suppose that i < patternLen -1
        char nextChar = pattern.charAt(i + 1);

        switch (nextChar) {
        case 'i':
          tc = addTokenConverter(tc, new IntegerTokenConverter());
          lastIndex = i + 2;

          break; // break from switch statement 

        case 'd':

          int optionEnd = getOptionEnd(i + 2);

          String option;

          if (optionEnd != -1) {
            option = pattern.substring(i + 3, optionEnd);
            lastIndex = optionEnd + 1;
          } else {
            logger.debug("Assuming daily rotation schedule");
            option = "yyyy-MM-dd";
            lastIndex = i+2;
          }
          tc = addTokenConverter(tc, new DateTokenConverter(option));
          break; // break from switch statement 

        case '%':
          tc = addTokenConverter(tc, new IdentityTokenConverter("%"));
          lastIndex = i + 2;

          break;

        default:
          throw new IllegalArgumentException(
            "The pattern[" + pattern
            + "] does not contain a valid specifer at position " + (i + 1));
        }
      }
    }
  }

  /**
   *  Find the position of the last character of option enclosed within the '{}'
   * characters inside the pattern
   * */
  protected int getOptionEnd(int i) {
    //logger.debug("Char at "+i+" "+pattern.charAt(i));
    if ((i < patternLength) && (pattern.charAt(i) == '{')) {
      int end = pattern.indexOf('}', i);

      if (end > i) {
        return end;
      } else {
        return -1;
      }
    }

    return -1;
  }

  TokenConverter addTokenConverter(
    TokenConverter tc, TokenConverter newTokenConverter) {
    if (tc == null) {
      tc = headTokenConverter = newTokenConverter;
    } else {
      tc.next = newTokenConverter;
      tc = newTokenConverter;
    }

    return tc;
  }

  public DateTokenConverter getDateTokenConverter() {
    TokenConverter p = headTokenConverter;

    while (p != null) {
      if (p.getType() == TokenConverter.DATE) {
        return (DateTokenConverter) p;
      }

      p = p.getNext();
    }

    return null;
  }

  public IntegerTokenConverter getIntegerTokenConverter() {
    TokenConverter p = headTokenConverter;

    while (p != null) {
      if (p.getType() == TokenConverter.INTEGER) {
        return (IntegerTokenConverter) p;
      }

      p = p.getNext();
    }
    return null;
  }
  
  public String convert(int i) {
    TokenConverter p = headTokenConverter;
    StringBuffer buf = new StringBuffer();

    while (p != null) {
      switch (p.getType()) {
      case TokenConverter.IDENTITY:
        buf.append(((IdentityTokenConverter) p).convert());

        break;

      case TokenConverter.INTEGER:
        buf.append(((IntegerTokenConverter) p).convert(i));

        break;

      default:
        logger.error(
          "Encountered an unknown TokenConverter type for pattern [" + pattern
          + "].");
      }

      p = p.getNext();
    }

    return buf.toString();
  }

  public String convert(Date date) {
    TokenConverter p = headTokenConverter;
    StringBuffer buf = new StringBuffer();

    while (p != null) {
      switch (p.getType()) {
      case TokenConverter.IDENTITY:
        buf.append(((IdentityTokenConverter) p).convert());

        break;

      case TokenConverter.DATE:
        buf.append(((DateTokenConverter) p).convert(date));

        break;

      default:
        logger.error(
          "Encountered an unknown TokenConverter type for pattern [" + pattern
          + "].");
      }

      p = p.getNext();
    }

    return buf.toString();
  }


  public String getPattern() {
    return pattern;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2636.java