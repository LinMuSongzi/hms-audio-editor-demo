package com.example.musicplay

import com.example.musicplay.ExoMediaAudioProcessor.Companion.ARRAY_EQ
import com.example.musicplay.ExoMediaAudioProcessor.Companion.ARRAY_STR_EQ
import com.example.musicplay.ExoMediaAudioProcessor.Companion.EN_STR_TYPE
import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplay.ExoMediaAudioProcessor.Companion.EN_TYPE
import com.example.musicplay.ExoMediaAudioProcessor.Companion.SOUND_FIELD
import com.example.musicplay.ExoMediaAudioProcessor.Companion.SOUND_STR_FIELD
import com.example.musicplay.databinding.ActivityExoPlaytest2Binding
import com.example.musicplay.databinding.ItemEqBinding
import com.example.musicplay.bean.StreamModeInfo
import com.example.musicplay.viewmodel.ContextViewModel
import com.example.musicplay.viewmodel.ExoPlayViewModel

class ExoPlaytestActivity2 : AppCompatActivity() {

    val TAG = "ItemEqBinding"

    private val mExoPlayViewModel by viewModels<ExoPlayViewModel>()
    private val mContextViewModel by viewModels<ContextViewModel>()
    private lateinit var dataBinding: ActivityExoPlaytest2Binding

    private var launcher: ActivityResultLauncher<String>? = null
    private val mStreamModeInfos = mutableListOf<StreamModeInfo>().apply {
        add(StreamModeInfo("均衡器", null, 1 or 0x100))
        add(StreamModeInfo(ARRAY_STR_EQ[0], ARRAY_EQ[0], 1))
        add(StreamModeInfo(ARRAY_STR_EQ[1], ARRAY_EQ[1], 1))
        add(StreamModeInfo(ARRAY_STR_EQ[2], ARRAY_EQ[2], 1))
        add(StreamModeInfo(ARRAY_STR_EQ[3], ARRAY_EQ[3], 1))
        add(StreamModeInfo(ARRAY_STR_EQ[4], ARRAY_EQ[4], 1))
        add(StreamModeInfo(ARRAY_STR_EQ[5], ARRAY_EQ[5], 1))

        add(StreamModeInfo("场景", null, 2 or 0x100))
        add(StreamModeInfo(EN_STR_TYPE[0], EN_TYPE[0], 2))
        add(StreamModeInfo(EN_STR_TYPE[1], EN_TYPE[1], 2))
        add(StreamModeInfo(EN_STR_TYPE[2], EN_TYPE[2], 2))
        add(StreamModeInfo(EN_STR_TYPE[3], EN_TYPE[3], 2))
        add(StreamModeInfo(EN_STR_TYPE[4], EN_TYPE[4], 2))

        add(StreamModeInfo("声场", null, 3 or 0x100))
        add(StreamModeInfo(SOUND_STR_FIELD[0], SOUND_FIELD[0], 4))
        add(StreamModeInfo(SOUND_STR_FIELD[1], SOUND_FIELD[1], 4))
        add(StreamModeInfo(SOUND_STR_FIELD[2], SOUND_FIELD[2], 4))
        add(StreamModeInfo(SOUND_STR_FIELD[3], SOUND_FIELD[3], 4))

        add(StreamModeInfo("原声", false, 3))
//        add(StreamModeInfo("效果", 0, 4))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_exo_playtest2)//setContentView(R.layout.activity_exo_playtest2)


//        CViewmode

        Handler(Looper.getMainLooper()).postDelayed({
            mContextViewModel.changeItem.value = "asdasd"
            mContextViewModel.changePosition.value = 0x10
        },2000)

        launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {

            if (it) {

                mExoPlayViewModel.playMusic(this);
                runOnUiThread {
                    dataBinding.idRecycle.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    dataBinding.idRecycle.adapter = object : RecyclerView.Adapter<DataBindingViewHolder<ItemEqBinding>>() {
                        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                            DataBindingViewHolder(ItemEqBinding.inflate(layoutInflater, parent, false))

                        override fun onBindViewHolder(holder: DataBindingViewHolder<ItemEqBinding>, position: Int) {
                            Log.i(TAG, "onBindViewHolder: position = $position")
                            val info = mStreamModeInfos[position]
                            holder.dataBinding.idTitle.text = info.title
                            holder.dataBinding.root.setOnClickListener {
                                var index = info.type and 0x100 == 0;
                                if (index) {
                                    when (info.type) {
                                        1 -> {
                                            mExoPlayViewModel.chooseEqualizer = info.values as IntArray
                                        }
                                        2 -> {
                                            mExoPlayViewModel.chooseSceneStream = info.values as Int
                                        }
                                        3 -> {
                                            mExoPlayViewModel.enable = info.values as Boolean
                                        }
                                        4 -> {
                                            mExoPlayViewModel.chooseSoundStream = info.values as Int
                                        }
                                    }
                                }else {
                                    mExoPlayViewModel.enableIndex = 0x100.inv() and info.type

                                }
                            }
                        }

                        override fun getItemCount() = mStreamModeInfos.size

                    }
                }

            }


        }

    }


    fun playClick(v: View) {
        launcher?.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }


}