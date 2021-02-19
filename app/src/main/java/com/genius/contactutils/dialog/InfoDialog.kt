package com.genius.contactutils.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.genius.contactutils.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Created by geniuS on 19/02/2021.
 */
class InfoDialog(val callback: InfoDialogCallback) : BottomSheetDialogFragment() {

    private lateinit var continueBtn: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        val view: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_asking_permissions, null)

        continueBtn = view.findViewById(R.id.continue_btn)
        continueBtn.setOnClickListener {
            dialog.cancel()
            callback.onContinue()
        }

        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }

    public interface InfoDialogCallback {
        fun onContinue()
    }
}