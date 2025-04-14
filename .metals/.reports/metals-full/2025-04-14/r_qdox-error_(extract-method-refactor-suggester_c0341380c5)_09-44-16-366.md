error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2995.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2995.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2995.java
text:
```scala
r@@eturn (cache.get(assignable)).contains(toAssigTo);

/*******************************************************************************
 * Copyright (c) 2005, 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

package org.eclipse.xtend.shared.ui.core.metamodel.jdt.oaw;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.internal.xtend.util.Cache;
import org.eclipse.internal.xtend.util.StringHelper;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.xtend.shared.ui.core.metamodel.jdt.JdtTypeStrategy;
import org.eclipse.xtend.shared.ui.internal.XtendLog;

public class JdtOawClassicTypeStrategy implements JdtTypeStrategy {

    public JdtOawClassicTypeStrategy() {
        super();
    }

    public IType[] getSuperTypes(final IType type) throws JavaModelException {
        final ITypeHierarchy hier = type.newSupertypeHierarchy(new NullProgressMonitor());
        final IType[] ifs = hier.getSuperInterfaces(type);
        final IType st = hier.getSuperclass(type);
        if (st == null)
            return ifs;
        final IType[] result = new IType[ifs.length + 1];
        System.arraycopy(ifs, 0, result, 0, ifs.length);
        result[ifs.length] = st;
        return result;
    }

    private final Cache<IType,ITypeHierarchy> cache = new Cache<IType,ITypeHierarchy>() {

        @Override
        protected ITypeHierarchy createNew(IType type) {
            try {
                return type.newSupertypeHierarchy(new NullProgressMonitor());
            } catch (JavaModelException e) {
                XtendLog.logError(e);
                return null;
            }
        }
    };

    public boolean isAssignable(final IType toAssigTo, final IType assignable) {
        return ((ITypeHierarchy) cache.get(assignable)).contains(toAssigTo);
    }

    public boolean isGetter(final IMethod method) {
        int flags;
        try {
            flags = method.getFlags();
        } catch (final JavaModelException e) {
            return false;
        }
        try {
            if (!Flags.isStatic(flags) && method.getParameterTypes().length == 0
                    && !Signature.SIG_VOID.equals(method.getReturnType()))
                return true;
        } catch (final JavaModelException e) {
            XtendLog.logError(e);
        }
        return false;
    }

    public String getterToProperty(final String elementName) {
        return elementName;
    }

    public boolean isOperation(final IMethod method) {
        if (!isGetter(method)) {
            try {
                final int flags = method.getFlags();
                if (!Flags.isStatic(flags)) {
                    if (!method.getElementName().startsWith("set"))
                        return true;
                }
            } catch (final JavaModelException e) {
                return false;
            }
        }
        return false;
    }

    public String propertyName(final IMethod method) {
        return getterToProperty(method.getElementName());
    }

    public String getPropertiesInnerType(final IMethod method) {
        final IType type = method.getDeclaringType();
        IMethod[] methods = null;
        final String adderMethod = "add" + StringHelper.firstUpper(method.getElementName());
        try {
            methods = type.getMethods();
            for (int i = 0; i < methods.length; i++) {
                final IMethod m = methods[i];
                if (m.getParameterTypes().length == 1 && m.getElementName().equals(adderMethod))
                    return m.getParameterTypes()[0];
            }
        } catch (final JavaModelException e) {
            XtendLog.logError(e);
        }
        return null;
    }
    
    public boolean isConstant(IField field) {
		try {
			if (field.isEnumConstant() || field.getDeclaringType().isInterface() || (Flags.isPublic(field.getFlags()) && Flags.isFinal(field.getFlags()) && Flags.isStatic(field.getFlags()))) {
				return true;
			}
		} catch (JavaModelException e) {
			return false;
		}
		return false;
	}

	public String propertyName(IField field) {
		return field.getElementName();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2995.java