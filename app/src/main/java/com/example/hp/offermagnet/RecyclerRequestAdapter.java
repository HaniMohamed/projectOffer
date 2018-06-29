package com.example.hp.offermagnet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerRequestAdapter extends RecyclerView.Adapter<RecyclerRequestAdapter.ViewHolder> {

    private ArrayList<DataItemRequest> dataItem;
    Context context;
    public static ItemClickListener clickListener;
    LayoutInflater inflater;
    View alertLayout;
    RatingBar rateUser;
    TextView numberOfUsers, txtPath, txtfinish, pro_des,pro_name,txtNumJoin;
    ImageView pro_img;
    CircleImageView imageProfile,Pro_img_offer;
    Button btnAddOffer, Join ;
    EditText txtOffer;
    Button btnSend;

    int position;

    Database db;
    View view;
    // data is passed into the constructor
    public RecyclerRequestAdapter(ArrayList<DataItemRequest> dataItem, Context context) {

        this.dataItem = dataItem;
        this.context=context;
        db=new Database(context);


    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_request_resource, parent, false);


        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        this.position=i;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        alertLayout = inflater.inflate(R.layout.fragment_request_details, null);
        imageProfile = alertLayout.findViewById(R.id.imageProfileR);
        txtNumJoin=alertLayout.findViewById(R.id.numJoin);
        txtfinish = alertLayout.findViewById(R.id.txtTo);
        pro_img = alertLayout.findViewById(R.id.jpjProduct);
        pro_des = alertLayout.findViewById(R.id.txtDesc);
        pro_name=alertLayout.findViewById(R.id.user_name);
        //btnAddOffer=alertLayout.findViewById(R.id.addOffer);
        Join=alertLayout.findViewById(R.id.joinR);
        Picasso.with(context)
                .load(dataItem.get(position).getImageUrl())
                .into(holder.usrImage);
        holder.txtTitle.setText(dataItem.get(position).getTitle());
        holder.desc.setText(dataItem.get(position).getDesc());
        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Details");
                alert.setView(alertLayout);
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (view != null) {
                            ViewGroup parent = (ViewGroup) view.getParent();
                            if (parent != null) {
                                parent.removeAllViews();
                            }
                        }
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
                txtfinish.setText("finish in "+dataItem.get(position).getDateTo());
                pro_des.setText(""+dataItem.get(position).getDesc());

                pro_name.setText(""+dataItem.get(position).getUser_name());
                Picasso.with(context)
                        .load(dataItem.get(position).getImageUrl())
                        .into(imageProfile);

                Picasso.with(context)
                        .load(dataItem.get(position).getProductImageUrl())
                        .into(pro_img);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://offer-system.000webhostapp.com/JoinedRequests.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    String s = URLEncoder.encode(response, "ISO-8859-1");
                                    response = URLDecoder.decode(s, "UTF-8");

                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                                Log.i("tagconvertstr", "["+response+"]");
                                txtNumJoin.setText(response+" people join in this offer");
                                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> stringStringHashMap = new HashMap<>();

                        stringStringHashMap.put("request_id", dataItem.get(position).getId());
                        return stringStringHashMap;
                    }

                };
                Volley.newRequestQueue(context).add(stringRequest);
                Join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Start", Toast.LENGTH_SHORT).show();
                        final ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("Loading ...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://offer-system.000webhostapp.com/Join.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            String s = URLEncoder.encode(response, "ISO-8859-1");
                                            response = URLDecoder.decode(s, "UTF-8");

                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        progressDialog.dismiss();
                                        Log.i("tagconvertstr", "["+response+"]");
                                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> stringStringHashMap = new HashMap<>();
                                stringStringHashMap.put("user_id", db.getId());
                                stringStringHashMap.put("request_id", String.valueOf(dataItem.get(position).getId()));
                                return stringStringHashMap;
                            }

                        };
                        Volley.newRequestQueue(context).add(stringRequest);
                    }
                });



            }
        });
        holder.btnAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertLayout = inflater.inflate(R.layout.fragment_add_offer_to_request, null);
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                txtOffer=alertLayout.findViewById(R.id.offerOfRequest);
                btnSend=alertLayout.findViewById(R.id.btnSendOffer);
                Pro_img_offer=alertLayout.findViewById(R.id.userimageOffer);
                pro_name.setText(""+dataItem.get(position).getUser_name());
                Picasso.with(context)
                        .load(dataItem.get(position).getImageUrl())
                        .into(Pro_img_offer);
                alert.setTitle("Add Offer");
                alert.setView(alertLayout);
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (view != null) {
                            ViewGroup parent = (ViewGroup) view.getParent();
                            if (parent != null) {
                                parent.removeAllViews();
                            }
                        }
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!txtOffer.getText().toString().equals("")) {
                            Toast.makeText(context, "Start", Toast.LENGTH_SHORT).show();
                            final ProgressDialog progressDialog = new ProgressDialog(context);
                            progressDialog.setMessage("Loading ...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://offer-system.000webhostapp.com/OffersBerRequest.php",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                String s = URLEncoder.encode(response, "ISO-8859-1");
                                                response = URLDecoder.decode(s, "UTF-8");

                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            progressDialog.dismiss();
                                            Log.i("tagconvertstr", "[" + response + "]");
                                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> stringStringHashMap = new HashMap<>();
                                    stringStringHashMap.put("user_id", db.getId());
                                    stringStringHashMap.put("request_id", dataItem.get(position).getId());
                                    stringStringHashMap.put("description", txtOffer.getText().toString());
                                    return stringStringHashMap;
                                }
                            };
                            Volley.newRequestQueue(context).add(stringRequest);
                        }else {
                            Toast.makeText(context, "Please Fill Description", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }

        });
    }

    // total number of rows
    @Override
    public int getItemCount()
    {

        return dataItem.size();
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        TextView txtTitle, desc;
        CircleImageView usrImage;
        Button btnDetails,btnAddOffer;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.txtTitleR);
            desc = (TextView) itemView.findViewById(R.id.txtContentR);
            usrImage = (CircleImageView) itemView.findViewById(R.id.userPhotoR);
            btnDetails=(Button)itemView.findViewById(R.id.detailsRequButton) ;
            btnAddOffer=(Button)itemView.findViewById(R.id.addOffer);
            // btnDetails.setOnClickListener(this);

        }

        /*@Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }*/
    }
}

