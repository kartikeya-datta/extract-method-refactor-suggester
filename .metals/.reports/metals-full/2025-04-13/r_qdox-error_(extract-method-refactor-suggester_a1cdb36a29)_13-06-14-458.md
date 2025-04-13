error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1649.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1649.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1060
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1649.java
text:
```scala
class SocketBindingTransformers {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

p@@ackage org.jboss.as.domain.controller.transformers;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;

import java.util.HashMap;
import java.util.Map;

import org.jboss.as.controller.resource.AbstractSocketBindingResourceDefinition;
import org.jboss.as.controller.transform.AttributeTransformationRequirementChecker;
import org.jboss.as.controller.transform.RejectExpressionValuesTransformer;
import org.jboss.as.controller.transform.ResourceTransformer;
import org.jboss.as.controller.transform.TransformersSubRegistration;
import org.jboss.as.domain.controller.resources.SocketBindingResourceDefinition;

/**
 * The older versions of the model do not allow expressions for some socket-binding resource attributes.
 * Reject the attributes if they contain an expression.
 *
 * @author Brian Stansberry (c) 2012 Red Hat Inc.
 */
public class SocketBindingTransformers {

    static TransformersSubRegistration registerTransformers(TransformersSubRegistration parent) {

        Map<String, AttributeTransformationRequirementChecker> fieldCheckers = new HashMap<String, AttributeTransformationRequirementChecker>();
        fieldCheckers.put(AbstractSocketBindingResourceDefinition.CLIENT_MAPPING_SOURCE_NETWORK.getName(), AttributeTransformationRequirementChecker.SIMPLE_EXPRESSIONS);
        fieldCheckers.put(AbstractSocketBindingResourceDefinition.CLIENT_MAPPING_DESTINATION_ADDRESS.getName(), AttributeTransformationRequirementChecker.SIMPLE_EXPRESSIONS);
        fieldCheckers.put(AbstractSocketBindingResourceDefinition.CLIENT_MAPPING_DESTINATION_PORT.getName(), AttributeTransformationRequirementChecker.SIMPLE_EXPRESSIONS);

        AttributeTransformationRequirementChecker elementChecker = new AttributeTransformationRequirementChecker.ObjectFieldsAttributeTransformationRequirementChecker(fieldCheckers);
        AttributeTransformationRequirementChecker attrChecker = new AttributeTransformationRequirementChecker.ListAttributeTransformationRequirementChecker(elementChecker);
        RejectExpressionValuesTransformer rejectExpression = new RejectExpressionValuesTransformer(SocketBindingResourceDefinition.CLIENT_MAPPINGS.getName(), attrChecker);
        TransformersSubRegistration reg = parent.registerSubResource(SocketBindingResourceDefinition.PATH, (ResourceTransformer) rejectExpression);

        reg.registerOperationTransformer(ADD, rejectExpression);
        reg.registerOperationTransformer(WRITE_ATTRIBUTE_OPERATION, rejectExpression.getWriteAttributeTransformer());

        return reg;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1649.java