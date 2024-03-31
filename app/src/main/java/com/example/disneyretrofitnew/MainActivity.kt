package com.example.disneyretrofitnew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.disneyretrofitnew.R.id.textView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
    var lista = StringBuilder()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView=findViewById(R.id.textView)
        crearLista()
        textView.text = "prueba"
        textView.append(lista.toString())
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.disneyapi.dev/")
            .addConverterFactory(GsonConverterFactory.create()) //inicialización
            .build()
    }

    private fun escribirLista(data:ApiDisneyClass?) {

        data?.data?.forEach{
            lista.append(it.name).append("\n")
            // Si queremos incluir texto: lista.append("Personaje: ${it.name} \n")
        }
        //llamada en la corrutina desde la actividad dónde esté (runOnUiThread) para ejecutar en el hilo principal (ej:toast)



        /*la función anterior equivaldría a esta otra:
           fun escribirLista (valores: List<Data>?){
                val lista = StringBuilder()
                if (valores != null) {
                    valores.forEach { registro ->
                        lista.append("Personaje: ${registro.name} \n")
                    }
               }
            activity?.runOnUiThread {
                textView.text = stringBuilder.toString()
            }
           } */
    }


    fun crearLista(){
        // Las llamadas a internet se hacen en corutinas
        CoroutineScope(Dispatchers.IO).launch {
            //Lanzamos el hilo a través de la interfaz que hemos creado (ServicioApi) y la func conseguirLista de la misma
            val llamada = getRetrofit().create(ServicioApi::class.java).conseguirLista()
            if(llamada.isSuccessful){
                /*Necesitamos permiso de internet en el manifest, para poder acceder a la web de rutas*/
               escribirLista(llamada.body()) //podemos crear val data=llamada.body()?.data y escribirLista(data)
            }else{
                Log.i("TENGO LISTA:","NO")
            }
        }
    }

}