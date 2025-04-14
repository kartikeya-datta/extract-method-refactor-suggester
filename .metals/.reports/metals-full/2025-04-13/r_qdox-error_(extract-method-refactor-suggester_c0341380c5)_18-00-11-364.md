error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1351.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1351.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1351.java
text:
```scala
J@@EditBuffer buffer)

/*
 * BufferHandler.java
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2001, 2005 Slava Pestov
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

package org.gjt.sp.jedit.textarea;

import java.util.Iterator;
import org.gjt.sp.jedit.buffer.*;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.Debug;
import org.gjt.sp.util.Log;

/**
 * Note that in this class we take great care to defer complicated
 * calculations to the end of the current transaction if the buffer
 * informs us a compound edit is in progress
 * (<code>isTransactionInProgress()</code>).
 *
 * This greatly speeds up replace all for example, by only doing certain
 * things once, particularly in <code>moveCaretPosition()</code>.
 *
 * Try doing a replace all in a large file, for example. It is very slow
 * in 3.2, faster in 4.0 (where the transaction optimization was
 * introduced) and faster still in 4.1 (where it was further improved).
 *
 * There is still work to do; see TODO.txt.
 */
class BufferHandler implements BufferListener
{
	private DisplayManager displayManager;
	private JEditTextArea textArea;
	private JEditBuffer buffer;

	boolean delayedUpdate;
	boolean delayedMultilineUpdate;
	int delayedUpdateStart;
	int delayedUpdateEnd;

	//{{{ BufferChangeHandler constructor
	BufferHandler(DisplayManager displayManager,
		JEditTextArea textArea,
		Buffer buffer)
	{
		this.displayManager = displayManager;
		this.textArea = textArea;
		this.buffer = buffer;
	} //}}}

	//{{{ bufferLoaded() method
	public void bufferLoaded(JEditBuffer buffer)
	{
		displayManager.bufferLoaded();
	} //}}}

	//{{{ foldHandlerChanged() method
	public void foldHandlerChanged(JEditBuffer buffer)
	{
		displayManager.foldHandlerChanged();
	} //}}}

	//{{{ foldLevelChanged() method
	public void foldLevelChanged(JEditBuffer buffer, int start, int end)
	{
		//System.err.println("foldLevelChanged " + (start-1) + " to " + textArea.getLastPhysicalLine() + "," + end);

		if(textArea.getDisplayManager() == displayManager
			&& end != 0 && !buffer.isLoading())
		{
			textArea.invalidateLineRange(start - 1,
				textArea.getLastPhysicalLine());
		}
	} //}}}

	//{{{ contentInserted() method
	public void contentInserted(JEditBuffer buffer, int startLine,
		int offset, int numLines, int length)
	{
		if(buffer.isLoading())
			return;

		displayManager.screenLineMgr.contentInserted(startLine,numLines);

		int endLine = startLine + numLines;

		if(numLines != 0)
			delayedMultilineUpdate = true;

		displayManager.folds.contentInserted(startLine,numLines);

		FirstLine firstLine = displayManager.firstLine;
		ScrollLineCount scrollLineCount = displayManager.scrollLineCount;

		if(textArea.getDisplayManager() == displayManager)
		{
			if(numLines != 0)
			{
				firstLine.contentInserted(startLine,numLines);
				scrollLineCount.contentInserted(startLine,numLines);
			}

			if(delayedUpdateEnd >= startLine)
				delayedUpdateEnd += numLines;
			delayUpdate(startLine,endLine);

			//{{{ resize selections if necessary
			Iterator iter = textArea.getSelectionIterator();
			while(iter.hasNext())
			{
				Selection s = (Selection)iter.next();

				if(s.contentInserted(buffer,startLine,offset,
					numLines,length))
				{
					delayUpdate(s.startLine,s.endLine);
				}
			} //}}}

			int caret = textArea.getCaretPosition();
			if(caret >= offset)
			{
				int scrollMode = (textArea.caretAutoScroll()
					? JEditTextArea.ELECTRIC_SCROLL
					: JEditTextArea.NO_SCROLL);
				textArea.moveCaretPosition(
					caret + length,scrollMode);
			}
			else
			{
				int scrollMode = (textArea.caretAutoScroll()
					? JEditTextArea.NORMAL_SCROLL
					: JEditTextArea.NO_SCROLL);
				textArea.moveCaretPosition(
					caret,scrollMode);
			}
		}
		else
		{
			firstLine.callReset = true;
			scrollLineCount.callReset = true;
		}
	} //}}}

	//{{{ preContentRemoved() method
	public void preContentRemoved(JEditBuffer buffer, int startLine,
		int offset, int numLines, int length)
	{
		if(buffer.isLoading())
			return;

		FirstLine firstLine = displayManager.firstLine;
		ScrollLineCount scrollLineCount = displayManager.scrollLineCount;

		if(textArea.getDisplayManager() == displayManager)
		{
			if(numLines != 0)
			{
				firstLine.preContentRemoved(startLine,numLines);
				scrollLineCount.preContentRemoved(startLine,numLines);
			}

			if(delayedUpdateEnd >= startLine)
				delayedUpdateEnd -= numLines;
			delayUpdate(startLine,startLine);
		}
		else
		{
			firstLine.callReset = true;
			scrollLineCount.callReset = true;
		}

		displayManager.screenLineMgr.contentRemoved(startLine,numLines);

		if(numLines == 0)
			return;

		delayedMultilineUpdate = true;

		if(displayManager.folds.preContentRemoved(startLine,numLines))
		{
			displayManager.folds.reset(buffer.getLineCount());
			firstLine.callReset = true;
			scrollLineCount.callReset = true;
		}

		if(firstLine.physicalLine
			> displayManager.getLastVisibleLine()
 firstLine.physicalLine
			< displayManager.getFirstVisibleLine())
		{
			// will be handled later.
			// see comments at the end of
			// transactionComplete().
		}
		// very subtle... if we leave this for
		// ensurePhysicalLineIsVisible(), an
		// extra line will be added to the
		// scroll line count.
		else if(!displayManager.isLineVisible(
			firstLine.physicalLine))
		{
			firstLine.physicalLine =
				displayManager.getNextVisibleLine(
				firstLine.physicalLine);
		}
	} //}}}

	//{{{ contentRemoved() method
	public void contentRemoved(JEditBuffer buffer, int startLine,
		int start, int numLines, int length)
	{
		if(buffer.isLoading())
			return;

		if(textArea.getDisplayManager() == displayManager)
		{
			//{{{ resize selections if necessary
			Iterator iter = textArea.getSelectionIterator();
			while(iter.hasNext())
			{
				Selection s = (Selection)iter.next();

				if(s.contentRemoved(buffer,startLine,
					start,numLines,length))
				{
					delayUpdate(s.startLine,s.endLine);
					if(s.start == s.end)
						iter.remove();
				}
			} //}}}

			int caret = textArea.getCaretPosition();

			if(caret >= start + length)
			{
				int scrollMode = (textArea.caretAutoScroll()
					? JEditTextArea.ELECTRIC_SCROLL
					: JEditTextArea.NO_SCROLL);
				textArea.moveCaretPosition(
					caret - length,
					scrollMode);
			}
			else if(caret >= start)
			{
				int scrollMode = (textArea.caretAutoScroll()
					? JEditTextArea.ELECTRIC_SCROLL
					: JEditTextArea.NO_SCROLL);
				textArea.moveCaretPosition(
					start,scrollMode);
			}
			else
			{
				int scrollMode = (textArea.caretAutoScroll()
					? JEditTextArea.NORMAL_SCROLL
					: JEditTextArea.NO_SCROLL);
				textArea.moveCaretPosition(caret,scrollMode);
			}
		}
	}
	//}}}

	//{{{ transactionComplete() method
	public void transactionComplete(JEditBuffer buffer)
	{
		if(textArea.getDisplayManager() != displayManager)
		{
			delayedUpdate = false;
			return;
		}

		if(delayedUpdate)
			doDelayedUpdate();

		textArea._finishCaretUpdate();

		delayedUpdate = false;

		//{{{ Debug code
		if(Debug.SCROLL_VERIFY)
		{
			int scrollLineCount = 0;
			int line = delayedUpdateStart;
			if(!displayManager.isLineVisible(line))
				line = displayManager.getNextVisibleLine(line);
			System.err.println(delayedUpdateStart + ":" + delayedUpdateEnd + ":" + textArea.getLineCount());
			while(line != -1 && line <= delayedUpdateEnd)
			{
				scrollLineCount += displayManager.getScreenLineCount(line);
				line = displayManager.getNextVisibleLine(line);
			}

			if(scrollLineCount != displayManager.getScrollLineCount())
			{
				throw new InternalError(scrollLineCount
					+ " != "
					+ displayManager.getScrollLineCount());
			}
		} //}}}
	} //}}}

	//{{{ doDelayedUpdate() method
	private void doDelayedUpdate()
	{
		// must update screen line counts before we call
		// notifyScreenLineChanges() since that calls
		// updateScrollBar() which needs valid info
		int line = delayedUpdateStart;
		if(!displayManager.isLineVisible(line))
			line = displayManager.getNextVisibleLine(line);
		while(line != -1 && line <= delayedUpdateEnd)
		{
			displayManager.updateScreenLineCount(line);
			line = displayManager.getNextVisibleLine(line);
		}

		// must be before the below call
		// so that the chunk cache is not
		// updated with an invisible first
		// line (see above)
		displayManager.notifyScreenLineChanges();

		if(delayedMultilineUpdate)
		{
			textArea.invalidateScreenLineRange(
				textArea.chunkCache
				.getScreenLineOfOffset(
				delayedUpdateStart,0),
				textArea.getVisibleLines());
			delayedMultilineUpdate = false;
		}
		else
		{
			textArea.invalidateLineRange(
				delayedUpdateStart,
				delayedUpdateEnd);
		}

		// update visible lines
		int visibleLines = textArea.getVisibleLines();
		if(visibleLines != 0)
		{
			textArea.chunkCache.getLineInfo(
				visibleLines - 1);
		}

		// force the fold levels to be
		// updated.

		// when painting the last line of
		// a buffer, Buffer.isFoldStart()
		// doesn't call getFoldLevel(),
		// hence the foldLevelChanged()
		// event might not be sent for the
		// previous line.

		buffer.getFoldLevel(delayedUpdateEnd);
	} //}}}

	//{{{ delayUpdate() method
	private void delayUpdate(int startLine, int endLine)
	{
		textArea.chunkCache.invalidateChunksFromPhys(startLine);
		textArea.repaintMgr.setFastScroll(false);

		if(!delayedUpdate)
		{
			delayedUpdateStart = startLine;
			delayedUpdateEnd = endLine;
			delayedUpdate = true;
		}
		else
		{
			delayedUpdateStart = Math.min(
				delayedUpdateStart,
				startLine);
			delayedUpdateEnd = Math.max(
				delayedUpdateEnd,
				endLine);
		}
	} //}}}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1351.java