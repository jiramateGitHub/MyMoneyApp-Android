package com.example.projectmymoney.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmymoney.R
import com.example.projectmymoney.pages.TransactionFragment
import kotlinx.android.synthetic.main.layout_transaction.view.*
import org.json.JSONArray

class TransactionAdapter (fragmentActivity: FragmentActivity, val dataSource: JSONArray) : RecyclerView.Adapter<TransactionAdapter.Holder>() {

    //    private val thiscontext : Context = context
    private val thiscontext : Context = fragmentActivity.baseContext
    private val thisActivity = fragmentActivity


    class Holder(view : View) : RecyclerView.ViewHolder(view) {
        private val View = view
        lateinit var layout : LinearLayout
        lateinit var view_transaction_category: TextView
        lateinit var view_transaction_note: TextView
        lateinit var view_transaction_date: TextView
        lateinit var view_transaction_amount: TextView

        fun Holder(){
            layout = View.findViewById<View>(R.id.recyview_layout) as LinearLayout
            view_transaction_category = View.findViewById<View>(R.id.view_transaction_category) as TextView
            view_transaction_note = View.findViewById<View>(R.id.view_transaction_note) as TextView
            view_transaction_date = View.findViewById<View>(R.id.view_transaction_date) as TextView
            view_transaction_amount = View.findViewById<View>(R.id.view_transaction_amount) as TextView

        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.layout_transaction, parent, false))
    }


    override fun getItemCount(): Int {
        return dataSource.length()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.Holder()

        holder.view_transaction_category.setText( dataSource.getJSONObject(position).getString("categories_name").toString() )
        holder.view_transaction_note.setText( dataSource.getJSONObject(position).getString("transaction_note").toString() )
        holder.view_transaction_date.setText( dataSource.getJSONObject(position).getString("transaction_date").toString() )
        holder.view_transaction_amount.setText( dataSource.getJSONObject(position).getString("transaction_amount").toString() )

        holder.layout.setOnClickListener {
            var key = dataSource.getJSONObject(position).getString("key").toString()
            var categories_name = dataSource.getJSONObject(position).getString("categories_name").toString()
            var categories_type = dataSource.getJSONObject(position).getString("categories_type").toString()
            var transaction_amount = dataSource.getJSONObject(position).getString("transaction_amount").toString()
            var transaction_date = dataSource.getJSONObject(position).getString("transaction_date").toString()
            var transaction_note = dataSource.getJSONObject(position).getString("transaction_note").toString()
            var username = dataSource.getJSONObject(position).getString("username").toString()


            val fm = thisActivity.supportFragmentManager
            val transaction: FragmentTransaction = fm!!.beginTransaction()
            val transaction_Fragment = TransactionFragment().newInstance(key, categories_name, categories_type, transaction_amount, transaction_date, transaction_note, username)
            transaction.replace(R.id.contentContainer, transaction_Fragment,"fragment_transaction")
            transaction.addToBackStack("fragment_transaction")
            transaction.commit()
        }

    }





}

