error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15362.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15362.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15362.java
text:
```scala
c@@tx.getResourceManager().setFileEncoding (fileEncoding);

/*
Copyright (c) 2008, 2009 Arno Haase, André Arnold.
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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.internal.xtend.expression.ast.Expression;
import org.eclipse.internal.xtend.xtend.parser.ParseFacade;
import org.eclipse.xtend.backend.BackendFacade;
import org.eclipse.xtend.backend.common.ExecutionContext;
import org.eclipse.xtend.backend.common.ExpressionBase;
import org.eclipse.xtend.backend.common.FunctionDefContext;
import org.eclipse.xtend.backend.common.NamedFunction;
import org.eclipse.xtend.backend.common.QualifiedName;
import org.eclipse.xtend.backend.functions.FunctionDefContextInternal;
import org.eclipse.xtend.expression.ExecutionContextImpl;
import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtend.middleend.LanguageContributor;
import org.eclipse.xtend.middleend.MiddleEnd;
import org.eclipse.xtend.middleend.MiddleEndFactory;
import org.eclipse.xtend.middleend.xtend.internal.OldExpressionConverter;
import org.eclipse.xtend.middleend.xtend.internal.OldHelper;
import org.eclipse.xtend.middleend.xtend.internal.TypeToBackendType;
import org.eclipse.xtend.middleend.xtend.internal.xtendlib.XtendGlobalVarOperations;
import org.eclipse.xtend.middleend.xtend.internal.xtendlib.XtendLibContributor;
import org.eclipse.xtend.middleend.xtend.plugin.OldCheckRegistryFactory;
import org.eclipse.xtend.middleend.xtend.plugin.OldXtendRegistryFactory;
import org.eclipse.xtend.typesystem.MetaModel;


/**
 * 
 * @author Arno Haase (http://www.haase-consulting.com)
 * @author André Arnold
 */
public final class XtendBackendFacade {
    private final String _xtendFile;
    private final MiddleEnd _middleEnd;
    private final Collection<MetaModel> _mms;
    
    /**
     * This method invokes a "stand alone" expression that knows nothing about any functions defined in files. It is useful for
     *  *very* simple use cases, and for testing purposes. <br>
     *  
     * Both mms and localVars may be null.
     */
    public static Object evaluateExpression (String expression, Collection<MetaModel> mms, Map<String, Object> localVars) {
        return evaluateExpression (expression, mms, localVars, null);
    }
        
    public static Object evaluateExpression (String expression, Collection<MetaModel> mms, Map<String, Object> localVars, Map<String, Object> globalVars) {
        return evaluateExpression (expression, null, null, mms, localVars, globalVars, null);
    }

    /**
     * This method invokes an expression that may call functions from an Xtend file.<br>
     * 
     * The fileEncoding may be null, in which case the platform's default encoding is used. Both mms and localVars may be null.
     */
    public static Object evaluateExpression (String expression, String initialXtendFileName, String fileEncoding, Collection<MetaModel> mms, Map<String, Object> localVars) {
        return evaluateExpression (expression, initialXtendFileName, fileEncoding, mms, localVars, null, null);
    }
        
    public static Object evaluateExpression (String expression, String initialXtendFileName, String fileEncoding, Collection<MetaModel> mms, Map<String, Object> localVars, Map<String, Object> globalVars, List<String> adviceResources) {
        return createForFile (initialXtendFileName, fileEncoding, mms).evaluateExpression (expression, localVars, globalVars, adviceResources);
    }
        
    public Object evaluateExpression (String expression, Map<String, Object> localVars) {
        return evaluateExpression (expression, localVars, null, null);
    }
    
    public Object evaluateExpression (String expression, Map<String, Object> localVars, Map<String, Object> globalVars, List<String> adviceResources) {
        if (localVars == null)
            localVars = new HashMap<String, Object> ();
        if (globalVars == null)
            globalVars = new HashMap<String, Object> ();
        if (adviceResources == null)
            adviceResources = new ArrayList<String> ();
        
        for (String a: adviceResources)
            _middleEnd.applyAdvice (a);

        final Expression oldAst = ParseFacade.expression (expression);
        
        ExecutionContextImpl ctx = new ExecutionContextImpl ();
        for (MetaModel mm: _mms)
            ctx.registerMetaModel (mm);
        for (String varName: localVars.keySet())
            ctx = (ExecutionContextImpl) ctx.cloneWithVariable (new Variable (varName, ctx.getType (localVars.get (varName))));
            
        final TypeToBackendType typeConverter = new TypeToBackendType (_middleEnd.getTypesystem(), ctx);
        final ExpressionBase newAst = new OldExpressionConverter (ctx, typeConverter, "<no file>").convert (oldAst);

        _middleEnd.getExecutionContext().setFunctionDefContext (createFdc ());
        //TODO configure isLogStacktrace
        _middleEnd.getExecutionContext().getLocalVarContext().getLocalVars().putAll (localVars);
        _middleEnd.getExecutionContext().getContributionStateContext().storeState (XtendGlobalVarOperations.GLOBAL_VAR_VALUES_KEY, globalVars);

        return newAst.evaluate (_middleEnd.getExecutionContext());
    }

    
    private FunctionDefContext createFdc () {
        if (_xtendFile != null) 
            return _middleEnd.getFunctions (_xtendFile);

        final FunctionDefContextInternal result = _middleEnd.createEmptyFdc();
        
        for (NamedFunction f: new XtendLibContributor (_middleEnd).getContributedFunctions())
            result.register (f, false);
        return result;
    }
    
    
    /**
     * This function invokes a single Xtend function, returning the result. The fileEncoding may be null, in which case the platform's default file
     *  encoding is used.
     */
    public static Object invokeXtendFunction (String xtendFileName, String fileEncoding, Collection<MetaModel> mms, QualifiedName functionName, Object... parameters) {
        return createForFile (xtendFileName, fileEncoding, mms).invokeXtendFunction (functionName, parameters);
    }
        
    public Object invokeXtendFunction (QualifiedName functionName, Object... parameters) {
        final FunctionDefContext fdc = _middleEnd.getFunctions (_xtendFile);
        final ExecutionContext ctx = BackendFacade.createExecutionContext (fdc, _middleEnd.getTypesystem(), true); //TODO configure isLogStacktrace
        return fdc.invoke (ctx, functionName, Arrays.asList (parameters));
    }
    
    
    public static XtendBackendFacade createForFile (String xtendFileName, String fileEncoding, Collection<MetaModel> mms) {
        return new XtendBackendFacade (xtendFileName, fileEncoding, mms);
    }

    private Map<Class<?>, Object> getSpecificParameters (String fileEncoding, Collection<MetaModel> mms) {
        fileEncoding = OldHelper.normalizedFileEncoding (fileEncoding);

        final ExecutionContextImpl ctx = new ExecutionContextImpl ();
        ctx.setFileEncoding (fileEncoding);
        for (MetaModel mm: mms)
            ctx.registerMetaModel (mm);
        
        final Map<Class<?>, Object> result = new HashMap<Class<?>, Object> ();
        result.put (OldXtendRegistryFactory.class, ctx);
        result.put (OldCheckRegistryFactory.class, ctx);
        return result;
    }
    
    
    private XtendBackendFacade (String xtendFileName, String fileEncoding, Collection<MetaModel> mms) {
        if (mms == null)
            mms = new ArrayList<MetaModel> ();
        
        _xtendFile = OldHelper.normalizeXtendResourceName (xtendFileName);
        _mms = mms;
        if (MiddleEndFactory.canCreateFromExtentions()) {
        	_middleEnd = MiddleEndFactory.createFromExtensions (OldHelper.guessTypesystem (mms), getSpecificParameters (fileEncoding, mms));
        } else {
        	_middleEnd = MiddleEndFactory.create (OldHelper.guessTypesystem (mms), LanguageContributor.INSTANCE.getFreshMiddleEnds (getSpecificParameters (fileEncoding, mms)));
        }
    }

    public FunctionDefContext getFunctionDefContext () {
        return _middleEnd.getFunctions (_xtendFile);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15362.java