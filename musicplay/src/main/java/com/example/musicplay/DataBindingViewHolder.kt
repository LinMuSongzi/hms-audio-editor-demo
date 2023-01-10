package com.example.musicplay

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class DataBindingViewHolder<D:ViewDataBinding>(var dataBinding: D) : RecyclerView.ViewHolder(dataBinding.root)
