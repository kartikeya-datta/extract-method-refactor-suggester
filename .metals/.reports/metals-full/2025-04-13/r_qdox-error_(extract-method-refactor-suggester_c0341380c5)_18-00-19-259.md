error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6713.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6713.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,203]

error in qdox parser
file content:
```java
offset: 203
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6713.java
text:
```scala
"<b>Contributors:</b><ul><li>Scott Deboy &lt;sdeboy@apache.org&gt;</li><li>Paul Smith &lt;psmith@apache.org&gt;</li><li>Ceki G&uuml;lc&uuml; &lt;ceki@apache.org&gt;</li><li>Oliver Burn</li></ul></html>")@@;

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

/*
 * @author Paul Smith <psmith@apache.org>
 *
*/
package org.apache.log4j.chainsaw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.chainsaw.icons.ChainsawIcons;


/**
 * A simple About box telling people stuff about this project
 *
 * @author Paul Smith <psmith@apache.org>
 *
 */
class ChainsawAbout extends JDialog {
  ChainsawAbout(JFrame parent) {
    super(parent, "About Chainsaw v2", true);
//    setResizable(false);
    setBackground(Color.white);
	getContentPane().setLayout(new BorderLayout());
    JPanel panel = new JPanel(new GridBagLayout());
//    panel.setOpaque(false);
    panel.setBackground(Color.white);
    panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

    GridBagConstraints c = new GridBagConstraints();

    c.anchor = GridBagConstraints.WEST;
    c.gridx = 0;
    c.gridy = 0;

    final JLabel info =
      new JLabel("<html>Chainsaw 2.0alpha<p><p>" +
       "Brought to you by the Log4J team:<p>" +
       "<b>http://logging.apache.org/log4j</b><p><p>" +
      "Bug report, mailing list and wiki information:<p>" +
     "<b>http://logging.apache.org/site/bugreport.html</b><p><p>" +
      "<b>Contributors:</b><ul><li>Scott Deboy &lt;sdeboy@apache.org&gt;</li><li>Paul Smith &lt;psmith@apache.org&gt;</li><li>Ceki G&uuml;lc&uuml; &lt;ceki@apache.org&gt;</li></ul></html>");

      JButton button = new JButton("Copy bug report link to clipboard");
      button.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent event) {
              Toolkit tk = getToolkit();
              Clipboard cb = tk.getSystemClipboard();
              cb.setContents(new StringSelection("http://logging.apache.org/site/bugreport.html"), null);
              }});
              panel.add(info, c);

    JLabel title = new JLabel(ChainsawIcons.ICON_LOG4J);
    c.gridy = 1;

    panel.add(button, c);

    c.gridy = 2;
    panel.add(title, c);

    c.gridy = 3;
    c.anchor = GridBagConstraints.EAST;

    JButton closeButton = new JButton("Close");
    closeButton.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          setVisible(false);
        }
      });
    closeButton.setDefaultCapable(true);
    panel.add(closeButton, c);

	getContentPane().add(panel, BorderLayout.CENTER);
    pack();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6713.java