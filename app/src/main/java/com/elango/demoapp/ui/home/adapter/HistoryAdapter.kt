package com.elango.demoapp.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elango.demoapp.R
import com.elango.demoapp.listener.CommonListener
import com.elango.demoapp.model.MapModel

class HistoryAdapter(
    private val myContext: Context,
    private val mData: ArrayList<MapModel>,
    private var mClickList: CommonListener,
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
            .inflate(R.layout.inflate_history_item, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
        aHolder: ViewHolder,
        position: Int
    ) {
        if (position % 2 == 0) {
            aHolder.mRootlay.setBackgroundColor(myContext.resources.getColor(R.color.lite_primary_color_recycle_item))
        } else {
            aHolder.mRootlay.setBackgroundColor(myContext.resources.getColor(R.color.white))
        }
        val data = mData.get(position)

        aHolder.mAddressTV.text = data.address
        aHolder.mRootlay.setOnClickListener {
            mClickList.click(data)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder constructor(aView: View) :
        RecyclerView.ViewHolder(aView) {
        val mAddressTV: TextView
        val mLatTv: TextView
        val mLngTv: TextView
        val mRootlay: LinearLayout

        init {
            mAddressTV = aView.findViewById<View>(R.id.addressTV) as TextView
            mLatTv = aView.findViewById<View>(R.id.latTv) as TextView
            mLngTv = aView.findViewById<View>(R.id.lngTv) as TextView

            mRootlay = aView.findViewById<View>(R.id.root_lay) as LinearLayout
        }
    }

}