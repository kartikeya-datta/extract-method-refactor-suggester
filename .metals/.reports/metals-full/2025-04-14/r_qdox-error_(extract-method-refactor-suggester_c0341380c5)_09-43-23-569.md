error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8228.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8228.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8228.java
text:
```scala
public final b@@oolean isHandled() {

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Bruno Haible haible@ilog.fr - bug 228890 
 *******************************************************************************/
package org.eclipse.ui.internal.handlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.ExceptionHandler;

/**
 * Handles the cut command in both dialogs and windows. This handler is enabled
 * if the focus control supports the "cut" method.
 * 
 * @since 3.0
 */
public class WidgetMethodHandler extends AbstractHandler implements
		IExecutableExtension {

	/**
	 * The parameters to pass to the method this handler invokes. This handler
	 * always passes no parameters.
	 */
	protected static final Class[] NO_PARAMETERS = new Class[0];

	/**
	 * The name of the method to be invoked by this handler. This value should
	 * never be <code>null</code>.
	 */
	protected String methodName;

	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Method methodToExecute = getMethodToExecute();
		if (methodToExecute != null) {
			try {
				final Control focusControl = Display.getCurrent()
						.getFocusControl();
				if ((focusControl instanceof Composite)
						&& ((((Composite) focusControl).getStyle() & SWT.EMBEDDED) != 0)) {
					/*
					 * Okay. Have a seat. Relax a while. This is going to be a
                     * bumpy ride. If it is an embedded widget, then it *might*
                     * be a Swing widget. At the point where this handler is
					 * executing, the key event is already bound to be
					 * swallowed. If I don't do something, then the key will be
					 * gone for good. So, I will try to forward the event to the
					 * Swing widget. Unfortunately, we can't even count on the
					 * Swing libraries existing, so I need to use reflection
					 * everywhere. And, to top it off, I need to dispatch the
					 * event on the Swing event queue, which means that it will
					 * be carried out asynchronously to the SWT event queue.
					 */
					try {
						final Object focusComponent = getFocusComponent();
						if (focusComponent != null) {
							Runnable methodRunnable = new Runnable() {
								public void run() {
									try {
										methodToExecute.invoke(focusComponent,
												null);
									} catch (final IllegalAccessException e) {
										// The method is protected, so do
										// nothing.
									} catch (final InvocationTargetException e) {
										/*
										 * I would like to log this exception --
										 * and possibly show a dialog to the
										 * user -- but I have to go back to the
										 * SWT event loop to do this. So, back
										 * we go....
										 */
										focusControl.getDisplay().asyncExec(
												new Runnable() {
													public void run() {
														ExceptionHandler
																.getInstance()
																.handleException(
																		new ExecutionException(
																				"An exception occurred while executing " //$NON-NLS-1$
																						+ methodToExecute
																								.getName(),
																				e
																						.getTargetException()));
													}
												});
									}
								}
							};

							swingInvokeLater(methodRunnable);
						}
					} catch (final ClassNotFoundException e) {
						// There is no Swing support, so do nothing.

					} catch (final NoSuchMethodException e) {
						// The API has changed, which seems amazingly unlikely.
						throw new Error("Something is seriously wrong here"); //$NON-NLS-1$
					}

				} else {

					methodToExecute.invoke(focusControl, null);
				}

			} catch (IllegalAccessException e) {
				// The method is protected, so do nothing.

			} catch (InvocationTargetException e) {
				throw new ExecutionException(
						"An exception occurred while executing " //$NON-NLS-1$
								+ methodToExecute.getName(), e
								.getTargetException());

			}
		}

		return null;
	}

	/**
	 * Invoke a runnable on the swing EDT.
	 * 
	 * @param methodRunnable
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected void swingInvokeLater(Runnable methodRunnable)
			throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		final Class swingUtilitiesClass = Class
				.forName("javax.swing.SwingUtilities"); //$NON-NLS-1$
		final Method swingUtilitiesInvokeLaterMethod = swingUtilitiesClass
				.getMethod("invokeLater", //$NON-NLS-1$
						new Class[] { Runnable.class });
		swingUtilitiesInvokeLaterMethod.invoke(swingUtilitiesClass,
				new Object[] { methodRunnable });
	}

	/**
	 * Find the swing focus component, if it is available.
	 * 
	 * @return Hopefully, the swing focus component, but it can return
	 * 	<code>null</code>.
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected Object getFocusComponent() throws ClassNotFoundException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		/*
		 * Before JRE 1.4, one has to use
		 * javax.swing.FocusManager.getCurrentManager().getFocusOwner(). Since
		 * JRE 1.4, one has to use
		 * java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager
		 * ().getFocusOwner(); the use of the older API would install a
		 * LegacyGlueFocusTraversalPolicy which causes endless recursions in
		 * some situations.
		 */
		Class keyboardFocusManagerClass = null;
		try {
			keyboardFocusManagerClass = Class
					.forName("java.awt.KeyboardFocusManager"); //$NON-NLS-1$
		} catch (ClassNotFoundException e) {
			// switch to the old guy
		}
		if (keyboardFocusManagerClass != null) {
			// Use JRE 1.4 API
			final Method keyboardFocusManagerGetCurrentKeyboardFocusManagerMethod = keyboardFocusManagerClass
					.getMethod("getCurrentKeyboardFocusManager", null); //$NON-NLS-1$
			final Object keyboardFocusManager = keyboardFocusManagerGetCurrentKeyboardFocusManagerMethod
					.invoke(keyboardFocusManagerClass, null);
			final Method keyboardFocusManagerGetFocusOwner = keyboardFocusManagerClass
					.getMethod("getFocusOwner", null); //$NON-NLS-1$
			final Object focusComponent = keyboardFocusManagerGetFocusOwner
					.invoke(keyboardFocusManager, null);
			return focusComponent;
		}
		// Use JRE 1.3 API
		final Class focusManagerClass = Class
				.forName("javax.swing.FocusManager"); //$NON-NLS-1$
		final Method focusManagerGetCurrentManagerMethod = focusManagerClass
				.getMethod("getCurrentManager", null); //$NON-NLS-1$
		final Object focusManager = focusManagerGetCurrentManagerMethod
		        .invoke(focusManagerClass, null);
		final Method focusManagerGetFocusOwner = focusManagerClass
		        .getMethod("getFocusOwner", null); //$NON-NLS-1$
		final Object focusComponent = focusManagerGetFocusOwner
		        .invoke(focusManager, null);
		return focusComponent;

	}

	public final boolean isEnabled() {
		return getMethodToExecute() != null;
	}

	/**
	 * Looks up the method on the focus control.
	 * 
	 * @return The method on the focus control; <code>null</code> if none.
	 */
	protected Method getMethodToExecute() {
		Display display = Display.getCurrent();
		if (display == null)
			return null;
		final Control focusControl = display.getFocusControl();
		Method method = null;

		if (focusControl != null) {
			final Class clazz = focusControl.getClass();
			try {
				method = clazz.getMethod(methodName, NO_PARAMETERS);
			} catch (NoSuchMethodException e) {
				// Fall through...
			}
		}

		if ((method == null)
				&& (focusControl instanceof Composite)
				&& ((((Composite) focusControl).getStyle() & SWT.EMBEDDED) != 0)) {
			/*
			 * We couldn't find the appropriate method on the current focus
			 * control. It is possible that the current focus control is an
			 * embedded SWT composite, which could be containing some Swing
			 * components. If this is the case, then we should try to pass
			 * through to the underlying Swing component hierarchy. Insha'allah,
			 * this will work.
			 */
			try {
				final Object focusComponent = getFocusComponent();
				if (focusComponent != null) {
					final Class clazz = focusComponent.getClass();

					try {
						method = clazz.getMethod(methodName, NO_PARAMETERS);
					} catch (NoSuchMethodException e) {
						// Do nothing.
					}
				}
			} catch (final ClassNotFoundException e) {
				// There is no Swing support, so do nothing.

			} catch (final NoSuchMethodException e) {
				// The API has changed, which seems amazingly unlikely.
				throw new Error("Something is seriously wrong here"); //$NON-NLS-1$
			} catch (IllegalAccessException e) {
				// The API has changed, which seems amazingly unlikely.
				throw new Error("Something is seriously wrong here"); //$NON-NLS-1$
			} catch (InvocationTargetException e) {
				// The API has changed, which seems amazingly unlikely.
				throw new Error("Something is seriously wrong here"); //$NON-NLS-1$
			}
		}

		return method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org
	 * .eclipse.core.runtime.IConfigurationElement, java.lang.String,
	 * java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) {
		// The data is really just a string (i.e., the method name).
		methodName = data.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8228.java