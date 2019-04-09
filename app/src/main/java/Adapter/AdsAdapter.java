package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import Model.Ads;
import se.binninge.korp.serviceapp.LookAtAdActivity;
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

        String userName = ad.getFirstName() + " " + ad.getLastName();
        String adPrice = Double.toString(ad.getPrice());

        viewHolder.title1.setText(ad.getTitle());
        viewHolder.price.setText(adPrice);
        viewHolder.user1.setText(userName);
    }

    @Override
    public int getItemCount() {
        return adsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title1;
        public  TextView price;
        public TextView user1;

        public ViewHolder(@NonNull View adsView) {
            super(adsView);

            adsView.setOnClickListener(this);

            title1 = adsView.findViewById(R.id.title);
            price = adsView.findViewById(R.id.price);
            user1 = adsView.findViewById(R.id.user);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            Ads ad = (Ads) adsList.get(position);

            Intent intent = new Intent(context, LookAtAdActivity.class);

            intent.putExtra("ADOBJECT", ad);

            context.startActivity(intent);

            Toast.makeText(context, ad.getTitle(), Toast.LENGTH_LONG).show();
        }

    }
}
