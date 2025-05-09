addTest(7, 9, 180.0f);

/*
 * Copyright 2008 ZXing authors
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

package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.common.AbstractBlackBoxTestCase;

/**
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ITFBlackBox2TestCase extends AbstractBlackBoxTestCase {

  public ITFBlackBox2TestCase() {
    super("test/data/blackbox/itf-2", new MultiFormatReader(), BarcodeFormat.ITF);
    addTest(8, 9, 0.0f);
    addTest(8, 9, 180.0f);
  }

}