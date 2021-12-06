package com.lopniv.testapp.activities.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity()
{
    lateinit var _binding: VB

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(_binding.root)
        startAction()
    }

    abstract fun getViewBinding(): VB
    abstract fun startAction()

    fun View.visible()
    {
        this.visibility = View.VISIBLE
    }

    fun View.gone()
    {
        this.visibility = View.GONE
    }

    fun View.invisible()
    {
        this.visibility = View.INVISIBLE
    }

    fun View.enable()
    {
        this.isEnabled = true
    }

    fun View.disable()
    {
        this.isEnabled = false
    }

    fun showToast(message : String)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    fun showErrorLog(message: String)
    {
        Log.e("Error", message)
    }

    fun showDebugLog(message: String)
    {
        Log.d("Debug", message)
    }
}