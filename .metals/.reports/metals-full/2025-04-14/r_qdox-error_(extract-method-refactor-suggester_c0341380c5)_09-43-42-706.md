error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10703.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10703.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10703.java
text:
```scala
r@@eturn new HashMap<String, Object>();

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

package org.jboss.as.ejb3.remote.protocol.versionone;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.jboss.ejb.client.remoting.PackedInteger;
import org.jboss.ejb.client.remoting.ProtocolV1ClassTable;
import org.jboss.ejb.client.remoting.ProtocolV1ObjectTable;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.ByteOutput;
import org.jboss.marshalling.ClassResolver;
import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;
import org.jboss.remoting3.Channel;


/**
 * @author Jaikiran Pai
 */
abstract class AbstractMessageHandler implements MessageHandler {

    protected static final byte HEADER_NO_SUCH_EJB_FAILURE = 0x0A;
    protected static final byte HEADER_NO_SUCH_EJB_METHOD_FAILURE = 0x0B;
    protected static final byte HEADER_SESSION_NOT_ACTIVE_FAILURE = 0x0C;
    private static final byte HEADER_INVOCATION_EXCEPTION = 0x06;


    protected Map<String, Object> readAttachments(final ObjectInput input) throws IOException, ClassNotFoundException {
        final int numAttachments = input.readByte();
        if (numAttachments == 0) {
            return null;
        }
        final Map<String, Object> attachments = new HashMap<String, Object>(numAttachments);
        for (int i = 0; i < numAttachments; i++) {
            // read the key
            final String key = (String) input.readObject();
            // read the attachment value
            final Object val = input.readObject();
            attachments.put(key, val);
        }
        return attachments;
    }

    protected void writeAttachments(final ObjectOutput output, final Map<String, Object> attachments) throws IOException {
        if (attachments == null) {
            output.writeByte(0);
            return;
        }
        // write the attachment count
        PackedInteger.writePackedInteger(output, attachments.size());
        for (Map.Entry<String, Object> entry : attachments.entrySet()) {
            output.writeObject(entry.getKey());
            output.writeObject(entry.getValue());
        }
    }

    protected void writeException(final Channel channel, final MarshallerFactory marshallerFactory,
                                  final short invocationId, final Throwable t,
                                  final Map<String, Object> attachments) throws IOException {
        final DataOutputStream outputStream = new DataOutputStream(channel.writeMessage());
        try {
            // write the header
            outputStream.write(HEADER_INVOCATION_EXCEPTION);
            // write the invocation id
            outputStream.writeShort(invocationId);
            // write out the exception
            final Marshaller marshaller = this.prepareForMarshalling(marshallerFactory, outputStream);
            marshaller.writeObject(t);
            // write the attachments
            this.writeAttachments(marshaller, attachments);
            // finish marshalling
            marshaller.finish();
        } finally {
            outputStream.close();
        }
    }

    protected void writeInvocationFailure(final Channel channel, final byte messageHeader, final short invocationId, final String failureMessage) throws IOException {
        final DataOutputStream dataOutputStream = new DataOutputStream(channel.writeMessage());
        try {
            // write header
            dataOutputStream.writeByte(messageHeader);
            // write invocation id
            dataOutputStream.writeShort(invocationId);
            // write the failure message
            dataOutputStream.writeUTF(failureMessage);
        } finally {
            dataOutputStream.close();
        }

    }

    protected void writeNoSuchEJBFailureMessage(final Channel channel, final short invocationId, final String appName, final String moduleName,
                                                final String distinctname, final String beanName, final String viewClassName) throws IOException {
        final StringBuffer sb = new StringBuffer("No such EJB[");
        sb.append("appname=").append(appName).append(", ");
        sb.append("modulename=").append(moduleName).append(", ");
        sb.append("distinctname=").append(distinctname).append(", ");
        sb.append("beanname=").append(beanName).append(", ");
        sb.append("viewclassname=").append(viewClassName).append("]");
        this.writeInvocationFailure(channel, HEADER_NO_SUCH_EJB_FAILURE, invocationId, sb.toString());
    }

    protected void writeSessionNotActiveFailureMessage(final Channel channel, final short invocationId, final String appName, final String moduleName,
                                                       final String distinctname, final String beanName) throws IOException {
        final StringBuffer sb = new StringBuffer("Session not active for EJB[");
        sb.append("appname=").append(appName).append(", ");
        sb.append("modulename=").append(moduleName).append(", ");
        sb.append("distinctname=").append(distinctname).append(", ");
        sb.append("beanname=").append(beanName).append("]");
        this.writeInvocationFailure(channel, HEADER_SESSION_NOT_ACTIVE_FAILURE, invocationId, sb.toString());
    }

    protected void writeNoSuchEJBMethodFailureMessage(final Channel channel, final short invocationId, final String appName, final String moduleName,
                                                      final String distinctname, final String beanName, final String viewClassName,
                                                      final String methodName, final String[] methodParamTypes) throws IOException {
        final StringBuffer sb = new StringBuffer("No such method ");
        sb.append(methodName).append("(");
        if (methodParamTypes != null) {
            for (int i = 0; i < methodParamTypes.length; i++) {
                if (i != 0) {
                    sb.append(",");
                }
                sb.append(methodParamTypes[i]);
            }
        }
        sb.append(") on EJB[");
        sb.append("appname=").append(appName).append(", ");
        sb.append("modulename=").append(moduleName).append(", ");
        sb.append("distinctname=").append(distinctname).append(", ");
        sb.append("beanname=").append(beanName).append(", ");
        sb.append("viewclassname=").append(viewClassName).append("]");
        this.writeInvocationFailure(channel, HEADER_NO_SUCH_EJB_METHOD_FAILURE, invocationId, sb.toString());
    }

    /**
     * Creates and returns a {@link org.jboss.marshalling.Marshaller} which is ready to be used for marshalling. The {@link org.jboss.marshalling.Marshaller#start(org.jboss.marshalling.ByteOutput)}
     * will be invoked by this method, to use the passed {@link java.io.DataOutput dataOutput}, before returning the marshaller.
     *
     * @param marshallerFactory The marshaller factory
     * @param dataOutput        The {@link java.io.DataOutput} to which the data will be marshalled
     * @return
     * @throws IOException
     */
    protected org.jboss.marshalling.Marshaller prepareForMarshalling(final org.jboss.marshalling.MarshallerFactory marshallerFactory, final DataOutput dataOutput) throws IOException {
        final org.jboss.marshalling.Marshaller marshaller = this.getMarshaller(marshallerFactory);
        final OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                final int byteToWrite = b & 0xff;
                dataOutput.write(byteToWrite);
            }
        };
        final ByteOutput byteOutput = Marshalling.createByteOutput(outputStream);
        // start the marshaller
        marshaller.start(byteOutput);

        return marshaller;
    }

    /**
     * Creates and returns a {@link org.jboss.marshalling.Marshaller}
     *
     * @param marshallerFactory The marshaller factory
     * @return
     * @throws IOException
     */
    private org.jboss.marshalling.Marshaller getMarshaller(final org.jboss.marshalling.MarshallerFactory marshallerFactory) throws IOException {
        final MarshallingConfiguration marshallingConfiguration = new MarshallingConfiguration();
        marshallingConfiguration.setClassTable(ProtocolV1ClassTable.INSTANCE);
        marshallingConfiguration.setObjectTable(ProtocolV1ObjectTable.INSTANCE);
        marshallingConfiguration.setVersion(2);

        return marshallerFactory.createMarshaller(marshallingConfiguration);
    }

    /**
     * Creates and returns a {@link org.jboss.marshalling.Unmarshaller} which is ready to be used for unmarshalling. The {@link org.jboss.marshalling.Unmarshaller#start(org.jboss.marshalling.ByteInput)}
     * will be invoked by this method, to use the passed {@link java.io.DataInput dataInput}, before returning the unmarshaller.
     *
     * @param marshallerFactory The marshaller factory
     * @param classResolver     The {@link ClassResolver} which will be used during unmarshalling
     * @param dataInput         The data input from which to unmarshall
     * @return
     * @throws IOException
     */
    protected Unmarshaller prepareForUnMarshalling(final MarshallerFactory marshallerFactory, final ClassResolver classResolver, final DataInput dataInput) throws IOException {
        final Unmarshaller unmarshaller = this.getUnMarshaller(marshallerFactory, classResolver);
        final InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                try {

                    final int b = dataInput.readByte();
                    return b & 0xff;
                } catch (EOFException eof) {
                    return -1;
                }
            }
        };
        final ByteInput byteInput = Marshalling.createByteInput(is);
        // start the unmarshaller
        unmarshaller.start(byteInput);

        return unmarshaller;
    }

    /**
     * Creates and returns a {@link Unmarshaller}
     *
     * @param marshallerFactory The marshaller factory
     * @return
     * @throws IOException
     */
    private Unmarshaller getUnMarshaller(final MarshallerFactory marshallerFactory, final ClassResolver classResolver) throws IOException {
        final MarshallingConfiguration marshallingConfiguration = new MarshallingConfiguration();
        marshallingConfiguration.setVersion(2);
        marshallingConfiguration.setClassTable(ProtocolV1ClassTable.INSTANCE);
        marshallingConfiguration.setObjectTable(ProtocolV1ObjectTable.INSTANCE);
        marshallingConfiguration.setClassResolver(classResolver);

        return marshallerFactory.createUnmarshaller(marshallingConfiguration);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10703.java