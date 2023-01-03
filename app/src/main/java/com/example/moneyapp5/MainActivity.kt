package com.example.moneyapp5

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.example.moneyapp5.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var fullSpeech = " I wish to thank Him who allowed me to return to my homeland so that I could return it to my German Reich! May every German realize the importance of the hour tomorrow, assess it and then bow his head in reverence before the will of the Almighty who has wrought this miracle in all of us within these past few weeks."
    private var fullList =fullSpeech.split(" ")
    private var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Firebase.database
        val myRef = database.getReference("/Array")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        myRef.get().addOnSuccessListener { Log.i("Firebase", "Got Value: ${it.value}") }

        val toast = Toast.makeText(this, myRef.toString(), Toast.LENGTH_SHORT)
        toast.show()
        val list = listOf<String>("Hello World", "Hello KingsMan")
        myRef.setValue(list)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var listView = findViewById<ListView>(R.id.listView)
        var names =mutableListOf<String>("Hello", "My", "Money App", "Is", "As 100")
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)
        listView.adapter = arrayAdapter

        listView.setOnItemClickListener { adapterView, view, i, l ->
            names.removeAt(i)
            arrayAdapter.notifyDataSetChanged()
        }

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener{
            var message = "No more words in the speech"
            if(index < fullList.size){
                names.add(fullList[index])
                index += 1
                message = names[names.size - 1]
                arrayAdapter.notifyDataSetChanged()
            }
            val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
            toast.show()
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
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}