package moe.feng.common.view.breadcrumbs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import moe.feng.common.view.breadcrumbs.demo.R;
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private BreadcrumbsView mBreadcrumbsView;
	private CoordinatorLayout mCoordinatorLayout;

	private int counter = 3;

	private static final int REQUEST_PERMISSION_CODE = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mCoordinatorLayout = findViewById(R.id.coordinator_layout);

		mBreadcrumbsView = findViewById(R.id.breadcrumbs_view);
		mBreadcrumbsView.setItems(new ArrayList<>(Arrays.asList(
				BreadcrumbItem.createSimpleItem("Root Path"),
				createItem("Path 1"),
				createItem("Path 2")
		)));
		mBreadcrumbsView.setCallback(new DefaultBreadcrumbsCallback() {
			@Override
			public void onNavigateBack(BreadcrumbItem item, int position) {
				Snackbar.make(
						mCoordinatorLayout,
						"onNavigateBack: " + position + " titles=" + item.getSelectedItemTitle(),
						Snackbar.LENGTH_LONG
				).show();
			}

			@Override
			public void onNavigateNewLocation(BreadcrumbItem newItem, int changedPosition) {
				Snackbar.make(
						mCoordinatorLayout,
						"onItemChange: " + changedPosition + " nextTitle=" + newItem.getSelectedItemTitle(),
						Snackbar.LENGTH_LONG
				).show();
			}
		});

		findViewById(R.id.btn_add_more).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mBreadcrumbsView.addItem(createItem("Path " + counter++));
			}
		});
		findViewById(R.id.btn_simple_file_manager).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (ContextCompat.checkSelfPermission(MainActivity.this,
						Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions(MainActivity.this,
							new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
				} else {
					openFileManager();
				}
			}
		});
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		if (requestCode == REQUEST_PERMISSION_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				openFileManager();
			} else {
				// I am lazy. It's unnecessary to tell a developer why cannot deny the permission.
			}
		}
	}

	private void openFileManager() {
		Intent intent = new Intent(this, SimpleFileManagerActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private static BreadcrumbItem createItem(String title) {
		List<String> list = new ArrayList<>();
		list.add(title);
		list.add(title + " A");
		list.add(title + " B");
		list.add(title + " C");
		return new BreadcrumbItem(list);
	}

}
