error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3236.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3236.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3236.java
text:
```scala
i@@f (!namingAuthority.equalsIgnoreCase(JSLP_DEFAULT_NA) && i == 0) {

/*******************************************************************************
 * Copyright (c) 2007 Versant Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Kuppe (mkuppe <at> versant <dot> com) - initial API and implementation
 ******************************************************************************/

package org.eclipse.ecf.provider.jslp.identity;

import ch.ethz.iks.slp.ServiceType;
import ch.ethz.iks.slp.ServiceURL;
import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.util.StringUtils;
import org.eclipse.ecf.discovery.identity.IServiceTypeID;
import org.eclipse.ecf.discovery.identity.ServiceTypeID;
import org.eclipse.ecf.internal.provider.jslp.Messages;
import org.eclipse.osgi.util.NLS;

public class JSLPServiceTypeID extends ServiceTypeID {

	private static final String JSLP_DELIM = ":"; //$NON-NLS-1$
	private static final String JSLP_DEFAULT_NA = "IANA"; //$NON-NLS-1$

	private static final long serialVersionUID = -4558132760112793805L;

	private final ServiceType st;

	protected JSLPServiceTypeID(final Namespace namespace, final String type) {
		super(namespace);
		try {
			st = new ServiceType(type);
			// verify that the ServiceType is proper
			Assert.isNotNull(st.toString());
			Assert.isTrue(!st.toString().equals("")); //$NON-NLS-1$

			final String na = st.getNamingAuthority();
			String str = st.toString();
			if ("".equals(na)) { //$NON-NLS-1$
				namingAuthority = DEFAULT_NA;
			} else {
				namingAuthority = na;
				// remove the naming authority from the string
				str = StringUtils.replaceAllIgnoreCase(str, "." + na, ""); //$NON-NLS-1$//$NON-NLS-2$
			}

			services = StringUtils.split(str, JSLP_DELIM);
			scopes = DEFAULT_SCOPE; //TODO-mkuppe https://bugs.eclipse.org/218308
			protocols = DEFAULT_PROTO; //TODO-mkuppe https://bugs.eclipse.org/230182

			createType();
		} catch (Exception e) {
			throw new IDCreateException(NLS.bind(Messages.JSLPServiceTypeID_4, type));
		}
	}

	JSLPServiceTypeID(final Namespace namespace, final ServiceURL anURL, final String[] scopes) {
		this(namespace, anURL.getServiceType());

		if (scopes != null && scopes.length > 0) {
			this.scopes = scopes;
		}

		// set the protocol if provided
		final String protocol = anURL.getProtocol();
		if (protocol != null) {
			protocols = new String[] {protocol};
			createType();
		}
	}

	JSLPServiceTypeID(final Namespace namespace, final IServiceTypeID type) {
		super(namespace, type);

		final StringBuffer buf = new StringBuffer("service:"); //$NON-NLS-1$
		for (int i = 0; i < services.length; i++) {
			buf.append(services[i]);
			// #228876
			if (!namingAuthority.equalsIgnoreCase(JSLP_DEFAULT_NA) && i == 1) {
				buf.append('.');
				buf.append(namingAuthority);
			}
			buf.append(':');
		}
		// remove dangling colon
		final String string = buf.toString();
		st = new ServiceType(string.substring(0, string.length() - 1));
	}

	JSLPServiceTypeID(final Namespace namespace, final ServiceType aServiceType) {
		this(namespace, aServiceType.toString());

		// remove the SLP "service" prefix
		final String[] newServices = new String[services.length - 1];
		System.arraycopy(services, 1, newServices, 0, newServices.length);
		services = newServices;

		createType();
	}

	/**
	 * @return the jSLP ServiceType
	 */
	public ServiceType getServiceType() {
		return st;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.identity.ServiceTypeID#getInternal()
	 */
	public String getInternal() {
		final String str = st.toString();
		Assert.isNotNull(str);

		// remove the dangling colon if present
		if (str.endsWith(":")) { //$NON-NLS-1$
			Assert.isTrue(str.length() > 1);
			return str.substring(0, str.length() - 1);
		}

		// remove the default naming authority #228876
		return StringUtils.replaceAllIgnoreCase(str, "." + JSLP_DEFAULT_NA, ""); //$NON-NLS-1$//$NON-NLS-2$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.identity.ServiceTypeID#getName()
	 */
	public String getName() {
		String name = super.getName();
		name = StringUtils.replaceAll(name, JSLP_DEFAULT_NA, IServiceTypeID.DEFAULT_NA);
		return name;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3236.java