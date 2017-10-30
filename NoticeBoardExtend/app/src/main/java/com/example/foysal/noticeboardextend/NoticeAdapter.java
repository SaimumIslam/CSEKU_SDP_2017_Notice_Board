package com.example.foysal.noticeboardextend;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {

    private List<Notice> NoticesList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView title, noticeWriter, description;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            noticeWriter = (TextView) view.findViewById(R.id.noticeWriter);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo contextMenuInfo) {

        }
    }


    public NoticeAdapter(List<Notice> NoticesList) {
        this.NoticesList = NoticesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notice_style, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notice Notice = NoticesList.get(position);
        holder.title.setText(Notice.getTitle());
        holder.description.setText(Notice.getdescription());
        holder.noticeWriter.setText(Notice.getnoticeWriter());
    }

    @Override
    public int getItemCount() {
        return NoticesList.size();
    }
}