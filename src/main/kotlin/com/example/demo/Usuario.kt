package com.example.demo

import com.google.gson.Gson
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
@Entity
data class Usuario(  var nombre:String,var pass:String) {
    @Id
    @GeneratedValue
    var id=0
    var token = generarToken()

    fun generarToken(): String {
        var palabra = ""
        repeat(20) {
            val letra = 1..9
            val letras = letra.random()
            palabra += letras
        }

        return palabra
    }

    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}

@Entity
data class Admin(@Id var user:String,var pass:String){
    override fun toString(): String {
        val gson= Gson()
        return gson.toJson(this)
    }
}