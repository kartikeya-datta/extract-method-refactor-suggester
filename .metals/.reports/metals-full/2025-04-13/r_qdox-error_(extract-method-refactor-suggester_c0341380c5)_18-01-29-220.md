error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7805.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7805.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7805.java
text:
```scala
i@@f (constantPoolEntry.getKind() != IConstantPoolConstant.CONSTANT_Utf8) {

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.util;

import org.eclipse.jdt.core.util.ClassFormatException;
import org.eclipse.jdt.core.util.IAnnotation;
import org.eclipse.jdt.core.util.IAnnotationComponentValue;
import org.eclipse.jdt.core.util.IConstantPool;
import org.eclipse.jdt.core.util.IConstantPoolConstant;
import org.eclipse.jdt.core.util.IConstantPoolEntry;

/**
 * Default implementation of IAnnotationComponent
 */
public class AnnotationComponentValue extends ClassFileStruct implements IAnnotationComponentValue {
	private IAnnotationComponentValue[] annotationComponentValues;
	private IAnnotation attributeValue;
	private IConstantPoolEntry classFileInfo;
	private int classFileInfoIndex;
	private IConstantPoolEntry constantValue;
	private int constantValueIndex;
	private int enumConstantTypeNameIndex;
	private int enumConstantNameIndex;
	private IConstantPoolEntry enumConstantTypeName;
	private IConstantPoolEntry enumConstantName;

	private int readOffset;
	private int tag;
	private int valuesNumber;
	
	public AnnotationComponentValue(
			byte[] classFileBytes,
			IConstantPool constantPool,
			int offset) throws ClassFormatException {
		final int t = u1At(classFileBytes, 0, offset);
		this.tag = t;
		this.readOffset = 1;
		switch(t) {
			case 'B' :
			case 'C' :
			case 'D' :
			case 'F' :
			case 'I' :
			case 'J' :
			case 'S' :
			case 'Z' :
			case 's' :
				final int constantIndex = this.u2At(classFileBytes, this.readOffset, offset);
				this.constantValueIndex = constantIndex;
				if (constantIndex != 0) {
					IConstantPoolEntry constantPoolEntry = constantPool.decodeEntry(constantIndex);
					switch(constantPoolEntry.getKind()) {
						case IConstantPoolConstant.CONSTANT_Long :
						case IConstantPoolConstant.CONSTANT_Float :
						case IConstantPoolConstant.CONSTANT_Double :
						case IConstantPoolConstant.CONSTANT_Integer :
						case IConstantPoolConstant.CONSTANT_Utf8 :
							break;
						default :
							throw new ClassFormatException(ClassFormatException.INVALID_CONSTANT_POOL_ENTRY);
					}
					this.constantValue = constantPoolEntry;
				}
				this.readOffset += 2;
				break;
			case 'e' :
				int index = this.u2At(classFileBytes, this.readOffset, offset);
				this.enumConstantTypeNameIndex = index;
				if (index != 0) {
					IConstantPoolEntry constantPoolEntry = constantPool.decodeEntry(index);
					if (constantPoolEntry.getKind() != IConstantPoolConstant.CONSTANT_Utf8) {
						throw new ClassFormatException(ClassFormatException.INVALID_CONSTANT_POOL_ENTRY);
					}
					this.enumConstantTypeName = constantPoolEntry;
				}
				this.readOffset += 2;
				index = this.u2At(classFileBytes, this.readOffset, offset);
				this.enumConstantNameIndex = index;
				if (index != 0) {
					IConstantPoolEntry constantPoolEntry = constantPool.decodeEntry(index);
					if (constantPoolEntry.getKind() != IConstantPoolConstant.CONSTANT_Utf8) {
						throw new ClassFormatException(ClassFormatException.INVALID_CONSTANT_POOL_ENTRY);
					}
					this.enumConstantName = constantPoolEntry;
				}
				this.readOffset += 2;
				break;
			case 'c' :
				final int classFileIndex = this.u2At(classFileBytes, this.readOffset, offset);
				this.classFileInfoIndex = classFileIndex;
				if (classFileIndex != 0) {
					IConstantPoolEntry constantPoolEntry = constantPool.decodeEntry(classFileIndex);
					if (constantPoolEntry.getKind() != IConstantPoolConstant.CONSTANT_Class) {
						throw new ClassFormatException(ClassFormatException.INVALID_CONSTANT_POOL_ENTRY);
					}
					this.classFileInfo = constantPoolEntry;
				}
				this.readOffset += 2;
				break;
			case '@' :
				Annotation annotation = new Annotation(classFileBytes, constantPool, this.readOffset + offset);
				this.attributeValue = annotation;
				this.readOffset += annotation.sizeInBytes();
				break;
			case '[' :
				final int numberOfValues = this.u2At(classFileBytes, this.readOffset, offset);
				this.valuesNumber = numberOfValues;
				if (numberOfValues != 0) {
					this.readOffset += 2;
					this.annotationComponentValues = new IAnnotationComponentValue[numberOfValues];
					for (int i = 0; i < numberOfValues; i++) {
						AnnotationComponentValue value = new AnnotationComponentValue(classFileBytes, constantPool, offset + readOffset);
						this.annotationComponentValues[i] = value;
						this.readOffset += value.sizeInBytes();
					}
				}
				break;
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.util.IAnnotationComponentValue#getAnnotationComponentValues()
	 */
	public IAnnotationComponentValue[] getAnnotationComponentValues() {
		return this.annotationComponentValues;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.util.IAnnotationComponentValue#getAttributeValue()
	 */
	public IAnnotation getAttributeValue() {
		return this.attributeValue;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.util.IAnnotationComponentValue#getClassInfo()
	 */
	public IConstantPoolEntry getClassInfo() {
		return this.classFileInfo;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.util.IAnnotationComponentValue#getClassInfoIndex()
	 */
	public int getClassInfoIndex() {
		return this.classFileInfoIndex;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.util.IAnnotationComponentValue#getConstantValue()
	 */
	public IConstantPoolEntry getConstantValue() {
		return this.constantValue;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.util.IAnnotationComponentValue#getConstantValueIndex()
	 */
	public int getConstantValueIndex() {
		return this.constantValueIndex;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.util.IAnnotationComponentValue#getEnumConstantName()
	 */
	public IConstantPoolEntry getEnumConstantName() {
		return this.enumConstantName;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.util.IAnnotationComponentValue#getEnumConstantNameIndex()
	 */
	public int getEnumConstantNameIndex() {
		return this.enumConstantNameIndex;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.util.IAnnotationComponentValue#getEnumConstantTypeName()
	 */
	public IConstantPoolEntry getEnumConstantTypeName() {
		return this.enumConstantTypeName;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.util.IAnnotationComponentValue#getEnumConstantTypeNameIndex()
	 */
	public int getEnumConstantTypeNameIndex() {
		return enumConstantTypeNameIndex;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.util.IAnnotationComponentValue#getTag()
	 */
	public int getTag() {
		return this.tag;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.util.IAnnotationComponentValue#getValuesNumber()
	 */
	public int getValuesNumber() {
		return this.valuesNumber;
	}
	
	int sizeInBytes() {
		return this.readOffset;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7805.java