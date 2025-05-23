error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8487.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8487.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8487.java
text:
```scala
t@@hrow new BuildException( "Error", t );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.types;
import java.util.Properties;
import java.util.Stack;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.FileNameMapper;

/**
 * Element to define a FileNameMapper.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class Mapper extends DataType implements Cloneable
{

    protected MapperType type = null;

    protected String classname = null;

    protected Path classpath = null;

    protected String from = null;

    protected String to = null;

    public Mapper( Project p )
    {
        setProject( p );
    }

    /**
     * Set the class name of the FileNameMapper to use.
     *
     * @param classname The new Classname value
     */
    public void setClassname( String classname )
    {
        if( isReference() )
        {
            throw tooManyAttributes();
        }
        this.classname = classname;
    }

    /**
     * Set the classpath to load the FileNameMapper through (attribute).
     *
     * @param classpath The new Classpath value
     */
    public void setClasspath( Path classpath )
    {
        if( isReference() )
        {
            throw tooManyAttributes();
        }
        if( this.classpath == null )
        {
            this.classpath = classpath;
        }
        else
        {
            this.classpath.append( classpath );
        }
    }

    /**
     * Set the classpath to load the FileNameMapper through via reference
     * (attribute).
     *
     * @param r The new ClasspathRef value
     */
    public void setClasspathRef( Reference r )
    {
        if( isReference() )
        {
            throw tooManyAttributes();
        }
        createClasspath().setRefid( r );
    }

    /**
     * Set the argument to FileNameMapper.setFrom
     *
     * @param from The new From value
     */
    public void setFrom( String from )
    {
        if( isReference() )
        {
            throw tooManyAttributes();
        }
        this.from = from;
    }

    /**
     * Make this Mapper instance a reference to another Mapper. <p>
     *
     * You must not set any other attribute if you make it a reference.</p>
     *
     * @param r The new Refid value
     * @exception BuildException Description of Exception
     */
    public void setRefid( Reference r )
        throws BuildException
    {
        if( type != null || from != null || to != null )
        {
            throw tooManyAttributes();
        }
        super.setRefid( r );
    }

    /**
     * Set the argument to FileNameMapper.setTo
     *
     * @param to The new To value
     */
    public void setTo( String to )
    {
        if( isReference() )
        {
            throw tooManyAttributes();
        }
        this.to = to;
    }

    /**
     * Set the type of FileNameMapper to use.
     *
     * @param type The new Type value
     */
    public void setType( MapperType type )
    {
        if( isReference() )
        {
            throw tooManyAttributes();
        }
        this.type = type;
    }

    /**
     * Returns a fully configured FileNameMapper implementation.
     *
     * @return The Implementation value
     * @exception BuildException Description of Exception
     */
    public FileNameMapper getImplementation()
        throws BuildException
    {
        if( isReference() )
        {
            return getRef().getImplementation();
        }

        if( type == null && classname == null )
        {
            throw new BuildException( "one of the attributes type or classname is required" );
        }

        if( type != null && classname != null )
        {
            throw new BuildException( "must not specify both type and classname attribute" );
        }

        try
        {
            if( type != null )
            {
                classname = type.getImplementation();
            }

            Class c = null;
            if( classpath == null )
            {
                c = Class.forName( classname );
            }
            else
            {
                AntClassLoader al = new AntClassLoader( getProject(),
                    classpath );
                c = al.loadClass( classname );
                AntClassLoader.initializeClass( c );
            }

            FileNameMapper m = ( FileNameMapper )c.newInstance();
            m.setFrom( from );
            m.setTo( to );
            return m;
        }
        catch( BuildException be )
        {
            throw be;
        }
        catch( Throwable t )
        {
            throw new BuildException( t );
        }
        finally
        {
            if( type != null )
            {
                classname = null;
            }
        }
    }

    /**
     * Set the classpath to load the FileNameMapper through (nested element).
     *
     * @return Description of the Returned Value
     */
    public Path createClasspath()
    {
        if( isReference() )
        {
            throw noChildrenAllowed();
        }
        if( this.classpath == null )
        {
            this.classpath = new Path( getProject() );
        }
        return this.classpath.createPath();
    }

    /**
     * Performs the check for circular references and returns the referenced
     * Mapper.
     *
     * @return The Ref value
     */
    protected Mapper getRef()
    {
        if( !checked )
        {
            Stack stk = new Stack();
            stk.push( this );
            dieOnCircularReference( stk, getProject() );
        }

        Object o = ref.getReferencedObject( getProject() );
        if( !( o instanceof Mapper ) )
        {
            String msg = ref.getRefId() + " doesn\'t denote a mapper";
            throw new BuildException( msg );
        }
        else
        {
            return ( Mapper )o;
        }
    }

    /**
     * Class as Argument to FileNameMapper.setType.
     *
     * @author RT
     */
    public static class MapperType extends EnumeratedAttribute
    {
        private Properties implementations;

        public MapperType()
        {
            implementations = new Properties();
            implementations.put( "identity",
                "org.apache.tools.ant.util.IdentityMapper" );
            implementations.put( "flatten",
                "org.apache.tools.ant.util.FlatFileNameMapper" );
            implementations.put( "glob",
                "org.apache.tools.ant.util.GlobPatternMapper" );
            implementations.put( "merge",
                "org.apache.tools.ant.util.MergingMapper" );
            implementations.put( "regexp",
                "org.apache.tools.ant.util.RegexpPatternMapper" );
        }

        public String getImplementation()
        {
            return implementations.getProperty( getValue() );
        }

        public String[] getValues()
        {
            return new String[]{"identity", "flatten", "glob", "merge", "regexp"};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8487.java