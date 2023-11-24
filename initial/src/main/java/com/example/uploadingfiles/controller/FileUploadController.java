package com.example.uploadingfiles.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.uploadingfiles.storage.StorageFileNotFoundException;
import com.example.uploadingfiles.storage.StorageService;

@Controller
public class FileUploadController {

   private final StorageService storageService;

   @Autowired
   public FileUploadController(StorageService storageService){
      this.storageService = storageService;
   }

   /**
    * busca la lista actual de archivos cargados desde StorageServicey la carga en una plantilla
    * de Thymeleaf. Calcula un enlace al recurso real utilizando MvcUriComponentsBuilder.
    * */
   @GetMapping("/")
   public String listUploadedFiles(Model model) throws IOException {

      model.addAttribute("files", storageService.loadAll().map(
              path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                  "serveFile", path.getFileName().toString()).build().toUri().toString())
          .collect(Collectors.toList()));

      return "uploadForm";
   }

   /**
    * Carga el recurso (si existe) y lo envía al navegador para descargarlo
    * mediante un Content-Dispositionencabezado de respuesta.
    * */
   @GetMapping("/files/{filename:.+}")
   @ResponseBody
   public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

      Resource file = storageService.loadAsResource(filename);

      if (file == null)
         return ResponseEntity.notFound().build();

      return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
          "attachment; filename=\"" + file.getFilename() + "\"").body(file);
   }

   /**
    * Maneja un mensaje de varias partes file y se lo entrega
    * StorageService para que lo guarde.
    * */
   @PostMapping("/")
   public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                  RedirectAttributes redirectAttributes) {

      storageService.store(file);
      redirectAttributes.addFlashAttribute("message",
          "You successfully uploaded " + file.getOriginalFilename() + "!");

      return "redirect:/";
   }

   @ExceptionHandler(StorageFileNotFoundException.class)
   public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
      return ResponseEntity.notFound().build();
   }

}
