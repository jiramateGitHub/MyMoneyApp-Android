package com.example.projectmymoney.pages


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog.show
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
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

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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
        val view_transaction_date = view.findViewById<TextView>(R.id.view_text_date)
        val view_transaction_amount = view.findViewById<TextView>(R.id.view_input_amount)
        val view_transaction_category_type = view.findViewById<RadioGroup>(R.id.view_radio_type)
        val view_text_header = view.findViewById<TextView>(R.id.text_header)
        val btn_back = view.findViewById(R.id.view_btn_back) as ImageButton
        val btn_delete = view.findViewById(R.id.view_btn_delete) as ImageButton

        val dateTime = LocalDateTime.now()
        var category_type = ""

        if(Str_key != ""){
            view_transaction_category.setText(value_transaction.categories_name)
            view_transaction_note.setText(value_transaction.transaction_note)
            view_transaction_date.setText("Date: " + value_transaction.transaction_date)
            view_transaction_amount.setText(value_transaction.transaction_amount)
            if(value_transaction.categories_type == "income"){
                view_transaction_category_type.check(R.id.radio_income)
            }else{
                view_transaction_category_type.check(R.id.radio_expense)
            }
            view_text_header.setText("Update Transaction")
        }else{
            view_transaction_date.setText("Date: " + dateTime.format(DateTimeFormatter.ofPattern("y-M-d")))
            view_transaction_category_type.check(R.id.radio_income)
            category_type = "income"
            view_text_header.setText("Add Transaction")
            btn_delete.setVisibility(View.GONE)
        }

0
        val btn4 = view.findViewById<Button>(R.id.btn4)

        //ประกาศตัวแปร DatabaseReference รับค่า Instance และอ้างถึง path ที่เราต้องการใน database
        val mRootRef = FirebaseDatabase.getInstance().getReference()

        //อ้างอิงไปที่ path ที่เราต้องการจะจัดการข้อมูล ตัวอย่างคือ users และ messages
        val mMessagesRef = mRootRef.child("transaction")

        view_transaction_category_type.setOnCheckedChangeListener{group, checkedId ->
            if(checkedId == R.id.radio_income){
                category_type = "income"
            }else if(checkedId == R.id.radio_expense){
                category_type = "expense"
            }
        }

        btn4.setOnClickListener {
            var check_insert = true
            if(view_transaction_amount.text.toString() == ""){
                check_insert = false
                Toast.makeText(activity!!.baseContext, "Please Enter Amount", Toast.LENGTH_SHORT).show()
            }else
            if(view_transaction_category.text.toString() == ""){
                check_insert = false
                Toast.makeText(activity!!.baseContext, "Please Enter Category", Toast.LENGTH_SHORT).show()
            }else
            if(view_transaction_note.text.toString() == ""){
                check_insert = false
                Toast.makeText(activity!!.baseContext, "Please Enter Note", Toast.LENGTH_SHORT).show()
            }else

            if(check_insert == true){
                if(Str_key == ""){
                    value_transaction = m_transaction(
                        value_transaction.username,
                        view_transaction_category.text.toString(),
                        category_type,
                        view_transaction_amount.text.toString(),
                        dateTime.format(DateTimeFormatter.ofPattern("y-M-d")),
                        view_transaction_note.text.toString()
                    )
                    mMessagesRef.push().setValue(value_transaction)
                    Toast.makeText(activity!!.baseContext, "Add Transaction Success.", Toast.LENGTH_SHORT).show()
                    activity!!.supportFragmentManager.popBackStack()
                }else{
                    mMessagesRef.child(Str_key).child("categories_name").setValue(view_transaction_category.text.toString())
                    mMessagesRef.child(Str_key).child("categories_type").setValue(category_type)
                    mMessagesRef.child(Str_key).child("transaction_amount").setValue(view_transaction_amount.text.toString())
                    mMessagesRef.child(Str_key).child("transaction_note").setValue(view_transaction_note.text.toString())

                    Toast.makeText(activity!!.baseContext, "Update Transaction Successful.", Toast.LENGTH_SHORT).show()
                    activity!!.supportFragmentManager.popBackStack()
                }

            }
        }

        btn_delete.setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
            builder.setMessage("Confirm to delete?")
            builder.setPositiveButton("Delete",
                DialogInterface.OnClickListener { dialog, id ->
                    val mMessagesRef = mRootRef.child("transaction").child(Str_key)

                    mMessagesRef.setValue(null)

                    Toast.makeText(activity!!.baseContext, "Delete Transaction Successful.", Toast.LENGTH_SHORT).show()
                    val fm: FragmentManager = activity!!.getSupportFragmentManager()
                    fm.popBackStack("fragment_transaction", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                })
            builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which ->
                    //dialog.dismiss();
                })
            builder.show()
        }

        btn_back.setOnClickListener{
            LoginManager.getInstance().logOut()
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
