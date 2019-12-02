package com.example.rubbishcommunity.model.sharedpref

import com.chibatching.kotpref.KotprefModel
import com.example.rubbishcommunity.utils.Constants

object SharedPrefModel : KotprefModel() {
    override val kotprefName: String = Constants.SHARED_PREF_FILE_NAME
    var accessToken: String by stringPref()
    var tokenInfo : String by stringPref()
  

}