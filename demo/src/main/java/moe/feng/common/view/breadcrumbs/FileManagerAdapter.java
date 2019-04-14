package moe.feng.common.view.breadcrumbs;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import moe.feng.common.view.breadcrumbs.model.FileList;
import moe.feng.common.view.breadcrumbs.demo.R;

public class FileManagerAdapter extends RecyclerView.Adapter<FileManagerAdapter.FileHolder> {

	private FileList fileList;
	private Callback callback;

	@Override
	public FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new FileHolder(LayoutInflater.from(parent.getContext())
				.inflate(R.layout.file_manager_item, parent, false));
	}

	@Override
	public void onBindViewHolder(FileHolder holder, int position) {
		if (position < fileList.directories.size()) {
			// Directories
			holder.icon.setImageResource(R.drawable.ic_folder_black_24dp);
			holder.title.setText(fileList.directories.get(position).toString());
		} else {
			// Files
			holder.icon.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
			holder.title.setText(fileList.files.get(position - fileList.directories.size()).toString());
		}
	}

	@Override
	public int getItemCount() {
		return fileList != null ? fileList.directories.size() + fileList.files.size() : 0;
	}

	public void setFileList(FileList fileList) {
		this.fileList = fileList;
	}

	public FileList getFileList() {
		return this.fileList;
	}

	public Callback getCallback() {
		return callback;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	class FileHolder extends RecyclerView.ViewHolder {

		ImageView icon;
		TextView title;

		public FileHolder(View itemView) {
			super(itemView);
			icon = itemView.findViewById(R.id.file_icon);
			title = itemView.findViewById(R.id.file_name);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (callback != null) {
						int position = getAdapterPosition();
						if (position < fileList.directories.size()) {
							callback.onItemClick(fileList.directories.get(position));
						} else {
							callback.onItemClick(fileList.files.get(position - fileList.directories.size()));
						}
					}
				}
			});
		}

	}

	interface Callback {

		void onItemClick(FileList.FileWrapper file);

	}

}
