error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2024.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2024.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2024.java
text:
```scala
i@@f (state == NetworkInfo.State.CONNECTED && ssid != null){

/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.wifi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.TextView;

import com.google.zxing.client.android.R;

/**
 * Get a broadcast when the network is connected, and kill the activity.
 */
final class WifiReceiver extends BroadcastReceiver {
  private final String TAG = "WifiReceiver";
  private final WifiManager mWifiManager;
  private final Activity parent;
  private final TextView statusView;

  WifiReceiver(WifiManager wifiManager, Activity wifiActivity, TextView statusView, String ssid) {
    this.parent = wifiActivity;
    this.statusView = statusView;
    this.mWifiManager = wifiManager;
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
      handleChange((SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE),
          intent.hasExtra(WifiManager.EXTRA_SUPPLICANT_ERROR),
          intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0));
    } else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
      handleNetworkStateChanged((NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO));
    } else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
      final ConnectivityManager con = (ConnectivityManager) parent.getSystemService(Context.CONNECTIVITY_SERVICE);
      final NetworkInfo[] s = con.getAllNetworkInfo();
      for (final NetworkInfo i : s){
        if (i.getTypeName().contentEquals("WIFI")){
          final NetworkInfo.State state = i.getState();
          final String ssid = mWifiManager.getConnectionInfo().getSSID();

          if (state == NetworkInfo.State.CONNECTED){
            mWifiManager.saveConfiguration();
            final String label = parent.getString(R.string.wifi_connected);
            statusView.setText(label + "\n" + ssid);
            Runnable delayKill = new Killer(parent);
            delayKill.run();
          }
          if (state == NetworkInfo.State.DISCONNECTED){
            Log.d(TAG, "Got state: " + state.toString() + " for ssid: " + ssid);
            ((WifiActivity)parent).gotError();
          }
        }
      }
    }
  }

  private void handleNetworkStateChanged(NetworkInfo networkInfo) {
    final NetworkInfo.DetailedState state = networkInfo.getDetailedState();
    if (state == NetworkInfo.DetailedState.FAILED){
      Log.d(TAG, "Detailed Network state failed");
      ((WifiActivity)parent).gotError();
    }
  }

  private void handleChange(SupplicantState state, boolean hasError, int error) {
    if (hasError || state == SupplicantState.INACTIVE){
      Log.d(TAG, "Found an error");
      ((WifiActivity)parent).gotError();
    }
  }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2024.java