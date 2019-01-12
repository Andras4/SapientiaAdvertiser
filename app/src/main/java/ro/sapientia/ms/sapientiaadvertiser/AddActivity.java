package ro.sapientia.ms.sapientiaadvertiser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddActivity extends Fragment implements View.OnClickListener {

    private ImageView image;
    private EditText title, description;
    private Button addButton;

    //Database reference
    private FirebaseAuth mAuth;
    String userUID = FirebaseAuth.getInstance().getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    private StorageReference mStorageRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.activity_add,container,false);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        image = fragmentView.findViewById(R.id.image_add);
        title = fragmentView.findViewById(R.id.set_title);
        description = fragmentView.findViewById(R.id.set_short_description);
        addButton = fragmentView.findViewById(R.id.add_button);

        return fragmentView;
    }

    @Override
    public void onClick(View view) {

    }
}
