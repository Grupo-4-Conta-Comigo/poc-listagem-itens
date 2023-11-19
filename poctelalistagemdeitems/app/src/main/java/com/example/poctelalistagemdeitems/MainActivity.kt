package com.example.poctelalistagemdeitems

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.poctelalistagemdeitems.databinding.ActivityMainBinding
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Chame o método trustAllHosts() ao iniciar a atividade (ou em outro momento apropriado)
        SslConfig.trustAllHosts()

        testandoListagem()

    }

    private fun testandoListagem() {
        binding.btnListarItens.setOnClickListener {


            val get = ApiItens.criar().get()

            // Callback do pacote retrofit

            get.enqueue(object : Callback<List<Item>> {
                override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                    // pegando a lista de itens retornada pela API
                    val itens = response.body()!!

                    val transacao = supportFragmentManager.beginTransaction()

                    // iterando na lista de Itens
                    itens.forEach {
                        // Criando um FragmentContainerView em tempo de execução
                        val containerView = FragmentContainerView(baseContext)
                        containerView.id = View.generateViewId() // dando um id aleatório
                        binding.layoutItens.addView(containerView) // colocando ele na LinearLayout

                        // criando uma instancia da fragment
                        val fragmento = ItemFragment()
                        fragmento.item = it // o item da fragment é o item da iteração (it)

                        transacao.replace(containerView.id, fragmento, null)
                    }
                    transacao.commit()

                }

                /*
                Cai neste método quando:
                1 - A rede está sem internet
                2 - A API está fora do ar
                3 - Não foi possível converter o resultado na classe de resposta
                    Neste exemplo, cairia aqui se o JSON não fosse compatível com Filme
                 */
                override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(baseContext, "Erro na API: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }

            })
        }
    }
}