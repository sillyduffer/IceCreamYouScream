package com.example.android.icecreamyouscream;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(@NonNull Context context, ArrayList<Article> theseArticles) {
        super(context, 0, theseArticles);
    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        ViewHolder holder;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.article_list_item, parent, false);
            holder = new ViewHolder();
            holder.dateView = (TextView) listItemView.findViewById(R.id.article_date);
            holder.titleView = (TextView) listItemView.findViewById(R.id.article_title);
            holder.sectionView = (TextView) listItemView.findViewById(R.id.article_section);

            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        Article currentArticle = getItem(position);

        holder.titleView.setText(currentArticle.getmTitle());

        holder.sectionView.setText(currentArticle.getmSection());

        holder.dateView.setText(currentArticle.getmDate());

        return listItemView;
    }

    private static class ViewHolder {
        TextView dateView;
        TextView titleView;
        TextView sectionView;
    }

}
