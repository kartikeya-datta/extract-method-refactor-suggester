error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7533.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7533.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7533.java
text:
```scala
final N@@aturalKey naturalKey = keyStroke.getNaturalKey();

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

import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.swt.SWT;

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
public final class KeySupport {

    /**
     * JAVADOC
     * 
     * @param key
     * @return
     */
    public static KeyStroke convertFromSWT(int key) {
        final SortedSet modifierKeys = new TreeSet();
        NaturalKey naturalKey = null;

        if ((key & SWT.ALT) != 0)
            modifierKeys.add(ModifierKey.ALT);

        if ((key & SWT.COMMAND) != 0)
            modifierKeys.add(ModifierKey.COMMAND);

        if ((key & SWT.CTRL) != 0)
            modifierKeys.add(ModifierKey.CTRL);

        if ((key & SWT.SHIFT) != 0)
            modifierKeys.add(ModifierKey.SHIFT);

		key &= SWT.KEY_MASK;

		switch (key) {
            case SWT.ARROW_DOWN :
                naturalKey = SpecialKey.ARROW_DOWN;
                break;
            case SWT.ARROW_LEFT :
                naturalKey = SpecialKey.ARROW_LEFT;
                break;
            case SWT.ARROW_RIGHT :
                naturalKey = SpecialKey.ARROW_RIGHT;
                break;
            case SWT.ARROW_UP :
                naturalKey = SpecialKey.ARROW_UP;
                break;
            case SWT.END :
                naturalKey = SpecialKey.END;
                break;
            case SWT.F1 :
                naturalKey = SpecialKey.F1;
                break;
            case SWT.F10 :
                naturalKey = SpecialKey.F10;
                break;
            case SWT.F11 :
                naturalKey = SpecialKey.F11;
                break;
            case SWT.F12 :
                naturalKey = SpecialKey.F12;
                break;
            case SWT.F2 :
                naturalKey = SpecialKey.F2;
                break;
            case SWT.F3 :
                naturalKey = SpecialKey.F3;
                break;
            case SWT.F4 :
                naturalKey = SpecialKey.F4;
                break;
            case SWT.F5 :
                naturalKey = SpecialKey.F5;
                break;
            case SWT.F6 :
                naturalKey = SpecialKey.F6;
                break;
            case SWT.F7 :
                naturalKey = SpecialKey.F7;
                break;
            case SWT.F8 :
                naturalKey = SpecialKey.F8;
                break;
            case SWT.F9 :
                naturalKey = SpecialKey.F9;
                break;
            case SWT.HOME :
                naturalKey = SpecialKey.HOME;
                break;
            case SWT.INSERT :
                naturalKey = SpecialKey.INSERT;
                break;
            case SWT.PAGE_DOWN :
                naturalKey = SpecialKey.PAGE_DOWN;
                break;
            case SWT.PAGE_UP :
                naturalKey = SpecialKey.PAGE_UP;
                break;
            default:
            	naturalKey = CharacterKey.getInstance((char) (key & 0xFFFF));
        }

        return KeyStroke.getInstance(modifierKeys, naturalKey);
    }

    /**
     * Converts a KeyStroke object (used by the Platform-UI) to an integer value
     * that will be recognized by SWT as the matching SWT key stroke.  This is a
     * utility method to convert between the two formats.
     * 
     * @param keyStroke The key stroke to be converted; must not be 
     * <code>null</code>.
     * @return The matching SWT key stroke integer.
     */
    public static final int convertToSWT(final KeyStroke keyStroke) {
    	if (keyStroke == null)
    		throw new NullPointerException();
    	
        int key = 0;
        final Iterator iterator = keyStroke.getModifierKeys().iterator();
        
        while (iterator.hasNext()) {
            final ModifierKey modifierKey = (ModifierKey) iterator.next();
            
            if (modifierKey == ModifierKey.ALT)
                key |= SWT.ALT;
			else if (modifierKey == ModifierKey.COMMAND)
                key |= SWT.COMMAND;
			else if (modifierKey == ModifierKey.CTRL)
                key |= SWT.CTRL;
            else if (modifierKey == ModifierKey.SHIFT)
                key |= SWT.SHIFT;
        }

        final NaturalKey naturalKey = keyStroke.getNonModifierKey();
        
        if (naturalKey instanceof CharacterKey)
            key |= ((CharacterKey) naturalKey).getCharacter();
		else if (naturalKey instanceof SpecialKey) {
            final SpecialKey specialKey = (SpecialKey) naturalKey;
            
            if (specialKey == SpecialKey.ARROW_DOWN)
                key |= SWT.ARROW_DOWN;
            else if (specialKey == SpecialKey.ARROW_LEFT)
                key |= SWT.ARROW_LEFT;
            else if (specialKey == SpecialKey.ARROW_RIGHT)
                key |= SWT.ARROW_RIGHT;
            else if (specialKey == SpecialKey.ARROW_UP)
                key |= SWT.ARROW_UP;
            else if (specialKey == SpecialKey.END)
                key |= SWT.END;
            else if (specialKey == SpecialKey.F1)
                key |= SWT.F1;
            else if (specialKey == SpecialKey.F10)
                key |= SWT.F10;
            else if (specialKey == SpecialKey.F11)
                key |= SWT.F11;
            else if (specialKey == SpecialKey.F12)
                key |= SWT.F12;
            else if (specialKey == SpecialKey.F2)
                key |= SWT.F2;
            else if (specialKey == SpecialKey.F3)
                key |= SWT.F3;
            else if (specialKey == SpecialKey.F4)
                key |= SWT.F4;
            else if (specialKey == SpecialKey.F5)
                key |= SWT.F5;
            else if (specialKey == SpecialKey.F6)
                key |= SWT.F6;
            else if (specialKey == SpecialKey.F7)
                key |= SWT.F7;
            else if (specialKey == SpecialKey.F8)
                key |= SWT.F8;
            else if (specialKey == SpecialKey.F9)
                key |= SWT.F9;
            else if (specialKey == SpecialKey.HOME)
                key |= SWT.HOME;
            else if (specialKey == SpecialKey.INSERT)
                key |= SWT.INSERT;
            else if (specialKey == SpecialKey.PAGE_DOWN)
                key |= SWT.PAGE_DOWN;
            else if (specialKey == SpecialKey.PAGE_UP)
                key |= SWT.PAGE_UP;
        }

        return key;
    }

	static String translateString(ResourceBundle resourceBundle, String key, String string) {
		if (resourceBundle != null && key != null)
			try {
				final String translatedString = resourceBundle.getString(key);
				
				if (translatedString != null)
					return translatedString.trim();
			} catch (MissingResourceException eMissingResource) {
			}

		return string;
	}

    private KeySupport() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7533.java