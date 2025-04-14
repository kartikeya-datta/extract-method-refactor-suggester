error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7725.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7725.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7725.java
text:
```scala
v@@alue = token.toUpperCase().charAt(0);

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

package org.eclipse.ui.internal.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.eclipse.swt.SWT;

public final class KeySupport {

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(KeySupport.class.getName());

	private final static String ALT = "Alt"; //$NON-NLS-1$
	private final static String COMMAND = "Command"; //$NON-NLS-1$
	private final static String CTRL = "Ctrl"; //$NON-NLS-1$
	private final static String MODIFIER_SEPARATOR = "+"; //$NON-NLS-1$
	private final static String SHIFT = "Shift"; //$NON-NLS-1$
	private final static String STROKE_SEPARATOR = " "; //$NON-NLS-1$

	private static Map stringToValueMap = new TreeMap();	
	private static Map valueToStringMap = new TreeMap();

	static {
		stringToValueMap.put("BACKSPACE", new Integer(8)); //$NON-NLS-1$
		stringToValueMap.put("TAB", new Integer(9)); //$NON-NLS-1$
		stringToValueMap.put("RETURN", new Integer(13)); //$NON-NLS-1$
		stringToValueMap.put("ENTER", new Integer(13)); //$NON-NLS-1$
		stringToValueMap.put("ESCAPE", new Integer(27)); //$NON-NLS-1$
		stringToValueMap.put("ESC", new Integer(27)); //$NON-NLS-1$
		stringToValueMap.put("DELETE", new Integer(127)); //$NON-NLS-1$
		stringToValueMap.put("SPACE", new Integer(' ')); //$NON-NLS-1$
		stringToValueMap.put("ARROW_UP", new Integer(SWT.ARROW_UP)); //$NON-NLS-1$
		stringToValueMap.put("ARROW_DOWN", new Integer(SWT.ARROW_DOWN)); //$NON-NLS-1$
		stringToValueMap.put("ARROW_LEFT", new Integer(SWT.ARROW_LEFT)); //$NON-NLS-1$
		stringToValueMap.put("ARROW_RIGHT", new Integer(SWT.ARROW_RIGHT)); //$NON-NLS-1$
		stringToValueMap.put("PAGE_UP", new Integer(SWT.PAGE_UP)); //$NON-NLS-1$
		stringToValueMap.put("PAGE_DOWN", new Integer(SWT.PAGE_DOWN)); //$NON-NLS-1$
		stringToValueMap.put("HOME", new Integer(SWT.HOME)); //$NON-NLS-1$
		stringToValueMap.put("END", new Integer(SWT.END)); //$NON-NLS-1$
		stringToValueMap.put("INSERT", new Integer(SWT.INSERT)); //$NON-NLS-1$
		stringToValueMap.put("F1", new Integer(SWT.F1)); //$NON-NLS-1$
		stringToValueMap.put("F2", new Integer(SWT.F2)); //$NON-NLS-1$
		stringToValueMap.put("F3", new Integer(SWT.F3)); //$NON-NLS-1$
		stringToValueMap.put("F4", new Integer(SWT.F4)); //$NON-NLS-1$
		stringToValueMap.put("F5", new Integer(SWT.F5)); //$NON-NLS-1$
		stringToValueMap.put("F6", new Integer(SWT.F6)); //$NON-NLS-1$
		stringToValueMap.put("F7", new Integer(SWT.F7)); //$NON-NLS-1$
		stringToValueMap.put("F8", new Integer(SWT.F8)); //$NON-NLS-1$
		stringToValueMap.put("F9", new Integer(SWT.F9)); //$NON-NLS-1$
		stringToValueMap.put("F10", new Integer(SWT.F10)); //$NON-NLS-1$
		stringToValueMap.put("F11", new Integer(SWT.F11)); //$NON-NLS-1$
		stringToValueMap.put("F12", new Integer(SWT.F12)); //$NON-NLS-1$

		valueToStringMap.put(new Integer(8), "Backspace"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(9), "Tab"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(13), "Return"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(13), "Enter"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(27), "Escape"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(27), "Esc"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(127), "Delete"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(' '), "Space"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.ARROW_UP), "Arrow_Up"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.ARROW_DOWN), "Arrow_Down"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.ARROW_LEFT), "Arrow_Left"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.ARROW_RIGHT), "Arrow_Right"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.PAGE_UP), "Page_Up"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.PAGE_DOWN), "Page_Down"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.HOME), "Home"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.END), "End"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.INSERT), "Insert"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.F1), "F1"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.F2), "F2"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.F3), "F3"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.F4), "F4"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.F5), "F5"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.F6), "F6"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.F7), "F7"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.F8), "F8"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.F9), "F9"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.F10), "F10"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.F11), "F11"); //$NON-NLS-1$
		valueToStringMap.put(new Integer(SWT.F12), "F12"); //$NON-NLS-1$		
	}

	public static String formatSequence(Sequence sequence, boolean localize)
		throws IllegalArgumentException {
		if (sequence == null)
			throw new IllegalArgumentException();
			
		int i = 0;
		Iterator iterator = sequence.getStrokes().iterator();
		StringBuffer stringBuffer = new StringBuffer();
		
		while (iterator.hasNext()) {
			if (i != 0)
				stringBuffer.append(STROKE_SEPARATOR);

			stringBuffer.append(formatStroke((Stroke) iterator.next(), localize));
			i++;
		}

		return stringBuffer.toString();
	}

	public static String formatStroke(Stroke stroke, boolean localize)
		throws IllegalArgumentException {
		if (stroke == null)
			throw new IllegalArgumentException();		
			
		StringBuffer stringBuffer = new StringBuffer();
		int value = stroke.getValue();
		
		if ((value & SWT.CTRL) != 0) {
			stringBuffer.append(localize ? Util.getString(resourceBundle, CTRL) : CTRL);
		}
		
		if ((value & SWT.ALT) != 0) {
			if (stringBuffer.length() > 0)
				stringBuffer.append(MODIFIER_SEPARATOR);
			
			stringBuffer.append(localize ? Util.getString(resourceBundle, ALT) : ALT);								
		}

		if ((value & SWT.SHIFT) != 0) {
			if (stringBuffer.length() > 0)
				stringBuffer.append(MODIFIER_SEPARATOR);
			
			stringBuffer.append(localize ? Util.getString(resourceBundle, SHIFT) : SHIFT);								
		}

		if ((value & SWT.COMMAND) != 0) {
			if (stringBuffer.length() > 0)
				stringBuffer.append(MODIFIER_SEPARATOR);
			
			stringBuffer.append(localize ? Util.getString(resourceBundle, COMMAND) : COMMAND);								
		}		

		if (stringBuffer.length() > 0)
			stringBuffer.append(MODIFIER_SEPARATOR);

		value &= ~(SWT.CTRL | SWT.ALT | SWT.SHIFT | SWT.COMMAND);
		String string = (String) valueToStringMap.get(new Integer(value));

		if (string != null)
			stringBuffer.append(localize ? Util.getString(resourceBundle, string) : string);
		else 
			stringBuffer.append(Character.toUpperCase((char) value));

		return stringBuffer.toString();				
	}
	
	public static Sequence parseSequence(String string)
		throws IllegalArgumentException {
		if (string == null)
			throw new IllegalArgumentException();

		List strokes = new ArrayList();
		StringTokenizer stringTokenizer = new StringTokenizer(string);
				
		while (stringTokenizer.hasMoreTokens())
			strokes.add(parseStroke(stringTokenizer.nextToken()));
			
		return Sequence.create(strokes);
	}	
	
	public static Stroke parseStroke(String string)
		throws IllegalArgumentException {
		if (string == null)
			throw new IllegalArgumentException();
		
		List list = new ArrayList();
		StringTokenizer stringTokenizer = new StringTokenizer(string, MODIFIER_SEPARATOR, true);
		
		while (stringTokenizer.hasMoreTokens())
			list.add(stringTokenizer.nextToken());

		int size = list.size();
		int value = 0;

		if (size % 2 == 1) {
			String token = (String) list.get(size - 1);			
			Integer integer = (Integer) stringToValueMap.get(token.toUpperCase());
		
			if (integer != null)
				value = integer.intValue();
			else if (token.length() == 1)
				value = token.charAt(0);

			if (value != 0) {
				for (int i = 0; i < size - 1; i++) {
					token = (String) list.get(i);			
					
					if (i % 2 == 0) {
						if (CTRL.equalsIgnoreCase(token)) {
							if ((value & SWT.CTRL) != 0)
								return Stroke.create(0);
							
							value |= SWT.CTRL;
						} else if (ALT.equalsIgnoreCase(token)) {
							if ((value & SWT.ALT) != 0)
								return Stroke.create(0);

							value |= SWT.ALT;
						} else if (SHIFT.equalsIgnoreCase(token)) {
							if ((value & SWT.SHIFT) != 0)
								return Stroke.create(0);

							value |= SWT.SHIFT;
						} else if (COMMAND.equalsIgnoreCase(token)) {
							if ((value & SWT.COMMAND) != 0)
								return Stroke.create(0);

							value |= SWT.COMMAND;
						} else
							return Stroke.create(0);
					} else if (!MODIFIER_SEPARATOR.equals(token))
						return Stroke.create(0);
				}				
			}				
		}

		return Stroke.create(value);
	}
	
	private KeySupport() {
		super();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7725.java