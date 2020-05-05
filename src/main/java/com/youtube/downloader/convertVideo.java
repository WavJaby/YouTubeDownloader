package com.youtube.downloader;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;

import java.io.*;

public class convertVideo {

    public static void main(String[] args) {

        String outputDir = "D:\\My project\\Java\\YouTubeDownloader\\file_out";

        new convertVideo(outputDir + "/video/【MC梦想改造家】基岩版爆改赛博朋克光污染大厦！光线追踪效果太惊人了.mp4", outputDir + "/audio/【MC梦想改造家】基岩版爆改赛博朋克光污染大厦！光线追踪效果太惊人了.mp4", outputDir);
    }

    public convertVideo(String videoFile, String audioFile, String outputFile) {
        Movie video;
        Movie audio;

        try {
            video = new MovieCreator().build(videoFile);
            audio = new MovieCreator().build(audioFile);

        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
            return;
        }

        Track audioTrack = audio.getTracks().get(0);
        video.addTrack(audioTrack);

        Container out = new DefaultMp4Builder().build(video);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(outputFile+"/out.mp4");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        BufferedWritableFileByteChannel byteBufferByteChannel = new BufferedWritableFileByteChannel(fos);
        try {
            out.writeContainer(byteBufferByteChannel);
            byteBufferByteChannel.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
//            return false;
        }
//        return true;
    }
}
