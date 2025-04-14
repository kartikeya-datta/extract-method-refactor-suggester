error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8536.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8536.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8536.java
text:
```scala
g@@etLogger().debug("Removing component from the object stack");

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

package org.apache.joran.action;

import org.apache.joran.ExecutionContext;
import org.apache.joran.Pattern;
import org.apache.joran.helper.Option;

import org.apache.log4j.config.PropertySetter;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.spi.Component;
import org.apache.log4j.spi.ErrorItem;
import org.apache.log4j.spi.OptionHandler;

import org.xml.sax.Attributes;

import java.util.Stack;


/**
 * @author Ceki G&uuml;lc&uuml;
 *
 */
public class NestComponentIA extends ImplicitAction {
  
  // actionDataStack contains ActionData instances
  // We use a stack of ActionData objects in order to support nested
  // elements which are handled by the same NestComponentIA instance.
  // We push a ActionData instance in the isApplicable method (if the
  // action is applicable) and pop it in the end() method.
  // The XML well-formedness rule will guarantee that a push will eventually
  // be followed by the corresponding pop.
  Stack actionDataStack = new Stack();

  public boolean isApplicable(
    Pattern pattern, Attributes attributes, ExecutionContext ec) {
    //LogLog.debug("in NestComponentIA.isApplicable <" + pattern + ">");
    String nestedElementTagName = pattern.peekLast();

    Object o = ec.peekObject();
    PropertySetter parentBean = new PropertySetter(o);

    int containmentType = parentBean.canContainComponent(nestedElementTagName);

    switch (containmentType) {
    case PropertySetter.NOT_FOUND:
      return false;

    // we only push action data if NestComponentIA is applicable
    case PropertySetter.AS_COLLECTION:
    case PropertySetter.AS_PROPERTY:
      ActionData ad = new ActionData(parentBean, containmentType);
      actionDataStack.push(ad);

      return true;
    default:
      ec.addError(
        new ErrorItem(
          "PropertySetter.canContainComponent returned " + containmentType));
      return false;
    }
  }

  public void begin(
    ExecutionContext ec, String localName, Attributes attributes) {
    //LogLog.debug("in NestComponentIA begin method");
    // get the action data object pushed in isApplicable() method call
    ActionData actionData = (ActionData) actionDataStack.peek();

    String className = attributes.getValue(CLASS_ATTRIBUTE);

    // perform variable name substitution
    className = ec.subst(className);

    if (Option.isEmpty(className)) {
      actionData.inError = true;

      String errMsg = "No class name attribute in <" + localName + ">";
      getLogger().error(errMsg);
      ec.addError(new ErrorItem(errMsg));

      return;
    }

    try {
      getLogger().debug(
        "About to instantiate component <{}> of type [{}]", localName,
        className);

      actionData.nestedComponent = Loader.loadClass(className).newInstance();
      
      // pass along the repository
      if(actionData.nestedComponent instanceof Component) {
        ((Component) actionData.nestedComponent).setLoggerRepository(this.repository);
      }
      getLogger().debug(
        "Pushing component <{}> on top of the object stack.", localName);
      ec.pushObject(actionData.nestedComponent);
    } catch (Exception oops) {
      actionData.inError = true;

      String msg = "Could not create component <" + localName + ">.";
      getLogger().error(msg, oops);
      ec.addError(new ErrorItem(msg));
    }
  }

  public void end(ExecutionContext ec, String tagName) {
    getLogger().debug("entering end method");

    // pop the action data object pushed in isApplicable() method call
    // we assume that each this begin
    ActionData actionData = (ActionData) actionDataStack.pop();

    if (actionData.inError) {
      return;
    }

    if (actionData.nestedComponent instanceof OptionHandler) {
      ((OptionHandler) actionData.nestedComponent).activateOptions();
    }

    Object o = ec.peekObject();

    if (o != actionData.nestedComponent) {
      getLogger().warn(
        "The object on the top the of the stack is not the component pushed earlier.");
    } else {
      getLogger().warn("Removing component from the object stack");
      ec.popObject();

      // Now let us attach the component
      switch (actionData.containmentType) {
      case PropertySetter.AS_PROPERTY:
        getLogger().debug(
          "Setting [{}] to parent of type [{}]", tagName,
          actionData.parentBean.getObjClass());
        actionData.parentBean.setComponent(
          tagName, actionData.nestedComponent);

        break;
      case PropertySetter.AS_COLLECTION:
        getLogger().debug(
          "Adding [{}] to parent of type [{}]", tagName,
          actionData.parentBean.getObjClass());
        actionData.parentBean.addComponent(
          tagName, actionData.nestedComponent);

        break;
      }
    }
  }

  public void finish(ExecutionContext ec) {
  }
}


class ActionData {
  PropertySetter parentBean;
  Object nestedComponent;
  int containmentType;
  boolean inError;

  ActionData(PropertySetter parentBean, int containmentType) {
    this.parentBean = parentBean;
    this.containmentType = containmentType;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8536.java