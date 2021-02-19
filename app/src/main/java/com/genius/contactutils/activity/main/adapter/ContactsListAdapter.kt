package com.genius.contactutils.activity.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.genius.contactutils.R
import com.genius.contactutils.databinding.ItemContactsBinding
import com.genius.contactutils.models.Contact

/**
 * Created by geniuS on 19/02/2021.
 */
class ContactsListAdapter(
    private val items: ArrayList<Contact>,
    private val callback: ContactsAdapterCallback
) :
    RecyclerView.Adapter<ContactsListAdapter.ItemVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemVH(
            ItemContactsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        holder.onBind(items[position])
    }

    inner class ItemVH(private val binding: ItemContactsBinding) :
        RecyclerView.ViewHolder(binding.root),
        ContactsItemVH.ContactsCallback {

        override fun onContactsClick(items: Contact) {
            items.isSelected = !items.isSelected
            setDrawable(binding.contactSelected, items)
            callback.onContactClick(items)
        }

        fun onBind(contact: Contact) {
            binding.contactsViewModel = ContactsItemVH(contact, this)
            binding.executePendingBindings()

            setDrawable(binding.contactSelected, contact)
        }
    }

    private fun setDrawable(
        imageView: ImageView,
        items: Contact
    ) {
        imageView.setImageDrawable(
            if (items.isSelected) ContextCompat.getDrawable(
                imageView.context,
                R.drawable.ic_check_box_checked
            )
            else ContextCompat.getDrawable(
                imageView.context,
                R.drawable.ic_check_box_not_checked
            )
        )
    }

    public interface ContactsAdapterCallback {
        fun onContactClick(items: Contact)
    }
}