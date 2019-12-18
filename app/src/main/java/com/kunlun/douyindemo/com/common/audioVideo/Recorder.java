package com.kunlun.douyindemo.com.common.audioVideo;

import com.kunlun.douyindemo.com.common.util.ConstAV;

import java.io.IOException;

public interface Recorder<T> {



    @ConstAV.DataType
    int getDataType();

    void setOnRecordFinishListener(OnRecordFinishListener listener);

    void configure(T configuration);

    void start();

    void stop();

    void prepareCodec() throws IOException;

    void shutdown();

    boolean isStarted();
}

