error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2322.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2322.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2322.java
text:
```scala
A@@ctionRouter.getInstance().doActionNow(new ActionEvent(subTree,e.getID(),CheckDirty.SUB_TREE_SAVED));

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
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
package org.apache.jmeter.gui.action;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JFileChooser;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.gui.util.FileDialoger;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/****************************************
 * Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author    Michael Stover
 *@created   February 13, 2001
 *@version   1.0
 ***************************************/

public class Save implements Command
{
	transient private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(
			"jmeter.gui");
	public final static String SAVE_ALL = "save_all";
	public final static String SAVE = "save_as";
	public final static String SAVE_TO_PREVIOUS = "save";
	private String chosenFile;
	private String testPlanFile;

	private static Set commands = new HashSet();
	static
	{
		commands.add(SAVE);
		commands.add(SAVE_ALL);
		commands.add(SAVE_TO_PREVIOUS);
	}


	/****************************************
	 * Constructor for the Save object
	 ***************************************/
	public Save() { }


	/****************************************
	 * Gets the ActionNames attribute of the Save object
	 *
	 *@return   The ActionNames value
	 ***************************************/
	public Set getActionNames()
	{
		return commands;
	}
	
	public void setTestPlanFile(String f)
	{
		testPlanFile = f;
	}


	/****************************************
	 * Description of the Method
	 *
	 *@param e  Description of Parameter
	 ***************************************/
	public void doAction(ActionEvent e)
	{
		HashTree subTree = null;
		if(e.getActionCommand().equals(SAVE))
		{
			subTree = GuiPackage.getInstance().getCurrentSubTree();
		}
		else if(e.getActionCommand().equals(SAVE_ALL) || e.getActionCommand().equals(SAVE_TO_PREVIOUS))
		{
			subTree = GuiPackage.getInstance().getTreeModel().getTestPlan();
		}
		
		if(!SAVE_TO_PREVIOUS.equals(e.getActionCommand()) || testPlanFile == null)
		{
			JFileChooser chooser = FileDialoger.promptToSaveFile(
					GuiPackage.getInstance().getTreeListener().getCurrentNode().getName() + ".jmx");
			if(chooser == null)
			{
				return;
			}
			if(e.getActionCommand().equals(SAVE_ALL) || e.getActionCommand().equals(SAVE_TO_PREVIOUS))
			{
				testPlanFile = chooser.getSelectedFile().getAbsolutePath();
				chosenFile = testPlanFile;
			}
			else
			{
				chosenFile = chooser.getSelectedFile().getAbsolutePath();
			}
		}
		else
		{
			chosenFile = testPlanFile;
		}

        ActionRouter.getInstance().actionPerformed(new ActionEvent(subTree.clone(),e.getID(),CheckDirty.SUB_TREE_SAVED));
        try
                {
                    convertSubTree(subTree);
                }catch(Exception err)
                {}
		OutputStream writer = null;
		try
		{
			writer = new FileOutputStream(chosenFile);
			SaveService.saveSubTree(subTree,writer);
		}
		catch(Throwable ex)
		{
			log.error("",ex);
		}
		finally
		{
			closeWriter(writer);
			GuiPackage.getInstance().getMainFrame().repaint();
		}
	}

	private void convertSubTree(HashTree tree)
	{
		Iterator iter = new LinkedList(tree.list()).iterator();
		while (iter.hasNext())
		{
			JMeterTreeNode item = (JMeterTreeNode)iter.next();
			convertSubTree(tree.getTree(item));
			TestElement testElement = item.createTestElement();
			tree.replace(item,testElement);
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
			JMeterTreeNode root = new JMeterTreeNode(new Arguments(),null);
			tree.add(root,root);
			tree.getTree(root).add(root,root);
			save.convertSubTree(tree);
			assertEquals(tree.getArray()[0].getClass().getName(),root.createTestElement().getClass().getName());
			tree = tree.getTree(tree.getArray()[0]);
			assertEquals(tree.getArray()[0].getClass().getName(),
					root.createTestElement().getClass().getName());
			assertEquals(tree.getTree(tree.getArray()[0]).getArray()[0].getClass().getName(),
					root.createTestElement().getClass().getName());
		}
	}


	/****************************************
	 * Description of the Method
	 *
	 *@param writer  Description of Parameter
	 ***************************************/
	private void closeWriter(OutputStream writer)
	{
		if(writer != null)
		{
			try
			{
				writer.close();
			}
			catch(Exception ex)
			{
				log.error("",ex);
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2322.java