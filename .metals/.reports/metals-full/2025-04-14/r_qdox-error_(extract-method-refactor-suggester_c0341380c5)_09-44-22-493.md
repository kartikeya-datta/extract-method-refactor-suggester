error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15217.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15217.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15217.java
text:
```scala
i@@f (debugSettings.isLinePreciseReportingOnAddComponentEnabled())

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
package org.apache.wicket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupException;
import org.apache.wicket.markup.MarkupNotFoundException;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.markup.resolver.IComponentResolver;
import org.apache.wicket.model.IComponentInheritedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.settings.IDebugSettings;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.version.undo.Change;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A MarkupContainer holds a map of child components.
 * <ul>
 * <li><b>Children </b>- Children can be added by calling the add() method, and
 * they can be looked up using a dotted path. For example, if a container called
 * "a" held a nested container "b" which held a nested component "c", then
 * a.get("b.c") would return the Component with id "c". The number of children
 * in a MarkupContainer can be determined by calling size(), and the whole
 * hierarchy of children held by a MarkupContainer can be traversed by calling
 * visitChildren(), passing in an implementation of Component.IVisitor.
 * 
 * <li><b>Markup Rendering </b>- A MarkupContainer also holds/references
 * associated markup which is used to render the container. As the markup stream
 * for a container is rendered, component references in the markup are resolved
 * by using the container to look up Components in the container's component map
 * by id. Each component referenced by the markup stream is given an opportunity
 * to render itself using the markup stream.
 * <p>
 * Components may alter their referring tag, replace the tag's body or insert
 * markup after the tag. But components cannot remove tags from the markup
 * stream. This is an important guarantee because graphic designers may be
 * setting attributes on component tags that affect visual presentation.
 * <p>
 * The type of markup held in a given container subclass can be determined by
 * calling getMarkupType(). Markup is accessed via a MarkupStream object which
 * allows a component to traverse ComponentTag and RawMarkup MarkupElements
 * while rendering a response. Markup in the stream may be HTML or some other
 * kind of markup, such as VXML, as determined by the specific container
 * subclass.
 * <p>
 * A markup stream may be directly associated with a container via
 * setMarkupStream. However, a container which does not have a markup stream
 * (its getMarkupStream() returns null) may inherit a markup stream from a
 * container above it in the component hierarchy. The findMarkupStream() method
 * will locate the first container at or above this container which has a markup
 * stream.
 * <p>
 * All Page containers set a markup stream before rendering by calling the
 * method getAssociatedMarkupStream() to load the markup associated with the
 * page. Since Page is at the top of the container hierarchy, it is guaranteed
 * that findMarkupStream will always return a valid markup stream.
 * 
 * @see MarkupStream
 * @author Jonathan Locke
 */
public abstract class MarkupContainer extends Component
{
	private static final long serialVersionUID = 1L;

	/** Log for reporting. */
	private static final Logger log = LoggerFactory.getLogger(MarkupContainer.class);

	/** List of children or single child */
	private Object children;

	/**
	 * The markup stream for this container. This variable is used only during
	 * the render phase to provide access to the current element within the
	 * stream.
	 */
	private transient MarkupStream markupStream;

	/**
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public MarkupContainer(final String id)
	{
		super(id);
	}

	/**
	 * @see org.apache.wicket.Component#Component(String, IModel)
	 */
	public MarkupContainer(final String id, IModel model)
	{
		super(id, model);
	}

	/**
	 * Adds a child component to this container.
	 * 
	 * @param child
	 *            The child
	 * @throws IllegalArgumentException
	 *             Thrown if a child with the same id is replaced by the add
	 *             operation.
	 * @return This
	 */
	public final MarkupContainer add(final Component child)
	{
		checkHierarchyChange(child);

		if (child == null)
		{
			throw new IllegalArgumentException("argument child may not be null");
		}

		if (log.isDebugEnabled())
		{
			log.debug("Add " + child.getId() + " to " + this);
		}

		// Add to map
		addedComponent(child);
		if (put(child) != null)
		{
			throw new IllegalArgumentException(exceptionMessage("A child with id '" +
					child.getId() + "' already exists"));
		}

		return this;
	}

	/**
	 * Replaces a child component of this container with another or just adds it
	 * in case no child with the same id existed yet.
	 * 
	 * @param child
	 *            The child
	 * @return This
	 */
	public final MarkupContainer addOrReplace(final Component child)
	{
		checkHierarchyChange(child);

		if (child == null)
		{
			throw new IllegalArgumentException("argument child must be not null");
		}

		if (get(child.getId()) == null)
		{
			add(child);
		}
		else
		{
			replace(child);
		}

		return this;
	}

	/**
	 * This method allows a component to be added by an auto-resolver such as
	 * AutoComponentResolver or AutoLinkResolver. While the component is being
	 * added, the component's FLAG_AUTO boolean is set. The isAuto() method of
	 * Component returns true if a component or any of its parents has this bit
	 * set. When a component is added via autoAdd(), the logic in Page that
	 * normally (a) checks for modifications during the rendering process, and
	 * (b) versions components, is bypassed if Component.isAuto() returns true.
	 * <p>
	 * The result of all this is that components added with autoAdd() are free
	 * from versioning and can add their own children without the usual
	 * exception that would normally be thrown when the component hierarchy is
	 * modified during rendering.
	 * 
	 * @param component
	 *            The component to add
	 * @param markupStream
	 *            Null, if the parent container is able to provide the markup.
	 *            Else the markup stream to be used to render the component.
	 * @return True, if component has been added
	 */
	public final boolean autoAdd(final Component component, final MarkupStream markupStream)
	{
		if (component == null)
		{
			throw new IllegalArgumentException("argument component may not be null");
		}

		/* Replace strategy */
		component.setAuto(true);
		if (get(component.getId()) != null)
		{
			this.remove(component);
		}
		add(component);
		component.beforeRender();
		component.markRendering();
		if (markupStream == null)
		{
			component.render();
		}
		else
		{
			component.render(markupStream);
		}
		return true;
	}

	/**
	 * 
	 * @param component
	 *            The component to add
	 * @return True, if component has been added
	 * 
	 * @deprecated since 1.3 Please use
	 *             {@link #autoAdd(Component, MarkupStream)} instead
	 */
	public final boolean autoAdd(final Component component)
	{
		return autoAdd(component, null);
	}

	/**
	 * @param component
	 *            The component to check
	 * @param recurse
	 *            True if all descendents should be considered
	 * @return True if the component is contained in this container
	 */
	public final boolean contains(final Component component, final boolean recurse)
	{
		if (component == null)
		{
			throw new IllegalArgumentException("argument component may not be null");
		}

		if (recurse)
		{
			// Start at component and continue while we're not out of parents
			for (Component current = component; current != null;)
			{
				// Get parent
				final MarkupContainer parent = current.getParent();

				// If this container is the parent, then the component is
				// recursively contained by this container
				if (parent == this)
				{
					// Found it!
					return true;
				}

				// Move up the chain to the next parent
				current = parent;
			}

			// Failed to find this container in component's ancestry
			return false;
		}
		else
		{
			// Is the component contained in this container?
			return component.getParent() == this;
		}
	}

	/**
	 * Get a child component by looking it up with the given path.
	 * 
	 * @param path
	 *            Path to component
	 * @return The component at the path
	 */
	public final Component get(final String path)
	{
		// Reference to this container
		if (path == null || path.trim().equals(""))
		{
			return this;
		}

		// Get child's id, if any
		final String id = Strings.firstPathComponent(path, Component.PATH_SEPARATOR);

		// Get child by id
		Component child = children_get(id);

		// If the container is transparent, than ask its parent.
		// ParentResolver does something quite similar, but because of <head>,
		// <body>, <wicket:panel> etc. it is quite common to have transparent
		// components. Hence, this is little short cut for a tiny performance
		// optimization.
		if ((child == null) && isTransparentResolver() && (getParent() != null))
		{
			child = getParent().get(path);
		}

		// Found child?
		if (child != null)
		{
			final String path2 = Strings.afterFirstPathComponent(path, Component.PATH_SEPARATOR);
			// Recurse on latter part of path
			return child.get(path2);
		}

		return child;
	}

	/**
	 * Gets a fresh markup stream that contains the (immutable) markup resource
	 * for this class.
	 * 
	 * @param throwException
	 *            If true, throw an exception, if markup could not be found
	 * @return A stream of MarkupElement elements
	 */
	public MarkupStream getAssociatedMarkupStream(final boolean throwException)
	{
		try
		{
			return getApplication().getMarkupSettings().getMarkupCache().getMarkupStream(this,
					false, throwException);
		}
		catch (MarkupException ex)
		{
			// re-throw it. The exception contains already all the information
			// required.
			throw ex;
		}
		catch (WicketRuntimeException ex)
		{
			// throw exception since there is no associated markup
			throw new MarkupNotFoundException(
					exceptionMessage("Markup of type '" + getMarkupType() + "' for component '" +
							getClass().getName() + "' not found." +
							" Enable debug messages for org.apache.wicket.util.resource to get a list of all filenames tried"),
					ex);
		}
	}


	/**
	 * Get the markup stream set on this container.
	 * 
	 * @return Returns the markup stream set on this container.
	 */
	public final MarkupStream getMarkupStream()
	{
		return markupStream;
	}


	/**
	 * Get the type of associated markup for this component.
	 * 
	 * @return The type of associated markup for this component (for example,
	 *         "html", "wml" or "vxml"). The markup type for a component is
	 *         independent of whether or not the component actually has an
	 *         associated markup resource file (which is determined at runtime).
	 *         If there is no markup type for a component, null may be returned,
	 *         but this means that no markup can be loaded for the class.
	 */
	public String getMarkupType()
	{
		throw new IllegalStateException(
				exceptionMessage("You cannot directly subclass Page or MarkupContainer.	 Instead, subclass a markup-specific class, such as WebPage or WebMarkupContainer"));
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT USE IT.
	 * 
	 * Adds a child component to this container.
	 * 
	 * @param child
	 *            The child
	 * @throws IllegalArgumentException
	 *             Thrown if a child with the same id is replaced by the add
	 *             operation.
	 */
	public void internalAdd(final Component child)
	{
		if (log.isDebugEnabled())
		{
			log.debug("internalAdd " + child.getId() + " to " + this);
		}

		// Add to map
		addedComponent(child);
		put(child);
	}

	/**
	 * Some MarkupContainers (e.g. HtmlHeaderContainer, BodyOnLoadContainer)
	 * have to be transparent with respect to there child components. A
	 * transparent container gets its children from its parent container.
	 * <p>
	 * 
	 * @see org.apache.wicket.markup.resolver.ParentResolver
	 * 
	 * @return false. By default a MarkupContainer is not transparent.
	 */
	public boolean isTransparentResolver()
	{
		return false;
	}

	/**
	 * @return Iterator that iterates through children in the order they were
	 *         added
	 */
	public final Iterator iterator()
	{
		return new Iterator()
		{
			int index = 0;

			public boolean hasNext()
			{
				return index < children_size();
			}

			public Object next()
			{
				return children_get(index++);
			}

			public void remove()
			{
				final Component removed = children_remove(--index);
				checkHierarchyChange(removed);
				removedComponent(removed);
			}
		};
	}

	/**
	 * @param comparator
	 *            The comparator
	 * @return Iterator that iterates over children in the order specified by
	 *         comparator
	 */
	public final Iterator iterator(Comparator comparator)
	{
		final List sorted;
		if (children == null)
		{
			sorted = Collections.EMPTY_LIST;
		}
		else
		{
			if (children instanceof Component)
			{
				sorted = new ArrayList(1);
				sorted.add(children);
			}
			else
			{
				sorted = Arrays.asList((Component[])children);
			}
		}
		Collections.sort(sorted, comparator);
		return sorted.iterator();
	}

	/**
	 * NOT USED ANYMORE; it's here for helping people migrate from Wicket 1.2 to
	 * Wicket 1.3
	 * 
	 * @param containerClass
	 * @return nothing
	 * @throws always
	 *             throws an {@link IllegalStateException}
	 */
	// TODO remove after release 1.3.0
	public final IResourceStream newMarkupResourceStream(Class containerClass)
	{
		throw new IllegalStateException(
				"this method is not used any more (and shouldn't be called by clients anyway)");
	}

	/**
	 * @param component
	 *            Component to remove from this container
	 */
	public void remove(final Component component)
	{
		checkHierarchyChange(component);

		if (component == null)
		{
			throw new IllegalArgumentException("argument component may not be null");
		}

		children_remove(component);
		removedComponent(component);
	}

	/**
	 * Removes the given component
	 * 
	 * @param id
	 *            The id of the component to remove
	 */
	public final void remove(final String id)
	{
		if (id == null)
		{
			throw new IllegalArgumentException("argument id may not be null");
		}

		final Component component = get(id);
		if (component != null)
		{
			remove(component);
		}
		else
		{
			throw new WicketRuntimeException("Unable to find a component with id '" + id +
					"' to remove");
		}
	}

	/**
	 * Removes all children from this container.
	 * <p>
	 * Note: implementation does not call
	 * {@link MarkupContainer#remove(Component) } for each component.
	 */
	public final void removeAll()
	{
		if (children != null)
		{
			addStateChange(new Change()
			{
				private static final long serialVersionUID = 1L;

				final Object removedChildren = children;

				public String toString()
				{
					return "RemoveAllChange[component: " + getPath() + ", removed Children: " +
							removedChildren + "]";
				}

				public void undo()
				{
					children = removedChildren;
					int size = children_size();
					for (int i = 0; i < size; i++)
					{
						// Get next child
						final Component child = children_get(i);
						child.setParent(MarkupContainer.this);
					}
				}
			});

			// Loop through child components
			int size = children_size();
			for (int i = 0; i < size; i++)
			{
				// Get next child
				final Component child = children_get(i);

				// Do not call remove() because the state change would than be
				// recorded twice.
				child.detachModel();
				child.setParent(null);
			}

			children = null;
		}
	}

	/**
	 * Renders the entire associated markup stream for a container such as a
	 * Border or Panel. Any leading or trailing raw markup in the associated
	 * markup is skipped.
	 * 
	 * @param openTagName
	 *            the tag to render the associated markup for
	 * @param exceptionMessage
	 *            message that will be used for exceptions
	 */
	public final void renderAssociatedMarkup(final String openTagName, final String exceptionMessage)
	{
		// Get markup associated with Border or Panel component
		final MarkupStream originalMarkupStream = getMarkupStream();
		final MarkupStream associatedMarkupStream = getAssociatedMarkupStream(true);

		// skip until the targetted tag is found
		associatedMarkupStream.skipUntil(openTagName);
		setMarkupStream(associatedMarkupStream);

		// Get open tag in associated markup of border component
		final ComponentTag associatedMarkupOpenTag = associatedMarkupStream.getTag();

		// Check for required open tag name
		if (!((associatedMarkupOpenTag != null) && associatedMarkupOpenTag.isOpen() && (associatedMarkupOpenTag instanceof WicketTag)))
		{
			associatedMarkupStream.throwMarkupException(exceptionMessage);
		}

		try
		{
			setIgnoreAttributeModifier(true);
			renderComponentTag(associatedMarkupOpenTag);
			associatedMarkupStream.next();

			if (getApplication().getDebugSettings().isOutputMarkupContainerClassName())
			{
				getResponse().write("<!-- MARKUP FOR ");
				getResponse().write(getClass().getName());
				getResponse().write(" BEGIN -->");
			}

			renderComponentTagBody(associatedMarkupStream, associatedMarkupOpenTag);

			if (getApplication().getDebugSettings().isOutputMarkupContainerClassName())
			{
				getResponse().write("<!-- MARKUP FOR ");
				getResponse().write(getClass().getName());
				getResponse().write(" END -->");
			}

			renderClosingComponentTag(associatedMarkupStream, associatedMarkupOpenTag, false);
			setMarkupStream(originalMarkupStream);
		}
		finally
		{
			setIgnoreAttributeModifier(false);
		}
	}

	/**
	 * Replaces a child component of this container with another
	 * 
	 * @param child
	 *            The child
	 * @throws IllegalArgumentException
	 *             Thrown if there was no child with the same id.
	 * @return This
	 */
	public final MarkupContainer replace(final Component child)
	{
		checkHierarchyChange(child);

		if (child == null)
		{
			throw new IllegalArgumentException("argument child must be not null");
		}

		if (log.isDebugEnabled())
		{
			log.debug("Replacing " + child.getId() + " in " + this);
		}

		if (child.getParent() != this)
		{
			// Add to map
			final Component replaced = put(child);

			// Look up to make sure it was already in the map
			if (replaced == null)
			{
				throw new WicketRuntimeException(
						exceptionMessage("Cannot replace a component which has not been added: id='" +
								child.getId() + "', component=" + child));
			}

			// first remove the component.
			removedComponent(replaced);

			// then add the other one.
			addedComponent(child);

			// The position of the associated markup remains the same
			child.markupIndex = replaced.markupIndex;

			// The generated markup id remains the same
			String replacedId = (replaced.hasMarkupIdMetaData()) ? replaced.getMarkupId() : null;
			child.setMarkupId(replacedId);
		}

		return this;
	}

	/**
	 * @see org.apache.wicket.Component#setModel(org.apache.wicket.model.IModel)
	 */
	public Component setModel(final IModel model)
	{
		final IModel previous = this.model;
		super.setModel(model);
		if (previous instanceof IComponentInheritedModel)
		{
			visitChildren(new IVisitor()
			{
				public Object component(Component component)
				{
					IModel compModel = component.getModel();
					if (compModel instanceof IWrapModel)
					{
						compModel = ((IWrapModel)compModel).getWrappedModel();
					}
					if (compModel == previous)
					{
						component.setModel(null);
					}
					else if (compModel == model)
					{
						component.modelChanged();
					}
					return IVisitor.CONTINUE_TRAVERSAL;
				}

			});
		}
		return this;
	}

	/**
	 * Get the number of children in this container.
	 * 
	 * @return Number of children in this container
	 */
	public final int size()
	{
		return children_size();
	}

	/**
	 * @see org.apache.wicket.Component#toString()
	 */
	public String toString()
	{
		return toString(false);
	}

	/**
	 * @param detailed
	 *            True if a detailed string is desired
	 * @return String representation of this container
	 */
	public String toString(final boolean detailed)
	{
		final StringBuffer buffer = new StringBuffer();
		buffer.append("[MarkupContainer ");
		buffer.append(super.toString(true));
		if (detailed)
		{
			if (getMarkupStream() != null)
			{
				buffer.append(", markupStream = " + getMarkupStream());
			}

			if (children_size() != 0)
			{
				buffer.append(", children = ");

				// Loop through child components
				final int size = children_size();
				for (int i = 0; i < size; i++)
				{
					// Get next child
					final Component child = children_get(i);
					if (i != 0)
					{
						buffer.append(' ');
					}
					buffer.append(child.toString());
				}
			}
		}
		buffer.append(']');
		return buffer.toString();
	}

	/**
	 * Traverses all child components of the given class in this container,
	 * calling the visitor's visit method at each one.
	 * 
	 * @param clazz
	 *            The class of child to visit, or null to visit all children
	 * @param visitor
	 *            The visitor to call back to
	 * @return The return value from a visitor which halted the traversal, or
	 *         null if the entire traversal occurred
	 */
	public final Object visitChildren(final Class clazz, final IVisitor visitor)
	{
		if (visitor == null)
		{
			throw new IllegalArgumentException("argument visitor may not be null");
		}

		// Iterate through children of this container
		for (int i = 0; i < children_size(); i++)
		{
			// Get next child component
			final Component child = children_get(i);
			Object value = null;

			// Is the child of the correct class (or was no class specified)?
			if (clazz == null || clazz.isInstance(child))
			{
				// Call visitor
				value = visitor.component(child);

				// If visitor returns a non-null value, it halts the traversal
				if ((value != IVisitor.CONTINUE_TRAVERSAL) &&
						(value != IVisitor.CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER))
				{
					return value;
				}
			}

			// If child is a container
			if ((child instanceof MarkupContainer) &&
					(value != IVisitor.CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER))
			{
				// visit the children in the container
				value = ((MarkupContainer)child).visitChildren(clazz, visitor);

				// If visitor returns a non-null value, it halts the traversal
				if ((value != IVisitor.CONTINUE_TRAVERSAL) &&
						(value != IVisitor.CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER))
				{
					return value;
				}
			}
		}

		return null;
	}

	/**
	 * Traverses all child components in this container, calling the visitor's
	 * visit method at each one.
	 * 
	 * @param visitor
	 *            The visitor to call back to
	 * @return The return value from a visitor which halted the traversal, or
	 *         null if the entire traversal occurred
	 */
	public final Object visitChildren(final IVisitor visitor)
	{
		return visitChildren(null, visitor);
	}

	/**
	 * @param component
	 *            Component being added
	 */
	private final void addedComponent(final Component component)
	{
		// Check for degenerate case
		if (component == this)
		{
			throw new IllegalArgumentException("Component can't be added to itself");
		}

		MarkupContainer parent = component.getParent();
		if (parent != null)
		{
			parent.remove(component);
		}

		// Set child's parent
		component.setParent(this);

		final Page page = findPage();
		if (page != null && page.isAttached())
		{
			// if page is not null and the page has already been attached,
			// attach the component. we only attach if the page has been
			// attached because at some point the page must be attached and the
			// call would cascade down anyways.
			component.attach();
		}

		final IDebugSettings debugSettings = Application.get().getDebugSettings();
		if (debugSettings.getComponentUseCheck())
		{
			component.setMetaData(ADDED_AT_KEY, Strings.toString(component, new MarkupException(
					"added")));
		}

		if (page != null)
		{
			page.componentAdded(component);
		}

		// if the PREPARED_FOR_RENDER flag is set, we have alrady called
		// beforeRender on this
		// component's children. So we need to initialize the newly added one
		if (isPreparedForRender())
		{
			component.beforeRender();
		}
	}

	/**
	 * @param child
	 *            Child to add
	 */
	private final void children_add(final Component child)
	{
		if (children == null)
		{
			children = child;
		}
		else
		{
			// Get current list size
			final int size = children_size();

			// Create array that holds size + 1 elements
			final Component[] children = new Component[size + 1];

			// Loop through existing children copying them
			for (int i = 0; i < size; i++)
			{
				children[i] = children_get(i);
			}

			// Add new child to the end
			children[size] = child;

			// Save new children
			this.children = children;
		}
	}

	private final Component children_get(int index)
	{
		if (index == 0)
		{
			if (children instanceof Component)
			{
				return (Component)children;
			}
			else
			{
				return ((Component[])children)[index];
			}
		}
		else
		{
			return ((Component[])children)[index];
		}
	}

	private final Component children_get(final String id)
	{
		if (children instanceof Component)
		{
			final Component component = (Component)children;
			if (component.getId().equals(id))
			{
				return component;
			}
		}
		else
		{
			if (children != null)
			{
				final Component[] components = (Component[])children;
				for (int i = 0; i < components.length; i++)
				{
					if (components[i].getId().equals(id))
					{
						return components[i];
					}
				}
			}
		}
		return null;
	}

	private final int children_indexOf(Component child)
	{
		if (children instanceof Component)
		{
			if (((Component)children).getId().equals(child.getId()))
			{
				return 0;
			}
		}
		else
		{
			if (children != null)
			{
				final Component[] components = (Component[])children;
				for (int i = 0; i < components.length; i++)
				{
					if (components[i].getId().equals(child.getId()))
					{
						return i;
					}
				}
			}
		}
		return -1;
	}

	private final Component children_remove(Component component)
	{
		int index = children_indexOf(component);
		if (index != -1)
		{
			return children_remove(index);
		}
		return null;
	}

	private final Component children_remove(int index)
	{
		if (children instanceof Component)
		{
			if (index == 0)
			{
				final Component removed = (Component)children;
				children = null;
				return removed;
			}
			else
			{
				throw new IndexOutOfBoundsException();
			}
		}
		else
		{
			Component[] c = ((Component[])children);
			final Component removed = c[index];
			if (c.length == 2)
			{
				if (index == 0)
				{
					children = c[1];
				}
				else if (index == 1)
				{
					children = c[0];
				}
				else
				{
					throw new IndexOutOfBoundsException();
				}
			}
			else
			{
				Component[] newChildren = new Component[c.length - 1];
				int j = 0;
				for (int i = 0; i < c.length; i++)
				{
					if (i != index)
					{
						newChildren[j++] = c[i];
					}
				}
				children = newChildren;
			}
			return removed;
		}
	}

	private final Component children_set(int index, Component child)
	{
		final Component replaced;
		if (index < children_size())
		{
			if (children == null || children instanceof Component)
			{
				replaced = (Component)children;
				children = child;
			}
			else
			{
				final Component[] children = (Component[])this.children;
				replaced = children[index];
				children[index] = child;
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
		return replaced;
	}

	private final int children_size()
	{
		if (children == null)
		{
			return 0;
		}
		else
		{
			if (children instanceof Component)
			{
				return 1;
			}
			return ((Component[])children).length;
		}
	}

	/**
	 * Ensure that there is space in childForId map for a new entry before
	 * adding it.
	 * 
	 * @param child
	 *            The child to put into the map
	 * @return Any component that was replaced
	 */
	private final Component put(final Component child)
	{
		int index = children_indexOf(child);
		if (index == -1)
		{
			children_add(child);
			return null;
		}
		else
		{
			return children_set(index, child);
		}
	}

	/**
	 * @param component
	 *            Component being removed
	 */
	private final void removedComponent(final Component component)
	{
		// Notify Page that component is being removed
		final Page page = component.findPage();
		if (page != null)
		{
			page.componentRemoved(component);
		}

		component.detach();

		// Component is removed
		component.setParent(null);
	}

	/**
	 * Renders the next element of markup in the given markup stream.
	 * 
	 * @param markupStream
	 *            The markup stream
	 */
	private final void renderNext(final MarkupStream markupStream)
	{
		// Get the current markup element
		final MarkupElement element = markupStream.get();

		// If it a tag like <wicket..> or <span wicket:id="..." >
		if ((element instanceof ComponentTag) && !markupStream.atCloseTag())
		{
			// Get element as tag
			final ComponentTag tag = (ComponentTag)element;

			// Get component id
			final String id = tag.getId();

			// Get the component for the id from the given container
			final Component component = get(id);

			// Failed to find it?
			if (component != null)
			{
				component.render(markupStream);
			}
			else
			{
				// 2rd try: Components like Border and Panel might implement
				// the ComponentResolver interface as well.
				MarkupContainer container = this;
				while (container != null)
				{
					if (container instanceof IComponentResolver)
					{
						if (((IComponentResolver)container).resolve(this, markupStream, tag))
						{
							return;
						}
					}

					container = container.findParent(MarkupContainer.class);
				}

				// 3rd try: Try application's component resolvers
				final List componentResolvers = getApplication().getPageSettings()
						.getComponentResolvers();
				final Iterator iterator = componentResolvers.iterator();
				while (iterator.hasNext())
				{
					final IComponentResolver resolver = (IComponentResolver)iterator.next();
					if (resolver.resolve(this, markupStream, tag))
					{
						return;
					}
				}

				if (tag instanceof WicketTag)
				{
					if (((WicketTag)tag).isChildTag())
					{
						markupStream.throwMarkupException("Found " + tag.toString() +
								" but no <wicket:extend>");
					}
					else
					{
						markupStream.throwMarkupException("Failed to handle: " + tag.toString());
					}
				}

				// No one was able to handle the component id
				markupStream.throwMarkupException("Unable to find component with id '" + id +
						"' in " + this + ". This means that you declared wicket:id=" + id +
						" in your markup, but that you either did not add the " +
						"component to your page at all, or that the hierarchy does not match.");
			}
		}
		else
		{
			// Render as raw markup
			if (log.isDebugEnabled())
			{
				log.debug("Rendering raw markup");
			}
			getResponse().write(element.toCharSequence());
			markupStream.next();
		}
	}

	/**
	 * Get the markup stream for this component.
	 * 
	 * @return The markup stream for this component, or if it doesn't have one,
	 *         the markup stream for the nearest parent which does have one
	 */
	protected final MarkupStream findMarkupStream()
	{
		// Start here
		MarkupContainer c = this;

		// Walk up hierarchy until markup found
		while (c.getMarkupStream() == null)
		{
			// Check parent
			c = c.getParent();

			// Are we at the top of the hierarchy?
			if (c == null)
			{
				// Failed to find markup stream
				throw new WicketRuntimeException(exceptionMessage("No markup found"));
			}
		}

		return c.getMarkupStream();
	}

	/**
	 * Handle the container's body. If your override of this method does not
	 * advance the markup stream to the close tag for the openTag, a runtime
	 * exception will be thrown by the framework.
	 * 
	 * @param markupStream
	 *            The markup stream
	 * @param openTag
	 *            The open tag for the body
	 */
	protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		renderComponentTagBody(markupStream, openTag);
	}

	/**
	 * Renders this component. This implementation just calls renderComponent.
	 * 
	 * @param markupStream
	 */
	protected void onRender(final MarkupStream markupStream)
	{
		renderComponent(markupStream);
	}

	/**
	 * Renders this component and all sub-components using the given markup
	 * stream.
	 * 
	 * @param markupStream
	 *            The markup stream
	 */
	protected void renderAll(final MarkupStream markupStream)
	{
		// Loop through the markup in this container
		while (markupStream.hasMore())
		{
			// Element rendering is responsible for advancing markup stream!
			final int index = markupStream.getCurrentIndex();
			renderNext(markupStream);
			if (index == markupStream.getCurrentIndex())
			{
				markupStream.throwMarkupException("Component at markup stream index " + index +
						" failed to advance the markup stream");
			}
		}
	}

	/**
	 * Renders markup for the body of a ComponentTag from the current position
	 * in the given markup stream. If the open tag passed in does not require a
	 * close tag, nothing happens. Markup is rendered until the closing tag for
	 * openTag is reached.
	 * 
	 * @param markupStream
	 *            The markup stream
	 * @param openTag
	 *            The open tag
	 */
	protected final void renderComponentTagBody(final MarkupStream markupStream,
			final ComponentTag openTag)
	{
		// If the open tag requires a close tag
		boolean render = openTag.requiresCloseTag();
		if (render == false)
		{
			// Tags like <p> do not require a close tag, but they may have.
			render = !openTag.hasNoCloseTag();
		}
		if (render == true)
		{
			// Loop through the markup in this container
			while (markupStream.hasMore() && !markupStream.get().closes(openTag))
			{
				// Render markup element. Doing so must advance the markup
				// stream
				final int index = markupStream.getCurrentIndex();
				renderNext(markupStream);
				if (index == markupStream.getCurrentIndex())
				{
					markupStream.throwMarkupException("Markup element at index " + index +
							" failed to advance the markup stream");
				}
			}
		}
	}

	/**
	 * Set markup stream for this container.
	 * 
	 * @param markupStream
	 *            The markup stream
	 */
	protected final void setMarkupStream(final MarkupStream markupStream)
	{
		this.markupStream = markupStream;
	}

	final void internalAttach2()
	{
		if (!getFlag(FLAG_ATTACHED))
		{
			setFlag(FLAG_ATTACHING, true);
			visitChildren(new IVisitor()
			{
				public Object component(Component component)
				{
					component.setFlag(FLAG_ATTACHING, true);
					return IVisitor.CONTINUE_TRAVERSAL;
				}
			});
			setFlag(FLAG_ATTACH_SUPER_CALL_VERIFIED, false);
			onAttach();
			if (!getFlag(FLAG_ATTACH_SUPER_CALL_VERIFIED))
			{
				throw new IllegalStateException(
						"Component " +
								this +
								" of type " +
								getClass().getName() +
								" has not been properly attached.  " +
								"Something in its class hierarchy has failed to call super.onAttach() in an override of onAttach() method");
			}

			visitChildren(new IVisitor()
			{
				public Object component(Component component)
				{
					component.setFlag(FLAG_ATTACH_SUPER_CALL_VERIFIED, false);
					component.onAttach();
					if (!component.getFlag(FLAG_ATTACH_SUPER_CALL_VERIFIED))
					{
						throw new IllegalStateException(
								"Component " +
										component +
										" of type " +
										component.getClass().getName() +
										" has not been properly attached.  " +
										"Something in its class hierarchy has failed to call super.onAttach() in an override of onAttach() method");
					}
					return IVisitor.CONTINUE_TRAVERSAL;
				}
			});

			visitChildren(new IVisitor()
			{
				public Object component(Component component)
				{
					component.setFlag(FLAG_ATTACHING, false);
					component.setFlag(FLAG_ATTACHED, true);
					return IVisitor.CONTINUE_TRAVERSAL;
				}
			});

			setFlag(FLAG_ATTACHING, false);
			setFlag(FLAG_ATTACHED, true);
		}
	}

	void detachChildren()
	{
		// Loop through child components
		final Iterator iter = iterator();
		while (iter.hasNext())
		{
			// Get next child
			final Component child = (Component)iter.next();

			// Call end request on the child
			child.detach();
		}
		super.detachChildren();
	}

	void internalMarkRendering()
	{
		super.internalMarkRendering();
		final int size = children_size();
		for (int i = 0; i < size; i++)
		{
			final Component child = children_get(i);
			child.internalMarkRendering();
		}
	}

	private Component[] copyChildren()
	{
		int size = children_size();
		Component result[] = new Component[size];
		for (int i = 0; i < size; ++i)
		{
			result[i] = children_get(i);
		}
		return result;
	}

	void onBeforeRenderChildren()
	{
		super.onBeforeRenderChildren();

		// We need to copy the children list because the children components can
		// modify the hierarchy in their onBeforeRender.
		Component[] children = copyChildren();
		try
		{
			// Loop through child components
			for (int i = 0; i < children.length; i++)
			{
				// Get next child
				final Component child = children[i];

				// Call begin request on the child
				// We need to check whether the child's wasn't removed from the
				// component in the meanwhile (e.g. from another's child
				// onBeforeRender)
				if (child.getParent() == this)
				{
					child.beforeRender();
				}
			}
		}
		catch (RuntimeException ex)
		{
			if (ex instanceof WicketRuntimeException)
			{
				throw ex;
			}
			else
			{
				throw new WicketRuntimeException("Error attaching this container for rendering: " +
						this, ex);
			}
		}
	}

	void onAfterRenderChildren()
	{
		// Loop through child components
		final Iterator iter = iterator();
		while (iter.hasNext())
		{
			// Get next child
			final Component child = (Component)iter.next();

			// Call end request on the child
			child.afterRender();
		}
		super.onAfterRenderChildren();
	}

	/**
	 * @return True if this markup container has associated markup
	 */
	public boolean hasAssociatedMarkup()
	{
		return getApplication().getMarkupSettings().getMarkupCache().hasAssociatedMarkup(this);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15217.java