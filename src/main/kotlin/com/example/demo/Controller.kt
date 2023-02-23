package com.example.demo

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

const val MULTIPART_FORM_DATA = "multipart/form-data"

@RestController
@RequestMapping("/user")
class UserController(@Autowired val service: UserService) {

    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: String): ResponseEntity<out Any> {
        return service.getUserById(id)?.let {
            logger.info("Fetching record of id {} ", id)
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/all")
    fun getAllUsers(): ResponseEntity<out Any> {
        return service.getAllUser()?.let {
            logger.info("Fetching all records...")
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping(consumes = [MULTIPART_FORM_DATA])
    fun saveSchema(
        @RequestParam("profilePic") profilePic: MultipartFile?,
        @RequestParam("coverPic") coverPic: MultipartFile?,
        @RequestParam("voterId") voterId: MultipartFile?,
        @RequestPart("data") user: User
    ): ResponseEntity<out Any> {
        service.saveUser(
            profilePic,
            coverPic,
            voterId,
            user)?.let {
            logger.info("Saving record to users...")
            return ResponseEntity.ok("Record saved successfully")
        }
        logger.info("Record not saved to users")
        return ResponseEntity.notFound().build()
    }

    @PutMapping("/{id}")
    fun updateSchema(@PathVariable("id") id: String,
                     @RequestParam("profilePic") profilePic: MultipartFile?,
                     @RequestParam("coverPic") coverPic: MultipartFile?,
                     @RequestParam("voterId") voterId: MultipartFile?,
                     @RequestPart("data") user: User): ResponseEntity<out Any> {
        service.updateSchema(
            id,
            profilePic,
            coverPic,
            voterId,
            user)?.let {
            logger.info("Updated record of id {} to users...", id)
            return ResponseEntity.ok("Record saved successfully")
        }
        logger.info("Record not updated to users")
        return ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteSchema(@PathVariable("id") id: String): ResponseEntity<out Any> =
        service.deleteById(id).run {
            if (this) ResponseEntity.ok("Record with ID-$id deleted successfully") else ResponseEntity.notFound().build()
        }

    companion object {
        private val logger = LoggerFactory.getLogger(UserController::class.toString())
    }
}