error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17613.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17613.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[221,80]

error in qdox parser
file content:
```java
offset: 7756
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17613.java
text:
```scala
{ // TODO finalize javadoc

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
package wicket.markup.html.form;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.RenderException;
import wicket.RequestCycle;
import wicket.markup.ComponentTag;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.model.PropertyModel;


/**
 * A password text field component. As you type, characters show up as asterisks or some
 * other such character so that nobody can look over your shoulder and read your password.
 * @author Jonathan Locke
 */
public final class PasswordTextField extends FormComponent implements FormComponent.ICookieValue
{
    // Code broadcaster for reporting
    private static final Log log = LogFactory.getLog(PasswordTextField.class);

    /** Serial Version ID */
	private static final long serialVersionUID = 1776665507834380353L;


	/**
	 * Flag indicating whether the contents of the field should be reset each time it is rendered.
	 * If <code>true</code>, the contents are emptied when the field is rendered. This is useful
	 * for login forms. If <code>false</code>, the contents of the model are put into the field.
	 * This is useful for entry forms where the contents of the model should be editable, or
	 * resubmitted.
	 */
	private boolean resetPassword = true;
	
	/**
     * Constructor that uses the provided {@link IModel}as its model. All components have
     * names. A component's name cannot be null.
     * @param name The non-null name of this component
     * @param model
     * @throws RenderException Thrown if the component has been given a null name.
     */
    public PasswordTextField(String name, IModel model)
    {
        super(name, model);
    }

    /**
     * Constructor that uses the provided instance of {@link IModel}as a dynamic model.
     * This model will be wrapped in an instance of {@link PropertyModel}using the
     * provided expression. Thus, using this constructor is a short-hand for:
     * 
     * <pre>
     * new MyComponent(name, new PropertyModel(myIModel, expression));
     * </pre>
     * 
     * All components have names. A component's name cannot be null.
     * @param name The non-null name of this component
     * @param model the instance of {@link IModel}from which the model object will be
     *            used as the subject for the given expression
     * @param expression the OGNL expression that works on the given object
     * @throws RenderException Thrown if the component has been given a null name.
     */
    public PasswordTextField(String name, IModel model, String expression)
    {
        super(name, model, expression);
    }

    /**
     * Constructor that uses the provided object as a simple model. This object will be
     * wrapped in an instance of {@link Model}. All components have names. A component's
     * name cannot be null.
     * @param name The non-null name of this component
     * @param object the object that will be used as a simple model
     * @throws RenderException Thrown if the component has been given a null name.
     */
    public PasswordTextField(String name, Serializable object)
    {
        super(name, object);
    }

    /**
     * Constructor that uses the provided object as a dynamic model. This object will be
     * wrapped in an instance of {@link Model}that will be wrapped in an instance of
     * {@link PropertyModel}using the provided expression. Thus, using this constructor
     * is a short-hand for:
     * 
     * <pre>
     * new MyComponent(name, new PropertyModel(new Model(object), expression));
     * </pre>
     * 
     * All components have names. A component's name cannot be null.
     * @param name The non-null name of this component
     * @param object the object that will be used as the subject for the given expression
     * @param expression the OGNL expression that works on the given object
     * @throws RenderException Thrown if the component has been given a null name.
     */
    public PasswordTextField(String name, Serializable object, String expression)
    {
        super(name, object, expression);
    }

    /**
     * @see wicket.Component#handleComponentTag(RequestCycle, ComponentTag)
     */
    protected void handleComponentTag(final RequestCycle cycle, final ComponentTag tag)
    {
        checkTag(tag, "input");
        checkAttribute(tag, "type", "password");
        super.handleComponentTag(cycle, tag);
		if (isResetPassword())
		{
			tag.put("value", "");
		}
		else
		{
			tag.put("value", getModelObjectAsString());
		}
    }

    /**
     * @param cycle The request cycle
     */
    public void updateModel(final RequestCycle cycle)
    {
        setModelObject(getRequestString(cycle));
    }

    /**
     * @see wicket.markup.html.form.FormComponent.ICookieValue#getCookieValue()
     */
    public String getCookieValue()
    {
        final String value = getModelObjectAsString();
        try
        {
            return getPage().getApplicationSettings().getCryptInstance().encryptStringToString(value);
        }
        catch (Exception ex) 
        {
            log.error("Failed to instantiate encryption object. Continue without encryption");
        }
        
        return value;
    }

    /**
     * @see wicket.markup.html.form.FormComponent.ICookieValue#setCookieValue(java.lang.String)
     */
    public void setCookieValue(String value)
    {
        String decryptedValue;
        try
        {
            decryptedValue = getPage().getApplicationSettings().getCryptInstance().decryptStringToString(value);
        } 
        catch (Exception ex) 
        {
            decryptedValue = value;
            log.error("Failed to instantiate encryption object. Continue without encryption");
        }
        
        setModelObject(decryptedValue);
    }

    /**
	 * Flag indicating whether the contents of the field should be reset each time it is rendered.
	 * If <code>true</code>, the contents are emptied when the field is rendered. This is useful
	 * for login forms. If <code>false</code>, the contents of the model are put into the field.
	 * This is useful for entry forms where the contents of the model should be editable, or
	 * resubmitted.
	 * 
	 * @return Returns the resetPassword.
	 */
	public final boolean isResetPassword()
	{
		return resetPassword;
	}

	/**
	 * Flag indicating whether the contents of the field should be reset each time it is rendered.
	 * If <code>true</code>, the contents are emptied when the field is rendered. This is useful
	 * for login forms. If <code>false</code>, the contents of the model are put into the field.
	 * This is useful for entry forms where the contents of the model should be editable, or
	 * resubmitted.
	 * 
	 * @param resetPassword
	 *            The resetPassword to set.
	 * @return <code>this</code>.
	 */
	public final PasswordTextField setResetPassword(final boolean resetPassword)
	{
		this.resetPassword = resetPassword;
		return this;
	}
}

///////////////////////////////// End of File /////////////////////////////////@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17613.java