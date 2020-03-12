package com.example.projectmymoney.pages


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide

import com.example.projectmymoney.R
import com.facebook.login.LoginManager

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    var PhotoURL : String = ""
    var Name : String = ""
    var Email : String = ""

    companion object {
        fun newInstance() = HomeFragment()
    }
    fun newInstance(url: String,name : String,email : String): HomeFragment {
        var Instance = HomeFragment()
        val bundle = Bundle()
        bundle.putString("PhotoURL", url)
        bundle.putString("Name", name)
        bundle.putString("Email", email)
        Instance.setArguments(bundle)
        return Instance
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        var btn_transaction: ImageButton = view.findViewById<ImageButton>(R.id.btn_transaction)
        var btn_report: ImageButton = view.findViewById<ImageButton>(R.id.btn_report)

        var txt_transaction: TextView = view.findViewById<TextView>(R.id.txt_transaction)
        var txt_report: TextView = view.findViewById<TextView>(R.id.txt_report)

        viewModel.text_btn_transaction.observe(this, Observer {
            txt_transaction.text = it
        })
        viewModel.text_btn_report.observe(this, Observer {
            txt_report.text = it
        })
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        val view_ProfilePicture = view.findViewById(R.id.view_profile) as ImageView
        val view_name = view.findViewById(R.id.view_name) as TextView
        val view_Email = view.findViewById(R.id.view_email) as TextView
        val btn_logout = view.findViewById(R.id.view_btn_logout) as Button

        Glide.with(activity!!.baseContext)
            .load(PhotoURL)
            .into(view_ProfilePicture)

        view_name.setText(Name)
        view_Email.setText(Email)
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        val fm = fragmentManager
        val transaction : FragmentTransaction = fm!!.beginTransaction()

        btn_transaction!!.setOnClickListener{
            val transaction_Fragment = TransactionFragment()
            transaction.replace(R.id.contentContainer, transaction_Fragment,"fragment_transaction")
            transaction.addToBackStack("fragment_transaction")
            transaction.commit()
        }

        btn_report!!.setOnClickListener{
            val report_Fragment = ReportFragment()
            transaction.replace(R.id.contentContainer, report_Fragment,"fragment_report")
            transaction.addToBackStack("fragment_report")
            transaction.commit()
        }

        btn_logout.setOnClickListener{
            LoginManager.getInstance().logOut()
            activity!!.supportFragmentManager.popBackStack()
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            PhotoURL = bundle.getString("PhotoURL").toString()
            Name = bundle.getString("Name").toString()
            Email = bundle.getString("Email").toString()

        }
    }


}
