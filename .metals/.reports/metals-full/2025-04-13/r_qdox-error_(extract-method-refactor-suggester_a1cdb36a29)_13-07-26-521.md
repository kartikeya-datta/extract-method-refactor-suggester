error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8145.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8145.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8145.java
text:
```scala
private final static R@@esources REZ =

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.aut.nativelib;

import java.util.Locale;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;

/**
 * Class to help determining the OS.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:umagesh@apache.org">Magesh Umasankar</a>
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public class Os
{
    private static final Resources REZ =
        ResourceManager.getPackageResources( Os.class );

    private final static String OS_NAME =
        System.getProperty( "os.name" ).toLowerCase( Locale.US );
    private final static String OS_ARCH =
        System.getProperty( "os.arch" ).toLowerCase( Locale.US );
    private final static String OS_VERSION =
        System.getProperty( "os.version" ).toLowerCase( Locale.US );
    private final static String PATH_SEP =
        System.getProperty( "path.separator" );

    /**
     * Private constructor to block instantiation.
     */
    private Os()
    {
    }

    /**
     * Determines if the OS on which Ant is executing matches the given OS
     * version.
     */
    public static boolean isVersion( final String version )
    {
        return isOs( null, null, null, version );
    }

    /**
     * Determines if the OS on which Ant is executing matches the given OS
     * architecture.
     *
     * @param arch Description of Parameter
     * @return The Arch value
     */
    public static boolean isArch( final String arch )
    {
        return isOs( null, null, arch, null );
    }

    /**
     * Determines if the OS on which Ant is executing matches the given OS
     * family.
     *
     * @param family Description of Parameter
     * @return The Family value
     * @since 1.5
     */
    public static boolean isFamily( final String family )
    {
        return isOs( family, null, null, null );
    }

    /**
     * Determines if the OS on which Ant is executing matches the given OS name.
     *
     * @param name Description of Parameter
     * @return The Name value
     * @since 1.7
     */
    public static boolean isName( final String name )
    {
        return isOs( null, name, null, null );
    }

    /**
     * Determines if the OS on which Ant is executing matches the given OS
     * family, name, architecture and version
     *
     * @param family The OS family
     * @param name The OS name
     * @param arch The OS architecture
     * @param version The OS version
     * @return The Os value
     */
    public static boolean isOs( final String family,
                                final String name,
                                final String arch,
                                final String version )
    {
        if( family != null || name != null || arch != null || version != null )
        {
            final boolean isFamily = familyMatches( family );
            final boolean isName = nameMatches( name );
            final boolean isArch = archMatches( arch );
            final boolean isVersion = versionMatches( version );

            return isFamily && isName && isArch && isVersion;
        }
        else
        {
            return false;
        }
    }

    private static boolean versionMatches( final String version )
    {
        boolean isVersion = true;
        if( version != null )
        {
            isVersion = version.equals( OS_VERSION );
        }
        return isVersion;
    }

    private static boolean archMatches( final String arch )
    {
        boolean isArch = true;
        if( arch != null )
        {
            isArch = arch.equals( OS_ARCH );
        }
        return isArch;
    }

    private static boolean nameMatches( final String name )
    {
        boolean isName = true;
        if( name != null )
        {
            isName = name.equals( OS_NAME );
        }
        return isName;
    }

    private static boolean familyMatches( final String family )
    {
        boolean isFamily = true;
        if( family != null )
        {
            if( family.equals( "windows" ) )
            {
                isFamily = OS_NAME.indexOf( "windows" ) > -1;
            }
            else if( family.equals( "os/2" ) )
            {
                isFamily = OS_NAME.indexOf( "os/2" ) > -1;
            }
            else if( family.equals( "netware" ) )
            {
                isFamily = OS_NAME.indexOf( "netware" ) > -1;
            }
            else if( family.equals( "dos" ) )
            {
                isFamily = PATH_SEP.equals( ";" ) && !isFamily( "netware" );
            }
            else if( family.equals( "mac" ) )
            {
                isFamily = OS_NAME.indexOf( "mac" ) > -1;
            }
            else if( family.equals( "unix" ) )
            {
                isFamily = PATH_SEP.equals( ":" ) &&
                    ( !isFamily( "mac" ) || OS_NAME.endsWith( "x" ) );
            }
            else
            {
                final String message = REZ.getString( "unknown-os-family", family );
                throw new IllegalArgumentException( message );
            }
        }
        return isFamily;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8145.java