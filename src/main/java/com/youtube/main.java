package com.youtube;

import com.sun.media.jfxmedia.logging.Logger;
import com.youtube.downloader.OnYoutubeDownloadListener;
import com.youtube.downloader.YoutubeDownloader;
import com.youtube.downloader.YoutubeException;
import com.youtube.downloader.convertVideo;
import com.youtube.downloader.model.Extension;
import com.youtube.downloader.model.Itag;
import com.youtube.downloader.model.VideoDetails;
import com.youtube.downloader.model.YoutubeVideo;
import com.youtube.downloader.model.formats.AudioFormat;
import com.youtube.downloader.model.formats.AudioVideoFormat;
import com.youtube.downloader.model.formats.Format;
import com.youtube.downloader.model.formats.VideoFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class main {

    public static void main(String[] args) throws IOException, YoutubeException {
        // 下載器
        YoutubeDownloader downloader = new YoutubeDownloader();
        // cipher features
        downloader.addCipherFunctionPattern(2, "\\b([a-zA-Z0-9$]{2})\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)");
        // downloader.addCipherFunctionEquivalent("some regex for js function", new CustomJavaFunction());
        // extractor features
        downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        downloader.setParserRetryOnFailure(1);

        // 影片網址
//        String videoId = "https://www.youtube.com/watch?v=7G0PYEFdB6Y";
        String videoId = input("video url");
        System.out.println("取得影片...");

        // 取得影片
        YoutubeVideo video = downloader.getVideo(videoId);

        // 影片標題
        VideoDetails details = video.details();
        System.out.println("標題" + details.title());
        // 觀看數
        System.out.println("觀看數" + details.viewCount());
        details.thumbnails().forEach(image -> System.out.println("Thumbnail: " + image));

        printFormats(video);

        //輸出位置
        String outputDir = "file_out";

        int itag = Integer.parseInt(input("輸入itag:"));
        Format videoFile = null;
        Format audioFile = null;
        if (Itag.valueOf("i" + itag).isVideo()) {
            videoFile = video.findFormatByItag(itag);// 選擇的影片
            audioFile = video.findFormatByItag(140);// 選擇的音檔
            System.out.println("找到:" + videoFile.itag().videoQuality() + "." + videoFile.extension().value());
        } else if (Itag.valueOf("i" + itag).isAudio()) {
            audioFile = video.findFormatByItag(itag);// 選擇的音檔
            System.out.println("找到:" + audioFile.itag().videoQuality() + "." + audioFile.extension().value());
        }
        downloadVideoAudio(video, outputDir, videoFile, audioFile);
    }

    public static void downloadVideoAudio(YoutubeVideo ytVideo, String outputDir, Format videoFile, Format audioFile) throws IOException, YoutubeException {
        int coreCount = Runtime.getRuntime().availableProcessors();
        File[] outFile = new File[2];
        File videoOutDir = new File(outputDir + "/video");
        File audioOutDir = new File(outputDir + "/audio");
        ExecutorService executorService = Executors.newFixedThreadPool(coreCount);
        executorService.submit(ytVideo.downloadAsync(audioFile, audioOutDir, new OnYoutubeDownloadListener() {// 下載聲音
            @Override
            public void onDownloading(int progress) {
                System.out.println("聲音: " + progress + "%");
            }

            @Override
            public void onFinished(File file) {
                System.out.println("音檔下載完成");
                outFile[0] = file;
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable);
            }
        }));

        if (videoFile != null) //有要下載影片檔
            executorService.submit(ytVideo.downloadAsync(videoFile, videoOutDir, new OnYoutubeDownloadListener() {// 下載影片
                long now;
                long startTime = System.currentTimeMillis() / 1000;

                @Override
                public void onDownloading(int progress) {
                    now = System.currentTimeMillis() / 1000;
                    int timeUse = (int) (now - startTime);
                    System.out.print("影片下載剩餘" + (((timeUse * 100) / progress) - timeUse) + "秒,");
                    System.out.println(progress + "%");
                }

                @Override
                public void onFinished(File file) {
                    System.out.println("影片下載完成: " + file);
                    outFile[1] = file;
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("Error: " + throwable);
                }
            }));


        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (videoFile != null) { //有要下載影片檔
            //video = 1, audio = 0
            File endVideoFile = new convertVideo(outFile[1], outFile[0], outputDir);
            System.out.println(endVideoFile.getName() + "轉換完成");
            outFile[0].delete();
            outFile[1].delete();
        } else {
            File endVideoFile = new convertVideo(outFile[0], outputDir);
            System.out.println(endVideoFile.getName() + "轉換完成");
        }
    }

    public static String input(String message) {
        System.out.println(message);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String userInput = null;
        try {
            userInput = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInput;
    }

    public static void printFormats(YoutubeVideo video) {
        //itags https://gist.github.com/sidneys/7095afe4da4ae58694d128b1034e01e2
//        System.out.println("#########所有#########");
//        //所有
//        List<Format> allFormats = video.formats();
//        allFormats.forEach(it -> {
//            System.out.println(it.extension().value() + ":" + Itag.valueOf("i" + it.itag()).videoQuality() + " : " + it.url());
//        });

        System.out.println("#########影片+聲音#########");
        //影片+聲音
//        List<AudioVideoFormat> videoAudioFormats = video.videoWithAudioFormats();
//        videoAudioFormats.forEach(it -> {
//            System.out.println(it.extension().value() + ":\t" + it.videoQuality() + ":\t" + it.audioQuality() + ":\t" +
//                     it.averageBitrate() / 1000 + ":\t" + it.itag() + ":\t" + it.url());
//        });

        System.out.println("#########聲音#########");
        //聲音
        List<AudioFormat> AudioFormats = video.audioFormats();
        AudioFormats.forEach(it -> {
            System.out.println(it.extension().value() + ":\t" + it.audioQuality() + ":\t" +
                    it.averageBitrate() / 1000 + "k:\t" + it.itag() + ":\t" + it.url());
        });

        System.out.println("#########影片#########");
        //影片
        List<VideoFormat> videoFormats = video.findVideoWithExtension(Extension.MP4);
        videoFormats.forEach(it -> {
            System.out.println(it.extension().value() + ":\t" + it.videoQuality() + ":\t" +
                    it.fps() + ":\t" + it.itag() + ":\t" + it.url());
        });
    }
}
