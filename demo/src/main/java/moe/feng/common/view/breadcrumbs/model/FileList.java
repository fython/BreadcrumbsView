package moe.feng.common.view.breadcrumbs.model;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

public class FileList {

	public List<FileWrapper> directories;
	public List<FileWrapper> files;

	private FileList() {}

	public static FileList newInstance(String path) {
		FileList fileList = new FileList();
		Log.i("FileList", path);
		fileList.directories = Arrays.asList(FileWrapper.wraps(new File(path).listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		})));
		fileList.files = Arrays.asList(FileWrapper.wraps(new File(path).listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile();
			}
		})));
		return fileList;
	}

	public static class FileWrapper {

		private File mFile;

		public FileWrapper(File file) {
			mFile = file;
		}

		public boolean isDirectory() {
			return mFile.isDirectory();
		}

		public boolean isFile() {
			return mFile.isFile();
		}

		@Override
		public String toString() {
			return mFile.getName();
		}

		public static FileWrapper[] wraps(File[] files) {
			FileWrapper[] array = new FileWrapper[files.length];
			for (int i = 0; i < files.length; i++) {
				array[i] = new FileWrapper(files[i]);
			}
			return array;
		}

	}

}