package com.youtube.downloader;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;

import java.io.*;
import java.nio.file.Files;

public class convertVideo extends File {


    public convertVideo(File videoFile, File audioFile, String outputDir) {
        super(outputDir + "/" + videoFile.getName());
        Movie video;
        Movie audio;

        try {
            video = new MovieCreator().build(videoFile.getPath());
            audio = new MovieCreator().build(audioFile.getPath());

        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
            return;
        }

        Track audioTrack = audio.getTracks().get(0);
        video.addTrack(audioTrack);

        Container out = new DefaultMp4Builder().build(video);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(outputDir + "/" + videoFile.getName());
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
        }
//        return new File(outputDir + "/" + videoFile.getName() + ".mp4");
    }

    public convertVideo(File audioFile, String outputDir) {
        super(outputDir + "/" + audioFile.getName().replace(".mp4", ".mp3"));

        String filePath = outputDir + "/" + audioFile.getName().replace(".mp4", ".mp3");
        File toFile = new File(filePath);
        audioFile.renameTo(toFile);
    }
}
