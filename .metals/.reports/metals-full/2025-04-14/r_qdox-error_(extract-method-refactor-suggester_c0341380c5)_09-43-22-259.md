error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1373.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1373.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1373.java
text:
```scala
i@@f (behaviors == null || !behaviors.contains(behavior))

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.AuthorizationException;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.authorization.UnauthorizedActionException;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupException;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IComponentInheritedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IModelComparator;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.settings.IDebugSettings;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.util.string.PrependingStringBuffer;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.version.undo.Change;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Component serves as the highest level abstract base class for all components.
 * 
 * <ul>
 * <li><b>Identity </b>- All Components must have a non-null id which is retrieved by calling
 * getId(). The id must be unique within the MarkupContainer that holds the Component, but does not
 * have to be globally unique or unique within a Page's component hierarchy.
 * 
 * <li><b>Hierarchy </b>- A component has a parent which can be retrieved with {@link #getParent()}.
 * If a component is an instance of MarkupContainer, it may have children. In this way it has a
 * place in the hierarchy of components contained on a given page.
 * 
 * <li><b>Component Paths </b>- The path from the Page at the root of the component hierarchy to a
 * given Component is simply the concatenation with dot separators of each id along the way. For
 * example, the path "a.b.c" would refer to the component named "c" inside the MarkupContainer named
 * "b" inside the container named "a". The path to a component can be retrieved by calling
 * getPath(). This path is an absolute path beginning with the id of the Page at the root. Pages
 * bear a PageMap/Session-relative identifier as their id, so each absolute path will begin with a
 * number, such as "0.a.b.c". To get a Component path relative to the page that contains it, you can
 * call getPageRelativePath().
 * 
 * <li><b>LifeCycle </b>- Components participate in the following lifecycle phases:
 * <ul>
 * <li><b>Construction </b>- A Component is constructed with the Java language new operator.
 * Children may be added during construction if the Component is a MarkupContainer.
 * 
 * <li><b>Request Handling </b>- An incoming request is processed by a protocol request handler
 * such as WicketServlet. An associated Application object creates Session, Request and Response
 * objects for use by a given Component in updating its model and rendering a response. These
 * objects are stored inside a container called {@link RequestCycle} which is accessible via
 * {@link Component#getRequestCycle()}. The convenience methods {@link Component#getRequest()},
 * {@link Component#getResponse()} and {@link Component#getSession()} provide easy access to the
 * contents of this container.
 * 
 * <li><b>Listener Invocation </b>- If the request references a listener on an existing Component,
 * that listener is called, allowing arbitrary user code to handle events such as link clicks or
 * form submits. Although arbitrary listeners are supported in Wicket, the need to implement a new
 * class of listener is unlikely for a web application and even the need to implement a listener
 * interface directly is highly discouraged. Instead, calls to listeners are routed through logic
 * specific to the event, resulting in calls to user code through other overridable methods. For
 * example, the {@link org.apache.wicket.markup.html.form.IFormSubmitListener#onFormSubmitted()}
 * method implemented by the Form class is really a private implementation detail of the Form class
 * that is not designed to be overridden (although unfortunately, it must be public since all
 * interface methods in Java must be public). Instead, Form subclasses should override user-oriented
 * methods such as onValidate(), onSubmit() and onError() (although only the latter two are likely
 * to be overridden in practice).
 * 
 * <li><b>onBeginRequest </b>- The {@link Component#onBeginRequest()} method is called.
 * 
 * <li><b>Form Submit </b>- If a Form has been submitted and the Component is a FormComponent, the
 * component's model is validated by a call to FormComponent.validate().
 * 
 * <li><b>Form Model Update </b>- If a valid Form has been submitted and the Component is a
 * FormComponent, the component's model is updated by a call to FormComponent.updateModel().
 * 
 * <li><b>Rendering </b>- A markup response is generated by the Component via
 * {@link Component#render()}, which calls subclass implementation code contained in
 * {@link Component#onRender(org.apache.wicket.markup.MarkupStream)}. Once this phase begins, a
 * Component becomes immutable. Attempts to alter the Component will result in a
 * WicketRuntimeException.
 * 
 * <li><b>onEndRequest </b>() - The {@link Component#onEndRequest()} method is called.
 * </ul>
 * 
 * <li><b>Component Models </b>- The primary responsibility of a component is to use its model (an
 * object that implements IModel), which can be set via {@link Component#setModel(IModel model)} and
 * retrieved via {@link Component#getModel()}, to render a response in an appropriate markup
 * language, such as HTML. In addition, form components know how to update their models based on
 * request information. Since the IModel interface is a wrapper around an actual model object, a
 * convenience method {@link Component#getModelObject()} is provided to retrieve the model Object
 * from its IModel wrapper. A further convenience method, {@link Component#getModelObjectAsString()},
 * is provided for the very common operation of converting the wrapped model Object to a String.
 * 
 * <li><b>Visibility </b>- Components which have setVisible(false) will return false from
 * isVisible() and will not render a response (nor will their children).
 * 
 * <li><b>Page </b>- The Page containing any given Component can be retrieved by calling
 * {@link Component#getPage()}. If the Component is not attached to a Page, an
 * IllegalStateException will be thrown. An equivalent method, {@link Component#findPage()} is
 * available for special circumstances where it might be desirable to get a null reference back
 * instead.
 * 
 * <li><b>Session </b>- The Page for a Component points back to the Session that contains the Page.
 * The Session for a component can be accessed with the convenience method getSession(), which
 * simply calls getPage().getSession().
 * 
 * <li><b>Locale </b>- The Locale for a Component is available through the convenience method
 * getLocale(), which is equivalent to getSession().getLocale().
 * 
 * <li><b>String Resources </b>- Components can have associated String resources via the
 * Application's Localizer, which is available through the method {@link Component#getLocalizer()}.
 * The convenience methods {@link Component#getString(String key)} and
 * {@link Component#getString(String key, IModel model)} wrap the identical methods on the
 * Application Localizer for easy access in Components.
 * 
 * <li><b>Style </b>- The style ("skin") for a component is available through
 * {@link Component#getStyle()}, which is equivalent to getSession().getStyle(). Styles are
 * intended to give a particular look to a Component or Resource that is independent of its Locale.
 * For example, a style might be a set of resources, including images and markup files, which gives
 * the design look of "ocean" to the user. If the Session's style is set to "ocean" and these
 * resources are given names suffixed with "_ocean", Wicket's resource management logic will prefer
 * these resources to other resources, such as default resources, which are not as good of a match.
 * 
 * <li><b>Variation </b>- Whereas Styles are Session (user) specific, variations are component
 * specific. E.g. if the Style is "ocean" and the Variation is "NorthSea", than the resources are
 * given the names suffixed with "_ocean_NorthSea".
 * 
 * <li><b>AttributeModifiers </b>- You can add one or more {@link AttributeModifier}s to any
 * component if you need to programmatically manipulate attributes of the markup tag to which a
 * Component is attached.
 * 
 * <li><b>Application, ApplicationSettings and ApplicationPages </b>- The getApplication() method
 * provides convenient access to the Application for a Component via getSession().getApplication().
 * The getApplicationSettings() method is equivalent to getApplication().getSettings(). The
 * getApplicationPages is equivalent to getApplication().getPages().
 * 
 * <li><b>Feedback Messages </b>- The {@link Component#debug(String)},
 * {@link Component#info(String)}, {@link Component#warn(String)},
 * {@link Component#error(java.io.Serializable)} and {@link Component#fatal(String)} methods
 * associate feedback messages with a Component. It is generally not necessary to use these methods
 * directly since Wicket validators automatically register feedback messages on Components. Any
 * feedback message for a given Component can be retrieved with {@link Component#getFeedbackMessage}.
 * 
 * <li><b>Page Factory </b>- It is possible to change the way that Pages are constructed by
 * overriding the {@link Component#getPageFactory()} method, returning your own implementation of
 * {@link org.apache.wicket.IPageFactory}.
 * 
 * <li><b>Versioning </b>- Pages are the unit of versioning in Wicket, but fine-grained control of
 * which Components should participate in versioning is possible via the
 * {@link Component#setVersioned(boolean)} method. The versioning participation of a given Component
 * can be retrieved with {@link Component#isVersioned()}.
 * 
 * <li><b>AJAX support</b>- Components can be re-rendered after the whole Page has been rendered
 * at least once by calling doRender().
 * 
 * @author Jonathan Locke
 * @author Chris Turner
 * @author Eelco Hillenius
 * @author Johan Compagner
 * @author Juergen Donnerstag
 * @author Igor Vaynberg (ivaynberg)
 */
public abstract class Component implements IClusterable, IConverterLocator
{
	/**
	 * Change record of a model.
	 */
	public class ComponentModelChange extends Change
	{
		private static final long serialVersionUID = 1L;

		/** Former model. */
		private final IModel model;

		/**
		 * Construct.
		 * 
		 * @param model
		 */
		public ComponentModelChange(IModel model)
		{
			super();
			this.model = model;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString()
		{
			return "ComponentModelChange[component: " + getPath() + "]";
		}

		/**
		 * @see org.apache.wicket.version.undo.Change#undo()
		 */
		public void undo()
		{
			setModel(model);
		}
	}

	/**
	 * Generic component visitor interface for component traversals.
	 */
	public static interface IVisitor
	{
		/**
		 * Value to return to continue a traversal.
		 */
		public static final Object CONTINUE_TRAVERSAL = null;

		/**
		 * A generic value to return to contiue a traversal, but if the component is a container,
		 * don't visit its children.
		 */
		public static final Object CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER = new Object();

		/**
		 * A generic value to return to stop a traversal.
		 */
		public static final Object STOP_TRAVERSAL = new Object();

		/**
		 * Called at each component in a traversal.
		 * 
		 * @param component
		 *            The component
		 * @return CONTINUE_TRAVERSAL (null) if the traversal should continue, or a non-null return
		 *         value for the traversal method if it should stop. If no return value is useful,
		 *         the generic non-null value STOP_TRAVERSAL can be used.
		 */
		public Object component(Component component);
	}

	/**
	 * Change object for undoing addition of behavior
	 * 
	 * @author Igor Vaynberg (ivaynberg)
	 */
	private final class AddedBehaviorChange extends Change
	{

		private static final long serialVersionUID = 1L;

		private final IBehavior behavior;

		/**
		 * Construct.
		 * 
		 * @param behavior
		 */
		public AddedBehaviorChange(IBehavior behavior)
		{
			this.behavior = behavior;
		}

		public String toString()
		{
			return "[" + getClass().getName() + " behavior=" + behavior.toString() + "]";
		}

		public void undo()
		{
			behaviors.remove(behavior);
			if (behaviors.size() == 0)
			{
				behaviors = null;
			}
		}

	}

	/**
	 * Undo change for component border property
	 * 
	 * @author ivaynberg
	 */
	private class ComponentBorderChange extends Change
	{
		private static final long serialVersionUID = 1L;

		private final IComponentBorder old = getComponentBorder();

		public void undo()
		{
			setComponentBorder(old);
		}

	}

	/**
	 * Change object for undoing removal of behavior
	 * 
	 * @author Igor Vaynberg (ivaynberg)
	 */
	private final class RemovedBehaviorChange extends Change
	{

		private static final long serialVersionUID = 1L;

		private final IBehavior behavior;

		/**
		 * Construct.
		 * 
		 * @param behavior
		 */
		public RemovedBehaviorChange(IBehavior behavior)
		{
			this.behavior = behavior;
		}

		public String toString()
		{
			return "[" + getClass().getName() + " behavior=" + behavior.toString() + "]";
		}

		public void undo()
		{
			if (behaviors == null)
			{
				behaviors = new ArrayList(1);
			}
			behaviors.add(behavior);
		}

	}


	/**
	 * A enabled change operation.
	 */
	protected final static class EnabledChange extends Change
	{
		private static final long serialVersionUID = 1L;

		/** Subject. */
		private final Component component;

		/** Former value. */
		private final boolean enabled;

		/**
		 * Construct.
		 * 
		 * @param component
		 */
		EnabledChange(final Component component)
		{
			this.component = component;
			enabled = component.getFlag(FLAG_ENABLED);
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString()
		{
			return "EnabledChange[component: " + component.getPath() + ",enabled: " + enabled + "]";
		}

		/**
		 * @see org.apache.wicket.version.undo.Change#undo()
		 */
		public void undo()
		{
			component.setEnabled(enabled);
		}
	}

	/**
	 * A visibility change operation.
	 */
	protected final static class VisibilityChange extends Change
	{
		private static final long serialVersionUID = 1L;

		/** Subject. */
		private final Component component;

		/** Former value. */
		private final boolean visible;

		/**
		 * Construct.
		 * 
		 * @param component
		 */
		VisibilityChange(final Component component)
		{
			this.component = component;
			visible = component.getFlag(FLAG_VISIBLE);
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString()
		{
			return "VisibilityChange[component: " + component.getPath() + ", visible: " + visible +
					"]";
		}

		/**
		 * @see org.apache.wicket.version.undo.Change#undo()
		 */
		public void undo()
		{
			component.setVisible(visible);
		}
	}


	/**
	 * Action used with IAuthorizationStrategy to determine whether a component is allowed to be
	 * enabled.
	 * <p>
	 * If enabling is authorized, a component may decide by itself (typically using it's enabled
	 * property) whether it is enabled or not. If enabling is not authorized, the given component is
	 * marked disabled, regardless its enabled property.
	 * <p>
	 * When a component is not allowed to be enabled (in effect disabled through the implementation
	 * of this interface), Wicket will try to prevent model updates too. This is not completely fail
	 * safe, as constructs like:
	 * 
	 * <pre>
	 * User u = (User)getModelObject();
	 * u.setName(&quot;got you there!&quot;);
	 * </pre>
	 * 
	 * can't be prevented. Indeed it can be argued that any model protection is best dealt with in
	 * your model objects to be completely secured. Wicket will catch all normal framework-directed
	 * use though.
	 */
	public static final Action ENABLE = new Action(Action.ENABLE);

	/** Separator for component paths */
	public static final char PATH_SEPARATOR = ':';

	/**
	 * Action used with IAuthorizationStrategy to determine whether a component and its children are
	 * allowed to be rendered.
	 * <p>
	 * There are two uses for this method:
	 * <ul>
	 * <li>The 'normal' use is for controlling whether a component is rendered without having any
	 * effect on the rest of the processing. If a strategy lets this method return 'false', then the
	 * target component and its children will not be rendered, in the same fashion as if that
	 * component had visibility property 'false'.</li>
	 * <li>The other use is when a component should block the rendering of the whole page. So
	 * instead of 'hiding' a component, what we generally want to achieve here is that we force the
	 * user to logon/give-credentials for a higher level of authorization. For this functionality,
	 * the strategy implementation should throw a {@link AuthorizationException}, which will then
	 * be handled further by the framework.</li>
	 * </ul>
	 * </p>
	 */
	public static final Action RENDER = new Action(Action.RENDER);

	/** meta data key for missing body tags logging. */
	private static final MetaDataKey BORDER_KEY = new MetaDataKey(IComponentBorder.class)
	{
		private static final long serialVersionUID = 1L;
	};

	/** Basic model IModelComparator implementation for normal object models */
	private static final IModelComparator defaultModelComparator = new IModelComparator()
	{
		private static final long serialVersionUID = 1L;

		public boolean compare(Component component, Object b)
		{
			final Object a = component.getModelObject();
			if (a == null && b == null)
			{
				return true;
			}
			if (a == null || b == null)
			{
				return false;
			}
			return a.equals(b);
		}
	};

	private static final int FLAG_AFTER_RENDERING = 0x8000000;

	/** True when a component is being auto-added */
	private static final int FLAG_AUTO = 0x0001;

	private static final int FLAG_BEFORE_RENDERING_SUPER_CALL_VERIFIED = 0x1000000;

	private static final int FLAG_DETACHING = 0x80000000;

	/** True when a component is enabled for model updates and is reachable. */
	private static final int FLAG_ENABLED = 0x0080;

	/** Flag for escaping HTML in model strings */
	private static final int FLAG_ESCAPE_MODEL_STRINGS = 0x0002;

	/**
	 * Boolean whether this component was rendered at least once for tracking changes.
	 */
	private static final int FLAG_HAS_BEEN_RENDERED = 0x1000;

	/** Ignore attribute modifiers */
	private static final int FLAG_IGNORE_ATTRIBUTE_MODIFIER = 0x0040;

	/** Flag for escaping HTML in model strings */
	private static final int FLAG_INHERITABLE_MODEL = 0x0004;

	/**
	 * Internal indicator of whether this component may be rendered given the current context's
	 * authorization. It overrides the visible flag in case this is false. Authorization is done
	 * before trying to render any component (otherwise we would end up with a half rendered page in
	 * the buffer)
	 */
	private static final int FLAG_IS_RENDER_ALLOWED = 0x2000;

	/**
	 * Whether or not the component should print out its markup id into the id attribute
	 */
	private static final int FLAG_OUTPUT_MARKUP_ID = 0x4000;

	/**
	 * Ouput a placeholder tag if the component is not visible. This is useful in ajax mode to go to
	 * visible(false) to visible(true) without the overhead of repaiting a visible parent container
	 */

	private static final int FLAG_PLACEHOLDER = 0x8000;

	/** Render tag boolean */
	private static final int FLAG_RENDER_BODY_ONLY = 0x0020;

	private static final int FLAG_RENDERING = 0x2000000;

	/** Versioning boolean */
	private static final int FLAG_VERSIONED = 0x0008;

	/** Visibility boolean */
	private static final int FLAG_VISIBLE = 0x0010;

	/** Log. */
	private static final Logger log = LoggerFactory.getLogger(Component.class);

	/**
	 * The name of attribute that will hold markup id
	 */
	private static final String MARKUP_ID_ATTR_NAME = "id";

	/**
	 * Metadata key used to store/retrieve markup id
	 */
	private static MetaDataKey MARKUP_ID_KEY = new MetaDataKey(String.class)
	{

		private static final long serialVersionUID = 1L;

	};
	private static final long serialVersionUID = 1L;
	/** Reserved subclass-definable flag bit */
	protected static final int FLAG_RESERVED1 = 0x0100;
	/** Reserved subclass-definable flag bit */
	protected static final int FLAG_RESERVED2 = 0x0200;

	/** Reserved subclass-definable flag bit */
	protected static final int FLAG_RESERVED3 = 0x0400;
	/** Reserved subclass-definable flag bit */
	protected static final int FLAG_RESERVED4 = 0x0800;
	/** Reserved subclass-definable flag bit */
	protected static final int FLAG_RESERVED5 = 0x10000;


	/** Reserved subclass-definable flag bit */
	protected static final int FLAG_RESERVED6 = 0x20000;

	/** Reserved subclass-definable flag bit */
	protected static final int FLAG_RESERVED7 = 0x40000;

	/** Reserved subclass-definable flag bit */
	protected static final int FLAG_RESERVED8 = 0x80000;

	/**
	 * Meta data key for line precise error logging for the moment of addition. Made package private
	 * for access in {@link MarkupContainer} and {@link Page}
	 */
	static final MetaDataKey ADDED_AT_KEY = new MetaDataKey(String.class)
	{
		private static final long serialVersionUID = 1L;
	};

	/**
	 * meta data key for line precise error logging for the moment of construction. Made package
	 * private for access in {@link Page}
	 */
	static final MetaDataKey CONSTRUCTED_AT_KEY = new MetaDataKey(String.class)
	{
		private static final long serialVersionUID = 1L;
	};

	static final int FLAG_ATTACH_SUPER_CALL_VERIFIED = 0x10000000;


	static final int FLAG_ATTACHED = 0x20000000;

	static final int FLAG_ATTACHING = 0x40000000;

	/**
	 * Flag that makes we are in before-render callback phase Set after component.onBeforeRender is
	 * invoked (right before invoking beforeRender on children)
	 */
	static final int FLAG_PREPARED_FOR_RENDER = 0x4000000;

	/** List of behaviors to be applied for this Component */
	private ArrayList behaviors = null;

	/** Component flags. See FLAG_* for possible non-exclusive flag values. */
	private int flags = FLAG_VISIBLE | FLAG_ESCAPE_MODEL_STRINGS | FLAG_VERSIONED | FLAG_ENABLED |
			FLAG_IS_RENDER_ALLOWED;

	/** Component id. */
	private String id;

	/**
	 * MetaDataEntry array.
	 */
	private MetaDataEntry[] metaData;

	/** Any parent container. */
	private MarkupContainer parent;

	/**
	 * I really dislike it, but for now we need it. Reason: due to transparent containers and
	 * IComponentResolver there is guaranteed 1:1 mapping between component and markup
	 */
	int markupIndex = -1;

	/** The model for this component. */
	IModel model;

	/**
	 * Constructor. All components have names. A component's id cannot be null. This is the minimal
	 * constructor of component. It does not register a model.
	 * 
	 * @param id
	 *            The non-null id of this component
	 * @throws WicketRuntimeException
	 *             Thrown if the component has been given a null id.
	 */
	public Component(final String id)
	{
		setId(id);
		getApplication().notifyComponentInstantiationListeners(this);

		final IDebugSettings debugSettings = Application.get().getDebugSettings();
		if (debugSettings.isLinePreciseReportingOnNewComponentEnabled())
		{
			setMetaData(CONSTRUCTED_AT_KEY, Strings.toString(this, new MarkupException(
					"constructed")));
		}
	}

	/**
	 * Constructor. All components have names. A component's id cannot be null. This constructor
	 * includes a model.
	 * 
	 * @param id
	 *            The non-null id of this component
	 * @param model
	 *            The component's model
	 * 
	 * @throws WicketRuntimeException
	 *             Thrown if the component has been given a null id.
	 */
	public Component(final String id, final IModel model)
	{
		this(id);
		this.model = wrap(model);
	}

	/**
	 * Adds an behavior modifier to the component.
	 * 
	 * <p>
	 * Note: this method is override to enable users to do things like discussed in <a
	 * href="http://www.nabble.com/Why-add%28IBehavior%29-is-final--tf2598263.html#a7248198">this
	 * thread</a>.
	 * </p>
	 * 
	 * @param behavior
	 *            The behavior modifier to be added
	 * @return this (to allow method call chaining)
	 */
	public Component add(final IBehavior behavior)
	{
		if (behavior == null)
		{
			throw new IllegalArgumentException("Argument may not be null");
		}

		// Lazy create
		if (behaviors == null)
		{
			behaviors = new ArrayList(1);
		}

		behaviors.add(behavior);

		if (!behavior.isTemporary())
		{
			addStateChange(new AddedBehaviorChange(behavior));
		}

		// Give handler the opportunity to bind this component
		behavior.bind(this);

		return this;
	}

	/**
	 * Called on very component after the page is renderd It will call onAfterRender for it self and
	 * its childeren.
	 */
	public final void afterRender()
	{
		// if the component has been previously attached via attach()
		// detach it now
		try
		{
			setFlag(FLAG_AFTER_RENDERING, true);
			onAfterRender();
			getApplication().notifyComponentOnAfterRenderListeners(this);
			if (getFlag(FLAG_AFTER_RENDERING))
			{
				throw new IllegalStateException(Component.class.getName() +
						" has not been properly detached. Something in the hierarchy of " +
						getClass().getName() +
						" has not called super.onAfterRender() in the override of onAfterRender() method");
			}
			// always detach children because components can be attached
			// independently of their parents
			onAfterRenderChildren();
		}
		finally
		{
			// this flag must always be set to false.
			setFlag(FLAG_RENDERING, false);
		}
	}

	/**
	 * Attaches the component. This is called when the page is starting to be used for rendering or
	 * when a component listener call is executed on it.
	 */
	public final void attach()
	{
		internalAttach2();
	}

	private final void internalBeforeRender()
	{
		if (isVisible() && !getFlag(FLAG_RENDERING) && !getFlag(FLAG_PREPARED_FOR_RENDER))
		{
			setFlag(FLAG_BEFORE_RENDERING_SUPER_CALL_VERIFIED, false);

			onBeforeRender();
			getApplication().notifyComponentOnBeforeRenderListeners(this);
			if (!getFlag(FLAG_BEFORE_RENDERING_SUPER_CALL_VERIFIED))
			{
				throw new IllegalStateException(Component.class.getName() +
						" has not been properly rendered. Something in the hierarchy of " +
						getClass().getName() +
						" has not called super.onBeforeRender() in the override of onBeforeRender() method");
			}
		}
	}

	/**
	 * We need to postpone calling beforeRender() on components that implement IFeedback, to be sure
	 * that all other component's beforeRender() has been already called, so that IFeedbacks can
	 * collect all feedback messages. This is the key under list of postponed IFeedback is stored to
	 * request cycle metadata. The List is then iterated over in {@link #prepareForRender()} after
	 * calling {@link #beforeRender()}, to initialize postponed components.
	 */
	private static final MetaDataKey FEEDBACK_LIST = new MetaDataKey(List.class)
	{
		private static final long serialVersionUID = 1L;
	};

	/**
	 * Called for every component when the page is getting to be rendered. it will call
	 * onBeforeRender for this component and all the child components
	 */
	public final void beforeRender()
	{
		if (!(this instanceof IFeedback))
		{
			internalBeforeRender();
		}
		else
		{
			// this component is a feedback. Feedback must be initialized last, so that
			// they can collect messages from other components
			List feedbacks = (List)getRequestCycle().getMetaData(FEEDBACK_LIST);
			if (feedbacks == null)
			{
				feedbacks = new ArrayList();
				getRequestCycle().setMetaData(FEEDBACK_LIST, (Serializable)feedbacks);
			}
			feedbacks.add(this);
		}
	}

	/**
	 * Redirects to any intercept page previously specified by a call to redirectToInterceptPage.
	 * 
	 * @return True if an original destination was redirected to
	 * @see Component#redirectToInterceptPage(Page)
	 */
	public final boolean continueToOriginalDestination()
	{
		return getPage().getPageMap().continueToOriginalDestination();
	}

	/**
	 * Registers a debug feedback message for this component
	 * 
	 * @param message
	 *            The feedback message
	 */
	public final void debug(final String message)
	{
		Session.get().getFeedbackMessages().debug(this, message);
		Session.get().dirty();
	}

	/**
	 * Detaches the component. This is called at the end of the request for all the pages that are
	 * touched in that request.
	 */
	public final void detach()
	{
		// if the component has been previously attached via attach()
		// detach it now
		setFlag(FLAG_DETACHING, true);
		onDetach();
		if (getFlag(FLAG_DETACHING))
		{
			throw new IllegalStateException(Component.class.getName() +
					" has not been properly detached. Something in the hierarchy of " +
					getClass().getName() +
					" has not called super.onDetach() in the override of onDetach() method");
		}
		setFlag(FLAG_ATTACHED, false);

		// always detach models because they can be attached without the
		// component. eg component has a compoundpropertymodel and one of its
		// children component's getmodelobject is called
		detachModels();

		// always detach children because components can be attached
		// independently of their parents
		detachChildren();

		// reset the model to null when the current model is a IWrapModel and
		// the model that created it/wrapped in it is a IComponentInheritedModel
		// The model will be created next time.
		if (getFlag(FLAG_INHERITABLE_MODEL))
		{
			model = null;
			setFlag(FLAG_INHERITABLE_MODEL, false);
		}
	}

	/**
	 * THIS IS WICKET INTERNAL ONLY. DO NOT USE IT.
	 * 
	 * Traverses all behaviors and calls detachModel() on them. This is needed to cleanup behavior
	 * after render. This method is necessary for {@link AjaxRequestTarget} to be able to cleanup
	 * component's behaviors after header contribution has been done (which is separated from
	 * component render).
	 */
	public final void detachBehaviors()
	{
		if (behaviors != null)
		{
			for (Iterator i = behaviors.iterator(); i.hasNext();)
			{
				IBehavior behavior = (IBehavior)i.next();

				// Always detach models, 'accepted' or not. Otherwise, if they
				// are accepted during render, but not here - something can go
				// undetached, and calling isEnabled can also lead to nasty side
				// effects. See for instance Timo's comment on
				// http://issues.apache.org/jira/browse/WICKET-673
				behavior.detach(this);

				if (behavior.isTemporary())
				{
					i.remove();
				}
			}
		}
	}

	/**
	 * Detaches all models
	 */
	public void detachModels()
	{
		// Detach any detachable model from this component
		detachModel();

		// detach any behaviors
		detachBehaviors();
	}

	/**
	 * Registers an error feedback message for this component
	 * 
	 * @param message
	 *            The feedback message
	 */
	public final void error(final Serializable message)
	{
		Session.get().getFeedbackMessages().error(this, message);
		Session.get().dirty();
	}

	/**
	 * Registers an fatal error feedback message for this component
	 * 
	 * @param message
	 *            The feedback message
	 */
	public final void fatal(final String message)
	{
		Session.get().getFeedbackMessages().fatal(this, message);
		Session.get().dirty();
	}

	/**
	 * Finds the first container parent of this component of the given class.
	 * 
	 * @param c
	 *            MarkupContainer class to search for
	 * @return First container parent that is an instance of the given class, or null if none can be
	 *         found
	 */
	public final MarkupContainer findParent(final Class c)
	{
		// Start with immediate parent
		MarkupContainer current = parent;

		// Walk up containment hierarchy
		while (current != null)
		{
			// Is current an instance of this class?
			if (c.isInstance(current))
			{
				return current;
			}

			// Check parent
			current = current.getParent();
		}

		// Failed to find component
		return null;
	}

	/**
	 * @return The nearest markup container with associated markup
	 */
	public final MarkupContainer findParentWithAssociatedMarkup()
	{
		MarkupContainer container = parent;
		while (container != null)
		{
			if (container.hasAssociatedMarkup())
			{
				return container;
			}
			container = container.getParent();
		}

		// This should never happen since Page always has associated markup
		throw new WicketRuntimeException("Unable to find parent with associated markup");
	}

	/**
	 * Gets interface to application that this component is a part of.
	 * 
	 * @return The application associated with the session that this component is in.
	 * @see Application
	 */
	public final Application getApplication()
	{
		return Application.get();
	}

	/**
	 * Gets the currently coupled {@link IBehavior}s as a unmodifiable list. Returns an empty list
	 * rather than null if there are no behaviors coupled to this component.
	 * 
	 * @return The currently coupled behaviors as a unmodifiable list
	 */
	public final List/* <IBehavior> */getBehaviors()
	{
		return getBehaviors(null);
	}

	/**
	 * @return A path of the form [page-class-name].[page-relative-path]
	 * @see Component#getPageRelativePath()
	 */
	public final String getClassRelativePath()
	{
		return getClass().getName() + PATH_SEPARATOR + getPageRelativePath();
	}

	/**
	 * @return component border assigned to this component, or null if none
	 */
	public final IComponentBorder getComponentBorder()
	{
		return (IComponentBorder)getMetaData(BORDER_KEY);
	}

	/**
	 * @return nothing, will always throw an exception. Use {@link #getConverter(Class)} instead.
	 * @deprecated To be removed. Please use/ override {@link #getConverter(Class)} instead.
	 */
	public final IConverter getConverter()
	{
		throw new UnsupportedOperationException("use #getConverter(Class) instead");
	}

	/**
	 * Gets the converter that should be used by this component.
	 * 
	 * @param type
	 *            The type to convert to
	 * 
	 * @return The converter that should be used by this component
	 */
	public IConverter getConverter(Class/* <?> */type)
	{
		return getApplication().getConverterLocator().getConverter(type);
	}

	/**
	 * Gets whether model strings should be escaped.
	 * 
	 * @return Returns whether model strings should be escaped
	 */
	public final boolean getEscapeModelStrings()
	{
		return getFlag(FLAG_ESCAPE_MODEL_STRINGS);
	}

	/**
	 * @return Any feedback message for this component
	 */
	public final FeedbackMessage getFeedbackMessage()
	{
		return Session.get().getFeedbackMessages().messageForComponent(this);
	}

	/**
	 * Gets the id of this component.
	 * 
	 * @return The id of this component
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @return Innermost model for this component
	 */
	public final IModel getInnermostModel()
	{
		return getInnermostModel(getModel());
	}

	/**
	 * Gets the locale for this component. By default, it searches it parent for a locale. If no
	 * parents (it's a recursive search) returns a locale, it gets one from the session.
	 * 
	 * @return The locale to be used for this component
	 * @see Session#getLocale()
	 */
	public Locale getLocale()
	{
		Locale locale = null;
		if (parent != null)
		{
			locale = parent.getLocale();
		}
		return (locale != null) ? locale : getSession().getLocale();
	}

	/**
	 * Convenience method to provide easy access to the localizer object within any component.
	 * 
	 * @return The localizer object
	 */
	public final Localizer getLocalizer()
	{
		return getApplication().getResourceSettings().getLocalizer();
	}

	/**
	 * THIS IS WICKET INTERNAL ONLY. DO NOT USE IT.
	 * 
	 * Get a copy of the markup's attributes which are associated with the component.
	 * <p>
	 * Modifications to the map returned don't change the tags attributes. It is just a copy.
	 * <p>
	 * Note: The component must have been added (directly or indirectly) to a container with an
	 * associated markup file (Page, Panel or Border).
	 * 
	 * @return markup attributes
	 */
	public final ValueMap getMarkupAttributes()
	{
		MarkupStream markupStream = locateMarkupStream();
		ValueMap attrs = new ValueMap(markupStream.getTag().getAttributes());
		attrs.makeImmutable();
		return attrs;
	}

	/**
	 * Retrieves id by which this component is represented within the markup. This is either the id
	 * attribute set explicitly via a call to {@link #setMarkupId(String)}, id attribute defined in
	 * the markup, or an automatically generated id - in that order.
	 * <p>
	 * If no explicit id is set this function will generate an id value that will be unique in the
	 * page. This is the preferred way as there is no chance of id collision.
	 * <p>
	 * Note: This method should only be called after the component or its parent have been added to
	 * the page.
	 * 
	 * @return markup id of the component
	 */
	public String getMarkupId()
	{
		String markupId = (String)getMetaData(MARKUP_ID_KEY);
		if (markupId == null)
		{
			Page page = findPage();
			if (page == null)
			{
				throw new WicketRuntimeException(
						"This component is not (yet) coupled to a page. It has to be able "
								+ "to find the page it is supposed to operate in before you can call "
								+ "this method (Component#getMarkupId)");
			}
			// try to read from markup
			// TODO getting the id from markup doesn't work everywhere yet.
			// unfortunately, we have to drop this until we have a good solution
			// for issue http://issues.apache.org/jira/browse/WICKET-694
			// markupId = getMarkupAttributes().getString("id");

			// if (markupId == null)
			// {
			// if not in the markup, generate one

			markupId = getId() + page.getAutoIndex();
			// make sure id is compliant with w3c requirements (starts with a
			// letter)
			char c = markupId.charAt(0);
			if (!Character.isLetter(c))
			{
				markupId = "id" + markupId;
			}

			// }
			setMetaData(MARKUP_ID_KEY, markupId);

		}
		return markupId;
	}

	/**
	 * Gets metadata for this component using the given key.
	 * 
	 * @param key
	 *            The key for the data
	 * @return The metadata or null of no metadata was found for the given key
	 * @see MetaDataKey
	 */
	public final Serializable getMetaData(final MetaDataKey key)
	{
		return key.get(metaData);
	}

	/**
	 * Gets the model. It returns the object that wraps the backing model.
	 * 
	 * @return The model
	 */
	public final IModel getModel()
	{
		// If model is null
		if (model == null)
		{
			// give subclass a chance to lazy-init model
			model = initModel();
		}

		return model;
	}

	/**
	 * Gets the backing model object; this is shorthand for getModel().getObject().
	 * 
	 * @return The backing model object
	 */
	public final Object getModelObject()
	{
		final IModel model = getModel();
		if (model != null)
		{
			// Get model value for this component.
			return model.getObject();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Gets a model object as a string.
	 * 
	 * @return Model object for this component as a string
	 */
	public final String getModelObjectAsString()
	{
		return getModelObjectAsString(getModelObject());
	}

	/**
	 * @param modelObject
	 *            Model object to convert to string
	 * @return The string
	 */
	public final String getModelObjectAsString(final Object modelObject)
	{
		if (modelObject != null)
		{
			// Get converter
			final IConverter converter = getConverter(modelObject.getClass());

			// Model string from property
			final String modelString = converter.convertToString(modelObject, getLocale());

			if (modelString != null)
			{
				// If we should escape the markup
				if (getFlag(FLAG_ESCAPE_MODEL_STRINGS))
				{
					// Escape it
					return Strings.escapeMarkup(modelString, false, true).toString();
				}
				return modelString;
			}
		}
		return "";
	}

	/**
	 * Gets whether or not component will output id attribute into the markup. id attribute will be
	 * set to the value returned from {@link Component#getMarkupId()}.
	 * 
	 * @return whether or not component will output id attribute into the markup
	 */
	public final boolean getOutputMarkupId()
	{
		return getFlag(FLAG_OUTPUT_MARKUP_ID);
	}

	/**
	 * Gets the page holding this component.
	 * 
	 * @return The page holding this component
	 * @throws IllegalStateException
	 *             Thrown if component is not yet attached to a Page.
	 */
	public final Page getPage()
	{
		// Search for nearest Page
		final Page page = findPage();

		// If no Page was found
		if (page == null)
		{
			// Give up with a nice exception
			throw new IllegalStateException("No Page found for component " + this);
		}

		return page;
	}

	/**
	 * @return The page factory for the session that this component is in
	 */
	public final IPageFactory getPageFactory()
	{
		return getSession().getPageFactory();
	}

	/**
	 * Gets the path to this component relative to the page it is in.
	 * 
	 * @return The path to this component relative to the page it is in
	 */
	public final String getPageRelativePath()
	{
		return Strings.afterFirstPathComponent(getPath(), PATH_SEPARATOR);
	}

	/**
	 * Gets any parent container, or null if there is none.
	 * 
	 * @return Any parent container, or null if there is none
	 */
	public final MarkupContainer getParent()
	{
		return parent;
	}

	/**
	 * Gets this component's path.
	 * 
	 * @return Colon separated path to this component in the component hierarchy
	 */
	public final String getPath()
	{
		final PrependingStringBuffer buffer = new PrependingStringBuffer(32);
		for (Component c = this; c != null; c = c.getParent())
		{
			if (buffer.length() > 0)
			{
				buffer.prepend(PATH_SEPARATOR);
			}
			buffer.prepend(c.getId());
		}
		return buffer.toString();
	}

	/**
	 * If false the component's tag will be printed as well as its body (which is default). If true
	 * only the body will be printed, but not the component's tag.
	 * 
	 * @return If true, the component tag will not be printed
	 */
	public final boolean getRenderBodyOnly()
	{
		return getFlag(FLAG_RENDER_BODY_ONLY);
	}

	/**
	 * @return The request for this component's active request cycle
	 */
	public final Request getRequest()
	{
		RequestCycle requestCycle = getRequestCycle();
		if (requestCycle == null)
		{
			// Happens often with WicketTester when one forgets to call
			// createRequestCycle()
			throw new WicketRuntimeException("No RequestCycle is currently set!");
		}
		return requestCycle.getRequest();
	}

	/**
	 * Gets the active request cycle for this component
	 * 
	 * @return The request cycle
	 */
	public final RequestCycle getRequestCycle()
	{
		return RequestCycle.get();
	}

	/**
	 * @return The response for this component's active request cycle
	 */
	public final Response getResponse()
	{
		return getRequestCycle().getResponse();
	}

	/**
	 * Gets the current Session object.
	 * 
	 * @return The Session that this component is in
	 */
	public Session getSession()
	{
		return Session.get();
	}

	/**
	 * @return Size of this Component in bytes
	 */
	public long getSizeInBytes()
	{
		final MarkupContainer originalParent = parent;
		parent = null;
		long size = -1;
		try
		{
			size = Objects.sizeof(this);
		}
		catch (Exception e)
		{
			log.error("Exception getting size for component " + this, e);
		}
		parent = originalParent;
		return size;
	}

	/**
	 * @param key
	 *            Key of string resource in property file
	 * @return The String
	 * @see Localizer
	 */
	public final String getString(final String key)
	{
		return getString(key, null);
	}

	/**
	 * @param key
	 *            The resource key
	 * @param model
	 *            The model
	 * @return The formatted string
	 * @see Localizer
	 */
	public final String getString(final String key, final IModel model)
	{
		return getLocalizer().getString(key, this, model);
	}

	/**
	 * @param key
	 *            The resource key
	 * @param model
	 *            The model
	 * @param defaultValue
	 *            A default value if the string cannot be found
	 * @return The formatted string
	 * @see Localizer
	 */
	public final String getString(final String key, final IModel model, final String defaultValue)
	{
		return getLocalizer().getString(key, this, model, defaultValue);
	}

	/**
	 * Gets the style of this component (see {@link org.apache.wicket.Session}).
	 * 
	 * @return The style of this component.
	 * 
	 * @see org.apache.wicket.Session
	 * @see org.apache.wicket.Session#getStyle()
	 */
	public final String getStyle()
	{
		String variation = getVariation();
		String style = getSession().getStyle();
		if (variation != null && !"".equals(variation))
		{
			if (style != null && !"".equals(style))
			{
				style = variation + "_" + style;
			}
			else
			{
				style = variation;
			}
		}
		return style;
	}

	/**
	 * Gets the variation string of this component that will be used to look up markup for this
	 * component. Subclasses can override this method to define by an instance what markup variation
	 * should be picked up. By default it will return null or the value of a parent.
	 * 
	 * @return The variation of this component.
	 */
	public String getVariation()
	{
		String variation = null;
		if (parent != null)
		{
			variation = parent.getVariation();
		}
		return variation;
	}

	/**
	 * Gets whether this component was rendered at least once.
	 * 
	 * @return true if the component has been rendered before, false if it is merely constructed
	 */
	public final boolean hasBeenRendered()
	{
		return getFlag(FLAG_HAS_BEEN_RENDERED);
	}

	/**
	 * @return True if this component has an error message
	 */
	public final boolean hasErrorMessage()
	{
		return Session.get().getFeedbackMessages().hasErrorMessageFor(this);
	}

	/**
	 * @return True if this component has some kind of feedback message
	 */
	public final boolean hasFeedbackMessage()
	{
		return Session.get().getFeedbackMessages().hasMessageFor(this);
	}

	/**
	 * Registers an informational feedback message for this component
	 * 
	 * @param message
	 *            The feedback message
	 */
	public final void info(final String message)
	{
		Session.get().getFeedbackMessages().info(this, message);
		Session.get().dirty();
	}

	/**
	 * @deprecated
	 */
	// TODO remove after deprecation release
	public final void internalAttach()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	// TODO remove after deprecation release
	public final void internalDetach()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Authorizes an action for a component.
	 * 
	 * @param action
	 *            The action to authorize
	 * @return True if the action is allowed
	 * @throws AuthorizationException
	 *             Can be thrown by implementation if action is unauthorized
	 */
	public final boolean isActionAuthorized(Action action)
	{
		IAuthorizationStrategy authorizationStrategy = getSession().getAuthorizationStrategy();
		if (authorizationStrategy != null)
		{
			return authorizationStrategy.isActionAuthorized(this, action);
		}
		return true;
	}

	/**
	 * Returns true if this component is an ancestor of the given component
	 * 
	 * @param component
	 *            The component to check
	 * @return True if the given component has this component as an ancestor
	 * @deprecated use getParent().contains(component, false)
	 */
	public final boolean isAncestorOf(final Component component)
	{
		return getParent().contains(component, false);
		// // Walk up containment hierarchy
		// for (MarkupContainer current = component.parent; current != null;
		// current = current
		// .getParent())
		// {
		// // Is this an ancestor?
		// if (current == this)
		// {
		// return true;
		// }
		// }
		//
		// // This component is not an ancestor of the given component
		// return false;
	}

	/**
	 * @return true if this component is authorized to be enabled, false otherwise
	 */
	public final boolean isEnableAllowed()
	{
		return isActionAuthorized(ENABLE);
	}

	/**
	 * Gets whether this component is enabled. Specific components may decide to implement special
	 * behavior that uses this property, like web form components that add a disabled='disabled'
	 * attribute when enabled is false.
	 * 
	 * @return Whether this component is enabled.
	 */
	public boolean isEnabled()
	{
		return getFlag(FLAG_ENABLED);
	}

	/**
	 * Checks the security strategy if the {@link Component#RENDER} action is allowed on this
	 * component
	 * 
	 * @return ture if {@link Component#RENDER} action is allowed, false otherwise
	 */
	public final boolean isRenderAllowed()
	{
		return getFlag(FLAG_IS_RENDER_ALLOWED);
	}

	/**
	 * Returns if the component is stateless or not. It checks the stateless hint if that is false
	 * it returns directly false. If that is still true it checks all its behaviours if they can be
	 * stateless.
	 * 
	 * @return whether the component is stateless.
	 */
	public final boolean isStateless()
	{
		if (!getStatelessHint())
		{
			return false;
		}

		final Iterator behaviors = getBehaviors().iterator();

		while (behaviors.hasNext())
		{
			IBehavior behavior = (IBehavior)behaviors.next();
			if (!behavior.getStatelessHint(this))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * @return True if this component is versioned
	 */
	public boolean isVersioned()
	{
		// Is the component itself versioned?
		if (!getFlag(FLAG_VERSIONED) || !getFlag(FLAG_HAS_BEEN_RENDERED))
		{
			return false;
		}
		else
		{
			// If there's a parent and this component is versioned
			if (parent != null)
			{
				// Check if the parent is unversioned. If any parent
				// (recursively) is unversioned, then this component is too
				if (!parent.isVersioned())
				{
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * Gets whether this component and any children are visible.
	 * <p>
	 * WARNING: this method can be called multiple times during a request. If you override this
	 * method, it is a good idea to keep it cheap in terms of processing. Alternatively, you can
	 * call {@link #setVisible(boolean)}.
	 * <p>
	 * 
	 * @return True if component and any children are visible
	 */
	public boolean isVisible()
	{
		return getFlag(FLAG_VISIBLE);
	}

	/**
	 * Checks if the component itself and all its parents are visible.
	 * 
	 * @return true if the component and all its parents are visible.
	 */
	public final boolean isVisibleInHierarchy()
	{
		Component component = this;
		while (component != null)
		{
			if (component.isRenderAllowed() && component.isVisible())
			{
				component = component.getParent();
			}
			else
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Sets the RENDERING flag on component and it's children.
	 */
	public final void markRendering()
	{
		internalMarkRendering();
	}

	/**
	 * Called to indicate that the model content for this component has been changed
	 */
	public final void modelChanged()
	{
		// Call user code
		internalOnModelChanged();
		onModelChanged();
	}

	/**
	 * Called to indicate that the model content for this component is about to change
	 */
	public final void modelChanging()
	{
		checkHierarchyChange(this);

		// Call user code
		onModelChanging();

		// Tell the page that our model changed
		final Page page = findPage();
		if (page != null)
		{
			page.componentModelChanging(this);
		}
	}

	/**
	 * Creates a new page using the component's page factory
	 * 
	 * @param c
	 *            The class of page to create
	 * @return The new page
	 */
	public final Page newPage(final Class c)
	{
		return getPageFactory().newPage(c);
	}

	/**
	 * Creates a new page using the component's page factory
	 * 
	 * @param c
	 *            The class of page to create
	 * @param parameters
	 *            Any parameters to pass to the constructor
	 * @return The new page
	 */
	public final Page newPage(final Class c, final PageParameters parameters)
	{
		return getPageFactory().newPage(c, parameters);
	}

	/**
	 * Prepares the component and it's children for rendering. On whole page render this method must
	 * be called on the page. On AJAX request, this method must be called on updated component.
	 */
	public void prepareForRender()
	{
		beforeRender();
		List feedbacks = (List)getRequestCycle().getMetaData(FEEDBACK_LIST);
		if (feedbacks != null)
		{
			for (Iterator i = feedbacks.iterator(); i.hasNext();)
			{
				Component feedback = (Component)i.next();
				feedback.internalBeforeRender();
			}
		}
		getRequestCycle().setMetaData(FEEDBACK_LIST, null);
		markRendering();
	}

	/**
	 * Redirects browser to an intermediate page such as a sign-in page. The current request's url
	 * is saved for future use by method continueToOriginalDestination(); Only use this method when
	 * you plan to continue to the current url at some later time; otherwise just use
	 * setResponsePage or - when you are in a constructor or checkAccessMethod, call redirectTo.
	 * 
	 * @param page
	 *            The sign in page
	 * 
	 * @see Component#continueToOriginalDestination()
	 */
	public final void redirectToInterceptPage(final Page page)
	{
		getPage().getPageMap().redirectToInterceptPage(page);
	}

	/**
	 * Removes this component from its parent. It's important to remember that a component that is
	 * removed cannot be referenced from the markup still.
	 */
	public final void remove()
	{
		if (parent == null)
		{
			throw new IllegalStateException("Cannot remove " + this + " from null parent!");
		}

		parent.remove(this);
	}

	/**
	 * Removes behavior from component
	 * 
	 * @param behavior
	 *            behavior to remove
	 * 
	 * @return this (to allow method call chaining)
	 */
	public Component remove(final IBehavior behavior)
	{
		if (behavior == null)
		{
			throw new IllegalArgumentException("Argument `behavior` cannot be null");
		}
		if (!behaviors.contains(behavior))
		{
			throw new IllegalStateException(
					"Tried to remove a behavior that was not added to the component. Behavior: " +
							behavior.toString());
		}

		if (!behavior.isTemporary())
		{
			addStateChange(new RemovedBehaviorChange(behavior));
		}
		behaviors.remove(behavior);

		if (behaviors.size() == 0)
		{
			behaviors = null;
		}

		return this;
	}

	/**
	 * Performs a render of this component as part of a Page level render process.
	 * <p>
	 * For component level re-render (e.g. AJAX) please call {@link #renderComponent()}. Though
	 * render() does seem to work, it will fail for panel children.
	 */
	public final void render()
	{
		// Allow currently invisible components to be re-rendered as well
		MarkupStream markupStream = null;
		if (getParent() != null)
		{
			markupStream = findMarkupStream();
		}

		render(markupStream);
	}


	/**
	 * Performs a render of this component as part of a Page level render process.
	 * <p>
	 * For component level re-render (e.g. AJAX) please call {@link #renderComponent(MarkupStream)}.
	 * Though render() does seem to work, it will fail for panel children.
	 * 
	 * @param markupStream
	 */
	public final void render(final MarkupStream markupStream)
	{
		// We need to know the index before we do the visibility check.
		// Otherwise
		// we wouldn't know the markup index for invisible components
		if (markupStream != null)
		{
			markupIndex = markupStream.getCurrentIndex();
		}

		markRendering();

		setMarkupStream(markupStream);
		setFlag(FLAG_HAS_BEEN_RENDERED, true);

		// Determine if component is visible using it's authorization status
		// and the isVisible property.
		if (isRenderAllowed() && isVisible())
		{
			// Rendering is beginning
			if (log.isDebugEnabled())
			{
				log.debug("Begin render " + this);
			}

			try
			{
				// Call implementation to render component
				notifyBehaviorsComponentBeforeRender();
				final IComponentBorder border = getComponentBorder();
				if (border != null)
				{
					border.renderBefore(this);
				}
				onRender(markupStream);
				if (border != null)
				{
					border.renderAfter(this);
				}
				// Component has been rendered
				rendered();
			}
			catch (RuntimeException ex)
			{
				// Call each behaviors onException() to allow the
				// behavior to clean up
				if (behaviors != null)
				{
					for (Iterator i = behaviors.iterator(); i.hasNext();)
					{
						IBehavior behavior = (IBehavior)i.next();
						if (isBehaviorAccepted(behavior))
						{
							try
							{
								behavior.exception(this, ex);
							}
							catch (Throwable ex2)
							{
								log.error("Error while cleaning up after exception", ex2);
							}
						}
					}
				}

				// Re-throw the exception
				throw ex;
			}

			if (log.isDebugEnabled())
			{
				log.debug("End render " + this);
			}
		}
		// markupStream is null when rendering a page
		else if (markupStream != null)
		{
			if (getFlag(FLAG_PLACEHOLDER))
			{
				// write out a placeholder tag into the markup
				final ComponentTag tag = markupStream.getTag();

				getResponse().write("<");
				getResponse().write(tag.getName());
				getResponse().write(" id=\"");
				getResponse().write(getMarkupId());
				getResponse().write("\" style=\"display:none\"></");
				getResponse().write(tag.getName());
				getResponse().write(">");
			}
			markupStream.skipComponent();
		}
	}

	/**
	 * Page.renderPage() is used to render a whole page. With AJAX however it must be possible to
	 * render any one component contained in a page. That is what this method is for.
	 * <p>
	 * Note: it is not necessary that the page has previously been rendered. But the component must
	 * have been added (directly or indirectly) to a container with an associated markup file (Page,
	 * Panel or Border).
	 */
	public final void renderComponent()
	{
		// If this Component is a Page
		if (this instanceof Page)
		{
			// Render as Page, with all the special logic that entails
			((Page)this).renderPage();
		}
		else
		{
			// Save the parent's markup stream to re-assign it at the end
			MarkupContainer parent = getParent();
			MarkupStream originalMarkupStream = parent.getMarkupStream();
			MarkupStream markupStream = locateMarkupStream();

			try
			{
				// Make sure that while rendering the markup stream is found
				parent.setMarkupStream(markupStream);

				prepareForRender();
				// check authorization
				// first the component itself
				// (after attach as otherwise list views etc wont work)
				setRenderAllowed(isActionAuthorized(RENDER));
				// check children if this is a container
				if (this instanceof MarkupContainer)
				{
					MarkupContainer container = (MarkupContainer)this;
					container.visitChildren(new IVisitor()
					{
						public Object component(final Component component)
						{
							// Find out if this component can be rendered
							final boolean renderAllowed = component.isActionAuthorized(RENDER);
							// Authorize rendering
							component.setRenderAllowed(renderAllowed);
							return IVisitor.CONTINUE_TRAVERSAL;
						}
					});
				}

				// Render the component and all its children
				render(markupStream);
			}
			finally
			{
				// Make sure the original markup stream is back in place
				parent.setMarkupStream(originalMarkupStream);
				afterRender();
			}
		}
	}


	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT USE IT.
	 * <p>
	 * Renders the component at the current position in the given markup stream. The method
	 * onComponentTag() is called to allow the component to mutate the start tag. The method
	 * onComponentTagBody() is then called to permit the component to render its body.
	 * 
	 * @param markupStream
	 *            The markup stream
	 */
	public final void renderComponent(final MarkupStream markupStream)
	{
		markupIndex = markupStream.getCurrentIndex();

		// Get mutable copy of next tag
		final ComponentTag openTag = markupStream.getTag();
		final ComponentTag tag = openTag.mutable();

		// Call any tag handler
		onComponentTag(tag);

		// If we're an openclose tag
		if (!tag.isOpenClose() && !tag.isOpen())
		{
			// We were something other than <tag> or <tag/>
			markupStream
					.throwMarkupException("Method renderComponent called on bad markup element: " +
							tag);
		}

		if (tag.isOpenClose() && openTag.isOpen())
		{
			markupStream
					.throwMarkupException("You can not modify a open tag to open-close: " + tag);
		}

		try
		{
			// Render open tag
			if (getRenderBodyOnly() == false)
			{
				renderComponentTag(tag);
			}
			markupStream.next();

			// Render the body only if open-body-close. Do not render if
			// open-close.
			if (tag.isOpen())
			{
				// Render the body
				onComponentTagBody(markupStream, tag);
			}

			// Render close tag
			if (tag.isOpen())
			{
				if (openTag.isOpen())
				{
					renderClosingComponentTag(markupStream, tag, getRenderBodyOnly());
				}
				else
				{
					// If a open-close tag has been to modified to be
					// open-body-close than a synthetic close tag must be
					// rendered.
					if (getRenderBodyOnly() == false)
					{
						final boolean stripWicketTags = Application.get().getMarkupSettings()
								.getStripWicketTags();
						if (!(openTag instanceof WicketTag) || !stripWicketTags)
						{
							// Close the manually opened panel tag.
							getResponse().write(openTag.syntheticCloseTagString());
						}
					}
				}
			}
		}
		catch (RuntimeException re)
		{
			if (re instanceof WicketRuntimeException || re instanceof AbortException)
			{
				throw re;
			}
			throw new WicketRuntimeException("Exception in rendering component: " + this, re);
		}
	}

	/**
	 * Called to indicate that a component has been rendered. This method should only very rarely be
	 * called at all. One usage is in ImageMap, which renders its link children its own special way
	 * (without calling render() on them). If ImageMap did not call rendered() to indicate that its
	 * child components were actually rendered, the framework would think they had never been
	 * rendered, and in development mode this would result in a runtime exception.
	 */
	public final void rendered()
	{
		notifyBehaviorsComponentRendered();
		// Tell the page that the component rendered
		getPage().componentRendered(this);
	}

	/**
	 * Print to the web response what ever the component wants to contribute to the head section.
	 * Make sure that all attached behaviors are asked as well.
	 * <p>
	 * NOT intended for overriding by framework clients. Rather, use
	 * {@link IHeaderContributor#renderHead(org.apache.wicket.markup.html.IHeaderResponse)}
	 * </p>
	 * 
	 * @param container
	 *            The HtmlHeaderContainer
	 */
	public void renderHead(final HtmlHeaderContainer container)
	{
		if (isVisible())
		{
			if (this instanceof IHeaderContributor)
			{
				((IHeaderContributor)this).renderHead(container.getHeaderResponse());
			}

			// Ask all behaviors if they have something to contribute to the
			// header or body onLoad tag.
			if (behaviors != null)
			{
				final Iterator iter = behaviors.iterator();
				while (iter.hasNext())
				{
					IBehavior behavior = (IBehavior)iter.next();
					if (behavior instanceof IHeaderContributor && isBehaviorAccepted(behavior))
					{
						((IHeaderContributor)behavior).renderHead(container.getHeaderResponse());
					}
				}
			}
		}
	}

	/**
	 * Replaces this component with another. The replacing component must have the same component id
	 * as this component. This method serves as a shortcut to
	 * <code>this.getParent().replace(replacement)</code> and provides a better context for
	 * errors.
	 * 
	 * @since 1.2.1
	 * 
	 * @param replacement
	 *            component to replace this one
	 */
	public void replaceWith(Component replacement)
	{
		if (replacement == null)
		{
			throw new IllegalArgumentException("Argument [[replacement]] cannot be null.");
		}
		if (!getId().equals(replacement.getId()))
		{
			throw new IllegalArgumentException(
					"Replacement component must have the same id as the component it will replace. Replacement id [[" +
							replacement.getId() + "]], replaced id [[" + getId() + "]].");
		}
		if (parent == null)
		{
			throw new IllegalStateException(
					"This method can only be called on a component that has already been added to its parent.");
		}
		parent.replace(replacement);
	}

	/**
	 * @param component
	 *            The component to compare with
	 * @return True if the given component's model is the same as this component's model.
	 */
	public final boolean sameInnermostModel(final Component component)
	{
		return sameInnermostModel(component.getModel());
	}

	/**
	 * @param model
	 *            The model to compare with
	 * @return True if the given component's model is the same as this component's model.
	 */
	public final boolean sameInnermostModel(final IModel model)
	{
		// Get the two models
		IModel thisModel = getModel();
		IModel thatModel = model;

		// If both models are non-null they could be the same
		if (thisModel != null && thatModel != null)
		{
			return getInnermostModel(thisModel) == getInnermostModel(thatModel);
		}

		return false;
	}

	/**
	 * Assigns a component border to this component. If called with <code>null</code> any previous
	 * border will be cleared.
	 * 
	 * @param border
	 *            componnet border to assign, or <code>null</code> to clear any previous
	 * @return component for chaining
	 */
	public final Component setComponentBorder(final IComponentBorder border)
	{
		if (!Objects.equal(getComponentBorder(), border))
		{
			addStateChange(new ComponentBorderChange());
		}
		setMetaData(BORDER_KEY, border);
		return this;
	}

	/**
	 * Sets whether this component is enabled. Specific components may decide to implement special
	 * behavior that uses this property, like web form components that add a disabled='disabled'
	 * attribute when enabled is false. If it is not enabled, it will not be allowed to call any
	 * listener method on it (e.g. Link.onClick) and the model object will be protected (for the
	 * common use cases, not for programmer's misuse)
	 * 
	 * @param enabled
	 *            whether this component is enabled
	 * @return This
	 */
	public final Component setEnabled(final boolean enabled)
	{
		// Is new enabled state a change?
		if (enabled != getFlag(FLAG_ENABLED))
		{
			// Tell the page that this component's enabled was changed
			if (isVersioned())
			{
				final Page page = findPage();
				if (page != null)
				{
					addStateChange(new EnabledChange(this));
				}
			}

			// Change visibility
			setFlag(FLAG_ENABLED, enabled);
		}
		return this;
	}

	/**
	 * Sets whether model strings should be escaped.
	 * 
	 * @param escapeMarkup
	 *            True is model strings should be escaped
	 * @return This
	 */
	public final Component setEscapeModelStrings(final boolean escapeMarkup)
	{
		setFlag(FLAG_ESCAPE_MODEL_STRINGS, escapeMarkup);
		return this;
	}

	/**
	 * Sets this component's markup id to a user defined value. It is up to the user to ensure this
	 * value is unique.
	 * <p>
	 * The recommended way is to let wicket generate the value automatically, this method is here to
	 * serve as an override for that value in cases where a specific id must be used.
	 * <p>
	 * If null is passed in the user defined value is cleared and markup id value will fall back on
	 * automatically generated value
	 * 
	 * @see #getMarkupId()
	 * 
	 * @param markupId
	 *            markup id value or null to clear any previous user defined value
	 */
	public void setMarkupId(String markupId)
	{
		if (markupId != null && Strings.isEmpty(markupId))
		{
			throw new IllegalArgumentException("Markup id cannot be an empty string");
		}
		setMetaData(MARKUP_ID_KEY, markupId);

	}

	/**
	 * Sets the metadata for this component using the given key. If the metadata object is not of
	 * the correct type for the metadata key, an IllegalArgumentException will be thrown. For
	 * information on creating MetaDataKeys, see {@link MetaDataKey}.
	 * 
	 * @param key
	 *            The singleton key for the metadata
	 * @param object
	 *            The metadata object
	 * @throws IllegalArgumentException
	 * @see MetaDataKey
	 */
	public final void setMetaData(final MetaDataKey key, final Serializable object)
	{
		metaData = key.set(metaData, object);
	}

	/**
	 * Sets the given model.
	 * <p>
	 * WARNING: DO NOT OVERRIDE THIS METHOD UNLESS YOU HAVE A VERY GOOD REASON FOR IT. OVERRIDING
	 * THIS MIGHT OPEN UP SECURITY LEAKS AND BREAK BACK-BUTTON SUPPORT.
	 * </p>
	 * 
	 * @param model
	 *            The model
	 * @return This
	 */
	public Component setModel(final IModel model)
	{
		// Detach current model
		if (this.model != null)
		{
			this.model.detach();
		}

		IModel prevModel = this.model;
		if (prevModel instanceof IWrapModel)
		{
			prevModel = ((IWrapModel)prevModel).getWrappedModel();
		}

		// Change model
		if (prevModel != model)
		{
			if (prevModel != null)
			{
				addStateChange(new ComponentModelChange(prevModel));
			}

			this.model = wrap(model);
		}

		modelChanged();
		return this;
	}

	/**
	 * Sets the backing model object; shorthand for getModel().setObject(object).
	 * 
	 * @param object
	 *            The object to set
	 * @return This
	 */
	public final Component setModelObject(final Object object)
	{
		final IModel model = getModel();

		// Check whether anything can be set at all
		if (model == null)
		{
			throw new IllegalStateException(
					"Attempt to set model object on null model of component: " +
							getPageRelativePath());
		}

		// Check authorization
		if (!isActionAuthorized(ENABLE))
		{
			throw new UnauthorizedActionException(this, ENABLE);
		}

		// Check whether this will result in an actual change
		if (!getModelComparator().compare(this, object))
		{
			modelChanging();
			model.setObject(object);
			modelChanged();
		}

		return this;
	}

	/**
	 * Sets whether or not component will output id attribute into the markup. id attribute will be
	 * set to the value returned from {@link Component#getMarkupId()}.
	 * 
	 * @param output
	 *            True if the component will out the id attribute into markup. Please note that the
	 *            default behavior is to use the same id as the component. This means that your
	 *            component must begin with [a-zA-Z] in order to generate a valid markup id
	 *            according to: http://www.w3.org/TR/html401/types.html#type-name
	 * 
	 * @return this for chaining
	 */
	public final Component setOutputMarkupId(final boolean output)
	{
		setFlag(FLAG_OUTPUT_MARKUP_ID, output);
		return this;
	}

	/**
	 * Render a placeholder tag when the component is not visible. The tag is of form:
	 * &lt;componenttag style="display:none;" id="componentid"/&gt;. This method will also call
	 * <code>setOutputMarkupId(true)</code>.
	 * 
	 * This is useful, for example, in ajax situations where the component starts out invisible and
	 * then becomes visible through an ajax update. With a placeholder tag already in the markup you
	 * do not need to repaint this component's parent, instead you can repaint the component
	 * directly.
	 * 
	 * When this method is called with parameter <code>false</code> the outputmarkupid flag is not
	 * reverted to false.
	 * 
	 * @param outputTag
	 * @return this for chaining
	 */
	public final Component setOutputMarkupPlaceholderTag(final boolean outputTag)
	{
		if (outputTag != getFlag(FLAG_PLACEHOLDER))
		{
			if (outputTag)
			{
				setOutputMarkupId(true);
				setFlag(FLAG_PLACEHOLDER, true);
			}
			else
			{
				setFlag(FLAG_PLACEHOLDER, false);
				// I think it's better to not setOutputMarkupId to false...
				// user can do it if we want
			}
		}
		return this;
	}

	/**
	 * @param redirect
	 *            True if the response should be redirected to
	 * @see RequestCycle#setRedirect(boolean)
	 */
	public final void setRedirect(final boolean redirect)
	{
		getRequestCycle().setRedirect(redirect);
	}

	/**
	 * If false the component's tag will be printed as well as its body (which is default). If true
	 * only the body will be printed, but not the component's tag.
	 * 
	 * @param renderTag
	 *            If true, the component tag will not be printed
	 * @return This
	 */
	public final Component setRenderBodyOnly(final boolean renderTag)
	{
		this.setFlag(FLAG_RENDER_BODY_ONLY, renderTag);
		return this;
	}

	/**
	 * Sets the page that will respond to this request
	 * 
	 * @param cls
	 *            The response page class
	 * @see RequestCycle#setResponsePage(Class)
	 */
	public final void setResponsePage(final Class cls)
	{
		getRequestCycle().setResponsePage(cls);
	}


	/**
	 * Sets the page class and its parameters that will respond to this request
	 * 
	 * @param cls
	 *            The response page class
	 * @param parameters
	 *            The parameters for this bookmarkable page.
	 * @see RequestCycle#setResponsePage(Class, PageParameters)
	 */
	public final void setResponsePage(final Class cls, PageParameters parameters)
	{
		getRequestCycle().setResponsePage(cls, parameters);
	}

	/**
	 * Sets the page that will respond to this request
	 * 
	 * @param page
	 *            The response page
	 * @see RequestCycle#setResponsePage(Page)
	 */
	public final void setResponsePage(final Page page)
	{
		getRequestCycle().setResponsePage(page);
	}

	/**
	 * @param versioned
	 *            True to turn on versioning for this component, false to turn it off for this
	 *            component and any children.
	 * @return This
	 */
	public Component setVersioned(boolean versioned)
	{
		setFlag(FLAG_VERSIONED, versioned);
		return this;
	}

	/**
	 * Sets whether this component and any children are visible.
	 * 
	 * @param visible
	 *            True if this component and any children should be visible
	 * @return This
	 */
	public final Component setVisible(final boolean visible)
	{
		// Is new visibility state a change?
		if (visible != getFlag(FLAG_VISIBLE))
		{
			// record component's visibility change
			addStateChange(new VisibilityChange(this));

			// Change visibility
			setFlag(FLAG_VISIBLE, visible);
		}
		return this;
	}

	/**
	 * Gets the string representation of this component.
	 * 
	 * @return The path to this component
	 */
	public String toString()
	{
		return toString(true);
	}

	/**
	 * @param detailed
	 *            True if a detailed string is desired
	 * @return The string
	 */
	public String toString(final boolean detailed)
	{
		if (detailed)
		{
			final Page page = findPage();
			if (page == null)
			{
				return new StringBuffer("[Component id = ").append(getId()).append(
						", page = <No Page>, path = ").append(getPath()).append(".").append(
						Classes.simpleName(getClass())).append("]").toString();
			}
			else
			{
				return new StringBuffer("[Component id = ").append(getId()).append(", page = ")
						.append(getPage().getClass().getName()).append(", path = ").append(
								getPath()).append(".").append(Classes.simpleName(getClass()))
						.append(", isVisible = ").append((isRenderAllowed() && isVisible()))
						.append(", isVersioned = ").append(isVersioned()).append("]").toString();
			}
		}
		else
		{
			return "[Component id = " + getId() + "]";
		}
	}

	/**
	 * Returns a bookmarkable URL that references a given page class using a given set of page
	 * parameters. Since the URL which is returned contains all information necessary to instantiate
	 * and render the page, it can be stored in a user's browser as a stable bookmark.
	 * 
	 * @see RequestCycle#urlFor(IPageMap, Class, PageParameters)
	 * 
	 * @param pageClass
	 *            Class of page
	 * @param parameters
	 *            Parameters to page
	 * @return Bookmarkable URL to page
	 */
	public final CharSequence urlFor(final Class pageClass, final PageParameters parameters)
	{
		return getRequestCycle().urlFor(getPage().getPageMap(), pageClass, parameters);
	}

	/**
	 * Gets a URL for the listener interface on a behaviour (e.g. IBehaviorListener on
	 * AjaxPagingNavigationBehavior).
	 * 
	 * @param behaviour
	 *            The behaviour that the URL should point to
	 * @param listener
	 *            The listener interface that the URL should call
	 * @return The URL
	 */
	public final CharSequence urlFor(final IBehavior behaviour,
			final RequestListenerInterface listener)
	{
		return getRequestCycle().urlFor(this, behaviour, listener);
	}

	/**
	 * Returns a bookmarkable URL that references a given page class using a given set of page
	 * parameters. Since the URL which is returned contains all information necessary to instantiate
	 * and render the page, it can be stored in a user's browser as a stable bookmark.
	 * 
	 * @see RequestCycle#urlFor(IPageMap, Class, PageParameters)
	 * 
	 * @param pageMap
	 *            Page map to use
	 * @param pageClass
	 *            Class of page
	 * @param parameters
	 *            Parameters to page
	 * 
	 * 
	 * @return Bookmarkable URL to page
	 */
	public final CharSequence urlFor(final IPageMap pageMap, final Class pageClass,
			final PageParameters parameters)
	{
		return getRequestCycle().urlFor(pageMap, pageClass, parameters);
	}

	/**
	 * Returns a URL that references the given request target.
	 * 
	 * @see RequestCycle#urlFor(IRequestTarget)
	 * 
	 * @param requestTarget
	 *            the request target to reference
	 * 
	 * @return a URL that references the given request target
	 */
	public final CharSequence urlFor(final IRequestTarget requestTarget)
	{
		return getRequestCycle().urlFor(requestTarget);
	}

	/**
	 * Gets a URL for the listener interface (e.g. ILinkListener).
	 * 
	 * @param listener
	 *            The listener interface that the URL should call
	 * @return The URL
	 */
	public final CharSequence urlFor(final RequestListenerInterface listener)
	{
		return getRequestCycle().urlFor(this, listener);
	}

	/**
	 * Returns a URL that references a shared resource through the provided resource reference.
	 * 
	 * @see RequestCycle#urlFor(ResourceReference)
	 * 
	 * @param resourceReference
	 *            The resource reference
	 * @return The url for the shared resource
	 */
	public final CharSequence urlFor(final ResourceReference resourceReference)
	{
		return getRequestCycle().urlFor(resourceReference);
	}

	/**
	 * Traverses all parent components of the given class in this container, calling the visitor's
	 * visit method at each one.
	 * 
	 * @param c
	 *            Class
	 * @param visitor
	 *            The visitor to call at each parent of the given type
	 * @return First non-null value returned by visitor callback
	 */
	public final Object visitParents(final Class c, final IVisitor visitor)
	{
		// Start here
		Component current = this;

		// Walk up containment hierarchy
		while (current != null)
		{
			// Is current an instance of this class?
			if (c.isInstance(current))
			{
				final Object object = visitor.component(current);
				if (object != IVisitor.CONTINUE_TRAVERSAL)
				{
					return object;
				}
			}

			// Check parent
			current = current.getParent();
		}
		return null;
	}

	/**
	 * Registers a warning feedback message for this component.
	 * 
	 * @param message
	 *            The feedback message
	 */
	public final void warn(final String message)
	{
		Session.get().getFeedbackMessages().warn(this, message);
		Session.get().dirty();
	}

	/**
	 * {@link IBehavior#beforeRender(Component)} Notify all behaviors that are assigned to this
	 * component that the component is about to be rendered.
	 */
	private void notifyBehaviorsComponentBeforeRender()
	{
		if (behaviors != null)
		{
			for (Iterator i = behaviors.iterator(); i.hasNext();)
			{
				IBehavior behavior = (IBehavior)i.next();
				if (isBehaviorAccepted(behavior))
				{
					behavior.beforeRender(this);
				}
			}
		}
	}

	/**
	 * {@link IBehavior#afterRender(Component)} Notify all behaviors that are assigned to this
	 * component that the component has rendered.
	 */
	private void notifyBehaviorsComponentRendered()
	{
		// notify the behaviors that component has been rendered
		if (behaviors != null)
		{
			for (Iterator i = behaviors.iterator(); i.hasNext();)
			{
				IBehavior behavior = (IBehavior)i.next();
				if (isBehaviorAccepted(behavior))
				{
					behavior.afterRender(this);
				}
			}
		}
	}

	/**
	 * Adds state change to page.
	 * 
	 * @param change
	 *            The change
	 */
	protected final void addStateChange(final Change change)
	{
		checkHierarchyChange(this);
		final Page page = findPage();
		if (page != null)
		{
			page.componentStateChanging(this, change);
		}
	}

	/**
	 * Checks whether the given type has the expected name.
	 * 
	 * @param tag
	 *            The tag to check
	 * @param name
	 *            The expected tag name
	 * @throws MarkupException
	 *             Thrown if the tag is not of the right name
	 */
	protected final void checkComponentTag(final ComponentTag tag, final String name)
	{
		if (!tag.getName().equalsIgnoreCase(name))
		{
			findMarkupStream().throwMarkupException(
					"Component " + getId() + " must be applied to a tag of type '" + name +
							"', not " + tag.toUserDebugString());
		}
	}

	/**
	 * Checks that a given tag has a required attribute value.
	 * 
	 * @param tag
	 *            The tag
	 * @param key
	 *            The attribute key
	 * @param value
	 *            The required value for the attribute key
	 * @throws MarkupException
	 *             Thrown if the tag does not have the required attribute value
	 */
	protected final void checkComponentTagAttribute(final ComponentTag tag, final String key,
			final String value)
	{
		if (key != null)
		{
			final String tagAttributeValue = tag.getAttributes().getString(key);
			if (tagAttributeValue == null || !value.equalsIgnoreCase(tagAttributeValue))
			{
				findMarkupStream().throwMarkupException(
						"Component " + getId() + " must be applied to a tag with '" + key +
								"' attribute matching '" + value + "', not '" + tagAttributeValue +
								"'");
			}
		}
	}

	/**
	 * Checks whether the hierarchy may be changed at all, and throws an exception if this is not
	 * the case.
	 * 
	 * @param component
	 *            the component which is about to be added or removed
	 */
	protected void checkHierarchyChange(final Component component)
	{
		// Throw exception if modification is attempted during rendering
		if (!component.isAuto() && getFlag(FLAG_RENDERING))
		{
			throw new WicketRuntimeException(
					"Cannot modify component hierarchy during render phase");
		}

		// Throw exception if modification is attempted during attach
		if (getFlag(FLAG_ATTACHING))
		{
			throw new WicketRuntimeException(
					"Cannot modify component hierarchy during attach phase");
		}
	}

	/**
	 * Detaches the model for this component if it is detachable.
	 */
	protected void detachModel()
	{
		if (model != null)
		{
			model.detach();
		}
		// also detach the wrapped model of a component assigned wrap (not
		// inherited)
		if (model instanceof IWrapModel && !getFlag(FLAG_INHERITABLE_MODEL))
		{
			((IWrapModel)model).getWrappedModel().detach();
		}
	}

	/**
	 * Prefixes an exception message with useful information about this. component.
	 * 
	 * @param message
	 *            The message
	 * @return The modified message
	 */
	protected final String exceptionMessage(final String message)
	{
		return message + ":\n" + toString();
	}

	/**
	 * Finds the markup stream for this component.
	 * 
	 * @return The markup stream for this component. Since a Component cannot have a markup stream,
	 *         we ask this component's parent to search for it.
	 */
	protected MarkupStream findMarkupStream()
	{
		if (parent == null)
		{
			throw new IllegalStateException("Cannot find markupstream for " + this +
					" as there is no parent");
		}

		return parent.findMarkupStream();
	}

	/**
	 * If this Component is a Page, returns self. Otherwise, searches for the nearest Page parent in
	 * the component hierarchy. If no Page parent can be found, null is returned.
	 * 
	 * @return The Page or null if none can be found
	 */
	protected final Page findPage()
	{
		// Search for page
		return (Page)(this instanceof Page ? this : findParent(Page.class));
	}

	/**
	 * Gets the subset of the currently coupled {@link IBehavior}s that are of the provided type as
	 * a unmodifiable list or null if there are no behaviors attached. Returns an empty list rather
	 * than null if there are no behaviors coupled to this component.
	 * 
	 * @param type
	 *            The type or null for all
	 * 
	 * @return The subset of the currently coupled behaviors that are of the provided type as a
	 *         unmodifiable list or null
	 */
	protected List/* <IBehavior> */getBehaviors(Class type)
	{
		if (behaviors == null)
		{
			return Collections.EMPTY_LIST;
		}

		List subset = new ArrayList(behaviors.size()); // avoid growing
		for (Iterator i = behaviors.iterator(); i.hasNext();)
		{
			Object behavior = i.next();
			if (type == null || type.isAssignableFrom(behavior.getClass()))
			{
				subset.add(behavior);
			}
		}
		return Collections.unmodifiableList(subset);
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT USE IT!
	 * 
	 * @param flag
	 *            The flag to test
	 * @return True if the flag is set
	 */
	protected final boolean getFlag(final int flag)
	{
		return (flags & flag) != 0;
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT USE IT!
	 * 
	 * @param flag
	 *            The flag to test
	 * @return True if the flag is set
	 */
	protected final boolean getFlag(final short flag)
	{
		return getFlag((int)flag);
	}

	/**
	 * Finds the innermost IModel object for an IModel that might contain nested IModel(s).
	 * 
	 * @param model
	 *            The model
	 * @return The innermost (most nested) model
	 */
	protected final IModel getInnermostModel(final IModel model)
	{
		IModel nested = model;
		while (nested != null && nested instanceof IWrapModel)
		{
			final IModel next = ((IWrapModel)nested).getWrappedModel();
			if (nested == next)
			{
				throw new WicketRuntimeException("Model for " + nested + " is self-referential");
			}
			nested = next;
		}
		return nested;
	}

	/**
	 * Gets the value defaultModelComparator. Implementations of this interface can be used in the
	 * Component.getComparator() for testing the current value of the components model data with the
	 * new value that is given.
	 * 
	 * @return the value defaultModelComparator
	 */
	protected IModelComparator getModelComparator()
	{
		return defaultModelComparator;
	}

	/**
	 * Returns whether the component can be stateless. Also the component behaviors must be
	 * stateless, otherwise the component will be treat as statefull. In order for page to be
	 * stateless (and not to be stored in session), all components (and component behaviors) must be
	 * stateless.
	 * 
	 * @return whether the component can be stateless
	 */
	protected boolean getStatelessHint()
	{
		return true;
	}

	/**
	 * Called when a null model is about to be retrieved in order to allow a subclass to provide an
	 * initial model. This gives FormComponent, for example, an opportunity to instantiate a model
	 * on the fly using the containing Form's model.
	 * 
	 * @return The model
	 */
	protected IModel initModel()
	{
		// Search parents for CompoundPropertyModel
		for (Component current = getParent(); current != null; current = current.getParent())
		{
			// Get model
			// Dont call the getModel() that could initialize many inbetween
			// completely useless models.
			// IModel model = current.getModel();
			IModel model = current.model;

			if (model instanceof IWrapModel)
			{
				model = ((IWrapModel)model).getWrappedModel();
			}

			if (model instanceof IComponentInheritedModel)
			{
				// we turn off versioning as we share the model with another
				// component that is the owner of the model (that component
				// has to decide whether to version or not
				// TODO can we really do this?? Model shouldn't versioned but
				// all other things?? (add/remove)
				setVersioned(false);

				// return the shared inherited
				model = ((IComponentInheritedModel)model).wrapOnInheritance(this);
				setFlag(FLAG_INHERITABLE_MODEL, true);
				return model;
			}
		}

		// No model for this component!
		return null;
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL OR OVERRIDE.
	 * 
	 * Called when a request begins.
	 * 
	 * @Deprecated use {@link #onBeforeRender()} instead
	 */
	protected final void internalOnAttach()
	{
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL OR OVERRIDE.
	 * 
	 * Called when a request ends.
	 * 
	 * @Deprecated use {@link #onBeforeRender()} instead
	 * 
	 */
	protected final void internalOnDetach()
	{
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL OR OVERRIDE.
	 * 
	 * Called anytime a model is changed via setModel or setModelObject.
	 */
	protected void internalOnModelChanged()
	{
	}

	/**
	 * @return true if this component is attached
	 */
	protected final boolean isAttached()
	{
		return getFlag(FLAG_ATTACHED);
	}

	/**
	 * Components are allowed to reject behavior modifiers.
	 * 
	 * @param behavior
	 * @return False, if the component should not apply this behavior
	 */
	protected boolean isBehaviorAccepted(final IBehavior behavior)
	{
		// Ignore AttributeModifiers when FLAG_IGNORE_ATTRIBUTE_MODIFIER is set
		if ((behavior instanceof AttributeModifier) &&
				(getFlag(FLAG_IGNORE_ATTRIBUTE_MODIFIER) != false))
		{
			return false;
		}

		return behavior.isEnabled(this);
	}

	/**
	 * If true, all attribute modifiers will be ignored
	 * 
	 * @return True, if attribute modifiers are to be ignored
	 */
	protected final boolean isIgnoreAttributeModifier()
	{
		return this.getFlag(FLAG_IGNORE_ATTRIBUTE_MODIFIER);
	}

	/**
	 * @return Component's markup stream
	 */
	protected MarkupStream locateMarkupStream()
	{
		return new MarkupFragmentFinder().find(this);
	}

	/**
	 * Called just after a component is rendered.
	 */
	protected void onAfterRender()
	{
		setFlag(FLAG_AFTER_RENDERING, false);
	}

	/**
	 * Called to allow a component to attach resources for use.
	 * 
	 * Overrides of this method MUST call the super implementation, the most logical place to do
	 * this is the first line of the override method.
	 * 
	 */
	protected void onAttach()
	{
		setFlag(FLAG_ATTACH_SUPER_CALL_VERIFIED, true);
	}

	/**
	 * Called just before a component is rendered.
	 * <p>
	 * *NOTE* If you override this, you *must* call super.onBeforeRender() within your
	 * implementation.
	 * 
	 * Because this method is responsible for cascading {@link #onBeforeRender()} call to its
	 * children it is strongly recommended that super call is made at the end of the override.
	 * </p>
	 */
	protected void onBeforeRender()
	{
		setFlag(FLAG_PREPARED_FOR_RENDER, true);
		onBeforeRenderChildren();
		setFlag(FLAG_BEFORE_RENDERING_SUPER_CALL_VERIFIED, true);
	}

	/**
	 * @deprecated use onAttach() instead
	 */
	// TODO remove after the deprecation release
	protected final void onBeginRequest()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Processes the component tag.
	 * 
	 * @param tag
	 *            Tag to modify
	 */
	protected void onComponentTag(final ComponentTag tag)
	{
		// We can't try to get the ID from markup. This could be different than
		// id returned from getMarkupId() prior first rendering the component
		// (due to transparent resolvers and borders which break the 1:1
		// component <-> markup relation)
		if (getFlag(FLAG_OUTPUT_MARKUP_ID))
		{
			tag.put(MARKUP_ID_ATTR_NAME, getMarkupId());
		}
	}

	/**
	 * Processes the body.
	 * 
	 * @param markupStream
	 *            The markup stream
	 * @param openTag
	 *            The open tag for the body
	 */
	protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
	}

	/**
	 * Called to allow a component to detach resources after use.
	 * 
	 * Overrides of this method MUST call the super implementation, the most logical place to do
	 * this is the last line of the override method.
	 * 
	 * 
	 */
	protected void onDetach()
	{
		if (behaviors != null)
		{
			behaviors.trimToSize();
		}
		setFlag(FLAG_DETACHING, false);

	}

	/**
	 * @deprecated use onDetach() instead
	 */
	// TODO remove after the deprecation release
	protected final void onEndRequest()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Called anytime a model is changed after the change has occurred
	 */
	protected void onModelChanged()
	{
	}

	/**
	 * Called anytime a model is changed, but before the change actually occurs
	 */
	protected void onModelChanging()
	{
	}

	/**
	 * Implementation that renders this component.
	 * 
	 * @since Wicket 1.2
	 * @param markupStream
	 */
	protected abstract void onRender(final MarkupStream markupStream);

	/**
	 * Writes a simple tag out to the response stream. Any components that might be referenced by
	 * the tag are ignored. Also undertakes any tag attribute modifications if they have been added
	 * to the component.
	 * 
	 * @param tag
	 *            The tag to write
	 */
	protected final void renderComponentTag(ComponentTag tag)
	{
		final boolean stripWicketTags = Application.get().getMarkupSettings().getStripWicketTags();
		if (!(tag instanceof WicketTag) || !stripWicketTags)
		{
			// Apply behavior modifiers
			if ((behaviors != null) && !behaviors.isEmpty() && !tag.isClose() &&
					(isIgnoreAttributeModifier() == false))
			{
				tag = tag.mutable();

				for (Iterator i = behaviors.iterator(); i.hasNext();)
				{
					IBehavior behavior = (IBehavior)i.next();

					// Components may reject some behavior components
					if (isBehaviorAccepted(behavior))
					{
						behavior.onComponentTag(this, tag);
					}
				}
			}

			// apply behaviors that are attached to the component tag.
			if (tag.hasBehaviors())
			{
				Iterator behaviors = tag.getBehaviors();
				while (behaviors.hasNext())
				{
					final IBehavior behavior = (IBehavior)behaviors.next();
					behavior.onComponentTag(this, tag);
				}
			}

			// Write the tag
			tag
					.writeOutput(getResponse(), stripWicketTags, findMarkupStream()
							.getWicketNamespace());
		}
	}

	/**
	 * Replaces the body with the given one.
	 * 
	 * @param markupStream
	 *            The markup stream to replace the tag body in
	 * @param tag
	 *            The tag
	 * @param body
	 *            The new markup
	 */
	protected final void replaceComponentTagBody(final MarkupStream markupStream,
			final ComponentTag tag, final CharSequence body)
	{
		// The tag might have been changed from open-close to open. Hence
		// we'll need what was in the markup itself
		ComponentTag markupOpenTag = null;

		// If tag has a body
		if (tag.isOpen())
		{
			// Get what tag was in the markup; not what the user it might
			// have changed it to.
			markupStream.setCurrentIndex(markupStream.getCurrentIndex() - 1);
			markupOpenTag = markupStream.getTag();
			markupStream.next();

			// If it was an open tag in the markup as well, than ...
			if (markupOpenTag.isOpen())
			{
				// skip any raw markup in the body
				markupStream.skipRawMarkup();
			}
		}

		if (body != null)
		{
			// Write the new body
			getResponse().write(body);
		}

		// If we had an open tag (and not an openclose tag) and we found a
		// close tag, we're good
		if (tag.isOpen())
		{
			// If it was an open tag in the markup, than there must be
			// a close tag as well.
			if ((markupOpenTag != null) && markupOpenTag.isOpen() && !markupStream.atCloseTag())
			{
				// There must be a component in this discarded body
				markupStream.throwMarkupException("Expected close tag for '" + markupOpenTag +
						"' Possible attempt to embed component(s) '" + markupStream.get() +
						"' in the body of this component which discards its body");
			}
		}
	}

	/**
	 * @param auto
	 *            True to put component into auto-add mode
	 */
	protected final void setAuto(final boolean auto)
	{
		setFlag(FLAG_AUTO, auto);
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT USE IT!
	 * 
	 * @param flag
	 *            The flag to set
	 * @param set
	 *            True to turn the flag on, false to turn it off
	 */
	protected final void setFlag(final int flag, final boolean set)
	{
		if (set)
		{
			flags |= flag;
		}
		else
		{
			flags &= ~flag;
		}
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT USE IT!
	 * 
	 * @param flag
	 *            The flag to set
	 * @param set
	 *            True to turn the flag on, false to turn it off
	 */
	protected final void setFlag(final short flag, final boolean set)
	{
		setFlag((int)flag, set);
	}

	/**
	 * If true, all attribute modifiers will be ignored
	 * 
	 * @param ignore
	 *            If true, all attribute modifiers will be ignored
	 * @return This
	 */
	protected final Component setIgnoreAttributeModifier(final boolean ignore)
	{
		this.setFlag(FLAG_IGNORE_ATTRIBUTE_MODIFIER, ignore);
		return this;
	}

	/**
	 * The markup stream will be assigned to the component at the beginning of the component render
	 * phase. It is temporary working variable only.
	 * 
	 * @see #findMarkupStream()
	 * @see MarkupContainer#getMarkupStream()
	 * 
	 * @param markupStream
	 *            The current markup stream which should be applied by the component to render
	 *            itself
	 */
	protected void setMarkupStream(final MarkupStream markupStream)
	{
	}

	/**
	 * @param model
	 *            The model to wrap if need be
	 * @return The wrapped model
	 */
	protected final IModel wrap(final IModel model)
	{
		if (model instanceof IComponentAssignedModel)
		{
			return ((IComponentAssignedModel)model).wrapOnAssignment(this);
		}
		else
		{
			return model;
		}
	}

	/**
	 * Detaches any child components
	 */
	void detachChildren()
	{
	}

	/**
	 * Gets the component at the given path.
	 * 
	 * @param path
	 *            Path to component
	 * @return The component at the path
	 */
	Component get(final String path)
	{
		// Path to this component is an empty path
		if (path.equals(""))
		{
			return this;
		}
		throw new IllegalArgumentException(
				exceptionMessage("Component is not a container and so does " +
						"not contain the path " + path));
	}

	/**
	 * Checks whether or not this component has a markup id value generated, whether it is automatic
	 * or user defined
	 * 
	 * @return true if this component has a markup id value generated
	 */
	final boolean hasMarkupIdMetaData()
	{
		return getMetaData(MARKUP_ID_KEY) != null;
	}

	/**
	 * Attaches any child components
	 * 
	 * This method is here only for {@link MarkupContainer}. It is broken out of
	 * {@link #onBeforeRender()} so we can guarantee that it executes as the last in onAttach()
	 * chain no matter where user places the <code>super.onAttach()</code> call
	 */
	void internalAttach2()
	{
		if (!getFlag(FLAG_ATTACHED))
		{
			setFlag(FLAG_ATTACHING, true);
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
			setFlag(FLAG_ATTACHING, false);
			setFlag(FLAG_ATTACHED, true);
		}
	}

	void internalMarkRendering()
	{
		setFlag(FLAG_PREPARED_FOR_RENDER, false);
		setFlag(FLAG_RENDERING, true);
	}

	/**
	 * @return True if this component or any of its parents is in auto-add mode
	 */
	final boolean isAuto()
	{
		// Search up hierarchy for FLAG_AUTO
		for (Component current = this; current != null; current = current.getParent())
		{
			if (current.getFlag(FLAG_AUTO))
			{
				return true;
			}
		}
		return false;
	}

	boolean isPreparedForRender()
	{
		return getFlag(FLAG_PREPARED_FOR_RENDER);
	}

	void onAfterRenderChildren()
	{
	}

	/**
	 * This method is here for {@link MarkupContainer}. It is broken out of
	 * {@link #onBeforeRender()} so we can guarantee that it executes as the last in
	 * onBeforeRender() chain no matter where user places the <code>super.onBeforeRender()</code>
	 * call.
	 */
	void onBeforeRenderChildren()
	{
	}

	/**
	 * Renders the close tag at the current position in the markup stream.
	 * 
	 * @param markupStream
	 *            the markup stream
	 * @param openTag
	 *            the tag to render
	 * @param renderTagOnly
	 *            if true, the tag will not be written to the output
	 */
	final void renderClosingComponentTag(final MarkupStream markupStream,
			final ComponentTag openTag, final boolean renderTagOnly)
	{
		// Tag should be open tag and not openclose tag
		if (openTag.isOpen())
		{
			// If we found a close tag and it closes the open tag, we're good
			if (markupStream.atCloseTag() && markupStream.getTag().closes(openTag))
			{
				// Get the close tag from the stream
				ComponentTag closeTag = markupStream.getTag();

				// If the open tag had its id changed
				if (openTag.getNameChanged())
				{
					// change the id of the close tag
					closeTag = closeTag.mutable();
					closeTag.setName(openTag.getName());
				}

				// Render the close tag
				if (renderTagOnly == false)
				{
					renderComponentTag(closeTag);
				}
				markupStream.next();
			}
			else
			{
				if (openTag.requiresCloseTag())
				{
					// Missing close tag
					markupStream.throwMarkupException("Expected close tag for " + openTag);
				}
			}
		}
	}

	/**
	 * Sets the id of this component. This method is private because the only time a component's id
	 * can be set is in its constructor.
	 * 
	 * @param id
	 *            The non-null id of this component
	 */
	final void setId(final String id)
	{
		if (id == null && !(this instanceof Page))
		{
			throw new WicketRuntimeException("Null component id is not allowed.");
		}
		this.id = id;
	}

	/**
	 * Sets the parent of a component.
	 * 
	 * @param parent
	 *            The parent container
	 */
	final void setParent(final MarkupContainer parent)
	{
		if (this.parent != null && log.isDebugEnabled())
		{
			log.debug("Replacing parent " + this.parent + " with " + parent);
		}
		this.parent = parent;
	}

	/**
	 * Sets the render allowed flag.
	 * 
	 * @param renderAllowed
	 */
	final void setRenderAllowed(boolean renderAllowed)
	{
		setFlag(FLAG_IS_RENDER_ALLOWED, renderAllowed);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1373.java