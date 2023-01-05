package com.huawei.hms.audioeditor.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.huawei.hms.audioeditor.sdk.*
import com.huawei.hms.audioeditor.sdk.engine.audio.AudioUtils.BIT_DEPTH
import com.huawei.hms.audioeditor.sdk.engine.audio.AudioUtils.CHANNEL_COUNT
import javax.sound.sampled.FloatControl.Type.SAMPLE_RATE


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MsongziTestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MsongziTestFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }





    }


    lateinit var changeVoiceOption:ChangeVoiceOption


    fun 设置变声效果(){//设置变声效果（语音版-针对人声，不支持带背景音乐）。
        val haeChangeVoiceStream = HAEChangeVoiceStream()
        changeVoiceOption = ChangeVoiceOption()
        changeVoiceOption.setSpeakerSex(ChangeVoiceOption.SpeakerSex.MALE)
        changeVoiceOption.setVoiceType(ChangeVoiceOption.VoiceType.CUTE)
        haeChangeVoiceStream.changeVoiceOption(changeVoiceOption)
// 设置音频的格式参数，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
// 设置音频的格式参数，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
        val res: Int = haeChangeVoiceStream.setAudioFormat(BIT_DEPTH, CHANNEL_COUNT, FloatControl.Type.SAMPLE_RATE)
// 对pcm数据（buffer）进行变声操作，返回变声后的pcm数据（resultByte）
// 对pcm数据（buffer）进行变声操作，返回变声后的pcm数据（resultByte）
        while (buffer != null) {
            val resultByte: ByteArray = haeChangeVoiceStream.applyPcmData(buffer)
        }
// 变声结束后释放资源
// 变声结束后释放资源
        haeChangeVoiceStream.release()


    }

    fun 设置变声效果2(){//设置变声效果（通用版-支持带背景音乐）。
        val haeChangeVoiceStreamCommon = HAEChangeVoiceStreamCommon()
        haeChangeVoiceStreamCommon.changeVoiceType(VoiceTypeCommon.SEASONED)
// 设置音频的格式参数，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
// 设置音频的格式参数，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
        val res: Int = haeChangeVoiceStreamCommon.setAudioFormat(BIT_DEPTH, CHANNEL_COUNT, javax.sound.sampled.FloatControl.Type.SAMPLE_RATE)
// 对PCM数据（buffer）进行变声操作，返回变声后的PCM数据（resultByte）
// 对PCM数据（buffer）进行变声操作，返回变声后的PCM数据（resultByte）
        while (buffer != null) {
            val resultByte: ByteArray = haeChangeVoiceStreamCommon.applyPcmData(buffer)
        }
// 变声结束后释放资源
// 变声结束后释放资源
        haeChangeVoiceStreamCommon.release()
    }


    fun 进行声音美化(){
        val haeVoiceBeautifierStream = HAEVoiceBeautifierStream()
        haeVoiceBeautifierStream.setVoiceBeautifierType(VoiceBeautifierType.CD)
// 设置音频的格式参数，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
// 设置音频的格式参数，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
        val res: Int = haeVoiceBeautifierStream.setAudioFormat(BIT_DEPTH, CHANNEL_COUNT, javax.sound.sampled.FloatControl.Type.SAMPLE_RATE)
// 对PCM数据（buffer）进行变声操作，返回变声后的PCM数据（resultByte）
// 对PCM数据（buffer）进行变声操作，返回变声后的PCM数据（resultByte）
        while (buffer != null) {
            val resultByte: ByteArray = haeVoiceBeautifierStream.applyPcmData(buffer)
        }
// 美化结束后释放资源
// 美化结束后释放资源
        haeVoiceBeautifierStream.release()
    }

    fun 添加环境效果(){

        val haeSceneStream = HAESceneStream()
// 设置音频的格式参数，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
// 设置音频的格式参数，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
        var res = haeSceneStream.setAudioFormat(BIT_DEPTH, CHANNEL_COUNT, javax.sound.sampled.FloatControl.Type.SAMPLE_RATE)
// 设置环境类型，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
// 设置环境类型，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
        haeSceneStream.setEnvironmentType(AudioParameters.ENVIRONMENT_TYPE_BROADCAST)
// 对pcm数据（buffer）进行环境效果操作，返回变声后的pcm数据（resultByte）
// 对pcm数据（buffer）进行环境效果操作，返回变声后的pcm数据（resultByte）
        while (buffer != null) {
            val resultByte = haeSceneStream.applyPcmData(buffer)
        }
// 环境结束后释放资源
// 环境结束后释放资源
        haeSceneStream.release()


    }

    fun 添加声场效果(){
        val haeSoundFieldStream = HAESoundFieldStream()
// 设置音频的格式参数，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
// 设置音频的格式参数，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
        var res = haeSoundFieldStream.setAudioFormat(BIT_DEPTH, CHANNEL_COUNT, javax.sound.sampled.FloatControl.Type.SAMPLE_RATE)
// 设置声场类型，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
// 设置声场类型，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
        haeSoundFieldStream.setSoundType(AudioParameters.SOUND_FIELD_WIDE)
// 对pcm数据（buffer）进行声场操作，返回变声后的pcm数据（resultByte）
// 对pcm数据（buffer）进行声场操作，返回变声后的pcm数据（resultByte）
        while (buffer != null) {
            val resultByte = haeSoundFieldStream.applyPcmData(buffer)
        }
// 声场结束后释放资源
// 声场结束后释放资源
        haeSoundFieldStream.release()
    }

    fun 设置均衡参数(){
        val haeEqualizerStream = HAEEqualizerStream()
// 设置均衡的格式参数，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
// 设置均衡的格式参数，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
        var res = haeEqualizerStream.setAudioFormat(BIT_DEPTH, CHANNEL_COUNT, javax.sound.sampled.FloatControl.Type.SAMPLE_RATE)
// 设置声场类型，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
// 设置声场类型，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
        haeEqualizerStream.setEqParams(AudioParameters.EQUALIZER_POP_VALUE)
// 对pcm数据（buffer）进行均衡操作，返回变声后的pcm数据（resultByte）
// 对pcm数据（buffer）进行均衡操作，返回变声后的pcm数据（resultByte）
        while (buffer != null) {
            val resultByte = haeEqualizerStream.applyPcmData(buffer)
        }
// 均衡结束后释放资源
// 均衡结束后释放资源
        haeEqualizerStream.release()
    }

    fun 添加降噪效果(){

        val haeNoiseReductionStream = HAENoiseReductionStream()
// 设置音频的格式参数，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
// 设置音频的格式参数，返回设置结果res（注：当res为HAEErrorCode.SUCCESS时才可进行后续操作）
        var res = haeNoiseReductionStream.setAudioFormat(BIT_DEPTH, CHANNEL_COUNT, javax.sound.sampled.FloatControl.Type.SAMPLE_RATE)
// 对pcm数据（buffer）进行降噪操作，返回变声后的pcm数据（resultByte）
// 对pcm数据（buffer）进行降噪操作，返回变声后的pcm数据（resultByte）
        while (buffer != null) {
            val resultByte = haeNoiseReductionStream.applyPcmData(buffer)
        }
// 降噪结束后释放资源
// 降噪结束后释放资源
        haeNoiseReductionStream.release()

    }






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_msongzi_test, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MsongziTestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MsongziTestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}