package ro.sapientia.ms.sapientiaadvertiser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.util.Patterns;
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

public class SettingsActivity extends Fragment implements View.OnClickListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private ImageView changePhoto;
    private Button signOut, save;
    private EditText firstName, lastName, email, address;

    Uri mImageUri;
    boolean photoChanged = false;

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
            mImageUri = data.getData();
            Glide.with(getContext())
                    .load(mImageUri)
                    .into(changePhoto);
            photoChanged = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.activity_settings,container,false);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        changePhoto = fragmentView.findViewById(R.id.change_profile_picture);
        signOut = fragmentView.findViewById(R.id.sign_out);
        save = fragmentView.findViewById(R.id.save_button);
        firstName = fragmentView.findViewById(R.id.change_first_name);
        lastName = fragmentView.findViewById(R.id.change_last_name);
        email = fragmentView.findViewById(R.id.change_email);
        address = fragmentView.findViewById(R.id.change_address);

        // Get data from database, to prefill some of the EditTexts
        // if there are data to be shown.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.hasChild(userUID)) {
                    String dfirstName = dataSnapshot.child(userUID).child("FirstName").getValue().toString();
                    String dlastName = dataSnapshot.child(userUID).child("LastName").getValue().toString();
                    String demail = dataSnapshot.child(userUID).child("Email").getValue().toString();
                    String daddress = dataSnapshot.child(userUID).child("Address").getValue().toString();

                    if(dataSnapshot.hasChild(userUID + "/UserImage"))
                    {
                        String duserImage = dataSnapshot.child(userUID).child("UserImage").getValue().toString();
                        if(duserImage != null && !(duserImage.trim().isEmpty()))
                        {
                            mStorageRef.child(duserImage).getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Successfully downloaded data to local file
                                            // ...
                                            Glide.with(getContext())
                                                    .load(uri)
                                                    .into(changePhoto);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failed download
                                    // ...
                                    Toast.makeText(getContext(),"Image load failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    firstName.setText(dfirstName);
                    lastName.setText(dlastName);
                    email.setText(demail);
                    address.setText(daddress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Toast.makeText(getContext(), "Database read failed!", Toast.LENGTH_SHORT).show();
            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(),"Signed out",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mfirstName = firstName.getText().toString();
                String mlastName = lastName.getText().toString();
                String memail = email.getText().toString();
                String maddress = address.getText().toString();

                if (mfirstName.isEmpty()) {
                    firstName.setError("Please enter your first name!");
                    firstName.requestFocus();
                    return;
                }
                if (mlastName.isEmpty()) {
                    lastName.setError("Please enter your last name!");
                    lastName.requestFocus();
                    return;
                }
                if (memail.isEmpty()) {
                    email.setError("Please enter your email!");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(memail).matches()) {
                    email.setError("Please enter a valid email address!");
                    email.requestFocus();
                    return;
                }
                if(maddress.isEmpty()) {
                    address.setError("Please enter your address!");
                    address.requestFocus();
                    return;
                }

                myRef.child(userUID).child("FirstName").setValue(mfirstName);
                myRef.child(userUID).child("LastName").setValue(mlastName);
                myRef.child(userUID).child("Email").setValue(memail);
                myRef.child(userUID).child("Address").setValue(maddress);
                if(photoChanged)
                {
                    String childRoute = "images/" + userUID + "/profile/" + System.currentTimeMillis() + ".jpg";
                    StorageReference riversRef = mStorageRef.child(childRoute);
                    riversRef.putFile(mImageUri);
                    myRef.child(userUID).child("UserImage").setValue(childRoute);
                }
                Toast.makeText(getContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
            }
        });

        return fragmentView;
    }

    @Override
    public void onClick(View view) {

    }
}
