error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1383.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1383.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1383.java
text:
```scala
private static F@@ile incomingFeed = new File("src/core/src/test/org/apache/lucene/gdata/server/registry/TestEntityBuilderIncomingFeed.xml");

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.gdata.servlet.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.apache.lucene.gdata.data.GDataAccount;
import org.apache.lucene.gdata.data.ServerBaseFeed;
import org.apache.lucene.gdata.server.GDataResponse;
import org.apache.lucene.gdata.server.ServiceException;
import org.apache.lucene.gdata.server.ServiceFactory;
import org.apache.lucene.gdata.server.administration.AdminService;
import org.apache.lucene.gdata.server.registry.ComponentType;
import org.apache.lucene.gdata.server.registry.GDataServerRegistry;
import org.apache.lucene.gdata.server.registry.RegistryException;
import org.apache.lucene.gdata.servlet.handler.AbstractFeedHandler.FeedHandlerException;
import org.apache.lucene.gdata.utils.ProvidedServiceStub;
import org.apache.lucene.gdata.utils.ServiceFactoryStub;
import org.apache.lucene.gdata.utils.StorageStub;
import org.easymock.MockControl;

import com.google.gdata.util.ParseException;

/**
 * @author Simon Willnauer
 *
 */
public class TestAbstractFeedHandler extends TestCase {
    private MockControl requestMockControl; 
    
    private HttpServletRequest mockRequest = null; 
    
    private String accountName = "acc"; 
    private MockControl adminServiceMockControl;
    private AdminService adminService = null;
    private ServiceFactoryStub stub;
    private String serviceName = StorageStub.SERVICE_TYPE_RETURN;
    private static File incomingFeed = new File("src/test/org/apache/lucene/gdata/server/registry/TestEntityBuilderIncomingFeed.xml");
    BufferedReader reader;
    static{
        
        try {
            
            GDataServerRegistry.getRegistry().registerComponent(StorageStub.class,null);
            GDataServerRegistry.getRegistry().registerComponent(ServiceFactoryStub.class,null);
        } catch (RegistryException e) {
            
            e.printStackTrace();
        }
    }
    protected void setUp() throws Exception {
        super.setUp();
        
        GDataServerRegistry.getRegistry().registerService(new ProvidedServiceStub());
       this.requestMockControl = MockControl.createControl(HttpServletRequest.class);
       this.adminServiceMockControl = MockControl.createControl(AdminService.class);
       this.adminService = (AdminService)this.adminServiceMockControl.getMock();
       this.mockRequest = (HttpServletRequest)this.requestMockControl.getMock();
       this.stub = (ServiceFactoryStub)GDataServerRegistry.getRegistry().lookup(ServiceFactory.class,ComponentType.SERVICEFACTORY);
       this.stub.setAdminService(this.adminService);
       this.reader =  new BufferedReader(new FileReader(incomingFeed));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
     * Test method for 'org.apache.lucene.gdata.servlet.handler.AbstractFeedHandler.createFeedFromRequest(HttpServletRequest)'
     */
    public void testCreateFeedFromRequest() throws ParseException, IOException, FeedHandlerException {
        
        this.requestMockControl.expectAndDefaultReturn(this.mockRequest 
                .getParameter("service"), this.serviceName);
        this.requestMockControl.expectAndReturn(this.mockRequest.getReader(),this.reader);
        this.requestMockControl.replay();
        AbstractFeedHandler handler = new InsertFeedHandler();
        try{
        ServerBaseFeed feed = handler.createFeedFromRequest(this.mockRequest);
        assertNotNull(feed.getId());
        
        }catch (Exception e) {
            e.printStackTrace();
            fail("unexpected exception -- "+e.getMessage());
            
        }
        this.requestMockControl.verify();
        this.requestMockControl.reset();
        /*
         * Test for not registered service
         */
        this.requestMockControl.expectAndDefaultReturn(this.mockRequest 
                .getParameter("service"), "some other service");
        this.requestMockControl.replay();
         handler = new InsertFeedHandler();
        try{
        ServerBaseFeed feed = handler.createFeedFromRequest(this.mockRequest);
        
        fail(" exception expected");
        }catch (FeedHandlerException e) {
            e.printStackTrace();
            assertEquals(HttpServletResponse.SC_NOT_FOUND,handler.getErrorCode());
        }
        this.requestMockControl.verify();
        
        this.requestMockControl.reset();
        /*
         * Test for IOException
         */
        this.requestMockControl.expectAndDefaultReturn(this.mockRequest 
                .getParameter("service"), this.serviceName);
        this.reader.close();
        this.requestMockControl.expectAndReturn(this.mockRequest.getReader(),this.reader);
        this.requestMockControl.replay();
         handler = new InsertFeedHandler();
        try{
        ServerBaseFeed feed = handler.createFeedFromRequest(this.mockRequest);
        
        fail(" exception expected");
        }catch (IOException e) {
            e.printStackTrace();
            assertEquals(HttpServletResponse.SC_BAD_REQUEST,handler.getErrorCode());
        }
        this.requestMockControl.verify();
        
        
        
        
    }

    /*
     * Test method for 'org.apache.lucene.gdata.servlet.handler.AbstractFeedHandler.createRequestedAccount(HttpServletRequest)'
     */
    public void testCreateRequestedAccount() throws IOException, ParseException, ServiceException {
        this.requestMockControl.expectAndDefaultReturn(this.mockRequest 
                .getParameter(AbstractFeedHandler.PARAMETER_ACCOUNT), this.accountName);
        GDataAccount a = new GDataAccount();
        a.setName("helloworld");
        this.adminServiceMockControl.expectAndReturn(this.adminService.getAccount(this.accountName),a );
        this.requestMockControl.replay();
        this.adminServiceMockControl.replay();
        AbstractFeedHandler handler = new InsertFeedHandler();
        try{
            
            GDataAccount account = handler.createRequestedAccount(this.mockRequest);
       
        assertEquals(a,account);
        
        }catch (Exception e) {
            e.printStackTrace();
            fail("unexpected exception -- "+e.getMessage());
            
        }
        this.requestMockControl.verify();
        this.requestMockControl.reset();
        this.adminServiceMockControl.verify();
        this.adminServiceMockControl.reset();
        
        /*
         *Test for service exception 
         */
        
        this.requestMockControl.expectAndDefaultReturn(this.mockRequest 
                .getParameter(AbstractFeedHandler.PARAMETER_ACCOUNT), this.accountName);
        
        a.setName("helloworld");
        this.adminServiceMockControl.expectAndDefaultThrow(this.adminService.getAccount(this.accountName),new ServiceException(GDataResponse.BAD_REQUEST) );
        this.requestMockControl.replay();
        this.adminServiceMockControl.replay();
         handler = new InsertFeedHandler();
        try{
            
            GDataAccount account = handler.createRequestedAccount(this.mockRequest);
       
            fail(" exception expected ");
        
        }catch (Exception e) {
            e.printStackTrace();
            assertEquals(HttpServletResponse.SC_BAD_REQUEST,handler.getErrorCode());
            
        }
        this.requestMockControl.verify();
        this.requestMockControl.reset();
        this.adminServiceMockControl.verify();
        this.adminServiceMockControl.reset();
        
        
        
        
        
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1383.java