package com.appstreettask.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appstreettask.R
import com.appstreettask.ui.AppStreetApplication
import com.appstreettask.ui.callbacks.ItemClickListener
import com.appstreettask.ui.model.DataDetailsItem

class DataListAdapter(
    private var mContext: Context, private var listener: ItemClickListener
) : RecyclerView.Adapter<DataListAdapter.MyViewHolder>() {

    private var gitDataList: List<DataDetailsItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val mInflater = LayoutInflater.from(mContext)
        val view = mInflater.inflate(R.layout.git_data_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.tag = position
        holder.itemView.setOnClickListener(itemClickListener)
        val gitDataItem = gitDataList[position]
        holder.name.text = mContext.getString(R.string.author) + " "+ gitDataItem.name.toUpperCase()
        holder.userName.text = mContext.getString(R.string.user) + " "+gitDataItem.username
        holder.otherDetails.text = mContext.getString(R.string.repo) + " "+ gitDataItem.repo.name
        AppStreetApplication.getImageLoader().DisplayImage(gitDataItem.avatar, holder.userImage)
    }

    private var itemClickListener = View.OnClickListener { view ->
        var position = view.tag as Int
        listener.onItemClick(gitDataList[position], view)
    }

    override fun getItemCount(): Int {
        return gitDataList.size ?: 0
    }

    fun setData(gitDataList: List<DataDetailsItem>) {
        this.gitDataList = gitDataList
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var userImage: ImageView = view.findViewById(R.id.user_image) as ImageView
        var name: TextView = view.findViewById(R.id.name) as TextView
        var userName: TextView = view.findViewById(R.id.sub_name) as TextView
        var otherDetails: TextView = view.findViewById(R.id.other_details) as TextView
    }
}
