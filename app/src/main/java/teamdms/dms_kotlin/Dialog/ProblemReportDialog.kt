package teamdms.dms_kotlin.Dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DialogFragment
import android.app.MediaRouteButton
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.RequiresApi
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_report_problem.*
import team_dms.dms.Base.Util
import team_dms.dms.Base.Util.getToken
import team_dms.dms.Connect.Connector
import team_dms.dms.Connect.Res
import teamdms.dms_kotlin.Fragment.NoticeMainFragment
import teamdms.dms_kotlin.R
import javax.crypto.spec.RC2ParameterSpec

/**
 * Created by dsm2017 on 2017-12-20.
 */

class ProblemReportDialog (context :Context) : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_report_problem)

        button_problem_dialog_cancle.setOnClickListener { dismiss() } // 기본 값 세팅
        checkValidate()

        edit_problem_dialog_content.addTextChangedListener(object : TextWatcher{ // 지속적으로 유효성 확인
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkValidate()
            }
        })

        edit_problem_dialog_title.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkValidate()
            }
        })

        edit_problem_dialog_roomnumber.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkValidate()
            }
        })
    }

    private fun checkValidate () { // 항목이 비어있는지 안비어있는지에 대해 체크한다

        val title : String = edit_problem_dialog_title.text.toString().trim()
        val room : String  =  edit_problem_dialog_roomnumber.text.toString().trim()
        val content : String = edit_problem_dialog_content.text.toString().trim()

       button_problem_dialog_report.setOnClickListener {

            if(!title.isEmpty() && !room.isEmpty() && !content.isEmpty()) {


                Connector.api.reportProblem(getToken(context), title, Integer.parseInt(room), content)
                        .enqueue(object : Res<Void> (context) {

                            override fun callBack(code: Int, body: Void?) {

                                when(code) {

                                    201 -> {
                                        Toast.makeText(context, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
                                        dismiss()
                                    }
                                    else -> {
                                        Toast.makeText(context, "신고가 접수에 실패했습니다.", Toast.LENGTH_SHORT).show()
                                        dismiss()
                                    }
                                }
                            }
                        })
            }
            else {

                Toast.makeText(context, "모두 다 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


