package com.example.poctelalistagemdeitems

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.poctelalistagemdeitems.databinding.FragmentItemBinding
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ItemFragment : Fragment() {

    lateinit var binding: FragmentItemBinding
    lateinit var item: Item

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // apagar o conteúdo que vem e criar essas 2 linhas para poder usar binding numa fragment
        binding = FragmentItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // esse "tag" é quando a activity envia só o id
        if (tag != null) {
            val id = tag!!.toInt()

            val dataTxt = "31/21/2023"

            LocalDate.parse(dataTxt, DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString()

            // criando o cliente REST
            val get = ApiItens.criar().get(id)

            // chamada da API via Retrofit
            // Callback do pacote retrofit
            // este enqueue é ASSÍNCRONO
            get.enqueue(object : Callback<Item> {

                override fun onResponse(call: Call<Item>, response: Response<Item>) {
                    val sucesso = response.isSuccessful()
                    // response.isSuccessful() -> é true se o status for 2xx ou 3xx

                    // obtendo o status de resposta
                    val statusResposta = response.code()

                    // podemos ainda verificar se o response.body() é null

                    val item = response.body()!!
                    binding.tvItemNome.text = item.nome
                    binding.tvItemQtd.text = item.quantidade.toString()
                    binding.tvItemPreco.text = item.preco.toString()

                }

                /*
                Cai neste método quando:
                1 - A rede está sem internet
                2 - A API está fora do ar
                3 - Não foi possível converter o resultado na classe de resposta
                    Neste exemplo, cairia aqui se o JSON não fosse compatível com Item
                 */
                override fun onFailure(call: Call<Item>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(
                        view.context,
                        "Erro na API: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

            println("bla bla")
        } else { // caso não tenha tag (id) mas tenha o item inteiro
            binding.tvItemNome.text = item.nome
            binding.tvItemQtd.text = item.quantidade.toString()
            binding.tvItemPreco.text = item.preco.toString()
        }

    }
}