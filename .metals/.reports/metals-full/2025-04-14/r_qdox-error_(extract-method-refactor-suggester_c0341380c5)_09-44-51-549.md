error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5199.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5199.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5199.java
text:
```scala
_@@fieldIndex = parent.getFieldIndex();

package storm.trident.tuple;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import clojure.lang.IPersistentVector;
import clojure.lang.PersistentVector;
import clojure.lang.RT;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//extends abstractlist so that it can be emitted directly as Storm tuples
public class TridentTupleView extends AbstractList<Object> implements TridentTuple {
    ValuePointer[] _index;
    Map<String, ValuePointer> _fieldIndex;
    IPersistentVector _delegates;
    
    public static class ProjectionFactory implements Factory {
        Map<String, ValuePointer> _fieldIndex;
        ValuePointer[] _index;
        Factory _parent;

        public ProjectionFactory(Factory parent, Fields projectFields) {
            _parent = parent;
            if(projectFields==null) projectFields = new Fields();
            Map<String, ValuePointer> parentFieldIndex = parent.getFieldIndex();
            _fieldIndex = new HashMap<String, ValuePointer>();
            for(String f: projectFields) {
                _fieldIndex.put(f, parentFieldIndex.get(f));
            }            
            _index = ValuePointer.buildIndex(projectFields, _fieldIndex);
        }
        
        public TridentTuple create(TridentTuple parent) {
            if(_index.length==0) return EMPTY_TUPLE;
            else return new TridentTupleView(((TridentTupleView)parent)._delegates, _index, _fieldIndex);
        }

        @Override
        public Map<String, ValuePointer> getFieldIndex() {
            return _fieldIndex;
        }

        @Override
        public int numDelegates() {
            return _parent.numDelegates();
        }

        @Override
        public List<String> getOutputFields() {
            return indexToFieldsList(_index);
        }
    }
    
    public static class FreshOutputFactory  implements Factory {
        Map<String, ValuePointer> _fieldIndex;
        ValuePointer[] _index;

        public FreshOutputFactory(Fields selfFields) {
            _fieldIndex = new HashMap<String, ValuePointer>();
            for(int i=0; i<selfFields.size(); i++) {
                String field = selfFields.get(i);
                _fieldIndex.put(field, new ValuePointer(0, i, field));
            }
            _index = ValuePointer.buildIndex(selfFields, _fieldIndex);
        }
        
        public TridentTuple create(List<Object> selfVals) {
            return new TridentTupleView(PersistentVector.EMPTY.cons(selfVals), _index, _fieldIndex);
        }

        @Override
        public Map<String, ValuePointer> getFieldIndex() {
            return _fieldIndex;
        }

        @Override
        public int numDelegates() {
            return 1;
        }
        
        @Override
        public List<String> getOutputFields() {
            return indexToFieldsList(_index);
        }        
    }
    
    public static class OperationOutputFactory implements Factory {
        Map<String, ValuePointer> _fieldIndex;
        ValuePointer[] _index;
        Factory _parent;

        public OperationOutputFactory(Factory parent, Fields selfFields) {
            _parent = parent;
            _fieldIndex = new HashMap(parent.getFieldIndex());
            int myIndex = parent.numDelegates();
            for(int i=0; i<selfFields.size(); i++) {
                String field = selfFields.get(i);
                _fieldIndex.put(field, new ValuePointer(myIndex, i, field));
            }
            List<String> myOrder = new ArrayList<String>(parent.getOutputFields());
            
            Set<String> parentFieldsSet = new HashSet<String>(myOrder);
            for(String f: selfFields) {
                if(parentFieldsSet.contains(f)) {
                    throw new IllegalArgumentException(
                            "Additive operations cannot add fields with same name as already exists. "
                            + "Tried adding " + selfFields + " to " + parent.getOutputFields());
                }
                myOrder.add(f);
            }
            
            _index = ValuePointer.buildIndex(new Fields(myOrder), _fieldIndex);
        }
        
        public TridentTuple create(TridentTupleView parent, List<Object> selfVals) {
            IPersistentVector curr = parent._delegates;
            curr = (IPersistentVector) RT.conj(curr, selfVals);
            return new TridentTupleView(curr, _index, _fieldIndex);
        }

        @Override
        public Map<String, ValuePointer> getFieldIndex() {
            return _fieldIndex;
        }

        @Override
        public int numDelegates() {
            return _parent.numDelegates() + 1;
        }

        @Override
        public List<String> getOutputFields() {
            return indexToFieldsList(_index);
        }
    }
    
    public static class RootFactory implements Factory {
        ValuePointer[] index;
        Map<String, ValuePointer> fieldIndex;
        
        public RootFactory(Fields inputFields) {
            index = new ValuePointer[inputFields.size()];
            int i=0;
            for(String f: inputFields) {
                index[i] = new ValuePointer(0, i, f);
                i++;
            }
            fieldIndex = ValuePointer.buildFieldIndex(index);
        }
        
        public TridentTuple create(Tuple parent) {            
            return new TridentTupleView(PersistentVector.EMPTY.cons(parent.getValues()), index, fieldIndex);
        }

        @Override
        public Map<String, ValuePointer> getFieldIndex() {
            return fieldIndex;
        }

        @Override
        public int numDelegates() {
            return 1;
        }
        
        @Override
        public List<String> getOutputFields() {
            return indexToFieldsList(this.index);
        }
    }
    
    private static List<String> indexToFieldsList(ValuePointer[] index) {
        List<String> ret = new ArrayList<String>();
        for(ValuePointer p: index) {
            ret.add(p.field);
        }
        return ret;
    }
    
    public static TridentTupleView EMPTY_TUPLE = new TridentTupleView(null, new ValuePointer[0], new HashMap());
    
    // index and fieldIndex are precomputed, delegates built up over many operations using persistent data structures
    public TridentTupleView(IPersistentVector delegates, ValuePointer[] index, Map<String, ValuePointer> fieldIndex) {
        _delegates = delegates;
        _index = index;
        _fieldIndex = fieldIndex;
    }

    @Override
    public List<Object> getValues() {
        return this;
    }    

    @Override
    public int size() {
        return _index.length;
    }

    @Override
    public Object get(int i) {
        return getValue(i);
    }    
    
    @Override
    public Object getValue(int i) {
        return getValueByPointer(_index[i]);
    }

    @Override
    public String getString(int i) {
        return (String) getValue(i);
    }

    @Override
    public Integer getInteger(int i) {
        return (Integer) getValue(i);
    }

    @Override
    public Long getLong(int i) {
        return (Long) getValue(i);
    }

    @Override
    public Boolean getBoolean(int i) {
        return (Boolean) getValue(i);
    }

    @Override
    public Short getShort(int i) {
        return (Short) getValue(i);
    }

    @Override
    public Byte getByte(int i) {
        return (Byte) getValue(i);
    }

    @Override
    public Double getDouble(int i) {
        return (Double) getValue(i);
    }

    @Override
    public Float getFloat(int i) {
        return (Float) getValue(i);
    }

    @Override
    public byte[] getBinary(int i) {
        return (byte[]) getValue(i);
    }

    @Override
    public Object getValueByField(String field) {
        return getValueByPointer(_fieldIndex.get(field));
    }

    @Override
    public String getStringByField(String field) {
        return (String) getValueByField(field);
    }

    @Override
    public Integer getIntegerByField(String field) {
        return (Integer) getValueByField(field);
    }

    @Override
    public Long getLongByField(String field) {
        return (Long) getValueByField(field);
    }

    @Override
    public Boolean getBooleanByField(String field) {
        return (Boolean) getValueByField(field);
    }

    @Override
    public Short getShortByField(String field) {
        return (Short) getValueByField(field);
    }

    @Override
    public Byte getByteByField(String field) {
        return (Byte) getValueByField(field);
    }

    @Override
    public Double getDoubleByField(String field) {
        return (Double) getValueByField(field);
    }

    @Override
    public Float getFloatByField(String field) {
        return (Float) getValueByField(field);
    }

    @Override
    public byte[] getBinaryByField(String field) {
        return (byte[]) getValueByField(field);
    }

    private Object getValueByPointer(ValuePointer ptr) {
        return ((List<Object>)_delegates.nth(ptr.delegateIndex)).get(ptr.index);     
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5199.java