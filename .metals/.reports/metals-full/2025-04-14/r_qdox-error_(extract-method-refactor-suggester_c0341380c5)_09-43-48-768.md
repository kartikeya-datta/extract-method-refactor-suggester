error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/770.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/770.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/770.java
text:
```scala
i@@f (listener != null /*synchronousMode==true*/) {

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
 *
 */

package org.apache.jmeter.protocol.smtp.sampler.protocol;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.IOUtils;
import org.apache.jmeter.config.Argument;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This class performs all tasks necessary to send a message (build message,
 * prepare connection, send message). Provides getter-/setter-methods for an
 * SmtpSampler-object to configure transport and message settings. The
 * send-mail-command itself is started by the SmtpSampler-object.
 */
public class SendMailCommand {

    // local vars
    private static final Logger logger = LoggingManager.getLoggerForClass();

    // Use the actual class so the name must be correct.
    private static final String TRUST_ALL_SOCKET_FACTORY = TrustAllSSLSocketFactory.class.getName();

    private boolean useSSL = false;
    private boolean useStartTLS = false;
    private boolean trustAllCerts = false;
    private boolean enforceStartTLS = false;
    private boolean sendEmlMessage = false;
    private boolean enableDebug;
    private String smtpServer;
    private String smtpPort;
    private String sender;
    private List<InternetAddress> replyTo;
    private String emlMessage;
    private List<InternetAddress> receiverTo;
    private List<InternetAddress> receiverCC;
    private List<InternetAddress> receiverBCC;
    private CollectionProperty headerFields;
    private String subject = "";

    private boolean useAuthentication = false;
    private String username;
    private String password;

    private boolean useLocalTrustStore;
    private String trustStoreToUse;

    private List<File> attachments;

    private String mailBody;

    // case we are measuring real time of spedition
    private boolean synchronousMode;

    private Session session;

    private StringBuilder serverResponse = new StringBuilder(); // TODO this is not populated currently

    /** send plain body, i.e. not multipart/mixed */
    private boolean plainBody;

    /**
     * Standard-Constructor
     */
    public SendMailCommand() {
        headerFields = new CollectionProperty();
        attachments = new ArrayList<File>();
    }

    /**
     * Prepares message prior to be sent via execute()-method, i.e. sets
     * properties such as protocol, authentication, etc.
     *
     * @return Message-object to be sent to execute()-method
     * @throws MessagingException
     * @throws IOException
     */
    public Message prepareMessage() throws MessagingException, IOException {

        Properties props = new Properties();

        String protocol = getProtocol();

        // set properties using JAF
        props.setProperty("mail." + protocol + ".host", smtpServer);
        props.setProperty("mail." + protocol + ".port", getPort());
        props.setProperty("mail." + protocol + ".auth", Boolean.toString(useAuthentication));

        if (enableDebug) {
            props.setProperty("mail.debug","true");
        }

        if (useStartTLS) {
            props.setProperty("mail.smtp.starttls.enable", "true");
            if (enforceStartTLS){
                // Requires JavaMail 1.4.2+
                props.setProperty("mail.smtp.starttls.require", "true");
            }
        }

        if (trustAllCerts) {
            if (useSSL) {
                props.setProperty("mail.smtps.ssl.socketFactory.class", TRUST_ALL_SOCKET_FACTORY);
                props.setProperty("mail.smtps.ssl.socketFactory.fallback", "false");
            } else if (useStartTLS) {
                props.setProperty("mail.smtp.ssl.socketFactory.class", TRUST_ALL_SOCKET_FACTORY);
                props.setProperty("mail.smtp.ssl.socketFactory.fallback", "false");
            }
        } else if (useLocalTrustStore){
            File truststore = new File(trustStoreToUse);
            logger.info("load local truststore - try to load truststore from: "+truststore.getAbsolutePath());
            if(!truststore.exists()){
                logger.info("load local truststore -Failed to load truststore from: "+truststore.getAbsolutePath());
                truststore = new File(FileServer.getFileServer().getBaseDir(), trustStoreToUse);
                logger.info("load local truststore -Attempting to read truststore from:  "+truststore.getAbsolutePath());
                if(!truststore.exists()){
                    logger.info("load local truststore -Failed to load truststore from: "+truststore.getAbsolutePath() + ". Local truststore not available, aborting execution.");
                    throw new IOException("Local truststore file not found. Also not available under : " + truststore.getAbsolutePath());
                }
            }
            if (useSSL) {
                // Requires JavaMail 1.4.2+
                props.put("mail.smtps.ssl.socketFactory", new LocalTrustStoreSSLSocketFactory(truststore));
                props.put("mail.smtps.ssl.socketFactory.fallback", "false");
            } else if (useStartTLS) {
                // Requires JavaMail 1.4.2+
                props.put("mail.smtp.ssl.socketFactory", new LocalTrustStoreSSLSocketFactory(truststore));
                props.put("mail.smtp.ssl.socketFactory.fallback", "false");
            }
        }

        session = Session.getInstance(props, null);
        
        Message message;

        if (sendEmlMessage) {
            message = new MimeMessage(session, new BufferedInputStream(new FileInputStream(emlMessage)));
        } else {
            message = new MimeMessage(session);
            // handle body and attachments
            Multipart multipart = new MimeMultipart();
            final int attachmentCount = attachments.size();
            if (plainBody && 
               (attachmentCount == 0 ||  (mailBody.length() == 0 && attachmentCount == 1))) {
                if (attachmentCount == 1) { // i.e. mailBody is empty
                    File first = attachments.get(0);
                    InputStream is = null;
                    try {
                        is = new BufferedInputStream(new FileInputStream(first));
                        message.setText(IOUtils.toString(is));
                    } finally {
                        IOUtils.closeQuietly(is);
                    }
                } else {
                    message.setText(mailBody);
                }
            } else {
                BodyPart body = new MimeBodyPart();
                body.setText(mailBody);
                multipart.addBodyPart(body);
                for (File f : attachments) {
                    BodyPart attach = new MimeBodyPart();
                    attach.setFileName(f.getName());
                    attach.setDataHandler(new DataHandler(new FileDataSource(f.getAbsolutePath())));
                    multipart.addBodyPart(attach);
                }
                message.setContent(multipart);
            }
        }

        // set from field and subject
        if (null != sender) {
            message.setFrom(new InternetAddress(sender));
        }

        if (null != replyTo) {
            InternetAddress[] to = new InternetAddress[replyTo.size()];
            message.setReplyTo(replyTo.toArray(to));
        }

        message.setSubject(subject);

        if (receiverTo != null) {
            InternetAddress[] to = new InternetAddress[receiverTo.size()];
            receiverTo.toArray(to);
            message.setRecipients(Message.RecipientType.TO, to);
        }

        if (receiverCC != null) {
            InternetAddress[] cc = new InternetAddress[receiverCC.size()];
            receiverCC.toArray(cc);
            message.setRecipients(Message.RecipientType.CC, cc);
        }

        if (receiverBCC != null) {
            InternetAddress[] bcc = new InternetAddress[receiverBCC.size()];
            receiverBCC.toArray(bcc);
            message.setRecipients(Message.RecipientType.BCC, bcc);
        }

        for (int i = 0; i < headerFields.size(); i++) {
            Argument argument = (Argument)((TestElementProperty)headerFields.get(i)).getObjectValue();
            message.setHeader(argument.getName(), argument.getValue());
        }

        message.saveChanges();
        return message;
    }

    /**
     * Sends message to mailserver, waiting for delivery if using synchronous mode.
     *
     * @param message
     *            Message prior prepared by prepareMessage()
     * @throws MessagingException
     * @throws IOException
     * @throws InterruptedException
     */
    public void execute(Message message) throws MessagingException, IOException, InterruptedException {

        Transport tr = session.getTransport(getProtocol());
        SynchronousTransportListener listener = null;

        if (synchronousMode) {
            listener = new SynchronousTransportListener();
            tr.addTransportListener(listener);
        }

        if (useAuthentication) {
            tr.connect(smtpServer, username, password);
        } else {
            tr.connect();
        }

        tr.sendMessage(message, message.getAllRecipients());

        if (synchronousMode) {
            listener.attend(); // listener cannot be null here
        }

        tr.close();
        logger.debug("transport closed");

        logger.debug("message sent");
        return;
    }

    /**
     * Processes prepareMessage() and execute()
     *
     * @throws Exception
     */
    public void execute() throws Exception {
        execute(prepareMessage());
    }

    /**
     * Returns FQDN or IP of SMTP-server to be used to send message - standard
     * getter
     *
     * @return FQDN or IP of SMTP-server
     */
    public String getSmtpServer() {
        return smtpServer;
    }

    /**
     * Sets FQDN or IP of SMTP-server to be used to send message - to be called
     * by SmtpSampler-object
     *
     * @param smtpServer
     *            FQDN or IP of SMTP-server
     */
    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    /**
     * Returns sender-address for current message - standard getter
     *
     * @return sender-address
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the sender-address for the current message - to be called by
     * SmtpSampler-object
     *
     * @param sender
     *            Sender-address for current message
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Returns subject for current message - standard getter
     *
     * @return Subject of current message
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets subject for current message - called by SmtpSampler-object
     *
     * @param subject
     *            Subject for message of current message - may be null
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns username to authenticate at the mailserver - standard getter
     *
     * @return Username for mailserver
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username to authenticate at the mailserver - to be called by
     * SmtpSampler-object
     *
     * @param username
     *            Username for mailserver
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns password to authenticate at the mailserver - standard getter
     *
     * @return Password for mailserver
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password to authenticate at the mailserver - to be called by
     * SmtpSampler-object
     *
     * @param password
     *            Password for mailserver
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets receivers of current message ("to") - to be called by
     * SmtpSampler-object
     *
     * @param receiverTo
     *            List of receivers <InternetAddress>
     */
    public void setReceiverTo(List<InternetAddress> receiverTo) {
        this.receiverTo = receiverTo;
    }

    /**
     * Returns receivers of current message <InternetAddress> ("cc") - standard
     * getter
     *
     * @return List of receivers
     */
    public List<InternetAddress> getReceiverCC() {
        return receiverCC;
    }

    /**
     * Sets receivers of current message ("cc") - to be called by
     * SmtpSampler-object
     *
     * @param receiverCC
     *            List of receivers <InternetAddress>
     */
    public void setReceiverCC(List<InternetAddress> receiverCC) {
        this.receiverCC = receiverCC;
    }

    /**
     * Returns receivers of current message <InternetAddress> ("bcc") - standard
     * getter
     *
     * @return List of receivers
     */
    public List<InternetAddress> getReceiverBCC() {
        return receiverBCC;
    }

    /**
     * Sets receivers of current message ("bcc") - to be called by
     * SmtpSampler-object
     *
     * @param receiverBCC
     *            List of receivers <InternetAddress>
     */
    public void setReceiverBCC(List<InternetAddress> receiverBCC) {
        this.receiverBCC = receiverBCC;
    }

    /**
     * Returns if authentication is used to access the mailserver - standard
     * getter
     *
     * @return True if authentication is used to access mailserver
     */
    public boolean isUseAuthentication() {
        return useAuthentication;
    }

    /**
     * Sets if authentication should be used to access the mailserver - to be
     * called by SmtpSampler-object
     *
     * @param useAuthentication
     *            Should authentication be used to access mailserver?
     */
    public void setUseAuthentication(boolean useAuthentication) {
        this.useAuthentication = useAuthentication;
    }

    /**
     * Returns if SSL is used to send message - standard getter
     *
     * @return True if SSL is used to transmit message
     */
    public boolean getUseSSL() {
        return useSSL;
    }

    /**
     * Sets SSL to secure the delivery channel for the message - to be called by
     * SmtpSampler-object
     *
     * @param useSSL
     *            Should StartTLS be used to secure SMTP-connection?
     */
    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    /**
     * Returns if StartTLS is used to transmit message - standard getter
     *
     * @return True if StartTLS is used to transmit message
     */
    public boolean getUseStartTLS() {
        return useStartTLS;
    }

    /**
     * Sets StartTLS to secure the delivery channel for the message - to be
     * called by SmtpSampler-object
     *
     * @param useStartTLS
     *            Should StartTLS be used to secure SMTP-connection?
     */
    public void setUseStartTLS(boolean useStartTLS) {
        this.useStartTLS = useStartTLS;
    }

    /**
     * Returns port to be used for SMTP-connection (standard 25 or 465) -
     * standard getter
     *
     * @return Port to be used for SMTP-connection
     */
    public String getSmtpPort() {
        return smtpPort;
    }

    /**
     * Sets port to be used for SMTP-connection (standard 25 or 465) - to be
     * called by SmtpSampler-object
     *
     * @param smtpPort
     *            Port to be used for SMTP-connection
     */
    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    /**
     * Returns if sampler should trust all certificates - standard getter
     *
     * @return True if all Certificates are trusted
     */
    public boolean isTrustAllCerts() {
        return trustAllCerts;
    }

    /**
     * Determines if SMTP-sampler should trust all certificates, no matter what
     * CA - to be called by SmtpSampler-object
     *
     * @param trustAllCerts
     *            Should all certificates be trusted?
     */
    public void setTrustAllCerts(boolean trustAllCerts) {
        this.trustAllCerts = trustAllCerts;
    }

    /**
     * Instructs object to enforce StartTLS and not to fallback to plain
     * SMTP-connection - to be called by SmtpSampler-object
     *
     * @param enforceStartTLS
     *            Should StartTLS be enforced?
     */
    public void setEnforceStartTLS(boolean enforceStartTLS) {
        this.enforceStartTLS = enforceStartTLS;
    }

    /**
     * Returns if StartTLS is enforced to secure the connection, i.e. no
     * fallback is used (plain SMTP) - standard getter
     *
     * @return True if StartTLS is enforced
     */
    public boolean isEnforceStartTLS() {
        return enforceStartTLS;
    }

    /**
     * Returns headers for current message - standard getter
     *
     * @return CollectionProperty of headers for current message
     */
    public CollectionProperty getHeaders() {
        return headerFields;
    }

    /**
     * Sets headers for current message
     *
     * @param headerFields
     *            CollectionProperty of headers for current message
     */
    public void setHeaderFields(CollectionProperty headerFields) {
        this.headerFields = headerFields;
    }

    /**
     * Adds a header-part to current HashMap of headers - to be called by
     * SmtpSampler-object
     *
     * @param headerName
     *            Key for current header
     * @param headerValue
     *            Value for current header
     */
    public void addHeader(String headerName, String headerValue) {
        if (this.headerFields == null){
            this.headerFields = new CollectionProperty();
        }
        Argument argument = new Argument(headerName, headerValue);
        this.headerFields.addItem(argument);
    }

    /**
     * Deletes all current headers in HashMap
     */
    public void clearHeaders() {
        if (this.headerFields == null){
            this.headerFields = new CollectionProperty();
        }else{
            this.headerFields.clear();
        }
    }

    /**
     * Returns all attachment for current message - standard getter
     *
     * @return List of attachments for current message
     */
    public List<File> getAttachments() {
        return attachments;
    }

    /**
     * Adds attachments to current message
     *
     * @param attachments
     *            List of files to be added as attachments to current message
     */
    public void setAttachments(List<File> attachments) {
        this.attachments = attachments;
    }

    /**
     * Adds an attachment to current message - to be called by
     * SmtpSampler-object
     *
     * @param attachment
     *            File-object to be added as attachment to current message
     */
    public void addAttachment(File attachment) {
        this.attachments.add(attachment);
    }

    /**
     * Clear all attachments for current message
     */
    public void clearAttachments() {
        this.attachments.clear();
    }

    /**
     * Returns if synchronous-mode is used for current message (i.e. time for
     * delivery, ... is measured) - standard getter
     *
     * @return True if synchronous-mode is used
     */
    public boolean isSynchronousMode() {
        return synchronousMode;
    }

    /**
     * Sets the use of synchronous-mode (i.e. time for delivery, ... is
     * measured) - to be called by SmtpSampler-object
     *
     * @param synchronousMode
     *            Should synchronous-mode be used?
     */
    public void setSynchronousMode(boolean synchronousMode) {
        this.synchronousMode = synchronousMode;
    }

    /**
     * Returns which protocol should be used to transport message (smtps for
     * SSL-secured connections or smtp for plain SMTP / StartTLS)
     *
     * @return Protocol that is used to transport message
     */
    private String getProtocol() {
        return (useSSL) ? "smtps" : "smtp";
    }

    /**
     * Returns port to be used for SMTP-connection - returns the
     * default port for the protocol if no port has been supplied.
     *
     * @return Port to be used for SMTP-connection
     */
    private String getPort() {
        String port = smtpPort.trim();
        if (port.length() > 0) { // OK, it has been supplied
            return port;
        }
        if (useSSL){
            return "465";
        }
        if (useStartTLS) {
            return "587";
        }
        return "25";
    }

    /**
     * Assigns the object to use a local truststore for SSL / StartTLS - to be
     * called by SmtpSampler-object
     *
     * @param useLocalTrustStore
     *            Should a local truststore be used?
     */
    public void setUseLocalTrustStore(boolean useLocalTrustStore) {
        this.useLocalTrustStore = useLocalTrustStore;
    }

    /**
     * Sets the path to the local truststore to be used for SSL / StartTLS - to
     * be called by SmtpSampler-object
     *
     * @param trustStoreToUse
     *            Path to local truststore
     */
    public void setTrustStoreToUse(String trustStoreToUse) {
        this.trustStoreToUse = trustStoreToUse;
    }

    public void setUseEmlMessage(boolean sendEmlMessage) {
        this.sendEmlMessage = sendEmlMessage;
    }

    /**
     * Sets eml-message to be sent
     *
     * @param emlMessage
     *            path to eml-message
     */
    public void setEmlMessage(String emlMessage) {
        this.emlMessage = emlMessage;
    }

    /**
     * Set the mail body.
     *
     * @param body
     */
    public void setMailBody(String body){
        mailBody = body;
    }
    
    /**
     * Set whether to send a plain body (i.e. not multipart/mixed)
     *
     * @param plainBody <code>true</code> if sending a plain body (i.e. not multipart/mixed)
     */
    public void setPlainBody(boolean plainBody){
        this.plainBody = plainBody;
    }

    public String getServerResponse() {
        return this.serverResponse.toString();
    }

    public void setEnableDebug(boolean selected) {
        enableDebug = selected;

    }

    public void setReplyTo(List<InternetAddress> replyTo) {
        this.replyTo = replyTo;
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/770.java