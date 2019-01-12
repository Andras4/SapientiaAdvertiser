package ro.sapientia.ms.sapientiaadvertiser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    Context mContext;
    List<Data> mData;

    public RecyclerViewAdapter(Context mContext, List<Data> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageText.setText(mData.get(position).getText());
        holder.imageDescription.setText(mData.get(position).getDescription());
        holder.imageData.setImageResource(mData.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView imageText, imageDescription;
        private ImageView imageData;

        public ViewHolder(View itemView) {
            super(itemView);

            imageText = itemView.findViewById(R.id.image_name);
            imageDescription = itemView.findViewById(R.id.image_description);
            imageData = itemView.findViewById(R.id.image_recycler);
        }
    }
}
