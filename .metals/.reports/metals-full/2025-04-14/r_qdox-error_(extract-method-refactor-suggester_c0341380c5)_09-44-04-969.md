error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5581.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5581.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5581.java
text:
```scala
private final M@@ap<MethodIntf, TransactionAttributeType> txPerViewStyle1 = new HashMap<MethodIntf, TransactionAttributeType>();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.as.ejb3.component;

import org.jboss.as.ee.component.AbstractComponentConfiguration;
import org.jboss.as.ee.component.AbstractComponentDescription;
import org.jboss.as.ee.component.ComponentNamingMode;
import org.jboss.as.ee.component.EEModuleDescription;

import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagementType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public abstract class EJBComponentDescription extends AbstractComponentDescription {
    /**
     * EJB 3.1 FR 13.3.7, the default transaction attribute is <i>REQUIRED</i>.
     */
    private TransactionAttributeType beanTransactionAttribute = TransactionAttributeType.REQUIRED;
    /**
     * EJB 3.1 FR 13.3.1, the default transaction management type is container-managed transaction demarcation.
     */
    private TransactionManagementType transactionManagementType = TransactionManagementType.CONTAINER;

    private final Map<MethodIntf, TransactionAttributeType> txPerViewStyle1 = new HashMap<MethodIntf, TransactionAttributeType>();;
    private final PopulatingMap<MethodIntf, Map<String, TransactionAttributeType>> txPerViewStyle2 = new PopulatingMap<MethodIntf, Map<String, TransactionAttributeType>>() {
        @Override
        Map<String, TransactionAttributeType> populate() {
            return new HashMap<String, TransactionAttributeType>();
        }
    };
    private final PopulatingMap<MethodIntf, PopulatingMap<String, Map<ArrayKey, TransactionAttributeType>>> txPerViewStyle3 = new PopulatingMap<MethodIntf, PopulatingMap<String, Map<ArrayKey, TransactionAttributeType>>>() {
        @Override
        PopulatingMap<String, Map<ArrayKey, TransactionAttributeType>> populate() {
            return new PopulatingMap<String, Map<ArrayKey, TransactionAttributeType>>() {
                @Override
                Map<ArrayKey, TransactionAttributeType> populate() {
                    return new HashMap<ArrayKey, TransactionAttributeType>();
                }
            };
        }
    };

    // style 1 == beanTransactionAttribute
    private final Map<String, TransactionAttributeType> txStyle2 = new HashMap<String, TransactionAttributeType>();
    private final PopulatingMap<String, Map<ArrayKey, TransactionAttributeType>> txStyle3 = new PopulatingMap<String, Map<ArrayKey, TransactionAttributeType>>() {
        @Override
        Map<ArrayKey, TransactionAttributeType> populate() {
            return new HashMap<ArrayKey, TransactionAttributeType>();
        }
    };

    /**
     * Construct a new instance.
     *
     * @param componentName      the component name
     * @param componentClassName the component instance class name
     * @param moduleDescription the module
     */
    public EJBComponentDescription(final String componentName, final String componentClassName, final EEModuleDescription moduleDescription) {
        super(componentName, componentClassName, moduleDescription);
        //TODO: This should not be create for EJB's in a war
        setNamingMode(ComponentNamingMode.CREATE);
    }

    private static <K, V> V get(Map<K, V> map, K key) {
        if (map == null)
            return null;
        return map.get(key);
    }

    public TransactionAttributeType getTransactionAttribute(MethodIntf methodIntf, String methodName, String... methodParams) {
        assert methodIntf != null : "methodIntf is null";
        assert methodName != null : "methodName is null";
        assert methodParams != null : "methodParams is null";

        ArrayKey methodParamsKey = new ArrayKey(methodParams);
        TransactionAttributeType txAttr = get(get(get(txPerViewStyle3, methodIntf), methodName), methodParamsKey);
        if (txAttr != null)
            return txAttr;
        txAttr = get(get(txPerViewStyle2, methodIntf), methodName);
        if (txAttr != null)
            return txAttr;
        txAttr = get(txPerViewStyle1, methodIntf);
        if (txAttr != null)
            return txAttr;
        txAttr = get(get(txStyle3, methodName), methodParamsKey);
        if (txAttr != null)
            return txAttr;
        txAttr = get(txStyle2, methodName);
        if (txAttr != null)
            return txAttr;
        return beanTransactionAttribute;
    }

    public TransactionManagementType getTransactionManagementType() {
        return transactionManagementType;
    }

    public abstract MethodIntf getMethodIntf(String viewClassName);

    /**
     * Style 1 (13.3.7.2.1 @1)
     *
     * @param methodIntf           the method-intf the annotations apply to or null if EJB class itself
     * @param transactionAttribute
     */
    public void setTransactionAttribute(MethodIntf methodIntf, TransactionAttributeType transactionAttribute) {
        if (methodIntf == null)
            this.beanTransactionAttribute = transactionAttribute;
        else
            txPerViewStyle1.put(methodIntf, transactionAttribute);
    }

    /**
     * Style 2 (13.3.7.2.1 @2)
     *
     * @param methodIntf           the method-intf the annotations apply to or null if EJB class itself
     * @param transactionAttribute
     * @param methodName
     */
    public void setTransactionAttribute(MethodIntf methodIntf, TransactionAttributeType transactionAttribute, String methodName) {
        if (methodIntf == null)
            txStyle2.put(methodName, transactionAttribute);
        else
            txPerViewStyle2.pick(methodIntf).put(methodName, transactionAttribute);
    }

    /**
     * Style 3 (13.3.7.2.1 @3)
     *
     * @param methodIntf           the method-intf the annotations apply to or null if EJB class itself
     * @param transactionAttribute
     * @param methodName
     * @param methodParams
     */
    public void setTransactionAttribute(MethodIntf methodIntf, TransactionAttributeType transactionAttribute, String methodName, String... methodParams) {
        ArrayKey methodParamsKey = new ArrayKey(methodParams);
        if (methodIntf != null)
            txStyle3.pick(methodName).put(methodParamsKey, transactionAttribute);
        else
            txPerViewStyle3.pick(methodIntf).pick(methodName).put(methodParamsKey, transactionAttribute);
    }

    public void setTransactionManagementType(TransactionManagementType transactionManagementType) {
        this.transactionManagementType = transactionManagementType;
    }

    public String getEJBName() {
        return this.getComponentName();
    }

    public String getEJBClassName() {
        return this.getComponentClassName();
    }

    @Override
    protected void processComponentMethod(AbstractComponentConfiguration configuration, Method componentMethod) {
        super.processComponentMethod(configuration, componentMethod);

        // TODO: a temporary measure until EJBTHREE-2120 is fully resolved
        MethodIntf methodIntf = MethodIntf.BEAN;
        processTxAttr((EJBComponentConfiguration) configuration, methodIntf, componentMethod);
    }

    @Override
    protected void processViewMethod(AbstractComponentConfiguration configuration, Class<?> viewClass, Method viewMethod, Method componentMethod) {
        super.processViewMethod(configuration, viewClass, viewMethod, componentMethod);

        MethodIntf methodIntf = getMethodIntf(viewClass.getName());
        processTxAttr((EJBComponentConfiguration) configuration, methodIntf, viewMethod);
    }

    private void processTxAttr(EJBComponentConfiguration configuration, MethodIntf methodIntf, Method method) {
        if(configuration.getTransactionManagementType().equals(TransactionManagementType.BEAN)) {
            // it's a BMT bean
            return;
        }

        String methodName = method.getName();
        TransactionAttributeType txAttr = getTransactionAttribute(methodIntf, methodName, toString(method.getParameterTypes()));

        ConcurrentMap<MethodIntf, ConcurrentMap<String, ConcurrentMap<ArrayKey, TransactionAttributeType>>> txAttrs = configuration.getTxAttrs();
        ConcurrentMap<String, ConcurrentMap<ArrayKey, TransactionAttributeType>> perMethodIntf = txAttrs.get(methodIntf);
        if (perMethodIntf == null) {
            perMethodIntf = new ConcurrentHashMap<String, ConcurrentMap<ArrayKey, TransactionAttributeType>>();
            txAttrs.put(methodIntf, perMethodIntf);
        }
        ConcurrentMap<ArrayKey, TransactionAttributeType> perMethod = perMethodIntf.get(methodName);
        if (perMethod == null) {
            perMethod = new ConcurrentHashMap<ArrayKey, TransactionAttributeType>();
            perMethodIntf.put(methodName, perMethod);
        }
        perMethod.put(new ArrayKey(method.getParameterTypes()), txAttr);
    }

    private static String[] toString(Class<?>[] a) {
        final String[] result = new String[a.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = a[i].getName();
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5581.java