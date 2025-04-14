error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2422.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2422.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2422.java
text:
```scala
final S@@tringBuilder response = new StringBuilder(256);

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
 */

package org.apache.jmeter.protocol.jms.sampler;

import java.util.Date;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;

/**
 *
 * BaseJMSSampler is an abstract class which provides implementation for common
 * properties. Rather than duplicate the code, it's contained in the base class.
 */
public abstract class BaseJMSSampler extends AbstractSampler implements TestListener {

    private static final long serialVersionUID = 240L;

    //++ These are JMX file names and must not be changed
    private static final String JNDI_INITIAL_CONTEXT_FAC = "jms.initial_context_factory"; // $NON-NLS-1$

    private static final String PROVIDER_URL = "jms.provider_url"; // $NON-NLS-1$

    private static final String CONN_FACTORY = "jms.connection_factory"; // $NON-NLS-1$

    private static final String TOPIC = "jms.topic"; // $NON-NLS-1$

    private static final String PRINCIPAL = "jms.security_principle"; // $NON-NLS-1$

    private static final String CREDENTIALS = "jms.security_credentials"; // $NON-NLS-1$

    private static final String ITERATIONS = "jms.iterations"; // $NON-NLS-1$

    private static final String USE_AUTH = "jms.authenticate"; // $NON-NLS-1$

    private static final String USE_PROPERTIES_FILE = "jms.jndi_properties"; // $NON-NLS-1$

    private static final String READ_RESPONSE = "jms.read_response"; // $NON-NLS-1$
    //--

    // See BUG 45460. We need to keep the resource in order to interpret existing files
    private static final String REQUIRED = JMeterUtils.getResString("jms_auth_required"); // $NON-NLS-1$

    public BaseJMSSampler() {
    }

    /**
     * {@inheritDoc}
     */
    public SampleResult sample(Entry e) {
        return this.sample();
    }

    public abstract SampleResult sample();

    // ------------- get/set properties ----------------------//
    /**
     * set the initial context factory
     *
     * @param icf
     */
    public void setJNDIIntialContextFactory(String icf) {
        setProperty(JNDI_INITIAL_CONTEXT_FAC, icf);
    }

    /**
     * method returns the initial context factory for jndi initial context
     * lookup.
     *
     * @return the initial context factory
     */
    public String getJNDIInitialContextFactory() {
        return getPropertyAsString(JNDI_INITIAL_CONTEXT_FAC);
    }

    /**
     * set the provider user for jndi
     *
     * @param url the provider URL
     */
    public void setProviderUrl(String url) {
        setProperty(PROVIDER_URL, url);
    }

    /**
     * method returns the provider url for jndi to connect to
     *
     * @return the provider URL
     */
    public String getProviderUrl() {
        return getPropertyAsString(PROVIDER_URL);
    }

    /**
     * set the connection factory for
     *
     * @param factory
     */
    public void setConnectionFactory(String factory) {
        setProperty(CONN_FACTORY, factory);
    }

    /**
     * return the connection factory parameter used to lookup the connection
     * factory from the JMS server
     *
     * @return the connection factory
     */
    public String getConnectionFactory() {
        return getPropertyAsString(CONN_FACTORY);
    }

    /**
     * set the topic
     *
     * @param topic
     */
    public void setTopic(String topic) {
        setProperty(TOPIC, topic);
    }

    /**
     * return the topic used for the benchmark
     *
     * @return the topic
     */
    public String getTopic() {
        return getPropertyAsString(TOPIC);
    }

    /**
     * set the username to login into the jms server if needed
     *
     * @param user
     */
    public void setUsername(String user) {
        setProperty(PRINCIPAL, user);
    }

    /**
     * return the username used to login to the jms server
     *
     * @return the username used to login to the jms server
     */
    public String getUsername() {
        return getPropertyAsString(PRINCIPAL);
    }

    /**
     * Set the password to login to the jms server
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        setProperty(CREDENTIALS, pwd);
    }

    /**
     * return the password used to login to the jms server
     *
     * @return the password used to login to the jms server
     */
    public String getPassword() {
        return getPropertyAsString(CREDENTIALS);
    }

    /**
     * set the number of iterations the sampler should aggregate
     *
     * @param count
     */
    public void setIterations(String count) {
        setProperty(ITERATIONS, count);
    }

    /**
     * get the iterations as string
     *
     * @return the number of iterations
     */
    public String getIterations() {
        return getPropertyAsString(ITERATIONS);
    }

    /**
     * return the number of iterations as int instead of string
     *
     * @return the number of iterations as int instead of string
     */
    public int getIterationCount() {
        return getPropertyAsInt(ITERATIONS);
    }

    /**
     * Set whether authentication is required for JNDI
     *
     * @param useAuth
     */
    public void setUseAuth(boolean useAuth) {
        setProperty(USE_AUTH, useAuth);
    }

    /**
     * return whether jndi requires authentication
     *
     * @return whether jndi requires authentication
     */
    public boolean isUseAuth() {
        final String useAuth = getPropertyAsString(USE_AUTH);
        return useAuth.equalsIgnoreCase("true") || useAuth.equals(REQUIRED); // $NON-NLS-1$
    }

    /**
     * set whether the sampler should read the response or not
     *
     * @param read whether the sampler should read the response or not
     */
    public void setReadResponse(String read) {
        setProperty(READ_RESPONSE, read);
    }

    /**
     * return whether the sampler should read the response
     *
     * @return whether the sampler should read the response
     */
    public String getReadResponse() {
        return getPropertyAsString(READ_RESPONSE);
    }

    /**
     * return whether the sampler should read the response as a boolean value
     *
     * @return whether the sampler should read the response as a boolean value
     */
    public boolean getReadResponseAsBoolean() {
        return getPropertyAsBoolean(READ_RESPONSE);
    }

    /**
     * if the sampler should use jndi.properties file, call the method with true
     *
     * @param properties
     */
    public void setUseJNDIProperties(String properties) {
        setProperty(USE_PROPERTIES_FILE, properties);
    }

    /**
     * return whether the sampler should use properties file instead of UI
     * parameters.
     *
     * @return  whether the sampler should use properties file instead of UI parameters.
     */
    public String getUseJNDIProperties() {
        return getPropertyAsString(USE_PROPERTIES_FILE);
    }

    /**
     * return the properties as boolean true/false.
     *
     * @return whether the sampler should use properties file instead of UI parameters.
     */
    public boolean getUseJNDIPropertiesAsBoolean() {
        return getPropertyAsBoolean(USE_PROPERTIES_FILE);
    }


    /**
     * Returns a String with the JMS Message Header values.
     *
     * @param message JMS Message
     * @return String with message header values.
     */
    public static String getMessageHeaders(Message message) {
        final StringBuffer response = new StringBuffer(256);
        try {
            response.append("JMS Message Header Attributes:");
            response.append("\n   Correlation ID: ");
            response.append(message.getJMSCorrelationID());

            response.append("\n   Delivery Mode: ");
            if (message.getJMSDeliveryMode() == DeliveryMode.PERSISTENT) {
                response.append("PERSISTANT");
            } else {
                response.append("NON-PERSISTANT");
            }

            final Destination destination = message.getJMSDestination();

            response.append("\n   Destination: ");
            response.append((destination == null ? null : destination
                .toString()));

            response.append("\n   Expiration: ");
            response.append(new Date(message.getJMSExpiration()));

            response.append("\n   Message ID: ");
            response.append(message.getJMSMessageID());

            response.append("\n   Priority: ");
            response.append(message.getJMSPriority());

            response.append("\n   Redelivered: ");
            response.append(message.getJMSRedelivered());

            final Destination replyTo = message.getJMSReplyTo();
            response.append("\n   Reply to: ");
            response.append((replyTo == null ? null : replyTo.toString()));

            response.append("\n   Timestamp: ");
            response.append(new Date(message.getJMSTimestamp()));

            response.append("\n   Type: ");
            response.append(message.getJMSType());

            response.append("\n\n");

        } catch (JMSException e) {
            e.printStackTrace();
        }

        return new String(response);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2422.java