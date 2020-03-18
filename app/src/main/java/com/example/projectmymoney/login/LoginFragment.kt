package com.example.projectmymoney.login


import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction

import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.projectmymoney.R
import com.example.projectmymoney.pages.HomeFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    var user : FirebaseUser? = null
    lateinit var facebookSignInButton : LoginButton
    var callbackManager : CallbackManager? = null
    // Firebase Auth Object.
    var firebaseAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =  inflater.inflate(R.layout.fragment_login, container, false)

        val fm = fragmentManager
        val transaction : FragmentTransaction = fm!!.beginTransaction()

        val username = view.findViewById<EditText>(R.id.editUsername) as EditText
        val password = view.findViewById<EditText>(R.id.editPassword) as EditText
        val signin = view.findViewById<Button>(R.id.signin) as Button
        val signup = view.findViewById<TextView>(R.id.text_signup) as TextView

        val mRootRef = FirebaseDatabase.getInstance().getReference()
        val mMessagesRef = mRootRef.child("account")

        val list = JSONArray()

        mMessagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children) {

                    val jObject = JSONObject()

                    val temp_name = ds.child("account_name").getValue(String::class.java)!!
                    val temp_username = ds.child("username").getValue(String::class.java)!!
                    val temp_password = ds.child("password").getValue(String::class.java)!!

                    jObject.put("key",ds.key)
                    jObject.put("account_name",temp_name)
                    jObject.put("username",temp_username)
                    jObject.put("password",temp_password)

                    list.put(jObject)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        signin!!.setOnClickListener{
            var validate = true
            if(username.text.toString() == ""){
                validate = false
                Toast.makeText(activity!!.baseContext, "Please enter username.", Toast.LENGTH_SHORT).show()
            }else if(password.text.toString() == ""){
                validate = false
                Toast.makeText(activity!!.baseContext, "Please enter password.", Toast.LENGTH_SHORT).show()
            }

            if(validate == true){
                var check_username = false
                var check_password = false
                var account_name = ""
                for (i in 0 until list.length()) {
                    if(list.getJSONObject(i).getString("username").toString() == username.text.toString()){
                        check_username = true
                        if(list.getJSONObject(i).getString("password").toString() == password.text.toString()){
                            check_password = true
                            account_name = list.getJSONObject(i).getString("account_name").toString()
                            break
                        }
                    }
                    // use item
                }

                if(check_username == true && check_password == true){
                    Toast.makeText(context,"Welcome " + account_name!!.toString(), Toast.LENGTH_SHORT).show()

                    var url_img = "https://previews.123rf.com/images/afe207/afe2071306/afe207130600098/20416813-male-profile-picture.jpg"

                    val Home_Fragment = HomeFragment().newInstance(url_img, account_name!!.toString(),username!!.text.toString())
                    transaction.replace(R.id.contentContainer, Home_Fragment,"fragment_home")
                    transaction.addToBackStack("fragment_home")
                    transaction.commit()
                }else{
                    Toast.makeText(context,"Username or Password Incorrect.", Toast.LENGTH_SHORT).show()
                }

            }
        }

        signup!!.setOnClickListener{
            val RegisterFragment = RegisterFragment()
            transaction.replace(R.id.contentContainer, RegisterFragment,"fragment_register")
            transaction.addToBackStack("fragment_register")
            transaction.commit()
        }


        callbackManager = CallbackManager.Factory.create()
        facebookSignInButton  = view.findViewById(R.id.login_button) as LoginButton
        firebaseAuth = FirebaseAuth.getInstance()
        facebookSignInButton.setReadPermissions("email")

        // If using in a fragment
        facebookSignInButton.setFragment(this)

        val token: AccessToken?
        token = AccessToken.getCurrentAccessToken()

        if (token != null) { //Means user is not logged in
            LoginManager.getInstance().logOut()
        }

        // Callback registration
        facebookSignInButton.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) { // App code

                handleFacebookAccessToken(loginResult!!.accessToken)

            }
            override fun onCancel() { // App code
            }
            override fun onError(exception: FacebookException) { // App code
            }
        })

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) { // App code

                    handleFacebookAccessToken(loginResult!!.accessToken)

                }

                override fun onCancel() { // App code
                }

                override fun onError(exception: FacebookException) { // App code
                }
            })

        // Inflate the layout for this fragment
        return view;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FacebookSdk.sdkInitialize(FacebookSdk.getApplicationContext())
        AppEventsLogger.activateApp(activity!!.baseContext)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token : AccessToken) {
        Log.d(ContentValues.TAG, "handleFacebookAccessToken:" + token)
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth!!.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(ContentValues.TAG, "signInWithCredential:success")
                user = firebaseAuth!!.currentUser

                val Home_Fragment = HomeFragment().newInstance(user!!.photoUrl.toString(),user!!.displayName.toString(),user!!.email.toString())
                val fm = fragmentManager
                val transaction : FragmentTransaction = fm!!.beginTransaction()
                transaction.replace(R.id.contentContainer, Home_Fragment,"fragment_home")
                transaction.addToBackStack("fragment_home")
                transaction.commit()
            } else {
                // If sign in fails, display a message to the user.
                Log.w(ContentValues.TAG, "signInWithCredential:failure", task.getException())
                Toast.makeText(activity!!.baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}

