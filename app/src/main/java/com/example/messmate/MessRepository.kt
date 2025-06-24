package com.example.messmate

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class MessRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    // Menu Operations
    suspend fun getMenuItems(date: String): Result<List<MenuItem>> =
        try {
            val snapshot = firestore.collection("menu_items")
                .whereEqualTo("date", date)
                .get()
                .await()

            val menuItems = snapshot.documents.mapNotNull { doc ->
                doc.toObject(MenuItem::class.java)?.copy(id = doc.id)
            }
            Result.Success(menuItems)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun addMenuItem(menuItem: MenuItem): Result<Boolean> =
        try {
            firestore.collection("menu_items")
                .add(menuItem)
                .await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    // Attendance Operations
    suspend fun markAttendance(attendance: Attendance): Result<Boolean> =
        try {
            val attendanceId = "${attendance.userId}_${attendance.date}_${attendance.mealType}"
            firestore.collection("attendance")
                .document(attendanceId)
                .set(attendance)
                .await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun getAttendance(userId: String, date: String): Result<List<Attendance>> =
        try {
            val snapshot = firestore.collection("attendance")
                .whereEqualTo("userId", userId)
                .whereEqualTo("date", date)
                .get()
                .await()

            val attendanceList = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Attendance::class.java)?.copy(id = doc.id)
            }
            Result.Success(attendanceList)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun updateAttendance(attendanceId: String, isPresent: Boolean): Result<Boolean> =
        try {
            firestore.collection("attendance")
                .document(attendanceId)
                .update("isPresent", isPresent, "timestamp", System.currentTimeMillis())
                .await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    // Feedback Operations
    suspend fun submitFeedback(feedback: Feedback): Result<Boolean> =
        try {
            firestore.collection("feedback")
                .add(feedback)
                .await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun getUserFeedback(userId: String): Result<List<Feedback>> =
        try {
            val snapshot = firestore.collection("feedback")
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val feedbackList = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Feedback::class.java)?.copy(id = doc.id)
            }
            Result.Success(feedbackList)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun getAllFeedback(): Result<List<Feedback>> =
        try {
            val snapshot = firestore.collection("feedback")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val feedbackList = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Feedback::class.java)?.copy(id = doc.id)
            }
            Result.Success(feedbackList)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun updateFeedbackStatus(feedbackId: String, status: String): Result<Boolean> =
        try {
            firestore.collection("feedback")
                .document(feedbackId)
                .update("status", status)
                .await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    // Discussion Operations
    suspend fun createDiscussion(discussion: Discussion): Result<Boolean> =
        try {
            firestore.collection("discussions")
                .add(discussion)
                .await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun getDiscussions(category: String? = null): Result<List<Discussion>> =
        try {
            var query = firestore.collection("discussions")
                .orderBy("timestamp", Query.Direction.DESCENDING)

            if (category != null && category != "all") {
                query = query.whereEqualTo("category", category)
            }

            val snapshot = query.get().await()

            val discussions = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Discussion::class.java)?.copy(id = doc.id)
            }
            Result.Success(discussions)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun addReplyToDiscussion(discussionId: String, reply: DiscussionReply): Result<Boolean> =
        try {
            val discussionRef = firestore.collection("discussions").document(discussionId)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(discussionRef)
                val discussion = snapshot.toObject(Discussion::class.java)

                if (discussion != null) {
                    val updatedReplies = discussion.replies + reply
                    transaction.update(discussionRef, "replies", updatedReplies)
                }
            }.await()

            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun likeDiscussion(discussionId: String): Result<Boolean> =
        try {
            val discussionRef = firestore.collection("discussions").document(discussionId)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(discussionRef)
                val discussion = snapshot.toObject(Discussion::class.java)

                if (discussion != null) {
                    transaction.update(discussionRef, "likes", discussion.likes + 1)
                }
            }.await()

            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    // Utility function to get current user
    fun getCurrentUser() = auth.currentUser

    // Utility function to get current user email
    fun getCurrentUserEmail() = auth.currentUser?.email ?: ""
}