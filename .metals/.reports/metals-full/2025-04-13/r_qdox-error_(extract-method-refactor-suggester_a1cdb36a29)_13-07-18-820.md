error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5793.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5793.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5793.java
text:
```scala
s@@uper.checkConfigurationInternal (issues);

/*
Copyright (c) 2008 Arno Haase, André Arnold.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

Contributors:
    Arno Haase - initial API and implementation
    André Arnold
 */
package org.eclipse.xtend.middleend.xtend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.WorkflowInterruptedException;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.xtend.expression.AbstractExpressionsUsingWorkflowComponent;


//TODO test this

/**
 * 
 * @author Arno Haase (http://www.haase-consulting.com)
 * @author André Arnold
 */
public class CheckComponent extends AbstractExpressionsUsingWorkflowComponent {

    private String _expression = null;
    private List<String> _checkFiles = new ArrayList<String>();
    private boolean _abortOnError = true;
    private boolean _warnIfNothingChecked = false;
    private String _emfAllChildrenSlot;
    private String _fileEncoding = null;

    public void setAbortOnError (boolean abortOnError) {
        _abortOnError = abortOnError;
    }

    public void addCheckFile (String checkFile) {
        _checkFiles.add(checkFile);
    }

    public void setExpression (String expression) {
        _expression = expression;
    }

    public void setWarnIfNothingChecked (boolean b) {
        _warnIfNothingChecked = b;
    }

    public void setEmfAllChildrenSlot (String childExpression) {
        _emfAllChildrenSlot = childExpression;
    }

    public void setFileEncoding (String fileEncoding) {
        _fileEncoding = fileEncoding;
    }

    @Override
    public String getLogMessage() {
    	final StringBuilder result = new StringBuilder ();
    	if ( _emfAllChildrenSlot != null ) 
    		result.append ("slot " + _emfAllChildrenSlot + " ");
    	else 
    		result.append ("expression " + _expression + " ");
    	
    	result.append ("check file(s): ");
    	for (String f: _checkFiles) 
    		result.append (f + " ");
		
    	return result.toString();
    }    

    
    @Override
    protected void invokeInternal2 (WorkflowContext wfCtx, ProgressMonitor monitor, Issues issues) {
        final Collection<?> allObjects = getExpressionResult (wfCtx, _expression);

        for (String checkFile : _checkFiles) 
            CheckBackendFacade.checkAll (checkFile, _fileEncoding, metaModels, issues, allObjects);

        if (_abortOnError && issues.hasErrors())
            throw new WorkflowInterruptedException ("Errors during validation.");
    }


    @Override
    public void checkConfigurationInternal (Issues issues) {
        super.checkConfiguration (issues);
        
        if (_expression == null && _emfAllChildrenSlot != null) 
            _expression = _emfAllChildrenSlot + ".eAllContents.union ( {" + _emfAllChildrenSlot + "} )";
        else if (_expression != null && _emfAllChildrenSlot == null) {
            // ok - do nothing, expression already has a reasonable value
        } 
        else 
            issues.addError(this, "You have to set one of the properties 'expression' and 'emfAllChildrenSlot'!");

        if (_checkFiles.isEmpty()) 
            issues.addError (this, "Property 'checkFile' not set!");
    }

    private Collection<?> getExpressionResult (WorkflowContext wfCtx, String expression2) {
        final Map<String, Object> localVars = new HashMap<String, Object>();
        final String[] names = wfCtx.getSlotNames ();
        for (int i = 0; i < names.length; i++) 
            localVars.put (names[i], wfCtx.get (names[i]));
        
        final Object result = XtendBackendFacade.evaluateExpression (expression2, metaModels, localVars);
        
        if (result instanceof Collection)
            return (Collection<?>) result;

        if (result == null)
            return Collections.EMPTY_SET;

        return Collections.singleton (result);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5793.java