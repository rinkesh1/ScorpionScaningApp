package in.webx.scorpion.activity;

import static in.webx.scorpion.Application.DTDCVelocityApplicationData.firebaseCrashlytics;
import static in.webx.scorpion.Application.DTDCVelocityApplicationData.getAppContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import in.webx.scorpion.Adapter.RVInvoiceAdapter;
import in.webx.scorpion.BuildConfig;
import in.webx.scorpion.Model.CityListModel;
import in.webx.scorpion.Model.CustomerListModel;
import in.webx.scorpion.Model.GetBillingPartyListOutputModel;
import in.webx.scorpion.Model.GetCityListOutputModel;
import in.webx.scorpion.Model.GetDestinationListOutputModel;
import in.webx.scorpion.Model.GetPayBasisListInputModel;
import in.webx.scorpion.Model.GetPayBasisListOutputModel;
import in.webx.scorpion.Model.GetProductListOutputModel;
import in.webx.scorpion.Model.InvoiceDataModel;
import in.webx.scorpion.Model.LocationListModel;
import in.webx.scorpion.Model.PayBasisListModel;
import in.webx.scorpion.Model.ProductListModel;
import in.webx.scorpion.R;
import in.webx.scorpion.Retrofit.APIService;
import in.webx.scorpion.Retrofit.MyRetro;
import in.webx.scorpion.util.CommonMethod;
import in.webx.scorpion.util.ConstantData;
import in.webx.scorpion.util.SharedPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuickBookingActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> arrayList;
    RelativeLayout rl_progressBar;
    ImageView ivBackPress;
    Dialog dialog;
    TextInputEditText et_paybasetype;
    TextInputEditText et_billingparty;
    TextInputEditText et_transportmode;
    TextInputEditText et_destination;
    TextInputEditText et_fromcity;
    TextInputEditText et_tocity;
    TextInputEditText et_consigneename;
    TextInputEditText et_product;
    TextInputEditText et_packagetype;
    TextInputEditText et_invoiceno;
    TextInputEditText et_noofpkg;
    TextInputEditText et_declaredvalue;
    TextInputEditText et_actualweight;
    TextInputEditText et_chargedweight;
    TextInputEditText et_invoicevalue;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RVInvoiceAdapter adapter;
    ArrayList<InvoiceDataModel> invoiceData;
    Button btnAddInvoice;
    MaterialButton btnPOD;
    String type, sImage = "", listImage = "", fileExtension = "", status = "", mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int IMAGE_LIST_REQUEST = 2;
    private static final int MAX_IMAGES = 3;
    private static final int SAVE_REQUEST_CODE = 4;
    ImageView captureImage;
    ArrayList<String> imagesEncodedList = new ArrayList<>();
    private ImageView mIvClear;
    private String signature_byte_data;
    private Bitmap mImageBitmap;

    public ArrayList<String> paybaseData = new ArrayList<>();
    public ArrayList<String> billingPartyData = new ArrayList<>();
    public ArrayList<String> transportmodeData = new ArrayList<>();
    public ArrayList<String> destinationData = new ArrayList<>();
    public ArrayList<String> fromcityData = new ArrayList<>();
    public ArrayList<String> tocityData = new ArrayList<>();
    public ArrayList<String> consigneenameData = new ArrayList<>();
    public ArrayList<String> productData = new ArrayList<>();
    public ArrayList<String> packagetypeData = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NoActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_quick_booking);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(
                getResources().getColor(
                        R.color.StatusBarTransparent));

        init();
    }

    void init() {

        // assign variable
        et_paybasetype = findViewById(R.id.et_paybasetype);
        et_billingparty = findViewById(R.id.et_billingparty);
        et_transportmode = findViewById(R.id.et_transportmode);
        et_destination = findViewById(R.id.et_destination);
        et_fromcity = findViewById(R.id.et_fromcity);
        et_tocity = findViewById(R.id.et_tocity);
        et_consigneename = findViewById(R.id.et_consigneename);
        et_product = findViewById(R.id.et_product);
        et_packagetype = findViewById(R.id.et_packagetype);
        et_invoiceno = findViewById(R.id.et_invoiceno);
        et_noofpkg = findViewById(R.id.et_noofpkg);
        et_declaredvalue = findViewById(R.id.et_declaredvalue);
        et_chargedweight = findViewById(R.id.et_chargedweight);
        et_actualweight = findViewById(R.id.et_actualweight);
        et_invoicevalue = findViewById(R.id.et_invoicevalue);
        btnAddInvoice = findViewById(R.id.btn_addInvoice);
        btnPOD = findViewById(R.id.btnPOD);
        ivBackPress = findViewById(R.id.ivBackPress);

        et_paybasetype.setOnClickListener(this);
        et_billingparty.setOnClickListener(this);
        et_transportmode.setOnClickListener(this);
        et_destination.setOnClickListener(this);
        et_fromcity.setOnClickListener(this);
        et_tocity.setOnClickListener(this);
        et_consigneename.setOnClickListener(this);
        et_product.setOnClickListener(this);
        et_packagetype.setOnClickListener(this);
        btnAddInvoice.setOnClickListener(this);
        btnPOD.setOnClickListener(this);
        ivBackPress.setOnClickListener(this);

        //Define Function
        backpress();

        // initialize array list
        arrayList = new ArrayList<>();

        // set value in array list
        arrayList.add("NO DATA FOUND");

        invoiceData = new ArrayList<>();

        et_declaredvalue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_invoicevalue.setText(et_declaredvalue.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getDataList();

    }

    void getDataList() {
        getPaybaseList();
        getBillingPartyList();
        getDestinationList();
        getCityList();
        getProductList();
        getPackageTypeList();

        setrecycleview();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_paybasetype:
                getDialog(et_paybasetype, "et_paybasetype");
                break;
            case R.id.et_billingparty:
                getDialog(et_billingparty, "et_billingparty");
                break;
            case R.id.et_transportmode:
                getDialog(et_transportmode, "et_transportmode");
                break;
            case R.id.et_destination:
                getDialog(et_destination, "et_destination");
                break;
            case R.id.et_fromcity:
                getDialog(et_fromcity, "et_fromcity");
                break;
            case R.id.et_tocity:
                getDialog(et_tocity, "et_tocity");
                break;
            case R.id.et_consigneename:
                getDialog(et_consigneename, "et_consigneename");
                break;
            case R.id.et_product:
                getDialog(et_product, "et_product");
                break;
            case R.id.et_packagetype:
                getDialog(et_packagetype, "et_packagetype");
                break;
            case R.id.btnPOD:
                if (!CommonMethod.isDeviceSupportCamera(this)) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Your device doesn't support camera",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        promptSettings("Alert", "You have denied the permission for 'Storage' access. Please go to app settings and allow permission.");
                    } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        promptSettings("Alert", "You have denied the permission for 'Camera' access. Please go to app settings and allow permission.");
                    } else {

                        selectImage();

                    }
                }
                break;
            case R.id.btn_addInvoice:
                if (et_invoiceno.getText().toString().isEmpty()) {
                    CommonMethod.showToastMessage(QuickBookingActivity.this, "Please Enter Invoice");
                } else if (et_product.getText().toString().isEmpty()) {
                    CommonMethod.showToastMessage(QuickBookingActivity.this, "Please Enter Valid Product");
                } else if (et_packagetype.getText().toString().isEmpty()) {
                    CommonMethod.showToastMessage(QuickBookingActivity.this, "Please Select Package Type");
                } else if (et_noofpkg.getText().toString().isEmpty()) {
                    CommonMethod.showToastMessage(QuickBookingActivity.this, "Please Enter Valid PKGs");
                } else if (et_declaredvalue.getText().toString().isEmpty()) {
                    CommonMethod.showToastMessage(QuickBookingActivity.this, "Please Enter Value");
                } else if (et_actualweight.getText().toString().isEmpty()) {
                    CommonMethod.showToastMessage(QuickBookingActivity.this, "Please Enter Valid Actual Weight");
                } else if (et_invoicevalue.getText().toString().isEmpty()) {
                    CommonMethod.showToastMessage(QuickBookingActivity.this, "Please Enter Valid Value");
                } else {
                    invoiceData.add(
                            new InvoiceDataModel(
                                    et_invoiceno.getText().toString().trim(),
                                    et_product.toString(),
                                    et_packagetype.toString(),
                                    et_noofpkg.getText().toString().trim(),
                                    et_declaredvalue.getText().toString().trim(),
                                    et_actualweight.getText().toString().trim(),
                                    et_chargedweight.getText().toString().trim(),
                                    et_invoicevalue.getText().toString().trim()));
                    adapter.notifyDataSetChanged();
                    et_invoiceno.setText("");
                    et_product.setText("");
                    et_packagetype.setText("");
                    et_noofpkg.setText("");
                    et_declaredvalue.setText("");
                    et_actualweight.setText("");
                    et_chargedweight.setText("");
                    et_invoicevalue.setText("");
                }
                break;


        }
    }

    private void backpress() {
        ivBackPress = findViewById(R.id.ivBackPress);
        ivBackPress.setOnClickListener(v -> finish());
    }

    private void getDialog(TextInputEditText inputEditText, String str) {
        if (str.equalsIgnoreCase("et_paybasetype")) {
            arrayList = paybaseData;
        } else if (str.equalsIgnoreCase("et_billingparty")) {
            arrayList = billingPartyData;
        } else if (str.equalsIgnoreCase("et_transportmode")) {
            arrayList = transportmodeData;
        } else if (str.equalsIgnoreCase("et_destination")) {
            arrayList = destinationData;
        } else if (str.equalsIgnoreCase("et_fromcity")) {
            arrayList = fromcityData;
        } else if (str.equalsIgnoreCase("et_tocity")) {
            arrayList = tocityData;
        } else if (str.equalsIgnoreCase("et_consigneename")) {
//            arrayList = consigneenameData;
            arrayList = billingPartyData;
        } else if (str.equalsIgnoreCase("et_product")) {
            arrayList = productData;
        } else if (str.equalsIgnoreCase("et_packagetype")) {
            arrayList = packagetypeData;
        } else {
            arrayList = arrayList;
        }
        dialog = new Dialog(QuickBookingActivity.this);

        // set custom dialog
        dialog.setContentView(R.layout.dialog_searchable_spinner);

        // set custom height and width
        dialog.getWindow().setLayout(650, 800);

        // set transparent background
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // show dialog
        dialog.show();

        // Initialize and assign variable
        EditText editText = dialog.findViewById(R.id.edit_text);
        ListView listView = dialog.findViewById(R.id.list_view);

        // Initialize array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(QuickBookingActivity.this, android.R.layout.simple_list_item_1, arrayList);

        // set adapter
        listView.setAdapter(adapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // when item selected from list
            // set selected item on textView
            inputEditText.setText(adapter.getItem(position));

            // Dismiss dialog
            dialog.dismiss();
        });
    }

    // set up the RecyclerView
    private void setrecycleview() {

        recyclerView = findViewById(R.id.rv_invoice);
        layoutManager = new LinearLayoutManager(QuickBookingActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RVInvoiceAdapter(QuickBookingActivity.this, invoiceData);
        recyclerView.setAdapter(adapter);

    }

    private void promptSettings(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("go to Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                goToSettings();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + this.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                fileExtension = "JPG";
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();

                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    Uri imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);

                    // Continue only if the File was successfully created
                    if (imageUri != null) {
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            } else if (options[item].equals("Choose from Gallery")) {
                fileExtension = "JPG";
                // initialising intent
                Intent intent = new Intent();

                // setting type to select to be image
                intent.setType("image/*");

                // allowing multiple image to be selected
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.putExtra(Intent.EXTRA_INTENT, MAX_IMAGES);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_LIST_REQUEST);
            } else if (options[item].equals("select pdf file")) {
                fileExtension = "PDF";
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, SAVE_REQUEST_CODE);
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );


        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                imagesEncodedList = new ArrayList<>();

                try {
                    mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = mImageBitmap;

                // initialize byte stream
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // compress Bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                // Initialize byte array
                byte[] bytes = stream.toByteArray();
                // get base64 encoded string
                sImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                // set encoded text on textview
                imagesEncodedList.add(sImage);
                byte[] bytes1 = Base64.decode(sImage, Base64.DEFAULT);
                // Initialize bitmap
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
                // set bitmap on imageView
//                captureImage.setImageBitmap(bitmap1);

            } else if (requestCode == IMAGE_LIST_REQUEST) {
                if (data.getClipData() != null) {
                    imagesEncodedList = new ArrayList<>();
                    ClipData mClipData = data.getClipData();
                    int cout = data.getClipData().getItemCount();
                    for (int i = 0; i < cout; i++) {
                        // adding imageuri in array
                        Uri imageurl = data.getClipData().getItemAt(i).getUri();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageurl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                        byte[] byteArray = outputStream.toByteArray();
                        // get base64 encoded string
                        listImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        // set encoded text on textview
                        imagesEncodedList.add(listImage);
                        byte[] bytes1 = Base64.decode(listImage, Base64.DEFAULT);
                        // Initialize bitmap
                        Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
                        // set bitmap on imageView
//                        captureImage.setImageBitmap(bitmap1);
                    }
                } else {
                    imagesEncodedList = new ArrayList<>();
                    Uri imageurl = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageurl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                    byte[] byteArray = outputStream.toByteArray();
                    // get base64 encoded string
                    listImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    // set encoded text on textview
                    imagesEncodedList.add(listImage);
                    byte[] bytes1 = Base64.decode(listImage, Base64.DEFAULT);
                    // Initialize bitmap
                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
                    // set bitmap on imageView
//                    captureImage.setImageBitmap(bitmap1);
                }

            } else if (requestCode == SAVE_REQUEST_CODE) {
                imagesEncodedList = new ArrayList<>();
                Uri uri = data.getData();
                try {
                    InputStream in = getContentResolver().openInputStream(uri);
                    byte[] bytes = getBytes(in);
                    sImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                    imagesEncodedList.add(sImage);
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonMethod.appendLogs(e.getMessage());
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + e.getMessage()));
                }


            }
        }

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void getPaybaseList() {
        if (CommonMethod.isNetworkConnected(this)) {

            //Request for get Booking Scan Data
            GetPayBasisListInputModel getPayBasisListInputModel = new GetPayBasisListInputModel();

            getPayBasisListInputModel.setLocation("MUMB");
            getPayBasisListInputModel.setLastFetchedDateTime("01 JAN 2000 00:00:00");
            getPayBasisListInputModel.setTenantId(10065);

            rl_progressBar = findViewById(R.id.rl_progressbar);

            rl_progressBar.setVisibility(View.VISIBLE);

            Call<GetPayBasisListOutputModel> call = MyRetro.createSecureServiceApp(this, APIService.class).getPayBasisList(getPayBasisListInputModel);
            call.enqueue(new Callback<GetPayBasisListOutputModel>() {
                @Override
                public void onResponse(Call<GetPayBasisListOutputModel> call, Response<GetPayBasisListOutputModel> response) {
                    rl_progressBar.setVisibility(View.GONE);
                    GetPayBasisListOutputModel model = response.body();

                    paybaseData.clear();

                    for (PayBasisListModel paybaseList : model.getPayBasisList()) {
                        paybaseData.add(paybaseList.getName());
                    }
                }

                @Override
                public void onFailure(Call<GetPayBasisListOutputModel> call, Throwable t) {
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            + SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                }
            });
        }
    }

    private void getBillingPartyList() {
        if (CommonMethod.isNetworkConnected(this)) {

            //Request for get Booking Scan Data
            GetPayBasisListInputModel getPayBasisListInputModel = new GetPayBasisListInputModel();

            getPayBasisListInputModel.setLocation("MUMB");
            getPayBasisListInputModel.setLastFetchedDateTime("01 JAN 2000 00:00:00");
            getPayBasisListInputModel.setTenantId(10065);

            rl_progressBar = findViewById(R.id.rl_progressbar);

            rl_progressBar.setVisibility(View.VISIBLE);

            Call<GetBillingPartyListOutputModel> call = MyRetro.createSecureServiceApp(this, APIService.class).getBillingParty(getPayBasisListInputModel);
            call.enqueue(new Callback<GetBillingPartyListOutputModel>() {
                @Override
                public void onResponse(Call<GetBillingPartyListOutputModel> call, Response<GetBillingPartyListOutputModel> response) {
                    rl_progressBar.setVisibility(View.GONE);
                    GetBillingPartyListOutputModel model = response.body();

                    billingPartyData.clear();

                    for (CustomerListModel customerList : model.getCustomerList()) {
                        billingPartyData.add(customerList.getCustomerName());
                    }
                }

                @Override
                public void onFailure(Call<GetBillingPartyListOutputModel> call, Throwable t) {
                    CommonMethod.appendLogs(t.getMessage());
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                }
            });
        }
    }

    private void getDestinationList() {
        if (CommonMethod.isNetworkConnected(this)) {

            //Request for get Booking Scan Data
            GetPayBasisListInputModel getPayBasisListInputModel = new GetPayBasisListInputModel();

            getPayBasisListInputModel.setLocation("MUMB");
            getPayBasisListInputModel.setLastFetchedDateTime("01 JAN 2000 00:00:00");
            getPayBasisListInputModel.setTenantId(10065);

            rl_progressBar = findViewById(R.id.rl_progressbar);

            rl_progressBar.setVisibility(View.VISIBLE);

            Call<GetDestinationListOutputModel> call = MyRetro.createSecureServiceApp(this, APIService.class).getDestination(getPayBasisListInputModel);
            call.enqueue(new Callback<GetDestinationListOutputModel>() {
                @Override
                public void onResponse(Call<GetDestinationListOutputModel> call, Response<GetDestinationListOutputModel> response) {
                    rl_progressBar.setVisibility(View.GONE);
                    GetDestinationListOutputModel model = response.body();

                    destinationData.clear();

                    for (LocationListModel customerList : model.getLocationList()) {
                        destinationData.add(customerList.getLocationName());
                    }
                }

                @Override
                public void onFailure(Call<GetDestinationListOutputModel> call, Throwable t) {
                    CommonMethod.appendLogs(t.getMessage());
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                }
            });
        }
    }

    private void getCityList() {
        if (CommonMethod.isNetworkConnected(this)) {

            //Request for get Booking Scan Data
            GetPayBasisListInputModel getPayBasisListInputModel = new GetPayBasisListInputModel();

            getPayBasisListInputModel.setLocation("MUMB");
            getPayBasisListInputModel.setLastFetchedDateTime("01 JAN 2000 00:00:00");
            getPayBasisListInputModel.setTenantId(10065);

            rl_progressBar = findViewById(R.id.rl_progressbar);

            rl_progressBar.setVisibility(View.VISIBLE);

            Call<GetCityListOutputModel> call = MyRetro.createSecureServiceApp(this, APIService.class).getCityList(getPayBasisListInputModel);
            call.enqueue(new Callback<GetCityListOutputModel>() {
                @Override
                public void onResponse(Call<GetCityListOutputModel> call, Response<GetCityListOutputModel> response) {
                    rl_progressBar.setVisibility(View.GONE);
                    GetCityListOutputModel model = response.body();

                    fromcityData.clear();
                    tocityData.clear();

                    for (CityListModel customerList : model.getCityList()) {
                        fromcityData.add(customerList.getCityName());
                        tocityData.add(customerList.getCityName());
                    }
                }

                @Override
                public void onFailure(Call<GetCityListOutputModel> call, Throwable t) {
                    CommonMethod.appendLogs(t.getMessage());
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                }
            });
        }
    }

    private void getProductList(){
        if (CommonMethod.isNetworkConnected(this)) {

            //Request for get Booking Scan Data
            GetPayBasisListInputModel getPayBasisListInputModel = new GetPayBasisListInputModel();

            getPayBasisListInputModel.setLocation("MUMB");
            getPayBasisListInputModel.setLastFetchedDateTime("01 JAN 2000 00:00:00");
            getPayBasisListInputModel.setTenantId(10065);

            rl_progressBar = findViewById(R.id.rl_progressbar);

            rl_progressBar.setVisibility(View.VISIBLE);

            Call<GetProductListOutputModel> call = MyRetro.createSecureServiceApp(this, APIService.class).getProductList(getPayBasisListInputModel);
            call.enqueue(new Callback<GetProductListOutputModel>() {
                @Override
                public void onResponse(Call<GetProductListOutputModel> call, Response<GetProductListOutputModel> response) {
                    rl_progressBar.setVisibility(View.GONE);
                    GetProductListOutputModel model = response.body();

                    productData.clear();

                    for (ProductListModel customerList : model.getProductList()) {
                        productData.add(customerList.getProductName());
                    }
                }

                @Override
                public void onFailure(Call<GetProductListOutputModel> call, Throwable t) {
                    CommonMethod.appendLogs(t.getMessage());
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                }
            });
        }
    }

    private void getPackageTypeList(){
        if (CommonMethod.isNetworkConnected(this)) {

            //Request for get Booking Scan Data
            GetPayBasisListInputModel getPayBasisListInputModel = new GetPayBasisListInputModel();

            getPayBasisListInputModel.setLocation("MUMB");
            getPayBasisListInputModel.setLastFetchedDateTime("01 JAN 2000 00:00:00");
            getPayBasisListInputModel.setTenantId(10065);

            rl_progressBar = findViewById(R.id.rl_progressbar);

            rl_progressBar.setVisibility(View.VISIBLE);

            Call<GetCityListOutputModel> call = MyRetro.createSecureServiceApp(this, APIService.class).getCityList(getPayBasisListInputModel);
            call.enqueue(new Callback<GetCityListOutputModel>() {
                @Override
                public void onResponse(Call<GetCityListOutputModel> call, Response<GetCityListOutputModel> response) {
                    rl_progressBar.setVisibility(View.GONE);
                    GetCityListOutputModel model = response.body();

                    fromcityData.clear();
                    tocityData.clear();

                    for (CityListModel customerList : model.getCityList()) {
                        fromcityData.add(customerList.getCityName());
                        tocityData.add(customerList.getCityName());
                    }
                }

                @Override
                public void onFailure(Call<GetCityListOutputModel> call, Throwable t) {
                    CommonMethod.appendLogs(t.getMessage());
                    firebaseCrashlytics.recordException(new RuntimeException(CommonMethod.getDeviceId(getAppContext())+" : " + Build.MODEL +" : "
                            +SharedPreference.getStringValue(ConstantData.SP_KEY_USERNAME)+" : " + t.getMessage()));
                }
            });
        }
    }

}