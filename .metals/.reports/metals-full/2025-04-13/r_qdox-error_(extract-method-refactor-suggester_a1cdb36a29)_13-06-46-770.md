error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/149.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/149.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/149.java
text:
```scala
r@@eturn sourceCollection;

/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.core.convert.support;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.TypeDescriptor;

class CollectionGenericConverter implements GenericConverter {

	private GenericConversionService conversionService;

	public CollectionGenericConverter(GenericConversionService conversionService) {
		this.conversionService = conversionService;
	}

	public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return sourceType.isArray() || targetType.isArray() || sourceType.isCollection() || targetType.isCollection();	
	}
	
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (isArrayToArray(sourceType, targetType)) {
			return convertArrayToArray(source, sourceType, targetType);
		} else if (isArrayToCollection(sourceType, targetType)) {
			return convertArrayToCollection(source, sourceType, targetType);
		} else if (isCollectionToCollection(sourceType, targetType)) {
			return convertCollectionToCollection(source, sourceType, targetType);
		} else if (isCollectionToArray(sourceType, targetType)) {
			return convertCollectionToArray(source, sourceType, targetType); 
		} else if (isArrayToObject(sourceType, targetType)) {
			return convertArrayToObject(source, sourceType, targetType);			
		} else if (isObjectToArray(sourceType, targetType)) {
			return convertObjectToArray(source, sourceType, targetType);
		} else if (isCollectionToObject(sourceType, targetType))  {
			return convertCollectionToObject(source, sourceType, targetType);
		} else {
			return convertObjectToCollection(source, sourceType, targetType);
		}
	}
	
	private boolean isArrayToArray(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return sourceType.isArray() && targetType.isArray();
	}

	private Object convertArrayToArray(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (sourceType.isAssignableTo(targetType)) {
			return source;
		}
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		Object target = Array.newInstance(targetElementType.getType(), Array.getLength(source));
		GenericConverter converter = conversionService.getConverter(sourceElementType, targetElementType);
		for (int i = 0; i < Array.getLength(target); i++) {
			Array.set(target, i, converter.convert(Array.get(source, i), sourceElementType, targetElementType));
		}
		return target;		
	}

	private boolean isArrayToCollection(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return sourceType.isArray() && targetType.isCollection();
	}

	private Object convertArrayToCollection(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		int length = Array.getLength(source);
		Collection collection = CollectionFactory.createCollection(targetType.getType(), length);
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		if (targetElementType == TypeDescriptor.NULL || sourceElementType.isAssignableTo(targetElementType)) {
			for (int i = 0; i < length; i++) {
				collection.add(Array.get(source, i));
			}
		} else {
			GenericConverter converter = conversionService.getConverter(sourceElementType, targetElementType);
			for (int i = 0; i < length; i++) {
				collection.add(converter.convert(Array.get(source, i), sourceElementType, targetElementType));
			}
		}
		return collection;
	}

	private boolean isCollectionToArray(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return sourceType.isCollection() && targetType.isArray();
	}

	private Object convertCollectionToArray(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		Collection sourceCollection = (Collection) source;
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		if (sourceElementType == TypeDescriptor.NULL) {
			sourceElementType = getElementType(sourceCollection);
		}
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		Object array = Array.newInstance(targetElementType.getType(), sourceCollection.size());
		int i = 0;		
		if (sourceElementType == TypeDescriptor.NULL || sourceElementType.isAssignableTo(targetElementType)) {
			for (Iterator it = sourceCollection.iterator(); it.hasNext(); i++) {
				Array.set(array, i, it.next());
			}
		} else {
			GenericConverter converter = conversionService.getConverter(sourceElementType, targetElementType);
			for (Iterator it = sourceCollection.iterator(); it.hasNext(); i++) {
				Array.set(array, i, converter.convert(it.next(), sourceElementType, targetElementType));
			}
		}
		return array;
	}
	
	private boolean isCollectionToCollection(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return sourceType.isCollection() && targetType.isCollection();
	}

	public Object convertCollectionToCollection(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		Collection sourceCollection = (Collection) source;
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		if (sourceElementType == TypeDescriptor.NULL) {
			sourceElementType = getElementType(sourceCollection);
		}
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		if (sourceElementType == TypeDescriptor.NULL || sourceElementType.isAssignableTo(targetElementType)) {
			if (sourceType.isAssignableTo(targetType)) {
				return source;
			} else {
				Collection targetCollection = CollectionFactory.createCollection(targetType.getType(), sourceCollection.size());
				targetCollection.addAll(sourceCollection);
				return targetCollection;
			}
		}
		Collection targetCollection = CollectionFactory.createCollection(targetType.getType(), sourceCollection.size());
		GenericConverter converter = conversionService.getConverter(sourceElementType, targetElementType);
		for (Object element : sourceCollection) {
			targetCollection.add(converter.convert(element, sourceElementType, targetElementType));
		}
		return targetCollection;
	}
	
	private TypeDescriptor getElementType(Collection collection) {
		for (Object element : collection) {
			if (element != null) {
				return TypeDescriptor.valueOf(element.getClass());
			}
		}
		return TypeDescriptor.NULL;
	}

	private boolean isArrayToObject(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return sourceType.isArray();
	}

	private Object convertArrayToObject(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private boolean isObjectToArray(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return targetType.isArray();
	}

	private Object convertObjectToArray(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	private boolean isCollectionToObject(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return sourceType.isCollection();
	}

	private Object convertCollectionToObject(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private Object convertObjectToCollection(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		throw new UnsupportedOperationException("Not yet implemented");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/149.java