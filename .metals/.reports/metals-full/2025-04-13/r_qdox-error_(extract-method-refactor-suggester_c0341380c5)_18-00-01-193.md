error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10518.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10518.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10518.java
text:
```scala
o@@ut.printAttribute("text", value);

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/

package org.aspectj.testing.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.aspectj.bridge.*;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.bridge.SourceLocation;
import org.aspectj.util.LangUtil;

/**
 * Implement messages.
 * This implementation is immutable if ISourceLocation is immutable,
 * except for adding source locations.
 */
public class SoftMessage implements IMessage {
	public static String XMLNAME = "message";
	public static final File NO_FILE = ISourceLocation.NO_FILE;
	private String message;
	private IMessage.Kind kind;
	private Throwable thrown;
	private ISourceLocation sourceLocation;
	private String details;
	private final ArrayList extraSourceLocations = new ArrayList();

	//private ISourceLocation pseudoSourceLocation;  // set directly
	// collapse enclosed source location for shorter, property-based xml
	private String file;
	private int line = Integer.MAX_VALUE;

	/** convenience for constructing failure messages */
	public static SoftMessage fail(String message, Throwable thrown) {
		return new SoftMessage(message, IMessage.FAIL, thrown, null);
	}

	/** 
	 * Print messages. 
	 * @param messages List of IMessage
	 */
	public static void writeXml(XMLWriter out, IMessageHolder messages) {
		if ((null == out)
 (null == messages)
 (0 == messages.numMessages(null, true))) {
			return;
		}
		List list = messages.getUnmodifiableListView();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			writeXml(out, (IMessage) iter.next());
		}
	}

	/** 
	 * Print messages. 
	 * @param messages IMessage[] 
	 */
	public static void writeXml(XMLWriter out, IMessage[] messages) {
		if ((null == out) || (null == messages)) {
			return;
		}
		for (int i = 0; i < messages.length; i++) {
			writeXml(out, messages[i]);
		}
	}

	/** print message as an element 
	 * XXX has to sync with ajcTests.dtd
	 * @throws IllegalArgumentException if message.getThrown() is not null
	 */
	public static void writeXml(
		XMLWriter out,
		IMessage message) { // XXX short form only, no files
		if ((null == out) || (null == message)) {
			return;
		}
		Throwable thrown = message.getThrown();
		if (null != thrown) {
			String m = "unable to write " + message + " thrown not permitted";
			throw new IllegalArgumentException(m);
		}
		final String elementName = XMLNAME;
		out.startElement(elementName, false);
		out.printAttribute("kind", message.getKind().toString());
		String value = message.getMessage();
		if (null != value) {
			value = XMLWriter.attributeValue(value);
			out.printAttribute("message", value);
		}
		value = message.getDetails();
		if (null != value) {
			value = XMLWriter.attributeValue(value);
			out.printAttribute("details", value);
		}
		ISourceLocation sl = message.getSourceLocation();
		if (null != sl) {
            int line = sl.getLine();
            if (-1 < line) {
                out.printAttribute("line", "" + line);
            }
			File file = sl.getSourceFile();
			if ((null != file) && !ISourceLocation.NO_FILE.equals(file)) {
                value = XMLWriter.attributeValue(file.getPath());
                out.printAttribute("file", value);
			}
		}
        List extras = message.getExtraSourceLocations();
        if (!LangUtil.isEmpty(extras)) {
            out.endAttributes();
            for (Iterator iter = extras.iterator(); iter.hasNext();) {
				ISourceLocation element = (ISourceLocation) iter.next();
                SoftSourceLocation.writeXml(out, sl);            
			}
        }
		out.endElement(elementName);
	}

	public SoftMessage() {
	} // XXX programmatic only

	/**
	 * Create a (compiler) error or warning message
	 * @param message the String used as the underlying message
	 * @param sourceLocation the ISourceLocation, if any, associated with this message
	 * @param isError if true, use IMessage.ERROR; else use IMessage.WARNING
	 */
	public SoftMessage(
		String message,
		ISourceLocation location,
		boolean isError) {
		this(
			message,
			(isError ? IMessage.ERROR : IMessage.WARNING),
			null,
			location);
	}

	/**
	 * Create a message, handling null values for message and kind
	 * if thrown is not null.
	 * @param message the String used as the underlying message
	 * @param kind the IMessage.Kind of message - not null
	 * @param thrown the Throwable, if any, associated with this message
	 * @param sourceLocation the ISourceLocation, if any, associated with this message
	 * @throws IllegalArgumentException if message is null and
	 * thrown is null or has a null message, or if kind is null
	 * and thrown is null.
	 */
	public SoftMessage(
		String message,
		IMessage.Kind kind,
		Throwable thrown,
		ISourceLocation sourceLocation) {
		this.message = message;
		this.kind = kind;
		this.thrown = thrown;
		this.sourceLocation = sourceLocation;
		if (null == message) {
			if (null != thrown) {
				message = thrown.getMessage();
			}
			if (null == message) {
				throw new IllegalArgumentException("null message");
			}
		}
		if (null == kind) {
			throw new IllegalArgumentException("null kind");
		}
	}

	/** @return the kind of this message */
	public IMessage.Kind getKind() {
		return kind;
	}

	/** @return true if kind == IMessage.ERROR */
	public boolean isError() {
		return kind == IMessage.ERROR;
	}

	/** @return true if kind == IMessage.WARNING */
	public boolean isWarning() {
		return kind == IMessage.WARNING;
	}

	/** @return true if kind == IMessage.DEBUG */
	public boolean isDebug() {
		return kind == IMessage.DEBUG;
	}

	/** 
	 * @return true if kind == IMessage.INFO  
	 */
	public boolean isInfo() {
		return kind == IMessage.INFO;
	}

	/** @return true if  kind == IMessage.ABORT  */
	public boolean isAbort() {
		return kind == IMessage.ABORT;
	}

	/** 
	 * @return true if kind == IMessage.FAIL
	 */
	public boolean isFailed() {
		return kind == IMessage.FAIL;
	}

	/** @return non-null String with simple message */
	final public String getMessage() {
		return message;
	}

	/** @return Throwable associated with this message, or null if none */
	final public Throwable getThrown() {
		return thrown;
	}

	/** 
	 * This returns any ISourceLocation set or a mock-up
	 * if file and/or line were set.
	 * @return ISourceLocation associated with this message, 
	 * a mock-up if file or line is available, or null if none 
	 */
	final public ISourceLocation getSourceLocation() {
		if ((null == sourceLocation)
			&& ((null != file) || (line != Integer.MAX_VALUE))) {
			File f = (null == file ? NO_FILE : new File(file));
			int line = (this.line == Integer.MAX_VALUE ? 0 : this.line);
			sourceLocation = new SourceLocation(f, line);
		}
		return sourceLocation;
	}

	/** set the kind of this message */
	public void setMessageKind(IMessage.Kind kind) {
		this.kind = (null == kind ? IMessage.ERROR : kind);
	}

	/** set the file for the underlying source location of this message
	 * @throws IllegalStateException if source location was set directly
	 *          or indirectly by calling getSourceLocation after setting
	 *          file or line.
	 */
	public void setFile(String path) {
		LangUtil.throwIaxIfFalse(!LangUtil.isEmpty(path), "empty path");
		if (null != sourceLocation) {
			throw new IllegalStateException("cannot set line after creating source location");
		}
		this.file = path;
	}

	/** set the kind of this message */
	public void setKindAsString(String kind) {
		setMessageKind(MessageUtil.getKind(kind));
	}

	public void setSourceLocation(ISourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	/** 
	 * Set the line for the underlying source location.
	 * @throws IllegalStateException if source location was set directly
	 *          or indirectly by calling getSourceLocation after setting
	 *          file or line.
	 */
	public void setLineAsString(String line) {
		if (null != sourceLocation) {
			throw new IllegalStateException("cannot set line after creating source location");
		}
		this.line = Integer.valueOf(line).intValue();
		SourceLocation.validLine(this.line);
	}

	public void setText(String text) {
		this.message = (null == text ? "" : text);
	}

	public String toString() {
		StringBuffer result = new StringBuffer();

		result.append(null == getKind() ? "<null kind>" : getKind().toString());

		String messageString = getMessage();
		if (!LangUtil.isEmpty(messageString)) {
			result.append(messageString);
		}

		ISourceLocation loc = getSourceLocation();
		if ((null != loc) && (loc != ISourceLocation.NO_FILE)) {
			result.append(" at " + loc);
		}
		if (null != thrown) {
			result.append(" -- " + LangUtil.renderExceptionShort(thrown));
		}
		return result.toString();
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String string) {
		details = string;
	}

	/* (non-Javadoc)
	 * @see org.aspectj.bridge.IMessage#getExtraSourceLocations()
	 */
	public List getExtraSourceLocations() {
		return extraSourceLocations;
	}
	public void addSourceLocation(ISourceLocation location) {
		if (null != location) {
			extraSourceLocations.add(location);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10518.java