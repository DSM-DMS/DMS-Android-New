package teamdms.dms_kotlin.RecyclerAdapter

import android.content.*
import android.support.v7.widget.*
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.view_mypage_list_content.view.*
import team_dms.dms.Base.*
import team_dms.dms.Connect.*
import teamdms.dms_kotlin.*
import teamdms.dms_kotlin.Activity.*
import teamdms.dms_kotlin.Dialog.BugReportDialog
import teamdms.dms_kotlin.Fragment.*

/**
 * Created by root1 on 2017. 11. 30..
 */
class MyPageRecyclerAdapter(val fragment: MyPageFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context
    lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        inflater = LayoutInflater.from(parent?.context)
        context = parent!!.context

        return when (viewType) {
            0, 4, 7 -> {
                val view = inflater.inflate(R.layout.view_mypage_list_no_content, parent, false)
                MyPageListNoContentViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.view_mypage_list_content, parent, false)
                MyPageListContentViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position == 0 || position == 7 || position == 4) return

        val contentHolder = holder as MyPageListContentViewHolder
        val haveToken = !(Util.getToken(context) == "JWT ")

        when (position) {
            1 -> {
                if (haveToken) {
                    contentHolder.bind("로그아웃", { _ ->
                        Util.removeToken(context)
                        Util.showToast(context, "로그아웃 성공")
                        fragment.setStateData(null)
                        notifyDataSetChanged()
                    })
                } else {
                    contentHolder.bind("로그인", { _ ->
                        context.startActivity(Intent(context, SignInActivity::class.java))
                    })
                }
            }
            2 -> {
                contentHolder.bind("비밀번호 변경", { _ ->
                    if (haveToken) {
                        context.startActivity(Intent(context, ChangePWActivity::class.java))
                    } else {
                        Toast.makeText(context, "로그인 해주세요", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            3 -> {
                contentHolder.bind("상벌점 내역 조회", { _ ->
                    if (haveToken) {
                        context.startActivity(Intent(context, PointHistoryActivity::class.java))
                    } else {
                        Toast.makeText(context, "로그인 해주세요", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            5 -> contentHolder.bind("버그 신고", { _ ->
                Util.showCustomDialog(BugReportDialog(context), R.style.DialogSlideDialog)
            })
            6 -> contentHolder.bind("개발자 소개", { context.startActivity(Intent(context, IntroDeveloperActivity::class.java)) })
        }
    }

    override fun getItemCount(): Int = 8

    override fun getItemViewType(position: Int): Int = position

    private fun sendBugReport(message: String): Boolean {
        if (message.isEmpty()) return false
        Connector.api.sendBugReport(Util.getToken(context), hashMapOf("platform" to 2, "content" to message))
                .enqueue(object : Res<Void>(context) {
                    override fun callBack(code: Int, body: Void?) {
                        if (code == 201) {
                            Util.showToast(context, "전송 성공")
                        } else {
                            Util.showToast(context, "전송 실패 : $code")
                        }
                    }
                })
        return true
    }

}

class MyPageListNoContentViewHolder(view: View) : RecyclerView.ViewHolder(view)

class MyPageListContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    lateinit var rootView: View

    init {
        rootView = view
    }

    fun bind(title: String, onClick: (Any) -> Unit) {
        with(rootView) { text_mypage_list_title.text = title }
        rootView.setOnClickListener(onClick)
    }

}