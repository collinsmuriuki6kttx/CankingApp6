package com.canking.sdcardhelper;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestEncodingUtils {
    private static final String TAG = "[DigestEncodingUtils] ";

    private static final char[] HEXCHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * Encode the bytes with hex string (lower case)
     *
     * @param data
     * @return
     */
    public static String encodeHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (int i = 0; i < data.length; i++) {
            sb.append(HEXCHARS[(data[i] & 0xf0) >>> 4]);
            sb.append(HEXCHARS[data[i] & 0x0f]);
        }
        return sb.toString();
    }

    static MessageDigest md5 = null;

    /**
     * Compute MD5 digest with hex encoding (lower case)
     *
     * @return null may be returned if unexpected exceptions happen
     */
    public static String computeMd5HexString(byte[] data) {
        try {
            if (md5 == null) {
                md5 = MessageDigest.getInstance("MD5");
            } else {
                md5.reset();
            }
            md5.update(data);
            return encodeHexString(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            w(TAG, "Should never happen!", e);
        }
        return null;
    }

    /**
     * Compute MD5 digest with hex encoding (lower case)</p>
     * Using {@link String#getBytes(String)} with "UTF-8" to convert String to byte[]</p>
     *
     * @return null may be returned if unexpected exceptions happen
     */
    public static String computeMd5HexString(String data) {
        try {
            return computeMd5HexString(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            w(TAG, "should never happen!", e);
        }
        return null;
    }

    /**
     * Compute MD5 digest with hex encoding (lower case)
     *
     * @return null may be returned if unexpected exceptions happen
     */
    public static String computeMd5HexString(InputStream is) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            int len;
            byte buffer[] = new byte[32 * 1024]; // 32 KB
            while ((len = is.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, len);
            }
            return encodeHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            w(TAG, "Should never happen!", e);
        } catch (IOException e) {
            w(TAG, "unexpected exception happened", e);
        }

        return null;
    }

    /**
     * Compute file MD5 digest with hex encoding (lower case)
     *
     * @return null may be returned if the file not found or other unexpected
     * exceptions happen
     */
    public static String computeFileMd5HexString(String filepath) {
        File apkFile = new File(filepath);
        if (!apkFile.exists()) {
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(apkFile);
            return computeMd5HexString(fis);
        } catch (FileNotFoundException e) {
            w(TAG, "should not happen", e);
        } finally {
            close(fis);
        }
        return null;
    }

    static void w(String tag, String msg, Throwable tr) {
        System.err.println(tag + msg);
        tr.printStackTrace();
    }

    /**
     * Close a {@link Closeable} object and ignore the exception.
     *
     * @param target The target to close. Can be null.
     */
    public static void close(Closeable target) {
        try {
            if (target != null)
                target.close();
        } catch (IOException e) {
            w(TAG, "Failed to close the target", e);
        }
    }

}
