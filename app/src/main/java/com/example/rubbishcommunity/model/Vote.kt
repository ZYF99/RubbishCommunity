package com.example.rubbishcommunity.model

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/7/31 20:23
 */
class Vote(content: String) {

    internal var uid: Int = 0
    var content: String
        internal set
    internal var date: String? = null


    init {
        this.content = content
    }

}

