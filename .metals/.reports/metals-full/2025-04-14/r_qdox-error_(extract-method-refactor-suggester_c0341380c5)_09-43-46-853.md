error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5911.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5911.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5911.java
text:
```scala
r@@eturn new Character((char)ch.getBytes()).toString();

/* *******************************************************************
 * Copyright (c) 2004 IBM
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Andy Clement -     initial implementation {date}
 * ******************************************************************/

package org.aspectj.apache.bcel.classfile.annotation;

import java.io.DataOutputStream;
import java.io.IOException;

import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.classfile.ConstantDouble;
import org.aspectj.apache.bcel.classfile.ConstantFloat;
import org.aspectj.apache.bcel.classfile.ConstantInteger;
import org.aspectj.apache.bcel.classfile.ConstantLong;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.ConstantUtf8;

/**
 * An element value representing a primitive or string value.
 */
public class SimpleElementValue extends ElementValue {	
	
	// For primitive types and string type, this points to the value entry in the cpool
	// For 'class' this points to the class entry in the cpool
	private int idx;
	
    public SimpleElementValue(int type,int idx,ConstantPool cpool) {
    	super(type,cpool);
    	this.idx = idx;
    }
    
    public int getIndex() {
    	return idx;
    }
    
    
    public String getValueString() {
    	if (type != STRING) 
    		throw new RuntimeException("Dont call getValueString() on a non STRING ElementValue");
		ConstantUtf8 c = (ConstantUtf8)cpool.getConstant(idx,Constants.CONSTANT_Utf8);
		return c.getBytes();
    }
    
    public int getValueInt() {
    	if (type != PRIMITIVE_INT) 
    		throw new RuntimeException("Dont call getValueString() on a non STRING ElementValue");
		ConstantInteger c = (ConstantInteger)cpool.getConstant(idx,Constants.CONSTANT_Integer);
		return c.getBytes();
    }
    
    public byte getValueByte() {
    	if (type != PRIMITIVE_BYTE) 
    		throw new RuntimeException("Dont call getValueByte() on a non BYTE ElementValue");
		ConstantInteger c = (ConstantInteger)cpool.getConstant(idx,Constants.CONSTANT_Integer);
		return (byte)c.getBytes();
    }
    
    public char getValueChar() {
    	if (type != PRIMITIVE_CHAR) 
    		throw new RuntimeException("Dont call getValueChar() on a non CHAR ElementValue");
		ConstantInteger c = (ConstantInteger)cpool.getConstant(idx,Constants.CONSTANT_Integer);
		return (char)c.getBytes();
    }
    
    public long getValueLong() {
    	if (type != PRIMITIVE_LONG) 
    		throw new RuntimeException("Dont call getValueLong() on a non LONG ElementValue");
    	ConstantLong j = (ConstantLong)cpool.getConstant(idx);
    	return j.getBytes();
    }
    
    public float getValueFloat() {
    	if (type != PRIMITIVE_FLOAT)
    		throw new RuntimeException("Dont call getValueFloat() on a non FLOAT ElementValue");
    	ConstantFloat f = (ConstantFloat)cpool.getConstant(idx);
    	return f.getBytes();
    }


    public double getValueDouble() {
    	if (type != PRIMITIVE_DOUBLE)
    		throw new RuntimeException("Dont call getValueDouble() on a non DOUBLE ElementValue");
    	ConstantDouble d = (ConstantDouble)cpool.getConstant(idx);
    	return d.getBytes();
    }
    
    public boolean getValueBoolean() {
    	if (type != PRIMITIVE_BOOLEAN)
    		throw new RuntimeException("Dont call getValueBoolean() on a non BOOLEAN ElementValue");
    	ConstantInteger bo = (ConstantInteger)cpool.getConstant(idx);
    	return (bo.getBytes()!=0);
    }
    
    public short getValueShort() {
    	if (type != PRIMITIVE_SHORT)
    		throw new RuntimeException("Dont call getValueShort() on a non SHORT ElementValue");
    	ConstantInteger s = (ConstantInteger)cpool.getConstant(idx);
    	return (short)s.getBytes();
    }
    
    public String toString() {
    	return stringifyValue();
    }
    
    // Whatever kind of value it is, return it as a string
    public String stringifyValue() {
    	switch (type) {
    	  case PRIMITIVE_INT:
    	  	ConstantInteger c = (ConstantInteger)cpool.getConstant(idx,Constants.CONSTANT_Integer);
    		return Integer.toString(c.getBytes());
    	  case PRIMITIVE_LONG:
    	  	ConstantLong j = (ConstantLong)cpool.getConstant(idx,Constants.CONSTANT_Long);
    		return Long.toString(j.getBytes());
    	  case PRIMITIVE_DOUBLE:
    	  	ConstantDouble d = (ConstantDouble)cpool.getConstant(idx,Constants.CONSTANT_Double);
    		return Double.toString(d.getBytes());
    	  case PRIMITIVE_FLOAT:
    	  	ConstantFloat f = (ConstantFloat)cpool.getConstant(idx,Constants.CONSTANT_Float);
    		return Float.toString(f.getBytes());
    	  case PRIMITIVE_SHORT:
    		ConstantInteger s = (ConstantInteger)cpool.getConstant(idx,Constants.CONSTANT_Integer);
    		return Integer.toString(s.getBytes());
    	  case PRIMITIVE_BYTE:
    		ConstantInteger b = (ConstantInteger)cpool.getConstant(idx,Constants.CONSTANT_Integer);
    		return Integer.toString(b.getBytes());
    	  case PRIMITIVE_CHAR:
    		ConstantInteger ch = (ConstantInteger)cpool.getConstant(idx,Constants.CONSTANT_Integer);
    		return Integer.toString(ch.getBytes());
    	  case PRIMITIVE_BOOLEAN:
    		ConstantInteger bo = (ConstantInteger)cpool.getConstant(idx,Constants.CONSTANT_Integer);
    		if (bo.getBytes() == 0) return "false";
    		if (bo.getBytes() != 0) return "true";
    	  case STRING:
    		ConstantUtf8 cu8 = (ConstantUtf8)cpool.getConstant(idx,Constants.CONSTANT_Utf8);
    		return cu8.getBytes();
    		
 		  default:
   			throw new RuntimeException("SimpleElementValue class does not know how to stringify type "+type);
    	}
    }
    
    public void dump(DataOutputStream dos) throws IOException {
    	dos.writeByte(type); // u1 kind of value
    	switch (type) {
    		case PRIMITIVE_INT: 
    		case PRIMITIVE_BYTE:
    		case PRIMITIVE_CHAR:
    		case PRIMITIVE_FLOAT:
    		case PRIMITIVE_LONG:
    		case PRIMITIVE_BOOLEAN:
    		case PRIMITIVE_SHORT:
    		case PRIMITIVE_DOUBLE:
    		case STRING:
    			dos.writeShort(idx);
    			break;
   			default:
   				throw new RuntimeException("SimpleElementValue doesnt know how to write out type "+type);
    	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5911.java