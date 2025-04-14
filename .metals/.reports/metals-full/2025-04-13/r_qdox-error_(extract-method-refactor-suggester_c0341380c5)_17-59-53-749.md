error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3427.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3427.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3427.java
text:
```scala
public H@@ttpServletResponse getFacade() ;

/*
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * [Additional notices, if required by prior licensing conditions]
 *
 */


package org.apache.tomcat.core;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.tomcat.util.*;

/**
 *
 * @author James Duncan Davidson [duncan@eng.sun.com]
 * @author Jason Hunter [jch@eng.sun.com]
 * @author James Todd [gonzo@eng.sun.com]
 * @author Harish Prabandham
 * @author Hans Bergsten <hans@gefionsoftware.com>
 * @author costin@dnt.ro
 */
public interface Response {
    // -------------------- Headers -------------------- 
    public boolean containsHeader(String name) ;

    public void setHeader(String name, String value) ;

    public void addHeader(String name, String value) ;

    /** Signal that we're done with the headers, and body will follow.
     *  Any implementation needs to notify ContextManager, to allow
     *  interceptors to fix headers.
     */
    public void endHeaders() throws IOException;

    // -------------------- Output method --------------------
    /** True if getOutputStream or getWriter was called.
     *  XXX change it to "if any output was writen"
     *
     *  Used by RD.forward() and ServletWrapper.error()
     */
    public boolean isStarted() ;

    /** True if getOutputStream was called.
     *  Used to avoid the ugly try getWriter() catch getOutputStream.
     */
    public boolean isUsingStream();

    /** The output stream is used.
     */
    public void setUsingStream( boolean stream );

    /** Stream/Writer control
     */
    public boolean isUsingWriter();

    /** Stream/Writer control
     */
    public void setUsingWriter( boolean writer );

    
    /** Signal that we're done with a particular request, the
     *	server can go on and read more requests or close the socket
     */
    public void finish() throws IOException ;

    /** Either re-implement getOutputStream or return BufferedServletOutputStream(this)
     *  and implement doWrite();
     */
    public ServletOutputStream getOutputStream() ;

    public PrintWriter getWriter() throws IOException ;

    /** True if we are in an included servlet
     */
    public boolean isIncluded();

    /** The response will not set any header or the status line -
     *  it can only write to the output stream or flush.
     *  This is used to implement RD.include() and can be used for
     *  HTTP/0.9
     */
    public void setIncluded(boolean b);
	
    
    // -------------------- Buffering --------------------
    
    public int getBufferSize() ;

    public void setBufferSize(int size) throws IllegalStateException ;

    public boolean isBufferCommitted() ;

    public void reset() throws IllegalStateException ;

    // Reset the response buffer but not headers and cookies
    public void resetBuffer() throws IllegalStateException ;

    /** Any implementation needs to notify ContextManger
     */
    public void flushBuffer() throws IOException ;

    // -------------------- Cookies --------------------

    public void addCookie(Cookie cookie) ;

    public Enumeration getCookies();

    // -------------------- Response properties --------------------
    // Note: headers are not set when you invoke the methods, but
    // later, when final fixHeaders happens ( before sending the body )
    
    public Locale getLocale() ;

    public void setLocale(Locale locale) ;

    /**  translate locale into encoding. 
     */
    public String getCharacterEncoding() ;

    /** Set content type - this might also set encoding, if specified
     */
    public void setContentType(String contentType) ;

    public String getContentType();

    public void setContentLength(int contentLength) ;

    public int getContentLength() ;

    public void setStatus(int status);

    public int getStatus() ;

    /** Will set the session id. The session interceptor might
     *  process it and add a Cookie header, and it can be used to
     *  rewrite URLs.
     *  This replace "system cookies" ( it was the only use for them )
     */
    public void setSessionId(String sId );
    
    public String getSessionId( );
    
    // -------------------- Internal methods --------------------
    /** One-to-one with Facade.
     *  You can use HttpResponseFacade.
     */
    public HttpServletResponseFacade getFacade() ;

    /** One-to-one relation with Request
     */
    public void setRequest(Request request) ;
    
    public Request getRequest() ;

    /** Response objects will be pool-able
     */
    public void recycle() ;


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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3427.java