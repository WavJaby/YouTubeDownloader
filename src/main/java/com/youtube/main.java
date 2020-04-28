package com.youtube;

import com.github.kiulian.downloader.OnYoutubeDownloadListener;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.model.Itag;
import com.github.kiulian.downloader.model.VideoDetails;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioFormat;
import com.github.kiulian.downloader.model.formats.AudioVideoFormat;
import com.github.kiulian.downloader.model.formats.Format;
import com.github.kiulian.downloader.model.formats.VideoFormat;
import com.github.kiulian.downloader.model.quality.AudioQuality;
import com.github.kiulian.downloader.model.quality.VideoQuality;
import com.github.kiulian.downloader.parser.DefaultParser;
import com.github.kiulian.downloader.parser.Parser;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class main {

    public static void main(String[] args) throws IOException, YoutubeException {
        // init downloader
        YoutubeDownloader downloader = new YoutubeDownloader();

        // you can easly implement or extend default parsing logic
//        YoutubeDownloader downloader = new YoutubeDownloader(new Parser());
        // or just extend functionality via existing API
        // cipher features
        downloader.addCipherFunctionPattern(2, "\\b([a-zA-Z0-9$]{2})\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)");
        // downloader.addCipherFunctionEquivalent("some regex for js function", new CustomJavaFunction());
        // extractor features
        downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        downloader.setParserRetryOnFailure(1);

        // parsing data
        String videoId = "jps1vRhJkh0"; // for url https://www.youtube.com/watch?v=abc12345
        YoutubeVideo video = downloader.getVideo(videoId);


        // 影片標題
        VideoDetails details = video.details();
        System.out.println("標題" + details.title());
        // 觀看數
        System.out.println("觀看數" + details.viewCount());
        details.thumbnails().forEach(image -> System.out.println("Thumbnail: " + image));

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
            System.out.println(it.extension().value() + ":" + it.audioQuality() + ":" + it.itag() + " : " + it.url());
        });

        //輸出位置
        File outputDir = new File("file_out");
        String outputVideo = outputDir + "/outVideo.mp4";

        int index = input("輸入itag:");
        Format file = video.findFormatByItag(index);// 選擇的影片
        System.out.println("找到:" + file.itag().videoQuality() + file.extension().value());

        File videoFile = video.download(file, outputDir);// 下載影片
        File audioFile = video.download(AudioFormats.get(0), outputDir);//下載音檔

        audioFile = extensionToMp3(outputDir, audioFile);//改音檔副檔名

        new convertVideo(videoFile.getPath(), audioFile.getPath(), outputVideo);
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

    public static File extensionToMp3(File outPath, File file) {
        String name = file.getName().replace("(1).mp4", "");
        File toFile = new File(outPath + "/" + name + ".m4a");
        file.renameTo(toFile);
        return toFile;
    }
}
