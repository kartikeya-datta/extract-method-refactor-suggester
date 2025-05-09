error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6756.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6756.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6756.java
text:
```scala
public b@@oolean containsKey(Object name) {

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.commons.collections;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/** An implementation of Map for JavaBeans which uses introspection to
  * get and put properties in the bean.
  *
  * If an exception occurs during attempts to get or set a property then the
  * property is considered non existent in the Map
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  */

public class BeanMap extends AbstractMap {

    private Object bean;

    private HashMap readMethods = new HashMap();
    private HashMap writeMethods = new HashMap();
    private HashMap types = new HashMap();

    public static final Object[] NULL_ARGUMENTS = {};
    public static HashMap defaultTransformers = new HashMap();
    
    static {
        defaultTransformers.put( 
            Boolean.TYPE, 
            new Transformer() {
                public Object transform( Object input ) {
                    return Boolean.valueOf( input.toString() );
                }
            }
        );
        defaultTransformers.put( 
            Character.TYPE, 
            new Transformer() {
                public Object transform( Object input ) {
                    return new Character( input.toString().charAt( 0 ) );
                }
            }
        );
        defaultTransformers.put( 
            Byte.TYPE, 
            new Transformer() {
                public Object transform( Object input ) {
                    return Byte.valueOf( input.toString() );
                }
            }
        );
        defaultTransformers.put( 
            Short.TYPE, 
            new Transformer() {
                public Object transform( Object input ) {
                    return Short.valueOf( input.toString() );
                }
            }
        );
        defaultTransformers.put( 
            Integer.TYPE, 
            new Transformer() {
                public Object transform( Object input ) {
                    return Integer.valueOf( input.toString() );
                }
            }
        );
        defaultTransformers.put( 
            Long.TYPE, 
            new Transformer() {
                public Object transform( Object input ) {
                    return Long.valueOf( input.toString() );
                }
            }
        );
        defaultTransformers.put( 
            Float.TYPE, 
            new Transformer() {
                public Object transform( Object input ) {
                    return Float.valueOf( input.toString() );
                }
            }
        );
        defaultTransformers.put( 
            Double.TYPE, 
            new Transformer() {
                public Object transform( Object input ) {
                    return Double.valueOf( input.toString() );
                }
            }
        );
    }
    
    
    // Constructors
    //-------------------------------------------------------------------------
    public BeanMap() {
    }

    public BeanMap(Object bean) {
        this.bean = bean;
        initialise();
    }

    // Map interface
    //-------------------------------------------------------------------------

    public Object clone() {
        Class beanClass = bean.getClass();
        try {
            Object newBean = beanClass.newInstance();
            Map newMap = new BeanMap( newBean );
            newMap.putAll( this );
            return newMap;
        } 
        catch (Exception e) {
            throw new UnsupportedOperationException( "Could not create new instance of class: " + beanClass );
        }
    }

    public void clear() {
        Class beanClass = bean.getClass();
        try {
            bean = beanClass.newInstance();
        }
        catch (Exception e) {
            throw new UnsupportedOperationException( "Could not create new instance of class: " + beanClass );
        }
    }

    public boolean containsKey(String name) {
        Method method = getReadMethod( name );
        return method != null;
    }

    public boolean containsValue(Object value) {
        // use default implementation
        return super.containsValue( value );
    }

    public Object get(Object name) {
        if ( bean != null ) {
            Method method = getReadMethod( name );
            if ( method != null ) {
                try {
                    return method.invoke( bean, NULL_ARGUMENTS );
                }
                catch (  IllegalAccessException e ) {
                    logWarn( e );
                }
                catch ( IllegalArgumentException e ) {
                    logWarn(  e );
                }
                catch ( InvocationTargetException e ) {
                    logWarn(  e );
                }
                catch ( NullPointerException e ) {
                    logWarn(  e );
                }
            }
        }
        return null;
    }

    public Object put(Object name, Object value) throws IllegalArgumentException, ClassCastException {
        if ( bean != null ) {
            Object oldValue = get( name );
            Method method = getWriteMethod( name );
            if ( method == null ) {
                throw new IllegalArgumentException( "The bean of type: "+ bean.getClass().getName() + " has no property called: " + name );
            }
            try {
                Object[] arguments = createWriteMethodArguments( method, value );
                method.invoke( bean, arguments );

                Object newValue = get( name );
                firePropertyChange( name, oldValue, newValue );
            }
            catch ( InvocationTargetException e ) {
                logInfo( e );
                throw new IllegalArgumentException( e.getMessage() );
            }
            catch ( IllegalAccessException e ) {
                logInfo( e );
                throw new IllegalArgumentException( e.getMessage() );
            }
            return oldValue;
        }
        return null;
    }
                    
    public int size() {
        return readMethods.size();
    }

    
    public Set keySet() {
        return readMethods.keySet();
    }

    public Set entrySet() {
        return readMethods.keySet();
    }

    public Collection values() {
        ArrayList answer = new ArrayList( readMethods.size() );
        for ( Iterator iter = valueIterator(); iter.hasNext(); ) {
            answer.add( iter.next() );
        }
        return answer;
    }


    // Helper methods
    //-------------------------------------------------------------------------
    
    public Class getType(String name) {
        return (Class) types.get( name );
    }

    public Iterator keyIterator() {
        return readMethods.keySet().iterator();
    }

    public Iterator valueIterator() {
        final Iterator iter = keyIterator();
        return new Iterator() {            
            public boolean hasNext() {
                return iter.hasNext();
            }
            public Object next() {
                Object key = iter.next();
                return get( (String) key );
            }
            public void remove() {
                throw new UnsupportedOperationException( "remove() not supported for BeanMap" );
            }
        };
    }

    public Iterator entryIterator() {
        final Iterator iter = keyIterator();
        return new Iterator() {            
            public boolean hasNext() {
                return iter.hasNext();
            }            
            public Object next() {
                Object key = iter.next();
                Object value = get( (String) key );
                return new MyMapEntry( BeanMap.this, key, value );
            }            
            public void remove() {
                throw new UnsupportedOperationException( "remove() not supported for BeanMap" );
            }
        };
    }


    // Properties
    //-------------------------------------------------------------------------
    public Object getBean() {
        return bean;
    }

    public void setBean( Object newBean ) {
        bean = newBean;
        reinitialise();
    }


    // Implementation methods
    //-------------------------------------------------------------------------

    protected Method getReadMethod( Object name ) {
        return (Method) readMethods.get( name );
    }

    protected Method getWriteMethod( Object name ) {
        return (Method) writeMethods.get( name );
    }

    protected void reinitialise() {
        readMethods.clear();
        writeMethods.clear();
        types.clear();
        initialise();
    }

    private void initialise() {
        Class  beanClass = getBean().getClass();
        try {
            //BeanInfo beanInfo = Introspector.getBeanInfo( bean, null );
            BeanInfo beanInfo = Introspector.getBeanInfo( beanClass );
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if ( propertyDescriptors != null ) {
                for ( int i = 0; i < propertyDescriptors.length; i++ ) {
                    PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
                    if ( propertyDescriptor != null ) {
                        String name = propertyDescriptor.getName();
                        Method readMethod = propertyDescriptor.getReadMethod();
                        Method writeMethod = propertyDescriptor.getWriteMethod();
                        Class aType = propertyDescriptor.getPropertyType();

                        if ( readMethod != null ) {
                            readMethods.put( name, readMethod );
                        }
                        if ( writeMethods != null ) {
                            writeMethods.put( name, writeMethod );
                        }
                        types.put( name, aType );
                    }
                }
            }
        }
        catch ( IntrospectionException e ) {
            logWarn(  e );
        }
    }

    protected void firePropertyChange( Object key, Object oldValue, Object newValue ) {
    }

    // Implementation classes
    //-------------------------------------------------------------------------
    protected static class MyMapEntry extends DefaultMapEntry {        
        private BeanMap owner;
        
        protected MyMapEntry( BeanMap owner, Object key, Object value ) {
            super( key, value );
            this.owner = owner;
        }

        public Object setValue(Object value) {
            Object key = getKey();
            Object oldValue = owner.get( key );

            owner.put( key, value );
            Object newValue = owner.get( key );
            super.setValue( newValue );
            return oldValue;
        }
    }
    
    protected Object[] createWriteMethodArguments( Method method, Object value ) throws IllegalAccessException, ClassCastException {            
        try {
            if ( value != null ) {
                Class[] types = method.getParameterTypes();
                if ( types != null && types.length > 0 ) {
                    Class paramType = types[0];
                    if ( ! paramType.isAssignableFrom( value.getClass() ) ) {
                        value = convertType( paramType, value );
                    }
                }
            }
            Object[] answer = { value };
            return answer;
        }
        catch ( InvocationTargetException e ) {
            logInfo( e );
            throw new IllegalArgumentException( e.getMessage() );
        }
        catch ( InstantiationException e ) {
            logInfo( e );
            throw new IllegalArgumentException( e.getMessage() );
        }
    }
    
    protected Object convertType( Class newType, Object value ) 
        throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        
        // try call constructor
        Class[] types = { value.getClass() };
        try {
            Constructor constructor = newType.getConstructor( types );        
            Object[] arguments = { value };
            return constructor.newInstance( arguments );
        }
        catch ( NoSuchMethodException e ) {
            // try using the transformers
            Transformer transformer = getTypeTransformer( newType );
            if ( transformer != null ) {
                return transformer.transform( value );
            }
            return value;
        }
    }
    
    protected Transformer getTypeTransformer( Class aType ) {
        return (Transformer) defaultTransformers.get( aType );
    }
    
    protected void logInfo(Exception e) {
        // XXXX: should probably use log4j here instead...
        System.out.println( "INFO: Exception: " + e );
    }
    
    protected void logWarn(Exception e) {
        // XXXX: should probably use log4j here instead...
        System.out.println( "WARN: Exception: " + e );
        e.printStackTrace();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6756.java