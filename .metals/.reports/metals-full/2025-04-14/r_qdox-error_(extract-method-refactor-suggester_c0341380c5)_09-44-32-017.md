error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8308.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8308.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 616
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8308.java
text:
```scala
public class WorkbenchBrowserSupport extends AbstractWorkbenchBrowserSupport {

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
p@@ackage org.eclipse.ui.internal.browser;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.dynamicHelpers.IExtensionChangeHandler;
import org.eclipse.core.runtime.dynamicHelpers.IExtensionTracker;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.AbstractWorkbenchBrowserSupport;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * Implements the support interface and delegates the calls to the active
 * support if contributed via the extension point, or the default support
 * otherwise.
 * 
 * @since 3.1
 */
public class WorkbenchBrowserSupport implements IWorkbenchBrowserSupport {
	private static final String BROWSER_SUPPORT_EXTENSION_ID = "org.eclipse.ui.browserSupport"; //$NON-NLS-1$

	private static final String EL_SUPPORT = "support"; //$NON-NLS-1$	

	private static final String ATT_CLASS = "class"; //$NON-NLS-1$

	private static final String ATT_DEFAULT = "default"; //$NON-NLS-1$

	private static WorkbenchBrowserSupport instance;

	private IWorkbenchBrowserSupport activeSupport;

	private boolean initialized;

	private IExtensionChangeHandler handler = new IExtensionChangeHandler() {
        
        /* (non-Javadoc)
         * @see org.eclipse.core.runtime.dynamicHelpers.IExtensionChangeHandler#addExtension(org.eclipse.core.runtime.dynamicHelpers.IExtensionTracker, org.eclipse.core.runtime.IExtension)
         */
        public void addExtension(IExtensionTracker tracker,IExtension extension) {
            //Do nothing
        }

        /* (non-Javadoc)
         * @see org.eclipse.core.runtime.dynamicHelpers.IExtensionChangeHandler#removeExtension(org.eclipse.core.runtime.IExtension, java.lang.Object[])
         */
        public void removeExtension(IExtension source, Object[] objects) {
			for (int i = 0; i < objects.length; i++) {
				if (objects[i] == activeSupport) {
					activeSupport = null;
					initialized = false;
					// remove ourselves - we'll be added again in initalize if
					// needed
					PlatformUI.getWorkbench().getExtensionTracker()
							.unregisterHandler(handler);
				}
			}
		}
	};

	/**
	 * 
	 */
	private WorkbenchBrowserSupport() {
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return shared instance
	 */
	public static IWorkbenchBrowserSupport getInstance() {
		if (instance == null) {
			instance = new WorkbenchBrowserSupport();
		}
		return instance;
	}

	public IWebBrowser createBrowser(int style, String browserId, String name,
			String tooltip) throws PartInitException {
		return getActiveSupport()
				.createBrowser(style, browserId, name, tooltip);
	}

	public IWebBrowser createBrowser(String browserId) throws PartInitException {
		return getActiveSupport().createBrowser(browserId);
	}

	private IWorkbenchBrowserSupport getActiveSupport() {
		if (initialized == false) {
			loadActiveSupport();
		}
		// ensure we always have an active instance
		if (activeSupport == null)
			activeSupport = new DefaultWorkbenchBrowserSupport();
		return activeSupport;
	}

	private void loadActiveSupport() {
		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				// get the help UI extension from the registry
				IExtensionPoint point = Platform.getExtensionRegistry()
						.getExtensionPoint(BROWSER_SUPPORT_EXTENSION_ID);
				if (point == null) {
					// our extension point is missing (!) - act like there was
					// no browser support
					return;
				}
				IExtension[] extensions = point.getExtensions();
				if (extensions.length == 0) {
					// no browser support present
					return;
				}

				IConfigurationElement elementToUse = null;
				elementToUse = getElementToUse(extensions);
				if (elementToUse != null)
					initialized = initializePluggableBrowserSupport(elementToUse);
			}

			private IConfigurationElement getElementToUse(
					IExtension[] extensions) {
				IConfigurationElement[] elements = extensions[0]
						.getConfigurationElements();
				if (elements.length == 0) {
					// help UI present but mangled - act like there was no
					// help
					// UI
					return null;
				}
				IConfigurationElement defaultElement = null;
				IConfigurationElement choice = null;
				// find the first default element and
				// the first non-default element. If non-default
				// is found, pick it. Otherwise, use default.
				for (int i = 0; i < elements.length; i++) {
					IConfigurationElement element = elements[i];
					if (element.getName().equals(EL_SUPPORT)) {
						String def = element.getAttribute(ATT_DEFAULT);
						if (def != null && def.equalsIgnoreCase("true")) { //$NON-NLS-1$
							if (defaultElement == null)
								defaultElement = element;
						} else {
							// non-default
							if (choice == null)
								choice = element;
						}
					}
				}
				if (choice == null)
					choice = defaultElement;
				return choice;
			}

			private boolean initializePluggableBrowserSupport(
					IConfigurationElement element) {
				// Instantiate the browser support
				try {
					activeSupport = (AbstractWorkbenchBrowserSupport) WorkbenchPlugin
							.createExtension(element, ATT_CLASS);
					// start listening for removals
					PlatformUI.getWorkbench().getExtensionTracker()
							.registerHandler(handler, null);
					// register the new browser support for removal
					// notification
					PlatformUI.getWorkbench().getExtensionTracker()
							.registerObject(element.getDeclaringExtension(),
									activeSupport, IExtensionTracker.REF_WEAK);
					return true;
				} catch (CoreException e) {
					WorkbenchPlugin
							.log("Unable to instantiate browser support" + e.getStatus(), e);//$NON-NLS-1$
				}
				return false;
			}

		});
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8308.java