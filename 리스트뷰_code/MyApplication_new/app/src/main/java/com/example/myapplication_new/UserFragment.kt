package com.example.myapplication_new

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication_new.databinding.FragmentUserBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


data class MyFriend(val name:String, val contents:String, var checked: Boolean)

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    var myFriendList = ArrayList<MyFriend>()
    var favoriteList = ArrayList<MyFriend>()
    var listNum = ArrayList<Int>()


    private lateinit var customAdapter: CustomAdapter
    private lateinit var favorAdapter: CustomAdapter
    lateinit var binding: FragmentUserBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentUserBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(layoutInflater)
        val view = binding.root

        myFriendList.add(MyFriend("홍길동","Hi", false))
        myFriendList.add(MyFriend("이순신","거북이", false))
        myFriendList.add(MyFriend("신사임당","50000", false))
        myFriendList.add(MyFriend("세종대왕","하이루", false))
        myFriendList.add(MyFriend("광개토대왕","만주", false))
        myFriendList.add(MyFriend("허준","동의~보감!", false))
        myFriendList.add(MyFriend("을지문덕","살수", false))
        myFriendList.add(MyFriend("주몽","슝슝", false))
        myFriendList.add(MyFriend("온달","바보", false))
        myFriendList.add(MyFriend("산타","크리스마스", false))
        myFriendList.add(MyFriend("스파이더맨","거미", false))
        myFriendList.add(MyFriend("아이언맨","로봇", false))
        myFriendList.add(MyFriend("루돌프","빨간코", false))

        customAdapter = context?.let { CustomAdapter(it,myFriendList) }!!
        binding.listViewUser.adapter = customAdapter


        //binding.listViewUser.setOnClickListener {
        //    val line = myFriendList[i]
        //    favoriteList.add(line)
         //   Log.v("*********", "line ")
        //    val intent = Intent(context, FriendActivity::class.java)
        //    intent.putExtra("lineInfo",line.toString())
        //    startActivity(intent)
        //}

        return view
    }

    override fun onResume() {
        super.onResume()
        binding.btnAdd.setOnClickListener {
            var num = 0
            for(i in myFriendList){
                if(myFriendList[num].checked == true){
                    if(!listNum.contains(num)){
                        favoriteList.add(myFriendList[num])
                        listNum.add(num)
                    }
                }
                num++
            }
            if(listNum.size != 0) {
                num = listNum.size - 1
                for (i in num downTo 0) {
                    if (favoriteList[i].checked == false) {
                        //var dataNum = listNum[num]
                        Log.v("*********", "num " + i + " ")
                        favoriteList.removeAt(i)
                        listNum.removeAt(i)
                    }
                }
            }
            favorAdapter =  context?.let { CustomAdapter(it,favoriteList) }!!
            binding.listViewFavor.adapter = favorAdapter
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

