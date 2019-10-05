package com.sociorich.app.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.sociorich.app.R;
import com.sociorich.app.adapters.GalleryAdapter;
import com.sociorich.app.adapters.MyAdapter;
import com.sociorich.app.app_utils.CommonVariables;
import com.sociorich.app.app_utils.ConstantMethods;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sociorich.app.app_utils.AppApis.UPLAOD_SINGLE_IMAGE;

public class CreatePostActivity extends BaseActivity {
    private ImageView addBtn;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;
    private Button postData;
    ArrayList<Uri> mArrayUri;
    private Spinner catSpnr;
    private LinearLayout lnrImages;
    private ArrayList<String> imagesPathList;
    private Bitmap yourbitmap;
    String[] imagesPath;
    private static final int CAMERA_PHOTO = 1;
    private Uri imageToUploadUri;
    private ArrayList<Uri> arrayList;
    private ArrayList<String> pathlist;
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private String mCurrentPhotoPath;
    private GridView listImages;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantMethods.setTitleAndBack(this,"Create Post");
        addBtn = findViewById(R.id.add_btn);
        gvGallery = findViewById(R.id.gv);
        postData = findViewById(R.id.post_data);
        catSpnr  = findViewById(R.id.cat_spnr);
        listImages = findViewById(R.id.gv);
//        viewImage = findViewById(R.id.add_btn);
//        lnrImages = findViewById(R.id.lnrImages);
        arrayList = new ArrayList<>();
        pathlist = new ArrayList<>();
        setCatSpinner(catSpnr);
        addBtn.setOnClickListener(v->{
            selectImage();
        });
        postData.setOnClickListener(v->{
            for(int i=0;i<imagesPath.length;i++){
                uploadSingleImage(imagesPath[i]);
            }
        });
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_post;
    }

    private void uploadSingleImage(String path){
        String token = ConstantMethods.getStringPreference("user_token",this);
        File file = new File(path);
        AndroidNetworking.upload(UPLAOD_SINGLE_IMAGE)
                .addMultipartFile("image",file)
                .addMultipartParameter("fileSize","545466565")//new StringBody("545466565")
                .setTag("uploadTest")
                .addHeaders("authorization","Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response",response.toString());
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e("response",error.toString());
                    }
                });
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void setCatSpinner(Spinner catSpinner){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CommonVariables.POST_CATEGORY);//setting the country_array to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(adapter);
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra( MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
                    if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);

                        File pictureFile = null;
                        try {
                            pictureFile = getPictureFile();
                        } catch (IOException ex) {
                            Toast.makeText(CreatePostActivity.this, "Photo file nt", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (pictureFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(CreatePostActivity.this,
                                    "com.app.sociorichapp.fileprovider",
                                    pictureFile);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                            startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
                        }
                    }

                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    static final int REQUEST_PICTURE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new  File(pictureFilePath);
            if(imgFile.exists())            {
//                image.setImageURI(Uri.fromFile(imgFile));
                Uri uri = Uri.fromFile(imgFile);
                arrayList.add(uri);
                MyAdapter mAdapter = new MyAdapter(this, arrayList);
                listImages.setAdapter(mAdapter);
            }
        }
        else if (requestCode == 2) {
//                Uri selectedImage = data.getData();
//                String[] filePath = { MediaStore.Images.Media.DATA };
//                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                String picturePath = c.getString(columnIndex);
//                c.close();
//                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                Log.w("path of image", picturePath+"");
//                viewImage.setImageBitmap(thumbnail);
            if(requestCode == 2){
                imagesPathList = new ArrayList<>();
                String[] imagesPath = data.getStringExtra("data").split("\\|");
                try{
                    lnrImages.removeAllViews();
                }catch (Throwable e){
                    e.printStackTrace();
                }
                for (int i=0;i<imagesPath.length;i++){
                    imagesPathList.add(imagesPath[i]);
                    yourbitmap = BitmapFactory.decodeFile(imagesPath[i]);
                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(yourbitmap);
                    imageView.setAdjustViewBounds(true);
                    lnrImages.addView(imageView);
                }
            }
            }
        }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CAMERA_PHOTO && resultCode == Activity.RESULT_OK) {
//            if(imageToUploadUri != null){
//                Uri selectedImage = imageToUploadUri;
//                final String path = FileUtils.getPath(this, selectedImage);
//                Log.d("Single File Selected", path);
//                pathlist.add(path);
//                arrayList.add(selectedImage);
//                MyAdapter mAdapter = new MyAdapter(CreatePostActivity.this, arrayList);
//                gvGallery.setAdapter(mAdapter);
//            }else{
//                Toast.makeText(this,"Error while capturing Image",Toast.LENGTH_LONG).show();
//            }
//        }
//
//        switch (requestCode) {
//            case REQUEST_CODE:
//                // If the file selection was successful
//                if (resultCode == RESULT_OK) {
//                    if(data.getClipData() != null) {
//                        int count = data.getClipData().getItemCount();
//                        int currentItem = 0;
//                        while(currentItem < count) {
//                            Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
//                            //do something with the image (save it to some directory or whatever you need to do with it here)
//                            currentItem = currentItem + 1;
//                            Log.d("Uri Selected", imageUri.toString());
//                            try {
//                                // Get the file path from the URI
//                                String path = FileUtils.getPath(this, imageUri);
//
//                                Log.d("Multiple File Selected", path);
//                                pathlist.add(path);
//                                arrayList.add(imageUri);
//                                MyAdapter mAdapter = new MyAdapter(CreatePostActivity.this, arrayList);
//                                gvGallery.setAdapter(mAdapter);
//
//                            } catch (Exception e) {
//                                Log.e("TAG", "File select error", e);
//                            }
//                        }
//                    } else if(data.getData() != null) {
//                        //do something with the image (save it to some directory or whatever you need to do with it here)
//                        final Uri uri = data.getData();
//                        Log.i("TAG", "Uri = " + uri.toString());
//                        try {
//                            // Get the file path from the URI
//                            final String path = FileUtils.getPath(this, uri);
//                            Log.d("Single File Selected", path);
//                            pathlist.add(path);
//                            arrayList.add(uri);
//                            MyAdapter mAdapter = new MyAdapter(CreatePostActivity.this, arrayList);
//                            gvGallery.setAdapter(mAdapter);
//
//                        } catch (Exception e) {
//                            Log.e("TAG", "File select error", e);
//                        }
//                    }
//                }
//                break;
//
//            case  REQUEST_IMAGE_CAPTURE:
//                if(resultCode == RESULT_OK){
//                    try {
////                        mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
////                        mImageView.setImageBitmap(mImageBitmap);
//                        Uri uri = Uri.parse(mCurrentPhotoPath);
//                        final String path = FileUtils.getPath(this, uri);
//                        Log.d("Single File Selected", path);
//                        pathlist.add(path);
//                        arrayList.add(uri);
//                        MyAdapter mAdapter = new MyAdapter(CreatePostActivity.this, arrayList);
//                        gvGallery.setAdapter(mAdapter);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//        }
//
//    }
    private String pictureFilePath;
    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "ZOFTINO_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }
}
