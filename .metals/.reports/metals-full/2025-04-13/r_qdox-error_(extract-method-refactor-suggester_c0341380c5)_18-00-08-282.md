error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2708.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2708.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2708.java
text:
```scala
T@@hrowable ex, Object preCallToken) throws Throwable {

/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.aries.blueprint.testbundlea;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.aries.blueprint.Interceptor;
import org.apache.aries.blueprint.NamespaceHandler;
import org.apache.aries.blueprint.ParserContext;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.osgi.service.blueprint.reflect.Metadata;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A simple example namespace handler, that understands an element, and 2 attributes
 * 
 * When attribone is found on a bean, an interceptor is added that will track invocations.
 * 
 * This handler is designed to exercise aspects of the NamespaceHandler capability set.
 *
 */
public class NSHandlerTwo implements NamespaceHandler{
    
    public static String NSURI = "http://ns.handler.two";
    
    private static String ELT_NAME = "nshandlertwo";
    private static String ATTRIB_ONE = "attribone";
    private static String ATTRIB_TWO = "attribtwo";
    
    private static List<String> interceptorLog = new ArrayList<String>();
    
    private static Interceptor tracker = new Interceptor() {
        
        //debug/trace calls to toString etc will mess up the interceptor
        //log, and break tests if tracked. So we filter them out here.
        private boolean isIgnorableMethod(Method m){
            if(m.getDeclaringClass()==Object.class){
                return true;
            }
            else
                return false;
        }
        
        public Object preCall(ComponentMetadata cm, Method m, Object... parameters)
                throws Throwable {            
            String args = "[";
            if(parameters!=null){
                if(parameters.length>0){
                    args+=parameters[0]==null ? "null" : parameters[0].toString();
                }
                for(int index=1; index<parameters.length; index++){
                    args+=","+(parameters[index]==null ? "null" : parameters[index].toString());
                }
            }
            args+="]";
            String token = cm.getId() +":"+ m.getName() +":"+args+":"+System.currentTimeMillis();
            
            if(!isIgnorableMethod(m))
              interceptorLog.add("PRECALL:"+token);
            
            return token;
        }
        
        public void postCallWithReturn(ComponentMetadata cm, Method m,
                Object returnType, Object preCallToken) throws Throwable {
            
            if(!isIgnorableMethod(m))
                interceptorLog.add("POSTCALL["+returnType.toString()+"]:"+preCallToken);
        }
        
        public void postCallWithException(ComponentMetadata cm, Method m,
                Exception ex, Object preCallToken) throws Throwable {
            
            if(!isIgnorableMethod(m))
                interceptorLog.add("POSTCALLEXCEPTION["+ex.toString()+"]:"+preCallToken);
        }
        
        public int getRank() {
            return 0;
        }
    };
    
    //
    public ComponentMetadata decorate(Node node, ComponentMetadata component,
            ParserContext context) {
        
        if(node.getLocalName().equals(ATTRIB_ONE)){
            if(context.getComponentDefinitionRegistry().getInterceptors(component) == null ||
               !context.getComponentDefinitionRegistry().getInterceptors(component).contains(tracker) ){
                context.getComponentDefinitionRegistry().registerInterceptorWithComponent(component, tracker);
            }
        }
        return component;
    }
    
    //process elements
    public Metadata parse(Element element, ParserContext context) {
        return null;
    }    

    //supply schema back to blueprint.
    public URL getSchemaLocation(String namespace) {
        return this.getClass().getResource("nshandlertwo.xsd");
    }

    public Set<Class> getManagedClasses() {
        return null;
    }

    public List<String> getLog() {
        return Collections.unmodifiableList(interceptorLog);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2708.java