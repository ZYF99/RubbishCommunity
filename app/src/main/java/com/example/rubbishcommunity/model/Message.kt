package com.example.rubbishcommunity.model


import java.io.Serializable

class Message(var iconUrl: String, var title: String, var msg: String, var time: String) : Serializable {

    override fun toString(): String {
        return "Message{" +
                "iconUrl='" + iconUrl + '\''.toString() +
                ", toolbarTitle='" + title + '\''.toString() +
                ", msg='" + msg + '\''.toString() +
                ", time='" + time + '\''.toString() +
                '}'.toString()
    }
}
