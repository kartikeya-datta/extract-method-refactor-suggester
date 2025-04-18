error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8467.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8467.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8467.java
text:
```scala
t@@hrow new BuildException( "file token must not be null." );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs.optional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.ListIterator;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


/**
 * Task for creating a manifest file for a jar archiv. use: <pre>
 *   <taskdef name="manifest" classname="ManifestFile"/>
 *   <target name="jar_manifest">
 *     <manifest file="manifest.mf" method="replaceAll">
 *         <entry value="Manifest-Version: 1_0"/>
 *     </manifest> </target> </pre>
 *
 * @author Thomas Kerle
 * @version 1.0 2001-10-11
 */
public class ManifestFile extends Task
{

    private final static String newLine = System.getProperty( "line.separator" );
    private final static String keyValueSeparator = ":";
    private final static String UPDATE_ = "update";
    private final static String REPLACEALL_ = "replaceAll";
    private EntryContainer container;
    private String currentMethod;
    private Vector entries;

    private File manifestFile;

    public ManifestFile()
    {
        entries = new Vector();
        container = new EntryContainer();
    }

    /**
     * Setter for the file attribute
     *
     * @param f The new File value
     */
    public void setFile( File f )
    {
        manifestFile = f;
    }

    /**
     * Setter for the method attribute (update/replaceAll)
     *
     * @param method Method to set task
     */
    public void setMethod( String method )
    {
        currentMethod = method.toUpperCase();
    }

    /**
     * creating entries by Ant
     *
     * @return Description of the Returned Value
     */
    public Entry createEntry()
    {
        Entry entry = new Entry();
        entries.addElement( entry );
        return entry;
    }

    /**
     * execute task
     *
     * @exception BuildException : Failure in building
     */
    public void execute()
        throws BuildException
    {
        checkParameters();
        if( isUpdate( currentMethod ) )
            readFile();

        executeOperation();
        writeFile();
    }

    private StringTokenizer getLineTokens( StringBuffer buffer )
    {
        String manifests = buffer.toString();
        StringTokenizer strTokens = new StringTokenizer( manifests, newLine );
        return strTokens;
    }

    private boolean isReplaceAll( String method )
    {
        return method.equals( REPLACEALL_.toUpperCase() );
    }


    private boolean isUpdate( String method )
    {
        return method.equals( UPDATE_.toUpperCase() );
    }

    private void addLine( String line )
    {
        Entry entry = new Entry();

        entry.setValue( line );
        entry.addTo( container );
    }


    private StringBuffer buildBuffer()
    {
        StringBuffer buffer = new StringBuffer();

        ListIterator iterator = container.elements();

        while( iterator.hasNext() )
        {
            Entry entry = ( Entry )iterator.next();

            String key = ( String )entry.getKey();
            String value = ( String )entry.getValue();
            String entry_string = key + keyValueSeparator + value;

            buffer.append( entry_string + this.newLine );
        }

        return buffer;
    }

    private boolean checkParam( String param )
    {
        return !( ( param == null ) || ( param.equals( "null" ) ) );
    }

    private boolean checkParam( File param )
    {
        return !( param == null );
    }

    private void checkParameters()
        throws BuildException
    {
        if( !checkParam( manifestFile ) )
        {
            throw new BuildException( "file token must not be null.", location );
        }
    }

    /**
     * adding entries to a container
     *
     * @exception BuildException
     */
    private void executeOperation()
        throws BuildException
    {
        Enumeration enum = entries.elements();

        while( enum.hasMoreElements() )
        {
            Entry entry = ( Entry )enum.nextElement();
            entry.addTo( container );
        }
    }

    private void readFile()
        throws BuildException
    {

        if( manifestFile.exists() )
        {
            this.log( "update existing manifest file " + manifestFile.getAbsolutePath() );

            if( container != null )
            {
                try
                {
                    FileInputStream fis = new FileInputStream( manifestFile );

                    int c;
                    StringBuffer buffer = new StringBuffer( "" );
                    boolean stop = false;
                    while( !stop )
                    {
                        c = fis.read();
                        if( c == -1 )
                        {
                            stop = true;
                        }
                        else
                            buffer.append( ( char )c );
                    }
                    fis.close();
                    StringTokenizer lineTokens = getLineTokens( buffer );
                    while( lineTokens.hasMoreElements() )
                    {
                        String currentLine = ( String )lineTokens.nextElement();
                        addLine( currentLine );
                    }
                }
                catch( FileNotFoundException fnfe )
                {
                    throw new BuildException( "File not found exception " + fnfe.toString() );
                }
                catch( IOException ioe )
                {
                    throw new BuildException( "Unknown input/output exception " + ioe.toString() );
                }
            }
        }

    }


    private void writeFile()
        throws BuildException
    {
        try
        {
            manifestFile.delete();
            log( "Replacing or creating new manifest file " + manifestFile.getAbsolutePath() );
            if( manifestFile.createNewFile() )
            {
                FileOutputStream fos = new FileOutputStream( manifestFile );

                StringBuffer buffer = buildBuffer();

                int size = buffer.length();

                for( int i = 0; i < size; i++ )
                {
                    fos.write( ( char )buffer.charAt( i ) );
                }

                fos.flush();
                fos.close();
            }
            else
            {
                throw new BuildException( "Can't create manifest file" );
            }

        }
        catch( IOException ioe )
        {
            throw new BuildException( "An input/ouput error occured" + ioe.toString() );
        }
    }

    public class Entry implements Comparator
    {
        //extern format
        private String value = null;

        //intern representation
        private String val = null;
        private String key = null;

        public Entry() { }

        public void setValue( String value )
        {
            this.value = new String( value );
        }

        public String getKey()
        {
            return key;
        }

        public String getValue()
        {
            return val;
        }

        public int compare( Object o1, Object o2 )
        {
            int result = -1;

            try
            {
                Entry e1 = ( Entry )o1;
                Entry e2 = ( Entry )o2;

                String key_1 = e1.getKey();
                String key_2 = e2.getKey();

                result = key_1.compareTo( key_2 );
            }
            catch( Exception e )
            {

            }
            return result;
        }


        public boolean equals( Object obj )
        {
            Entry ent = new Entry();
            boolean result = false;
            int res = ent.compare( this, ( Entry )obj );
            if( res == 0 )
                result = true;

            return result;
        }


        protected void addTo( EntryContainer container )
            throws BuildException
        {
            checkFormat();
            split();
            container.set( this );
        }

        private void checkFormat()
            throws BuildException
        {

            if( value == null )
            {
                throw new BuildException( "no argument for value" );
            }

            StringTokenizer st = new StringTokenizer( value, ManifestFile.keyValueSeparator );
            int size = st.countTokens();

            if( size < 2 )
            {
                throw new BuildException( "value has not the format of a manifest entry" );
            }
        }

        private void split()
        {
            StringTokenizer st = new StringTokenizer( value, ManifestFile.keyValueSeparator );
            key = ( String )st.nextElement();
            val = ( String )st.nextElement();
        }

    }

    public class EntryContainer
    {

        private ArrayList list = null;

        public EntryContainer()
        {
            list = new ArrayList();
        }

        public void set( Entry entry )
        {

            if( list.contains( entry ) )
            {
                int index = list.indexOf( entry );

                list.remove( index );
                list.add( index, entry );
            }
            else
            {
                list.add( entry );
            }
        }

        public ListIterator elements()
        {
            ListIterator iterator = list.listIterator();
            return iterator;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8467.java