error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13992.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13992.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13992.java
text:
```scala
n@@ew String[] { Help.HELP_FUNCTIONS,

package org.apache.jmeter.functions.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.functions.Function;
import org.apache.jmeter.gui.action.ActionRouter;
import org.apache.jmeter.gui.action.Help;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.ComponentUtil;
import org.apache.jorphan.gui.JLabeledChoice;
import org.apache.jorphan.gui.JLabeledTextField;
import org.apache.jorphan.reflect.ClassFinder;

/**
 * @version $Revision$
 */
public class FunctionHelper
    extends JDialog
    implements ActionListener, ChangeListener
{
    JLabeledChoice functionList;
    ArgumentsPanel parameterPanel;
    JLabeledTextField cutPasteFunction;
    private Map functionMap = new HashMap();
    JButton generateButton;

    public FunctionHelper()
    {
        super(
            (JFrame) null,
            JMeterUtils.getResString("function_helper_title"),
            false);
        init();
    }

    private void init()
    {
        parameterPanel =
            new ArgumentsPanel(JMeterUtils.getResString("function_params"));
        initializeFunctionList();
        this.getContentPane().setLayout(new BorderLayout(10, 10));
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comboPanel.add(functionList);
        JButton helpButton = new JButton(JMeterUtils.getResString("help"));
        helpButton.addActionListener(new HelpListener());
        comboPanel.add(helpButton);
        this.getContentPane().add(comboPanel, BorderLayout.NORTH);
        this.getContentPane().add(parameterPanel, BorderLayout.CENTER);
        JPanel resultsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cutPasteFunction =
            new JLabeledTextField(
                JMeterUtils.getResString("cut_paste_function"),
                35);
        resultsPanel.add(cutPasteFunction);
        generateButton = new JButton(JMeterUtils.getResString("generate"));
        generateButton.addActionListener(this);
        resultsPanel.add(generateButton);
        this.getContentPane().add(resultsPanel, BorderLayout.SOUTH);
        this.pack();
        ComponentUtil.centerComponentInWindow(this);
    }

    private void initializeFunctionList()
    {
        try
        {
            List functionClasses =
                ClassFinder.findClassesThatExtend(
                    JMeterUtils.getSearchPaths(),
                    new Class[] { Function.class },
                    true);
            Iterator iter = functionClasses.iterator();
            String[] functionNames = new String[functionClasses.size()];
            int count = 0;
            while (iter.hasNext())
            {
                Class cl = Class.forName((String) iter.next());
                functionNames[count] =
                    ((Function) cl.newInstance()).getReferenceKey();
                functionMap.put(functionNames[count], cl);
                count++;
            }
            functionList =
                new JLabeledChoice(
                    JMeterUtils.getResString("choose_function"),
                    functionNames);
            functionList.addChangeListener(this);
        }
        catch (IOException e)
        {
        }
        catch (ClassNotFoundException e)
        {
        }
        catch (InstantiationException e)
        {
        }
        catch (IllegalAccessException e)
        {
        }
    }

    public void stateChanged(ChangeEvent event)
    {
        try
        {
            Arguments args = new Arguments();
            Function function =
                (Function) ((Class) functionMap.get(functionList.getText()))
                    .newInstance();
            List argumentDesc = function.getArgumentDesc();
            Iterator iter = argumentDesc.iterator();
            while (iter.hasNext())
            {
                String help = (String) iter.next();
                args.addArgument(help, "");
            }
            parameterPanel.configure(args);
            parameterPanel.revalidate();
            getContentPane().remove(parameterPanel);
            this.pack();
            getContentPane().add(parameterPanel, BorderLayout.CENTER);
            this.pack();
            this.validate();
            this.repaint();
        }
        catch (InstantiationException e)
        {
        }
        catch (IllegalAccessException e)
        {
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        StringBuffer functionCall = new StringBuffer("${");
        functionCall.append(functionList.getText());
        Arguments args = (Arguments) parameterPanel.createTestElement();
        if (args.getArguments().size() > 0)
        {
            functionCall.append("(");
            PropertyIterator iter = args.iterator();
            boolean first = true;
            while (iter.hasNext())
            {
                Argument arg = (Argument) iter.next().getObjectValue();
                if (!first)
                {
                    functionCall.append(",");
                }
                functionCall.append((String) arg.getValue());
                first = false;
            }
            functionCall.append(")");
        }
        functionCall.append("}");
        cutPasteFunction.setText(functionCall.toString());
    }

    private class HelpListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String[] source =
                new String[] { Help.HELP_PAGE,
                    functionList.getText()};
            ActionEvent helpEvent = new ActionEvent(source, e.getID(), "help");
            ActionRouter.getInstance().actionPerformed(helpEvent);
        }
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13992.java