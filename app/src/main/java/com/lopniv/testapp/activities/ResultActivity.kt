package com.lopniv.testapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.content.res.AppCompatResources
import com.google.mlkit.vision.barcode.Barcode
import com.lopniv.testapp.R
import com.lopniv.testapp.activities.base.BaseActivity
import com.lopniv.testapp.functions.ClipboardFunction
import com.lopniv.testapp.constants.StringConstants.STRING_FORMAT_RESULT
import com.lopniv.testapp.constants.StringConstants.STRING_RESULT
import com.lopniv.testapp.databinding.ActivityResultBinding
import com.lopniv.testapp.functions.DateFunction

class ResultActivity : BaseActivity<ActivityResultBinding>()
{

    private var _stringResult: String? = null
    private var _intResultFormat: Int? = null

    companion object
    {
        fun startResult(
            context: Context,
            stringResult: String,
            stringFormat: Int
        ) =
            context.startActivity(
                Intent(context, ResultActivity::class.java).apply()
                {
                    putExtra(STRING_RESULT, stringResult)
                    putExtra(STRING_FORMAT_RESULT, stringFormat)
                }
            )
    }

    override fun getViewBinding() = ActivityResultBinding.inflate(layoutInflater)

    override fun startAction() {
        _stringResult = intent.extras?.getString(STRING_RESULT)
        _intResultFormat = intent.extras?.getInt(STRING_FORMAT_RESULT, 0)
        initViews()
    }

    private fun  initViews()
    {
        with(_binding)
        {
            imageViewCopy.setOnClickListener { copyText() }
            buttonScanAgain.setOnClickListener { onBackPressed() }
            textViewResult.text = _stringResult
            textViewDate.text = DateFunction().getCurrentDate("dd.MM.yyyy HH:mm")
            if (_intResultFormat == Barcode.FORMAT_QR_CODE)
            {
                textViewTitleItem.text = getString(R.string.TEXT_QR_CODE)
                imageViewItem.background = AppCompatResources.getDrawable(this@ResultActivity, R.drawable.ic_qr_code)
            }
            else
            {
                textViewTitleItem.text = getString(R.string.TEXT_BARCODE)
                imageViewItem.background = AppCompatResources.getDrawable(this@ResultActivity, R.drawable.ic_barcode)
            }
        }
    }

    private fun copyText()
    {
        _stringResult?.let { ClipboardFunction().copyToClipboard(this, it) }
        showToast("Result has been copied")
    }
}