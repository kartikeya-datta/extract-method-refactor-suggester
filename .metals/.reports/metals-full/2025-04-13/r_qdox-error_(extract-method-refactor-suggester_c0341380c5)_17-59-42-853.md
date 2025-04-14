error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16826.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16826.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16826.java
text:
```scala
final S@@et<Type> result = new HashSet<Type>(typeNameCache.getValues());

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

package org.eclipse.xtend.shared.ui.core.metamodel.jdt;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.internal.xtend.expression.parser.SyntaxConstants;
import org.eclipse.internal.xtend.util.Cache;
import org.eclipse.internal.xtend.util.Pair;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.xtend.expression.TypeSystem;
import org.eclipse.xtend.shared.ui.internal.XtendLog;
import org.eclipse.xtend.typesystem.MetaModel;
import org.eclipse.xtend.typesystem.Type;

public class JdtMetaModel implements MetaModel, IElementChangedListener {

	protected TypeSystem typesystem;

	private final String name;

	private final JdtTypeStrategy strategy;

	private final IJavaProject project;

	public TypeSystem getTypeSystem() {
		return typesystem;
	}

	public void setTypeSystem(final TypeSystem typeSystem) {
		typesystem = typeSystem;
	}

	private final static Map<IPath, JdtMetaModel> metaModels = new HashMap<IPath, JdtMetaModel>();

	public boolean changed = false;

	public void elementChanged(final ElementChangedEvent event) {
		if (!changed && project.isOnClasspath(event.getDelta().getElement())) {
			changed = true;
		}
	}

	public final static JdtMetaModel create(final String name, final IJavaProject project,
			final JdtTypeStrategy strategy) {
		JdtMetaModel mm = JdtMetaModel.metaModels.get(project.getPath());
		if (mm == null || mm.changed) {
			if (mm != null) {
				JavaCore.removeElementChangedListener(mm);
			}
			mm = new JdtMetaModel(name, project, strategy);
			JavaCore.addElementChangedListener(mm);
			JdtMetaModel.metaModels.put(project.getPath(), mm);
		}
		return mm;
	}

	public JdtMetaModel(final String name, final IJavaProject project, final JdtTypeStrategy strategy) {
		this.name = name;
		this.strategy = strategy;
		this.project = project;
	}

	private Type getTypeForIType(final IType type) {
		if (type == null || !type.exists()) {
			return null;
		}
		try {
			final ITypeHierarchy hierarchy = type.newSupertypeHierarchy(new NullProgressMonitor());
			if (hierarchy.contains(type.getJavaProject().findType(List.class.getName()))) {
				return typesystem.getListType(typesystem.getObjectType());
			} else if (hierarchy.contains(type.getJavaProject().findType(Set.class.getName()))) {
				return typesystem.getSetType(typesystem.getObjectType());
			} else if (hierarchy.contains(type.getJavaProject().findType(Collection.class.getName()))) {
				return typesystem.getCollectionType(typesystem.getObjectType());
			}
		} catch (final JavaModelException e) {
			XtendLog.logError(e);
			return null;
		}
		return new JdtTypeImpl(JdtMetaModel.this, type, getName(type), strategy);
	}

	protected Type getType(final String fqn) throws JavaModelException {
		if (fqn.indexOf(SyntaxConstants.NS_DELIM) == -1) {
			// namespace
			return null;
		}
		final String typeName = fqn.replaceAll(SyntaxConstants.NS_DELIM, ".");
		final IType type = findType(project, typeName);
		if (type != null) {
			return getTypeForIType(type);
		} else {
			return null;
		}
	}

	private String getName(final IType class1) {
		return class1.getFullyQualifiedName().replaceAll("\\.", SyntaxConstants.NS_DELIM);
	}

	private final Cache<String,Type> typeNameCache = new Cache<String,Type>() {
		@Override
		protected Type createNew(String typeName) {
			try {
				Type result = getType(typeName);
				return result;
			} catch (Exception e) {
				return null;
			}
		}
	};

	public Type getTypeForName(final String typeName) {
		return typeNameCache.get(typeName);
	}

	public Type getType(final Object obj) {
		throw new UnsupportedOperationException();
	}

	public Type getTypeForClass(final IType clazz) {
		return getTypeSystem().getTypeForName(getName(clazz));
	}

	public Set<? extends Type> getKnownTypes() {
		final Set<Type> result = new HashSet(typeNameCache.getValues());
		result.remove(null);
		return result;
	}

	public String getName() {
		return name;
	}

	private final Cache<Pair<String,IType>,String> signCache = new Cache<Pair<String,IType>,String>() {

		@Override
		protected String createNew(Pair<String,IType> p) {
			String signature = p.getFirst();
			// primitives
			if (Signature.SIG_BOOLEAN.equals(signature)) {
				return "Boolean";
			} else if (Signature.SIG_INT.equals(signature)) {
				return "Integer";
			} else if (Signature.SIG_LONG.equals(signature)) {
				return "Integer";
			} else if (Signature.SIG_SHORT.equals(signature)) {
				return "Integer";
			} else if (Signature.SIG_CHAR.equals(signature)) {
				return "String";
			} else if (Signature.SIG_BYTE.equals(signature)) {
				return "Integer";
			}
			IType usingType = p.getSecond();
			try {
				String[][] result = usingType.resolveType(Signature.toString(signature));
				if (result == null) {
					return Signature.toString(signature).replaceAll("\\.", SyntaxConstants.NS_DELIM);
				}
				if (result.length > 0) {
					StringBuffer buff = new StringBuffer();
					for (int i = 0; i < result[0].length; i++) {
						String part = result[0][i];
						buff.append(part);
						if (i < result[0].length - 1) {
							buff.append(".");
						}
					}
					String fqn = buff.toString();
					return fqn.replaceAll("\\.", SyntaxConstants.NS_DELIM);
				}
			} catch (JavaModelException e) {
				XtendLog.logError(e);
			}
			return null;
		}
	};

	public String getFullyQualifiedName(final String signature, final IType usingType) {
		return signCache.get(new Pair<String,IType>(signature, usingType));
	}

	public IType findType(final IJavaProject project, final String typeName) throws JavaModelException {
		IType t = project.findType(typeName);
		if (t == null) {
			final String[] projects = project.getRequiredProjectNames();
			for (int i = 0; i < projects.length && t == null; i++) {
				final IJavaProject anotherProject = project.getJavaModel().getJavaProject(projects[i]);
				if (anotherProject != null) {
					t = anotherProject.findType(typeName);
				}
			}
		}
		return t;
	}

	public Set<String> getNamespaces() {
		return new HashSet<String>();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16826.java