package com.example.webimage.image;

import java.io.File;
import java.lang.ref.WeakReference;

import com.example.webimage.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

/**
 * 加载本地图片到指定的ImageView控件，以异步的方式来读取图片到缓存
 * 
 * @author Li Bin
 */
public class ImageWorker {
	public static final File APP_DIR = new File(
			Environment.getExternalStorageDirectory(), "demo");

	/**
	 * 将指定路径的图片，显示在指定的ImageView控件中
	 * 
	 * @param path
	 * @param view
	 */
	public void fetch(String path, ImageView view) {
		ImageFetchTask task = null;
		// ImageView的tag如果不为空，说明这是一个被重用的控件
		if (view.getTag() != null) {
			task = ((WeakReference<ImageFetchTask>) view.getTag()).get();
			if (task != null) {
				// 如果被重用，则不显示图片
				view.setImageBitmap(null);
			}
		}

		task = new ImageFetchTask(view);
		WeakReference<ImageFetchTask> wrTask = new WeakReference<ImageWorker.ImageFetchTask>(
				task);
		view.setTag(wrTask);
		task.execute(path);
	}

	/**
	 * 异步加载图片的异步任务
	 */
	class ImageFetchTask extends AsyncTask<String, Void, Bitmap> {
		private WeakReference<ImageView> mWeakImageView;

		public ImageFetchTask(ImageView view) {
			mWeakImageView = new WeakReference<ImageView>(view);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String path = params[0];
			Bitmap bm = ImageCache.getInstance().get(path);
			if (mWeakImageView.get() != null) {
				if (bm == null) {
					ImageView imageView = mWeakImageView.get();
					Context ctx = imageView.getContext();

					int pxWidth = imageView.getWidth();
					int pxHeight = imageView.getHeight();

					// 这里一定要把图片宽高的px单位值转换成dip单位
					int reqWidth = DisplayUtil.px2Dip(ctx, pxWidth);
					int reqHeight = DisplayUtil.px2Dip(ctx, pxHeight);
					bm = getBitmapByFilePath(path, reqWidth, reqHeight);
					// 从文件读取出来之后，要put到缓存中
					ImageCache.getInstance().put(path, bm);
				}
			}
			return bm;
		}

		protected void onPostExecute(Bitmap result) {
			ImageView imageView = mWeakImageView.get();
			if (imageView != null) {
				if (result != null) {
					imageView.setImageBitmap(result);
				} else {
					imageView.setImageResource(R.drawable.ic_launcher);
				}
			}
		}
	}

	/**
	 * 从指定的文件路径，读取图片信息，作为Bitmap对象返回
	 * 
	 * @param path
	 *            图片路径
	 * @param reqWidth
	 *            需要的图片宽度
	 * @param reqHeight
	 * @return
	 */
	public Bitmap getBitmapByFilePath(String path, int reqWidth, int reqHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();

		File file = new File(APP_DIR,String.valueOf(path.hashCode()));
		
		// 设置当前只加载图片的宽高等边界信息,不加载图片本身的数据
		opts.inJustDecodeBounds = true;
		// 把读取到的图片的宽高等信息存入opts
		BitmapFactory.decodeFile(file.getAbsolutePath(), opts);

		// 计算缩放比
		opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight);
		// 加载图片数据
		opts.inJustDecodeBounds = false;// 为啥你懂得
		Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(),
				opts);

		return bm;
	}

	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

			final float totalPixels = width * height;
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;
			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

}

