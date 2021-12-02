package com.lopniv.testapp.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.lopniv.testapp.activities.base.BaseActivity
import com.lopniv.testapp.camera.CameraRecord
import com.lopniv.testapp.constants.IntConstants.INT_PERMISSION_CAMERA_REQUEST_CODE
import com.lopniv.testapp.constants.StringConstants.STRING_PATH_RECORD
import com.lopniv.testapp.constants.StringConstants.STRING_TAG_RECORD
import com.lopniv.testapp.databinding.ActivityRecordBinding

class RecordActivity : BaseActivity<ActivityRecordBinding>()
{

    private val _cameraRecord = CameraRecord()

    companion object
    {
        fun startRecord(context: Context) =
            context.startActivity(Intent(context, RecordActivity::class.java))
    }

    private var _uriVideoPath: Uri? = null

    override fun getViewBinding() = ActivityRecordBinding.inflate(layoutInflater)

    override fun startAction()
    {
        initViews()
    }

    private fun initViews()
    {
        if (_cameraRecord.checkCameraPresent(baseContext))
        {
            if (_cameraRecord.checkCameraPermission(this))
            {
                recordVideo()
            }
            else
            {
                _cameraRecord.getCameraPermission(this)
                Log.e(STRING_TAG_RECORD, "No Camera Permission")
            }
            Log.i(STRING_TAG_RECORD, "Camera is detected")
        }
        else
        {
            Log.i(STRING_TAG_RECORD, "No camera is detected")
        }
    }

    private val _registerForResultVideo = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        when (it.resultCode) {
            RESULT_OK -> {
                _uriVideoPath = it.data?.data
                Log.e(STRING_TAG_RECORD, "Video is recorded and available at path $_uriVideoPath")
                onBackPressed()
            }
            RESULT_CANCELED -> {
                Log.e(STRING_TAG_RECORD, "Recording video is cancelled")
                onBackPressed()
            }
            else -> {
                Log.e(STRING_TAG_RECORD, "Recording video has got some error")
                onBackPressed()
            }
        }
    }

    private fun recordVideo()
    {
        _registerForResultVideo.launch(
            Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply()
            {
                putExtra(MediaStore.EXTRA_OUTPUT, STRING_PATH_RECORD)
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    )
    {
        if (requestCode == INT_PERMISSION_CAMERA_REQUEST_CODE)
        {
            if (_cameraRecord.checkCameraPermission(this))
            {
                recordVideo()
            }
            else
            {
                Log.e(STRING_TAG_RECORD, "No Camera Permission")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}