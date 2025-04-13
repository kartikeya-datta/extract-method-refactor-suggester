error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12377.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12377.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12377.java
text:
```scala
private final C@@ompoundVariable masterFunction = new CompoundVariable();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.engine.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.MultiProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Perfom replacement of ${variable} references.
 */
public class ValueReplacer {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private CompoundVariable masterFunction = new CompoundVariable();

    private Map variables = new HashMap();

    public ValueReplacer() {
    }

    public ValueReplacer(TestPlan tp) {
        setUserDefinedVariables(tp.getUserDefinedVariables());
    }

    boolean containsKey(String k){
        return variables.containsKey(k);
    }
    public void setUserDefinedVariables(Map variables) {
        this.variables = variables;
    }

    public void replaceValues(TestElement el) throws InvalidVariableException {
        Collection newProps = replaceValues(el.propertyIterator(), new ReplaceStringWithFunctions(masterFunction,
                variables));
        setProperties(el, newProps);
    }

    private void setProperties(TestElement el, Collection newProps) {
        Iterator iter = newProps.iterator();
        el.clear();
        while (iter.hasNext()) {
            el.setProperty((JMeterProperty) iter.next());
        }
    }

    public void reverseReplace(TestElement el) throws InvalidVariableException {
        Collection newProps = replaceValues(el.propertyIterator(), new ReplaceFunctionsWithStrings(masterFunction,
                variables));
        setProperties(el, newProps);
    }

    public void reverseReplace(TestElement el, boolean regexMatch) throws InvalidVariableException {
        Collection newProps = replaceValues(el.propertyIterator(), new ReplaceFunctionsWithStrings(masterFunction,
                variables, regexMatch));
        setProperties(el, newProps);
    }

    public void undoReverseReplace(TestElement el) throws InvalidVariableException {
        Collection newProps = replaceValues(el.propertyIterator(), new UndoVariableReplacement(masterFunction,
                variables));
        setProperties(el, newProps);
    }

    public void addVariable(String name, String value) {
        variables.put(name, value);
    }

    /**
     * Add all the given variables to this replacer's variables map.
     *
     * @param vars
     *            A map of variable name-value pairs (String-to-String).
     */
    public void addVariables(Map vars) {
        variables.putAll(vars);
    }

    private Collection replaceValues(PropertyIterator iter, ValueTransformer transform) throws InvalidVariableException {
        List props = new LinkedList();
        while (iter.hasNext()) {
            JMeterProperty val = iter.next();
            if (log.isDebugEnabled()) {
                log.debug("About to replace in property of type: " + val.getClass() + ": " + val);
            }
            if (val instanceof StringProperty) {
                // Must not convert TestElement.gui_class etc
                if (!val.getName().equals(TestElement.GUI_CLASS) &&
                        !val.getName().equals(TestElement.TEST_CLASS)) {
                    val = transform.transformValue(val);
                    if (log.isDebugEnabled()) {
                        log.debug("Replacement result: " + val);
                    }
                }
            } else if (val instanceof MultiProperty) {
                MultiProperty multiVal = (MultiProperty) val;
                Collection newValues = replaceValues(multiVal.iterator(), transform);
                multiVal.clear();
                Iterator propIter = newValues.iterator();
                while (propIter.hasNext()) {
                    multiVal.addProperty((JMeterProperty) propIter.next());
                }
                if (log.isDebugEnabled()) {
                    log.debug("Replacement result: " + multiVal);
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Won't replace " + val);
                }
            }
            props.add(val);
        }
        return props;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12377.java