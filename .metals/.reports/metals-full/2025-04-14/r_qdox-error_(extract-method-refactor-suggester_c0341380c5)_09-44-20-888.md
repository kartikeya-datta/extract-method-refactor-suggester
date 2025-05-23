error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17163.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17163.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17163.java
text:
```scala
g@@etApplicationPages().getHomePage()));

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
package wicket.markup.html.panel;

import wicket.Page;
import wicket.RequestCycle;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.PasswordTextField;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.validation.IValidationErrorHandler;
import wicket.markup.html.form.validation.ValidationErrorMessage;
import wicket.util.value.ValueMap;


/**
 * Log-in panel with username and password.
 *
 * @author Jonathan Locke
 * @author Juergen Donnerstag
 * @author Eelco Hillenius
 */
public abstract class SignInPanel extends Panel
{ // TODO finalize javadoc
    /** field for user name. */
    private TextField username;

    /** field for password. */
    private PasswordTextField password;
    
    /**
     * Constructor.
     * @param componentName name of the component
     */
    public SignInPanel(String componentName)
    {
        super(componentName);

        // Create feedback panel and add to page
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        add(feedback);

        // Add sign-in form to page, passing feedback panel as 
        // validation error handler
        add(new SignInForm("signInForm", feedback));
    }
    
    /**
     * Convenience method to access the username.
     * @return the user name
     */
    public String getUsername() 
    {
    	return username.getModelObjectAsString();
    }

    /**
     * Convenience method to access the password. 
     * @return the password
     */
    public String getPassword() 
    {
    	return password.getModelObjectAsString();
    }
    
    /**
     * Convenience method set persistence for username and password.
     * @param enable whether the fields should be persistent
     */
    public void setPersistent(boolean enable)
    {
    	username.setPersistenceEnabled(enable);
    	password.setPersistenceEnabled(enable);
    }

    /**
     * Sign in user if possible.
     * @param username The username
     * @param password The password
     * @return Error message to display, or null if the user was signed in
     */
    public abstract String signIn(final String username, final String password);

    /**
     * Sign in form.
     */
    public final class SignInForm extends Form
    {
		/** Serial Version ID. */
		private static final long serialVersionUID = 303695648327317416L;
		
		/** El-cheapo model for form. */
        private final ValueMap properties = new ValueMap();

        /**
         * Constructor.
         * @param componentName Name of the form component
         * @param feedback The feedback panel to update
         */
        public SignInForm(final String componentName, final IValidationErrorHandler feedback)
        {
            super(componentName, feedback);

            // Attach textfield components that edit properties map
            // in lieu of a formal beans model
            add(username = new TextField("username", properties, "username"));
            add(password = new PasswordTextField("password", properties, "password"));
        }

        /**
         * @see wicket.markup.html.form.Form#handleSubmit()
         */
        public final void handleSubmit()
        {
            // Sign the user in
            final String error = signIn(getUsername(), getPassword());

            if (error == null)
            {
                // Get active request cycle
                final RequestCycle cycle = getRequestCycle();
                
            	// If login has been called because the user was not yet
            	// logged in, than continue to the original destination.
            	// Else to the Home page
                if (cycle.continueToOriginalDestination())
                {
                	// HTTP redirect response has been committed. No more data 
                	// shall be written to the response.
                	cycle.setPage((Page)null);
                } 
                else 
                {
                    cycle.setPage(
                            getApplicationSettings().getDefaultPageFactory().newPage(
                                    getApplicationSettings().getHomePage()));
                }
            }
            else
            {
                handleError(new ValidationErrorMessage(this, error));
            }
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17163.java