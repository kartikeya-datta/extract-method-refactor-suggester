error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14283.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14283.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14283.java
text:
```scala
B@@orderFactory.createEtchedBorder(), JMeterUtils.getResString("scheduler_configuration")));

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002,2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.jmeter.threads.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.gui.util.FocusRequester;
import org.apache.jmeter.gui.util.JDateField;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.LongProperty;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;

/****************************************
 * Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author    Michael Stover
 *@created   $Date$
 *@version   1.0
 ***************************************/

public class ThreadGroupGui extends AbstractJMeterGuiComponent implements ItemListener
{
    LoopControlPanel loopPanel;
    private VerticalPanel mainPanel;


    private final static String THREAD_NAME = "Thread Field";
    private final static String RAMP_NAME = "Ramp Up Field";

    private JTextField threadInput;
    private JTextField rampInput;

    private final static String SCHEDULER = "scheduler";
    private final static String START_TIME= "start_time";
    private final static String END_TIME= "end_time";

    private JDateField start;
    private JDateField end;
    private JCheckBox scheduler;

    /****************************************
     * !ToDo (Constructor description)
     ***************************************/
    public ThreadGroupGui()
    {
        super();
        init();
    }

    /****************************************
     * !ToDoo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public Collection getMenuCategories()
    {
        return null;
    }

    /****************************************
     * !ToDo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public TestElement createTestElement()
    {
        ThreadGroup tg = new ThreadGroup();
        modifyTestElement(tg);
        return tg;
    }

    /**
         * Modifies a given TestElement to mirror the data in the gui components.
         * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
         */
    public void modifyTestElement(TestElement tg)
    {
        super.configureTestElement(tg);
        if (tg instanceof ThreadGroup)
        {
            ((ThreadGroup) tg).setSamplerController((LoopController) loopPanel.createTestElement());
        }
        tg.setProperty(ThreadGroup.NUM_THREADS, threadInput.getText());
        tg.setProperty(ThreadGroup.RAMP_TIME, rampInput.getText());
        tg.setProperty(new LongProperty(ThreadGroup.START_TIME,((Date)start.getDate()).getTime()));
        tg.setProperty(new LongProperty(ThreadGroup.END_TIME,((Date)end.getDate()).getTime()));
        tg.setProperty(new BooleanProperty(ThreadGroup.SCHEDULER,scheduler.isSelected()));
    }

    /****************************************
     * !ToDo (Method description)
     *
     *@param tg  !ToDo (Parameter description)
     ***************************************/
    public void configure(TestElement tg)
    {
        super.configure(tg);
        threadInput.setText(tg.getPropertyAsString(ThreadGroup.NUM_THREADS));
        rampInput.setText(tg.getPropertyAsString(ThreadGroup.RAMP_TIME));
        loopPanel.configure((TestElement) tg.getProperty(ThreadGroup.MAIN_CONTROLLER).getObjectValue());
        scheduler.setSelected(tg.getPropertyAsBoolean(ThreadGroup.SCHEDULER));
        if (scheduler.isSelected()) {
            mainPanel.setVisible(true);
        }else {
            mainPanel.setVisible(false);
        }
        start.setDate(new Date(tg.getPropertyAsLong(ThreadGroup.START_TIME)));
        end.setDate(new Date(tg.getPropertyAsLong(ThreadGroup.END_TIME)));
    }

    public void itemStateChanged(ItemEvent ie){
        if (ie.getItem().equals(scheduler)){
            if(scheduler.isSelected()) {
                mainPanel.setVisible(true);
            } else {
                mainPanel.setVisible(false);
            }
        }
    }

    /****************************************
     * !ToDo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public JPopupMenu createPopupMenu()
    {
        JPopupMenu pop = new JPopupMenu();
        pop.add(
            MenuFactory.makeMenus(
                new String[] { MenuFactory.CONTROLLERS, MenuFactory.LISTENERS, MenuFactory.SAMPLERS, 
                    MenuFactory.TIMERS, MenuFactory.CONFIG_ELEMENTS,MenuFactory.PRE_PROCESSORS,
                    MenuFactory.POST_PROCESSORS },
                JMeterUtils.getResString("Add"),
                "Add"));
        MenuFactory.addEditMenu(pop, true);
        MenuFactory.addFileMenu(pop);
        return pop;
    }

    /****************************************
     * !ToDo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public JPanel createControllerPanel()
    {
        loopPanel = new LoopControlPanel(false);
        LoopController looper = (LoopController) loopPanel.createTestElement();
        looper.setLoops(-1);
        loopPanel.configure(looper);
        return loopPanel;
    }


    /**
     * Create a panel containing the StartTime field and corresponding label.
     * 
     * @return a GUI panel containing the StartTime field
     */
    private JPanel createStartTimePanel()
    {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        JLabel label = new JLabel(JMeterUtils.getResString("starttime"));
        panel.add(label, BorderLayout.WEST);
        Date today = new Date();
        start = new JDateField(today);
        panel.add(start, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Create a panel containing the EndTime field and corresponding label.
     * 
     * @return a GUI panel containing the EndTime field
     */
    private JPanel createEndTimePanel()
    {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        JLabel label = new JLabel(JMeterUtils.getResString("endtime"));
        panel.add(label, BorderLayout.WEST);
        Date today = new Date();
        end = new JDateField(today);
        panel.add(end, BorderLayout.CENTER);
        return panel;
    }

    /****************************************
     * !ToDoo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public String getStaticLabel()
    {
        return JMeterUtils.getResString("ThreadGroup");
    }

    private void init()
    {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
                
        add(makeTitlePanel(), BorderLayout.NORTH);
        
        //JPanel mainPanel = new JPanel(new BorderLayout());
        
        // THREAD PROPERTIES
        VerticalPanel threadPropsPanel = new VerticalPanel();
        threadPropsPanel.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                JMeterUtils.getResString("thread_delay_properties")));

        // NUMBER OF THREADS
        JPanel threadPanel = new JPanel(new BorderLayout(5, 0));

        JLabel threadLabel = new JLabel(JMeterUtils.getResString("number_of_threads"));
        threadPanel.add(threadLabel, BorderLayout.WEST);

        threadInput = new JTextField("1", 5);
        threadInput.setName(THREAD_NAME);
        threadLabel.setLabelFor(threadInput);
        threadPanel.add(threadInput, BorderLayout.CENTER);

        threadPropsPanel.add(threadPanel);
        new FocusRequester(threadInput);

        // RAMP-UP
        JPanel rampPanel = new JPanel(new BorderLayout(5, 0));
        JLabel rampLabel = new JLabel(JMeterUtils.getResString("ramp_up"));
        rampPanel.add(rampLabel, BorderLayout.WEST);
        
        rampInput = new JTextField("1", 5);
        rampInput.setName(RAMP_NAME);
        rampLabel.setLabelFor(rampInput);
        rampPanel.add(rampInput, BorderLayout.CENTER);
        
        threadPropsPanel.add(rampPanel);

        // LOOP COUNT
        threadPropsPanel.add(createControllerPanel());

       // mainPanel.add(threadPropsPanel, BorderLayout.NORTH);
        //add(mainPanel, BorderLayout.CENTER);        

        scheduler = new JCheckBox( JMeterUtils.getResString("scheduler"));
        scheduler.addItemListener(this); 
        threadPropsPanel.add(scheduler);
        mainPanel = new VerticalPanel();
        mainPanel.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), JMeterUtils.getResString("scheduler_cofiguration")));
        mainPanel.add(createStartTimePanel());
        mainPanel.add(createEndTimePanel());
        mainPanel.setVisible(false);
        VerticalPanel intgrationPanel = new VerticalPanel();
        intgrationPanel.add(threadPropsPanel);        
        intgrationPanel.add(mainPanel);        
        add(intgrationPanel, BorderLayout.CENTER);

    }

    public void setNode(JMeterTreeNode node)
    {
        getNamePanel().setNode(node);
    }
    
    public Dimension getPreferredSize() {
        return getMinimumSize();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14283.java