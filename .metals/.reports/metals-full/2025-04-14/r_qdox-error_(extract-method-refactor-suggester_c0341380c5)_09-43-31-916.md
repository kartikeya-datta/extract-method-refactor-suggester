error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3910.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3910.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3910.java
text:
```scala
i@@f(SearchAndReplace.find(view,view.getBuffer(),start,false,reverse))

/*
 * SearchBar.java - Search & replace toolbar
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2000, 2001, 2002 Slava Pestov
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

package org.gjt.sp.jedit.search;

//{{{ Imports
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.gui.*;
import org.gjt.sp.jedit.textarea.*;
import org.gjt.sp.util.Log;
//}}}

public class SearchBar extends JPanel
{
	//{{{ SearchBar constructor
	public SearchBar(final View view, boolean temp)
	{
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));

		this.view = view;

		add(Box.createHorizontalStrut(2));

		JLabel label = new JLabel(jEdit.getProperty("view.search.find"));
		add(label);
		add(Box.createHorizontalStrut(12));
		add(find = new HistoryTextField("find"));
		find.setSelectAllOnFocus(true);
		Dimension max = find.getPreferredSize();
		max.width = Integer.MAX_VALUE;
		find.setMaximumSize(max);
		ActionHandler actionHandler = new ActionHandler();
		find.addKeyListener(new KeyHandler());
		find.addActionListener(actionHandler);
		find.getDocument().addDocumentListener(new DocumentHandler());

		Insets margin = new Insets(1,1,1,1);

		add(Box.createHorizontalStrut(12));
		add(ignoreCase = new JCheckBox(jEdit.getProperty(
			"search.case")));
		ignoreCase.addActionListener(actionHandler);
		ignoreCase.setMargin(margin);
		ignoreCase.setRequestFocusEnabled(false);
		add(Box.createHorizontalStrut(2));
		add(regexp = new JCheckBox(jEdit.getProperty(
			"search.regexp")));
		regexp.addActionListener(actionHandler);
		regexp.setMargin(margin);
		regexp.setRequestFocusEnabled(false);
		add(Box.createHorizontalStrut(2));
		add(hyperSearch = new JCheckBox(jEdit.getProperty(
			"search.hypersearch")));
		hyperSearch.addActionListener(actionHandler);
		hyperSearch.setMargin(margin);
		hyperSearch.setRequestFocusEnabled(false);

		if(temp)
		{
			close = new RolloverButton(new ImageIcon(
				getClass().getResource(
				"/org/gjt/sp/jedit/icons/closebox.gif")));
			close.addActionListener(actionHandler);
			close.setToolTipText(jEdit.getProperty(
				"view.search.close-tooltip"));
			add(close);
		}

		update();

		//{{{ Create the timer used by incremental search
		timer = new Timer(0,new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				if(!incrementalSearch(searchStart,searchReverse))
				{
					if(!incrementalSearch(
						(searchReverse
						? view.getBuffer().getLength()
						: 0),searchReverse))
					{
						// not found at all.
						view.getStatus().setMessageAndClear(
							jEdit.getProperty(
							"view.status.search-not-found"));
					}
				}
			}
		}); //}}}

		// if 'temp' is true, hide search bar after user is done with it
		this.temp = temp;
	} //}}}

	//{{{ getField() method
	public HistoryTextField getField()
	{
		return find;
	} //}}}

	//{{{ setHyperSearch() method
	public void setHyperSearch(boolean hyperSearch)
	{
		jEdit.setBooleanProperty("view.search.hypersearch.toggle",hyperSearch);
		this.hyperSearch.setSelected(hyperSearch);
	} //}}}

	//{{{ update() method
	public void update()
	{
		ignoreCase.setSelected(SearchAndReplace.getIgnoreCase());
		regexp.setSelected(SearchAndReplace.getRegexp());
		hyperSearch.setSelected(jEdit.getBooleanProperty(
			"view.search.hypersearch.toggle"));
	} //}}}

	//{{{ Private members

	//{{{ Instance variables
	private View view;
	private HistoryTextField find;
	private JCheckBox ignoreCase, regexp, hyperSearch;
	private Timer timer;

	// close button only there if 'temp' is true
	private RolloverButton close;

	private int searchStart;
	private boolean searchReverse;
	private boolean temp;
	//}}}

	//{{{ find() method
	private void find(boolean reverse)
	{
		timer.stop();

		String text = find.getText();
		//{{{ If nothing entered, show search and replace dialog box
		if(text.length() == 0)
		{
			jEdit.setBooleanProperty("search.hypersearch.toggle",
				hyperSearch.isSelected());
			new SearchDialog(view,null);
		} //}}}
		//{{{ HyperSearch
		else if(hyperSearch.isSelected())
		{
			if(temp)
			{
				view.removeToolBar(SearchBar.this);
			}
                        else
				find.setText(null);

			SearchAndReplace.setSearchString(text);
			SearchAndReplace.setSearchFileSet(new CurrentBufferSet());
			SearchAndReplace.hyperSearch(view);
		} //}}}
		//{{{ Incremental search
		else
		{
			// on enter, start search from end
			// of current match to find next one
			int start;
			JEditTextArea textArea = view.getTextArea();
			Selection s = textArea.getSelectionAtOffset(
				textArea.getCaretPosition());
			if(s == null)
				start = textArea.getCaretPosition();
			else if(reverse)
				start = s.getStart();
			else
				start = s.getEnd();

			if(!incrementalSearch(start,reverse))
			{
				// not found. start from
				// beginning
				if(!incrementalSearch(reverse
					? view.getBuffer().getLength()
					: 0,reverse))
				{
					// not found at all.
					view.getStatus().setMessageAndClear(
						jEdit.getProperty(
						"view.status.search-not-found"));
				}
				else
				{
					// inform user search restarted
					view.getStatus().setMessageAndClear(
						jEdit.getProperty("view.status.auto-wrap"));
					// beep if beep property set
					if(jEdit.getBooleanProperty("search.beepOnSearchAutoWrap"))
					{
						getToolkit().beep();
					}
				}
			}
		} //}}}
	} //}}}

	//{{{ incrementalSearch() method
	private boolean incrementalSearch(int start, boolean reverse)
	{
		/* For example, if the current fileset is a directory,
		 * C+g will find the next match within that fileset.
		 * This can be annoying if you have just done an
		 * incremental search and want the next occurrence
		 * in the current buffer. */
		SearchAndReplace.setSearchFileSet(new CurrentBufferSet());
		SearchAndReplace.setSearchString(find.getText());
		SearchAndReplace.setReverseSearch(reverse);

		try
		{
			if(SearchAndReplace.find(view,view.getBuffer(),start))
				return true;
		}
		catch(Exception e)
		{
			Log.log(Log.DEBUG,this,e);

			// invalid regexp, ignore
			// return true to avoid annoying beeping while
			// typing a re
			return true;
		}

		return false;
	} //}}}

	//{{{ timerIncrementalSearch() method
	private void timerIncrementalSearch(int start, boolean reverse)
	{
		this.searchStart = start;
		this.searchReverse = reverse;

		timer.stop();
		timer.setRepeats(false);
		timer.setInitialDelay(150);
		timer.start();
	} //}}}

	//}}}

	//{{{ Inner classes

	//{{{ ActionHandler class
	class ActionHandler implements ActionListener
	{
		//{{{ actionPerformed() method
		public void actionPerformed(ActionEvent evt)
		{
			Object source = evt.getSource();
			if(evt.getSource() == find)
				find(false);
			else if(evt.getSource() == hyperSearch)
			{
				jEdit.setBooleanProperty("view.search.hypersearch.toggle",
					hyperSearch.isSelected());
				update();
			}
			else if(evt.getSource() == ignoreCase)
			{
				SearchAndReplace.setIgnoreCase(ignoreCase
					.isSelected());
			}
			else if(evt.getSource() == regexp)
			{
				SearchAndReplace.setRegexp(regexp
					.isSelected());
			}
			else if(evt.getSource() == close)
			{
				view.removeToolBar(SearchBar.this);
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						view.getEditPane().focusOnTextArea();
					}
				});
			}
		} //}}}
	} //}}}

	//{{{ DocumentHandler class
	class DocumentHandler implements DocumentListener
	{
		//{{{ insertUpdate() method
		public void insertUpdate(DocumentEvent evt)
		{
			// on insert, start search from beginning of
			// current match. This will continue to highlight
			// the current match until another match is found
			if(!hyperSearch.isSelected())
			{
				int start;
				JEditTextArea textArea = view.getTextArea();
				Selection s = textArea.getSelectionAtOffset(
					textArea.getCaretPosition());
				if(s == null)
					start = textArea.getCaretPosition();
				else
					start = s.getStart();

				timerIncrementalSearch(start,false);
			}
		} //}}}

		//{{{ removeUpdate() method
		public void removeUpdate(DocumentEvent evt)
		{
			// on backspace, restart from beginning
			if(!hyperSearch.isSelected())
			{
				String text = find.getText();
				if(text.length() != 0)
				{
					// don't beep if not found.
					// subsequent beeps are very
					// annoying when backspacing an
					// invalid search string.
					if(regexp.isSelected())
					{
						// reverse regexp search
						// not supported yet, so
						// 'simulate' with restart
						timerIncrementalSearch(0,false);
					}
					else
					{
						int start;
						JEditTextArea textArea = view.getTextArea();
						Selection s = textArea.getSelectionAtOffset(
							textArea.getCaretPosition());
						if(s == null)
							start = textArea.getCaretPosition();
						else
							start = s.getStart();
						timerIncrementalSearch(start,true);
					}
				}
			}
		} //}}}

		//{{{ changedUpdate() method
		public void changedUpdate(DocumentEvent evt) {}
		//}}}
	} //}}}

	//{{{ KeyHandler class
	class KeyHandler extends KeyAdapter
	{
		public void keyPressed(KeyEvent evt)
		{
			switch(evt.getKeyCode())
			{
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_RIGHT:
				if(!hyperSearch.isSelected())
				{
					if(temp)
					{
						view.removeToolBar(SearchBar.this);
					}

					evt.consume();
					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
							view.getEditPane().focusOnTextArea();
						}
					});
					view.getEditPane().getTextArea()
						.processKeyEvent(evt);
				}
				break;
			case KeyEvent.VK_ESCAPE:
				if(temp)
				{
					view.removeToolBar(SearchBar.this);
				}
				evt.consume();
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						view.getEditPane().focusOnTextArea();
					}
				});
				break;
			case KeyEvent.VK_ENTER:
				if(evt.isShiftDown())
				{
					evt.consume();
					// reverse search with regexps not
					// supported yet
					find(regexp.isSelected() ? false : true);
				}
				break;
			}
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3910.java