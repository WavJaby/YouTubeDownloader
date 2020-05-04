package com.youtube.downloader.model;

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

import com.youtube.downloader.model.quality.AudioQuality;
import com.youtube.downloader.model.quality.VideoQuality;

public enum Itag {

    unknown {
        @Override
        public void setId(int id) {
            this.id = id;
        }
    },

    i5(VideoQuality.low240p, AudioQuality.low50k),
    i6(VideoQuality.low270p, AudioQuality.low50k),
    i13(VideoQuality.unknown, AudioQuality.low50k),
    i17(VideoQuality.low144p, AudioQuality.low50k),
    i18(VideoQuality.low360p, AudioQuality.low50k),
    i22(VideoQuality.hd720p, AudioQuality.med160k),

    i34(VideoQuality.low360p, AudioQuality.med160k),
    i35(VideoQuality.low480p, AudioQuality.med160k),
    i36(VideoQuality.low180p, AudioQuality.unknown),
    i37(VideoQuality.hd1080p, AudioQuality.med160k),
    i38(VideoQuality.hd3072p, AudioQuality.med160k),

    i43(VideoQuality.low360p, AudioQuality.med160k),
    i44(VideoQuality.low480p, AudioQuality.med160k),
    i45(VideoQuality.hd720p, AudioQuality.med160k),
    i46(VideoQuality.hd1080p, AudioQuality.med160k),

    // 3D videos
    i82(VideoQuality.low360p, AudioQuality.med160k, true),
    i83(VideoQuality.low480p, AudioQuality.med160k, true),
    i84(VideoQuality.hd720p, AudioQuality.med160k, true),
    i85(VideoQuality.hd1080p, AudioQuality.med160k, true),
    i100(VideoQuality.low360p, AudioQuality.med160k, true),
    i101(VideoQuality.low480p, AudioQuality.med160k, true),
    i102(VideoQuality.hd720p, AudioQuality.med160k, true),

    // Apple HTTP Live Streaming
    i91(VideoQuality.low144p, AudioQuality.low50k),
    i92(VideoQuality.low240p, AudioQuality.low50k, true),
    i93(VideoQuality.low360p, AudioQuality.med160k, true),
    i94(VideoQuality.low480p, AudioQuality.med160k, true),
    i95(VideoQuality.hd720p, AudioQuality.high256k, true),
    i96(VideoQuality.hd1080p, AudioQuality.high256k),
    i132(VideoQuality.low240p, AudioQuality.low50k),
    i151(VideoQuality.low144p, AudioQuality.low50k),

    // DASH mp4 video
    i133(VideoQuality.low240p),
    i134(VideoQuality.low360p),
    i135(VideoQuality.low480p),
    i136(VideoQuality.hd720p),
    i137(VideoQuality.hd1080p),
    i138(VideoQuality.hd2160p60),
    i160(VideoQuality.low144p),
    i212(VideoQuality.low480p),
    i264(VideoQuality.hd1440P),
    i266(VideoQuality.hd2160p60),
    i298(VideoQuality.hd720p60),
    i299(VideoQuality.hd1080p60),

    // DASH mp4 audio
    i139(AudioQuality.low48k),
    i140(AudioQuality.med128k),
    i141(AudioQuality.high256k),
    i256(AudioQuality.unknown),
    i325(AudioQuality.unknown),
    i328(AudioQuality.unknown),

    // DASH webm video
    i167(VideoQuality.low360p),
    i168(VideoQuality.low480p),
    i169(VideoQuality.hd1080p),
    i170(VideoQuality.hd720p),
    i218(VideoQuality.low480p),
    i219(VideoQuality.low144p),
    i242(VideoQuality.low240p),
    i243(VideoQuality.low360p),
    i244(VideoQuality.low480p),
    i245(VideoQuality.low480p),
    i246(VideoQuality.low480p),
    i247(VideoQuality.hd720p),
    i248(VideoQuality.hd1080p),
    i271(VideoQuality.hd1440P),
    i272(VideoQuality.hd4320p),
    i278(VideoQuality.low144p),
    i302(VideoQuality.hd720p60),
    i303(VideoQuality.hd1080p60),
    i308(VideoQuality.hd1440p60),
    i313(VideoQuality.hd2160p),
    i315(VideoQuality.hd2160p60),

    // DASH webm audio
    i171(AudioQuality.med128k),
    i172(AudioQuality.high256k),

    // Dash webm audio with opus inside
    i249(AudioQuality.low50k),
    i250(AudioQuality.low70k),
    i251(AudioQuality.med160k),

    // Dash webm hdr video
    i330(VideoQuality.low144p60, true),
    i331(VideoQuality.low240p60, true),
    i332(VideoQuality.low360p60, true),
    i333(VideoQuality.low480p60, true),
    i334(VideoQuality.hd720p60, true),
    i335(VideoQuality.hd1080p60, true),
    i336(VideoQuality.hd1440p60, true),
    i337(VideoQuality.hd2160p60, true),

    // av01 video only formats
    i394(VideoQuality.low144p),
    i395(VideoQuality.low240p),
    i396(VideoQuality.low360p),
    i397(VideoQuality.low480p),
    i398(VideoQuality.hd720p),
    i399(VideoQuality.hd1080p),
    i400(VideoQuality.hd1440P),
    i401(VideoQuality.hd2160p),
    i402(VideoQuality.hd4320p60);

    protected int id;
    private VideoQuality videoQuality;
    private AudioQuality audioQuality;
    private boolean isVRor3D;
    private boolean isHDR;

    Itag() {
        this.videoQuality = VideoQuality.unknown;
        this.audioQuality = AudioQuality.unknown;
        this.isVRor3D = false;
        this.isHDR = false;
    }

    Itag(VideoQuality videoQuality) {
        this(videoQuality, AudioQuality.noAudio, false);
    }

    Itag(AudioQuality audioQuality) {
        this(VideoQuality.noVideo, audioQuality, false);
    }

    Itag(VideoQuality videoQuality, AudioQuality audioQuality) {
        this(videoQuality, audioQuality, false);
    }

    Itag(VideoQuality videoQuality, AudioQuality audioQuality, boolean isVRor3D) {
        setId(Integer.parseInt(name().substring(1)));
        this.videoQuality = videoQuality;
        this.audioQuality = audioQuality;
        this.isVRor3D = isVRor3D;
    }

    Itag(VideoQuality videoQuality, boolean isHDR) {
        setId(Integer.parseInt(name().substring(1)));
        this.videoQuality = videoQuality;
        this.isHDR = isHDR;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }

    public VideoQuality videoQuality() {
        return videoQuality;
    }

    public AudioQuality audioQuality() {
        return audioQuality;
    }

    public boolean isVideo() {
        return videoQuality != VideoQuality.noVideo;
    }

    public boolean isAudio() {
        return audioQuality != AudioQuality.noAudio;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
