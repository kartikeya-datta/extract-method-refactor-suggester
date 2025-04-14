error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7291.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7291.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7291.java
text:
```scala
r@@eturn "Event {type=" + type + " " + widget + " time=" + time + " data=" + data + " x=" + x + " y=" + y + " width=" + width + " height=" + height + " detail=" + detail + "}";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide a description of a particular
 * event which occurred within SWT. The SWT <em>untyped listener</em>
 * API uses these instances for all event dispatching.
 * <p>
 * Note: For a given event, only the fields which are appropriate
 * will be filled in. The contents of the fields which are not used
 * by the event are unspecified.
 * </p>
 * 
 * @see Listener
 * @see org.eclipse.swt.events.TypedEvent
 */

public class Event {
	
	/**
	 * the display where the event occurred
	 * 
	 * @since 2.0 
	 */	
	public Display display;
		
	/**
	 * the widget that issued the event
	 */
	public Widget widget;
	
	/**
	 * the type of event, as defined by the event type constants
	 * in class <code>SWT</code>
	 *
	 * @see org.eclipse.swt.SWT
	 */
	public int type;
	
	/**
	 * the event specific detail field, as defined by the detail constants
	 * in class <code>SWT</code>
	 * 
	 * @see org.eclipse.swt.SWT
	 */
	public int detail;
	
	/**
	 * the item that the event occurred in (can be null)
	 */
	public Widget item;
	
	/**
	 * the index of the item where the event occurred
	 * 
	 * @since 3.2
	 */
	public int index;
	
	/**
	 * the graphics context to use when painting
	 * that is configured to use the colors, font and
	 * damaged region of the control.  It is valid
	 * only during the paint and must not be disposed
	 */
	public GC gc;
	
	/**
	 * depending on the event type, the x offset of the bounding
	 * rectangle of the region that requires painting or the
	 * widget-relative, x coordinate of the pointer at the
	 * time the mouse button was pressed or released
	 */
	public int x;
	
	/**
	 * depending on the event type, the y offset of the bounding
	 * rectangle of the  region that requires painting or the
	 * widget-relative, y coordinate of the pointer at the
	 * time the mouse button was pressed or released
	 */
	public int y;
	
	/**
	 * the width of the bounding rectangle of the 
	 * region that requires painting
	 */
	public int width;
	
	/**
	 * the height of the bounding rectangle of the 
	 * region that requires painting
	 */
	public int height;

	/**
	 * depending on the event type, the number of following
	 * paint events which are pending which may always be zero
	 * on some platforms or the number of lines or pages to
	 * scroll using the mouse wheel
	 */
	public int count;
	
	/**
	 * the time that the event occurred.
	 * 
	 * NOTE: This field is an unsigned integer and should
	 * be AND'ed with 0xFFFFFFFFL so that it can be treated
	 * as a signed long.
	 */	
	public int time;
	
	/**
	 * the button that was pressed or released; 1 for the
	 * first button, 2 for the second button, and 3 for the
	 * third button, etc.
	 */	
	public int button;
	
	/**
	 * depending on the event, the character represented by the key
	 * that was typed.  This is the final character that results
	 * after all modifiers have been applied.  For example, when the
	 * user types Ctrl+A, the character value is 0x01 (ASCII SOH).
	 * It is important that applications do not attempt to modify the
	 * character value based on a stateMask (such as SWT.CTRL) or the
	 * resulting character will not be correct.
	 */
	public char character;
	
	/**
	 * depending on the event, the key code of the key that was typed,
	 * as defined by the key code constants in class <code>SWT</code>.
	 * When the character field of the event is ambiguous, this field
	 * contains the unaffected value of the original character.  For
	 * example, typing Ctrl+M or Enter both result in the character '\r'
	 * but the keyCode field will also contain '\r' when Enter was typed
	 * and 'm' when Ctrl+M was typed.
	 * 
	 * @see org.eclipse.swt.SWT
	 */
	public int keyCode;
	
	/**
	 * depending on the event, the state of the keyboard modifier
	 * keys and mouse masks at the time the event was generated.
	 * 
	 * @see org.eclipse.swt.SWT
	 */
	public int stateMask;
	
	/**
	 * depending on the event, the range of text being modified.
	 * Setting these fields has no effect.
	 */
	public int start, end;
	
	/**
	 * depending on the event, the new text that will be inserted.
	 * Setting this field will change the text that is about to
	 * be inserted or deleted.
	 */
	public String text;

	/**
	 * depending on the event, a flag indicating whether the operation
	 * should be allowed.  Setting this field to false will cancel the
	 * operation.
	 */
	public boolean doit = true;
	
	/**
	 * a field for application use
	 */
	public Object data;
	
/**
 * Gets the bounds.
 * 
 * @return a rectangle that is the bounds.
 */
public Rectangle getBounds () {
	return new Rectangle (x, y, width, height);
}

/**
 * Sets the bounds.
 * 
 * @param rect the new rectangle
 */
public void setBounds (Rectangle rect) {
	this.x = rect.x;
	this.y = rect.y;
	this.width = rect.width;
	this.height = rect.height;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the event
 */
public String toString () {
	return "Event {type=" + type + ",widget=" + widget + ",x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + "}";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7291.java