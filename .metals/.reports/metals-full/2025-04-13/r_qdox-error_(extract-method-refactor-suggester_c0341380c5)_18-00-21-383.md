error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4025.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4025.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4025.java
text:
```scala
c@@ontinueBox.setSelected(errorAction == OnErrorTestElement.ON_ERROR_CONTINUE);

// $Header$
/*
 * Copyright 2003-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
*/

package org.apache.jmeter.gui;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.jmeter.testelement.OnErrorTestElement;
import org.apache.jmeter.util.JMeterUtils;

/**
 * 
 * @version $Revision$ $Date$
 */
public class OnErrorPanel extends JPanel 
{
	// Sampler error action buttons
	private JRadioButton continueBox;
	private JRadioButton stopThrdBox;
	private JRadioButton stopTestBox;

	private JPanel createOnErrorPanel()
	{
		JPanel panel = new JPanel();
		panel.setBorder(
			BorderFactory.createTitledBorder(
				JMeterUtils.getResString("sampler_on_error_action")));

		ButtonGroup group = new ButtonGroup();

		continueBox =
			new JRadioButton(JMeterUtils.getResString("sampler_on_error_continue"));
		group.add(continueBox);
		continueBox.setSelected(true);
		panel.add(continueBox);

		stopThrdBox =
			new JRadioButton(JMeterUtils.getResString("sampler_on_error_stop_thread"));
		group.add(stopThrdBox);
		panel.add(stopThrdBox);

		stopTestBox =
			new JRadioButton(JMeterUtils.getResString("sampler_on_error_stop_test"));
		group.add(stopTestBox);
		panel.add(stopTestBox);

		return panel;
	}
    /**
     * Create a new NamePanel with the default name.
     */
    public OnErrorPanel()
    {
        init();
    }

    /**
     * Initialize the GUI components and layout.
     */
    private void init()
    {
        setLayout(new BorderLayout(5, 0));
        add(createOnErrorPanel());
    }
    public void configure(int errorAction)
    {
		stopTestBox.setSelected(errorAction == OnErrorTestElement.ON_ERROR_STOPTEST);
		stopThrdBox.setSelected(errorAction == OnErrorTestElement.ON_ERROR_STOPTHREAD);
		//continueBox.setSelected(etc);// no need to set the remaining Radio Button
    }
    
    public int getOnErrorSetting()
    {
		if (stopTestBox.isSelected()) return OnErrorTestElement.ON_ERROR_STOPTEST;
		if (stopThrdBox.isSelected()) return OnErrorTestElement.ON_ERROR_STOPTHREAD;

		// Defaults to continue
		return OnErrorTestElement.ON_ERROR_CONTINUE;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4025.java