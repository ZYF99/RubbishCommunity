package com.example.rubbishcommunity.ui.adapter


import android.util.Base64
import com.example.rubbishcommunity.PersonOutClass
import java.util.*

object Test {
	
	@JvmStatic
	fun main(args: Array<String>) {
		val person =PersonOutClass.Person
			.newBuilder()
			.setName("Fenrir")
			.setAge(34)
			.setAge1(46)
			.setAge2(56)
			.setAge3(16)
			.setAge4(56)
			.setAge5(86)
			.setAge6(66)
			.setAge7(56)
			.setAge8(86)
			.setAge9(54)
			.setAge10(66)
			.setAge11(66)
			.build()
		//print(java.util.Base64.getDecoder().decode(person.toByteArray()))
		print(Arrays.toString(person.toByteArray()))
		
		//val person = PersonOutClass.Person.parseFrom(byteArrayOf(10, 6, 70, 101, 110, 114, 105, 114, 16, 56))
		//val array = person.toByteArray()
		
		//print(person)
		
	}
	
}
