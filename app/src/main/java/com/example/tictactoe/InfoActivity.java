package com.example.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.util.List;

/**
 * This PDF Viewer was created by github user: barteksc
 * https://github.com/barteksc/AndroidPdfViewer
 *
 * I followed barteksc's installation guide as best as I could and when
 * I got stuck I found a solution on this stackoverflow page:
 * https://stackoverflow.com/questions/9666030/display-pdf-file-inside-my-android-application#42246889
 *
 * Note: For this to work you must create an assets folder inside your main folder
 * and your PDF filename should be all lowercase
 *
 * Also parts of the installation instructions are unclear, when it says to add to your build.gradle
 * file there are two files. Choose the one that says 'Module: app'
 *
 * And when it says to add this line to build.gradle: compile 'com.github.barteksc:android-pdf-viewer:3.1.0-beta.1'
 * the line must go inside the dependencies {} section and you should replace 'compile' with 'implementation'
 * because compile is outdated and will be removed in future versions
 *
 * Android will complain of version incompatibility. It will still run ok, but it should be fixed
 * to ensure future stability. The solution is here:
 * https://stackoverflow.com/questions/42374151/all-com-android-support-libraries-must-use-the-exact-same-version-specification
 *
 * In my case I needed to add two more lines of implementation:
 *     implementation 'com.android.support:support-media-compat:28.0.0'
 *     implementation 'com.android.support:support-v4:28.0.0'
 *
 */
public class InfoActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    PDFView pdfView;
    String pdfFileName;
    Integer pageNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        pdfView = findViewById(R.id.pdfView);
        displayFromAsset("manual.pdf");
    }

    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromAsset("manual.pdf")
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }


    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }
}





