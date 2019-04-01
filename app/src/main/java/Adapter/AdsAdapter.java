package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Model.Ads;
import se.binninge.korp.serviceapp.R;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.ViewHolder> {

    private Context context;
    private List<Ads> adsList;

    public AdsAdapter(Context context, List adsList) {
        this.context = context;
        this.adsList = adsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ads_layout_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Ads ad = adsList.get(i);

        viewHolder.title1.setText(ad.getTitle());
        viewHolder.description1.setText(ad.getDescription());
        viewHolder.user1.setText(ad.getUser());
    }

    @Override
    public int getItemCount() {
        return adsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title1;
        public  TextView description1;
        public TextView user1;

        public ViewHolder(@NonNull View adsView) {
            super(adsView);

            adsView.setOnClickListener(this);

            title1 = adsView.findViewById(R.id.title);
            description1 = adsView.findViewById(R.id.description);
            user1 = adsView.findViewById(R.id.user);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            Ads ad = adsList.get(position);

            Toast.makeText(context, ad.getTitle(), Toast.LENGTH_LONG).show();
        }
        /*public TextView category1;
        public  TextView price1;*/

    }
}
