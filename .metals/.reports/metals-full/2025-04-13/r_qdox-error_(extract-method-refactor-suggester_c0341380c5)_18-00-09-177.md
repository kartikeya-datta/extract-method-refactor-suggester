error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12205.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12205.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12205.java
text:
```scala
private static final i@@nt CONTROL_MASK =Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.gui.action;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

/*
 * Collect all the keystrokes together in one place.
 * This helps to ensure that there are no duplicates.
 */
public final class KeyStrokes {
    // Prevent instantiation
    private KeyStrokes(){
    }
    
    // Bug 47064 - fixes for Mac LAF
    private static int CONTROL_MASK =Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    
    public static final KeyStroke CUT = KeyStroke.getKeyStroke(KeyEvent.VK_X, CONTROL_MASK);
    public static final KeyStroke COPY = KeyStroke.getKeyStroke(KeyEvent.VK_C, CONTROL_MASK);
    public static final KeyStroke PASTE = KeyStroke.getKeyStroke(KeyEvent.VK_V, CONTROL_MASK);
    public static final KeyStroke REMOVE = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
    public static final KeyStroke SAVE_GRAPHICS = KeyStroke.getKeyStroke(KeyEvent.VK_G, CONTROL_MASK);
    public static final KeyStroke SAVE_GRAPHICS_ALL = KeyStroke.getKeyStroke(KeyEvent.VK_G, CONTROL_MASK | KeyEvent.SHIFT_DOWN_MASK);
    public static final KeyStroke HELP = KeyStroke.getKeyStroke(KeyEvent.VK_H, CONTROL_MASK);
    public static final KeyStroke WHAT_CLASS = KeyStroke.getKeyStroke(KeyEvent.VK_W, CONTROL_MASK);
    public static final KeyStroke DEBUG_ON = KeyStroke.getKeyStroke(KeyEvent.VK_D, CONTROL_MASK | KeyEvent.SHIFT_DOWN_MASK);
    public static final KeyStroke DEBUG_OFF = KeyStroke.getKeyStroke(KeyEvent.VK_D, CONTROL_MASK);
    public static final KeyStroke FUNCTIONS = KeyStroke.getKeyStroke(KeyEvent.VK_F, CONTROL_MASK);
    public static final KeyStroke SSL_MANAGER = KeyStroke.getKeyStroke(KeyEvent.VK_M, CONTROL_MASK);
    public static final KeyStroke ACTION_START = KeyStroke.getKeyStroke(KeyEvent.VK_R, CONTROL_MASK);
    public static final KeyStroke ACTION_STOP = KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, CONTROL_MASK);
    public static final KeyStroke ACTION_SHUTDOWN = KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, CONTROL_MASK);
    public static final KeyStroke CLEAR = KeyStroke.getKeyStroke(KeyEvent.VK_E, CONTROL_MASK|KeyEvent.SHIFT_DOWN_MASK);
    public static final KeyStroke CLEAR_ALL = KeyStroke.getKeyStroke(KeyEvent.VK_E, CONTROL_MASK);
    public static final KeyStroke REMOTE_START_ALL = KeyStroke.getKeyStroke(KeyEvent.VK_R, CONTROL_MASK | KeyEvent.SHIFT_DOWN_MASK);
    public static final KeyStroke REMOTE_STOP_ALL = KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.ALT_DOWN_MASK);
    public static final KeyStroke SAVE = KeyStroke.getKeyStroke(KeyEvent.VK_S, CONTROL_MASK);
    public static final KeyStroke SAVE_ALL_AS = KeyStroke.getKeyStroke(KeyEvent.VK_S, CONTROL_MASK | KeyEvent.SHIFT_DOWN_MASK);
    public static final KeyStroke OPEN = KeyStroke.getKeyStroke(KeyEvent.VK_O, CONTROL_MASK);
    public static final KeyStroke CLOSE = KeyStroke.getKeyStroke(KeyEvent.VK_L, CONTROL_MASK);
    public static final KeyStroke EXIT = KeyStroke.getKeyStroke(KeyEvent.VK_Q, CONTROL_MASK);
    public static final KeyStroke COLLAPSE_ALL = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, CONTROL_MASK);
    // VK_PLUS + CTRL_DOWN_MASK did not work...
    public static final KeyStroke EXPAND_ALL = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, CONTROL_MASK | KeyEvent.SHIFT_DOWN_MASK);


    /**
     * Check if an event matches the KeyStroke definition.
     *
     * @param e event
     * @param k keystroke
     * @return true if event matches the keystroke definition
     */
    public static boolean matches(KeyEvent e, KeyStroke k){
        final int modifiersEx = e.getModifiersEx()  | e.getModifiers();// Hack to get full modifier value
        return e.getKeyCode() == k.getKeyCode() && modifiersEx == k.getModifiers();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12205.java