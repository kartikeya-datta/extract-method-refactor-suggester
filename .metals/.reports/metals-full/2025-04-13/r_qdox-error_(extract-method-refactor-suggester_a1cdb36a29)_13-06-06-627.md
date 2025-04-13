error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12433.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12433.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12433.java
text:
```scala
i@@f (!path.startsWith("/"))buf.append("/"); //$NON-NLS-1$ //$NON-NLS-2$

/****************************************************************************
 * Copyright (c) 2008 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/
package org.eclipse.ecf.discovery.ui;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.discovery.IServiceInfo;
import org.eclipse.ecf.discovery.IServiceProperties;
import org.eclipse.ecf.discovery.identity.IServiceID;
import org.eclipse.ecf.discovery.ui.views.IServiceAccessHandler;
import org.eclipse.ecf.internal.discovery.ui.Activator;
import org.eclipse.ecf.internal.discovery.ui.Messages;
import org.eclipse.jface.action.*;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

/**
 * This is a service access handler for handling the http/http service.  The associated properties
 * for this service are documented at <a href="http://www.dns-sd.org/ServiceTypes.html">http://www.dns-sd.org/ServiceTypes.html</a>.
 */
public class UrlServiceAccessHandler implements IServiceAccessHandler {

	private static final String RFC2782_PATH = "path"; //$NON-NLS-1$
	//private static final String RFC2782_USERNAME = "u"; //$NON-NLS-1$
	//private static final String RFC2782_PASSWORD = "p"; //$NON-NLS-1$
	static final IContributionItem[] EMPTY_CONTRIBUTION = {};

	public UrlServiceAccessHandler() {
		// nothing to do
	}

	public IContributionItem[] getContributionsForService(IServiceInfo serviceInfo) {
		IServiceID serviceID = serviceInfo.getServiceID();
		List serviceTypes = Arrays.asList(serviceID.getServiceTypeID().getServices());
		String protocol = null;
		if (serviceTypes.size() == 1 && serviceTypes.contains("http")) //$NON-NLS-1$
			protocol = "http"; //$NON-NLS-1$
		else if (serviceTypes.size() == 1 && serviceTypes.contains("https")) //$NON-NLS-1$
			protocol = "https"; //$NON-NLS-1$
		else if (serviceTypes.size() == 1 && serviceTypes.contains("ftp")) //$NON-NLS-1$
			protocol = "ftp"; //$NON-NLS-1$
		if (protocol == null)
			return EMPTY_CONTRIBUTION;
		URI location = serviceInfo.getLocation();
		StringBuffer buf = new StringBuffer(protocol);
		buf.append("://").append(location.getHost()); //$NON-NLS-1$
		if (location.getPort() != -1)
			buf.append(":").append(location.getPort()); //$NON-NLS-1$ 
		IServiceProperties svcProps = serviceInfo.getServiceProperties();
		final String path = svcProps.getPropertyString(RFC2782_PATH);
		if (path != null) {
			if (path.startsWith("/"))buf.append("/"); //$NON-NLS-1$ //$NON-NLS-2$
			buf.append(path);
		}
		final String urlString = buf.toString();
		//final String username = svcProps.getPropertyString(RFC2782_USERNAME);
		//final String password = svcProps.getPropertyString(RFC2782_PASSWORD);
		Action action = new Action() {
			public void run() {
				openBrowser(urlString);
			}
		};
		action.setText(NLS.bind(Messages.HttpServiceAccessHandler_MENU_TEXT, urlString));
		return new IContributionItem[] {new ActionContributionItem(action)};
	}

	protected void openBrowser(String urlString) {
		final IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
		try {
			support.createBrowser(null).openURL(new URL(urlString));
		} catch (final Exception e) {
			logError(Messages.HttpServiceAccessHandler_EXCEPTION_CREATEBROWSER, e);
		}

	}

	protected void logError(String exceptionString, Throwable e) {
		Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.ERROR, exceptionString, e));

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12433.java