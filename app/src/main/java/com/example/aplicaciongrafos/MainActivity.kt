package com.example.aplicaciongrafos


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.example.aplicaciongrafos.Excepciones.ExcepcionPosicionDelVerticeInvalida
import com.example.aplicaciongrafos.Pesados.Dijkstra
import com.example.aplicaciongrafos.Pesados.GrafoPesado
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar

class MainActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap
    private var currentLocation: LatLng? = null
    private val destinationMarkers = mutableListOf<LatLng>()
    private lateinit var grafo: GrafoPesado
    private lateinit var tvRuta: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // iniciamos el grafo
        grafo = GrafoPesado()

        // aca iniciamos el textview
        tvRuta = findViewById(R.id.tv_ruta)

        // configuramos los botones
        findViewById<Button>(R.id.btn_dijkstra).setOnClickListener {
            calcularRutaDijkstra()
        }
        findViewById<Button>(R.id.btn_limpiar).setOnClickListener {
            limpiarMarcadores()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        googleMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                currentLocation = LatLng(location.latitude, location.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation!!, 15f))
            }
        }

        googleMap.setOnMapClickListener { latLng ->
            googleMap.addMarker(MarkerOptions().position(latLng).title("Marker ${destinationMarkers.size + 1}"))
            destinationMarkers.add(latLng)
            grafo.insertarVertice()
            if (destinationMarkers.size > 1) {
                val origen = destinationMarkers[destinationMarkers.size - 2]
                val destino = latLng
                val distancia = calcularDistancia(origen, destino)
                grafo.insertarArista(destinationMarkers.size - 2, destinationMarkers.size - 1, distancia)
                googleMap.addPolyline(PolylineOptions().add(origen, destino).color(Color.BLUE).width(5f))
            }
        }
    }

    private fun calcularDistancia(origen: LatLng, destino: LatLng): Double {
        val resultado = FloatArray(1)
        Location.distanceBetween(
            origen.latitude, origen.longitude,
            destino.latitude, destino.longitude,
            resultado
        )
        return resultado[0].toDouble()
    }

    private fun calcularRutaDijkstra() {
        if (currentLocation == null || destinationMarkers.size < 2) {
            Snackbar.make(findViewById(android.R.id.content), "Por favor, selecciona al menos dos ubicaciones.", Snackbar.LENGTH_LONG).show()
            return
        }
        val rutaIndices = obtenerRutaMasCortaIndices(currentLocation!!, destinationMarkers)
        if (rutaIndices.isNotEmpty()) {
            val rutaStr = rutaIndices.joinToString(" -> ")
            tvRuta.text = "Ruta más corta: $rutaStr"
        } else {
            tvRuta.text = "No se encontró una ruta válida."
        }
        val rutaLatLng = obtenerRutaMasCorta(currentLocation!!, destinationMarkers)
        dibujarRuta(rutaLatLng, Color.RED)
    }

    private fun obtenerRutaMasCortaIndices(origen: LatLng, destinos: List<LatLng>): List<Int> {
        val origenIndex = obtenerIndiceVertice(origen)
        val destinoIndices = destinos.map { obtenerIndiceVertice(it) }

        val dijkstra = Dijkstra(grafo)
        val rutaIndices = mutableListOf<Int>()

        for (destinoIndex in destinoIndices) {
            if (destinoIndex < grafo.cantidadDeVertices()) {
                val rutaParcial = try {
                    dijkstra.encontrarRutaMasCorta(origenIndex, destinoIndex)
                } catch (e: Exception) {
                    Log.e("Dijkstra", "Error al calcular la ruta: ${e.message}")
                    emptyList<Int>()
                }
                rutaIndices.addAll(rutaParcial)
            } else {
                Log.e("Dijkstra", "Índice de destino fuera de los límites: $destinoIndex")
            }
        }

        Log.d("Dijkstra", "Ruta más corta encontrada: $rutaIndices")
        return rutaIndices
    }

    private fun obtenerRutaMasCorta(origen: LatLng, destinos: List<LatLng>): List<LatLng> {
        val origenIndex = obtenerIndiceVertice(origen)
        val destinoIndices = destinos.map { obtenerIndiceVertice(it) }

        val dijkstra = Dijkstra(grafo)
        val rutaIndices = mutableListOf<Int>()

        for (destinoIndex in destinoIndices) {
            if (destinoIndex < grafo.cantidadDeVertices()) {
                val rutaParcial = try {
                    dijkstra.encontrarRutaMasCorta(origenIndex, destinoIndex)
                } catch (e: Exception) {
                    Log.e("Dijkstra", "Error al calcular la ruta: ${e.message}")
                    emptyList<Int>()
                }
                rutaIndices.addAll(rutaParcial)
            } else {
                Log.e("Dijkstra", "Índice de destino fuera de los límites: $destinoIndex")
            }
        }

        val rutaLatLng = mutableListOf<LatLng>()
        Log.d("Dijkstra", "Ruta más corta encontrada: $rutaIndices")

        for (indice in rutaIndices) {
            val verticeLatLng = obtenerLatLngDesdeIndice(indice)
            rutaLatLng.add(verticeLatLng)
            Log.d("Dijkstra", "Añadiendo punto a la ruta: $verticeLatLng")
        }
        return rutaLatLng
    }

    private fun obtenerIndiceVertice(latLng: LatLng): Int {
        return when (latLng) {
            currentLocation -> 0
            in destinationMarkers -> destinationMarkers.indexOf(latLng) + 1
            else -> throw IllegalArgumentException("Coordenadas no válidas")
        }
    }

    private fun obtenerLatLngDesdeIndice(indice: Int): LatLng {
        return when (indice) {
            0 -> currentLocation ?: LatLng(0.0, 0.0)
            in 1..destinationMarkers.size -> destinationMarkers[indice - 1]
            else -> throw IllegalArgumentException("Índice no válido")
        }
    }

    private fun dibujarRuta(ruta: List<LatLng>, color: Int) {
        val polylineOptions = PolylineOptions().addAll(ruta).color(color).width(5f)
        googleMap.addPolyline(polylineOptions)
    }

    private fun limpiarMarcadores() {
        googleMap.clear()
        destinationMarkers.clear()
        grafo = GrafoPesado()
        tvRuta.text = "Ruta más corta: "
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}