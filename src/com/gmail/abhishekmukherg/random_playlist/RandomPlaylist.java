package com.gmail.abhishekmukherg.random_playlist;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RandomPlaylist extends ListActivity {
    private static final String LOG_TAG = "random_playlist";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	if (true) {
	    StrictMode.enableDefaults();
	}
	super.onCreate(savedInstanceState);

	setCursors();
	setClickListeners();
	startMovingThread();
	Log.d(LOG_TAG, "Set up!");

	setContentView(R.layout.main);
    }

    private int currentPosition = 0;
    private Thread movingThread;

    private void startMovingThread() {
	movingThread = new Thread(new Runnable() {
	    @Override
	    public void run() {
		while (true) {
		    runOnUiThread(new Runnable() {
			public void run() {
			    getListView().smoothScrollToPosition(
				    currentPosition);
			    currentPosition += 1;
			}
		    });
		    try {
			Thread.sleep(1000);
		    } catch (InterruptedException e) {
			return;
		    }
		}
	    }
	});

	movingThread.start();
    }

    private void setCursors() {
	String[] proj = { MediaStore.Audio.Media._ID,
		MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST };
	String[] from = { MediaStore.Audio.Media.TITLE,
		MediaStore.Audio.Media.ARTIST };
	int[] to = { R.id.row_title, R.id.row_artist };
	Cursor managedQuery = managedQuery(
		MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null,
		"random()");
	startManagingCursor(managedQuery);
	SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
		R.layout.list_item, managedQuery, from, to);
	setListAdapter(adapter);
    }

    private void setClickListeners() {
	ListView lv = getListView();
	lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		Log.d(LOG_TAG, "CLICKED!");
		// When clicked, show a toast with the TextView text
		Toast.makeText(getApplicationContext(),
			((TextView) view).getText(), Toast.LENGTH_SHORT).show();
	    }
	});
    }
}