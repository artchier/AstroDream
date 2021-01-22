package com.example.astrodream.ui.favorites

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.astrodream.R
import kotlinx.android.synthetic.main.fragment_fav_recycler.*
import kotlinx.android.synthetic.main.fragment_fav_recycler.view.*

class FavRecyclerFragment() : Fragment(), FavAdapter.OnClickFavListener {

    private lateinit var listFavs: List<Any>  // TODO: criar sealed class??
    private lateinit var adapterFav: FavAdapter
    private lateinit var recyclerViewState: Parcelable // variavel para salvar a posição do Recycler

    val viewModel: FavViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fav_recycler, container, false)

        // Retoma a posição do Recycler, caso exista um estado anterior salvo
        if (::recyclerViewState.isInitialized) { // O estado do Recycler está sendo inicializado no onDestroyView
            view.rvFav.layoutManager?.onRestoreInstanceState(recyclerViewState)
        }

        if (container != null) {
            val type = viewModel.favType.value!!
            // Atualiza a lista de itens favoritos
            viewModel.dummyFavData(type)
            viewModel.favList.observe(viewLifecycleOwner) {
                if(it != null && !viewModel.hasOngoingQuery.value!!) {
                    listFavs = it
                    // Atualiza o adapter de acordo com a tab selecionada
                    adapterFav = FavAdapter(listFavs, this, type)
                    // Atribui o adapter criado acima ao adapter do RecyclerView
                    view.rvFav.adapter = adapterFav
                    // Cria o layout do RecyclerView
                    view.rvFav.layoutManager = LinearLayoutManager(container.context)
                    view.rvFav.setHasFixedSize(true)
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerViewState = rvFav.layoutManager?.onSaveInstanceState()!!
    }

    override fun onClickFav(position: Int) {

        // Pega a posição do favorito clicado
        viewModel.selectDetail(listFavs[position])

        // Passa dados para o viewModel
        // e navega para o respectivo fragment
        // TODO: asteroide, globo e tech => passar dados para o viewModel e buscar fragment correto
        when (viewModel.favType.value) {
            "daily" -> {
                findNavController().navigate(R.id.action_favRecyclerFragment_to_dailyImageFragment)
            }
            "asteroid" -> {
                findNavController().navigate(R.id.action_favRecyclerFragment_to_asteroidOrbitFragment)
            }
            "tech" -> {
                findNavController().navigate(R.id.action_favRecyclerFragment_to_favTechFragment)
            }
            "mars" -> {
                findNavController().navigate(R.id.action_favRecyclerFragment_to_recentMarsFragment2)
            }
        }
    }
}