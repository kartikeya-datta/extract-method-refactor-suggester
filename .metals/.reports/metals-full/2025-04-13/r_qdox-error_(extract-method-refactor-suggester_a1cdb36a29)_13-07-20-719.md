error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4007.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4007.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4007.java
text:
```scala
r@@esultSetReaders[i] = property.getResultSetReader();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.cmp.jdbc;

import java.util.HashMap;
import javax.ejb.EJBException;

/**
 * JDBCTypeComplex provides the mapping between a Java Bean (not an EJB)
 * and a set of columns. This class has a flattened view of the Java Bean,
 * which may contain other Java Beans.  This class simply treats the bean
 * as a set of properties, which may be in the a.b.c style. The details
 * of how this mapping is performed can be found in JDBCTypeFactory.
 * <p/>
 * This class holds a description of the columns
 * and the properties that map to the columns. Additionally, this class
 * knows how to extract a column value from the Java Bean and how to set
 * a column value info the Java Bean. See JDBCTypeComplexProperty for
 * details on how this is done.
 *
 * @author <a href="mailto:dain@daingroup.com">Dain Sundstrom</a>
 * @version $Revision: 81030 $
 */
public final class JDBCTypeComplex implements JDBCType {
    private final JDBCTypeComplexProperty[] properties;
    private final String[] columnNames;
    private final Class[] javaTypes;
    private final int[] jdbcTypes;
    private final String[] sqlTypes;
    private final boolean[] notNull;
    private final JDBCResultSetReader[] resultSetReaders;
    private final JDBCParameterSetter[] paramSetters;
    private final Class fieldType;
    private final HashMap propertiesByName = new HashMap();

    public JDBCTypeComplex(
            JDBCTypeComplexProperty[] properties,
            Class fieldType) {

        this.properties = properties;
        this.fieldType = fieldType;

        int propNum = properties.length;
        columnNames = new String[propNum];
        javaTypes = new Class[propNum];
        jdbcTypes = new int[propNum];
        sqlTypes = new String[propNum];
        notNull = new boolean[propNum];
        resultSetReaders = new JDBCResultSetReader[propNum];
        paramSetters = new JDBCParameterSetter[propNum];
        for (int i = 0; i < properties.length; i++) {
            JDBCTypeComplexProperty property = properties[i];
            columnNames[i] = property.getColumnName();
            javaTypes[i] = property.getJavaType();
            jdbcTypes[i] = property.getJDBCType();
            sqlTypes[i] = property.getSQLType();
            notNull[i] = property.isNotNull();
            resultSetReaders[i] = property.getResulSetReader();
            paramSetters[i] = property.getParameterSetter();
            propertiesByName.put(property.getPropertyName(), property);
        }
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public Class[] getJavaTypes() {
        return javaTypes;
    }

    public int[] getJDBCTypes() {
        return jdbcTypes;
    }

    public String[] getSQLTypes() {
        return sqlTypes;
    }

    public boolean[] getNotNull() {
        return notNull;
    }

    public boolean[] getAutoIncrement() {
        return new boolean[]{false};
    }

    public Object getColumnValue(int index, Object value) {
        return getColumnValue(properties[index], value);
    }

    public Object setColumnValue(int index, Object value, Object columnValue) {
        return setColumnValue(properties[index], value, columnValue);
    }

    public boolean hasMapper() {
        return false;
    }

    public boolean isSearchable() {
        return false;
    }

    public JDBCResultSetReader[] getResultSetReaders() {
        return resultSetReaders;
    }

    public JDBCParameterSetter[] getParameterSetter() {
        return paramSetters;
    }

    public JDBCTypeComplexProperty[] getProperties() {
        return properties;
    }

    public JDBCTypeComplexProperty getProperty(String propertyName) {
        JDBCTypeComplexProperty prop = (JDBCTypeComplexProperty) propertiesByName.get(propertyName);
        if (prop == null) {
            throw new EJBException(fieldType.getName() +
                    " does not have a property named " + propertyName);
        }
        return prop;
    }

    private static Object getColumnValue(JDBCTypeComplexProperty property, Object value) {
        try {
            return property.getColumnValue(value);
        } catch (EJBException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("Error getting column value", e);
        }
    }

    private Object setColumnValue(
            JDBCTypeComplexProperty property,
            Object value,
            Object columnValue) {

        if (value == null && columnValue == null) {
            // nothing to do
            return null;
        }

        try {
            if (value == null) {
                value = fieldType.newInstance();
            }
            return property.setColumnValue(value, columnValue);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException("Error setting column value", e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4007.java