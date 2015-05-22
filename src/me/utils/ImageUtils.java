package me.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

/**
 * 图片工具类，关于调用图库和相机，可以继承此来写，也可以单独写，请求命令用此处的REQUEST_CODE.
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ImageUtils {
	
	public static Map<String, SoftReference<Bitmap>> mPhotoOriginalCache = new HashMap<String, SoftReference<Bitmap>>();

	/** 请求相册 */
	public static final int REQUEST_CODE_GETIMAGE_BYALBUM = 0;
	/** 请求相机 */
	public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	/** 请求裁剪 */
	public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;
	/** 请求滤镜 */
	public static final int REQUEST_CODE_GETIMAGE_BYFLITER = 3;
	
	/**图片的宽**/
	public static final String BUNDLE_IMG_WIDTH = "width";
	/**图片的高**/
	public static final String BUNDLE_IMG_HEIGHT = "height";	
	
	/**
	 * 将Drawable转化为Bitmap
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable d) {
        return d == null ? null : ((BitmapDrawable)d).getBitmap();
    }

	/**
	 * 将bitmap转化为drawable
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap b) {
		return b == null ? null : new BitmapDrawable(b);
	}
	
	/**
	 * convert Bitmap to byte array
	 * @return
	 */
	public static byte[] bitmapToByte(Bitmap b) {
		if (b == null) return null;

		ByteArrayOutputStream o = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.PNG, 100, o);
		return o.toByteArray();
	}
	
	/**
     * convert byte array to Bitmap
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }
    
    /**
     * convert Drawable to byte array
     */
    public static byte[] drawableToByte(Drawable d) {
        return bitmapToByte(drawableToBitmap(d));
    }

	/**
	 * convert byte array to Drawable
	 */
	public static Drawable byteToDrawable(byte[] b) {
		return bitmapToDrawable(byteToBitmap(b));
	}

	/**
	 * 让Gallery上能马上看到该图片
	 */
	private static void insertPhoto(Context c, String imgFileName) {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File file = new File(imgFileName);
		Uri contentUri = Uri.fromFile(file);
		mediaScanIntent.setData(contentUri);
		c.sendBroadcast(mediaScanIntent);
	}
	
	/**
	 * 保存 质量为quality的bitmap图片到filePath下
	 * @param filePath
	 * @param bitmap
	 * @param quality
	 * @return
	 * @throws IOException
	 * Uri.fromFile(file);
	 */
	public static String saveImage(Context ctx, String filePath, Bitmap bitmap, int quality) throws IOException {
		if (bitmap != null) {
			File file = new File(filePath);
			if (!file.exists()) file.mkdirs();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
			bitmap.compress(CompressFormat.JPEG, quality, bos);
			bos.flush();
			bos.close();
			
			insertPhoto(ctx, filePath);//插入图库
			
			return filePath;
		} else {
			return null;
		}		
	}
	
	/**
	 * 保存 bitmap图片到filePath下
	 * @param filePath
	 * @param bitmap
	 * @throws IOException
	 * Uri.fromFile(file);
	 */
	public static String saveImage(Context ctx, String filePath, Bitmap bitmap) throws IOException {
		return saveImage(ctx, filePath, bitmap, 100);
	}
	
	/**
	 * 根据路径得到适中的图片,此处选择适中的720px的大小输出
	 * @param imagePath
	 * @return
	 */
	public static Bitmap decodeBitmapFromFile(String imagePath) {
		return decodeBitmapFromFile(imagePath, 720, 720);
	}
	
	/**
	 * 根据路径和请求显示的宽高得到最适中的图片
	 * @param imagePath
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeBitmapFromFile(String imagePath, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(imagePath, options);
	}
	
	private static int calculateSampleSize(BitmapFactory.Options options, int reqHeight, int reqWidth) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		if (width < 720) reqWidth = width;
		if (height < 720) reqHeight = height;
		
		int inSampleSize = 1;
		
		if (height > reqHeight || width > reqWidth) {		
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
	
			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}
	
	/**
	 * 根据图片本地路径得到不加任何处理(压缩,滤色...)的原图
	 * @param imagePath 路径
	 */
	public static Bitmap decodeBitmapOriginalFromFile(String imagePath) {
		if (mPhotoOriginalCache.containsKey(imagePath)) {
			Reference<Bitmap> reference = mPhotoOriginalCache.get(imagePath);
			if (reference.get() == null || reference.get().isRecycled()) {
				mPhotoOriginalCache.remove(imagePath);
			} else {
				return reference.get();
			}
		}
		
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(imagePath);
			if (bitmap == null) {
				throw new FileNotFoundException(imagePath + "is not find");
			}
			mPhotoOriginalCache.put(imagePath,
					new SoftReference<Bitmap>(bitmap));
			return bitmap;
		} catch (Exception e) {
			return null;
		} 
	}
	
	/**
	 * 获取图片的长度和宽度
	 * 
	 * @param bitmap
	 *            图片bitmap对象
	 * @return
	 */
	public static Bundle getBitmapWidthAndHeight(Bitmap bitmap) {
		Bundle bundle = null;
		if (bitmap != null) {
			bundle = new Bundle();
			bundle.putInt(BUNDLE_IMG_WIDTH, bitmap.getWidth());
			bundle.putInt(BUNDLE_IMG_HEIGHT, bitmap.getHeight());
			return bundle;
		}
		return null;
	}
	
	/**
	 * 通过uri获取文件的绝对路径
	 * @param uri
	 * @return
	 */
	public static String getAbsolutePathOfImage(Activity activity, Uri uri) {
		String filePath = "";
		
		String mUriString = uri.toString();
		mUriString = Uri.decode(mUriString);

		/**
		 * file:///storage/sdcard0/ZhiDuan/YeWen/Dcim/Crop/IMG_20140407_153038.jpg
		 * file:///storage/emulated/0/ZhiDuan/YeWen/Dcim/Crop/IMG_20140407_153038.jpg
		 * file:///mnt/sdcard/ZhiDuan/YeWen/Dcim/Crop/logo422a7ba438b4a158e5b28c6ba6f6d4b0.png
		 * file:///sdcard/ZhiDuan/YeWen/Dcim/Crop/IMG_20140407_153038.jpg
		 * file:////storage/extSdCard/aaaa.png
		 */
		String pre = "file://";

		if (mUriString.startsWith(pre)) {
			filePath = mUriString.substring(pre.length());
			
		} else {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.managedQuery(uri, proj, // Which columns to
					// return
					null, // WHERE clause; which rows to return (all rows)
					null, // WHERE clause selection arguments (none)
					null); // Order-by clause (ascending by name)

			if (cursor != null) {
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				if (cursor.getCount() > 0 && cursor.moveToFirst()) {
					filePath = cursor.getString(column_index);
				}
			}
		}
		return filePath;
	}
	
	/**
	 * 当前有无摄像头,或者是否有摄像头的使用允许
	 * @param context
	 * @return
	 */
	public static boolean hasCamera(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }
	
	/**
	 * 获取图片类型
	 * @param file
	 * @return
	 */
	public static String getImageType(File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			String type = getImageType(in);
			return type;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 获取图片的类型信息
	 * @param in
	 * @return
	 * @see #getImageType(byte[])
	 */
	public static String getImageType(InputStream in) {
		if (in == null) {
			return null;
		}
		try {
			byte[] bytes = new byte[8];
			in.read(bytes);
			return getImageType(bytes);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 获取图片的类型信息
	 * @param bytes
	 *            2~8 byte at beginning of the image file
	 * @return image mimetype or null if the file is not image
	 */
	public static String getImageType(byte[] bytes) {
		if (isJPEG(bytes)) {
			return "image/jpeg";
		}
		if (isGIF(bytes)) {
			return "image/gif";
		}
		if (isPNG(bytes)) {
			return "image/png";
		}
		if (isBMP(bytes)) {
			return "application/x-bmp";
		}
		return null;
	}

	private static boolean isJPEG(byte[] b) {
		if (b.length < 2) {
			return false;
		}
		return (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
	}

	private static boolean isGIF(byte[] b) {
		if (b.length < 6) {
			return false;
		}
		return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8' && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
	}

	private static boolean isPNG(byte[] b) {
		if (b.length < 8) {
			return false;
		}
		return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78 && b[3] == (byte) 71 && b[4] == (byte) 13
				&& b[5] == (byte) 10 && b[6] == (byte) 26 && b[7] == (byte) 10);
	}

	private static boolean isBMP(byte[] b) {
		if (b.length < 2) {
			return false;
		}
		return (b[0] == 0x42) && (b[1] == 0x4d);
	}
		
}
