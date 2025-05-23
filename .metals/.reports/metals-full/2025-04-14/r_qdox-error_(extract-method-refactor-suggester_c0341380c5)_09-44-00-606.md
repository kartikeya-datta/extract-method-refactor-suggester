error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7267.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7267.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7267.java
text:
```scala
private static final S@@tring mangleChar(char ch) {

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2002 The Apache Software Foundation.  All rights
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
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
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
 */
package org.apache.tools.ant.taskdefs.optional.jsp;
import java.io.File;

/**
 * This is a class derived from the Jasper code 
 * (org.apache.jasper.compiler.CommandLineCompiler) to map from a JSP filename
 * to a valid Java classname.
 *
 * @author Steve Loughran
 * @author Danno Ferrin
 */
public class JspNameMangler implements JspMangler {

    /**
     * this is the list of keywords which can not be used as classnames
     */
    public static String[] keywords = {
            "assert",
            "abstract", "boolean", "break", "byte",
            "case", "catch", "char", "class",
            "const", "continue", "default", "do",
            "double", "else", "extends", "final",
            "finally", "float", "for", "goto",
            "if", "implements", "import",
            "instanceof", "int", "interface",
            "long", "native", "new", "package",
            "private", "protected", "public",
            "return", "short", "static", "super",
            "switch", "synchronized", "this",
            "throw", "throws", "transient",
            "try", "void", "volatile", "while"
            };


    /**
     * map from a jsp file to a java filename; does not do packages
     *
     * @param jspFile file
     * @return java filename
     */
    public String mapJspToJavaName(File jspFile) {
        return mapJspToBaseName(jspFile) + ".java";
    }


    /**
     * map from a jsp file to a base name; does not deal with extensions
     *
     * @param jspFile jspFile file
     * @return exensionless potentially remapped name
     */
    private String mapJspToBaseName(File jspFile) {
        String className;
        className = stripExtension(jspFile);

        // since we don't mangle extensions like the servlet does,
        // we need to check for keywords as class names
        for (int i = 0; i < keywords.length; ++i) {
            if (className.equals(keywords[i])) {
                className += "%";
                break;
            }
        }

        // Fix for invalid characters. If you think of more add to the list.
        StringBuffer modifiedClassName = new StringBuffer(className.length());
        // first char is more restrictive than the rest
        char firstChar = className.charAt(0);
        if (Character.isJavaIdentifierStart(firstChar)) {
            modifiedClassName.append(firstChar);
        } else {
            modifiedClassName.append(mangleChar(firstChar));
        }
        // this is the rest
        for (int i = 1; i < className.length(); i++) {
            char subChar = className.charAt(i);
            if (Character.isJavaIdentifierPart(subChar)) {
                modifiedClassName.append(subChar);
            } else {
                modifiedClassName.append(mangleChar(subChar));
            }
        }
        return modifiedClassName.toString();
    }


    /**
     * get short filename from file
     *
     * @param jspFile file in
     * @return file without any jsp extension
     */
    private String stripExtension(File jspFile) {
        String className;
        String filename = jspFile.getName();
        if (filename.endsWith(".jsp")) {
            className = filename.substring(0, filename.length() - 4);
        } else {
            className = filename;
        }
        return className;
    }


    /**
     * definition of the char escaping algorithm
     *
     * @param ch char to mangle
     * @return mangled string; 5 digit hex value 
     */
    private final static String mangleChar(char ch) {

        if (ch == File.separatorChar) {
            ch = '/';
        }
        String s = Integer.toHexString(ch);
        int nzeros = 5 - s.length();
        char[] result = new char[6];
        result[0] = '_';
        for (int i = 1; i <= nzeros; ++i) {
            result[i] = '0';
        }
        int resultIndex = 0;
        for (int i = nzeros + 1; i < 6; ++i) {
            result[i] = s.charAt(resultIndex++);
        }
        return new String(result);
    }

    /**
     * taking in the substring representing the path relative to the source dir
     * return a new string representing the destination path
     * @todo
     */
    public String mapPath(String path) {
        return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7267.java