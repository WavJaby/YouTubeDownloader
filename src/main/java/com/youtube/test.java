package com.youtube;


import com.youtube.downloader.model.formats.AudioFormat;
import com.youtube.downloader.OnYoutubeDownloadListener;
import com.youtube.downloader.model.YoutubeVideo;
import com.youtube.downloader.model.formats.Format;
import com.youtube.downloader.model.formats.VideoFormat;
import com.youtube.downloader.YoutubeDownloader;
import com.youtube.downloader.YoutubeException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

public class test {
    public static void main(String[] args) throws Exception {
        YoutubeDownloader downloader = new YoutubeDownloader();
        downloader.addCipherFunctionPattern(2, "\\b([a-zA-Z0-9$]{2})\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)");
        downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        downloader.setParserRetryOnFailure(1);

        String videoId = "https://www.youtube.com/watch?v=N3PI8_2eAtU";
        YoutubeVideo video = downloader.getVideo(videoId);

        System.out.println("#########影片#########");
        //影片
        List<VideoFormat> videoFormats = video.videoFormats();
        videoFormats.forEach(it -> {
            System.out.println(it.extension().value() + ":" + it.videoQuality() + ":" + it.itag() + " : " + it.url());
        });

        System.out.println("#########聲音#########");
        //聲音
        List<AudioFormat> AudioFormats = video.audioFormats();
        AudioFormats.forEach(it -> {
            System.out.println(it.extension().value() + ":" + it.averageBitrate() / 1000 + "k:" + it.itag() + " : " + it.url());
        });

        File outputDir = new File("file_out");

        Format videoFile = video.findFormatByItag(137);// 選擇的影片
        Format audioFile = video.findFormatByItag(140);// 選擇的聲音
        System.out.println("找到:" + videoFile.itag().videoQuality() + "." + videoFile.extension().value());

        int coreCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(coreCount);
        executorService.submit(video.downloadAsync(audioFile, outputDir, new OnYoutubeDownloadListener() {// 下載聲音
            @Override
            public void onDownloading(int progress) {
                System.out.println("聲音: " + progress + "%");
            }

            @Override
            public void onFinished(File file) {
                System.out.println("完成:" + file);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable);
            }
        }));

        executorService.submit(video.downloadAsync(videoFile, outputDir, new OnYoutubeDownloadListener() {// 下載影片
            @Override
            public void onDownloading(int progress) {
                System.out.println("影片: " + progress + "%");
            }

            @Override
            public void onFinished(File file) {
                System.out.println("完成:" + file);
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

    }
}
