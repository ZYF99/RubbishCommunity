package com.example.fenrir_stage4.utils


import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser

import java.util.ArrayList

class SharedPreferencesUtil private constructor(context: Context, name: String) {

    init {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    companion object {

        private var util: SharedPreferencesUtil? = null
        private lateinit var sp: SharedPreferences

        fun getInstance(context: Context, name: String) {
            if (util == null) {
                util =
                    SharedPreferencesUtil(context, name)
            }
        }

        fun putData(key: String, value: Any): Boolean {
            var result: Boolean
            val editor = sp.edit()
            val type = value.javaClass.simpleName
            try {
                when (type) {
                    "Boolean" -> editor.putBoolean(key, value as Boolean)
                    "Long" -> editor.putLong(key, value as Long)
                    "Float" -> editor.putFloat(key, value as Float)
                    "String" -> editor.putString(key, value as String)
                    "Integer" -> editor.putInt(key, value as Int)
                    else -> {
                        val gson = Gson()
                        val json = gson.toJson(value)
                        editor.putString(key, json)
                    }
                }
                result = true
            } catch (e: Exception) {
                result = false
                e.printStackTrace()
            }

            editor.apply()
            return result
        }

        fun getData(key: String, defaultValue: Any): Any? {
            var result: Any?
            val type = defaultValue.javaClass.simpleName
            try {
                when (type) {
                    "Boolean" -> result = sp.getBoolean(key, defaultValue as Boolean)
                    "Long" -> result = sp.getLong(key, defaultValue as Long)
                    "Float" -> result = sp.getFloat(key, defaultValue as Float)
                    "String" -> result = sp.getString(key, defaultValue as String)
                    "Integer" -> result = sp.getInt(key, defaultValue as Int)
                    else -> {
                        val gson = Gson()
                        val json = sp.getString(key, "")
                        if (json != "" && json!!.isNotEmpty()) {
                            result = gson.fromJson(json, defaultValue.javaClass)
                        } else {
                            result = defaultValue
                        }
                    }
                }
            } catch (e: Exception) {
                result = null
                e.printStackTrace()
            }

            return result
        }

        fun <T : Any> putListData(key: String, list: MutableList<T>): Boolean {
            var result: Boolean
            var type = "Boolean"
            if(list.isNotEmpty()){
                type = list[0].run { javaClass.simpleName }
            }
            val editor = sp.edit()
            val array = JsonArray()
            try {
                when (type) {
                    "Boolean" -> for (i in list.indices) {
                        array.add(list[i] as Boolean)
                    }
                    "Long" -> for (i in list.indices) {
                        array.add(list[i] as Long)
                    }
                    "Float" -> for (i in list.indices) {
                        array.add(list[i] as Float)
                    }
                    "String" -> for (i in list.indices) {
                        array.add(list[i] as String)
                    }
                    "Integer" -> for (i in list.indices) {
                        array.add(list[i] as Int)
                    }
                    else -> {
                        val gson = Gson()
                        for (i in list.indices) {
                            val obj = gson.toJsonTree(list[i])
                            array.add(obj)
                        }
                    }
                }
                editor.putString(key, array.toString())
                result = true
            } catch (e: Exception) {
                result = false
                e.printStackTrace()
            }

            editor.apply()


            return result
        }

        fun <T> getListData(key: String, cls: Class<T>): MutableList<T> {
            val list = ArrayList<T>()
            val json = sp.getString(key, "")
            if (!(json == "" || json!!.isEmpty())) {
                val gson = Gson()
                val array = JsonParser().parse(json).asJsonArray
                for (elem in array) {
                    list.add(gson.fromJson(elem, cls))
                }
            }
            return list
        }
    }


}