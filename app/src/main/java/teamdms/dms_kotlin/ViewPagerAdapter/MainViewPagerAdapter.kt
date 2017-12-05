package teamdms.dms_kotlin.ViewPagerAdapter

import android.support.v4.app.*
import teamdms.dms_kotlin.Fragment.*

/**
 * Created by root1 on 2017. 11. 25..
 */
class MainViewPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> MealFragment()
//            1 ->
            2 -> NoticeMainFragment()
            else -> MyPageFragment()
        }
    }

    override fun getCount(): Int {
        return 4
    }

}