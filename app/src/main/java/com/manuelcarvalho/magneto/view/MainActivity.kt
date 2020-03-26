package com.manuelcarvalho.magneto.view


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.manuelcarvalho.magneto.R
import com.manuelcarvalho.magneto.viewmodel.ListViewModel
import io.reactivex.plugins.RxJavaPlugins
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

//pdf methods from https://devofandroid.blogspot.com/2018/11/write-pdf-android-studio-kotlin.html

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ListViewModel
    private val STORAGE_CODE: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        RxJavaPlugins.setErrorHandler { throwable: Throwable? -> }

        viewModel = ViewModelProviders.of(this)[ListViewModel::class.java]
        viewModel.refresh()

//        ViewModelProvider(
//            this,
//            ViewModelProvider.AndroidViewModelFactory(this.application)
//        ).get(ListViewModel::class.java)

//        val zipFile = File("path_to_your_zip_file")
//        zipFile.unzip()

        fab.setOnClickListener { view ->
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                //system OS >= Marshmallow(6.0), check permission is enabled or not
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    //permission was not granted, request it
                    val permissions = arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    requestPermissions(permissions, STORAGE_CODE)
                } else {
                    //permission already granted, call savePdf() method
                    savePdf()
                }
            } else {
                //system OS < marshmallow, call savePdf() method
//                savePdf()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_about -> {
                showDialogue()
                return true
            }
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun savePdf() {
        //create object of Document class
        val mDoc = Document()
        //pdf file name
        val mFileName = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())
        //pdf file path
        val mFilePath =
            Environment.getExternalStorageDirectory().toString() + "/" + mFileName + ".pdf"
        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))

            //open the document for writing
            mDoc.open()

            //get text from EditText i.e. textEt
            val mText = "Test string"
            //add author of the document (metadata)
            mDoc.addAuthor("Manuel Carvalho")

            //add paragraph to the document
            mDoc.add(Paragraph(mText))

            //close document
            mDoc.close()

            //show file saved message with file name and path
            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT)
                .show()
        } catch (e: Exception) {
            //if anything goes wrong causing exception, get and show exception message
            Log.d(TAG, e.message)
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted, call savePdf() method
                    savePdf()
                } else {
                    //permission from popup was denied, show error message
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun showDialogue() {
        val builder = AlertDialog.Builder(this@MainActivity)

        // Set the alert dialog title
        builder.setTitle("About Magneto")

        // Display a message on alert dialog
        builder.setMessage("Magneto was developed by Manuel carvalho")

        builder.setNeutralButton("O.K") { _, _ ->

        }


        val dialog: AlertDialog = builder.create()
        dialog.show()
    }



}
