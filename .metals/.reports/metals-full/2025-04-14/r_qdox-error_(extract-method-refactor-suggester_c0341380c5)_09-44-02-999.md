error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1456.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1456.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1456.java
text:
```scala
n@@ew SegmentCharSequence(text,false),

/*
 * HyperSearchRequest.java - HyperSearch request, run in I/O thread
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 1998, 1999, 2000, 2001, 2002 Slava Pestov
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
import javax.swing.text.Segment;
import javax.swing.tree.*;
import javax.swing.SwingUtilities;
import org.gjt.sp.jedit.textarea.Selection;
import org.gjt.sp.jedit.io.VFSManager;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.View;
import org.gjt.sp.util.*;
//}}}

class HyperSearchRequest extends WorkRequest
{
	//{{{ HyperSearchRequest constructor
	public HyperSearchRequest(View view, SearchMatcher matcher,
		HyperSearchResults results, Selection[] selection)
	{
		this.view = view;
		this.matcher = matcher;

		this.results = results;
		this.searchString = SearchAndReplace.getSearchString();
		this.rootSearchNode = new DefaultMutableTreeNode(new HyperSearchOperationNode(searchString));

		this.selection = selection;
	} //}}}

	//{{{ run() method
	public void run()
	{
		setStatus(jEdit.getProperty("hypersearch-status"));

		SearchFileSet fileset = SearchAndReplace.getSearchFileSet();
		String[] files = fileset.getFiles(view);
		if(files == null || files.length == 0)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					GUIUtilities.error(view,"empty-fileset",null);
					results.searchDone(rootSearchNode);
				}
			});
			return;
		}

		setMaximum(fileset.getFileCount(view));

		// to minimise synchronization and stuff like that, we only
		// show a status message at most twice a second

		// initially zero, so that we always show the first message
		long lastStatusTime = 0;
		String searchingCaption = jEdit.getProperty("hypersearch-results.searching") + ' ';
		try
		{
			if(selection != null)
			{
				Buffer buffer = view.getBuffer();

				searchInSelection(buffer);
			}
			else
			{
				int current = 0;

loop:				for(int i = 0; i < files.length; i++)
				{
					String file = files[i];
					current++;

					long currentTime = System.currentTimeMillis();
					if(currentTime - lastStatusTime > 250)
					{
						setValue(current);
						lastStatusTime = currentTime;
						results.setSearchStatus(searchingCaption + file);
					}

					Buffer buffer = jEdit.openTemporary(null,null,file,false);
					if(buffer == null)
						continue loop;

					doHyperSearch(buffer);
				}
			}
		}
		catch(final Exception e)
		{
			Log.log(Log.ERROR,this,e);
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					SearchAndReplace.handleError(view,e);
				}
			});
		}
		catch(WorkThread.Abort a)
		{
		}
		finally
		{
			VFSManager.runInAWTThread(new Runnable()
			{
				public void run()
				{
					results.searchDone(rootSearchNode);
				}
			});
		}
	} //}}}

	//{{{ Private members

	//{{{ Instance variables
	private View view;
	private SearchMatcher matcher;
	private HyperSearchResults results;
	private DefaultMutableTreeNode rootSearchNode;
	private Selection[] selection;
	private String searchString;
	//}}}

	//{{{ searchInSelection() method
	private int searchInSelection(Buffer buffer) throws Exception
	{
		setAbortable(false);

		int resultCount = 0;

		try
		{
			buffer.readLock();

			for(int i = 0; i < selection.length; i++)
			{
				Selection s = selection[i];
				if(s instanceof Selection.Rect)
				{
					for(int j = s.getStartLine();
						j <= s.getEndLine(); j++)
					{
						resultCount += doHyperSearch(buffer,
							s.getStart(buffer,j),
							s.getEnd(buffer,j));
					}
				}
				else
				{
					resultCount += doHyperSearch(buffer,
						s.getStart(),s.getEnd());
				}
			}
		}
		finally
		{
			buffer.readUnlock();
		}

		setAbortable(true);

		return resultCount;
	} //}}}

	//{{{ doHyperSearch() method
	private int doHyperSearch(Buffer buffer)
		throws Exception
	{
		return doHyperSearch(buffer, 0, buffer.getLength());
	} //}}}

	//{{{ doHyperSearch() method
	private int doHyperSearch(Buffer buffer, int start, int end)
		throws Exception
	{
		setAbortable(false);

		final DefaultMutableTreeNode bufferNode = new DefaultMutableTreeNode(
			new HyperSearchFileNode(buffer.getPath()));

		int resultCount = doHyperSearch(buffer,start,end,bufferNode);

		if(resultCount != 0)
			rootSearchNode.insert(bufferNode,rootSearchNode.getChildCount());

		setAbortable(true);

		return resultCount;
	} //}}}

	//{{{ doHyperSearch() method
	private int doHyperSearch(Buffer buffer, int start, int end,
		DefaultMutableTreeNode bufferNode)
	{
		int resultCount = 0;

		try
		{
			buffer.readLock();

			boolean endOfLine = (buffer.getLineEndOffset(
				buffer.getLineOfOffset(end)) - 1 == end);

			Segment text = new Segment();
			int offset = start;

			HyperSearchResult lastResult = null;

loop:			for(int counter = 0; ; counter++)
			{
				boolean startOfLine = (buffer.getLineStartOffset(
					buffer.getLineOfOffset(offset)) == offset);

				buffer.getText(offset,end - offset,text);
				SearchMatcher.Match match = matcher.nextMatch(
					new CharIndexedSegment(text,false),
					startOfLine,endOfLine,counter == 0,
					false);
				if(match == null)
					break loop;

				int newLine = buffer.getLineOfOffset(
					offset + match.start);
				if(lastResult == null || lastResult.line != newLine)
				{
					lastResult = new HyperSearchResult(
						buffer,newLine);
					bufferNode.add(
						new DefaultMutableTreeNode(
						lastResult,false));
				}

				lastResult.addOccur(offset + match.start,
					offset + match.end);

				offset += match.end;

				resultCount++;
			}
		}
		finally
		{
			buffer.readUnlock();
		}

		return resultCount;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1456.java