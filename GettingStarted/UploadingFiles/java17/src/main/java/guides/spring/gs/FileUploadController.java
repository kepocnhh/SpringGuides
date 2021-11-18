package guides.spring.gs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {
    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute(
            "files",
            storageService.loadAll().map(it ->
                MvcUriComponentsBuilder.fromMethodName(
                    FileUploadController.class,
                    "serveFile",
                    it.getFileName().toString()
                ).build().toUri().toString()
            ).collect(Collectors.toList())
        );
        return "uploadForm";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\""
        ).body(file);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public String handleFileUpload(
        @RequestParam("file") MultipartFile file,
        RedirectAttributes redirectAttributes
    ) {
        storageService.store(file);
        redirectAttributes.addFlashAttribute(
            "message",
            "You successfully uploaded " + file.getOriginalFilename() + "!"
        );
        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
