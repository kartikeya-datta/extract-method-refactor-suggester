error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17192.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17192.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17192.java
text:
```scala
V@@ector<File> files = new Vector<File>();

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant.taskdefs.email;

import java.io.File;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.ClasspathUtils;

/**
 * A task to send SMTP email. This is a refactoring of the SendMail and
 * MimeMail tasks such that both are within a single task.
 *
 * @since Ant 1.5
 * @ant.task name="mail" category="network"
 */
public class EmailTask extends Task {
    private static final int SMTP_PORT = 25;

    /** Constant to show that the best available mailer should be used.  */
    public static final String AUTO = "auto";
    /** Constant to allow the Mime mailer to be requested  */
    public static final String MIME = "mime";
    /** Constant to allow the UU mailer to be requested  */
    public static final String UU = "uu";
    /** Constant to allow the plaintext mailer to be requested  */
    public static final String PLAIN = "plain";

    /**
     * Enumerates the encoding constants.
     */
    public static class Encoding extends EnumeratedAttribute {
        /**
         * finds the valid encoding values
         *
         * @return a list of valid entries
         */
        public String[] getValues() {
            return new String[] {AUTO, MIME, UU, PLAIN};
        }
    }

    private String encoding = AUTO;
    /** host running SMTP  */
    private String host = "localhost";
    private Integer port = null;
    /** subject field  */
    private String subject = null;
    /** any text  */
    private Message message = null;
    /** failure flag */
    private boolean failOnError = true;
    private boolean includeFileNames = false;
    private String messageMimeType = null;
    /* special headers */
    /** sender  */
    private EmailAddress from = null;
    /** replyto */
    private Vector replyToList = new Vector();
    /** TO recipients  */
    private Vector toList = new Vector();
    /** CC (Carbon Copy) recipients  */
    private Vector ccList = new Vector();
    /** BCC (Blind Carbon Copy) recipients  */
    private Vector bccList = new Vector();

    /** generic headers */
    private Vector headers = new Vector();

    /** file list  */
    private Path attachments = null;
    /** Character set for MimeMailer*/
    private String charset = null;
    /** User for SMTP auth */
    private String user = null;
    /** Password for SMTP auth */
    private String password = null;
    /** indicate if the user wishes SSL-TLS */
    private boolean ssl = false;
    /** indicate if the user wishes support for STARTTLS */
    private boolean starttls = false;

    /** ignore invalid recipients? */
    private boolean ignoreInvalidRecipients = false;

    /**
     * Set the user for SMTP auth; this requires JavaMail.
     * @param user the String username.
     * @since Ant 1.6
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Set the password for SMTP auth; this requires JavaMail.
     * @param password the String password.
     * @since Ant 1.6
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set whether to send data over SSL.
     * @param ssl boolean; if true SSL will be used.
     * @since Ant 1.6
     */
    public void setSSL(boolean ssl) {
        this.ssl = ssl;
    }

    /**
     * Set whether to allow authentication to switch to a TLS
     * connection via STARTTLS.
     * @param b boolean; if true STARTTLS will be supported.
     * @since Ant 1.8.0
     */
    public void setEnableStartTLS(boolean b) {
        this.starttls = b;
    }

    /**
     * Set the preferred encoding method.
     *
     * @param encoding The encoding (one of AUTO, MIME, UU, PLAIN).
     */
    public void setEncoding(Encoding encoding) {
        this.encoding = encoding.getValue();
    }

    /**
     * Set the mail server port.
     *
     * @param port The port to use.
     */
    public void setMailport(int port) {
        this.port = new Integer(port);
    }

    /**
     * Set the host.
     *
     * @param host The host to connect to.
     */
    public void setMailhost(String host) {
        this.host = host;
    }

    /**
     * Set the subject line of the email.
     *
     * @param subject Subject of this email.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Shorthand method to set the message.
     *
     * @param message Message body of this email.
     */
    public void setMessage(String message) {
        if (this.message != null) {
            throw new BuildException("Only one message can be sent in an "
                 + "email");
        }
        this.message = new Message(message);
        this.message.setProject(getProject());
    }

    /**
     * Shorthand method to set the message from a file.
     *
     * @param file The file from which to take the message.
     */
    public void setMessageFile(File file) {
        if (this.message != null) {
            throw new BuildException("Only one message can be sent in an "
                 + "email");
        }
        this.message = new Message(file);
        this.message.setProject(getProject());
    }

    /**
     * Shorthand method to set type of the text message, text/plain by default
     * but text/html or text/xml is quite feasible.
     *
     * @param type The new MessageMimeType value.
     */
    public void setMessageMimeType(String type) {
        this.messageMimeType = type;
    }

    /**
     * Add a message element.
     *
     * @param message The message object.
     * @throws BuildException if a message has already been added.
     */
    public void addMessage(Message message) throws BuildException {
        if (this.message != null) {
            throw new BuildException(
                "Only one message can be sent in an email");
        }
        this.message = message;
    }

    /**
     * Add a from address element.
     *
     * @param address The address to send from.
     */
    public void addFrom(EmailAddress address) {
        if (this.from != null) {
            throw new BuildException("Emails can only be from one address");
        }
        this.from = address;
    }

    /**
     * Shorthand to set the from address element.
     *
     * @param address The address to send mail from.
     */
    public void setFrom(String address) {
        if (this.from != null) {
            throw new BuildException("Emails can only be from one address");
        }
        this.from = new EmailAddress(address);
    }

    /**
     * Add a replyto address element.
     *
     * @param address The address to reply to.
     * @since Ant 1.6
     */
    public void addReplyTo(EmailAddress address) {
        this.replyToList.add(address);
    }

    /**
     * Shorthand to set the replyto address element.
     *
     * @param address The address to which replies should be directed.
     * @since Ant 1.6
     */
    public void setReplyTo(String address) {
        this.replyToList.add(new EmailAddress(address));
    }

    /**
     * Add a to address element.
     *
     * @param address An email address.
     */
    public void addTo(EmailAddress address) {
        toList.addElement(address);
    }

    /**
     * Shorthand to set the "to" address element.
     *
     * @param list Comma-separated list of addresses.
     */
    public void setToList(String list) {
        StringTokenizer tokens = new StringTokenizer(list, ",");

        while (tokens.hasMoreTokens()) {
            toList.addElement(new EmailAddress(tokens.nextToken()));
        }
    }

    /**
     * Add a "cc" address element.
     *
     * @param address The email address.
     */
    public void addCc(EmailAddress address) {
        ccList.addElement(address);
    }

    /**
     * Shorthand to set the "cc" address element.
     *
     * @param list Comma separated list of addresses.
     */
    public void setCcList(String list) {
        StringTokenizer tokens = new StringTokenizer(list, ",");

        while (tokens.hasMoreTokens()) {
            ccList.addElement(new EmailAddress(tokens.nextToken()));
        }
    }

    /**
     * Add a "bcc" address element.
     *
     * @param address The email address.
     */
    public void addBcc(EmailAddress address) {
        bccList.addElement(address);
    }

    /**
     * Shorthand to set the "bcc" address element.
     *
     * @param list comma separated list of addresses.
     */
    public void setBccList(String list) {
        StringTokenizer tokens = new StringTokenizer(list, ",");

        while (tokens.hasMoreTokens()) {
            bccList.addElement(new EmailAddress(tokens.nextToken()));
        }
    }

    /**
     * Set whether BuildExceptions should be passed back to the core.
     *
     * @param failOnError The new FailOnError value.
     */
    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    /**
     * Set the list of files to be attached.
     *
     * @param filenames Comma-separated list of files.
     */
    public void setFiles(String filenames) {
        StringTokenizer t = new StringTokenizer(filenames, ", ");

        while (t.hasMoreTokens()) {
            createAttachments()
                .add(new FileResource(getProject().resolveFile(t.nextToken())));
        }
    }

    /**
     * Add a set of files (nested fileset attribute).
     *
     * @param fs The fileset.
     */
    public void addFileset(FileSet fs) {
        createAttachments().add(fs);
    }

    /**
     * Creates a Path as container for attachments.  Supports any
     * filesystem resource-collections that way.
     * @return the path to be configured.
     * @since Ant 1.7
     */
    public Path createAttachments() {
        if (attachments == null) {
            attachments = new Path(getProject());
        }
        return attachments.createPath();
    }

    /**
     * Create a nested header element.
     * @return a Header instance.
     */
    public Header createHeader() {
        Header h = new Header();
        headers.add(h);
        return h;
    }

    /**
     * Set whether to include filenames.
     *
     * @param includeFileNames Whether to include filenames in the text of the
     *      message.
     */
    public void setIncludefilenames(boolean includeFileNames) {
        this.includeFileNames = includeFileNames;
    }

    /**
     * Get whether file names should be included.
     *
     * @return Identifies whether file names should be included.
     */
    public boolean getIncludeFileNames() {
        return includeFileNames;
    }

    /**
     * Whether invalid recipients should be ignored (but a warning
     * will be logged) instead of making the task fail.
     *
     * <p>Even with this property set to true the task will still fail
     * if the mail couldn't be sent to any recipient at all.</p>
     *
     * @since Ant 1.8.0
     */
    public void setIgnoreInvalidRecipients(boolean b) {
        ignoreInvalidRecipients = b;
    }

    /**
     * Send an email.
     */
    public void execute() {
        Message savedMessage = message;

        try {
            Mailer mailer = null;

            // prepare for the auto select mechanism
            boolean autoFound = false;
            // try MIME format
            if (encoding.equals(MIME)
 (encoding.equals(AUTO) && !autoFound)) {
                try {
                    //check to make sure that activation.jar 
                    //and mail.jar are available - see bug 31969
                    Class.forName("javax.activation.DataHandler");
                    Class.forName("javax.mail.internet.MimeMessage");

                    mailer = (Mailer) ClasspathUtils.newInstance(
                            "org.apache.tools.ant.taskdefs.email.MimeMailer",
                            EmailTask.class.getClassLoader(), Mailer.class);
                    autoFound = true;

                    log("Using MIME mail", Project.MSG_VERBOSE);
                } catch (BuildException e) {
                    logBuildException("Failed to initialise MIME mail: ", e);
                }
            }
            // SMTP auth only allowed with MIME mail
            if (!autoFound && ((user != null) || (password != null))
                && (encoding.equals(UU) || encoding.equals(PLAIN))) {
                throw new BuildException("SMTP auth only possible with MIME mail");
            }
            // SSL only allowed with MIME mail
            if (!autoFound  && (ssl || starttls)
                && (encoding.equals(UU) || encoding.equals(PLAIN))) {
                throw new BuildException("SSL and STARTTLS only possible with"
                                         + " MIME mail");
            }
            // try UU format
            if (encoding.equals(UU)
 (encoding.equals(AUTO) && !autoFound)) {
                try {
                    mailer = (Mailer) ClasspathUtils.newInstance(
                            "org.apache.tools.ant.taskdefs.email.UUMailer",
                            EmailTask.class.getClassLoader(), Mailer.class);
                    autoFound = true;
                    log("Using UU mail", Project.MSG_VERBOSE);
                } catch (BuildException e) {
                    logBuildException("Failed to initialise UU mail: ", e);
                }
            }
            // try plain format
            if (encoding.equals(PLAIN)
 (encoding.equals(AUTO) && !autoFound)) {
                mailer = new PlainMailer();
                autoFound = true;
                log("Using plain mail", Project.MSG_VERBOSE);
            }
            // a valid mailer must be present by now
            if (mailer == null) {
                throw new BuildException("Failed to initialise encoding: "
                     + encoding);
            }
            // a valid message is required
            if (message == null) {
                message = new Message();
                message.setProject(getProject());
            }
            // an address to send from is required
            if (from == null || from.getAddress() == null) {
                throw new BuildException("A from element is required");
            }
            // at least one address to send to/cc/bcc is required
            if (toList.isEmpty() && ccList.isEmpty() && bccList.isEmpty()) {
                throw new BuildException("At least one of to, cc or bcc must "
                     + "be supplied");
            }
            // set the mimetype if not done already (and required)
            if (messageMimeType != null) {
                if (message.isMimeTypeSpecified()) {
                    throw new BuildException("The mime type can only be "
                         + "specified in one location");
                }
                message.setMimeType(messageMimeType);
            }
            // set the character set if not done already (and required)
            if (charset != null) {
                if (message.getCharset() != null) {
                    throw new BuildException("The charset can only be "
                         + "specified in one location");
                }
                message.setCharset(charset);
            }

            // identify which files should be attached
            Vector files = new Vector();
            if (attachments != null) {
                for (Resource r : attachments) {
                    files.addElement(r.as(FileProvider.class)
                                     .getFile());
                }
            }
            // let the user know what's going to happen
            log("Sending email: " + subject, Project.MSG_INFO);
            log("From " + from, Project.MSG_VERBOSE);
            log("ReplyTo " + replyToList, Project.MSG_VERBOSE);
            log("To " + toList, Project.MSG_VERBOSE);
            log("Cc " + ccList, Project.MSG_VERBOSE);
            log("Bcc " + bccList, Project.MSG_VERBOSE);

            // pass the params to the mailer
            mailer.setHost(host);
            if (port != null) {
                mailer.setPort(port.intValue());
                mailer.setPortExplicitlySpecified(true);
            } else {
                mailer.setPort(SMTP_PORT);
                mailer.setPortExplicitlySpecified(false);
            }
            mailer.setUser(user);
            mailer.setPassword(password);
            mailer.setSSL(ssl);
            mailer.setEnableStartTLS(starttls);
            mailer.setMessage(message);
            mailer.setFrom(from);
            mailer.setReplyToList(replyToList);
            mailer.setToList(toList);
            mailer.setCcList(ccList);
            mailer.setBccList(bccList);
            mailer.setFiles(files);
            mailer.setSubject(subject);
            mailer.setTask(this);
            mailer.setIncludeFileNames(includeFileNames);
            mailer.setHeaders(headers);
            mailer.setIgnoreInvalidRecipients(ignoreInvalidRecipients);

            // send the email
            mailer.send();

            // let the user know what happened
            int count = files.size();

            log("Sent email with " + count + " attachment"
                 + (count == 1 ? "" : "s"), Project.MSG_INFO);
        } catch (BuildException e) {
            logBuildException("Failed to send email: ", e);
            if (failOnError) {
                throw e;
            }
        } catch (Exception e) {
          log("Failed to send email: " + e.getMessage(), Project.MSG_WARN);
          if (failOnError) {
            throw new BuildException(e);
          }
        } finally {
            message = savedMessage;
        }
    }

    private void logBuildException(String reason, BuildException e) {
        Throwable t = e.getCause() == null ? e : e.getCause();
        log(reason + t.getMessage(), Project.MSG_WARN);
    }

    /**
     * Sets the character set of mail message.
     * Will be ignored if mimeType contains ....; Charset=... substring or
     * encoding is not a <code>mime</code>.
     * @param charset the character encoding to use.
     * @since Ant 1.6
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * Returns the character set of mail message.
     *
     * @return Charset of mail message.
     * @since Ant 1.6
     */
    public String getCharset() {
        return charset;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17192.java