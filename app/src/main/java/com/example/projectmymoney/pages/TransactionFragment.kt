package com.example.projectmymoney.pages


import android.app.DatePickerDialog
import android.app.ProgressDialog.show
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import com.example.projectmymoney.R
import com.example.projectmymoney.adapter.TransactionAdapter
import com.example.projectmymoney.pages.HomeFragment.Companion.newInstance
import com.example.projectmymoney.pages.ReportFragment.Companion.newInstance
import com.facebook.login.LoginManager
import com.facebook.share.widget.ShareDialog.show
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_transaction.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.math.log

/**
 * A simple [Fragment] subclass.
 */
class TransactionFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var value_transaction : m_transaction
    var Str_key = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)
        // Inflate the layout for this fragment


        val view_transaction_category = view.findViewById<TextView>(R.id.view_input_categories)
        val view_transaction_note = view.findViewById<TextView>(R.id.view_input_note)
        val view_transaction_date = view.findViewById<TextView>(R.id.tvDate)
        val view_transaction_amount = view.findViewById<TextView>(R.id.view_input_amount)

        view_transaction_category.setText(value_transaction.categories_name)
        view_transaction_note.setText(value_transaction.transaction_note)
        view_transaction_date.setText(value_transaction.transaction_date)
        view_transaction_amount.setText(value_transaction.transaction_amount)

        val btn4 = view.findViewById<Button>(R.id.btn4)

        //ประกาศตัวแปร DatabaseReference รับค่า Instance และอ้างถึง path ที่เราต้องการใน database
        val mRootRef = FirebaseDatabase.getInstance().getReference()

        //อ้างอิงไปที่ path ที่เราต้องการจะจัดการข้อมูล ตัวอย่างคือ users และ messages
        val mMessagesRef = mRootRef.child("transaction")

        btn4.setOnClickListener {
            value_transaction = m_transaction(
                "60160157",
                "Salary",
                "income",
                "5000",
                "2563-3-12",
                "no comment"
            )
            mMessagesRef.push().setValue(value_transaction)

            activity!!.supportFragmentManager.popBackStack()
        }


        return view

    }

    data class m_transaction(
        var username: String? = "",
        var categories_name: String? = "",
        var categories_type: String? = "",
        var transaction_amount: String? = "",
        var transaction_date: String? = "",
        var transaction_note: String? = ""
    )

    fun newInstance(key: String,categories_name: String,categories_type:String,transaction_amount:String,transaction_date:String,transaction_note:String ,username:String): TransactionFragment {
        val fragment = TransactionFragment()
        val bundle = Bundle()
        bundle.putString("key", key)
        bundle.putString("categories_name", categories_name)
        bundle.putString("categories_type", categories_type)
        bundle.putString("transaction_amount", transaction_amount)
        bundle.putString("transaction_date", transaction_date)
        bundle.putString("transaction_note", transaction_note)
        bundle.putString("username", username)
        fragment.setArguments(bundle)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        if (bundle != null) {
            value_transaction = m_transaction(
                bundle.getString("username").toString(),
                bundle.getString("categories_name").toString(),
                bundle.getString("categories_type").toString(),
                bundle.getString("transaction_amount").toString(),
                bundle.getString("transaction_date").toString(),
                bundle.getString("transaction_note").toString()
            )
            Str_key = bundle.getString("key").toString()
        }
    }

}
