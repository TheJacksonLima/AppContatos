package com.jfl.appcontatos.ui.contact.list

import com.jfl.appcontatos.data.Contact

data class ContactsListUiState(
    val isLoading: Boolean = false,
    val hasError:  Boolean = false,
    val contacts: Map<String, List<Contact>> = mapOf()
)

