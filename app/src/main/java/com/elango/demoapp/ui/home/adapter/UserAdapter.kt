package com.elango.demoapp.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.elango.demoapp.R
import com.elango.demoapp.listener.CommonListener
import com.elango.demoapp.model.MapModel

class UserAdapter(
    private val myContext: Context,
    private var mData: ArrayList<Int>,
    private var mClickList: CommonListener,
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
            .inflate(R.layout.inflate_user_item, parent, false)
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

        aHolder.userIdTV.text = data.toString()
        aHolder.mRootlay.setOnClickListener {
            mClickList.click(data)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder constructor(aView: View) :
        RecyclerView.ViewHolder(aView) {
        val userIdTV: TextView

        val mRootlay: LinearLayout

        init {
            userIdTV = aView.findViewById<View>(R.id.userIdTV) as TextView

            mRootlay = aView.findViewById<View>(R.id.root_lay) as LinearLayout
        }
    }

    fun update(data: ArrayList<Int>) {
        mData = data
        notifyDataSetChanged()
    }
}