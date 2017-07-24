package moe.feng.common.view.breadcrumbs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem;
import moe.feng.common.view.breadcrumbs.model.FileList;
import moe.feng.common.view.breadcrumbs.demo.R;

public class SimpleFileManagerActivity extends AppCompatActivity {

	private BreadcrumbsView mBreadcrumbsView;
	private RecyclerView mRecyclerView;
	private FileManagerAdapter mAdapter;

	private String currentLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_file_manager);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mRecyclerView = findViewById(R.id.recycler_view);
		mBreadcrumbsView = findViewById(R.id.breadcrumbs_view);

		mAdapter = new FileManagerAdapter();
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.setCallback(new FileManagerAdapter.Callback() {
			@Override
			public void onItemClick(FileList.FileWrapper file) {
				if (file.isDirectory()) {
					BreadcrumbItem breadcrumbItem = new BreadcrumbItem(mAdapter.getFileList().getDirectoriesString());
					breadcrumbItem.setSelectedItem(file.toString());
					currentLocation = getCurrentPath() + "/" + file.toString();
					new LoadTask(breadcrumbItem).execute(currentLocation);
				} else if (file.isFile()) {
					// Nothing happen lol
				}
			}
		});

		mBreadcrumbsView.setCallback(new DefaultBreadcrumbsCallback() {
			@Override
			public void onNavigateBack(BreadcrumbItem item, int position) {
				currentLocation = getPath(position);
				new LoadTask().execute(currentLocation);
			}

			@Override
			public void onNavigateNewLocation(BreadcrumbItem newItem, int changedPosition) {
				currentLocation = getPath(changedPosition - 1) + "/" + newItem.getSelectedItem();
				new LoadTask().execute(currentLocation);
			}
		});

		if (savedInstanceState == null) {
			mBreadcrumbsView.addItem(BreadcrumbItem.createSimpleItem("External Storage"));
			currentLocation = getCurrentPath();
			new LoadTask().execute(currentLocation);
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentLocation = getCurrentPath();
		new LoadTask().execute(currentLocation);
	}

	private String getCurrentPath() {
		return getPath(-1);
	}

	private String getPath(int depth) {
		if (depth == -1) depth = mBreadcrumbsView.getItems().size() - 1;
		StringBuffer sb = new StringBuffer(Environment.getExternalStorageDirectory().getAbsolutePath());
		for (int i = 1; i <= depth; i++) {
			sb.append("/").append(mBreadcrumbsView.getItems().get(i).getSelectedItem());
		}
		return sb.toString();
	}

	@Override
	public void onBackPressed() {
		if (mBreadcrumbsView.getItems().size() > 1) {
			mBreadcrumbsView.removeLastItem();
			currentLocation = getCurrentPath();
			new LoadTask().execute(currentLocation);
		} else {
			super.onBackPressed();
		}
	}

	private class LoadTask extends AsyncTask<String, Void, FileList> {

		private BreadcrumbItem nextItem;

		LoadTask() {}

		LoadTask(BreadcrumbItem nextItem) {
			this.nextItem = nextItem;
		}

		@Override
		protected FileList doInBackground(String... path) {
			try {
				return FileList.newInstance(path[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(FileList list) {
			if (list != null) {
				mAdapter.setFileList(list);
				mAdapter.notifyDataSetChanged();
				if (nextItem != null) {
					mBreadcrumbsView.addItem(nextItem);
				}
			} else if (nextItem != null) {
				Snackbar.make(findViewById(R.id.coordinator_layout), "Something wrong", Snackbar.LENGTH_SHORT)
						.show();
			}
		}

	}

}
