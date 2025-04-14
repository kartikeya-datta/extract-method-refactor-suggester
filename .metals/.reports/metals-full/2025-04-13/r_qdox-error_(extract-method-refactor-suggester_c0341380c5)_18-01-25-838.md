error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10379.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10379.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10379.java
text:
```scala
i@@f(SaveService.isSaveTestPlanFormat20())

// $Header$
/*
 * Copyright 2001-2004 The Apache Software Foundation.
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

package org.apache.jmeter.gui.action;

import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JFileChooser;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.exceptions.IllegalUserActionException;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.gui.util.FileDialoger;
import org.apache.jmeter.save.OldSaveService;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author  Michael Stover
 * @author	<a href="mailto:klancast@swbell.net">Keith Lancaster</a>
 * @version $Revision$ updated on $Date$
 */
public class Save implements Command
{
    transient private static Logger log = LoggingManager.getLoggerForClass();
    public final static String SAVE_ALL_AS = "save_all_as";
    public final static String SAVE_AS = "save_as";
    public final static String SAVE = "save";
    //NOTUSED private String chosenFile;
    private String testPlanFile;

    private static Set commands = new HashSet();
    static {
        commands.add(SAVE_AS);
        commands.add(SAVE_ALL_AS);
        commands.add(SAVE);
    }

    /**
     * Constructor for the Save object.
     */
    public Save()
    {
    }

    /**
     * Gets the ActionNames attribute of the Save object.
     *
     * @return   the ActionNames value
     */
    public Set getActionNames()
    {
        return commands;
    }
    
    public boolean hasTestPlanFile()
    {
        return testPlanFile != null;
    }

    public void setTestPlanFile(String f)
    {
        testPlanFile = f;
        GuiPackage.getInstance().getMainFrame().setTitle(JMeterUtils.getExtendedFrameTitle(testPlanFile));
        try
        {
            FileServer.getFileServer().setBasedir(testPlanFile);
        }
        catch(IOException e1)
        {
            log.error("Failure setting file server's base dir",e1);
        } 
    }

    public void doAction(ActionEvent e) throws IllegalUserActionException
    {
        HashTree subTree = null;
        if (!commands.contains(e.getActionCommand()))
        {
            throw new IllegalUserActionException("Invalid user command:" + e.getActionCommand());
        }
        if (e.getActionCommand().equals(SAVE_AS))
        {
            subTree = GuiPackage.getInstance().getCurrentSubTree();
        }
        else
        {
            subTree = GuiPackage.getInstance().getTreeModel().getTestPlan();
        }

        String updateFile = testPlanFile; 
        if (!SAVE.equals(e.getActionCommand())
 testPlanFile == null)
        {
            JFileChooser chooser =
                FileDialoger.promptToSaveFile(
                    GuiPackage
                        .getInstance()
                        .getTreeListener()
                        .getCurrentNode()
                        .getName()
                        + ".jmx");
            if (chooser == null)
            {
                return;
            }
            updateFile = chooser.getSelectedFile().getAbsolutePath();
            if (!e.getActionCommand().equals(SAVE_AS))
            {
                setTestPlanFile(updateFile);
            }
        }
        // TODO: doesn't putting this here mark the tree as
        // saved even though a failure may occur later? 
        
        ActionRouter.getInstance().doActionNow(
            new ActionEvent(subTree, e.getID(), CheckDirty.SUB_TREE_SAVED));
        try
        {
            convertSubTree(subTree);
        }
        catch (Exception err)
        {
        }
        Writer writer = null;
		FileOutputStream ostream=null;
        try
        {
            if(JMeterUtils.getPropDefault("file_format","2.1").equals("2.0"))
            {
				ostream=new FileOutputStream(updateFile);
                OldSaveService.saveSubTree(subTree,ostream);
            }
            else
            {
                writer = new FileWriter(updateFile);
                SaveService.saveTree(subTree,writer);
            }
        }
        catch (Throwable ex)
        {
            testPlanFile = null;
            log.error("", ex);
            throw new IllegalUserActionException(
                "Couldn't save test plan to file: " + updateFile);
        }
        finally
        {
            closeWriter(writer);
            closeStream(ostream);
            if(testPlanFile != null)
            {
                GuiPackage.getInstance().getMainFrame().setTitle(JMeterUtils.getExtendedFrameTitle(testPlanFile));
            }
            GuiPackage.getInstance().getMainFrame().repaint();
        }
    }

    private void convertSubTree(HashTree tree)
    {
        Iterator iter = new LinkedList(tree.list()).iterator();
        while (iter.hasNext())
        {
            JMeterTreeNode item = (JMeterTreeNode) iter.next();
            convertSubTree(tree.getTree(item));
            TestElement testElement = item.getTestElement();
            tree.replace(item, testElement);
        }
    }

    private void closeWriter(Writer writer)
    {
        if (writer != null)
        {
            try
            {
                writer.close();
            }
            catch (Exception ex)
            {
                log.error("", ex);
            }
        }
    }

    private void closeStream(FileOutputStream fos)
    {
        if (fos != null)
        {
            try
            {
                fos.close();
            }
            catch (Exception ex)
            {
                log.error("", ex);
            }
        }
    }

	public static class Test extends junit.framework.TestCase
    {
        Save save;
        public Test(String name)
        {
            super(name);
        }

        public void setUp()
        {
            save = new Save();
        }

        public void testTreeConversion() throws Exception
        {
            HashTree tree = new ListedHashTree();
            JMeterTreeNode root = new JMeterTreeNode(new Arguments(), null);
            tree.add(root, root);
            tree.getTree(root).add(root, root);
            save.convertSubTree(tree);
            assertEquals(
                tree.getArray()[0].getClass().getName(),
                root.getTestElement().getClass().getName());
            tree = tree.getTree(tree.getArray()[0]);
            assertEquals(
                tree.getArray()[0].getClass().getName(),
                root.getTestElement().getClass().getName());
            assertEquals(
                tree
                    .getTree(tree.getArray()[0])
                    .getArray()[0]
                    .getClass()
                    .getName(),
                root.getTestElement().getClass().getName());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10379.java