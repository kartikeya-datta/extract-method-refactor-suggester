error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2281.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2281.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[28,1]

error in qdox parser
file content:
```java
offset: 910
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2281.java
text:
```scala
class MailPrintStream extends PrintStream {

/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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

/*
 * The original version of this class was donated by Jason Hunter,
 * who wrote the class as part of the com.oreilly.servlet
 * package for his book "Java Servlet Programming" (O'Reilly).
 * See http://www.servlets.com.
 *
 */

p@@ackage org.apache.tools.mail;

import java.io.IOException;
import java.io.PrintStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.InetAddress;
import java.util.Vector;
import java.util.Enumeration;

/**
 * A class to help send SMTP email.
 * This class is an improvement on the sun.net.smtp.SmtpClient class
 * found in the JDK.  This version has extra functionality, and can be used
 * with JVMs that did not extend from the JDK.  It's not as robust as
 * the JavaMail Standard Extension classes, but it's easier to use and
 * easier to install, and has an Open Source license.
 * <p>
 * It can be used like this:
 * <blockquote><pre>
 * String mailhost = "localhost";  // or another mail host
 * String from = "Mail Message Servlet &lt;MailMessage@server.com&gt;";
 * String to = "to@you.com";
 * String cc1 = "cc1@you.com";
 * String cc2 = "cc2@you.com";
 * String bcc = "bcc@you.com";
 * &nbsp;
 * MailMessage msg = new MailMessage(mailhost);
 * msg.setPort(25);
 * msg.from(from);
 * msg.to(to);
 * msg.cc(cc1);
 * msg.cc(cc2);
 * msg.bcc(bcc);
 * msg.setSubject("Test subject");
 * PrintStream out = msg.getPrintStream();
 * &nbsp;
 * Enumeration enum = req.getParameterNames();
 * while (enum.hasMoreElements()) {
 *   String name = (String)enum.nextElement();
 *   String value = req.getParameter(name);
 *   out.println(name + " = " + value);
 * }
 * &nbsp;
 * msg.sendAndClose();
 * </pre></blockquote>
 * <p>
 * Be sure to set the from address, then set the recepient
 * addresses, then set the subject and other headers, then get the
 * PrintStream, then write the message, and finally send and close.
 * The class does minimal error checking internally; it counts on the mail
 * host to complain if there's any malformatted input or out of order
 * execution.
 * <p>
 * An attachment mechanism based on RFC 1521 could be implemented on top of
 * this class.  In the meanwhile, JavaMail is the best solution for sending
 * email with attachments.
 * <p>
 * Still to do:
 * <ul>
 * <li>Figure out how to close the connection in case of error
 * </ul>
 *
 * @version 1.1, 2000/03/19, added angle brackets to address, helps some servers
 * version 1.0, 1999/12/29
 */
public class MailMessage {

    /** default mailhost */
    public static final String DEFAULT_HOST = "localhost";

    /** default port for SMTP: 25 */
    public static final int DEFAULT_PORT = 25;

    /** host name for the mail server */
    private String host;

    /** host port for the mail server */
    private int port = DEFAULT_PORT;

    /** sender email address */
    private String from;

    /** list of email addresses to reply to */
    private Vector replyto;

    /** list of email addresses to send to */
    private Vector to;

    /** list of email addresses to cc to */
    private Vector cc;

    /** headers to send in the mail */
    private Vector headersKeys;
    private Vector headersValues;

    private MailPrintStream out;

    private SmtpResponseReader in;

    private Socket socket;
    private static final int OK_READY = 220;
    private static final int OK_HELO = 250;
    private static final int OK_FROM = 250;
    private static final int OK_RCPT_1 = 250;
    private static final int OK_RCPT_2 = 251;
    private static final int OK_DATA = 354;
    private static final int OK_DOT = 250;
    private static final int OK_QUIT = 221;

  /**
   * Constructs a new MailMessage to send an email.
   * Use localhost as the mail server with port 25.
   *
   * @exception IOException if there's any problem contacting the mail server
   */
  public MailMessage() throws IOException {
    this(DEFAULT_HOST, DEFAULT_PORT);
  }

  /**
   * Constructs a new MailMessage to send an email.
   * Use the given host as the mail server with port 25.
   *
   * @param host the mail server to use
   * @exception IOException if there's any problem contacting the mail server
   */
  public MailMessage(String host) throws IOException {
    this(host, DEFAULT_PORT);
  }

  /**
   * Constructs a new MailMessage to send an email.
   * Use the given host and port as the mail server.
   *
   * @param host the mail server to use
   * @param port the port to connect to
   * @exception IOException if there's any problem contacting the mail server
   */
  public MailMessage(String host, int port) throws IOException {
    this.port = port;
    this.host = host;
    replyto = new Vector();
    to = new Vector();
    cc = new Vector();
    headersKeys = new Vector();
    headersValues = new Vector();
    connect();
    sendHelo();
  }

    /**
     * Set the port to connect to the SMTP host.
     * @param port the port to use for connection.
     * @see #DEFAULT_PORT
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Sets the from address.  Also sets the "From" header.  This method should
     * be called only once.
     * @param from the from address
     * @exception IOException if there's any problem reported by the mail server
     */
    public void from(String from) throws IOException {
        sendFrom(from);
        this.from = from;
    }

    /**
     * Sets the replyto address
     * This method may be
     * called multiple times.
     * @param rto the replyto address
     *
     */
    public void replyto(String rto) {
      this.replyto.addElement(rto);
    }

  /**
   * Sets the to address.  Also sets the "To" header.  This method may be
   * called multiple times.
   *
   * @param to the to address
   * @exception IOException if there's any problem reported by the mail server
   */
  public void to(String to) throws IOException {
    sendRcpt(to);
    this.to.addElement(to);
  }

  /**
   * Sets the cc address.  Also sets the "Cc" header.  This method may be
   * called multiple times.
   *
   * @param cc the cc address
   * @exception IOException if there's any problem reported by the mail server
   */
  public void cc(String cc) throws IOException {
    sendRcpt(cc);
    this.cc.addElement(cc);
  }

  /**
   * Sets the bcc address.  Does NOT set any header since it's a *blind* copy.
   * This method may be called multiple times.
   *
   * @param bcc the bcc address
   * @exception IOException if there's any problem reported by the mail server
   */
  public void bcc(String bcc) throws IOException {
    sendRcpt(bcc);
    // No need to keep track of Bcc'd addresses
  }

  /**
   * Sets the subject of the mail message.  Actually sets the "Subject"
   * header.
   * @param subj the subject of the mail message
   */
  public void setSubject(String subj) {
    setHeader("Subject", subj);
  }

  /**
   * Sets the named header to the given value.  RFC 822 provides the rules for
   * what text may constitute a header name and value.
   * @param name name of the header
   * @param value contents of the header
   */
  public void setHeader(String name, String value) {
    // Blindly trust the user doesn't set any invalid headers
    headersKeys.add(name);
    headersValues.add(value);
  }

  /**
   * Returns a PrintStream that can be used to write the body of the message.
   * A stream is used since email bodies are byte-oriented.  A writer can
   * be wrapped on top if necessary for internationalization.
   * This is actually done in Message.java
   *
   * @return a printstream containing the data and the headers of the email
   * @exception IOException if there's any problem reported by the mail server
   * @see org.apache.tools.ant.taskdefs.email.Message
   */
  public PrintStream getPrintStream() throws IOException {
    setFromHeader();
    setReplyToHeader();
    setToHeader();
    setCcHeader();
    setHeader("X-Mailer", "org.apache.tools.mail.MailMessage (ant.apache.org)");
    sendData();
    flushHeaders();
    return out;
  }


  // RFC 822 s4.1: "From:" header must be sent
  // We rely on error checking by the MTA
  void setFromHeader() {
    setHeader("From", from);
  }

  // RFC 822 s4.1: "Reply-To:" header is optional
  void setReplyToHeader() {
    if (!replyto.isEmpty()) {
      setHeader("Reply-To", vectorToList(replyto));
    }
  }

  void setToHeader() {
    if (!to.isEmpty()) {
      setHeader("To", vectorToList(to));
    }
  }

  void setCcHeader() {
    if (!cc.isEmpty()) {
      setHeader("Cc", vectorToList(cc));
    }
  }

  String vectorToList(Vector v) {
    StringBuffer buf = new StringBuffer();
    Enumeration e = v.elements();
    while (e.hasMoreElements()) {
      buf.append(e.nextElement());
      if (e.hasMoreElements()) {
        buf.append(", ");
      }
    }
    return buf.toString();
  }

  void flushHeaders() throws IOException {
    // RFC 822 s4.1:
    //   "Header fields are NOT required to occur in any particular order,
    //    except that the message body MUST occur AFTER the headers"
    // (the same section specifies a reccommended order, which we ignore)
   for (int i = 0; i < headersKeys.size(); i++) {
      String name = (String) headersKeys.elementAt(i);
      String value = (String) headersValues.elementAt(i);
      out.println(name + ": " + value);
    }
    out.println();
    out.flush();
  }

  /**
   * Sends the message and closes the connection to the server.
   * The MailMessage object cannot be reused.
   *
   * @exception IOException if there's any problem reported by the mail server
   */
  public void sendAndClose() throws IOException {
      try {
          sendDot();
          sendQuit();
      } finally {
          disconnect();
      }
  }

  // Make a limited attempt to extract a sanitized email address
  // Prefer text in <brackets>, ignore anything in (parentheses)
  static String sanitizeAddress(String s) {
    int paramDepth = 0;
    int start = 0;
    int end = 0;
    int len = s.length();

    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      if (c == '(') {
        paramDepth++;
        if (start == 0) {
          end = i;  // support "address (name)"
        }
      } else if (c == ')') {
        paramDepth--;
        if (end == 0) {
          start = i + 1;  // support "(name) address"
        }
      } else if (paramDepth == 0 && c == '<') {
        start = i + 1;
      } else if (paramDepth == 0 && c == '>') {
        end = i;
      }
    }

    if (end == 0) {
      end = len;
    }

    return s.substring(start, end);
  }

  // * * * * * Raw protocol methods below here * * * * *

  void connect() throws IOException {
    socket = new Socket(host, port);
    out = new MailPrintStream(
          new BufferedOutputStream(
          socket.getOutputStream()));
    in = new SmtpResponseReader(socket.getInputStream());
    getReady();
  }

  void getReady() throws IOException {
    String response = in.getResponse();
    int[] ok = {OK_READY};
    if (!isResponseOK(response, ok)) {
      throw new IOException(
        "Didn't get introduction from server: " + response);
    }
  }
  void sendHelo() throws IOException {
    String local = InetAddress.getLocalHost().getHostName();
    int[] ok = {OK_HELO};
    send("HELO " + local, ok);
  }
  void sendFrom(String from) throws IOException {
    int[] ok = {OK_FROM};
    send("MAIL FROM: " + "<" + sanitizeAddress(from) + ">", ok);
  }
  void sendRcpt(String rcpt) throws IOException {
    int[] ok = {OK_RCPT_1, OK_RCPT_2};
    send("RCPT TO: " + "<" + sanitizeAddress(rcpt) + ">", ok);
  }

  void sendData() throws IOException {
    int[] ok = {OK_DATA};
    send("DATA", ok);
  }

  void sendDot() throws IOException {
    int[] ok = {OK_DOT};
    send("\r\n.", ok);  // make sure dot is on new line
  }

    void sendQuit() throws IOException {
        int[] ok = {OK_QUIT};
        try {
            send("QUIT", ok);
        } catch (IOException e) {
            throw new ErrorInQuitException(e);
        }
    }

    void send(String msg, int[] ok) throws IOException {
        out.rawPrint(msg + "\r\n");  // raw supports <CRLF>.<CRLF>
        String response = in.getResponse();
        if (!isResponseOK(response, ok)) {
            throw new IOException("Unexpected reply to command: "
                                  + msg + ": " + response);
        }
    }

  boolean isResponseOK(String response, int[] ok) {
    // Check that the response is one of the valid codes
    for (int i = 0; i < ok.length; i++) {
      if (response.startsWith("" + ok[i])) {
        return true;
      }
    }
    return false;
  }

    void disconnect() throws IOException {
        if (out != null) {
            out.close();
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                // ignore
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}

/**
 * This PrintStream subclass makes sure that <CRLF>. becomes <CRLF>..
 *  per RFC 821.  It also ensures that new lines are always \r\n.
*/
class MailPrintStream extends java.io.PrintStream {

  private int lastChar;

  public MailPrintStream(OutputStream out) {
    super(out, true);  // deprecated, but email is byte-oriented
  }

  // Mac does \n\r, but that's tough to distinguish from Windows \r\n\r\n.
  // Don't tackle that problem right now.
  public void write(int b) {
    if (b == '\n' && lastChar != '\r') {
      rawWrite('\r');  // ensure always \r\n
      rawWrite(b);
    } else if (b == '.' && lastChar == '\n') {
      rawWrite('.');  // add extra dot
      rawWrite(b);
    } else {
      rawWrite(b);
    }
    lastChar = b;
  }

  public void write(byte[] buf, int off, int len) {
    for (int i = 0; i < len; i++) {
      write(buf[off + i]);
    }
  }

  void rawWrite(int b) {
    super.write(b);
  }

  void rawPrint(String s) {
    int len = s.length();
    for (int i = 0; i < len; i++) {
      rawWrite(s.charAt(i));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2281.java