package br.com.gmauricio.mycar.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.gmauricio.mycar.R
import br.com.gmauricio.mycar.models.RequestState
import br.com.gmauricio.mycar.ui.base.auth.BaseAuthViewModel
import br.com.gmauricio.mycar.ui.base.auth.BaseFragment

const val NAVIGATION_KEY = "NAV_KEY"
abstract class BaseAuthFragment : BaseFragment() {

    private val baseAuthViewModel: BaseAuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerObserver()
        baseAuthViewModel.isLoggedIn()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun registerObserver() {
        baseAuthViewModel.loggedState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RequestState.Loading -> showLoading()
                is RequestState.Success -> {
                    hideLoading()
                }
                is RequestState.Error -> {
                    hideLoading()
                    findNavController().navigate(
                        R.id.logInFragment, bundleOf(
                            NAVIGATION_KEY to findNavController().currentDestination?.id
                        )
                    )
                }
            }
        })
    }
}