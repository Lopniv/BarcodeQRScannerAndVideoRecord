package com.lopniv.testapp.activities

import com.lopniv.testapp.activities.base.BaseActivity
import com.lopniv.testapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>()
{

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun startAction()
    {
        initViews()
    }

    private fun initViews()
    {
        with(_binding)
        {
            buttonScan.setOnClickListener { openScanner() }
            buttonRecord.setOnClickListener { openRecorder() }
        }
    }

    private fun openScanner()
    {
        ScannerActivity.startScanner(this)
    }

    private fun openRecorder()
    {
        RecordActivity.startRecord(this)
    }
}