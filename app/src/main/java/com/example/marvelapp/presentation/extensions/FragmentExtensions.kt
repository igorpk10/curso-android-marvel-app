package com.example.marvelapp.presentation.extensions

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment


fun Fragment.showShortToast(
    @StringRes messageResId: Int
) = Toast.makeText(requireContext(), messageResId, Toast.LENGTH_SHORT).show()