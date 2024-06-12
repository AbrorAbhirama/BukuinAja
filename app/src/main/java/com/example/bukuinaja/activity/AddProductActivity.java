package com.example.bukuinaja.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bukuinaja.R;
import com.example.bukuinaja.dbhelper.ProdukDatabaseHelper;
import com.example.bukuinaja.dbhelper.VarianDatabaseHelper;

import java.io.ByteArrayOutputStream;

public class AddProductActivity extends AppCompatActivity {

    Button album;
    Button save;
    ImageView imageView;
    TextView productName;
    TextView modalPrice;
    TextView sellPrice;


    ActivityResultLauncher<Intent> resultLauncher;

    ProdukDatabaseHelper produkdb;
    VarianDatabaseHelper variandb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.product_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        album = findViewById(R.id.album);
        save = findViewById(R.id.save);
        imageView = findViewById(R.id.imageView);
        productName = findViewById(R.id.product_name);
        String name = String.valueOf(productName.getText());
        modalPrice = findViewById(R.id.modal_price);
        int modal = Integer.parseInt(String.valueOf(modalPrice.getText()));
        sellPrice = findViewById(R.id.sell_price);
        int sell = Integer.parseInt(String.valueOf(sellPrice.getText()));

        produkdb = new ProdukDatabaseHelper(this);
        variandb = new VarianDatabaseHelper(this);

        registerResult();


        if (album != null) {
            // Set a click listener for the button
            album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickImage();
                }
            });
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }

        if (save != null) {
            // Set a click listener for the button
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageView != null) {
                        Bitmap photo = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        byte[] bArray = bos.toByteArray();
                        produkdb.addData(name, bArray, 0);
                        int id = produkdb.getID(name);
                        variandb.addData(name, modal, sell,id);
                    } else {
                        Toast.makeText(getBaseContext(), "Enter image", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();

        }
    }

    private void pickImage(){
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            Uri imageURi = result.getData().getData();
                            imageView.setImageURI(imageURi);
                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}