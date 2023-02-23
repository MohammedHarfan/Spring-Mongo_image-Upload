package com.example.demo

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class User(
    @Id
    val id: String?,
    val name: String?,
    val gender: String?,
    val age: String?,
    val personalDocument: PersonalDocument?,
    //These two fields are going to be stored as base64 in db
    var profilePic: String?,
    var coverPic: String?,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PersonalDocument(
    @Id
    val id: String?,
    var panNo: String?,
    //this field is going to be stored as base64 in db
    var voterId: String?,
)