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
import kotlinx.android.synthetic.main.layout_transaction.view.*
import org.json.JSONArray

class TransactionAdapter (fragmentActivity: FragmentActivity, val dataSource: JSONArray) : RecyclerView.Adapter<TransactionAdapter.Holder>() {

    //    private val thiscontext : Context = context
    private val thiscontext : Context = fragmentActivity.baseContext
    private val thisActivity = fragmentActivity


    class Holder(view : View) : RecyclerView.ViewHolder(view) {
        private val View = view

        lateinit var userTextView: TextView
        lateinit var detailTextView: TextView

        fun Holder(){

            userTextView = View.findViewById<View>(R.id.username) as TextView
            detailTextView = View.findViewById<View>(R.id.text) as TextView

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

        holder.userTextView.setText( dataSource.getJSONObject(position).getString("username").toString() )
        holder.detailTextView.setText( dataSource.getJSONObject(position).getString("text").toString() )



    }





}

