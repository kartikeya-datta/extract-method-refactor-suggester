error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5197.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5197.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5197.java
text:
```scala
e@@rror.setVariables(vars);

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
package wicket.validation.validator;

import java.util.HashMap;
import java.util.Map;

import wicket.IClusterable;
import wicket.markup.html.form.FormComponent;
import wicket.util.lang.Classes;
import wicket.validation.IValidatable;
import wicket.validation.IValidator;
import wicket.validation.ValidationError;

/**
 * Convinience base class for {@link IValidator}s. This class is thread-safe
 * and therefore it is safe to share validators across sessions/threads.
 * <p>
 * Error messages can be registered by calling one of the error(IValidatable
 * ...) overloads.
 * <p>
 * By default this class will skip validation if the
 * {@link IValidatable#getValue()} returns null, validators that wish to
 * validate the null value need to override {@link #validateOnNullValue()} and
 * return <code>true</code>.
 * 
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 * @author Igor Vaynberg (ivaynbeg)
 * 
 */
public abstract class AbstractValidator implements IValidator, IClusterable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Whether or not to validate the value if it is null. It is usually
	 * desirable to skip validation if the value is null - unless we want to
	 * make sure the value is in fact null which is a rare usecase. Validators
	 * that extend this and wish to validate that the value is null should
	 * override this method and return <code>true</code>.
	 * 
	 * @return true to validate on null value, false to skip validation on null
	 *         value
	 */
	public boolean validateOnNullValue()
	{
		return false;
	}

	/**
	 * Method used to validate the validatable instance
	 * 
	 * @param validatable
	 */
	protected abstract void onValidate(IValidatable validatable);

	/**
	 * @see wicket.validation.IValidator#validate(wicket.validation.IValidatable)
	 */
	public final void validate(IValidatable validatable)
	{
		if (validatable.getValue() != null || validateOnNullValue())
		{
			onValidate(validatable);
		}
	}


	/**
	 * Reports an error against validatable using the map returned by
	 * {@link #variablesMap(IValidatable)}for variable interpolations and
	 * message key returned by {@link #resourceKey()}.
	 * 
	 * @param validatable
	 *            validatble being validated
	 * 
	 */
	public void error(final IValidatable validatable)
	{
		error(validatable, resourceKey(), variablesMap(validatable));
	}

	/**
	 * Reports an error against validatable using the map returned by
	 * {@link #variablesMap(IValidatable)}for variable interpolations and the
	 * specified resourceKey
	 * 
	 * @param validatable
	 *            validatble being validated
	 * @param resourceKey
	 *            the message resource key to use
	 * 
	 */
	public void error(final IValidatable validatable, String resourceKey)
	{
		if (resourceKey == null)
		{
			throw new IllegalArgumentException("Argument [[resourceKey]] cannot be null");
		}
		error(validatable, resourceKey, variablesMap(validatable));
	}

	/**
	 * Reports an error against the validatalbe using the given map for variable
	 * interpolations and message resource key provided by
	 * {@link #resourceKey()}
	 * 
	 * @param validatable
	 *            validatble being validated
	 * @param vars
	 *            variables for variable interpolation
	 */
	public void error(final IValidatable validatable, final Map vars)
	{
		if (vars == null)
		{
			throw new IllegalArgumentException("Argument [[vars]] cannot be null");
		}
		error(validatable, resourceKey(), vars);
	}

	/**
	 * Reports an error against the validatable using the specified resource key
	 * and variable map
	 * 
	 * @param validatable
	 *            validatble being validated
	 * @param resourceKey
	 *            The message resource key to use
	 * @param vars
	 *            The model for variable interpolation
	 */
	public void error(final IValidatable validatable, final String resourceKey,
			Map vars)
	{
		if (validatable == null)
		{
			throw new IllegalArgumentException("Argument [[validatable]] cannot be null");
		}
		if (vars == null)
		{
			throw new IllegalArgumentException("Argument [[vars]] cannot be null");
		}
		if (resourceKey == null)
		{
			throw new IllegalArgumentException("Argument [[resourceKey]] cannot be null");
		}


		ValidationError error = new ValidationError().addMessageKey(resourceKey);
		final String defaultKey = Classes.simpleName(getClass());
		if (!resourceKey.equals(defaultKey))
		{
			error.addMessageKey(defaultKey);
		}

		error.setVars(vars);
		validatable.error(error);
	}

	/**
	 * Gets the resource key for validator's error message from the
	 * ApplicationSettings class.
	 * 
	 * <strong>NOTE</strong>: THIS METHOD SHOULD NEVER RETURN NULL
	 * 
	 * @return the resource key for the validator
	 */
	protected String resourceKey()
	{
		return Classes.simpleName(getClass());
	}

	/**
	 * Gets the default variable map
	 * 
	 * <strong>NOTE</strong>: THIS METHOD SHOULD NEVER RETURN NULL
	 * 
	 * @param validatable
	 *            validatable being validated
	 * 
	 * @return a map with the variables for interpolation
	 */
	protected Map variablesMap(IValidatable validatable)
	{
		final Map resourceModel = new HashMap(1);
		return resourceModel;
	}

	// deprecated methods


	/**
	 * DEPRECATED/UNSUPPORTED
	 * 
	 * Gets the default variables for interpolation. These are:
	 * <ul>
	 * <li>${input}: the user's input</li>
	 * <li>${name}: the name of the component</li>
	 * <li>${label}: the label of the component - either comes from
	 * FormComponent.labelModel or resource key [form-id].[form-component-id] in
	 * that order</li>
	 * </ul>
	 * 
	 * @param formComponent
	 *            form component
	 * @return a map with the variables for interpolation
	 * 
	 * @deprecated use {@link #variablesMap(IValidatable)} instead
	 * @throws UnsupportedOperationException
	 * 
	 * 
	 * FIXME 2.0: remove asap
	 */
	protected final Map messageModel(final FormComponent formComponent)
	{
		throw new UnsupportedOperationException("THIS METHOD IS DEPRECATED, SEE JAVADOC");
	}

	/**
	 * DEPRECATED/UNSUPPORTED
	 * 
	 * Gets the resource key for validator's error message from the
	 * ApplicationSettings class.
	 * 
	 * @param formComponent
	 *            form component that is being validated
	 * 
	 * @return the resource key based on the form component
	 * 
	 * @deprecated use {@link #resourceKey()} instead
	 * @throws UnsupportedOperationException
	 * 
	 * 
	 * FIXME 2.0: remove asap
	 * 
	 */
	protected final String resourceKey(final FormComponent formComponent)
	{
		throw new UnsupportedOperationException("THIS METHOD IS DEPRECATED, SEE JAVADOC");
	}

	/**
	 * DEPRECATED/UNSUPPORTED
	 * 
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT IMPLEMENT IT.
	 * <p>
	 * Instead of subclassing IValidator, you should use one of the existing
	 * validators, which cover a huge number of cases, or if none satisfies your
	 * need, subclass CustomValidator.
	 * <p>
	 * Validates the given input. The input corresponds to the input from the
	 * request for a component.
	 * 
	 * @param component
	 *            Component to validate
	 * 
	 * @deprecated use {@link #variablesMap(IValidatable)} instead
	 * @throws UnsupportedOperationException
	 * 
	 * 
	 * FIXME 2.0: remove asap
	 */
	public final void validate(final FormComponent component)
	{
		throw new UnsupportedOperationException("THIS METHOD IS DEPRECATED, SEE JAVADOC");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5197.java