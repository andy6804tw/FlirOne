package com.andy6804tw.flirone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FirebaseActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private static final int GALLERY_INTENT=4;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        progressDialog=new ProgressDialog(this);
        storageReference=FirebaseStorage.getInstance().getReference();//建立儲存位置


    }

    public void onClick(View view) {

        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);//結果回傳Activity

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT&&resultCode==RESULT_OK){
            progressDialog.setMessage("上傳中...");
            progressDialog.show();
            Uri uri=data.getData();
            StorageReference filepath=storageReference.child("image").child(uri.getLastPathSegment());
            Log.e("img",uri.toString());
            File imgFile = new  File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/T-2017-09-30-22-59-43+0800.jpg");

            filepath.putFile(Uri.fromFile(imgFile)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(FirebaseActivity.this,"ok",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }
    }
}
