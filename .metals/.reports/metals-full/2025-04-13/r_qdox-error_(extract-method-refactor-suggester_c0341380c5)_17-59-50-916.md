error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4690.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4690.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4690.java
text:
```scala
v@@iew.processKeyEvent(evt);

/*
 * ActionBar.java - For invoking actions directly
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2003 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.gjt.sp.jedit.gui;

//{{{ Imports
import bsh.NameSpace;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.event.*;
import javax.swing.*;
import org.gjt.sp.jedit.*;
//}}}

/**
 * Action invocation bar.
 */
public class ActionBar extends JPanel
{
	//{{{ ActionBar constructor
	public ActionBar(final View view, boolean temp)
	{
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));

		this.view = view;
		this.temp = temp;

		add(Box.createHorizontalStrut(2));

		JLabel label = new JLabel(jEdit.getProperty("view.action.prompt"));
		add(label);
		add(Box.createHorizontalStrut(12));
		add(action = new ActionTextField());
		action.setEnterAddsToHistory(false);
		Dimension max = action.getPreferredSize();
		max.width = Integer.MAX_VALUE;
		action.setMaximumSize(max);
		action.addActionListener(new ActionHandler());
		action.getDocument().addDocumentListener(new DocumentHandler());

		if(temp)
		{
			close = new RolloverButton(GUIUtilities.loadIcon("closebox.gif"));
			close.addActionListener(new ActionHandler());
			close.setToolTipText(jEdit.getProperty(
				"view.action.close-tooltip"));
			add(close);
		}

		// if 'temp' is true, hide search bar after user is done with it
		this.temp = temp;
	} //}}}

	//{{{ getField() method
	public HistoryTextField getField()
	{
		return action;
	} //}}}

	//{{{ goToActionBar() method
	public void goToActionBar()
	{
		repeatCount = view.getInputHandler().getRepeatCount();
		action.setText(null);
		action.requestFocus();
	} //}}}

	//{{{ actionListChanged()
	/**
	 * Called when plugins are added or removed to notify the action bar
	 * that the action list has changed.
	 * @since jEdit 4.2pre2
	 */
	public void actionListChanged()
	{
		actions = null;
	} //}}}

	//{{{ Private members

	private static NameSpace namespace = new NameSpace(
		BeanShell.getNameSpace(),"action bar namespace");

	//{{{ Instance variables
	private View view;
	private boolean temp;
	private int repeatCount;
	private HistoryTextField action;
	private CompletionPopup popup;
	private RolloverButton close;
	private String[] actions;
	//}}}

	//{{{ initActions() method
	private void initActions()
	{
		if(actions != null)
			return;

		actions = jEdit.getActionNames();
		Arrays.sort(actions,new MiscUtilities.StringICaseCompare());
	} //}}}

	//{{{ invoke() method
	private void invoke()
	{
		String cmd;
		if(popup != null)
			cmd = popup.list.getSelectedValue().toString();
		else
		{
			cmd = action.getText().trim();
			int index = cmd.indexOf('=');
			if(index != -1)
			{
				action.addCurrentToHistory();
				String propName = cmd.substring(0,index).trim();
				String propValue = cmd.substring(index + 1).trim();
				String code;
				/* construct a BeanShell snippet instead of
				 * invoking directly so that user can record
				 * property changes in macros. */
				if(propName.startsWith("buffer."))
				{
					if(propName.equals("buffer.mode"))
					{
						code = "buffer.setMode(\""
							+ MiscUtilities.charsToEscapes(
							propValue) + "\");";
					}
					else
					{
						code = "buffer.setStringProperty(\""
							+ MiscUtilities.charsToEscapes(
							propName.substring("buffer.".length())
							) + "\",\""
							+ MiscUtilities.charsToEscapes(
							propValue) + "\");";
					}

					code = code + "\nbuffer.propertiesChanged();";
				}
				else if(propName.startsWith("!buffer."))
				{
					code = "jEdit.setProperty(\""
						+ MiscUtilities.charsToEscapes(
						propName.substring(1)) + "\",\""
						+ MiscUtilities.charsToEscapes(
						propValue) + "\");\n"
						+ "jEdit.propertiesChanged();";
				}
				else
				{
					code = "jEdit.setProperty(\""
						+ MiscUtilities.charsToEscapes(
						propName) + "\",\""
						+ MiscUtilities.charsToEscapes(
						propValue) + "\");\n"
						+ "jEdit.propertiesChanged();"
						+ "EditBus.send(new DockableWindowUpdate(wm,"
						+ "DockableWindowUpdate."
						+ "PROPERTIES_CHANGED,null));";
				}

				Macros.Recorder recorder = view.getMacroRecorder();
				if(recorder != null)
					recorder.record(code);
				BeanShell.eval(view,namespace,code);
				cmd = null;
			}
			else if(cmd.length() != 0)
			{
				String[] completions = getCompletions(cmd);
				if(completions.length != 0)
				{
					cmd = completions[0];
				}
			}
			else
				cmd = null;
		}

		if(popup != null)
		{
			popup.dispose();
			popup = null;
		}

		final String finalCmd = cmd;
		final EditAction act = (finalCmd == null ? null : jEdit.getAction(finalCmd));
		if(temp)
			view.removeToolBar(ActionBar.this);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				view.getTextArea().requestFocus();
				if(act == null)
				{
					if(finalCmd != null)
					{
						view.getStatus().setMessageAndClear(
							jEdit.getProperty(
							"view.action.no-completions"));
					}
				}
				else
				{
					view.getInputHandler().setRepeatCount(repeatCount);
					view.getInputHandler().invokeAction(act);
				}
			}
		});
	} //}}}

	//{{{ getCompletions() method
	private String[] getCompletions(String str)
	{
		initActions();

		str = str.toLowerCase();
		ArrayList returnValue = new ArrayList(actions.length);
		for(int i = 0; i < actions.length; i++)
		{
			if(actions[i].toLowerCase().indexOf(str) != -1)
				returnValue.add(actions[i]);
		}

		return (String[])returnValue.toArray(new String[returnValue.size()]);
	} //}}}

	//{{{ complete() method
	private void complete(boolean insertLongestPrefix)
	{
		String text = action.getText().trim();
		String[] completions = getCompletions(text);
		if(completions.length == 1)
		{
			if(insertLongestPrefix)
				action.setText(completions[0]);
		}
		else if(completions.length != 0)
		{
			if(insertLongestPrefix)
			{
				String prefix = MiscUtilities.getLongestPrefix(
					completions,true);
				if(prefix.indexOf(text) != -1)
					action.setText(prefix);
			}

			if(popup != null)
				popup.setModel(completions);
			else
				popup = new CompletionPopup(completions);
			return;
		}

		if(popup != null)
		{
			popup.dispose();
			popup = null;
		}
	} //}}}

	//}}}

	//{{{ Inner classes

	//{{{ ActionHandler class
	class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			if(evt.getSource() == close)
				view.removeToolBar(ActionBar.this);
			else
				invoke();
		}
	} //}}}

	//{{{ DocumentHandler class
	class DocumentHandler implements DocumentListener
	{
		//{{{ insertUpdate() method
		public void insertUpdate(DocumentEvent evt)
		{
			if(popup != null)
				complete(false);
		} //}}}

		//{{{ removeUpdate() method
		public void removeUpdate(DocumentEvent evt)
		{
			if(popup != null)
				complete(false);
		} //}}}

		//{{{ changedUpdate() method
		public void changedUpdate(DocumentEvent evt) {}
		//}}}
	} //}}}

	//{{{ ActionTextField class
	class ActionTextField extends HistoryTextField
	{
		boolean repeat;
		boolean nonDigit;

		ActionTextField()
		{
			super("action");
			setSelectAllOnFocus(true);
		}

		public boolean isManagingFocus()
		{
			return false;
		}

		public boolean getFocusTraversalKeysEnabled()
		{
			return false;
		}

		public void processKeyEvent(KeyEvent evt)
		{
			evt = KeyEventWorkaround.processKeyEvent(evt);
			if(evt == null)
				return;

			switch(evt.getID())
			{
			case KeyEvent.KEY_TYPED:
				char ch = evt.getKeyChar();
				if(!nonDigit && Character.isDigit(ch))
				{
					super.processKeyEvent(evt);
					repeat = true;
					repeatCount = Integer.parseInt(action.getText());
				}
				else
				{
					nonDigit = true;
					if(repeat)
						passToView(evt);
					else
						super.processKeyEvent(evt);
				}
				break;
			case KeyEvent.KEY_PRESSED:
				int keyCode = evt.getKeyCode();
				if(evt.isActionKey()
 evt.isControlDown()
 evt.isAltDown()
 evt.isMetaDown()
 keyCode == KeyEvent.VK_BACK_SPACE
 keyCode == KeyEvent.VK_DELETE
 keyCode == KeyEvent.VK_ENTER
 keyCode == KeyEvent.VK_TAB
 keyCode == KeyEvent.VK_ESCAPE)
				{
					nonDigit = true;
					if(repeat)
					{
						passToView(evt);
						break;
					}
					else if(keyCode == KeyEvent.VK_TAB)
					{
						complete(true);
						evt.consume();
					}
					else if(keyCode == KeyEvent.VK_ESCAPE)
					{
						evt.consume();
						if(popup != null)
						{
							popup.dispose();
							popup = null;
							action.requestFocus();
						}
						else
						{
							if(temp)
								view.removeToolBar(ActionBar.this);
							view.getEditPane().focusOnTextArea();
						}
						break;
					}
					else if((keyCode == KeyEvent.VK_UP
 keyCode == KeyEvent.VK_DOWN)
						&& popup != null)
					{
						popup.list.processKeyEvent(evt);
						break;
					}
				}
				super.processKeyEvent(evt);
				break;
			}
		}

		private void passToView(final KeyEvent evt)
		{
			if(temp)
				view.removeToolBar(ActionBar.this);
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					view.getTextArea().requestFocus();
					view.getInputHandler().setRepeatCount(repeatCount);
					view.getInputHandler().processKeyEvent(evt);
				}
			});
		}

		public void addNotify()
		{
			super.addNotify();
			repeat = nonDigit = false;
		}
	} //}}}

	//{{{ CompletionPopup class
	class CompletionPopup extends JWindow
	{
		CompletionList list;

		//{{{ CompletionPopup constructor
		CompletionPopup(String[] actions)
		{
			super(view);

			setContentPane(new JPanel(new BorderLayout())
			{
				/**
				 * Returns if this component can be traversed by pressing the
				 * Tab key. This returns false.
				 */
				public boolean isManagingFocus()
				{
					return false;
				}

				/**
				 * Makes the tab key work in Java 1.4.
				 */
				public boolean getFocusTraversalKeysEnabled()
				{
					return false;
				}
			});

			list = new CompletionList(actions);
			list.setVisibleRowCount(8);
			list.addMouseListener(new MouseHandler());
			list.setSelectedIndex(0);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			// stupid scrollbar policy is an attempt to work around
			// bugs people have been seeing with IBM's JDK -- 7 Sep 2000
			JScrollPane scroller = new JScrollPane(list,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			getContentPane().add(scroller, BorderLayout.CENTER);

			GUIUtilities.requestFocus(this,list);

			pack();
			Point p = new Point(0,-getHeight());
			SwingUtilities.convertPointToScreen(p,action);
			setLocation(p);
			show();

			KeyHandler keyHandler = new KeyHandler();
			addKeyListener(keyHandler);
			list.addKeyListener(keyHandler);
		} //}}}

		//{{{ setModel() method
		void setModel(String[] actions)
		{
			list.setListData(actions);
			list.setSelectedIndex(0);
		} //}}}

		//{{{ MouseHandler class
		class MouseHandler extends MouseAdapter
		{
			public void mouseClicked(MouseEvent evt)
			{
				invoke();
			}
		} //}}}

		//{{{ CompletionList class
		class CompletionList extends JList
		{
			CompletionList(Object[] data)
			{
				super(data);
			}

			// we need this public not protected
			public void processKeyEvent(KeyEvent evt)
			{
				super.processKeyEvent(evt);
			}
		} //}}}

		//{{{ KeyHandler class
		class KeyHandler extends KeyAdapter
		{
			public void keyTyped(KeyEvent evt)
			{
				action.processKeyEvent(evt);
			}

			public void keyPressed(KeyEvent evt)
			{
				int keyCode = evt.getKeyCode();
				if(keyCode == KeyEvent.VK_ESCAPE)
					action.processKeyEvent(evt);
				else if(keyCode == KeyEvent.VK_ENTER)
					invoke();
				else if(keyCode == KeyEvent.VK_UP)
				{
					int selected = list.getSelectedIndex();
					if(selected == 0)
					{
						list.setSelectedIndex(
							list.getModel().getSize()
							- 1);
						evt.consume();
					}
				}
				else if(keyCode == KeyEvent.VK_DOWN)
				{
					int selected = list.getSelectedIndex();
					if(selected == list.getModel().getSize() - 1)
					{
						list.setSelectedIndex(0);
						evt.consume();
					}
				}
			}
		} //}}}
	} //}}}

	//}}}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4690.java