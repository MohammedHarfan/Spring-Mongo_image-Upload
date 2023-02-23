package com.example.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*


@Service
class UserService(@Autowired val repo: UserRepository) {

    fun getUserById(id: String): User? {
        return repo.findById(id).orElse(null)
    }

    fun getAllUser(): MutableList<User>? {
        return repo.findAll()
    }

    fun saveUser(
        profilePic: MultipartFile?,
        coverPic: MultipartFile?,
        voterId: MultipartFile?,
        user: User?
    ): User? {
        val profileImage = encodeImageAsBase64(profilePic)
        val coverImage = encodeImageAsBase64(coverPic)
        val voterIdImage = encodeImageAsBase64(voterId)

        user?.profilePic = profileImage
        user?.coverPic = coverImage
        user?.personalDocument?.voterId = voterIdImage

        return user?.let { repo.save(it) }
    }

    fun updateSchema(
        id: String,
        profilePic: MultipartFile?,
        coverPic: MultipartFile?,
        voterId: MultipartFile?,
        user: User?
    ): User? {
        val existingUser = getUserById(id) ?: return null

        val updatedPersonalDocument= existingUser.personalDocument?.copy(
            panNo = user?.personalDocument?.panNo ?: existingUser.personalDocument.panNo,
        )

        val updatedUser = existingUser.copy(
            name = user?.name ?: existingUser.name,
            gender = user?.gender ?: existingUser.gender,
            age = user?.age ?: existingUser.age,
            personalDocument = updatedPersonalDocument,
        )

        profilePic?.let { updatedUser.profilePic = encodeImageAsBase64(it) }
        coverPic?.let { updatedUser.coverPic = encodeImageAsBase64(it) }
        voterId?.let { updatedUser.personalDocument?.voterId = encodeImageAsBase64(it) }

        return repo.save(updatedUser)
    }

    fun deleteById(id: String): Boolean {
        return repo.existsById(id) && runCatching { repo.deleteById(id) }.isSuccess
    }

    private fun encodeImageAsBase64(image: MultipartFile?): String? {
        return image?.let {
            val imageBytes = it.bytes
            Base64.getEncoder().encodeToString(imageBytes)
        }
    }

}