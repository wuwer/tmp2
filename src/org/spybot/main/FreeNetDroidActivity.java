package org.spybot.main;

import org.spybot.main.R;
import org.spybot.connection.ServerConnector;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

class NoLocationException extends Throwable {
	private static final long serialVersionUID = 1L;
}

public class FreeNetDroidActivity extends ListActivity {
	/** Called when the activity is first created. */
	private static final int SHOW_NETWORK = Menu.FIRST + 721;
	private static final int COMMENT = Menu.FIRST + 722;
	private static final int SHOW_COMMENTS = Menu.FIRST + 723;

	// private static final int SHOW_SINGLE_NETWORK = 0;
	private static final int GET_DATA = 1;



	private ServerConnector serverConnection;

	public FreeNetDroidActivity() {
		super();
		serverConnection = new ServerConnector(
				"http://192.168.0.12:8000/command_server");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// fillData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// fillData();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, GET_DATA, 0, R.string.get_data);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case GET_DATA:
			serverConnection.invokeCommands();
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}



	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, SHOW_NETWORK, 0, R.string.show_on_map);
		menu.add(0, COMMENT, 0, R.string.comment);
		menu.add(0, SHOW_COMMENTS, 0, R.string.show_comments);
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		registerForContextMenu(getListView());
	}
}