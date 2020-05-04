package com.youtube;

import com.youtube.downloader.OnYoutubeDownloadListener;
import com.youtube.downloader.YoutubeException;
import com.youtube.downloader.model.VideoDetails;
import com.youtube.downloader.model.formats.AudioFormat;
import com.youtube.downloader.model.formats.VideoFormat;
import com.youtube.downloader.YoutubeDownloader;
import com.youtube.downloader.model.YoutubeVideo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class test {
    public static void main(String[] args) throws IOException, YoutubeException {
        // init downloader
        YoutubeDownloader downloader = new YoutubeDownloader();
        // you can easly implement or extend default parsing logic
        //YoutubeDownloader downloader = new YoutubeDownloader(new Parser());
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
        VideoDetails details = video.details();

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


        File outputDir = new File("file_out");

        video.downloadAsync(AudioFormats.get(0), outputDir, new OnYoutubeDownloadListener() {
            @Override
            public void onDownloading(int progress) {
                System.out.printf("Downloaded %d%%\n", progress);
            }

            @Override
            public void onFinished(File file) {
                System.out.println("Finished file :" + file);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable);
            }
        });


//        Downloader videoDownloader = new Downloader();
//        videoDownloader.setUrl(videoFormats.get(3).url());
//        videoDownloader.setDestinationFolder(outputDir);
//        videoDownloader.setFilename(details.title() + ".mp4");
//        videoDownloader.setCallback(new Downloader.DownloaderCallback() {
//
//            @Override
//            public void onComplete() {
//                System.out.println("Finished");
//            }
//
//            @Override
//            public void onFailed(String message) {
//                System.out.println("Error: " + message);
//            }
//
//            @Override
//            public void onProgress(int progress) {
//                System.out.printf("Downloaded %d%%\n", progress);
//            }
//        });
//
//        videoDownloader.download();
    }
}
