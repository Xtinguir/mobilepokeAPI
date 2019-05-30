package id.ac.umn.projectuas00000013728;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.ArrayList;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {
    private ArrayList<Pokemon> dataList;
    String empty = "-";
    private Context context;
    public PokemonAdapter(ArrayList<Pokemon> dataList){
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public PokemonAdapter.PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = layoutInflater.inflate(R.layout.row_pokemon,parent,false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonAdapter.PokemonViewHolder holder, int position) {
        int tempNumber = dataList.get(position).getOrder();
        holder.txtOrderPokemon.setText(Integer.toString(tempNumber));
        holder.txtNamePokemon.setText(dataList.get(position).getName());
        holder.txtTypePokemon.setText(dataList.get(position).getType());
        holder.txtType2Pokemon.setText(dataList.get(position).getTyp2());
        ImageView tempImage = holder.imgSpritePokemon;
        Glide.with(context).load(dataList.get(position).getSpriteURL()).into(tempImage);
        Log.d("GAMBAR",dataList.get(position).getSpriteURL());
    }

    @Override
    public int getItemCount() {
            return (dataList!=null) ? dataList.size() : 0;
    }

    public class PokemonViewHolder extends RecyclerView.ViewHolder{
        private TextView txtOrderPokemon,txtNamePokemon,txtTypePokemon,txtType2Pokemon;
        private ImageView imgSpritePokemon;

        public PokemonViewHolder(final View itemView){
            super(itemView);
            txtOrderPokemon = (TextView) itemView.findViewById(R.id.row_order);
            txtNamePokemon = (TextView) itemView.findViewById(R.id.row_name);
            txtTypePokemon = (TextView) itemView.findViewById(R.id.row_type);
            txtType2Pokemon = (TextView) itemView.findViewById(R.id.row_type2);
            imgSpritePokemon = (ImageView) itemView.findViewById(R.id.imageviewPokemon);
            /////SET ON CLICK NANTI AJA
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(itemView.getContext(),DetailPokemonActivity.class);

                    String tempId = Integer.toString(dataList.get(position).getOrder());
                    intent.putExtra("id",tempId);
                    intent.putExtra("name",dataList.get(position).getName());
                    intent.putExtra("type",dataList.get(position).getType());
                    intent.putExtra("type2",dataList.get(position).getTyp2());
                    intent.putExtra("imagePath",dataList.get(position).getSpriteURL());
                    String tempWeight = Integer.toString(dataList.get(position).getWeight()*10);
                    intent.putExtra("weight",tempWeight);
                    String tempHeight = Integer.toString(dataList.get(position).getHeight()*10);
                    intent.putExtra("height",tempHeight);
                    intent.putExtra("abilities",dataList.get(position).getAbilityList());
                    itemView.getContext().startActivity(intent);

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();

                    Toast.makeText(context,"It's "+dataList.get(position).getName(),Toast.LENGTH_SHORT).show();
                    final String CHANNEL_ID = "Project UAS 00000013728";
                    final NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        CharSequence name = "Notification";
                        String description = "Description";
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
                        channel.setDescription(description);
                        notificationManager.createNotificationChannel(channel);
                    }
                    final NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(dataList.get(position).getName())
                            .setContentText(dataList.get(position).getType())
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true);
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    notificationManager.notify(1,builder.build());
                                }},5000);

                    return true;
                }
            });

        }
    }


}
