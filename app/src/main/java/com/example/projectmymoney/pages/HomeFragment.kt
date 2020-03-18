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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.projectmymoney.R
import com.example.projectmymoney.adapter.TransactionAdapter
import com.example.projectmymoney.login.LoginFragment
import com.facebook.login.LoginManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView

    var PhotoURL : String = ""
    var Username : String = ""
    var Name : String = ""

    companion object {
        fun newInstance() = HomeFragment()
    }
    fun newInstance(url: String,name : String,user : String): HomeFragment {
        var Instance = HomeFragment()
        val bundle = Bundle()
        bundle.putString("PhotoURL", url)
        bundle.putString("Name", name)
        bundle.putString("Username", user)
        Instance.setArguments(bundle)
        return Instance
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        val mRootRef = FirebaseDatabase.getInstance().reference
        val mMessagesRef = mRootRef.child("transaction")

        mMessagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val list = JSONArray()
                recyclerView = view.findViewById(R.id.recyLayout)

                val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!.baseContext)
                recyclerView.layoutManager = layoutManager

                for (ds in dataSnapshot.children) {

                    val jObject = JSONObject()

                    val username = ds.child("username").getValue(String::class.java)!!
                    val categories_name = ds.child("categories_name").getValue(String::class.java)!!
                    val categories_type = ds.child("categories_type").getValue(String::class.java)!!
                    val transaction_amount = ds.child("transaction_amount").getValue(String::class.java)!!
                    val transaction_date = ds.child("transaction_date").getValue(String::class.java)!!
                    val transaction_note = ds.child("transaction_note").getValue(String::class.java)!!

                    if(username == Username){
                        jObject.put("key",ds.key)
                        jObject.put("username",username)
                        jObject.put("categories_name",categories_name)
                        jObject.put("categories_type",categories_type)
                        jObject.put("transaction_amount",transaction_amount)
                        jObject.put("transaction_date",transaction_date)
                        jObject.put("transaction_note",transaction_note)

                        list.put(jObject)
                    }

                }

                val adapter = TransactionAdapter(activity!!,list,Username)

                recyclerView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

//        var txt_transaction: TextView = view.findViewById<TextView>(R.id.txt_transaction)
//        var txt_report: TextView = view.findViewById<TextView>(R.id.txt_report)
//
//        viewModel.text_btn_transaction.observe(this, Observer {
//            txt_transaction.text = it
//        })
//        viewModel.text_btn_report.observe(this, Observer {
//            txt_report.text = it
//        })
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        val view_ProfilePicture = view.findViewById(R.id.view_profile) as ImageView
        val view_name = view.findViewById(R.id.view_name) as TextView
        val btn_logout = view.findViewById(R.id.view_btn_logout) as ImageButton

        Glide.with(activity!!.baseContext)
            .load(PhotoURL)
            .into(view_ProfilePicture)

        view_name.setText(Name)

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        val fm = fragmentManager
        val transaction : FragmentTransaction = fm!!.beginTransaction()
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        var btn_transaction: Button = view.findViewById<Button>(R.id.view_btn_transaction)
        var btn_report: Button = view.findViewById<Button>(R.id.view_btn_tab_report)

        btn_transaction!!.setOnClickListener{
            val transaction_Fragment = TransactionFragment().newInstance("", "", "", "", "", "", Username)
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
            val LoginFragment = LoginFragment()
            transaction.replace(R.id.contentContainer, LoginFragment,"fragment_login")
            transaction.addToBackStack("fragment_login")
            transaction.commit()
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
            Username = bundle.getString("Username").toString()
            Name = bundle.getString("Name").toString()

        }
    }


}
