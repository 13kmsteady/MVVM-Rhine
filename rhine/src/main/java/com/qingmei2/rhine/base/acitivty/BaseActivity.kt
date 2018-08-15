package com.qingmei2.rhine.base.acitivty

import android.app.ProgressDialog
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.qingmei2.rhine.base.BaseViewModel
import com.qingmei2.rhine.http.service.ServiceManager
import com.qingmei2.rxschedulers.SchedulerProvider
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.instance

abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(), KodeinAware {

    private val parentKodein by closestKodein()

    override val kodein: Kodein by retainedKodein {
        extend(parentKodein, copy = Copy.All)
    }

    val serviceManager: ServiceManager by instance()
    val schedulers: SchedulerProvider by instance()


    protected lateinit var binding: B

    protected lateinit var viewModel: VM

    protected var progressDialog: ProgressDialog? = null

    protected abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        viewModel = initViewModel()
        initView()
        initData()
    }

    protected fun onStateChanged(state: BaseViewModel.State) {
        when (state) {
            BaseViewModel.State.LOAD_WAIT -> {
            }
            BaseViewModel.State.LOAD_ING -> loading(true)
            BaseViewModel.State.LOAD_SUCCESS -> loading(false)
            BaseViewModel.State.LOAD_FAILED -> loading(false)
        }
    }

    protected fun loading(showing: Boolean) {
        if (showing) {
            progressDialog = ProgressDialog(this)
            progressDialog?.setCancelable(false)
            progressDialog?.show()
        } else {
            progressDialog?.dismiss()
            progressDialog = null
        }
    }

    protected abstract fun initViewModel(): VM

    protected abstract fun initData()

    protected abstract fun initView()
}
