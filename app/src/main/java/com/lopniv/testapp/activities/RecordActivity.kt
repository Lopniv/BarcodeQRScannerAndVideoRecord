package com.lopniv.testapp.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.lifecycle.ViewModelProvider
import com.lopniv.testapp.activities.base.BaseActivity
import com.lopniv.testapp.constants.EnumConstant.ENUM_RECORD_STATE
import com.lopniv.testapp.constants.IntConstants
import com.lopniv.testapp.constants.StringConstants
import com.lopniv.testapp.constants.StringConstants.STRING_TAG_RECORD
import com.lopniv.testapp.databinding.ActivityRecordBinding
import com.lopniv.testapp.functions.DateFunction
import com.lopniv.testapp.functions.SettingFunction
import com.lopniv.testapp.functions.VideoRecordFunction
import com.lopniv.testapp.viewmodels.CameraXViewModel

class RecordActivity : BaseActivity<ActivityRecordBinding>()
{

    companion object
    {
        fun startRecord(context: Context) =
            context.startActivity(Intent(context, RecordActivity::class.java))
    }


    //region DECLARATION

    private lateinit var _videoRecordEvent: VideoRecordEvent
    private lateinit var _videoCapture: VideoCapture<Recorder>
    private lateinit var _animation: Animation
    private var _activeRecording: ActiveRecording? = null
    private var _videoRecordFunction: VideoRecordFunction? = null
    private val _mainThreadExecutor by lazy { ContextCompat.getMainExecutor(this) }

    //endregion


    private val _consumerRecordEvent = Consumer<VideoRecordEvent>
    { event ->
        if (event !is VideoRecordEvent.Status)
            _videoRecordEvent = event
    }

    override fun getViewBinding() = ActivityRecordBinding.inflate(layoutInflater)

    override fun startAction()
    {
        initViews()
        setupVideoCapture()
        setupCamera()
        setupAnimationBlink()
    }

    private fun initViews()
    {
        with(_binding)
        {
            buttonCapture.setOnClickListener { startRecord() }
            buttonStop.setOnClickListener { stopRecord() }
        }
    }

    private fun setupVideoCapture()
    {
        val qualitySelector = QualitySelector.of(QualitySelector.QUALITY_HD)
        val recorder = Recorder.Builder()
            .setQualitySelector(qualitySelector)
            .build()
        _videoCapture = VideoCapture.withOutput(recorder)
    }

    private fun setupCamera()
    {
        ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[CameraXViewModel::class.java]
            .processCameraProvider
            .observe(this)
            { provider: ProcessCameraProvider? ->
                _videoRecordFunction =
                    provider?.let()
                    {
                        VideoRecordFunction(
                            this,
                            _binding.previewView,
                            it,
                            this,
                            _videoCapture
                        )
                    }
                if (_videoRecordFunction?.checkAllPermissionIsGranted(this) == true)
                {
                    _videoRecordFunction?.bindPreviewUseCase()
                }
                else
                {
                    _videoRecordFunction?.getAllPermission(this)
                }
            }
    }

    private fun setupAnimationBlink()
    {
        _animation = AlphaAnimation(0.0f, 1.0f)
        _animation.duration = 500
        _animation.startOffset = 100
        _animation.repeatMode = Animation.REVERSE
        _animation.repeatCount = Animation.INFINITE
    }

    private fun setIsTimerRecord(boolean: Boolean)
    {
        with(_binding)
        {
            if (boolean)
            {
                groupTimer.visible()
                chronometer.base = SystemClock.elapsedRealtime()
                chronometer.start()
                imageViewRedDot.animation = _animation
            }
            else
            {
                imageViewRedDot.clearAnimation()
                chronometer.stop()
                groupTimer.invisible()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startRecord()
    {
        showUI(ENUM_RECORD_STATE.RECORDING)
        val name = "TestAppRecording-" +
                DateFunction().getCurrentDate("yyyyddMM") + ".mp4"
        val contentValues = ContentValues().apply()
        {
            put(MediaStore.Video.Media.DISPLAY_NAME, name)
        }
        val mediaStoreOutput = MediaStoreOutputOptions.Builder(
            contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        _activeRecording =
            _videoCapture.output.prepareRecording(this, mediaStoreOutput)
                .withEventListener(
                    _mainThreadExecutor,
                    _consumerRecordEvent
                )
                .apply { withAudioEnabled() }
                .start()
        Log.i(StringConstants.STRING_TAG_CAMERA_RECORDER, "Recording started")
    }

    private fun stopRecord()
    {
        if (_activeRecording == null || _videoRecordEvent is VideoRecordEvent.Finalize)
        {
            return
        }
        else if (_activeRecording != null)
        {
            _activeRecording?.stop()
            _activeRecording = null
            showUI(ENUM_RECORD_STATE.IDLE)
        }
    }

    private fun showUI(state: ENUM_RECORD_STATE)
    {
        with(_binding)
        {
            when(state)
            {
                ENUM_RECORD_STATE.IDLE ->
                {
                    setIsTimerRecord(false)
                    buttonCapture.visible()
                    buttonStop.invisible()
                    buttonCapture.setImageResource(com.lopniv.testapp.R.drawable.ic_record)
                }
                ENUM_RECORD_STATE.RECORDING ->
                {
                    setIsTimerRecord(true)
                    buttonStop.visible()
                    buttonCapture.invisible()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    )
    {
        if (requestCode == IntConstants.INT_PERMISSION_VIDEO_RECORD_REQUEST_CODE)
        {
            if (grantResults.isNotEmpty())
            {
                grantResults.forEach()
                {
                    if (it == PackageManager.PERMISSION_GRANTED)
                    {
                        _videoRecordFunction?.bindPreviewUseCase()
                    }
                    else
                    {
                        SettingFunction().openSettingPermission(this)
                        Log.e(STRING_TAG_RECORD, "No Permissions Granted")
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume()
    {
        super.onResume()
        setupCamera()
    }
}

