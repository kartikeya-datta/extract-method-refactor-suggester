error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5792.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5792.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,72]

error in qdox parser
file content:
```java
offset: 72
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5792.java
text:
```scala
"( level ~= deb ) && ( logger like logger[1-2] || PROP.entry1 >= 234 )")@@;

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

package org.apache.log4j.rule;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.chainsaw.filter.FilterModel;
import org.apache.log4j.spi.LoggingEvent;


public class RuleTest extends JFrame {
  /**
   * UI for demonstrating infix/postfix conversion and expression rule evaluation...work in progress...
   *
   * Infix to postfix conversion routines and evaluation methods for boolean expressions.
   * See http://www.qiksearch.com/articles/cs/infix-postfix/
   * and http://www.spsu.edu/cs/faculty/bbrown/web_lectures/postfix/
   *
   * for more information.
   *
   * @author Scott Deboy <sdeboy@apache.org>
   *
   */
  Rule rule;
  FilterModel filterModel;

  public RuleTest(String inFixText) {
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    getContentPane().setLayout(new BorderLayout());

    filterModel = new FilterModel();

    final List eventList = new ArrayList();
    MDC.put("entry1", "123");
    eventList.add(
      new LoggingEvent(
        "org.apache.log4j.chainsaw", Logger.getLogger("logger1"),
        System.currentTimeMillis(), Level.DEBUG, "message1",
        new Exception("test")));
    MDC.put("entry2", "test1");
    eventList.add(
      new LoggingEvent(
        "org.apache.log4j.chainsaw", Logger.getLogger("logger2"),
        System.currentTimeMillis(), Level.DEBUG, "message2",
        new Exception("test2")));
    eventList.add(
      new LoggingEvent(
        "org.apache.log4j.net", Logger.getLogger("logger3"),
        System.currentTimeMillis(), Level.DEBUG, "message3",
        new Exception("test3")));

    MDC.put("test", "234");
    eventList.add(
      new LoggingEvent(
        "org.apache.log4j.chainsaw", Logger.getLogger("logger4"),
        System.currentTimeMillis(), Level.WARN, "message4",
        new Exception("test4")));

    Iterator iter = eventList.iterator();

    while (iter.hasNext()) {
      LoggingEvent event = (LoggingEvent) iter.next();
      filterModel.processNewLoggingEvent(event);
    }

    JPanel fieldPanel = new JPanel(new GridLayout(5, 1));

    fieldPanel.add(
      new JLabel("Enter infix expression to convert to postfix: "));

    final JTextField inFixTextField = new JTextField(inFixText);
    fieldPanel.add(inFixTextField);

    ExpressionRuleContext listener =
      new ExpressionRuleContext(filterModel, inFixTextField);
    inFixTextField.addKeyListener(listener);

    JButton inFixButton = new JButton("Convert InFix to PostFix");
    fieldPanel.add(inFixButton);

    JLabel resultsLabel = new JLabel("Results:");
    fieldPanel.add(resultsLabel);

    final JTextField inFixResult = new JTextField();
    fieldPanel.add(inFixResult);
    inFixButton.addActionListener(
      new AbstractAction() {
        public void actionPerformed(ActionEvent evt) {
          InFixToPostFix inFixConverter = new InFixToPostFix();
          inFixResult.setText(
            inFixConverter.convert(inFixTextField.getText()));
          rule = ExpressionRule.getRule(inFixResult.getText(), true);
        }
      });

    JPanel resultsPanel = new JPanel(new BorderLayout());

    JButton resultsButton =
      new JButton(
        "Evaluate postfix expression against collection of events: ");
    resultsPanel.add(resultsButton, BorderLayout.NORTH);

    final JTextArea results = new JTextArea(5, 50);
    resultsPanel.add(results, BorderLayout.CENTER);

    resultsButton.addActionListener(
      new AbstractAction() {
        public void actionPerformed(ActionEvent evt) {
          results.setText("");

          Iterator iter2 = eventList.iterator();

          while (iter2.hasNext()) {
            LoggingEvent event = (LoggingEvent) iter2.next();
            Iterator iter3 = event.getPropertyKeySet().iterator();
            StringBuffer mdc = new StringBuffer();

            while (iter3.hasNext()) {
              String mdcKey = (String) iter3.next();
              mdc.append(mdcKey);
              mdc.append(":");
              mdc.append(event.getProperty(mdcKey));
            }

            results.setText(
              results.getText()
              + ((results.getText().length() == 0) ? "" : "\n") + "level: "
              + event.getLevel() + ", logger: " + event.getLoggerName()
              + ", MDC: " + mdc.toString() + " - result: "
              + rule.evaluate(event));
          }
        }
      });

    getContentPane().add(fieldPanel, BorderLayout.NORTH);
    getContentPane().add(resultsPanel, BorderLayout.CENTER);
  }

  public static void main(String[] args) {
    RuleTest test =
      new RuleTest(
        "( level ~= deb ) && ( logger like logger[1-2] || MDC.entry1 >= 234 )");
    test.pack();
    test.setVisible(true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5792.java