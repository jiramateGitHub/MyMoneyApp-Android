package com.example.projectmymoney.pages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _text_btn_transaction = MutableLiveData<String>().apply {
        value = "Transaction"
    }
    val text_btn_transaction: LiveData<String> = _text_btn_transaction

    private val _text_btn_report = MutableLiveData<String>().apply {
        value = "Report"
    }
    val text_btn_report: LiveData<String> = _text_btn_report



}
