error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4277.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4277.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4277.java
text:
```scala
t@@his.modifierKeysAsArray = (ModifierKey[]) this.modifierKeys.toArray(new ModifierKey[this.modifierKeys.size()]);

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

package org.eclipse.ui.keys;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.ui.internal.util.Util;

/**
 * <p>
 * JAVADOC
 * </p>
 * <p>
 * <em>EXPERIMENTAL</em>
 * </p>
 * 
 * @since 3.0
 */
public final class KeyStroke implements Comparable {

	public final static String ALT = "ALT"; //$NON-NLS-1$
	public final static String ARROW_DOWN = "ARROW_DOWN"; //$NON-NLS-1$
	public final static String ARROW_LEFT = "ARROW_LEFT"; //$NON-NLS-1$
	public final static String ARROW_RIGHT = "ARROW_RIGHT"; //$NON-NLS-1$
	public final static String ARROW_UP = "ARROW_UP"; //$NON-NLS-1$
	public final static String BS = "BS"; //$NON-NLS-1$
	public final static String COMMAND = "COMMAND"; //$NON-NLS-1$
	public final static String CR = "CR"; //$NON-NLS-1$
	public final static String CTRL = "CTRL"; //$NON-NLS-1$
	public final static String DEL = "DEL"; //$NON-NLS-1$
	public final static String END = "END"; //$NON-NLS-1$
	public final static String ESC = "ESC"; //$NON-NLS-1$
	public final static String F1 = "F1"; //$NON-NLS-1$
	public final static String F10 = "F10"; //$NON-NLS-1$
	public final static String F11 = "F11"; //$NON-NLS-1$
	public final static String F12 = "F12"; //$NON-NLS-1$
	public final static String F2 = "F2"; //$NON-NLS-1$
	public final static String F3 = "F3"; //$NON-NLS-1$
	public final static String F4 = "F4"; //$NON-NLS-1$
	public final static String F5 = "F5"; //$NON-NLS-1$
	public final static String F6 = "F6"; //$NON-NLS-1$
	public final static String F7 = "F7"; //$NON-NLS-1$
	public final static String F8 = "F8"; //$NON-NLS-1$
	public final static String F9 = "F9"; //$NON-NLS-1$
	public final static String FF = "FF"; //$NON-NLS-1$
	public final static String HOME = "HOME"; //$NON-NLS-1$
	public final static String INSERT = "INSERT"; //$NON-NLS-1$
	public final static char KEY_DELIMITER = '+'; //$NON-NLS-1$
	public final static String KEY_DELIMITERS = KEY_DELIMITER + ""; //$NON-NLS-1$
	public final static String LF = "LF"; //$NON-NLS-1$
	public final static String PAGE_DOWN = "PAGE_DOWN"; //$NON-NLS-1$
	public final static String PAGE_UP = "PAGE_UP"; //$NON-NLS-1$
	public final static String PLUS = "PLUS"; //$NON-NLS-1$
	public final static String SHIFT = "SHIFT"; //$NON-NLS-1$
	public final static String SPACE = "SPACE"; //$NON-NLS-1$
	public final static String TAB = "TAB"; //$NON-NLS-1$

	private final static int HASH_FACTOR = 89;
	private final static int HASH_INITIAL = KeyStroke.class.getName().hashCode();
	private final static String KEY_DELIMITER_KEY = "KEY_DELIMITER"; //$NON-NLS-1$	
	private final static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(KeySequence.class.getName());	
	
	private static SortedMap escapeKeyLookup = new TreeMap();
	private static SortedMap modifierKeyLookup = new TreeMap();
	private static SortedMap specialKeyLookup = new TreeMap();
	
	static {
		escapeKeyLookup.put(BS, CharacterKey.getInstance('\b'));
		escapeKeyLookup.put(CR, CharacterKey.getInstance('\r'));
		escapeKeyLookup.put(DEL, CharacterKey.getInstance('\u007F'));
		escapeKeyLookup.put(ESC, CharacterKey.getInstance('\u001b'));
		escapeKeyLookup.put(FF, CharacterKey.getInstance('\f'));
		escapeKeyLookup.put(LF, CharacterKey.getInstance('\n'));
		escapeKeyLookup.put(PLUS, CharacterKey.getInstance('+'));
		escapeKeyLookup.put(SPACE, CharacterKey.getInstance(' '));
		escapeKeyLookup.put(TAB, CharacterKey.getInstance('\t'));
		modifierKeyLookup.put(ALT, ModifierKey.ALT);
		modifierKeyLookup.put(COMMAND, ModifierKey.COMMAND);
		modifierKeyLookup.put(CTRL, ModifierKey.CTRL);
		modifierKeyLookup.put(SHIFT, ModifierKey.SHIFT);
		specialKeyLookup.put(ARROW_DOWN, SpecialKey.ARROW_DOWN);
		specialKeyLookup.put(ARROW_LEFT, SpecialKey.ARROW_LEFT);
		specialKeyLookup.put(ARROW_RIGHT, SpecialKey.ARROW_RIGHT);
		specialKeyLookup.put(ARROW_UP, SpecialKey.ARROW_UP);		
		specialKeyLookup.put(END, SpecialKey.END);
		specialKeyLookup.put(F1, SpecialKey.F1);
		specialKeyLookup.put(F10, SpecialKey.F10);
		specialKeyLookup.put(F11, SpecialKey.F11);		
		specialKeyLookup.put(F12, SpecialKey.F12);
		specialKeyLookup.put(F2, SpecialKey.F2);
		specialKeyLookup.put(F3, SpecialKey.F3);
		specialKeyLookup.put(F4, SpecialKey.F4);		
		specialKeyLookup.put(F5, SpecialKey.F5);
		specialKeyLookup.put(F6, SpecialKey.F6);
		specialKeyLookup.put(F7, SpecialKey.F7);
		specialKeyLookup.put(F8, SpecialKey.F8);		
		specialKeyLookup.put(F9, SpecialKey.F9);
		specialKeyLookup.put(HOME, SpecialKey.HOME);
		specialKeyLookup.put(INSERT, SpecialKey.INSERT);
		specialKeyLookup.put(PAGE_DOWN, SpecialKey.PAGE_DOWN);		
		specialKeyLookup.put(PAGE_UP, SpecialKey.PAGE_UP);
	}

	/**
	 * JAVADOC
	 * 
	 * @param naturalKey
	 * @return
	 */		
	public static KeyStroke getInstance(NaturalKey naturalKey) {
		return new KeyStroke(Util.EMPTY_SORTED_SET, naturalKey);
	}

	/**
	 * JAVADOC
	 * 
	 * @param modifierKey
	 * @param naturalKey
	 * @return
	 */
	public static KeyStroke getInstance(ModifierKey modifierKey, NaturalKey naturalKey) {
		if (modifierKey == null)
			throw new NullPointerException();

		return new KeyStroke(new TreeSet(Collections.singletonList(modifierKey)), naturalKey);
	}

	/**
	 * JAVADOC
	 * 
	 * @param modifierKeys
	 * @param naturalKey
	 * @return
	 */
	public static KeyStroke getInstance(ModifierKey[] modifierKeys, NaturalKey naturalKey) {
		Util.assertInstance(modifierKeys, ModifierKey.class);		
		return new KeyStroke(new TreeSet(Arrays.asList(modifierKeys)), naturalKey);
	}

	/**
	 * JAVADOC
	 * 
	 * @param modifierKeys
	 * @param naturalKey
	 * @return
	 */
	public static KeyStroke getInstance(SortedSet modifierKeys, NaturalKey naturalKey) {
		return new KeyStroke(modifierKeys, naturalKey);
	}

	/**
	 * JAVADOC
	 * 
	 * @param string
	 * @return
	 * @throws ParseException
	 */
	public static KeyStroke getInstance(String string)
		throws ParseException {
		if (string == null)
			throw new NullPointerException();

		SortedSet modifierKeys = new TreeSet();
		NaturalKey naturalKey = null;
		StringTokenizer stringTokenizer = new StringTokenizer(string, KEY_DELIMITERS);
		
		while (stringTokenizer.hasMoreTokens()) {
			String name = stringTokenizer.nextToken();
			
			if (stringTokenizer.hasMoreTokens()) {
				name = name.toUpperCase();
				ModifierKey modifierKey = (ModifierKey) modifierKeyLookup.get(name);
				
				if (modifierKey == null || !modifierKeys.add(modifierKey))
					throw new ParseException();
			} else if (name.length() == 1) {
				naturalKey = CharacterKey.getInstance(name.charAt(0));				
				break;
			} else {
				name = name.toUpperCase();
				naturalKey = (NaturalKey) escapeKeyLookup.get(name);
				
				if (naturalKey == null)
					naturalKey = (NaturalKey) specialKeyLookup.get(name);

				if (naturalKey == null)
					throw new ParseException();
				
				break;
			} 					
		}
		
		return new KeyStroke(modifierKeys, naturalKey);
	}

	private SortedSet modifierKeys;
	private NaturalKey naturalKey;

	private transient int hashCode;
	private transient boolean hashCodeComputed;
	private transient ModifierKey[] modifierKeysAsArray;
	private transient String string;
	
	private KeyStroke(SortedSet modifierKeys, NaturalKey naturalKey) {
		if (naturalKey == null)
			throw new NullPointerException();

		this.modifierKeys = Util.safeCopy(modifierKeys, ModifierKey.class);
		this.naturalKey = naturalKey;		
		this.modifierKeysAsArray = (ModifierKey[]) this.modifierKeys.toArray(new ModifierKey[modifierKeys.size()]);
	}

	public int compareTo(Object object) {
		KeyStroke keyStroke = (KeyStroke) object;
		int compareTo = Util.compare((Comparable[]) modifierKeysAsArray, (Comparable[]) keyStroke.modifierKeysAsArray);
		
		if (compareTo == 0)
			compareTo = naturalKey.compareTo(keyStroke.naturalKey);			
			
		return compareTo;	
	}
	
	public boolean equals(Object object) {
		if (!(object instanceof KeyStroke))
			return false;

		KeyStroke keyStroke = (KeyStroke) object;	
		boolean equals = true;
		equals &= modifierKeys.equals(keyStroke.modifierKeys);
		equals &= naturalKey.equals(keyStroke.naturalKey);		
		return equals;
	}

	/**
	 * JAVADOC
	 * 
	 * @return
	 */
	public String format() {
		return format(true);
	}

	/**
	 * JAVADOC
	 * 
	 * @return
	 */
	public Set getModifierKeys() {
		return Collections.unmodifiableSet(modifierKeys);
	}

	/**
	 * JAVADOC
	 * 
	 * @return
	 */
	public NaturalKey getNonModifierKey() {
		return naturalKey;
	}

	public int hashCode() {
		if (!hashCodeComputed) {
			hashCode = HASH_INITIAL;
			hashCode = hashCode * HASH_FACTOR + modifierKeys.hashCode();
			hashCode = hashCode * HASH_FACTOR + naturalKey.hashCode();
			hashCodeComputed = true;
		}
			
		return hashCode;
	}

	public String toString() {
		if (string == null)
			string = format(false);
	
		return string;
	}

	private String format(boolean localize) {
		Iterator iterator = modifierKeys.iterator();
		StringBuffer stringBuffer = new StringBuffer();
	
		while (iterator.hasNext()) {
			stringBuffer.append(iterator.next().toString());
			
			if (localize)
				stringBuffer.append(Util.getString(RESOURCE_BUNDLE, KEY_DELIMITER_KEY));
			else
				stringBuffer.append(KEY_DELIMITER);
		}

		String name = naturalKey.toString();
		String value;

		if ("\b".equals(name)) //$NON-NLS-1$
			value = BS;
		else if ("\t".equals(name)) //$NON-NLS-1$
			value = TAB;
		else if ("\n".equals(name)) //$NON-NLS-1$
			value = LF;
		else if ("\f".equals(name)) //$NON-NLS-1$
			value = FF;
		else if ("\r".equals(name)) //$NON-NLS-1$	
			value = CR;
		else if ("\u001b".equals(name)) //$NON-NLS-1$	
			value = ESC;
		else if (" ".equals(name)) //$NON-NLS-1$	
			value = SPACE;
		else if ("+".equals(name)) //$NON-NLS-1$	
			value = PLUS;
		else if ("\u007F".equals(name)) //$NON-NLS-1$	
			value = DEL;
		else
			value = name;
		
		stringBuffer.append(localize ? Util.getString(RESOURCE_BUNDLE, value) : value);		
		return stringBuffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4277.java