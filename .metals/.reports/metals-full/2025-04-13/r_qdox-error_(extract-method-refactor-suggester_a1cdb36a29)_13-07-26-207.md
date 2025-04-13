error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9719.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9719.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9719.java
text:
```scala
public static final v@@oid unauthorizeAll(Class<? extends Component> componentClass)

/*
 * $Id: MetaDataRoleAuthorizationStrategy.java,v 1.2 2006/02/09 17:14:01 eelco12
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
package wicket.authorization.strategies.role.metadata;

import java.util.Set;

import wicket.Application;
import wicket.Component;
import wicket.MetaDataKey;
import wicket.authorization.Action;
import wicket.authorization.strategies.role.AbstractRoleAuthorizationStrategy;
import wicket.authorization.strategies.role.IRoleAuthorizer;

/**
 * Strategy that uses the Wicket metadata facility to check authorization. The
 * static <code>authorize</code> methods are for authorizing component actions
 * and component instantiation by role. This class is is the main entry point
 * for users wanting to use the roles-based authorization of the
 * wicket-auth-roles package based on wicket metadata.
 * 
 * For instance, use like:
 * 
 * <pre>
 * MetaDataRoleAuthorizationStrategy.authorize(myPanel, RENDER, &quot;ADMIN&quot;);
 * </pre>
 * 
 * for actions on component instances, or:
 * 
 * <pre>
 * MetaDataRoleAuthorizationStrategy.authorize(AdminBookmarkablePage.class, &quot;ADMIN&quot;);
 * </pre>
 * 
 * for doing role based authorization for component instantation.
 * 
 * @see wicket.MetaDataKey
 * 
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public class MetaDataRoleAuthorizationStrategy extends AbstractRoleAuthorizationStrategy
{
	/**
	 * Component meta data key for ations/ roles information. Typically, you do
	 * not need to use this meta data key directly, but instead use one of the
	 * bind methods of this class.
	 */
	public static final MetaDataKey ACTION_PERMISSIONS = new MetaDataKey(ActionPermissions.class)
	{
	};

	/**
	 * Application meta data key for ations/ roles information. Typically, you
	 * do not need to use this meta data key directly, but instead use one of
	 * the bind methods of this class.
	 */
	private static final MetaDataKey INSTANTIATION_PERMISSIONS = new MetaDataKey(
			InstantiationPermissions.class)
	{
	};

	/** Special role string for denying access to all */
	public static final String NO_ROLE = "wicket:NO_ROLE";

	/**
	 * Authorizes the given role to create component instances of type
	 * componentClass. This authorization is added to any previously authorized
	 * roles.
	 * 
	 * @param componentClass
	 *            The component type that is subject for the authorization
	 * @param role
	 *            The role that is authorized to create component instances of
	 *            type componentClass
	 */
	public static final void authorize(final Class< ? extends Component> componentClass,
			final String role)
	{
		final Application application = Application.get();
		InstantiationPermissions permissions = (InstantiationPermissions)application
				.getMetaData(INSTANTIATION_PERMISSIONS);
		if (permissions == null)
		{
			permissions = new InstantiationPermissions();
			application.setMetaData(INSTANTIATION_PERMISSIONS, permissions);
		}
		permissions.authorize(componentClass, role);
	}

	/**
	 * Authorizes the given role to perform the given action on the given
	 * component.
	 * 
	 * @param component
	 *            The component that is subject to the authorization
	 * @param action
	 *            The action to authorize
	 * @param role
	 *            The role to authorize
	 */
	public static final void authorize(final Component component, final Action action,
			final String role)
	{
		ActionPermissions permissions = (ActionPermissions)component
				.getMetaData(ACTION_PERMISSIONS);
		if (permissions == null)
		{
			permissions = new ActionPermissions();
			component.setMetaData(ACTION_PERMISSIONS, permissions);
		}
		permissions.authorize(action, role);
	}

	/**
	 * Grants permission to all roles to create instances of the given component
	 * class.
	 * 
	 * @param componentClass
	 *            The component class
	 */
	public static final void authorizeAll(final Class< ? extends Component> componentClass)
	{
		Application application = Application.get();
		InstantiationPermissions authorizedRoles = (InstantiationPermissions)application
				.getMetaData(INSTANTIATION_PERMISSIONS);
		if (authorizedRoles != null)
		{
			authorizedRoles.authorizeAll(componentClass);
		}
	}

	/**
	 * Grants permission to all roles to perform the given action on the given
	 * component.
	 * 
	 * @param component
	 *            The component that is subject to the authorization
	 * @param action
	 *            The action to authorize
	 */
	public static final void authorizeAll(final Component component, final Action action)
	{
		ActionPermissions permissions = (ActionPermissions)component
				.getMetaData(ACTION_PERMISSIONS);
		if (permissions != null)
		{
			permissions.authorizeAll(action);
		}
	}

	/**
	 * Removes permission for the given role to create instances of the given
	 * component class. There is no danger in removing authorization by calling
	 * this method. If the last authorization grant is removed for a given
	 * componentClass, the internal role NO_ROLE will automatically be added,
	 * effectively denying access to all roles (if this was not done, all roles
	 * would suddenly have access since no authorization is equivalent to full
	 * access).
	 * 
	 * @param componentClass
	 *            The component type
	 * @param role
	 *            The role that is no longer to be authorized to create
	 *            instances of type componentClass
	 */
	public static final void unauthorize(final Class< ? extends Component> componentClass,
			final String role)
	{
		final InstantiationPermissions permissions = (InstantiationPermissions)Application.get()
				.getMetaData(INSTANTIATION_PERMISSIONS);
		if (permissions != null)
		{
			permissions.unauthorize(componentClass, role);
		}
	}

	/**
	 * Removes permission for the given role to perform the given action on the
	 * given component. There is no danger in removing authorization by calling
	 * this method. If the last authorization grant is removed for a given
	 * action, the internal role NO_ROLE will automatically be added,
	 * effectively denying access to all roles (if this was not done, all roles
	 * would suddenly have access since no authorization is equivalent to full
	 * access).
	 * 
	 * @param component
	 *            The component
	 * @param action
	 *            The action
	 * @param role
	 *            The role that is no longer allowed to perform the given action
	 */
	public static final void unauthorize(final Component component, final Action action,
			final String role)
	{
		final ActionPermissions permissions = (ActionPermissions)component
				.getMetaData(ACTION_PERMISSIONS);
		if (permissions != null)
		{
			permissions.unauthorize(action, role);
		}
	}

	/**
	 * Grants authorization to instantiate the given class to just the role
	 * NO_ROLE, effectively denying all other roles.
	 * 
	 * @param componentClass
	 *            The component class
	 */
	public static final void unauthorizeAll(Class<Component> componentClass)
	{
		authorizeAll(componentClass);
		authorize(componentClass, NO_ROLE);
	}

	/**
	 * Grants authorization to perform the given action to just the role
	 * NO_ROLE, effectively denying all other roles.
	 * 
	 * @param component
	 *            the component that is subject to the authorization
	 * @param action
	 *            the action to authorize
	 */
	public static final void unauthorizeAll(final Component component, final Action action)
	{
		authorizeAll(component, action);
		authorize(component, action, NO_ROLE);
	}

	/**
	 * Construct.
	 * 
	 * @param rolesAuthorizer
	 *            the authorizer object
	 */
	public MetaDataRoleAuthorizationStrategy(final IRoleAuthorizer rolesAuthorizer)
	{
		super(rolesAuthorizer);
	}

	/**
	 * Uses component level meta data to match roles for component action
	 * execution.
	 * 
	 * @see wicket.authorization.IAuthorizationStrategy#authorizeAction(wicket.Component,
	 *      wicket.authorization.Action)
	 */
	public boolean authorizeAction(final Component component, final Action action)
	{
		if (component == null)
		{
			throw new IllegalArgumentException("argument component has to be not null");
		}
		if (action == null)
		{
			throw new IllegalArgumentException("argument action has to be not null");
		}

		Set<String> roles = rolesAuthorizedToPerformAction(component, action);
		if (roles != null)
		{
			return hasAny(roles.toArray(new String[roles.size()]));
		}
		return true;
	}

	/**
	 * Uses application level meta data to match roles for component
	 * instantiation.
	 * 
	 * @see wicket.authorization.IAuthorizationStrategy#authorizeInstantiation(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public boolean authorizeInstantiation(final Class componentClass)
	{
		if (componentClass == null)
		{
			throw new IllegalArgumentException("argument componentClass cannot be null");
		}

		// as long as the interface does not use generics, we should check this
		if (!Component.class.isAssignableFrom(componentClass))
		{
			throw new IllegalArgumentException("argument componentClass must be of type "
					+ Component.class.getName());
		}

		Set<String> roles = rolesAuthorizedToInstantiate(componentClass);
		if (roles != null)
		{
			return hasAny(roles.toArray(new String[roles.size()]));
		}
		return true;
	}

	/**
	 * Gets the roles for creation of the given component class, or null if none
	 * were registered.
	 * 
	 * @param componentClass
	 *            the component class
	 * @return the roles that are authorized for creation of the componentClass,
	 *         or null if no specific authorization was configured
	 */
	private static Set<String> rolesAuthorizedToInstantiate(
			final Class< ? extends Component> componentClass)
	{
		final InstantiationPermissions permissions = (InstantiationPermissions)Application.get()
				.getMetaData(INSTANTIATION_PERMISSIONS);
		if (permissions != null)
		{
			return permissions.authorizedRoles(componentClass);
		}
		return null;
	}

	/**
	 * Gets the roles for the given action/ component combination.
	 * 
	 * @param component
	 *            the component
	 * @param action
	 *            the action
	 * @return the roles for the action as defined with the given component
	 */
	private static Set<String> rolesAuthorizedToPerformAction(final Component component,
			final Action action)
	{
		final ActionPermissions permissions = (ActionPermissions)component
				.getMetaData(ACTION_PERMISSIONS);
		if (permissions != null)
		{
			return permissions.rolesFor(action);
		}
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9719.java