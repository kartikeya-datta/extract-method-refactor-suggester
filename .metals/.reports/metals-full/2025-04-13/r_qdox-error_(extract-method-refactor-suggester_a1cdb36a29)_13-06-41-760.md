error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17894.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17894.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17894.java
text:
```scala
X@@tendFacade(final ExecutionContext ctx) {

/*******************************************************************************
 * Copyright (c) 2005, 2006 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/
package org.eclipse.xtend;

import java.io.StringReader;
import java.util.List;
import java.util.Set;

import org.eclipse.internal.xtend.xtend.ast.Extension;
import org.eclipse.internal.xtend.xtend.ast.ExtensionFile;
import org.eclipse.internal.xtend.xtend.parser.ParseFacade;
import org.eclipse.xtend.expression.AnalysationIssue;
import org.eclipse.xtend.expression.ExecutionContext;
import org.eclipse.xtend.expression.ExecutionContextImpl;
import org.eclipse.xtend.expression.Resource;
import org.eclipse.xtend.expression.TypeSystemImpl;
import org.eclipse.xtend.typesystem.MetaModel;
import org.eclipse.xtend.typesystem.Type;

public class XtendFacade {

    private ExecutionContext ctx;

    private XtendFacade(final ExecutionContext ctx) {
        this.ctx = ctx;
    }
    
    public final XtendFacade cloneWithExtensions(String extensionCode) {
        return new XtendFacade( ctx.cloneWithResource(parse(extensionCode)));
    }
    
    public final void registerMetaModel(final MetaModel mm) {
    	if (ctx instanceof ExecutionContextImpl) {
    		((ExecutionContextImpl) ctx).registerMetaModel(mm);
    	} else {
    		throw new IllegalStateException("Couldn't register Metamodel - ExecutionContextImpl expected.");
    	}
    }
    
    private ExtensionFile parse(final String extFile) {
    	return ParseFacade.file(new StringReader(extFile), "nofile");
    }

    public final static XtendFacade create(final String... extFile) {
        return create(new ExecutionContextImpl(new TypeSystemImpl()),extFile);
    }
    
    public final static XtendFacade create(ExecutionContext ctx,final String... extFile) {
    	ctx = ctx.cloneWithResource(new Resource() {
    		
    		public String getFullyQualifiedName() {
    			return null;
    		}
    		
    		public void setFullyQualifiedName(final String fqn) {
    			
    		}
    		
    		public String[] getImportedNamespaces() {
    			return null;
    		}
    		
    		public String[] getImportedExtensions() {
    			return extFile;
    		}
    	});
    	return new XtendFacade(ctx);
    }

    public Object call(final String ext, Object... params) {
        if (params==null)
            params = new Object[] {null};
        final Extension extension = ctx.getExtension(ext, params);
        if (extension == null)
            throw new IllegalArgumentException("Couldn't find extension " + ext);
        return extension.evaluate(params, ctx);
    }
    
    public Object call(final String ext, List<?> params) {
    	Object[] paramsArray = new Object[params.size()];
    	paramsArray = params.toArray(paramsArray);
    	final Extension extension = ctx.getExtension(ext, paramsArray);
        if (extension == null)
            throw new IllegalArgumentException("Couldn't find extension " + ext);
        return extension.evaluate(paramsArray, ctx);
    }

    public boolean hasExtension(final String ext, Object[] paramsArray) {
    	final Extension extension = ctx.getExtension(ext, paramsArray);
        return extension != null;
    }

    public boolean hasExtension(final String ext, List<?> params) {
    	Object[] paramsArray = new Object[params.size()];
    	paramsArray = params.toArray(paramsArray);
    	return hasExtension(ext, paramsArray);
    }
    
    public Type analyze(final String string, Object[] objects, final Set<AnalysationIssue> issues) {
        if (objects == null) {
            objects = new Object[0];
        }
        final Extension extension = ctx.getExtension(string, objects);
        final Type[] params = new Type[objects.length];
        for (int i = 0; i < params.length; i++) {
            params[i] = ctx.getType(objects[i]);
        }
        return ctx.getReturnType(extension, params, issues);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17894.java