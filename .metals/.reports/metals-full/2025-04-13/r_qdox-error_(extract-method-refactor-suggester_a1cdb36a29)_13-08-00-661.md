error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3214.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3214.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3214.java
text:
```scala
protected b@@oolean isPageAuthorized(final Class<? extends Page> pageClass)

/*
 * $Id: SimplePageAuthorizationStrategy.java,v 1.1 2006/02/09 01:04:49 eelco12
 * Exp $ $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.authorization.strategies.page;

import wicket.Application;
import wicket.Component;
import wicket.Page;
import wicket.RestartResponseAtInterceptPageException;
import wicket.authorization.IUnauthorizedComponentInstantiationListener;
import wicket.authorization.UnauthorizedInstantiationException;

/**
 * A very simple authorization strategy that takes a supertype (a base class or
 * tagging interface) and performs a simple authorization check by calling the
 * abstract method isAuthorized() whenever a Page class that extends or
 * implements the supertype is about to be instantiated. If that method returns
 * true, page instantiation proceeds normally. If it returns false, the user is
 * automatically directed to the specified sign-in page for authentication,
 * which will presumably allow authorization to succeed once they have signed
 * in.
 * <p>
 * In your Application.init() method do something like the following:
 * 
 * <pre>
 * SimplePageAuthorizationStrategy authorizationStrategy = new SimplePageAuthorizationStrategy(
 * 		MySecureWebPage.class, MySignInPage.class)
 * {
 * 	protected boolean isAuthorized()
 * 	{
 * 		// Authorize access based on user authentication in the session
 * 		return (((MySession)Session.get()).isSignedIn());
 * 	}
 * };
 * 
 * getSecuritySettings().setAuthorizationStrategy(authorizationStrategy);
 * </pre>
 * 
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public abstract class SimplePageAuthorizationStrategy extends AbstractPageAuthorizationStrategy
{
	/**
	 * The supertype (class or interface) of Pages that require authorization to
	 * be instantiated.
	 */
	private final Class securePageSuperType;

	/**
	 * Construct.
	 * 
	 * @param securePageSuperType
	 *            The class or interface supertype that indicates that a given
	 *            Page requires authorization
	 * @param signInPageClass
	 *            The sign in page class
	 */
	public SimplePageAuthorizationStrategy(final Class securePageSuperType,
			final Class<? extends Page> signInPageClass)
	{
		if (securePageSuperType == null)
		{
			throw new IllegalArgumentException("Secure page super type must not be null");
		}

		this.securePageSuperType = securePageSuperType;

		// Handle unauthorized access to pages
		Application.get().getSecuritySettings().setUnauthorizedComponentInstantiationListener(
				new IUnauthorizedComponentInstantiationListener()
				{
					public void onUnauthorizedInstantiation(final Component component)
					{
						// If there is a sign in page class declared, and the
						// unauthorized component is a page, but it's not the
						// sign in page
						if (component instanceof Page)
						{
							// Redirect to page to let the user sign in
							throw new RestartResponseAtInterceptPageException(signInPageClass);
						}
						else
						{
							// The component was not a page, so throw exception
							throw new UnauthorizedInstantiationException(component.getClass());
						}
					}
				});
	}

	/**
	 * @see wicket.authorization.strategies.page.AbstractPageAuthorizationStrategy#isPageAuthorized(java.lang.Class)
	 */
	@Override
	protected boolean isPageAuthorized(final Class pageClass)
	{
		if (instanceOf(pageClass, securePageSuperType))
		{
			return isAuthorized();
		}

		// Allow construction by default
		return true;
	}

	/**
	 * Gets whether the current user/session is authorized to instantiate a page
	 * class which extends or implements the supertype (base class or tagging
	 * interface) passed to the constructor.
	 * 
	 * @return True if the instantiation should be allowed to proceed. False, if
	 *         the user should be directed to the application's sign-in page.
	 */
	protected abstract boolean isAuthorized();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3214.java