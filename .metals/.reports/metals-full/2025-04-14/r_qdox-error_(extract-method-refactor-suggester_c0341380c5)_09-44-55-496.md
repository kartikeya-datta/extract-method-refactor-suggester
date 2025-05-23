error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1520.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1520.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1520.java
text:
```scala
public I@@nterpreter(RuleStore rs) {

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

package org.apache.joran;

import org.apache.joran.action.*;

import org.apache.log4j.Logger;
import org.apache.log4j.helpers.LogLog;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.Vector;


// TODO Errors should be reported in Error objects instead of just strings.
// TODO Interpreter should set its own ErrorHander for XML parsing errors.
/**
 * <id>Interpreter</id> is Joran's main driving class. It acts as a SAX 
 * {@link ContentHandler} which invokes various 
 * {@link Action actions} according to predefined patterns. 
 * 
 * <p>Patterns are kept in a {@link RuleStore} which is programmed to store and
 * then later produce the applicable actions for a given pattern.
 * 
 * <p>The pattern corresponding to a top level &lt;a&gt; element is the string 
 * <id>"a"</id>.
 * 
 * <p>The pattern corresponding to an element &lt;b&gt; embeded within a top level 
 * &lt;a&gt; element is the string <id>"a/b"</id>.
 * 
 * <p>The pattern corresponding to an &lt;b&gt; and any level of nesting is
 * "&star;/b. Thus, the &star; character placed at the beginning of a pattern
 * serves as a wildcard for the level of nesting.  
 * 
 * Conceptually, this is very similar to the API of commons-digester. Joran 
 * offers several small advantages. First and foremost, it offers support for
 * implicit actions which result in a significant leap in flexibility. Second,
 * in our opinion better error reporting capability. Third, it is self-reliant.
 * It does not depend on other APIs, in particular commons-logging which is
 * a big no-no for log4j. Last but not least, joran is quite tiny and is 
 * expected to remain so.  
 *   
 * @author Ceki G&uuml;lcu&uuml;
 *
 */
public class Interpreter extends DefaultHandler {
  static final Logger logger = Logger.getLogger(Interpreter.class);
  private static List EMPTY_LIST = new Vector(0);
  private RuleStore ruleStore;
  private ExecutionContext ec;
  private ArrayList implicitActions;
  Pattern pattern;
  Locator locator;
  
  /**
   * The <id>actionListStack</id> contains a list of actions that are
   * executing for the given XML element.
   *
   * A list of actions is pushed by the {link #startElement} and popped by
   * {@link #endElement}.
   *
   */
  Stack actionListStack;

  Interpreter(RuleStore rs) {
    ruleStore = rs;
    ec = new ExecutionContext(this);
    implicitActions = new ArrayList(3);
    pattern = new Pattern();
    actionListStack = new Stack();
  }

  public ExecutionContext getExecutionContext() {
    return ec;
  }

  public void startDocument() {
    System.out.println(" in JP startDocument");
  }

  public void startElement(
    String namespaceURI, String localName, String qName, Attributes atts) {
    String x = null;

    String tagName = getTagName(localName, qName);

    logger.debug("in startElement <" + tagName + ">");

    pattern.push(tagName);

    List applicableActionList = getapplicableActionList(pattern);

    if (applicableActionList != null) {
      actionListStack.add(applicableActionList);
      callBeginAction(applicableActionList, tagName, atts);
    } else {
      actionListStack.add(EMPTY_LIST);
      logger.debug("no applicable action for <" + tagName + ">.");
    }
  }

  public void endElement(String namespaceURI, String localName, String qName) {
    List applicableActionList = (List) actionListStack.pop();

    if (applicableActionList != EMPTY_LIST) {
      callEndAction(applicableActionList, getTagName(localName, qName));
    }

    // given that we always push, we must also pop the pattern
    pattern.pop();
  }

  public Locator getLocator() {
    return locator;
  }

  public void setDocumentLocator(Locator l) {
    locator = l;
  }

  String getTagName(String localName, String qName) {
    String tagName = localName;

    if ((tagName == null) || (tagName.length() < 1)) {
      tagName = qName;
    }

    return tagName;
  }

  public void addImplcitAction(ImplicitAction ia) {
    implicitActions.add(ia);
  }

  /**
   * Check if any implicit actions are applicable. As soon as an applicable
   * action is found, it is returned. Thus, the returned list will have at most
   * one element.
   */
  List lookupImplicitAction(ExecutionContext ec, Pattern pattern) {
    int len = implicitActions.size();

    for (int i = 0; i < len; i++) {
      ImplicitAction ia = (ImplicitAction) implicitActions.get(i);

      if (ia.isApplicable(ec, pattern.peekLast())) {
        List actionList = new ArrayList(1);
        actionList.add(ia);

        return actionList;
      }
    }

    return null;
  }

  /**
   * Return the list of applicable patterns for this
  */
  List getapplicableActionList(Pattern pattern) {
    List applicableActionList = ruleStore.matchActions(pattern);

    //logger.debug("set of applicable patterns: " + applicableActionList);
    if (applicableActionList == null) {
      applicableActionList = lookupImplicitAction(ec, pattern);
    }

    return applicableActionList;
  }

  void callBeginAction(
    List applicableActionList, String tagName, Attributes atts) {
    if (applicableActionList == null) {
      return;
    }

    Iterator i = applicableActionList.iterator();

    while (i.hasNext()) {
      Action action = (Action) i.next();
      action.begin(ec, tagName, atts);
    }
  }

  void callEndAction(List applicableActionList, String tagName) {
    if (applicableActionList == null) {
      return;
    }

    //logger.debug("About to call end actions on node: <" + localName + ">");
    Iterator i = applicableActionList.iterator();

    while (i.hasNext()) {
      Action action = (Action) i.next();
      action.end(ec, tagName);
    }
  }

  public RuleStore getRuleStore() {
    return ruleStore;
  }

  public void setRuleStore(RuleStore ruleStore) {
    this.ruleStore = ruleStore;
  }

  public void endDocument() {
  }

  public void error(SAXParseException spe) throws SAXException {
    ec.addError(new ErrorItem("Parsing error", getLocator(), spe));
    LogLog.error(
        "Parsing problem on line " + spe.getLineNumber() + " and column "
        + spe.getColumnNumber());
  }
  
  public void fatalError(SAXParseException spe)  throws SAXException {
    ec.addError(new ErrorItem("Parsing fatal error", getLocator(), spe));
    LogLog.error(
        "Parsing problem on line " + spe.getLineNumber() + " and column "
        + spe.getColumnNumber());
  }
  
  public void warning(SAXParseException spe) throws SAXException {
    ec.addError(new ErrorItem("Parsing warning", getLocator(), spe));
    LogLog.warn(
        "Parsing problem on line " + spe.getLineNumber() + " and column "
        + spe.getColumnNumber());
  } 

  public void endPrefixMapping(java.lang.String prefix) {
  }

  public void ignorableWhitespace(char[] ch, int start, int length) {
  }

  public void processingInstruction(
    java.lang.String target, java.lang.String data) {
  }

  public void skippedEntity(java.lang.String name) {
  }

  public void startPrefixMapping(
    java.lang.String prefix, java.lang.String uri) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1520.java