package com.jfl.appcontatos.ui.contact

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.SurfaceCoroutineScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jfl.appcontatos.R
import com.jfl.appcontatos.data.Contact
import com.jfl.appcontatos.ui.theme.AppContatosTheme
import com.jfl.appcontatos.ui.utils.compasables.ContactAvatar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ContactListScreen(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
   val isInitialComposition: MutableState<Boolean> = rememberSaveable { mutableStateOf(true) }
   val isLoading: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
   val hasError: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
   val contacts: MutableState<List<Contact>> = remember{ mutableStateOf(listOf()) }

   val loadContacts: () -> Unit = {
       isLoading.value = true
       hasError.value = false

       coroutineScope.launch{
           delay(2000)
           contacts.value = generateContacts()
           isLoading.value = false
           hasError.value = false
       }
   }

    val toggleFavorite: (Contact) -> Unit = { contact->
       contacts.value = contacts.value.map {
           if (it.id == contact.id)
               it.copy(isFavorite = !it.isFavorite)
           else
               it
       }
    }

    if (isInitialComposition.value){
       loadContacts()
       isInitialComposition.value = false
    }

    val contentModifier = modifier.fillMaxSize()
    if(isLoading.value)
        LoadingContent(modifier = contentModifier)
    else if(hasError.value)
        ErrorContent(modifier = contentModifier, onTryAgainPressed = loadContacts)
    else
    {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = { AppBar(
                onRefreshPressed = loadContacts
            )},
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = {
                    contacts.value = contacts.value.plus(
                        Contact(firstName="Teste", lastName = "Testenildo")
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Adicionar"
                    )
                    Spacer(Modifier.size(8.dp))
                    Text("Novo Contato")
                }
            }
        ){ paddingValues ->
            val defaultModifier = Modifier.padding(paddingValues)
            if(contacts.value.isEmpty()){
                EmptyList(modifier = defaultModifier)
            }
            else{
                List(modifier = defaultModifier, contacts = contacts.value, onFavoritePressed = toggleFavorite)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar (
    modifier: Modifier = Modifier,
    onRefreshPressed: ()->Unit
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.contacts))},
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            IconButton(onClick = { onRefreshPressed() }) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription =  stringResource(R.string.refresh)
                )

            }
        }


    )
}

@Preview(showBackground = true)
@Composable
private fun AppBarPreview(modifier: Modifier = Modifier) {
    AppContatosTheme {
        AppBar(onRefreshPressed = {})
    }
}

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(60.dp)
        )
        Spacer(Modifier.size(8.dp))
        Text(
           // modifier = Modifier.padding(top = 8.dp),
            text = stringResource(R.string.loading_contacts),
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.Blue
            )
        )

     }

}

@Preview(showBackground = true, heightDp = 400)
@Composable
fun LoadingContentPreview(modifier: Modifier = Modifier) {
     AppContatosTheme {
        LoadingContent()
     }
}

@Composable
fun ErrorContent(
    modifier: Modifier = Modifier,
    onTryAgainPressed:() -> Unit

) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val defaultColor = MaterialTheme.colorScheme.primary
        Icon(
            imageVector = Icons.Filled.CloudOff,
            contentDescription = "Erro ao carregar",
            tint = defaultColor,
            modifier = Modifier.size(80.dp)
        )
        val textPadding = PaddingValues(
            top = 8.dp,
            start = 8.dp,
            end = 8.dp
        )
        Text(
            modifier = Modifier.padding(textPadding),
            text  = stringResource(R.string.loading_error),
            style = MaterialTheme.typography.titleLarge,
            color = defaultColor
        )
        Text(
            modifier = Modifier.padding(textPadding),
            text  = stringResource(R.string.wait_and_try_again),
            style = MaterialTheme.typography.titleSmall,
            color = defaultColor
        )
        ElevatedButton(
            onClick = onTryAgainPressed,
            modifier = Modifier.padding(top= 16.dp)
        )
        {
            Text(stringResource(R.string.try_again))
        }


    }
    
}

@Preview(showBackground = true, heightDp = 400)
@Composable
fun ErrorContentPreview(modifier: Modifier = Modifier) {
    AppContatosTheme {
        ErrorContent(
            onTryAgainPressed={}
        )
    }
}

@Composable
private fun EmptyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(all = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Image(
            painter = painterResource(R.drawable.no_data),
            contentDescription = stringResource(R.string.nothing_here)
        )
        Text(
            text = stringResource(R.string.nothing_here),
            modifier = Modifier.padding(top=16.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Você ainda não adicionou nenhum contato."
            +"\nAdicione o primeiro utilizando o botão: \"Novo contato\"",
            modifier = Modifier.padding(top=16.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyListPreview(modifier: Modifier = Modifier) {
    AppContatosTheme {
        EmptyList()
    }
}

@Composable
fun List(
    modifier: Modifier = Modifier,
    contacts: List<Contact>,
    onFavoritePressed: (Contact) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .height(500.dp)
    ){
        //contacts.forEach{ contact -> ContactItem(contact = contact)} -- nao usa com LazyColumn
        items(contacts){ contact -> ContactItem(contact = contact, onFavoritePressed = onFavoritePressed)}
    }
}

@Composable
fun ContactItem(
    modifier: Modifier = Modifier,
    contact: Contact,
    onFavoritePressed: (Contact) -> Unit
) {
    ListItem(
        modifier = modifier,
        headlineContent = { Text(contact.fullName) },
        leadingContent = {
            ContactAvatar(firstName = contact.firstName , lastName = contact.lastName)
        },
        trailingContent = {
            IconButton(onClick = {onFavoritePressed(contact)}) {
                Icon(
                    imageVector = if (contact.isFavorite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    contentDescription = stringResource(R.string.favorite),
                    tint = if (contact.isFavorite) {
                        Color.Red
                    } else {
                        LocalContentColor.current
                    }
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ListPreview(modifier: Modifier = Modifier) {
    AppContatosTheme {
        List(
            contacts = generateContacts(),
            onFavoritePressed = {}
        )
    }
}

private fun generateContacts(): List<Contact> {
    val lNames = listOf( "Conor McGregor", "Khabib Nurmagomedov", "Jon Jones", "Georges St-Pierre", "Anderson Silva", "Israel Adesanya", "Amanda Nunes", "Ronda Rousey", "Daniel Cormier", "Stipe Miocic", "Francis Ngannou", "Henry Cejudo", "Jose Aldo", "Max Holloway", "Tony Ferguson", "Dustin Poirier", "Nate Diaz", "Charles Oliveira", "Valentina Shevchenko", "Joanna Jedrzejczyk" )

    return lNames.mapIndexed { index, fullName ->
        val parts = fullName.split(" ")
        val firstName = parts.first()
        val lastName = parts.last()
        Contact(id = index + 1, firstName = firstName, lastName = lastName)
    }
}
