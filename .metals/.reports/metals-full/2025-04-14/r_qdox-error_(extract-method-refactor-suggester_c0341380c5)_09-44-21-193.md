error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17629.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17629.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[74,27]

error in qdox parser
file content:
```java
offset: 2070
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17629.java
text:
```scala
{ // TODO finalize javadoc

package wicket.markup.html.form.validation;

import java.io.Serializable;

import wicket.Component;
import wicket.model.IModel;
import wicket.model.Model;


/**
 * Decorates a model, using the given input (that was the cause of a validation
 * error) as the model object instead of the 'real' model object.
 * NOT INTENDED TO BE USED BY FRAMEWORK CLIENTS.
 * <p>
 * Method getObject() will return the input, setObject(Object) will do nothing at all.
 * </p>
 * IMPORTANT NOTE: AFTER THE PAGE RENDERING IS DONE, THE ORIGINAL MODEL MUST BE SET BACK
 * ON THE COMPONENT. THIS DECORATOR IS HERE FOR THE SOLE PURPOSE OF BEING ABLE TO
 * RENDER THE INPUT ON THE COMPONENT, AND TO AVOID POSSIBLE EXCEPTIONS THAT CAN
 * OCCUR WHEN INVALID VALUES ARE SET INTO THE MODEL.
 * </p>
 */
public final class ValidationErrorModelDecorator extends Model
{
    /** the decorated model. */
    private IModel originalModel;

    /** the reporting component. */
    private Component reporter;

    /**
     * Construct.
     * @param reporter the reporting component
     * @param input the input that caused to validation error
     */
    public ValidationErrorModelDecorator(
            Component reporter, Serializable input)
    {
        super(input);
        this.originalModel = reporter.getModel();
        this.reporter = reporter;
    }

    /**
     * Will do nothing.
     * @see wicket.model.IModel#setObject(java.lang.Object)
     */
    public void setObject(Object object)
    {
        // ignore. This method *will* be called by the framework, but as we
        // just want to return the input that caused the validation error,
        // it's best to ignore it alltogether
    }
    
    /**
     * Gets the decorated model.
     * @return the decorated model.
     */
    public IModel getOriginalModel()
    {
        return originalModel;
    }
    /**
     * Gets the reporting component.
     * @return the reporting component
     */
    public Component getReporter()
    {
        return reporter;
    }
}
 No newline at end of file@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17629.java