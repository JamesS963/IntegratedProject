package main.controllers;

import main.dao.DocumentDao;
import main.dao.UserDao;
import main.models.Document;
import main.models.User;
import main.storage.StorageFileNotFoundException;
import main.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Created by Dean on 24/03/2017.
 */
@Controller
public class FileController {
    private final StorageService storageService;
    private final DocumentDao documentDao;
    private final UserDao userDao;

    @Autowired
    public FileController(StorageService storageService, DocumentDao documentDao, UserDao userDao) {
        this.storageService = storageService;
        this.documentDao = documentDao;
        this.userDao = userDao;
    }

    /***
     * Attaches new Document object to form on upload page
     * @param model
     * @return String
     */
    @GetMapping("/upload")
    public String uploadForm(Model model) {
        model.addAttribute("document", new Document());
        return "upload";
    }

    /***
     * Website to file system mapping
     * @param file
     * @param redirectAttributes
     * @return String
     */
    @PostMapping("/uploadLanding")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes,
                                   @ModelAttribute Document document) {

        /* Load logged user */
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userDao.findByUsername(userDetails.getUsername());

        /* Finalise document details and save to database */
        long id = documentDao.save(document).getId(); // gets generated id
        document = documentDao.findById(id);
        document.setAuthor(user.getUsername());
        document.setFilepath(storageService.getRootLocation().toString() +
                "/" +user.getUsername()+ "/" + id);

        /* Set branch for file */
        String branch = "/"  +user.getUsername()+ "/" + document.getId() + "/" +document.getRevisionNo()+ "/";

        userDao.save(user);
        storageService.store(file, branch);
        documentDao.save(document);


        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/uploadLanding";
    }

    @GetMapping("/uploadLanding")
    public String uploadLanding() {
        return "userDashboard";
    }

    /***
     * File system to website method
     * @param filename
     * @return ResponseEntity
     */
    @GetMapping("/files/{username}/{docId}/{revisionNo}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile (@PathVariable("username") String username,
                                               @PathVariable("docId") String docId,
                                               @PathVariable("revisionNo") String revisionNo,
                                               @PathVariable("filename") String filename)
    {
        System.out.println("Serve file firing"); // for debug
        String filepath = username + "/" + docId + "/" + revisionNo + "/" + filename;
        Resource file = storageService.loadAsResource(filepath);
        System.out.println(file.getFilename());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    /***
     * Shows all uploaded files using Java 8 lambda mapping
     * @param model
     * @return String
     * @throws IOException
     */
    @GetMapping("/download")
    public String listUploadedFiles(Model model, @ModelAttribute Document document) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userDao.findByUsername(userDetails.getUsername());
        String username = user.getUsername();
        String docId = Long.toString(document.getId());
        String revisionNo = Long.toString(document.getRevisionNo());
        model.addAttribute("files", storageService
                .loadAll()
                .map(path -> MvcUriComponentsBuilder
                        .fromMethodName(FileController.class, "serveFile", path.getFileName().toString())
                        .build().toString())
                .collect(Collectors.toList()));
        model.addAttribute("documents", documentDao.findAll());
        model.addAttribute("document", document);

        return "download";
    }

    /***
     * Handles FileNotFound exceptions
     * @param exc
     * @return ResponseEntity
     */
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}


