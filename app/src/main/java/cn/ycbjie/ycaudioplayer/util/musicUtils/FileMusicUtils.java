package cn.ycbjie.ycaudioplayer.util.musicUtils;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ycbjie.ycaudioplayer.R;
import cn.ycbjie.ycaudioplayer.ui.music.local.model.LocalMusic;

/**
 * Created by yc on 2018/1/23.
 */

public class FileMusicUtils {

    private static final String MP3 = ".mp3";
    private static final String LRC = ".lrc";

    private static String getAppDir() {
        return Environment.getExternalStorageDirectory() + "/YCAudioPlayer";
    }

    public static String getMusicDir() {
        String dir = getAppDir() + "/Music/";
        return mkdirs(dir);
    }

    public static String getLrcDir() {
        String dir = getAppDir() + "/Lyric/";
        return mkdirs(dir);
    }

    public static String getAlbumDir() {
        String dir = getAppDir() + "/Album/";
        return mkdirs(dir);
    }

    public static String getLogDir() {
        String dir = getAppDir() + "/Log/";
        return mkdirs(dir);
    }

    public static String getRelativeMusicDir() {
        String dir = "YCAudioPlayer/Music/";
        return mkdirs(dir);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static String mkdirs(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    public static String getArtistAndAlbum(String artist, String album) {
        if (TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            return "";
        } else if (!TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            return artist;
        } else if (TextUtils.isEmpty(artist) && !TextUtils.isEmpty(album)) {
            return album;
        } else {
            return artist + " - " + album;
        }
    }

    static Uri getMediaStoreAlbumCoverUri(long albumId) {
        Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
        return ContentUris.withAppendedId(artworkUri, albumId);
    }


    /**
     * 获取歌词路径<br>
     * 先从已下载文件夹中查找，如果不存在，则从歌曲文件所在文件夹查找。
     * @return 如果存在返回路径，否则返回null
     */
    public static String getLrcFilePath(LocalMusic music) {
        if (music == null) {
            return null;
        }
        String lrcFilePath = getLrcDir() + getLrcFileName(music.getArtist(), music.getTitle());
        if (!exists(lrcFilePath)) {
            lrcFilePath = music.getPath().replace(MP3, LRC);
            if (!exists(lrcFilePath)) {
                lrcFilePath = null;
            }
        }
        return lrcFilePath;
    }

    private static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static String getMp3FileName(String artist, String title) {
        return getFileName(artist, title) + MP3;
    }

    public static String getLrcFileName(String artist, String title) {
        return getFileName(artist, title) + LRC;
    }

    public static String getAlbumFileName(String artist, String title) {
        return getFileName(artist, title);
    }

    public static String getFileName(String artist, String title) {
        artist = stringFilter(artist);
        title = stringFilter(title);
        if (TextUtils.isEmpty(artist)) {
            artist = Utils.getApp().getString(R.string.unknown);
        }
        if (TextUtils.isEmpty(title)) {
            title = Utils.getApp().getString(R.string.unknown);
        }
        return artist + " - " + title;
    }

    /**
     * 过滤特殊字符(\/:*?"<>|)
     */
    private static String stringFilter(String str) {
        if (str == null) {
            return null;
        }
        String regEx = "[\\/:*?\"<>|]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    public static void saveLrcFile(String path, String content) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(content);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
