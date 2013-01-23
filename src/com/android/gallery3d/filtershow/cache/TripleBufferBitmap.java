/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.android.gallery3d.filtershow.cache;

import android.graphics.Bitmap;
import com.android.gallery3d.app.Log;

public class TripleBufferBitmap {

    private static String LOGTAG = "TripleBufferBitmap";

    private Bitmap mBitmaps[] = new Bitmap[3];
    private Bitmap mProducer = null;
    private Bitmap mConsumer = null;
    private Bitmap mIntermediate = null;
    private boolean mNeedsSwap = false;

    private final Bitmap.Config mBitmapConfig = Bitmap.Config.ARGB_8888;

    public TripleBufferBitmap() {

    }

    public synchronized void updateBitmaps(Bitmap bitmap) {
        mBitmaps[0] = bitmap.copy(mBitmapConfig, true);
        mBitmaps[1] = bitmap.copy(mBitmapConfig, true);
        mBitmaps[2] = bitmap.copy(mBitmapConfig, true);
        mProducer = mBitmaps[0];
        mConsumer = mBitmaps[1];
        mIntermediate = mBitmaps[2];
    }

    public synchronized Bitmap getProducer() {
        return mProducer;
    }

    public synchronized Bitmap getConsumer() {
        return mConsumer;
    }

    public synchronized void swapProducer() {
        Bitmap intermediate = mIntermediate;
        mIntermediate = mProducer;
        mProducer = intermediate;
        mNeedsSwap = true;
    }

    public synchronized void swapConsumer() {
        if (!mNeedsSwap) {
            return;
        }
        Bitmap intermediate = mIntermediate;
        mIntermediate = mConsumer;
        mConsumer = intermediate;
        mNeedsSwap = false;
    }
}