package com.example.projectmymoney.pages

import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.projectmymoney.R
import com.example.projectmymoney.adapter.TransactionAdapter
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject

class ReportFragment : Fragment() {
    lateinit var Pie_id : PieChart
    var Username = ""
    var Income : Int = 0
    var Expense : Int = 0
    companion object {
        fun newInstance() = ReportFragment()
    }

    fun newInstance(user : String): ReportFragment {
        var Instance = ReportFragment()
        val bundle = Bundle()
        bundle.putString("Username", user)
        Instance.setArguments(bundle)
        return Instance
    }

    private lateinit var viewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        val fm = fragmentManager
        val transaction : FragmentTransaction = fm!!.beginTransaction()

        val mRootRef = FirebaseDatabase.getInstance().reference
        val mMessagesRef = mRootRef.child("transaction")

        mMessagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val list = JSONArray()

                Income = 0
                Expense = 0

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

                        if(categories_type == "income"){
                            Income += transaction_amount.toInt()
                        }else{
                            Expense += transaction_amount.toInt()
                        }
                    }

                }

                Pie_id = view.findViewById(R.id.pie_id)
                Pie_Chart(Pie_id,Income,Expense)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        var btn_home : Button = view.findViewById<Button>(R.id.view_btn_tab_transaction)

        btn_home!!.setOnClickListener{
            activity!!.supportFragmentManager.popBackStack()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ReportViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            Username = bundle.getString("Username").toString()
        }
    }

    fun Pie_Chart( chart: PieChart, balance_income : Int, balance_expense : Int){
        //ปิด Description
        chart.description.isEnabled = false

        val entries: ArrayList<PieEntry> = ArrayList()
            entries.add(PieEntry(balance_income.toFloat(), "Income"))
            entries.add(PieEntry(balance_expense.toFloat(), "Expense"))

        val dataset = PieDataSet(entries, "Balance")

        //กำหนดให้มีช่องว่างตรงกลางได้
        dataset.selectionShift = 10f
        dataset.valueTextSize = 5f

        //ตั้งค่า color
        dataset.setColors(*ColorTemplate.COLORFUL_COLORS) // set the color

        //เซ้ทช่องว่างความห่างของข้อมูล
        dataset.setSliceSpace(3f)

        //กำหนดข้อมูล
        val data = PieData(dataset)
        chart.setData(data)

        //กำหนดให้มีช่องว่างตรงกลางได้
        chart.setHoleRadius(30F)
        chart.setTransparentCircleRadius(40F)

        //กำหนด animation
        chart.animateY(3000)

        //อาตัวหนังสือออกมาไว้ข้างนอกตัวแผนภูมิ
        //X คือ ชื่อข้อมูล
        //Y คือ ค่าของข้อมูล
//        dataset.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE)
        dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE)

        //เส้นที่โยงออกมา
        dataset.setValueLinePart1Length(0.5f)
        dataset.setValueLinePart2Length(0.5f)

        //กำหนดให้แสดงเป็น %
        chart.setUsePercentValues(true)
        dataset.setValueFormatter(PercentFormatter(chart))

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE)

        //ข้อความตรงกลางแผนภูมิ
        chart.setCenterText("My Balance");
        chart.setCenterTextSize(5F)

    }

}
