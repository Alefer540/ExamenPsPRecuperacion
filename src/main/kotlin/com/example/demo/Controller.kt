package com.example.demo

import com.sun.net.httpserver.Authenticator.Success
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller (
    private val mensajeRepository: MensajeRepository,
    private val usuarioRepository: UsuarioRepository,
    private val adminsRepository:AdminsRepository
) {

    //curl --request POST  --header "Content-type:application/json; charset=utf-8" --data "{\"nombre\":\"Alejandro\",\"pass\":\"123\"}" localhost:8084/crearUsuario
    @PostMapping("crearUsuario")
    fun crearUsuario(@RequestBody datosUsuario: Usuario): Any {
        usuarioRepository.findAll().forEach {
            if (it.nombre == datosUsuario.nombre) {
                if (it.pass == datosUsuario.pass) {
                    return it.token
                } else {
                    return Error(1, "Pass invalida")
                }
            }
        }
        usuarioRepository.save(datosUsuario)
        return MostrarResultados(datosUsuario.id, datosUsuario.token)

    }

    //curl --request POST  --header "Content-type:application/json" --data "{\"id\":1,\"token\":75435878584359247627,\"texto\":\"Mensaje para el usuario 1\"}" localhost:8084/crearMensaje
    @PostMapping("crearMensaje")
    fun crearUsuario(@RequestBody datosMensaje: Mensaje): Any {
        usuarioRepository.findAll().forEach {
            if (it.id == datosMensaje.id) {
                if (it.token == datosMensaje.token) {
                        mensajeRepository.save(datosMensaje)

                    }
                else {
                    return Error(3, "Token Inválido")
                }
            } else {
                return Error(2, "Usuario inexistente")
            }
        }
        return "Success"
    }
   //curl -v localhost:8084/descargarMensajes
    @GetMapping("descargarMensajes")
    fun descargarMensajes(): Any {
        vaciarLista()
        mensajeRepository.findAll().forEach {
            Lista.list.add(it)
        }
        return Lista
    }

    //curl --request GET  --header "Content-type:application/json" --data "75435878584359247627" localhost:8084/descargarMensajesPorToken
    //NO SABIA REALIZARLO MEDIANTE ESA REQUEST ENTONCES PROBE CON ESTE METODO,
    //HE MODIFICADO LA REQUEST PARA QUE PIDA UN STRING COMO EN EL CASO POSTERIOR y HACER EL FILTRADO SOBRE EL MISMO
    //NO SE MUY BIEN PERO AL REALIZARLO MEDIANTE EL REQUESTBODY tokenUsuario:Usuario no podia conectarlo
    //MEDIANTE ESTE METODO LO HACE CORRECTAMENTE


    @GetMapping("descargarMensajesPorToken")
    fun descargarMensajesPorToken(@RequestBody tokenUsuario:String):Any{
        vaciarLista()
        mensajeRepository.findAll().forEach {
            if(it.token.contains(tokenUsuario))
                Lista.list.add(it)
        }
        return Lista
    }
    //curl --request DELETE --header "Content-type:application/json" --data "75435878584359247627" localhost:8084/borrarMensajePorToken
    @DeleteMapping("borrarMensajePorToken")
    fun borrarMensajePorToken(@RequestBody token:String):Any {
        var cont=0
        usuarioRepository.findAll().forEach {
            if (it.token == token) {

                mensajeRepository.deleteAll(mensajeRepository.findAll().filter { it.token == token })

                return "se han borrado los mensajes"
            }

        }
        return Error(3, "Token inesixtente")
    }



        fun vaciarLista() {
            Lista.list.clear()
        }

}







