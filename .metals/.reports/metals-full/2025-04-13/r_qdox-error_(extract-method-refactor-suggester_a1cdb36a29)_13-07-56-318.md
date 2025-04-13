error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8464.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8464.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8464.java
text:
```scala
S@@ystem.out.print("HANDLERS >>> Command('" + id //$NON-NLS-1$

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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.ui.commands.CommandEvent;
import org.eclipse.ui.commands.ExecutionException;
import org.eclipse.ui.commands.ICommand;
import org.eclipse.ui.commands.ICommandListener;
import org.eclipse.ui.commands.IHandler;
import org.eclipse.ui.commands.IKeySequenceBinding;
import org.eclipse.ui.commands.NotDefinedException;
import org.eclipse.ui.commands.NotHandledException;
import org.eclipse.ui.internal.misc.Policy;
import org.eclipse.ui.internal.util.Util;

final class Command implements ICommand {

    /**
     * Whether commands should print out information about handler changes.
     */
    private static final boolean DEBUG_HANDLERS = Policy.DEBUG_HANDLERS
            && Policy.DEBUG_HANDLERS_VERBOSE;

    /**
     * Which command should print out debugging information.
     */
    private static final String DEBUG_HANDLERS_COMMAND_ID = Policy.DEBUG_HANDLERS_VERBOSE_COMMAND_ID;

    private final static int HASH_FACTOR = 89;

    private final static int HASH_INITIAL = Command.class.getName().hashCode();

    private String categoryId;

    private List commandListeners;

    private Set commandsWithListeners;

    private boolean defined;

    private String description;

    private IHandler handler;

    private transient int hashCode;

    private transient boolean hashCodeComputed;

    private String id;

    private List keySequenceBindings;

    private IKeySequenceBinding[] keySequenceBindingsAsArray;

    private String name;

    private transient String string;

    Command(Set commandsWithListeners, String id) {
        if (commandsWithListeners == null || id == null)
                throw new NullPointerException();
        this.commandsWithListeners = commandsWithListeners;
        this.id = id;
    }

    public void addCommandListener(ICommandListener commandListener) {
        if (commandListener == null) throw new NullPointerException();
        if (commandListeners == null) commandListeners = new ArrayList();
        if (!commandListeners.contains(commandListener))
                commandListeners.add(commandListener);
        commandsWithListeners.add(this);
    }

    public int compareTo(Object object) {
        Command castedObject = (Command) object;
        int compareTo = Util.compare(categoryId, castedObject.categoryId);
        if (compareTo == 0) {
            compareTo = Util.compare(defined, castedObject.defined);
            if (compareTo == 0) {
                compareTo = Util.compare(description, castedObject.description);
                if (compareTo == 0) {
                    compareTo = Util.compare(handler, castedObject.handler);
                    if (compareTo == 0) {
                        compareTo = Util.compare(id, castedObject.id);
                        if (compareTo == 0) {
                            compareTo = Util.compare(name, castedObject.name);
                        }
                    }
                }
            }
        }
        return compareTo;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Command)) return false;
        Command castedObject = (Command) object;
        boolean equals = true;
        equals &= Util.equals(categoryId, castedObject.categoryId);
        equals &= Util.equals(defined, castedObject.defined);
        equals &= Util.equals(description, castedObject.description);
        equals &= Util.equals(handler, castedObject.handler);
        equals &= Util.equals(id, castedObject.id);
        equals &= Util.equals(keySequenceBindings,
                castedObject.keySequenceBindings);
        equals &= Util.equals(name, castedObject.name);
        return equals;
    }

    public Object execute(Map parameterValuesByName) throws ExecutionException,
            NotHandledException {
        IHandler handler = this.handler;
        if (handler != null)
            return handler.execute(parameterValuesByName);
        else {
            throw new NotHandledException("There is no handler to execute."); //$NON-NLS-1$
        }
    }

    void fireCommandChanged(CommandEvent commandEvent) {
        if (commandEvent == null) throw new NullPointerException();
        if (commandListeners != null)
                for (int i = 0; i < commandListeners.size(); i++)
                    ((ICommandListener) commandListeners.get(i))
                            .commandChanged(commandEvent);
    }

    public Map getAttributeValuesByName() throws NotHandledException {
        IHandler handler = this.handler;
        if (handler != null)
            return handler.getAttributeValuesByName();
        else
            throw new NotHandledException(
                    "There is no handler from which to retrieve attributes."); //$NON-NLS-1$
    }

    public String getCategoryId() throws NotDefinedException {
        if (!defined)
                throw new NotDefinedException(
                        "Cannot get category identifier from an undefined command."); //$NON-NLS-1$
        return categoryId;
    }

    public String getDescription() throws NotDefinedException {
        if (!defined)
                throw new NotDefinedException(
                        "Cannot get a description from an undefined command."); //$NON-NLS-1$
        return description;
    }

    public String getId() {
        return id;
    }

    public List getKeySequenceBindings() {
        return keySequenceBindings;
    }

    public String getName() throws NotDefinedException {
        if (!defined)
                throw new NotDefinedException(
                        "Cannot get the name from an undefined command."); //$NON-NLS-1$
        return name;
    }

    public int hashCode() {
        if (!hashCodeComputed) {
            hashCode = HASH_INITIAL;
            hashCode = hashCode * HASH_FACTOR + Util.hashCode(categoryId);
            hashCode = hashCode * HASH_FACTOR + Util.hashCode(defined);
            hashCode = hashCode * HASH_FACTOR + Util.hashCode(description);
            hashCode = hashCode * HASH_FACTOR + Util.hashCode(handler);
            hashCode = hashCode * HASH_FACTOR + Util.hashCode(id);
            hashCode = hashCode * HASH_FACTOR
                    + Util.hashCode(keySequenceBindings);
            hashCode = hashCode * HASH_FACTOR + Util.hashCode(name);
            hashCodeComputed = true;
        }
        return hashCode;
    }

    public boolean isDefined() {
        return defined;
    }

    public boolean isHandled() {
        if (handler == null) return false;
        Map attributeValuesByName = handler.getAttributeValuesByName();
        if (attributeValuesByName.containsKey("handled") //$NON-NLS-1$
                && !Boolean.TRUE.equals(attributeValuesByName.get("handled"))) //$NON-NLS-1$
            return false;
        else
            return true;
    }

    public void removeCommandListener(ICommandListener commandListener) {
        if (commandListener == null) throw new NullPointerException();
        if (commandListeners != null) commandListeners.remove(commandListener);
        if (commandListeners.isEmpty()) commandsWithListeners.remove(this);
    }

    boolean setCategoryId(String categoryId) {
        if (!Util.equals(categoryId, this.categoryId)) {
            this.categoryId = categoryId;
            hashCodeComputed = false;
            hashCode = 0;
            string = null;
            return true;
        }
        return false;
    }

    boolean setDefined(boolean defined) {
        if (defined != this.defined) {
            this.defined = defined;
            hashCodeComputed = false;
            hashCode = 0;
            string = null;
            return true;
        }
        return false;
    }

    boolean setDescription(String description) {
        if (!Util.equals(description, this.description)) {
            this.description = description;
            hashCodeComputed = false;
            hashCode = 0;
            string = null;
            return true;
        }
        return false;
    }

    boolean setHandler(IHandler handler) {
        if (handler != this.handler) {
            this.handler = handler;
            hashCodeComputed = false;
            hashCode = 0;
            string = null;
            if ((DEBUG_HANDLERS)
                    && ((DEBUG_HANDLERS_COMMAND_ID == null) || (DEBUG_HANDLERS_COMMAND_ID
                            .equals(id)))) {
                System.out.print("HANDLERS >> Command('" + id //$NON-NLS-1$
                        + "' has changed to "); //$NON-NLS-1$
                if (handler == null) {
                    System.out.println("no handler"); //$NON-NLS-1$
                } else {
                    System.out.print("'"); //$NON-NLS-1$
                    System.out.print(handler);
                    System.out.println("' as its handler"); //$NON-NLS-1$
                }
            }
            return true;
        }
        return false;
    }

    boolean setKeySequenceBindings(List keySequenceBindings) {
        keySequenceBindings = Util.safeCopy(keySequenceBindings,
                IKeySequenceBinding.class);
        if (!Util.equals(keySequenceBindings, this.keySequenceBindings)) {
            this.keySequenceBindings = keySequenceBindings;
            this.keySequenceBindingsAsArray = (IKeySequenceBinding[]) this.keySequenceBindings
                    .toArray(new IKeySequenceBinding[this.keySequenceBindings.size()]);
            hashCodeComputed = false;
            hashCode = 0;
            string = null;
            return true;
        }
        return false;
    }

    boolean setName(String name) {
        if (!Util.equals(name, this.name)) {
            this.name = name;
            hashCodeComputed = false;
            hashCode = 0;
            string = null;
            return true;
        }
        return false;
    }

    public String toString() {
        if (string == null) {
            final StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append('[');
            stringBuffer.append(categoryId);
            stringBuffer.append(',');
            stringBuffer.append(defined);
            stringBuffer.append(',');
            stringBuffer.append(description);
            stringBuffer.append(',');
            stringBuffer.append(handler);
            stringBuffer.append(',');
            stringBuffer.append(id);
            stringBuffer.append(',');
            stringBuffer.append(keySequenceBindings);
            stringBuffer.append(',');
            stringBuffer.append(name);
            stringBuffer.append(']');
            string = stringBuffer.toString();
        }
        return string;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8464.java