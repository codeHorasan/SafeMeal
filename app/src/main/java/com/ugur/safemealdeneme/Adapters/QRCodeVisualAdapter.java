package com.ugur.safemealdeneme.Adapters;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.ugur.safemealdeneme.Activities.QRCodeViewActivity;
import com.ugur.safemealdeneme.Models.QRCodeVisualModel;
import com.ugur.safemealdeneme.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCodeVisualAdapter extends RecyclerView.Adapter<QRCodeVisualAdapter.QRCodeVisualViewHolder> {
    ArrayList<QRCodeVisualModel> mQrList;

    public QRCodeVisualAdapter(ArrayList<QRCodeVisualModel> qrList) {
        mQrList = qrList;
    }

    public static class QRCodeVisualViewHolder extends RecyclerView.ViewHolder {
        public ImageView qrImage;
        public TextView qrInfo;
        public Button qrButton;

        public QRCodeVisualViewHolder(@NonNull View itemView) {
            super(itemView);
            qrImage = itemView.findViewById(R.id.image_view_qrcode);
            qrInfo = itemView.findViewById(R.id.text_view_qrcode);
            qrButton = itemView.findViewById(R.id.button_qr_code_download);
        }
    }

    @NonNull
    @Override
    public QRCodeVisualViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_qrcode,parent,false);
        QRCodeVisualViewHolder qrCodeVisualAdapter = new QRCodeVisualViewHolder(v);
        return qrCodeVisualAdapter;
    }

    @Override
    public void onBindViewHolder(@NonNull final QRCodeVisualViewHolder holder, int position) {
        final QRCodeVisualModel currentItem = mQrList.get(position);
        String qrString = currentItem.getQrCompositeID();
        QRGEncoder qrgEncoder = new QRGEncoder(qrString,null, QRGContents.Type.TEXT,100);
        Bitmap qrBits = qrgEncoder.getBitmap();
        holder.qrImage.setImageBitmap(qrBits);
        holder.qrInfo.setText(currentItem.getQrInfo());
        final OutputStream[] outputStream = new OutputStream[1];

        holder.qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BitmapDrawable drawable = (BitmapDrawable) holder.qrImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                File filepath = Environment.getExternalStorageDirectory();
                File dir = new File(filepath.getPath() + "/" + currentItem.getQrInfo() + "/");
                dir.mkdir();
                File file = new File(dir,currentItem.getQrInfo() + ".jpg");
                try {
                    outputStream[0] = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                bitmap.compress(Bitmap.CompressFormat.JPEG,100, outputStream[0]);

                try {
                    outputStream[0].flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    outputStream[0].close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MediaScannerConnection.scanFile(v.getContext(), new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);

                StyleableToast.makeText(QRCodeViewActivity.context,"Downloaded Successfully!",R.style.SuccessToast).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mQrList.size();
    }


}
