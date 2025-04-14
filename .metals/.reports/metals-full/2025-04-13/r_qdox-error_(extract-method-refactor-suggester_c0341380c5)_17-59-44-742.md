error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14503.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14503.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14503.java
text:
```scala
r@@eturn JMeterUtils.getResString("foreach_controller_title");

// $Header$
/*
 * Copyright 2004 The Apache Software Foundation.
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

package org.apache.jmeter.control.gui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.control.ForeachController;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

/**
 * The user interface for a foreach controller which specifies that its subcomponents
 * should be executed some number of times in a loop.  This component can be
 * used standalone or embedded into some other component.
 * Copyright: 2000
 *
 * @author    Dolf Smits
 * @author    Michael Stover
 * @version   $Revision$
 */

public class ForeachControlPanel
    extends AbstractControllerGui
{

    /**
     * A field allowing the user to specify the input variable the controller
     * should loop.
     */
    private JTextField inputVal;

    /**
     * A field allowing the user to specify output variable the controller
     * should return.
     */
    private JTextField returnVal;


    /**
     * Boolean indicating whether or not this component should display its
     * name. If true, this is a standalone component. If false, this component
     * is intended to be used as a subpanel for another component.
     */
    private boolean displayName = true;

    /** The name of the infinite checkbox component. */
    private static final String INPUTVAL = "Input Field";

    /** The name of the loops field component. */
    private static final String RETURNVAL = "Return Field";

    /**
     * Create a new LoopControlPanel as a standalone component.
     */
    public ForeachControlPanel()
    {
        this(true);
    }

    /**
     * Create a new LoopControlPanel as either a standalone or an embedded
     * component.
     *
     * @param displayName  indicates whether or not this component should
     *                     display its name.  If true, this is a standalone
     *                     component.  If false, this component is intended
     *                     to be used as a subpanel for another component.
     */
    public ForeachControlPanel(boolean displayName)
    {
        this.displayName = displayName;
        init();
     }

    /**
     * A newly created component can be initialized with the contents of
     * a Test Element object by calling this method.  The component is
     * responsible for querying the Test Element object for the
     * relevant information to display in its GUI.
     *
     * @param element the TestElement to configure
     */
    public void configure(TestElement element)
    {
        super.configure(element);
        inputVal.setText(((ForeachController) element).getInputValString());
        returnVal.setText(((ForeachController) element).getReturnValString());
    }

    /* Implements JMeterGUIComponent.createTestElement() */
    public TestElement createTestElement()
    {
        ForeachController lc = new ForeachController();
        modifyTestElement(lc);
        return lc;
    }

    /* Implements JMeterGUIComponent.modifyTestElement(TestElement) */
    public void modifyTestElement(TestElement lc)
    {
        configureTestElement(lc);
        if (lc instanceof ForeachController)
        {
            if (inputVal.getText().length() > 0)
            {
                ((ForeachController) lc).setInputVal(inputVal.getText());
            }
            else
            {
                ((ForeachController) lc).setInputVal("");
            }
            if (returnVal.getText().length() > 0)
            {
                ((ForeachController) lc).setReturnVal(returnVal.getText());
            }
            else
            {
                ((ForeachController) lc).setReturnVal("");
            }
        }
    }


    /* Implements JMeterGUIComponent.getStaticLabel() */
    public String getStaticLabel()
    {
        return JMeterUtils.getResString("foreach_controller_title") +" (BETA)";
    }

    /**
     * Initialize the GUI components and layout for this component.
     */
    private void init()
    {
        // The Loop Controller panel can be displayed standalone or inside
        // another panel.  For standalone, we want to display the TITLE, NAME,
        // etc. (everything). However, if we want to display it within another
        // panel, we just display the Loop Count fields (not the TITLE and
        // NAME).

        // Standalone
        if (displayName)
        {
            setLayout(new BorderLayout(0, 5));
            setBorder(makeBorder());
            add(makeTitlePanel(), BorderLayout.NORTH);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(createLoopCountPanel(), BorderLayout.NORTH);
            add(mainPanel, BorderLayout.CENTER);
        }
        else
        {
            // Embedded
            setLayout(new BorderLayout());
            add(createLoopCountPanel(), BorderLayout.NORTH);
        }
    }

    /**
     * Create a GUI panel containing the components related to the number of
     * loops which should be executed.
     *
     * @return a GUI panel containing the loop count components
     */
    private JPanel createLoopCountPanel()
    {
//        JPanel loopPanel = new JPanel(new BorderLayout(5, 0));
        VerticalPanel loopPanel = new VerticalPanel();

        // LOOP LABEL
        JLabel inputValLabel =
            new JLabel(JMeterUtils.getResString("foreach_input"));
        JLabel returnValLabel =
            new JLabel(JMeterUtils.getResString("foreach_output"));

        // TEXT FIELD
		JPanel inputValSubPanel = new JPanel(new BorderLayout(5, 0));
        inputVal = new JTextField("", 5);
        inputVal.setName(INPUTVAL);
        inputValLabel.setLabelFor(inputVal);
        inputValSubPanel.add(inputValLabel, BorderLayout.WEST);
        inputValSubPanel.add(inputVal, BorderLayout.CENTER);

        // TEXT FIELD
		JPanel returnValSubPanel = new JPanel(new BorderLayout(5, 0));
        returnVal = new JTextField("", 5);
        returnVal.setName(RETURNVAL);
        returnValLabel.setLabelFor(returnVal);
        returnValSubPanel.add(returnValLabel, BorderLayout.WEST);
        returnValSubPanel.add(returnVal, BorderLayout.CENTER);

        loopPanel.add(inputValSubPanel);
        loopPanel.add(returnValSubPanel);

        return loopPanel;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14503.java