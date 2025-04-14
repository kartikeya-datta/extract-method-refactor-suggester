error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5514.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5514.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5514.java
text:
```scala
O@@bject[] arguments = { Long.valueOf(resultSize), msg, Long.valueOf(getAllowedSize()) };

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

package org.apache.jmeter.assertions;

import java.io.Serializable;
import java.text.MessageFormat;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractScopedAssertion;
import org.apache.jmeter.testelement.property.IntegerProperty;
import org.apache.jmeter.testelement.property.LongProperty;
import org.apache.jmeter.util.JMeterUtils;

//@see org.apache.jmeter.assertions.SizeAssertionTest for unit tests

/**
 * Checks if the results of a Sample matches a particular size.
 * 
 */
public class SizeAssertion extends AbstractScopedAssertion implements Serializable, Assertion {

    private static final long serialVersionUID = 233L;

    // * Static int to signify the type of logical comparitor to assert
    public final static int EQUAL = 1;

    public final static int NOTEQUAL = 2;

    public final static int GREATERTHAN = 3;

    public final static int LESSTHAN = 4;

    public final static int GREATERTHANEQUAL = 5;

    public final static int LESSTHANEQUAL = 6;

    /** Key for storing assertion-informations in the jmx-file. */
    private static final String SIZE_KEY = "SizeAssertion.size"; // $NON-NLS-1$

    private static final String OPERATOR_KEY = "SizeAssertion.operator"; // $NON-NLS-1$

    /**
     * Returns the result of the Assertion. 
     * Here it checks the Sample responseData length.
     */
    public AssertionResult getResult(SampleResult response) {
        AssertionResult result = new AssertionResult(getName());
        result.setFailure(false);
        long resultSize=0;
        if (isScopeVariable()){
            String variableName = getVariableName();
            String value = getThreadContext().getVariables().get(variableName);
            try {
                resultSize = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                result.setFailure(true);
                result.setFailureMessage("Error parsing variable name: "+variableName+" value: "+value);
                return result;
            }
        } else {
            resultSize = response.getBytes();
        }
        // is the Sample the correct size?
        final String msg = compareSize(resultSize);
        if (msg.length() > 0) {
            result.setFailure(true);
            Object[] arguments = { new Long(resultSize), msg, new Long(getAllowedSize()) };
            String message = MessageFormat.format(JMeterUtils.getResString("size_assertion_failure"), arguments); //$NON-NLS-1$
            result.setFailureMessage(message);
        }
        return result;
    }

    /**
     * Returns the size in bytes to be asserted.
     */
    public long getAllowedSize() {
        return getPropertyAsLong(SIZE_KEY);
    }

    /***************************************************************************
     * set the Operator
     **************************************************************************/
    public void setCompOper(int operator) {
        setProperty(new IntegerProperty(OPERATOR_KEY, operator));

    }

    /**
     * Returns the operator to be asserted. EQUAL = 1, NOTEQUAL = 2 GREATERTHAN =
     * 3,LESSTHAN = 4,GREATERTHANEQUAL = 5,LESSTHANEQUAL = 6
     */

    public int getCompOper() {
        return getPropertyAsInt(OPERATOR_KEY);
    }

    /**
     * Set the size that shall be asserted.
     * 
     * @param size -
     *            a number of bytes. Is not allowed to be negative. Use
     *            Long.MAX_VALUE to indicate illegal or empty inputs. This will
     *            result in not checking the assertion.
     * 
     * @throws IllegalArgumentException
     *             If <code>size</code> is negative.
     */
    public void setAllowedSize(long size) throws IllegalArgumentException {
        if (size < 0L) {
            throw new IllegalArgumentException(JMeterUtils.getResString("argument_must_not_be_negative")); //$NON-NLS-1$
        }
        if (size == Long.MAX_VALUE) {
            setProperty(new LongProperty(SIZE_KEY, 0));
        } else {
            setProperty(new LongProperty(SIZE_KEY, size));
        }
    }

    /**
     * Compares the the size of a return result to the set allowed size using a
     * logical comparator set in setLogicalComparator().
     * 
     * Possible values are: equal, not equal, greater than, less than, greater
     * than eqaul, less than equal, .
     * 
     */
    private String compareSize(long resultSize) {
        String comparatorErrorMessage;
        boolean result = false;
        int comp = getCompOper();
        switch (comp) {
        case EQUAL:
            result = (resultSize == getAllowedSize());
            comparatorErrorMessage = JMeterUtils.getResString("size_assertion_comparator_error_equal"); //$NON-NLS-1$
            break;
        case NOTEQUAL:
            result = (resultSize != getAllowedSize());
            comparatorErrorMessage = JMeterUtils.getResString("size_assertion_comparator_error_notequal"); //$NON-NLS-1$
            break;
        case GREATERTHAN:
            result = (resultSize > getAllowedSize());
            comparatorErrorMessage = JMeterUtils.getResString("size_assertion_comparator_error_greater"); //$NON-NLS-1$
            break;
        case LESSTHAN:
            result = (resultSize < getAllowedSize());
            comparatorErrorMessage = JMeterUtils.getResString("size_assertion_comparator_error_less"); //$NON-NLS-1$
            break;
        case GREATERTHANEQUAL:
            result = (resultSize >= getAllowedSize());
            comparatorErrorMessage = JMeterUtils.getResString("size_assertion_comparator_error_greaterequal"); //$NON-NLS-1$
            break;
        case LESSTHANEQUAL:
            result = (resultSize <= getAllowedSize());
            comparatorErrorMessage = JMeterUtils.getResString("size_assertion_comparator_error_lessequal"); //$NON-NLS-1$
            break;
        default:
            result = false;
            comparatorErrorMessage = "ERROR - invalid condition";
            break;
        }
        return result ? "" : comparatorErrorMessage;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5514.java