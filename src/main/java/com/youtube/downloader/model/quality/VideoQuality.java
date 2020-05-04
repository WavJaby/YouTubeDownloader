package com.youtube.downloader.model.quality;

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

public enum VideoQuality {
    unknown,
    hd4320p,
    hd3072p,
    hd2880p,
    hd2160p,
    hd1440P,
    hd1080p,
    hd720p,
    low480p,
    low360p,
    low270p,//weird
    low240p,
    low180p,//weird
    low144p,

    hd4320p60,//8k
    hd2160p60,//4k
    hd1440p60,//2k
    hd1080p60,
    hd720p60,
    low480p60,
    low360p60,
    low240p60,
    low144p60,
    noVideo

}
