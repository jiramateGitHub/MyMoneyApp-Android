package com.example.projectmymoney.login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.projectmymoney.R
import com.example.projectmymoney.adapter.TransactionAdapter
import com.example.projectmymoney.pages.HomeFragment
import com.example.projectmymoney.pages.TransactionFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {
    private lateinit var value_account : m_account

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =   inflater.inflate(R.layout.fragment_register, container, false)

        val fm = fragmentManager
        val transaction : FragmentTransaction = fm!!.beginTransaction()

        val mRootRef = FirebaseDatabase.getInstance().getReference()
        val mMessagesRef = mRootRef.child("account")

        val list = JSONArray()

        mMessagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children) {

                    val jObject = JSONObject()

                    val name = ds.child("account_name").getValue(String::class.java)!!
                    val username = ds.child("username").getValue(String::class.java)!!
                    val password = ds.child("password").getValue(String::class.java)!!

                    jObject.put("key",ds.key)
                    jObject.put("account_name",name)
                    jObject.put("username",username)
                    jObject.put("password",password)

                    list.put(jObject)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        var name = view.findViewById<EditText>(R.id.editName) as  EditText
        var username = view.findViewById<EditText>(R.id.editUsername) as EditText
        var password = view.findViewById<EditText>(R.id.editPassword) as EditText
        var signup = view.findViewById<Button>(R.id.signup) as Button
        var signin = view.findViewById<TextView>(R.id.text_signin) as TextView

        signup.setOnClickListener{
            var validate = true
            var check_username = true

            if(name.text.toString() == ""){
                validate = false
                Toast.makeText(activity!!.baseContext, "Please enter name.", Toast.LENGTH_SHORT).show()
            }else if(username.text.toString() == ""){
                validate = false
                Toast.makeText(activity!!.baseContext, "Please enter username.", Toast.LENGTH_SHORT).show()
            }else if(password.text.toString() == ""){
                validate = false
                Toast.makeText(activity!!.baseContext, "Please enter password.", Toast.LENGTH_SHORT).show()
            }

            if(validate == true){
                for (i in 0 until list.length()) {
                    if(list.getJSONObject(i).getString("username").toString() == username.text.toString()){
                        check_username = false
                    }
                    // use item
                }

                if(check_username == true){
                    value_account = m_account(
                        name.text.toString(),
                        username.text.toString(),
                        password.text.toString()
                    )

                    mMessagesRef.push().setValue(value_account)
                    Toast.makeText(activity!!.baseContext, "Sign up Successful.", Toast.LENGTH_SHORT).show()

                    var url_img = "https://previews.123rf.com/images/afe207/afe2071306/afe207130600098/20416813-male-profile-picture.jpg"
                    val Home_Fragment = HomeFragment().newInstance(url_img,name!!.text.toString(),username!!.text.toString()+"@gmail.com")
                    transaction.replace(R.id.contentContainer, Home_Fragment,"fragment_home")
                    transaction.addToBackStack("fragment_home")
                    transaction.commit()
                }else{
                    Toast.makeText(activity!!.baseContext, "Username duplicate.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        signin.setOnClickListener{
            val LoginFragment = LoginFragment()
            transaction.replace(R.id.contentContainer, LoginFragment,"fragment_login")
            transaction.addToBackStack("fragment_login")
            transaction.commit()
        }


        return view
    }

    data class m_account(
        var account_name: String? = "",
        var username: String? = "",
        var password: String? = ""
    )


}
