package guides.spring.gs

import kotlin.streams.asSequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class FileUploadController @Autowired constructor(private val service: StorageService) {
    @RequestMapping(method = [RequestMethod.GET], value = ["/"])
    fun listUploadedFiles(model: Model): String {
        model.addAttribute(
            "files",
            service.loadAll().asSequence().map {
                MvcUriComponentsBuilder.fromMethodName(
                    FileUploadController::class.java,
                    "serveFile",
                    it.fileName.toString()
                ).build().toUri().toString()
            }.toList()
        )
        return "uploadForm"
    }

    @RequestMapping(method = [RequestMethod.GET], value = ["/files/{filename:.+}"])
    @ResponseBody
    fun serveFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val file = service.loadAsResource(filename)
        return ResponseEntity.ok().header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.filename + "\""
        ).body(file)
    }

    @RequestMapping(method = [RequestMethod.POST], value = ["/"])
    fun handleFileUpload(
        @RequestParam("file") file: MultipartFile,
        redirectAttributes: RedirectAttributes
    ): String {
        service.store(file)
        redirectAttributes.addFlashAttribute(
            "message",
            "You successfully uploaded " + file.originalFilename + "!"
        )
        return "redirect:/"
    }

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleStorageFileNotFound(ignored: StorageFileNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.notFound().build()
    }
}
