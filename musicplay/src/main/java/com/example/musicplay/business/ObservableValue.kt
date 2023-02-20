package com.example.musicplay.business

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.musongzi.core.ExtensionCoreMethod.exceptionRun

class ObservableValue<V> private constructor(
    v: V? = null,
    private val addMethodCall: ((Observer<V?>) -> Unit)? = null
) {

    private val flags: MutableCollection<Observer<V?>> = mutableListOf()
    private var handler = Handler(Looper.getMainLooper())

    /**
     * 当版本变动的时候触发[onValuesChange]
     */
    private var version = Long.MIN_VALUE
        set(value) {
            field = value
            onValuesChange()
        }

    /**
     * 当值变动的时候，改变当前版本
     */
    var value: V? = v
        set(value) {
            field = value
            version++
        }

    /**
     * 移除上次的[running]
     * 触发回调，post出去主线程
     */
    private fun onValuesChange() {
        handler.removeCallbacks(running)
        handler.post(running)
    }

    init {
        if (v != null) {
            value = v
        }
    }

    /**
     * 回调遍历的runnable
     */
    private val running = Runnable {
        //先把当前的集合赋值给新的集合，此做法避免当遍历时有人移除了集合里的观察者导致集合同步问题
        val newL = mutableListOf<Observer<V?>>().apply {
            addAll(flags)
        }
        //获取迭代器
        val i = newL.iterator()
        while (i.hasNext()) {
            val observer = i.next()
            observer.onChanged(value)
        }
    }


    /**
     * 注册观察
     * @param lifecycleOwner 生命周期的持有者：
     * @param observer 观察者
     * @param isCall 是否绑定立刻回调
     */
    @JvmOverloads
    fun observer(lifecycleOwner: LifecycleOwner? = null, isCall: Boolean = false, observerCall: Call<V>? = null, observer: Observer<V?>) {
        if (lifecycleOwner == null) {
            plusObserver(isCall, ValueObserver(this, observer, isCall).apply {
                this.call = observerCall
            })
        } else {
            lifecycleOwner.lifecycle.addObserver(ValueLifecycleObserver(this, observer, isCall).apply {
                this.call = observerCall
            })
        }
    }

//    fun getObserverOtherCall(obs: Observer<V?>): IValueObserver<V>? {
//        val newL = mutableListOf<Observer<V?>>().apply {
//            addAll(flags)
//        }
//        //获取迭代器
//        val i = newL.iterator()
//        while (i.hasNext()) {
//            val observer = i.next()
//            if (observer is BaseValueObserver<*>) {
//                if (observer.myObserver() == obs) {
//                    return observer as? IValueObserver<V>
//                }
//            }
//        }
//        return null;
//    }


    /**
     * 移除观察者
     * @param observer 观察者
     */
    fun remove(obs: Observer<V?>): Boolean {
        val newL = mutableListOf<Observer<V?>>().apply {
            addAll(flags)
        }
        //获取迭代器
        val i = newL.iterator()
        while (i.hasNext()) {
            val observer = i.next()
            if (observer is BaseValueObserver<V>) {
                if (observer.myObserver() == obs) {
                    val removeFlag = flags.remove(observer)
                    if (removeFlag) {
                        observer.call?.onRemoveCall(value)
                    }
                    return removeFlag
                }
            }
        }
        return false;
    }

    private fun plusObserver(isCall: Boolean = false, observer: BaseValueObserver<V>) {
        if (addMethodCall == null) {
            flags.add(observer)
            observer.call?.onAddCall(value)
        } else {
            addMethodCall.invoke(observer)
        }
        if (isCall && value != null) {
            exceptionRun {
                observer.onChanged(value)
            }
        }
    }

    internal class ValueObserver<V, L : MutableCollection<Observer<V?>>>(
        observerSimpleSupport: ObservableValue<V>? = null,
        observer: Observer<V?>,
        call: Boolean
    ) : ValueLifecycleObserver<V, L>(observerSimpleSupport, observer, call) {

        init {
            isCallValueChange = true
        }

    }

    abstract class BaseValueObserver<V> : IValueObserver<V>, Observer<V?> {
        abstract fun myObserver(): Any
        override var call: Call<V>? = null
    }

    internal open class ValueLifecycleObserver<V, L : MutableCollection<Observer<V?>>>
        (private var observerSimpleSupport: ObservableValue<V>? = null, val observer: Observer<V?>, private var isCall: Boolean) :
        DefaultLifecycleObserver,
        Observer<V?>, BaseValueObserver<V>() {

        var isCallValueChange = false
        var ver = 0L
        var isF = true;


        override fun myObserver() = observer


        override fun onCreate(owner: LifecycleOwner) {
            observerSimpleSupport?.apply {
                plusObserver(isCall, this@ValueLifecycleObserver)
            }
        }

        override fun onResume(owner: LifecycleOwner) {
            isCallValueChange = true
            observerSimpleSupport?.apply {
                onChanged(this.value)
            }
        }

        override fun onPause(owner: LifecycleOwner) {
            isCallValueChange = false
        }


        override fun onDestroy(owner: LifecycleOwner) {
            observerSimpleSupport = null
        }

        override fun onChanged(t: V?) {
            observerSimpleSupport?.apply {
                if (isCallValueChange && ver != this.version) {
                    if (isF) {
                        isF = false
                        if (value == null) {
                            return
                        }
                    }
                    ver = this.version
                    observer.onChanged(t)
                } else {
                    Log.d(TAG, "onChanged: 过滤")
                }
            }

        }


    }


    companion object {

        const val TAG = "ObsevabkeValue"

        @JvmStatic
        @JvmOverloads
        fun <V> create(defau: V? = null, addMethodCall: ((Observer<V?>) -> Unit)? = null) =
            ObservableValue(v = defau, addMethodCall)

    }

    interface IValueObserver<V> {
        var call: Call<V>?
    }

    interface Call<V> {
        fun onRemoveCall(value: V?)
        fun onAddCall(value: V?)
    }

}