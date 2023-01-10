package com.example.musicplay.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.musicplay.ExoMediaAudioProcessor
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.audio.*

class ExoPlayViewModel() : ViewModel() {

    companion object {
        const val TAG = "ExoPlayViewModel"
    }

    lateinit var exoPlayer: ExoPlayer
    var chooseEqualizer: IntArray? = null
        set(value) {
            mExoMediaAudioProcessor.enableEffect = true
            mExoMediaAudioProcessor.haeEqualizerStreamEnable = true
            mExoMediaAudioProcessor.haeEqualizerStream?.setEqParams(value)
            field = value
        }
    var chooseSceneStream: Int? = null //MutableStateFlow(AudioParameters.ENVIRONMENT_TYPE_UNDERWATER)
        set(value) {
            field = value?.apply {
                mExoMediaAudioProcessor.enableEffect = true
                mExoMediaAudioProcessor.hAESceneStreamEnable = true
                mExoMediaAudioProcessor.hAESceneStream?.setEnvironmentType(this)
            }
        }
    var chooseSoundStream: Int? = null
        set(value) {
            field = value?.apply {
                mExoMediaAudioProcessor.enableEffect = true
                mExoMediaAudioProcessor.hAESoundFieldStreamEnable = true
                mExoMediaAudioProcessor.hAESoundFieldStream?.setSoundType(this)
            }
        }

    var mExoMediaAudioProcessor = ExoMediaAudioProcessor()

    var enableIndex: Int? = null
        set(value) {
            field = value?.apply {
                when (this) {
                    1 -> {
                        mExoMediaAudioProcessor.haeEqualizerStreamEnable = false
                    }
                    2 -> {
                        mExoMediaAudioProcessor.hAESceneStreamEnable = false
                    }
                    3 -> {
                        mExoMediaAudioProcessor.hAESoundFieldStreamEnable = false
                    }
                }
            }
        }

    var enable: Boolean = false
        set(value) {
            if (!value) {
                mExoMediaAudioProcessor.hAESceneStreamEnable = false
                mExoMediaAudioProcessor.haeEqualizerStreamEnable = false
                mExoMediaAudioProcessor.hAESoundFieldStreamEnable = false
            }
            field = value
        }

    //    private var path = Environment.getExternalStorageDirectory().absolutePath + File.separator + "ad7d1d4edff2167163b7303f0fd9f369.wav"//Environment.getExternalStorageDirectory().absolutePath + File.separator + "千山万水.mp3"
//    private var path = Environment.getExternalStorageDirectory().absolutePath + File.separator + "千山万水.mp3"
    private var path = "file:///android_asset/千山万水.mp3"
//    private var path = "http://192.168.88.39:8080/wavTest2.wav"
//private var path = Environment.getExternalStorageDirectory().absolutePath + File.separator + "stream.pcm"

    fun playMusic(context: Context) {

        if (!this::exoPlayer.isInitialized) {
            exoPlayer = ExoPlayer.Builder(context).setRenderersFactory(object :
                DefaultRenderersFactory(context) {
                override fun buildAudioSink(
                    context: Context,
                    enableFloatOutput: Boolean,
                    enableAudioTrackPlaybackParams: Boolean,
                    enableOffload: Boolean
                ): AudioSink? {
                    return DefaultAudioSink.Builder()
                        .setAudioCapabilities(AudioCapabilities.getCapabilities(context))
                        .setEnableFloatOutput(enableFloatOutput)
                        .setEnableAudioTrackPlaybackParams(enableAudioTrackPlaybackParams)
//                        .setAudioTrackBufferSizeProvider(DefaultAudioTrackBufferSizeProvider.Builder().build())
                        /**
                         * 设置SoxAudioProcessor
                         * 处理音频数据
                         */
                        .setAudioProcessors(arrayOf(mExoMediaAudioProcessor.apply {
//                            viewModelScope.launch {
//                                chooseEqualizer.collect {
//                                    enableEffect = true
//                                    haeEqualizerStreamEnable = true
//                                    haeEqualizerStream?.setEqParams(it)
//                                }
//                            }
//                            viewModelScope.launch {
//                                chooseSceneStream.collect {
//                                    enableEffect = true
//                                    hAESceneStreamEnable = true
//                                    hAESceneStream?.setEnvironmentType(it)
//                                }
//                            }
//                            viewModelScope.launch {
//                                enable.collect {
//                                    enableEffect = 1 == it
//                                    if(!enableEffect) {
//                                        hAESceneStreamEnable = false
//                                        haeEqualizerStreamEnable = false
//                                    }
//                                }
//                            }
//                            viewModelScope.launch {
//                                enableEqualizer.collect {
//                                    if(it) {
//                                        enableEffect = true
//                                    }
//                                    haeEqualizerStreamEnable = it
//                                }
//                            }
//
//                            viewModelScope.launch {
//                                enableEqualizer.collect {
//                                    if(it) {
//                                        enableEffect = true
//                                    }
//                                    hAESceneStreamEnable = it
//                                }
//                            }
                        }))
                        .setOffloadMode(
                            if (enableOffload) DefaultAudioSink.OFFLOAD_MODE_ENABLED_GAPLESS_REQUIRED else DefaultAudioSink.OFFLOAD_MODE_DISABLED
                        )
                        .build()
                }
            }).build()
        }
        Log.i(TAG, "exoPlaySImple: $path")
        exoPlayer.setMediaItem(MediaItem.fromUri(path))

        //4.当Player处于STATE_READY状态时，进行播放
        exoPlayer.playWhenReady = true

        //5. 调用prepare开始加载准备数据，该方法时异步方法，不会阻塞ui线程
        exoPlayer.prepare()
        exoPlayer.play() //  此时处于 STATE_BUFFERING = 2;
//        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
//            override fun onDestroy(owner: LifecycleOwner) {
////                player.pause()
//                exoPlayer.stop()
//                exoPlayer.release()
//            }
//        })
    }

    override fun onCleared() {
        super.onCleared()
        if (this::exoPlayer.isInitialized) {
            exoPlayer.stop()
            exoPlayer.release()
        }
    }


}