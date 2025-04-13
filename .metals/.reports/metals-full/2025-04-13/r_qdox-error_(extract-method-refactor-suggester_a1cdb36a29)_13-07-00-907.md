error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1472.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1472.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1472.java
text:
```scala
transient private static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor(

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
package org.apache.jmeter.gui.tree;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.JMeterGUIComponent;
import org.apache.jmeter.gui.MainFrame;
import org.apache.jmeter.gui.action.DragNDrop;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/****************************************
 * Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author    Michael Stover
 *@created   March 11, 2001
 *@version   1.0
 ***************************************/

public class JMeterTreeListener implements TreeSelectionListener, MouseListener, 
		KeyListener,MouseMotionListener
{
	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(
			"jmeter.gui");
	Container endWindow;
	JPopupMenu pop;
	TreePath currentPath;
	ActionListener actionHandler;

	private JMeterTreeModel model;
	private JTree tree;
	private boolean dragging = false;
	private JMeterTreeNode draggedNode;
	private JLabel dragIcon = new JLabel(JMeterUtils.getImage("leafnode.gif"));

	/****************************************
	 * Constructor for the JMeterTreeListener object
	 *
	 *@param model  Description of Parameter
	 ***************************************/
	public JMeterTreeListener(JMeterTreeModel model)
	{
		this.model = model;
		dragIcon.validate();
		dragIcon.setVisible(true);
	}
	
	public JMeterTreeListener()
	{
		dragIcon.validate();
		dragIcon.setVisible(true);
	}
	
	public void setModel(JMeterTreeModel m)
	{
		model = m;
	}


	/****************************************
	 * Sets the ActionHandler attribute of the JMeterTreeListener object
	 *
	 *@param ah  The new ActionHandler value
	 ***************************************/
	public void setActionHandler(ActionListener ah)
	{
		actionHandler = ah;
	}


	/****************************************
	 * Sets the JTree attribute of the JMeterTreeListener object
	 *
	 *@param tree  The new JTree value
	 ***************************************/
	public void setJTree(JTree tree)
	{
		this.tree = tree;
	}


	/****************************************
	 * Sets the EndWindow attribute of the JMeterTreeListener object
	 *
	 *@param window  The new EndWindow value
	 ***************************************/
	public void setEndWindow(Container window)
	{
		endWindow = window;
	}
	
	/****************************************
	 * Gets the JTree attribute of the JMeterTreeListener object.
	 *
	 *@return tree The current JTree value.
	 ***************************************/
	public JTree getJTree()
	{
		return tree;
	}


	/****************************************
	 * Gets the CurrentNode attribute of the JMeterTreeListener object
	 *
	 *@return   The CurrentNode value
	 ***************************************/
	public JMeterTreeNode getCurrentNode()
	{
		if(currentPath != null)
		{
			if(currentPath.getLastPathComponent() != null)
			{
				return (JMeterTreeNode)currentPath.getLastPathComponent();
			}
			else
			{
				return (JMeterTreeNode)currentPath.getParentPath().getLastPathComponent();
			}
		}
		else
		{
			return (JMeterTreeNode)model.getRoot();
		}
	}

	/****************************************
	 * !ToDoo (Method description)
	 *
	 *@return   !ToDo (Return description)
	 ***************************************/
	public JMeterTreeNode[] getSelectedNodes()
	{
		TreePath[] paths = tree.getSelectionPaths();
		if(paths == null)
		{
			return new JMeterTreeNode[]{getCurrentNode()};
		}
		JMeterTreeNode[] nodes = new JMeterTreeNode[paths.length];
		for(int i = 0; i < paths.length; i++)
		{
			nodes[i] = (JMeterTreeNode)paths[i].getLastPathComponent();
		}

		return nodes;
	}

	/****************************************
	 * !ToDo (Method description)
	 ***************************************/
	public void removedSelectedNode()
	{
		currentPath = currentPath.getParentPath();
	}


	/****************************************
	 * Description of the Method
	 *
	 *@param e  Description of Parameter
	 ***************************************/
	public void valueChanged(TreeSelectionEvent e)
	{
		currentPath = e.getNewLeadSelectionPath();
		actionHandler.actionPerformed(new ActionEvent(this, 3333, "edit"));
	}


	/****************************************
	 * Description of the Method
	 *
	 *@param ev  Description of Parameter
	 ***************************************/
	public void mouseClicked(MouseEvent ev) { }


	/****************************************
	 * Description of the Method
	 *
	 *@param e  Description of Parameter
	 ***************************************/
	public void mouseReleased(MouseEvent e) 
	{ 
		if(dragging && draggedNode != getCurrentNode())
		{
			dragging = false;
			JPopupMenu dragNdrop = new JPopupMenu();
			JMenuItem item = new JMenuItem(JMeterUtils.getResString("Insert Before"));
			item.addActionListener(actionHandler);
			item.setActionCommand(DragNDrop.INSERT_BEFORE);
			dragNdrop.add(item);
			item = new JMenuItem(JMeterUtils.getResString("Insert After"));
			item.addActionListener(actionHandler);
			item.setActionCommand(DragNDrop.INSERT_AFTER);
			dragNdrop.add(item);
			item = new JMenuItem(JMeterUtils.getResString("Add as Child"));
			item.addActionListener(actionHandler);
			item.setActionCommand(DragNDrop.ADD);
			dragNdrop.add(item);
			dragNdrop.addSeparator();
			item = new JMenuItem(JMeterUtils.getResString("Cancel"));
			dragNdrop.add(item);
			displayPopUp(e,dragNdrop);
		}
		dragging = false;
	}
	
	public JMeterTreeNode getDraggedNode()
	{
		return draggedNode;
	}


	/****************************************
	 * Description of the Method
	 *
	 *@param ev  Description of Parameter
	 ***************************************/
	public void mouseEntered(MouseEvent e) 
	{ 
	}
	
	private void changeSelectionIfDragging(MouseEvent e)
	{
		if(dragging)
		{	
			GuiPackage.getInstance().getMainFrame().drawDraggedComponent(dragIcon,e.getX(),e.getY());
			if(tree.getPathForLocation(e.getX(), e.getY()) != null)
			{
				currentPath = tree.getPathForLocation(e.getX(), e.getY());
				if(getCurrentNode() != draggedNode)
				{
					tree.setSelectionPath(currentPath);
				}
			}
		}
	}


	/****************************************
	 * Description of the Method
	 *
	 *@param e  Description of Parameter
	 ***************************************/
	public void mousePressed(MouseEvent e)
	{
		// Get the Main Frame.
		MainFrame mainFrame = GuiPackage.getInstance().getMainFrame();

		// Close any Main Menu that is open
		mainFrame.closeMenu();
		int selRow = tree.getRowForLocation(e.getX(), e.getY());
		if(tree.getPathForLocation(e.getX(), e.getY()) != null)
		{
			currentPath = tree.getPathForLocation(e.getX(), e.getY());
		}
		if(selRow != -1)
		{
			//updateMainMenu(((JMeterGUIComponent)getCurrentNode().getUserObject()).createPopupMenu());
			if(isRightClick(e))
			{
				if(tree.getSelectionCount() < 2)
				{
					tree.setSelectionPath(currentPath);
				}
				if(getCurrentNode().getUserObject() instanceof JMeterGUIComponent)
				{
					displayPopUp(e);
				}
			}
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{
		if(!dragging)
		{
			dragging = true;
			draggedNode = getCurrentNode();
			
		}
		changeSelectionIfDragging(e);
	}
	
	public void mouseMoved(MouseEvent e)
	{
	}


	/****************************************
	 * Description of the Method
	 *
	 *@param ev  Description of Parameter
	 ***************************************/
	public void mouseExited(MouseEvent ev) { }

	/****************************************
	 * Description of the Method
	 *
	 *@param e  Description of Parameter
	 ***************************************/
	public void keyPressed(KeyEvent e) { }

	/****************************************
	 * Description of the Method
	 *
	 *@param e  Description of Parameter
	 ***************************************/
	public void keyReleased(KeyEvent e) { }

	/****************************************
	 * Description of the Method
	 *
	 *@param e  Description of Parameter
	 ***************************************/
	public void keyTyped(KeyEvent e) { }


	/****************************************
	 * Description of the Method
	 *
	 *@param e  Description of Parameter
	 *@return   Description of the Returned Value
	 ***************************************/
	private boolean isRightClick(MouseEvent e)
	{
		return (MouseEvent.BUTTON2_MASK & e.getModifiers()) > 0 ||
				(MouseEvent.BUTTON3_MASK == e.getModifiers());
	}


	/****************************************
	 * Description of the Method
	 *
	 *@param addMenu  Description of Parameter
	 ***************************************/
	private void updateMainMenu(JPopupMenu menu)
	{
		try
		{
			Object model = getCurrentNode().getUserObject();
			MainFrame mainFrame = GuiPackage.getInstance().getMainFrame();
			mainFrame.setEditMenu(menu);
		}
		catch(NullPointerException e)
		{
			log.error("Null pointer: JMeterTreeListener.updateMenuItem()",e);
			log.error("",e);
		}
	}

	private void displayPopUp(MouseEvent e)
	{
		JPopupMenu pop = ((JMeterGUIComponent)getCurrentNode().getUserObject()).createPopupMenu();
		displayPopUp(e,pop);
	}
	
	private void displayPopUp(MouseEvent e,JPopupMenu popup)
	{
		if(popup != null)
		{
			popup.pack();
			popup.show(tree, e.getX(), e.getY());
			popup.setVisible(true);
			popup.requestFocus();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1472.java