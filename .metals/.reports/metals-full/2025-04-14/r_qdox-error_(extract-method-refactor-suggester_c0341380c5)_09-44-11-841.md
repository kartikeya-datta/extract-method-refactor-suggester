error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14694.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14694.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14694.java
text:
```scala
i@@f(current[0] != '$' && current[0] != ',' && current[0] != '\\')

/*
 * Created on Jul 25, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.apache.jmeter.engine.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

import org.apache.jmeter.functions.Function;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author ano ano
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
class FunctionParser
{
    Logger log = LoggingManager.getLoggerForClass();
    
    /**
     * Compile a general string into a list of elements for a CompoundVariable.
     * @param value
     * @return
     * @throws InvalidVariableException
     */
    LinkedList compileString(String value) throws InvalidVariableException
    {
        StringReader reader = new StringReader(value);
        LinkedList result = new LinkedList();
        StringBuffer buffer = new StringBuffer();
        char previous = ' ';
        char[] current = new char[1];
       try
        {
             while(reader.read(current) == 1)
                {
                    if(current[0] == '\\')
                    {
                        previous = current[0];
                        if(reader.read(current) == 0)
                        {
                            break;
                        }
                        if(current[0] != '$' && current[0] != ',')
                        {
                            buffer.append(previous);
                        }
                        previous = ' ';
                        buffer.append(current[0]);
                        continue;
                    }
                    else if(current[0] == '{' && previous == '$')
                    {
                        buffer.deleteCharAt(buffer.length()-1);
                        if(buffer.length() > 0)
                        {
                            result.add(buffer.toString());
                            buffer.setLength(0);
                        }
                        result.add(makeFunction(reader));
                        previous = ' ';
                    }
                    else
                    {
                        buffer.append(current[0]);
                        previous = current[0];
                    }
                }
                if(buffer.length() > 0)
                {
                    result.add(buffer.toString());
                }
        }
        catch (IOException e)
        {
            log.error("Error parsing function: " + value,e);
            result.clear();
            result.add(value);
        }
        if(result.size() == 0)
        {
            result.add("");
        }
        return result;
    }
    
    /**
     * Compile a string into a function or SimpleVariable.
     * @param reader
     * @return
     * @throws InvalidVariableException
     */
    Object makeFunction(StringReader reader) throws InvalidVariableException
    {
        char[] current = new char[1];
        char previous = ' ';;
        StringBuffer buffer = new StringBuffer();
        Object function;
        try
        {
            while(reader.read(current) == 1)
            {
                if(current[0] == '\\')
                {
                    if(reader.read(current) == 0)
                    {
                        break;
                    }
                    previous = ' ';
                    buffer.append(current[0]);
                    continue;
                }
                else if(current[0] == '(' && previous != ' ')
                {
                    function = CompoundVariable.getNamedFunction(buffer.toString());
                    buffer.setLength(0);
                    if(function instanceof Function)
                    {
                        ((Function)function).setParameters(parseParams(reader));
                        if(reader.read(current) == 0 || current[0] != '}')
                        {
                            throw new InvalidVariableException();
                        }
                        return function;
                    }
                    else
                    {
                        continue;
                    }
                }
                else if(current[0] == '}')
                {
                    function = CompoundVariable.getNamedFunction(buffer.toString());
                    buffer.setLength(0);
                    return function;
                }
                else
                {
                    buffer.append(current[0]);
                    previous = current[0];
                }                        
            }
        }
        catch (IOException e)
        {
            log.error("Error parsing function: " + buffer.toString(),e);
            return null;
        }
        log.warn("Probably an invalid function string: " + buffer.toString());
        return buffer.toString();
    }
    
    /**
     * Compile a String into a list of parameters, each made into a CompoundVariable
     * @param reader
     * @return
     * @throws InvalidVariableException
     */
    LinkedList parseParams(StringReader reader) throws InvalidVariableException
    {
        LinkedList result = new LinkedList();
        StringBuffer buffer = new StringBuffer();
        char[] current = new char[1];
        char previous = ' ';
        int functionRecursion = 0;
        int parenRecursion = 0;
        try
        {
            while(reader.read(current) == 1)
            {
                if(current[0] == '\\')
                {
                    buffer.append(current[0]);
                    if(reader.read(current) == 0)
                    {
                        break;
                    }
                    previous = ' ';
                    buffer.append(current[0]);
                    continue;
                }
                else if(current[0] == ',' && functionRecursion == 0)
                {
                    CompoundVariable param = new CompoundVariable();
                    param.setParameters(buffer.toString());
                    buffer.setLength(0);
                    result.add(param);
                }
                else if(current[0] == ')' && functionRecursion == 0 && parenRecursion == 0)
                {
                    CompoundVariable param = new CompoundVariable();
                    param.setParameters(buffer.toString());
                    buffer.setLength(0);
                    result.add(param);
                    return result;
                }
                else if(current[0] == '{' && previous == '$')
                {
                    buffer.append(current[0]);
                    previous = current[0];
                    functionRecursion++;
                }
                else if(current[0] == '}' && functionRecursion > 0)
                {
                    buffer.append(current[0]);
                    previous = current[0];
                    functionRecursion--;
                }
                else if(current[0] == ')' && functionRecursion == 0 && parenRecursion > 0)
                {
                    buffer.append(current[0]);
                    previous = current[0];
                    parenRecursion--;
                }
                else if(current[0] == '(' && functionRecursion == 0)
                {
                    buffer.append(current[0]);
                    previous = current[0];
                    parenRecursion++;
                }
                else
                {
                    buffer.append(current[0]);
                    previous = current[0];
                }
            }
        }
        catch (IOException e)
        {
            log.error("Error parsing function: " + buffer.toString(),e);
        }
        log.warn("Probably an invalid function string: " + buffer.toString());
        CompoundVariable var = new CompoundVariable();
        var.setParameters(buffer.toString());
        result.add(var);
        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14694.java