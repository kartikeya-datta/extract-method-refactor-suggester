error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1437.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1437.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1437.java
text:
```scala
O@@S.objc_msgSend_struct(result, OS.class_NSEvent, OS.sel_mouseLocation);

package org.eclipse.swt.internal.cocoa;

public class NSEvent extends NSObject {

public NSEvent() {
	super();
}

public NSEvent(int id) {
	super(id);
}

public int CGEvent() {
	return OS.objc_msgSend(this.id, OS.sel_CGEvent);
}

public int absoluteX() {
	return OS.objc_msgSend(this.id, OS.sel_absoluteX);
}

public int absoluteY() {
	return OS.objc_msgSend(this.id, OS.sel_absoluteY);
}

public int absoluteZ() {
	return OS.objc_msgSend(this.id, OS.sel_absoluteZ);
}

public int buttonMask() {
	return OS.objc_msgSend(this.id, OS.sel_buttonMask);
}

public int buttonNumber() {
	return OS.objc_msgSend(this.id, OS.sel_buttonNumber);
}

public int capabilityMask() {
	return OS.objc_msgSend(this.id, OS.sel_capabilityMask);
}

public NSString characters() {
	int result = OS.objc_msgSend(this.id, OS.sel_characters);
	return result != 0 ? new NSString(result) : null;
}

public NSString charactersIgnoringModifiers() {
	int result = OS.objc_msgSend(this.id, OS.sel_charactersIgnoringModifiers);
	return result != 0 ? new NSString(result) : null;
}

public int clickCount() {
	return OS.objc_msgSend(this.id, OS.sel_clickCount);
}

public NSGraphicsContext context() {
	int result = OS.objc_msgSend(this.id, OS.sel_context);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public int data1() {
	return OS.objc_msgSend(this.id, OS.sel_data1);
}

public int data2() {
	return OS.objc_msgSend(this.id, OS.sel_data2);
}

public float deltaX() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_deltaX);
}

public float deltaY() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_deltaY);
}

public float deltaZ() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_deltaZ);
}

public int deviceID() {
	return OS.objc_msgSend(this.id, OS.sel_deviceID);
}

public static NSEvent enterExitEventWithType(int type, NSPoint location, int flags, double time, int wNum, NSGraphicsContext context, int eNum, int tNum, int data) {
	int result = OS.objc_msgSend(OS.class_NSEvent, OS.sel_enterExitEventWithType_1location_1modifierFlags_1timestamp_1windowNumber_1context_1eventNumber_1trackingNumber_1userData_1, type, location, flags, time, wNum, context != null ? context.id : 0, eNum, tNum, data);
	return result != 0 ? new NSEvent(result) : null;
}

public int eventNumber() {
	return OS.objc_msgSend(this.id, OS.sel_eventNumber);
}

public int eventRef() {
	return OS.objc_msgSend(this.id, OS.sel_eventRef);
}

public static NSEvent eventWithCGEvent(int cgEvent) {
	int result = OS.objc_msgSend(OS.class_NSEvent, OS.sel_eventWithCGEvent_1, cgEvent);
	return result != 0 ? new NSEvent(result) : null;
}

public static NSEvent eventWithEventRef(int eventRef) {
	int result = OS.objc_msgSend(OS.class_NSEvent, OS.sel_eventWithEventRef_1, eventRef);
	return result != 0 ? new NSEvent(result) : null;
}

public boolean isARepeat() {
	return OS.objc_msgSend(this.id, OS.sel_isARepeat) != 0;
}

public boolean isEnteringProximity() {
	return OS.objc_msgSend(this.id, OS.sel_isEnteringProximity) != 0;
}

public static boolean isMouseCoalescingEnabled() {
	return OS.objc_msgSend(OS.class_NSEvent, OS.sel_isMouseCoalescingEnabled) != 0;
}

public short keyCode() {
	return (short)OS.objc_msgSend(this.id, OS.sel_keyCode);
}

public static NSEvent keyEventWithType(int type, NSPoint location, int flags, double time, int wNum, NSGraphicsContext context, NSString keys, NSString ukeys, boolean flag, short code) {
	int result = OS.objc_msgSend(OS.class_NSEvent, OS.sel_keyEventWithType_1location_1modifierFlags_1timestamp_1windowNumber_1context_1characters_1charactersIgnoringModifiers_1isARepeat_1keyCode_1, type, location, flags, time, wNum, context != null ? context.id : 0, keys != null ? keys.id : 0, ukeys != null ? ukeys.id : 0, flag, code);
	return result != 0 ? new NSEvent(result) : null;
}

public NSPoint locationInWindow() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_struct(result, this.id, OS.sel_locationInWindow);
	return result;
}

public int modifierFlags() {
	return OS.objc_msgSend(this.id, OS.sel_modifierFlags);
}

public static NSEvent mouseEventWithType(int type, NSPoint location, int flags, double time, int wNum, NSGraphicsContext context, int eNum, int cNum, float pressure) {
	int result = OS.objc_msgSend(OS.class_NSEvent, OS.sel_mouseEventWithType_1location_1modifierFlags_1timestamp_1windowNumber_1context_1eventNumber_1clickCount_1pressure_1, type, location, flags, time, wNum, context != null ? context.id : 0, eNum, cNum, pressure);
	return result != 0 ? new NSEvent(result) : null;
}

public static NSPoint mouseLocation() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, OS.class_NSEvent, OS.sel_mouseLocation);
	return result;
}

public static NSEvent otherEventWithType(int type, NSPoint location, int flags, double time, int wNum, NSGraphicsContext context, short subtype, int d1, int d2) {
	int result = OS.objc_msgSend(OS.class_NSEvent, OS.sel_otherEventWithType_1location_1modifierFlags_1timestamp_1windowNumber_1context_1subtype_1data1_1data2_1, type, location, flags, time, wNum, context != null ? context.id : 0, subtype, d1, d2);
	return result != 0 ? new NSEvent(result) : null;
}

public int pointingDeviceID() {
	return OS.objc_msgSend(this.id, OS.sel_pointingDeviceID);
}

public int pointingDeviceSerialNumber() {
	return OS.objc_msgSend(this.id, OS.sel_pointingDeviceSerialNumber);
}

public int pointingDeviceType() {
	return OS.objc_msgSend(this.id, OS.sel_pointingDeviceType);
}

public float pressure() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_pressure);
}

public float rotation() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_rotation);
}

public static void setMouseCoalescingEnabled(boolean flag) {
	OS.objc_msgSend(OS.class_NSEvent, OS.sel_setMouseCoalescingEnabled_1, flag);
}

public static void startPeriodicEventsAfterDelay(double delay, double period) {
	OS.objc_msgSend(OS.class_NSEvent, OS.sel_startPeriodicEventsAfterDelay_1withPeriod_1, delay, period);
}

public static void stopPeriodicEvents() {
	OS.objc_msgSend(OS.class_NSEvent, OS.sel_stopPeriodicEvents);
}

public short subtype() {
	return (short)OS.objc_msgSend(this.id, OS.sel_subtype);
}

public int systemTabletID() {
	return OS.objc_msgSend(this.id, OS.sel_systemTabletID);
}

public int tabletID() {
	return OS.objc_msgSend(this.id, OS.sel_tabletID);
}

public float tangentialPressure() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_tangentialPressure);
}

public NSPoint tilt() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_tilt);
	return result;
}

public double timestamp() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_timestamp);
}

public NSTrackingArea trackingArea() {
	int result = OS.objc_msgSend(this.id, OS.sel_trackingArea);
	return result != 0 ? new NSTrackingArea(result) : null;
}

public int trackingNumber() {
	return OS.objc_msgSend(this.id, OS.sel_trackingNumber);
}

public int type() {
	return OS.objc_msgSend(this.id, OS.sel_type);
}

public long uniqueID() {
	return (long)OS.objc_msgSend(this.id, OS.sel_uniqueID);
}

public int userData() {
	return OS.objc_msgSend(this.id, OS.sel_userData);
}

public id vendorDefined() {
	int result = OS.objc_msgSend(this.id, OS.sel_vendorDefined);
	return result != 0 ? new id(result) : null;
}

public int vendorID() {
	return OS.objc_msgSend(this.id, OS.sel_vendorID);
}

public int vendorPointingDeviceType() {
	return OS.objc_msgSend(this.id, OS.sel_vendorPointingDeviceType);
}

public NSWindow window() {
	int result = OS.objc_msgSend(this.id, OS.sel_window);
	return result != 0 ? new NSWindow(result) : null;
}

public int windowNumber() {
	return OS.objc_msgSend(this.id, OS.sel_windowNumber);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1437.java