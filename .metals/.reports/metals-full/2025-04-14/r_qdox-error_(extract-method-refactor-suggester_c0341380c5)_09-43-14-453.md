error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7161.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7161.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7161.java
text:
```scala
M@@enuFactory.addFileMenu(pop, false);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

package org.apache.jmeter.threads.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;

import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.action.ActionNames;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.util.JMeterUtils;

public abstract class AbstractThreadGroupGui extends AbstractJMeterGuiComponent {
    private static final long serialVersionUID = 240L;

    // Sampler error action buttons
    private JRadioButton continueBox;

    private JRadioButton startNextLoop;

    private JRadioButton stopThrdBox;

    private JRadioButton stopTestBox;

    private JRadioButton stopTestNowBox;

    public AbstractThreadGroupGui(){
        super();
        init();
        initGui();
    }

    @Override
    public Collection<String> getMenuCategories() {
        return Arrays.asList(new String[] { MenuFactory.THREADS });
    }

    @Override
    public JPopupMenu createPopupMenu() {
        JPopupMenu pop = new JPopupMenu();
        pop.add(MenuFactory.makeMenus(new String[] {
                MenuFactory.CONTROLLERS,
                MenuFactory.CONFIG_ELEMENTS,
                MenuFactory.TIMERS,
                MenuFactory.PRE_PROCESSORS,
                MenuFactory.SAMPLERS,
                MenuFactory.POST_PROCESSORS,
                MenuFactory.ASSERTIONS,
                MenuFactory.LISTENERS,
                },
                JMeterUtils.getResString("add"), // $NON-NLS-1$
                ActionNames.ADD));
        MenuFactory.addEditMenu(pop, true);
        MenuFactory.addFileMenu(pop);
        return pop;
    }

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    @Override
    public void clearGui(){
        super.clearGui();
        initGui();
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        Box box = Box.createVerticalBox();
        box.add(makeTitlePanel());
        box.add(createOnErrorPanel());
        add(box, BorderLayout.NORTH);
    }
    
    private void initGui() {
        continueBox.setSelected(true);
    }

    private JPanel createOnErrorPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("sampler_on_error_action"))); // $NON-NLS-1$

        ButtonGroup group = new ButtonGroup();

        continueBox = new JRadioButton(JMeterUtils.getResString("sampler_on_error_continue")); // $NON-NLS-1$
        group.add(continueBox);
        panel.add(continueBox);

        startNextLoop = new JRadioButton(JMeterUtils.getResString("sampler_on_error_start_next_loop")); // $NON-NLS-1$
        group.add(startNextLoop);
        panel.add(startNextLoop);

        stopThrdBox = new JRadioButton(JMeterUtils.getResString("sampler_on_error_stop_thread")); // $NON-NLS-1$
        group.add(stopThrdBox);
        panel.add(stopThrdBox);

        stopTestBox = new JRadioButton(JMeterUtils.getResString("sampler_on_error_stop_test")); // $NON-NLS-1$
        group.add(stopTestBox);
        panel.add(stopTestBox);

        stopTestNowBox = new JRadioButton(JMeterUtils.getResString("sampler_on_error_stop_test_now")); // $NON-NLS-1$
        group.add(stopTestNowBox);
        panel.add(stopTestNowBox);

        return panel;
    }

    private void setSampleErrorBoxes(AbstractThreadGroup te) {
        if (te.getOnErrorStopTest()) {
            stopTestBox.setSelected(true);
        } else if (te.getOnErrorStopTestNow()) {
            stopTestNowBox.setSelected(true);
        } else if (te.getOnErrorStopThread()) {
            stopThrdBox.setSelected(true);
        } else if (te.getOnErrorStartNextLoop()) {
            startNextLoop.setSelected(true);
        } else {
            continueBox.setSelected(true);
        }
    }

    private String onSampleError() {
        if (stopTestBox.isSelected()) {
            return AbstractThreadGroup.ON_SAMPLE_ERROR_STOPTEST;
        }
        if (stopTestNowBox.isSelected()) {
            return AbstractThreadGroup.ON_SAMPLE_ERROR_STOPTEST_NOW;
        }
        if (stopThrdBox.isSelected()) {
            return AbstractThreadGroup.ON_SAMPLE_ERROR_STOPTHREAD;
        }
        if (startNextLoop.isSelected()) {
            return AbstractThreadGroup.ON_SAMPLE_ERROR_START_NEXT_LOOP;
        }

        // Defaults to continue
        return AbstractThreadGroup.ON_SAMPLE_ERROR_CONTINUE;
    }

   @Override
    public void configure(TestElement tg) {
        super.configure(tg);
        setSampleErrorBoxes((AbstractThreadGroup) tg);
    }
    
   @Override
    protected void configureTestElement(TestElement tg) {
        super.configureTestElement(tg);
        tg.setProperty(new StringProperty(AbstractThreadGroup.ON_SAMPLE_ERROR, onSampleError()));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7161.java