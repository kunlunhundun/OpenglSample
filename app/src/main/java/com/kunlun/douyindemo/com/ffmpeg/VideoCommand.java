package com.kunlun.douyindemo.com.ffmpeg;

import android.media.MediaExtractor;
import android.media.MediaFormat;

import com.kunlun.common.LogUtil;
import com.kunlun.douyindemo.com.common.util.FileUtils;
import com.kunlun.douyindemo.com.common.util.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoCommand {

    public ArrayList<String> mContent;

    public VideoCommand() {
        mContent = new ArrayList<>();
    }

    public VideoCommand append(String cmd) {
        mContent.add(cmd);
        return this;
    }

    public VideoCommand append(long cmd) {
        return append(String.valueOf(cmd));
    }

    public VideoCommand append(int cmd) {
        return append(String.valueOf(cmd));
    }

    public VideoCommand append(float cmd) {
        return append(String.valueOf(cmd));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : mContent) {
            stringBuilder.append(str).append(" ");
        }
        return stringBuilder.toString();
    }

    public String[] toArray() {
        String[] array = new String[mContent.size()];
        mContent.toArray(array);
        LogUtil.d(toString());
        return array;
    }

    /**
     * 无损合并多个视频
     * <p>
     * 注意：此方法要求视频格式非常严格，需要合并的视频必须分辨率相同，帧率和码率也得相同
     */
    public static VideoCommand mergeVideo(List<String> videos, String output) {
        String appDir = StorageUtil.getExternalStoragePath() + File.separator;
        String fileName = "ffmpeg_concat.txt";
        FileUtils.writeTxtToFile(videos, appDir, fileName);
        VideoCommand cmd = new VideoCommand();
        cmd.append("ffmpeg").append("-y").append("-f").append("concat").append("-safe")
                .append("0").append("-i").append(appDir + fileName)
                .append("-c").append("copy").append("-threads").append("5").append(output);
        return cmd;
    }


    public static VideoCommand mergeVideoAudio(String inputVideo, String inputMusic, String output) {
        VideoCommand cmd = new VideoCommand();
//        cmd.append("ffmpeg").append("-i").append(inputVideo).append("-i").append(inputMusic).
//                append("-c").append("copy").append("-threads").append("5").append(output);
//        "ffmpeg -i video.mp4 -i audio.aac -map 0:0 -map 1:0 -vcodec copy -acodec copy newvideo.mp4";
//        cmd.append("ffmpeg").append("-i").append(inputVideo).append("-i").append(inputMusic).append("-map").append("0:0")
//                .append("-map").append("1:0").append("-vcodec").append("copy").append("-acodec").append("copy").append(output);
        cmd.append("ffmpeg").append("-y").append("-i").append(inputMusic);
        float duration = getDuration(inputMusic) / 1000000f;
        cmd.append("-ss").append("0").append("-t").append(duration).append("-i").
                append(inputVideo).
                append("-acodec").
                append("copy").
                append("-vcodec").
                append("copy");
        cmd.append(output);
        return cmd;
    }

    public static long getDuration(String url) {
        try {
            MediaExtractor mediaExtractor = new MediaExtractor();
            mediaExtractor.setDataSource(url);
            int videoExt = selectVideoTrack(mediaExtractor);
            if (videoExt == -1) {
                videoExt = selectAudioTrack(mediaExtractor);
                if (videoExt == -1) {
                    return 0;
                }
            }
            MediaFormat mediaFormat = mediaExtractor.getTrackFormat(videoExt);
            long res = mediaFormat.containsKey(MediaFormat.KEY_DURATION) ? mediaFormat.getLong(MediaFormat.KEY_DURATION) : 0;//时长
            mediaExtractor.release();
            return res;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 查找视频轨道
     *
     * @param extractor
     * @return
     */
    public static int selectVideoTrack(MediaExtractor extractor) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video/")) {
                LogUtil.d("Extractor selected track " + i + " (" + mime + "): " + format);
                return i;
            }
        }
        return -1;
    }

    /**
     * 查找音频轨道
     *
     * @param extractor
     * @return
     */
    public static int selectAudioTrack(MediaExtractor extractor) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
                LogUtil.d("Extractor selected track " + i + " (" + mime + "): " + format);
                return i;
            }
        }
        return -1;
    }

    public static int calculateAudioSamples(int seconds, int sampleRate, int samplePerFrame) {
        return (int) (Math.ceil(seconds * 1.0 * sampleRate / samplePerFrame)) * 2;
    }


}
