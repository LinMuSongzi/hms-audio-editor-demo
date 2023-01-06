package com.huawei.hms.audioeditor.demo.viewmodel

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.audio.AudioCapabilities
import com.google.android.exoplayer2.audio.AudioSink
import com.google.android.exoplayer2.audio.DefaultAudioSink
import com.huawei.hms.audioeditor.sdk.AudioParameters
import com.huawei.hms.audioeditor.sdk.HAEEqualizerStream
import com.huawei.hms.audioeditor.sdk.HAEErrorCode
import com.huawei.hms.network.embedded.aa
import java.io.File
import java.nio.ByteBuffer
import java.util.Arrays

class ExoPlayViewModel : ViewModel() {

    companion object {
        const val TAG = "ExoPlayViewModel"
    }

    lateinit var exoPlayer: ExoPlayer

//    private var path = Environment.getExternalStorageDirectory().absolutePath + File.separator + "ad7d1d4edff2167163b7303f0fd9f369.wav"//Environment.getExternalStorageDirectory().absolutePath + File.separator + "千山万水.mp3"
    private var path = Environment.getExternalStorageDirectory().absolutePath + File.separator + "千山万水.mp3"
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
//                            ,se
                        .setEnableAudioTrackPlaybackParams(enableAudioTrackPlaybackParams)
                        /**
                         * 设置SoxAudioProcessor
                         * 处理音频数据
                         */
                        .setAudioProcessors(arrayOf(AudioProcessor()))
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


    class AudioProcessor : com.google.android.exoplayer2.audio.AudioProcessor {
        private var outputByteBuffer: ByteBuffer = com.google.android.exoplayer2.audio.AudioProcessor.EMPTY_BUFFER
        var haeEqualizerStream: HAEEqualizerStream? = null
        private var inputEnded = false;

        private var arrayEq = arrayOf(
            AudioParameters.EQUALIZER_POP_VALUE,
            AudioParameters.EQUALIZER_CLASSICAL_VALUE,
            AudioParameters.EQUALIZER_JAZZ_VALUE,
            AudioParameters.EQUALIZER_ROCK_VALUE,
            AudioParameters.EQUALIZER_RB_VALUE,
            AudioParameters.EQUALIZER_BALLADS_VALUE
        )
        private var eqLenght = arrayEq.size

        override fun configure(inputAudioFormat: com.google.android.exoplayer2.audio.AudioProcessor.AudioFormat): com.google.android.exoplayer2.audio.AudioProcessor.AudioFormat {

            if (haeEqualizerStream == null) {
                val haeEqualizerStream = HAEEqualizerStream()
//                haeEqualizerStream.
                haeEqualizerStream.setEqParams(AudioParameters.EQUALIZER_CLASSICAL_VALUE)
                val re = haeEqualizerStream.setAudioFormat(16, inputAudioFormat.channelCount, inputAudioFormat.sampleRate)
                if (re == HAEErrorCode.SUCCESS) {
                    this.haeEqualizerStream = haeEqualizerStream;
                    Log.i(
                        TAG,
                        "configure: success bit = 16 , channelCount = ${inputAudioFormat.channelCount} , sampleRate = ${inputAudioFormat.sampleRate}"
                    )
                } else {
                    Log.i(TAG, "configure: error = $re")
                }
            }
            return inputAudioFormat;
        }
        override fun isActive(): Boolean {
            return true
        }
        override fun queueInput(inputBuffer: ByteBuffer) {
            this.outputByteBuffer = haeEqualizerStream?.let {
//                inputBuffer.flip();
                //获取buffer中有效大小
                val len = inputBuffer.capacity()// - inputBuffer.position()
                Log.i(TAG, "queueInput: len = $len")
                if (len == 0 ) {
                    inputBuffer;
                } else {
//                    inputBuffer
                    val getByteArray = ByteArray(len)
                    if (!inputBuffer.hasRemaining()) {
                        return@let inputBuffer
                    }
//
                    for (index in 0 until len) {
                        getByteArray[index] = inputBuffer.get()
                    }
                    Log.i(TAG, "queueInput: ${getByteArray.contentToString()}")

                    val array = it.applyPcmData(getByteArray)

                    if(array!=null) {
                        Log.i(TAG, "queueInput:array.size = "+array.size)
                        val byteBuffer = ByteBuffer.allocate(array.size)

                        byteBuffer.put(array)

                        byteBuffer
                    }else{
                        inputBuffer
                    }

                }
            } ?: inputBuffer
        }

        override fun queueEndOfStream() {
            inputEnded = true
            haeEqualizerStream?.release();
        }

        override fun getOutput(): ByteBuffer {
            val buffer = this.outputByteBuffer
            this.outputByteBuffer = com.google.android.exoplayer2.audio.AudioProcessor.EMPTY_BUFFER
            return buffer
        }

        override fun isEnded(): Boolean {
            return inputEnded
        }

        override fun flush() {

        }

        override fun reset() {
            inputEnded = true
            outputByteBuffer = com.google.android.exoplayer2.audio.AudioProcessor.EMPTY_BUFFER
        }

    }

}