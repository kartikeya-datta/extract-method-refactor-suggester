error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8517.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8517.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8517.java
text:
```scala
public static final S@@tring ONE_D_CODE_TYPES = PRODUCT_CODE_TYPES + ",CODE_39,CODE_93,CODE_128";

/*
 * Copyright 2009 ZXing authors
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

package com.google.zxing.integration.android;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/**
 * <p>A utility class which helps ease integration with Barcode Scanner via {@link Intent}s. This is a simple
 * way to invoke barcode scanning and receive the result, without any need to integrate, modify, or learn the
 * project's source code.</p>
 *
 * <h2>Initiating a barcode scan</h2>
 *
 * <p>Integration is essentially as easy as calling {@link #initiateScan(Activity)} and waiting
 * for the result in your app.</p>
 *
 * <p>It does require that the Barcode Scanner application is installed. The
 * {@link #initiateScan(Activity)} method will prompt the user to download the application, if needed.</p>
 *
 * <p>There are a few steps to using this integration. First, your {@link Activity} must implement
 * the method {@link Activity#onActivityResult(int, int, Intent)} and include a line of code like this:</p>
 *
 * <p>{@code
 * public void onActivityResult(int requestCode, int resultCode, Intent intent) {
 *   IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
 *   if (scanResult != null) {
 *     // handle scan result
 *   }
 *   // else continue with any other code you need in the method
 *   ...
 * }
 * }</p>
 *
 * <p>This is where you will handle a scan result.
 * Second, just call this in response to a user action somewhere to begin the scan process:</p>
 *
 * <p>{@code IntentIntegrator.initiateScan(yourActivity);}</p>
 *
 * <p>You can use {@link #initiateScan(Activity, CharSequence, CharSequence, CharSequence, CharSequence)} or
 * {@link #initiateScan(Activity, int, int, int, int)} to customize the download prompt with
 * different text labels.</p>
 *
 * <p>Note that {@link #initiateScan(Activity)} returns an {@link AlertDialog} which is non-null if the
 * user was prompted to download the application. This lets the calling app potentially manage the dialog.
 * In particular, ideally, the app dismisses the dialog if it's still active in its {@link Activity#onPause()}
 * method.</p>
 *
 * <h2>Sharing text via barcode</h2>
 *
 * <p>To share text, encoded as a QR Code on-screen, similarly, see {@link #shareText(Activity, CharSequence)}.</p>
 *
 * <p>Some code, particularly download integration, was contributed from the Anobiit application.</p>
 *
 * @author Sean Owen
 * @author Fred Lin
 * @author Isaac Potoczny-Jones
 * @author Brad Drehmer
 */
public final class IntentIntegrator {

  public static final int REQUEST_CODE = 0x0ba7c0de; // get it?

  public static final String DEFAULT_TITLE = "Install Barcode Scanner?";
  public static final String DEFAULT_MESSAGE =
      "This application requires Barcode Scanner. Would you like to install it?";
  public static final String DEFAULT_YES = "Yes";
  public static final String DEFAULT_NO = "No";

  // supported barcode formats
  public static final String PRODUCT_CODE_TYPES = "UPC_A,UPC_E,EAN_8,EAN_13";
  public static final String ONE_D_CODE_TYPES = PRODUCT_CODE_TYPES + ",CODE_39,CODE_128";
  public static final String QR_CODE_TYPES = "QR_CODE";
  public static final String ALL_CODE_TYPES = null;

  private IntentIntegrator() {
  }

  /**
   * See {@link #initiateScan(Activity, CharSequence, CharSequence, CharSequence, CharSequence)} --
   * same, but uses default English labels.
   */
  public static AlertDialog initiateScan(Activity activity) {
    return initiateScan(activity, DEFAULT_TITLE, DEFAULT_MESSAGE, DEFAULT_YES, DEFAULT_NO);
  }

  /**
   * See {@link #initiateScan(Activity, CharSequence, CharSequence, CharSequence, CharSequence)} --
   * same, but takes string IDs which refer
   * to the {@link Activity}'s resource bundle entries.
   */
  public static AlertDialog initiateScan(Activity activity,
                                         int stringTitle,
                                         int stringMessage,
                                         int stringButtonYes,
                                         int stringButtonNo) {
    return initiateScan(activity,
                        activity.getString(stringTitle),
                        activity.getString(stringMessage),
                        activity.getString(stringButtonYes),
                        activity.getString(stringButtonNo));
  }

  /**
   * See {@link #initiateScan(Activity, CharSequence, CharSequence, CharSequence, CharSequence, CharSequence)} --
   * same, but scans for all supported barcode types.
   * @param stringTitle title of dialog prompting user to download Barcode Scanner
   * @param stringMessage text of dialog prompting user to download Barcode Scanner
   * @param stringButtonYes text of button user clicks when agreeing to download
   *  Barcode Scanner (e.g. "Yes")
   * @param stringButtonNo text of button user clicks when declining to download
   *  Barcode Scanner (e.g. "No")
   * @return an {@link AlertDialog} if the user was prompted to download the app,
   *  null otherwise
   */
  public static AlertDialog initiateScan(Activity activity,
                                         CharSequence stringTitle,
                                         CharSequence stringMessage,
                                         CharSequence stringButtonYes,
                                         CharSequence stringButtonNo) {

    return initiateScan(activity,
                        stringTitle,
                        stringMessage,
                        stringButtonYes,
                        stringButtonNo,
                        ALL_CODE_TYPES);
  }

  /**
   * Invokes scanning.
   *
   * @param stringTitle title of dialog prompting user to download Barcode Scanner
   * @param stringMessage text of dialog prompting user to download Barcode Scanner
   * @param stringButtonYes text of button user clicks when agreeing to download
   *  Barcode Scanner (e.g. "Yes")
   * @param stringButtonNo text of button user clicks when declining to download
   *  Barcode Scanner (e.g. "No")
   * @param stringDesiredBarcodeFormats a comma separated list of codes you would
   *  like to scan for.
   * @return an {@link AlertDialog} if the user was prompted to download the app,
   *  null otherwise
   * @throws InterruptedException if timeout expires before a scan completes
   */
  public static AlertDialog initiateScan(Activity activity,
                                         CharSequence stringTitle,
                                         CharSequence stringMessage,
                                         CharSequence stringButtonYes,
                                         CharSequence stringButtonNo,
                                         CharSequence stringDesiredBarcodeFormats) {
    Intent intentScan = new Intent("com.google.zxing.client.android.SCAN");
    intentScan.addCategory(Intent.CATEGORY_DEFAULT);

    // check which types of codes to scan for
    if (stringDesiredBarcodeFormats != null) {
      // set the desired barcode types
      intentScan.putExtra("SCAN_FORMATS", stringDesiredBarcodeFormats);
    }

    try {
      activity.startActivityForResult(intentScan, REQUEST_CODE);
      return null;
    } catch (ActivityNotFoundException e) {
      return showDownloadDialog(activity, stringTitle, stringMessage, stringButtonYes, stringButtonNo);
    }
  }

  private static AlertDialog showDownloadDialog(final Activity activity,
                                                CharSequence stringTitle,
                                                CharSequence stringMessage,
                                                CharSequence stringButtonYes,
                                                CharSequence stringButtonNo) {
    AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity);
    downloadDialog.setTitle(stringTitle);
    downloadDialog.setMessage(stringMessage);
    downloadDialog.setPositiveButton(stringButtonYes, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialogInterface, int i) {
        Uri uri = Uri.parse("market://search?q=pname:com.google.zxing.client.android");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
      }
    });
    downloadDialog.setNegativeButton(stringButtonNo, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialogInterface, int i) {}
    });
    return downloadDialog.show();
  }


  /**
   * <p>Call this from your {@link Activity}'s
   * {@link Activity#onActivityResult(int, int, Intent)} method.</p>
   *
   * @return null if the event handled here was not related to {@link IntentIntegrator}, or
   *  else an {@link IntentResult} containing the result of the scan. If the user cancelled scanning,
   *  the fields will be null.
   */
  public static IntentResult parseActivityResult(int requestCode, int resultCode, Intent intent) {
    if (requestCode == REQUEST_CODE) {
      if (resultCode == Activity.RESULT_OK) {
        String contents = intent.getStringExtra("SCAN_RESULT");
        String formatName = intent.getStringExtra("SCAN_RESULT_FORMAT");
        return new IntentResult(contents, formatName);
      } else {
        return new IntentResult(null, null);
      }
    }
    return null;
  }

  /**
   * See {@link #shareText(Activity, CharSequence, CharSequence, CharSequence, CharSequence, CharSequence)} --
   * same, but uses default English labels.
   */
  public static void shareText(Activity activity, CharSequence text) {
    shareText(activity, text, DEFAULT_TITLE, DEFAULT_MESSAGE, DEFAULT_YES, DEFAULT_NO);
  }

  /**
   * See {@link #shareText(Activity, CharSequence, CharSequence, CharSequence, CharSequence, CharSequence)} --
   * same, but takes string IDs which refer to the {@link Activity}'s resource bundle entries.
   */
  public static void shareText(Activity activity,
                               CharSequence text,
                               int stringTitle,
                               int stringMessage,
                               int stringButtonYes,
                               int stringButtonNo) {
    shareText(activity,
              text,
              activity.getString(stringTitle),
              activity.getString(stringMessage),
              activity.getString(stringButtonYes),
              activity.getString(stringButtonNo));
  }

  /**
   * Shares the given text by encoding it as a barcode, such that another user can
   * scan the text off the screen of the device.
   *
   * @param text the text string to encode as a barcode
   * @param stringTitle title of dialog prompting user to download Barcode Scanner
   * @param stringMessage text of dialog prompting user to download Barcode Scanner
   * @param stringButtonYes text of button user clicks when agreeing to download
   *  Barcode Scanner (e.g. "Yes")
   * @param stringButtonNo text of button user clicks when declining to download
   *  Barcode Scanner (e.g. "No")
   */
  public static void shareText(Activity activity,
                               CharSequence text,
                               CharSequence stringTitle,
                               CharSequence stringMessage,
                               CharSequence stringButtonYes,
                               CharSequence stringButtonNo) {

    Intent intent = new Intent();
    intent.setAction("com.google.zxing.client.android.ENCODE");
    intent.putExtra("ENCODE_TYPE", "TEXT_TYPE");
    intent.putExtra("ENCODE_DATA", text);
    try {
      activity.startActivity(intent);
    } catch (ActivityNotFoundException e) {
      showDownloadDialog(activity, stringTitle, stringMessage, stringButtonYes, stringButtonNo);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8517.java