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

    private static final int PICK_IMAGE = 100;
    public static final int RESULT_OK = -1;

    private void openGallery()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            final Uri imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.activity_add,container,false);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        image = fragmentView.findViewById(R.id.image_add);
        title = fragmentView.findViewById(R.id.set_title);
        description = fragmentView.findViewById(R.id.set_short_description);
        addButton = fragmentView.findViewById(R.id.add_button);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mtitle = title.getText().toString();
                String mdescription = description.getText().toString();

                if (mtitle.isEmpty()) {
                    title.setError("Please enter the title of your advertisement!");
                    title.requestFocus();
                    return;
                }
                if (mdescription.isEmpty()) {
                    description.setError("Please enter a short description of your advertisement!");
                    description.requestFocus();
                    return;
                }
                Toast.makeText(getContext(),"Advertisement added!",Toast.LENGTH_SHORT).show();
            }
        });

        return fragmentView;
    }

    @Override
    public void onClick(View view) {

    }
}
