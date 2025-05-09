error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/563.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/563.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/563.java
text:
```scala
S@@tring argConnectionProperties();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.appclient.logging;

import java.io.File;
import java.net.URL;

import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;

import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.logging.Cause;
import org.jboss.logging.Message;
import org.jboss.logging.MessageBundle;
import org.jboss.logging.Messages;
import org.jboss.logging.Param;
import org.jboss.vfs.VirtualFile;

/**
 * This module is using message IDs in the range 13200-14599. This file is using the subset 13220-13299 for
 * non-logger messages. See http://community.jboss.org/docs/DOC-16810 for the full list of currently reserved
 * JBAS message id blocks.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@MessageBundle(projectCode = "JBAS")
public interface AppClientMessages {

    /**
     * The default message bundle
     */
    AppClientMessages MESSAGES = Messages.getBundle(AppClientMessages.class);

    @Message(id = Message.NONE, value = "Name of the app client configuration file to use (default is \"appclient.xml\")")
    String argAppClientConfig();

    /**
     * Instructions for the {@link org.jboss.as.appclient.subsystem.CommandLineArgument#HELP} command line arguments.
     *
     * @return the instructions.
     */
    @Message(id = Message.NONE, value = "Display this message and exit")
    String argHelp();

    /**
     * Instructions for the {@link org.jboss.as.appclient.subsystem.CommandLineArgument#HOST} command line arguments.
     *
     * @return the instructions.
     */
    @Message(id = Message.NONE, value = "Set the url of the application server instance to connect to")
    String argHost();

    /**
     * Instructions for the {@link org.jboss.as.appclient.subsystem.CommandLineArgument#CONNECTION_PROPERTIES} command line
     * arguments.
     *
     * @return the instructions.
     */
    @Message(id = Message.NONE, value = "Load ejb-client.properties file from the given url")
    String connectionProperties();

    /**
     * Instructions for the {@link org.jboss.as.appclient.subsystem.CommandLineArgument#PROPERTIES} command line
     * arguments.
     *
     * @return the instructions.
     */
    @Message(id = Message.NONE, value = "Load system properties from the given url")
    String argProperties();

    /**
     * Instructions for {@link org.jboss.as.appclient.subsystem.CommandLineArgument#SYS_PROP} command line argument.
     *
     * @return the instructions.
     */
    @Message(id = Message.NONE, value = "Set a system property")
    String argSystemProperty();

    /**
     * Instructions for the usage of the
     * {@link org.jboss.as.appclient.subsystem.CommandLineArgument command line arguments} instructions.
     *
     * @return the instructions.
     */
    @Message(id = Message.NONE, value = "Usage: ./appclient.sh [args...] myear.ear#appClient.jar [client args...]%n%nwhere args include:%n")
    String argUsage();

    /**
     * Instructions for {@link org.jboss.as.appclient.subsystem.CommandLineArgument#VERSION} command line argument.
     *
     * @return the instructions.
     */
    @Message(id = Message.NONE, value = "Print version and exit")
    String argVersion();

    /**
     * A message indicating that you must specify an application client to execute.
     *
     * @return the message.
     */
    @Message(id = 13220, value = "You must specify the application client to execute")
    String appClientNotSpecified();

    /**
     * A message indicating the argument, represented by the {@code arg} parameter, expected an additional argument.
     *
     * @param arg the argument that expects an additional argument.
     *
     * @return the message.
     */
    @Message(id = 13221, value = "Argument expected for option %s")
    String argumentExpected(String arg);

    /**
     * Creates an exception indicating the application client could not be found to start.
     *
     * @return an {@link RuntimeException} for the error.
     */
    @Message(id = 13222, value = "Could not find application client jar in deployment")
    RuntimeException cannotFindAppClient();

    /**
     * Creates an exception indicating that the application client, represented by the {@code deploymentName}, could
     * not be found.
     *
     * @param deploymentName the name of the deployment.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 13223, value = "Could not find application client %s")
    DeploymentUnitProcessingException cannotFindAppClient(String deploymentName);

    /**
     * Creates an exception indicating that the application client could not load the main class.
     *
     * @param cause the cause of the error.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 13224, value = "Could not load application client main class")
    RuntimeException cannotLoadAppClientMainClass(@Cause Throwable cause);

    /**
     * Creates an exception indicating the component class could not be loaded.
     *
     * @param cause the cause of the error.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 13225, value = "Could not load component class")
    DeploymentUnitProcessingException cannotLoadComponentClass(@Cause Throwable cause);

    /**
     * A message indicating the properties could not be loaded from the URL.
     *
     * @param url the url to the properties.
     *
     * @return the message.
     */
    @Message(id = 13226, value = "Unable to load properties from URL %s")
    String cannotLoadProperties(URL url);

    /**
     * Creates an exception indicating the app client could not start due to no main class being found.
     *
     * @param deploymentName the deployment name.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 13227, value = "Could not start app client %s as no main class was found")
    RuntimeException cannotStartAppClient(String deploymentName);

    /**
     * Creates an exception indicating the app client could not start due to the main method missing on the main class.
     *
     * @param deploymentName the deployment name.
     * @param mainClass      the main class defined.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 13228, value = "Could not start app client %s as no main method was found on main class %s")
    RuntimeException cannotStartAppClient(String deploymentName, Class<?> mainClass);

    /**
     * Creates an exception indicating the subsystem declaration has been duplicated.
     *
     * @param location the location of the error for the constructor of th exception.
     *
     * @return a {@link XMLStreamException} for the error.
     */
    @Message(id = 13229, value = "Duplicate subsystem declaration")
    XMLStreamException duplicateSubsystemDeclaration(@Param Location location);

    /**
     * Creates an exception indicating the element, represented by the {@code elementName} parameter, has already been
     * declared.
     *
     * @param elementName the element name.
     * @param value       the value.
     * @param location    the location used in the constructor of the exception.
     *
     * @return a {@link XMLStreamException} for the error.
     */
    @Message(id = 13230, value = "%s %s already declared")
    XMLStreamException elementAlreadyDeclared(String elementName, Object value, @Param Location location);

    /**
     * Creates an exception indicating a failure to parse the xml file represented by the {@code appXml} parameter.
     *
     * @param cause  the cause of the error.
     * @param appXml the file that failed to be parsed.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 13231, value = "Failed to parse %s")
    DeploymentUnitProcessingException failedToParseXml(@Cause Throwable cause, VirtualFile appXml);

    /**
     * Creates an exception indicating a failure to parse the xml file represented by the {@code appXml} parameter.
     *
     * @param appXml       the file that failed to be parsed.
     * @param lineNumber   the line the failure occurred on.
     * @param columnNumber the column the failure occurred on.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 13232, value = "Failed to parse %s at [%d,%d]")
    DeploymentUnitProcessingException failedToParseXml(@Cause Throwable cause, VirtualFile appXml, int lineNumber, int columnNumber);

    /**
     * A message indicating the URL in the argument was malformed.
     *
     * @param arg the invalid argument.
     *
     * @return the message.
     */
    @Message(id = 13233, value = "Malformed URL provided for option %s")
    String malformedUrl(String arg);

    /**
     * Creates an exception indicating that more than one application client was found and not app client name was
     * specified.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 13234, value = "More than one application client found and no app client name specified")
    RuntimeException multipleAppClientsFound();

    /**
     * Creates an exception indicating the model contains multiple nodes represented by the {@code nodeName} parameter.
     *
     * @param nodeName the name of the node.
     *
     * @return an {@link IllegalStateException} for the error.
     */
    @Message(id = 13235, value = "Model contains multiple %s nodes")
    IllegalStateException multipleNodesFound(String nodeName);

    /**
     * A message indicating an known option.
     *
     * @param option the unknown option.
     *
     * @return the message.
     */
    @Message(id = 13236, value = "Unknown option %s")
    String unknownOption(String option);

    /**
     * A message indicating the callback handler could not be loaded
     *
     */
    @Message(id = 13237, value = "Could not load callback-handler class %s")
    DeploymentUnitProcessingException couldNotLoadCallbackClass(String clazz);

    /**
     * A message indicating the callback handler could not be instantiated
     *
     */
    @Message(id = 13238, value = "Could not create instance of callback-handler class %s")
    DeploymentUnitProcessingException couldNotCreateCallbackHandler(String clazz);

    /**
     * Creates an exception indicating that the application client, represented by the {@code deploymentName}, could
     * not be found.
     *
     * @param deploymentName the name of the deployment.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 13239, value = "Could find application client %s")
    RuntimeException cannotFindAppClientFile(File deploymentName);

    @Message(id = 13240, value = "Cannot specify both a host to connect to and an ejb-client.properties file. ")
    RuntimeException cannotSpecifyBothHostAndPropertiesFile();

    /**
     * The ejb-client.properties could not be loaded
     */
    @Message(id = 13241, value = "Unable to load ejb-client.properties URL: %s ")
    DeploymentUnitProcessingException exceptionLoadingEjbClientPropertiesURL(final String file, @Cause Throwable cause);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/563.java