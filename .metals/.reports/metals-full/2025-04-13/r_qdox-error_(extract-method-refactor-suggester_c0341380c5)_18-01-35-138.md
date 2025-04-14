error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7879.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7879.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7879.java
text:
```scala
public S@@tringBuilder asValue(AliasContext q) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.persistence.criteria;

import java.util.Collection;

import javax.persistence.criteria.ParameterExpression;

import org.apache.openjpa.kernel.exps.ExpressionFactory;
import org.apache.openjpa.kernel.exps.Value;
import org.apache.openjpa.persistence.meta.MetamodelImpl;
import org.apache.openjpa.util.InternalException;

/**
 * Parameter of a criteria query.
 * <br>
 * A parameter in CriteriaQuery is always a named parameter but can be constructed with a null name.
 * Positional parameters are not allowed in CriteraQuery.
 * <br> 
 * 
 * @author Pinaki Poddar
 * @author Fay wang
 * 
 * @param <T> the type of value held by this parameter.
 */
public class ParameterExpressionImpl<T> extends ExpressionImpl<T> 
    implements ParameterExpression<T> {
    private String _name;
    private int _index = 0; // index of the parameter as seen by the kernel, not position
	
	/**
	 * Construct a Parameter of given expected value type and name.
	 * 
	 * @param cls expected value type
	 * @param name name of the parameter which can be null.
	 */
    public ParameterExpressionImpl(Class<T> cls, String name) {
        super(cls);
        _name = name;
    }

    /**
     * Gets the name of this parameter.
     * The name can be null.
     */
    public final String getName() {
        return _name;
    }
    
    /**
     * Raises an internal exception because parameters of CriteriaQuery
     * are not positional. 
     */
    public final Integer getPosition() {
        throw new InternalException(this + " must not be asked for its position");
    }
    
    void setIndex(int index) {
        _index = index;
    }
    
    public String toString() {
        StringBuilder buf = new StringBuilder("ParameterExpression");
        buf.append("<" + getJavaType().getSimpleName() + ">");
        if (_name != null)
            buf.append("('"+ _name +"')"); 

        return buf.toString();
    }
    
    @Override
    public Value toValue(ExpressionFactory factory, MetamodelImpl model, CriteriaQueryImpl<?> q) {
        Class<?> clzz = getJavaType();
        Object paramKey = _name == null ? _index : _name;
        boolean isCollectionValued  = Collection.class.isAssignableFrom(clzz);
        org.apache.openjpa.kernel.exps.Parameter param = isCollectionValued 
            ? factory.newCollectionValuedParameter(paramKey, clzz) 
            : factory.newParameter(paramKey, clzz);
        param.setIndex(_index);
        
        return param;
    }   
    
    @Override
    public StringBuilder asValue(CriteriaQueryImpl<?> q) {
        return Expressions.asValue(q, ":", _name == null ? "param" : _name);
    }
    
    public Class<T> getParameterType() {
        return getJavaType();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7879.java