package guides.spring.gs

import java.io.IOException
import java.net.MalformedURLException
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Stream
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile

@Service
class FileSystemStorageService(properties: StorageProperties) : StorageService {
    private val rootLocation = Paths.get(properties.location)

    override fun init() {
        try {
            Files.createDirectories(rootLocation)
        } catch (e: IOException) {
            throw StorageException("Could not initialize storage", e)
        }
    }

    override fun store(file: MultipartFile) {
        if (file.isEmpty) {
            throw StorageException("Failed to store empty file.")
        }
        val originalFilename = file.originalFilename!!
        val destinationFile = rootLocation.resolve(Paths.get(originalFilename)).normalize().toAbsolutePath()
        if (!destinationFile.parent.equals(rootLocation.toAbsolutePath())) {
            // This is a security check
            throw StorageException("Cannot store file outside current directory.")
        }
        try {
            file.inputStream.use {
                Files.copy(it, destinationFile, StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (e: IOException) {
            throw StorageException("Failed to store file.", e)
        }
    }

    override fun loadAll(): Stream<Path> {
        try {
            return Files.walk(rootLocation, 1)
                .filter { !it.equals(rootLocation) }
                .map(rootLocation::relativize)
        } catch (e: IOException) {
            throw StorageException("Failed to read stored files", e)
        }
    }

    override fun load(filename: String): Path {
        return rootLocation.resolve(filename)
    }

    private fun URI.getResource(): Resource {
        try {
            return UrlResource(this)
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("Could not read: $this", e)
        }
    }

    override fun loadAsResource(filename: String): Resource {
        val resource = load(filename).toUri().getResource()
        if (resource.exists() || resource.isReadable) {
            return resource
        } else {
            throw StorageFileNotFoundException("Could not read file: $filename")
        }
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }
}
