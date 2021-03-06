/*
 * Copyright (C) 2007-2015 FBReader.ORG Limited <contact@fbreader.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.zlibrary.ui.android.library;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.geometerplus.android.fbreader.FBReader;
import org.geometerplus.android.fbreader.api.FBReaderIntents;
import org.geometerplus.android.fbreader.libraryService.BookCollectionShadow;
import org.geometerplus.fbreader.Paths;
import org.geometerplus.fbreader.book.Book;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.zlibrary.ui.android.image.ZLAndroidImageManager;

import org.geometerplus.android.fbreader.config.ConfigShadow;

import java.io.File;

public abstract class ZLAndroidApplication {
    private static ZLAndroidLibrary myLibrary;
    private static ConfigShadow myConfig;
    private static boolean isInit = false;

    public static void init(Context application) {
        Log.e("ZLAndroidApplication","ZLAndroidApplication is Init = "+isInit );
        if(isInit == false){
            try {
                Class.forName("android.os.AsyncTask");
            } catch (Throwable t) {
            }

            myConfig = new ConfigShadow(application);
            new ZLAndroidImageManager();
            myLibrary = new ZLAndroidLibrary(application);

            new FBReaderApp(Paths.systemInfo(application), new BookCollectionShadow());
            isInit = true;
        }

    }

    public static final ZLAndroidLibrary library() {
        return myLibrary;
    }
}
