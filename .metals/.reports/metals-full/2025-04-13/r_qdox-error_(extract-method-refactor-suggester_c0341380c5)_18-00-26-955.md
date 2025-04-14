error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9242.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9242.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9242.java
text:
```scala
final P@@opupSettings popupSettings = getPopupSettings();

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
package wicket.markup.html.link;

import java.io.Serializable;

import wicket.markup.ComponentTag;
import wicket.model.IModel;

/**
 * Implementation of a special link component that can handle linkClicked events (implements
 * {@link wicket.markup.html.link.ILinkListener}) but that can be used with any kind of tag.
 * At render time, an onclick event handler is added to the tag (or the existing one
 * is replaced) like: <pre>onclick="location.href='/myapp/...'"</pre>.
 * <p>
 * The OnClickLink can be placed on any tag that has a onclick handler
 * (e.g. buttons, tr/ td's etc).
 * </p>
 * <p>
 * You can use a onClickLink like:
 * <pre>
 * add(new OnClickLink("myLink"){
 *
 *   public void linkClicked(RequestCycle cycle)
 *   {
 *      // do something here...  
 *   }
 * );
 * </pre>
 * and in your HTML file:
 * <pre>
 *  &lt;input type="button" id="wicket-remove" value="push me" /&gt;
 * </pre>
 * or (with a onclick handler that will be replaced but can be usefull when designing):
 * <pre>
 *  &lt;input type="button" onclick="alert('test');" id="wicket-remove" value="push me" /&gt;
 * </pre>
 * </p>
 *
 * @author Eelco Hillenius
 */
public abstract class OnClickLink extends AbstractLink
{
    /**
     * Construct.
     * @param componentName the name of the component
     */
    public OnClickLink(String componentName)
    {
        super(componentName);
    }

    /**
     * Constructor that uses the provided {@link IModel} as its model.
     * @param name The non-null name of this component
     * @param model the model
     * @throws wicket.WicketRuntimeException Thrown if the component has been given a null name.
     */
    public OnClickLink(String name, IModel model)
    {
        super(name, model);
    }

    /**
     * Constructor that uses the provided instance of {@link IModel} as a dynamic model.
     * This model will be wrapped in an instance of {@link wicket.model.PropertyModel}
     * using the provided expression.
     *
     * @param name The non-null name of this component
     * @param model the instance of {@link IModel} from which the model object will be
     *            used as the subject for the given expression
     * @param expression the OGNL expression that works on the given object
     * @throws wicket.WicketRuntimeException Thrown if the component has been given a null name.
     */
    public OnClickLink(String name, IModel model, String expression)
    {
        super(name, model, expression);
    }

    /**
     * Constructor that uses the provided object as a simple model. This object will be
     * wrapped in an instance of {@link wicket.model.Model}.
     * @param name The non-null name of this component
     * @param object the object that will be used as a simple model
     * @throws wicket.WicketRuntimeException Thrown if the component has been given a null name.
     */
    public OnClickLink(String name, Serializable object)
    {
        super(name, object);
    }

    /**
     * Constructor that uses the provided object as a dynamic model. This object will be
     * wrapped in an instance of {@link wicket.model.Model} that will be wrapped in an instance of
     * {@link wicket.model.PropertyModel} using the provided expression.
     * @param name The non-null name of this component
     * @param object the object that will be used as the subject for the given expression
     * @param expression the OGNL expression that works on the given object
     * @throws wicket.WicketRuntimeException Thrown if the component has been given a null name.
     */
    public OnClickLink(String name, Serializable object, String expression)
    {
        super(name, object, expression);
    }

    /**
	 * Processes the component tag.
	 * @param tag Tag to modify
     * @see wicket.Component#handleComponentTag(ComponentTag)
     */
    protected final void handleComponentTag(final ComponentTag tag)
    {
        // Add simple javascript on click handler that links to this
        // link's linkClicked method
        final String url = getURL().replaceAll("&", "&amp;");
        PopupSettings popupSettings = getPopupSettings();
        if (popupSettings != null)
        {
        	popupSettings.setTarget("'" + url + "'");
            String popupScript = popupSettings.getPopupJavaScript();
            popupScript = popupScript.replaceAll("&", "&amp;");
            tag.put("onclick", popupScript);
        }
        else
        {
        	tag.put("onclick", "location.href='" + url + "';");
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9242.java