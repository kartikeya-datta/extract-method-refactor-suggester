error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6945.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6945.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6945.java
text:
```scala
p@@arserConfiguration.addPackageImport("org.joda.time");

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
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

package org.elasticsearch.script.mvel;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Scorer;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.math.UnboxedMathUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.ScriptEngineService;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.lookup.SearchLookup;
import org.mvel2.MVEL;
import org.mvel2.ParserConfiguration;
import org.mvel2.ParserContext;
import org.mvel2.compiler.ExecutableStatement;
import org.mvel2.integration.impl.MapVariableResolverFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MvelScriptEngineService extends AbstractComponent implements ScriptEngineService {

    private final ParserConfiguration parserConfiguration;

    @Inject
    public MvelScriptEngineService(Settings settings) {
        super(settings);

        parserConfiguration = new ParserConfiguration();
        parserConfiguration.addPackageImport("java.util");
        parserConfiguration.addPackageImport("org.joda");
        parserConfiguration.addImport("time", MVEL.getStaticMethod(System.class, "currentTimeMillis", new Class[0]));
        // unboxed version of Math, better performance since conversion from boxed to unboxed my mvel is not needed
        for (Method m : UnboxedMathUtils.class.getMethods()) {
            if ((m.getModifiers() & Modifier.STATIC) > 0) {
                parserConfiguration.addImport(m.getName(), m);
            }
        }
    }

    @Override
    public void close() {
        // nothing to do here...
    }

    @Override
    public String[] types() {
        return new String[]{"mvel"};
    }

    @Override
    public String[] extensions() {
        return new String[]{"mvel"};
    }

    @Override
    public Object compile(String script) {
        return MVEL.compileExpression(script.trim(), new ParserContext(parserConfiguration));
    }

    @Override
    public Object execute(Object compiledScript, Map vars) {
        return MVEL.executeExpression(compiledScript, vars);
    }

    @Override
    public ExecutableScript executable(Object compiledScript, Map vars) {
        return new MvelExecutableScript(compiledScript, vars);
    }

    @Override
    public SearchScript search(Object compiledScript, SearchLookup lookup, @Nullable Map<String, Object> vars) {
        return new MvelSearchScript(compiledScript, lookup, vars);
    }

    @Override
    public Object unwrap(Object value) {
        return value;
    }

    public static class MvelExecutableScript implements ExecutableScript {

        private final ExecutableStatement script;

        private final MapVariableResolverFactory resolver;

        public MvelExecutableScript(Object script, Map vars) {
            this.script = (ExecutableStatement) script;
            if (vars != null) {
                this.resolver = new MapVariableResolverFactory(vars);
            } else {
                this.resolver = new MapVariableResolverFactory(new HashMap());
            }
        }

        @Override
        public void setNextVar(String name, Object value) {
            resolver.createVariable(name, value);
        }

        @Override
        public Object run() {
            return script.getValue(null, resolver);
        }

        @Override
        public Object unwrap(Object value) {
            return value;
        }
    }

    public static class MvelSearchScript implements SearchScript {

        private final ExecutableStatement script;

        private final SearchLookup lookup;

        private final MapVariableResolverFactory resolver;

        public MvelSearchScript(Object script, SearchLookup lookup, Map<String, Object> vars) {
            this.script = (ExecutableStatement) script;
            this.lookup = lookup;
            if (vars != null) {
                this.resolver = new MapVariableResolverFactory(vars);
            } else {
                this.resolver = new MapVariableResolverFactory(new HashMap());
            }
            for (Map.Entry<String, Object> entry : lookup.asMap().entrySet()) {
                resolver.createVariable(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public void setScorer(Scorer scorer) {
            lookup.setScorer(scorer);
        }

        @Override
        public void setNextReader(AtomicReaderContext context) {
            lookup.setNextReader(context);
        }

        @Override
        public void setNextDocId(int doc) {
            lookup.setNextDocId(doc);
        }

        @Override
        public void setNextScore(float score) {
            resolver.createVariable("_score", score);
        }

        @Override
        public void setNextVar(String name, Object value) {
            resolver.createVariable(name, value);
        }

        @Override
        public void setNextSource(Map<String, Object> source) {
            lookup.source().setNextSource(source);
        }

        @Override
        public Object run() {
            return script.getValue(null, resolver);
        }

        @Override
        public float runAsFloat() {
            return ((Number) run()).floatValue();
        }

        @Override
        public long runAsLong() {
            return ((Number) run()).longValue();
        }

        @Override
        public double runAsDouble() {
            return ((Number) run()).doubleValue();
        }

        @Override
        public Object unwrap(Object value) {
            return value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6945.java