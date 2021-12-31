package com.example.myapplication_new

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.myapplication_new.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {



    private val chatFragment = ChatFragment()
    private val etcFragment=EtcFragment()
    private val newsFragment=NewsFragment()
    private val userFragment=UserFragment()


    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, UserFragment()).commit()

        replaceFragment(userFragment)

        binding= ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        supportActionBar?.hide()

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.user-> replaceFragment(userFragment)
                R.id.chat -> replaceFragment(chatFragment)
                R.id.news -> replaceFragment(newsFragment)
                R.id.etc -> replaceFragment(etcFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        if(fragment != null){
            val transaction: FragmentTransaction =supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}