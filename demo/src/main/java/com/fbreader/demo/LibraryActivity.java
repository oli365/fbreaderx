package com.fbreader.demo;

import android.Manifest;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fbreader.common.FBReaderHelper;

import org.geometerplus.android.fbreader.FBReader;
import org.geometerplus.android.fbreader.FBReaderApplication;
import org.geometerplus.fbreader.book.Book;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;


public class LibraryActivity extends ListActivity
{
	protected final int UPDATE_DELAY = 5000;
	protected final int PERMISSION_REQUEST = 42;

	protected SharedPreferences prefs;
	protected File topDirectory, currentDirectory;
	protected ArrayAdapter<Item> adapter;
	protected Timer updateTimer;
	private FBReaderHelper fbReaderHelper = null;

	protected static class Item {
		public File file;
		public String string;
		public Item(File file) {
			this.file = file;
			if (file.isDirectory())
				string = file.getName() + "/";
			else
				string = file.getName();
		}
		public Item(File file, String string) {
			this.file = file;
			this.string = string;
		}
		public String toString() {
			return string;
		}
	}

	protected boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
			return true;
		return false;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FBReaderApplication.init(this);
		fbReaderHelper = new FBReaderHelper(this);
		prefs = getPreferences(Context.MODE_PRIVATE);

		topDirectory = Environment.getExternalStorageDirectory();
		currentDirectory = new File(prefs.getString("currentDirectory", topDirectory.getAbsolutePath()));

		adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1);
		setListAdapter(adapter);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
	}

	public void onResume() {
		super.onResume();
		TimerTask updateTask = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						updateFileList();
					}
				});
			}
		};
		updateTimer = new Timer();
		updateTimer.scheduleAtFixedRate(updateTask, 0, UPDATE_DELAY);
//		fbReaderHelper.bindToService(null);
	}

	public void onPause() {
		super.onPause();
		updateTimer.cancel();
		updateTimer = null;
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("currentDirectory", currentDirectory.getAbsolutePath());
		editor.apply();
//		fbReaderHelper.unBind();
	}

	protected void updateFileList() {
		adapter.clear();

		if (!isExternalStorageReadable()) {
			adapter.add(new Item(topDirectory, "library_no_external_storage"));
			return;
		}

		if (!currentDirectory.isDirectory()) {
			adapter.add(new Item(topDirectory, "library_not_a_directory"));
			return;
		}

		String curPath = currentDirectory.getAbsolutePath();
		String topPath = topDirectory.getAbsolutePath();
		if (curPath.startsWith(topPath))
			curPath = "~" + curPath.substring(topPath.length());
		setTitle(curPath);

		File parent = currentDirectory.getParentFile();
		if (parent != null && !currentDirectory.equals(topDirectory))
			adapter.add(new Item(parent, "../"));

		File[] files = currentDirectory.listFiles(new FileFilter() {
			public boolean accept(File file) {
				if (file.isDirectory())
					return true;
				return true;
			}
		});

		if (files == null)
			adapter.add(new Item(topDirectory, "根目录"));
		else
			for (File file : files)
				adapter.add(new Item(file,file.getName()));

		adapter.sort(new Comparator<Item>() {
			public int compare(Item a, Item b) {
				boolean ad = a.file.isDirectory();
				boolean bd = b.file.isDirectory();
				if (ad && !bd) return -1;
				if (bd && !ad) return 1;
				if (a.string.equals("../")) return -1;
				if (b.string.equals("../")) return 1;
				return a.string.compareTo(b.string);
			}
		});
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		final Item item = adapter.getItem(position);

		if (item.file.isDirectory()) {
			currentDirectory = item.file;
			updateFileList();
			return;
		}

		if (!item.file.isFile())
			return;

		fbReaderHelper.bindToService(new Runnable() {
			@Override
			public void run() {
				Book book = fbReaderHelper.getCollection().getBookByFile(item.file.getPath());
				FBReader.openBook(LibraryActivity.this,book,null);
				fbReaderHelper.unBind();
//				TimerTask task = new TimerTask() {
//					@Override
//					public void run() {
//						fbReaderHelper.unBind();
//					}
//				};
//				Timer timer = new Timer();
//				timer.schedule(task, 10);

			}
		});
	}
}
