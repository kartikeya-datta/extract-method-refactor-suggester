error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2951.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2951.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2951.java
text:
```scala
r@@eturn new String[]{"python", "py"};

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.script.python;

import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.ScriptEngineService;
import org.python.core.Py;
import org.python.core.PyCode;
import org.python.core.PyObject;
import org.python.core.PyStringMap;
import org.python.util.PythonInterpreter;

import java.util.Map;

/**
 * @author kimchy (shay.banon)
 */
//TODO we can optimize the case for Map<String, Object> similar to PyStringMap
public class PythonScriptEngineService extends AbstractComponent implements ScriptEngineService {

    private final PythonInterpreter interp;

    @Inject public PythonScriptEngineService(Settings settings) {
        super(settings);

        this.interp = PythonInterpreter.threadLocalStateInterpreter(null);
    }

    @Override public String[] types() {
        return new String[]{"python"};
    }

    @Override public String[] extensions() {
        return new String[]{"py"};
    }

    @Override public Object compile(String script) {
        return interp.compile(script);
    }

    @Override public ExecutableScript executable(Object compiledScript, Map<String, Object> vars) {
        return new PythonExecutableScript((PyCode) compiledScript, vars);
    }

    @Override public Object execute(Object compiledScript, Map<String, Object> vars) {
        PyObject pyVars = Py.java2py(vars);
        interp.setLocals(pyVars);
        PyObject ret = interp.eval((PyCode) compiledScript);
        if (ret == null) {
            return null;
        }
        return ret.__tojava__(Object.class);
    }

    @Override public Object unwrap(Object value) {
        return unwrapValue(value);
    }

    @Override public void close() {
        interp.cleanup();
    }

    public class PythonExecutableScript implements ExecutableScript {

        private final PyCode code;

        private final PyStringMap pyVars;

        public PythonExecutableScript(PyCode code, Map<String, Object> vars) {
            this.code = code;
            this.pyVars = new PyStringMap();
            for (Map.Entry<String, Object> entry : vars.entrySet()) {
                pyVars.__setitem__(entry.getKey(), Py.java2py(entry.getValue()));
            }
        }

        @Override public Object run() {
            interp.setLocals(pyVars);
            PyObject ret = interp.eval(code);
            if (ret == null) {
                return null;
            }
            return ret.__tojava__(Object.class);
        }

        @Override public Object run(Map<String, Object> vars) {
            for (Map.Entry<String, Object> entry : vars.entrySet()) {
                pyVars.__setitem__(entry.getKey(), Py.java2py(entry.getValue()));
            }
            interp.setLocals(pyVars);
            PyObject ret = interp.eval(code);
            if (ret == null) {
                return null;
            }
            return ret.__tojava__(Object.class);
        }

        @Override public Object unwrap(Object value) {
            return unwrapValue(value);
        }
    }


    public static Object unwrapValue(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof PyObject) {
            // seems like this is enough, inner PyDictionary will do the conversion for us for example, so expose it directly 
            return ((PyObject) value).__tojava__(Object.class);
        }
        return value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2951.java