error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2938.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2938.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2938.java
text:
```scala
s@@uper(name, XsdTypesystem.XSD_TYPE_PREFIX + name, Arrays.asList(ListType.INSTANCE));

package org.eclipse.xtend.backend.types.xsd.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.util.FeatureMapUtil.Validator;
import org.eclipse.xtend.backend.common.BackendType;
import org.eclipse.xtend.backend.common.BackendTypesystem;
import org.eclipse.xtend.backend.common.ExecutionContext;
import org.eclipse.xtend.backend.common.QualifiedName;
import org.eclipse.xtend.backend.functions.AbstractFunction;
import org.eclipse.xtend.backend.types.AbstractProperty;
import org.eclipse.xtend.backend.types.AbstractType;
import org.eclipse.xtend.backend.types.builtin.BooleanType;
import org.eclipse.xtend.backend.types.builtin.CollectionType;
import org.eclipse.xtend.backend.types.builtin.ListType;
import org.eclipse.xtend.backend.types.builtin.ObjectType;
import org.eclipse.xtend.backend.types.builtin.VoidType;
import org.eclipse.xtend.backend.types.emf.EObjectType;
import org.eclipse.xtend.backend.types.xsd.XsdTypesystem;

public class EFeatureMapType extends AbstractType {

	public EFeatureMapType (final String name, EClass owner, XsdTypesystem ts) {
		super(name, XsdTypesystem.XSD_TYPE_PREFIX + name, Arrays.asList(CollectionType.INSTANCE));
	
		for (final EStructuralFeature f : getMapFeatures()) {
			register ( new AbstractProperty (this, EStructuralFeature.class, f.getName(), true, f.isChangeable()) {

				@Override
				protected Object getRaw (ExecutionContext ctx, Object target) {
					if (target == null)
						return null;
					FeatureMap map = (FeatureMap) target;
					return map.list(f);
				}

				public BackendType getType (BackendTypesystem ts) {
					return ts.findType(f);
				}
				
			}, this);
			
			register (new QualifiedName ("add"), new AbstractFunction (null, Arrays.asList (this, new EFeatureType(XsdTypesystem.EFEATURE, ts), ObjectType.INSTANCE), BooleanType.INSTANCE, false) {

				public Object invoke(ExecutionContext ctx, Object[] params) {
					FeatureMap map = (FeatureMap) params[0];
					EStructuralFeature f = (EStructuralFeature) params[1];
					return map.add(f, params[2]);
				}
				
			});
			
			register (new QualifiedName ("set"), new AbstractFunction (null, Arrays.asList(this, new EFeatureType(XsdTypesystem.EFEATURE, ts), ObjectType.INSTANCE), VoidType.INSTANCE, false) {

				public Object invoke(ExecutionContext ctx, Object[] params) {
					FeatureMap map = (FeatureMap) params[0];
					EStructuralFeature f = (EStructuralFeature) params[1];
					map.set (f, params[2]);
					return null;
				}
				
			});
			
			register (new QualifiedName ("unset"), new AbstractFunction (null, Arrays.asList(this, new EFeatureType(XsdTypesystem.EFEATURE, ts)), VoidType.INSTANCE, false) {

				public Object invoke(ExecutionContext ctx, Object[] params) {
					FeatureMap map = (FeatureMap) params[0];
					EStructuralFeature f = (EStructuralFeature) params[1];
					map.unset (f);
					return null;
				}
				
			});
			
			register (new QualifiedName ("isSet"), new AbstractFunction (null, Arrays.asList(this, new EFeatureType(XsdTypesystem.EFEATURE, ts)), BooleanType.INSTANCE, false) {

				public Object invoke(ExecutionContext ctx, Object[] params) {
					FeatureMap map = (FeatureMap) params[0];
					EStructuralFeature f = (EStructuralFeature) params[1];
					return map.isSet (f);
				}
				
			});
			
			register (new QualifiedName ("list"), new AbstractFunction (null, Arrays.asList(this), ListType.INSTANCE, false) {

				public Object invoke(ExecutionContext ctx, Object[] params) {
					FeatureMap map = (FeatureMap) params[0];
					EStructuralFeature f = (EStructuralFeature) params[1];
					return map.list(f);
				}
				
			});
			
			register (new QualifiedName("addAll"), new AbstractFunction (null, Arrays.asList(this), BooleanType.INSTANCE, false) {

				public Object invoke(ExecutionContext ctx, Object[] params) {
					FeatureMap map = (FeatureMap) params[0];
					EStructuralFeature f = (EStructuralFeature) params[1];
					Collection aList = (Collection) params[2];
					return map.addAll(aList);
				}
				
			});
			
			register (new QualifiedName ("addFrom"), new AbstractFunction (null, Arrays.asList(this, EObjectType.INSTANCE), VoidType.INSTANCE, false) {

				public Object invoke(ExecutionContext ctx, Object[] params) {
					FeatureMap map = (FeatureMap) params[0];
					EStructuralFeature.Setting s = (EStructuralFeature.Setting) map;
					EObject o = (EObject) params[1];
					Validator v = FeatureMapUtil.getValidator(s.getEObject()
							.eClass(), s.getEStructuralFeature());
					for (EStructuralFeature f : o.eClass()
							.getEAllStructuralFeatures())
						if (!f.isDerived() && o.eIsSet(f)) {
							Object val = o.eGet(f);
							if (val instanceof FeatureMap) {
								for (FeatureMap.Entry e : new ArrayList<FeatureMap.Entry>(
										(FeatureMap) val))
									if (v.isValid(e.getEStructuralFeature()))
										add(map, e.getEStructuralFeature(), e
												.getValue());
							} else if (v.isValid(f))
								add(map, f, val);
						}
					return null;
				}

				@SuppressWarnings("unchecked")
				private void add(FeatureMap map, EStructuralFeature f, Object val) {
					if (f.isMany() && val instanceof Collection) {
						map.addAll(f, (Collection) val);
					} else
						map.add(f, val);
				}

			});
		}
	}

	@SuppressWarnings("unchecked")
	protected List<EStructuralFeature> getMapFeatures() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (getClass() != other.getClass())
            return false;
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2938.java