error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,20]

error in qdox parser
file content:
```java
offset: 20
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17.java
text:
```scala
protected abstract C@@lass< ? extends WebPage< ? >> getSignInPageClass();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.authentication;

import java.lang.ref.WeakReference;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.authorization.strategies.role.IRoleCheckingStrategy;
import org.apache.wicket.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;


/**
 * A web application subclass that does role-based authentication.
 * 
 * @author Jonathan Locke
 */
public abstract class AuthenticatedWebApplication extends WebApplication
		implements
			IRoleCheckingStrategy,
			IUnauthorizedComponentInstantiationListener
{
	/** Subclass of authenticated web session to instantiate */
	private final WeakReference<Class< ? extends AuthenticatedWebSession>> webSessionClassRef;

	/**
	 * Constructor
	 */
	public AuthenticatedWebApplication()
	{
		// Get web session class to instantiate
		webSessionClassRef = new WeakReference<Class< ? extends AuthenticatedWebSession>>(
				getWebSessionClass());
	}

	@Override
	protected void init()
	{
		super.init();

		// Set authorization strategy and unauthorized instantiation listener
		getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(this));
		getSecuritySettings().setUnauthorizedComponentInstantiationListener(this);
	}

	/**
	 * @see IRoleCheckingStrategy#hasAnyRole(Roles)
	 */
	public final boolean hasAnyRole(final Roles roles)
	{
		final Roles sessionRoles = AuthenticatedWebSession.get().getRoles();
		return sessionRoles != null && sessionRoles.hasAnyRole(roles);
	}

	/**
	 * @see IUnauthorizedComponentInstantiationListener#onUnauthorizedInstantiation(Component)
	 */
	public final void onUnauthorizedInstantiation(final Component< ? > component)
	{
		// If there is a sign in page class declared, and the unauthorized
		// component is a page, but it's not the sign in page
		if (component instanceof Page)
		{
			if (!AuthenticatedWebSession.get().isSignedIn())
			{
				// Redirect to intercept page to let the user sign in
				throw new RestartResponseAtInterceptPageException(getSignInPageClass());
			}
			else
			{
				onUnauthorizedPage((Page< ? >)component);
			}
		}
		else
		{
			// The component was not a page, so throw an exception
			throw new UnauthorizedInstantiationException(component.getClass());
		}
	}

	/**
	 * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.Request,
	 *      org.apache.wicket.Response)
	 */
	@Override
	public Session newSession(final Request request, final Response response)
	{
		try
		{
			return webSessionClassRef.get().getDeclaredConstructor(Request.class).newInstance(
					request);
		}
		catch (Exception e)
		{
			throw new WicketRuntimeException("Unable to instantiate web session " +
					webSessionClassRef.get(), e);
		}
	}

	/**
	 * @return AuthenticatedWebSession subclass to use in this authenticated web application.
	 */
	protected abstract Class< ? extends AuthenticatedWebSession> getWebSessionClass();

	/**
	 * @return Subclass of sign-in page
	 */
	protected abstract Class< ? extends WebPage> getSignInPageClass();

	/**
	 * Called when an AUTHENTICATED user tries to navigate to a page that they are not authorized to
	 * access. You might want to override this to navigate to some explanatory page or to the
	 * application's home page.
	 * 
	 * @param page
	 *            The page
	 */
	protected void onUnauthorizedPage(final Page page)
	{
		// The component was not a page, so throw an exception
		throw new UnauthorizedInstantiationException(page.getClass());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17.java