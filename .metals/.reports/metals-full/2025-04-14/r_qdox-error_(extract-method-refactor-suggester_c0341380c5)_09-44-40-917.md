error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1691.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1691.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1691.java
text:
```scala
public v@@oid close() {

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.*;

/**
 * @see IBuffer
 */
public class Buffer implements IBuffer {
	protected IFile file;
	protected int flags;
	protected char[] contents;
	protected ArrayList changeListeners;
	protected IOpenable owner;
	protected int gapStart= -1;
	protected int gapEnd= -1;

	protected Object lock= new Object();

	protected static final int F_HAS_UNSAVED_CHANGES= 1;
	protected static final int F_IS_READ_ONLY= 2;
	protected static final int F_IS_CLOSED= 4;

/**
 * Creates a new buffer on an underlying resource.
 */
protected Buffer(IFile file, IOpenable owner, boolean readOnly) {
	this.file = file;
	this.owner = owner;
	if (file == null) {
		setReadOnly(readOnly);
	}
}
/**
 * @see IBuffer
 */
public void addBufferChangedListener(IBufferChangedListener listener) {
	if (this.changeListeners == null) {
		this.changeListeners = new ArrayList(5);
	}
	if (!this.changeListeners.contains(listener)) {
		this.changeListeners.add(listener);
	}
}
/**
 * Append the <code>text</code> to the actual content, the gap is moved
 * to the end of the <code>text</code>.
 */
public void append(char[] text) {
	if (!isReadOnly()) {
		if (text == null || text.length == 0) {
			return;
		}
		int length = getLength();
		moveAndResizeGap(length, text.length);
		System.arraycopy(text, 0, this.contents, length, text.length);
		this.gapStart += text.length;
		this.flags |= F_HAS_UNSAVED_CHANGES;
		notifyChanged(new BufferChangedEvent(this, length, 0, new String(text)));
	}
}
/**
 * Append the <code>text</code> to the actual content, the gap is moved
 * to the end of the <code>text</code>.
 */
public void append(String text) {
	if (text == null) {
		return;
	}
	this.append(text.toCharArray());
}
/**
 * @see IBuffer
 */
public void close() throws IllegalArgumentException {
	BufferChangedEvent event = null;
	synchronized (this.lock) {
		if (isClosed())
			return;
		event = new BufferChangedEvent(this, 0, 0, null);
		this.contents = null;
		this.flags |= F_IS_CLOSED;
	}
	notifyChanged(event); // notify outside of synchronized block
	this.changeListeners = null;
}
/**
 * @see IBuffer
 */
public char getChar(int position) {
	synchronized (this.lock) {
		if (position < this.gapStart) {
			return this.contents[position];
		}
		int gapLength = this.gapEnd - this.gapStart;
		return this.contents[position + gapLength];
	}
}
/**
 * @see IBuffer
 */
public char[] getCharacters() {
	if (this.contents == null) return null;
	synchronized (this.lock) {
		if (this.gapStart < 0) {
			return this.contents;
		}
		int length = this.contents.length;
		char[] newContents = new char[length - this.gapEnd + this.gapStart];
		System.arraycopy(this.contents, 0, newContents, 0, this.gapStart);
		System.arraycopy(this.contents, this.gapEnd, newContents, this.gapStart, length - this.gapEnd);
		return newContents;
	}
}
/**
 * @see IBuffer
 */
public String getContents() {
	char[] chars = this.getCharacters();
	if (chars == null) return null;
	return new String(chars);
}
/**
 * @see IBuffer
 */
public int getLength() {
	synchronized (this.lock) {
		int length = this.gapEnd - this.gapStart;
		return (this.contents.length - length);
	}
}
/**
 * @see IBuffer
 */
public IOpenable getOwner() {
	return this.owner;
}
/**
 * @see IBuffer
 */
public String getText(int offset, int length) {
	if (this.contents == null)
		return ""; //$NON-NLS-1$
	synchronized (this.lock) {
		if (offset + length < this.gapStart)
			return new String(this.contents, offset, length);
		if (this.gapStart < offset) {
			int gapLength = this.gapEnd - this.gapStart;
			return new String(this.contents, offset + gapLength, length);
		}
		StringBuffer buf = new StringBuffer();
		buf.append(this.contents, offset, this.gapStart - offset);
		buf.append(this.contents, this.gapEnd, offset + length - this.gapStart);
		return buf.toString();
	}
}
/**
 * @see IBuffer
 */
public IResource getUnderlyingResource() {
	return this.file;
}
/**
 * @see IBuffer
 */
public boolean hasUnsavedChanges() {
	return (this.flags & F_HAS_UNSAVED_CHANGES) != 0;
}
/**
 * @see IBuffer
 */
public boolean isClosed() {
	return (this.flags & F_IS_CLOSED) != 0;
}
/**
 * @see IBuffer
 */
public boolean isReadOnly() {
	return (this.flags & F_IS_READ_ONLY) != 0;
}
/**
 * Moves the gap to location and adjust its size to the
 * anticipated change size. The size represents the expected 
 * range of the gap that will be filled after the gap has been moved.
 * Thus the gap is resized to actual size + the specified size and
 * moved to the given position.
 */
protected void moveAndResizeGap(int position, int size) {
	char[] content = null;
	int oldSize = this.gapEnd - this.gapStart;
	if (size < 0) {
		if (oldSize > 0) {
			content = new char[this.contents.length - oldSize];
			System.arraycopy(this.contents, 0, content, 0, this.gapStart);
			System.arraycopy(this.contents, this.gapEnd, content, this.gapStart, content.length - this.gapStart);
			this.contents = content;
		}
		this.gapStart = this.gapEnd = position;
		return;
	}
	content = new char[this.contents.length + (size - oldSize)];
	int newGapStart = position;
	int newGapEnd = newGapStart + size;
	if (oldSize == 0) {
		System.arraycopy(this.contents, 0, content, 0, newGapStart);
		System.arraycopy(this.contents, newGapStart, content, newGapEnd, content.length - newGapEnd);
	} else
		if (newGapStart < this.gapStart) {
			int delta = this.gapStart - newGapStart;
			System.arraycopy(this.contents, 0, content, 0, newGapStart);
			System.arraycopy(this.contents, newGapStart, content, newGapEnd, delta);
			System.arraycopy(this.contents, this.gapEnd, content, newGapEnd + delta, this.contents.length - this.gapEnd);
		} else {
			int delta = newGapStart - this.gapStart;
			System.arraycopy(this.contents, 0, content, 0, this.gapStart);
			System.arraycopy(this.contents, this.gapEnd, content, this.gapStart, delta);
			System.arraycopy(this.contents, this.gapEnd + delta, content, newGapEnd, content.length - newGapEnd);
		}
	this.contents = content;
	this.gapStart = newGapStart;
	this.gapEnd = newGapEnd;
}
/**
 * Notify the listeners that this buffer has changed.
 * To avoid deadlock, this should not be called in a synchronized block.
 */
protected void notifyChanged(final BufferChangedEvent event) {
	if (this.changeListeners != null) {
		for (int i = 0, size = this.changeListeners.size(); i < size; ++i) {
			final IBufferChangedListener listener = (IBufferChangedListener) this.changeListeners.get(i);
			Platform.run(new ISafeRunnable() {
				public void handleException(Throwable exception) {
					Util.log(exception, "Exception occurred in listener of buffer change notification"); //$NON-NLS-1$
				}
				public void run() throws Exception {
					listener.bufferChanged(event);
				}
			});
			
		}
	}
}
/**
 * @see IBuffer
 */
public void removeBufferChangedListener(IBufferChangedListener listener) {
	if (this.changeListeners != null) {
		this.changeListeners.remove(listener);
		if (this.changeListeners.size() == 0) {
			this.changeListeners = null;
		}
	}
}
/**
 * Replaces <code>length</code> characters starting from <code>position</code> with <code>text<code>.
 * After that operation, the gap is placed at the end of the 
 * inserted <code>text</code>.
 */
public void replace(int position, int length, char[] text) {
	if (!isReadOnly()) {
		int textLength = text == null ? 0 : text.length;
		synchronized (this.lock) {
			// move gap
			moveAndResizeGap(position + length, textLength - length);

			// overwrite
			int min = Math.min(textLength, length);
			if (min > 0) {
				System.arraycopy(text, 0, this.contents, position, min);
			}
			if (length > textLength) {
				// enlarge the gap
				this.gapStart -= length - textLength;
			} else if (textLength > length) {
				// shrink gap
				this.gapStart += textLength - length;
				System.arraycopy(text, 0, this.contents, position, textLength);
			}
		}
		this.flags |= F_HAS_UNSAVED_CHANGES;
		String string = null;
		if (textLength > 0) {
			string = new String(text);
		}
		notifyChanged(new BufferChangedEvent(this, position, length, string));
	}
}
/**
 * Replaces <code>length</code> characters starting from <code>position</code> with <code>text<code>.
 * After that operation, the gap is placed at the end of the 
 * inserted <code>text</code>.
 */
public void replace(int position, int length, String text) {
	this.replace(position, length, text == null ? null : text.toCharArray());
}
/**
 * @see IBuffer
 */
public void save(IProgressMonitor progress, boolean force) throws JavaModelException {

	// determine if saving is required 
	if (isReadOnly() || this.file == null) {
		return;
	}
	if (!hasUnsavedChanges())
		return;
		
	// use a platform operation to update the resource contents
	try {
		String encoding = ((IJavaElement)this.owner).getJavaProject().getOption(JavaCore.CORE_ENCODING, true);
		String stringContents = this.getContents();
		if (stringContents == null) return;
		byte[] bytes = encoding == null 
			? stringContents.getBytes() 
			: stringContents.getBytes(encoding);
		ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

		if (this.file.exists()) {
			this.file.setContents(
				stream, 
				force ? IResource.FORCE | IResource.KEEP_HISTORY : IResource.KEEP_HISTORY, 
				null);
		} else {
			this.file.create(stream, force, null);
		}	
	} catch (IOException e) {
		throw new JavaModelException(e, IJavaModelStatusConstants.IO_EXCEPTION);
	} catch (CoreException e) {
		throw new JavaModelException(e);
	}

	// the resource no longer has unsaved changes
	this.flags &= ~ (F_HAS_UNSAVED_CHANGES);
}
/**
 * @see IBuffer
 */
public void setContents(char[] newContents) {
	// allow special case for first initialization 
	// after creation by buffer factory
	if (this.contents == null) {
		this.contents = newContents;
		this.flags &= ~ (F_HAS_UNSAVED_CHANGES);
		return;
	}
	
	if (!isReadOnly()) {
		String string = null;
		if (newContents != null) {
			string = new String(newContents);
		}
		BufferChangedEvent event = new BufferChangedEvent(this, 0, this.getLength(), string);
		synchronized (this.lock) {
			this.contents = newContents;
			this.flags |= F_HAS_UNSAVED_CHANGES;
			this.gapStart = -1;
			this.gapEnd = -1;
		}
		notifyChanged(event);
	}
}
/**
 * @see IBuffer
 */
public void setContents(String newContents) {
	this.setContents(newContents.toCharArray());
}
/**
 * Sets this <code>Buffer</code> to be read only.
 */
protected void setReadOnly(boolean readOnly) {
	if (readOnly) {
		this.flags |= F_IS_READ_ONLY;
	} else {
		this.flags &= ~(F_IS_READ_ONLY);
	}
}
public String toString() {
	StringBuffer buffer = new StringBuffer();
	buffer.append("Owner: " + ((JavaElement)this.owner).toStringWithAncestors()); //$NON-NLS-1$
	buffer.append("\nHas unsaved changes: " + this.hasUnsavedChanges()); //$NON-NLS-1$
	buffer.append("\nIs readonly: " + this.isReadOnly()); //$NON-NLS-1$
	buffer.append("\nIs closed: " + this.isClosed()); //$NON-NLS-1$
	buffer.append("\nContents:\n"); //$NON-NLS-1$
	char[] charContents = this.getCharacters();
	if (charContents == null) {
		buffer.append("<null>"); //$NON-NLS-1$
	} else {
		int length = charContents.length;
		for (int i = 0; i < length; i++) {
			char c = charContents[i];
			switch (c) {
				case '\n': 
					buffer.append("\\n\n"); //$NON-NLS-1$
					break;
				case '\r':
					if (i < length-1 && this.contents[i+1] == '\n') {
						buffer.append("\\r\\n\n"); //$NON-NLS-1$
						i++;
					} else {
						buffer.append("\\r\n"); //$NON-NLS-1$
					}
					break;
				default:
					buffer.append(c);
					break;
			}
		}
	}
	return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1691.java