package emad.todo.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import emad.todo.DB.DBHelper;
import emad.todo.MainActivity;
import emad.todo.Model.Item;
import emad.todo.R;
import emad.todo.ViewItem;

public class HomeAdapter extends  RecyclerView.Adapter<HomeAdapter.MyViewHolder>{

    ArrayList<Item> items;
    Context context;
    Dialog dialog;
    DBHelper dbHelper;

    public HomeAdapter(ArrayList<Item> items, Context context, Dialog dialog) {
        this.items = items;
        this.context = context;
        dbHelper = new DBHelper(context);
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_holder, parent, false);
        return new HomeAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {
        final Intent intent = new Intent("NoItems");

        holder.txtTitle.setText(items.get(i).getTitle());
        holder.txtDescription.setText(items.get(i).getDescription());
        String[] text = items.get(i).getDateAdded().split(",");
        holder.dateAdded.setText(text[0]);
        holder.timeAdded.setText(text[1]);

        // remove item from DB
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove item from sqlite & firebase



                dialog.show();
                LinearLayout deleteIcon = dialog.findViewById(R.id.deleteYesIcon);
                deleteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    dbHelper.removeItem(items.get(i));
//                    dbHelper.addToRemovedItems(items.get(i));
                    Toast.makeText(context.getApplicationContext(), "Note Removed", Toast.LENGTH_SHORT).show();
//                    context.startActivity(new Intent(context, MainActivity.class));
                    items = dbHelper.getAllItems();
                    if (items.size()==0)
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

//                    else
//                        ((MainActivity)context).noNotes.setVisibility(View.GONE);

                        notifyDataSetChanged();
                    dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.deleteNoIcon).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.findViewById(R.id.deleteExitIcon).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageButton btnRemove;
        TextView txtTitle;
        TextView txtDescription;
        TextView dateAdded;
        TextView timeAdded;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            btnRemove = itemView.findViewById(R.id.btnRemove);
            txtTitle = itemView.findViewById(R.id.titleItem);
            txtDescription = itemView.findViewById(R.id.descItem);
            dateAdded = itemView.findViewById(R.id.dateAdded);
            timeAdded = itemView.findViewById(R.id.timeAdded);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewItem.class);
                    intent.putExtra("item", items.get(getAdapterPosition()));
                    context.startActivity(intent);
//                    ((Activity)itemView.getContext()).finish();
                }
            });
        }
    }
}