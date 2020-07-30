package com.example.zxt

import com.orhanobut.hawk.Hawk

var API_BASE_URL = if(Hawk.get<String>("IP").isNullOrBlank())  "http://120.79.2.255:8080" else Hawk.get<String>("IP")

const val KEY_TOKEN = "KEY_TOKEN"


