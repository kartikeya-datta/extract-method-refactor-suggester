error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16550.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16550.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16550.java
text:
```scala
i@@s = ResourceLoaderFactory.createResourceLoader().getResourceAsStream (_extensionFile.replace (SyntaxConstants.NS_DELIM, "/") + "." +XtendFile.FILE_EXTENSION);

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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.emf.mwe.core.resources.ResourceLoaderFactory;
import org.eclipse.internal.xtend.expression.parser.SyntaxConstants;
import org.eclipse.internal.xtend.xtend.XtendFile;
import org.eclipse.xtend.expression.AbstractExpressionsUsingWorkflowComponent;
import org.eclipse.xtend.middleend.LanguageContributor;
import org.eclipse.xtend.middleend.xtend.plugin.OldXtendRegistryFactory;


/**
 * 
 * @author Arno Haase (http://www.haase-consulting.com)
 * @author André Arnold
 */
public class XtendComponent extends AbstractExpressionsUsingWorkflowComponent {

    /** Stores the value of the 'invoke' property. Needed for error analysis. */ 
    private String _invokeExpression;
    String _extensionFile = null;
    private String _expression = null;
    private String _outputSlot = WorkflowContext.DEFAULT_SLOT;
    
    private String _fileEncoding = null;
    
    public XtendComponent () {
    	if (LanguageContributor.INSTANCE.getLanguageContributionByName(OldXtendRegistryFactory.LANGUAGE_NAME) == null)
    		LanguageContributor.INSTANCE.addLanguageContribution (OldXtendRegistryFactory.class);
	}

    public void setFileEncoding (String fileEncoding) {
        _fileEncoding = fileEncoding;
    }
    
    @Override
    public String getLogMessage() {
    	return "executing '" + _invokeExpression + "'";
    }
    
    public void setInvoke (String invoke) {
    	_invokeExpression = invoke;
        final int i = invoke.lastIndexOf (SyntaxConstants.NS_DELIM);
        if (i != -1) {
            _extensionFile = invoke.substring(0, i);
            _expression = invoke.substring (i + SyntaxConstants.NS_DELIM.length());
        } 
        else 
            _expression = invoke;
    }

    public void setOutputSlot (String outputSlot) {
        _outputSlot = outputSlot;
    }
    
    @Override
    public void invokeInternal2(final WorkflowContext ctx, final ProgressMonitor monitor, final Issues issues) {
        if (! extensionFileExists ()) {
            issues.addError ("Cannot find extension file: " + _extensionFile);
            return;
        }
        
        final Map<String, Object> localVars = new HashMap<String, Object> ();
        for (String slotName: ctx.getSlotNames())
            localVars.put (slotName, ctx.get (slotName));
        
        final Map<String, Object> globalVars = new HashMap<String, Object> ();
        Set<String> varNames = getGlobalVars(ctx).keySet();
        for (String varName: varNames)
            globalVars.put (varName, getGlobalVars(ctx).get(varName).getValue());
        
        final Object result = XtendBackendFacade.evaluateExpression (_expression, _extensionFile, _fileEncoding, metaModels, localVars, globalVars, _advice);
        ctx.set (_outputSlot, result);
    }

    private boolean extensionFileExists() {
        InputStream is = null;
        try {
            is = ResourceLoaderFactory.createResourceLoader().getResourceAsStream (_extensionFile.replace (SyntaxConstants.NS_DELIM, "/") + XtendFile.FILE_EXTENSION);
        }
        catch (Exception exc) {
            // do nothing - an exception just means that the extension file does not exist 
        }
        if (is != null) {
            try {
                is.close ();
            }
            catch (Exception e) {}
        }
        return is != null;
    }

    @Override
    public void checkConfigurationInternal (Issues issues) {
        super.checkConfigurationInternal (issues);
        
        // Try to create detailed error message (see Bug#172567)
        String compPrefix = getId()!=null ? getId()+": " : "";
        
        if (_invokeExpression == null || _invokeExpression.trim().length()==0) {
            issues.addError(compPrefix + "Property 'invoke' not specified.");
            return;
        }
        if (_extensionFile == null) {
            issues.addError (compPrefix + "Error parsing property 'invoke': Could not extract name of the extension file.");
            return;
        }
        if (! extensionFileExists () || _expression == null) {
            issues.addError (compPrefix + "Property 'invoke' not specified properly. Extension file '" + _extensionFile + "' not found.");
            return;
        }
        if (_expression == null) {
            issues.addError (compPrefix + "Error parsing property 'invoke': Could not extract the expression to invoke in extension file '" + _extensionFile + "'.");
            return;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16550.java