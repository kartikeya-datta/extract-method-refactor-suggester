error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4356.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4356.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4356.java
text:
```scala
(@@(PersistentState) state).save(store, preferenceKey);

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

package org.eclipse.ui.internal.commands;

import org.eclipse.core.commands.IStateListener;
import org.eclipse.core.commands.State;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.commands.PersistentState;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * <p>
 * A proxy for handler state that has been defined in XML. This delays the class
 * loading until the state is really asked for information. Asking a proxy for
 * anything (except disposing, and adding and removing listeners) will cause the
 * proxy to instantiate the proxied handler.
 * </p>
 * <p>
 * Loading the proxied state will automatically cause it to load its value from
 * the preference store. Disposing of the state will cause it to persist its
 * value.
 * </p>
 * <p>
 * <strong>EXPERIMENTAL</strong>. This class or interface has been added as
 * part of a work in progress. There is a guarantee neither that this API will
 * work nor that it will remain the same. Please do not use this API without
 * consulting with the Platform/UI team.
 * </p>
 * 
 * @since 3.2
 */
public final class CommandStateProxy extends PersistentState {

	/**
	 * The configuration element from which the state can be created. This value
	 * will exist until the element is converted into a real class -- at which
	 * point this value will be set to <code>null</code>.
	 */
	private IConfigurationElement configurationElement;

	/**
	 * The key in the preference store to locate the persisted state.
	 */
	private String preferenceKey;

	/**
	 * The preference store containing the persisted state, if any.
	 */
	private IPreferenceStore preferenceStore;

	/**
	 * The real state. This value is <code>null</code> until the proxy is
	 * forced to load the real state. At this point, the configuration element
	 * is converted, nulled out, and this state gains a reference.
	 */
	private State state = null;

	/**
	 * The name of the configuration element attribute which contains the
	 * information necessary to instantiate the real state.
	 */
	private final String stateAttributeName;

	/**
	 * Constructs a new instance of <code>HandlerState</code> with all the
	 * information it needs to create the real state later.
	 * 
	 * @param configurationElement
	 *            The configuration element from which the real class can be
	 *            loaded at run-time; must not be <code>null</code>.
	 * @param stateAttributeName
	 *            The name of the attribute or element containing the state
	 *            executable extension; must not be <code>null</code>.
	 * @param preferenceStore
	 *            The preference store to which any persistent data should be
	 *            written, and from which it should be loaded; may be
	 *            <code>null</code>.
	 * @param preferenceKey
	 *            The key at which the persistent data is located within the
	 *            preference store.
	 */
	public CommandStateProxy(final IConfigurationElement configurationElement,
			final String stateAttributeName,
			final IPreferenceStore preferenceStore, final String preferenceKey) {

		if (configurationElement == null) {
			throw new NullPointerException(
					"The configuration element backing a state proxy cannot be null"); //$NON-NLS-1$
		}

		if (stateAttributeName == null) {
			throw new NullPointerException(
					"The attribute containing the state class must be known"); //$NON-NLS-1$
		}

		this.configurationElement = configurationElement;
		this.stateAttributeName = stateAttributeName;
		this.preferenceKey = preferenceKey;
		this.preferenceStore = preferenceStore;
	}

	public final void addListener(final IStateListener listener) {
		if (state == null) {
			addListenerObject(listener);
		} else {
			state.addListener(listener);
		}
	}

	public final void dispose() {
		if (state != null) {
			state.dispose();
			if (state instanceof PersistentState) {
				final PersistentState persistableState = (PersistentState) state;
				if (persistableState.shouldPersist() && preferenceStore != null
						&& preferenceKey != null) {
					persistableState.save(preferenceStore, preferenceKey);
				}
			}
		}
	}

	public final Object getValue() {
		if (loadState()) {
			return state.getValue();
		}

		return null;
	}

	public final void load(final IPreferenceStore store,
			final String preferenceKey) {
		if (loadState() && state instanceof PersistentState) {
			final PersistentState persistableState = (PersistentState) state;
			if (persistableState.shouldPersist() && preferenceStore != null
					&& preferenceKey != null) {
				persistableState.load(preferenceStore, preferenceKey);
			}
		}
	}

	/**
	 * Loads the state, if possible. If the state is loaded, then the member
	 * variables are updated accordingly and the state is told to load its value
	 * from the preference store.
	 * 
	 * @return <code>true</code> if the state is now non-null;
	 *         <code>false</code> otherwise.
	 */
	private final boolean loadState() {
		if (state == null) {
			try {
				state = (State) configurationElement
						.createExecutableExtension(stateAttributeName);
				state.setId(getId());
				configurationElement = null;

				// Try to load the persistent state, if possible.
				load(preferenceStore, preferenceKey);

				// Transfer the local listeners to the real state.
				final Object[] listenerArray = getListeners();
				for (int i = 0; i < listenerArray.length; i++) {
					state.addListener((IStateListener) listenerArray[i]);
				}
				clearListeners();

				return true;

			} catch (final ClassCastException e) {
				final String message = "The proxied state was the wrong class"; //$NON-NLS-1$
				final IStatus status = new Status(IStatus.ERROR,
						WorkbenchPlugin.PI_WORKBENCH, 0, message, e);
				WorkbenchPlugin.log(message, status);
				return false;

			} catch (final CoreException e) {
				final String message = "The proxied state for '" + configurationElement.getAttribute(stateAttributeName) //$NON-NLS-1$
						+ "' could not be loaded"; //$NON-NLS-1$
				IStatus status = new Status(IStatus.ERROR,
						WorkbenchPlugin.PI_WORKBENCH, 0, message, e);
				WorkbenchPlugin.log(message, status);
				return false;
			}
		}

		return true;
	}

	public final void removeListener(final IStateListener listener) {
		if (state == null) {
			removeListenerObject(listener);
		} else {
			state.removeListener(listener);
		}
	}

	public final void save(final IPreferenceStore store,
			final String preferenceKey) {
		if (loadState() && state instanceof PersistentState) {
			((PersistentState) state).load(store, preferenceKey);
		}
	}

	public final void setId(final String id) {
		super.setId(id);
		if (state != null) {
			state.setId(id);
		}
	}

	public final void setShouldPersist(final boolean persisted) {
		if (loadState() && state instanceof PersistentState) {
			((PersistentState) state).setShouldPersist(persisted);
		}
	}
	
	public final void setValue(final Object value) {
		if (loadState()) {
			state.setValue(value);
		}
	}

	public final boolean shouldPersist() {
		if (loadState() && state instanceof PersistentState) {
			return ((PersistentState) state).shouldPersist();
		}

		return false;
	}

	public final String toString() {
		if (state == null) {
			return configurationElement.getAttribute(stateAttributeName);
		}

		return state.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4356.java