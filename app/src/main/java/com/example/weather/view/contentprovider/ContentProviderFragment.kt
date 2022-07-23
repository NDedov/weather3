package com.example.weather.view.contentprovider

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weather.databinding.FragmentContentProviderBinding
import com.example.weather.utils.alertDialogPositiveAction

const val REQUEST_CODE_READ_CONTACTS = 999
const val REQUEST_CODE_CALL = 998

class ContentProviderFragment : Fragment() {
    private var _binding: FragmentContentProviderBinding? = null
    private val binding: FragmentContentProviderBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentProviderBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        val permResult =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)

        if (permResult == PackageManager.PERMISSION_GRANTED) {
            getContacts()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            context?.alertDialogPositiveAction(
                "Доступ к контактам",
                "Объяснение Объяснение Объяснение Объяснение",
                "Предоставить доступ",
                { permissionRequest(Manifest.permission.READ_CONTACTS, REQUEST_CODE_READ_CONTACTS)},
                "Не надо"
            )
        } else {
            permissionRequest(Manifest.permission.READ_CONTACTS, REQUEST_CODE_READ_CONTACTS)
        }

    }

    private fun permissionRequest(permission: String, requestCode: Int) {
        requestPermissions(arrayOf(permission), requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            for (pIndex in permissions.indices) {
                if (permissions[pIndex] == Manifest.permission.READ_CONTACTS
                    && grantResults[pIndex] == PackageManager.PERMISSION_GRANTED
                ) {
                    getContacts()
                    Log.d("@@@", "Ура")
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("Range")
    private fun getContacts() {
        val contentResolver: ContentResolver = requireContext().contentResolver
        // Отправляем запрос на получение контактов и получаем ответ в виде Cursor
        val cursorWithContacts: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )
        cursorWithContacts?.let { cursor ->
            for (i in 0 until cursor.count) { // аналог 0..cursorWithContacts.count-1
                cursor.moveToPosition(i)
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val contactId =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val numbers = getNumbersFromID(contentResolver, contactId)

                binding.containerForContacts.addView(TextView(requireContext()).apply {
                    text = name
                    textSize = 25f
                })
                numbers.forEach { number ->
                    binding.containerForContacts.addView(TextView(requireContext()).apply {
                        text = number
                        textSize = 20f
                        setOnClickListener {
                            makeCall(number)
                        }
                    })
                }
            }
        }
        cursorWithContacts?.close()
    }

    private fun makeCall(number: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
            startActivity(intent)
        } else {
            permissionRequest(Manifest.permission.CALL_PHONE, REQUEST_CODE_CALL)
        }
    }

    @SuppressLint("Range", "Recycle")
    private fun getNumbersFromID(cr: ContentResolver, contactId: String): List<String> {
        val phones = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null
        )
        val listNumbers = mutableListOf<String>()
        phones?.let { cursor ->
            while (cursor.moveToNext()) {
                listNumbers.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
            }
        }
        return listNumbers
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
