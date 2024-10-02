package com.jfl.appcontatos.ui.contact.details

import androidx.collection.emptyLongSet
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jfl.appcontatos.R
import com.jfl.appcontatos.data.Contact
import com.jfl.appcontatos.ui.theme.AppContatosTheme
import com.jfl.appcontatos.ui.utils.compasables.ContactAvatar
import com.jfl.appcontatos.ui.utils.compasables.FavoriteIconButton
import java.time.format.DateTimeFormatter


@Composable
fun ContactDetailsScreen(modifier: Modifier = Modifier) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    isDeleting: Boolean,
    contact: Contact,
    onBackPressed: () ->Unit,
    onDeletePressed: () ->Unit,
    onEditPressed: () ->Unit,
    onFavoritePressed: () ->Unit,
) {
    TopAppBar(
          modifier = modifier.fillMaxWidth(),
          title = { Text("") },
          colors = TopAppBarDefaults.topAppBarColors(
              titleContentColor = MaterialTheme.colorScheme.primary,
              actionIconContentColor = MaterialTheme.colorScheme.primary,
              navigationIconContentColor = MaterialTheme.colorScheme.primary
          ),
          navigationIcon = {
              IconButton(onClick = onBackPressed) {
                  Icon(
                      imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                      contentDescription = stringResource(R.string.back)
                  )
              }
          },
          actions = {
              if(isDeleting){
                  CircularProgressIndicator(
                      modifier = Modifier
                          .size(60.dp)
                          .padding(all = 16.dp),
                      strokeWidth = 2.dp
                  )
              }
              else
              {
                  IconButton(onClick = onEditPressed) {
                      Icon(
                          imageVector = Icons.Filled.Edit,
                          contentDescription = stringResource(R.string.edit)
                      )
                  }
                  FavoriteIconButton(
                      isFavorite = contact.isFavorite,
                      onPressed = onFavoritePressed)

                  IconButton(onClick = onDeletePressed) {
                      Icon(
                          imageVector = Icons.Filled.Delete,
                          contentDescription = stringResource(R.string.delete)
                      )
                  }

              }
          }
    )
}

@Preview
@Composable
fun AppBarPreview() {
    AppContatosTheme {
        AppBar(
            isDeleting = false,
            contact = Contact(),
            onBackPressed = {},
            onDeletePressed = {},
            onEditPressed = {},
            onFavoritePressed = {}
        )
    }
}

@Preview
@Composable
fun AppBarPreviewDeleting() {
    AppContatosTheme {
        AppBar(
            isDeleting = true,
            contact = Contact(),
            onBackPressed = {},
            onDeletePressed = {},
            onEditPressed = {},
            onFavoritePressed = {}
        )
    }
}

@Preview
@Composable
fun AppBarPreviewDeletingIsFavorite() {
    AppContatosTheme {
        AppBar(
            isDeleting = false,
            contact = Contact(isFavorite = true),
            onBackPressed = {},
            onDeletePressed = {},
            onEditPressed = {},
            onFavoritePressed = {}
        )
    }
}


@Composable
fun ContactDetails(
    modifier: Modifier = Modifier,
    contact: Contact,
    isDeleting: Boolean
) {
    Column(
        modifier = modifier
            .padding(top = 24.dp)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ContactAvatar(
            firstName = contact.firstName,
            lastName = contact.lastName,
            size = 150.dp,
            textStyle = MaterialTheme.typography.displayLarge
        )
        Spacer(Modifier.size(24.dp))
        Text(
            text = contact.fullName,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.size(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            QuickAction(
                imageVector = Icons.Filled.Phone,
                text = stringResource(R.string.call),
                onPressed = {},
                enabled = contact.phoneNumber.isNotBlank() && !isDeleting
            )
            QuickAction(
                imageVector = Icons.Filled.Sms,
                text = stringResource(R.string.mensagem),
                onPressed = {},
                enabled = contact.phoneNumber.isNotBlank() && !isDeleting
            )
            QuickAction(
                imageVector = Icons.Filled.Videocam,
                text = stringResource(R.string.video),
                onPressed = {},
                enabled = contact.phoneNumber.isNotBlank() && !isDeleting
            )
            QuickAction(
                imageVector = Icons.Filled.Email,
                text = "Email",
                onPressed = {},
                enabled = contact.email.isNotBlank() && !isDeleting
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ){
            Text(
                modifier = Modifier.padding(all = 16.dp),
                text = stringResource(R.string.contact_info),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            ContactInfo(
                imageVector = Icons.Outlined.Phone,
                value = contact.phoneNumber.ifBlank { stringResource(R.string.add_fone_number) },
                enabled = contact.phoneNumber.isBlank() && !isDeleting,
                onPressed={}
            )
            ContactInfo(
                imageVector = Icons.Outlined.Email,
                value = contact.email.ifBlank { stringResource(R.string.add_email) },
                enabled = contact.email.isBlank() && !isDeleting,
                onPressed={}
            )
            Spacer(Modifier.size(8.dp))
        }
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        val formattedDateTime = contact.createdAt.format(
            DateTimeFormatter.ofPattern( "dd/MM/yyyy HH:mm" )
        )
        Text(
            text = stringResource(R.string.add_in, formattedDateTime),
            style = MaterialTheme.typography.labelSmall
        )


    }
    
}

@Preview(showBackground = true)
@Composable
fun ContactDetailsPreview() {
    AppContatosTheme {
        ContactDetails(
            contact = Contact(
                firstName = "João",
                lastName = "Kleber"
            ),
            isDeleting = false
        )
    }
}

@Composable
fun QuickAction(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    text: String,
    onPressed: () -> Unit,
    enabled: Boolean = true
) {
   Column(
       modifier = modifier,
       horizontalAlignment = Alignment.CenterHorizontally
   ){
       FilledIconButton(
           enabled = enabled,
           onClick = onPressed
       ) {
           Icon(
               imageVector = imageVector,
               contentDescription = text
           )
       }
       Text(
           text = text,
           style = MaterialTheme.typography.labelSmall
       )
   }
}

@Composable
fun ContactInfo(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    value: String,
    enabled: Boolean,
    onPressed: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(all = 16.dp)
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = onPressed
            ),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = imageVector,
            contentDescription = value
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall
        )
    }
}