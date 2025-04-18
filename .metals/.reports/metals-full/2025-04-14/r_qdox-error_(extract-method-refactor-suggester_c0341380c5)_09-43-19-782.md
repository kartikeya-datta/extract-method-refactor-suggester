error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2679.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2679.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2679.java
text:
```scala
m@@anager.declareBean("context", getAntContext(), AntContext.class);

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.ant.antlib.script;
import com.ibm.bsf.BSFEngine;
import com.ibm.bsf.BSFException;
import com.ibm.bsf.BSFManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.ant.common.antlib.AbstractTask;
import org.apache.ant.common.antlib.DeferredTask;
import org.apache.ant.common.util.ExecutionException;
import org.apache.ant.common.antlib.AntContext;

/**
 * Task to import a component or components from a library
 *
 * @author <a href="mailto:conor@apache.org">Conor MacNeill</a>
 * @created 27 January 2002
 */
public class ScriptBase extends AbstractTask implements DeferredTask {
    /** The script factory instance to be used by this script */
    private ScriptFactory factory;
    /** the name to which this script has been defined */
    private String scriptName;

    /** the attribute values set by the core */
    private Map attributes = new HashMap();

    /** Any embedded set by the core */
    private String text = "";

    /** A list of the nested element names which have been configured */
    private List nestedElementNames = new ArrayList();
    /** A list of the nested elements objects which have been configured */
    private List nestedElements = new ArrayList();

    /**
     * Set the given attribute
     *
     * @param name the name of the attribute
     * @param attributeValue the new attribute value
     */
    public void setAttribute(String name, String attributeValue) {
        attributes.put(name, attributeValue);
    }

    /**
     * Add a nested element
     *
     * @param nestedElementName the nested element's name
     * @param value the object being added
     */
    public void addElement(String nestedElementName, Object value) {
        nestedElementNames.add(nestedElementName);
        nestedElements.add(value);
    }


    /**
     * Execute the script
     *
     * @exception ExecutionException if tghe script execution fails
     */
    public void execute() throws ExecutionException {
        String language = factory.getScriptLanguage(scriptName);
        String script = factory.getScript(scriptName);

        try {
            BSFManager manager = new BSFManager();
            manager.declareBean("self", this, getClass());
            manager.declareBean("context", getContext(), AntContext.class);
            
            // execute the script
            BSFEngine engine = manager.loadScriptingEngine(language);
            
            engine.exec(scriptName, 0, 0, script);
            for (Iterator i = attributes.keySet().iterator(); i.hasNext(); ) {
                String attributeName = (String)i.next();
                String value = (String)attributes.get(attributeName);
                StringBuffer setter = new StringBuffer(attributeName);
                setter.setCharAt(0, Character.toUpperCase(setter.charAt(0)));
                engine.call(null, "set" + setter, new Object[]{value});
            }
            
            Iterator i = nestedElementNames.iterator();
            Iterator j = nestedElements.iterator();
            while (i.hasNext()) {
                String nestedName = (String)i.next();
                Object nestedElement = j.next();
                StringBuffer adder = new StringBuffer(nestedName);
                adder.setCharAt(0, Character.toUpperCase(adder.charAt(0)));
                engine.call(null, "add" + adder, new Object[]{nestedElement});
            }
            
            engine.call(null, "execute", new Object[]{});
        } catch (BSFException e) {
            Throwable t = e;
            Throwable te = e.getTargetException();
            if (te != null) {
                if (te instanceof ExecutionException) {
                    throw (ExecutionException)te;
                } else {
                    t = te;
                }
            }
            throw new ExecutionException(t);
        }
    }

    /**
     * Defines the script.
     *
     * @param text Sets the value for the script variable.
     */
    public void addText(String text) {
        this.text += text;
    }

    /**
     * Sets the factory of the ScriptBase
     *
     * @param factory the script factory this script instance will use
     */
    protected void setFactory(ScriptFactory factory) {
        this.factory = factory;
    }

    /**
     * set the name of the script
     *
     * @param scriptName the script's defined name
     */
    protected void setScriptName(String scriptName) {
        this.scriptName = scriptName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2679.java