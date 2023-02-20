package com.example.musicplay.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.example.musicplay.bean.StreamModeInfo;
import com.example.musicplay.business.ObservableValue;
import com.musongzi.core.base.business.EmptyBusiness;
import com.musongzi.core.base.vm.DataDriveViewModel;

public class ValuesViewModel extends ViewModel {


    public ObservableValue<StreamModeInfo> mStreamModeInfo = ObservableValue.create();






}
