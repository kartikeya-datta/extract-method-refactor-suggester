error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/210.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/210.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/210.java
text:
```scala
public R@@oles(final String... roles)

/*
 * $Id$ $Revision$
 * $Date$
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
package wicket.authorization.strategies.role;

import java.io.Serializable;
import java.util.HashSet;

import wicket.util.string.StringList;

/**
 * Utility class for working with roles.
 * 
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public final class Roles extends HashSet<String> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** USER role (for use in annotations) */
	public static final String USER = "USER";
	
	/** ADMIN role (for use in annotations) */
	public static final String ADMIN = "ADMIN";

	/**
	 * Construct.
	 */
	public Roles()
	{
	}

	/**
	 * Construct.
	 * 
	 * @param roles
	 *            Roles as a comma separated list, like "ADMIN, USER"
	 */
	public Roles(final String roles)
	{
		for (final String role : roles.split("\\s*,\\s*"))
		{
			add(role);
		}
	}

	/**
	 * Construct.
	 * 
	 * @param roles
	 *            Roles
	 */
	public Roles(final String[] roles)
	{
		for (final String role : roles)
		{
			add(role);
		}
	}

	/**
	 * Whether this roles object containes the provided role.
	 * 
	 * @param role
	 *            the role to check
	 * @return true if it contains the role, false otherwise
	 */
	public boolean hasRole(final String role)
	{
		if (role != null)
		{
			return contains(role);
		}
		return false;
	}

	/**
	 * Whether this roles object contains any of the provided roles.
	 * 
	 * @param roles
	 *            the roles to check
	 * @return true if it contains any of the roles, false otherwise
	 */
	public boolean hasAnyRole(Roles roles)
	{
		if (roles != null)
		{
			for (String role : roles)
			{
				if (hasRole(role))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Whether this roles object contains all the provided roles.
	 * 
	 * @param roles
	 *            the roles to check
	 * @return true if it contains all the roles or the provided roles object is
	 *         null, false otherwise
	 */
	public boolean hasAllRoles(Roles roles)
	{
		if (roles != null)
		{
			for (String role : roles)
			{
				if (!hasRole(role))
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return StringList.valueOf(this).join();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/210.java