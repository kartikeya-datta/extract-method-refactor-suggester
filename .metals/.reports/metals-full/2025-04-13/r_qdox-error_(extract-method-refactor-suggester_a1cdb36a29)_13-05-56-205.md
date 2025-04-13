error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15361.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15361.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15361.java
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
import java.util.Map;

import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.xtend.backend.BackendFacade;
import org.eclipse.xtend.backend.common.ExecutionContext;
import org.eclipse.xtend.backend.common.FunctionDefContext;
import org.eclipse.xtend.backend.common.QualifiedName;
import org.eclipse.xtend.expression.ExecutionContextImpl;
import org.eclipse.xtend.middleend.LanguageContributor;
import org.eclipse.xtend.middleend.MiddleEnd;
import org.eclipse.xtend.middleend.MiddleEndFactory;
import org.eclipse.xtend.middleend.xtend.internal.OldHelper;
import org.eclipse.xtend.middleend.xtend.internal.xtend.CheckConverter;
import org.eclipse.xtend.middleend.xtend.plugin.OldCheckRegistryFactory;
import org.eclipse.xtend.middleend.xtend.plugin.OldXtendRegistryFactory;
import org.eclipse.xtend.typesystem.MetaModel;


/**
 * 
 * @author Arno Haase (http://www.haase-consulting.com)
 * @author André Arnold
 */
public class CheckBackendFacade {

    private final String _checkFile;
    private final MiddleEnd _middleEnd;
    private final Collection<MetaModel> _mms;

    public static void checkAll (String xtendFileName, Collection<MetaModel> mms, Issues issues, Collection<?> allObjects) {
        checkAll (xtendFileName, null, mms, issues, allObjects);
    }
    
    public static void checkAll (String checkFileName, String fileEncoding, Collection<MetaModel> mms, Issues issues, Collection<?> allObjects) {
        CheckBackendFacade.invokeCheckFunction (checkFileName, fileEncoding, mms, CheckConverter.ALL_CHECKS_FUNCTION_NAME, issues, allObjects);
    }

    /**
     * This function invokes a single Xtend function, returning the result. The fileEncoding may be null, in which case the platform's default file
     *  encoding is used.
     */
    public static Object invokeCheckFunction (String checkFileName, String fileEncoding, Collection<MetaModel> mms, QualifiedName functionName, Object... parameters) {
        return createForFile (checkFileName, fileEncoding, mms).invokeCheckFunction (functionName, parameters);
    }
    
    public Object invokeCheckFunction (QualifiedName functionName, Object... parameters) {
        final FunctionDefContext fdc = _middleEnd.getFunctions (_checkFile);
        final ExecutionContext ctx = BackendFacade.createExecutionContext (fdc, _middleEnd.getTypesystem(), true); //TO--DO configure isLogStacktrace
        return fdc.invoke (ctx, functionName, Arrays.asList (parameters));
    }
    
    
    public static CheckBackendFacade createForFile (String checkFileName, String fileEncoding, Collection<MetaModel> mms) {
        return new CheckBackendFacade (checkFileName, fileEncoding, mms);
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
    
    private CheckBackendFacade (String checkFileName, String fileEncoding, Collection<MetaModel> mms) {
        if (mms == null)
            mms = new ArrayList<MetaModel> ();
        
        _checkFile = OldHelper.normalizeCheckResourceName (checkFileName);
        _mms = mms;
    	if (LanguageContributor.INSTANCE.getLanguageContributionByName (OldXtendRegistryFactory.LANGUAGE_NAME) == null) {
    		LanguageContributor.INSTANCE.addLanguageContribution (OldXtendRegistryFactory.class);
    	}
    	if (LanguageContributor.INSTANCE.getLanguageContributionByName (OldCheckRegistryFactory.LANGUAGE_NAME) == null) {
    		LanguageContributor.INSTANCE.addLanguageContribution (OldCheckRegistryFactory.class);
    	}
        if (MiddleEndFactory.canCreateFromExtentions()) {
            _middleEnd = MiddleEndFactory.createFromExtensions (OldHelper.guessTypesystem (mms), getSpecificParameters (fileEncoding, mms));
        } else {
        	_middleEnd = MiddleEndFactory.create (OldHelper.guessTypesystem (mms), LanguageContributor.INSTANCE.getFreshMiddleEnds (getSpecificParameters (fileEncoding, mms)));
        }
    }

    public FunctionDefContext getFunctionDefContext () {
        return _middleEnd.getFunctions (_checkFile);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15361.java