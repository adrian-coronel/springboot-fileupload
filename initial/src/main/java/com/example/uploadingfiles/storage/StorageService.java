package com.example.uploadingfiles.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Se utiliza para que el controlador pueda interactuar con una capa
 * de almacenamiento (como un sistema de archivos)
 * */
public interface StorageService {

	// Inicializa el servicio de almacenamiento
	void init();

	// Almacena un archivo representado por un objeto MultipartFile
	void store(MultipartFile file);

	// Retorna un flujo (Stream) de rutas (Path) que representan todos los archivos almacenados.
	Stream<Path> loadAll();

	// Retorna la ruta (Path) asociada al archivo con el nombre especificado.
	Path load(String filename);

	// Retorna un recurso (Resource) asociado al archivo con el nombre especificado.
	Resource loadAsResource(String filename);

	void deleteAll();

}
