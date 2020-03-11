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

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private var username : EditText? = null
    private var password : EditText? = null
    private var signin : Button? = null

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

        username = view.findViewById<EditText>(R.id.editUsername)
        password = view.findViewById<EditText>(R.id.editPassword)
        signin = view.findViewById<Button>(R.id.signin)

        signin!!.setOnClickListener{
            Toast.makeText(context,"Hello " + username!!.text.toString(), Toast.LENGTH_SHORT).show()
//            val fragmentListView = profile().newInstance(username!!.text.toString(),password!!.text.toString())
            var url_img = "https://previews.123rf.com/images/afe207/afe2071306/afe207130600098/20416813-male-profile-picture.jpg"

            val fm = fragmentManager
            val transaction : FragmentTransaction = fm!!.beginTransaction()

            val Home_Fragment = HomeFragment().newInstance(url_img,username!!.text.toString(),username!!.text.toString()+"@gmail.com")
            transaction.replace(R.id.contentContainer, Home_Fragment,"fragment_home")
            transaction.addToBackStack("fragment_home")
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

                val Home_Fragment = HomeFragment()
                    .newInstance(user!!.photoUrl.toString(),user!!.displayName.toString(),user!!.email.toString())
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

