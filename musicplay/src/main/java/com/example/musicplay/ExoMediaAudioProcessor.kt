package com.example.musicplay

import android.util.Log
import com.google.android.exoplayer2.audio.BaseAudioProcessor
import com.example.musicplay.viewmodel.ExoPlayViewModel
import com.huawei.hms.audioeditor.sdk.*
import com.huawei.hms.audioeditor.sdk.AudioParameters.*
import java.nio.ByteBuffer

class ExoMediaAudioProcessor : BaseAudioProcessor() {

    var enableEffect: Boolean = false
    var systemTime = System.currentTimeMillis()

    /**
     * 均衡器
     */
    var haeEqualizerStream: HAEEqualizerStream? = null
    var haeEqualizerStreamEnable = false;

    /**
     * 场景
     */
    var hAESceneStream: HAESceneStream? = null
    var hAESceneStreamEnable = false;

    /**
     * 声场
     */
    var hAESoundFieldStream: HAESoundFieldStream? = null
    var hAESoundFieldStreamEnable = true

    override fun onConfigure(inputAudioFormat: com.google.android.exoplayer2.audio.AudioProcessor.AudioFormat): com.google.android.exoplayer2.audio.AudioProcessor.AudioFormat {
        if (haeEqualizerStream == null) {
            val haeEqualizerStream = HAEEqualizerStream()
//                haeEqualizerStream.
            haeEqualizerStream.setEqParams(ARRAY_EQ[0])
            val re = haeEqualizerStream.setAudioFormat(16, inputAudioFormat.channelCount, inputAudioFormat.sampleRate)
            if (re == HAEErrorCode.SUCCESS) {
                this.haeEqualizerStream = haeEqualizerStream;
                Log.i(
                    ExoPlayViewModel.TAG,
                    "configure: success bit = 16 , channelCount = ${inputAudioFormat.channelCount} , sampleRate = ${inputAudioFormat.sampleRate}"
                )
            } else {
                Log.i(ExoPlayViewModel.TAG, "configure: haeEqualizerStream error = $re")
            }
        }

        if (hAESoundFieldStream == null) {
            val hAESoundFieldStream = HAESoundFieldStream()
            hAESoundFieldStream.setSoundType(SOUND_FIELD_GRAND)
            val re = hAESoundFieldStream.setAudioFormat(16, inputAudioFormat.channelCount, inputAudioFormat.sampleRate)
            if (re == HAEErrorCode.SUCCESS) {
                this.hAESoundFieldStream = hAESoundFieldStream
            } else {
                Log.i(ExoPlayViewModel.TAG, "configure: hAESoundFieldStream error = $re")
            }
        }

        if (hAESceneStream == null) {

            val hAESceneStream = HAESceneStream()
            hAESceneStream.setEnvironmentType(EN_TYPE[0])
            val re = hAESceneStream.setAudioFormat(16, inputAudioFormat.channelCount, inputAudioFormat.sampleRate)
            if (re == HAEErrorCode.SUCCESS) {
                this.hAESceneStream = hAESceneStream
            } else {
                Log.i(ExoPlayViewModel.TAG, "configure: hAESoundFieldStream error = $re")
            }
        }

        return inputAudioFormat;

        //super.onConfigure(inputAudioFormat)

    }

    override fun queueInput(inputBuffer: ByteBuffer) {
        if (inputBuffer.hasRemaining()) {

            val dataMaxSize = inputBuffer.limit()
            val offset = inputBuffer.position()
            val hadByte = dataMaxSize - offset;


            var bb: ByteArray? = ByteArray(dataMaxSize)
            inputBuffer.get(bb!!)

            if (enableEffect) {
                if (haeEqualizerStreamEnable) {
                    bb = haeEqualizerStream?.applyPcmData(bb)
                }
                if (hAESceneStreamEnable) {
                    bb = hAESceneStream?.applyPcmData(bb)
                }
                if (hAESoundFieldStreamEnable) {
                    bb = hAESoundFieldStream?.applyPcmData(bb)
                }
            }

            if (bb != null) {
//                Log.i(ExoPlayViewModel.TAG, "queueInput: enableEffect = $enableEffect")
                replaceOutputBuffer(bb.size).put(bb).flip()
            }
        }
    }

    companion object {

        val SOUND_FIELD = arrayOf(
            SOUND_FIELD_WIDE,
            SOUND_FIELD_FRONT_FACING,
            SOUND_FIELD_NEAR,
            SOUND_FIELD_GRAND
        )
        val SOUND_STR_FIELD = arrayOf(
            "SOUND_FIELD_WIDE",
            "SOUND_FIELD_FRONT_FACING",
            "SOUND_FIELD_NEAR",
            "SOUND_FIELD_GRAND"
        )

        val EN_TYPE = arrayOf(
            ENVIRONMENT_TYPE_BROADCAST,
            ENVIRONMENT_TYPE_EARPIECE,
            ENVIRONMENT_TYPE_UNDERWATER,
            ENVIRONMENT_TYPE_GRAMOPHONE,
            ENVIRONMENT_TYPE_CLOSE
        )
        val EN_STR_TYPE = arrayOf(
            "ENVIRONMENT_TYPE_BROADCAST",
            "ENVIRONMENT_TYPE_EARPIECE",
            "ENVIRONMENT_TYPE_UNDERWATER",
            "ENVIRONMENT_TYPE_GRAMOPHONE",
            "ENVIRONMENT_TYPE_CLOSE"
        )


        @JvmStatic
        val ARRAY_EQ = arrayOf(
            AudioParameters.EQUALIZER_POP_VALUE,
            AudioParameters.EQUALIZER_CLASSICAL_VALUE,
            AudioParameters.EQUALIZER_JAZZ_VALUE,
            AudioParameters.EQUALIZER_ROCK_VALUE,
            AudioParameters.EQUALIZER_RB_VALUE,
            AudioParameters.EQUALIZER_BALLADS_VALUE
        )

        @JvmStatic
        val ARRAY_STR_EQ = arrayOf(
            "AudioParameters.EQUALIZER_POP_VALUE",
            "AudioParameters.EQUALIZER_CLASSICAL_VALUE",
            "AudioParameters.EQUALIZER_JAZZ_VALUE",
            "AudioParameters.EQUALIZER_ROCK_VALUE",
            "AudioParameters.EQUALIZER_RB_VALUE",
            "AudioParameters.EQUALIZER_BALLADS_VALUE"
        )


    }

}