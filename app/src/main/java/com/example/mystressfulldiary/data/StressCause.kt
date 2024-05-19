package com.example.mystressfulldiary.data

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "stress-causes")
data class StressCause (
    @PrimaryKey
    val name: String,
    val color: String,
);