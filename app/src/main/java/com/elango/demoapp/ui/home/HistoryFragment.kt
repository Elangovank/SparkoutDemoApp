package com.elango.demoapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elango.demoapp.R
import com.elango.demoapp.listener.CommonListener
import com.elango.demoapp.model.MapDataDAO
import com.elango.demoapp.model.MapModel
import com.elango.demoapp.ui.home.adapter.HistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_history.*
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class HistoryFragment : Fragment(), CommonListener {

    @Inject
    @Named("LocationDAO")
    lateinit var mMapDao: MapDataDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    fun setAdapter() {
        val data = mMapDao.getMapDetails() as ArrayList<MapModel>
        historyRV.setHasFixedSize(true)
        historyRV.adapter = HistoryAdapter(requireActivity(), data, this)
    }

    override fun click(value: MapModel) {
        startActivity(
            Intent(requireContext(), MapDetailsActivity::class.java).putExtra(
                "lat",
                value.lat
            ).putExtra("lng", value.lng).putExtra("address", value.address)
        )
    }

    override fun click(value: Int) {
    }


}