/*package com.example.tp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import kotlinx.coroutines.delay
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                // Llamamos a la función principal que manejará agregar, buscar y eliminar libros
                BookManager()
            }
        }
    }
}

// Función principal que maneja la lista de libros, búsqueda, agregar y eliminar
@Composable
fun BookManager() {
    var books by remember { mutableStateOf(listOf<Libro>()) }
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var portadaUrl by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<Libro>()) }
    var showNoResults by remember { mutableStateOf(false) }
    var showAddConfirmation by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Sección de búsqueda
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), shape = RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text("Buscar libro por título o autor", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        }
                        innerTextField()
                    }
                }
            )
            Button(onClick = {
                searchResults = books.filter {
                    it.titulo.contains(searchQuery, ignoreCase = true) ||
                            it.autor.contains(searchQuery, ignoreCase = true)
                }
                showNoResults = searchResults.isEmpty() && searchQuery.isNotEmpty()
            }) {
                Text("Buscar")
            }
        }

        if (showNoResults) {
            Text(
                "No se encontraron libros con ese título o autor",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Mostrar resultados de búsqueda o todos los libros
        val displayedBooks = if (searchQuery.isNotEmpty()) searchResults else books
        BookList(books = displayedBooks, onDelete = { bookToDelete ->
            books = books.filter { it != bookToDelete }
        })

        Spacer(modifier = Modifier.height(16.dp))

        // Sección para agregar un nuevo libro
        Text(text = "Agregar nuevo libro", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = autor,
            onValueChange = { autor = it },
            label = { Text("Autor") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = anio,
            onValueChange = { anio = it },
            label = { Text("Año") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = genero,
            onValueChange = { genero = it },
            label = { Text("Género") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = isbn,
            onValueChange = { isbn = it },
            label = { Text("ISBN") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = portadaUrl,
            onValueChange = { portadaUrl = it },
            label = { Text("URL de la portada") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        Button(
            onClick = {
                val parsedAnio = anio.toIntOrNull() ?: 0
                val newBook = Libro(titulo, autor, parsedAnio, genero, isbn, portadaUrl)
                books = books + newBook
                titulo = ""
                autor = ""
                anio = ""
                genero = ""
                isbn = ""
                portadaUrl = ""
                showAddConfirmation = true
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Agregar libro")
        }

        // Mostrar confirmación de adición de libro al final
        if (showAddConfirmation) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text("✓", color = Color.Green, modifier = Modifier.padding(end = 8.dp))
                Text("Libro agregado con éxito", color = Color.Green)
            }
            LaunchedEffect(showAddConfirmation) {
                delay(3000)
                showAddConfirmation = false
            }
        }
    }
}

// Función para mostrar la lista de libros con la opción de eliminar
@Composable
fun BookList(books: List<Libro>, onDelete: (Libro) -> Unit) {
    LazyColumn {
        items(books) { book ->
            BookItem(book = book, onDelete = onDelete)
        }
    }
}

@Composable
fun BookItem(book: Libro, onDelete: (Libro) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = book.titulo, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow {
                item {
                    // Placeholder para la imagen de portada
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)), // Puedes agregar un borde
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Portada", color = Color.Gray) // Texto como placeholder
                    }
                }
                item {
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text("Autor: ${book.autor}")
                        Text("Año: ${book.anio}")
                        Text("Género: ${book.genero}")
                        Text("ISBN: ${book.isbn}")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onDelete(book) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Eliminar")
            }
        }
    }
}



// Función para previsualizar en el editor
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        BookManager()
    }
}

*/


package com.example.tp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImagePainter
import kotlinx.coroutines.delay
import coil.compose.rememberImagePainter


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                BookManager()
            }
        }
    }
}

@Composable
fun BookManager() {
    var books by remember { mutableStateOf(listOf<Libro>()) }
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var portadaUrl by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var showNoResults by remember { mutableStateOf(false) }
    var showAddConfirmation by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Filtrar los libros según la búsqueda

    val displayedBooks = if (searchQuery.isNotEmpty()) {
        books.filter {
            it.titulo.contains(searchQuery, ignoreCase = true) ||
                    it.autor.contains(searchQuery, ignoreCase = true)
        }.also { showNoResults = it.isEmpty() }
    } else {
        books.also { showNoResults = false }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Sección de búsqueda
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), shape = RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text("Buscar libro por título o autor", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        }
                        innerTextField()
                    }
                }
            )
            Button(onClick = {}) {
                Text("Buscar")
            }
        }

        if (showNoResults) {
            Text(
                "No se encontraron libros con ese título o autor",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Agregar LazyColumn para el listado de libros
        LazyColumn(
            modifier = Modifier.weight(1f) // Permitir que ocupe espacio restante
        ) {
            items(displayedBooks) { book ->
                BookItem(book = book) { bookToDelete ->
                    // Borrar libro y restablecer la búsqueda
                    books = books.filter { it != bookToDelete }
                    searchQuery = "" // Reiniciar la búsqueda
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección para agregar un nuevo libro
        Text(text = "Agregar nuevo libro", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = autor,
            onValueChange = { autor = it },
            label = { Text("Autor") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = anio,
            onValueChange = { anio = it },
            label = { Text("Año") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = genero,
            onValueChange = { genero = it },
            label = { Text("Género") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = isbn,
            onValueChange = { isbn = it },
            label = { Text("ISBN") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = portadaUrl,
            onValueChange = { portadaUrl = it },
            label = { Text("URL de la portada") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        // Mostrar mensaje de error si hay problemas con las validaciones
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(vertical = 4.dp))
        }

        Button(
            onClick = {
                // Validaciones
                when {
                    titulo.isBlank() || autor.isBlank() || anio.isBlank() || genero.isBlank() || isbn.isBlank() || portadaUrl.isBlank() -> {
                        errorMessage = "Todos los campos son obligatorios"
                    }
                    anio.toIntOrNull() == null || anio.toInt() <= 0 -> {
                        errorMessage = "El año debe ser un número mayor a 0"
                    }
                    isbn.length !in 10..13 -> {
                        errorMessage = "El ISBN debe tener entre 10 y 13 caracteres"
                    }
                    !portadaUrl.startsWith("http", ignoreCase = true) -> {
                        errorMessage = "La URL de la portada debe comenzar con 'http'"
                    }
                    else -> {
                        val parsedAnio = anio.toInt()
                        val newBook = Libro(titulo, autor, parsedAnio.toString(), genero, isbn, portadaUrl)
                        books = books + newBook
                        titulo = ""
                        autor = ""
                        anio = ""
                        genero = ""
                        isbn = ""
                        portadaUrl = ""
                        errorMessage = ""
                        showAddConfirmation = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Agregar libro")
        }

        // Mostrar confirmación de adición de libro al final
        if (showAddConfirmation) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text("✓", color = Color.Green, modifier = Modifier.padding(end = 8.dp))
                Text("Libro agregado con éxito", color = Color.Green)
            }
            LaunchedEffect(showAddConfirmation) {
                delay(3000)
                showAddConfirmation = false
            }
        }
    }
}

/*
@Composable
fun BookList(books: List<Libro>, onDelete: (Libro) -> Unit) {
    LazyColumn {
        items(books) { book ->
            BookItem(book = book, onDelete = onDelete)
        }
    }
} */

@Composable
fun BookItem(book: Libro, onDelete: (Libro) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = book.titulo, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow {
                item {
                    // Usar rememberImagePainter para cargar la imagen de la URL
                    val painter = rememberImagePainter(
                        data = book.portadaUrl,
                        builder = {
                            crossfade(true)
                            error(R.drawable.ic_broken_image) // Imagen de error, asegúrate de tener un recurso adecuado
                        }
                    )

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = "Portada de ${book.titulo}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Mostrar un texto de "Cargando..." mientras se carga la imagen
                        if (painter.state is AsyncImagePainter.State.Loading) {
                            Text("Cargando...", textAlign = TextAlign.Center)
                        }
                    }
                }
                item {
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text("Autor: ${book.autor}")
                        Text("Año: ${book.anio}")
                        Text("Género: ${book.genero}")
                        Text("ISBN: ${book.isbn}")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onDelete(book) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Eliminar")
            }
        }
    }
}


// Función para previsualizar en el editor
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        BookManager()
    }
}
