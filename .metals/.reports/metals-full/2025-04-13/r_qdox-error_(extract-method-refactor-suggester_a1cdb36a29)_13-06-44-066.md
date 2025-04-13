error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2294.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2294.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2294.java
text:
```scala
private final M@@ap<Component,Object> nestedComponents = new HashMap<Component,Object>();

/*
 * $Id: AutoComponentResolver.java,v 1.4 2005/01/18 08:04:29 jonathanlocke
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
package wicket.markup.resolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.WicketRuntimeException;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupException;
import wicket.markup.MarkupStream;
import wicket.markup.WicketTag;
import wicket.markup.parser.filter.WicketTagIdentifier;
import wicket.util.lang.Classes;

/**
 * &lt;wicket:component class="myApp.MyTable" key=value&gt; tags may be used to add 
 * Wicket components (e.g. a specialized PageableListView) and pass parameters (e.g. the number
 * of rows per list view page). The object is automatically instantiated, initialized
 * and added to the page's component hierarchy.
 * <p>
 * Note: The component must have a constructor with a single String parameter: 
 * the component name.
 * <p>
 * Note: The component must provide a setter for each key/value attribute provided.
 * 
 * @author Juergen Donnerstag
 */
public final class AutoComponentResolver implements IComponentResolver
{
	private static final long serialVersionUID = 1L;

	static
	{
		// register "wicket:fragement"
		WicketTagIdentifier.registerWellKnownTagName("component");
	}

    /** 
     * Temporary storage for containers currently being rendered. Thus child
     * components can be re-parented. Remember: <wicket:component> are an 
     * exception to the rule. Though the markup of the children are nested
     * inside <wicket:component>, their respective Java components are not.
     * They must be added to the parent container of <wicket:component>.
     */ 
    private final Map nestedComponents = new HashMap();
    
    /**
     * @see wicket.markup.resolver.IComponentResolver#resolve(MarkupContainer, MarkupStream,
     *      ComponentTag)
     * @param container
     *            The container parsing its markup
     * @param markupStream
     *            The current markupStream
     * @param tag
     *            The current component tag while parsing the markup
     * @return true, if componentId was handle by the resolver. False,
     *         otherwise
     */
    public final boolean resolve(final MarkupContainer container, final MarkupStream markupStream,
            final ComponentTag tag)
    {
        // It must be <wicket:...>
        if (tag instanceof WicketTag)
        {
            // It must be <wicket:component...>
            final WicketTag wicketTag = (WicketTag)tag;
            if (wicketTag.isComponentTag())
            {
                // Create and initialize the component
                final Component component = createComponent(container, wicketTag);
                if (component != null)
                {
                    // 1. push the current component onto the stack
                    nestedComponents.put(component, null);
                    
                    try
                    {
	                    // 2. Add it to the hierarchy and render it
	                    container.autoAdd(component);
                    }
                    finally
                    {
                        // 3. remove it from the stack
                        nestedComponents.remove(component);
                    }
                    
                    return true;
                }
            }
        }
        
        // Re-parent children of <wicket:component>. 
        if ((tag.getId() != null) && nestedComponents.containsKey(container))
        {
            MarkupContainer parent = container.getParent();
            
            // Take care of nested <wicket:component>
            while ((parent != null) && nestedComponents.containsKey(parent))
            {
                parent = parent.getParent();
            }
            
            if (parent != null)
            {
                final Component component = parent.get(tag.getId());
                if (component != null)
                {
                    component.render(markupStream);
                    return true;
                }
            }
        }

        // We were not able to handle the componentId
        return false;
    }

    /**
     * Based on the tag, create and initalize the component.
     *  
     * @param container The current container. The new compent will be added to that container.
     * @param tag The tag containing the information about component 
     * @return The new component
     * @throws WicketRuntimeException in case the component could not be created
     */
    // Wicket is current not using any bean util jar, which is why ...
    private final Component createComponent(final MarkupContainer container, final WicketTag tag)
    {
        // If no component name is given, create a page-unique one yourself.
        String componentId = tag.getNameAttribute();
        if (componentId == null)
        {
            componentId = "anonymous-" + container.getPage().getAutoIndex();
        }

        // Get the component class name
        final String classname = tag.getAttributes().getString("class");
        if ((classname == null) || (classname.trim().length() == 0))
        {
            throw new MarkupException("Tag <wicket:component> must have attribute 'class'");
        }

        // Load the class. In case a Groovy Class Resolver has been provided,
        // the name might be a Groovy file.
        // Note: Spring based components are not supported this way. May be we
        //  should provide a ComponentFactory like we provide a PageFactory.
        final Class componentClass = container.getSession().getClassResolver().resolveClass(classname);

        // construct the component. It must have a constructor with a single
        // String (componentId) parameter.
        final Component component;
        try
        {
            final Constructor constructor = componentClass
                    .getConstructor(new Class[] { String.class });
            component = (Component)constructor.newInstance(new Object[] { componentId });
        }
        catch (NoSuchMethodException e)
        {
            throw new MarkupException(
                    "Unable to create Component from wicket tag: Cause: " 
                    + e.getMessage());
        }
        catch (InvocationTargetException e)
        {
            throw new MarkupException(
                    "Unable to create Component from wicket tag: Cause: " 
                    + e.getMessage());
        }
        catch (IllegalAccessException e)
        {
            throw new MarkupException(
                    "Unable to create Component from wicket tag: Cause: " 
                    + e.getMessage());
        }
        catch (InstantiationException e)
        {
            throw new MarkupException(
                    "Unable to create Component from wicket tag: Cause: " 
                    + e.getMessage());
        }
        catch (ClassCastException e)
        {
            throw new MarkupException(
                    "Unable to create Component from wicket tag: Cause: " 
                    + e.getMessage());
        }
        catch (SecurityException e)
        {
            throw new MarkupException(
                    "Unable to create Component from wicket tag: Cause: " 
                    + e.getMessage());
        }

        // Get all remaining attributes and invoke the component's setters
        Iterator iter = tag.getAttributes().entrySet().iterator();
        while (iter.hasNext())
        {
            final Map.Entry entry = (Map.Entry)iter.next();
            final String key = (String)entry.getKey();
            final String value = (String)entry.getValue();

            // Ignore attributes 'name' and 'class'
            if ("name".equalsIgnoreCase(key) || ("class".equalsIgnoreCase(key)))
            {
                continue;
            }

           	Classes.invokeSetter(component, key, value, container.getLocale());
        }

        return component;
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2294.java