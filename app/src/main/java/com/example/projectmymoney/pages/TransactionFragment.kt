package com.example.projectmymoney.pages


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import com.example.projectmymoney.R
import com.example.projectmymoney.adapter.TransactionAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class TransactionFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)
        // Inflate the layout for this fragment

        val mRootRef = FirebaseDatabase.getInstance().reference
        val mMessagesRef = mRootRef.child("data")

        mMessagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val list = JSONArray()
                recyclerView = view.findViewById(R.id.recyLayout)

                val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!.baseContext)
                recyclerView.layoutManager = layoutManager

                for (ds in dataSnapshot.children) {

                    val jObject = JSONObject()

                    val username = ds.child("username").getValue(String::class.java)!!
                    val text = ds.child("text").getValue(String::class.java)!!

                    jObject.put("key",ds.key)
                    jObject.put("username",username)
                    jObject.put("text",text)

                    list.put(jObject)

                }

                val adapter = TransactionAdapter(activity!!,list)

                recyclerView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        return view

    }


}
