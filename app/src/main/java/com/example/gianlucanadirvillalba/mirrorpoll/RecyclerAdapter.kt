package com.example.gianlucanadirvillalba.mirrorpoll

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso


/**
 * Created by gianlucanadirvillalba on 06/10/2017.
 */
class RecyclerAdapter(context: Context) : RecyclerView.Adapter<RecyclerAdapter.PollsHolder>(), PollListener
{
    private lateinit var mContext: Context
    private var mLayoutInflater = LayoutInflater.from(context)

    companion object
    {
        var data: ArrayList<Poll> = ArrayList()

        fun addData(instance: RecyclerAdapter, data: ArrayList<Poll>)
        {
            this.data = data
            instance.notifyItemRangeChanged(0, data.size)
        }
    }

    fun addNewData(instance: RecyclerAdapter, newData: Poll)
    {
        data.add(newData)
        instance.notifyItemChanged(0, data.size)
        instance.notifyDataSetChanged() //aggiunto per non far crashare l'app a quando ho aggiunto lo swiperefreshlayout nel coordinator
    }

    override fun onBindViewHolder(holder: PollsHolder?, position: Int)
    {
        val poll = data[position]
        holder?.textName?.text = poll.name
        holder?.textVotes?.text = "voti ${poll.votes}"
        val candidates = poll.candidates.toString()
        holder?.textCandidates?.text = candidates.substring(1, candidates.length-1).replace(", ", "")
        if (poll.image.isNotEmpty())
        {
            Picasso.with(MyApplication.appContext)
                    .load(poll.image)
                    .placeholder(ContextCompat.getDrawable(MyApplication.appContext, R.mipmap.ic_mirror))
                    //.resize(100, 100)
                    //.centerInside()
                    .into(holder?.pollImage)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PollsHolder
    {
        val view = mLayoutInflater.inflate(R.layout.custom_list_poll, parent, false)
        return PollsHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onAddPoll(pollArray: ArrayList<Poll>)
    {
        addData(this, pollArray)
    }

    class PollsHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        var textName = itemView?.findViewById(R.id.pollname) as TextView
        var textCandidates = itemView?.findViewById(R.id.candidates) as TextView
        var textVotes = itemView?.findViewById(R.id.votes) as TextView
        var pollImage = itemView?.findViewById(R.id.pollimage) as ImageView

        init
        {
            itemView?.setOnClickListener(this)
        }

        override fun onClick(p0: View?)
        {
            Toast.makeText(MyApplication.appContext, "Sending poll ${data[adapterPosition].name}", Toast.LENGTH_SHORT).show()

//            if (p0?.findViewById(R.id.candidates)?.visibility == View.GONE)
//                p0?.findViewById(R.id.candidates)?.visibility = View.VISIBLE
//            else p0?.findViewById(R.id.candidates)?.visibility = View.GONE

            MyApplication.sendToMirror(data[adapterPosition]) //invio dati al mirror
        }
    }

}