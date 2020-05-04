package com.youtube;

import com.youtube.downloader.OnYoutubeDownloadListener;
import com.youtube.downloader.YoutubeException;
import com.youtube.downloader.model.Itag;
import com.youtube.downloader.model.VideoDetails;
import com.youtube.downloader.model.formats.AudioFormat;
import com.youtube.downloader.model.formats.AudioVideoFormat;
import com.youtube.downloader.model.formats.Format;
import com.youtube.downloader.model.formats.VideoFormat;
import com.youtube.downloader.YoutubeDownloader;
import com.youtube.downloader.model.YoutubeVideo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.*;

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
        String videoId = "https://www.youtube.com/watch?v=N3PI8_2eAtU";
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
        String outputVideo = outputDir + "/outVideo.mp4";

        int itag = input("輸入itag:");
        Format videoFile = video.findFormatByItag(itag);// 選擇的影片
        Format audioFile = video.findFormatByItag(140);// 選擇的音檔
        System.out.println("找到:" + videoFile.itag().videoQuality() + "." + videoFile.extension().value());

        downloadVideoAudio(video, outputDir, videoFile, audioFile);

//        File audioFile = video.downloadAsync(AudioFormats.get(1), outputDir, new OnYoutubeDownloadListener() {
//            @Override
//            public void onDownloading(int progress) {
//                System.out.println("聲音: " + progress + "%");
//            }
//
//            @Override
//            public void onFinished(File file) {
//                System.out.println("完成:" + file);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                System.out.println("Error: " + throwable);
//            }
//        });//下載音檔
//
//        File videoFile = video.downloadAsync(file, outputDir, new OnYoutubeDownloadListener() {// 下載影片
//            @Override
//            public void onDownloading(int progress) {
//                System.out.println("影片: " + progress + "%");
//            }
//
//            @Override
//            public void onFinished(File file) {
//                System.out.println("完成:" + file);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                System.out.println("Error: " + throwable);
//            }
//        });

//        audioFile = extensionToM4a(outputDir, audioFile);//改音檔副檔名

//        new convertVideo(videoFile.getPath(), audioFile.getPath(), outputVideo);
    }

    public static File[] downloadVideoAudio(YoutubeVideo video, String outputDir, Format videoFile, Format audioFile) throws IOException, YoutubeException {
        int coreCount = Runtime.getRuntime().availableProcessors();
        File[] outFile = new File[2];
        File videoOutDir = new File(outputDir + "/video");
        File audioOutDir = new File(outputDir + "/audio");
        ExecutorService executorService = Executors.newFixedThreadPool(coreCount);
        executorService.submit(video.downloadAsync(audioFile, audioOutDir, new OnYoutubeDownloadListener() {// 下載聲音
            @Override
            public void onDownloading(int progress) {
                System.out.println("聲音: " + progress + "%");
            }

            @Override
            public void onFinished(File file) {
                System.out.println("完成:" + file);
                outFile[0] = extensionToM4a(file);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable);
            }
        }));

        executorService.submit(video.downloadAsync(videoFile, videoOutDir, new OnYoutubeDownloadListener() {// 下載影片
            @Override
            public void onDownloading(int progress) {
                System.out.println("影片: " + progress + "%");
            }

            @Override
            public void onFinished(File file) {
                System.out.println("完成:" + file);
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
        return outFile;
    }

    public static File extensionToM4a(File file) {
        System.out.println(file.getPath());
        String filePath = file.getPath().replace(".mp4", ".m4a");
        File toFile = new File(filePath);
        file.renameTo(toFile);
        return toFile;
    }

    public static int input(String message) {
        System.out.println(message);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String userInput = null;
        try {
            userInput = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(userInput);
    }

    public static void printFormats(YoutubeVideo video) {
        //itags https://gist.github.com/sidneys/7095afe4da4ae58694d128b1034e01e2
        System.out.println("#########所有#########");
        //所有
        List<Format> allFormats = video.formats();
        allFormats.forEach(it -> {
            System.out.println(it.extension().value() + ":" + Itag.valueOf("i" + it.itag()).videoQuality() + " : " + it.url());
        });

        System.out.println("#########影片+聲音#########");
        //影片+聲音
        List<AudioVideoFormat> videoAudioFormats = video.videoWithAudioFormats();
        videoAudioFormats.forEach(it -> {
            System.out.println(it.extension().value() + ":" + it.videoQuality() + ":" + it.itag() + " : " + it.url());
        });

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
    }

}
