error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1424.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1424.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1424.java
text:
```scala
l@@ogger.debug(

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
package org.apache.log4j.joran.action;

import org.apache.joran.ErrorItem;
import org.apache.joran.ExecutionContext;
import org.apache.joran.action.Action;
import org.apache.joran.helper.Option;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.OptionHandler;

import org.xml.sax.Attributes;

import java.util.HashMap;


public class AppenderAction extends Action {
  static final Logger logger = Logger.getLogger(AppenderAction.class);
  Appender appender;

  private boolean inError = false;
  
  /**
   * Instantiates an appender of the given class and sets its name.
   *
   * The appender thus generated is placed in the ExecutionContext appender bag.
   */
  public void begin(
    ExecutionContext ec, String localName, Attributes attributes) {
    String className = attributes.getValue(CLASS_ATTRIBUTE);

    try {
      logger.debug(
        "About to instantiate appender of type [" + className + "]");

      Object instance =
        OptionConverter.instantiateByClassName(
          className, org.apache.log4j.Appender.class, null);
      appender = (Appender) instance;

      String appenderName = attributes.getValue(NAME_ATTRIBUTE);

      if (Option.isEmpty(appenderName)) {
        logger.warn(
          "No appender name given for appender of type " + className + "].");
      } else {
        appender.setName(appenderName);
        logger.debug("Appender named as [" + appenderName + "]");
      }

      // The execution context contains a bag which contains the appenders
      // created thus far.
      HashMap appenderBag =
        (HashMap) ec.getObjectMap().get(ActionConst.APPENDER_BAG);
      // add the appender just created to the appender bag.
      appenderBag.put(appenderName, appender);

      logger.debug("Pushing appender on to the object stack.");
      ec.pushObject(appender);
    } catch (Exception oops) {
      inError = true;
      logger.error(
        "Could not create an Appender. Reported error follows.", oops);
      ec.addError(
        new ErrorItem(
          "Could not create appender of type " + className + "]."));
    }
  }

  /**
   * Once the children elements are also parsed, now is the time to activate
   * the appender options.
   */
  public void end(ExecutionContext ec, String name) {
    if (inError) {
      return;
    }

    if (appender instanceof OptionHandler) {
      ((OptionHandler) appender).activateOptions();
    }

    Object o = ec.peekObject();

    if (o != appender) {
      logger.warn(
        "The object at the of the stack is not the appender named ["
        + appender.getName() + "] pushed earlier.");
    } else {
      logger.warn(
        "Popping appender named [" + appender.getName()
        + "] from the object stack");
      ec.popObject();
    }
  }

  public void finish(ExecutionContext ec) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1424.java