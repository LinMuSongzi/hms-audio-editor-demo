package com.huawei.hms.audioeditor.demo

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.huawei.hms.audioeditor.demo.viewmodel.ExoPlayViewModel

class ExoPlaytestActivity2 : AppCompatActivity() {


    private val mExoPlayViewModel by viewModels<ExoPlayViewModel>()

    private  var launcher: ActivityResultLauncher<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_playtest2)

        launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()){


            if(it){

                mExoPlayViewModel.playMusic(this);

            }


        }

    }



    fun playClick(v: View){
        launcher?.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }










}