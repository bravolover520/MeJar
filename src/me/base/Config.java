package me.base;

import me.utils.TextUtils;
import android.content.Context;
import android.content.res.Resources;

/**
 * 程序配置类，主要是配置文件，目录，及其公司信息配置
 * @author Jesus{931178805@qq.com}
 * 2014年7月9日
 */
public class Config {

	public final Resources resources;
	public final boolean writeLogs;
	
	public final String preferName;
	
	public final boolean reportsDelete;
	public final String reportsExtension;
	public final String reportsTrace;
	
	public final String supportCompany;
	public final String supportCopyright;
	public final String supportWebsite;
	public final String supportTel;
	public final String supportEmail;
	
	public final String dirAppRoot;
	public final String dirCache;
	public final String dirImgCache;
	public final String dirDcim;
	public final String dirTemp;
	public final String dirCrop;
	public final String dirErrorLog;
	public final String dirApk;
	public final String dirSound;
	public final String dirMap;
	public final String dirEmoticon;
	
	private Config(final Builder builder) {
		resources = builder.context.getResources();
		writeLogs = builder.writeLogs;
		preferName = builder.preferName;
		reportsDelete = builder.reportsDelete;
		reportsExtension = builder.reportsExtension;
		reportsTrace = builder.reportsTrace;
		supportCompany = builder.supportCompany;
		supportCopyright = builder.supportCopyright;
		supportWebsite = builder.supportWebsite;
		supportTel = builder.supportTel;
		supportEmail = builder.supportEmail;
		dirAppRoot = builder.dirAppRoot;
		dirCache = builder.dirCache;
		dirImgCache = builder.dirImgCache;
		dirDcim = builder.dirDcim;
		dirTemp = builder.dirTemp;
		dirCrop = builder.dirCrop;
		dirErrorLog = builder.dirErrorLog;
		dirApk = builder.dirApk;
		dirSound = builder.dirSound;
		dirMap = builder.dirMap;
		dirEmoticon = builder.dirEmoticon;
	}
	
	
	
	/**
	 * Builder for Common
	 * @author Jesus
	 * 2014年7月8日
	 */
	public static class Builder {
		private Context context;
		
		public int sha1Develop;
		public int sha1Release;
		
		private boolean writeLogs = false;
		
		private String preferName = null;
		
		private boolean reportsDelete = true;
		private String reportsExtension = null;
		private String reportsTrace = null;
		
		private String supportCompany = null;
		private String supportCopyright = null;
		private String supportWebsite = null;
		private String supportTel = null;
		private String supportEmail = null;
		
		private String dirAppRoot = null;
		private String dirCache = null;
		private String dirImgCache = null;
		private String dirDcim = null;
		private String dirTemp = null;
		private String dirCrop = null;
		private String dirErrorLog = null;
		private String dirApk = null;
		private String dirSound = null;
		private String dirMap = null;
		private String dirEmoticon = null;
		
		public Builder(Context context) {
			this.context = context.getApplicationContext();
		}
		
		/**
		 * 是否写日志
		 * @return
		 */
		public Builder writeDebugLogs() {
			this.writeLogs = true;
			return this;
		}
		
		/**
		 * 配置文件名
		 * @param preferName
		 * @return
		 */
		public Builder preferName(String preferName) {
			this.preferName = preferName;
			return this;
		}
		
		/**
		 * 错误日志上传后是否删除. default true.
		 * @param reportsDelete
		 * @return
		 */
		public Builder reportsDelete(boolean reportsDelete) {
			this.reportsDelete = reportsDelete;
			return this;
		}
		
		/**
		 * 错误日志后缀. default ".log".
		 * @param reportsExtension
		 * @return
		 */
		public Builder reportsExtension(String reportsExtension) {
			this.reportsExtension = reportsExtension;
			return this;
		}
		
		/**
		 * 错误日志搜集标示. default "trace".
		 * @param reportsTrace
		 * @return
		 */
		public Builder reportsTrace(String reportsTrace) {
			this.reportsTrace = reportsTrace;
			return this;
		}
		
		/**
		 * 公司名.
		 * @param supportCompany
		 * @return
		 */
		public Builder supportCompany(String supportCompany) {
			this.supportCompany = supportCompany;
			return this;
		}
		
		/**
		 * 公司版权。
		 * @param supportCopyright
		 * @return
		 */
		public Builder supportCopyright(String supportCopyright) {
			this.supportCopyright = supportCopyright;
			return this;
		}
		
		/**
		 * 公司外站站点.
		 * @param supportWebsite
		 * @return
		 */
		public Builder supportWebsite(String supportWebsite) {
			this.supportWebsite = supportWebsite;
			return this;
		}
		
		/**
		 * 公司服务电话.
		 * @param supportTel
		 * @return
		 */
		public Builder supportTel(String supportTel) {
			this.supportTel = supportTel;
			return this;
		}
		
		/**
		 * 公司支持邮箱.
		 * @param supportEmail
		 * @return
		 */
		public Builder supportEmail(String supportEmail) {
			this.supportEmail = supportEmail;
			return this;
		}
		
		/**
		 * 程序默认根目录. default 
		 * @param dirAppRoot
		 * @return
		 */
		public Builder dirAppRoot(String dirAppRoot) {
			this.dirAppRoot = dirAppRoot;
			return this;
		}
		
		/**
		 * 程序缓存目录. etc File,Json,XML...
		 * @param dirCache
		 * @return
		 */
		public Builder dirCache(String dirCache) {
			this.dirCache = dirCache;
			return this;
		}
		
		/**
		 * 程序图片缓存目录.
		 * @param dirImgCache
		 * @return
		 */
		public Builder dirImgCache(String dirImgCache) {
			this.dirImgCache = dirImgCache;
			return this;
		}
		
		/**
		 * 程序内部拍照目录.
		 * @param dirDcim
		 * @return
		 */
		public Builder dirTemp(String dirTemp) {
			this.dirTemp = dirTemp;
			return this;
		}
		
		/**
		 * 程序内部截图目录.
		 * @param dirCrop
		 * @return
		 */
		public Builder dirCrop(String dirCrop) {
			this.dirCrop = dirCrop;
			return this;
		}
		
		/**
		 * 程序抛出错误日志目录.
		 * @param dirErrorLog
		 * @return
		 */
		public Builder dirErrorLog(String dirErrorLog) {
			this.dirErrorLog = dirErrorLog;
			return this;
		}
		
		/**
		 * 程序APK包目录.
		 * @param dirApk
		 * @return
		 */
		public Builder dirApk(String dirApk) {
			this.dirApk = dirApk;
			return this;
		}
		
		/**
		 * 程序声音目录.
		 * @param dirSound
		 * @return
		 */
		public Builder dirSound(String dirSound) {
			this.dirSound = dirSound;
			return this;
		}
		
		/**
		 * 程序地图目录.
		 * @param dirMap
		 * @return
		 */
		public Builder dirMap(String dirMap) {
			this.dirMap = dirMap;
			return this;
		}
		
		/**
		 * 程序emoji表情目录.
		 * @param dirMap
		 * @return
		 */
		public Builder dirEmoticon(String dirEmoticon) {
			this.dirEmoticon = dirEmoticon;
			return this;
		}
		
		/** Builds configured {@link Common} object */
		public Config build() {
			initEmptyFieldsWithDefaultValues();
			return new Config(this);
		}
		
		private void initEmptyFieldsWithDefaultValues() {
			if (null == preferName)
				preferName = "_preferences";
			if (null == reportsExtension)
				reportsExtension = ".log";
			if (null == reportsTrace)
				reportsTrace = "trace";
			if (TextUtils.isEmpty(dirAppRoot))
				dirAppRoot = "Jesus";
			if (TextUtils.isEmpty(dirCache))
				dirCache = "Cache";
			if (TextUtils.isEmpty(dirImgCache))
				dirImgCache = "ImgCache";
			if (TextUtils.isEmpty(dirDcim))
				dirDcim = "Dcim";
			if (TextUtils.isEmpty(dirTemp))
				dirTemp = "Temp";
			if (TextUtils.isEmpty(dirCrop))
				dirCrop = "Crop";
			if (TextUtils.isEmpty(dirErrorLog))
				dirErrorLog = "ErrorLog";
			if (TextUtils.isEmpty(dirApk))
				dirApk = "Apk";
			if (TextUtils.isEmpty(dirSound))
				dirSound = "Sound";
			if (TextUtils.isEmpty(dirMap))
				dirMap = "Map";
			if (TextUtils.isEmpty(dirEmoticon))
				dirEmoticon = "Emoticon";
		}
	}
}
