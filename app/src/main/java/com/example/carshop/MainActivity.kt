package com.example.carshop

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.view.LayoutInflater
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.layout_dialog.view.*


class MainActivity : AppCompatActivity(), CarAdapter.OnItemClickListener {
    private val carList = ArrayList<CarItem>()
    private val adapter = CarAdapter(carList, this)
    private val db = DataBaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        findViewById<FloatingActionButton>(R.id.add_car).setOnClickListener {
            openDialog("Add", CarItem())
        }
        reloadList()
    }

    private fun reloadList() {
        val data = db.readData()
        carList.clear()
        carList.sortBy { it.id }
        carList.addAll(data)
    }

    private fun openDialog(typeOfAction: String, carSelected: CarItem) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("$typeOfAction Car")
        val mAlertDialog = mBuilder.show()

        if (typeOfAction == "Modify") {
            mDialogView.dialog_add.text = typeOfAction
            mDialogView.edit_model.setText(carSelected.model)
            mDialogView.edit_seats.setText(carSelected.seats.toString())
            mDialogView.edit_date.setText(carSelected.date.toString())
            mDialogView.edit_category.setSelection(
                getIndex(
                    mDialogView.edit_category,
                    carSelected.category
                )
            )
            mDialogView.edit_condition.setSelection(
                getIndex(
                    mDialogView.edit_condition,
                    carSelected.condition
                )
            )
            mDialogView.edit_price.setText(carSelected.price.toString())
        }

        mDialogView.dialog_add.setOnClickListener {
            mAlertDialog.dismiss()
            var id = if (typeOfAction == "Modify") {
                carSelected.id
            } else {
                carList.size + 1
            }
            val model = mDialogView.edit_model.text.toString()
            val seats = Integer.parseInt(mDialogView.edit_seats.text.toString())
            val date = Integer.parseInt(mDialogView.edit_date.text.toString())
            val category = mDialogView.edit_category.selectedItem.toString()
            val condition = mDialogView.edit_condition.selectedItem.toString()
            val price = Integer.parseInt(mDialogView.edit_price.text.toString())
            var aux: String = ""
            print(category)
            print(category == "Electric")
            if (category == "Electric") {
                aux = getCapacity()
            }
            if (category == "Truck") {
                aux = getMaxPayload()
            }
            if (category == "Commercial") {
                aux = getStorage()
            }

            val newCar = CarItem(id, model, seats, date, category, condition, price, aux)

            if (typeOfAction == "Modify") {
                modifyCar(newCar)
            } else {
                insertCar(newCar)
            }

        }
    }

    @SuppressLint("PrivateApi")
    private fun getCapacity(): String {
        var mPowerProfile: Any? = null
        val powerProfileClass = "com.android.internal.os.PowerProfile"
        try {
            mPowerProfile = Class.forName(powerProfileClass)
                .getConstructor(Context::class.java).newInstance(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return try {
            val batteryCapacity = Class.forName(powerProfileClass)
                .getMethod("getAveragePower", String::class.java)
                .invoke(mPowerProfile, "battery.capacity") as Double
            ("Capacity: $batteryCapacity mAh")
        } catch (e: Exception) {
            "Error"
        }
    }

    private fun getMaxPayload(): String {
        return "Payload: "
    }

    private fun getStorage(): String {
        val stat = StatFs(Environment.getExternalStorageDirectory().path)
        val bytesAvailable = stat.blockSize.toLong() * stat.blockCount.toLong()
        val megAvailable = bytesAvailable / 1048576
        return ("Capacity: $megAvailable MB")
    }


    private fun insertCar(newCar: CarItem) {
        db.insertData(newCar)
        carList.add(0, newCar)
        adapter.notifyItemInserted(0)
    }

    private fun modifyCar(car: CarItem) {
        db.modifyData(car.id, car)
        carList.set(carList.size - car.id, car)
        adapter.notifyItemChanged(carList.size - car.id)
    }

    override fun onItemClick(position: Int) {
        val clickedItem: CarItem = carList[position]
        if (carList[position].category != "Electric") {
            openDialog("Modify", clickedItem)
        }
        Toast.makeText(this, carList[position].aux, Toast.LENGTH_SHORT).show()

    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        var index = 0
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i) == myString) {
                index = i
            }
        }
        return index
    }


}