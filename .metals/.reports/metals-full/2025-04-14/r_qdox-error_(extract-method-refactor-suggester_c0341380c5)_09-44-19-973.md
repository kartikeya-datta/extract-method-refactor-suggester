error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8155.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8155.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8155.java
text:
```scala
private final static R@@esources REZ =

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.myrmidon.components.role;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.SAXConfigurationHandler;
import org.apache.myrmidon.interfaces.role.RoleManager;
import org.xml.sax.XMLReader;

/**
 * Interface to manage roles and mapping to names.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version CVS $Revision$ $Date$
 */
public class DefaultRoleManager
    implements RoleManager, Initializable
{
    private static final Resources REZ =
        ResourceManager.getPackageResources( DefaultRoleManager.class );

    private final static String ROLE_DESCRIPTOR = "META-INF/ant-roles.xml";

    /** Parent <code>RoleManager</code> for nested resolution */
    private final RoleManager m_parent;

    /** Map for name to role mapping */
    private final HashMap m_names = new HashMap();

    /** Map for role to name mapping */
    private final HashMap m_roles = new HashMap();

    /**
     *  constructor--this RoleManager has no parent.
     */
    public DefaultRoleManager()
    {
        this( null );
    }

    /**
     * Alternate constructor--this RoleManager has the specified
     * parent.
     *
     * @param parent The parent <code>RoleManager</code>.
     */
    public DefaultRoleManager( final RoleManager parent )
    {
        m_parent = parent;
    }

    /**
     * initialize the RoleManager.
     * This involves reading all Role descriptors in common classloader.
     *
     * @exception Exception if an error occurs
     */
    public void initialize()
        throws Exception
    {
        final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        final SAXParser saxParser = saxParserFactory.newSAXParser();
        final XMLReader parser = saxParser.getXMLReader();
        //parser.setFeature( "http://xml.org/sax/features/namespace-prefixes", false );

        final SAXConfigurationHandler handler = new SAXConfigurationHandler();
        parser.setContentHandler( handler );
        parser.setErrorHandler( handler );

        final Enumeration enum = getClass().getClassLoader().getResources( ROLE_DESCRIPTOR );
        while( enum.hasMoreElements() )
        {
            final URL url = (URL)enum.nextElement();
            parser.parse( url.toString() );
            handleDescriptor( handler.getConfiguration() );
        }
    }

    /**
     * Configure RoleManager based on contents of single descriptor.
     *
     * @param descriptor the descriptor
     * @exception ConfigurationException if an error occurs
     */
    private void handleDescriptor( final Configuration descriptor )
        throws ConfigurationException
    {
        final Configuration[] types = descriptor.getChildren( "role" );
        for( int i = 0; i < types.length; i++ )
        {
            final String name = types[ i ].getAttribute( "shorthand" );
            final String role = types[ i ].getAttribute( "name" );
            addNameRoleMapping( name, role );
        }
    }

    /**
     * Find Role name based on shorthand name.
     *
     * @param name the shorthand name
     * @return the role
     */
    public String getRoleForName( final String name )
    {
        final String role = (String)m_names.get( name );

        if( null == role && null != m_parent )
        {
            return m_parent.getRoleForName( name );
        }

        return role;
    }

    /**
     * Find name based on role.
     *
     * @param role the role
     * @return the name
     */
    public String getNameForRole( final String role )
    {
        final String name = (String)m_roles.get( role );

        if( null == name && null != m_parent )
        {
            return m_parent.getNameForRole( name );
        }

        return name;
    }

    /**
     * Add a mapping between name and role
     *
     * @param name the shorthand name
     * @param role the role
     * @exception IllegalArgumentException if an name is already mapped to a different role
     */
    public void addNameRoleMapping( final String name, final String role )
        throws IllegalArgumentException
    {
        final String oldRole = (String)m_names.get( name );
        if( null != oldRole && oldRole.equals( role ) )
        {
            final String message = REZ.getString( "duplicate-name.error", oldRole );
            throw new IllegalArgumentException( message );
        }

        final String oldName = (String)m_roles.get( role );
        if( null != oldName && oldName.equals( name ) )
        {
            final String message = REZ.getString( "duplicate-role.error", oldName );
            throw new IllegalArgumentException( message );
        }

        m_names.put( name, role );
        m_roles.put( role, name );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8155.java