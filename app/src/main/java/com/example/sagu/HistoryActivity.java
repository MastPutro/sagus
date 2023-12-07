package com.example.sagu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HistoryActivity extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        btn = findViewById(R.id.buttonasu);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printToBluetoothPrinter(view);
            }
        });

    }
    public void printToBluetoothPrinter(View view) {
        String barang = "Barang: Meja";
        String harga = "Harga: $100";
        String dataToPrint = barang + "\n" + harga;

        // Menggunakan PrintManager untuk mencetak data ke printer Bluetooth
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = new MyPrintDocumentAdapter(dataToPrint);

        if (printManager != null) {
            String jobName = getString(R.string.app_name) + " Document";
            printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
        }

        Toast.makeText(this, "Data telah terkirim ke printer Bluetooth", Toast.LENGTH_SHORT).show();
    }

    private class MyPrintDocumentAdapter extends PrintDocumentAdapter {
        private String dataToPrint;

        MyPrintDocumentAdapter(String dataToPrint) {
            this.dataToPrint = dataToPrint;
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
            } else {
                PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("document_name").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();
                callback.onLayoutFinished(pdi, true);
            }
        }

        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
            if (cancellationSignal.isCanceled()) {
                callback.onWriteCancelled();
            } else {
                try {
                    InputStream input = new ByteArrayInputStream(dataToPrint.getBytes());
                    OutputStream output = new FileOutputStream(destination.getFileDescriptor());

                    byte[] buf = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = input.read(buf)) > 0) {
                        output.write(buf, 0, bytesRead);
                    }

                    callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

                } catch (FileNotFoundException e) {
                    // Handle the exception
                } catch (IOException e) {
                    // Handle the exception
                }
            }
        }
    }
}
