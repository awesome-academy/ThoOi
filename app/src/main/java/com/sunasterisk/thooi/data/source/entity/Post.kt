package com.sunasterisk.thooi.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.sunasterisk.thooi.data.source.local.database.DatabaseConstants.DEFAULT_ZONE_OFFSET
import com.sunasterisk.thooi.data.source.remote.dto.FirestorePost
import com.sunasterisk.thooi.util.toLatLng
import com.sunasterisk.thooi.util.toLocalDateTime
import org.threeten.bp.LocalDateTime

@Entity
data class Post(
    @PrimaryKey
    val id: String,
    val address: String,
    val appointment: LocalDateTime,
    val categoryRef: String,
    val customerRef: String,
    val description: String,
    val fixerId: String?,
    val appliedFixerIds: List<String>,
    val imagesRefs: List<String>,
    val location: LatLng,
    val suggestedPrice: Int,
    val status: PostStatus,
    val voucher: String?,
) {
    constructor(id: String, firestorePost: FirestorePost) : this(
        id,
        firestorePost.address,
        firestorePost.appointment.toLocalDateTime(),
        firestorePost.category,
        firestorePost.customer,
        firestorePost.description,
        firestorePost.fixer_id,
        firestorePost.fixers_id,
        firestorePost.images,
        firestorePost.location.toLatLng(),
        firestorePost.suggestedPrice,
        PostStatus.valueOf(firestorePost.status),
        firestorePost.voucher
    )

    companion object {
        val default = Post(
            "",
            "",
            LocalDateTime.ofEpochSecond(0, 0, DEFAULT_ZONE_OFFSET),
            "",
            "",
            "",
            null,
            emptyList(),
            emptyList(),
            LatLng(0.0, 0.0),
            0,
            PostStatus.NEW,
            null
        )
    }
}

enum class PostStatus {
    /**
     * No fixer has applied
     */
    NEW,

    /**
     * At least one fixer applied
     */
    OPEN,

    /**
     * Customer picked a fixer for the job
     */
    PENDING,

    /**
     * Job has finished
     */
    FINISHED
}
