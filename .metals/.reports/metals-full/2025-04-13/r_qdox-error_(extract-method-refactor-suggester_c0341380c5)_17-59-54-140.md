error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11943.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11943.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11943.java
text:
```scala
t@@hrow new EvaluationException(ite.getCause(), this, ctx);

/*******************************************************************************
 * Copyright (c) 2005, 2009 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

package org.eclipse.internal.xtend.xtend.ast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.mwe.core.resources.ResourceLoaderFactory;
import org.eclipse.internal.xtend.expression.ast.DeclaredParameter;
import org.eclipse.internal.xtend.expression.ast.Identifier;
import org.eclipse.xtend.expression.AnalysationIssue;
import org.eclipse.xtend.expression.EvaluationException;
import org.eclipse.xtend.expression.ExecutionContext;
import org.eclipse.xtend.expression.IExecutionContextAware;
import org.eclipse.xtend.typesystem.Type;

/**
 * @author Sven Efftinge (http://www.efftinge.de)
 * @author Arno Haase
 * @author Heiko Behrens
 */
public class JavaExtensionStatement extends AbstractExtension {

    protected Identifier javaType;

    protected Identifier javaMethod;

    protected List<Identifier> javaParamTypes;
    
    private Method method = null;

    public JavaExtensionStatement(final Identifier name,
            final List<DeclaredParameter> formalParameters, final Identifier returnType, final Identifier javaType,
            final Identifier javaMethod, final List<Identifier> javaParamTypes, final boolean cached, final boolean isPrivate) {
        super(name, returnType, formalParameters, cached, isPrivate);
        this.javaType = javaType;
        this.javaMethod = javaMethod;
        this.javaParamTypes = javaParamTypes;
    }

    public Identifier getJavaType() {
        return javaType;
    }
    
    public String getJavaMethodName () {
        return javaMethod.getValue();
    }

    @Override
    public Object evaluateInternal(final Object[] parameters, final ExecutionContext ctx) {
        final HashSet<AnalysationIssue> issues = new HashSet<AnalysationIssue>();
        try {
            final Method method = getJavaMethod(ctx, issues);
            if (method == null) {
                final StringBuilder b = new StringBuilder();
                for (final Iterator<AnalysationIssue> iter = issues.iterator(); iter.hasNext();) {
                    final AnalysationIssue element = iter.next();
                    b.append(element.toString()).append("\n");
                }
                throw new EvaluationException(javaMethodToString() + " not found, problems were: \n" + b, this, ctx);
            }
            convertTypesToMethodSignature(ctx, method, parameters);
            if (Modifier.isStatic(method.getModifiers())) {
                return method.invoke(null, parameters);
            } else {
            	Object instance = method.getDeclaringClass().newInstance();
            	if (IExecutionContextAware.class.isAssignableFrom(method.getDeclaringClass())) {
            		((IExecutionContextAware)instance).setExecutionContext(ctx);
            	}
            	return method.invoke(instance, parameters);
            }
        } catch (final InvocationTargetException ite) {
            throw new RuntimeException(ite.getCause());
        } catch (final Exception e) {
            throw new EvaluationException(e, this, ctx);
        }
    }

	private void convertTypesToMethodSignature(ExecutionContext ctx, Method method, Object[] parameters) {
		Class<?>[] paramTypes = method.getParameterTypes();
		for(int i = 0; i < parameters.length; i++) {
			Object param = parameters[i];
			parameters[i] = ctx.getType(param).convert(param, paramTypes[i]);
		}
	}

	private String javaMethodToString() {
        final StringBuffer buff = new StringBuffer();
        for (final Iterator<Identifier> iter = javaParamTypes.iterator(); iter.hasNext();) {
            buff.append(iter.next());
            if (iter.hasNext()) {
                buff.append(",");
            }
        }

        return javaType + "." + javaMethod + "(" + buff + ")";
    }

	public Method getJavaMethod(final ExecutionContext ctx, final Set<AnalysationIssue> issues) {
		if (method != null) {
			return method;
			}
		try {
            Class<?> clazz = null;
            clazz = ResourceLoaderFactory.createResourceLoader().loadClass(javaType.getValue());
            if (clazz == null) {
                issues.add(new AnalysationIssue(AnalysationIssue.TYPE_NOT_FOUND, "Couldn't find Java type "+javaType.getValue(), javaType));
                return null;
            }
            final Class<?>[] paramTypes = new Class[javaParamTypes.size()];
            for (int i = 0, x = javaParamTypes.size(); i < x; i++) {
                final Identifier javaParamType = javaParamTypes.get(i);

                paramTypes[i] = ResourceLoaderFactory.createResourceLoader().loadClass(javaParamType.getValue());
                if (paramTypes[i] == null) {
                    issues.add(new AnalysationIssue(AnalysationIssue.TYPE_NOT_FOUND, javaParamType.getValue(), javaParamType));
                    return null;
                }
            }
            final Method m = clazz.getMethod(javaMethod.getValue(), paramTypes);

            if (!Modifier.isPublic(m.getModifiers())) {
                issues.add(new AnalysationIssue(AnalysationIssue.FEATURE_NOT_FOUND, javaMethod.getValue() + " must be public!", javaMethod));
            }
            method = m;
        } catch (final NoSuchMethodException e) {
            issues.add(new AnalysationIssue(AnalysationIssue.FEATURE_NOT_FOUND, javaMethod.getValue(), javaMethod));
        }
        return method;
    }

    @Override
    public void analyzeInternal(final ExecutionContext ctx, final Set<AnalysationIssue> issues) {
        if (returnType == null) {
            issues.add(new AnalysationIssue(AnalysationIssue.SYNTAX_ERROR, "A return type must be specified for java extensions!", this));
        }
        getJavaMethod(ctx, issues);
    }

    @Override
    protected Type internalGetReturnType(final Type[] parameters, final ExecutionContext ctx, final Set<AnalysationIssue> issues) {
        if (returnType == null) {
            issues.add(new AnalysationIssue(AnalysationIssue.SYNTAX_ERROR, "A return type must be specified for java extensions!", this));
            return null;
        } else
            return ctx.getTypeForName(returnType.getValue());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11943.java