package com.example.roomdatabasesample.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabasesample.OnUserDeleteListener
import com.example.roomdatabasesample.databinding.UserDataItemListBinding
import com.example.roomdatabasesample.model.UserData

class UserAdapter(private val context: Context) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private val TAG = "UserAdapter"
    private var userList: List<UserData> = emptyList()
    private var deleteListener: OnUserDeleteListener? = null

    inner class ViewHolder(private val binding: UserDataItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userData: UserData) {
            binding.apply {
                userName.text = userData.name
                userAge.text = userData.age.toString()
                deleteIcon.setOnClickListener {
                    userData.userid?.let { item ->
                        deleteListener?.onDeleteUser(item)
                        Log.e(TAG, "bind: $item")
                        userList = userList.filter { data->
                            data.userid != item
                        } // Remove the deleted item from the list
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater =
            UserDataItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    fun setData(userData: List<UserData>) {
        userList = userData
        notifyDataSetChanged()
    }

    fun setOnUserDeleteListener(onUserDeleteListener: OnUserDeleteListener) {
        this.deleteListener = onUserDeleteListener
        notifyDataSetChanged()
    }
}
