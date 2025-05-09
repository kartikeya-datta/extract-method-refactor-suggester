error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/354.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/354.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/354.java
text:
```scala
S@@tringManager.getManager("org.apache.tomcat.util.buf.res");

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

package org.apache.tomcat.util.buf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.text.*;

import org.apache.tomcat.util.res.StringManager;

/**
 *  Common place for date utils.
 *
 * @deprecated Will be replaced with a more efficient impl, based on
 * FastDateFormat, with an API using less objects.
 * @author dac@eng.sun.com
 * @author Jason Hunter [jch@eng.sun.com]
 * @author James Todd [gonzo@eng.sun.com]
 * @author Costin Manolache
 */
public class DateTool {

    /** US locale - all HTTP dates are in english
     */
    private final static Locale LOCALE_US = Locale.US;

    /** GMT timezone - all HTTP dates are on GMT
     */
    private final static TimeZone GMT_ZONE = TimeZone.getTimeZone("GMT");

    /** format for RFC 1123 date string -- "Sun, 06 Nov 1994 08:49:37 GMT"
     */
    private final static String RFC1123_PATTERN =
        "EEE, dd MMM yyyy HH:mm:ss z";

    // format for RFC 1036 date string -- "Sunday, 06-Nov-94 08:49:37 GMT"
    private final static String rfc1036Pattern =
        "EEEEEEEEE, dd-MMM-yy HH:mm:ss z";

    // format for C asctime() date string -- "Sun Nov  6 08:49:37 1994"
    private final static String asctimePattern =
        "EEE MMM d HH:mm:ss yyyy";

    /** Pattern used for old cookies
     */
    private final static String OLD_COOKIE_PATTERN = "EEE, dd-MMM-yyyy HH:mm:ss z";

    /** DateFormat to be used to format dates. Called from MessageBytes
     */
    private final static DateFormat rfc1123Format =
	new SimpleDateFormat(RFC1123_PATTERN, LOCALE_US);
    
    /** DateFormat to be used to format old netscape cookies
	Called from ServerCookie
     */
    private final static DateFormat oldCookieFormat =
	new SimpleDateFormat(OLD_COOKIE_PATTERN, LOCALE_US);
    
    private final static DateFormat rfc1036Format =
	new SimpleDateFormat(rfc1036Pattern, LOCALE_US);
    
    private final static DateFormat asctimeFormat =
	new SimpleDateFormat(asctimePattern, LOCALE_US);
    
    static {
	rfc1123Format.setTimeZone(GMT_ZONE);
	oldCookieFormat.setTimeZone(GMT_ZONE);
	rfc1036Format.setTimeZone(GMT_ZONE);
	asctimeFormat.setTimeZone(GMT_ZONE);
    }
 
    private static StringManager sm =
        StringManager.getManager("org.apache.tomcat.resources");

    // Called from MessageBytes.getTime()
    static long parseDate( MessageBytes value ) {
     	return parseDate( value.toString());
    }

    // Called from MessageBytes.setTime
    /** 
     */
    public static String format1123( Date d ) {
	return rfc1123Format.format( d );
    } 


    // Called from ServerCookie
    /** 
     */
    public static void formatOldCookie( Date d, StringBuffer sb,
					  FieldPosition fp )
    {
	oldCookieFormat.format( d, sb, fp );
    }

    // Called from ServerCookie
    public static String formatOldCookie( Date d )
    {
	return oldCookieFormat.format( d );
    }

    
    /** Called from HttpServletRequest.getDateHeader().
	Not efficient - but not very used.
     */
    public static long parseDate( String dateString ) {
	Date date=null;
        try {
            date = DateTool.rfc1123Format.parse(dateString);
	    return date.getTime();
	} catch (ParseException e) { }
          catch (StringIndexOutOfBoundsException e) { }
	
        try {
	    date = DateTool.rfc1036Format.parse(dateString);
	    return date.getTime();
	} catch (ParseException e) { }
          catch (StringIndexOutOfBoundsException e) { }
	
        try {
            date = DateTool.asctimeFormat.parse(dateString);
	    return date.getTime();
        } catch (ParseException pe) { }
          catch (StringIndexOutOfBoundsException e) { }
	String msg = sm.getString("httpDate.pe", dateString);
	throw new IllegalArgumentException(msg);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/354.java