package com.metova.dewey;

import com.metova.deweydecoration.DeweyProvider;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.SampleViewHolder> implements DeweyProvider {

    private Context mContext;
    private List<Person> mPersonList;

    public SampleAdapter(Context context, List<Person> personList) {
        mContext = context;
        mPersonList = personList;
    }

    @Override
    public SampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        return new SampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SampleViewHolder holder, int position) {
        Person person = mPersonList.get(position);
        holder.mTextView.setText(mContext.getString(R.string.person_name, person.getFirstName(), person.getLastName()));
    }

    @Override
    public int getItemCount() {
        return mPersonList.size();
    }

    @Override
    public CharSequence getDeweyLabelForPosition(int position) {
        Person person = mPersonList.get(position);
        return person.getLastName().substring(0, 1).toUpperCase(Locale.US);
    }

    class SampleViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public SampleViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
