package me.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import me.base.Config;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

/**
 * 文件操作工具包
 * File类的对象可以代表一个具体的文件路径,可以是:1.绝对路径，2.相对路径，3.一个文件夹(文件夹也是文件路径的一种)
 * <li>(1.)createNewFile() 创建指定的文件。该方法只能用于创建文件，不能用于创建文件夹，且文件路径中包含的文件夹必须存在。
 * <li>(2.)delete() 删除当前文件或文件夹。如果删除的是文件夹，则该文件夹必须为空。如果需要删除一个非空的文件夹，则需要首先删除该文件夹内部的每个文件和文件夹，然后再删除。
 * <li>(3.)exists() 判断当前文件或文件夹是否存在
 * <li>(4.)getAbsolutePath() 获得当前文件或文件夹的绝对路径
 * <li>(5.)getName() 获得当前文件或文件夹的名称
 * <li>(6.)getParent() 获得当前路径中的父路径
 * <li>(7.)length() 返回文件存储时占用的字节数。该数值获得的是文件的实际大小，而不是文件在存储时占用的空间数。
 * <li>(8.)list() 返回当前文件夹下所有的文件名和文件夹名称。说明，该名称不是绝对路径。
 * <li>(9.)listFiles() 返回当前文件夹下所有的文件对象
 * <li>(10.)mkdir() 创建当前文件文件夹，而不创建该路径中的其它文件夹。假设d盘下只有一个test文件夹，则创建d:\test\abc文件夹则成功，如果创建d:\a\b文件夹则创建失败，因为该路径中d:\a文件夹不存在。
 * <li>(11.)mkdirs() 创建文件夹，如果当前路径中包含的父目录不存在时，也会自动根据需要创建。
 * <li>(12.)renameTo(File dest) 修改文件名。在修改文件名时不能改变文件路径，如果该路径下已有该文件，则会修改失败。
 * <li>(13.)setReadOnly() 设置当前文件或文件夹为只读。
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
public class FileUtils {
	
	private static final String LOG_INIT_CONFIG = "Initialize FileUtils with configuration.";
	private static final String ERROR_INIT_CONFIG_WITH_NULL = "FileUtils configuration can not be initialized with null.";
	private static final String WARNING_RE_INIT_CONFIG = "Try to initialize FileUtils which had already been initialized before.";
	
	public static final String UTF8_ENCODING = "UTF-8";
	public final static String FILE_EXTENSION_SEPARATOR = ".";
	
	private static Config config;
		
	/** gb to byte **/
    public static final long GB_2_BYTE = 1073741824;
    /** mb to byte **/
    public static final long MB_2_BYTE = 1048576;
    /** kb to byte **/
    public static final long KB_2_BYTE = 1024;
    
    private volatile static FileUtils instance;
    
    /** Returns singleton class instance */
	public static FileUtils getInstance() {
		if (instance == null) {
			synchronized (FileUtils.class) {
				if (instance == null) {
					instance = new FileUtils();
				}
			}
		}
		return instance;
	}
    
    public synchronized void init(Config cfg) {
    	if (cfg == null) {
			throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
		}
    	if (config == null) {
			if (cfg.writeLogs) L.d(LOG_INIT_CONFIG);
			config = cfg;
		} else {
			L.w(WARNING_RE_INIT_CONFIG);
		}
    }
    
    public static String system() {
    	return new StringBuilder(FileUtils.class.getSimpleName() + "\n")
    	.append(getAppRootPath() + "\n")	//根目录
    	.append(getCachePath() + "\n")		//缓存目录
    	.append(getErrorLogPath() + "\n")	//错误日志目录
    	.toString();
    }

	/** 得到Sd卡根目录 **/
	public static String getSdPath() {
		if (isSDIsMounted()) 
			return android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
		else {
			//提示未挂载异常
			return null;
		}
	}

	/*** 
	 * 得到本程序的根目录
	 * @return mnt/sdcard/appRoot/
	 */
	public static String getAppRootPath() {
		String path = getSdPath() + config.dirAppRoot + File.separator;
		if (!new File(path).exists()) new File(path).mkdirs();
		return path;
	}	

	/**
	 * 得到缓存目录
	 * @return mnt/sdcard/appRoot/Cache/
	 */
	public static String getCachePath() {
		String path = getAppRootPath() + config.dirCache + File.separator;
		if (!new File(path).exists()) new File(path).mkdirs();
		return path;
	}

	/**
	 * 得到图片缓存目录
	 * @return	mnt/sdcard/appRoot/Cache/ImgCache/
	 */
	public static String getImageCachePath() {
		String path = getCachePath() + config.dirImgCache + File.separator;
		if (!new File(path).exists()) new File(path).mkdirs();
		return path;
	}

	/**
	 * 得到自定义相机的图片目录
	 * @return mnt/sdcard/appRoot/Dcim/
	 */
	public static String getDcimPath() {
		String path = getAppRootPath() + config.dirDcim + File.separator;
		if (!new File(path).exists()) new File(path).mkdirs();
		return path;
	}

	/**
	 * 得到临时文件的目录
	 * @return mnt/sdcard/appRoot/Temp
	 */
	public static String getTempPath() {
		String path = getAppRootPath() + config.dirTemp + File.separator;
		if (!new File(path).exists()) new File(path).mkdirs();
		return path;
	}
	
	/**
	 * 得到截图之后的图片目录
	 * @return mnt/sdcard/appRoot/Dcim/Temp
	 */
	public static String getDcimCropPath() {
		String path = getDcimPath() + config.dirCrop + File.separator;
		if (!new File(path).exists()) new File(path).mkdirs();
		return path;
	}

	/**
	 * 得到错误日志目录 
	 * @return mnt/sdcard/appRoot/ErrorLog
	 */
	public static String getErrorLogPath() {
		String path = getAppRootPath() + config.dirErrorLog + File.separator;
		if (!new File(path).exists()) new File(path).mkdirs();
		return path;
	}
	
	/**
	 * 得到Apk目录
	 * @return mnt/sdcard/appRoot/Apk
	 */
	public static String getApkPath() {
		String path = getAppRootPath() + config.dirApk + File.separator;
		if (!new File(path).exists()) new File(path).mkdirs();
		return path;
	}
	
	/**
	 * 得到Sound声音文件目录
	 * @return mnt/sdcard/appRoot/Sound
	 */
	public static String getSoundPath() {
		String path = getAppRootPath() + config.dirSound + File.separator;
		if (!new File(path).exists()) new File(path).mkdirs();
		return path;
	}
	
	/**
	 * 得到Map地图文件目录
	 * @return mnt/sdcard/appRoot/Map
	 */
	public static String getMapPath() {
		String path = getAppRootPath() + config.dirMap + File.separator;
		if (!new File(path).exists()) new File(path).mkdirs();
		return path;
	}
	
	/**
	 * 得到Emoticon表情符号文件目录
	 * @return mnt/sdcard/appRoot/Emoticon
	 */
	public static String getEmoticonPath() {
		String path = getAppRootPath() + config.dirEmoticon + File.separator;
		if (!new File(path).exists()) new File(path).mkdirs();
		return path;
	}
	
	/**
	 * 得到APK文件名
	 */
	public static String getApkFilePath(String name, String url) {
		if (!TextUtils.isEmpty(name)) {
			return getApkPath() + File.separator + name;
		} else {
			return getApkPath() + File.separator + MD5.toMd5(url.getBytes());
		}
	}
	
	private static final String APK_TEMP_SUFFIX = ".download";
	
	/**
	 * 根据URL得到文件名
	 * @param url
	 * @return
	 */
	public static String getApkFilePath(String url) {
		return getApkFilePath(null, url);
	}
	
	/**
	 * 得到临时文件
	 */
	public static String getApkTempFilePath(String name, String url) {
		String path = getApkFilePath(name, url) + APK_TEMP_SUFFIX;
		if (!new File(path).exists()) {
    		new File(path).mkdirs();
		}
		return path;
	}
	
	/**
	 * 得到APK临时文件
	 */
	public static String getApkTempFile(String filePath) {
		return filePath + APK_TEMP_SUFFIX;
	}
	
	/**
	 * 是否可写
	 * @return
	 */
	public static boolean isSdCardWrittenable() {
		return new File(Environment.getExternalStorageDirectory().getAbsolutePath()).canWrite();
	}

	/**
	 * 检测SD卡是否挂载(也就是是否可用)
	 * 
	 * @return
	 */
	public static boolean isSDIsMounted() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 转byte字节组
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			out.write(ch);
		}
		byte buffer[] = out.toByteArray();
		out.close();
		return buffer;
	}
	
	/**
	 * 读取
	 * @param inStream
	 * @return
	 */
	public static String readFileInputStream(FileInputStream inStream) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
			}

			outStream.close();
			inStream.close();
			return outStream.toString();
		} catch (IOException e) {
			L.e("readInStream", e);
		}
		return null;
	}
	
	/**
     * read file
     * 
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     * 
     * @param filePath
     * @param content
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (StringUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     * 
     * @param filePath
     * @param contentList
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if (ListUtils.isEmpty(contentList)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file, the string will be written to the begin of the file
     * 
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * write file, the string list will be written to the begin of the file
     * 
     * @param filePath
     * @param contentList
     * @return
     */
    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     * 
     * @param filePath
     * @param stream
     * @return
     * @see {@link #writeFile(String, InputStream, boolean)}
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     * 
     * @param file the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     * 
     * @param file
     * @param stream
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * write file
     * 
     * @param file the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }
    
    /**
     * copy file
     * 
     * @param sourceFilePath
     * @param destFilePath
     * @return
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }

    /**
     * read file to string list, a element of list is a line
     * 
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }
    
    /**
	 * 列出root目录下所有子目录
	 * @param path
	 * @return 绝对路径
	 */
	public static List<String> listPath(String root) {
		List<String> allDir = new ArrayList<String>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				if (f.isDirectory()) {
					allDir.add(f.getAbsolutePath());
				}
			}
		}
		return allDir;
	}
	
	/**
	 * 获取目录文件个数
	 * @param f
	 * @return
	 */
	public long getFileList(File dir) {
		long count = 0;
		File[] files = dir.listFiles();
		count = files.length;
		for (File file : files) {
			if (file.isDirectory()) {
				count = count + getFileList(file);// 递归
				count--;
			}
		}
		return count;
	}
	
	/**
	 * 获取目录文件大小
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 递归调用继续统计
			}
		}
		return dirSize;
	}

    /**
     * get file name from path, not include suffix
     * 
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     * 
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutSuffix(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * get file name from path, include suffix
     * 
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     * 
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * get folder name or path name from path
     * 
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     * 
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * get suffix of file from path
     * 
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     * 
     * @param filePath
     * @return
     */
    public static String getFileSuffix(String filePath) {
        if (TextUtils.isBlank(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }
    
    /**
	 * 重命名
	 * @param oldName
	 * @param newName
	 * @return
	 */
	public static boolean reName(String oldName, String newName) {
		File f = new File(oldName);
		return f.renameTo(new File(newName));
	}

    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     * 
     * @param filePath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     *         the directories can not be created.
     *         <ul>
     *         <li>if {@link FileUtils#getFolderName(String)} return null, return false</li>
     *         <li>if target directory already exists, return true</li>
     *         <li>return {@link java.io.File#makeFolder}</li>
     *         </ul>
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (StringUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * @param filePath
     * @return
     * @see #makeDirs(String)
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }
    
    /**
     * 创建文件(实体文件，而不是创建File对象)
     * @param filePath
     * @return
     */
    public static boolean makeFile(String filePath) {
		if(TextUtils.isBlank(filePath)) 
			return false;
		
		File file = new File(filePath);
		try {
			return (file.exists() && file.isFile()) ? true : file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

    /**
     * Indicates if this file represents a file on the underlying file system.
     * 
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (TextUtils.isBlank(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     * 
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (TextUtils.isBlank(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * delete file or directory(包括里面所有的文件)
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     * 
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isBlank(path)) {
            return true;
        }

        SecurityManager checker = new SecurityManager();
        File file = new File(path);
        checker.checkDelete(file.toString());
        
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * <ul>
     * 
     * @param path
     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
     */
    public static long getFileSize(String path) {
        if (TextUtils.isBlank(path)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }
	
	/**
	 * 格式化获取文件大小
	 * @param size	字节
	 * @return
	 */
	public static String getFileSize(long size) {
		if (size <= 0)
			return "0";
		java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
		float temp = (float) size / KB_2_BYTE;
		if (temp >= KB_2_BYTE) {
			return df.format(temp / KB_2_BYTE) + "M";
		} else {
			return df.format(temp) + "K";
		}
	}
	
	/**
	 * 转换文件大小
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < KB_2_BYTE) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < MB_2_BYTE) {
			fileSizeString = df.format((double) fileS / KB_2_BYTE) + "KB";
		} else if (fileS < GB_2_BYTE) {
			fileSizeString = df.format((double) fileS / MB_2_BYTE) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / GB_2_BYTE) + "G";
		}
		return fileSizeString;
	}
    
    /**
	 * 从Assets文件夹里读取文件，返回String
	 * @param fileName  文件名
	 * @return null文件不存在
	 */
	public static String getFromAssets(Context context, String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(context
					.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
     * get content from a raw resource. This can only be used with resources whose value is the name of an asset files
     * -- that is, it can be used to open drawable, sound, and raw resources; it will fail on string and color
     * resources.
     * 
     * @param context
     * @param resId The resource identifier to open, as generated by the appt tool.
     * @return
     */
    public static String geFileFromRaw(Context context, int resId) {
        if (context == null) {
            return null;
        }

        StringBuilder s = new StringBuilder();
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
	 * 计算SD卡的剩余空间
	 * @return 返回-1，说明没有安装sd卡
	 */
	public static long getFreeDiskSpace() {
		String status = Environment.getExternalStorageState();
		long freeSpace = 0;
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				freeSpace = availableBlocks * blockSize / KB_2_BYTE;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return -1;
		}
		return (freeSpace);
	}
}
