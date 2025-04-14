error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/241.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/241.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/241.java
text:
```scala
a@@ssertEquals("There should be two service components in this sample", 2, serviceComponentIds.length);

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.jmx.test.blueprint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.openmbean.TabularData;

import org.apache.aries.jmx.AbstractIntegrationTest;
import org.apache.aries.jmx.blueprint.BlueprintMetadataMBean;
import org.apache.aries.jmx.blueprint.BlueprintStateMBean;
import org.apache.aries.jmx.test.blueprint.framework.BeanPropertyValidator;
import org.apache.aries.jmx.test.blueprint.framework.BeanValidator;
import org.apache.aries.jmx.test.blueprint.framework.BlueprintEventValidator;
import org.apache.aries.jmx.test.blueprint.framework.CollectionValidator;
import org.apache.aries.jmx.test.blueprint.framework.MapEntryValidator;
import org.apache.aries.jmx.test.blueprint.framework.RefValidator;
import org.apache.aries.jmx.test.blueprint.framework.ReferenceListValidator;
import org.apache.aries.jmx.test.blueprint.framework.ReferenceListenerValidator;
import org.apache.aries.jmx.test.blueprint.framework.ReferenceValidator;
import org.apache.aries.jmx.test.blueprint.framework.RegistrationListenerValidator;
import org.apache.aries.jmx.test.blueprint.framework.ServiceValidator;
import org.apache.aries.jmx.test.blueprint.framework.ValueValidator;
import org.junit.Test;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.blueprint.container.BlueprintContainer;

public class BlueprintMBeanTest extends AbstractIntegrationTest {
    
    @Configuration
    public static Option[] configuration() {    
        Option[] options = CoreOptions.options(
                CoreOptions.equinox(), 
                mavenBundle("org.apache.felix", "org.apache.felix.configadmin"),
                mavenBundle("org.ops4j.pax.logging", "pax-logging-api"), 
                mavenBundle("org.ops4j.pax.logging", "pax-logging-service"), 
                mavenBundle("org.apache.aries", "org.apache.aries.util"),
                mavenBundle("org.apache.aries.blueprint", "org.apache.aries.blueprint"), 
                mavenBundle("org.apache.aries.blueprint", "org.apache.aries.blueprint.sample"), 
                mavenBundle("org.apache.aries.jmx", "org.apache.aries.jmx.blueprint"),
                mavenBundle("org.osgi", "org.osgi.compendium")
        );
        options = updateOptions(options);
        return options;
    }  

    @Override
    public void doSetUp() throws Exception {
        waitForMBean(new ObjectName(BlueprintStateMBean.OBJECTNAME));
        waitForMBean(new ObjectName(BlueprintMetadataMBean.OBJECTNAME));
       
       // Wait enough time for osgi framework and blueprint bundles to be set up
       System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Waiting for bundles to be set up");
       getOsgiService(BlueprintContainer.class, "(osgi.blueprint.container.symbolicname=org.apache.aries.blueprint)", DEFAULT_TIMEOUT);
       getOsgiService(BlueprintContainer.class, "(osgi.blueprint.container.symbolicname=org.apache.aries.blueprint.sample)", DEFAULT_TIMEOUT);
    }
    
    @Test
    public void BlueprintSample() throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Start Test Blueprint Sample");
        
        //////////////////////////////
        //Test BlueprintStateMBean
        //////////////////////////////
        
        //find the Blueprint Sample bundle id and the blueprint extender bundle id
        long sampleBundleId = -1;
        long extenderBundleId = -1;     // the blueprint extender bundle "org.apache.geronimo.blueprint.geronimo-blueprint" is also a blueprint bundle.
        for (Bundle bundle : bundleContext.getBundles()){
            if (bundle.getSymbolicName().equals("org.apache.aries.blueprint.sample")) sampleBundleId = bundle.getBundleId();
            if (bundle.getSymbolicName().equals("org.apache.aries.blueprint")) extenderBundleId = bundle.getBundleId();
        }
        if (-1==sampleBundleId) fail("Blueprint Sample Bundle is not found!");
        if (-1==extenderBundleId) fail("Blueprint Extender Bundle is not found!");
        
        //retrieve the proxy object
        BlueprintStateMBean stateProxy = (BlueprintStateMBean) MBeanServerInvocationHandler.newProxyInstance(mbeanServer, new ObjectName(BlueprintStateMBean.OBJECTNAME), BlueprintStateMBean.class, false);
        
        // test getBlueprintBundleIds
        long[] bpBundleIds = stateProxy.getBlueprintBundleIds();
        assertEquals(2, bpBundleIds.length);
        // test getLastEvent
        BlueprintEventValidator sampleValidator = new BlueprintEventValidator(sampleBundleId, extenderBundleId, 2);
        sampleValidator.validate(stateProxy.getLastEvent(sampleBundleId));
        // test getLastEvents
        TabularData lastEvents = stateProxy.getLastEvents();
        assertEquals(BlueprintStateMBean.OSGI_BLUEPRINT_EVENTS_TYPE,lastEvents.getTabularType());
        sampleValidator.validate(lastEvents.get(new Long[]{sampleBundleId}));
        
        //////////////////////////////
        //Test BlueprintMetadataMBean
        //////////////////////////////
        
        //find the Blueprint Sample bundle's container service id
        Bundle sampleBundle = bundleContext.getBundle(sampleBundleId);
        String filter = "(&(osgi.blueprint.container.symbolicname=" // no similar one in interfaces
                + sampleBundle.getSymbolicName() + ")(osgi.blueprint.container.version=" + sampleBundle.getVersion() + "))";
        ServiceReference[] serviceReferences = null;
        try {
            serviceReferences = bundleContext.getServiceReferences(BlueprintContainer.class.getName(), filter);
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException(e);
        }
        long sampleBlueprintContainerServiceId = (Long) serviceReferences[0].getProperty(Constants.SERVICE_ID);
        
        //retrieve the proxy object
        BlueprintMetadataMBean metadataProxy = (BlueprintMetadataMBean) MBeanServerInvocationHandler.newProxyInstance(mbeanServer, new ObjectName(BlueprintMetadataMBean.OBJECTNAME), BlueprintMetadataMBean.class, false);
        
        // test getBlueprintContainerServiceIds
        long[] bpContainerServiceIds = metadataProxy.getBlueprintContainerServiceIds();
        assertEquals(2, bpContainerServiceIds.length);
        
        // test getBlueprintContainerServiceId
        assertEquals(sampleBlueprintContainerServiceId, metadataProxy.getBlueprintContainerServiceId(sampleBundleId));
        
        // test getComponentMetadata
        // bean: foo
        BeanValidator bv_foo = new BeanValidator("org.apache.aries.blueprint.sample.Foo", "init", "destroy");
        
        BeanPropertyValidator bpv_a = new BeanPropertyValidator("a");
        bpv_a.setObjectValueValidator(new ValueValidator("5"));
        
        BeanPropertyValidator bpv_b = new BeanPropertyValidator("b");
        bpv_b.setObjectValueValidator(new ValueValidator("-1"));
        
        BeanPropertyValidator bpv_bar = new BeanPropertyValidator("bar");
        bpv_bar.setObjectValueValidator(new RefValidator("bar"));
        
        BeanPropertyValidator bpv_currency = new BeanPropertyValidator("currency");
        bpv_currency.setObjectValueValidator(new ValueValidator("PLN"));
        
        BeanPropertyValidator bpv_date = new BeanPropertyValidator("date");
        bpv_date.setObjectValueValidator(new ValueValidator("2009.04.17"));
        
        bv_foo.addPropertyValidators(bpv_a, bpv_b, bpv_bar, bpv_currency, bpv_date);
        bv_foo.validate(metadataProxy.getComponentMetadata(sampleBlueprintContainerServiceId, "foo"));
        
        // bean: bar
        BeanPropertyValidator bpv_value = new BeanPropertyValidator("value");
        bpv_value.setObjectValueValidator(new ValueValidator("Hello FooBar"));
        
        BeanPropertyValidator bpv_context = new BeanPropertyValidator("context");
        bpv_context.setObjectValueValidator(new RefValidator("blueprintBundleContext"));
        
        CollectionValidator cv = new CollectionValidator("java.util.List");
        cv.addCollectionValueValidators(new ValueValidator("a list element"), new ValueValidator("5", "java.lang.Integer"));
        BeanPropertyValidator bpv_list = new BeanPropertyValidator("list");
        bpv_list.setObjectValueValidator(cv);
        
        BeanValidator bv_bar = new BeanValidator("org.apache.aries.blueprint.sample.Bar");
        bv_bar.addPropertyValidators(bpv_value, bpv_context, bpv_list);
        bv_bar.validate(metadataProxy.getComponentMetadata(sampleBlueprintContainerServiceId, "bar"));
        
        // service: ref=foo, no componentId set. So using it to test getComponentIdsByType.
        String[] serviceComponentIds = metadataProxy.getComponentIdsByType(sampleBlueprintContainerServiceId, BlueprintMetadataMBean.SERVICE_METADATA);
        assertEquals("There should be only one service component in this sample", 1, serviceComponentIds.length);
        
        MapEntryValidator mev = new MapEntryValidator();
        mev.setKeyValueValidator(new ValueValidator("key"), new ValueValidator("value"));
        
        RegistrationListenerValidator rglrv = new RegistrationListenerValidator("serviceRegistered", "serviceUnregistered");
        rglrv.setListenerComponentValidator(new RefValidator("fooRegistrationListener"));
        
        ServiceValidator sv = new ServiceValidator(4);
        sv.setServiceComponentValidator(new RefValidator("foo"));
        sv.addMapEntryValidator(mev);
        sv.addRegistrationListenerValidator(rglrv);
        sv.validate(metadataProxy.getComponentMetadata(sampleBlueprintContainerServiceId, serviceComponentIds[0]));
        
        // bean: fooRegistrationListener
        BeanValidator bv_fooRegistrationListener = new BeanValidator("org.apache.aries.blueprint.sample.FooRegistrationListener");
        bv_fooRegistrationListener.validate(metadataProxy.getComponentMetadata(sampleBlueprintContainerServiceId, "fooRegistrationListener"));
        
        // reference: ref2
        ReferenceListenerValidator rlrv_1 = new ReferenceListenerValidator("bind", "unbind");
        rlrv_1.setListenerComponentValidator(new RefValidator("bindingListener"));
        
        ReferenceValidator rv = new ReferenceValidator("org.apache.aries.blueprint.sample.InterfaceA", 100);
        rv.addReferenceListenerValidator(rlrv_1);
        rv.validate(metadataProxy.getComponentMetadata(sampleBlueprintContainerServiceId, "ref2"));
        
        // bean: bindingListener
        BeanValidator bv_bindingListener = new BeanValidator("org.apache.aries.blueprint.sample.BindingListener");
        bv_bindingListener.validate(metadataProxy.getComponentMetadata(sampleBlueprintContainerServiceId, "bindingListener"));

        // reference-list: ref-list
        ReferenceListenerValidator rlrv_2 = new ReferenceListenerValidator("bind", "unbind");
        rlrv_2.setListenerComponentValidator(new RefValidator("listBindingListener"));
        
        ReferenceListValidator rlv_ref_list = new ReferenceListValidator("org.apache.aries.blueprint.sample.InterfaceA");
        rlv_ref_list.addReferenceListenerValidator(rlrv_2);
        rlv_ref_list.validate(metadataProxy.getComponentMetadata(sampleBlueprintContainerServiceId, "ref-list"));
        
        // bean: listBindingListener
        BeanValidator bv_listBindingListener = new BeanValidator("org.apache.aries.blueprint.sample.BindingListener");
        bv_listBindingListener.validate(metadataProxy.getComponentMetadata(sampleBlueprintContainerServiceId, "listBindingListener"));
                
        // bean: circularReference
        ReferenceListenerValidator rlrv_3 = new ReferenceListenerValidator("bind", "unbind");
        rlrv_3.setListenerComponentValidator(new RefValidator("circularReference"));
        
        ReferenceListValidator rlv_2 = new ReferenceListValidator("org.apache.aries.blueprint.sample.InterfaceA", 2);
        rlv_2.addReferenceListenerValidator(rlrv_3);
        
        BeanPropertyValidator bpv_list_2 = new BeanPropertyValidator("list");
        bpv_list_2.setObjectValueValidator(rlv_2);
        
        BeanValidator bv_circularReference = new BeanValidator("org.apache.aries.blueprint.sample.BindingListener", "init");
        bv_circularReference.addPropertyValidators(bpv_list_2);
        bv_circularReference.validate(metadataProxy.getComponentMetadata(sampleBlueprintContainerServiceId, "circularReference"));
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/241.java