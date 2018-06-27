package com.example.hp.offermagnet;


import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

Database db;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Button edit;
        final EditText name,phone,pass;
        CircleImageView image;
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        db=new Database(getActivity());
        name=(EditText)view.findViewById(R.id.usr_name);
        name.setText(db.getName());
        name.setEnabled(false);
        phone=(EditText)view.findViewById(R.id.usr_phone);
        phone.setText(db.getPhone());
        phone.setEnabled(false);
        pass=(EditText)view.findViewById(R.id.usr_pass);
        pass.setText("********");
        pass.setEnabled(false);
        image=(CircleImageView)view.findViewById(R.id.image);
        Picasso.with(getActivity())
                .load(db.getImage())
                .into(image);
        edit=(Button) view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"presed",Toast.LENGTH_SHORT).show();

                if(edit.getText().equals("edit")){
                    name.setEnabled(true);
                    phone.setEnabled(true);
                    pass.setEnabled(true);
                    edit.setText("Save");

                }
                else{

                }
            }
        });
        return view;
    }

}
