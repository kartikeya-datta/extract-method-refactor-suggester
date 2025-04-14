error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4593.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4593.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4593.java
text:
```scala
i@@f (feature.isChangeable() && !feature.isMany()){ //  && !feature.isDerived()  { !feature.isUnsettable()

/**
 * <copyright>
 *
 * Copyright (c) 2005-2006 Sven Efftinge (http://www.efftinge.de) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sven Efftinge (http://www.efftinge.de) - Initial API and implementation
 *
 * </copyright>
 */
package org.eclipse.xtend.typesystem.emf;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.internal.xtend.type.baseimpl.FeatureImpl;
import org.eclipse.internal.xtend.type.baseimpl.OperationImpl;
import org.eclipse.internal.xtend.type.baseimpl.PropertyImpl;
import org.eclipse.internal.xtend.util.StringHelper;
import org.eclipse.xtend.typesystem.AbstractTypeImpl;
import org.eclipse.xtend.typesystem.Feature;
import org.eclipse.xtend.typesystem.Type;

public class EClassType extends AbstractTypeImpl {

	private final static Log log = LogFactory.getLog(EClassType.class);

	private final EmfRegistryMetaModel emfMetaModel;

	private final EClass eClass;

	public EClassType(final EmfRegistryMetaModel model, final String name, final EClass class1) {
		super(model.getTypeSystem(), name);
		emfMetaModel = model;
		eClass = class1;
	}

	@Override
	public Feature[] getContributedFeatures() {
		final Set<FeatureImpl> result = new HashSet<FeatureImpl>();
		// Attributes
		final List<EStructuralFeature> list = eClass.getEStructuralFeatures();
		for (final EStructuralFeature feature : list) {
			final Type t = emfMetaModel.getTypeForETypedElement(feature);
			if (t == null) {
				log.warn("Couldn't resolve type for " + getTypeName(feature.getEType()));
			}
			else {
				result.add(new PropertyImpl(this, feature.getName(), t) {

					public Object get(final Object target) {
						return ((EObject) target).eGet(feature);
					}

					@Override
					public void set(final Object target, Object newValue) {
						if (feature.isChangeable() && !feature.isUnsettable() && !feature.isDerived()) {
							if (feature.getEType() instanceof EDataType && !(feature.getEType() instanceof EEnum)) {
								final EDataType dt = (EDataType) feature.getEType();
								newValue = getReturnType().convert(newValue, dt.getInstanceClass());
							}
							((EObject) target).eSet(feature, newValue);
						}
						else
							throw new UnsupportedOperationException("setting property '" + feature.getName()
									+ "' is not allowed!");
					}
				});
			}
		}
		// Operations
		final List operations = eClass.getEOperations();
		for (final Iterator iter = operations.iterator(); iter.hasNext();) {
			final EOperation op = (EOperation) iter.next();
			final EList emfParams = op.getEParameters();
			final Type[] paramTypes = new Type[emfParams.size()];
			boolean errors = false;
			for (int i = 0, x = emfParams.size(); i < x; i++) {
				final EParameter param = (EParameter) emfParams.get(i);
				paramTypes[i] = emfMetaModel.getTypeForETypedElement(param);
				if (paramTypes[i] == null) {
					log.warn("Couldn't resolve type for " + getTypeName(param.getEType()));
					errors = true;
				}
			}
			final Type t = emfMetaModel.getTypeForETypedElement(op);
			if (t == null) {
				log.warn("Couldn't resolve type for " + getTypeName(op.getEType()));
				errors = true;
			}
			if (!errors) {

				result.add(new OperationImpl(this, op.getName(), t, paramTypes) {

					@Override
					protected Object evaluateInternal(final Object target, final Object[] params) {
						final Class[] paramClasses = new Class[emfParams.size()];
						for (int i = 0, x = emfParams.size(); i < x; i++) {
							final EParameter param = (EParameter) emfParams.get(i);
							if (param.isMany()) {
								paramClasses[i] = EList.class;
							}
							else {
								paramClasses[i] = param.getEType().getInstanceClass();
							}
							params[i] = getParameterTypes().get(i).convert(params[i], paramClasses[i]);
						}
						try {
							final Method m = target.getClass().getMethod(getName(), paramClasses);
							return m.invoke(target, params);
						}
						catch (final Exception e) {
							throw new RuntimeException(e);
						}
					}
				});
			}
		}
		// setter
		for (final EStructuralFeature feature : eClass.getEStructuralFeatures()) {

			final Type t = emfMetaModel.getTypeForETypedElement(feature);
			if (t == null) {
				log.warn("Couldn't resolve type for " + getTypeName(feature.getEType()));
			}
			else {
				if (feature.isChangeable() && !feature.isDerived() && !feature.isMany()) { // !feature.isUnsettable()
					// &&
					result.add(new OperationImpl(this, "set" + StringHelper.firstUpper(feature.getName()), this,
							new Type[] { t }) {

						@Override
						protected Object evaluateInternal(final Object target, final Object[] params) {
							Object newValue = params[0];
							if (newValue != null && feature.getEType() instanceof EDataType
									&& !(feature.getEType() instanceof EEnum)) {
								final EDataType dt = (EDataType) feature.getEType();
								newValue = getParameterTypes().get(0).convert(newValue, dt.getInstanceClass());
							}
							((EObject) target).eSet(feature, newValue);
							return target;
						}

					});
				}
			}
		}
		return result.toArray(new Feature[result.size()]);
	}

	private String getTypeName(final EClassifier type) {
		if (type == null)
			return "null";

		return type.getName();
	}

	public boolean isInstance(final Object o) {
		return eClass.isInstance(o);
	}

	public Object newInstance() {
		return eClass.getEPackage().getEFactoryInstance().create(eClass);
	}

	@Override
	protected Set<Type> internalGetSuperTypes() {
		final EList st = eClass.getESuperTypes();
		final Set<Type> result = new HashSet<Type>();
		for (final Iterator iter = st.iterator(); iter.hasNext();) {
			final EClass element = (EClass) iter.next();
			result.add(emfMetaModel.getTypeForEClassifier(element));
		}
		result.add(emfMetaModel.getEobjectType());
		return result;
	}

	@Override
	public boolean isAbstract() {
		return eClass.isAbstract();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4593.java