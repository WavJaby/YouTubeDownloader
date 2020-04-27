package com.youtube;

import com.github.kiulian.downloader.OnYoutubeDownloadListener;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
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

import java.io.File;
import java.io.IOException;
import java.util.List;


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
        String videoId = "8z3KCSRC6so"; // for url https://www.youtube.com/watch?v=abc12345
        YoutubeVideo video = downloader.getVideo(videoId);


        // 影片標題
        VideoDetails details = video.details();
        System.out.println(details.title());
        // 觀看數
        System.out.println(details.viewCount());
        details.thumbnails().forEach(image -> System.out.println("Thumbnail: " + image));

        // get videos with audio
//        List<AudioFormat> AudioFormats = video.findAudioWithQuality(AudioQuality.low);
//        AudioFormats.forEach(it -> {
//            System.out.println(it.audioQuality() + " : " + it.url());
//        });


        List<Format> allFormats = video.formats();
        allFormats.forEach(it -> {
            System.out.println(it.extension().value() + ":" + it.itag() + " : " + it.url());
        });

        System.out.println("###################");

        //filtering only video formats
        List<AudioVideoFormat> videoFormats = video.videoWithAudioFormats();
        videoFormats.forEach(it -> {
            System.out.println(it.extension().value() + ":" + it.itag() + " : " + it.url());
        });

        // itags can be found here - https://gist.github.com/sidneys/7095afe4da4ae58694d128b1034e01e2
//        Format formatByItag = video.findFormatByItag(151);
//        if (formatByItag != null) {
//            System.out.println(formatByItag.url());
//        }

//        File outputDir = new File("my_videos");

        // sync downloading
//        video.download(videoWithAudioFormats.get(0), outputDir);

        // async downloading with callback
//        video.downloadAsync(AudioFormats.get(0), outputDir, new OnYoutubeDownloadListener() {
//            @Override
//            public void onDownloading(int progress) {
//                System.out.printf("Downloaded %d%%\n", progress);
//            }
//
//            @Override
//            public void onFinished(File file) {
//                System.out.println("Finished file: " + file);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                System.out.println("Error: " + throwable.getLocalizedMessage());
//            }
//        });

        // async downloading with future
//        Future<File> future = video.downloadAsync(format, outputDir);
//        File file = future.get(5, TimeUnit.SECONDS);
    }
}
