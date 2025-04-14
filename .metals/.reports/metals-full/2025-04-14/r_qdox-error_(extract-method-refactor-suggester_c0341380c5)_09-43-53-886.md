error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7857.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7857.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7857.java
text:
```scala
.@@getDefaultPageFactory().classForName(classname);

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.markup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Component;
import wicket.Container;
import wicket.RenderException;
import wicket.RequestCycle;
import wicket.util.string.StringValueConversionException;

/**
 * 
 * @author Juergen Donnerstag
 */
public class WicketTagComponentResolver implements IComponentResolver
{ // TODO finalize javadoc
    /** Logging */
    private static Log log = LogFactory.getLog(WicketTagComponentResolver.class);

    /** Used to create anonymous component names */
    public static int autoIndex = 0;

    /**
     * @see wicket.markup.IComponentResolver#resolve(Container, MarkupStream, ComponentTag)
     * @param container The container parsing its markup
     * @param markupStream The current markupStream
     * @param tag The current component tag while parsing the markup
     * @return true, if componentName was handle by the resolver. False, otherwise  
     */
	public boolean resolve(final Container container, final MarkupStream markupStream,
			final ComponentTag tag)
	{
	    if (tag instanceof ComponentWicketTag)
	    {
	        final ComponentWicketTag wicketTag = (ComponentWicketTag)tag;
	        if (wicketTag.isComponentTag())
	        {
	            final Component component = createComponent(wicketTag);
	            if (component != null)
	            {
	                container.add(component);
	                component.render();
	                return true;
	            }
	        }
	    }
	    
        // We were not able to handle the componentName
        return false;
	}
    
    private Component createComponent(final ComponentWicketTag tag)
    {
        String componentName = tag.getNameAttribute();
        if (componentName == null)
        {
            componentName = "anonymous-" + autoIndex;
            autoIndex ++;
        }
        
        final String classname = tag.getAttributes().getString("class");
        final Class clazz = RequestCycle.get().getApplication().getSettings()
        		.getPageFactory().getClassInstance(classname);
        
        final Component component;
        try
        {
            final Constructor constructor = clazz.getConstructor(new Class[] { String.class });
            component = (Component) constructor.newInstance(new Object[] { componentName });
        }
        catch (NoSuchMethodException ex)
        {
            throw new RenderException(
                    "Unable to create Component derived from wicket tag", ex);
        }
        catch (InvocationTargetException ex)
        {
            throw new RenderException(
                    "Unable to create Component derived from wicket tag", ex);
        }
        catch (IllegalAccessException ex)
        {
            throw new RenderException(
                    "Unable to create Component derived from wicket tag", ex);
        }
        catch (InstantiationException ex)
        {
            throw new RenderException(
                    "Unable to create Component derived from wicket tag", ex);
        }
        catch (ClassCastException ex)
        {
            throw new RenderException(
                    "Unable to create Component derived from wicket tag", ex);
        }
        
        final Iterator iter = tag.getAttributes().entrySet().iterator();
        while (iter.hasNext())
        {
            final Map.Entry entry = (Map.Entry)iter.next();
            final String key = (String) entry.getKey();
            final String value = (String) entry.getKey();
            
            if ("name".equalsIgnoreCase(key) || ("class".equalsIgnoreCase(key)))
            {
                continue;
            }
            
            final String methodName = "set" + key;
            final Method[] methods = component.getClass().getMethods();
            Method method = null;
            for (int i=0; i < methods.length; i++)
            {
                if (methods[i].getName().equalsIgnoreCase(methodName))
                {
                    method = methods[i];
                }
            }
            
            if (method == null)
            {
                throw new RenderException(
                        "Unable to initialize Component. Method with name " 
                        + methodName + " not found");
            }
            
            final Class[] parameterClasses = method.getParameterTypes();
            if (parameterClasses.length != 1)
            {
                throw new RenderException(
                        "Unable to initialize Component. Method with name " 
                        + methodName + " must have one and only one parameter");
            }
            
            final Class paramClass = parameterClasses[0];
            try
            {
	            if (paramClass.equals(String.class))
	            {
	                method.invoke(component, new Object[] { value } );
	            }
	            else if (paramClass.equals(int.class))
	            {
	                method.invoke(component, new Object[] { new Integer(tag.getAttributes().getInt(key))} );
	            }
	            else if (paramClass.equals(long.class))
	            {
	                method.invoke(component, new Object[] { new Long(tag.getAttributes().getLong((key)))} );
	            }
            }
            catch (IllegalAccessException ex)
            {
                throw new RenderException(
                        "Unable to initialize Component. Failure while invoking method " 
                        + methodName, ex);
            }
            catch (InvocationTargetException ex)
            {
                throw new RenderException(
                        "Unable to initialize Component. Failure while invoking method " 
                        + methodName, ex);
            }
            catch (StringValueConversionException ex)
            {
                throw new RenderException(
                        "Unable to initialize Component. Failure while invoking method " 
                        + methodName, ex);
            }
        }
        
        return component;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7857.java