error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10474.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10474.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10474.java
text:
```scala
r@@eturn new ValidationErrorMessage(component, message);

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
package wicket.markup.html.form.validation;

import java.util.HashMap;
import java.util.Map;

import wicket.Component;
import wicket.Localizer;
import wicket.RenderException;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.FormComponent;
import wicket.model.IModel;
import wicket.model.MapModel;
import wicket.util.lang.Classes;

/**
 * Base class for form component validators.
 *
 * @author Jonathan Locke
 * @author Eelco Hillenius
 */
public abstract class AbstractValidator implements IValidator
{
    /**
     * Returns a formatted validation error message for a given component. The error
     * message is retrieved from a message bundle associated with the page in which this
     * validator is contained. The resource key must be of the form:
     * [form-name].[component-name].[validator-class]. For example, in the SignIn page's
     * SignIn.properties file, you might find an entry:
     * signInForm.password.RequiredValidator=A password is required Entries can contain
     * optional ognl variable interpolations from the component, such as:
     * editBook.name.LengthValidator='${input}' is too short a name.
     * <p>
     * Available variables for interpolation are:
     * <ul>
     *   <li>
     * 		${input}: the user's input
     *   </li>
     *   <li>
     *   	${name}: the name of the component
     *   </li>
     * </ul>
     * </p>
     * @param input the input (that caused the error)
     * @param component The component where the error occurred
     * @return The validation error message
     */
    public final ValidationErrorMessage errorMessage(
            final String input, final FormComponent component)
    {
    	return errorMessage(getResourceKey(component), input, component);
    }


    /**
     * Returns a formatted validation error message for a given component. The error
     * message is retrieved from a message bundle associated with the page in which this
     * validator is contained using the given resource key. The resourceModel is used
     * for variable interpolation.
     * @param resourceKey the resource key to be used for the message
     * @param resourceModel the model for variable interpolation
     * @param input the input (that caused the error)
     * @param component The component where the error occurred
     * @return The validation error message
     */
    public final ValidationErrorMessage errorMessage(
    		final String resourceKey, final IModel resourceModel,
            final String input, final FormComponent component)
    {
        // Return formatted error message
        Localizer localizer = component.getLocalizer();
		String message = localizer.getString(resourceKey, component, resourceModel);
        return new ValidationErrorMessage(input, component, message);
    }

    /**
     * Returns a formatted validation error message for a given component. The error
     * message is retrieved from a message bundle associated with the page in which this
     * validator is contained using the given resource key. The resourceModel is used
     * for variable interpolation.
     * @param resourceKey the resource key to be used for the message
     * @param map the model for variable interpolation
     * @param input the input (that caused the error)
     * @param component The component where the error occurred
     * @return The validation error message
     */
    public final ValidationErrorMessage errorMessage(
    		final String resourceKey, final Map map,
            final String input, final FormComponent component)
    {
    	return errorMessage(resourceKey, MapModel.valueOf(map), input, component);
    }

    /**
     * Returns a formatted validation error message for a given component. The error
     * message is retrieved from a message bundle associated with the page in which this
     * validator is contained using the given resource key.
     * <p>
     * The available variables for interpolation are by default:
     * <ul>
     *   <li>
     * 		${input}: the user's input
     *   </li>
     *   <li>
     *   	${name}: the name of the component
     *   </li>
     * </ul>
     * Optionally, you can either override getMessageContextVariables, or provide
     * a model or a map with those variables yourself by using one of the other errorMessage
     * methods.
     * </p>
     * @param resourceKey the resource key to be used for the message
     * @param input the input (that caused the error)
     * @param component The component where the error occurred
     * @return The validation error message
     */
    public final ValidationErrorMessage errorMessage(
    		final String resourceKey,
            final String input, final FormComponent component)
    {
        Map resourceModel = getMessageContextVariables(input, component);
		return errorMessage(resourceKey, resourceModel, input, component);
    }

	/**
	 * Gets the default variables for interpolation. These are:
     * <ul>
     *   <li>
     * 		${input}: the user's input
     *   </li>
     *   <li>
     *   	${name}: the name of the component
     *   </li>
     * </ul>
	 * @param input the user's input
	 * @param component the component
	 * @return a map with the variables for interpolation
	 */
	protected Map getMessageContextVariables(
			final String input, final FormComponent component)
	{
		Map resourceModel = new HashMap(2);
		resourceModel.put("input", input);
		resourceModel.put("name", component.getName());
		return resourceModel;
	}

    /**
     * Gets the resource key based on the form component. It will have the form:
     * <code>[form-name].[component-name].[validator-class]</code>
     * @param component the form component
     * @return the resource key based on the form component
     */
    protected final String getResourceKey(final FormComponent component)
    {
        // Resource key must be <form-name>.<component-name>.<validator-class>
        final Component parentForm = component.findParent(Form.class);
        if (parentForm != null)
        {
            return parentForm.getName() + "." + component.getName()
            	+ "." + Classes.name(getClass());
        }
        else
        {
            throw new RenderException(
                    "Unable to find Form parent for FormComponent " + component);
        }
    }
}

///////////////////////////////// End of File /////////////////////////////////
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10474.java