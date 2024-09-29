package com.jfl.appcontatos.data

class ContactDataSource private constructor() {
    companion object {
        val instance: ContactDataSource by lazy {
            ContactDataSource()
        }
    }

    private val contacts: MutableList<Contact> = mutableListOf()

    init {
        contacts.addAll(generateContacts())
    }

    fun findAll(): List<Contact> = contacts.toList()

    fun findById(id: Int): Contact? = contacts.firstOrNull { it.id == id }

    fun save(contact: Contact): Contact = if (contact.id > 0) {
        val index: Int = contacts.indexOfFirst { it.id == contact.id }
        contact.also { contacts[index] = it }
    } else {
        val maxId: Int = contacts.maxBy { it.id }.id
        contact.copy(id = maxId + 1).also { contacts.add(it) }
    }

    fun delete(contact: Contact){
        if(contact.id > 0){
            contacts.removeIf{ it.id == contact.id}
        }
    }
}

fun generateContacts(): List<Contact> {
    val lNames = listOf( "Conor McGregor", "Khabib Nurmagomedov", "Jon Jones", "Georges St-Pierre", "Anderson Silva", "Israel Adesanya", "Amanda Nunes", "Ronda Rousey", "Daniel Cormier", "Stipe Miocic", "Francis Ngannou", "Henry Cejudo", "Jose Aldo", "Max Holloway", "Tony Ferguson", "Dustin Poirier", "Nate Diaz", "Charles Oliveira", "Valentina Shevchenko", "Joanna Jedrzejczyk" )

    return lNames.mapIndexed { index, fullName ->
        val parts = fullName.split(" ")
        val firstName = parts.first()
        val lastName = parts.last()
        Contact(id = index + 1, firstName = firstName, lastName = lastName)
    }
}
