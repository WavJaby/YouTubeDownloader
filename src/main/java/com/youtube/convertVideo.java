package com.youtube;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Mp4TrackImpl;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AACTrackImpl;
import com.googlecode.mp4parser.authoring.tracks.h264.H264TrackImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class convertVideo {
    public static void main(String[] args) {
        val mergeExample = arrayListOf<String>(video.path, audio2.path)


        //This will merge audio and video files together in a single video file.
        mergeExample.merge()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
        if (TextUtils.isEmpty(it)) {
            showToast(getString(R.string.message_error))
        } else {
            Log.i(TAG, "Output Path: $it")
            showToast(getString(R.string.message_appended) + " " + it)
        }
                        },
        onError = {
                it.printStackTrace()
        },
                onComplete = {}
                )

    }
}
