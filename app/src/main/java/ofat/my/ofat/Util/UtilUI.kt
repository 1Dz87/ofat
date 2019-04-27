package ofat.my.ofat.Util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import ofat.my.ofat.R
import java.text.SimpleDateFormat
import java.util.*
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.text.format.DateUtils
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager


object UtilUI {

    val DRAWABLE_LEFT = 0
    val DRAWABLE_TOP = 1
    val DRAWABLE_RIGHT = 2
    val DRAWABLE_BOTTOM = 3

    fun showProgress(progressBar: ProgressBar) {
        if (progressBar.visibility == View.GONE) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    @SuppressLint("SimpleDateFormat", "ClickableViewAccessibility")
    fun setDateToField(input: TextInputEditText, activity: Activity) {
        input.keyListener = null
        input.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val mcurrentDate = Calendar.getInstance()
                val mYear = mcurrentDate.get(Calendar.YEAR)
                val mMonth = mcurrentDate.get(Calendar.MONTH)
                val mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH)
                val mDatePicker = DatePickerDialog(
                    activity,
                    DatePickerDialog.OnDateSetListener { _, year, month, day ->
                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, day)
                        val formatter = SimpleDateFormat("dd.MM.yyyy")
                        input.setText(formatter.format(calendar.time))
                    }, mYear, mMonth, mDay
                )

                mDatePicker.setButton(
                    DialogInterface.BUTTON_NEUTRAL,
                    activity.getString(R.string.erase)
                ) { dialog, which ->
                    if (which == DialogInterface.BUTTON_NEUTRAL) {
                        input.setText("")
                        dialog.cancel()
                    }
                }
                mDatePicker.show()
            }
        }
        input.setOnClickListener {
            val mcurrentDate = Calendar.getInstance()
            val mYear = mcurrentDate.get(Calendar.YEAR)
            val mMonth = mcurrentDate.get(Calendar.MONTH)
            val mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH)
            val mDatePicker = DatePickerDialog(
                activity,
                DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, day)
                    val formatter = SimpleDateFormat("dd.MM.yyyy")
                    input.setText(formatter.format(calendar.time))
                }, mYear, mMonth, mDay
            )
            mDatePicker.setButton(DialogInterface.BUTTON_NEUTRAL, activity.getString(R.string.erase)) { dialog, which ->
                if (which == DialogInterface.BUTTON_NEUTRAL) {
                    input.setText("")
                    dialog.cancel()
                }
            }
            mDatePicker.show()
        }
    }

    fun setTimeToField(input: TextInputEditText, activity: Activity) {
        input.keyListener = null
        val mcurrentDate = Calendar.getInstance()
        input.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val mTimePicker = TimePickerDialog(
                    activity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        mcurrentDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        mcurrentDate.set(Calendar.MINUTE, minute)
                        input.setText(
                            DateUtils.formatDateTime(
                                activity,
                                mcurrentDate.timeInMillis,
                                DateUtils.FORMAT_SHOW_TIME
                            )
                        )
                    },
                    mcurrentDate.get(Calendar.HOUR_OF_DAY),
                    mcurrentDate.get(Calendar.MINUTE), true
                )
                mTimePicker.setButton(
                    DialogInterface.BUTTON_NEUTRAL,
                    activity.getString(R.string.erase)
                ) { dialog, which ->
                    if (which == DialogInterface.BUTTON_NEUTRAL) {
                        input.setText("")
                        dialog.cancel()
                    }
                }
                mTimePicker.show()
            }
        }
        input.setOnClickListener {
            val mTimePicker = TimePickerDialog(
                activity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    mcurrentDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    mcurrentDate.set(Calendar.MINUTE, minute)
                    input.setText(
                        DateUtils.formatDateTime(
                            activity,
                            mcurrentDate.timeInMillis,
                            DateUtils.FORMAT_SHOW_TIME
                        )
                    )
                },
                mcurrentDate.get(Calendar.HOUR_OF_DAY),
                mcurrentDate.get(Calendar.MINUTE), true
            )
            mTimePicker.setButton(DialogInterface.BUTTON_NEUTRAL, activity.getString(R.string.erase)) { dialog, which ->
                if (which == DialogInterface.BUTTON_NEUTRAL) {
                    input.setText("")
                    dialog.cancel()
                }
            }
            mTimePicker.show()
        }
    }

    fun checkTextFields(editText: Array<EditText>): Boolean {
        var result = true
        for (eText in editText) {
            if (eText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                if (ExtractUtil.v(eText) != null && ExtractUtil.v(eText)!!.length < 5) {
                    eText.error = "Пароль должен быть не менее 5 латинских символов"
                    result = false
                }
            }
            if (ExtractUtil.v(eText) == null) {
                eText.error = "Требуется заполнение поля"
                result = false
            }
        }
        return result
    }

    fun checkTextInputFields(editText: Array<TextInputEditText>): Boolean {
        var result = true
        for (eText in editText) {
            if (eText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                if (ExtractUtil.v(eText) != null && ExtractUtil.v(eText)!!.length < 5) {
                    eText.error = "Пароль должен быть не менее 5 латинских символов"
                    result = false
                }
            }
            if (ExtractUtil.v(eText) == null) {
                eText.error = "Требуется заполнение поля"
                result = false
            }
        }
        return result
    }

    fun returnToMenu(view: View?) {
        view?.findNavController()?.navigate(R.id.menuFragment)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun clearTextField(editText: TextInputEditText?, context: Context) {
        editText?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_UP) {
                    val side = editText.right - editText.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                    if (event.rawX >= side) {
                        editText.setText("")
                    } else {
                        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
                        imm!!.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
                    }
                    return true
                }
                return false
            }
        })
    }
}