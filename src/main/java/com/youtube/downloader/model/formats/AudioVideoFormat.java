package com.youtube.downloader.model.formats;

/*-
 * #
 * Java youtube video and audio downloader
 *
 * Copyright (C) 2020 Igor Kiulian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #
 */


import com.alibaba.fastjson.JSONObject;
import com.youtube.downloader.model.quality.AudioQuality;
import com.youtube.downloader.model.quality.VideoQuality;

public class AudioVideoFormat extends Format {

    private final Integer averageBitrate;
    private final Integer audioSampleRate;
    private final AudioQuality audioQuality;
    private final String qualityLabel;
    private final Integer width;
    private final Integer height;
    private final VideoQuality videoQuality;

    public AudioVideoFormat(JSONObject json) {
        super(json);
        audioSampleRate = json.getInteger("audioSampleRate");
        averageBitrate = json.getInteger("averageBitrate");
        qualityLabel = json.getString("qualityLabel");
        width = json.getInteger("width");
        height = json.getInteger("height");

        VideoQuality videoQuality = null;
        if (json.containsKey("quality")) {
            try {
                videoQuality = VideoQuality.valueOf(json.getString("quality"));
            } catch (IllegalArgumentException ignore) {
            }
        }
        this.videoQuality = videoQuality;

        AudioQuality audioQuality = null;
        if (json.containsKey("audioQuality")) {
            String[] split = json.getString("audioQuality").split("_");
            String quality = split[split.length - 1].toLowerCase();
            try {
                audioQuality = AudioQuality.valueOf(quality);
            } catch (IllegalArgumentException ignore) {
            }
        }
        this.audioQuality = audioQuality;
    }

    @Override
    public String type() {
        return AUDIO_VIDEO;
    }

    public VideoQuality videoQuality() {
        return videoQuality != null ? videoQuality : itag.videoQuality();
    }

    public String qualityLabel() {
        return qualityLabel;
    }

    public Integer width() {
        return width;
    }

    public Integer height() {
        return height;
    }

    public Integer averageBitrate() {
        return averageBitrate;
    }

    public AudioQuality audioQuality() {
        return audioQuality != null ? audioQuality : itag.audioQuality();
    }

    public Integer audioSampleRate() {
        return audioSampleRate;
    }

}
