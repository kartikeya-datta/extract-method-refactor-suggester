error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8880.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8880.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8880.java
text:
```scala
i@@f(buffer != null && !buffer.isTemporary())

/*
 * HyperSearchResult.java - HyperSearch result
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 1998, 2003 Slava Pestov
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
import javax.swing.text.Position;
import org.gjt.sp.jedit.io.VFSManager;
import org.gjt.sp.jedit.textarea.*;
import org.gjt.sp.jedit.*;
//}}}

/**
 * A set of occurrences of the search string on a given line in a buffer.
 */
public class HyperSearchResult
{
	public String path;
	public Buffer buffer;
	public int line;
	public String str; // cached for speed
	public Occur occur;
	public int occurCount;

	//{{{ getBuffer() method
	public Buffer getBuffer()
	{
		if(buffer == null)
			buffer = jEdit.openFile(null,path);
		return buffer;
	} //}}}

	//{{{ getSelection() method
	/**
	 * Returns an array of selection objects pointing to the occurrences
	 * of the search term on the current line. The buffer must be opened
	 * first.
	 * @since jEdit 4.2pre5
	 */
	public Selection[] getSelection()
	{
		if(buffer == null)
			return null;

		Selection[] returnValue = new Selection[occurCount];
		Occur o = occur;
		int i = 0;
		while(o != null)
		{
			Selection.Range s = new Selection.Range(
				o.startPos.getOffset(),
				o.endPos.getOffset()
			);
			returnValue[i++] = s;
			o = o.next;
		}
		return returnValue;
	} //}}}

	//{{{ goTo() method
	public void goTo(final View view)
	{
		if(buffer == null)
			buffer = jEdit.openFile(null,path);

		if(buffer == null)
			return;

		VFSManager.runInAWTThread(new Runnable()
		{
			public void run()
			{
				Selection[] s = getSelection();
				if(s == null)
					return;

				EditPane pane = view.goToBuffer(buffer);
				JEditTextArea textArea = pane.getTextArea();
				if(textArea.isMultipleSelectionEnabled())
					textArea.addToSelection(s);
				else
					textArea.setSelection(s);
                
				textArea.moveCaretPosition(occur.endPos.getOffset());
			}
		});
	} //}}}

	//{{{ toString() method
	public String toString()
	{
		return str;
	} //}}}

	//{{{ Package-private members

	//{{{ HyperSearchResult constructor
	HyperSearchResult(Buffer buffer, int line)
	{
		path = buffer.getPath();

		if(!buffer.isTemporary())
			bufferOpened(buffer);

		this.line = line;

		str = (line + 1) + ": " + buffer.getLineText(line)
			.replace('\t',' ').trim();
	} //}}}

	//{{{ bufferOpened() method
	void bufferOpened(Buffer buffer)
	{
		this.buffer = buffer;
		Occur o = occur;
		while(o != null)
		{
			o.bufferOpened();
			o = o.next;
		}
	} //}}}

	//{{{ bufferClosed() method
	void bufferClosed()
	{
		buffer = null;
		Occur o = occur;
		while(o != null)
		{
			o.bufferClosed();
			o = o.next;
		}
	} //}}}

	//{{{ addOccur() method
	void addOccur(int start, int end)
	{
		Occur o = new Occur(start,end);
		o.next = occur;
		occur = o;
		occurCount++;
	} //}}}

	//}}}

	//{{{ Occur class
	public class Occur
	{
		public int start, end;
		public Position startPos, endPos;
		public Occur next;

		//{{{ Occur constructor
		Occur(int start, int end)
		{
			this.start = start;
			this.end = end;

			if(!buffer.isTemporary())
				bufferOpened();
		} //}}}

		//{{{ bufferOpened() method
		void bufferOpened()
		{
			startPos = buffer.createPosition(Math.min(
				buffer.getLength(),start));
			endPos = buffer.createPosition(Math.min(
				buffer.getLength(),end));
		} //}}}

		//{{{ bufferClosed() method
		void bufferClosed()
		{
			start = startPos.getOffset();
			end = endPos.getOffset();
			startPos = endPos = null;
		} //}}}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8880.java